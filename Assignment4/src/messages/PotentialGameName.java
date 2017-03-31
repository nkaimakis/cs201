package messages;

import java.io.Serializable;

public class PotentialGameName implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String name;
	
	public PotentialGameName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	
}
