package code;

import java.lang.reflect.Array;
import java.util.*;

import given.AbstractHashMap;
import given.HashEntry;
import given.iPrintable;

/*
 * The file should contain the implementation of a hashmap with:
 * - Open addressing for collision handling
 * - Double hashing for probing. The double hash function should be of the form: q - (k mod q)
 * - Multiply-Add-Divide (MAD) for compression: (a*k+b) mod p
 * - Resizing (to double its size) and rehashing when the load factor gets above a threshold
 * 
 * Some helper functions are provided to you. We suggest that you go over them.
 * 
 * You are not allowed to use any existing java data structures other than for the keyset method
 */

public class HashMapDH<Key, Value> extends AbstractHashMap<Key, Value> {

  // The underlying array to hold hash entries (see the HashEntry class)
  private HashEntry<Key, Value>[] buckets;
  private HashEntry<Key, Value> DEFUNCT = new HashEntry<Key, Value>(null, null);

  @SuppressWarnings("unchecked")
  protected void resizeBuckets(int newSize) {
    // Update the capacity
    N = nextPrime(newSize);
    buckets = (HashEntry<Key, Value>[]) Array.newInstance(HashEntry.class, N);
  }

  // The threshold of the load factor for resizing
  protected float criticalLoadFactor;

  // The prime number for the secondary hash
  int dhP;

  /*
   * ADD MORE FIELDS IF NEEDED
   * 
   */

  /*
   * ADD A NESTED CLASS IF NEEDED
   * 
   */

  // Default constructor
  public HashMapDH() {
    this(101);
  }

  public HashMapDH(int initSize) {
    this(initSize, 0.6f);
  }

  public HashMapDH(int initSize, float criticalAlpha) {
    N = initSize;
    criticalLoadFactor = criticalAlpha;
    resizeBuckets(N);

    // Set up the MAD compression and secondary hash parameters
    updateHashParams();

    /*
     * ADD MORE CODE IF NEEDED
     * 
     */
  }

  /*
   * ADD MORE METHODS IF NEEDED
   * 
   */

  /**
   * Calculates the hash value by compressing the given hashcode. Note that you
   * need to use the Multiple-Add-Divide method. The class variables "a" is the
   * scale, "b" is the shift, "mainP" is the prime which are calculated for you.
   * Do not include the size of the array here
   * 
   * Make sure to include the absolute value since there maybe integer overflow!
   */
  protected int primaryHash(int hashCode) {
    // TODO: Implement MAD compression given the hash code, should be 1 line
	int pH = ((this.a*hashCode + this.b) % this.P) % this.N;  
    return Math.abs(pH);
  }

  /**
   * The secondary hash function. Remember you need to use "dhP" here!
   * 
   */
  protected int secondaryHash(int hashCode) {
    int sH = this.dhP - (hashCode % this.dhP);
    return Math.abs(sH);
  }

  @Override
  public int hashValue(Key key, int iter) {
    int k = Math.abs(key.hashCode());
    return Math.abs(primaryHash(k) + iter * secondaryHash(k)) % N;
  }

  /**
   * checkAndResize checks whether the current load factor is greater than the
   * specified critical load factor. If it is, the table size should be increased
   * to 2*N and recreate the hash table for the keys (rehashing). Do not forget to
   * re-calculate the hash parameters and do not forget to re-populate the new
   * array!
   */
  protected void checkAndResize() {
    if (loadFactor() > criticalLoadFactor) {
      // TODO: Fill this yourself
    		@SuppressWarnings("unchecked")
			HashEntry<Key, Value>[] buffBucket = (HashEntry<Key, Value>[]) Array.newInstance(HashEntry.class, N);
    		
    		for(int i=0; i<buckets.length; i++) {
    			buffBucket[i] = buckets[i];
    		}
    		
    		resizeBuckets(2*N);
    		updateHashParams();
    		n=0;
    		
    		for(HashEntry<Key, Value> hE : buffBucket) {
    			if(hE != null) {
    				put(hE.getKey(), hE.getValue());
    			}
    		}

    }
  }

  @Override
  public Value get(Key k) {
	int iter = 0;  
    if(k == null) return null;
    while(buckets[hashValue(k,iter)] != null) {
    		if(buckets[hashValue(k, iter)] != DEFUNCT && buckets[hashValue(k,iter)].getKey().equals(k)) {
    			return buckets[hashValue(k,iter)].getValue();
    		}
    		iter++;
    }
    return null;
  }

  @Override
  public Value put(Key k, Value v) {  
    int iter = 0;
    if(k == null) return null;
    while(buckets[hashValue(k,iter)] != null) {
    		if(buckets[hashValue(k, iter)] != DEFUNCT && buckets[hashValue(k, iter)].getKey().equals(k)) {
    			Value val2BeRet = buckets[hashValue(k, iter)].getValue();
    			buckets[hashValue(k, iter)].setValue(v);
    			return val2BeRet;
    		}
    		iter++;
    }
    buckets[hashValue(k, iter)] = new HashEntry<Key, Value>(k, v);
    this.n++;
    checkAndResize();
    return null;
  }

  @Override
  public Value remove(Key k) {
    int iter = 0;
    if(k == null) return null;
	while(buckets[hashValue(k,iter)] != null) {
		if(buckets[hashValue(k, iter)] != DEFUNCT && buckets[hashValue(k, iter)].getKey().equals(k)) {
			 HashEntry<Key, Value> buffEnt = buckets[hashValue(k, iter)];
			 Value val2BeRet = buffEnt.getValue();
			 buckets[hashValue(k, iter)] = DEFUNCT;
			 this.n--;
			 return val2BeRet;
		}
		iter++;
    }
	return null;
  }

  // This is the only function you are allowed to use an existing Java data
  // structure!
  @Override
  public Iterable<Key> keySet() {
	  
	List<Key> list2BeRet = new ArrayList<Key>();
	for(int i=0; i<this.N; i++) {
		if(buckets[i] != null) {
			list2BeRet.add(buckets[i].getKey());
		}
	}
	return list2BeRet;
	
  }

  @Override
  protected void updateHashParams() {
    super.updateHashParams();
    dhP = nextPrime(N / 2);
  }

}
