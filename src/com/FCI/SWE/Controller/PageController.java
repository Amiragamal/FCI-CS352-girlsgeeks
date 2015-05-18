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
public class PageController {
	

	   @POST
		@Path("/savepage")
		@Produces(MediaType.TEXT_PLAIN)
		public String savepage(@FormParam("pname") String pname,
				@FormParam("ptype") String ptype, @FormParam("pcategory") String pcategory) throws ParseException {
			
			String serviceUrl = "http://localhost:8888/rest/PageCreationService";
			String urlParameters = "pname=" + pname + "&ptype=" + ptype+ "&pcategory=" + pcategory;
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				if (object.get("Status").equals("OK"))
					return "Page Created Successfully !";
			
			return "Failed";
		}
	   @POST
	   @Path("/viewepages")
		@Produces("text/html")
		public Response viewepages() throws ParseException{
			String serviceUrl = "http://localhost:8888/rest/ViewePagesService";
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, null, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				Vector pages=new Vector();
				Map<String, Vector> map = new HashMap<String, Vector>();
				int nofpages=Integer.parseInt( object.get("nop").toString());
				for(int i=0;i<nofpages;i++){
				 pages.add(object.get("page"+i).toString());}
				map.put("pages",pages);
					return Response.ok(new Viewable("/jsp/viewpages", map)).build();
		
		}
	   @POST
	   @Path("/viewemypages")
		@Produces("text/html")
		public Response viewemypages() throws ParseException{
			String serviceUrl = "http://localhost:8888/rest/VieweMyPagesService";
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, null, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				Vector pages=new Vector();
				Map<String, Vector> map = new HashMap<String, Vector>();
				int nofpages=Integer.parseInt( object.get("nop").toString());
				for(int i=0;i<nofpages;i++){
				 pages.add(object.get("page"+i).toString());}
				map.put("pages",pages);
					return Response.ok(new Viewable("/jsp/viewmypages", map)).build();
		
		}
	   @POST
	   @Path("/openmypage")
		@Produces("text/html")
		public Response openmypage(@FormParam("pagename") String pagename) throws ParseException{
			String serviceUrl = "http://localhost:8888/rest/HandlePageService";
			String urlParameters = "pagename=" + pagename ;

			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				Vector posts=new Vector();
				Vector name=new Vector(); name.add(object.get("name").toString());
				Vector likes=new Vector(); likes.add(object.get("likes").toString());
				Map<String, Vector> map = new HashMap<String, Vector>();
				int noposts=Integer.parseInt( object.get("noposts").toString());
				for(int i=2;i<noposts;i++){
				 posts.add(object.get("post"+i).toString());}
				map.put("like",likes);
				map.put("name", name);
				map.put("post", posts);
					return Response.ok(new Viewable("/jsp/handlepage",map)).build();
		
		}
	   @POST
	   @Path("/openpage")
		@Produces("text/html")
		public Response openpage(@FormParam("pagename") String pagename) throws ParseException{
			String serviceUrl = "http://localhost:8888/rest/OpenPageService";
			String urlParameters = "pagename=" + pagename ;
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, urlParameters, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				Vector posts=new Vector();
				Vector name=new Vector(); name.add(object.get("name").toString());
				Map<String, Vector> map = new HashMap<String, Vector>();
				int noposts=Integer.parseInt( object.get("noposts").toString());
				for(int i=1;i<noposts;i++){
				 posts.add(object.get("post"+i).toString());}
				
				map.put("name", name);
				map.put("post", posts);
					return Response.ok(new Viewable("/jsp/openpage",map)).build();
		
		}	
	   @POST
		@Path("/likepage")
		@Produces(MediaType.TEXT_PLAIN)
		public String likepage() throws ParseException {
			
			String serviceUrl = "http://localhost:8888/rest/LikePageService";
			Connectionn Connection2=new Connectionn();

			String retJson = Connection2.connect(serviceUrl, null, "POST",
							"application/x-www-form-urlencoded;charset=UTF-8");
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(retJson);
				JSONObject object = (JSONObject) obj;
				if (object.get("Status").equals("OK"))
					return "Page Liked Successfully !";
			return "Failed";
		}

	   @POST
		@Path("/likepagepost")
		@Produces(MediaType.TEXT_PLAIN)
		public String likepagepost(@FormParam("post") String post,@FormParam("timeline") String timeline) throws ParseException {
			
			String serviceUrl = "http://localhost:8888/rest/LikePagePostService";
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
	
	
	
}
