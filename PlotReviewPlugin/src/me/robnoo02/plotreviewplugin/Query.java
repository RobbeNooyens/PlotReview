package me.robnoo02.plotreviewplugin;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreviewplugin.files.DataFileManager;
import me.robnoo02.plotreviewplugin.files.UserDataFile.UserDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;

public class Query {
	
	public static enum QueryElement {

		TICKET_ID, REVIEWEE_NAME,
		REVIEWEE_UUID, PLOT_ID, WORLD, REVIEWED_BY_STAFF, // datafile.yml
		RANK, CREATION_DATE, STRUCTURE_SCORE, TERRAIN_SCORE, ORGANICS_SCORE, COMPOSITION_SCORE, RESULT, STAFF; // userdata
		
		public String request(int reviewId) {
			if(DataFileManager.containsId(reviewId))
				return null;
			switch(this) {
			case TICKET_ID:
				return String.valueOf(reviewId);
			case REVIEWEE_NAME:
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
			case REVIEWEE_UUID:
				return DataFileManager.getUUID(reviewId);
			case WORLD:
				break;
			default:
				break;
			}
			return null;
		}
		
	}
	
	public static enum QueryGroup {
		INFO, HISTORY, GUI, SCORES;
		
		public HashMap<QueryElement, String> group(int reviewId){
			switch(this) {
			case GUI:
				break;
			case HISTORY:
				break;
			case INFO:
				return requestInfoCommand(reviewId);
			case SCORES:
				return requestScores(reviewId);
			default:
				break;
			}
			return null;
		}
	}
	
	/**
	 * @return ArrayList containing info for /review info command
	 */
	private static HashMap<QueryElement, String> requestInfoCommand(int reviewId){
		HashMap<QueryElement, String> output = new HashMap<>();
		String idValue = DataFileManager.getValue(reviewId);
		HashMap<UserDataField, String> userData = UserDataManager.getInstance().getUserData(reviewId);
		OfflinePlayer reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.strip(idValue, "\\+", 0)));
		output.put(QueryElement.TICKET_ID, String.valueOf(reviewId));
		output.put(QueryElement.REVIEWEE_NAME, reviewee.getName());
		output.put(QueryElement.RANK, userData.get(UserDataField.RANK));
		output.put(QueryElement.CREATION_DATE, userData.get(UserDataField.DATE));
		output.put(QueryElement.WORLD, userData.get(UserDataField.WORLD));
		output.put(QueryElement.PLOT_ID, userData.get(UserDataField.PLOT));
		output.put(QueryElement.RESULT, userData.get(UserDataField.RESULT));
		return output;
	}
	
	/**
	 * 0: Structure
	 * 1: Terrain
	 * 2: Organics
	 * 3: Composition
	 * @return ArrayList containing scores
	 */
	private static HashMap<QueryElement, String> requestScores(int reviewId){
		HashMap<QueryElement, String> output = new HashMap<>();
		output.put(QueryElement.STRUCTURE_SCORE, QueryElement.STRUCTURE_SCORE.request(reviewId));
		output.put(QueryElement.TERRAIN_SCORE, QueryElement.TERRAIN_SCORE.request(reviewId));
		output.put(QueryElement.ORGANICS_SCORE, QueryElement.ORGANICS_SCORE.request(reviewId));
		output.put(QueryElement.COMPOSITION_SCORE, QueryElement.COMPOSITION_SCORE.request(reviewId));
		return output;
	}

}
