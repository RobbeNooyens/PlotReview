package me.robnoo02.plotreview.guis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.PlotPlayer;

import me.robnoo02.plotreview.Query.QueryElement;
import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.guis.GuiUtility.Gui;
import me.robnoo02.plotreview.guis.GuiUtility.GuiItem;
import me.robnoo02.plotreview.utils.PlotUtil;
import me.robnoo02.plotreview.utils.RankUtil;

public class HistoryGui extends Gui implements SkullTextures {

	private static final int[] SLOTS = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33,
			34, 37, 38, 39, 40, 41, 42, 43 }, CORNER_SLOTS = { 0, 8, 45, 53 },
			LINES_SLOTS = { 1, 2, 3, 4, 5, 6, 7, 9, 18, 27, 36, 17, 26, 35, 44, 46, 47, 48, 50, 51, 52 };;
	private final ArrayList<GuiItem> items;
	private OfflinePlayer target;
	private GuiItem pane1, pane2;

	private HistoryGui(Player p, int page, ArrayList<GuiItem> items, Gui previousGui, OfflinePlayer target) {
		super(p, "§8Review History: &3" + target.getName(), 54, page);
		this.items = items;
		this.target = target;
		final int size = items.size();
		this.fillSlots(SLOTS, page, items.toArray(new GuiItem[size]));
		this.setGuiLink(previousGui);
		border(page);
		this.setItem(46, getPreviousPage());
		this.setItem(49, getExit());
		this.setItem(52, getNextPage());
	}

	private void border(int page) {
		pane1 = getPane(3);
		pane2 = getPane(0);
		GuiItem[] panes1 = new GuiItem[CORNER_SLOTS.length];
		GuiItem[] panes2 = new GuiItem[LINES_SLOTS.length];
		Arrays.fill(panes1, pane1);
		Arrays.fill(panes2, pane2);
		this.fillSlots(CORNER_SLOTS, page, panes1);
		this.fillSlots(LINES_SLOTS, page, panes2);
	}

	public static boolean show(Player p, int page, ArrayList<GuiItem> items, Gui previousGui, OfflinePlayer target) {
		if (p == null || page < 1 || items == null) return false;
		Gui gui = new HistoryGui(p, page, items, previousGui, target);
		gui.open();
		return true;
	}

	public static boolean show(HistoryGui gui) {
		return show(gui.getPlayer(), gui.getPage(), gui.getItems(), gui, gui.getTarget());
	}

	/**
	 * @return GuiItem to go to the next page, only used in reviewGui() method.
	 */
	private GuiItem getNextPage() {
		int total = GuiUtility.getTotalPages(SLOTS, this.getPage(), items.size());
		if (this.getPage() >= total) return pane2;
		return new GuiItem.Builder().name("&aNext Page").customSkull(ARROW_RIGHT)
				.lore("&7Page &e" + this.getPage() + "/" + total).click(() -> HistoryGui.show(this)).build();
	}

	public ArrayList<GuiItem> getItems() {
		return items;
	}

	public OfflinePlayer getTarget() {
		return target;
	}

	private GuiItem getPane(int color) {
		return new GuiItem.Builder().material(Material.STAINED_GLASS_PANE).data(color).name(" ").hideFlags().build();
	}

	/**
	 * @return GuiItem to go back to the previous page.
	 */
	private GuiItem getPreviousPage() {
		int total = GuiUtility.getTotalPages(SLOTS, this.getPage(), items.size());
		if (this.getPage() < 2 || this.getGuiLink() == null) return pane2;
		return new GuiItem.Builder().name("&cPrevious Page").customSkull(ARROW_LEFT)
				.lore("&7Page &e" + this.getPage() + "/" + total).click(() -> this.getGuiLink().open()).build();
	}

	/**
	 * @return GuiItem to exit the menu.
	 */
	private GuiItem getExit() {
		return new GuiItem.Builder().material(Material.BARRIER).name("&cExit Menu")
				.click(() -> this.getPlayer().closeInventory()).build();
	}

	public static GuiItem getHistoryItem(Player p, HashMap<QueryElement, String> info, String playerUUID,
			int reviewID) {
		String dataFileInfo = DataFileManager.getValue(reviewID);
		int matData = 0;
		if (dataFileInfo.contains("passed"))
			matData = 5;
		else if (dataFileInfo.contains("failed"))
			matData = 14;
		else if (dataFileInfo.contains("n/a")) matData = 4;
		return new GuiItem.Builder()
				.name(RankUtil.getRankFormatted(info.get(QueryElement.RANK)) + " &7"
						+ Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName())
				.lore(getItemLore(info, reviewID))
				.leftClick(() -> PlotUtil.getPlot(info.get(QueryElement.WORLD), info.get(QueryElement.PLOT_ID))
						.teleportPlayer(PlotPlayer.wrap(p)))
				.material(Material.CONCRETE).data(matData).build();
	}

	private static String[] getItemLore(HashMap<QueryElement, String> info, int ticketId) {
		if (info.containsKey(QueryElement.STAFF_NAME)) {
			String[] lore = { "&3(" + String.valueOf(ticketId) + ")", "&7World: &f" + info.get(QueryElement.WORLD),
					"&7Plot: &f[" + info.get(QueryElement.PLOT_ID) + "]",
					"&7Staff: &f" + info.get(QueryElement.STAFF_NAME),
					"&7STOC: &f" + info.get(QueryElement.STRUCTURE_SCORE) + "-" + info.get(QueryElement.TERRAIN_SCORE)
							+ "-" + info.get(QueryElement.ORGANICS_SCORE)+ "-" + info.get(QueryElement.COMPOSITION_SCORE),
					"&7Average STOC: &f" + info.get(QueryElement.AVG_STOC),
					"&7Plot Score: &f" + info.get(QueryElement.PLOT_SCORE)};
			return lore;
		} else {
			String[] lore2 = { "&3(" + String.valueOf(ticketId) + ")", "&7World: &f" + info.get(QueryElement.WORLD),
					"&7Plot: &f[" + info.get(QueryElement.PLOT_ID) + "]" };
			return lore2;
		}
	}

}
