package phonebook.hashes;

import phonebook.exceptions.UnimplementedMethodException;
import phonebook.utils.KVPair;
import phonebook.utils.PrimeGenerator;

/**
 * <p>
 * {@link QuadraticProbingHashTable} is an Openly Addressed {@link HashTable}
 * which uses <b>Quadratic
 * Probing</b> as its collision resolution strategy. Quadratic Probing differs
 * from <b>Linear</b> Probing
 * in that collisions are resolved by taking &quot; jumps &quot; on the hash
 * table, the length of which
 * determined by an increasing polynomial factor. For example, during a key
 * insertion which generates
 * several collisions, the first collision will be resolved by moving 1^2 + 1 =
 * 2 positions over from
 * the originally hashed address (like Linear Probing), the second one will be
 * resolved by moving
 * 2^2 + 2= 6 positions over from our hashed address, the third one by moving
 * 3^2 + 3 = 12 positions over, etc.
 * </p>
 *
 * <p>
 * By using this collision resolution technique,
 * {@link QuadraticProbingHashTable} aims to get rid of the
 * &quot;key clustering &quot; problem that {@link LinearProbingHashTable}
 * suffers from. Leaving more
 * space in between memory probes allows other keys to be inserted without many
 * collisions. The tradeoff
 * is that, in doing so, {@link QuadraticProbingHashTable} sacrifices <em>cache
 * locality</em>.
 * </p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see OrderedLinearProbingHashTable
 * @see LinearProbingHashTable
 * @see CollisionResolver
 */
public class QuadraticProbingHashTable extends OpenAddressingHashTable {

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
    public QuadraticProbingHashTable(boolean soft) {
        super(soft);
    }

    @Override
    public String put(String key, String value) {
        resize();
        int index = hash(key);
        int originalIndex = hash(key);
        int i = 1;
        while (table[index] != null) {
            // if (softFlag && table[index].equals(TOMBSTONE)) {
            // tombstoneCount--;
            // break;
            // }
            index = nextIndex(originalIndex, i);
            i++;
        }
        table[index] = new KVPair(key, value);
        count++;
        return value;
    }

    private int nextIndex(int index, int i) {
        return (index + (i - 1) + ((i - 1) * (i - 1))) % table.length;
    }

    @Override
    public String get(String key) {
        int index = hash(key);
        int originalIndex = hash(key);
        int i = 1;
        while (table[index] != null) {
            if (table[index].getKey().equals(key)) {
                return table[index].getValue();
            }
            index = nextIndex(originalIndex, i);
            i++;
        }
        return null;
    }

    @Override
    public String remove(String key) {
        int index = hash(key);
        int originalIndex = hash(key);
        int i = 1;
        String value = null;
        while (table[index] != null) {
            if (table[index].getKey().equals(key)) {
                value = table[index].getValue();
                if (softFlag) {
                    table[index] = TOMBSTONE;
                    tombstoneCount++;
                    count--;
                } else {
                    table[index] = null;
                    count--;
                    resize(false);
                }
                return value;
            }
            index = nextIndex(originalIndex, i);
            i++;
        }
        return null;
    }
}