package networking;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.Game;
import messages.Entry;
import messages.NewGameRequest;
import messages.PotentialGameName;
import messages.PotentialGameToJoin;
import messages.PotentialUsername;

public class ClientThread extends Thread {
	public String username;
	public int incorrectGuesses;
	public String lettersGuessed;
	public boolean alive;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Server serv;
	private Game game;
	
	public ClientThread(Socket s, Server serv) {
		this.incorrectGuesses = 0;
		this.lettersGuessed = "";
		this.alive = true;
		try {
			this.serv = serv;
			this.oos = new ObjectOutputStream(s.getOutputStream());
			this.ois = new ObjectInputStream(s.getInputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}	
	
	public void run() {
		try {
			while(true) {
				 Object received = this.ois.readObject();
				 if(received instanceof PotentialGameName){
					 //send name to server function. returns false if name is available
					 //sends boolean
					 oos.writeObject(serv.gameTaken(((PotentialGameName)received).name));
					 this.oos.flush();
				 }else if(received instanceof PotentialGameToJoin){
					 String name = ((PotentialGameToJoin)received).name;
					 //if game exists
					 if(this.serv.gameTaken(name)){
						 //if game is not full
						 if(!this.serv.gameFull(name))
							 this.oos.writeObject(true); 
						 else
							 this.oos.writeObject(false);
					 }else{//if game is not taken return false
						 this.oos.writeObject(false);
					 }
					 this.oos.flush();
				 }else if(received instanceof PotentialUsername){
					 String game = ((PotentialUsername)received).game;
					 String un = ((PotentialUsername)received).username;
					 Game g = serv.getGame(game);
					 if(g.usernameValid(un)){
						 this.username = un;
						 
						 //send confirmation to Client
						 this.oos.writeObject(true);
						 
						 //add player to game
						 g.addPlayer(this);
						 this.game = g;
					 }
					 else
						 this.oos.writeObject(false);
					 this.oos.flush();
				 }else if(received instanceof NewGameRequest){
					 NewGameRequest ngr = ((NewGameRequest)received);
					 String phrase = serv.ds.getPhrase();
					 this.username = ngr.username;
					 Game g = new Game(ngr.name, ngr.players, phrase, this);
					 this.game = g;
					 serv.addGame(g);
					 g.addPlayer(this);
				 }else if(received instanceof Entry){
					 //handle guess from player
					 Entry e = (Entry)received;
					 handleEntry(e);
					 if(this.game.won){
						 //removes game from map so it can be used again
						 this.serv.nameToGame.remove(this.game.name);
						 break;
					 }
				 }
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe in clientThread run: " + cnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("ioe in clientThread run: " + ioe.getMessage());
		}
	}
	
	private void handleEntry(Entry guess) {
		//send guess to game and get updated UI
		boolean guessSuccessful = this.game.guessSuccessful(guess.guess, this);
		if(guessSuccessful){
			this.sendMessage("You guessed '" + guess.guess + 
					"' and you were right(surprisingly)");
			//update game and send update and UI as String to all clients
			game.handleEntry(guess.guess, guess.player);
		}
		else{
			this.sendMessage("You guessed '" + guess.guess + "' and it's a "
					+ "good thing your mom isn't here to watch you fail:(");
			this.incorrectGuesses++;

			
			if(this.incorrectGuesses >= 8){
				//send losing message to losers
				this.sendMessage("Your hangman has been completed. You lose.");
				this.alive = false;
				
				//update game and send update and UI as String to all clients
				game.handleEntry(guess.guess, guess.player);
				
				//send losing message to all others
				String message = this.username +" completed his/her hangman. "
						+ "He/she has been eliminated :/";
				this.game.sendMessageToAllButOne(message, this);
			}
			else{
				//update game and send update and UI as String to all clients
				game.handleEntry(guess.guess, guess.player);
			}
		}
	}
	
	public void sendMessage(String message){
		try {
			this.oos.writeObject(message);
			this.oos.flush();
		} catch (IOException e) {
			System.out.println("ioe in ct " + 
					this.username + " " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
}
