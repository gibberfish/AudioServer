package com.mindbadger.wmp;

import java.util.Map;

import com.mindbadger.library.Artist;

public interface IReadTheWmpLibrary {
  public Map<String, Artist> readLibrary (JacobAdapter adapter);
}
