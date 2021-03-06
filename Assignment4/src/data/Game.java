package data;


import java.util.Vector;

import networking.ClientThread;

public class Game {
	public Vector<ClientThread> clientThreads;
	public ClientThread host;
	public int totalPlayers;
	public int currentPlayers;
	public String name;
	public String phrase;
	public String incorrectGuesses;
	public String correctGuesses;
	public boolean won;
	public boolean winByDefault;
	public ClientThread winner;
	
	
	public Game(String name, int totalPlayers, String phrase, ClientThread maker){
		this.phrase = phrase;
		this.incorrectGuesses = "";
		this.correctGuesses = "";
		this.won = false;
		this.winByDefault = false;
		this.name = name;
		this.totalPlayers = totalPlayers;
		this.currentPlayers = 0;
		this.clientThreads = new Vector<ClientThread>();
		//host is stored in the vector and individually
		this.host = maker;
	}

	public boolean guessSuccessful(String guess, ClientThread ct){
		if(guess.length() == 1){//check if letter is in phrase
			if(guess.equals(" ") || this.correctGuesses.contains(guess)){
				return false;
			}
			else if(this.phrase.toLowerCase().contains(guess.toLowerCase())){
				this.correctGuesses += guess.toLowerCase();
				return true;
			}
			else{
				//if it has not been guessed before, add to incorrectGuesses
				if(!this.incorrectGuesses.contains(guess))
					this.incorrectGuesses += guess.toLowerCase();
				return false;
			}
		}
		else{ //check if phrase is an exact match
			if(this.phrase.toLowerCase().equals(guess)){
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	public ClientThread getClientThreadByName(String name){
		for(ClientThread ct: this.clientThreads){
			if(ct.username.equals(name)){
				return ct;
			}
		}
		return null;
	}
	
	//only called if the guess is confirmed
	//updates string and sends new UI to all clients
	public void handleEntry(String guess, String player){
		ClientThread guesser = getClientThreadByName(player);
		sendMessageToAllButOne(player + " guessed " + guess, guesser);
		
		if(guess.length() == 1){ //player guessed one letter
			if(phraseGuessed()){
				this.won = true;
			}
		}
		else{ //player guessed the correct phrase and WON
			if(guess.toLowerCase().equals(this.phrase.toLowerCase())){
				this.won = true;
			}
		}
		
		//check if game is won
		//make sure there isn't one player left
		//if only one player, don't set winner
		if(this.clientThreads.size() != 1){
			lastManStanding();
		}
		if(won){
			//if not win by default, current player is winner
			if(!this.winByDefault)
				this.winner = getClientThreadByName(player);
			sendUIToAll();
			sendEndGameMessage();
		}
		else{//game not won
			sendUIToAll();
			sendGuessPromptToAll();
		}
	}
	
	public ClientThread getHost(){
		return host;
	}
	
	public boolean isFull(){
		if(currentPlayers >= totalPlayers){
			return true;
		}
		else
			return false;
	}
	
	public boolean usernameValid(String un){
		for(ClientThread c: this.clientThreads){
			if(un.equals(c.username))
				return false;
		}
		return true;
	}
	
	
	public void addPlayer(ClientThread c){
		this.clientThreads.add(c);
		this.currentPlayers++;
		int leftToJoin = (this.totalPlayers - this.currentPlayers);
		String jgm;
		
		if(c.username.equals(this.host.username)){
			jgm = "You have started the game.";
			c.sendMessage(jgm);
		}
		
		//if full, print message to all players' consoles 
		//print different message to host
		if(this.isFull()){
			String allPlayers = "";
			for(ClientThread cl: this.clientThreads){
				allPlayers += cl.username + ", ";
			}
			//remove last comma in allPlayers
			//for substring, first index is inclusive, last is exclusive
			allPlayers = allPlayers.substring(0, allPlayers.length()-2);
			sendMessageToAll("All players have joined: " + allPlayers);
			sendUIToAll();

		}else if(leftToJoin == 1){
			if(c.username.equals(this.host.username)){
				jgm = "You have started the game. "
					+ "Waiting for 1 player to join...";
				c.sendMessage(jgm);
			}
			else{
				jgm = c.username + " has joined the game. " 
						+ "Waiting for " + Integer.toString(leftToJoin)
						+ " players to join...";
				this.host.sendMessage(jgm);
			}
		}else{
			if(c.username.equals(this.host.username)){
				String message = ("Waiting for " 
						+ Integer.toString(leftToJoin)
						+ " players to join...");
				this.host.sendMessage(message);
			}
			else{
				jgm = c.username + " has joined the game. " 
						+ "Waiting for " + Integer.toString(leftToJoin)
						+ " players to join...";
				this.host.sendMessage(jgm);
			}
			
		}
	}
	
	public boolean phraseGuessed(){
		for(int i = 0; i < this.phrase.length(); i++){
			//if the letter is not a space, check if guesses contains it
			//if not, return false
			if(this.phrase.charAt(i) != ' '){
				String s = "" + this.phrase.charAt(i);
				if(!this.correctGuesses.contains(s.toLowerCase())){
					return false;
				}
			}
		}
		//if all letters are within correctGuesses, return true
		return true;
	}
	
	public void lastManStanding(){
		//returns true if this player is the only one left
		int count = 0;
		ClientThread possibleWinner = null;
		for(ClientThread ct: this.clientThreads){
			if(ct.alive){
				count++;
				possibleWinner = ct;
			}
		}
		if(count > 1){
			return;
		}
		else{
			this.winByDefault = true;
			this.winner = possibleWinner;
			this.won = true;
			return;
		}
	}
	
	//send updated UI to all clients
	public void sendUIToAll(){
		for(ClientThread ct: this.clientThreads){
			ct.sendMessage(getUI(ct));
		}
	}
	
	public void sendGuessPromptToAll(){
		for(ClientThread ct: this.clientThreads){
			if(ct.alive)
				ct.sendMessage(ct.username + ", please enter a character "
					+ "or phrase as a guess.");
		}
	}
	
	//send message to all clients
	public void sendMessageToAll(String message){
		for(ClientThread ct: this.clientThreads){
			ct.sendMessage(message);
		}
	}
	//send message to all clients except given
	public void sendMessageToAllButOne(String message, ClientThread notThisOne){
		for(ClientThread ct: this.clientThreads){
			if(notThisOne.equals(ct)){
				//do nothing
			}else{
				ct.sendMessage(message);
			}
		}
	}
	
	public void sendEndGameMessage(){
		for(ClientThread ct: this.clientThreads){
			if(this.winner.equals(ct)){
				if(this.winByDefault){
					ct.sendMessage("All other players have been eliminated. "
							+ "You win by default!");
				}
				else{
					ct.sendMessage("You guessed the last character of the "
							+ "phrase. You win!");
				}
			}else{
				//create string of all other names
				String toPrint = "You and ";
				for(ClientThread c: this.clientThreads){
					if(c.equals(this.winner) || c.equals(ct)){
						//dont add to string
					}
					else{
						toPrint += c.username + " and ";
					}
				}
				toPrint = toPrint.substring(0, toPrint.length()-5) + " lose.";
				if(this.winByDefault){
					ct.sendMessage(this.winner.username + " won by default. "
							+ toPrint);
				}
				else{
					ct.sendMessage(this.winner.username + " guessed the last"
						+ " letter of the phrase. " + toPrint);
				}
			}
			ct.sendMessage("GAME OVER. HAVE A PLEASANT DAY.");
		}
	}
	
	public String getUI(ClientThread ct){//ClientThread ct
		String UI = "";
		String lineOne = "+----------------------------------------------------------------------------+\n";
		String lineTwo = "|  _____                                                                     |\n";
		String lineThree = "|  |                                                                         |\n";
		String lineFour = "|  |                               Cineman                                   |\n";
		String lineFive = "|  |                                                                         |\n";
		String lineSix = "|  |                                                                         |\n";
		String lineSeven = "|  |                                                                         |\n";
		String lineEight = "| _|_____                                                                    |\n";
		String lineNine = "|                                                                            |\n";
		String lineTen = "+----------------------------------------------------------------------------+\n";

		int i = ct.incorrectGuesses;
		// head, torso, left arm, right arm, left leg, right leg, noose
		String phraseSpaced = "";
		for(int j = 0; j < this.phrase.length()-1; j++){
			if(this.won || this.correctGuesses.contains(this.phrase.substring(j,j+1).toLowerCase())){
				phraseSpaced += this.phrase.substring(j, j+1) + " ";
			}
			else{
				if(this.phrase.charAt(j) == ' '){
					phraseSpaced += " ";
				}
				else{
					phraseSpaced += "_"; 
				}
				phraseSpaced += " ";
			}
		}
		//handle last letter in phrase
		if(this.won || this.correctGuesses.contains(this.phrase.substring(this.phrase.length()-1).toLowerCase())){
			phraseSpaced += this.phrase.substring(this.phrase.length()-1);
		}
		else{
			phraseSpaced += "_";
		}
		int endIndexWord = 14 + phraseSpaced.length();
		lineSix = lineSix.substring(0,14) + phraseSpaced + lineSix.substring(endIndexWord);
		
		//update line with incorrect guesses
		endIndexWord = 3 + this.incorrectGuesses.length();
		lineNine = lineNine.substring(0, 3) + this.incorrectGuesses + lineNine.substring(endIndexWord);
		
		if(i >=1){
			lineFour = lineFour.substring(0, 7) + "@" + lineFour.substring(8);
		}
		if(i>=2){
			lineFive = lineFive.substring(0, 7) + "|" + lineFive.substring(8);
		}
		if(i>=3){
			lineSix = lineSix.substring(0, 7) + "|" + lineSix.substring(8);
		}
		if(i>=4){
			lineFive = lineFive.substring(0, 6) + "/" + lineFive.substring(7);
		}
		if(i>=5){
			lineFive = lineFive.substring(0, 8) + "\\" + lineFive.substring(9);
		}
		if(i>=6){
			lineSeven = lineSeven.substring(0, 6) + "/" + lineSeven.substring(7);
		}
		if(i>=7){
			lineSeven = lineSeven.substring(0, 8) + "\\" + lineSeven.substring(9);
		}
		if(i>=8){
			lineThree = lineThree.subSequence(0, 7) + "|" + lineThree.substring(8);
		}
		
		UI = lineOne + lineTwo + lineThree + lineFour + lineFive + lineSix + 
				lineSeven + lineEight + lineNine + lineTen;
		return UI;
	}
	
}
