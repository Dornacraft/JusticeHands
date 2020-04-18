package fr.dornacraft.justicehands;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum SanctionType {
	KICK("kick", "Kick ", ChatColor.GREEN, Material.GREEN_CONCRETE),
	MUTE("mute", "Mute ", ChatColor.BLUE, Material.BLUE_CONCRETE),
	RISINGBAN("risingban", "Bannissement (transformé) ", ChatColor.YELLOW, Material.YELLOW_CONCRETE),
	BAN("ban", "Bannissement ", ChatColor.GOLD, Material.ORANGE_CONCRETE),
	BANDEF("bandef", "Banissement définitif ", ChatColor.RED, Material.RED_CONCRETE);
	
	private String configName;
	private String visualName;
	private ChatColor visualColor;
	private Material clayColor;
	
	private SanctionType(final String configName, final String visualName, final ChatColor visualColor, final Material clayColor) {
		this.configName = configName;
		this.visualName = visualName;
		this.visualColor = visualColor;
		this.clayColor = clayColor;
	}
	
	public static SanctionType getType(String configName) {
		SanctionType[] typeList = SanctionType.values();
		for (SanctionType type : typeList) {
			if (type.getConfigName().equalsIgnoreCase(configName)) {
				return type;
			}
		}
		return null;
	}
	
	public String getConfigName() {
		return this.configName;
	}
	public String getVisualName() {
		return this.visualName;
	}
	public ChatColor getVisualColor() {
		return this.visualColor;
	}
	public Material getClayColor() {
		return this.clayColor;
	}
}


