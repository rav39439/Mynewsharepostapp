package com.mynewapplication.web.Services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mynewapplication.web.entities.Emailrequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailnewService {

	@Autowired
	SendGrid sendGrid;
	
	public Response sendemail(Emailrequest emailrequest) 
	{
		
		Mail mail = new Mail(new Email("rav39439@gmail.com"), emailrequest.getSubject(), new Email(emailrequest.getTo()),new Content("text/plain", emailrequest.getBody()));
		//mail.setReplyTo(new Email("rav39439@gmail.com"));
		SendGrid sg = new SendGrid("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");
		Request request = new Request();

		Response response = null;

		try {

			request.setMethod(Method.POST);

			request.setEndpoint("mail/send");

			request.setBody(mail.build());

		 response=sg.api(request);;

		} catch (IOException ex) {

			System.out.println(ex.getMessage());

		}

		return response;
		
		
	}

}
