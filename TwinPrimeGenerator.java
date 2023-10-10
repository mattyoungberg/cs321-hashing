/**
 * A class that generates a twin prime in a given range. Use this class via the generateTwinPrime
 * static method.
 * 
 * @author Matt Youngberg
 */
public class TwinPrimeGenerator {

    /**
     * This method smoke tests the generateTwinPrime method with the arguments that HashtableTest 
     * will call it with.
     * 
     * @param args Command line arguments (ignored)
     */
    public static void main(String[] args) {
        // Test the method with the given range
        int twinPrime = generateTwinPrime(95500, 96000);
        System.out.println("The generated twin prime is: " + twinPrime);
    }

    /**
     * Generates a twin prime in the given range.
     * 
     * A twin prime is a prime number that is either 2 less or 2 more than another prime number. In
     * this case, we return the greater of the least two primes.
     * 
     * @param min   The minimum value of the range (inclusive)
     * @param max   The maximum value of the range (inclusive)
     * @return      A twin prime in the given range
     * @throws      IllegalArgumentException If no twin primes are found in the given range
     */
    public static int generateTwinPrime(int min, int max) {
        for (int i = min; i <= max - 2; i++) {
            if (isPrime(i) && isPrime(i + 2)) {
                return i + 2;
            }
        }
        throw new IllegalArgumentException("No twin primes found in the given range");
    }

    /**
     * Returns true if the given number is prime, false otherwise.
     * 
     * @param n The number to check
     * @return  True if the given number is prime, false otherwise
     */
    private static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3; i <= Math.sqrt(n); i += 2) {  // Even numbers already handled
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}