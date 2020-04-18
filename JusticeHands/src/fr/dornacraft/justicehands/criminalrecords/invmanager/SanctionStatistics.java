package fr.dornacraft.justicehands.criminalrecords.invmanager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.dornacraft.justicehands.Main;
import fr.dornacraft.justicehands.SanctionType;
import fr.dornacraft.justicehands.criminalrecords.objects.CJSanction;

public class SanctionStatistics {

	public static ItemStack getPlayerStatistics(List<CJSanction> playerAllSanctionList, Player target) {
		// Lors de la requête SQL, les sanctions sont classées de la plus récente à la plus ancienne
		
		List<CJSanction> activeSanctionList = new ArrayList<CJSanction>();
		List<CJSanction> bansList = new ArrayList<CJSanction>();
		List<CJSanction> mutesList = new ArrayList<CJSanction>();
		List<CJSanction> kicksList = new ArrayList<CJSanction>();

		// Tri des sanctions
		if (playerAllSanctionList.size() > 0) {
			for (CJSanction sanction : playerAllSanctionList) {
				if (!(sanction.getState().equals("cancel") || sanction.getState().equals("delete"))) {
						// Type : ban, bandef, risingban
					if (sanction.getInitialType().contains("ban")) {
						bansList.add(sanction);
						// Type : mute
					} else if (sanction.getInitialType().equals("mute")) {
						mutesList.add(sanction);
						// Type : kick
					} else if (sanction.getInitialType().equals("kick")) {
						kicksList.add(sanction);
					}
					activeSanctionList.add(sanction);
				}
			}
		}

		// Récupération du nombre de sanction de chaque type en utilisant les listes
		double nbrSanction = activeSanctionList.size();
		double nbrBans = bansList.size();
		double nbrMutes = mutesList.size();
		double nbrKicks = kicksList.size();

		// Ajout d'un pourcentage selon les sanctions
		DecimalFormat df = new DecimalFormat("#.##");
		String prcBan; 
		if (nbrBans > 0)
			prcBan = df.format(nbrBans * 100 / nbrSanction);
		else
			prcBan = "0";
		
		String prcMute;
		if (nbrMutes > 0)
			prcMute = df.format(nbrMutes * 100 / nbrSanction);
		else
			prcMute = "0";
		
		String prcKick;
		if (nbrKicks > 0)
			prcKick = df.format(nbrKicks * 100 / nbrSanction);
		else
			prcKick = "0";

		// Format de date et heure
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Derniere sanction (Valeurs par défaut)
		String lastSanctionID = "§7§oInexistant";
		String lastSanctionDate = "§7§oInexistante";
		String lastSanctionType = "§7§oInexistant";

		if (playerAllSanctionList.size() > 0) {
			CJSanction lastSanction = playerAllSanctionList.get(0);
			lastSanctionID = lastSanction.getID();
			lastSanctionDate = sdf.format(lastSanction.getTSDate());
			lastSanctionType = (SanctionType.getType(lastSanction.getInitialType()).getVisualColor() + SanctionType.getType(lastSanction.getInitialType()).getVisualName());
		}

		// Récupération premiere et dernière sanction de chaque type (Valeurs par défaut)
		String firstBanDate = "Aucun", firstMuteDate = "Aucun", firstKickDate = "Aucun";
		String lastBanDate = "Aucun", lastMuteDate = "Aucun", lastKickDate = "Aucun";
		String firstBanID = "-", firstMuteID = "-", firstKickID = "-";
		String lastBanID = "-", lastMuteID = "-", lastKickID = "-";

		if (!bansList.isEmpty()) {
			firstBanDate = sdf.format(bansList.get(bansList.size() - 1).getTSDate());
			firstBanID = bansList.get(0).getID();

			lastBanDate = sdf.format(bansList.get(0).getTSDate());
			lastBanID = bansList.get(bansList.size() - 1).getID();
		}

		if (!mutesList.isEmpty()) {
			firstMuteDate = sdf.format(mutesList.get(mutesList.size() - 1).getTSDate());
			firstMuteID = mutesList.get(0).getID();

			lastMuteDate = sdf.format(mutesList.get(0).getTSDate());
			lastMuteID = mutesList.get(mutesList.size() - 1).getID();
		}

		if (!kicksList.isEmpty()) {
			firstKickDate = sdf.format(kicksList.get(kicksList.size() - 1).getTSDate());
			firstKickID = kicksList.get(0).getID();

			lastKickDate = sdf.format(kicksList.get(0).getTSDate());
			lastKickID = kicksList.get(kicksList.size() - 1).getID();
		}

		// Récupération des points actuel du joueur
		int playerActualPoints = Main.getSqlPA().getPoints(target.getUniqueId());

		// Total des points obtenus depuis sa première connexion sur le serveur
		int playerAllPoints = 0;
		for (CJSanction sanction : playerAllSanctionList) {
			playerAllPoints += sanction.getPoints();
		}

		// Création de l'item servant à afficher les statistiques du joueur en question
		ItemStack stats = new ItemStack(Material.KNOWLEDGE_BOOK);
		ItemMeta infos = stats.getItemMeta();
		infos.setDisplayName("§c§lStatistiques du joueur §7" + target.getName() + " §c§l:");

		List<String> lores = new ArrayList<String>();
		lores.add("§6Nombre de bannissements: §e" + (int) nbrBans + " (" + prcBan + "%)");
		lores.add("§3Nombre de mutes: §b" + (int) nbrMutes + " (" + prcMute + "%)");
		lores.add("§2Nombre de kicks: §a" + (int) nbrKicks + " (" + prcKick + "%)");
		lores.add("");
		lores.add("§fDernière sanction:");
		lores.add("§7  - ID: §8" + lastSanctionID);
		lores.add("§7  - Date: §8" + lastSanctionDate);
		lores.add("§7  - Type: " + lastSanctionType);
		lores.add("");
		lores.add("§6Premier bannissement: §7" + firstBanDate + " §8§o(" + firstBanID + ")");
		lores.add("§3Premier mute: §7" + firstMuteDate + " §8§o(" + firstMuteID + ")");
		lores.add("§2Premier kick: §7" + firstKickDate + " §8§o(" + firstKickID + ")");
		lores.add("");
		lores.add("§6Dernier bannissement: §7" + lastBanDate + " §8§o(" + lastBanID + ")");
		lores.add("§3Dernier mute: §7" + lastMuteDate + " §8§o(" + lastMuteID + ")");
		lores.add("§2Dernier kick: §7" + lastKickDate + " §8§o(" + lastKickID + ")");
		lores.add("");
		lores.add("§fNombre actuel de points: §6" + playerActualPoints);
		lores.add("§fNombre total de points obtenus: §e" + playerAllPoints);
		lores.add("");
		lores.add("§4CLIC DROIT §cpour imprimer dans le tchat.");
		lores.add("§7(!) §oRécupération possible via les logs.");
		infos.setLore(lores);
		stats.setItemMeta(infos);
		return stats;
	}
}
