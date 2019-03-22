package me.robnoo02.plotreviewplugin.guis;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.PlotPlayer;

import me.robnoo02.plotreviewplugin.files.DataFile;
import me.robnoo02.plotreviewplugin.files.UserDataFile.UserDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;
import me.robnoo02.plotreviewplugin.guis.GuiUtil.Gui;
import me.robnoo02.plotreviewplugin.guis.GuiUtil.GuiItem;
import me.robnoo02.plotreviewplugin.review.ReviewReference;
import me.robnoo02.plotreviewplugin.utils.RankUtil;

/**
 * This class builds Gui's.
 * 
 * @author Robnoo02
 *
 */
public class GuiFactory {

	private static final String ARROW_LEFT = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=";
	private static final String ARROW_RIGHT = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVmMzU2YWQyYWE3YjE2NzhhZWNiODgyOTBlNWZhNWEzNDI3ZTVlNDU2ZmY0MmZiNTE1NjkwYzY3NTE3YjgifX19";

	/**
	 * Returns a new instance of a Gui.
	 * A Gui can be opened using its open() method.
	 * @param p is Player to open Gui for
	 * @param page is current page
	 * @param stored is a reference to another Gui (to go back to previous page)
	 * @return new Review Gui which can be opened with its open() method.
	 */
	public static Gui reviewGui(Player p, int page, Gui stored) {
		int start = 0;
		int end = 44;
		int size = DataFile.getInstance().getUnreviewedReferences().size();
		Gui gui = new Gui.Builder(p)
				.title("§4§lAvailable Reviews §9[" + size + "]")
				.fillSlots(start, end, page, getReviewHeads(p)).size(54).gui(stored).build();
		gui.setItem(48, getPreviousPage(p, start, end, page, size, gui.getGuiLinked()));
		gui.setItem(49, getExit(p));
		gui.setItem(50, getNextPage(p, start, end, page, size, gui));
		return gui;
	}

	/**
	 * @param p is Player
	 * @return array containing all heads which should be putted into the Gui
	 */
	private static GuiItem[] getReviewHeads(Player p) {
		HashMap<Integer, String> data = DataFile.getInstance().getUnreviewedReferences();
		GuiItem[] items = new GuiItem[data.size()];
		int i = 0;
		for (Integer id : data.keySet()) {
			String uuid = data.get(id);
			HashMap<UserDataField, String> info = UserDataManager.getInstance().getUserData(uuid, String.valueOf(id));
			GuiItem item = new GuiItem.Builder()
					.name(RankUtil.getRankFormatted(info.get(UserDataField.RANK)) + " &7"
							+ Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName())
					.lore("&3(" + String.valueOf(id) + ")", "&7World: &f" + info.get(UserDataField.WORLD),
							"&7Plot: &f[" + info.get(UserDataField.PLOT) + "]")
					.leftClick(
							() -> ReviewReference
											.getPlot(info.get(UserDataField.WORLD), info.get(UserDataField.PLOT))
											.teleportPlayer(PlotPlayer.wrap(p)))
					.playerSkull(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).build();
			items[i++] = item;
		}
		return items;
	}
	
	/*
	 * The following methods are used to build some GuiItems.	 * 
	 */

	/**
	 * @return GuiItem to go to the next page, only used in reviewGui() method.
	 */
	private static GuiItem getNextPage(Player p, int start, int end, int page, int size, Gui current) {
		int total = GuiUtil.getTotalPages(start, end, page, size);
		if (page >= total)
			return null;
		return new GuiItem.Builder().name("&aNext Page").customSkull(ARROW_RIGHT).lore("&7Page &e" + page + "/" + total)
				.click(() -> {
					Gui gui = reviewGui(p, page + 1, current);
					gui.open();
				}).build();
	}

	/**
	 * @return GuiItem to go back to the previous page.
	 */
	private static GuiItem getPreviousPage(Player p, int start, int end, int page, int size, Gui gui) {
		int total = GuiUtil.getTotalPages(start, end, page, size);
		if (page < 2)
			return null;
		return new GuiItem.Builder().name("&cPrevious Page").customSkull(ARROW_LEFT)
				.lore("&7Page &e" + page + "/" + total).click(() -> {
					gui.open();
				}).build();
	}

	/**
	 * @return GuiItem to exit the menu.
	 */
	private static GuiItem getExit(Player p) {
		return new GuiItem.Builder().material(Material.BARRIER).name("&cExit Menu").click(() -> p.closeInventory())
				.build();
	}

}
