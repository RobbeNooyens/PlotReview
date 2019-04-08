package me.robnoo02.plotreviewplugin;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.robnoo02.plotreviewplugin.files.DataFileManager;

public class Query {

	public static String request(int reviewId, QueryElement element) {
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
	}
	
	/**
	 * 0: Playername
	 * 1: UUID
	 * 2: World
	 * 3: PlotId
	 * @return ArrayList containing info for /review info command
	 */
	public static ArrayList<String> requestInfoCommand(int reviewId){
		ArrayList<String> output = new ArrayList<>();
		output.add(request(reviewId, QueryElement.PLAYERNAME));
		output.add(request(reviewId, QueryElement.UUID));
		output.add(request(reviewId, QueryElement.WORLD));
		output.add(request(reviewId, QueryElement.PLOT_ID));
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
