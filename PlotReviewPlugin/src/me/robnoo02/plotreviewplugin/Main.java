package me.robnoo02.plotreviewplugin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.robnoo02.plotreviewplugin.commands.DebugCmd;
import me.robnoo02.plotreviewplugin.commands.PreCommandEvent;
import me.robnoo02.plotreviewplugin.commands.ReviewCmd;
import me.robnoo02.plotreviewplugin.commands.SubmitCmd;
import me.robnoo02.plotreviewplugin.files.ConfigManager;
import me.robnoo02.plotreviewplugin.files.DataFile;
import me.robnoo02.plotreviewplugin.guis.GuiUtility;

public class Main extends JavaPlugin {
	
	public void onEnable() {
		Bukkit.getLogger().info(getDataFolder().toString());
		saveDefaultConfig();
		setupUserDataFolder();
		Bukkit.getPluginManager().registerEvents(new GuiUtility(), this);
		Bukkit.getPluginManager().registerEvents(new PreCommandEvent(), this);
		getCommand("review").setExecutor(new ReviewCmd());
		getCommand("submit").setExecutor(new SubmitCmd());
		getCommand("prdebug").setExecutor(new DebugCmd());
		ConfigManager.getInstance().setEnumMessages();
		DataFile.getInstance().setup();
	}
	
	public void setupUserDataFolder() {
		File file = new File(this.getDataFolder() + File.separator + "userdata");
		if(!file.exists())
			file.mkdirs();
	}
	
	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}
	
}
