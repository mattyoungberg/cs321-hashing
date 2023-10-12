/**
 * An object that can be used as a key in a hash table.
 * 
 * A HashObject additionally keeps track of two counts:
 * 
 * 1) The frequency which a given key was attempted to be inserted by calling code (namely, the 
 * Hashtable). For convenience, this is initialized to 1 assuming that a HashObject is only 
 * constructed when it is ready for insertion.
 * 
 * 2) The number of probes required to initially insert this key. This is expected to be set by the
 * Hashtable, which is aware of how many items of the probe sequence has to be queried within the
 * table before finding an unoccupied insertion point.
 * 
 * @author Matt Youngberg
 */
public class HashObject {
    
    /**
     * The key of this HashObject.
     */
    private Object key;

    /**
     * The count of insertions of this key, including the initial one.
     */
    private int frequencyCount;

    /**
     * The number of probes required to initially insert this key.
     * 
     * This is not affected by duplicate insertions of the same key. In that sense, once set by the
     * method setProbeCount, this value should be traeted as immutable.
     */
    private int probeCount;

    /**
     * Constructs a new HashObject with the given key.
     * 
     * Note: Frequency count is initialized to 1 to count this instance. Probe count is initialized
     * to 0 and is expected to be called by the Hashtable, which is aware of how many items of the
     * probe sequence has to be queried within the table before finding an unoccupied insertion 
     * point.
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
     * Increment the frequency count of this HashObject.
     * 
     * Should be called by the hash table when a duplicate of a key it's trying to insert is found.
     */
    public void incrementFrequency() {
        this.frequencyCount++;
    }
    
    /**
     * Retrieves the frequency of insertion attempts for the key represented by this HashObject.
     * 
     * @return The frequency of insertion attempts for the key represented by this HashObject
     */
    public int getFrequencyCount() {
        return frequencyCount;
    }
    
    /**
     * Set the probe count of this HashObject.
     * 
     * This should be called by the hash table when it is aware of how many items of the probe
     * sequence has to be queried within the table before finding an unoccupied insertion point.
     * 
     * @param probeCount The probe count of this HashObject
     */
    public void setProbeCount(int probeCount) {
        this.probeCount = probeCount;
    }
    
    /**
     * Get the probe count of this HashObject.
     * 
     * @return The probe count of this HashObject
     */
    public int getProbeCount() {
        return probeCount;
    }

    /**
     * Returns true if the given object is a HashObject with the same key as this one.
     * 
     * Note: The project spec asks us to create this method, but I found its only use in the
     * Hashtable.insert() method. Within, I could have just compared the keys directly in that
     * method (as opposed to creating a passing reference to a new HashObject just for the sake of
     * comparison), but the spec wanted me to use this method somewhere, so I used it there.
     * 
     * @param obj The object to compare to this one
     * @return    Wehter the given object is a HashObject with the same key as this one
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
