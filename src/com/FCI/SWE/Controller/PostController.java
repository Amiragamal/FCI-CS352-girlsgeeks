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



@Path("/")
@Produces("text/html")
public class PostController {
	
	   @POST
		@Path("/savepost")
		@Produces(MediaType.TEXT_PLAIN)
		public String savepost(@FormParam("pname") String pname,@FormParam("pagepost") String pagepost,@FormParam("privacy")String privacy) throws ParseException {
			
			String serviceUrl = "http://localhost:8888/rest/SavePostService";
			String urlParameters = "pname=" + pname + "&pagepost=" + pagepost+ "&privacy=" + privacy;
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
			
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				if (object.get("Status").equals("OK"))
					return "Posted to page Successfully";

			return "Failed";
		}

	  
	   @GET
	   @Path("/hashtag/{name}")
		@Produces("text/html")
		public Response hashtag(@PathParam("name") String name) throws ParseException{
			String serviceUrl = "http://localhost:8888/rest/HashtagService";
			String urlParameters = "name=" + name ;
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				Vector posts=new Vector();
				Vector nameh=new Vector();
				nameh.add(object.get("name"));
				Vector number=new Vector();
				number.add(object.get("nop"));
				Map<String, Vector> map = new HashMap<String, Vector>();
				int nofposts=Integer.parseInt( object.get("noposts").toString());
				for(int i=2;i<nofposts;i++){
				 posts.add(object.get("post"+i).toString());}
				map.put("nop", number);
				map.put("name", nameh);
				map.put("posts",posts);
					return Response.ok(new Viewable("/jsp/viewhashtag", map)).build();
			
		}
	   
	   
	   @POST
	  	@Path("/saveuserpost")
	  	@Produces(MediaType.TEXT_PLAIN)
	  	public String saveuserpost(@FormParam("pagepost") String pagepost,@FormParam("privacy") String privacy,@FormParam("user") String user,@FormParam("feeling") String feeling,@FormParam("custom") String custom) throws ParseException {
	  		
	  		String serviceUrl = "http://localhost:8888/rest/SaveUserPostService";
			String urlParameters = "pagepost=" + pagepost+ "&privacy=" + privacy+ "&user=" + user+ "&feeling=" + feeling+ "&custom=" + custom;
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
	  			JSONParser parser = new JSONParser();
	  			Object obj = parser.parse(retJson);
	  			JSONObject object = (JSONObject) obj;
	  			if (object.get("Status").equals("OK"))
	  				return "Posted to timeline Successfully";
	  		
	  		return "Failed";
	  	}
	   @POST
		@Path("/likeuserpost")
		@Produces(MediaType.TEXT_PLAIN)
		public String likeuserpost(@FormParam("post") String post,@FormParam("timeline") String timeline) throws ParseException {
			
			String serviceUrl = "http://localhost:8888/rest/LikeUserPostService";
			String urlParameters = "post=" + post+"&timeline=" + timeline ;
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				if (object.get("Status").equals("OK"))
					return "Post Liked Successfully !";
			return "Failed";
		}

	   @POST
	 	@Path("/shareuserpost")
	 	@Produces(MediaType.TEXT_PLAIN)
	 	public String shareuserpost(@FormParam("post") String post,@FormParam("creator") String creator,@FormParam("hashtag") String hashtag,@FormParam("feeling") String feeling) throws ParseException {
	 		
	 		String serviceUrl = "http://localhost:8888/rest/ShareUserPostService";
				String urlParameters = "post=" + post+ "&creator=" + creator+"&hashtag=" + hashtag+"&feeling=" + feeling;

				Connectionn Connection2=new Connectionn();

				String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
								"application/x-www-form-urlencoded;charset=UTF-8");
	 			JSONParser parser = new JSONParser();
	 			Object obj = parser.parse(retJson);
	 			JSONObject object = (JSONObject) obj;
	 			if (object.get("Status").equals("OK"))
	 				return "Posted to your timeline Successfully";
	 	
	 		return "Failed";
	 	}
	
	
	
	
	
	
	
	
	
	
}
