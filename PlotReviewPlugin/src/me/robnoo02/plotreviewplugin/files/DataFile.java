package me.robnoo02.plotreviewplugin.files;

import me.robnoo02.plotreviewplugin.review.Review;

public class DataFile {

	private static final DataFile INSTANCE = new DataFile();
	private CustomYml yml;
	private static final String REVIEWPATH = "reviews";

	private enum YmlField {
		REVIEWEE("reviewee-uuid"), PLOT_LOCATION("plot-location"), RANK("rank"), SUB_DATE("submission-date");

		private String fieldName;

		private YmlField(String s) {
			this.fieldName = s;
		}

		public String getPath(String id) {
			return REVIEWPATH + "." + id + "." + fieldName;
		}
	}

	private DataFile() {
	}
	
	public void setup() {
		this.yml = CustomYml.createFile("datafile");
		yml.setup();
	}

	public int getIDProgress() {
		return (int) yml.get("id-counter");
	}

	public Review getReview(String id) {
		if (!yml.containsKey(REVIEWPATH, id))
			return null;
		String reviewee = getString(id, YmlField.REVIEWEE);
		String pLoc = getString(id, YmlField.PLOT_LOCATION);
		String rank = getString(id, YmlField.RANK);
		String subDate = getString(id, YmlField.SUB_DATE);
		return Review.loadFromFile(id, reviewee, pLoc, subDate, rank);
	}
	
	public void saveReview(Review review) {
		String id = String.valueOf(review.getId().getId());
		yml.set(YmlField.REVIEWEE.getPath(id), review.getReviewee().getUUID().toString());
		yml.set(YmlField.PLOT_LOCATION.getPath(id), review.getPlotString());
		yml.set(YmlField.RANK.getPath(id), review.getReviewee().getCurrentRank());
		yml.set(YmlField.SUB_DATE.getPath(id), review.getReviewee().getDateFormatted());
	}

	public String getString(String id, YmlField field) {
		try {
			return (String) yml.get(field.getPath(id));
		} catch (Exception e) {
			return "";
		}
	}
	
	public void removeReview(int id) {
		yml.set(REVIEWPATH + "." + String.valueOf(id), null);
	}

	public static DataFile getInstance() {
		return INSTANCE;
	}

}
