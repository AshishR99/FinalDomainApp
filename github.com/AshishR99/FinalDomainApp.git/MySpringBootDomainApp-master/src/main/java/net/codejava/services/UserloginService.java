package net.codejava.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import net.codejava.entities.Userlogin;
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

	private boolean status = false;

	@Autowired
	private UserloginRepository userloginRepository;

	@Autowired
	private UserloginRoleRepository userloginRoleRepository;

	public List<Userlogin> listAll() {
		return userloginRepository.findAll();
	}

	public List<UserloginRolePayload> listAllUsersWithRoleDesc() {

		return userloginRoleRepository.findAllUsersWithRoleDesc();
	}


	public boolean save(Userlogin userlogin) {
	
		try
		{
			Userlogin userloginobj = userloginRepository.findByUserEmail(userlogin.getUser_email());
			if(userloginobj==null)
			{
				//Generate Salt. The generated value can be stored in DB. 
				String salt = getSalt(30);
				System.out.println("Salt value"+salt);

				// Protect user's password. The generated value can be stored in DB.
				String mySecurePassword = generateSecurePassword(userlogin.getUser_password(), salt);

				//setting encrypted password & salt value to entity class
				userlogin.setUser_password(mySecurePassword);
				userlogin.setSalt_value(salt);
				try
				{
					userloginRepository.save(userlogin);
					status = true;
				}

				catch(Exception ex)
				{
					status = false;
				}

			}

			else
			{
				status = false;
			}

		}


		catch(Exception ex)
		{
			status = false;
		}
		return status;

	}
	
	
	public boolean update(Userlogin userlogin)
	{
		
		
		try
		{
			Userlogin userloginobj = userloginRepository.findByUserEmail(userlogin.getUser_email());
			String user_password = userloginobj.getUser_password();
			String salt_value = userloginobj.getSalt_value();
			int created_by = userloginobj.getCreated_by();
			
			//Setting in userlogin
			userlogin.setCreated_by(created_by);
			userlogin.setUser_password(user_password);
			userlogin.setSalt_value(salt_value);
			
			//Updating the details
			try
			{
				userloginRepository.save(userlogin);
				status = true;
			}
			
			catch(Exception ex)
			{
				status = false;
			}
			
		}

		catch(Exception ex)
		{
			status = false;
		}
		
		return status;
	}
	
	
	
	
	

	public Userlogin get(int id) {
		try
		{
			return userloginRepository.findById(id).get();
		}

		catch(Exception ex)
		{
			Userlogin userlogin = new Userlogin();
			return userlogin;
		}

	}

	public void delete(int id) {
		userloginRepository.deleteById(id);
	}

	public Userlogin checkUserLoginDetails(String user_email,String user_pwd)
	{
		System.out.println("Provided email and password"+user_email+"<-->"+user_pwd);
		String providedPassword = user_pwd;
		Userlogin userlogin = userloginRepository.findByUserEmail(user_email);

		//Generate Salt. The generated value can be stored in DB. 
		String salt = userlogin.getSalt_value();
		String securedPassword = userlogin.getUser_password();
		boolean isMatched = verifyUserPassword(providedPassword,securedPassword,salt);
		System.out.println("Whether is matched ?"+isMatched);
		if(isMatched)
		{
			return userlogin;
		}
		else
		{
			userlogin = new Userlogin();
			return userlogin;
		}


		//   	return userloginRepository.findByCredentials(user_email,user_pwd);



	}


	//Find users by Role id
	public List<Userlogin> findByRoleId(int role_id)
	{
		return userloginRepository.find_by_role_id(role_id);
	}

	//Find User Creator by User ID
	public boolean findCreatorByUserId(int user_id)
	{
		List<Userlogin> listUsersCreatedAnotherUsers  =  userloginRepository.searchCreatorByUserId(user_id);
		if(!listUsersCreatedAnotherUsers.isEmpty())
		{
			status = true;
		}

		else
		{
			status = false;
		}

		return status;

	}
	
	
	//Find Allocated Domain IDs for a particular user
	public String findDomainByUserId(int user_id)
	{
		return userloginRepository.findDomainIdByUserId(user_id);
	}

	
	//Find Allocated Role IDs for a particular user
		public int findRoleIdByUserId(int user_id)
		{
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

	public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt)
	{
		boolean returnValue = false;

		// Generate New secure password with the same salt
		String newSecurePassword = generateSecurePassword(providedPassword, salt);

		// Check if two passwords are equal
		returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

		return returnValue;
	}



	

}
