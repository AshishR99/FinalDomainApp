package net.codejava.controllers;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import net.codejava.entities.Domain;
import net.codejava.entities.Role;
import net.codejava.entities.UserActivity;
import net.codejava.entities.Userlogin;
import net.codejava.payload.UserloginRolePayload;
import net.codejava.payload.UserloginUserActivityPayload;
import net.codejava.services.ContentService;
import net.codejava.services.DomainService;
import net.codejava.services.UserActivityService;
import net.codejava.services.UserloginService;

@RestController
@CrossOrigin("*")
public class UserController {

	boolean status = false;
	boolean isLoggedIn = false;
	boolean statusUserActviity = false;
	String statusMessage = "";
	private static final Logger logger=Logger.getLogger(UserController.class);
	
	@Autowired
	private DomainService domainService;
	
	@Autowired
	private UserloginService userloginService;
	
	@Autowired
	private UserActivityService userActivityService;

	
	@Autowired
	private ContentService contentService;
	
	

	//------------  user handler methods starts-----------------------------//

	//User Login credentials checking and sending roleid + user id to UI 

	@RequestMapping(value = "/api/users/checkUserLoginCredentials",method = RequestMethod.POST)
	public ResponseEntity<?> checkUserCredentials(@RequestBody Userlogin userlogin)
	{
		Map<String, Object> data = new HashMap<String,Object>();
		
		String user_email = userlogin.getUser_email();
		String user_pwd = userlogin.getUser_password();
		System.out.println("login details of User :"+user_email+","+user_pwd);

		try
		{
			userlogin = userloginService.checkUserLoginDetails(user_email, user_pwd);
			if(userlogin.getUser_id()<=0)
			{
				userlogin = new Userlogin();
				status = false;
				statusMessage = "Either User ID OR password DOES NOT match!!!";
				
				data.put("userlogin", userlogin);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
				
			} 
			else 
			{
//				status = true;
//				statusMessage = "";
				isLoggedIn = userActivityService.isUserLoggedIn(userlogin.getUser_id());
				System.out.println("Loggedin"+isLoggedIn);
				if(!isLoggedIn)
				{
					status = userActivityService.saveUserActivity(userlogin.getUser_id());
					System.out.println("User activity status"+status);
					if(status)
					{
						status = true;
						statusMessage = "Credentials ARE fine!!! Your session started Successfully!!!";
									
						data.put("userlogin", userlogin);
						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}
					else
					{
						status = false;
						statusMessage = "User Activity not yet saved...plz try again later!!!";
					
						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}
				}
				
				else
				{
					status = true;
					statusMessage = "User already loggedin...!!!";
				
					data.put("userlogin", userlogin);
					data.put("status", status);
					data.put("statusMessage", statusMessage);
				}
				
				
				
			}
		}
		
		catch(Exception ex)
		{
			userlogin = new Userlogin();
			status = false;
			statusMessage = "Internal Server Error!!!";
			
			data.put("userlogin", userlogin);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}


		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);


	}



	// Get All Users
	@RequestMapping("/api/users/getUsers")
	public ResponseEntity<?> getAllUsers()
	{
		Map<String, Object> data = new HashMap<String,Object>();
		List<UserloginRolePayload> listOfUserswithRoleDesc = userloginService.listAllUsersWithRoleDesc();
		if(!listOfUserswithRoleDesc.isEmpty())
		{
			status = true;
			statusMessage = "Retrieving Users.....!!!";
		}
		else 
		{
			status = false;
			statusMessage = "List of Users is empty!!!";

		}
		
		data.put("listOfUserswithRoleDesc", listOfUserswithRoleDesc);
		data.put("status", status);
		data.put("statusMessage", statusMessage);

		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}


	// Save User With Login Time By Admin/CC/Learner details
	@RequestMapping(value = "/api/users/save_user", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody Userlogin userlogin) 
	{
		Map<String, Object> data = new HashMap<String,Object>();
		
		//System.out.println("Checking user details from POJO:"+user);
		System.out.println("Checking userlogin details from POJO:"+userlogin);
		
		//Setting Mapped Domain as "NA" for the admin
		if(userlogin.getFk_role_id()==1)
		{
			userlogin.setFk_domain_id("NA");

		}
		
		//Userlogin details saving in Userlogin Table
		status = userloginService.save(userlogin);
		if(status)
		{
			
			System.out.println("Checking userlogin details from POJO:"+userlogin);
			int user_id = userlogin.getUser_id();
			System.out.println("user_id : "+user_id);
			
			status = true;
			statusMessage = "User Details Saved Successfully!!!";
						
			data.put("userlogin", userlogin);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			
		}

		else
		{
			userlogin = new Userlogin();
			status = false;
			statusMessage = "Duplicate User/Connection problem...plz try again later!!!";
			
			data.put("userlogin", userlogin);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}

		
//		return mav;
		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}

	
	
	//Update User Details
	@RequestMapping(value = "/api/users/update_user",method = RequestMethod.PUT)
	public ResponseEntity<?> showEditUserPage(@RequestBody Userlogin userlogin) 
	{
		Map<String, Object> data = new HashMap<String,Object>();
		
		//System.out.println("Checking user details from POJO:"+user);
		System.out.println("Checking userlogin details from POJO for updation:"+userlogin);
		
		
		if(userlogin.getFk_role_id()==1)
		{
			userlogin.setFk_domain_id("NA");

		}

		status = userloginService.update(userlogin);
		if(status)
		{
			status = true;
			statusMessage = "User updated Successfully!!!";
			
			data.put("userlogin", userlogin);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
			
		}

		else
		{
			userlogin = new Userlogin();
			status = false;
			statusMessage = "User not updated...plz try again later!!!";
			
			data.put("userlogin", userlogin);
			data.put("status", status);
			data.put("statusMessage", statusMessage);
		}



//		return mav;
		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}
	

	//Calculate User Activity Details
		@RequestMapping(value = "/api/users/logout_user/{user_id}",method = RequestMethod.GET)
		public ResponseEntity<?> calculateUserActivityTimeByLoggingOut(@PathVariable(name = "user_id") int user_id)
		{
			Map<String, Object> data = new HashMap<String,Object>();
			UserActivity userActivity = new UserActivity();
			String logout_time = "NA";
			String user_activity_time = "NA";
			try
			{
				userActivity = userActivityService.getUserLastActivityData(user_id,logout_time,user_activity_time);
				status = true;
				statusMessage = "Activity against this user is fetched successfully!!!";
				
				
				data.put("userActivity", userActivity);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			catch(Exception ex)
			{
				userActivity = new UserActivity();
				status = false;
				statusMessage = "Activity fetching failed...somthing went wrong!!!";
				
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			
			
			return new ResponseEntity<Map<String,Object>>(data,HttpStatus.OK);
			
		}
		
	
	//Update User Details
	@RequestMapping(value = "/api/users/get_user_activity/{user_id}/{no_of_days}",method = RequestMethod.GET)
	public ResponseEntity<?> getUserActivity(@PathVariable(name = "user_id") int user_id,@PathVariable(name = "no_of_days") int no_of_days)
	{
		Map<String, Object> data = new HashMap<String,Object>();
		List<UserloginUserActivityPayload> allUsersActivityList = new ArrayList<UserloginUserActivityPayload>();
		List<UserloginUserActivityPayload> particularUserActivityList = new ArrayList<UserloginUserActivityPayload>();
		try
		{
			int role_id = userloginService.findRoleIdByUserId(user_id);
			if(role_id==1)
			{
				 allUsersActivityList= userActivityService.getDateBasedAllUsersActivityList(no_of_days);
				 status = true;
				 statusMessage = "Activity against all users is fetched successfully!!!";
				 
				 data.put("allUsersActivityList", allUsersActivityList);
				 data.put("status", status);
				 data.put("statusMessage", statusMessage);
			}
			
			else 
			{
				particularUserActivityList = userActivityService.getDateBasedParticularUserActivityList(user_id,no_of_days);
				status = true;
				statusMessage = "Activity against this particular user is fetched successfully!!!";
				
				data.put("particularUserActivityList", particularUserActivityList);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
		}
		catch(Exception ex)
		{
			status = false;
			statusMessage = "Either user loggedin for the first time / Something went wrong!!!";
		}
	
		return new ResponseEntity<Map<String,Object>>(data,HttpStatus.OK);
	}
	
	
	//Get users details by user id
	@RequestMapping("/api/users/getByUserId/{user_id}")
	public ResponseEntity<?> showEditRolePage(@PathVariable(name = "user_id") int user_id) 
	{

//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		try
		{
			Userlogin userlogin = userloginService.get(user_id);
			if(userlogin.getUser_id()<=0)
			{
				status = false;
				statusMessage = "User not found..something went wrong!!!";

//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);

				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
			
			else
			{
				status = true;
				statusMessage = "User Data has been fetched successfully..";

//				mav.addObject("userlogin", userlogin);
//				mav.addObject("status",status);
//				mav.addObject("statusMessage", statusMessage);
				
				data.put("userlogin", userlogin);
				data.put("status", status);
				data.put("statusMessage", statusMessage);

			}
			
		}

		catch(Exception ex)
		{
			status = false;
			statusMessage = "User not found..something went wrong!!!";

//			mav.addObject("status",status);
//			mav.addObject("statusMessage", statusMessage);

			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}


//		return mav;
		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}


	//Delete User details
	@RequestMapping(value = "/api/users/delete_user/{user_id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable(name = "user_id") int user_id) 
	{
		
//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();
		Userlogin userlogin = userloginService.get(user_id);
		if(userlogin==null||userlogin.getUser_id()<=0)
		{
			status = false;
			statusMessage = "Something went wrong...user details cant be fetched!!!";
			
//			mav.addObject("status", status);
//			mav.addObject("statusMessage",statusMessage);	

			data.put("status", status);
			data.put("statusMessage", statusMessage);
		
		}
		
		else
		{
			
			try
			{
				int fk_role_id = userlogin.getFk_role_id();
				if(fk_role_id==1)
				{
					boolean hasCreatedAnyUser = userloginService.findCreatorByUserId(user_id);
					if(!hasCreatedAnyUser)
					{
						userloginService.delete(user_id);
						status = true;
						statusMessage = "This Admin deleted successfully!!!";

//						mav.addObject("status", status);
//						mav.addObject("statusMessage",statusMessage);

						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}
					
					else
					{
						status = false;
						statusMessage = "This admin has created users...plz delete users first to delete this admin!!!";
						
//						mav.addObject("status", status);
//						mav.addObject("statusMessage",statusMessage);

						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}
				}
				
				else if(fk_role_id==2)
				{
					boolean hasUploadedContents = contentService.findContentsUsingUserId(user_id);
					if(!hasUploadedContents)
					{
						userloginService.delete(user_id);
						status = true;
						statusMessage = "This ContentCreator deleted successfully!!!";

//						mav.addObject("status", status);
//						mav.addObject("statusMessage",statusMessage);

						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}
					else
					{
						status = false;
						statusMessage = "This content creator has uploaded contents....plz delete contents first to delete this user!!!";

//						mav.addObject("status", status);
//						mav.addObject("statusMessage",statusMessage);

						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}
				}
				
				else if(fk_role_id==3)
				{
					userloginService.delete(user_id);
					status = true;
					statusMessage = "This Learner is deleted successfully!!!";
					
//					mav.addObject("status", status);
//					mav.addObject("statusMessage",statusMessage);

					data.put("status", status);
					data.put("statusMessage", statusMessage);
				}
				
				
			}
			catch(Exception ex)
			{
				status = false;
				statusMessage = "Something went wrong....plz try again later!!!";
				
//				mav.addObject("status", status);
//				mav.addObject("statusMessage",statusMessage);
				
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}
		}
		
		
//		return mav;
		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}
	


	// GET Domain ID,Name,Tags using user_id

	@RequestMapping("/api/users/getDomainDetails/{user_id}")
	public ResponseEntity<?> getDomainDetailsByUserId(@PathVariable(name = "user_id") int user_id)
	{
		//	userlogin.setUser_id(6);
		System.out.println("User_id"+user_id);
		String domain_tags = "";
		String domain_name = "";
		Domain domain = new Domain();
//		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		Map<String, Object> data = new HashMap<String,Object>();

		try
		{
			Userlogin userlogin = userloginService.get(user_id);
			System.out.println("domain id from userlogin"+userlogin.getFk_domain_id());
			if(userlogin.getFk_role_id()==1)
			{
				status = true;
				statusMessage = "You are logged in as an ADMIN... no fixed domain allotted against you!!!";
				domain_tags = "Tags are not applicable for ADMIN!!!";


//				mav.addObject("userlogin", userlogin);
//				mav.addObject("status", status);
//				mav.addObject("statusMessage",statusMessage);
				
				
				data.put("userlogin", userlogin);
				data.put("status", status);
				data.put("statusMessage", statusMessage);
			}

			else if(userlogin.getFk_role_id()==2)
			{
				// Converting string to integer
				int domain_id = Integer.parseInt(userlogin.getFk_domain_id());
				// Get domain class object using domian_id
				domain = domainService.get(domain_id);
				if(domain.getStatus().equals("ACTIVE"))
				{
					//Setting Status,statusMessage 
					status = true;
					statusMessage = "You have already been assigned "+domain_name+" Domain";
					
					//Adding to modelandview object
//					mav.addObject("domain",domain);
//					mav.addObject("status", status);
//					mav.addObject("statusMessage",statusMessage);
					
					data.put("domain", domain);
					data.put("status", status);
					data.put("statusMessage", statusMessage);
				}
				
				else if(domain.getStatus().equals("INACTIVE"))
				{
					//Setting Status,statusMessage 
					status = true;
					statusMessage = "Assigned domain "+domain_name+"is kept inactivated... ";
					
					//Adding to modelandview object
//					mav.addObject("domain",domain);
//					mav.addObject("status", status);
//					mav.addObject("statusMessage",statusMessage);
					
					
					data.put("domain", domain);
					data.put("status", status);
					data.put("statusMessage", statusMessage);
				}
				
			}

			else if(userlogin.getFk_role_id()==3)
			{
				String[] domain_ids = userlogin.getFk_domain_id().split(",");
				List<Domain> domainList = new ArrayList<Domain>();
				
				for(int i=0;i<domain_ids.length;i++)
				{


					int domain_id = Integer.parseInt(domain_ids[i]);
					domain = domainService.get(domain_id);
					if(domain.getStatus().equals("ACTIVE"))
					{
						domainList.add(domain);
						status = true;
						statusMessage = "You have permission for domain:"+domain_name;	
												
						data.put("status", status);
						data.put("statusMessage", statusMessage);
					}

					else if(domain.getStatus().equals("INACTIVE"))
					{
						//Setting Status,statusMessage 
						status = true;
						statusMessage = "Assigned domain "+domain_name+"is kept inactivated... ";
						
						//Adding to modelandview object
//						mav.addObject("domain",domain);
//						mav.addObject("status", status);
//						mav.addObject("statusMessage",statusMessage);
						
						
						data.put("domain", domain);
						data.put("status", status);
						data.put("statusMessage", statusMessage);
						
						
					}
				}
				
//				mav.addObject("domainList", domainList);
				data.put("domainList", domainList);
			}
		}



		catch (Exception e) {
			Userlogin userlogin = new Userlogin();
			statusMessage = "Something went wrong...no user found!!!";
			status = false;
			//domain_tags = "Tags unavailable!!!";

//			mav.addObject("userlogin", userlogin);
//			mav.addObject("user_id",user_id);
//			mav.addObject("status", status);
//			mav.addObject("statusMessage",statusMessage);
			
			data.put("userlogin", userlogin);
			data.put("user_id", user_id);
			data.put("status", status);
			data.put("statusMessage", statusMessage);

		}


		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}
	
		
	
}
