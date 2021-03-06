package data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
	private String username;
	private String fname;
	private String lname;
	private String password;
	private String image;
	private List<Event> feed;
	private Set<String> following;
	private Set<String> followers;

	public User() {
		following = new HashSet<>();
		followers = new HashSet<>();
		feed = new ArrayList<>();
	}

	//GETTERS
	public String getUsername() {
		return username;
	}
	
	public String getFName() {
		return fname;
	}
	
	public String getLName() {
		return lname;
	}

	public String getPassword() {
		return password;
	}

	public List<Event> getFeed() {
		return feed;
	}

	public Set<String> getFollowing() {
		return following;
	}
	
	public Set<String> getFollowers(){
		return followers;
	}
	
	//SETTERS
	public void setFName(String fname){
		this.fname = fname;
	}
	
	public void setLName(String lname){
		this.lname = lname;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFeed(List<Event> feed) {
		this.feed = feed;
	}

	public void setFollowing(Set<String> following) {
		this.following = following;
	}

	public void setFollowers(Set<String> set) {
		this.followers = set;
	}
	
	//METHODS
	public void addFollower(String username){
		followers.add(username);
	}
	
	public void addFollowing(String username){
		following.add(username);
	}
	
	public void addEvent(Event event){
		feed.add(event);
	}
	
	//toString()s the user's profile
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("Name: "+fname+" "+lname);
		sb.append(System.lineSeparator()+"Username: "+username+System.lineSeparator());
		sb.append("Password: ");
		
		for (int i = 0; i<password.length(); i++){
			if (i == 0 || i == password.length()-1) sb.append(password.charAt(i));
			else sb.append("*");
		}
		
		sb.append(System.lineSeparator()+System.lineSeparator());
		sb.append("Following: "+System.lineSeparator());
		
		for (String user : following) sb.append(user+System.lineSeparator());
		
		sb.append(System.lineSeparator()+"Followers: "+System.lineSeparator());
		
		for (String user : followers) sb.append(user + System.lineSeparator());
		return sb.toString();
	}
	
	//toString()s the user's feed
	public String toStringFeed(){
		StringBuilder sb = new StringBuilder();
		
		for (Event event: feed){
			sb.append(event.getUsername());
			sb.append(" "+event.getAction().toLowerCase());
			sb.append(" '"+event.getMovieTitle()+"'");
			if (event.getRating() != -1) sb.append(" with a rating of "+event.getRating());
			sb.append(System.lineSeparator());
		}
		
		return sb.toString();
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
