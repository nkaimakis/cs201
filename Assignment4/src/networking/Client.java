package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import messages.Entry;
import messages.NewGameRequest;
import messages.PotentialGameName;
import messages.PotentialGameToJoin;
import messages.PotentialUsername;

public class Client extends Thread{
	public int port;
	public String game;
	public String username;
	public String lastGuess;
	boolean newGame; //also signifies host user
	boolean alive;
	boolean gameOver;
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Scanner scan;
	
	public Client(String ipaddress, int port, Scanner scan) throws IOException{
		this.scan = scan;
		this.alive = true;
		this.gameOver = false;
		
		//throws ioexception to main
		this.s = new Socket(ipaddress, port);
		this.oos = new ObjectOutputStream(this.s.getOutputStream());
		this.ois = new ObjectInputStream (this.s.getInputStream());
		System.out.println("Congrats! You have successfully connected to the Cineman server!");
		
		this.setupGame();
		this.start();
		
		//send messages from the client to the ClientThread to the Server
		//i.e. recognize and send guesses here
		try{
			while(!this.gameOver){
				this.lastGuess = scan.nextLine();
				if(this.alive){
					if(!this.lastGuess.equals("")){
						Entry entry = new Entry(this.lastGuess, this.username);
						this.oos.writeObject(entry);
						this.oos.flush();
					}
				}else{
					System.out.println("You have been eliminated. You may"
							+ " only spectate for the rest of the game.");
				}
			}
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} finally {
			try {
				if (s != null) {
					s.close();
				}
				if (scan != null) {
					scan.close();
				}
			} catch (IOException ioe) {
				System.out.println("ioe: " + ioe.getMessage());
			}
		}
	}
	
	public void setupGame(){
		int choice;

		while(true){
			System.out.println("Please choose from the following:");
			System.out.println("1. Start a game");
			System.out.println("2. Join a game");
		    try {
		        choice = Integer.parseInt(scan.nextLine());
		        if(choice < 1 || choice > 2){
		        	NumberFormatException ne = new NumberFormatException();
		        	throw ne;
		        }
		        break;
		    } catch (NumberFormatException ignore) {
		        System.out.print("Invalid command. ");
		    }
		}
		
		//create game
		if(choice == 1){
			this.createGame();
		}
		//join game
		else if(choice == 2){
			this.joinGame();
		}
	}
	
	public void createGame(){
		String nameOfGame;
		boolean taken = true;
		int numPlayers;
		NewGameRequest ngr = null;
		
		System.out.println("Please enter your username");
		this.username = scan.nextLine();
		
		while(true){
			System.out.println("Please enter a unique name for your game");
			nameOfGame = this.scan.nextLine();
			//check validity of game name by sending to server
			PotentialGameName pgn = new PotentialGameName(nameOfGame);
			try {
				this.oos.writeObject(pgn);
				this.oos.flush();
				
				try {
					//returns false if name is available
					taken = (Boolean)this.ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Class returned by server was not a "
							+ "bool: " + e.getMessage());
				}
				if(!taken){
					break; //name is available, continue
				}
				else{
					System.out.print("This name has already been taken by "
							+ "another game. ");
				}
			} catch (IOException e) {
				System.out.println("Error writing object to server: " 
						+ e.getMessage());
			}
		}
		
		while(true){
			System.out.println("Please enter the number of players (1-4) in "
					+ "this game");
		    try {
		    	while(!scan.hasNextInt()){
		    		//wait until an integer is entered
		    		scan.nextLine();
		    	}
		        numPlayers = scan.nextInt();
		        if(numPlayers < 1 || numPlayers > 4){
		        	NumberFormatException ne = new NumberFormatException();
		        	throw ne;
		        }
		        break; // will only get to here if input was valid
		    } catch (NumberFormatException ignore) {
		        System.out.print("Invalid number of players. ");
		    }
		}
		
		// send request to server to create new game
		ngr =  new NewGameRequest(this.username, nameOfGame, numPlayers);
		this.game = nameOfGame;
		
		//add game to server
		try {
			oos.writeObject(ngr);
			oos.flush();
		} catch (IOException e) {
			System.out.println("Error sending NewGameRequest in Client: " 
					+ e.getMessage());
		}
		
		//wait for other players
		//else print message sent
		String message;
		for(int i = 0; i < numPlayers; i++){
			//receive message for when each player joins
			while(true){
				//add game to server
				try {
					message = (String) ois.readObject();
					System.out.println(message);
					break;
				} catch (IOException e) {
					System.out.println("Error receiving joinGameMessage in Client: " 
							+ e.getMessage());
				} catch (ClassNotFoundException e) {
					System.out.println("cnfe in client CreateGame: " 
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		}

		
		//print final message with all in game
		try {
			message = (String) ois.readObject();
			System.out.println(message);
		} catch (IOException e) {
			System.out.println("Error receiving joinGameMessage in Client: " 
					+ e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("cnfe in client CreateGame: " 
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void joinGame(){
		String nameOfGame, potentialUsername;
		boolean canJoin = false, nameValid = false;
		this.newGame = false;
		PotentialGameToJoin gtj = null;
		PotentialUsername pu = null;

		//loop until valid game joined
		while(true){
			System.out.println("Please enter the name of the game you "
					+ "wish to join");
			nameOfGame = this.scan.nextLine();
			System.out.println("Name entered: "+nameOfGame);
			//check validity of game name by sending to server
			gtj = new PotentialGameToJoin(nameOfGame);
			try {
				this.oos.writeObject(gtj);
				this.oos.flush();
				
				try {
					//returns false if name is available
					canJoin = (Boolean)this.ois.readObject();
				} catch (ClassNotFoundException e) {
					System.out.println("Class returned by server was not a "
							+ "bool: " + e.getMessage());
				}
				if(canJoin){
					break; //game exists, continue
				}
				else{
					System.out.print("This game does not exist or has already "
							+ "reached the max number of players.\n");
				}
			} catch (IOException e) {
				System.out.println("Error writing object to server: " 
						+ e.getMessage());
			}
		}
		
		System.out.println("Congratulations! You have joined " + nameOfGame);
		
		//loop until valid username given
		while(true){
			System.out.println("Please enter your username");
			potentialUsername = this.scan.nextLine();
			//check validity of game name by sending to server
			pu = new PotentialUsername(potentialUsername, nameOfGame);
			try {
				this.oos.writeObject(pu);
				this.oos.flush();
				
				try {
					//returns false if name is available
					nameValid = (Boolean)this.ois.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("Class returned by server was not a "
							+ "bool: " + e.getMessage());
				}
				if(nameValid){
					this.username = potentialUsername;
					break; //name valid, continue
				}
				else{
					System.out.print("This username has already been taken"
							+ " by one of the other players. ");
				}
			} catch (IOException e) {
				System.out.println("Error writing object to server: " 
						+ e.getMessage());
			}
		}
	
		System.out.println("Waiting for all players to join..");
		try {
			String message = (String) ois.readObject();
			System.out.println(message);
		} catch (IOException e) {
			System.out.println("Error receiving joinGameMessage in Client: " 
					+ e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("cnfe in client CreateGame: " 
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void run(){
		//here receive updates from clientthread
		try{
			//prints UI then request for entry on first time
			String message = (String)ois.readObject();
			System.out.println(message);
			System.out.println(this.username + ", please enter a character"
					+ " or phrase as a guess.");
			
			while(true){
				message = (String)ois.readObject();
				//if lost game, print message and set alive to false
				//continue to print updates about game though
				if(message.equals("Your hangman has been completed. You lose.")){
					System.out.println(message);
					this.alive = false;
				}
				else if(message.equals("GAME OVER. HAVE A PLEASANT DAY.")){
					System.out.println(message);
					this.gameOver = true;
					break;
				}
				else{
					System.out.println(message);
				}
			}
		}catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
	
	public static void main (String[] args){
	    int port;
	    String ipaddress;
	    
		Scanner scan = new Scanner(System.in);
	    System.out.println("Welcome to Cineman!");
	    
		while(true){
		    System.out.println("Please enter the ipaddress");
		    ipaddress = scan.nextLine();
		    
			System.out.println("Please enter the port to host the client");
		    try {
		        port = Integer.parseInt(scan.nextLine());
		        if(port < 1024 || port > 49151){
		        	NumberFormatException ne = new NumberFormatException();
		        	throw ne;
		        }
		   
		        new Client(ipaddress, port, scan);
		        break; // will only get to here if input was valid
		    } catch (NumberFormatException ignore) {
		        System.out.print("Invalid port number. ");
		    }catch (Exception e){
		    	System.out.print("Invalid ipa/port combination. ");
		    	System.out.println(e.getMessage());
		    }
		}
		
	}
	
	public void printMessage(String Message){
		System.out.println(Message);
	}
	
	
	public boolean isHost(){
		return newGame;
	}
}
