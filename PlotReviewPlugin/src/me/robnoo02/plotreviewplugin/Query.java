package me.robnoo02.plotreviewplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreviewplugin.files.DataFileManager;
import me.robnoo02.plotreviewplugin.files.UserDataFile.UserDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;

public class Query {

	public static String request(int reviewId, QueryElement element) {
		if(DataFileManager.containsId(reviewId))
			return null;
		if(element == null)
			return null;
		switch(element) {
		case PLAYERNAME:
			return Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.getUUID(reviewId))).getName();
		case COMPOSITION_SCORE:
			break;
		case CREATION_DATE:
			break;
		case ORGANICS_SCORE:
			break;
		case PLOT_ID:
			break;
		case RANK:
			break;
		case RESULT:
			break;
		case REVIEWED_BY_STAFF:
			break;
		case STAFF:
			break;
		case STRUCTURE_SCORE:
			break;
		case TERRAIN_SCORE:
			break;
		case UUID:
			return DataFileManager.getUUID(reviewId);
		case WORLD:
			break;
		default:
			break;
		}
		return null;
	}
	
	/**
	 * 0: Review id
	 * 1: Reviewee name
	 * 2: Rank
	 * 3: Date
	 * 4: world
	 * 5: Plot
	 * 6: Result
	 * 7: score1
	 * 8: score2
	 * 9: score3
	 * 10: score4
	 * @return ArrayList containing info for /review info command
	 */
	public static ArrayList<String> requestInfoCommand(int reviewId){
		ArrayList<String> output = new ArrayList<>();
		String idValue = DataFileManager.getValue(reviewId);
		HashMap<UserDataField, String> userData = UserDataManager.getInstance().getUserData(reviewId);
		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.strip(idValue, "\\+", 0)));
		output.set(1, String.valueOf(reviewId));
		output.set(1, reviewee.getName());
		output.set(2, userData.get(UserDataField.RANK));
		output.set(3, userData.get(UserDataField.DATE));
		output.set(4, userData.get(UserDataField.WORLD));
		output.set(5, userData.get(UserDataField.PLOT));
		output.set(6, userData.get(UserDataField.RESULT));
		return output;
	}
	
	/**
	 * 0: Structure
	 * 1: Terrain
	 * 2: Organics
	 * 3: Composition
	 * @return ArrayList containing scores
	 */
	public static ArrayList<String> requestScores(int reviewId){
		ArrayList<String> output = new ArrayList<>();
		output.add(request(reviewId, QueryElement.STRUCTURE_SCORE));
		output.add(request(reviewId, QueryElement.TERRAIN_SCORE));
		output.add(request(reviewId, QueryElement.ORGANICS_SCORE));
		output.add(request(reviewId, QueryElement.COMPOSITION_SCORE));
		return output;
	}

}
