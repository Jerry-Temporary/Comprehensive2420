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
		}
		// String phrase = myGrammar.GetWordByKey("start");

		// System.out.println(phrase);

	}
}

class Grammar {
	static Scanner scn;
	Random rand = new Random();

	HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

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

				// System.out.println("found a {");

				current = scn.next();

				// System.out.println(" " + current);

				// TODO need to switch to while loop to find the next valid element.
				while (current.charAt(0) != '<' && current.charAt(current.length() - 1) != '>') {

					current = scn.next();
				}

				// System.out.println("found a non terminal title.");
				builder.append(current.substring(1, current.length() - 1));

				// System.out.println(builder.toString());

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

	public String generatePhrase() {
		StringBuilder builder = new StringBuilder();
		boolean finished = false;
		String phrase = this.GetWordByKey("start");
		// System.out.println(phrase);
		scn = new Scanner(phrase);
		String current;
		while (!finished) {
			finished = true;
			if (scn.hasNext()) {

				while (scn.hasNext()) {
					current = scn.next();
					if (current.charAt(0) == '<' && current.charAt(current.length() - 1) == '>') {
						finished = false;
						builder.append(this.GetWordByKey(current.substring(1, current.length() - 1)));
						if (scn.hasNext()) {
							builder.append(" ");
						}
					} else {
						builder.append(current);
						if (scn.hasNext()) {
							builder.append(" ");
						}
					}
					// current = scn.next();
					// System.out.println(builder.toString());
				}
			}

			phrase = builder.toString();
			builder = new StringBuilder();
			scn = new Scanner(phrase);
		}
		System.out.println("\n" + "Finished generating " + "phrase");
		return phrase;
	}

	public String GetWordByKey(String key) {
		int range = map.get(key).size();
		// System.out.println(map.get(key).get(0) + " " + range);

		String temp = map.get(key).get(Math.abs(rand.nextInt(range)));
		return temp;
	}

}