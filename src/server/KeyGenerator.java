package server;

import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;

/**
 * Class used to generate two pairs of keys,one public and one private
 * @author Giachetto Daniele
 *
 */
public class KeyGenerator extends Thread{
	
	private BigInteger firstPrime;
	private BigInteger secondPrime;
	private BigInteger n;
	private BigInteger z;
	private BigInteger e;
	private BigInteger d;
	
	/**
	 * Constructor used to start the thread
	 */
	public KeyGenerator() {
		start();
	}
	@Override
	public void run() {
		
		
		generatePrimes();
		
		//privata = d,n
		//pubblica = e,n
		
		n = firstPrime.multiply(secondPrime);
		
		
		z = secondPrime.subtract(BigInteger.ONE).multiply((firstPrime.subtract(BigInteger.ONE)));

		
		e = coPrime(z);
			   
		d = e.modInverse(z);
		
		System.out.println("n :" + n );
		
		System.out.println("z :" + z);
		
		System.out.println("e : " + e);
		
		System.out.println("d : " + d);
		   

	}
	
	/**
	 * Method used to generate two Prime numbers different from each other
	 */
	private void generatePrimes() {
		//Generate prime numbers with 192 bits (16 seconds maximum to generate)
		firstPrime = BigInteger.probablePrime(192, new Random());
		secondPrime = BigInteger.probablePrime(192, new Random());
		while (true) {
			//Check if they are the same
			if (firstPrime.compareTo(secondPrime) !=0) {
				//Check if they are truly prime
				if (firstPrime.isProbablePrime(1)) {
					if (secondPrime.isProbablePrime(1)) {
						break;
					}else {
						secondPrime = BigInteger.probablePrime(192, new Random());
					}
				}else {
					firstPrime = BigInteger.probablePrime(192, new Random());
				}
			}else {
				firstPrime = BigInteger.probablePrime(192, new Random());
				secondPrime = BigInteger.probablePrime(192, new Random());
			}
		}
	}
	
	/**
	 * Method used to generate coPrime of a given prime
	 * @param firstPrime prime from which it gets coprime
	 * @return BigInteger coprime
	 */
	private BigInteger coPrime(BigInteger firstPrime) {
		BigInteger coPrime;
		while ((coPrime = BigInteger.probablePrime(192, new Random())).compareTo(firstPrime)>=0) {
		}
		return coPrime;
		
	}
	
	/**
	 * Getter used to get the pair of public keys
	 * @return Vector of two BigInteger keys
	 */
	public Vector<BigInteger> getPublicKeys() {
		Vector<BigInteger> toReturn = new Vector<BigInteger>();
		toReturn.add(e);
		toReturn.add(n);
		return toReturn;
	}
	
	/**
	 * Getter used to get the pair of private keys
	 * @return Vector of two BigInteger keys
	 */
	public Vector<BigInteger> getPrivateKeys() {
		Vector<BigInteger> toReturn = new Vector<BigInteger>();
		toReturn.add(d);
		toReturn.add(n);
		return toReturn;
	}
	
	/**
	 * Getter used to get both pairs of keys.
	 * @return Vector of four BigInteger keys
	 */
	public Vector<BigInteger> getAllKeys() {
		Vector<BigInteger> toReturn = new Vector<BigInteger>();
		toReturn.add(d);
		toReturn.add(e);
		toReturn.add(n);
		return toReturn;
		
	}
	
	

}
