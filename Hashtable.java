import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * An abstract Hashtable class.
 * 
 * Implementations must define the hash method, which will define how the table's probe sequence is
 * generated. Insertions and searching are methods that are common to all hash tables, so they are
 * defined here. Additionally, implementations get access to the table and size itself, as well
 * as methods that can dump to a file and provide summary statistics.
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
     * Returns -1 if the given key would duplicate one already in the table.
     * 
     * Throws a RuntimeException if the table is full.
     * 
     * @param key   The key to be inserted
     * @return      The index of the inserted key
     */
    public int insert(Object key) {
        int i = 0;
        int retIdx = -1;
        while (i < table.length) {
            int q = hash(key, i);
            if (table[q] == null) {  // Insertion point found; will break
                HashObject hashObj = new HashObject(key);
                hashObj.setProbeCount(i + 1);
                table[q] = hashObj;
                retIdx = q;
                break;  // Insertion complete
            } else {  // Already occupied; might be duplicate, in which case, exit
                if (table[q].equals(new HashObject(key))) {  // Could compare the keys directly here, but the spec wants me to use this method somewhere.
                    table[q].incrementFrequency();
                    break;
                } else {
                    i++;
                    // Keep probing
                }
            }
        }

        if (i == table.length) {
            throw new RuntimeException("Table is full");
        }

        return retIdx;
    }

    /**
     * Search for the given key in the hash table.
     * 
     * @param key   The key to be searched for
     * @return      The index of the key, or -1 if it is not found
     */
    public int search(Object key) {
        int i = 0;
        int currentIdx = hash(key, i);
        
        while (table[currentIdx] != null && i < table.length) {
            if (table[currentIdx].getKey().equals(key)) {
                return currentIdx;
            }
            i++;
            currentIdx = hash(key, i);
        }
        return -1;
    }

    /**
     * A modulous function that always returns a positive number.
     * 
     * @param dividend  The dividend
     * @param divisor   The divisor
     * @return          The positive modulus
     */
    protected int positiveMod(int dividend, int divisor) {
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
    
    /**
     * Dump the hash table to a file.
     * 
     * @param fileName                  The name of the file to dump to
     * @throws FileNotFoundException    If there is an issue writing to the file
     */
    public void dumpToFile(String fileName) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    HashObject hashObj = table[i];
                    out.println("table[" + i + "]: " + hashObj.getKey() + " " 
                        + hashObj.getFrequencyCount() + " " + hashObj.getProbeCount());
                }
            }
        }
    }

    /**
     * Get the amount of attempted insertions into the table.
     * 
     * @return The amount of attempted insertions into the table
     */
    public int getInsertionCount() {
        int insertionCount = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                insertionCount += table[i].getFrequencyCount();
            }
        }
        return insertionCount;
    }

    /**
     * Get the amount of attempted duplicate insertions into the table.
     * 
     * @return The amount of attempted duplicate insertions into the table
     */
    public int getDuplicateCount() {
        int duplicateCount = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                duplicateCount += table[i].getFrequencyCount() - 1;  // Derived, not defined
            }
        }
        return duplicateCount;
    }

    /**
     * Get the average amount each object in the table was probed.
     * 
     * @return  The average amount each object in the table was probed
     */
    public double getAverageProbes() {
        int probeCount = 0;
        int occupiedSlots = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                probeCount += table[i].getProbeCount();
                occupiedSlots++;
            }
        }
        return (double) probeCount / occupiedSlots;
    }
}
