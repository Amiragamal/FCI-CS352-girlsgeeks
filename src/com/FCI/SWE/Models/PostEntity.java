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
public class PostEntity {
	
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
				UserEntity u = null;
				for (Entity entity3 : pq3.asIterable()) {
					
					if (entity3.getProperty("hashtag").toString().equals(name) ) {
						if(entity3.getProperty("privacy").equals("public")||entity3.getProperty("postcreator").toString().equals(u.getName())){
							  posts.add( entity3.getProperty("post").toString());
							 }
							else if(entity3.getProperty("privacy").equals("private")){
								 DatastoreService datastore4 = DatastoreServiceFactory.getDatastoreService();
								  Query gaeQuery4 = new Query("Friends");
								  PreparedQuery pq4 = datastore4.prepare(gaeQuery4);
						        for (Entity entity4 : pq4.asIterable()) {
									if ((entity4.getProperty("Username").toString().equals(u.getName()) && entity4.getProperty("Friendname").toString().equals(entity3.getProperty("postcreator").toString()))||(entity4.getProperty("Username").toString().equals(entity3.getProperty("postcreator").toString())&& entity4.getProperty("Friendname").toString().equals(u.getName()))) {
										 System.out.println("they're friends");
										  posts.add( entity3.getProperty("post").toString());
									}
						 }
					}
				}
				}}}
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
	
	
}
