package me.robnoo02.plotreviewplugin.files;

import me.robnoo02.plotreviewplugin.review.ReviewID;

/**
 * Represents the datafile.yml file.
 * This class has static fields and methods since there's only one DataFile.
 * The custom yml "datafile" stores:
 * - ReviewID counter
 * - References to files
 *   - Key: id
 *   - Value: uuid+plotLocation+reviewed
 *     - uuid: String used to get the filepath for userdata
 *     - plotLocation: Used to determine whether plot is already submitted or not
 *     - reviewed: whether or not the plot is already reviewed or not
 * 
 * @author Robnoo02
 *
 */
public class DataFile {
	
	private static CustomYml yml;

	/**
	 * Load file and reads current ID progress from datafile
	 */
	public static void setup() {
		if(yml != null)
			return; // prevents multiple setup() calls
		yml = CustomYml.createFile("datafile", false);
		yml.setup();
		ReviewID.setCurrentCount(DataFileManager.getIDProgress());
	}
	
	public static CustomYml getCustomYml() {
		return yml;
	}

}
