/**
 * An abstract Hashtable class.
 * 
 * Implementations must define the hash method, which will define how the table's probe sequence is
 * generated.
 * 
 * @author Matt Youngberg
 */
public abstract class Hashtable {
    
    /**
     * The hash table, represented as an array of HashObjects.
     */
    protected HashObject[] table;

    /**
     * The size of the hash table.
     */
    protected int tableSize;

    /**
     * Constructs a new Hashtable with the given table size.
     * 
     * Intended to be called by implementing classes.
     * 
     * @param tableSize The size of the hash table
     */
    public Hashtable(int tableSize) {
        this.tableSize = tableSize;
        this.table = new HashObject[tableSize];
    }

    /**
     * A method that calculates the next step in a probe sequence for the given key and probe
     * number.
     * 
     * @param key           The key to be hashed
     * @param probeNumber   The number of the probe
     * @return              The next step in the probe sequence
     */
    public abstract int hash(Object key, int probeNumber);

    /**
     * Insert the given key into the hash table.
     * 
     * @param key   The key to be inserted
     * @return      The index of the inserted key
     */
    public int insert(Object key) {
        int i = 0;
        do {  // Following the pseudocode in the book, hence do-while
            int q = hash(key, i);
            if (table[q] == null) {
                table[q] = new HashObject(key);
                return q;
            }
            else {
                table[q].incrementProbeCount();
                if (table[q].equals(new HashObject(key))) {
                    table[q].incrementFrequency();
                    return q;
                }
                i++;
            };
        } while (i < table.length);
        throw new RuntimeException("Table is full");
    }

    /**
     * Search for the given key in the hash table.
     * 
     * @param key   The key to be searched for
     * @return      The index of the key, or -1 if it is not found
     */
    public int search(Object key) {
        int i = 0;
        int q = -1;
        do {  // Following the pseudocode in the book, hence do-while
            q = hash(key, i);
            if (table[q] == key) {
                return q;
            }
            else i++;
        } while (table[q] != null && i < table.length);
        return -1;
    }

    /**
     * A modulous function that always returns a positive number.
     * 
     * @param dividend  The dividend
     * @param divisor   The divisor
     * @return          The positive modulus
     */
    protected int positiveMod (int dividend, int divisor) {
        int quotient = dividend % divisor;
        if (quotient < 0) {
            quotient += divisor;
        }
        return quotient;
    }

    /**
     * h1 is an auxillary hash function that is common to both linear probing and double hashing.
     * 
     * Since the project spec stated that all common functionality should belong to the parent 
     * abstract class, it is placed here.
     * 
     * @param key   The key to be hashed
     * @return      The hash of the key
     */
    protected int h1(Object key) {
        return positiveMod(key.hashCode(), tableSize);
    }       
}
