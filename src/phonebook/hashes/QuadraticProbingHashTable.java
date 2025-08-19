package phonebook.hashes;

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
    // Constructor: initializes the hash table, optionally enabling soft deletion
    public QuadraticProbingHashTable(boolean soft) {
        super(soft);
    }

    @Override
    public String put(String key, String value) {
        resize(); // Ensure table is large enough before inserting
        int index = hash(key); // Get initial index using hash function
        int originalIndex = hash(key); // Store original index for probing
        int i = 1; // Probe counter
        // Probe until we find an empty slot
        while (table[index] != null) {
            // If slot is occupied, calculate next index using quadratic probing
            index = nextIndex(originalIndex, i);
            i++;
        }
        // Insert the key-value pair at the found index
        table[index] = new KVPair(key, value);
        count++; // Increment count of elements
        return value;
    }

    // Calculates the next index for quadratic probing
    private int nextIndex(int index, int i) {
        // Formula: (original index + (i-1) + (i-1)^2) mod table length
        return (index + (i - 1) + ((i - 1) * (i - 1))) % table.length;
    }

    @Override
    public String get(String key) {
        int index = hash(key); // Get initial index
        int originalIndex = hash(key); // Store original index for probing
        int i = 1; // Probe counter
        // Probe until we find the key or hit an empty slot
        while (table[index] != null) {
            // If the key matches, return its value
            if (table[index].getKey().equals(key)) {
                return table[index].getValue();
            }
            // Otherwise, continue probing
            index = nextIndex(originalIndex, i);
            i++;
        }
        // Key not found
        return null;
    }

    @Override
    public String remove(String key) {
        int index = hash(key); // Get initial index
        int originalIndex = hash(key); // Store original index for probing
        int i = 1; // Probe counter
        String value = null; // To store value if found
        // Probe until we find the key or hit an empty slot
        while (table[index] != null) {
            // If the key matches, remove it
            if (table[index].getKey().equals(key)) {
                value = table[index].getValue();
                if (softFlag) {
                    // Soft deletion: mark slot as tombstone
                    table[index] = TOMBSTONE;
                    tombstoneCount++;
                    count--;
                } else {
                    // Hard deletion: clear slot and resize if needed
                    table[index] = null;
                    count--;
                    resize(false);
                }
                return value;
            }
            // Otherwise, continue probing
            index = nextIndex(originalIndex, i);
            i++;
        }
        // Key not found
        return null;
    }
}