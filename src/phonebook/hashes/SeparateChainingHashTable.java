package phonebook.hashes;

import phonebook.utils.KVPairList;
import phonebook.utils.PrimeGenerator;

/**
 * <p>
 * {@link SeparateChainingHashTable} is a {@link HashTable} that implements
 * <b>Separate Chaining</b>
 * as its collision resolution strategy, i.e the collision chains are
 * implemented as actual
 * Linked Lists. These Linked Lists are <b>not assumed ordered</b>. It is the
 * easiest and most &quot; natural &quot; way to
 * implement a hash table and is useful for estimating hash function quality. In
 * practice, it would
 * <b>not</b> be the best way to implement a hash table, because of the wasted
 * space for the heads of the lists.
 * Open Addressing methods, like those implemented in
 * {@link LinearProbingHashTable} and {@link QuadraticProbingHashTable}
 * are more desirable in practice, since they use the original space of the
 * table for the collision chains themselves.
 * </p>
 *
 * @author YOUR NAME HERE!
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see OrderedLinearProbingHashTable
 * @see CollisionResolver
 */
public class SeparateChainingHashTable implements HashTable {

    /* ****************************************************************** */
    /* ***** PRIVATE FIELDS / METHODS PROVIDED TO YOU: DO NOT EDIT! ***** */
    /* ****************************************************************** */

    // Array of linked lists, each representing a bucket in the hash table
    private KVPairList[] table;
    // Number of key-value pairs currently stored
    private int count;
    // Helper to generate prime numbers for resizing the table
    private PrimeGenerator primeGenerator;

    // We mask the top bit of the default hashCode() to filter away negative values.
    // Have to copy over the implementation from OpenAddressingHashTable; no biggie.
    // Hash function: ensures non-negative index within table bounds
    public int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /* **************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS: */
    /* **************************************** */
    /**
     * Default constructor. Initializes the internal storage with a size equal to
     * the default of {@link PrimeGenerator}.
     */
    // Constructor: initializes table with default prime size
    public SeparateChainingHashTable() {
        primeGenerator = new PrimeGenerator();
        table = new KVPairList[primeGenerator.getCurrPrime()];
        count = 0;
    }

    @Override
    // Adds a key-value pair to the hash table
    public String put(String key, String value) {
        int index = hash(key); // Find bucket index
        if (table[index] == null) {
            table[index] = new KVPairList(); // Create list if bucket is empty
        }
        table[index].addBack(key, value); // Add pair to end of list
        count++;
        return value;
    }

    @Override
    // Retrieves the value for a given key, or null if not found
    public String get(String key) {
        int index = hash(key);
        if (table[index] == null) {
            return null; // No list at this bucket
        }
        return table[index].getValue(key).getValue(); // Get value from list
    }

    @Override
    // Removes a key-value pair by key, returns the value or null if not found
    public String remove(String key) {
        int index = hash(key);
        if (table[index] == null) {
            return null; // No list at this bucket
        }
        var probe = table[index].removeByKey(key); // Remove from list
        if (probe.getValue() != null) {
            count--; // Only decrement if something was removed
        }
        return probe.getValue();
    }

    @Override
    // Checks if a key exists in the table
    public boolean containsKey(String key) {
        int index = hash(key);
        if (table[index] == null) {
            return false;
        }
        return table[index].containsKey(key);
    }

    @Override
    // Checks if a value exists anywhere in the table
    public boolean containsValue(String value) {
        for (var list : table) {
            if (list != null) {
                if (list.containsValue(value)) {
                    return true; // Found value in a bucket
                }
            }
        }
        return false;
    }

    @Override
    // Returns the number of key-value pairs in the table
    public int size() {
        return count;
    }

    @Override
    // Returns the current capacity (number of buckets)
    public int capacity() {
        return table.length; // Or the value of the current prime.
    }

    // Returns the linked list at a specific bucket index
    public KVPairList get(int idx) throws IndexOutOfBoundsException {
        return table[idx];
    }

    /**
     * Enlarges this hash table. At the very minimum, this method should increase
     * the <b>capacity</b> of the hash table and ensure
     * that the new size is prime. The class {@link PrimeGenerator} implements the
     * enlargement heuristic that
     * we have talked about in class and can be used as a black box if you wish.
     * 
     * @see PrimeGenerator#getNextPrime()
     */
    // Increases the table size to the next prime and rehashes all entries
    public void enlarge() {
        var temp = table; // Save old table
        table = new KVPairList[primeGenerator.getNextPrime()]; // New larger table
        count = 0;
        for (var list : temp) {
            if (list != null) {
                for (var pair : list) {
                    put(pair.getKey(), pair.getValue()); // Re-insert all pairs
                }
            }
        }
    }

    /**
     * Shrinks this hash table. At the very minimum, this method should decrease the
     * size of the hash table and ensure
     * that the new size is prime. The class {@link PrimeGenerator} implements the
     * shrinking heuristic that
     * we have talked about in class and can be used as a black box if you wish.
     *
     * @see PrimeGenerator#getPreviousPrime()
     */
    // Decreases the table size to the previous prime and rehashes all entries
    public void shrink() {
        var temp = table; // Save old table
        table = new KVPairList[primeGenerator.getPreviousPrime()]; // New smaller table
        count = 0;
        for (var list : temp) {
            if (list != null) {
                for (var pair : list) {
                    put(pair.getKey(), pair.getValue()); // Re-insert all pairs
                }
            }
        }
    }
}
