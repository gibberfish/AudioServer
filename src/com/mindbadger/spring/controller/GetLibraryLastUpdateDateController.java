package com.mindbadger.spring.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mindbadger.audioserver.schema.AudioserverDocument;
import com.mindbadger.library.Librarian;

@Controller
public class GetLibraryLastUpdateDateController {

  @Autowired
  private Librarian librarian;
  
  @RequestMapping("/getLastUpdateDate")
  public ModelAndView getLibraryLastUpdateDate(HttpServletRequest request, HttpServletResponse response) {
    try {
      AudioserverDocument doc = librarian.getXml();
      
      Calendar calendar = doc.getAudioserver().getLastUpdated();
      
      ServletOutputStream outputStream = response.getOutputStream();
      outputStream.print(calendar.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }

}
