package com.mindbadger.wmp;

public class IdGenerator {
  private int id = 0;
  
  public int getNextId () {
    return ++id;
  }

	public void seedCurrentValue(int largestId) {
	  id = largestId;
  }
}
