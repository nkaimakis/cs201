package movieMedia;

public class Event {
	String action;
	String movie;
	double rating;

	public Event(String nAction, String nMovie, String nRating) {
		action = nAction;
		movie = nMovie;
		rating = Double.parseDouble(nRating);
	}

	public Event(String nAction, String nMovie) {
		action = nAction;
		movie = nMovie;
		rating = -1.0;
	}

	public void setRating(String nRating) {
		rating = Double.parseDouble(nRating);
	}

	public String getAction() {
		return this.action;
	}

	public String getMovie() {
		return this.movie;
	}

	public double getRating() {
		return this.rating;
	}
}
