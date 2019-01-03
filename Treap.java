import java.util.Iterator;

/**
 * A treap is a form of key-value tree map which relies on randomly generated
 * priorities to stay balanced.
 */
public interface Treap<K extends Comparable<K>, V> extends Iterable<K> {

    /**
     * The maximum priority that a node can have.
     */
    public static final int MAX_PRIORITY = 65535;

    /**
     * Retrieves the value associated with a key in this dictionary.
     * If the key is null or the key is not present in this
     * dictionary, this method returns null.
     */
    V lookup(K key);

    /**
     * Adds a key-value pair to this dictionary.  Any old value
     * associated with the key is lost.  If the key or the value is
     * null, the pair is not added to the dictionary.
     */
    void insert(K key, V value);

    /**
     * Removes a key from this dictionary.  If the key is not present
     * in this dictionary, this method does nothing.  Returns the
     * value associated with the removed key, or null if the key
     * is not present.
     */
    V remove(K key);

    /**
     * Splits this treap into two treaps.  The left treap should contain
     * values less than the key, while the right treap should contain
     * values greater than or equal to the key.
     */
    Treap<K, V> [] split(K key);

    /**
     * Joins this treap with another treap.  If the other treap is not
     * an instance of the implementing class, both treaps are unmodified.
     * At the end of the join, this treap will contain the result.
     * This method may destructively modify both treaps.
     */
    void join(Treap<K, V> t);

    /**
     * Build a human-readable version of the treap.
     * Each node in the treap will be represented as
     *
     *     [priority] <key, value>\n
     *
     * Subtreaps are indented one tab over from their parent for
     * printing.  This method prints out the string representations
     * of key and value using the object's toString(). Treaps should
     * be printed in pre-order traversal fashion.
     */
    String toString();


    /**
     * @return a fresh iterator that points to the first element in
     * this Treap and iterates in sorted order.
     */
    Iterator<K> iterator();

    /**
     * @return the balance factor of the treap.  The balance factor
     * is the height of the treap divided by the minimum possible
     * height of a treap of this size.  A perfectly balanced treap
     * has a balance factor of 1.0.  If this treap does not support
     * balance statistics, throw an exception.
     */
    double balanceFactor();
}
