package com.mindbadger.spring.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.library.Librarian;

@Controller
public class GetLibraryController {
  
  @Autowired
  private Librarian librarian;
  
  /*
   * http://localhost:1970/AudioServer/svr/getLibrary
   */
  
	@RequestMapping("/getLibrary")
	public ModelAndView getLibrary(HttpServletRequest request, HttpServletResponse response) {
		String message = "getLibrary!";
		
		try {
      AudioserverDocument doc = librarian.getXml();
      doc.save(response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
		
		return null;
	}
}
