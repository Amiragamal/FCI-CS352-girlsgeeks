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
import java.util.Vector;

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
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.cloud.sql.jdbc.Connection;


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
		@Path("/createpage")
		public Response createpage() {
			return Response.ok(new Viewable("/jsp/createpage")).build();
		}
	   
	
	@GET
	@Path("/grp")
	public Response group() {
		return Response.ok(new Viewable("/jsp/grpmsg")).build();
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
	 * @throws ParseException 
	 */
	@POST
	@Path("/response")
	@Produces(MediaType.TEXT_PLAIN)
	public String response(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) throws ParseException {
		Connectionn Connection2=new Connectionn();
		String serviceUrl = "http://localhost:8888/rest/RegistrationService";
		String urlParameters = "uname=" + uname + "&email=" + email+ "&password=" + pass;
		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Registered Successfully";
		
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
	 * @throws ParseException 
	 */
	@POST
	@Path("/home")
	@Produces("text/html")
	public Response home(@FormParam("uname") String uname,
			@FormParam("password") String pass) throws ParseException {
		Connectionn Connection2=new Connectionn();
		String serviceUrl = "http://localhost:8888/rest/LoginService";
		String urlParameters = "uname=" + uname + "&password=" + pass;
		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
	
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			Vector hashtags=new Vector();
			Vector name=new Vector();
			Vector email=new Vector();
			
			Map<String, Vector> map = new HashMap<String, Vector>();
			int nofhash=Integer.parseInt( object.get("noh").toString());
			for(int i=0;i<nofhash;i++){
			 hashtags.add(object.get("hashtag"+i).toString());}
			UserEntity user = UserEntity.getUser(object.toJSONString());
			name.add(user.getName());
			email.add(user.getEmail());
			map.put("name",name );
			map.put("email",email );
			map.put("hashtags",hashtags);
			return Response.ok(new Viewable("/jsp/home", map)).build();}
		
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
	
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
	public String sendReq(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom) throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/ReqService";
		String urlParameters = "userto=" + userto + "&userfrom=" + userfrom;
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
	"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Request Sent	";
			else if (object.get("Status").equals("NO")){return "User Doesn't Exist ";}
	
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
	 * @throws ParseException 
	 */
	
   @POST
   @Path("/request")
	@Produces("text/html")
	public Response viewrequests(@FormParam("uf") String userfrom) throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/ViewService";
		String urlParameters = "uf=" + userfrom ;
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Map<String, String> map = new HashMap<String, String>();
			int nofreq=Integer.parseInt( object.get("nor").toString());
			System.out.println("number of requests is :"+nofreq);
			if(nofreq==0){map.put("request0", "<br><h3>You Have No friend Requests !</h3>");}
			else{for(int i=0;i<nofreq;i++){
			map.put("request"+i,object.get("request"+i).toString());}}
			return Response.ok(new Viewable("/jsp/requests", map)).build();
		
		
	}
   
   /**
	 * Action function to response to ignore request. This function will act as a
	 * controller part, it will calls ignore service to change requests accepted column in the datastore
	 * 
	 * 
	 * @param userfrom
	 *          the currently logged in user
	 * @return a message wether success or failed
 * @throws ParseException 
	 */
   @POST
   @Path("/ignore")
	@Produces(MediaType.TEXT_PLAIN)
	public String ignorerequest(@FormParam("userfrom") String userfrom) throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/IgnoreService";
		String urlParameters = "userfrom=" + userfrom ;
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Request Ignored";
	
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
 * @throws ParseException 
  	 */
   @POST
   @Path("/accept")
	@Produces(MediaType.TEXT_PLAIN)
	public String acceptfriend(@FormParam("userfrom") String userfrom) throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/AcceptService";
		String urlParameters = "userfrom=" + userfrom ;
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Friend Added";
		
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
 * @throws ParseException 
  	 */
   @POST
   @Path("/friends")
	@Produces("text/html")
	public Response viewfriends(@FormParam("uf") String userfrom) throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/ViewFriendService";
		String urlParameters = "uf=" + userfrom ;
		
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Map<String, String> map = new HashMap<String, String>();
			int noffrnds=Integer.parseInt( object.get("nof").toString());
			System.out.println("number of friends is :"+noffrnds);
			if(noffrnds==0){map.put("friend0", "<br><h3>You Have No friends !</h3>");}
			else{for(int i=0;i<noffrnds;i++){
			map.put("friend"+i,object.get("friend"+i).toString());}}
			return Response.ok(new Viewable("/jsp/friends", map)).build();
		
		
		
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
 * @throws JSONException 
 * @throws ParseException 
  	 */
   
   @POST
   @Path("/users")
	@Produces("text/html")
	public Response viewusers(@FormParam("userfrom") String userfrom,@FormParam("userto") String userto) throws JSONException, ParseException{

		String serviceUrl = "http://localhost:8888/rest/ViewUsersService";
		String urlParameters = "userfrom=" + userfrom  + "&userto=" + userto;
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Vector users=new Vector();
			Vector logged=new Vector(); logged.add(userfrom);
			Map<String, Vector> map = new HashMap<String, Vector>();
			int nofusers=Integer.parseInt( object.get("nou").toString());
			for(int i=0;i<nofusers;i++){
			 users.add(object.get("user"+i).toString());}
			map.put("userlist",users);
			map.put("currentuser", logged);
			return Response.ok(new Viewable("/jsp/search", map)).build();
		
		
	}
   
   
   
   @POST
	@Path("/sendtogrp")
	@Produces(MediaType.TEXT_PLAIN)
	public String groupmsg(@FormParam("to") String usersto,@FormParam("convname") String convname,@FormParam("msgbody") String msgbody) throws ParseException {
		String serviceUrl = "http://localhost:8888/rest/SendgroupService";
		String urlParameters = "to=" + usersto + "&convname=" + convname+ "&msgbody=" + msgbody;

		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return"Message Sent Successfuly";
	
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */

		return "failed to send group msg";
	}
   
   
   @POST
  	@Path("/showgrp")
  	@Produces("text/html")
  	public Response shhowgroup() throws ParseException {
  		String serviceUrl = "http://localhost:8888/rest/ShowgroupService";
  		Connectionn Connection2=new Connectionn();

  		String retJson = Connection2.connect(serviceUrl, null, "POST",
  						"application/x-www-form-urlencoded;charset=UTF-8");
  			JSONParser parser = new JSONParser();
  			Object obj = parser.parse(retJson);
JSONObject object = (JSONObject) obj;
  			Vector groups=new Vector();
			//Vector logged=new Vector(); logged.add(userfrom);
			Map<String, Vector> map = new HashMap<String, Vector>();
			int nofgrps=Integer.parseInt( object.get("nog").toString());
			for(int i=0;i<nofgrps;i++){
			 groups.add(object.get("grps"+i).toString());}
			map.put("grplist",groups);
			return Response.ok(new Viewable("/jsp/showgrps", map)).build();
	
  	}
   @POST
 	@Path("/showconv")
 	@Produces("text/html")
 	public Response shhowconv(@FormParam("convname") String convname) throws ParseException {
 		String serviceUrl = "http://localhost:8888/rest/ShowconvService";
		String urlParameters = "convname=" + convname  ;
		
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
 			JSONParser parser = new JSONParser();
 			Object obj = parser.parse(retJson);
            JSONObject object = (JSONObject) obj;
 			Vector msgs=new Vector();
 			Vector members=new Vector();
 			Vector senders=new Vector();
            Vector convnamee=new Vector();
            convnamee.add(convname);
			Map<String, Vector> map = new HashMap<String, Vector>();
			int nofmsgs=Integer.parseInt( object.get("nom").toString());
			int nofmms=Integer.parseInt( object.get("nomem").toString());
			int nofsnd=Integer.parseInt( object.get("nos").toString());

			for(int i=0;i<nofmsgs;i++){
			 msgs.add(object.get("msg"+i).toString());}
			map.put("msgs",msgs);
			for(int i=0;i<nofmms;i++){
				 members.add(object.get("member"+i).toString());}

				map.put("members",members);
				for(int i=0;i<nofsnd;i++){
					 senders.add(object.get("sender"+i).toString());}
					map.put("senders",senders);
					map.put("name",convnamee);
			return Response.ok(new Viewable("/jsp/showconv", map)).build();
 			
 	}
 
   @POST
	@Path("/replygrp")
	@Produces(MediaType.TEXT_PLAIN)
	public String replygrp(@FormParam("msgbody") String msgbody,@FormParam("convname") String convname) throws ParseException {
		String serviceUrl = "http://localhost:8888/rest/ReplyService";
		String urlParameters = "msgbody=" + msgbody + "&convname=" + convname;
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return"Message Sent Successfuly";
		

		return "failed to send group msg";
	}
  
   
   @POST
	@Path("/shownotif")
	@Produces("text/html")
	public Response shownotif() throws ParseException {
		String serviceUrl = "http://localhost:8888/rest/ShownotifService";
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, null, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
           JSONObject object = (JSONObject) obj;
			Vector reqs=new Vector();
			Vector grpmsgs=new Vector();
			Vector msgs=new Vector();
      
			Map<String, Vector> map = new HashMap<String, Vector>();
			int noreq=Integer.parseInt( object.get("nor").toString());
			int nogrp=Integer.parseInt( object.get("nog").toString());
			int nofmsg=Integer.parseInt( object.get("nom").toString());

			for(int i=0;i<noreq;i++){
			 reqs.add(object.get("req"+i).toString());}
			map.put("requests",reqs);
			for(int i=0;i<nogrp;i++){
				 grpmsgs.add(object.get("grp"+i).toString());}

				map.put("groups",grpmsgs);
				for(int i=0;i<nofmsg;i++){
					 msgs.add(object.get("msg"+i).toString());}
					map.put("messages",msgs);
			return Response.ok(new Viewable("/jsp/notifications", map)).build();
		
	}
   
   @POST
	@Path("/msgpage")
	@Produces("text/html")
public Response opnMsgpage(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom){

	   Map <String,String> map = new HashMap<String,String>();
	   map.put("userto", userto);
	   map.put("userfrom", userfrom);
		return Response.ok(new Viewable("/jsp/sendmsg", map)).build();
}
   @POST
	@Path("/sendmsg")
	@Produces("text/html")

   
   public String sndMsg(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom,@FormParam("msgbody") String msgbody) throws ParseException{
	   String serviceUrl = "http://localhost:8888/rest/SendMsgService";
	   String urlParameters = "userto=" + userto + "&userfrom=" + userfrom+ "&msgbody=" + msgbody;
	   Connectionn Connection2=new Connectionn();

	   String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
	   				"application/x-www-form-urlencoded;charset=UTF-8");
	  
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK"))
				return "Message Sent	";
			else if (object.get("Status").equals("NO")){return "Message not sent ";}
	
		return "Failed";
}
   

   @POST
   @Path("/viewmsgs")
	@Produces("text/html")
	public Response showmsgs() throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/ViewmsgService";
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, null, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Vector msgs=new Vector();
			Map<String, Vector> map = new HashMap<String, Vector>();
			int nofgrps=Integer.parseInt( object.get("nom").toString());
			for(int i=0;i<nofgrps;i++){
			 msgs.add(object.get("message"+i).toString());}
			map.put("msgs",msgs);
				return Response.ok(new Viewable("/jsp/viewmessages", map)).build();
		
	}
   
   @POST
   @Path("/read")
	@Produces("text/html")
	public Response readmsg(@FormParam("userfrom") String userfrom) throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/ReadMsgService";
		String urlParameters = "userfrom=" + userfrom ;
		Connectionn Connection2=new Connectionn();

		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;

			Map<String, String> map = new HashMap<String, String>();
			map.put("message",object.get("msg").toString());
				return Response.ok(new Viewable("/jsp/readmsg", map)).build();
	
	}


   @POST
   @Path("/viewtimeline")
	@Produces("text/html")
	public Response viewtimeline(@FormParam("name") String name) throws ParseException{
		String serviceUrl = "http://localhost:8888/rest/ViewTimeLineService";
		String urlParameters = "name=" + name ;

		Connectionn Connection2=new Connectionn();
		String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
						"application/x-www-form-urlencoded;charset=UTF-8");
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			Vector posts=new Vector();
			Vector username=new Vector(); username.add(object.get("name").toString());
			Map<String, Vector> map = new HashMap<String, Vector>();
			int noposts=Integer.parseInt( object.get("noposts").toString());
			for(int i=0;i<noposts;i++){
			 posts.add(object.get("post"+i).toString());}
	
			map.put("name", username);
			map.put("post", posts);
				return Response.ok(new Viewable("/jsp/timeline",map)).build();

	}

 
}

