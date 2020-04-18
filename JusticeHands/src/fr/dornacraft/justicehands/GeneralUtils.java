package fr.dornacraft.justicehands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

// Ici va se trouveer toutes les méthodes qui vont pouvoir être utiles n'importe où
// dans le code du plugin, comme des outils généraux.

public class GeneralUtils {
	private final static String CRPrefix = "§7[§6CriminalRecords§7] §r";
	private final static String MTPrefix = "§7[§cModeratorTools§7] §r";
	private final static String SMPrefix = "§7[§9SanctionManager§7] §r";
	private final static String KKPrefix = "§7[§aKeysKeeper§7] §r";
	
	// Récupère la tête du joueur entrain de se faire modérer:
	@SuppressWarnings("deprecation")
	public static ItemStack getTargetHead(Player target) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		ItemStack targetHead = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta headMeta = (SkullMeta) targetHead.getItemMeta();
		headMeta.setOwner(target.getName());
		headMeta.setDisplayName("§cInformations sur le joueur:");

		List<String> lores = new ArrayList<String>();
		lores.add("§7Pseudo: §f" + target.getName());
		lores.add("§7Grade: §fà remplir");
		lores.add("§7Points de sanctions: §6" + Main.getSqlPA().getPoints(target.getUniqueId()));
		lores.add("§7AchievementPoints: §fà remplir");
		lores.add("§7Connecté: " + getState(target.isOnline()));
		Date firstPlayed = new Date(target.getFirstPlayed());
		lores.add("§7Première connexion: §f" + sdf.format(firstPlayed));
		if (!target.isOnline()) {
			Date lastPlayed = new Date(target.getLastPlayed());
			lores.add("§7Dernière déconnexion : §f" + sdf.format(lastPlayed));
		}
		lores.add("§7Banni: §f" + getState(target.isBanned()));
		headMeta.setLore(lores);
		targetHead.setItemMeta(headMeta);

		return targetHead;
	}
	
	// Retourne l'objet de la page précédente
	public static ItemStack pagePrécedente() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7Page précédente");
		meta.setLore(Arrays.asList(" ","   §cCLIC DROIT","§f<<Page précendente"));
		item.setItemMeta(meta);
		return item;
	}

	// Retourne l'objet de la page suivante
	public static ItemStack pageSuivante() {
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7Page suivante");
		meta.setLore(Arrays.asList(" ","   §cCLIC DROIT","      §f>>Page suivante"));
		item.setItemMeta(meta);
		return item;
	}
	
	// Retourne l'objet indiquant que le casier judiciaire du joueur est vide (all sanctions)
	public static ItemStack emptyCR() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cCasier judiciaire vide");
		meta.setLore(Arrays.asList(" ","§7Le casier judiciaire du joueur","§7est vide, aucune sanction."));
		item.setItemMeta(meta);
		return item;
	}
	
	// Retourne l'objet indiquant que le joueur n'a jamais eu un certain type de sanction
	public static ItemStack emptyTypeCR() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cAucune sanction renseignée");
		meta.setLore(Arrays.asList(" ","§7Le joueur n'a jamais","§7eu ce type de sanction."));
		item.setItemMeta(meta);
		return item;
	}
	
	// Retourne l'objet indiquant qu'une catégorie ne contient aucune sanction
	public static ItemStack emptySM() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cCatégorie vide");
		meta.setLore(Arrays.asList(" ","§7La catégorie ne contient","§7aucune sanction."));
		item.setItemMeta(meta);
		return item;
	}
	
	// Permet de transformer des true et false en oui et non (respectivement)
	private static String getState(boolean b) {
		String state;
		if (b)
			state = "§aOui";
		else
			state = "§cNon";
		return state;
	}
	
	// Récupère le préfix de chaque partie du plugin via les initiales
	public static String getPrefix(String prefix) {
		if (prefix.equalsIgnoreCase("cr")) {
			return CRPrefix;
		}
		if (prefix.equalsIgnoreCase("kk")) {
			return KKPrefix;
		}
		if (prefix.equalsIgnoreCase("mt")) {
			return MTPrefix;
		}
		if (prefix.equalsIgnoreCase("sm")) {
			return SMPrefix;
		}
		return "[Inexistant] §r";
	}
	
	// Méthode permettant de récupèrer une différence de temps et de la formater en String 
	// 		exemple: 1 mois, 12 jours, 13 heures, 56 secondes
	public static String timeRemaining(long timeDifference) {
		final long SECOND = 1000; // number of ms in a second
		final long MINUTE = SECOND * 60; // number of ms in a minute
		final long HOUR = MINUTE * 60; // number of ms in an hour
		final long DAY = HOUR * 24; // number of ms in a day
		// final long WEEK = DAY * 7;
		final long MOUNTH = DAY * 30;

		int mounths = (int) (timeDifference / MOUNTH);
		int days = (int) ((timeDifference % MOUNTH) / DAY);
		int hours = (int) ((timeDifference % DAY) / HOUR);
		int minutes = (int) ((timeDifference % HOUR) / MINUTE);
		int seconds = (int) ((timeDifference % MINUTE) / SECOND);

		String unitMo = "", unitD = "", unitH = "", unitMi = "", unitS = "";
		String mounthsStr, daysStr, hoursStr, minutesStr, secondsStr;

		// MOIS
		if (mounths <= 0)
			mounthsStr = "";
		else {
			unitMo = "mois";
			mounthsStr = mounths + " " + unitMo + " ";
		}

		// JOUR
		if (days <= 0)
			daysStr = "";
		else if (days == 1) {
			unitD = "jour";
			daysStr = days + " " + unitD + " ";
		} else {
			unitD = "jours";
			daysStr = days + " " + unitD + " ";
		}

		// HEURES
		if (hours <= 0)
			hoursStr = "";
		else if (hours == 1) {
			unitH = "heure";
			hoursStr = hours + " " + unitH + " ";
		} else {
			unitH = "heures";
			hoursStr = hours + " " + unitH + " ";
		}

		// MINUTES
		if (minutes <= 0)
			minutesStr = "";
		else if (minutes == 1) {
			unitMi = "minute";
			minutesStr = minutes + " " + unitMi + " ";
		} else {
			unitMi = "minutes";
			minutesStr = minutes + " " + unitMi + " ";
		}

		// SECONDES
		if (seconds <= 0)
			secondsStr = "";
		else if (seconds == 1) {
			unitS = "seconde";
			secondsStr = seconds + " " + unitS;
		} else {
			unitS = "secondes";
			secondsStr = seconds + " " + unitS;
		}
		
		final String timeRemaining = (mounthsStr + daysStr + hoursStr + minutesStr + secondsStr);
		return timeRemaining;
	}
}
