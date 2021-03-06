package code;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

import given.AbstractHashMap;
import given.HashEntry;

/*
 * The file should contain the implementation of a hashmap with:
 * - Separate Chaining for collision handling
 * - Multiply-Add-Divide (MAD) for compression: (a*k+b) mod p
 * - Java's own linked lists for the secondary containers
 * - Resizing (to double its size) and rehashing when the load factor gets above a threshold
 *   Note that for this type of hashmap, load factor can be higher than 1
 * 
 * Some helper functions are provided to you. We suggest that you go over them.
 * 
 * You are not allowed to use any existing java data structures other than for the buckets (which have been 
 * created for you) and the keyset method
 */

public class HashMapSC<Key, Value> extends AbstractHashMap<Key, Value> {

  // The underlying array to hold hash entry Lists
  private LinkedList<HashEntry<Key, Value>>[] buckets;

  // Note that the Linkedlists are still not initialized!
  @SuppressWarnings("unchecked")
  protected void resizeBuckets(int newSize) {
    // Update the capacity
    N = nextPrime(newSize);
    buckets = (LinkedList<HashEntry<Key, Value>>[]) Array.newInstance(LinkedList.class, N);
  }

  /*
   * ADD MORE FIELDS IF NEEDED
   * 
   */

  // The threshold of the load factor for resizing
  protected float criticalLoadFactor;

  /*
   * ADD A NESTED CLASS IF NEEDED
   * 
   */

  public int hashValue(Key key, int iter) {
    return hashValue(key);
  }

  public int hashValue(Key key) {
    // TODO: Implement the hashvalue computation with the MAD method. Will be almost
    // the same as the primaryHash method of HashMapDH
	int hC = Math.abs(key.hashCode());
	int MADhC = ((this.a*hC + this.b) % this.P) % this.N;
    return Math.abs(MADhC);
  }

  // Default constructor
  public HashMapSC() {
    this(101);
  }

  public HashMapSC(int initSize) {
    // High criticalAlpha for representing average items in a secondary container
    this(initSize, 10f);
  }

  public HashMapSC(int initSize, float criticalAlpha) {
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

  @Override
  public Value get(Key k) {
    if(k == null) return null;
    if(buckets[hashValue(k)] != null) {
	    for(int i=0; i<buckets[hashValue(k)].size(); i++) {
			if(buckets[hashValue(k)].get(i).getKey().equals(k)) {
				return buckets[hashValue(k)].get(i).getValue();
			}
	    }
    }
    return null;
  }

  @Override
  public Value put(Key k, Value v) {
	checkAndResize();  
    if(k == null) return null;
    HashEntry<Key, Value> ent2BeAdded = new HashEntry<Key, Value>(k, v);
    if(buckets[hashValue(k)] == null) {
    		LinkedList<HashEntry<Key, Value>> list2BeAdded = new LinkedList<HashEntry<Key, Value>>();
    		list2BeAdded.add(ent2BeAdded);
    		buckets[hashValue(k)] = list2BeAdded;
    		this.n++;
    		return null;
    } else {
    		for(int i=0; i<buckets[hashValue(k)].size(); i++) {
    			if(buckets[hashValue(k)].get(i).getKey().equals(k)) {
    				Value val2BeRet = buckets[hashValue(k)].get(i).getValue();
    				buckets[hashValue(k)].get(i).setValue(v);
    				return val2BeRet;
    			}
    		}
    		buckets[hashValue(k)].add(ent2BeAdded);
    		this.n++;
    		return null;
    }
  }

  @Override
  public Value remove(Key k) {
    if(k == null) return null;
    if(buckets[hashValue(k)] != null) {
    		for(int i=0; i<buckets[hashValue(k)].size(); i++) {
    			if(buckets[hashValue(k)].get(i).getKey().equals(k)) {
    				Value val2BeRet = buckets[hashValue(k)].get(i).getValue();
    				buckets[hashValue(k)].remove(i);
    				if(buckets[hashValue(k)].size() == 0) {
    	    				buckets[hashValue(k)] = null;
    	    			}
    				this.n--;
    				return val2BeRet;
    			}
    		}
    }
    return null;
  }

  @Override
  public Iterable<Key> keySet() {
	  List<Key> list2BeRet = new LinkedList<Key>();
	  for(int i=0; i<buckets.length; i++) {
		  if(buckets[i] != null) {
			  for(HashEntry<Key, Value> hE : buckets[i]) {
				  list2BeRet.add(hE.getKey());
			  }
		  }
	  }
	  return list2BeRet;
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
	    	int j=0;
    		for(int i=0; i<buckets.length; i++) {
    			if(buckets[i] != null) {
    				for(HashEntry<Key, Value> hE : buckets[i]) {
    					buffBucket[j] = hE;
    					j++;
    				}
    			}
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
}
