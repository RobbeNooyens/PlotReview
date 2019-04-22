package me.robnoo02.plotreviewplugin.guis;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.PlotPlayer;

import me.robnoo02.plotreviewplugin.files.DataFileManager;
import me.robnoo02.plotreviewplugin.files.UserDataFile.TicketDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;
import me.robnoo02.plotreviewplugin.guis.GuiUtility.Gui;
import me.robnoo02.plotreviewplugin.guis.GuiUtility.GuiItem;
import me.robnoo02.plotreviewplugin.utils.PlotUtil;
import me.robnoo02.plotreviewplugin.utils.RankUtil;

public class ReviewListGui extends Gui implements SkullTextures {

	private static final int START_FILL = 0, END_FILL = 44;
	private final int UNREVIEWED_SIZE;
	private final HashMap<Integer, String> unreviewed;

	private ReviewListGui(Player p, int page, Gui previousGui) {
		super(p, "§4§lReview History", 54, page);
		this.unreviewed = DataFileManager.getUnreviewedReferences();
		this.UNREVIEWED_SIZE = unreviewed.size();
		this.fillSlots(START_FILL, END_FILL, page, getReviewHeads());
		this.setGuiLink(previousGui);
		this.setItem(48, getPreviousPage());
		this.setItem(49, getExit());
		this.setItem(50, getNextPage());
	}

	public static boolean show(Player p, int page, Gui previousGui) {
		if(p == null || page < 1)
			return false;
		Gui gui = new ReviewListGui(p, page, previousGui);
		gui.open();
		return true;
	}
	
	public static void show(ReviewListGui gui) {
		Gui hGui = new ReviewListGui(gui.getPlayer(), gui.getPage(), gui);
		hGui.open();
	}

	/**
	 * @return GuiItem to go to the next page, only used in reviewGui() method.
	 */
	private GuiItem getNextPage() {
		int total = GuiUtility.getTotalPages(START_FILL, END_FILL, this.getPage(), this.UNREVIEWED_SIZE);
		if (this.getPage() >= total)
			return null;
		return new GuiItem.Builder().name("&aNext Page").customSkull(ARROW_RIGHT)
				.lore("&7Page &e" + this.getPage() + "/" + total)
				.click(() -> ReviewListGui.show(this)).build();
	}

	/**
	 * @return GuiItem to go back to the previous page.
	 */
	private GuiItem getPreviousPage() {
		int total = GuiUtility.getTotalPages(START_FILL, END_FILL, this.getPage(), this.UNREVIEWED_SIZE);
		if (this.getPage() < 2 || this.getGuiLink() == null)
			return null;
		return new GuiItem.Builder().name("&cPrevious Page").customSkull(ARROW_LEFT)
				.lore("&7Page &e" + this.getPage() + "/" + total).click(() -> this.getGuiLink().open()).build();
	}

	/**
	 * @return GuiItem to exit the menu.
	 */
	private GuiItem getExit() {
		return new GuiItem.Builder().material(Material.BARRIER).name("&cExit Menu").click(() -> this.getPlayer().closeInventory())
				.build();
	}
	
	/**
	 * @param p is Player
	 * @return array containing all heads which should be putted into the Gui
	 */
	private GuiItem[] getReviewHeads() {
		HashMap<Integer, String> data = DataFileManager.getUnreviewedReferences();
		GuiItem[] items = new GuiItem[data.size()];
		int i = 0;
		for (Integer id : data.keySet()) {
			String uuid = data.get(id);
			HashMap<TicketDataField, String> info = UserDataManager.getUserDataFile(id).getUserData(id);
			GuiItem item = new GuiItem.Builder()
					.name(RankUtil.getRankFormatted(info.get(TicketDataField.RANK)) + " &7"
							+ Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName())
					.lore("&3(" + String.valueOf(id) + ")", "&7World: &f" + info.get(TicketDataField.WORLD),
							"&7Plot: &f[" + info.get(TicketDataField.PLOT) + "]")
					.leftClick(
							() -> PlotUtil
											.getPlot(info.get(TicketDataField.WORLD), info.get(TicketDataField.PLOT))
											.teleportPlayer(PlotPlayer.wrap(this.getPlayer())))
					.playerSkull(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName()).build();
			items[i++] = item;
		}
		return items;
	}
	
}
