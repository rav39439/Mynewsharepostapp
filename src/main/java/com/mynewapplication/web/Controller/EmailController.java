package com.mynewapplication.web.Controller;

import java.util.Optional;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mynewapplication.web.Services.EmailnewService;
import com.mynewapplication.web.entities.Emailrequest;
import com.mynewapplication.web.entities.InfoUser;
import com.sendgrid.Response;



public class EmailController {

	@Autowired
	private EmailnewService emailservice;
	
//	@PostMapping("/sendemail")
////	public ResponseEntity<String> sendmemail(@RequestBody Emailrequest emailrequest)
////	{
////		
////		System.out.println("the api is hit");
////		Response response=this.emailservice.sendemail(emailrequest);
////		if(response.getStatusCode()==200||response.getStatusCode()==202)
////			return new ResponseEntity<>("send successfully",HttpStatus.OK);
////		return new ResponseEntity<>("failed to sent",HttpStatus.NOT_FOUND);
////			    
////	}
	
	

	
	
}
