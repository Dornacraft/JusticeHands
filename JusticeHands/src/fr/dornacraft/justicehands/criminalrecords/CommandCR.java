package fr.dornacraft.justicehands.criminalrecords;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.Main;
import fr.dornacraft.justicehands.criminalrecords.invmanager.InventoryBuilderCR;

public class CommandCR implements CommandExecutor {

	//TODO A changer en utilisant le plugin
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("justicehands.cr")) {
			if (sender instanceof Player) {
				Player moderator = (Player) sender;
				if (args.length == 0) {
					moderator.sendMessage(GeneralUtils.getPrefix("CR") + "§cSyntaxe incomplète, il manque un argument.");
					moderator.sendMessage("     §7/" + cmd.getName().toString().toLowerCase() + " §7<joueur>");
				} else if (args.length == 1 && args[0] != null) {
					if (Main.getSqlPA().hasAccount(Bukkit.getPlayer(args[0]).getUniqueId())) {
						UUID targetUUID = Main.getSqlPA().getAccount(Bukkit.getPlayer(args[0]).getUniqueId());
						
						InventoryBuilderCR.openMainMenu(moderator, targetUUID); // Ouverture de l'inventaire SM du joueur target.
					}
					else {
						moderator.sendMessage(GeneralUtils.getPrefix("CR") + "§cCe joueur ne s'est jamais connecté sur le serveur.");
					}
				}
				return true;
			} else {
				sender.sendMessage(GeneralUtils.getPrefix("CR") + "§cTu dois être sur le serveur pour éxécuter cette commande.");
			}
		} else {
			sender.sendMessage(GeneralUtils.getPrefix("CR") + "§cTu n'as pas accès à cette commande.");
		}
		return false;
	}
}
