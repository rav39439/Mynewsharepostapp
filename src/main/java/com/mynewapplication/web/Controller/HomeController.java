package com.mynewapplication.web.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.mynewapplication.web.Services.EmailnewService;
import com.mynewapplication.web.dao.Bookrepository;
import com.mynewapplication.web.dao.Commentsrepo;
import com.mynewapplication.web.dao.Databaseusers;
import com.mynewapplication.web.entities.Books;
import com.mynewapplication.web.entities.Comments;
import com.mynewapplication.web.entities.Emailrequest;
import com.mynewapplication.web.entities.InfoUser;
import com.mynewapplication.web.entities.User;
import com.mynewapplication.web.dao.Databaseusers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mynewapplication.web.helpers.Message;
import com.sendgrid.Response;

@Controller
public class HomeController {

	Random random = new Random(1000);
	@Autowired
	private Databaseusers userRepository;
	
	@Autowired
	private EmailnewService emailservice;
	
	@Autowired
	private Bookrepository bookrepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	@Autowired
	private Commentsrepo commentsrepo;
	
	
//	
	@RequestMapping("/")
	public String Myhome(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		//String name=principal.getName();
		//User user=this.userRepository.getUserByUserName(name);
		User user=new User();
		user.setName("default");
		 List<Books> mybook=this.bookrepository.findAll();
		   
		 List<String> allusers = new ArrayList<>();

		 for(Books ybook : mybook) {
			    allusers.add(ybook.getUser().getName());
			    // any other property.
			}
		 System.out.println("thasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssl");

		 
		 System.out.println(allusers);
		   
		  model.addAttribute("allusers",allusers);
		   model.addAttribute("mybooks",mybook);
		//model.addAttribute("newid",user.getId());
		return "Home";
	}
	
	
	
	@RequestMapping("/about")
	public String about(Model model) {
		
		
		return "about";
		
	}
	
	
	@RequestMapping("/signin")
	public String login(Model model) {
		
		
		return "login";
		
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	
	
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session,Emailrequest emailrequest) {

		try {

			if (!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}

			if (result1.hasErrors()) {
				System.out.println("ERROR " + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
		}

			catch (Exception e) {
				e.printStackTrace();
	}
		
		session.setAttribute("name", user.getName());
		session.setAttribute("email", user.getEmail());
		
		session.setAttribute("password",user.getPassword());
		
		session.setAttribute("about", user.getAbout());
		
		int otp = random.nextInt(99999);
		String body="Your email verification"+ "OTP is "
				+ "<b>"+otp;
		String subject="verification";
		
		emailrequest.setTo(user.getEmail());
		emailrequest.setBody(body);
		emailrequest.setSubject(subject);
		
		session.setAttribute("myotp", otp);
		Response response=this.emailservice.sendemail(emailrequest);

		
		if(response.getStatusCode()==200||response.getStatusCode()==202) {
		return "success";
		}
		else {
			return "failure";
		}
			
		}
		
	
	
	
	
	@PostMapping("/verifyuser")
	
	public String verify(User user,@RequestParam("to") int userotp  ,HttpSession session,Model model) {
		
		int myOtp=(int)session.getAttribute("myotp");
//		
////		System.out.println("User OTP "+otp);
////		System.out.println("Our OTP "+myOtp);
//				
		String email=(String)session.getAttribute("email");	
		
		String password=(String)session.getAttribute("password");	
		String about=(String)session.getAttribute("about");	
		String name=(String)session.getAttribute("name");	
		
		if(myOtp==userotp) {
			
			user.setAbout(about);
			user.setEmail(email);
			//user.setPassword(password);
			user.setName(name);
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("img/default.png");
			user.setPassword(passwordEncoder.encode(password));	
			
			
			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());

			session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
			return "signup";
			
		}
		
		else {
		//session.setAttribute("newuser", user);
		
		
		

//		System.out.println("Agreement " + agreement);
//		System.out.println("USER " + user);

		

	
		model.addAttribute("user", user);
		session.setAttribute("message", new Message("Something Went wrong !! " , "alert-danger"));
		return "signup";
	

		
	}
		
		
		
	
	}
	
	
	
	
	
	
	
	
	
	
	//-----------------------------------------------otp verification-----------------------------------------

//	@RequestMapping("/sendmail")
//	
//	public String sendmessage(Model m) {
//		
//		
//	m.addAttribute("emaildata",emailservice);
//	return "mymessage";
//		
//	}
//	
//	
//	
//	@PostMapping("/sendmail")
//	public String sendmemail(@ModelAttribute("emaildata") Emailrequest emailrequest,@RequestParam("id") int id ,HttpSession session)
//	{
//		int otp = random.nextInt(99999);
//		
//		System.out.println("the api is hit");
//		
//		String body="Your email verification"+ "OTP is "
//				+ "<b>"+otp;
//		String subject="verification";
//		emailrequest.setBody(body);
//		emailrequest.setSubject(subject);
//		Response response=this.emailservice.sendemail(emailrequest);
//		if(response.getStatusCode()==200||response.getStatusCode()==202) {
//			
//			
//			session.setAttribute("myotp", otp);
//			session.setAttribute("email", emailrequest.getTo());
//			//session.setAttribute("email", emailrequest.getTo());
//			
//			return "success";
//			
//		}
//		else {
//			
//			return "failure";
//		}
//			    
//	}
	
//	@PostMapping("/verifyemail")
//	
//	public String verifyemail(@RequestParam("to") int userotp,Model m,HttpSession session) {
//		
//int myOtp=(int)session.getAttribute("myotp");
//		
////		System.out.println("User OTP "+otp);
////		System.out.println("Our OTP "+myOtp);
//		
//		String email=(String)session.getAttribute("email");		
//		if(myOtp==userotp) {
//			m.addAttribute("usermail",email);
//			return"details";
//		}
//		else {
//			return "success";
//		}
//		
//	}
	
	//-----------------------------------------------------------------------------------------------------------

	
	
	
	
	
//	@RequestMapping("/verification/{id}")
//	
//	public String verifyuser(@PathVariable("id") int id,Model m) {
//		
//		
//		m.addAttribute("yourid",id);
//		return "details";
//		
//		
//	}
//	
	@RequestMapping("/Home")
	public String Home(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
//		String name=principal.getName();
//		User user=this.userRepository.getUserByUserName(name);
		 List<Books> mybook=this.bookrepository.findAll();
		   
		   
		  // m.addAttribute("username",name);
		   model.addAttribute("mybooks",mybook);
		//model.addAttribute("newid",user.getId());
		return "Home";
	}
	
	
	
@RequestMapping("/testajax")
	
	public String handlerequest(Model m,InfoUser infouser){
		
		m.addAttribute("infouser",infouser);
		
		return "newform";
	}
	
	
	//@PostMapping("/saveuser")
//	public @ResponseBody String handlepost(@RequestParam("name")String username ,@RequestParam("degree") String Degree 
//			,@RequestParam("age") int age ,InfoUser infouser){
//		
//		infouser.setAge(age);
//		infouser.setDegree(Degree);
//		infouser.setUsername(username);
//		
//		return infouser.toString();
//		
//		
//		//return ResponseEntity.of(Optional.of(infouser));
//	}
//	
	@PostMapping("/saveuser")
	public ResponseEntity<InfoUser> handlepost(@RequestParam("name")String username ,@RequestParam("degree") String Degree 
			,@RequestParam("myfile") String myfile ){
		
		System.out.println("thsi is the test sda");
		
		InfoUser infouser=new InfoUser();
		infouser.setMyfile(myfile);
		infouser.setDegree(Degree);
		infouser.setUsername(username);
		
		//return infouser.toString();
		
		
	return ResponseEntity.of(Optional.of(infouser));
	}
	
	@PostMapping("/getcomments")
	public ResponseEntity <List <Comments>> handlegetcomment(@RequestParam("nid") int noteid ,Principal principal){
		
		
		
//		System.out.println("hi this is new acommoentsdfa");
//		String name=principal.getName();
//		User user=this.userRepository.getUserByUserName(name);

		List<Comments>comments=this.commentsrepo.findCommentsById(noteid);
		
		
		
			
	return ResponseEntity.of(Optional.of(comments));
	}
	
	
	
	
}
