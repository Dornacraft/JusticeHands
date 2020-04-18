package fr.dornacraft.justicehands.keyskeeper.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.keyskeeper.KeysKeeperBot;

public class AsyncChatListener implements Listener {
	
	@EventHandler
	public void AsyncChatEvent(AsyncPlayerChatEvent e) {
		Long unmuteDate = KeysKeeperBot.getPlayerMuteDate(e.getPlayer());
		
		// Vérification si le joueur peut parler
		if (unmuteDate<System.currentTimeMillis()) {
			e.getPlayer().sendMessage(GeneralUtils.getPrefix("kk") + "§cTu ne peux pas parler, tu es réduit au silence pendant encore §b" + GeneralUtils.timeRemaining(unmuteDate-System.currentTimeMillis()) + "§c.");
			e.setCancelled(true);
		} else {
			e.setCancelled(false);
		}
	}
}
