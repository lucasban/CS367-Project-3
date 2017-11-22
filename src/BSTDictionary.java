import java.util.Iterator;

public class BSTDictionary<K extends Comparable<K>> implements DictionaryADT<K> {
	private BSTnode<K> root; // the root node
	private int numItems; // the number of items in the dictionary

	/**
	 * Default constructor, creates a new BSTDictionary with no items
	 */
	public BSTDictionary() {
		numItems = 0;
		root = null;
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
			numItems++;
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
	 * TODO !!! DELETE RECURSIVE AND SMALLEST ARE ALL COPY HELPER FUNCTIONS
	 */
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
		// First check that the key is in the dictionary, and only attempt deletion if
		// it is.
		if (lookup(key) != null) {
			// Key was found
			// delete it using recursive helper function
			root = delete(root, key);
			// decrement numItems as we have removed it.
			numItems--;
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
			return findMin(node.getLeft());
		}
	}

	/**
	 * TODO FIXME LOOKUP and recursive
	 */
	public K lookup(K key) {
		return lookup(root, key);
	}

	/**
	 * Private method to recursively search for the given key in the given tree
	 * 
	 * @param n
	 *            root of tree to search through
	 * @param key
	 *            to search for
	 * @return the key found in the tree
	 */
	private K lookup(BSTnode<K> n, K key) {
		// Base cases
		// Not found in the tree, return null
		if (n == null) {
			return null;
		}
		// Found return the key searched for
		if (n.getKey().equals(key)) {
			return n.getKey();
		}

		// Recursive cases
		if (key.compareTo(n.getKey()) < 0) {
			// key < this node's key; look in left subtree
			return lookup(n.getLeft(), key);
		} else {
			// key > this node's key; look in right subtree
			return lookup(n.getRight(), key);
		}
	}

	/**
	 * TODO
	 */
	public boolean isEmpty() {
		return (numItems == 0);
	}

	/**
	 * TODO
	 */
	public int size() {
		return numItems;
	}

	/**
	 * Returns the total path length. The total path length is the sum of the
	 * lengths of the paths to each (key, value) pair.
	 * 
	 * @return the total path length
	 */
	public int totalPathLength() {
		return 0; // TODO replace this stub with your code
	}

	/**
	 * TODO
	 */
	public Iterator<K> iterator() {
		return new BSTDictionaryIterator<K>(root);
	}
}
