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
public class AddItemsToPlaylistController {

  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/addItemToPlaylist")
  public ModelAndView getLibraryLastUpdateDate(HttpServletRequest request, HttpServletResponse response) {
    try {
      ServletOutputStream outputStream = response.getOutputStream();
      
      String itemId = request.getParameter("id");
      System.out.println("addItemToPlaylist, id=" + itemId);
      
      if (itemId != null) {
        jukebox.addItemToPlaylist(Integer.parseInt(itemId));
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
