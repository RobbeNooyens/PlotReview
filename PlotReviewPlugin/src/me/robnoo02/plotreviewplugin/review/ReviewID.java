package me.robnoo02.plotreviewplugin.review;

public final class ReviewID {

	private static int count;
	private final int intId;
	
	private ReviewID() {
		intId = count++;
	}
	
	public static ReviewID generateID() {
		return new ReviewID();
	}
	
	public int getId() {
		return intId;
	}
	
	
}
