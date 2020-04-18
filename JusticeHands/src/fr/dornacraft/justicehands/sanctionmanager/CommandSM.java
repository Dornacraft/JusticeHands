package fr.dornacraft.justicehands.sanctionmanager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.Main;
import fr.dornacraft.justicehands.sanctionmanager.invmanager.InventoryBuilderSM;

public class CommandSM implements CommandExecutor {

	
	/*
	 * TODO, a remplacer en utilisant le plugin permettant de mieux optimiser les commandes
	 */
	private FileConfiguration config;

	public CommandSM(FileConfiguration config) {
		this.config = config;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("justicehands.sm.use")) {
			if (sender instanceof Player) {
				Player moderator = (Player) sender;
				if (args.length == 0) {
					moderator.sendMessage(GeneralUtils.getPrefix("SM") + "§cSyntaxe incomplète, il manque un argument.");
					moderator.sendMessage("     §7/" + cmd.getName().toString().toLowerCase() + " §7<joueur>");
				} else if (args.length == 1 && args[0] != null) {
					if (Main.getSqlPA().hasAccount(Bukkit.getPlayer(args[0]).getUniqueId())) {
						UUID targetUUID = Main.getSqlPA().getAccount(Bukkit.getPlayer(args[0]).getUniqueId());
						
						InventoryBuilderSM.openMainMenu(moderator, targetUUID, config); // Ouverture de l'inventaire SM du joueur target.
					}
					else {
						moderator.sendMessage(GeneralUtils.getPrefix("SM") + "§cCe joueur ne s'est jamais connecté sur le serveur.");
					}
				}
				return true;
			} else {
				sender.sendMessage(GeneralUtils.getPrefix("SM") + "§cTu dois être sur le serveur pour éxécuter cette commande.");
			}
		} else {
			sender.sendMessage(GeneralUtils.getPrefix("SM") + "§cTu n'as pas accès à cette commande.");
		}
		return false;
	}
}
