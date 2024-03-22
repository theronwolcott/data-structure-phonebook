package phonebook.hashes;

import java.util.ArrayList;

import phonebook.exceptions.UnimplementedMethodException;
import phonebook.utils.KVPair;
import phonebook.utils.PrimeGenerator;

/**
 * <p>
 * {@link LinearProbingHashTable} is an Openly Addressed {@link HashTable}
 * implemented with <b>Linear Probing</b> as its
 * collision resolution strategy: every key collision is resolved by moving one
 * address over. It is
 * the most famous collision resolution strategy, praised for its simplicity,
 * theoretical properties
 * and cache locality. It <b>does</b>, however, suffer from the &quot;
 * clustering &quot; problem:
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
 * @see OrderedLinearProbingHashTable
 * @see QuadraticProbingHashTable
 * @see CollisionResolver
 */
public class LinearProbingHashTable extends OpenAddressingHashTable {

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
    public LinearProbingHashTable(boolean soft) {
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
     * <p>
     * Instances of {@link LinearProbingHashTable} will follow the writeup's
     * guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     *
     * @param key   The record's key.
     * @param value The record's value.
     * @return The value added.
     * @throws IllegalArgumentException if either argument is {@code null}.
     */
    @Override
    public String put(String key, String value) {
        resize();
        int index = hash(key);
        while (table[index] != null) {
            // if (softFlag && table[index].equals(TOMBSTONE)) {
            // tombstoneCount--;
            // break;
            // }
            index = (index + 1) % table.length;
        }
        table[index] = new KVPair(key, value);
        count++;
        return value;
    }

    @Override
    public String get(String key) {
        int index = hash(key);
        while (table[index] != null) {
            if (table[index].getKey().equals(key)) {
                return table[index].getValue();
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
            }
            index = (index + 1) % table.length;
        }
        if (found) {
            // put back all the items after the removed one in the cluster
            for (var pair : list) {
                put(pair.getKey(), pair.getValue());
            }
            return value;
        }
        return value;
    }
}
