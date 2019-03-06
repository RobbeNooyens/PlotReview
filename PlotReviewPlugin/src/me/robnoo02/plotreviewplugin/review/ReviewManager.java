package me.robnoo02.plotreviewplugin.review;

import java.util.LinkedList;

public class ReviewManager {

	private static final ReviewManager INSTANCE = new ReviewManager();
	
	private final LinkedList<Review> reviews = new LinkedList<>();
	
	private ReviewManager() {
		
	}
	
	public LinkedList<Review> getReviews(){
		return reviews;
	}
	
	public static ReviewManager getInstance() {
		return INSTANCE;
	}
	
}
