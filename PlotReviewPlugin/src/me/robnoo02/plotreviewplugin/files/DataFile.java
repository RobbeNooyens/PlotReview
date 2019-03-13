package me.robnoo02.plotreviewplugin.files;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.intellectualcrafters.plot.object.Plot;

import me.robnoo02.plotreviewplugin.review.ReviewID;
import me.robnoo02.plotreviewplugin.review.ReviewReference;

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
 * - Handle references (= reference to a userdata yml file) 
 * 
 * @author Robnoo02
 *
 */
public class DataFile {
	
	/*
	 * PLEASE NOTE:
	 * This class is temporary, it isn't finished yet.
	 * It still should be cleaned up or rewritten.
	 */
	
	private static final DataFile INSTANCE = new DataFile(); // Singleton instance
	private CustomYml yml;
	private static final String REVIEWPATH = "reviews";
	private static final String IDPATH = "id-counter";

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
		ReviewID.setCurrentCount(getIDProgress());
	}
	
	/**
	 * Extracts all unreviewed reviews saved in yml.
	 * @return HashMap containing Review ID and reference from datafile
	 */
	public HashMap<Integer, String> getUnreviewedReferences() {
		Set<String> keys = yml.getYml().getConfigurationSection(REVIEWPATH).getKeys(false); // All saved Review ID's
		HashMap<Integer, String> uuidOutput = new HashMap<>();
		for(String key: keys) { // Loops through keys to determine which Reviews aren't reviewed yet
			String value = yml.getString(REVIEWPATH + "." + key);
			if(value.contains("false"))
				uuidOutput.put(Integer.valueOf(key), ReviewReference.getUUID(value));
		}
		return uuidOutput;
	}

	public int getIDProgress() {
		return (Integer) yml.getInt(IDPATH);
	}

	public void updateIDProgress() {
		yml.set(IDPATH, ReviewID.getCurrentCount());
	}

	public String getValue(int id) {
		return (String) yml.get(REVIEWPATH + "." + String.valueOf(id));
	}

	public String getUUIDString(int id) {
		String info = (String) yml.get(REVIEWPATH + "." + String.valueOf(id));
		return info.substring(0, info.indexOf("+"));
	}

	public void addReview(int id, String uuid) {
		yml.set(REVIEWPATH + "." + String.valueOf(id), uuid);
	}

	public String getReviewID(final Plot plot) {
		ConfigurationSection section = getYml().getConfigurationSection("reviews");
		String formattedPlot = formatPlot(plot);
		for (String s : section.getKeys(false))
			if (section.getString(s).contains(formattedPlot))
				return s;
		return null;
	}

	public YamlConfiguration getYml() {
		return yml.getYml();
	}

	public static DataFile getInstance() {
		return INSTANCE;
	}

	public String formatPlot(Plot plot) {
		return plot.getWorldName() + ":" + plot.getId().toString();
	}

}
