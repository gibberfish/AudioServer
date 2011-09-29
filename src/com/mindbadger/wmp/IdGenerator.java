package com.mindbadger.wmp;

public class IdGenerator {
  private long id = 0;
  
  public long getNextId () {
    return ++id;
  }

	public void seedCurrentValue(long largestId) {
	  id = largestId;
  }
}
