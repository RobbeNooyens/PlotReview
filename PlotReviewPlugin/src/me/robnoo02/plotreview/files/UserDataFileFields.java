package me.robnoo02.plotreview.files;

public class UserDataFileFields {
	
	public interface UserDataField {
		public String getPath(int id);
		public String getDefaultValue();
	}

	/**
	 * Each enum value represents a key in a userdate yml.
	 * Placeholder is used to replace the yml's value with.
	 * @param pH Placeholder
	 * @author Robnoo02
	 *
	 */
	public static enum TicketDataField implements UserDataField {
		RANK("%rank%", "default"), DATE("%date%", "2000-01-01 00:00:00"), WORLD("%world%", "world"), PLOT("%plot%", "0;0"), STOC("%stoc%", "0"), AVERAGE_STOC("%average_stoc%", "0"), STRUCTURE_SCORE(
				"%structure_score%", "0"), TERRAIN_SCORE("%terrain_score%", "0"), ORGANICS_SCORE(
						"%organics_score%", "0"), COMPOSITION_SCORE("%composition_score%", "0"), STAFF("%staff%", ""), REVIEWED("%reviewed%", "false");

		private final String placeholder, defaultVal;

		private TicketDataField(String pH, String defaultVal) {
			this.placeholder = pH;
			this.defaultVal = defaultVal;
		}

		/**
		 * Returns path to get a value in a userdata yml.
		 * @param id Review ticket ID
		 * @return Path for yml
		 */
		@Override
		public String getPath(int id) {
			return "tickets." + String.valueOf(id) + "." + this.toString().toLowerCase();
		}

		public String getPlaceHolder() {
			return placeholder;
		}

		@Override
		public String getDefaultValue() {
			return this.defaultVal;
		}
	}
	
	public static enum PlayerInfoField implements UserDataField {
		AVERAGE_STOC("0"), TOTAL_STOC("0"), RATING("0"), TOTAL_PLOT_SCORE("0"), NUMBER_OF_SUBMISSIONS("0"), LATEST_NAME("unknown");
		
		private final String defaultVal;
		
		private PlayerInfoField(String defaultVal) {
			this.defaultVal = defaultVal;
		}
		
		@Override
		public String getPath(int id) {
			return getPath();
		}
		
		public String getPath() {
			return "player-info." + this.toString().toLowerCase();
		}

		@Override
		public String getDefaultValue() {
			return defaultVal;
		}
	}
	
	public static enum OldScoresField implements UserDataField {
		AVERAGE_STOC("0"), TOTAL_STOC("0"), RATING("0"), TOTAL_PLOT_SCORE("0");
		
		private final String defaultVal;
		
		private OldScoresField(String defaultVal) {
			this.defaultVal = defaultVal;
		}
		
		@Override
		public String getPath(int id) {
			return getPath();
		}
		
		public String getPath() {
			return "old-score." + this.toString().toLowerCase();
		}

		@Override
		public String getDefaultValue() {
			return defaultVal;
		}
	}

}
