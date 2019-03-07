package me.robnoo02.plotreviewplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.robnoo02.plotreviewplugin.commands.DebugCmd;
import me.robnoo02.plotreviewplugin.commands.PreCommandEvent;
import me.robnoo02.plotreviewplugin.commands.ReviewCmd;
import me.robnoo02.plotreviewplugin.commands.SubmitCmd;
import me.robnoo02.plotreviewplugin.files.Config;
import me.robnoo02.plotreviewplugin.files.DataFile;
import me.robnoo02.plotreviewplugin.guis.GuiUtil;

public class Main extends JavaPlugin {
	
	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(new GuiUtil(), this);
		Bukkit.getPluginManager().registerEvents(new PreCommandEvent(), this);
		getCommand("review").setExecutor(new ReviewCmd());
		getCommand("submit").setExecutor(new SubmitCmd());
		getCommand("prdebug").setExecutor(new DebugCmd());
		Config.getInstance().setEnumMessages();
		DataFile.getInstance().setup();
	}
	
	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}
	
}
