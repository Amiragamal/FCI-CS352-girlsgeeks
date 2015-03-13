package com.FCI.SWE.Services;

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

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.UserEntity;

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
public class Service {
	
	
	
	/*@GET
	@Path("/index")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}*/


		/**
	 * Registration Rest service, this service will be called to make
	 * registration. This function will store user data in data store
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided password
	 * @return Status json
	 */
	@POST
	@Path("/RegistrationService")
	public String registrationService(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		UserEntity user = new UserEntity(uname, email, pass);
		user.saveUser();
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * @param uname provided user name
	 * @param pass provided user password
	 * @return user in json format
	 */
	@POST
	@Path("/LoginService")
	public String loginService(@FormParam("uname") String uname,
			@FormParam("password") String pass) {
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(uname, pass);
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
		} 

		return object.toString();
	}
	/**
	 * req Rest Service, this service will be called to make request process
	 * also will add the request to requests table indatastore
	 * @param userto the user ill send the request for
	 * @param userfrom the current logged in user
	 * @return a status in json format
	 */
	@POST
	@Path("/ReqService")
	public String SendReqService(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom) {
		JSONObject object = new JSONObject();
		String user = UserEntity.sendReq(userfrom, userto);
       if(user.equals("ok"))
		object.put("Status", "OK");
       else 
    	   object.put("Status","NO");
       
		return object.toString();
		
	}
	/**
	 * View Rest Service, this service will be called to view request process
	 * also will retrieve the requests from datastore
	 * @param userfrom the current logged in user
	 * @return requests in json format
	 */
	
	@POST
	@Path("/ViewService")
	public String viewrequest(@FormParam("uf") String userfrom) {
		JSONObject object = new JSONObject();
		List request = UserEntity.viewrequests(userfrom);
       if(request.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<request.size();i++){
       object.put("request"+i, request.get(i));}
	}
       object.put("nor", request.size());
		return object.toString();}
	
	/**
	 * ignore Rest Service, this service will be called to ignore request process
	 * also will change the requests accepted column to ignore in datastore
	 * @param userfrom the current logged in user
	 * @return status in json format
	 */
	@POST
	@Path("/IgnoreService")
	public String ignorerequest(@FormParam("userfrom") String userfrom) {
		JSONObject object = new JSONObject();
		String ignore=UserEntity.ignorereq(userfrom);
       if(ignore.equals(null)){object.put("Status", "Failed");}
       else{
       object.put("Status","OK");}
	
		return object.toString();}
	/**
	 * accept Rest Service, this service will be called to accpet request process
	 * also will add friend to friends table in datastore
	 * @param userfrom the current logged in user
	 * @return status in json format
	 */
	@POST
	@Path("/AcceptService")
	public String acceptrequest(@FormParam("userfrom") String userfrom) {
		JSONObject object = new JSONObject();
		String accept=UserEntity.acceptReq(userfrom);
       if(accept.equals(null)){object.put("Status", "Failed");}
       else{
       object.put("Status","OK");}
		return object.toString();}
	
	/**
	 * Viewfriend Rest Service, this service will be called to view friends  process
	 * also will retrieve friends from datastore
	 * @param userfrom the current logged in user
	 * @return friends in json format
	 */
	@POST
	@Path("/ViewFriendService")
	public String viewfriends(@FormParam("uf") String userfrom) {
		JSONObject object = new JSONObject();
		List friends = UserEntity.getfriends(userfrom);
       if(friends.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<friends.size();i++){
       object.put("friend"+i, friends.get(i));}
	}
       object.put("nof", friends.size());
		return object.toString();}

	/**
	 * Viewusers Rest Service, this service will be called to view users  process
	 * also will retrieve the users from datastore
	 * @param userfrom the current logged in user
	 * 	 * @param userto the user im searching for

	 * @return users in json format
	 */
	
	@POST
	@Path("/ViewUsersService")
	public String viewusers(@FormParam("userfrom") String userfrom,@FormParam("userto") String userto) {
		JSONObject object = new JSONObject();
		List users = UserEntity.viewusers(userfrom,userto);
       if(users.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<users.size();i++){
       object.put("user"+i, users.get(i));}
	}
       object.put("nou", users.size());
		return object.toString();}
}
