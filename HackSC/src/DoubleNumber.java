import java.util.Scanner;

public class DoubleNumber {
	public int numToDouble;
	public String name;
	
	public DoubleNumber(String name, int num){
		this.numToDouble = num;
		this.name = name;
		System.out.println("Class created!");
		
		int numberDoubled = num *2;
		System.out.println("Number doubled: " + numberDoubled);
	}
	
	public static void main(String[] args){
		System.out.println("hello world");
		
		Scanner scan = new Scanner(System.in);
		System.out.println("What is your name?");
		
		String nameGiven = scan.nextLine();
		System.out.println("name entered: " + nameGiven);
		
		System.out.println("Enter the number you'd like to double");
		int numberGiven = scan.nextInt();
		System.out.println("num entered: " + numberGiven);
		
		new DoubleNumber(nameGiven, numberGiven);
	}
}
