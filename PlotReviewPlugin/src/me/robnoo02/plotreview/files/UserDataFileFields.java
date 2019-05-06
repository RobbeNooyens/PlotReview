package me.robnoo02.plotreview.files;

public class UserDataFileFields {

	public interface UserDataField {
		public String getPath(int id);

		public String getDefaultValue();
	}

	/**
	 * Each enum value represents a key in a userdate yml. Placeholder is used to
	 * replace the yml's value with.
	 * 
	 * @param pH
	 *            Placeholder
	 * @author Robnoo02
	 *
	 */
	public static enum TicketDataField implements UserDataField {
		RANK("default"), DATE("2000-01-01 00:00:00"), WORLD("world"), PLOT("0;0"), STOC("0"), AVERAGE_STOC(
				"0"), STRUCTURE_SCORE("0"), TERRAIN_SCORE(
						"0"), ORGANICS_SCORE("0"), COMPOSITION_SCORE("0"), STAFF("unknown"), REVIEWED("false"), PASSES("false");

		private final String defaultVal;

		private TicketDataField(String defaultVal) {
			this.defaultVal = defaultVal;
		}

		/**
		 * Returns path to get a value in a userdata yml.
		 * 
		 * @param id
		 *            Review ticket ID
		 * @return Path for yml
		 */
		@Override
		public String getPath(int id) {
			return "tickets." + String.valueOf(id) + "." + this.toString().toLowerCase().replaceAll("-", "_");
		}

		@Override
		public String getDefaultValue() {
			return this.defaultVal;
		}
	}

	public static enum PlayerInfoField implements UserDataField {
		AVERAGE_STOC("0"), TOTAL_STOC("0"), RATING("0"), TOTAL_PLOT_SCORE("0"), ACCEPTED_SUBMISSIONS("0"), LATEST_NAME(
				"unknown"), REVIEWED_WHILE_OFFLINE("false");

		private final String defaultVal;

		private PlayerInfoField(String defaultVal) {
			this.defaultVal = defaultVal;
		}

		@Override
		public String getPath(int id) {
			return getPath();
		}

		public String getPath() {
			return "player-info." + this.toString().toLowerCase().replaceAll("-", "_");
		}

		@Override
		public String getDefaultValue() {
			return defaultVal;
		}
	}

	public static enum NewScoresField implements UserDataField {
		AVERAGE_STOC("0"), TOTAL_STOC("0"), RATING("0"), TOTAL_PLOT_SCORE("0"), NEW_SCORES_AVAILABLE("false");

		private final String defaultVal;

		private NewScoresField(String defaultVal) {
			this.defaultVal = defaultVal;
		}

		@Override
		public String getPath(int id) {
			return getPath();
		}

		public String getPath() {
			return "old-score." + this.toString().toLowerCase().replaceAll("-", "_");
		}

		@Override
		public String getDefaultValue() {
			return defaultVal;
		}
	}

}
