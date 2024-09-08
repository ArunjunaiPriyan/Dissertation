package com.magna.user.controller;

import java.util.List;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.magna.user.service.UserService; 

@Controller 
@ResponseBody
@RequestMapping("/")
public class UserController { 
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userservice;
	
	  @CrossOrigin
	  @PostMapping(value = "/loginSecure")
	    public String loginUser(@RequestParam("username") String username,@RequestParam("userType") String userType) {
		 
		  String result = null;
		  try {
			  result = userservice.getUserDtl(username,userType);
		  }catch(Exception ex) {
			  logger.error("login method exception"+ ex);
		  }
	        return result;
	    }
	  	
	  // @Scheduled(fixedDelay = 1000, initialDelay = 1000)
	  @CrossOrigin
	  @PostMapping(value = "/setpws")
	    public String setpws() {
		 
		  String result = null;
		  try {
			  result = userservice.setpws();
		  }catch(Exception ex) {
			  logger.error("setpws method exception"+ ex);
		  }
	        return result;
	    }
	  
	  @CrossOrigin
	  @PostMapping(value = "/setNewPassword")
	    public String setNewPassword(@RequestParam("username") String username,@RequestParam("codePass") String codePass
	    		,@RequestParam("userType") String userType,@RequestParam("codeNew") String codeNew) {
		 
		  String result = null;
		  try {
			  result = userservice.setNewPassword(username,codePass,userType,codeNew);
		  }catch(Exception ex) {
			  logger.error("setNewPassword method exception"+ ex);
		  }
	        return result;
	    }
	  
	  @CrossOrigin
	  @PostMapping(value = "/loginSecureForSap")
	    public List<String> loginUserForSap(@RequestParam("username") String username,@RequestParam("userType") int userType) {
		 
		  List<String> result = null;
		  try {
			  result = userservice.getUserDtlForSap(username,userType);
		  }catch(Exception ex) {
			  logger.error("login method exception"+ ex);
		  }
	        return result;
	    }
}
