package networking;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import data.CinemateException;
import data.DataStorage;
import data.Game;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.InputStreamReader;

public class Server{
	int serverPort;
	int clientPort;
	public String filepath;
	public String ipa;
	public DataStorage ds = null;
	public Vector<ClientThread> clientThreads = null;
	public Vector<Game> games = null;
	public Map<String, Game> nameToGame = null;
	private ServerSocket ss = null;
	private Scanner scan = null;

	
	
	public Server(int port, String filepath, Scanner scan) throws CinemateException {
		this.filepath = filepath;
		this.scan = scan;
		this.ds = new DataStorage(filepath);
		this.serverPort = port;
		this.clientThreads = new Vector<ClientThread>();
		this.games = new Vector<Game>();
		this.nameToGame = new HashMap<String, Game>();

		try {
			this.ss = new ServerSocket(this.serverPort);
			System.out.println("Server started!");
		} catch (IOException e) {
			System.out.println("Server construction exception: " + e.getMessage());
		}
		
		try{
			while(true){
				Socket s = null;
				s = ss.accept();
				System.out.println("Connection from " + s.getInetAddress());
				ClientThread ct = new ClientThread(s, this);
				clientThreads.add(ct);
			}
		}catch (IOException ioe){
			System.out.println("ioe in PlayerServer: "+ioe.getMessage());
		}finally{
			try {
				this.ss.close();
				this.scan.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error closing server socket in server: " + e.getMessage());
			}
		}

	}
	
	public void printCheck(String message){
		System.out.println("print check: "+ message);
	}
	
	public Game getGame(String name){
		return this.nameToGame.get(name);
	}
	
	public boolean gameTaken(String name){
		return nameToGame.containsKey(name);
	}
	
	public boolean gameFull(String name){
		Game g = nameToGame.get(name);
		return g.isFull();
	}
	
	public void addGame(Game g){
		games.add(g);
		nameToGame.put(g.name, g);
	}

	
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		Server mServer = null;
		
		int port;
		while(true){
			System.out.println("Please enter the port to host the server");
		    try {
		        port = Integer.parseInt(scan.next());
		        if(port < 1024 || port > 49151){
		        	NumberFormatException ne = new NumberFormatException();
		        	throw ne;
		        }
		        break; // will only get to here if input was valid
		    } catch (NumberFormatException ignore) {
		        System.out.print("Invalid port(server). ");
		    }
		}
		
		while(true){
			System.out.println("Please enter the filepath to the xml file of game phrases");
			try{
				///Users/Nick/Desktop/cs201/Assignment4/valid.xml
				String filepath = (scan.next());
				mServer = new Server(port, filepath, scan);
				break;
			}catch(CinemateException ce){
				System.out.println(ce.getMessage() + " ");
			}
		}	
	}
}
