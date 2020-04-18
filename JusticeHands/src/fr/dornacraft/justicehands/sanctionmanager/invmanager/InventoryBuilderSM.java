package fr.dornacraft.justicehands.sanctionmanager.invmanager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dornacraft.devtoolslib.smartinvs.InventoryManager;
import fr.dornacraft.devtoolslib.smartinvs.SmartInventory;
import fr.dornacraft.devtoolslib.smartinvs.SmartInventory.Builder;
import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.Main;
import fr.dornacraft.justicehands.sanctionmanager.objects.Categorie;

//LE BON!!!!!!!!!!!!!!!!!!//
public class InventoryBuilderSM {
	
	// Ouverture de l'inventaire des catégories
	public static void openMainMenu(Player moderator, UUID targetUUID, FileConfiguration config) {
		// On récupère le joueur que veut modérer le modérateur.

		String mainMenuPath = "justicehands.sanctionmanager.MainMenu";
		int menuLines = config.getInt(mainMenuPath + ".lines");
		int targetHeadLine = config.getInt(mainMenuPath + ".playerhead-line");
		int targetHeadColum = config.getInt(mainMenuPath + ".playerhead-colum");
		System.out.println("line: "+ targetHeadLine);
		System.out.println("colum: "+ targetHeadColum);

		InventoryManager inventoryManager = new InventoryManager(JavaPlugin.getPlugin(Main.class));
		inventoryManager.init();

		Builder builder = SmartInventory.builder();
		builder.title("§7[§9" + Bukkit.getPlayer(targetUUID).getName() + "§7]" + "§7> §cMenu principal");
		builder.provider(new MainInventorySM(targetHeadLine, targetHeadColum));
		builder.size(menuLines, 9);
		builder.id(targetUUID.toString());
		builder.type(InventoryType.CHEST);
		builder.manager(inventoryManager);
		SmartInventory inventory = builder.build();
		inventory.open(moderator);
	}

	// Ouverture de l'inventaire des sanction de la catégorie voulue
	public static void openCategoryMenu(Categorie categorie, Player moderator, Player target) {
		InventoryManager inventoryManager = new InventoryManager(JavaPlugin.getPlugin(Main.class));
		inventoryManager.init();

		Builder builder = SmartInventory.builder();
		builder.title(GeneralUtils.getPrefix("SM") + "§7> §c" + categorie.getName());
		builder.provider(new CategoryInventorySM(categorie));
		builder.size(6, 9);
		builder.id(target.getUniqueId().toString());
		builder.type(InventoryType.CHEST);
		builder.manager(inventoryManager);
		SmartInventory inventory = builder.build();
		inventory.open(moderator);
	}
}
