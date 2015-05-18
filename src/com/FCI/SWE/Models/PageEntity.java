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
public class PageEntity {
	
	
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
			else if (entity.getProperty("pagename").toString().equals(pname)&&entity.getProperty("pagecreator").toString().equals(user)){posts.add(entity.getProperty("pagename").toString()+" ((Owner))");}
				else  if(entity.getProperty("pagename").toString().equals(pname)&&entity.getProperty("likers").toString().contains(user)){posts.add(entity.getProperty("pagename").toString()+" ((Liked))");}}
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
				UserEntity u=null;
                u.setpname(pname);
				return posts ; 
			
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
							 str=str.replaceAll("#+([a-zA-Z0-9_]+)", "<a href='/social/hashtag/"+hname+"'><font color='black'>#"+hname +"</font></a>");
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
	
	
	
	
	
}
