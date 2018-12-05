package comprehensive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class RandomPhraseGenerator {

	static File grammarIn;
	static int numPhrases;
	StringBuilder builder;

	public static void main(String[] args) {
		grammarIn = new File(args[0]);
		try {
			numPhrases = Integer.parseInt(args[1]);
		} catch (Exception e) {
			System.out.println("invalid input");
			return;
		}

		// Read the grammar
		Grammar myGrammar = new Grammar(grammarIn);

		// Pick a start and fill in the non-terminals
		for (int i = 0; i < numPhrases; i++) {

			System.out.println(myGrammar.generatePhrase());
			System.out.println();
		}
		// String phrase = myGrammar.GetWordByKey("start");

		// System.out.println(phrase);

	}
}

/**
 * This class represents a grammar file as an object for ease of use.
 * 
 * @author chris
 *
 */
class Grammar {
	static Scanner scn;
	Random rand = new Random();

	HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

	/**
	 * This constructor constructs a grammar object based on the file input and
	 * represents it with a hash map.
	 * 
	 * @param File input
	 */
	public Grammar(File input) {
		try {
			scn = new Scanner(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String current;

		while (scn.hasNext()) {
			StringBuilder builder = new StringBuilder();
			current = scn.next();
			if (current.equals("{")) {

				current = scn.next();

				while (current.charAt(0) != '<' && current.charAt(current.length() - 1) != '>') {

					current = scn.next();
				}

				// System.out.println("found a non terminal title.");
				builder.append(current.substring(1, current.length() - 1));

				map.put(builder.toString(), new ArrayList<String>());
				String item = scn.nextLine();
				item = scn.nextLine();

				while (!item.equals("}")) {
					map.get(builder.toString()).add(item);
					// System.out.println(item + " added to " + builder.toString());
					item = scn.nextLine();
				}

			}
		}

	}

	/**
	 * This method uses the values saved in the grammar hash map to construct one random phrase.
	 * @return String phrase
	 */
	public String generatePhrase() {
		StringBuilder builder = new StringBuilder();
		boolean finished = false;
		String phrase = this.GetWordByKey("start");
		scn = new Scanner(phrase);
		String current;
		while (!finished) {
			finished = true;
			if (scn.hasNext()) {

				while (scn.hasNext()) {
					current = scn.next();
					if (current.charAt(0) == '<' && current.charAt(current.length() - 1) == '>') {//detect non-terminals with no punctuation
						finished = false;
						builder.append(this.GetWordByKey(current.substring(1, current.length() - 1)));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					//check for non terminals with punctuation at the end, both ends (like quotation marks), and at the beginning.
					else if (current.charAt(0) == '<' && current.charAt(current.length() - 2) == '>') {//find punctuation at the end
						finished = false;
						builder.append(this.GetWordByKey(current.substring(1, current.length() - 2)));
						builder.append(current.charAt(current.length() - 1));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					//find punctuation at the ends
					else if (current.length() >= 4 && current.charAt(1) == '<' && current.charAt(current.length() - 2) == '>') {
						finished = false;
						builder.append(current.charAt(0));
						builder.append(this.GetWordByKey(current.substring(2, current.length() - 2)));
						builder.append(current.charAt(current.length() - 1));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					//find punctuation at the start
					else if (current.length() >= 3 && current.charAt(1) == '<' && current.charAt(current.length() - 1) == '>') {
						finished = false;
						builder.append(current.charAt(0));
						builder.append(this.GetWordByKey(current.substring(2, current.length() - 1)));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					//find double punctuation at the end
					else if (current.length() >= 6 && current.charAt(0) == '<' && current.charAt(current.length() - 3) == '>') {
						finished = false;
						builder.append(this.GetWordByKey(current.substring(1, current.length() - 3)));
						builder.append(current.charAt(current.length() - 2));
						builder.append(current.charAt(current.length() - 1));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					//find double punctuation at both ends
					else if (current.length() >= 6 && current.charAt(2) == '<' && current.charAt(current.length() - 3) == '>') {
						finished = false;
						builder.append(current.charAt(0));
						builder.append(current.charAt(1));
						builder.append(this.GetWordByKey(current.substring(3, current.length() - 3)));
						builder.append(current.charAt(current.length() - 2));
						builder.append(current.charAt(current.length() - 1));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					//find double punctuation at the beginning
					else if (current.length() >= 6 && current.charAt(2) == '<' && current.charAt(current.length() - 1) == '>') {
						finished = false;
						builder.append(current.charAt(0));
						builder.append(current.charAt(1));
						builder.append(this.GetWordByKey(current.substring(3, current.length() - 1)));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					else {
						builder.append(current);
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
				}
			}

			phrase = builder.toString();
			builder = new StringBuilder();
			scn = new Scanner(phrase);
		}
		//System.out.println("\n" + "Finished generating " + "phrase");
		return phrase;
	}

	/**
	 * This helper method randomly selects a word to replace any non-terminals we
	 * find in generatePhrase()
	 * 
	 * @param key
	 * @return String temp
	 */
	public String GetWordByKey(String key) {
		int range = map.get(key).size();
		String temp = map.get(key).get(Math.abs(rand.nextInt(range)));
		return temp;
	}

}