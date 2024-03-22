package phonebook.hashes;

import phonebook.exceptions.UnimplementedMethodException;
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

    private KVPairList[] table;
    private int count;
    private PrimeGenerator primeGenerator;

    // We mask the top bit of the default hashCode() to filter away negative values.
    // Have to copy over the implementation from OpenAddressingHashTable; no biggie.
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
    public SeparateChainingHashTable() {
        primeGenerator = new PrimeGenerator();
        table = new KVPairList[primeGenerator.getCurrPrime()];
        count = 0;
    }

    @Override
    public String put(String key, String value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new KVPairList();
        }
        table[index].addBack(key, value);
        count++;
        return value;
    }

    @Override
    public String get(String key) {
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        return table[index].getValue(key).getValue();
    }

    @Override
    public String remove(String key) {
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        var probe = table[index].removeByKey(key);
        if (probe.getValue() != null) {
            count--;
        }
        return probe.getValue();
    }

    @Override
    public boolean containsKey(String key) {
        int index = hash(key);
        if (table[index] == null) {
            return false;
        }
        return table[index].containsKey(key);

    }

    @Override
    public boolean containsValue(String value) {
        for (var list : table) {
            if (list != null) {
                if (list.containsValue(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int capacity() {
        return table.length; // Or the value of the current prime.
    }

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
    public void enlarge() {
        var temp = table;
        table = new KVPairList[primeGenerator.getNextPrime()];
        count = 0;
        for (var list : temp) {
            if (list != null) {
                for (var pair : list) {
                    put(pair.getKey(), pair.getValue());
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
    public void shrink() {
        var temp = table;
        table = new KVPairList[primeGenerator.getPreviousPrime()];
        count = 0;
        for (var list : temp) {
            if (list != null) {
                for (var pair : list) {
                    put(pair.getKey(), pair.getValue());
                }
            }
        }
    }
}
