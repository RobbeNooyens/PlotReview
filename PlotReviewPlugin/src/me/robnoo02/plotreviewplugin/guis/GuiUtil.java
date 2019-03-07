package me.robnoo02.plotreviewplugin.guis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class GuiUtil implements Listener {
	
	private static final GuiManager INSTANCE = new GuiManager();

	public static final class Gui {

		private final int invSize;
		private final String title;
		private final Inventory inv;
		private final Player player;
		private final GuiItem[] guiContents;
		private final Gui gui;

		private Gui(Builder builder) {
			this.invSize = builder.size;
			this.title = builder.title;
			this.inv = Bukkit.createInventory(null, invSize, title);
			this.player = builder.player;
			this.guiContents = builder.guiContents;
			this.gui = builder.guiLink;
			fillInventory();
		}

		public int getSize() {
			return invSize;
		}

		public String getTitle() {
			return title;
		}

		public Inventory toInventory() {
			return inv;
		}

		public Player getPlayer() {
			return player;
		}

		private void fillInventory() {
			for (int i = 0; i < invSize; i++)
				if (guiContents[i] != null)
					inv.setItem(i, guiContents[i].getItem());
		}

		public void open() {
			player.openInventory(inv);
			GuiManager.getInstance().addGuiLink(player, this);
		}

		public GuiItem getItem(int slot) {
			return guiContents[slot];
		}

		public Gui getGuiLinked() {
			return gui;
		}

		public static class Builder {
			private int size = 27;
			private String title = "&7Gui";
			private GuiItem[] guiContents = new GuiItem[54];
			private Player player;
			private Gui guiLink;

			public Builder(Player p) {
				this.player = p;
			}

			public Builder size(int size) {
				this.size = size;
				return this;
			}

			public Builder title(String title) {
				this.title = title;
				return this;
			}

			public Builder item(int place, GuiItem item) {
				if (!(place < 0 || place > 54))
					guiContents[place] = item;
				return this;
			}
			
			public Builder fillSlots(int start, int end, GuiItem... items) {
				if(start >= 0 && start < end && (end - start) < 54) {
					for(int i = start; i < end; i++)
						guiContents[i] = items[i-start];
				}
				return this;
			}

			public Builder gui(Gui gui) {
				this.guiLink = gui;
				return this;
			}

			public Gui build() {
				return new Gui(this);
			}
		}
	}

	public static final class GuiItem {

		private final ItemStack itemStack;
		private final Material material;
		private final int amount, data;
		private final ArrayList<String> lore;
		private final String name, customSkull;
		private final Runnable leftClick, rightClick, middleClick;
		private final boolean glowing, hideFlags;
		private final Skull skullType;
		protected InventoryClickEvent e;

		private enum Skull {
			NONE, PLAYER, CUSTOM
		};

		private GuiItem(Builder builder) {
			this.itemStack = builder.item;
			this.material = builder.material;
			this.amount = builder.amount;
			this.name = builder.itemName;
			this.lore = builder.lore;
			this.customSkull = builder.customSkull;
			this.leftClick = builder.leftClick;
			this.rightClick = builder.rightClick;
			this.middleClick = builder.middleClick;
			this.glowing = builder.glowing;
			this.hideFlags = builder.hideFlags;
			this.skullType = builder.skullType;
			this.data = builder.data;
		}

		public static class Builder {
			private ItemStack item;
			private Material material = Material.STONE;
			private int amount = 1, data = 0;
			private ArrayList<String> lore;
			private String itemName, customSkull;
			private Runnable leftClick, rightClick, middleClick;
			private boolean glowing = false, hideFlags = false;
			private Skull skullType = Skull.NONE;
			public InventoryClickEvent e;

			public Builder material(Material mat) {
				this.material = mat;
				return this;
			}

			public Builder amount(int amount) {
				this.amount = amount;
				return this;
			}

			public Builder lore(ArrayList<String> lore) {
				this.lore = lore;
				return this;
			}
			
			public Builder data(int data) {
				this.data = data;
				return this;
			}

			public Builder lore(String... loreItems) {
				ArrayList<String> lore = new ArrayList<>();
				for (int i = 0; i < loreItems.length; i++) {
					lore.add(toColor(loreItems[i]));
				}
				this.lore = lore;
				return this;
			}

			public Builder name(String name) {
				this.itemName = toColor(name);
				return this;
			}

			public Builder playerSkull(String playerName) {
				this.customSkull = playerName;
				this.skullType = Skull.PLAYER;
				this.material = Material.SKULL_ITEM;
				return this;
			}

			public Builder customSkull(String url) {
				this.customSkull = url;
				this.skullType = Skull.CUSTOM;
				return this;
			}

			public Builder leftClick(Runnable action) {
				this.leftClick = action;
				return this;
			}

			public Builder rightClick(Runnable action) {
				this.rightClick = action;
				return this;
			}

			public Builder middleClick(Runnable action) {
				this.middleClick = action;
				return this;
			}

			public Builder itemStack(ItemStack item) {
				this.item = item;
				return this;
			}

			public Builder glowing() {
				this.glowing = true;
				return this;
			}

			public Builder hideFlags() {
				this.hideFlags = true;
				return this;
			}

			public GuiItem build() {
				return new GuiItem(this);
			}

		}

		protected ItemStack getItem() {
			ItemStack item = new ItemStack(material, amount, (byte) data);
			if (itemStack != null)
				item = itemStack;
			if (skullType.equals(Skull.NONE))
				item.setItemMeta(defaultMeta(item));
			else if (skullType.equals(Skull.PLAYER))
				item.setItemMeta(playerHeadMeta(item));
			else if (skullType.equals(Skull.CUSTOM))
				item.setItemMeta(customSkullMeta());
			return item;
		}

		public ItemMeta defaultMeta(ItemStack item) {
			ItemMeta meta = item.getItemMeta();
			if (name != null)
				meta.setDisplayName(name);
			if (lore != null)
				meta.setLore(lore);
			if (glowing)
				meta.addEnchant(Enchantment.MENDING, 0, true);
			if (hideFlags)
				meta.addItemFlags(ItemFlag.values());
			meta.addItemFlags(ItemFlag.values());
			return meta;
		}

		@SuppressWarnings("deprecation")
		public ItemMeta playerHeadMeta(ItemStack item) {
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			if (customSkull != null)
				meta.setOwner(customSkull);
			if (name != null)
				meta.setDisplayName(name);
			if (lore != null)
				meta.setLore(lore);
			if (glowing)
				meta.addEnchant(Enchantment.MENDING, 0, true);
			if (hideFlags)
				meta.addItemFlags(ItemFlag.values());
			meta.addItemFlags(ItemFlag.values());
			return meta;
		}

		public ItemMeta customSkullMeta() {
			ItemStack item = createSkull(customSkull);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			if (name != null)
				meta.setDisplayName(name);
			if (lore != null)
				meta.setLore(lore);
			if (glowing)
				meta.addEnchant(Enchantment.MENDING, 0, true);
			if (hideFlags)
				meta.addItemFlags(ItemFlag.values());
			meta.addItemFlags(ItemFlag.values());
			return meta;
		}

		private ItemStack createSkull(String skinURL) {
			ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			if (skinURL.isEmpty())
				return head;
			ItemMeta headMeta = head.getItemMeta();
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);
			profile.getProperties().put("textures", new Property("textures", skinURL));
			Field profileField = null;
			try {
				profileField = headMeta.getClass().getDeclaredField("profile");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			profileField.setAccessible(true);
			try {
				profileField.set(headMeta, profile);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			head.setItemMeta(headMeta);
			return head;
		}

		public void rightClickAction(InventoryClickEvent e) {
			if (this.rightClick != null)
				this.rightClick.run();
		}

		public void leftClickAction(InventoryClickEvent e) {
			if (this.leftClick != null)
				this.leftClick.run();
		}

		public void scrollClickAction(InventoryClickEvent e) {
			if (this.middleClick != null)
				this.middleClick.run();
		}
	}

	public static class GuiManager {

		private HashMap<String, Gui> currentGuis = new HashMap<>();

		private GuiManager() {
		}

		public static GuiManager getInstance() {
			return INSTANCE;
		}

		public void addGuiLink(Player p, Gui gui) {
			this.currentGuis.put(p.getName(), gui);
		}

		public boolean hasOpenGui(Player p) {
			if (currentGuis.containsKey(p.getName())) {
				if (p.getOpenInventory().getTopInventory().getType().equals(InventoryType.CHEST))
					return true;
				GuiManager.getInstance().removeGuiLink(p);
				return false;
			} else {
				return false;
			}
		}

		public Gui getGuiFor(Player p) {
			return hasOpenGui(p) ? currentGuis.get(p.getName()) : null;
		}

		public void removeGuiLink(Player p) {
			this.currentGuis.remove(p.getName());
		}

		public void openExistingGui(Player p, Gui gui) {
			removeGuiLink(p);
			gui.open();
		}

	}

	private static String toColor(String input) {
		String output = input;
		output = (ChatColor.translateAlternateColorCodes('&', output));
		return output;
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (GuiManager.getInstance().hasOpenGui(p))
			p.closeInventory();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (GuiManager.getInstance().hasOpenGui(p))
			GuiManager.getInstance().removeGuiLink(p);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (e.getRawSlot() < e.getView().getTopInventory().getSize()) {
				int slot = e.getRawSlot();
				ClickType click = e.getClick();
				if (slot >= 0) {
					if (GuiManager.getInstance().hasOpenGui(p)) {
						e.setCancelled(true);
						ItemStack mat = e.getCurrentItem();
						if (mat != null && mat.getType() != Material.AIR) {
							if (e.getInventory().getHolder() == null) {
								Gui gui = GuiManager.getInstance().getGuiFor(p);
								GuiItem item = gui.getItem(slot);
								if (item != null)
									if (click.equals(ClickType.LEFT) || click.equals(ClickType.SHIFT_LEFT)
											|| click.equals(ClickType.SHIFT_LEFT)) {
										item.leftClickAction(e);
									} else if (click.equals(ClickType.RIGHT) || click.equals(ClickType.SHIFT_RIGHT)
											|| click.equals(ClickType.SHIFT_RIGHT)) {
										item.rightClickAction(e);
									} else if (click.equals(ClickType.MIDDLE)) {
										item.scrollClickAction(e);
									}
							}
						}
					}
				}
			} else if (GuiManager.getInstance().hasOpenGui(p)) {
				e.setCancelled(true);
			}
		}

	}

}
