package me.robnoo02.plotreviewplugin.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.PlotPlayer;

import me.robnoo02.plotreviewplugin.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreviewplugin.guis.GuiUtility.Gui;
import me.robnoo02.plotreviewplugin.guis.GuiUtility.GuiItem;
import me.robnoo02.plotreviewplugin.utils.PlotUtil;
import me.robnoo02.plotreviewplugin.utils.RankUtil;

public class HistoryGui extends Gui implements SkullTextures {

	private static final int START_FILL = 0, END_FILL = 44;
	private final ArrayList<GuiItem> items;

	private HistoryGui(Player p, int page, ArrayList<GuiItem> items, Gui previousGui) {
		super(p, "§4§lReview History", 54, page);
		this.items = items;
		final int size = items.size();
		this.fillSlots(START_FILL, END_FILL, page, items.toArray(new GuiItem[size]));
		this.setGuiLink(previousGui);
		this.setItem(48, getPreviousPage());
		this.setItem(49, getExit());
		this.setItem(50, getNextPage());
	}

	public static boolean show(Player p, int page, ArrayList<GuiItem> items, Gui previousGui) {
		if(p == null || page < 1 || items == null)
			return false;
		Gui gui = new HistoryGui(p, page, items, previousGui);
		gui.open();
		return true;
	}

	public static boolean show(HistoryGui gui) {
		return show(gui.getPlayer(), gui.getPage(), gui.getItems(), gui);
	}

	/**
	 * @return GuiItem to go to the next page, only used in reviewGui() method.
	 */
	private GuiItem getNextPage() {
		int total = GuiUtility.getTotalPages(START_FILL, END_FILL, this.getPage(), items.size());
		if (this.getPage() >= total) return null;
		return new GuiItem.Builder().name("&aNext Page").customSkull(ARROW_RIGHT)
				.lore("&7Page &e" + this.getPage() + "/" + total).click(() -> HistoryGui.show(this)).build();
	}

	public ArrayList<GuiItem> getItems() {
		return items;
	}

	/**
	 * @return GuiItem to go back to the previous page.
	 */
	private GuiItem getPreviousPage() {
		int total = GuiUtility.getTotalPages(START_FILL, END_FILL, this.getPage(), items.size());
		if (this.getPage() < 2 || this.getGuiLink() == null) return null;
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

	public static GuiItem getHistoryItem(Player p, HashMap<TicketDataField, String> info, String playerUUID, int reviewID) {
		return new GuiItem.Builder()
				.name(RankUtil.getRankFormatted(info.get(TicketDataField.RANK)) + " &7"
						+ Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName())
				.lore("&3(" + String.valueOf(reviewID) + ")", "&7World: &f" + info.get(TicketDataField.WORLD),
						"&7Plot: &f[" + info.get(TicketDataField.PLOT) + "]")
				.leftClick(() -> PlotUtil.getPlot(info.get(TicketDataField.WORLD), info.get(TicketDataField.PLOT))
						.teleportPlayer(PlotPlayer.wrap(p)))
				.playerSkull(Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).getName()).build();
	}

}
