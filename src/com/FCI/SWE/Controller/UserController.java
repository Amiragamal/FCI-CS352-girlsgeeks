package com.FCI.SWE.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.bytecode.Descriptor.Iterator;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */
@Path("/")
@Produces("text/html")
public class UserController {

	/**
	 * Action function to render Signup page, this function will be executed
	 * using url like this /rest/signup
	 * 
	 * @return sign up page
	 */
	@GET
	@Path("/signup")
	public Response signUp() {
		return Response.ok(new Viewable("/jsp/register")).build();
	}

	@GET
	@Path("/signout")
	public Response signout() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}
	/**
	 * Action function to render home page of application, home page contains
	 * only signup and login buttons
	 * 
	 * @return enty point page (Home page of this application)
	 */
	@GET
	@Path("/")
	public Response index() {
		System.out.println("here");
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}

	/**
	 * Action function to render login page this function will be executed using
	 * url like this /rest/login
	 * 
	 * @return login page
	 */
	@GET
	@Path("/login")
	public Response login() {
		return Response.ok(new Viewable("/jsp/login")).build();
	}
	
	


	/**
	 * Action function to response to signup request, This function will act as
	 * a controller part and it will calls RegistrationService to make
	 * registration
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided user password
	 * @return Status string
	 */
	@POST
	@Path("/response")
	@Produces(MediaType.TEXT_PLAIN)
	public String response(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/RegistrationService";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname=" + uname + "&email=" + email
					+ "&password=" + pass;
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Registered Successfully";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return "Failed";
	}

	/**
	 * Action function to response to login request. This function will act as a
	 * controller part, it will calls login service to check user data and get
	 * user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return Home page view
	 */
	@POST
	@Path("/home")
	@Produces("text/html")
	public Response home(@FormParam("uname") String uname,
			@FormParam("password") String pass) {
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/LoginService";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uname=" + uname + "&password=" + pass;
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			Map<String, String> map = new HashMap<String, String>();
			UserEntity user = UserEntity.getUser(object.toJSONString());
			map.put("name", user.getName());
			map.put("email", user.getEmail());
			return Response.ok(new Viewable("/jsp/home", map)).build();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return null;
	}
	
	/**
	 * Action function to response to send request. This function will act as a
	 * controller part, it will calls sendrequest service to add request to the datastor
	 * 
	 * 
	 * @param userto
	 *            user that will recieve the request
	 * @param userfrom
	 *            user that will send the request
	 * @return a message to the user wether success or failed
	 */
	@POST
	@Path("/req")
	@Produces(MediaType.TEXT_PLAIN)
	public String sendReq(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom){
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/ReqService";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "userto=" + userto + "&userfrom=" + userfrom;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Request Sent	";
			else if (object.get("Status").equals("NO")){return "User Doesn't Exist ";}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Failed";
}
	

	
	/**
	 * Action function to response to view request. This function will act as a
	 * controller part, it will calls view service to retirieve requests from the datastore
	 * 
	 * 
	 * @param userfrom
	 *          the currently logged in user
	 * @return a notifications page view
	 */
	
   @POST
   @Path("/notif")
	@Produces("text/html")
	public Response viewnotifications(@FormParam("uf") String userfrom){
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/ViewService";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uf=" + userfrom ;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Map<String, String> map = new HashMap<String, String>();
			int nofreq=Integer.parseInt( object.get("nor").toString());
			System.out.println("number of requests is :"+nofreq);
			if(nofreq==0){map.put("request0", "<br><h3>You Have No friend Requests !</h3>");}
			else{for(int i=0;i<nofreq;i++){
			map.put("request"+i,object.get("request"+i).toString());}}
			return Response.ok(new Viewable("/jsp/notifications", map)).build();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
   
   /**
	 * Action function to response to ignore request. This function will act as a
	 * controller part, it will calls ignore service to change requests accepted column in the datastore
	 * 
	 * 
	 * @param userfrom
	 *          the currently logged in user
	 * @return a message wether success or failed
	 */
   @POST
   @Path("/ignore")
	@Produces(MediaType.TEXT_PLAIN)
	public String ignorerequest(@FormParam("userfrom") String userfrom){
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/IgnoreService";
		System.out.println(userfrom+" fel controller");
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "userfrom=" + userfrom ;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Request Ignored";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";
		
	
	}
   
   /**
  	 * Action function to response to accept request. This function will act as a
  	 * controller part, it will calls accept service to change friends table in the datastore
  	 * 
  	 * 
  	 * @param userfrom
  	 *          the currently logged in user
  	 * @return a message wether success or failed
  	 */
   @POST
   @Path("/accept")
	@Produces(MediaType.TEXT_PLAIN)
	public String acceptfriend(@FormParam("userfrom") String userfrom){
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/AcceptService";
		try {
			URL url = new URL(serviceUrl);

			String urlParameters = "userfrom=" + userfrom ;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Added";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";
		
	
	}
	
   /**
  	 * Action function to response to show friends request. This function will act as a
  	 * controller part, it will calls showfriends service to retrieve friends the datastore
  	 * 
  	 * 
  	 * @param userfrom
  	 *          the currently logged in user
  	 * @return a friends page view
  	 */
   @POST
   @Path("/friends")
	@Produces("text/html")
	public Response viewfriends(@FormParam("uf") String userfrom){
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/ViewFriendService";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "uf=" + userfrom ;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Map<String, String> map = new HashMap<String, String>();
			int noffrnds=Integer.parseInt( object.get("nof").toString());
			System.out.println("number of friends is :"+noffrnds);
			if(noffrnds==0){map.put("friend0", "<br><h3>You Have No friends :( !</h3>");}
			else{for(int i=0;i<noffrnds;i++){
			map.put("friend"+i,object.get("friend"+i).toString());}}
			return Response.ok(new Viewable("/jsp/friends", map)).build();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
   /**
  	 * Action function to response to search request. This function will act as a
  	 * controller part, it will calls view user service to retrieve users from database
  	 * 
  	 * 
  	 * @param userfrom
  	 *          the currently logged in user
  	 *          @param userto
  	 *          the user im trying to search for
  	 * @return a search page view
  	 */
   
   @POST
   @Path("/users")
	@Produces("text/html")
	public Response viewusers(@FormParam("userfrom") String userfrom,@FormParam("userto") String userto){
		String serviceUrl = "http://1-dot-socialnetwork-31.appspot.com/rest/ViewUsersService";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "userfrom=" + userfrom  + "&userto=" + userto;;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Map<String, String> map = new HashMap<String, String>();
			int nofusers=Integer.parseInt( object.get("nou").toString());
			System.out.println("number of users is :"+nofusers);
			if(nofusers==0){map.put("user0", "<br><h3> No Users With Such Name !</h3>");}
			else{for(int i=0;i<nofusers;i++){
			map.put("user"+i,object.get("user"+i).toString());}}
			return Response.ok(new Viewable("/jsp/search", map)).build();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
   
 //1 to 1 message 
   
   @POST
	@Path("/msg")
	@Produces(MediaType.TEXT_PLAIN)
	public String sendMsg(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom){
		String serviceUrl = "http://localhost:8888/rest/MsgService";
		try {
			URL url = new URL(serviceUrl);
			String urlParameters = "userto=" + userto + "&userfrom=" + userfrom;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "message read	";
			else if (object.get("Status").equals("NO"))
			  {return "message unread ";}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Failed";
}
	

   @POST
   @Path("/read")
	@Produces(MediaType.TEXT_PLAIN)
	public String readmsg(@FormParam("userfrom") String userfrom){
		String serviceUrl = "http://localhost:8888/rest/ReadService";
		try {
			URL url = new URL(serviceUrl);

			String urlParameters = "userfrom=" + userfrom ;
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(60000);  //60 Seconds
			connection.setReadTimeout(60000);  //60 Seconds
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String line, retJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				retJson += line;
			}
			writer.close();
			reader.close();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "message added to inbox";
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";
		
	
	}
   
	
}
