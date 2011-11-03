package com.mindbadger.spring.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mindbadger.jukebox.Jukebox;

@Controller
public class ClearPlaylistController {

  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/clearPlaylist")
  public ModelAndView clearPlaylist(HttpServletRequest request, HttpServletResponse response) {
    try {
      ServletOutputStream outputStream = response.getOutputStream();
      jukebox.clearPlaylist();
      outputStream.print("OK");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }

}
