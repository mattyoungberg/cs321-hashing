import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is a test driver for the Hashtable class.
 * 
 * See the printUsage() method for command line arguments.
 * 
 * @author Matt Youngberg
 */
public class HashtableTest {

    /**
     * Inner class to parse and store command line arguments
     */
    private static class TestArgs {
        /**
         * The data source to use
         * 
         * 1 ==> random numbers
         * 2 ==> date value as a long
         * 3 ==> word list
         */
        int dataSource;

        /**
         * The ratio of objects to table size, denoted by alpha = n/m
         */
        double loadFactor;

        /**
         * The debug level to use
         *
         * 0 ==> print summary of experiment
         * 1 ==> save the two hash tables to a file at the end
         * 2 ==> print debugging output for each insert
         */
        int debugLevel;
    }

    /**
     * Interface for generating test data.
     * 
     * This interface is used to abstract the source of test data from the test driver.
     */
    private interface TestDataGenerator<T> {

        /**
         * Get the next test data object
         * 
         * @return The next test data object, or Optional.empty() if there are no more objects
         */
        public Optional<T> getNext();

        /**
         * Get the name of the input source for the sake of summary information.
         * 
         * @return The name of the input source
         */
        public String getInputName();

        /**
         * Reset the generator to the beginning of the input source.
         */
        public void reset();

        /**
         * Close the generator
         */
        public void close();
    }

    /**
     * Generates random integers.
     */
    private static class RandomIntGenerator implements TestDataGenerator<Integer> {
        
        /**
         * Random instance for generating random integers
         */
        private Random random = new Random();

        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<Integer> getNext() {
            return Optional.of(random.nextInt());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getInputName() { return "Random-Numbers"; }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reset() {}  // Can't control random output w/o seed, so do nothing

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() {}
    }

    /**
     * Generates a sequence of dates, starting from the current date and time.
     */
    private static class DateSequenceGenerator implements TestDataGenerator<Date> {
        private long current = new Date().getTime();

        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<Date> getNext() {
            current += 1000; //increase by 1 second (1000 ms)
            return Optional.of(new Date(current));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getInputName() { return "Random-Dates"; }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reset() { current = new Date().getTime(); }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() {}
    }

    /**
     * Generates words from a file.
     */
    private static class WordFileGenerator implements TestDataGenerator<String> {
        /**
         * Scanner for reading words from a file.
         */
        private Scanner scnr;

        /**
         * Construct a WordFileGenerator.
         * 
         * @param fileName  The name of the file to read words from. The file should contain one
         *                  word per line.
         */
        public WordFileGenerator(String fileName) {
            reset();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getInputName() { return "Word-List"; }

        /**
         * {@inheritDoc}
         */
        @Override
        public Optional<String> getNext() {
            if (scnr != null && scnr.hasNext()) {
                return Optional.of(scnr.next());
            }
            return Optional.empty();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void reset() {
            if (scnr != null) {
                scnr.close();
            }
            try {
                scnr = new Scanner(new File("word-list.txt"));  // hardcoded to this file
            } catch (FileNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Error: " + e.getMessage(), e);
            }
            scnr.useDelimiter(System.lineSeparator());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() {
            if (scnr != null) {
                scnr.close();
                scnr = null;
            }
        }
    }

    // TODO remove
    private static class RepeatingIntGenerator implements TestDataGenerator<Integer> {

        @Override
        public Optional<Integer> getNext() { return Optional.of(1); }

        @Override
        public String getInputName() { return "Repeating-Integers"; }

        @Override
        public void reset() {}

        @Override
        public void close() {}
        
    }
    
    /**
     * Main method; parse command line arguments and run the test.
     * 
     * @param args Command line arguments. See printUsage() for details.
     */
    public static void main(String[] args) {
        // Parse arguments from the command line
        TestArgs testArgs = new TestArgs();
        parseArgs(args, testArgs);

        // Find table size; print
        int tableSize = TwinPrimeGenerator.generateTwinPrime(95500, 96000);
        // int tableSize = TwinPrimeGenerator.generateTwinPrime(10, 100);  // TODO remove
        System.out.println("HashtableTest: Found a twin prime table capacity: " + tableSize);


        Hashtable linearProbingHashTable = new LinearProbing(tableSize);
        Hashtable doubleHashingHashTable = new DoubleHashing(tableSize);

        // Build TestDataGenerator
        TestDataGenerator<?> testDataGenerator = buildTestDataGenerator(testArgs);
        // TestDataGenerator<?> testDataGenerator = new RepeatingIntGenerator();  // TODO remove

        // Print summary information
        System.out.println("HashtableTest: Input: " + testDataGenerator.getInputName() + "\tLoadfactor: " + String.format("%.2f", testArgs.loadFactor));

        // Calculate load factor and number of objects to insert
        int numObjects = (int) Math.ceil(testArgs.loadFactor * tableSize);

        // Run tests
        runTest("Linear Probing", linearProbingHashTable, numObjects, testDataGenerator, testArgs, "linear-dump.txt");  // Linear Probing
        testDataGenerator.reset();  // Reset the generator
        runTest("Double Hashing", doubleHashingHashTable, numObjects, testDataGenerator, testArgs, "double-dump.txt");  // Double Hashing

        // Close the generator
        testDataGenerator.close();
    }

    /**
     * Parse command line arguments and store them in the TestArgs object.
     * 
     * @param args      Command line arguments. See printUsage() for details.
     * @param testArgs  TestArgs object to store the parsed arguments
     */
    private static void parseArgs(String[] args, TestArgs testArgs) {
        if (args.length < 2 || args.length > 3) {
            exitWithError();
        }
    
        try {
            testArgs.dataSource = Integer.parseInt(args[0]);
            testArgs.loadFactor = Double.parseDouble(args[1]);
            // Set default value for debugLevel
            testArgs.debugLevel = 0;
            if (args.length == 3) {
                testArgs.debugLevel = Integer.parseInt(args[2]);
            }
        } catch (NumberFormatException e) {
            exitWithError();
        }
    
        if ((testArgs.dataSource < 1 || testArgs.dataSource > 3) || 
            (testArgs.loadFactor < 0.0 || testArgs.loadFactor > 1.0) || 
            (testArgs.debugLevel < 0 || testArgs.debugLevel > 2)) {
            exitWithError();
        }
    }
    
    /**
     * Print the usage message.
     */
    private static void printUsage() {
            System.out.println("Usage: java HashtableTest <dataType> <loadFactor> [<debugLevel>]");
            System.out.println("       <dataSource>: 1 ==> random numbers");
            System.out.println("                     2 ==> date value as a long");
            System.out.println("                     3 ==> word list");
            System.out.println("       <loadFactor>: The ratio of objects to table size, ");
            System.out.println("                       denoted by alpha = n/m");
            System.out.println("       <debugLevel>: 0 ==> print summary of experiment");
            System.out.println("                     1 ==> save the two hash tables to a file at the end");
            System.out.println("                     2 ==> print debugging output for each insert");
    }
    
    /**
     * Print the usage message and exit with error code 1.
     */
    private static void exitWithError() {
        printUsage();
        System.exit(1);
    }

    private static TestDataGenerator<?> buildTestDataGenerator(TestArgs testArgs) {
        switch (testArgs.dataSource) {
            case 1:
                return new RandomIntGenerator();
            case 2:
                return new DateSequenceGenerator();
            case 3:
                return new WordFileGenerator("word-list.txt");  // hard coded to this file
            default:
                throw new RuntimeException("Invalid data source: " + testArgs.dataSource);
        }
    }

    private static void runTest(String using, Hashtable table, int numObjects, TestDataGenerator<?> testDataGenerator, TestArgs testArgs, String fileName) {
        System.out.println("\tUsing " + using);
        System.out.println("HashtableTest: size of hash table is " + numObjects);
        loadHashtable(table, numObjects, testDataGenerator, testArgs);
        int insertions = table.getInsertionCount();
        int duplicates = table.getDuplicateCount();
        System.out.println("\tInserted " + insertions + " elements, of which " + duplicates + " were duplicates");
        System.out.println("\tAvg. no. of probes = " + String.format("%.2f", table.getAverageProbes()));
        if (testArgs.debugLevel == 1) {
            try {
                table.dumpToFile(fileName);
                System.out.println("HashtableTest: Saved dump of hash table");
            } catch (FileNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Error: " + e.getMessage(), e);
            }
        }
    }

    private static void loadHashtable(Hashtable table, int numObjects, TestDataGenerator<?> testDataGenerator, TestArgs testArgs) {
        int insertedObjects = 0;
        while (insertedObjects < numObjects) {
            Optional<?> next = testDataGenerator.getNext();
            if (next.isPresent()) {
                Object key = next.get();
                int idx = table.insert(key);
                if (idx != -1) {
                    insertedObjects++;
                }
                if (testArgs.debugLevel == 2) {
                    if (idx == -1) {
                        System.out.println("Duplicate: " + key);
                    } else {
                        System.out.println("Inserted: " + key);
                    }
                }
            } else {  // Implies that the generator is exhausted; shouldn't happen
                break;
            }
        }
    }
}
