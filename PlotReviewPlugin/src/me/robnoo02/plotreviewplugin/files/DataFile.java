package me.robnoo02.plotreviewplugin.files;

import org.bukkit.configuration.file.YamlConfiguration;

import me.robnoo02.plotreviewplugin.review.ReviewID;

/**
 * Represents the datafile.yml file.
 * This singleton manages the file with references to userdata-files.
 * The custom yml "datafile" stores:
 * - ReviewID counter
 * - References to files
 *   - Key: id
 *   - Value: uuid+plotLocation+reviewed
 *     - uuid: String used to get the filepath for userdata
 *     - plotLocation: Used to determine whether plot is already submitted or not
 *     - reviewed: whether or not the plot is already reviewed or not
 * Function:
 * - Handles references (= reference to a userdata yml file) 
 * 
 * @author Robnoo02
 *
 */
public class DataFile {
	
	private static final DataFile INSTANCE = new DataFile(); // Singleton instance
	private CustomYml yml;

	/**
	 * Constructor
	 * Private to enforce noninstantiability
	 */
	private DataFile() {
	}

	/**
	 * Load file and reads current ID progress from datafile
	 */
	public void setup() {
		this.yml = CustomYml.createFile("datafile", false);
		yml.setup();
		ReviewID.setCurrentCount(DataFileManager.getIDProgress());
	}

	public YamlConfiguration getYmlFile() {
		return yml.getYml();
	}
	
	public CustomYml getCustomYml() {
		return yml;
	}

	public static DataFile getInstance() {
		return INSTANCE;
	}

}
