
///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            WordCloudGenerator
// Files:            ArrayHeap.java, BSTnode.java, BSTDictionary.java, 
//					 BSTDictionaryIterator.java, DictionaryADT.java,
//					 DuplicateException.java, Prioritizable.java, 
//                   PriorityQueueADT.java, WordCloudGenerator.java
// Semester:         CS 367 Fall 2017
//
// Author:           Lucas Bannister
// Email:            lbannister@wisc.edu
// CS Login:         lbannister
// Lecturer's Name:  Charles Fischer
// Lab Section:      N/A
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   
// Persons:          N/A
//
// Online sources:   StackOverflow.com - general Java information
//					 Github.com - general Java information
//                   Oracle JavaDocs - general Java information
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.*;
import java.io.*;

/**
 * The WordCloudGenerator class generates a word cloud given the appropriate
 * inputs as described in the main class
 * 
 * @author Lucas Bannister
 *
 */
public class WordCloudGenerator {
	/**
	 * The main method generates a word cloud as described in the program write-up.
	 * 
	 * @param args
	 *            the command-line arguments that determine where input and output
	 *            is done:
	 *            <ul>
	 *            <li>args[0] is the name of the input file</li>
	 *            <li>args[1] is the name of the output file</li>
	 *            <li>args[2] is the name of the file containing the words to ignore
	 *            when generating the word cloud</li>
	 *            <li>args[3] is the maximum number of words to include in the word
	 *            cloud</li>
	 *            </ul>
	 */
	public static void main(String[] args) {
		Scanner in = null; // for input from text file
		PrintStream out = null; // for output to html file
		Scanner inIgnore = null; // for input from ignore file
		int maxWords = 0; // maximum number of words to include in the word cloud
		DictionaryADT<KeyWord> dictionary = new BSTDictionary<KeyWord>();

		// Check the command-line arguments and set up the input and output
		// Check whether there are exactly four command-line arguments, if not display
		// usage info
		if (args.length != 4) {
			System.err.println("Four arguments required: inputFileName outputFileName ignoreFileName maxWords");
			System.exit(1);
		}

		// Check whether input file exists and is readable
		try {
			// Initializes file
			File inputFile = new File(args[0]);
			// checks for existence and readability, displays error if failure
			if (!inputFile.exists() || !inputFile.canRead()) {
				System.err.println("Error: cannot access file " + args[0]);
				System.exit(1);
			}
			// initialize scanner
			in = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			System.err.println("Error: cannot access file " + args[0]);
			System.exit(1);
		}

		// Check whether input ignore file exists and is readable
		try {
			// Initializes file
			File ignoreFile = new File(args[2]);
			// checks for existence and readability, displays error if failure
			if (!ignoreFile.exists() || !ignoreFile.canRead()) {
				System.err.println("Error: cannot access file " + args[2]);
				System.exit(1);
			}
			// initialize scanner
			inIgnore = new Scanner(ignoreFile);
		} catch (FileNotFoundException e) {
			System.err.println("Error: cannot access file " + args[2]);
			System.exit(1);
		}

		// Parse the max words command line argument and make sure it's positive
		try {
			maxWords = Integer.parseInt(args[3]);
			if (maxWords <= 0) {
				System.err.println("Error: maxWords must be a positive integer");
				System.exit(1);
			}
		} catch (NumberFormatException e) {
			System.err.println("Error: maxWords must be a positive integer");
			System.exit(1);
		}

		// Check whether output file is accessible, and warn if overwriting
		try {
			// initialize file
			File outputFile = new File(args[1]);
			// if it exists, tell the user
			if (outputFile.exists()) {
				System.err.println("Warning: file " + args[1] + " already exists, will be overwritten");
			}
			// if output file exists and cannot be written to, tell user and quit
			if (outputFile.exists() && !outputFile.canWrite()) {
				System.err.println("Error: cannot write to file " + args[1]);
				System.exit(1);
			}
			// initialize printstream
			out = new PrintStream(outputFile);
		} catch (FileNotFoundException e) {
			// if we can't write for other reasons, tell user and quit.
			System.err.println("Error: cannot write to file " + args[1]);
			System.exit(1);
		}

		// Create the dictionary of words to ignore
		// You do not need to change this code.
		DictionaryADT<String> ignore = new BSTDictionary<String>();
		while (inIgnore.hasNext()) {
			try {
				ignore.insert(inIgnore.next().toLowerCase());
			} catch (DuplicateException e) {
				// if there is a duplicate, we'll just ignore it
			}
		}

		// Process the input file line by line
		// Note: the code below just prints out the words contained in each
		// line. You will need to replace that code with code to generate
		// the dictionary of KeyWords.
		while (in.hasNext()) {
			String line = in.nextLine();
			List<String> words = parseLine(line);

			// Iterate through all words on this line
			KeyWord keyword;
			// for each word in words
			for (String word : words) {
				// convert to lowercase
				word = word.toLowerCase();
				// check if it is on the ignore list, don't add it if it is
				if (ignore.lookup(word) == null) {
					try {
						// try to insert it into the dictionary
						// create KeyWord object
						keyword = new KeyWord(word);
						// add to dictionary
						dictionary.insert(keyword);
						// increment count of KeyWord
						keyword.increment();
					} catch (DuplicateException e) {
						// If the keyword is already in the dictionary, find it and increment count
						keyword = dictionary.lookup(new KeyWord(word));
						keyword.increment();
					}
				}
			}
		} // end while

		// Add dictionary to priority queue by iterating through dictionary
		PriorityQueueADT<KeyWord> priorityQueue = new ArrayHeap<KeyWord>();
		// for each keyword in the dictionary
		for (KeyWord keyword : dictionary) {
			// insert it into the queue
			priorityQueue.insert(keyword);
		}

		// Rebuild dictionary using the priority queue only up to the maximum number of
		// words
		dictionary = new BSTDictionary<KeyWord>();
		// only go up to the maximum number of words
		for (int i = 0; i < maxWords; i++) {
			// can't remove the max if there isn't one
			if (!priorityQueue.isEmpty()) {
				try {
					// remove the max from the queue and add to new dictionary
					dictionary.insert(priorityQueue.removeMax());
				} catch (DuplicateException e) {
					// shouldn't be possible to get here?
					System.err.println("Unable to add to dictionary");
				}
			} else {
				// maxWords was greater than the number of words in the queue
				break;
			}
		}

		// Print out information:
		// the number of keys in the dictionary
		int numKeys = dictionary.size();
		System.out.println("# keys: " + numKeys);
		// the average path length
		int avgPathLength = dictionary.totalPathLength() / numKeys;
		System.out.println("avg path length: " + avgPathLength);
		// the average path length if the structure were linear
		int linearPathLength = (1 + numKeys) / 2;
		System.out.println("linear avg path: " + linearPathLength);

		// generate the HTML to the output file
		generateHtml(dictionary, out);

		// Close everything
		if (in != null)
			in.close();
		if (inIgnore != null)
			inIgnore.close();
		if (out != null)
			out.close();
	}

	/**
	 * Parses the given line into an array of words.
	 * 
	 * @param line
	 *            a line of input to parse
	 * @return a list of words extracted from the line of input in the order they
	 *         appear in the line
	 * 
	 *         DO NOT CHANGE THIS METHOD.
	 */
	private static List<String> parseLine(String line) {
		String[] tokens = line.split("[ ]+");
		ArrayList<String> words = new ArrayList<String>();
		for (int i = 0; i < tokens.length; i++) { // for each word

			// find index of first digit/letter
			boolean done = false;
			int first = 0;
			String word = tokens[i];
			while (first < word.length() && !done) {
				if (Character.isDigit(word.charAt(first)) || Character.isLetter(word.charAt(first)))
					done = true;
				else
					first++;
			}

			// find index of last digit/letter
			int last = word.length() - 1;
			done = false;
			while (last > first && !done) {
				if (Character.isDigit(word.charAt(last)) || Character.isLetter(word.charAt(last)))
					done = true;
				else
					last--;
			}

			// trim from beginning and end of string so that is starts and
			// ends with a letter or digit
			word = word.substring(first, last + 1);

			// make sure there is at least one letter in the word
			done = false;
			first = 0;
			while (first < word.length() && !done)
				if (Character.isLetter(word.charAt(first)))
					done = true;
				else
					first++;
			if (done)
				words.add(word);
		}

		return words;
	}

	/**
	 * Generates an html file using the given list of words. The html file is
	 * printed to the provided PrintStream.
	 * 
	 * @param words
	 *            a list of KeyWords
	 * @param out
	 *            the PrintStream to print the html file to
	 * 
	 *            DO NOT CHANGE THIS METHOD
	 */
	private static void generateHtml(DictionaryADT<KeyWord> words, PrintStream out) {

		final boolean debug = true;// Print out debugging information?

		// Should the generated HTML be a simple alphabetic listing of the selected
		// words
		// or a more complex 2 dimensional display?
		// Use simpleDisplay = true for testing and debugging. It will generate an HTML
		// file with all words in alphbetic order with size & colour denoting word
		// frequence.
		// With simpleDisplay = false, a more attractive two dimensional cloud will be
		// generated
		// with popular words in the centre and less popular words at the edges. The
		// layout is
		// targeted toward an 8.5 by 11 format so that the cloud can be easily printed.

		final boolean simpleDisplay = true;

		String[] colors = {
				/*
				 * Old color defs "6F", "6A", "65", "60", "5F", "5A", "55", "50", "4F", "4A",
				 * "45", "40", "3F", "3A", "35", "30", "2F", "2A", "25", "20", "1F", "1A", "15",
				 * "10", "0F", "0A", "05", "00"
				 */
				"#CD5C5C", // INDIANRED
				"#5F9EA0", // CADETBLUE
				"#FA8072", // SALMON
				"#E9967A", // DARKSALMON
				"#FF69B4", // HOTPINK
				"#FFA500", // ORANGE
				"#B22222", // FIREBRICK
				"#E6E6FA", // LAVENDER
				"#8A2BE2", // BLUEVIOLET
				"#6A5ACD", // SLATEBLUE
				"#7FFF00", // CHARTREUSE
				"#32CD32", // LIMEGREEN
				"#228B22", // FORESTGREEN
				"#66CDAA", // MEDIUMAQUAMARINE
				"#00FFFF", // CYAN
				"#1E90FF", // DODGERBLUE
				"#FFE4C4", // BISQUE
				"#8B4513", // SADDLEBROWN
				"#F5F5DC", // BEIGE
				"#C0C0C0" // SILVER
		};
		int initFontSize = 100;
		String fontFamily = "Cursive";

		// Print the header information including the styles
		out.println("<head>\n<title>Word Cloud</title>");
		out.println("<style type=\"text/css\">");
		out.println("body { font-family: " + fontFamily + " }");

		// Each style is of the form:
		// .styleN {
		// font-size: X%;
		// color: #YYYYYY;
		// }
		// where N and X are integers and Y is a hexadecimal digit
		for (int i = 0; i < colors.length; i++)
			out.println(".style" + i + " {\n    font-size: " + (initFontSize + i * 20) + "%;\n    color: " + colors[i]
					+ ";\n}");

		out.println("</style>\n</head>\n<body><p>");

		// Find the minimum and maximum values in the collection of words
		int min = Integer.MAX_VALUE, max = 0;
		for (KeyWord word : words) {
			int occur = word.getOccurrences();
			if (occur > max)
				max = occur;
			if (occur < min)
				min = occur;
		}

		ArrayList<Integer> styleList = new ArrayList<Integer>();

		// THe words that will be be displayed in the word cloud
		ArrayList<KeyWord2> cloudList = new ArrayList<KeyWord2>();

		for (KeyWord word : words) {

			int index = chooseStyle(word.getOccurrences(), colors.length, max, min);

			if (simpleDisplay) {
				out.print("<span class=\"style");
				out.println(index + "\">" + word.getWord() + "</span>&nbsp;");
			}

			if (!simpleDisplay) {
				styleList.add(index);
				cloudList.add(new KeyWord2(word.getWord(), index));
			}

		}

		if (!simpleDisplay) {
			final int targetRowSize = 11;
			// final int targetRowSize = 10;
			final int numWords = styleList.size();

			if (debug) {
				System.out.print("Unordered style list: ");
				System.out.println(styleList);
				System.out.print("Unordered cloud list: ");
				System.out.println(cloudList);
			}

			ArrayList<Integer> orderedStyleList = (ArrayList) (styleList.clone());
			Collections.sort(orderedStyleList);
			if (debug) {
				System.out.print("Ordered style list: ");
				System.out.println(orderedStyleList);
			}

			ArrayList<KeyWord2> orderedCloudList = (ArrayList) (cloudList.clone());
			Collections.sort(orderedCloudList);
			if (debug) {
				System.out.print("Ordered Cloud list: ");
				System.out.println(orderedCloudList);
			}

			int numRows = (numWords % targetRowSize == 0) ? numWords / targetRowSize : numWords / targetRowSize + 1;
			if (debug)
				System.out.println("Number of rows = " + numRows);

			// Max words in left & right tables
			final int endMax = (int) ((6.0 / targetRowSize) * numWords);
			final int sizeLimit = 4; // Biggest font size for end positions

			int pos = 0;
			ArrayList<Integer> smallSizeList = new ArrayList<Integer>();
			while ((pos < endMax) && orderedStyleList.get(pos) <= sizeLimit) {
				smallSizeList.add(orderedStyleList.get(pos++));
			}
			if (debug) {
				System.out.print("Small size list: ");
				System.out.println(smallSizeList);
			}

			pos = 0;
			ArrayList<KeyWord2> smallSizeCloudList = new ArrayList<KeyWord2>();
			while ((pos < endMax) && orderedCloudList.get(pos).style <= sizeLimit) {
				smallSizeCloudList.add(orderedCloudList.get(pos++));
			}
			if (debug) {
				System.out.print("Small size cloud list: ");
				System.out.println(smallSizeCloudList);
				System.out.print("Shuffled small size cloud list: ");
			}
			Collections.shuffle(smallSizeCloudList);
			if (debug)
				System.out.println(smallSizeCloudList);

			List<Integer> largeSizeList;
			largeSizeList = orderedStyleList.subList(pos, numWords);
			if (debug) {
				System.out.print("Large size list: ");
				System.out.println(largeSizeList);
			}

			List<KeyWord2> largeSizeCloudList;
			largeSizeCloudList = orderedCloudList.subList(pos, numWords);
			if (debug) {
				System.out.print("Large size cloud list: ");
				System.out.println(largeSizeCloudList);
			}

			List<?>[] leftTable = new List<?>[numRows + 1];
			List<?>[] rightTable = new List<?>[numRows + 1];
			List<?>[] centerList = new List<?>[numRows + 1];

			int currentLine = 1;

			Util<Integer> u = new Util<Integer>();
			for (int i = 1; i <= numRows; i++) {
				leftTable[i] = u.get3(smallSizeList);
				rightTable[i] = u.get3(smallSizeList);
				centerList[i] = new ArrayList<Integer>();
			}

			List<?>[] leftCloudTable = new List<?>[numRows + 1];
			List<?>[] rightCloudTable = new List<?>[numRows + 1];
			List<?>[] centerCloudList = new List<?>[numRows + 1];

			Util<KeyWord2> u2 = new Util<KeyWord2>();
			for (int i = 1; i <= numRows; i++) {
				leftCloudTable[i] = u2.get3(smallSizeCloudList);
				rightCloudTable[i] = u2.get3(smallSizeCloudList);
				centerCloudList[i] = new ArrayList<KeyWord2>();
			}

			int[] capacity = new int[numRows + 1];
			int availableCapacity = largeSizeList.size();
			for (int i = 1; i <= numRows; i++) {
				int desiredSize = targetRowSize - leftTable[i].size() - rightTable[i].size();
				if (desiredSize <= availableCapacity)
					capacity[i] = desiredSize;
				else
					capacity[i] = availableCapacity;
				availableCapacity -= capacity[i];
			}
			if (debug) {
				System.out.print("Capacity vector: ");
				System.out.println(Arrays.toString(capacity));
			}

			int[] rowMap = new int[numRows + 2];
			int center = (1 + numRows) / 2;
			int currentPos = 2, delta = 1;
			rowMap[1] = center;
			while (currentPos <= 1 + numRows / 2) {
				rowMap[currentPos] = center + delta;
				rowMap[currentPos + 1] = center - delta;
				delta++;
				currentPos += 2;
			}

			if (debug) {
				System.out.print("Row Map vector: ");
				System.out.println(Arrays.toString(rowMap));
			}

			Collections.reverse(largeSizeList);
			if (debug) {
				System.out.print("Large size list, reversed: ");
				System.out.println(largeSizeList);
			}

			Collections.reverse(largeSizeCloudList);
			if (debug) {
				System.out.print("Large size cloud list, reversed: ");
				System.out.println(largeSizeCloudList);
			}

			for (currentPos = 0; largeSizeList.size() > 0; currentPos++) {
				int index = (currentPos % numRows) + 1;
				if (capacity[index] > 0) {
					((ArrayList<Integer>) centerList[index]).add(largeSizeList.remove(0));
					((ArrayList<KeyWord2>) centerCloudList[index]).add(largeSizeCloudList.remove(0));
					capacity[index]--;
				}
			}

			for (int i = 1; i <= numRows; i++) {
				if (debug) {
					System.out.println("Row: " + i);
					System.out.println("\tLeft Table: " + leftTable[i]);
					System.out.println("\tLeft Cloud Table: " + leftCloudTable[i]);
					System.out.println("\tCenter List (unsorted): " + centerList[i]);
					System.out.println("\tCenter Cloud List (unsorted): " + centerCloudList[i]);
				}
				centerList[i] = u.centerSort((ArrayList<Integer>) centerList[i]);
				centerCloudList[i] = u2.centerSort((ArrayList<KeyWord2>) centerCloudList[i]);
				if (debug) {
					System.out.println("\tCenter List (center-sorted): " + centerList[i]);
					System.out.println("\tCenter Cloud List (center-sorted): " + centerCloudList[i]);
					System.out.println("\tRight Table: " + rightTable[i]);
					System.out.println("\tRight Cloud Table: " + rightCloudTable[i]);
				}

			}

			for (int i = 1; i <= numRows; i++) {
				generateHtmlTable((List<KeyWord2>) leftCloudTable[i], out, Pos.LEFT);
				generateHtmlTable((List<KeyWord2>) rightCloudTable[i], out, Pos.RIGHT);
				generateHtmlLine((List<KeyWord2>) centerCloudList[i], out, Pos.CENTER);
			}

		}

		// Print the closing tags
		out.println("</p></body>\n</html>");
	}

	enum Pos {
		LEFT, RIGHT, CENTER
	};

	static int visitCount = 1; // How many times has generateHTMLTable been called?

	private static void generateHtmlTable(List<KeyWord2> words, PrintStream out, Pos position) {
		switch (position) {
		case LEFT:
			out.println("<table align=\"left\">");
			break;
		case RIGHT:
			out.println("<table align=\"right\">");
			break;
		case CENTER:
			out.println("<table align=\"center\">");
			break;

		}

		// Initial left & right tables are better positioned if a blank table entry
		// is added at the top

		if (visitCount <= 2) {
			out.println("<tr> <td");
			out.println("class=\"style" + 0 + "\"; align=\"center\">" + "&nbsp;");
			out.println("</td> </tr>");
		}
		for (KeyWord2 w : words) {
			out.println("<tr> <td");
			out.println("class=\"style" + w.style + "\"; align=\"center\">" + w.word);
			out.println("</td> </tr>");
		}
		out.println("</table>");
		visitCount++;

	}

	private static void generateHtmlLine(List<KeyWord2> words, PrintStream out, Pos position) {
		switch (position) {
		case LEFT:
			out.println("<div align=\"left\">");
			break;
		case RIGHT:
			out.println("<div align=\"right\">");
			break;
		case CENTER:
			out.println("<div align=\"center\">");
			break;

		}

		for (KeyWord2 w : words) {
			out.println("<span class=\"style" + w.style + "\">" + w.word + "</span>&nbsp;");
		}
		out.println("</div>");

	}

	static int chooseStyle(int wordCount, int colorsCount, int max, int min) {

		double slope = (colorsCount - 1.0) / (max - min);

		// Determine the appropriate style for this value using
		// linear interpolation
		// y = slope *(x - min) (rounded to nearest integer)
		// where y = the style number
		// and x = number of occurrences

		return (int) Math.round(slope * (wordCount - min));
	}

}

/**
 * A KeyWord2 is essentially a KeyWord, with ordering defined by the occur field
 * rather than the word field.
 */
class KeyWord2 implements Comparable<KeyWord2> {
	String word;
	int style;

	/**
	 * Constructs a KeyWord with the given word (converted to lower-case) and zero
	 * occurrences. If the word is null or an empty string, an
	 * IllegalArgumentException is thrown.
	 * 
	 * @param word
	 *            the word for this KeyWord
	 * @throws IllegalArgumentException
	 *             if word is null or empty
	 */
	public KeyWord2(KeyWord w) {
		word = w.getWord();
		style = w.getOccurrences();
	}

	public KeyWord2(String w, int style) {
		this.word = w;
		this.style = style;
	}

	/**
	 * Compares the KeyWord2 with the one given. Two KeyWord2s are compared by
	 * comparing the occur filed associated with the two KeyWord2s.
	 * 
	 * @param other
	 *            the KeyWord2 with which to compare this KeyWord2
	 */
	public int compareTo(KeyWord2 other) {
		return (new Integer(style)).compareTo(new Integer(other.style));
	}

	public String toString() {
		return style + "[" + word + "]";
	}
}

class Util<T> {
	ArrayList<T> get3(ArrayList<T> inputList) {

		ArrayList<T> resultList = new ArrayList<T>();
		for (int i = 0; i <= 2 && !inputList.isEmpty(); i++) {
			resultList.add(inputList.remove(0));
		}
		return resultList;
	}

	ArrayList<T> getN(List<T> inputList, int count) {

		ArrayList<T> resultList = new ArrayList<T>();
		for (int i = 0; i <= count && !inputList.isEmpty(); i++) {
			resultList.add(inputList.remove(0));
		}
		return resultList;
	}

	ArrayList<T> centerSort(List<T> inputList) {
		// Sort a reverse ordered list to one that has biggest value is in center,
		// next 2 biggest values are to right and left of center, etc.
		// Thus [5,4,3,2,1] becomes [1,3,5,4,2]

		ArrayList<T> resultList = new ArrayList<T>();
		// Collections.reverse(inputList);
		boolean placeAtLeft = true;
		while (!inputList.isEmpty()) {
			if (placeAtLeft) {
				resultList.add(0, inputList.remove(0));
				placeAtLeft = false;
			} else {
				resultList.add(inputList.remove(0));
				placeAtLeft = true;
			}
		}
		return resultList;
	}

}
