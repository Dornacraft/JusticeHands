package fr.dornacraft.justicehands.criminalrecords.invmanager;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.devtoolslib.smartinvs.InventoryManager;
import fr.dornacraft.devtoolslib.smartinvs.SmartInventory;
import fr.dornacraft.devtoolslib.smartinvs.SmartInventory.Builder;
import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.Main;
import fr.dornacraft.justicehands.criminalrecords.objects.CJSanction;

public class InventoryBuilderCR {

	// Ouverture de l'inventaire principale des sanctions
	public static void openMainMenu(Player player, UUID targetUUID) {
		// On récupère le joueur que veut modérer le modérateur.
		
		Player target = Bukkit.getPlayer(targetUUID);
		
		// On récupère la liste des sanctions du joueur
		List<CJSanction> playerAllSanctionList = Main.getSqlSM().getPlayerSanctions(player);
		
		InventoryManager inventoryManger = new InventoryManager(JavaPlugin.getPlugin(Main.class));
		inventoryManger.init();
		
		Builder builder = SmartInventory.builder();
		builder.title(GeneralUtils.getPrefix("CR") + "§c" + target.getName());
		builder.provider(new InventoryCR(playerAllSanctionList));
		builder.id(targetUUID.toString());
		builder.size(6, 9);
		builder.type(InventoryType.CHEST);
		builder.manager(inventoryManger);
		SmartInventory inventory = builder.build();
		inventory.open(player);
	}
}