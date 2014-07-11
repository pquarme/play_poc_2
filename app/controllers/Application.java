package controllers;

import static play.data.Form.form;

import java.util.*;

import org.w3c.dom.*;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.*;
import play.*;
import play.libs.Json;
import play.data.DynamicForm;
import play.libs.XPath;
import play.libs.F.*;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.*;
import scala.util.parsing.json.*;
import views.html.*;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;

public class Application extends Controller {

	public static Result index(String any) {
		return ok(index.render("Your new application is ready."));
	}
	
	/*public static Result Redirect(String any) {
		return ok(main.render());
	}*/

	public static Result sendMessage() {
		DynamicForm dynamicForm = form().bindFromRequest();
		Logger.info("Message is: " + dynamicForm.get("message")); // testing Logger
		return ok(index.render(dynamicForm.get("message")));
	}
	
	public static Result google() {
	 	DynamicForm dynamicForm = form().bindFromRequest();
	 	String query = dynamicForm.get("googleQuery");
		Logger.info("Google query is: " + query);
	    return redirect("http://www.google.com/#q=" + query );
	 }

	// javascript routes
	public static Result jsRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes",
				controllers.routes.javascript.Application.getUsers(),
				controllers.routes.javascript.Application.getJson(),
				controllers.routes.javascript.Application.getMetar(),
				controllers.routes.javascript.Application.getMySqlDb()
				)
				);
	}

	public static Result getUsers() {
		ArrayList<User> users = new ArrayList<User>();

		User user1 = new User();
		user1.setFirstName("Frank");
		user1.setLastName("Castle");
		user1.setOccupation("FBI Agent");

		User user2 = new User();
		user2.setFirstName("Tony");
		user2.setLastName("Stark");
		user2.setOccupation("Stark idustry CEO");

		User user3 = new User();
		user3.setFirstName("Bruce");
		user3.setLastName("Wayne");
		user3.setOccupation("Wayne Enterprise CEO");

		users.add(user1);
		users.add(user2);
		users.add(user3);
		
		/*JsonNode result = Json.newObject()
				  .put("fName", user1.getFirstName() )
				  .put("lName", user1.getLastName() )
				  .put("occupation", user1.getOccupation());*/
		
		
		//prints array into console as string
		final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
	    final Gson gson = gsonBuilder.create();
		String j  = gson.toJson(users);
		Logger.info("Json file is :  " + j);
		
		/*j = j.replace("\"", "");
		j = j.replace("[", "");
		j = j.replace("]", "");
		JsonObject obj = new JsonObject();
		JsonObject newObj = new JsonParser().parse(j).getAsJsonObject();
		ObjectNode m = Json.newObject().put("users", newObj);*/
		
		//prints arraylist into json object
		return ok(Json.toJson(users));
	}

	public static Promise<Result> getJson(String query) {
		Promise<WSResponse> responsePromise = WS
				.url("http://md5.jsontest.com/?text="+query)
				.get();
		Promise<Result> resultPromise = responsePromise
				.map(new Function<WSResponse, Result>() {
					@Override
					public Result apply(WSResponse response) throws Throwable {
						if (response.getStatus() != OK)
							return status(response.getStatus(),
									response.getBody());

						JsonNode searchResponse = response.asJson();
						return ok(searchResponse);
					}
				});
		return resultPromise;
	}
	
	
	//Process xml and serve json
	//@BodyParser.Of(Xml.class)
	public static Promise<Result> getMetar(String query) {
		String url = "https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=" +
					 query+"&hoursBeforeNow=2";
		Promise<WSResponse> responsePromise = WS
				.url(url)
				.get();
		Promise<Result> resultPromise = responsePromise
				.map(new Function<WSResponse, Result>() {
					@Override
					public Result apply(WSResponse response) throws Throwable {
						if (response.getStatus() != OK)
							return status(response.getStatus(),
									response.getBody());

						
						Document dom = response.asXml();
						Logger.info("here: "+response.getStatus());
						String station = XPath.selectText("//station_id", dom);
						String temp = XPath.selectText("//temp_c", dom);
						String windSpeed = XPath.selectText("//wind_speed_kt", dom);
						String windDir = XPath.selectText("//wind_dir_degrees", dom);
						String elevation = XPath.selectText("//elevation_m", dom);
						
						JsonNode result = Json.newObject()
										  .put("station", station )
										  .put("temp", temp )
										  .put("windSpeed",windSpeed )
										  .put("windDir",windDir )
										  .put("elevation", elevation);
						return ok(result);
					}
				});
		return resultPromise;
	}
	
	public static Result getMySqlDb(){
		Logger.info("sql running: ");
		List<Actor> actors = Actor.find.all();
		Ebean.sort(actors, "firstName asc");
		
		for(int i =0; i < 5; i++){
			Logger.info("Actor : "+actors.get(i).firstName + " " + actors.get(i).lastName);
		}
		return ok(Json.toJson(actors));
	} 
}
