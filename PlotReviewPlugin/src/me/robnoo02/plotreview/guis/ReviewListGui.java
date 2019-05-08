package me.robnoo02.plotreview.guis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.PlotPlayer;

import me.robnoo02.plotreview.Query.QueryElement;
import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.guis.GuiUtility.Gui;
import me.robnoo02.plotreview.guis.GuiUtility.GuiItem;
import me.robnoo02.plotreview.utils.PlotUtil;
import me.robnoo02.plotreview.utils.RankUtil;
import me.robnoo02.plotreview.utils.SendMessageUtil;

public class ReviewListGui extends Gui implements SkullTextures {

	private static final int[] SLOTS = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43},
			CORNER_SLOTS = {0,8,45,53},
			LINES_SLOTS = {1,2,3,4,5,6,7,9,18,27,36,17,26,35,44,46,47,48,50,51,52};
	private final int UNREVIEWED_SIZE;
	private GuiItem pane1, pane2;

	private ReviewListGui(Player p, int page, Gui previousGui, HashMap<Integer, String> unreviewed) {
		super(p, "&8Review List &e[" + (unreviewed==null?0:unreviewed.size()) + "]", 54, page);
		this.UNREVIEWED_SIZE = unreviewed==null?0:unreviewed.size();
		if (unreviewed != null) {
			this.fillSlots(SLOTS, page, getReviewHeads());
		}
		border(page);
		this.setGuiLink(previousGui);
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

	public static boolean show(Player p, int page, Gui previousGui) {
		if (p == null || page < 1) return false;
		Gui gui = new ReviewListGui(p, page, previousGui, DataFileManager.getUnreviewedReferences());
		gui.open();
		return true;
	}

	public static void show(ReviewListGui gui) {
		Gui hGui = new ReviewListGui(gui.getPlayer(), gui.getPage(), gui, DataFileManager.getUnreviewedReferences());
		hGui.open();
	}

	/**
	 * @return GuiItem to go to the next page, only used in reviewGui() method.
	 */
	private GuiItem getNextPage() {
		int total = GuiUtility.getTotalPages(SLOTS, this.getPage(), this.UNREVIEWED_SIZE);
		if (this.getPage() >= total) return pane2;
		return new GuiItem.Builder().name("&aNext Page").material(Material.PAPER)
				.lore("&7Page &e" + this.getPage() + "/" + total).click(() -> ReviewListGui.show(this)).build();
	}

	/**
	 * @return GuiItem to go back to the previous page.
	 */
	private GuiItem getPreviousPage() {
		int total = GuiUtility.getTotalPages(SLOTS, this.getPage(), this.UNREVIEWED_SIZE);
		if (this.getPage() < 2 || this.getGuiLink() == null) return pane2;
		return new GuiItem.Builder().name("&cPrevious Page").material(Material.PAPER)
				.lore("&7Page &e" + this.getPage() + "/" + total).click(() -> this.getGuiLink().open()).build();
	}

	/**
	 * @return GuiItem to exit the menu.
	 */
	private GuiItem getExit() {
		return new GuiItem.Builder().material(Material.BARRIER).name("&cExit Menu")
				.click(() -> this.getPlayer().closeInventory()).build();
	}
	
	private GuiItem getPane(int color) {
		return new GuiItem.Builder().material(Material.STAINED_GLASS_PANE).data(color).name(" ").hideFlags().build();
	}

	/**
	 * @param p
	 *            is Player
	 * @return array containing all heads which should be putted into the Gui
	 */
	private GuiItem[] getReviewHeads() {
		HashMap<Integer, String> data = DataFileManager.getUnreviewedReferences();
		if (data == null) return new GuiItem[1];
		GuiItem[] items = new GuiItem[data.size()];
		int i = 0;
		for (Integer id : data.keySet()) {
			try {
				String uuid = data.get(id);
				HashMap<TicketDataField, String> info = UserDataManager.getUserDataFile(id).getUserData(id);
				GuiItem item = new GuiItem.Builder()
						.name(RankUtil.getRankFormatted(info.get(TicketDataField.RANK)) + " &7"
								+ Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName())
						.lore("&3(" + String.valueOf(id) + ")", "&7World: &f" + info.get(TicketDataField.WORLD),
								"&7Plot: &f[" + info.get(TicketDataField.PLOT) + "]")
						.leftClick(
								() -> {PlotUtil.getPlot(info.get(TicketDataField.WORLD), info.get(TicketDataField.PLOT))
										.teleportPlayer(PlotPlayer.wrap(this.getPlayer())); getClickable(id).run();})
						.rightClick(getClickable(id))
						.playerSkull(this.getPlayer().getName()).build();
				items[i++] = item;
			} catch (Exception e) {
				continue;
			}
		}
		return items;
	}

	private Runnable getClickable(int reviewId) {
		Runnable runnable = () -> SendMessageUtil.CLICK_TO_REVIEW.send((CommandSender) getPlayer(), QueryElement.TICKET_ID, String.valueOf(reviewId));
		return runnable;
	}

}
