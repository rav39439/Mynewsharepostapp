package com.mynewapplication.web.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mynewapplication.web.dao.Bookrepository;
import com.mynewapplication.web.dao.Commentsrepo;
import com.mynewapplication.web.dao.Databaseusers;
import com.mynewapplication.web.dao.Extractsrepository;
import com.mynewapplication.web.dao.Friendsrepo;
import com.mynewapplication.web.entities.Books;
import com.mynewapplication.web.entities.Extracts;
import com.mynewapplication.web.entities.Friends;
import com.mynewapplication.web.entities.User;
import com.mynewapplication.web.helpers.Message;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private Databaseusers userRepository;
	
	
	@Autowired
	private Extractsrepository extractsrepository;
	
	
	@Autowired
	private Commentsrepo commentsrepo;
	
	private Books mybook;
private Friends friends;
	


private Extracts myextract;
private User users;

	
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
		
		return "admin/adminhome";
	}
	
	@RequestMapping("/users")
	public String getusers(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		
		List<User> users=this.userRepository.findAll();
		
		model.addAttribute("users",users);
		return "admin/allusers";
	}
	
	@RequestMapping("/posts")
	public String getnotes(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		
		List<Books> books=this.bookrepository.findAll();
		
		model.addAttribute("notes",books);
		return "admin/allnotes";
	}
	
	//---------------------------admin posting notes-----------------------------------------------------------
	
	@RequestMapping("/postnotes")
	public String postnotes(Model model,Principal principal) {
		
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		model.addAttribute("name",user.getName());
		model.addAttribute("book", new Books());
		return "admin/addnote";
	}
	
	@PostMapping("/postnotes")
	public String processContact(@ModelAttribute ("book") Books book, @RequestParam("myfile") String file,
			Principal principal, HttpSession session,Model model) {

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

		return "admin/addnote";
	}
	
	//-----------------------------------------------------------------------------------------------------------
	
	
	@RequestMapping("/shownotes")
    public String shownotes(Model m , Principal principal) {
		
	String name=principal.getName();
	User user=this.userRepository.getUserByUserName(name);
	System.out.println("the new id of the user logged in is");
	 System.out.println(user.getId());
	
 List<Books> mybook=this.bookrepository.findBooksById(user.getId());
 
 
// m.addAttribute("username",name);
 m.addAttribute("notes",mybook);
 
 return "admin/shownotes";
 
			
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
		
		
		return "admin/notesdetail";
		
	}
	
	@RequestMapping("/notesupdate/{cId}")
	public String notesupdate(@PathVariable("cId") Integer id,Model model) {
		
		Books mybook=this.bookrepository.getById(id);
		model.addAttribute("title","Update Contact");
		model.addAttribute("notes",mybook);
		
		return "admin/notesupdate";
		
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
		return "redirect:/admin/notesupdate/"+mybook.getcId();
		
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

		return "redirect:/admin/shownotes/";
	}
	
	
	
	@GetMapping("/deletes/{cid}")
	@Transactional
	public String deletecont(@PathVariable("cid") Integer cId, Model model, HttpSession session,
			Principal principal) {
		System.out.println("CID " + cId);

		Books book = this.bookrepository.findById(cId).get();
		// check...Assignment..image delete

		// delete old photo

		User user = this.userRepository.getuserbyemail(book.getUser().getEmail());

		user.getMyBooks().remove(book);

		this.userRepository.save(user);

		System.out.println("DELETED");
		session.setAttribute("message", new Message("notes deleted succesfully...", "success"));

		return "redirect:/admin/posts";
	}
	
	
	@RequestMapping("/userupdate/{cId}")
	public String userupdate(@PathVariable("cId") Integer id,Model model) {
		
		User user=this.userRepository.getById(id);
		model.addAttribute("title","Update User");
		model.addAttribute("user",user);
		
		return "admin/userupdate";
		
	}
	
	
	@PostMapping("/user-update")
	public String updateuser(@ModelAttribute("user") User user,
			Principal principal,HttpSession session){
		
		//System.out.println(mybook.getTitle());
		
		User olduser=this.userRepository.getById(user.getId());
	
		
		
		System.out.println(olduser.getName());

		try {
			
			this.userRepository.save(user);

			session.setAttribute("message", new Message("Your USER is updated...", "success"));
			
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/userupdate/"+user.getId();
		
	}
	
	@GetMapping("/deleteuser/{cid}")
	@Transactional
	public String deleteuser(@PathVariable("cid") Integer cId, Model model, HttpSession session,
			Principal principal) {
		System.out.println("CID " + cId);

		User user = this.userRepository.findById(cId).get();
		// check...Assignment..image delete

		// delete old photo

//		User user = this.userRepository.getuserbyemail(book.getUser().getEmail());
//
//		user.getMyBooks().remove(book);

		this.userRepository.delete(user);

		System.out.println("DELETED");
		session.setAttribute("message", new Message("notes deleted succesfully...", "success"));

		return "redirect:/admin/users";
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
