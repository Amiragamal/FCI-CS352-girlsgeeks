                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        package com.FCI.SWE.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class UserEntity {
	private static String name;
	private String email;
	private String password;

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;

	}

	public static String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}

	/**
	 * 
	 * This static method will form UserEntity class using json format contains
	 * user data
	 * 
	 * @param json
	 *            String in json format contains user data
	 * @return Constructed user entity
	 */
	public static UserEntity getUser(String json) {

		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			return new UserEntity(object.get("name").toString(), object.get(
					"email").toString(), object.get("password").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static UserEntity getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().equals(name)
					&& entity.getProperty("password").toString().equals(pass)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				return returnedUser;
			}
		}

		return null;
	}

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		Entity employee = new Entity("users", list.size() + 1);

		employee.setProperty("name", this.name);
		employee.setProperty("email", this.email);
		employee.setProperty("password", this.password);
		datastore.put(employee);

		return true;

	}
	/**
	 * This method will be used to save request object in datastore
	 * 
	 * @return ok if request is saved correctly or not
	 */
	
	public static String sendReq(String e1,String e2){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("requests");
		boolean found=true;
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		Query gaeQuery2 = new Query("users");
		PreparedQuery pq2 = datastore.prepare(gaeQuery2);
		for (Entity entity : pq2.asIterable()) {	
			if (entity.getProperty("name").equals(e2)) {found=true;break;}
			else{found=false;}
		}
			if(found==true){
		Entity employee = new Entity("requests", list.size() + 1);
		employee.setProperty("Userfrom", e1);
		employee.setProperty("UserTo", e2);
		employee.setProperty("Accepted", "ny");
		datastore.put(employee); 
		return "ok";}
			else 
				return "no";
	}
	
	/**
	 * This method will be used to retrieve requests from datastore
	 * 
	 * @return list of requests 
	 */
	public static List viewrequests(String uname){
		List requests = new ArrayList();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("requests");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("UserTo").toString().equals(uname) && entity.getProperty("Accepted").toString().equals("ny")) {
				String s=entity.getProperty("Userfrom").toString();
				requests.add(entity.getProperty("Userfrom").toString()+"  "+"wants to add u <form action='/social/accept' method='post'><input type='hidden' value='"+s+"' name='userfrom'><input type='submit' value='Accept friend'></form><form action='/social/ignore' method='post'><input type='hidden' value='"+s+"' name='userfrom'></input><input type='submit' value='Ignore friend'></input></form><br>");
			}
		}
		return requests;
	}
	/**
	 * This method will be used to ignore request and change accepted column in table requests in datastore
	 * 
	 * @return ok if user is ignored correctly or not
	 */
	public static String ignorereq(String ufrom){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("requests");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("Userfrom").toString().equals(ufrom) && entity.getProperty("Accepted").toString().equals("ny")) {
				entity.setProperty("Accepted","no");
				datastore.put(entity);
			}
		}
		return "ok";
	}
	/**
	 * This method will be used to accept request and change accepted column in table requests in datastore
	 * also it will add the friend to frineds table in datastore
	 * 
	 * @return ok if user is accepted correctly or not
	 */
	public static String acceptReq(String ufrom){
		  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  Query gaeQuery = new Query("requests");
		  PreparedQuery pq = datastore.prepare(gaeQuery);
          List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
          for (Entity entity : pq.asIterable()) {
  			if (entity.getProperty("Userfrom").toString().equals(ufrom)) {
          entity.setProperty("Accepted","yes");
          datastore.put(entity);
          Entity friend = new Entity("Friends", list.size() + 1);
			friend.setProperty("Username", entity.getProperty("UserTo").toString());
			friend.setProperty("Friendname", entity.getProperty("Userfrom").toString());
			datastore.put(friend); }
			 }

				return "ok";
				
			}
	/**
	 * This method will be used to retrieve friends from table friends in data store
	 * 
	 * @return a list of friends
	 */
	public static List getfriends(String ufrom){
		  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  Query gaeQuery = new Query("Friends");
		  List friends = new ArrayList();
		  PreparedQuery pq = datastore.prepare(gaeQuery);
        for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("Username").toString().equals(ufrom)) {
			
				String s= entity.getProperty("Friendname").toString();
				friends.add("<font size='6'> - "+s+"   Is Your Friend !! </font><br>");

			}
			else if (entity.getProperty("Friendname").toString().equals(ufrom)) {
			
				String s= entity.getProperty("Username").toString();
				friends.add("<font size='6'> - "+s+"   Is Your Friend !! </font><br>");

			}
			
        }

				return friends;
			}
	
	/**
	 * This method will be used to retrieve users from datastore
	 * 
	 * @return a list of users 
	 */
	public static Vector viewusers(String userfrom,String searchuser){
		Vector requests = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().contains(searchuser)&& ! entity.getProperty("name").toString().equals(userfrom)) {
				String s=entity.getProperty("name").toString();
				requests.add(entity.getProperty("name").toString());
			}
		}
		return requests;
	}

	public static String sendgrpmsg(String usersto, String convname,String msgbody,String userfrom) {
		
		String[] users =usersto.split(",");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("convmembers");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		Entity members = new Entity("convmembers", list.size() + 1);
		members.setProperty("membername", userfrom);
		members.setProperty("convname", convname);
        datastore.put(members);
        
       
		for(int i=0;i<users.length;i++){

			DatastoreService datastore2 = DatastoreServiceFactory.getDatastoreService();
			Query gaeQuery2 = new Query("convmembers");
			PreparedQuery pq2 = datastore2.prepare(gaeQuery2);
			List<Entity> list2 = pq2.asList(FetchOptions.Builder.withDefaults());
			Entity members2 =new Entity("convmembers", list2.size() + 1);
			members2.setProperty("membername", users[i]);
            members2.setProperty("convname", convname);
			datastore2.put(members2);
          }

		DatastoreService datastore3 = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery3 = new Query("convmessages");
		PreparedQuery pq3 = datastore3.prepare(gaeQuery3);
		List<Entity> list3 = pq3.asList(FetchOptions.Builder.withDefaults());
		Entity members3 = new Entity("convmessages", list3.size() + 1);
		members3.setProperty("messagecontent", msgbody);
		members3.setProperty("seen","ny");
		members3.setProperty("convname", convname);
		members3.setProperty("userfrom", userfrom);

        datastore3.put(members3);
		
				return "ok";	
			}

	public static List showgrp(String currentuser) {
		
		Vector grp = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("convmembers");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("membername").toString().contains(currentuser) ) {
				grp.add(entity.getProperty("convname").toString());
			}
		}
		return grp;
	
	}

	public static Map showconv(String convname) {
		
        Vector msgs =new Vector();
        Vector users =new Vector();
        Vector userfrom = new Vector();

		Map<String,Vector> conv = new HashMap<String,Vector>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("convmembers");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("convname").toString().contains(convname) ) {
				users.add(entity.getProperty("membername").toString());
			}
		}
		
		DatastoreService datastore2 = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery2 = new Query("messages");
		PreparedQuery pq2 = datastore.prepare(gaeQuery2);
		for (Entity entity2 : pq2.asIterable()) {
			if (entity2.getProperty("convname").toString().contains(convname) ) {
				msgs.add(entity2.getProperty("messagecontent").toString());
                userfrom.add(entity2.getProperty("userfrom").toString());
			}
		}
		conv.put("msgs", msgs);
		conv.put("members", users);
		conv.put("sender", userfrom);
		return conv;
	}

	public static String replygrp(String msgbody, String convname,String userfrom) {
		DatastoreService datastore3 = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery3 = new Query("convmessages");
		PreparedQuery pq3 = datastore3.prepare(gaeQuery3);
		List<Entity> list3 = pq3.asList(FetchOptions.Builder.withDefaults());
		Entity members3 = new Entity("convmessages", list3.size() + 1);
		members3.setProperty("messagecontent", msgbody);
		members3.setProperty("seen", "ny");
		members3.setProperty("convname", convname);
		members3.setProperty("userfrom", userfrom);
        datastore3.put(members3);

				return "ok";	
	}
	public static Map viewnotificications(String currentuser){
		

        Vector reqs =new Vector();
        Vector msgs =new Vector();
        Vector grpmsgs = new Vector();
        String S;

		Map<String,Vector> notif = new HashMap<String,Vector>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("requests");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("UserTo").toString().equals(currentuser) && entity.getProperty("Accepted").toString().equals("ny")) {
				reqs.add(entity.getProperty("Userfrom").toString());
			}
		}
		DatastoreService datastore2 = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery2 = new Query("convmembers");
		PreparedQuery pq2 = datastore2.prepare(gaeQuery2);
		for (Entity entity2 : pq2.asIterable()) {
			if (entity2.getProperty("membername").toString().equals(currentuser) ) {
				S=entity2.getProperty("convname").toString();
				DatastoreService datastore3 = DatastoreServiceFactory.getDatastoreService();
				Query gaeQuery3 = new Query("convmessages");
				PreparedQuery pq3 = datastore3.prepare(gaeQuery3);
				for (Entity entity3 : pq3.asIterable()) {
					if (entity3.getProperty("convname").toString().equals(S) && entity3.getProperty("seen").toString().equals("ny")) {
						grpmsgs.add(entity3.getProperty("convname").toString());
					}

				
			}
		}
		
		}
		
		DatastoreService datastore3 = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery3 = new Query("messages");
		PreparedQuery pq3 = datastore3.prepare(gaeQuery3);
		for (Entity entity3 : pq3.asIterable()) {
			if (entity3.getProperty("UserTo").toString().equals(currentuser) && entity3.getProperty("read").toString().equals("not")) {
				msgs.add(entity3.getProperty("Userfrom").toString());
				
			}
		}
		notif.put("reqs", reqs);
		notif.put("grpmsgs", grpmsgs);
		notif.put("msgs",msgs);
		return notif;
	}

	public static String sendMsg(String userfrom,String userto,String msgbody){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
			
		Entity employee = new Entity("messages", list.size() + 1);
		employee.setProperty("Userfrom", userfrom);
		employee.setProperty("UserTo", userto);
		employee.setProperty("msgcontent", msgbody);
		employee.setProperty("read", "not");
		datastore.put(employee); 
		   return "ok";
		   
	}
	
	public static List viewmsgs(String uname){
		Vector messages = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("messages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("UserTo").toString().equals(uname) && entity.getProperty("read").toString().equals("not")) {
				String s=entity.getProperty("Userfrom").toString();
				messages.add(entity.getProperty("Userfrom").toString());
				
			}
		}
		return messages;
	}
	
	public static String readmsg(String ufrom){
		  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		  Query gaeQuery = new Query("messages");
		  String S="";
		  PreparedQuery pq = datastore.prepare(gaeQuery);
        List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
        for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("Userfrom").toString().equals(ufrom)) {
				S= entity.getProperty("msgcontent").toString();
        entity.setProperty("read","yes");
        datastore.put(entity);}
			
       
			 }
				return S;
				
			}
	
		

		


}
