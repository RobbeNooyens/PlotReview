package me.robnoo02.plotreviewplugin.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import me.robnoo02.plotreviewplugin.Main;

public class CustomYml {
	
	private final File file;
	private YamlConfiguration config;
	private final String fileName, path;

	private CustomYml(final String fileName) {
		this.fileName = fileName;
		this.path = fileName + ".yml";
		this.file = new File(Main.getInstance().getDataFolder(), path);
	}
	
	public static CustomYml createFile(final String fileName) {
		return new CustomYml(fileName);
	}
	
	public Object get(String path) {
		return config.get(path);
	}
	
	public void set(String path, Object obj) {
		config.set(path, obj);
		saveFile();
	}
	
	public boolean containsKey(String path, String key) {
		return config.getConfigurationSection(path).getKeys(false).contains(key);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setup() {
		Main plugin = Main.getInstance();
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdirs();
		if (!file.exists())
			plugin.saveResource(path, false);
		this.config = YamlConfiguration.loadConfiguration(file);
		loadFile();
		saveFile();
	}

	private void loadFile() {
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void saveFile() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
