package fr.dornacraft.justicehands.keyskeeper;

import java.util.List;

import org.bukkit.entity.Player;

import fr.dornacraft.justicehands.Main;

public class KeysKeeperBot {

	public static Long getPlayerMuteDate(Player player) {
		List<Long> muteDateTSList = Main.getSqlKK().getPlayerMutesEDLong(player);

		Long unmuteDate = muteDateTSList
				.stream()
				.max(Long::compare).get();
		
		return unmuteDate;
	}	
}
