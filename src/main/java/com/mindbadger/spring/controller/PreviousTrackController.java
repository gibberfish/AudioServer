package com.mindbadger.spring.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mindbadger.jukebox.Jukebox;

@Controller
public class PreviousTrackController {
  Logger logger = Logger.getLogger(PreviousTrackController.class);
  
  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/previousTrack")
  public ModelAndView previousTrack(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("CONTROLLER: previousTrack");
    try {
      ServletOutputStream outputStream = response.getOutputStream();
      jukebox.previousTrack();
      outputStream.print("OK");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }

}
