package fr.dornacraft.justicehands;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum SanctionType {
	// Liste de tous les types de sanction possible
	KICK("kick", "Kick ", ChatColor.GREEN, Material.GREEN_CONCRETE),
	MUTE("mute", "Mute ", ChatColor.BLUE, Material.BLUE_CONCRETE),
	RISINGBAN("risingban", "Bannissement (transformé) ", ChatColor.YELLOW, Material.YELLOW_CONCRETE),
	BAN("ban", "Bannissement ", ChatColor.GOLD, Material.ORANGE_CONCRETE),
	BANDEF("bandef", "Banissement définitif ", ChatColor.RED, Material.RED_CONCRETE);
	
	//Atrtibuts
	private String configName;
	private String visualName;
	private ChatColor visualColor;
	private Material clayColor;
	
	// Constructeur
	private SanctionType(final String configName, final String visualName, final ChatColor visualColor, final Material clayColor) {
		this.configName = configName;
		this.visualName = visualName;
		this.visualColor = visualColor;
		this.clayColor = clayColor;
	}
	
	// On récupère le type de sanction via le nom de la config
	public static SanctionType getType(String configName) {
		SanctionType[] typeList = SanctionType.values();
		for (SanctionType type : typeList) {
			if (type.getConfigName().equalsIgnoreCase(configName)) {
				return type;
			}
		}
		return null;
	}
	
	// Récupère le nom côté config
	public String getConfigName() {
		return this.configName;
	}
	// Récupère le nom côté visuel (visible par tous)
	public String getVisualName() {
		return this.visualName;
	}
	// Récupère la couleur en lien avec le nom visuel
	public ChatColor getVisualColor() {
		return this.visualColor;
	}
	// Récupère le bloc représentant la sanction
	public Material getClayColor() {
		return this.clayColor;
	}
}


