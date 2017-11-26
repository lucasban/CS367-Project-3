
///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            WordCloudGenerator
// File:             BSTDictionary.java
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

import java.util.Iterator;
/**
 * BSTDictionary is a binary search tree based implementation of the DictionaryADT
 * @author Lucas Bannister
 *
 * @param <K> class representing the key, should implement the Comparable<K> 
 *            interface 
 */
public class BSTDictionary<K extends Comparable<K>> implements DictionaryADT<K> {
	private BSTnode<K> root; // the root node of the tree
	private int numKeys; // the number of keys in the dictionary

	/**
	 * Default constructor, creates a new BSTDictionary with no items
	 */
	public BSTDictionary() {
		numKeys = 0; // no keys in it
		root = null; // no root node as it is empty
	}

	/**
	 * Inserts the given key into the Dictionary if the key is not already in the
	 * Dictionary. If the key is already in the Dictionary, a DuplicateException is
	 * thrown.
	 * 
	 * @param key
	 *            the key to insert into the Dictionary
	 * @throws DuplicateException
	 *             if the key is already in the Dictionary
	 * @throws IllegalArgumentException
	 *             if the key is null
	 */
	public void insert(K key) throws DuplicateException {
		// Call into recursive helper function
		root = insert(root, key);
	}

	/**
	 * Recursive helper function to insert key into the tree
	 * 
	 * @param node
	 *            the tree to insert the key into
	 * @param key
	 *            the key to insert
	 * @return the new root node of the tree
	 */
	private BSTnode<K> insert(BSTnode<K> node, K key) throws DuplicateException {
		// First handle base cases
		// Node is null, this is where we insert
		if (node == null) {
			// increment number of items
			numKeys++;
			// add key to tree
			return new BSTnode<K>(key, null, null);
		}

		// If the key is already in the dictionary, throw DuplicateException
		if (node.getKey().equals(key)) {
			throw new DuplicateException();
		}

		// Handle recursive cases
		// If the key is less than the current node, we will go into the left subtree
		if (key.compareTo(node.getKey()) < 0) {
			// recursive insert call for left subtree
			node.setLeft(insert(node.getLeft(), key));
			// returns node after recursion complete
			return node;
		} else {
			// key is greater than the current node, so we will go to the right subtree
			// recursive call into the right subtree
			node.setRight(insert(node.getRight(), key));
			// returns node after recursion complete
			return node;
		}
	}

	/**
	 * Deletes the given key from the Dictionary. If the key is in the Dictionary,
	 * the key is deleted and true is returned. If the key is not in the Dictionary,
	 * the Dictionary is unchanged and false is returned.
	 * 
	 * @param key
	 *            the key to delete from the Dictionary
	 * @return true if the deletion is successful (i.e., the key was in the
	 *         Dictionary and has been removed), false otherwise (i.e., the key was
	 *         not in the Dictionary)
	 */
	public boolean delete(K key) {
		// First check that the key is in the dictionary, 
		// and only attempt deletion if it is.
		if (lookup(key) != null) {
			// Key was found
			// delete it using recursive helper function
			root = delete(root, key);
			// decrement numKeys as we have removed it.
			numKeys--;
			// return true as deletion was successful
			return true;
		}
		// Key was not in dictionary, so dictionary is unchanged and false is returned
		return false;
	}

	/**
	 * Recursive helper function to delete a given key from subtree
	 * 
	 * @param node
	 *            the root of the tree to search through
	 * @param key
	 *            the key to delete from the Dictionary
	 * @return the new root node of the tree
	 */
	private BSTnode<K> delete(BSTnode<K> node, K key) {
		// Handle base case where the tree is null
		if (node == null) {
			// if the tree is null, we return null
			return null;
		}

		// Recursive cases
		// if the current node is the key we are searching for, we will delete it
		if (key.equals(node.getKey())) {
			// If there are no children return null, as we have removed the only node in the
			// tree
			if ((node.getLeft() == null) && (node.getRight() == null)) {
				return null;
			} else if (node.getLeft() == null) {
				// We know both aren't null, so determine which is, and return the other
				// if left was null, right is the new root
				return node.getRight();
			} else if (node.getRight() == null) {
				// if right was null, left is the new root
				return node.getLeft();
			} else {
				// neither left nor right were null, so the node has two children
				// so we need to find the smallest
				// (in order successor)
				// Use recursive helper function to find the smallest in the right subtree
				K minNode = findMin(node.getRight());
				// make the current node the smallest node of the right subtree
				node.setKey(minNode);
				// make the new right node the right subtree with minNode removed
				node.setRight(delete(node.getRight(), minNode));
				// return the new root
				return node;
			}
		}
		// Compare the key to the current node to figure out which subtree to go into
		else if (key.compareTo(node.getKey()) < 0) {
			// if key is smaller than current node, recursively delete from the left subtree
			node.setLeft(delete(node.getLeft(), key));
			// return the new subtree root
			return node;
		} else {
			// otherwise, the key is larger than the current node, so we will recursively
			// delete from the right subtree
			node.setRight(delete(node.getRight(), key));
			// return the new subtree root
			return node;
		}
	}

	/**
	 * Recursive helper function to find the min node of a subtree
	 * 
	 * @param node
	 *            the root node to search for the smallest node in
	 * @return the smallest key in the subtree rooted at n
	 */
	private K findMin(BSTnode<K> node) {
		// if the node is null, there is nothing to search
		if (node == null) {
			// return null
			return null;
		} else if (node.getLeft() == null) {
			// left subtree empty,
			// current node must be the min,
			// so we return it
			return node.getKey();
		} else {
			// otherwise, recursively search the left subtree for the min
			return findMin(node.getLeft());
		}
	}

	/**
	 * TODO FIXME LOOKUP and recursive
	 */
	/**
	 * Searches for the given key in the Dictionary and returns the key stored in
	 * the Dictionary. If the key is not in the Dictionary, null is returned.
	 * 
	 * @param key
	 *            the key to search for
	 * @return the key from the Dictionary, if the key is in the Dictionary; null if
	 *         the key is not in the Dictionary
	 */
	public K lookup(K key) {
		return lookup(root, key);
	}

	/**
	 * Private method to recursively search for the given key in the given tree
	 * 
	 * @param node
	 *            root of subtree to search through
	 * @param key
	 *            the key to search for
	 * @return the key found in the tree
	 */
	private K lookup(BSTnode<K> node, K key) {
		// Handle base case where the tree is null
		if (node == null) {
			// if the tree is null, we return null
			return null;
		}

		// If the key is equal to the current node, we found it, return it
		if (key.equals(node.getKey())) {
			return node.getKey();
		}

		// Recursive cases
		if (key.compareTo(node.getKey()) < 0) {
			// If the key is less than the current node, look in the left subtree
			return lookup(node.getLeft(), key);
		} else {
			// If the key is greater than the current node, look in the right subtree
			return lookup(node.getRight(), key);
		}
	}

	/**
	 * Returns true if and only if the Dictionary is empty.
	 * 
	 * @return true if the Dictionary is empty, false otherwise
	 */
	public boolean isEmpty() {
		// if the size is 0, then the Dictionary is empty
		return (size() == 0);
	}

	/**
	 * Returns the number of keys in the Dictionary.
	 * 
	 * @return the number of keys in the Dictionary
	 */
	public int size() {
		// the size of the dictionary is just the number of keys
		return numKeys;
	}

	/**
	 * Returns the total path length. The total path length is the sum of the
	 * lengths of the paths to each (key, value) pair.
	 * 
	 * @return the total path length
	 */
	public int totalPathLength() {
		int depth = 0; // initial depth is 0 at the root
		return totalPathLength(root, depth); // call into recursive helper function
	}

	/**
	 * Recursive helper function to calculate total path length of subtree
	 * 
	 * @param node
	 *            the root node of the subtree to get total path length of
	 * @return the total path length of subtree with root at node
	 */
	private int totalPathLength(BSTnode<K> node, int depth) {
		int count = 0; // count starts at 0

		if (node == null) {
			// if the node is null, return 0
			return count;
		}

		if (node.getLeft() != null) {
			// if there is a left node, increment depth and
			// recursively get path length of left subtree
			count += totalPathLength(node.getLeft(), depth + 1);
		}

		if (node.getRight() != null) {
			// if there is a right node, increment depth and
			// recursively get path length of right subtree
			count += totalPathLength(node.getRight(), depth + 1);
		}

		// increment count by the current depth (this is path length to current node)
		count += depth;

		// return count
		return count;

	}

	/**
	 * Returns an iterator over the Dictionary that iterates over the keys in the
	 * Dictionary in order from smallest to largest.
	 * 
	 * @return an iterator over the keys in the Dictionary in order
	 */
	public Iterator<K> iterator() {
		return new BSTDictionaryIterator<K>(root);
	}
}
