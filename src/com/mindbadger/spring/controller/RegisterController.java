package com.mindbadger.spring.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mindbadger.broadcast.StatusBroadcaster;
import com.mindbadger.jukebox.Jukebox;

@Controller
public class RegisterController {

  @Autowired
  private StatusBroadcaster statusBroadcaster;
  
  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/registerWithServer")
  public ModelAndView registerWithServer(HttpServletRequest request, HttpServletResponse response) {
    try {
      ServletOutputStream outputStream = response.getOutputStream();
      
      String ipAddress = request.getParameter("ipAddress");
      System.out.println("registerWithServer, ipAddress="+ipAddress);
      
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
