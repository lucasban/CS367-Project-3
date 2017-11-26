
///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            WordCloudGenerator
// File:             BSTDictionaryIterator.java
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

/**
 * BSTDictionaryIterator implements an iterator for a binary search tree (BST)
 * implementation of a Dictionary. The iterator iterates over the tree in order
 * of the key values (from smallest to largest).
 * 
 * @author Lucas Bannister
 */
public class BSTDictionaryIterator<K> implements Iterator<K> {
	private Stack<BSTnode<K>> stack; // The stack that will store the tree to allow it iterated in order

	/**
	 * Constructs an iterator for the BSTDictionary
	 * 
	 * @param root
	 *            the root the BSTDictionary to iterate
	 */
	public BSTDictionaryIterator(BSTnode<K> root) {
		// Create a new stack to hold the nodes
		stack = new Stack<BSTnode<K>>();

		// Push nodes starting at the root to the farthest left (min) node
		// only pushing the nodes needed at this point,
		// other nodes will be pushed as needed when calling next()
		BSTnode<K> node = root;
		while (node != null) {
			stack.push(node);
			node = node.getLeft();
		}
	}

	/**
	 * Returns true if the iteration has more elements. (In other words, returns
	 * true if next() would return an element rather than throwing an exception.)
	 */
	public boolean hasNext() {
		if (stack.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns the next element in the iteration.
	 * 
	 * @returns the next element in the iteration
	 */
	public K next() throws NoSuchElementException {
		// First check that it is possible to return the next element
		if (this.hasNext() == false) {
			throw new NoSuchElementException();
		}

		// Pop the next element and store the Key
		BSTnode<K> outNode = stack.pop();
		K outKey = outNode.getKey();

		// Push nodes starting at the popped node to in order successor node
		// only pushing the nodes needed at this point,
		// other nodes will be pushed as needed
		if (outNode.getRight() != null) {
			BSTnode<K> node = outNode.getRight();
			while (node != null) {
				stack.push(node);
				node = node.getLeft();
			}
		}

		// return the key of the popped node
		return outKey;
	}

	/**
	 * !! UNSUPPORTED OPERATION !!
	 * 
	 * Removes from the underlying collection the last element returned by this
	 * iterator (optional operation). This method can be called only once per call
	 * to next(). The behavior of an iterator is unspecified if the underlying
	 * collection is modified while the iteration is in progress in any way other
	 * than by calling this method.
	 */
	public void remove() {
		// DO NOT CHANGE: you do not need to implement this method
		throw new UnsupportedOperationException();
	}
}
