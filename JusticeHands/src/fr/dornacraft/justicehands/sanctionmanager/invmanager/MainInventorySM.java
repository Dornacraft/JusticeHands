package fr.dornacraft.justicehands.sanctionmanager.invmanager;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.dornacraft.devtoolslib.smartinvs.ClickableItem;
import fr.dornacraft.devtoolslib.smartinvs.SmartInventory;
import fr.dornacraft.devtoolslib.smartinvs.content.InventoryContents;
import fr.dornacraft.devtoolslib.smartinvs.content.InventoryProvider;
import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.sanctionmanager.CategoriesList;
import fr.dornacraft.justicehands.sanctionmanager.objects.Categorie;

public class MainInventorySM implements InventoryProvider {

	private int targetHeadLine;
	private int targetHeadColum;

	// Constructeur
	public MainInventorySM(int targetHeadLine, int targetHeadColum) {
		this.targetHeadLine = targetHeadLine;
		this.targetHeadColum = targetHeadColum;
	}

	@Override
	public void init(Player moderator, InventoryContents contents) {
		SmartInventory inventory = contents.inventory();
		contents.set(targetHeadLine, targetHeadColum, ClickableItem.empty(GeneralUtils.getTargetHead(Bukkit.getPlayer(UUID.fromString(inventory.getId())))));
		for (Categorie categorie : CategoriesList.getCategoriesList()) {
			contents.set(categorie.getLineSlot(), categorie.getColumSlot() ,ClickableItem.of(getCategoryItem(categorie), e -> {
				if (e.isLeftClick() || e.isRightClick() || e.isShiftClick()) {
					InventoryBuilderSM.openCategoryMenu(categorie, moderator, Bukkit.getPlayer(UUID.fromString(inventory.getId())));
				}
			}));
			contents.set(targetHeadLine, targetHeadColum, ClickableItem.empty(GeneralUtils.getTargetHead(Bukkit.getPlayer(UUID.fromString(inventory.getId())))));
		}
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// NOTHING TO DO
	}

	// Récupèration de l'item représentatif d'une catégorie
	private static ItemStack getCategoryItem(Categorie categorie) {
		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4Catégorie: §c" + categorie.getName());
		meta.setLore(Arrays.asList("", categorie.getDesc()));
		item.setItemMeta(meta);
		return item;
	}
}
