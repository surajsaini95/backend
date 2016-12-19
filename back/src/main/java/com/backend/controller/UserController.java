package com.backend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.dao.UserDao;
import com.backend.daoImpl.UserDaoImpl;
import com.backend.model.User;

@RestController 
public class UserController {

	
	@Autowired
	User user;
	
	@Autowired
	UserDao userDao;
	
	private static final Logger log=LoggerFactory.getLogger(UserDaoImpl.class);
	
	@RequestMapping("/")
	public String index()
	{
		return "index";
	}
	
	@RequestMapping(value="/users",method=RequestMethod.GET)
	public ResponseEntity<List<User>> getAllusers()
	{
		log.debug("Starting of the method getAllUsers ");
		List<User> u=userDao.list();
		
		if(u.isEmpty())
		{
			user.setErrorCode("404");
			user.setErrorMessage(" Users not available ");
		}
		 log.debug("Ending of the method  getAllUsers");
			
		return new ResponseEntity<List<User>>(u,HttpStatus.OK);
	}
	
	 @RequestMapping(value="/user/{id}",method=RequestMethod.GET)
		public ResponseEntity<User> getUserDetails(@PathVariable("id") String id)
		{
		 log.debug("Starting of the method  getUserDeatils for id = "+id);
			user=userDao.get(id);
		 if(user==null)
		 {
			user = new User();	
			 user.setErrorCode("404");
				user.setErrorMessage(" User does not exists with id = "+id);
		 }
		 else
		 {
			 user.setErrorCode("200");
			 user.setErrorMessage(" User  with id = "+id+" found");
		
		 }
		 log.debug("Ending of the method  getUserDetails");
			 
		 return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
	 @RequestMapping(value="/validate/",method=RequestMethod.POST)
		public ResponseEntity<User> login(@RequestBody User user ,HttpSession session)
		{
		 log.debug("Starting of the method login ");
		 String id=user.getId();
			user=userDao.validate(user.getId(),user.getPassword());
		 if(user==null)
		 {		user = new User();
				user.setErrorCode("404");
				user.setErrorMessage(" User does not exists with id = "+id);
		 }
		 else
		 {
			 user.setIsOnline('Y');
			 userDao.update(user);
			 user.setErrorCode("200");
			 user.setErrorMessage(" You logged in successfully");
			
			 session.setAttribute("loggedInUserId", user.getId());
		 }
		 log.debug("Ending of the method  login");
			
		 return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
	 @RequestMapping(value="/logout/",method=RequestMethod.GET)
		public ResponseEntity<User> logout(HttpSession session)
		{  
		 log.debug("Starting of the method logout ");
			
		 String loggedInUserID=(String)session.getAttribute("loggedInUserId");
		 user=userDao.get(loggedInUserID);
		 user.setIsOnline('N');
		 session.invalidate();
		 
		 if(userDao.update(user))
		 {
		 user.setErrorCode("200");
		 user.setErrorMessage(" You logged out successfully");
		 }
		 else
		 {		
			 
			 user.setErrorCode("404");
			 user.setErrorMessage(" You could not logged out ");
			 
		 }
		 log.debug("Ending of the method  logout");
			
		 return new ResponseEntity<User>(user,HttpStatus.OK);
		
		}
	 
	 @RequestMapping(value="/register/",method=RequestMethod.POST)
		public ResponseEntity<User> register(@RequestBody User user )
		{
		 log.debug("Starting of the method register  ");
			
		  if(userDao.get(user.getId())!=null)
		  { 
			 user.setErrorCode("404");
			 user.setErrorMessage(" Already registered with this id.choose other id ");
		  }
		  else
		  {   user.setStatus('n');
			  if(userDao.save(user))
			  {
			     user.setErrorCode("200");
			   	 user.setErrorMessage(" You have registered successfully ");
			  }
			  else
			  {
				  user.setErrorCode("405");
				  user.setErrorMessage(" You failed to register .plz contact admin ");
			  }
		  }
		  log.debug("Ending of the method register ");
			
		 return new ResponseEntity<User>(user,HttpStatus.OK);
		}
	 
	 @RequestMapping(value="/user/",method=RequestMethod.PUT)
	 public ResponseEntity<User> updateUser(@RequestBody User user)
	 {
		 log.debug("Starting of the method updateUser ");
		 if(userDao.get(user.getId())==null)
		 {
			  log.debug("user does not exists with this id = "+user.getId());
			  user=new User();
			  user.setErrorCode("404");
			  user.setErrorMessage("user does not exists with this id = "+user.getId());
		      return new ResponseEntity<User>(user,HttpStatus.NOT_FOUND);
		 }
		 else
		 {
			 userDao.update(user);
			 log.debug("User updated successfully... ");
			 return new ResponseEntity<User>(user,HttpStatus.OK);
		 }
	 }
	 
	 @RequestMapping(value="/user/{id}",method=RequestMethod.DELETE)
	 public ResponseEntity<User> deleteUser(@PathVariable("id") String id)
	 {
		 log.debug("Starting of the method deleteUser ");
		 User user=userDao.get(id);
		 if(user==null)
		 {
			  log.debug("user does not exists with this id = "+id);
			  user=new User();
			  user.setErrorCode("404");
			  user.setErrorMessage("user does not exists with this id = "+id);
		      return new ResponseEntity<User>(user,HttpStatus.NOT_FOUND);
		 }
		 else
		 {
			 //userDao.delete();
			 log.debug("User deleted successfully... ");
			 user.setErrorMessage("user deleted successfully...");
		     return new ResponseEntity<User>(user,HttpStatus.OK);
		 }
	 }
	 
	 
	 @RequestMapping(value="/reject/{id}/{reason}",method=RequestMethod.GET)
		public ResponseEntity<User> reject(@PathVariable("id")String id,@PathVariable("reason")String  reason )
		{
		 log.debug("Starting of the method reject  ");
		 user=updateStatus(id,'R',reason);
		   log.debug("Ending of the method reject ");
			
		 return new ResponseEntity<User>(user,HttpStatus.OK);
		}
	 
	 @RequestMapping(value="/accept/{id}",method=RequestMethod.GET)
		public ResponseEntity<User> accept(@PathVariable("id")String id)
		{
		 log.debug("Starting of the method accept ");
		
		 user=updateStatus(id,'A',"");
		   log.debug("Ending of the method accept ");
			
		 return new ResponseEntity<User>(user,HttpStatus.OK);
		}
	 
	 
	 private User updateStatus(String id,char status,String reason)
	 {
		 log.debug("Starting of the method updateStatus "+id+status+reason);
		 user=userDao.get(id);
		 if(user==null)
		 {	 
			 	user=new User();
				user.setErrorCode("404");
				user.setErrorMessage(" Could not update status");
		 }
		 else
		 {
			 user.setStatus(status);
			 user.setReason(reason);
			 userDao.update(user);
		 }
		 log.debug("Ending of the method updateStatus ");
		 return user;
	 }
}
