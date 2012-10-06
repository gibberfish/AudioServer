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
import com.mindbadger.jukebox.PlaylistRandomiser;

@Controller
public class AddItemsToPlaylistController {
  Logger logger = Logger.getLogger(AddItemsToPlaylistController.class);
  
  @Autowired
  private Jukebox jukebox;
  
  @RequestMapping("/addItemToPlaylist")
  public ModelAndView getLibraryLastUpdateDate(HttpServletRequest request, HttpServletResponse response) {
    try {
      ServletOutputStream outputStream = response.getOutputStream();
      
      String itemId = request.getParameter("id");
      logger.debug("CONTROLLER: addItemToPlaylist, id=" + itemId);
      
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
