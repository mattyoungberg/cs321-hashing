/**
 * A Hashtable implementation that uses linear probing to resolve collisions.
 * 
 * @author Matt Youngberg
 */
public class LinearProbing extends Hashtable {

    /**
     * Constructs a new LinearProbing Hashtable with the given table size.
     * 
     * @param tableSize The size of the hash table
     */
    public LinearProbing(int tableSize) {
        super(tableSize);
    }
    
    /**
     * Hashes the given key using linear probing.
     * 
     * Returns the index of the key in the hash table.
     * 
     * @param key           The key to be hashed
     * @param probeNumber   The number of the probe
     */
    @Override
    public int hash(Object key, int probeNumber) {
        return (h1(key) + probeNumber) % tableSize;
    }
}
