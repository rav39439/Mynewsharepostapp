package com.mynewapplication.web.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.mynewapplication.web.dao.Friendsrepo;
import com.mynewapplication.web.entities.ChatMessage;
import com.mynewapplication.web.Services.WSService;
import com.mynewapplication.web.dao.Bookrepository;
import com.mynewapplication.web.dao.Commentsrepo;
import com.mynewapplication.web.dao.Databaseusers;
import com.mynewapplication.web.dao.Extractsrepository;
import com.mynewapplication.web.entities.Books;
import com.mynewapplication.web.entities.Comments;
import com.mynewapplication.web.entities.User;
import com.mynewapplication.web.helpers.Message;
import com.mynewapplication.web.entities.Extracts;
import com.mynewapplication.web.entities.Friends;
import com.mynewapplication.web.entities.InfoUser;
import com.mynewapplication.web.entities.MyMessage;
import com.mynewapplication.web.entities.ResponseMessage;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private Databaseusers userRepository;
	
	private Message message;
	
	@Autowired
	private Extractsrepository extractsrepository;
	
	
	@Autowired
	private Commentsrepo commentsrepo;
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	//@Autowired
   // private Allmessagesrepo mymessagerepo;
	
	private Books mybook;
private Friends friends;
	


private Extracts myextract;
	
	@Autowired
	private Bookrepository bookrepository;
	
	@Autowired
	private Friendsrepo friendsrepo;
	

	
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);

		// get the user using usernamne(Email)

		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER " + user);
		model.addAttribute("user", user);
	}
	
	@RequestMapping("/")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);
		 List<Books> mybook=this.bookrepository.findAll();
		 
		 List<String> usernames = new ArrayList<>();

		 for(Books book : mybook) {
			    usernames.add(book.getUser().getName());
			    // any other property.
			}
		   
		 model.addAttribute("usernames",usernames);
		   model.addAttribute("mybooks",mybook);
		model.addAttribute("newid",user.getId());
		return "normal/dashboard";
	}
	
	
	
	@RequestMapping("/index")
	public String newdashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "normal/dashboard";
	}
	
	
	
	
	
	
	@RequestMapping("/postnotes")
	public String postnotes(Model model,Principal principal) {
		
		
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);

		model.addAttribute("title", "Add note");
		model.addAttribute("book", new Books());
		model.addAttribute("name",user.getName());
		return "normal/postnotes";
	}
	
	@PostMapping("/postnotes")
	public String processContact(@ModelAttribute ("book") Books book, @RequestParam("myfile") String file,
			Principal principal, HttpSession session) {

		try {

			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file..
book.setImage(file);
//			if (file.isEmpty()) {
//				// if the file is empty then try our message
//				System.out.println("File is empty");
//				book.setImage("default.png");
//
//			} else {
//				// file the file to folder and update the name to contact
//				book.setImage(file.getOriginalFilename());
//
//				File saveFile = new ClassPathResource("static/img").getFile();
//
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//
//				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//				System.out.println("Image is uploaded");
//
//			}

			user.getMyBooks().add(book);

			book.setUser(user);

			this.userRepository.save(user);

			System.out.println("DATA " + book);

			System.out.println("Added to data base");

			// message success.......
			session.setAttribute("message", new Message("Your contact is added !! Add more..", "success"));

		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			// message error
			session.setAttribute("message", new Message("Some went wrong !! Try again..", "danger"));

		}

		return "normal/postnotes";
	}

	@RequestMapping("/shownotes")
      public String shownotes(Model m , Principal principal) {
		
	String name=principal.getName();
	User user=this.userRepository.getUserByUserName(name);
	System.out.println("the new id of the user logged in is");
	 System.out.println(user.getId());
	
   List<Books> mybook=this.bookrepository.findBooksById(user.getId());
   
   
  // m.addAttribute("username",name);
   m.addAttribute("notes",mybook);
   
   return "normal/Yournotes";
   
			
}
	@RequestMapping("/{cId}/book")
	public String viewcontact(@PathVariable ("cId") Integer id,Model model,Principal principal) {
		
		
		Optional <Books> notesoptional= this.bookrepository.findById(id);
		Books note=notesoptional.get();
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		
		//if (user.getId() == note.getUser().getId()) {
			model.addAttribute("note", note);
			model.addAttribute("title", note.getTitle());
			model.addAttribute("writer",note.getUser().getName());
			model.addAttribute("username",user.getName());
		//}
		
		
		return "normal/notesdetail";
		
	}
	
	@RequestMapping("/notesupdate/{cId}")
	public String notesupdate(@PathVariable("cId") Integer id,Model model) {
		
		Books mybook=this.bookrepository.getById(id);
		model.addAttribute("title","Update Contact");
		model.addAttribute("notes",mybook);
		
		return "normal/notesupdate";
		
	}
	
	
	
	
	@PostMapping("/process-update")
	public String updatenote(@ModelAttribute Books mybook,@RequestParam("myfile") String file,
			Principal principal,HttpSession session){
		
		System.out.println(mybook.getTitle());
		
		Books oldnote=this.bookrepository.getById(mybook.getcId());
	
		
		
		System.out.println(oldnote.getTitle());

		try {
			mybook.setImage(file);
//			if(!file.isEmpty()) {
//			
//				
//				File deleteFile = new ClassPathResource("static/img").getFile();
//				File file1 = new File(deleteFile, oldnote.getImage());
//				file1.delete();
//				
//				File saveFile = new ClassPathResource("static/img").getFile();
//
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//
//				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//				mybook.setImage(file.getOriginalFilename());
//			} else {
//				mybook.setImage(oldnote.getImage());
//			}
			
			
			
			User user = this.userRepository.getUserByUserName(principal.getName());

			mybook.setUser(user);

			this.bookrepository.save(mybook);

			session.setAttribute("message", new Message("Your NOTE is updated...", "success"));
			
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/notesupdate/"+mybook.getcId();
		
	}
	
	
	@RequestMapping("/subnote/{cId}")
	
	public String getsubnote(@PathVariable("cId") Integer id,Model model,Extracts myextract) {
		
		model.addAttribute("noteid",id);
		model.addAttribute("myextract",myextract);
		
		return "normal/newextract";
		
	}
	
	@PostMapping("/postnewextract/{nid}")
	
	public String postnewextract(@ModelAttribute("myextract") Extracts aextract ,@PathVariable("nid") int id,@RequestParam("myfile") String file) {
		
		Books note=this.bookrepository.getById(id);
		
		System.out.println("post extract run");
		
		try {
		aextract.setEimage(file);

//		if (file.isEmpty()) {
//			// if the file is empty then try our message				System.out.println("File is empty");
//				aextract.setEimage("default.png");
//
//			} else {
//				// file the file to folder and update the name to contact
//				aextract.setEimage(file.getOriginalFilename());
//
//			File saveFile = new ClassPathResource("static/img").getFile();
//
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//
//			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//			System.out.println("Image is uploaded");
//
//		}	
//		
		
		
		
		
	}
	catch(Exception e) {
		e.printStackTrace();
			
	}
		
		aextract.setBook(note);
		note.getExtractnotes().add(aextract);
		this.bookrepository.save(note);
		return "normal/newextract";
		
		
	}
	
	
	
	//------------------------------------update extract-------------------------------------------------------
	
	
	@RequestMapping("/extractupdate/{cId}")
	public String extratupdate(@PathVariable("cId") Integer id,Model model) {
		
		Extracts extracts=this.extractsrepository.getById(id);
		model.addAttribute("title","Update extract");
		model.addAttribute("extracts",extracts);
		model.addAttribute("Bookid",extracts.getBook().getcId());
		
		return "normal/extractupdate";
		
	}
	
	
	@PostMapping("/extract-update")
	public String updatextract(@ModelAttribute("extracts") Extracts extract,@RequestParam("profileImage") MultipartFile file,
			@RequestParam("Id") int bookid ,@RequestParam("eId") int eid ,Principal principal,HttpSession session){
		
		//System.out.println(extract.ge);
		System.out.println("the new id is dsafa");
		
		Extracts oldextract=this.extractsrepository.getById(eid);
	
	
		
		//System.out.println(oldnote.getTitle());

		try {
			
			if(!file.isEmpty()) {
			
				
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldextract.getEimage());
				file1.delete();
				
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				extract.setEimage(file.getOriginalFilename());
			} else {
				extract.setEimage(oldextract.getEimage());
			}
			
			
			
			Books book = this.bookrepository.findBookById(bookid);
			
			//System.out.println(extract.getBook().getcId());

			extract.setBook(book);

			this.extractsrepository.save(extract);

			session.setAttribute("message", new Message("Your Extract is updated...", "success"));
			
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/extractupdate/"+extract.geteId();
		
	}
	
	
	@GetMapping("/deleteextract/{cid}")
	@Transactional
	public String deleteextract(@PathVariable("cid") Integer cId, Model model, HttpSession session,
			Principal principal) {
		System.out.println("CID " + cId);

		Extracts extract = this.extractsrepository.getById(cId);
		// check...Assignment..image delete

		// delete old photo
model.addAttribute("newid",extract.getBook().getcId());
		Books book = this.bookrepository.getById(extract.getBook().getcId());

		book.getExtractnotes().remove(extract);

		this.bookrepository.save(book);

		System.out.println("DELETED");
		session.setAttribute("message", new Message("Contact deleted succesfully...", "success"));

		return "redirect:/user/showextracts/"+extract.getBook().getcId();
	}

	
	
	//------------------------------------------------------------------------------------------------------------
	
	@RequestMapping("/searchfriends")
	public String searchfriends(Model m ,Friends friend) {
		
		m.addAttribute("friend",friend);
		
		return "normal/mybase";
	}
	
	
	
	@PostMapping("/search")
	public String searchresults(@RequestParam("searchcontent") String search, Model model,Friends friend) {
		
		try {
			
			User user=this.userRepository.getuserbyname(search);
			
			model.addAttribute("user",user);
			model.addAttribute("friend",friend);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	
		
		return"normal/searchresult";
	}
	
	@PostMapping("/sendrequest")
	public String sendrequest (@ModelAttribute("friend") Friends friend ,Principal principal,HttpSession session) {
		
		String name = principal.getName();
		User user1 = this.userRepository.getUserByUserName(name);
		
		User user=this.userRepository.getUserByUserId(friend.getFriendid());
		
	friend.setUser(user);
	
	friend.setFriendreqname(user1.getName());
		friend.setFriendid(user1.getId());
		user.getMyfriends().add(friend);
		this.userRepository.save(user);
		
		session.setAttribute("message", new Message("Message is successfully sent !!", "alert-success"));

		return"normal/mybase";
	}
	
	
	@RequestMapping("/friendrequests")

	public String friendrequests(Model m,Principal principal,Friends myfriend) {
		
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		List<Friends> friends=this.friendsrepo.findBooksById(user.getId());
		
		System.out.println(friends);
	
		m.addAttribute("friends",friends);
		m.addAttribute("myfriend",myfriend);
		
		return "normal/friendrequests";
	}
	
	
	@PostMapping("/updatefriend/{fid}")
	public String updatefriend(@PathVariable("fid") int id,@ModelAttribute("myfriend") Friends myfriend,Friends newfriend,Principal principal) {
		String name = principal.getName();
		User user1 = this.userRepository.getUserByUserName(name);
		
		//---------------------update--------------------------------------------------
		Friends oldfrienddata=this.friendsrepo.getById(id);
		oldfrienddata.setFriendreqname("");
         oldfrienddata.setUser(user1);
		oldfrienddata.setAllfriends(myfriend.getAllfriends());
//		
//		//user1.getMyfriends().add(oldfrienddata);
		this.friendsrepo.save(oldfrienddata);
		//------------------------------------------------------------------------------------
		User user=this.userRepository.getById(myfriend.getFriendid());
		//newfriend.setFid(0);
		newfriend.setUser(user);
newfriend.setFid(0);
		newfriend.setAllfriends(user1.getName());
		newfriend.setFriendid(user1.getId());
		user.getMyfriends().add(newfriend);
	
		//user.getMyfriends().add(newfriend);
		this.userRepository.save(user);
		
		return "normal/friendrequests";
		
	}
	
	
	@RequestMapping("/getfriends")
	public String getfriends(Model m, Principal principal) {
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);

		List<Friends> friends=this.friendsrepo.findBooksById(user.getId());
		m.addAttribute("friends",friends);
		m.addAttribute("username",user.getName());
		
		return "normal/allfriends";
	}
	
	
	
	@RequestMapping("/getnotes/{fname}")
    public String getnotes(Model m , Principal principal,@PathVariable("fname") int fname) {
		
	String name=principal.getName();
	User user=this.userRepository.getUserByUserName(name);
	System.out.println("the new id of the user logged in is");
	 System.out.println(user.getId());
	
 List<Books> mybook=this.bookrepository.findBooksById(fname);
 
 
 
 m.addAttribute("notes",mybook);
 
 return "normal/notesbaba";
 
			
}
	
	//---------------------------------------chat application---------------------------------------------------------
	
	@RequestMapping("/chat/{fname}")
    public String getchatpage(Model m , Principal principal,@PathVariable("fname") int fname) {
		
	String name=principal.getName();
	User user=this.userRepository.getUserByUserName(name);
	System.out.println("the new id of the user logged in is");
	 System.out.println(user.getId());
	
 User myuser=this.userRepository.getUserByUserId(fname);
 
 
 
 m.addAttribute("myuser",myuser);
 m.addAttribute("user",user);
//m.addAttribute("messages",user.getYourmessages());
//m.addAttribute("amessages",myuser.getYourmessages());
//m.addAttribute("messagesl",user.getYourmessages().size());
//m.addAttribute("amessagel",myuser.getYourmessages().size());

 return "chatpage";
 
			
}
//----------------------------------global chatting------------------------------------------------------------------------	
//	@MessageMapping("/send/")
//    @SendTo("/topic/chat")    
//	public ChatMessage sendMessage(ChatMessage chatMessage, Principal principal) {
//		chatMessage.setName(principal.getName());
//		///System.out.println("theafswfrdsdfddffffffffffffffffffffffffffffffffffffffffffffffff"+" "+to);
//		// simpMessagingTemplate.convertAndSend("/topic/chat/"+to, chatMessage);
//      return chatMessage;
//    }
	
	///--------------------------------------------------------------------------------------------------------------
	
	
	
	@MessageMapping("/send/{to}")
	//@SendTo("/topic/chat")    
	public void sendMessage(ChatMessage chatMessage,@DestinationVariable String to, Principal principal) {
		
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);

		chatMessage.setName(user.getName());
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		chatMessage.setDate(dtf.format(now));
		//Mychatmessage chat=new Mychatmessages();
		
//		chat.setFromuserid(chatMessage.getFrom());
//		chat.setMessage(chatMessage.getMessage());
//		chat.setTouserid(chatMessage.getTo());
//		chat.setName(name);
//		chat.setTuser(user);
//		chat.setMessageType(chatMessage.getMessageType());
//		chat.setDate(dtf.format(now));
		
		//chatMessage.setUser(user);
		
		
		//user.getYourmessages().add(chat);
		///this.userRepository.save(user);
		System.out.println("theafswfrdsdfddffffffffffffffffffffffffffffffffffffffffffffffff"+" "+to);
		 simpMessagingTemplate.convertAndSend("/topic/chat/"+to, chatMessage);
       //return chatMessage;
    }
	
	
//	@MessageMapping("/asend/{to}")
//	//@SendTo("/topic/chat")    
//	public void asendMessage(ChatMessage chatMessage,@DestinationVariable String to, Principal principal) {
//		chatMessage.setName(principal.getName());
//		System.out.println("theafswfrdsdfddffffffffffffffffffffffffffffffffffffffffffffffff"+" "+to);
//		 simpMessagingTemplate.convertAndSend("/topic/chat1/"+to, chatMessage);
//       //return chatMessage;
//    }
	
	
	///----------------------------------sending messages using routes--------------------------------------------------
//	@MessageMapping("/message")
//    @SendTo("/topic/messages")
//    public ResponseMessage getMessage(final MyMessage message) throws InterruptedException {
//        Thread.sleep(1000);
//        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
//    }
//	
//	
//	
//	
//	 @MessageMapping("/private-message")
//	    @SendToUser("/topic/private-messages")
//	    public ResponseMessage getPrivateMessage(final MyMessage message,
//	                                             final Principal principal) throws InterruptedException {
//	        Thread.sleep(1000);
//	        return new ResponseMessage(HtmlUtils.htmlEscape(
//	                "Sending private message to user " + principal.getName() + ": "
//	                        + message.getMessageContent())
//	        );
//	    }
//	 
//	 
//	 
//	
//	    @Autowired
//	    private WSService service;
//
//	    @PostMapping("/send-message")
//	    public void sendMessage(@RequestBody final MyMessage message) {
//	        service.notifyFrontend(message.getMessageContent());
//	    }
//
//	    @PostMapping("/send-private-message/{id}")
//	    public void sendPrivateMessage(@PathVariable final String id,
//	                                   @RequestBody final MyMessage message) {
//	        service.notifyUser(id, message.getMessageContent());
//	    }
	
	///------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------
	
	
	@RequestMapping("/showextracts/{cId}")
    public String showextracts(Model m , Principal principal,@PathVariable("cId") int id) {
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);

		List<Extracts> myextracts=this.extractsrepository.getExtractsbyId(id);
		m.addAttribute("myextracts",myextracts);
		m.addAttribute("name",user.getName());
		
		return "normal/allextracts";
		
	}
	
	@GetMapping("/delete/{cid}")
	@Transactional
	public String deleteContact(@PathVariable("cid") Integer cId, Model model, HttpSession session,
			Principal principal) {
		System.out.println("CID " + cId);

		Books book = this.bookrepository.findById(cId).get();
		// check...Assignment..image delete

		// delete old photo

		User user = this.userRepository.getuserbyemail(principal.getName());

		user.getMyBooks().remove(book);

		this.userRepository.save(user);

		System.out.println("DELETED");
		session.setAttribute("message", new Message("Contact deleted succesfully...", "success"));

		return "redirect:/user/shownotes/";
	}
	
	
	
//	@GetMapping("/delete/{cid}")
//	@Transactional
//	public ResponseEntity<Message>deleteContact(@PathVariable("cid") Integer cId, Model model,
//			Principal principal) {
//	System.out.println("CID " + cId);
//
//	Books book = this.bookrepository.findById(cId).get();
//		// check...Assignment..image delete
//
//	// delete old photo
//
//	User user = this.userRepository.getuserbyname(principal.getName());
//
//		user.getMyBooks().remove(book);
//		this.userRepository.save(user);
//
//	System.out.println("DELETED");
//
//	//return "redirect:/user/shownotes/";
//	Message message=new Message("Deleted","success");
//	
//	
//return ResponseEntity.of(Optional.of(message));
//	}
	
	
	
	
	

	
	@RequestMapping("/getextracts/{cId}")
    public String getextracts(Model m , Principal principal,@PathVariable("cId") int id) {
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);

		List<Extracts> myextracts=this.extractsrepository.getExtractsbyId(id);
		m.addAttribute("myextracts",myextracts);
		m.addAttribute("name",user.getName());
		
		return "normal/dashextracts";
		
	}
	
	
	@PostMapping("/postcomment")
	public ResponseEntity <List <Comments>> handlepost(@RequestParam("comments") String mycomment
			,@RequestParam("id") int noteid ,Principal principal){
		
		Comments comment=new Comments();
		
		System.out.println("hi this is new acommoentsdfa");
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);

		Books book=this.bookrepository.findBookById(noteid);
		
		
		
		comment.setBook(book);
		comment.setComments(mycomment);
		
		comment.setName(user.getName());
		 Calendar cal = Calendar.getInstance();
		    java.util.Date date=cal.getTime();
		    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		    String formattedDate=dateFormat.format(date);
		
		comment.setDate(formattedDate);
	
		book.getMycomments().add(comment);
this.bookrepository.save(book);

	List<Comments> myncomments=book.getMycomments();	
	return ResponseEntity.of(Optional.of(myncomments));
	}
	
	
	@PostMapping("/getcomment")
	public ResponseEntity <List <Comments>> handlegetcomment(@RequestParam("nid") int noteid ,Principal principal){
		
		
		
//		System.out.println("hi this is new acommoentsdfa");
//		String name=principal.getName();
//		User user=this.userRepository.getUserByUserName(name);

		List<Comments>comments=this.commentsrepo.findCommentsById(noteid);
		
		
		
			
	return ResponseEntity.of(Optional.of(comments));
	}
	
	
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/signin";
	}
	
	
	
	
	
	
	
	
}
