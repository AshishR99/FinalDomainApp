package net.codejava.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codejava.entities.UserActivity;
import net.codejava.entities.Userlogin;
import net.codejava.payload.UserloginUserActivityPayload;
import net.codejava.repositories.UserActivityRepository;
import net.codejava.repositories.UserloginRepository;
import net.codejava.repositories.UserloginUserActivityRepository;

@Service
public class UserActivityService {

	boolean status = false;
	boolean isLoggedIn = false; 
	List<UserloginUserActivityPayload> allUserActivityList = new ArrayList<UserloginUserActivityPayload>();

	List<UserloginUserActivityPayload> particularUserActivityList = new ArrayList<UserloginUserActivityPayload>();
	
	
	@Autowired
	private UserActivityRepository userActivityRepo;
	
	@Autowired
	private UserloginRepository userloginRepository;
	
	@Autowired
	private UserloginUserActivityRepository userloginUserActivityRepo;
	
	public boolean saveUserActivity(int user_id) 
	{
				
		UserActivity userActivity= new UserActivity();
				
		//setting User ID 
		userActivity.setFk_user_id(user_id);
		
		//Setting Login Date & time 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String login_time = sdf.format(date);
		userActivity.setLogin_time(login_time);
		
		//Setting Logout date & Time
		String logout_time = "NA";
		userActivity.setLogout_time(logout_time);
		
		//Setting Loggedin Activity Time
		String loggedin_activity_time = "NA";
		userActivity.setLoggedin_activity_time(loggedin_activity_time);
		
		
			
		try
		{
			System.out.println("UserActiv"+userActivity);
			userActivityRepo.save(userActivity);
			status = true;
		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
			status = false;
		}
			
		return status;

	}
	
	public UserActivity getUserLastActivityData(int user_id, String logout_time, String user_activity_time) throws Exception
	{
		UserActivity userActivity= new UserActivity();
		userActivity = userActivityRepo.findUserLastActivityData(user_id, logout_time, user_activity_time);
		
		//creating log out time and setting into Entity class
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		logout_time = sdf.format(date);
		
		//Get login time from db
		String login_time = userActivity.getLogin_time();
		
		
		//calculate user activity time by deducting login time from logout time
		Date d_in = sdf.parse(login_time);
		Date d_out = sdf.parse(logout_time);
		Long active_timing_in_sec = (d_out.getTime()-d_in.getTime())/1000;
		Long activity_sec = active_timing_in_sec%60;
		Long active_timing_in_min = active_timing_in_sec/60;
		Long activity_min = active_timing_in_min%60;
		Long activity_hour = active_timing_in_min/60;
		String loggedin_activity_time = activity_hour+"h "+activity_min+"min "+activity_sec+"sec ";
		
//		String loggedin_activity_time = String.valueOf(active_timing);
		
		
		userActivity.setLogout_time(logout_time);
		userActivity.setLoggedin_activity_time(loggedin_activity_time);
		System.out.println("Logout time"+userActivity);
		
		userActivityRepo.save(userActivity);
		return userActivity;
	}
	
	public boolean isUserLoggedIn(int user_id) throws Exception
	{
		String logout_time = "NA";
		String user_activity_time = "NA";
		try
		{
			UserActivity userActivity = userActivityRepo.findUserLastActivityData(user_id,logout_time,user_activity_time);
			if(userActivity.getFk_user_id()>0 && userActivity!=null)
			{
				isLoggedIn = true;
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			isLoggedIn = false;
		}
		
		
		
		return isLoggedIn;
	}
	
	public List<UserloginUserActivityPayload> getDateBasedParticularUserActivityList(int user_id,int no_of_days)
	{
		List<UserloginUserActivityPayload> particularUserActivityFinalList = new ArrayList<UserloginUserActivityPayload>();
		try
		{
			particularUserActivityList = userloginUserActivityRepo.findUserActivityByUserId(user_id);
			System.out.println("particular activity liust size "+particularUserActivityList.size());
			for(int i=0;i<particularUserActivityList.size();i++)
			{
				UserloginUserActivityPayload userActivityWithUsernameObj = particularUserActivityList.get(i);
				String login_time = userActivityWithUsernameObj.getLogin_time();
				Date date_in = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(login_time);

				// get Calendar instance
				Calendar cal = Calendar.getInstance();

				// substract no of days
				// If we give 7 there it will give 8 days back
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-no_of_days-1);

				// convert to before no of days specified before date
				Date back_date = cal.getTime();
				
				//Current Date
				Date curr_date = new Date();
				
				//Adding filtered data (Filtered out data not in between the specified date range & filtered out data loggedin currently having NA in 'logout_time'&'loggedinactivity' 
				if(date_in.after(back_date) || date_in.compareTo(back_date)==0 && date_in.before(curr_date) || date_in.compareTo(curr_date)==0)
				{
					if(!userActivityWithUsernameObj.getLogout_time().equalsIgnoreCase("NA") && !userActivityWithUsernameObj.getLoggedin_activity_time().equalsIgnoreCase("NA")) 
					{
						particularUserActivityFinalList.add(userActivityWithUsernameObj);
					}
				}
			}
			
		}
		catch(Exception ex)
		{
			particularUserActivityFinalList = new ArrayList<UserloginUserActivityPayload>();
		}
		
		return particularUserActivityFinalList;
	}
	
	
	//Get All users activity List
	public List<UserloginUserActivityPayload> getDateBasedAllUsersActivityList(int no_of_days)
	{
		List<UserloginUserActivityPayload> allUserActivityFinalList = new ArrayList<UserloginUserActivityPayload>();
		try
		{
//			allUserActivityList = userActivityRepo.findAll();
			allUserActivityList = userloginUserActivityRepo.findAllUsersActivityDetailsWithUsernames();
			for(int i=0;i<allUserActivityList.size();i++)
			{
				
				//Get User Login Date & Time from DB
				UserloginUserActivityPayload userActivityWithUsernameObj = allUserActivityList.get(i);
				String login_time = userActivityWithUsernameObj.getLogin_time();
				Date date_in = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(login_time);

				// get Calendar instance
				Calendar cal = Calendar.getInstance();

				// substract no of days
				// If we give 7 there it will give 8 days back
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-no_of_days-1);

				// convert to before no of days specified before date
				Date back_date = cal.getTime();
				
				//Current Date
				Date curr_date = new Date();
				
				//Adding filtered data (Filtered out data not in between the specified date range & filtered out data loggedin currently having NA in 'logout_time'&'loggedinactivity' 
				if(date_in.after(back_date) || date_in.compareTo(back_date)==0 && date_in.before(curr_date) || date_in.compareTo(curr_date)==0)
				{
					if(!userActivityWithUsernameObj.getLogout_time().equalsIgnoreCase("NA") && !userActivityWithUsernameObj.getLoggedin_activity_time().equalsIgnoreCase("NA"))
					{
						allUserActivityFinalList.add(userActivityWithUsernameObj);
						
					}
				}
				
				
			}
			
			
		}
		
		catch(Exception ex)
		{
			allUserActivityFinalList = new ArrayList<UserloginUserActivityPayload>();
		}
		
		return allUserActivityFinalList;
	}
	
	//Find Allocated Role ID for a particular user
	public int getRoleIdByUserId(int user_id)
	{
		return userloginRepository.findRoleIdByUserId(user_id);
	}

}
