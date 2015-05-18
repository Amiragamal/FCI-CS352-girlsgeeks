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
import java.util.Vector;

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

import com.FCI.SWE.Models.PageEntity;
import com.FCI.SWE.Models.PostEntity;
import com.FCI.SWE.Models.UserEntity;
import com.google.appengine.labs.repackaged.org.json.JSONArray;

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
		List hashtags=PostEntity.trendhashtag();
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
			 for(int i=0;i<hashtags.size();i++){
			      object.put("hashtag"+i, hashtags.get(i));}
			      object.put("noh", hashtags.size());
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
	public String sendReqService(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom) {
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

	@POST
	@Path("/SendgroupService")
	public String sendgroupService(@FormParam("to") String usersto,@FormParam("convname") String convname,@FormParam("msgbody") String msgbody) {
		JSONObject object = new JSONObject();
		String userfrom=UserEntity.getName();
		String accept=UserEntity.sendgrpmsg(usersto,convname,msgbody, userfrom);
       if(accept.equals("ok")){object.put("Status", "OK");}
       else{
       object.put("Status","failed");}
		return object.toString();}

	@POST
	@Path("/ShowgroupService")
	public String showgroup() {
		JSONObject object = new JSONObject();
        String currentuser=UserEntity.getName();
		List grpmsgs = UserEntity.showgrp(currentuser);
       if(grpmsgs.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<grpmsgs.size();i++){
       object.put("grps"+i, grpmsgs.get(i));}
	}
       object.put("nog", grpmsgs.size());
		return object.toString();}
	@POST
	@Path("/ShowconvService")
	public String showconvService(@FormParam("convname") String convname) {
		JSONObject object = new JSONObject();
        String currentuser=UserEntity.getName();
		Map <String,Vector> conv = new HashMap<String,Vector>();
		conv = UserEntity.showconv(convname);
		Vector msgs =new Vector();
		msgs= conv.get("msgs");
		    	   
		object.put("nom", msgs.size());
		Vector members =new Vector();
		members= conv.get("members");
		object.put("nomem", members.size());
		Vector sender =new Vector();
		sender= conv.get("sender");
		object.put("nos", sender.size());
       if(conv.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<msgs.size();i++){
       object.put("msg"+i, msgs.get(i));}
    	   
    	   for(int i=0;i<members.size();i++){
    	       object.put("member"+i, members.get(i));}
    	   for(int i=0;i<sender.size();i++){
    	       object.put("sender"+i, sender.get(i));}
	}
       
		return object.toString();}
	@POST
	@Path("/ReplyService")
	public String replyService(@FormParam("msgbody") String msgbody,@FormParam("convname") String convname) {
		JSONObject object = new JSONObject();
        String currentuser=UserEntity.getName();
	    String msg=UserEntity.replygrp(msgbody,convname,currentuser);
	    if(msg.equals("ok")){object.put("Status", "OK");}
	       else{
	       object.put("Status","failed");}
			return object.toString();
			}

	@POST
	@Path("/ShownotifService")
	public String shownotifService() {
		JSONObject object = new JSONObject();
        String currentuser=UserEntity.getName();
		Map <String,Vector> notif = new HashMap<String,Vector>();
		notif = UserEntity.viewnotificications(currentuser);
		Vector reqs =new Vector();
		reqs= notif.get("reqs");
		object.put("nor", reqs.size());
		Vector grpmsg =new Vector();
		grpmsg= notif.get("grpmsgs");
		object.put("nog", grpmsg.size());
		Vector msgs =new Vector();
		msgs= notif.get("msgs");
		object.put("nom", msgs.size());

       if(notif.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<reqs.size();i++){
       object.put("req"+i, reqs.get(i));}
    	   
    	   for(int i=0;i<grpmsg.size();i++){
    	       object.put("grp"+i, grpmsg.get(i));}
    	   for(int i=0;i<msgs.size();i++){
    	       object.put("msg"+i, msgs.get(i));}
	}
	return object.toString();

	}
	
	@POST
	@Path("/SendMsgService")
	public String sendMsgService(@FormParam("userto") String userto,@FormParam("userfrom") String userfrom,@FormParam("msgbody") String msgbody) {
		JSONObject object = new JSONObject();
		String user = UserEntity.sendMsg(userfrom, userto,msgbody);
       if(user.equals("ok"))
		object.put("Status", "OK");
       else 
    	   object.put("Status","NO");
       
		return object.toString();}
	
	@POST
	@Path("/ViewmsgService")
	public String viewmsgs() {
		JSONObject object = new JSONObject();

	    String currentuser=UserEntity.getName();
		List message = UserEntity.viewmsgs(currentuser);
       if(message.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<message.size();i++){
       object.put("message"+i, message.get(i));}
	}
       object.put("nom", message.size());
		return object.toString();}
	
	
	
	@POST
	@Path("/ReadMsgService")
	public String readmsg(@FormParam("userfrom") String userfrom) {
		JSONObject object = new JSONObject();
		String msg=UserEntity.readmsg(userfrom);
       if(msg.equals(null)){object.put("Status", "Failed");}
       else{
       object.put("msg",msg);}
		return object.toString();}
	
	@POST
	@Path("/PageCreationService")
	public String pagecreationservice(@FormParam("pname") String pname,
			@FormParam("ptype") String ptype, @FormParam("pcategory") String pcategory) {
		String pcreator=UserEntity.getName();
		String user =PageEntity.savePage(pname,ptype,pcategory,pcreator);
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}
	
	@POST
	@Path("/ViewePagesService")
	public String viewePagesService() {
		JSONObject object = new JSONObject();

	    String currentuser=UserEntity.getName();
		List page = PageEntity.viewpage(currentuser);
       if(page.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<page.size();i++){
       object.put("page"+i, page.get(i));}
	}
       object.put("nop", page.size());
		return object.toString();}
	
	
	@POST
	@Path("/VieweMyPagesService")
	public String vieweMyPagesService() {
		JSONObject object = new JSONObject();

	    String currentuser=UserEntity.getName();
		List page = PageEntity.viewmypages(currentuser);
       if(page.equals(null)){object.put("Status", "Failed");}
       else{
    	   for(int i=0;i<page.size();i++){
       object.put("page"+i, page.get(i));}
	}
       object.put("nop", page.size());
		return object.toString();}
	
	
	@POST
	@Path("/HandlePageService")
	public String handlePageService(@FormParam("pagename") String pagename) {
		JSONObject object = new JSONObject();
		List posts = PageEntity.handlemypage(pagename);
        object.put("name", posts.get(0));
        object.put("likes", posts.get(1));
   	   for(int i=2;i<posts.size();i++){
      object.put("post"+i, posts.get(i));}
      object.put("noposts", posts.size());
		object.put("Status","OK");
		return object.toString();}
	
	
	@POST
	@Path("/OpenPageService")
	public String openPageService(@FormParam("pagename") String pagename) {
		JSONObject object = new JSONObject();
		String user=UserEntity.getName();
		List posts = PageEntity.openpage(pagename, user);
        object.put("name", posts.get(0));
   	   for(int i=1;i<posts.size();i++){
      object.put("post"+i, posts.get(i));}
      object.put("noposts", posts.size());
		object.put("Status","OK");
		return object.toString();}
	@POST
	@Path("/SavePostService")
	public String savePostService(@FormParam("pname") String pname,@FormParam("pagepost") String pagepost,@FormParam("privacy")String privacy) {
		String pcreator=UserEntity.getName();
		String user =PostEntity.savepost(pname,pagepost,pcreator,privacy);
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}
	@POST
	@Path("/LikePageService")
	public String likePageService() {
		String currentuser=UserEntity.getName();
		String Pagename=UserEntity.getpname();
		String like =PageEntity.likepage(currentuser,Pagename);
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	@POST
	@Path("/HashtagService")
	public String 	hashtagService(@FormParam("name") String name) {
		JSONObject object = new JSONObject();
		List posts = PostEntity.viewhashtag(name);
        object.put("name", posts.get(0));
        object.put("nop", posts.get(1));
   	  for(int i=2;i<posts.size();i++){
      object.put("post"+i, posts.get(i));}
      object.put("noposts", posts.size());
		object.put("Status","OK");
		return object.toString();}

	@POST
	@Path("/ViewTimeLineService")
	public String viewTimeLineService(@FormParam("name") String name) {
		JSONObject object = new JSONObject();
		String uname=UserEntity.getName();
		List posts = UserEntity.vietimeline(name,uname);

      	   for(int i=0;i<posts.size();i++){
       object.put("post"+i, posts.get(i));}
       object.put("noposts", posts.size());
		object.put("name",name);
		return object.toString();}
	
	@POST
	@Path("/SaveUserPostService")
	public String saveUserPostService(@FormParam("pagepost") String pagepost,@FormParam("privacy") String privacy,@FormParam("user") String user,@FormParam("feeling") String feeling,@FormParam("custom") String custom) {
		String pcreator=UserEntity.getName();
		String post =PostEntity.saveuserpost(pcreator,pagepost,privacy,user,feeling,custom);
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}
	
	
	@POST
	@Path("/LikeUserPostService")
	public String likeUserPostService(@FormParam("post") String post,@FormParam("timeline") String timeline) {
		String currentuser=UserEntity.getName();
		String like =PostEntity.likeuserpost(post,timeline);
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}
	@POST
	@Path("/LikePagePostService")
	public String likePagePostService(@FormParam("post") String post,@FormParam("timeline") String timeline) {
		String currentuser=UserEntity.getName();
		String like =PageEntity.likepagepost(post,timeline);
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}

	@POST
	@Path("/ShareUserPostService")
	public String shareUserPostService(@FormParam("post") String post,@FormParam("creator") String creator,@FormParam("hashtag") String hashtag,@FormParam("feeling") String feeling) {
		String currentuser=UserEntity.getName();
		String post1 =PostEntity.shareuserpost(post,creator,currentuser,hashtag,feeling);
		JSONObject object = new JSONObject();
		object.put("Status", "OK");
		return object.toString();
	}




}
