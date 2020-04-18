package fr.dornacraft.justicehands.sanctionmanager.invmanager;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.dornacraft.devtoolslib.smartinvs.ClickableItem;
import fr.dornacraft.devtoolslib.smartinvs.SmartInventory;
import fr.dornacraft.devtoolslib.smartinvs.content.InventoryContents;
import fr.dornacraft.devtoolslib.smartinvs.content.InventoryProvider;
import fr.dornacraft.devtoolslib.smartinvs.content.Pagination;
import fr.dornacraft.devtoolslib.smartinvs.content.SlotIterator;
import fr.dornacraft.justicehands.GeneralUtils;
import fr.dornacraft.justicehands.SanctionType;
import fr.dornacraft.justicehands.sanctionmanager.CategoriesList;
import fr.dornacraft.justicehands.sanctionmanager.SanctionsAlgo;
import fr.dornacraft.justicehands.sanctionmanager.objects.Categorie;
import fr.dornacraft.justicehands.sanctionmanager.objects.Sanction;

public class CategoryInventorySM implements InventoryProvider {
	private Categorie currentCategorie;
	
	public CategoryInventorySM(Categorie currentCategorie) {
		this.currentCategorie = currentCategorie;
	}

	@Override
	public void init(Player moderator, InventoryContents contents) {
		SmartInventory inventory = contents.inventory();
		contents.set(0, 0, ClickableItem.empty(GeneralUtils.getTargetHead(Bukkit.getPlayer(UUID.fromString(inventory.getId())))));
		contents.fillRow(1, ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1)));
		for (Categorie categorie : CategoriesList.getCategoriesList()) {

			ItemStack itemCat = getCategoryItem(categorie);
			if (currentCategorie.getName().equals(categorie.getName())) {
				ItemMeta meta = itemCat.getItemMeta();
				meta.addEnchant(Enchantment.DURABILITY, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				itemCat.setItemMeta(meta);
			}
			contents.set(0, 9-CategoriesList.getCategoriesList().size()+CategoriesList.getCategoriesList().indexOf(categorie), ClickableItem.of(itemCat, e -> {
				if (e.isLeftClick()) {
					this.currentCategorie = categorie;
					init(moderator, contents);
					InventoryBuilderSM.openCategoryMenu(categorie, moderator, Bukkit.getPlayer(UUID.fromString(inventory.getId())));
				}
			}));
		}
		
		// Création de la page si pas vide
		if (currentCategorie.getSanctionsList().size() > 0) {
			ClickableItem[] sanctions = new ClickableItem[currentCategorie.getSanctionsList().size()];
			Pagination p = contents.pagination();
			
			for (int i = 0; i < sanctions.length; i++) {
				Sanction sanction = currentCategorie.getSanctionsList().get(i);
				//Création d'un ClickableItem à partir de la méthode getSantionItem...
				sanctions[i] = ClickableItem.of(getSanctionItem(sanction), e -> {
					if (e.isLeftClick()) {
						if (moderator.hasPermission("justicehands.sm." + sanction.getInitialType().toLowerCase())) {
							SanctionsAlgo.generateSanction(sanction, moderator, Bukkit.getPlayer(UUID.fromString(inventory.getId())));
						} else {
							SanctionType type = SanctionType.getType(sanction.getInitialType());
							moderator.sendMessage(GeneralUtils.getPrefix("SM") + "§cTu n'as pas la permission d'attribuer un " + type.getVisualColor() + type.getVisualName());
						}
						inventory.close(moderator);
					}
				});
			}
			p.setItems(sanctions);
			p.setItemsPerPage(27);
			p.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 2, 0));
			//Je me retrouve ici avec un inventaire vide et je comprend pas pourquoi mdr..
			
			if (!p.isFirst()) {
				contents.set(5, 0, ClickableItem.of(GeneralUtils.pagePrécedente(), e -> inventory.open(moderator, p.previous().getPage())));
			}
			if (!p.isLast() && sanctions.length > 27) {
				contents.set(5, 8, ClickableItem.of(GeneralUtils.pageSuivante(), e -> inventory.open(moderator, p.next().getPage())));
			}
		} else {
			contents.set(3, 4, ClickableItem.empty(GeneralUtils.emptySM()));
		}
	}

	@Override
	public void update(Player moderator, InventoryContents contents) {
		// NOTHING
	}

	// Récupère l'item représentatif d'une catégorie:
	private static ItemStack getCategoryItem(Categorie categorie) {
		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4Catégorie: §c" + categorie.getName());
		meta.setLore(Arrays.asList("", categorie.getDesc()));
		item.setItemMeta(meta);
		return item;
	}
	
	private static ItemStack getSanctionItem(Sanction sanction) {
		ItemStack item = new ItemStack(Material.GLOBE_BANNER_PATTERN, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cSanction: §7" + sanction.getName());
		meta.setLore(Arrays.asList("", "§7Raison: §8" + sanction.getReason(), "§7Points de sanction: §7" + sanction.getPoints(), ""));
		item.setItemMeta(meta);
		return item;
	}

}
