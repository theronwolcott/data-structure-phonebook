package phonebook.hashes;

import phonebook.exceptions.UnimplementedMethodException;
import phonebook.utils.PrimeGenerator;

/**
 * <p>{@link QuadraticProbingHashTable} is an Openly Addressed {@link HashTable} which uses <b>Quadratic
 * Probing</b> as its collision resolution strategy. Quadratic Probing differs from <b>Linear</b> Probing
 * in that collisions are resolved by taking &quot; jumps &quot; on the hash table, the length of which
 * determined by an increasing polynomial factor. For example, during a key insertion which generates
 * several collisions, the first collision will be resolved by moving 1^2 + 1 = 2 positions over from
 * the originally hashed address (like Linear Probing), the second one will be resolved by moving
 * 2^2 + 2= 6 positions over from our hashed address, the third one by moving 3^2 + 3 = 12 positions over, etc.
 * </p>
 *
 * <p>By using this collision resolution technique, {@link QuadraticProbingHashTable} aims to get rid of the
 * &quot;key clustering &quot; problem that {@link LinearProbingHashTable} suffers from. Leaving more
 * space in between memory probes allows other keys to be inserted without many collisions. The tradeoff
 * is that, in doing so, {@link QuadraticProbingHashTable} sacrifices <em>cache locality</em>.</p>
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
    /*  IMPLEMENT THE FOLLOWING PUBLIC METHODS: */
    /* **************************************** */

    /**
     * Constructor with soft deletion option. Initializes the internal storage with a size equal to the starting value of  {@link PrimeGenerator}.
     * @param soft A boolean indicator of whether we want to use soft deletion or not. {@code true} if and only if
     *               we want soft deletion, {@code false} otherwise.
     */
    public QuadraticProbingHashTable(boolean soft) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }

    @Override
    public String put(String key, String value) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }


    @Override
    public String get(String key) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }

    @Override
    public String remove(String key) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }


    @Override
    public boolean containsKey(String key) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }

    @Override
    public boolean containsValue(String value) {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }
    @Override
    public int size(){
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }

    @Override
    public int capacity() {
        throw new UnimplementedMethodException(); // ERASE THIS LINE AFTER IMPLEMENTING THIS METHOD!
    }

}