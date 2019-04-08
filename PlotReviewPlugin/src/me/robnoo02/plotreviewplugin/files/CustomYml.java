package me.robnoo02.plotreviewplugin.files;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import me.robnoo02.plotreviewplugin.Main;

/**
 * <h1>CustomYml</h1>
 * <hr>
 * <p>This class represents a Yml file.<br>
 * This class is designed for instantiation.<br>
 * IMPORTANT !! You should manually call the setup() method before editing the yml !!</p>
 * 
 * @author Robnoo02
 *
 */
public class CustomYml {

	private final File file;
	private YamlConfiguration config;
	private final String fileName;
	private final boolean createNew;

	/**
	 * Constructor
	 * Private for static factory instance creation
	 * @param folder Name subfolder
	 * @param fileName Name file without .yml part
	 * @param createNew true to create a whole new file (if not exists)
	 */
	private CustomYml(final String folder, final String fileName, final boolean createNew) {
		Objects.requireNonNull(folder, "Please specify a folder!");
		Objects.requireNonNull(fileName, "Please specify a file!");
		this.fileName = fileName.replaceAll(".yml", ""); // Makes sure that file name doesn't contain '.yml'.
		this.file = new File(Main.getInstance().getDataFolder() + File.separator + folder, this.fileName + ".yml");
		this.createNew = createNew;
	}

	/**
	 * Constructor
	 * Private for static factory instance creation.
	 * @param fileName Name file without .yml part.
	 * @param createNew true to create a whole new file (if not exists).
	 */
	private CustomYml(final String fileName, final boolean createNew) {
		this.fileName = fileName;
		this.file = new File(Main.getInstance().getDataFolder(), fileName + ".yml");
		this.createNew = createNew;
	}

	/**
	 * Setup a reference to a yml file in a folder and return it.
	 * @param folder is the folder where the file is located in.
	 * @param fileName is the name of the file, without the '.yml' part.
	 * @param createNew false if the file exists in the '.jar' file.
	 * @return new instance containing a reference to a yml.
	 */
	public static CustomYml createFileInFolder(final String folder, final String fileName, final boolean createNew) {
		return new CustomYml(folder, fileName, createNew);
	}

	/**
	 * Sets up a reference to a yml file and return it.
	 * @param fileName is the name of the file, without the '.yml' part
	 * @param createNew false if the file exists in the '.jar' file
	 * @return new instance containing a reference to a yml.
	 */
	public static CustomYml createFile(final String fileName, final boolean createNew) {
		return new CustomYml(fileName, createNew);
	}

	/**
	 * Gets an object from the referenced yml file.
	 * @param path to the object in the yml.
	 * @return the object stored in the field for the given path in the yml.
	 */
	public Object get(String path) {
		return config.get(path);
	}

	/**
	 * Gets a String from the referenced yml file.
	 * @param path to the String in the yml.
	 * @return the String in the field for the given path in the yml.
	 */
	public String getString(String path) {
		return (String) config.get(path);
	}

	/**
	 * Gets a int from the referenced yml file.
	 * @param path to the int in the yml.
	 * @return the int in the field for the given path in the yml.
	 */
	public int getInt(String path) {
		return config.getInt(path);
	}

	/**
	 * Sets a field in the yml file.
	 * @param path the field where the Object should be saved.
	 * @param obj the Object that should be saved.
	 */
	public void set(String path, Object obj) {
		config.set(path, obj);
		saveFile();
	}

	/**
	 * @return true if the configurationsection for the given path contains the key.
	 * @param path location of configurationsection.
	 * @param key the key that should be sought.
	 */
	public boolean containsKey(String path, String key) {
		return config.getConfigurationSection(path).getKeys(false).contains(key);
	}
	
	public ConfigurationSection getConfigSection(String path) {
		return config.getConfigurationSection(path);
	}

	/**
	 * @return the name of the file without the '.yml' part
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * VERY IMPORTANT!!!
	 * This method should be called in order to use the getters and setters.
	 */
	public void setup() {
		Main plugin = Main.getInstance();
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdirs();
		if (!file.exists())
			if (createNew)
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			else
				plugin.saveResource(fileName + ".yml", false);
		this.config = YamlConfiguration.loadConfiguration(file);
		loadFile();
		saveFile();
	}

	/**
	 * Ignore: supports setup()
	 */
	private void loadFile() {
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ignore: supports setup()
	 */
	private void saveFile() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a reference to this config file.
	 * Only use it to read inforation!
	 * Writing content directly to it without saving it correctly can clear the whole yml file.
	 * @return the config file of the referenced file.
	 */
	public YamlConfiguration getYml() {
		return config;
	}

}
