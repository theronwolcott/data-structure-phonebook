package phonebook.hashes;

import java.util.ArrayList;

import phonebook.exceptions.UnimplementedMethodException;
import phonebook.utils.KVPair;
import phonebook.utils.PrimeGenerator;

/**
 * <p>
 * {@link OrderedLinearProbingHashTable} is an Openly Addressed
 * {@link HashTable} implemented with
 * <b>Ordered Linear Probing</b> as its collision resolution strategy: every key
 * collision is resolved by moving
 * one address over, and the keys in the chain is in order. It suffer from the
 * &quot; clustering &quot; problem:
 * collision resolutions tend to cluster collision chains locally, making it
 * hard for new keys to be
 * inserted without collisions. {@link QuadraticProbingHashTable} is a
 * {@link HashTable} that
 * tries to avoid this problem, albeit sacrificing cache locality.
 * </p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see QuadraticProbingHashTable
 * @see CollisionResolver
 */
public class OrderedLinearProbingHashTable extends OpenAddressingHashTable {

    /* ********************************************************************/
    /* ** INSERT ANY PRIVATE METHODS OR FIELDS YOU WANT TO USE HERE: ******/
    /* ********************************************************************/

    /* ******************************************/
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS: */
    /* **************************************** */

    /**
     * Constructor with soft deletion option. Initializes the internal storage with
     * a size equal to the starting value of {@link PrimeGenerator}.
     * 
     * @param soft A boolean indicator of whether we want to use soft deletion or
     *             not. {@code true} if and only if
     *             we want soft deletion, {@code false} otherwise.
     */
    public OrderedLinearProbingHashTable(boolean soft) {
        super(soft);
    }

    /**
     * Inserts the pair &lt;key, value&gt; into this. The container should
     * <b>not</b> allow for {@code null}
     * keys and values, and we <b>will</b> test if you are throwing a
     * {@link IllegalArgumentException} from your code
     * if this method is given {@code null} arguments! It is important that we
     * establish that no {@code null} entries
     * can exist in our database because the semantics of {@link #get(String)} and
     * {@link #remove(String)} are that they
     * return {@code null} if, and only if, their key parameter is {@code null}.
     * This method is expected to run in <em>amortized
     * constant time</em>.
     *
     * Different from {@link LinearProbingHashTable}, the keys in the chain are
     * <b>in order</b>. As a result, we might increase
     * the cost of insertion and reduce the cost on search miss. One thing to notice
     * is that, in soft deletion, we ignore
     * the tombstone during the reordering of the keys in the chain. We will have
     * some example in the writeup.
     *
     * Instances of {@link OrderedLinearProbingHashTable} will follow the writeup's
     * guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     * 
     * @param key   The record's key.
     * @param value The record's value.
     * @throws IllegalArgumentException if either argument is {@code null}.
     * @return The value added.
     */
    @Override
    public String put(String key, String value) {
        var list = new ArrayList<KVPair>();
        boolean found = false;
        resize();
        int index = hash(key);
        while (table[index] != null) {
            if (found) {
                list.add(table[index]);
                table[index] = null;

                // if (softFlag && table[index].equals(TOMBSTONE)) {
                // tombstoneCount--;
                // break;
                // }
            } else if (table[index].getKey().compareTo(key) > 0) {
                found = true;
                list.add(table[index]);
                table[index] = new KVPair(key, value);
                count++;
            }
            index = (index + 1) % table.length;
        }
        if (!found) {
            table[index] = new KVPair(key, value);
            count++;
        } else {
            for (var pair : list) {
                if (softFlag && pair.equals(TOMBSTONE)) {
                    tombstoneCount--;
                } else {
                    count--;
                    put(pair.getKey(), pair.getValue());
                }
            }
        }
        return value;
    }

    @Override
    public String get(String key) {
        int index = hash(key);
        while (table[index] != null) {
            if (table[index].getKey().equals(key)) {
                return table[index].getValue();
            }
            if (table[index].getKey().compareTo(key) > 0) {
                return null;
            }
            index = (index + 1) % table.length;
        }
        return null;
    }

    /**
     * <b>Return</b> the value associated with key in the {@link HashTable}, and
     * <b>remove</b> the {@link phonebook.utils.KVPair} from the table.
     * If key does not exist in the database
     * or if key = {@code null}, this method returns {@code null}. This method is
     * expected to run in <em>amortized constant time</em>.
     *
     * @param key The key to search for.
     * @return The associated value. If the key is {@code null}, return
     *         {@code null};
     *         if the key doesn't exist in the database, return {@code null}.
     */
    @Override
    public String remove(String key) {
        var list = new ArrayList<KVPair>();
        int index = hash(key);
        boolean found = false;
        String value = null;
        while (table[index] != null) {
            if (found) {
                list.add(table[index]);
                table[index] = null;
                count--;
            } else if (table[index].getKey().equals(key)) {
                value = table[index].getValue();
                if (softFlag) {
                    table[index] = TOMBSTONE;
                    tombstoneCount++;
                    count--;
                    return value;
                }
                // hard deletion
                table[index] = null;
                count--;
                found = true;
            } else if (table[index].getKey().compareTo(key) > 0) {
                // stop looking if we are past the target key and haven't found it yet
                return value;
            }
            index = (index + 1) % table.length;
        }
        if (found) {
            for (var pair : list) {
                put(pair.getKey(), pair.getValue());
            }
            return value;
        }
        return value;
    }
}
