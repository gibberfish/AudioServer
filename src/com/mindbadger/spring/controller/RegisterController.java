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
public class RegisterController {

  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/registerWithServer")
  public ModelAndView registerWithServer(HttpServletRequest request, HttpServletResponse response) {
    try {
      ServletOutputStream outputStream = response.getOutputStream();
      
      String ipAddress = request.getParameter("ipAddress");
      
      if (ipAddress != null) {
        //jukebox.addItemToPlaylist(Integer.parseInt(ipAddress));
        //register();
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