package me.robnoo02.plotreviewplugin.review;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.intellectualcrafters.plot.object.Plot;

public class ReviewManager {

	private static final ReviewManager INSTANCE = new ReviewManager();
	
	private final HashMap<ReviewID, Review> reviews = new LinkedHashMap<>();
	
	private ReviewManager() {
	}
	
	public HashMap<ReviewID, Review> getReviews(){
		return reviews;
	}
	
	public boolean isAdded(final Plot plot) {
		Plot base = plot.getBasePlot(false);
		for(Review review: reviews.values())
			if(review.getPlot().equals(base))
				return true;
		return false;
	}
	
	public Review getReview(final ReviewID id) {
		if(reviews.size() == 0)
			return null;
		if(!reviews.containsKey(id))
			return null;
		return reviews.get(id);
	}
	
	public void saveReview(final Review review) {
		reviews.put(review.getId(), review);
	}
	
	public static ReviewManager getInstance() {
		return INSTANCE;
	}
	
	public String getReviewsString() {
		StringBuilder builder = new StringBuilder();
		for(Review review: ReviewManager.getInstance().getReviews().values())
			builder.append("§b" + review.getId().getId() + "§7,");
		if(builder.length() == 0)
			return "§7None";
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}
	
}
