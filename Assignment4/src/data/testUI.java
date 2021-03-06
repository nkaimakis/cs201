package data;

public class testUI {
	public static void main(String[] args){
		String hidden = "";
		String phrase = "big";
		String correctGuesses = "big";
		String phraseSpaced = "";

		
//		int length = phrase.length();
//		for(int i = 0; i < length; i++){
//			if(phrase.charAt(i) == (' ')){
//				hidden += " ";
//			}
//			else{
//				hidden += "_";
//			}
//		}
		System.out.println(phraseSpaced);
		
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

		int i = 3;
		for(int j = 0; j < phrase.length()-1; j++){
			if(correctGuesses.contains(phrase.substring(j,j+1).toLowerCase())){
				phraseSpaced +=phrase.substring(j, j+1) + " ";
			}
			else{
				if(phrase.charAt(j) == ' '){
					phraseSpaced += " ";
				}
				else{
					phraseSpaced += "_"; 
				}
				phraseSpaced += " ";
			}
		}
		//handle last letter in phrase
		if(correctGuesses.contains(phrase.substring(phrase.length()-1))){
			phraseSpaced += phrase.substring(phrase.length() - 1);
		}
		else{
			phraseSpaced += "_";
		}
		
		int endIndexWord = 14 + phraseSpaced.length();
		lineSix = lineSix.substring(0,14) + phraseSpaced + lineSix.substring(endIndexWord);
		
		// head, torso, left arm, right arm, left leg, right leg, noose
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
		System.out.println(UI);
	}
}
