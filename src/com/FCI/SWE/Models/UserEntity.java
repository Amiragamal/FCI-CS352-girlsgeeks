
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
	private static String pagename;
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
    public static void setpname(String pname){pagename=pname;}
    
    public static String getpname(){return pagename;}
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
		Query gaeQuery2 = new Query("convmessages");
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
			if (entity.getProperty("UserTo").toString().equals(uname) ) {
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
	
	public static String savePage(String pname,String ptype,String pcategory,String pcreator) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
       int noflikes=0;
       String likers="";
		Entity pages = new Entity("pages", list.size() + 1);

		pages.setProperty("pagename",pname );
		pages.setProperty("pagetype",ptype );
		pages.setProperty("pagecategory", pcategory);
		pages.setProperty("pagecreator", pcreator);
		pages.setProperty("numberoflikes",noflikes);
		pages.setProperty("likers",likers);
		datastore.put(pages);

		return "OK";

	}

	public static List viewpage(String uname){
		Vector pages = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("pagecreator").toString().equals(uname) ) {
				pages.add(entity.getProperty("pagename").toString());
			}
			else {
				pages.add(entity.getProperty("pagename").toString());
			}
		}
		return pages;
	}

	public static List viewmypages(String currentuser) {
		Vector pages = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("pagecreator").toString().equals(currentuser) ) {
				pages.add(entity.getProperty("pagename").toString());
			}
		}
		return pages;
	}
	
	public static List handlemypage(String pname) {
		Vector posts = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			String str="";
			String hname="";

			if (entity.getProperty("pagename").toString().equals(pname) ) {
				posts.add(entity.getProperty("pagename").toString());
				posts.add(entity.getProperty("numberoflikes"));
				Query gaeQuery2 = new Query("pageposts");
				PreparedQuery pq2 = datastore.prepare(gaeQuery2);
				for (Entity entity2 : pq2.asIterable()) {
					if (entity2.getProperty("pagename").toString().equals(pname) ) {
						  str = entity2.getProperty("post").toString();
						  hname=str.substring(str.indexOf("#")+1);
							 str=str.replaceAll("#+([a-zA-Z0-9_]+)", "<a href='/social/hashtag/"+hname+"'><font color='white'>#"+hname +"</font></a>");
							 str+="<br> Number of likes : "+entity2.getProperty("numberoflikes").toString();
							 str+="<br/> Number of seen : "+entity2.getProperty("numberofviews").toString();
								 str+="<br/> <form method='post' action='/social/likepagepost'>";
								 str+="<input type='hidden' value='"+entity2.getProperty("post").toString()+"' name='post'></input>";
								 str+="<input type='hidden' value='"+entity2.getProperty("pagename").toString()+"' name='timeline'></input>";
								 str+="<input  type='submit' value='Like Post' ></input></form>" ;
								  str+="<form method='post' action='/social/sharepagepost'>";
								  str+="<input type='hidden' value='none' name='feeling'></input>";
								  str+="<input type='hidden' value='"+entity2.getProperty("post").toString()+"' name='post'></input>";
									 str+="<input type='hidden' value='"+entity2.getProperty("postcreator").toString()+"' name='creator'></input>";
									 str+="<input type='hidden' value='"+entity2.getProperty("hashtag").toString()+"' name='hashtag'></input>";
								   str+="<input  type='submit' value='Share Post' ></input></form>";
								 posts.add(str);
						 }
						
					}
				}
			}
		return posts ; 
		}
	
	
	public static List openpage(String pname,String user) {
		Vector posts = new Vector();
		String str="";
		String hname="";
		int nofviews=0;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			 if (entity.getProperty("pagename").toString().equals(pname) && !(entity.getProperty("likers").toString().contains(user)) && !(entity.getProperty("pagecreator").toString().equals(user))) {
				posts.add(entity.getProperty("pagename").toString()+"<form method='post' action='/social/likepage'><input  type='submit' value='Like Page' style='width:100px; height:50px;'></form> ");
				}
			else if (entity.getProperty("pagecreator").toString().equals(user)){posts.add(entity.getProperty("pagename").toString()+" ((Owner))");}
				else  {posts.add(entity.getProperty("pagename").toString()+" ((Liked))");}}
				Query gaeQuery2 = new Query("pageposts");
				PreparedQuery pq2 = datastore.prepare(gaeQuery2);
				for (Entity entity2 : pq2.asIterable()) {
					if (entity2.getProperty("pagename").toString().equals(pname) ) {
						nofviews=Integer.parseInt(entity2.getProperty("numberofviews").toString())+1;
						entity2.setProperty("numberofviews", nofviews);
						datastore.put(entity2);
						if(entity2.getProperty("privacy").toString().equals("public")){
						 str = entity2.getProperty("post").toString();
						  hname=str.substring(str.indexOf("#")+1);
							 str=str.replaceAll("#+([a-zA-Z0-9_]+)", "<a href='/social/hashtag/"+hname+"'><font color='white'>#"+hname +"</font></a>");
							 str+="<br> Number of likes : "+entity2.getProperty("numberoflikes").toString();
							 str+="<br/> <form method='post' action='/social/likepagepost'>";
							str+="<input type='hidden' value='"+entity2.getProperty("post").toString()+"' name='post'></input>";
							 str+="<input type='hidden' value='"+entity2.getProperty("pagename").toString()+"' name='timeline'></input>";
							 str+="<input  type='submit' value='Like Post' ></input></form>" ;
							  str+="<form method='post' action='/social/shareuserpost'>";
							  str+="<input type='hidden' value='none' name='feeling'></input>";
							  str+="<input type='hidden' value='"+entity2.getProperty("post").toString()+"' name='post'></input>";
								 str+="<input type='hidden' value='"+entity2.getProperty("postcreator").toString()+"' name='creator'></input>";
								 str+="<input type='hidden' value='"+entity2.getProperty("hashtag").toString()+"' name='hashtag'></input>";
							   str+="<input  type='submit' value='Share Post' ></input></form>";
							 posts.add(str);
					}
					else if (entity2.getProperty("privacy").toString().equals("private")){
						Query gaeQuery3 = new Query("pages");
						PreparedQuery pq3 = datastore.prepare(gaeQuery3);
						for (Entity entity3 : pq3.asIterable()) {
							 if (entity3.getProperty("pagename").toString().equals(pname)) {
								 if(entity3.getProperty("likers").toString().contains(user)||entity3.getProperty("pagecreator").toString().equals(user)){
									 str = entity2.getProperty("post").toString();
									  hname=str.substring(str.indexOf("#")+1);
										 str=str.replaceAll("#+([a-zA-Z0-9_]+)", "<a href='/social/hashtag/"+hname+"'><font color='white'>#"+hname +"</font></a>");
										 str+="<br> Number of likes : "+entity2.getProperty("numberoflikes").toString();
										 str+="<br/> <form method='post' action='/social/likepagepost'>";
										str+="<input type='hidden' value='"+entity2.getProperty("post").toString()+"' name='post'></input>";
										 str+="<input type='hidden' value='"+entity2.getProperty("pagename").toString()+"' name='timeline'></input>";
										 str+="<input  type='submit' value='Like Post' ></input></form>" ;
										  str+="<form method='post' action='/social/shareuserpost'>";
										  str+="<input type='hidden' value='"+entity2.getProperty("post").toString()+"' name='post'></input>";
											 str+="<input type='hidden' value='"+entity2.getProperty("postcreator").toString()+"' name='creator'></input>";
											 str+="<input type='hidden' value='"+entity2.getProperty("hashtag").toString()+"' name='hashtag'></input>";
										   str+="<input  type='submit' value='Share Post' ></input></form>";
										 posts.add(str);
								 }
								 }
							 }
								
						
						
					}
						}
				}
                setpname(pname);
				return posts ; 
			
		}
	
	public static String savepost(String pname, String pagepost,String currentuser,String privacy) {
		String hname="";
		int noflikes=0;
		if(privacy.equals("null")){privacy="public";}

		if(pagepost.contains("#")){
			hname=pagepost.substring(pagepost.indexOf("#")+1);}
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pageposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
       int nofviews=0;
		Entity pages = new Entity("pageposts", list.size() + 1);

		pages.setProperty("postcreator",currentuser);
		pages.setProperty("privacy",privacy);
		pages.setProperty("pagename",pname );
		pages.setProperty("post",pagepost );
		pages.setProperty("numberofviews", nofviews);
		pages.setProperty("hashtag", hname);
		pages.setProperty("numberoflikes", noflikes);
		datastore.put(pages);
		if(pagepost.contains("#")){
		 int nofposts=1;
         Query gaeQuery2 = new Query("hashtags");		
		 PreparedQuery pq2 = datastore.prepare(gaeQuery2);
		 if(pq2.countEntities()==0){
			 Query gaeQuery3 = new Query("hashtags");
				PreparedQuery pq3 = datastore.prepare(gaeQuery3);
		        List<Entity> list2 = pq3.asList(FetchOptions.Builder.withDefaults());
		
		        Entity hashtags = new Entity("hashtags", list2.size() + 1);
		        hashtags.setProperty("hashtagname",hname );
		        hashtags.setProperty("numberofposts", nofposts);
		         datastore.put(hashtags);
							}
			 
			 
		 
		 else{
			 
				for (Entity entity : pq2.asIterable()) {
			 if (entity.getProperty("hashtagname").toString().equals(hname) ) {
				 nofposts=Integer.parseInt(entity.getProperty("numberofposts").toString())+1;
				entity.setProperty("numberofposts", nofposts);datastore.put(entity);break;}
				else{
					 Query gaeQuery3 = new Query("hashtags");
	PreparedQuery pq3 = datastore.prepare(gaeQuery3);
    List<Entity> list2 = pq3.asList(FetchOptions.Builder.withDefaults());

    Entity hashtags = new Entity("hashtags", list2.size() + 1);
    hashtags.setProperty("hashtagname",hname );
    hashtags.setProperty("numberofposts", nofposts);
     datastore.put(hashtags);
				}
		}
		 }}

		 return "OK";

	}

	public static String likepage(String currentuser, String pagename2) {
		int noflikes=0;
		String likers="";
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pages");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			 if (entity.getProperty("pagename").toString().equals(pagename2) ) {
				likers+=entity.getProperty("likers").toString();
				 likers+=","+currentuser;
			    noflikes=Integer.parseInt(entity.getProperty("numberoflikes").toString())+1;
			    entity.setProperty("numberoflikes",noflikes);
				entity.setProperty("likers",likers);
				datastore.put(entity);
				}}
		

		return "OK";

	
	}
		
	public static List viewhashtag(String name) {
		Vector posts = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("hashtags");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("hashtagname").toString().equals(name) ) {
				posts.add(entity.getProperty("hashtagname").toString());
				posts.add(entity.getProperty("numberofposts").toString());
				Query gaeQuery2 = new Query("pageposts");
				PreparedQuery pq2 = datastore.prepare(gaeQuery2);
				for (Entity entity2 : pq2.asIterable()) {
					if (entity2.getProperty("hashtag").toString().equals(name) ) {
						  posts.add( entity2.getProperty("post").toString());
						 }
					}
				Query gaeQuery3 = new Query("userposts");
				PreparedQuery pq3 = datastore.prepare(gaeQuery3);
				for (Entity entity3 : pq3.asIterable()) {
					
					if (entity3.getProperty("hashtag").toString().equals(name) ) {
						if(entity3.getProperty("privacy").equals("public")||entity3.getProperty("postcreator").toString().equals(getName())){
							  posts.add( entity3.getProperty("post").toString());
							 }
							else if(entity3.getProperty("privacy").equals("private")){
								 DatastoreService datastore4 = DatastoreServiceFactory.getDatastoreService();
								  Query gaeQuery4 = new Query("Friends");
								  PreparedQuery pq4 = datastore4.prepare(gaeQuery4);
						        for (Entity entity4 : pq4.asIterable()) {
									if ((entity4.getProperty("Username").toString().equals(getName()) && entity4.getProperty("Friendname").toString().equals(entity3.getProperty("postcreator").toString()))||(entity4.getProperty("Username").toString().equals(entity3.getProperty("postcreator").toString())&& entity4.getProperty("Friendname").toString().equals(getName()))) {
										 System.out.println("they're friends");
										  posts.add( entity3.getProperty("post").toString());
									}
						 }
					}
				}
				}}}
		return posts ; 
		}

	public static List trendhashtag() {
		Vector hashtags = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("hashtags");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) { 
			hashtags.add(entity.getProperty("hashtagname").toString());
				
				
			}
		return hashtags ; 
		}

	public static List vietimeline(String name,String currentuser) {
		Vector posts = new Vector();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("userposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
			String str="";
			String hname="";
				for (Entity entity : pq.asIterable()) {
					if (entity.getProperty("ontimeline").toString().equals(name) ) {
						if(entity.getProperty("privacy").equals("public")||entity.getProperty("postcreator").toString().equals(currentuser)){
						  str = entity.getProperty("post").toString();
						  hname=str.substring(str.indexOf("#")+1);
							 str=str.replaceAll("#+([a-zA-Z0-9_]+)", "<a href='/social/hashtag/"+hname+"'><font color='white'>#"+hname +"</font></a>");
							 str+="<br> Number of likes : "+entity.getProperty("numberoflikes").toString();
							if(!(entity.getProperty("feeling").toString().equals("none"))){ str+="<br/> Feeling: "+entity.getProperty("feeling").toString();}
							 str+="<br/> <form method='post' action='/social/likeuserpost'>";
							 str+="<input type='hidden' value='"+entity.getProperty("post").toString()+"' name='post'></input>";
							 str+="<input type='hidden' value='"+entity.getProperty("ontimeline").toString()+"' name='timeline'></input>";
							 str+="<input  type='submit' value='Like Post' ></input></form>" ;
							  str+="<form method='post' action='/social/shareuserpost'>";

							  str+="<input type='hidden' value='"+entity.getProperty("feeling").toString()+"' name='feeling'></input>";
							  str+="<input type='hidden' value='"+entity.getProperty("post").toString()+"' name='post'></input>";
								 str+="<input type='hidden' value='"+entity.getProperty("postcreator").toString()+"' name='creator'></input>";
								 str+="<input type='hidden' value='"+entity.getProperty("hashtag").toString()+"' name='hashtag'></input>";
							   str+="<input  type='submit' value='Share Post' ></input></form>";
							 posts.add(str);
						 }
						else if(entity.getProperty("privacy").equals("private")){
							 DatastoreService datastore2 = DatastoreServiceFactory.getDatastoreService();
							  Query gaeQuery2 = new Query("Friends");
							  PreparedQuery pq2 = datastore2.prepare(gaeQuery2);
					        for (Entity entity2 : pq2.asIterable()) {
								if ((entity2.getProperty("Username").toString().equals(currentuser) && entity2.getProperty("Friendname").toString().equals(name))||(entity2.getProperty("Username").toString().equals(name)&& entity2.getProperty("Friendname").toString().equals(currentuser))) {
									str = entity.getProperty("post").toString();
									  hname=str.substring(str.indexOf("#")+1);
										 str=str.replaceAll("#+([a-zA-Z0-9_]+)", "<a href='/social/hashtag/"+hname+"'><font color='white'>#"+hname +"</font></a>");
										 str+="<br> Number of likes : "+entity.getProperty("numberoflikes").toString();
										 if(!(entity.getProperty("feeling").toString().equals("none"))){ str+="<br/> Feeling: "+entity.getProperty("feeling").toString();}
											else{str+="";}
										 str+="<br/> <form method='post' action='/social/likeuserpost'>";
										 str+="<input type='hidden' value='"+entity.getProperty("post").toString()+"' name='post'></input>";
										 str+="<input type='hidden' value='"+entity.getProperty("ontimeline").toString()+"' name='timeline'></input>";
										 str+="<input  type='submit' value='Like Post' ></input></form>" ;
										  str+="<form method='post' action='/social/shareuserpost'>";
										  str+="<input type='hidden' value='"+entity.getProperty("feeling").toString()+"' name='feeling'></input>";
										  str+="<input type='hidden' value='"+entity.getProperty("post").toString()+"' name='post'></input>";
											 str+="<input type='hidden' value='"+entity.getProperty("postcreator").toString()+"' name='creator'></input>";
											 str+="<input type='hidden' value='"+entity.getProperty("hashtag").toString()+"' name='hashtag'></input>";
										   str+="<input  type='submit' value='Share Post' ></input></form>";
										 posts.add(str);
								}
								
					        }
							
							
						}
						
						else if (entity.getProperty("privacy").toString().equals("custom")){
							if(entity.getProperty("custom").toString().contains(currentuser)||entity.getProperty("ontimeline").toString().equals(currentuser)){
								str = entity.getProperty("post").toString();
								  hname=str.substring(str.indexOf("#")+1);
									 str=str.replaceAll("#+([a-zA-Z0-9_]+)", "<a href='/social/hashtag/"+hname+"'><font color='white'>#"+hname +"</font></a>");
									 str+="<br> Number of likes : "+entity.getProperty("numberoflikes").toString();
									 if(!(entity.getProperty("feeling").toString().equals("none"))){ str+="<br/> Feeling: "+entity.getProperty("feeling").toString();}
										else{str+="";}
									 str+="<br/> <form method='post' action='/social/likeuserpost'>";
									 str+="<input type='hidden' value='"+entity.getProperty("post").toString()+"' name='post'></input>";
									 str+="<input type='hidden' value='"+entity.getProperty("ontimeline").toString()+"' name='timeline'></input>";
									 str+="<input  type='submit' value='Like Post' ></input></form>" ;
									  str+="<form method='post' action='/social/shareuserpost'>";
									  str+="<input type='hidden' value='"+entity.getProperty("feeling").toString()+"' name='feeling'></input>";

									  str+="<input type='hidden' value='"+entity.getProperty("post").toString()+"' name='post'></input>";
										 str+="<input type='hidden' value='"+entity.getProperty("postcreator").toString()+"' name='creator'></input>";
										 str+="<input type='hidden' value='"+entity.getProperty("hashtag").toString()+"' name='hashtag'></input>";
									   str+="<input  type='submit' value='Share Post' ></input></form>";
									 posts.add(str);
								
								
							}
							
						}
					
					}
						
						
					}

			
		return posts ;  
	}

	public static String saveuserpost(String postcreator, String pagepost,String privacy,String userto,String feeling,String custom) {
		String hname="";
		int noflikes=0;
		if(privacy.equals("null")){privacy="public";}
		System.out.println(privacy);
		if(pagepost.contains("#")){
		hname=pagepost.substring(pagepost.indexOf("#")+1);}
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("userposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
       int nofviews=0;
		Entity pages = new Entity("userposts", list.size() + 1);
       
		pages.setProperty("postcreator",postcreator);
		pages.setProperty("post",pagepost );
		pages.setProperty("feeling",feeling);
		pages.setProperty("ontimeline", userto);
		pages.setProperty("hashtag", hname);
		pages.setProperty("privacy", privacy);
		pages.setProperty("custom",custom);
		pages.setProperty("numberoflikes", noflikes);
		datastore.put(pages);
		if(pagepost.contains("#")){
		 int nofposts=1;
         Query gaeQuery2 = new Query("hashtags");		
		 PreparedQuery pq2 = datastore.prepare(gaeQuery2);
		 if(pq2.countEntities()==0){
			 Query gaeQuery3 = new Query("hashtags");
				PreparedQuery pq3 = datastore.prepare(gaeQuery3);
		        List<Entity> list2 = pq3.asList(FetchOptions.Builder.withDefaults());
		
		        Entity hashtags = new Entity("hashtags", list2.size() + 1);
		        hashtags.setProperty("hashtagname",hname );
		        hashtags.setProperty("numberofposts", nofposts);
		         datastore.put(hashtags);
							}
			 
			 
		 
		 else{
			 
				for (Entity entity : pq2.asIterable()) {
			 if (entity.getProperty("hashtagname").toString().equals(hname) ) {
				 nofposts=Integer.parseInt(entity.getProperty("numberofposts").toString())+1;
				entity.setProperty("numberofposts", nofposts);datastore.put(entity);break;}
				else{
					 Query gaeQuery3 = new Query("hashtags");
	PreparedQuery pq3 = datastore.prepare(gaeQuery3);
    List<Entity> list2 = pq3.asList(FetchOptions.Builder.withDefaults());

    Entity hashtags = new Entity("hashtags", list2.size() + 1);
    hashtags.setProperty("hashtagname",hname );
    hashtags.setProperty("numberofposts", nofposts);
     datastore.put(hashtags);
				}
		}
		 }}

		 return "OK";

	}
	
	public static String likeuserpost(String post,String timeline) {
		int noflikes=0;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("userposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			//System.out.println(post);
			 if (entity.getProperty("post").toString().equals(post) &&entity.getProperty("ontimeline").toString().equals(timeline)) {
			    noflikes=Integer.parseInt(entity.getProperty("numberoflikes").toString())+1;
			    entity.setProperty("numberoflikes",noflikes);
				datastore.put(entity);
				}}

		return "OK";

	
	}

	public static String likepagepost(String post,String timeline) {
		int noflikes=0;
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("pageposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			//System.out.println(post);
			 if (entity.getProperty("post").toString().equals(post) &&entity.getProperty("pagename").toString().equals(timeline)) {
			    noflikes=Integer.parseInt(entity.getProperty("numberoflikes").toString())+1;
			    entity.setProperty("numberoflikes",noflikes);
				datastore.put(entity);
				}}

		return "OK";

	
	}
	public static String shareuserpost(String post, String creator,String currentuser,String hashtag,String feeling) {
		int noflikes=0;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("userposts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		Entity pages = new Entity("userposts", list.size() + 1);

		pages.setProperty("postcreator",creator);
		pages.setProperty("post",post );
		pages.setProperty("feeling",feeling );
		pages.setProperty("ontimeline", currentuser);
		pages.setProperty("hashtag", hashtag);
		pages.setProperty("privacy", "public");
		pages.setProperty("numberoflikes", noflikes);
		datastore.put(pages);
		return "ok";
	}

}
