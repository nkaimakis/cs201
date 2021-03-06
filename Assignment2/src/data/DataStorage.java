package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataStorage {
	//maps a username to a user object
	protected Map<String, User> usersMap;
	//maps a movie title to a movie object
	protected Map<String, Movie> moviesMap;
	//maps a case insensitive username to a list of case sensitive matches
	private Map<String, Set<User>> usernameToUsers;
	private Map<String, Set<User>> firstNameToUsers;
	private Map<String, Set<User>> lastNameToUsers;
	//maps a username to a list of followers
	private Map<String, Set<String>> usernameToFollowers;
	//maps an actor to a list of movies
	private Map<String, Set<Movie>> actorToMovies;
	//maps a genre to a list of movies
	private Map<String, Set<Movie>> genreToMovies;
	//maps a case insensitive movie title to a list of case sensitive matches
	private Map<String, Set<Movie>> titleToMovies;
	//genres
	private List<String> genresList;
	//actions
	private List<String> actionsList;

	private Document doc;
	//user currently logged in
	private User loggedInUser;
	private String filepath;
	private String parseError;
	
	public DataStorage(String filepath) throws CinemateException{
		this.filepath = filepath;
		usersMap = new HashMap<>();
		moviesMap = new HashMap<>();
		genresList = new ArrayList<>();
		actionsList = new ArrayList<>();
		usernameToUsers = new HashMap<>();
		firstNameToUsers = new HashMap<>();
		lastNameToUsers = new HashMap<>();
		usernameToFollowers = new HashMap<>();
		actorToMovies = new HashMap<>();
		genreToMovies = new HashMap<>();
		titleToMovies = new HashMap<>();
		parseError = null;
		

		try{
			//create the document object and parse it
			DocumentBuilder dBuilder= DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = dBuilder.parse(new File(filepath));
			parse();
			
			for (User user : usersMap.values()) {
				Set<String> followers = usernameToFollowers.get(user.getUsername());
				Set<String> following = user.getFollowing();
				
				//check that all usernames in following list are valid
				for (String follow : following){
					if (!usersMap.containsKey(follow)){
						throw new CinemateException("Invalid username in following list");
					}
				}
				
				if (followers != null) {
					
					user.setFollowers(followers);
				}
			}
		}
		catch (SAXException | IOException | ParserConfigurationException e){
			parseError = e.getMessage();
			throw new CinemateException(e.getMessage());
		}
		
	}
	public String getError(){
		return parseError;
	}
	
	//search methods
	public Set<Movie> searchByGenre(String genre){
		return genreToMovies.get(genre.toLowerCase());
	}
	
	public Set<Movie> searchByTitle(String title){
		return titleToMovies.get(title.toLowerCase());
	}
	
	public Set<Movie> searchByActor(String actor){
		return actorToMovies.get(actor.toLowerCase());
	}
	
	public User getUser(String usernanme){
		return usersMap.get(usernanme);
	}
	
	public void setLoggedInUser(String username){
		loggedInUser = usersMap.get(username);
	}
	
	public User getLoggedInUser(){
		return loggedInUser;
	}
	//check if a username is valid
	public Boolean validUsername(String username){
		return usersMap.containsKey(username);
	}
	//check if a password is correct
	public Boolean correctPassword(String username, String password){
		return usersMap.get(username).getPassword().equals(password);
	}
	//retrieve all users with matching username, fname or lname
	public Set<User> searchForUser(String username){
		Set<User> userSets = new HashSet<>();
		Set<User> usernames = usernameToUsers.get(username.toLowerCase());
		Set<User> fnames = firstNameToUsers.get(username.toLowerCase());
		Set<User> lnames = lastNameToUsers.get(username.toLowerCase());
		
		if (usernames != null) userSets.addAll(usernames);
		if(fnames != null) userSets.addAll(fnames);
		if (lnames != null) userSets.addAll(lnames);
		
		return userSets;
	}
	
	//takes a NodeList and either the list of actions or list of genres 
	private void addToGenresOrActions(List<String> toAddTo, NodeList startNode){
		//children of the parent tag (either <actions> or <genres>)
		NodeList children = startNode.item(0).getChildNodes();
		//iterate through the children and get their text content, add it to the list
		for (int i = 0; i<children.getLength(); i++){

			if (children.item(i).hasChildNodes()){

				toAddTo.add(children.item(i).getFirstChild().getTextContent());
			}
		}
	}
	
	private void parse() throws CinemateException{
		
		NodeList genresNodeList = doc.getElementsByTagName("genres");
		NodeList actionsNodeList = doc.getElementsByTagName("actions");
		NodeList moviesNodeList = doc.getElementsByTagName("movies");
		NodeList usersNodeList = doc.getElementsByTagName("users");
		//parsing actions and genres
		addToGenresOrActions(genresList, genresNodeList);
		addToGenresOrActions(actionsList, actionsNodeList);
		//parsing movies and users (parsing movies first so we will have the movie objects ready when parsing a user's feed)
		parseObjects(moviesNodeList, true);
		parseObjects(usersNodeList, false);
	}
	
	//the same logic is used for iterating through movie and user nodes. So, we pass in a boolean to determine the helper method to call
	private void parseObjects(NodeList rootNode, Boolean isMovie) throws CinemateException{
		NodeList children = rootNode.item(0).getChildNodes();
		
		int count = 0;
		
		for (int i = 0; i<children.getLength(); i++){
			Node node = children.item(i);
			//some of the elements show up as text elements, so we need this check before we choose to parse
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				count++;
				if (isMovie) parseMovie(node);
				else parseUser(node);
			}
		}
		//if we are parsing movies and there were none in the file, throw an exception
		if (isMovie && count == 0){
			throw new CinemateException("No movies found in file.");
		}
	}
	
	//PARSE MOVIES
	//parsing one movie object
	private void parseMovie(Node rootNode) throws CinemateException{
		//get the fields of the movie
		NodeList movieFields = rootNode.getChildNodes();
		Movie movie = new Movie();
		
		for (int i = 0; i<movieFields.getLength(); i++){
			//get the current field of the movie
			Node movieField = movieFields.item(i);
			String nodeName = movieField.getNodeName();
			
			switch (nodeName){
			
				case "director":
					movie.setDirector(movieField.getFirstChild().getTextContent());
					break;
				case "rating-total":
					if(movieField.getFirstChild() != null)
						movie.setTotalRatings(movieField.getFirstChild().getTextContent());
					break;
				case "rating-count":
					if(movieField.getFirstChild() != null)
						movie.setCountRatings(movieField.getFirstChild().getTextContent());
					break;
				case "image":
					movie.setImage(movieField.getFirstChild().getTextContent());
				case StringConstants.WRITERS:
					//parse the writers in a helper method
					parseMovieSubList(StringConstants.WRITERS, movieField, movie);
					break;
				case "year":
					//try to parse year, if we get NumberFormatException, we know we weren't given an integer
					try{
						movie.setYear(Integer.parseInt(movieField.getFirstChild().getTextContent()));
					}
					catch (NumberFormatException nfe){
						throw new CinemateException("Movie year is not an int value");
					}
					
					break;
				case StringConstants.ACTORS:
					//parse the actors in a helper method
					try{
						parseActorSubList(movieField, movie);
					}catch(Exception e){
						throw new CinemateException("Actors is not formatted correctly");
					}
					break;
				case StringConstants.GENRE:
					movie.setGenre(movieField.getFirstChild().getTextContent());
					
					if (!genresList.contains(movie.getGenre())){ throw new CinemateException("Movie object with an invalid genre found");}
					break;
				case StringConstants.TITLE:
					movie.setTitle(movieField.getFirstChild().getTextContent());
					break;
				case "description":
					movie.setDescription(movieField.getFirstChild().getTextContent());
					break;
				case StringConstants.RATING:
					//check to see if there is a non-empty rating valued
					try{
						//try to parse rating, if we get NumberFormatException, we know we weren't given a double
						if (movieField.getFirstChild() != null) {
							Double rating = Double.parseDouble(movieField.getFirstChild().getTextContent());
							movie.setRating(rating);
						}
					}
					catch (NumberFormatException nfe){
						throw new CinemateException("Movie rating is not a double value");
					}
					break;
			}
		}

		moviesMap.put(movie.getTitle(), movie);
		//add the movie to the correct genre's set
		addObjectToMap(genreToMovies, movie.getGenre().toLowerCase(), movie);
		//add the movie to the correct title's set
		addObjectToMap(titleToMovies, movie.getTitle().toLowerCase(), movie);
	}
	
	private void parseActorSubList(Node movieField, Movie movie) throws CinemateException{
		NodeList children = movieField.getChildNodes();
		
		for (int j = 0; j<children.getLength(); j++) {
			
			Node child = children.item(j);
			//if the current child is of the appropriate Node type
			if (child.getNodeType() == Node.ELEMENT_NODE) {
		        parseActor(child, movie);
		    }
		}
		

	}
	
	private void parseActor(Node rootNode, Movie movie) throws CinemateException{
		NodeList children = rootNode.getChildNodes();
		Actor actor = new Actor();
		
		for (int i = 0; i<children.getLength(); i++){
			Node child = children.item(i);
			String nodeName = child.getNodeName();
			
			switch(nodeName){
				case "fname":
					actor.setFname(child.getFirstChild().getTextContent());
					break;
				case "lname":
					actor.setLname(child.getFirstChild().getTextContent());
					break;
				case "image":
					actor.setImage(child.getFirstChild().getTextContent());
					break;
			}
		}
		movie.addActor(actor);
		addObjectToMap(actorToMovies, actor.getName().toLowerCase(), movie);
	}

	//used to parse actors and writers
	private void parseMovieSubList(String key, Node movieNode, Movie movie) throws CinemateException{
		NodeList children = movieNode.getChildNodes();
		
		for (int j = 0; j<children.getLength(); j++) {
			
			Node child = children.item(j);
			//if the current child is of the appropriate Node type
			if (child.getNodeType() == Node.ELEMENT_NODE) {
		       
		        String value = children.item(j).getFirstChild().getTextContent();
		        //if we are parsing the actors
				if (key.equals(StringConstants.ACTORS)){
					//add the actor to the movie, then add the movie to the map from actors to movies
					parseActorSubList(child, movie);
				}
				//if we are parsing the writers, add the value to the movie's list of writers
				else movie.addWriter(children.item(j).getFirstChild().getTextContent());
		    }
		}
		
	}
	
	//PARSE USERS
	//parse a user object
	private void parseUser(Node rootNode) throws CinemateException{
		NodeList children = rootNode.getChildNodes();
		User user = new User();
		
		for (int i = 0; i<children.getLength(); i++){
			Node child = children.item(i);
			String nodeName = child.getNodeName();
	
			switch(nodeName){
				case StringConstants.USERNAME:
					user.setUsername(child.getFirstChild().getTextContent());
					break;
				case "fname":
					user.setFName(child.getFirstChild().getTextContent());
					break;
				case "lname":
					user.setLName(child.getFirstChild().getTextContent());
					break;
				case "image":
					user.setImage(child.getFirstChild().getTextContent());
					break;
				case StringConstants.PASSWORD:
					user.setPassword(child.getFirstChild().getTextContent());
					break;
				//parse the following list
				case StringConstants.FOLLOWING:
					parseUserSubList(StringConstants.FOLLOWING, child, user);
					break;
				//parse the feed
				case StringConstants.FEED:
					parseUserSubList(StringConstants.FEED, child, user);
					break;
			}
		}
		//add the user to the users map
		usersMap.put(user.getUsername(), user);
		//add the user with their lowercased username to the usernameToUsers map
		addObjectToMap(usernameToUsers, user.getUsername().toLowerCase(), user);
		addObjectToMap(firstNameToUsers, user.getFName().toLowerCase(), user);
		addObjectToMap(lastNameToUsers, user.getLName().toLowerCase(), user);
	}
	
	//used to parse following and feed
	private void parseUserSubList(String key, Node userNode, User user) throws CinemateException{
		NodeList following = userNode.getChildNodes();
		
		for (int j = 0; j<following.getLength(); j++) {
			
			Node node = following.item(j);
			//if the current child is of the appropriate Node type
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				//if we are parsing the feed, parse this event
		        if (key.equals(StringConstants.FEED)) parseEvent(user, node);
		        //else add the username to the user's following set, then add the relationship to the usernameToFollowers map
		        else{
		        	String username = following.item(j).getFirstChild().getTextContent();
		        	user.addFollowing(username);
		        	addObjectToMap(usernameToFollowers, username, user.getUsername());
		        }
				
		    }
			
		}
	}
	
	//parse an event object
	private void parseEvent(User user, Node eventNode) throws CinemateException{
		NodeList eventFields = eventNode.getChildNodes();
		Event event = new Event();
		event.setUsername(user.getUsername());
		
		for (int i = 0; i<eventFields.getLength(); i++){
			Node eventField = eventFields.item(i);
			String nodeName = eventField.getNodeName();
			
			switch(nodeName){
				//set the movie object of the event.
				//this is why in parse() we made sure to parse the movies node list before the users node list: so this movie value wouldn't be null
				case "movie":
					event.setMovie(moviesMap.get(eventField.getFirstChild().getTextContent()));
					break;
				case StringConstants.ACTION:
					event.setAction(eventField.getFirstChild().getTextContent());
					//if we are given an invalid action
					if (!actionsList.contains(event.getAction())) { throw new CinemateException("Invalid action in an event in user's feed");}
					break;
				case StringConstants.RATING:
					try{
						
						if (eventField.getFirstChild() != null) {
							event.setRating(Double.parseDouble(eventField.getFirstChild().getTextContent()));
						}
					}
					catch (NumberFormatException nfe){
						throw new CinemateException("Event rating provided is not a double value");
					}
					
					break;
			}
		}
		//if the action is rated but there is no rating value
		if (event.getAction().equals("Rated") && (event.getRating()==-1)){
			throw new CinemateException("found an event with Rated action but no rating");
		}
		//if the action is not rated but we are given a rating value
		if (!event.getAction().equals("Rated") && (event.getRating() != -1)){
			throw new CinemateException("found an event without Rated action but with a non-empty rating value");
		}

		user.addEvent(event);
	}
	
	//SHARED HELPER METHODS
	
	//more generics!! Yay!!!! They are so helpful in times like these!!!
	//adds a new object to the appropriate set
	private <T> void addObjectToMap(Map<String, Set<T>> map, String key, T object){
		//if the map already contains the provided key, retrieve the value (which is a set) and add the new object
		if (map.containsKey(key)) map.get(key).add(object);
		//else create a new set with the object, and add the provided key and created set to the map
		else{
			Set<T> objects = new HashSet<>();
			objects.add(object);
			map.put(key, objects);
		}
	}
	
	public Movie getMovieByTitle(String title){
		Set<Movie> movieList = searchByTitle(title);
		for(Movie m: movieList){
			if(m.getTitle().equals(title))
				return m;
		}
		return null;
	}
	
	//write to XML
	public void updateMovieRating(Movie cMovie){
		try{
			NodeList moviesNodeList = doc.getElementsByTagName("movies");
			NodeList children = moviesNodeList.item(0).getChildNodes();
			boolean thisMovie = false;
			
			for (int i = 0; i<children.getLength(); i++){
				Node node = children.item(i);
				//some of the elements show up as text elements, so we need this check before we choose to parse
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					NodeList movieFields = node.getChildNodes();
					
					for (int j = 0; j<movieFields.getLength(); j++){
						//get the current field of the movie
						Node movieField = movieFields.item(j);
						//String nodeName = movieField.getNodeName();
						if(movieField.getFirstChild().getTextContent().equals(cMovie.getTitle())){
							thisMovie=true;
						}
						if(thisMovie && movieField.getFirstChild().getTextContent().equals("rating-total")){
							if(movieField.getNodeType() == Node.ELEMENT_NODE){
								Element e = (Element) movieField;
								e.setTextContent(Double.toString(cMovie.getTotalRatings()));
							}
						}
						if(thisMovie && movieField.getFirstChild().getTextContent().equals("rating-count")){
							if(movieField.getNodeType() == Node.ELEMENT_NODE){
								Element e = (Element) movieField;
								e.setTextContent(Integer.toString(cMovie.getCountRatings()));
							}
						}
						
					}
				}
			}
			
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult consoleResult = new StreamResult(filepath);
	        transformer.transform(source, consoleResult);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public void updateUserEvent(Event event, User user){
		
	}
	public void removeFollower(User toUnfollow, User follower){
		
	}
	public void addFollower(User toFollow, User follower){
		//add info to data structures
		toFollow.addFollower(follower.getUsername());
		follower.addFollowing(toFollow.getUsername());
		
		//add to xml file
		NodeList nl = doc.getElementsByTagName("user");
		for(int i=0; i<nl.getLength(); i++){
			Node userNode = nl.item(i);
			NodeList parsedUser = userNode.getChildNodes();
			boolean thisUser = false;
			
			for(int j=0; j<parsedUser.getLength(); j++){
				Node uNode = parsedUser.item(j);
				if (uNode.getNodeType() == Node.ELEMENT_NODE) {
					if(uNode.getNodeName().equals("username") 
							&& uNode.getTextContent().equals(follower.getUsername())){
						//if right user, change mark as right user
						thisUser = true;
					}
					else if(thisUser && uNode.getNodeName().equals("followers")){
						if(uNode.getNodeType() == Node.ELEMENT_NODE){
							Element e = doc.createElement("follower");
							e.setTextContent(toFollow.getUsername());
							uNode.appendChild(e);
						}
					}
				}
			}
		}
		//update xml file
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(filepath);
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
			System.out.println("file is saved");
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}