package me.robnoo02.plotreviewplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.robnoo02.plotreviewplugin.guis.GuiUtil;

public class Main extends JavaPlugin {

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new GuiUtil(), this);
	}
	
	
}
