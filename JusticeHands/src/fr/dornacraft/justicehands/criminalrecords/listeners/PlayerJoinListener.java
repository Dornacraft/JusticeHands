package fr.dornacraft.justicehands.criminalrecords.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.dornacraft.justicehands.Main;

public class PlayerJoinListener implements Listener {
	/* 
	 * onJoin(), lorsque le joueur se connecte, on lui créer 
	 * un compte si il en a pas déja un.
	 */
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Main.getSqlPA().createAccount(player.getUniqueId());
	}
}
