package com.mindbadger.spring.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GetLibraryController {
	@RequestMapping("/getLibrary")
	public ModelAndView getLibrary(HttpServletRequest request, HttpServletResponse response) {
		String message = "getLibrary!";
		//return new ModelAndView("byebye", "byemessage", message);
		
		try {
      response.getOutputStream().print("GET LIBRARY!");
    } catch (IOException e) {
      e.printStackTrace();
    }
		
		return null;
	}
}
