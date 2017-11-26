
///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            WordCloudGenerator
// File:             ArrayHeap.java
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

import java.util.NoSuchElementException;
/**
 * ArrayHeap is a heap-based implementation of the PriorityQueueADT
 * @author Lucas Bannister
 *
 * @param <E> the type that will be stored in  the heap
 */
public class ArrayHeap<E extends Prioritizable> implements PriorityQueueADT<E> {
	private static final int INIT_SIZE = 100; // default number of items the heap can hold before expanding
	private E[] data; // The data heap stored in an array
	private int currSize; // The representation of the current heap size
	private int dataSize; // the current max size of the data array

	/**
	 * Default constructor, constructs an ArrayHeap of size INIT_SIZE
	 */
	public ArrayHeap() {
		// Call the 1-argument constructor with the INIT_SIZE parameter
		this(INIT_SIZE);
	}

	/**
	 * Constructs the array heap with a given initial size for the array
	 * 
	 * @param initSize
	 *            the size of the array to initialize
	 */
	public ArrayHeap(int initSize) {
		// If the parameter value is less than 0, throw IllegalArgumentException
		if (initSize < 0) {
			throw new IllegalArgumentException();
		}
		// initialize with initial size as provided and current size as 0
		data = (E[]) new Prioritizable[initSize + 1];
		currSize = 0;
		dataSize = data.length;
	}

	/**
	 * Returns true if and only if the ArrayHeap is empty.
	 * 
	 * @return true if the ArrayHeap is empty, false otherwise
	 */
	public boolean isEmpty() {
		return (size() == 0);
	}

	/**
	 * Inserts a new item into the ArrayHeap
	 * 
	 * @param item
	 *            the item to be inserted
	 */
	public void insert(E item) {
		// first check if there is room for one more item
		if (dataSize == currSize + 1) {
			// if it does, make room
			makeRoom();
		}

		// Put the item in the next spot in the array/heap and increment size
		data[currSize + 1] = item;
		currSize++;

		// Rearrange heap by swapping values up until it is in order, starting at end
		int currPos = currSize;

		// While there is a parent, and it has lower priority than the current position
		while ((data[currPos / 2] != null) && (data[currPos / 2].getPriority() < data[currPos].getPriority())) {
			// Swap the item with it's parent
			swapItems(currPos, currPos / 2);
			// repeat this for the new position until the heap is in proper order
			currPos = currPos / 2;
		}
	}

	/**
	 * Private helper function to double the size of the data array.
	 */
	private void makeRoom() {
		// Create a new array of double the size
		E[] newData = (E[]) new Prioritizable[dataSize * 2];
		// move the data over to the new array
		System.arraycopy(data, 0, newData, 0, dataSize);
		// point data at the new array
		data = newData;
		dataSize = data.length;
	}

	/**
	 * Removes the item with the maximum value from the ArrayHeap
	 * 
	 * @return the item that was removed (the former max)
	 */
	public E removeMax() {
		// Validate that there is something to remove
		if (currSize == 0) {
			throw new NoSuchElementException();
		}

		// the root is the max, save off
		E outMax = data[1];

		// Set the last child as the new root, remove it from old position and decrement
		// currSize
		data[1] = data[currSize];
		data[currSize] = null;
		currSize--;

		// put heap back into order
		heapifyDown();

		// Return the original root
		return outMax;
	}

	/**
	 * Private helper function to put the heap back into order after removing the
	 * max
	 */
	private void heapifyDown() {
		// Put heap back into proper order by swapping down
		// start at the new root
		int currPos = 1;

		while (childrenExist(currPos) && leftChildLarger(currPos) || rightChildLarger(currPos)) {
			// while there are children and one is larger
			// If both children are larger than the parent
			if (leftChildLarger(currPos) && rightChildLarger(currPos)) {
				if (data[currPos * 2].getPriority() > data[currPos * 2 + 1].getPriority()) {
					// left > right > parent, so swap left with parent
					swapItems(currPos, currPos * 2);
					currPos = currPos * 2;
				} else {
					// right > left > parent, swap right with parent
					swapItems(currPos, currPos * 2 + 1);
					currPos = currPos * 2 + 1;
				}
			} else if (leftChildLarger(currPos)) {
				// left > parent > right, swap left with parent
				swapItems(currPos, currPos * 2);
				currPos = currPos * 2;
			} else {
				// right > parent > left, swap right with parent
				swapItems(currPos, currPos * 2 + 1);
				currPos = currPos * 2 + 1;
			}
		}
	}

	/**
	 * Private helper function to swap two items
	 * 
	 * @param position1
	 *            position of first item to swap
	 * @param position2
	 *            position of second item to swap
	 */
	private void swapItems(int position1, int position2) {
		E tempData = data[position1];
		data[position1] = data[position2];
		data[position2] = tempData;
	}

	/**
	 * private helper function to determine if children elements exist
	 * 
	 * @param currPos
	 *            the node to check for children
	 * @return true if the children exist, otherwise false
	 */
	private boolean childrenExist(int currPos) {
		// checks that the current position has two non-null children
		return currPos * 2 < dataSize && data[currPos * 2] != null && data[currPos * 2 + 1] != null;
	}

	/**
	 * Private helper function to check if the left child exists and is larger than
	 * the parent
	 * 
	 * @param currPos
	 *            the current position
	 * @return true if the left child exists and is larger than the parent
	 */
	private boolean leftChildLarger(int currPos) {
		// checks if the data in left child is larger than the current position
		boolean leftChildLarger = data[currPos * 2].getPriority() > data[currPos].getPriority();
		return leftChildLarger;
	}

	/**
	 * Private helper function to check if the right child exists and is larger than
	 * the parent
	 * 
	 * @param currPos
	 *            the current position
	 * @return true if the right child exists and is larger than the parent
	 */
	private boolean rightChildLarger(int currPos) {
		// checks if the data in the right child is larger than the current position
		boolean rightChildLarger = data[currPos * 2 + 1].getPriority() > data[currPos].getPriority();
		return rightChildLarger;
	}

	/**
	 * Find and return the item with the maximum value in the ArrayHeap
	 * 
	 * @return the item with the maximum value in the heap
	 */
	public E getMax() {
		// if the heap is empty, throw an exception
		if (size() == 0) {
			throw new NoSuchElementException();
		}
		// return root of heap, as this is the max
		return data[1];
	}

	/**
	 * Returns the current number of items in the ArrayHeap
	 * 
	 * @return the number of items in the ArrayHeap
	 */
	public int size() {
		return currSize;
	}
}