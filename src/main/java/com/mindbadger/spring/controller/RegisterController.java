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

import com.mindbadger.broadcast.StatusBroadcaster;
import com.mindbadger.jukebox.Jukebox;

@Controller
public class RegisterController {
  Logger logger = Logger.getLogger(RegisterController.class);
  
  @Autowired
  private StatusBroadcaster statusBroadcaster;
  
  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/registerWithServer")
  public ModelAndView registerWithServer(HttpServletRequest request, HttpServletResponse response) {
    String ipAddress = request.getParameter("ipAddress");
    logger.debug("CONTROLLER: registerWithServer, ipAddress="+ipAddress);

    try {
      ServletOutputStream outputStream = response.getOutputStream();
      
      if (ipAddress != null) {
        statusBroadcaster.register(ipAddress);
        jukebox.broadcastStatus();
        outputStream.print("OK");
      } else {
        outputStream.print("NO PARAM");
      }
      
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }

}
