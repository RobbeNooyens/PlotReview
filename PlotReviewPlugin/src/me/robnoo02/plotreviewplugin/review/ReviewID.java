package me.robnoo02.plotreviewplugin.review;

public final class ReviewID {

	private static int count;
	private final int intId;
	
	private ReviewID() {
		intId = count++;
	}
	
	private ReviewID(int id) {
		intId = id;
	}
	
	public static ReviewID generateID() {
		return new ReviewID();
	}
	
	public static ReviewID fromString(String id) {
		return new ReviewID(Integer.valueOf(id));
	}
	
	public static ReviewID fromInt(int id) {
		return new ReviewID(id);
	}
	
	public int getId() {
		return intId;
	}
	
	@Override 
	public boolean equals(Object o){
		if(!(o instanceof ReviewID))
			return false;
		ReviewID id = (ReviewID) o;
		return intId == id.getId();
	}
	
	@Override
	public int hashCode() {
		return intId;
	}
	
	
}
