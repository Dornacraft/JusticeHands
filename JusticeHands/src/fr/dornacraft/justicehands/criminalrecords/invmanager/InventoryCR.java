package fr.dornacraft.justicehands.criminalrecords.invmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
import fr.dornacraft.justicehands.criminalrecords.objects.CJSanction;

public class InventoryCR implements InventoryProvider {

	private List<CJSanction> playerAllSanctionList;
	private String select = "all";
	private Pagination p;
	
	// Constructeur
	public InventoryCR(List<CJSanction> playerAllSanctionList) {
		this.playerAllSanctionList = playerAllSanctionList;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		SmartInventory inventory = contents.inventory();
		
		// Header (séparation)
		contents.fillRow(1, ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1)));
		// Suppression de la ligne du bas
		contents.fillRow(5, ClickableItem.empty(new ItemStack(Material.AIR)));
		// Tête du joueur
		contents.set(0, 0, ClickableItem.empty(GeneralUtils.getTargetHead(Bukkit.getPlayer(UUID.fromString(inventory.getId())))));
				
		// Calcul le nombre de bannissement
		int nbrBans = 0; int nbrKicks = 0; int nbrMutes = 0; int nbrTotal = 0;
		int nbrBansCanceled = 0; int nbrKicksCanceled = 0; int nbrMutesCanceled = 0; int nbrTotalCanceled = 0;
		for (CJSanction sanction : playerAllSanctionList) {
			if (!sanction.getState().equals("delete")) {
				if (sanction.getInitialType().contains("ban")) {
					nbrBans++;
					if (sanction.getState().equals("cancel")) {
						nbrBansCanceled++;
					}
				}
				if (sanction.getInitialType().equals("mute")) {
					nbrMutes++;
					if (sanction.getState().equals("cancel")) {
						nbrMutesCanceled++;
					}
				}
				if (sanction.getInitialType().equals("kick")) {
					nbrKicks++;
					if (sanction.getState().equals("cancel")) {
						nbrKicksCanceled++;
					}
				}
				
				nbrTotal++;
				if (sanction.getState().equals("cancel")) {
					nbrTotalCanceled++;
				}
			}
		}
		
		int totalVisual = nbrTotal-nbrTotalCanceled;
		int bansVisual = nbrBans-nbrBansCanceled;
		int mutesVisual = nbrMutes-nbrMutesCanceled;
		int kicksVisual = nbrKicks-nbrKicksCanceled;
		
		// Si le nombre dépasse 64, il faut faire en sorte que l'on puisse pas mettre une quantité
		// supérieur à 64
		if (bansVisual>64) bansVisual=64;
		if (mutesVisual>64) mutesVisual=64;
		if (kicksVisual>64) kicksVisual=64;
		if (totalVisual>64) totalVisual=64;
		
		if (bansVisual<=0 && nbrBansCanceled > 0) bansVisual=1;
		if (mutesVisual<=0 && nbrMutesCanceled > 0) mutesVisual=1;
		if (kicksVisual<=0 && nbrKicksCanceled > 0) kicksVisual=1;
		if (totalVisual<=0 && nbrTotalCanceled > 0) totalVisual=1;
		
		// Statistiques du casier judiciaire du joueur
		contents.set(0, 2, ClickableItem.of(SanctionStatistics.getPlayerStatistics(playerAllSanctionList,
			Bukkit.getPlayer(UUID.fromString(inventory.getId()))), e -> {
				if (e.isRightClick()) {
					Player whoClicked = (Player) e.getWhoClicked();
					whoClicked.sendMessage("------------------------------------------");
					whoClicked.sendMessage(e.getCurrentItem().getItemMeta().getDisplayName());
					List<String> infos = e.getCurrentItem().getItemMeta().getLore();
					for (int i = 1; i < 4; i++)
						infos.remove(infos.size() - 1);
					for (String info : infos)
						whoClicked.sendMessage(" " + info);
					whoClicked.sendMessage("------------------------------------------");
				}
			}));
		
		// Création d'une pagination
		p = contents.pagination();
		
		// Tri des sanctions
		List<CJSanction> selectedSanctions = new ArrayList<CJSanction>();
		if (playerAllSanctionList.size() > 0) {
			for (CJSanction sanction : playerAllSanctionList) {
				if (!sanction.getState().equals("delete")) {
					if (select.equals("all")) {
						selectedSanctions.add(sanction);
					} else if (select.equals("mute")) {
						if (sanction.getInitialType().equals("mute")) {
							selectedSanctions.add(sanction);
						}
					} else if (select.equals("kick")) {
						if (sanction.getInitialType().equals("kick")) {
							selectedSanctions.add(sanction);
						}
					} else if (select.equals("ban")) {
						if (sanction.getInitialType().contains("ban")) {
							selectedSanctions.add(sanction);
						}
					}
				}
			}
		}

		// Création de la page si pas vide
		if (selectedSanctions.size() > 0) {
			ClickableItem[] sanctions = new ClickableItem[selectedSanctions.size()];
			for (int i = 0; i < sanctions.length; i++)
				sanctions[i] = ClickableItem.empty(getSanctionItem(selectedSanctions.get(i)));
			p.setItems(sanctions);
			p.setItemsPerPage(27);
			p.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 2, 0));
			if (!p.isFirst() && selectedSanctions.size() > 27) {
				contents.set(5, 0, ClickableItem.of(GeneralUtils.pagePrécedente(),
						e -> inventory.open(player, p.previous().getPage())));
			}
			if (!p.isLast() && selectedSanctions.size() > 27) {
				contents.set(5, 8,ClickableItem.of(GeneralUtils.pageSuivante(),
						e -> inventory.open(player, p.next().getPage())));
			}
		} else {
			if (!select.equals("all")) {
				contents.set(3, 4, ClickableItem.empty(GeneralUtils.emptyTypeCR()));
			} else {
				contents.set(3, 4, ClickableItem.empty(GeneralUtils.emptyCR()));
			}
		}
		
		// Compteur de sanctions
		ItemStack cptTotal = new ItemStack(Material.BLACK_BANNER, totalVisual); // noire
		ItemMeta metaTotal = cptTotal.getItemMeta();
		metaTotal.setDisplayName("§8Nombre total de sanctions du joueur: §7" + nbrTotal);
		if (nbrTotalCanceled < 2) {
			metaTotal.setLore(Arrays.asList("§8 dont §7" + nbrTotalCanceled + " annulée"));
		} else {
			metaTotal.setLore(Arrays.asList("§8 dont §7" + nbrTotalCanceled + " annulées"));
		}
		cptTotal.setItemMeta(metaTotal);
		contents.set(0, 8, ClickableItem.of(cptTotal, e -> {
			if (!select.equals("all")) {
				select = "all";
				p.page(0);
				init(player, contents);
			}
		}));
		
		// Compteur de bannissements
		ItemStack cptBans = new ItemStack(Material.ORANGE_BANNER, bansVisual); // jaune
		ItemMeta metaBans = cptBans.getItemMeta();
		metaBans.setDisplayName("§6Nombre total de bannissements du joueur: §e" + nbrBans);
		if (nbrBansCanceled < 2) {
			metaBans.setLore(Arrays.asList("§6 dont §e" + nbrBansCanceled + " annulé"));
		} else {
			metaBans.setLore(Arrays.asList("§6 dont §e" + nbrBansCanceled + " annulés"));
		}
		cptBans.setItemMeta(metaBans);
		contents.set(0, 7, ClickableItem.of(cptBans, e -> {
			if (!select.equals("ban")) {
				select = "ban";
				p.page(0);
				init(player, contents);
			}
		}));
		
		// Compteur de mutes
		ItemStack cptMutes = new ItemStack(Material.BLUE_BANNER, mutesVisual); // bleu
		ItemMeta metaMutes = cptMutes.getItemMeta();
		metaMutes.setDisplayName("§3Nombre total de mutes du joueur: §b" + nbrMutes);
		if (nbrMutesCanceled < 2) {
			metaMutes.setLore(Arrays.asList("§3 dont §b" + nbrMutesCanceled + " annulé"));
		} else {
			metaMutes.setLore(Arrays.asList("§3 dont §b" + nbrMutesCanceled + " annulés"));
		}
		cptMutes.setItemMeta(metaMutes);
		contents.set(0, 6, ClickableItem.of(cptMutes, e -> {
			if (!select.equals("mute")) {
				select = "mute";
				p.page(0);
				init(player, contents);
			}
		}));
		
		// Compteur de kick
		ItemStack cptKicks = new ItemStack(Material.GREEN_BANNER, kicksVisual); // vert
		ItemMeta metaKicks = cptKicks.getItemMeta();
		metaKicks.setDisplayName("§2Nombre total de kicks du joueur: §a" + nbrKicks);
		if (nbrKicksCanceled < 2) {
			metaKicks.setLore(Arrays.asList("§2 dont §a" + nbrKicksCanceled + " annulé"));
		} else {
			metaKicks.setLore(Arrays.asList("§2 dont §a" + nbrKicksCanceled + " annulés"));
		}
		cptKicks.setItemMeta(metaKicks);
		contents.set(0, 5, ClickableItem.of(cptKicks, e -> {
			if (!select.equals("kick")) {
				select = "kick";
				p.page(0);
				init(player, contents);
			}
		}));
	}

	@Override 
	public void update(Player player, InventoryContents contents) {
		// NOTHING
	}
	
	private static ItemStack getSanctionItem(CJSanction sanction) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ItemStack item = new ItemStack(Material.CLAY);
		ItemMeta meta = item.getItemMeta();
		
		List<String> lores = new ArrayList<String>();
		lores.add("§fID: §7" + ChatColor.GRAY + sanction.getID());
		lores.add("§fNom: " + ChatColor.GRAY + sanction.getName());
		lores.add("§fRaison: " + ChatColor.GRAY + sanction.getReason());
		lores.add("§fPoints: " + ChatColor.GRAY + sanction.getPoints());
		lores.add("§fJoueur sanctionné: " + ChatColor.GRAY + sanction.getPlayer().getName());
		lores.add("§fModérateur: " + ChatColor.GRAY + sanction.getModerator().getName());

		if (sanction.getTSExpireDate() != null) {
			final long currentTime = System.currentTimeMillis();
			if (currentTime >= sanction.getTSExpireDate().getTime())
				if (sanction.getState().equals("cancel"))
					lores.add(ChatColor.WHITE + "§l(Annulée) §f§mDate d'expiriation: §7§m" + sdf.format(sanction.getTSExpireDate()));
				else
					lores.add(ChatColor.GREEN + "(Terminée) Date d'expiriation: §7" + sdf.format(sanction.getTSExpireDate()));
			else
				if (sanction.getState().equals("cancel"))
					lores.add(ChatColor.WHITE + "§l(Annulée) §f§mDate d'expiriation: §7§m" + sdf.format(sanction.getTSExpireDate()));
				else
					lores.add(ChatColor.RED + "(En cours) Date d'expiriation: §7" + sdf.format(sanction.getTSExpireDate()));
					
		}

		SanctionType type = SanctionType.getType(sanction.getInitialType());
		if (!sanction.getState().equals("cancel")) {
			item.setType(type.getClayColor());
			meta.setDisplayName(type.getVisualColor() + type.getVisualName() + ChatColor.GRAY + sdf.format((Date) sanction.getTSDate()));
		} else {
			item.setType(Material.WHITE_CONCRETE);
			meta.setDisplayName("§f§l(Annulée) " + type.getVisualColor() + type.getVisualName() + ChatColor.GRAY + sdf.format((Date) sanction.getTSDate()));
		}
		
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}
}
