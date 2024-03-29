package com.mindbadger.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mindbadger.jukebox.Jukebox;
import com.mindbadger.library.Librarian;

@Controller
public class GetArtworkController {
  Logger logger = Logger.getLogger(GetArtworkController.class);
  
  @Autowired
  private Librarian librarian;
  
  @Autowired
  private Jukebox jukebox;
  
  /*
   * http://localhost:1970/AudioServer/svr/getLibrary
   */
  
	@RequestMapping("/getArtwork")
	public ModelAndView getArtwork(HttpServletRequest request, HttpServletResponse response) {
    String itemId = request.getParameter("id");
    logger.debug("CONTROLLER: getArtwork, id=" + itemId);
    
    String filename = jukebox.getArtworkForTrack(Integer.parseInt(itemId));
	  //String filename = "C:\\Music\\AC-DC\\Blow Up Your Video\\AlbumArtSmall.jpg";
	  
	  FileNameMap fileNameMap = URLConnection.getFileNameMap();
	  String mimeType = fileNameMap.getContentTypeFor(filename);

	  try
	  {
	    // Set content type
	    response.setContentType(mimeType);
	    
	    // Set content size
	    File file = new File(filename);
	    response.setContentLength((int)file.length());
	    
	    // Open the file and output streams
	    FileInputStream in = new FileInputStream(file);
	    OutputStream out = response.getOutputStream();
	    
	    // Copy the contents of the file to the output stream
	    byte[] buf = new byte[1024];
	    int count = 0;
	    while ((count = in.read(buf)) >= 0) {
	      out.write(buf, 0, count);
	    }
	    in.close();
	    out.close();
	  } catch (Exception e) {
	    logger.error("Error getting artwork: ");
	    e.printStackTrace();
	  }
		
		return null;
	}
}
