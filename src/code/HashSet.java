package code;

import given.iPrintable;
import given.iSet;

/*
 * A set class implemented with hashing. Note that there is no "value" here 
 * 
 * You are free to implement this however you want. Two potential ideas:
 * 
 * - Use a hashmap you have implemented with a dummy value class that does not take too much space
 * OR
 * - Re-implement the methods but tailor/optimize them for set operations
 * 
 * You are not allowed to use any existing java data structures
 * 
 */

public class HashSet<Key> implements iSet<Key>, iPrintable<Key>{
  
  // A default public constructor is mandatory!

  HashMapDH<Key, Byte> innerMap;
	
  public HashSet() {
   /*
    * Add code here 
    */
	innerMap = new HashMapDH	<Key, Byte>();  
  }
  
  /*
   * 
   * Add whatever you want!
   * 
   */

  @Override
  public int size() {
    return innerMap.size();
  }

  @Override
  public boolean isEmpty() {
    if(innerMap.size() == 0) {
    		return true;
    } else {
    		return false;
    }
  }

  @Override
  public boolean contains(Key k) {
    if(innerMap.get(k) != null) {
    		return true;
    } else {
    		return false;
    }
  }

  @Override
  public boolean put(Key k) {
	byte v = 1;  
	if(innerMap.put(k, v) != null) {
		return true;
	} else {
		return false;
	}
  }

  @Override
  public boolean remove(Key k) {
    if(innerMap.get(k) != null) {
    		innerMap.remove(k);
    		return true; 
    } else {
    		return false;
    }
  }

  @Override
  public Iterable<Key> keySet() {
    return innerMap.keySet();
	  
  }

  @Override
  public Object get(Key key) {
    // Do not touch
    return null;
  }

}
