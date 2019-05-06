package me.robnoo02.plotreview;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.robnoo02.plotreview.commands.DebugCmd;
import me.robnoo02.plotreview.commands.PreCommandEvent;
import me.robnoo02.plotreview.commands.ReviewCmd;
import me.robnoo02.plotreview.commands.SubmitCmd;
import me.robnoo02.plotreview.events.ReviewJoinLeaveEvent;
import me.robnoo02.plotreview.files.ConfigManager;
import me.robnoo02.plotreview.files.DataFile;
import me.robnoo02.plotreview.guis.GuiUtility;
import me.robnoo02.plotreview.score.STOC;

public class Main extends JavaPlugin {
	
	public void onEnable() {
		// Setup classes implementing Listener
		Bukkit.getPluginManager().registerEvents(new GuiUtility(), this);
		Bukkit.getPluginManager().registerEvents(new PreCommandEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ReviewJoinLeaveEvent(), this);
		// Registering classes implementing CommandExecutor
		getCommand("review").setExecutor(new ReviewCmd());
		getCommand("submit").setExecutor(new SubmitCmd());
		getCommand("prdebug").setExecutor(new DebugCmd());
		// Setup files and file classes
		saveDefaultConfig();
		setupUserDataFolder();
		ConfigManager.setup();
		DataFile.setup();
		STOC.setup();
	}
	
	private void setupUserDataFolder() {
		File file = new File(this.getDataFolder() + File.separator + "userdata");
		if(!file.exists())
			file.mkdirs();
	}
	
	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}
	
}
