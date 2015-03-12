                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        package com.FCI.SWE.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private String name;
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

	public String getName() {
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
	public static List viewusers(String userfrom,String searchuser){
		List requests = new ArrayList();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery1 = new Query("requests");
		PreparedQuery pq1 = datastore.prepare(gaeQuery1);
		for (Entity entity1 : pq1.asIterable()) {
			
			if(entity1.getProperty("UserTo").toString().equals(userfrom) && entity1.getProperty("Accepted").toString().equals("ny"))
			{
				DatastoreService datastore2 = DatastoreServiceFactory.getDatastoreService();
				Query gaeQuery = new Query("users");
				PreparedQuery pq = datastore2.prepare(gaeQuery);
				for (Entity entity : pq.asIterable()) {
					if (entity.getProperty("name").toString().contains(searchuser)&& ! entity.getProperty("name").toString().equals(userfrom)) {
						String s=entity.getProperty("name").toString();
						requests.add(entity.getProperty("name").toString()+"  "+"<form action='/social/req' method='post'><input type='hidden' value='"+s+"' name='userto'><input type='hidden' value='"+userfrom+"' name='userfrom'><input type='submit' value='Add friend'></form><br>");
			}
		}
		
		
		
			}}
			
		
		return requests;
	}
}
