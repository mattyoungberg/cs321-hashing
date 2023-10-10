/**
 * Double hashing implementation of a hash table.
 * 
 * @author Matt Youngberg
 */
public class DoubleHashing extends Hashtable {

    /**
     * Constructs a new DoubleHashing Hashtable with the given table size.
     * 
     * @param tableSize The size of the hash table
     */
    public DoubleHashing(int tableSize) {
        super(tableSize);
    }

    /**
     * Hashes the given key using double hashing.
     * 
     * Returns the index of the key in the hash table.
     * 
     * @param key           The key to be hashed
     * @param probeNumber   The number of the probe
     */
    @Override
    public int hash(Object key, int probeNumber) {
        return (h1(key) + probeNumber * h2(key)) % tableSize;
    }

    /**
     * An auxillary hash function unique to double hashing.
     * 
     * h2 is placed here, whereas h1 is placed in the superclass, Hashtable, per the project spec.
     * 
     * @param key   The key to be hashed
     * @return      The hash value of the key
     */
    private int h2(Object key) {
        return 1 + positiveMod(key.hashCode(), tableSize - 2);
    }
}
