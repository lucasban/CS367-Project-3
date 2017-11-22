/**
 * A KeyWord consists of a word and an integer (representing the number of
 * occurrences of the word). A word is a non-empty sequence of characters whose
 * letters are all lower-case.
 * 
 * @author Lucas Bannister
 *
 */
public class KeyWord implements Comparable<KeyWord>, Prioritizable {
	// Fields
	private int occurrences; // TODO count of the number of occurrences
	private String word; // TODO

	/**
	 * Constructs a KeyWord with the given word (converted to lower-case) and zero
	 * occurrences.
	 */
	public KeyWord(String word) {
		this.word = word;
		this.occurrences = 0;
	}

	/**
	 * Returns the word for this KeyWord.
	 * 
	 * @return the word for this KeyWord
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Returns the number of occurrences for this KeyWord.
	 * 
	 * @return the number of occurrences for this KeyWord
	 */
	public int getOccurrences() {
		return occurrences;
	}

	/**
	 * Adds one to the number of occurrences for this KeyWord.
	 */
	public void increment() {
		occurrences++;
	}

	/**
	 * Returns the priority for this KeyWord. The priority of a KeyWord is the
	 * number of occurrences it has.
	 * 
	 * @return the priority for this item.
	 */
	@Override
	public int getPriority() {
		return occurrences;
	}

	/**
	 * Compares the KeyWord with the one given. Two KeyWords are compared by
	 * comparing the word associated with the two KeyWords, ignoring case
	 * differences in the names.
	 * 
	 * @param other
	 *            the KeyWord with which to compare this KeyWord
	 * @return the value 0 if the argument string is equal to this string; a value
	 *         less than 0 if this string is lexicographically less than the string
	 *         argument; and a value greater than 0 if this string is
	 *         lexicographically greater than the string argument.
	 */
	@Override
	public int compareTo(KeyWord other) {
		int out = this.word.compareTo(other.getWord());
		return out;
	}

	/**
	 * Compares this KeyWord to the specified object. The result is true if and only
	 * if the argument is not null and is a KeyWord object whose word is the same as
	 * the word of this KeyWord, ignoring case differences.
	 * 
	 * @param other
	 *            the object with which to compare this KeyWord
	 * @return true if and only if the argument is not null and is a KeyWord object
	 *         whose word is the same as the word of this KeyWord, ignoring case
	 *         differences. Otherwise false.
	 */
	@Override
	public boolean equals(Object other) {
		// Make sure the other object is a non-null KeyWord
		if (other != null && (other instanceof KeyWord)) {
			// check if they are the same word
			if (this.word.equals(((KeyWord) other).getWord())) {
				return true;
			}
		}
		// otherwise false
		return false;

	}

}
