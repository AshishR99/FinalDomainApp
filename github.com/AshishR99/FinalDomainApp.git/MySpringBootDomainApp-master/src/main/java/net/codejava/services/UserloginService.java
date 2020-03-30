package net.codejava.services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import net.codejava.entities.UpdatePassword;
import net.codejava.entities.Userlogin;
import net.codejava.payload.ForgetPassword;
import net.codejava.payload.ForgotStatus;
import net.codejava.payload.SignUpPayload;
import net.codejava.payload.UserloginRolePayload;
import net.codejava.repositories.UserloginRepository;
import net.codejava.repositories.UserloginRoleRepository;

@Service
@Transactional
public class UserloginService {

	private static final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;

	private boolean status = false; //Changed by me
	static String unique_password="";// Changed by me for forgot password

	@Autowired
	private UserloginRepository userloginRepository;

	@Autowired
	private UserloginRoleRepository userloginRoleRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private PasswordEncoder passowrdEncoder;
	

	@Autowired
	PasswordEncoder passwordEncoder;

	public List<Userlogin> listAll() {
		return userloginRepository.findAll();
	}

	public List<UserloginRolePayload> listAllUsersWithRoleDesc() {

		return userloginRoleRepository.findAllUsersWithRoleDesc();
	}
	
	
/*
	public boolean save(Userlogin userlogin) {

		try {
			Userlogin userloginobj = userloginRepository.findByUserEmail(userlogin.getUserEmail());
			if (userloginobj == null) {
				// Generate Salt. The generated value can be stored in DB.
				String salt = getSalt(30);
				System.out.println("Salt value" + salt);

				// Protect user's password. The generated value can be stored in DB.
				String mySecurePassword = generateSecurePassword(userlogin.getUser_password(), salt);

				// setting encrypted password & salt value to entity class
				userlogin.setUser_password(mySecurePassword);
				userlogin.setSalt_value(salt);
				try {
					userloginRepository.save(userlogin);
					status = true;
				}

				catch (Exception ex) {
					status = false;
				}

			}

			else {
				status = false;
			}

		}

		catch (Exception ex) {
			status = false;
		}
		return status;

	}

*/
	
	
	public boolean save(SignUpPayload signUpPayload) {

		try {
			Userlogin userloginobj = userloginRepository.findByUserEmail(signUpPayload.getUserEmail());
			if (userloginobj == null) {
				Userlogin userlogin=new Userlogin();
				userlogin.setCreated_by(signUpPayload.getCreated_by());
				userlogin.setFk_domain_id(signUpPayload.getFk_domain_id());
				userlogin.setFk_role_id(signUpPayload.getFk_role_id());
				userlogin.setUser_id(signUpPayload.getUserId());
				userlogin.setUserEmail(signUpPayload.getUserEmail());
				userlogin.setUser_name(signUpPayload.getUser_name());
				userlogin.setUser_password(passowrdEncoder.encode(signUpPayload.getUser_password()));
				userlogin.setUser_phone(signUpPayload.getUser_phone());
				
				// Protect user's password. The generated value can be stored in DB.
				
				
				try {
					userloginRepository.save(userlogin);
					status = true;
				}

				catch (Exception ex) {
					status = false;
				}

			}

			else {
				status = false;
			}

		}

		catch (Exception ex) {
			status = false;
		}
		return status;

	}


	public boolean update(Userlogin userlogin) {

		try {
			Userlogin userloginobj = userloginRepository.findByUserEmail(userlogin.getUserEmail());
			String user_password = userloginobj.getUser_password();
			String salt_value = userloginobj.getSalt_value();
			int created_by = userloginobj.getCreated_by();

			// Setting in userlogin
			userlogin.setCreated_by(created_by);
			userlogin.setUser_password(user_password);
			userlogin.setSalt_value(salt_value);

			// Updating the details
			try {
				userloginRepository.save(userlogin);
				status = true;
			}

			catch (Exception ex) {
				status = false;
			}

		}

		catch (Exception ex) {
			status = false;
		}

		return status;
	}

	public Userlogin get(int id) {
		try {
			return userloginRepository.findById(id).get();
		}

		catch (Exception ex) {
			Userlogin userlogin = new Userlogin();
			return userlogin;
		}

	}

	public void delete(int id) {
		userloginRepository.deleteById(id);
	}

	public Userlogin checkUserLoginDetails(String userEmail, String user_pwd) {
		
		System.out.println("Provided email and password :" + "" + userEmail + "<-->" + user_pwd);
		
		String providedPassword = user_pwd;
		Userlogin userlogin = userloginRepository.findByUserEmail(userEmail);
		if(passowrdEncoder.matches(user_pwd,userlogin.getUser_password())){
			return userlogin;
		}else {
			userlogin = new Userlogin();
			return userlogin;
		}

		// Generate Salt. The generated value can be stored in DB.
//		String salt = userlogin.getSalt_value();
//		String securedPassword = userlogin.getUser_password();
//		boolean isMatched = verifyUserPassword(providedPassword, securedPassword, salt);
//		System.out.println("Whether is matched ?" + isMatched);
//		if (isMatched) {
//			return userlogin;
//		} else {
//			userlogin = new Userlogin();
//			return userlogin;
//		}
//
//		 return userloginRepository.findByCredentials(user_email,user_pwd);

	}

	// Find users by Role id
	public List<Userlogin> findByRoleId(int role_id) {
		return userloginRepository.find_by_role_id(role_id);
	}

	// Find User Creator by User ID
	public boolean findCreatorByUserId(int user_id) {
		List<Userlogin> listUsersCreatedAnotherUsers = userloginRepository.searchCreatorByUserId(user_id);
		if (!listUsersCreatedAnotherUsers.isEmpty()) {
			status = true;
		}

		else {
			status = false;
		}

		return status;

	}

	// Find Allocated Domain IDs for a particular user
	public String findDomainByUserId(int user_id) {
		return userloginRepository.findDomainIdByUserId(user_id);
	}

	// Find Allocated Role IDs for a particular user
	public int findRoleIdByUserId(int user_id) {
		return userloginRepository.findRoleIdByUserId(user_id);
	}

	// Password encryption

	public static String getSalt(int length) {
		StringBuilder returnValue = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(returnValue);
	}

	public static byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	public static String generateSecurePassword(String password, String salt) {
		String returnValue = null;
		byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

		returnValue = Base64.getEncoder().encodeToString(securePassword);

		return returnValue;
	}

	public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
		boolean returnValue = false;

		// Generate New secure password with the same salt
		String newSecurePassword = generateSecurePassword(providedPassword, salt);

		// Check if two passwords are equal
		returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

		return returnValue;
	}



//*************************************************************************************
//_____________________________________________________________________________________

	/* PASSWORD INTEGRATION DATE:03/03/2020 */

	public boolean checkUserEmail(String usernameOrEmail) {
		Boolean isExists=userloginRepository.existsByuserEmail(usernameOrEmail);
		// TODO Auto-generated method stub
		return isExists;		
	}

	public boolean forgotPassword(String usernameOrEmail) {
		// TODO Auto-generated method stub
//		long code=Code();
//		for (long i=code;i!=0;i/=100)//a loop extracting 2 digits from the code  
//		{ 
//			long digit=i%100;//extracting two digits 
//			if (digit<=90) 
//				digit=digit+32;  
//			//converting those two digits(ascii value) to its character value 
//			char ch=(char) digit; 
//			// adding 32 so that our least value be a valid character  
//			unique_password=ch+unique_password;//adding the character to the string 
//		} 
		Random rand=new Random();
		int uniqueValue=rand.nextInt(1000000);
		unique_password=String.valueOf(uniqueValue);
		Optional<Userlogin> user=userloginRepository.findByuserEmail(usernameOrEmail);
		Userlogin getUser=user.get();
		boolean istrue=sendEmail(unique_password,usernameOrEmail);
		return istrue;

	}

	public boolean sendEmail(String unique_password,String userOrEmail) {
		//logger.info("OTP Password is "+unique_password);
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(userOrEmail);
		msg.setSubject("Forget Password");
		msg.setText("Dear Userlogin ,\n" +
				"\n" +
				"The OTP generated for your Account with ID "+ userOrEmail +"  is : " + unique_password
				+"\n\n"+
				"\nUse this OTP to change the password\n"+
				"In case of any queries, kindly contact our customer service desk at the details below\n" +
				"\n" +
				"\n" +
				"Warm Regards,\n" +
				"\n" +
				"Aroha Technologies");
		try {
			javaMailSender.send(msg);
			//logger.info("Email sent to registered email");
			return true;
		}catch(Exception ex) {		
			System.out.println(ex.getMessage());

		}
		return false;

	}

	public static long Code() //this code returns the  unique 16 digit code  
	{  //creating a 16 digit code using Math.random function 
		long code   =(long)((Math.random()*9*Math.pow(10,15))+Math.pow(10,15)); 
		return code; //returning the code 
	}

	public Object updatePassword(ForgetPassword object) {
		String getOtpFromUser=object.getOneTimePass();
		ForgotStatus forgetpw =null;
		if(getOtpFromUser.equals(unique_password)) {
			String email=object.getUsernameOrEmail();
			Optional<Userlogin> obj=userloginRepository.findByuserEmail(email);
			Userlogin user=obj.get();
			if(passwordEncoder.matches(object.getPassword(), user.getUser_password()) ){
				//logger.info("Error You can not give previous password, please enter a new password");
				forgetpw=new ForgotStatus(Boolean.FALSE,  "You can not give previous password, please enter a new password");
				return forgetpw;
			}else {
				user.setUser_password((passwordEncoder.encode(object.getPassword())));
				userloginRepository.save(user);
				//logger.info("password changed for :"+user.getName());
				forgetpw=new ForgotStatus(Boolean.TRUE,"Password updated successfully, please login with your new password");
				return forgetpw;
			}
		}else {
			
			//logger.error("OTP didn't matched");
			forgetpw=new ForgotStatus(Boolean.FALSE, "OTP did not match");
			return forgetpw;
		}	
	}
public int resetPassword(UpdatePassword password) {

	int updatePassword=userloginRepository.updatePassword(passowrdEncoder.encode(password.getPassword()),password.getUserId());
	
	if(updatePassword!=0) {
		return 1;
		
		
	}
	return 0;
}
	
}




