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
public class PauseOrPlayController {
  Logger logger = Logger.getLogger(PauseOrPlayController.class);
  
  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/playOrPause")
  public ModelAndView pauseOrPlay(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("CONTROLLER: playOrPause");
    try {
      ServletOutputStream outputStream = response.getOutputStream();
      jukebox.playOrPause();
      outputStream.print("OK");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }

}
