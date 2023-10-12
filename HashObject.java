/**
 * An object that can be used as a key in a hash table.
 * 
 * @author Matt Youngberg
 */
public class HashObject {
    
    /**
     * The key of this HashObject.
     */
    private Object key;

    /**
     * The amount of duplicates counted.
     */
    private int frequencyCount;

    /**
     * The amount of times the hash table was probed.
     */
    private int probeCount;

    /**
     * Constructs a new HashObject with the given key.
     * 
     * @param key The key of this HashObject
     */
    public HashObject(Object key) {
        this.key = key;
        this.frequencyCount = 1;
        this.probeCount = 0;
    }

    /**
     * Returns the key of this HashObject.
     * 
     * @return The key of this HashObject
     */
    public Object getKey() {
        return key;
    }

    /**
     * TODO
     */
    public void incrementFrequency() {
        this.frequencyCount++;
    }
    
    /**
     * TODO
     * @return
     */
    public int getFrequencyCount() {
        return frequencyCount;
    }
    
    /**
     * TODO
     */
    public void setProbeCount(int probeCount) {
        this.probeCount = probeCount;
    }
    
    /**
     * TODO
     * @return
     */
    public int getProbeCount() {
        return probeCount;
    }

    /**
     * Returns true if the given object is a HashObject with the same key as this one.
     * 
     * @param obj The object to compare to this one
     * @return    True if the given object is a HashObject with the same key as this one
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HashObject) {
            return ((HashObject) obj).getKey().equals(key);
        }
        return false;
    }

    /**
     * Get the string representation of this HashObject.
     * 
     * @return The string representation of this HashObject
     */
    @Override
    public String toString() {
        return key.toString() + " " + frequencyCount + " " + probeCount;
    }
}
