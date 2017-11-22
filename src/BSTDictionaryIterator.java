import java.util.*;

/**
 * BSTDictionaryIterator implements an iterator for a binary search tree (BST)
 * implementation of a Dictionary. The iterator iterates over the tree in order
 * of the key values (from smallest to largest).
 */
public class BSTDictionaryIterator<K> implements Iterator<K> {

	// TO DO:
	//
	// Add your code to implement the BSTDictionaryIterator. To receive full
	// credit:
	// - You must not use recursion in any of methods or constructor.
	// - The constructor must have a worst-case complexity of O(height of BST).
	//
	// Hint: use a Stack and push/pop nodes as you iterate through the BST.
	// The constructor should push all the nodes needed so the *first* call
	// to next() returns the value in the node with the smallest key.
	// (You can use the Java API Stack or implement your own Stack - if you
	// implement your own, make sure to hand it in.)

	private Stack<BSTnode<K>> stack; // FIXME The stack containing the BSTDictionary tree so in is iterated in order
	
	/**
	 * Constructs an iterator for the BSTDictionary
	 * 
	 * @param root
	 *            the root the BSTDictionary to iterate
	 */
	public BSTDictionaryIterator(BSTnode<K> root) {
		stack = new Stack<BSTnode<K>>();

		// Push nodes from the root to the farthest left node
		BSTnode<K> n = root;
		while (n != null) {
			stack.push(n);
			n = n.getLeft();
		}

	}
	
	/**
	 * TODO
	 */
	public boolean hasNext() {
		if (stack.isEmpty()) {
			return false;
		}
		else return true;
	}

	public K next() {
		BSTnode<K> returnNode = stack.pop();

		// Push nodes from the popped node to it's inorder successor
		if (returnNode.getRight() != null) {
			BSTnode<K> n = returnNode.getRight();
			while (n != null) {
				stack.push(n);
				n = n.getLeft();
			}
		}

		return returnNode.getKey();
	}

	public void remove() {
		// DO NOT CHANGE: you do not need to implement this method
		throw new UnsupportedOperationException();
	}
}
