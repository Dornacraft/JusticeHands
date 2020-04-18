package fr.dornacraft.justicehands.sanctionmanager;

import java.sql.Timestamp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.Main;
import fr.dornacraft.justicehands.sanctionmanager.objects.Sanction;

public class SanctionsAlgo {

	// Mute (en minutes) = points*1
	// Ban (en jours) = points/10

	public static void generateSanction(Sanction sanction, Player moderator, Player target) {
		
		//Lors de l'algorithme, les points et le types de sanction peuvent être modifiés.
		//On va donc créer une nouvelle sanction identique à celle en paramètre.
		Sanction tempSanction = (Sanction) sanction.clone();
		final int targetPoints = Main.getSqlPA().getPoints(target.getUniqueId());
		
			// Permet de dire si un joueur mérite un ban au lieu d'un mute ou d'un kick
		if (targetPoints >= 100 && !(tempSanction.getInitialType().equals("ban") || tempSanction.getInitialType().equals("bandef"))) {
			tempSanction.setInitialType("risingban");
			tempSanction.setPoints(tempSanction.getPoints()*2); //Multiplication des points par deux
			generateBanMute(tempSanction, moderator, target);
			return;
			
			// Le type de sanction est un type sans date d'expiration
		} else if (tempSanction.getInitialType().equals("kick") || tempSanction.getInitialType().equals("bandef")) {
			Main.getSqlSM().addSanction(target, moderator, tempSanction, null);
			sendAlertMsg(target, tempSanction, GeneralUtils.getPrefix("SM"), System.currentTimeMillis(), 0);
			return;
			
		} else if (tempSanction.getInitialType().equals("mute") || tempSanction.getInitialType().equals("ban")) {
			generateBanMute(tempSanction, moderator, target);
			return;
			
		} else {
			moderator.sendMessage(GeneralUtils.getPrefix("SM") + "§cUne erreur vient de se produire, l'action n'a pas aboutie. Veuillez contacter un administrateur au plus vite !" );
			moderator.sendMessage("§8§lErreur: §r§7 config.yml > §b" +  sanction.getInitialType() + " §c(Mauvais type)");
		}
	}
	
	// Algorithme permettant la génération du temps d'expiration adéquat selon le joueur
	private static void generateBanMute(Sanction tempSanction, Player moderator, Player target) {
		final int targetPoints = Main.getSqlPA().getPoints(target.getUniqueId());
		final long currentTime = System.currentTimeMillis();
		final double extraTime;
		
		if (tempSanction.getInitialType().equals("mute")) {
			// Points convertis en minutes (MUTE)
			extraTime = (tempSanction.getPoints()+targetPoints) * 60l * 1000l;
		} else {
			// Points convertis en jours (BAN)
			extraTime = ((tempSanction.getPoints()+targetPoints) / 10l) * 24l * 60l * 60l * 1000l;
		} 
		
		long expireTime = (long) (currentTime + extraTime);
		Timestamp expireDate = new Timestamp(expireTime);

		Main.getSqlSM().addSanction(target, moderator, tempSanction, expireDate);
		sendAlertMsg(target, tempSanction, GeneralUtils.getPrefix("SM"), currentTime, expireTime);
	}
	
	private static void sendAlertMsg(Player target, Sanction sanction, String SMPrefix, long currentTime, long expireTime) {
		final long timeDifference = expireTime-currentTime;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != target) {
				if (sanction.getInitialType().equals("kick")) {
					player.sendMessage(SMPrefix + "§7Le joueur §c" + target.getName() + " §7a été éjecté du serveur. (§8" + sanction.getReason() + "§7)");
				} else if (sanction.getInitialType().equals("mute")) {
					player.sendMessage(SMPrefix + "§7Le joueur §c" + target.getName() + " §7est maintenant réduit au silence pendant §c" + GeneralUtils.timeRemaining(timeDifference) + "§7. (§8" + sanction.getReason() + "§7)");
				} else if (sanction.getInitialType().equals("ban") || sanction.getInitialType().equals("risingban")) {
					player.sendMessage(SMPrefix + "§cLe joueur §4" + target.getName() + " §ca été banni du serveur pendant §4" + GeneralUtils.timeRemaining(timeDifference) + "§7. (§8" + sanction.getReason() + "§7)");
				} else if (sanction.getInitialType().equals("bandef")) {
					player.sendMessage(SMPrefix + "§cLe joueur §4" + target.getName() + " §ca été banni défénitivement du serveur. §7(§8" + sanction.getReason()+ "§7)");
				}
			} else {
				if (sanction.getInitialType().equals("kick")) {
					//Link with keyskeeper
					target.sendMessage("kick");
				} else if (sanction.getInitialType().equals("mute")) {
					target.sendMessage(SMPrefix + "§7Tu es maintenant réduit au silence pendant §c" + GeneralUtils.timeRemaining(timeDifference) + "§7. (§8" + sanction.getReason() + "§7)");
				} else if (sanction.getInitialType().equals("ban") || sanction.getInitialType().equals("risingban")) {
					//Link with keyskeeper
					target.sendMessage("ban");
				} else if (sanction.getInitialType().equals("bandef")) {
					//Link with keyskeeper
					target.sendMessage("bandef");
				}
			}
		}
	}
}
