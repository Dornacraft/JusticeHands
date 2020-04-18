package fr.dornacraft.justicehands.keyskeeper;

import java.util.List;

import org.bukkit.entity.Player;

import fr.dornacraft.justicehands.Main;

public class KeysKeeperBot {

/*
 * TODO A chaque fois que le joueur parle ça fait une requete dans la base de données,
 * il va falloir que je mette en place un systeme de cache qui s'actualise toutes les 5 minutes
 * Si jamais il y a une sanction de mute il va falloir que je mette à jour ce dernier
 */
	
	public static Long getPlayerMuteDate(Player player) {
		List<Long> muteDateTSList = Main.getSqlKK().getPlayerMutesEDLong(player);

		Long unmuteDate = muteDateTSList
				.stream()
				.max(Long::compare).get();
		
		return unmuteDate;
	}	
}
