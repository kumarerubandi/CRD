package org.hl7.davinci.endpoint.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.hl7.davinci.RequestIncompleteException;
import org.hl7.davinci.endpoint.database.CoverageRequirementRule;
import org.hl7.davinci.endpoint.database.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
// import org.json.simple.parser.*;
import com.google.gson.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.LinkedHashMap;
// import javax.ws.rs.core.Response;







/**
 * Defines pages by searching for returned string in the resources/templates directory.
 * Making changes here will alter the home page.
 * The "Model" parameter can be given attributes which can be referenced in the html
 * Thymeleaf provides the ability to reference and use the attributes.
 */
@Controller
public class HomeController {
  static final Logger logger = LoggerFactory.getLogger(HomeController.class);


  @Autowired
  private DataService dataService;

  /**
   * Maps the home page (the base url) and gives the model some attributes.
   * @param model an object that describes the structure and variables available for some page
   * @return a string which maps the page to some html file in the /templates directory
   */
  @RequestMapping("/")
  public String index(Model model) {
    Iterable<CoverageRequirementRule> rules = dataService.findAll();
    model.addAttribute("rules", rules);
    return "index";
  }


  /**
   * Maps the database to a specific page, hooks up the model with relevant information.
   * @param model an object with attributes that can be accessed and put on the html page
   * @return a string which corresponds with the html file to be displayed
   */
  @GetMapping("/data")
  public String data(Model model) {
    Iterable<CoverageRequirementRule> rules = dataService.findAll();
    model.addAttribute("rules", rules);
    List<String> headers = CoverageRequirementRule.getFields();
    model.addAttribute("headers", headers);
    model.addAttribute("datum", new CoverageRequirementRule());

    return "data";
  }
  
 /* @GetMapping("/coverage_determination")
  @ResponseBody
  public Object CoverageDetermination1(Object inputjson) {
   /* Iterable<CoverageRequirementRule> rules = dataService.findAll();
    model.addAttribute("rules", rules);
    List<String> headers = CoverageRequirementRule.getFields();
    model.addAttribute("headers", headers);
    model.addAttribute("datum", new CoverageRequirementRule());
     System.out.println(model);
   System.out.println(inputjson);
    return inputjson;
  }*/

  /**
   * Accepts form submissions to create new entries in the database, then redirects to the
   * data page to keep the user on the same page and show the change to the database.
   * @param datum the data object to be added to the database
   * @param errors any errors incurred from the form submission
   * @return an object that contains the model and view of the data page
   */
  @PostMapping("/data")
  public ModelAndView saveDatum(@ModelAttribute CoverageRequirementRule datum,
      BindingResult errors) {


    if (errors.hasErrors()) {
      logger.warn("There was a error " + errors);
    }
    dataService.create(datum);

    return new ModelAndView("redirect:data");
  }
  
//  public String getToken() {
//	  
//	  
//  }
  @RequestMapping(value = "/smart_app", method = RequestMethod.POST, 
		  consumes = "application/json", produces = "application/json")
  @ResponseBody
  public String getFromFhir(@RequestBody Map<String, Object> inputjson,@RequestHeader Map<String,String> headers){
	   
	
	   System.out.println("Input Json");
	   System.out.println(inputjson);
	   ObjectMapper oMapper = new ObjectMapper();
	   List<Object> appContext = oMapper.convertValue(inputjson.get("appContext") , List.class);
	   Map<String, LinkedHashMap> resources = oMapper.convertValue(appContext.get(0) , Map.class);
	   final String authorization = headers.get("authorization");
	   CloseableHttpClient httpClient = HttpClients.createDefault();
	   List<Object> entries =  new ArrayList();
	   
	   resources.forEach((key,value) -> {
	       System.out.println("\n\n\n ------------");
	//		       System.out.println(value.get("codes").getClass());
	       List<LinkedHashMap> codes = oMapper.convertValue(value.get("codes") , List.class);
	       String code = oMapper.convertValue(codes.get(0).get("code") , String.class);
	       String display = oMapper.convertValue(value.get("display") , String.class);
	       if(key == "Location") {
	    	   System.out.println("Locacacacation key ");
	    	   
	       }
	       else {
//		       String urlString = "http://54.227.173.76:8181/fhir/baseDstu3/"+key+"?patient="+inputjson.get("patientId")+"&code="+code;
		       
		       String urlString = "http://hapi.fhir.org/baseDstu3/"+key+"?patient="+inputjson.get("patientId")+"&code="+code;
		       
		       System.out.println(urlString);
		       HttpGet httpGet = new HttpGet(urlString);
		//			   http://hapi.fhir.org/baseDstu3/Condition?patient=415133
		       System.out.println(authorization);
		   	   httpGet.addHeader("Authorization",authorization);
		   	   
		   	   try {
		   		   	StringBuffer fhirresponse = new StringBuffer();
			        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			   		System.out.println("GET Response Status:: "
			   				+ httpResponse.getStatusLine().getStatusCode());
			
			   		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			
			   		String inputLine;
			   		
			   		if(httpResponse.getStatusLine().getStatusCode() == 200) {
			   			while ((inputLine = reader.readLine()) != null) {
				   			fhirresponse.append(inputLine);
				   		}
			   			JSONObject response  = new JSONObject(fhirresponse.toString());
			   			System.out.println("Resssss");
			   			System.out.println(response);
			   			
			   			int total = (int) response.get("total");
			   			System.out.println(total);
			   			if(total != 0) {
			   				JSONArray entry = new JSONArray(response.get("entry").toString());
				   			System.out.println("\n\n\n"+response.get("entry").getClass()+":::::::::::::"+response.get("entry"));
		
		//		   			List<JSONObject> entry = oMapper.convertValue(response.get("entry"), List.class);;
				   			
		//		   			System.out.println("\n\n\n:::::::::::::"+entry);
		//		   			response.get("entry").getClass();
				   			
				   	       
				   			entry.forEach((element) -> {
				   				System.out.println("\n\n\n ==========="+element);
				   				entries.add(element);
				   				
				   			});
			   			}
			   			
	//		   			entries.addAll(entry);
			   		}
			   		
	
			   		System.out.println("fhirresponse");
			   		System.out.println(fhirresponse.getClass());
			   		reader.close();
		   	   }
			   	catch(Exception ex) {
		   	    	System.out.println("Exccpepe");
		   	    	
		   	    	ex.printStackTrace();
		   	    }
	       }
	       
	   });
	   System.out.println("entriii");

			   System.out.println(entries);
	  
	  
	
		// print result
		
		
		try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	   	 
	   return entries.toString();
	   
  }
  
  
  
//  @PostMapping("/coverage_determination")
  @RequestMapping(value = "/coverage_decision", method = RequestMethod.POST, 
  consumes = "application/json", produces = "application/json")
  @ResponseBody
  public String coverageDecision(@RequestBody Map<String, Object> inputjson,@RequestHeader Map<String,String> headers) {
	    
	  
	  File file;
//	  System.out.println("------");
//      System.out.println(inputjson);
      ObjectMapper oMapper = new ObjectMapper();
      Map<String, Object> context = oMapper.convertValue(inputjson.get("context") , Map.class);
      Map<String, Object> orders = oMapper.convertValue(context.get("orders") , Map.class);

//      System.out.println(context);
      
      JSONObject reqJson = new JSONObject();
      JSONObject patientFhir = new JSONObject();
      try {
    	  file = ResourceUtils.getFile("classpath:config/data.json");
		  InputStream in = new FileInputStream(file);
		  String jsonTxt = IOUtils.toString(in, StandardCharsets.UTF_8);
		  JSONObject configData = new JSONObject(jsonTxt);
		  
//		  System.out.println("configData:\n"+ configData.get("hook_cql_map"));
		  String hook  = (String) inputjson.get("hook");
		  JSONObject hookMap = oMapper.convertValue(configData.get("hook_cql_map") , JSONObject.class);
//		  System.out.println(hookMap);
		  List<String> hookList = oMapper.convertValue(hookMap.get(hook) , List.class);
		  
		  
    	  patientFhir.put("resourceType","Bundle");
    	  patientFhir.put("id",context.get("patientId"));
    	  patientFhir.put("type","collection");
    	  patientFhir.put("entry",orders.get("entry"));
    	  reqJson.put("cql",hookList.get(0));
    	  reqJson.put("patientFhir",patientFhir);
//    	  System.out.println("reqqjson -----\n");
//    	  System.out.println(reqJson);
      
      }
      catch(JSONException json_ex) {
    	  System.out.println(json_ex.getStackTrace());
      } catch (FileNotFoundException e) {
		e.printStackTrace();
	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      CloseableHttpClient client = HttpClients.createDefault();
      // Get the token and drop the "Bearer"
  /*//GEtting Token -------------    
      final String authorization = headers.get("authorization");
//      System.out.println("httpheaddd");
//      System.out.println(headers);
//      System.out.println(headers.get("authorization"));
      String username = null;
      String password = null;
      if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
          // Authorization: Basic base64credentials
          String base64Credentials = authorization.substring("Basic".length()).trim();
          byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
          String credentials = new String(credDecoded, StandardCharsets.UTF_8);
          // credentials = username:password
          final String[] auth_values = credentials.split(":", 2);
          if(auth_values.length == 2) {
        	  username = auth_values[0];
        	  password = auth_values[1];
          }
      }

//      System.out.println(password);
//      Object[] req_context = inputjson.get;
      
      String clientId = "app-login";
      HttpPost httpPost = new HttpPost("https://54.227.173.76:8443/auth/realms/ClientFhirServer/protocol/openid-connect/token");
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("client_id", clientId));
      params.add(new BasicNameValuePair("username",username));
      params.add(new BasicNameValuePair("password", password));
      params.add(new BasicNameValuePair("grant_type", "password"));
      try {
        httpPost.setEntity(new UrlEncodedFormEntity(params));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
//      System.out.println("introspectUrl::");
//      System.out.println(introspectUrl);
      JsonObject tokenResponse;
      
      
      // Map<String,Object> params = new LinkedHashMap<>();
      try {
        CloseableHttpResponse response = client.execute(httpPost);
        String jsonString = EntityUtils.toString(response.getEntity());
        
        tokenResponse = new JsonParser().parse(jsonString).getAsJsonObject();
        client.close();
      }
      catch (IOException e) {
//        System.out.println("\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
        e.printStackTrace();
        tokenResponse = null;
      }
     System.out.println(tokenResponse);
     
     
     */
      final String authorization = headers.get("authorization");
      String token = null;
      try {
	       if(authorization != null && authorization.startsWith("Bearer"))
	       {
	        token = authorization.substring("Bearer".length()).trim();
	        System.out.println("token....");
	        System.out.println(token);
	          
	       }
	       else {
		       
	    	   throw new RequestIncompleteException("No valid authorization header was found");	       
	       }
      }
      catch(RequestIncompleteException req_exception) {
    	  	JSONObject errorObj = new JSONObject();
    	  	errorObj.put("exception", req_exception.getMessage());
	 		return errorObj.toString();
	 	}
      catch (Exception exception) {
	        System.out.println("388 EXceptionnnnnn");
	        exception.printStackTrace();
	    }
      
      String client_Id = "app-token";
      String client_secret = "237b167a-c4d0-4861-856d-6decf5426022";
      HttpPost httpPost = new HttpPost("https://54.227.173.76:8443/auth/realms/ClientFhirServer/protocol/openid-connect/token/introspect");
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("client_id", client_Id));
      params.add(new BasicNameValuePair("client_secret", client_secret));
      params.add(new BasicNameValuePair("token", token));
      try {
        httpPost.setEntity(new UrlEncodedFormEntity(params));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      
      JsonObject tokenResponse;
      try {
        CloseableHttpResponse response = client.execute(httpPost);
        String jsonStr = EntityUtils.toString(response.getEntity());
        tokenResponse = new JsonParser().parse(jsonStr).getAsJsonObject();      
        client.close();
      }
      catch (IOException e) {
        System.out.println("412\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
        e.printStackTrace();
        tokenResponse = null;
      }
      
     
      StringBuilder sb = new StringBuilder();
      JSONObject responseObj = new JSONObject();
      System.out.println("Tokeken ress");

      System.out.println(tokenResponse);
      
      try {
      	if ((tokenResponse != null) && (tokenResponse.get("active").getAsBoolean())) {
      	
      		
	        // execute method and handle any error responses.
	    	URL url = new URL("http://localhost:3000/execute_cql");
	        Gson gsonObj = new Gson();
	        reqJson.put("request_for", "decision");
	        String jsonStr = reqJson.toString();
	        System.out.println(jsonStr);
	        byte[] postDataBytes = jsonStr.getBytes("UTF-8");
	
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept","application/json");
	        if(authorization!= null) {
	        	conn.setRequestProperty("Authorization",authorization);
	          
	        }
	        else {
	        	throw new RequestIncompleteException("Unable to call CDS . token doesn't exist");
	        }
	        
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        String line =null;
	        while((line=in.readLine())!= null){
	          sb.append(line);
	        }
	        JSONObject jsonObj = new JSONObject(sb.toString());
	        
	        if(jsonObj.get("Coverage") != null) {
	        	responseObj.put("Coverage", jsonObj.get("Coverage"));
	        }
	        
		 }
      	else {
      		
        	throw new RequestIncompleteException("Invalid Oauth Token");
        }
       }
	 catch(RequestIncompleteException req_exception) {
		 	JSONObject errorObj = new JSONObject();
 	  		errorObj.put("exception", req_exception.getMessage());
	 		return errorObj.toString();
	 	}
	 catch (Exception exception) {
	        System.out.println("\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
	        exception.printStackTrace();
	    }
      
    
	 String result = responseObj.toString();
	 return result;

  }
  
  
  
  @RequestMapping(value = "/coverage_requirement", method = RequestMethod.POST, 
		  consumes = "application/json", produces = "application/json")
  @ResponseBody
  public String coverageRequirement(@RequestBody Map<String, Object> inputjson,@RequestHeader Map<String,String> headers) {
	  
	  System.out.println("------");
      System.out.println(inputjson);
      ObjectMapper oMapper = new ObjectMapper();
      File file;
      Map<String, Object> context = oMapper.convertValue(inputjson.get("context") , Map.class);
      Map<String, Object> orders = oMapper.convertValue(context.get("orders") , Map.class);

//		      System.out.println(context);
      
      JSONObject reqJson = new JSONObject();
      JSONObject patientFhir = new JSONObject();
      try {
    	  file = ResourceUtils.getFile("classpath:config/data.json");
		  InputStream in = new FileInputStream(file);
		  String jsonTxt = IOUtils.toString(in, StandardCharsets.UTF_8);
		  JSONObject configData = new JSONObject(jsonTxt);
		  
//		  System.out.println("configData:\n"+ configData.get("hook_cql_map"));
		  String hook  = (String) inputjson.get("hook");
		  JSONObject hookMap = oMapper.convertValue(configData.get("hook_cql_map") , JSONObject.class);
//		  System.out.println(hookMap);
		  List<String> hookList = oMapper.convertValue(hookMap.get(hook) , List.class);
    	  patientFhir.put("resourceType","Bundle");
    	  patientFhir.put("id",context.get("patientId"));
    	  patientFhir.put("type","collection");
    	  patientFhir.put("entry",orders.get("entry"));
    	  reqJson.put("cql",hookList.get(0));
    	  reqJson.put("patientFhir",patientFhir);
    	  System.out.println("reqquirejson -----\n");
    	  System.out.println(reqJson);
      
      }
      catch(JSONException json_ex) {
    	  System.out.println(json_ex.getStackTrace());
      } 
      catch (FileNotFoundException e) {
		e.printStackTrace();
	  } 
      catch (IOException e) {
		e.printStackTrace();
	  }
      CloseableHttpClient client = HttpClients.createDefault();
      // Get the token and drop the "Bearer"
      final String authorization = headers.get("authorization");
      String token = null;
      try {
	       if(authorization != null && authorization.startsWith("Bearer"))
	       {
	        token = authorization.substring("Bearer".length()).trim();
	        System.out.println("token....");
	        System.out.println(token);
	          
	       }
	       else {
		       
	    	   throw new RequestIncompleteException("No valid authorization header was found");	       
	       }
      }
      catch(RequestIncompleteException req_exception) {
    	  	JSONObject errorObj = new JSONObject();
    	  	errorObj.put("exception", req_exception.getMessage());
	 		return errorObj.toString();
	 	}
      catch (Exception exception) {
	        System.out.println("388 EXceptionnnnnn");
	        exception.printStackTrace();
	    }
      
      String client_Id = "app-token";
      String client_secret = "237b167a-c4d0-4861-856d-6decf5426022";
      HttpPost httpPost = new HttpPost("https://54.227.173.76:8443/auth/realms/ClientFhirServer/protocol/openid-connect/token/introspect");
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("client_id", client_Id));
      params.add(new BasicNameValuePair("client_secret", client_secret));
      params.add(new BasicNameValuePair("token", token));
      try {
        httpPost.setEntity(new UrlEncodedFormEntity(params));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      
      JsonObject tokenResponse;
      try {
        CloseableHttpResponse response = client.execute(httpPost);
        String jsonStr = EntityUtils.toString(response.getEntity());
        tokenResponse = new JsonParser().parse(jsonStr).getAsJsonObject();      
        client.close();
      }
      catch (IOException e) {
        System.out.println("412\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
        e.printStackTrace();
        tokenResponse = null;
      }
      
      
    
      StringBuilder sb = new StringBuilder();
      JSONObject response = new JSONObject();
      try{
	 
    	if ((tokenResponse != null) && (tokenResponse.get("active").getAsBoolean())) {
    	      	
        
	        // execute method and handle any error responses.
	    	URL url = new URL("http://localhost:3000/execute_cql");
	        Gson gsonObj = new Gson();
	        reqJson.put("request_for", "requirements");
	        String jsonStr = reqJson.toString();
	        System.out.println(jsonStr);
	        byte[] postDataBytes = jsonStr.getBytes("UTF-8");
	
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Accept","application/json");
	        conn.setRequestProperty("Authorization",authorization);
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        String line =null;
	        while((line=in.readLine())!= null){
	          sb.append(line);
	        }
	//        System.out.println("");
	//        System.out.println(sb);
	        JSONObject jsonObj = new JSONObject(sb.toString());
	        JSONObject singleCard = new JSONObject();
	        
	        ArrayList<JSONObject> suggestions = new ArrayList<JSONObject>();
	        ArrayList<JSONObject> links = new ArrayList<JSONObject>();
	        ArrayList<JSONObject> cards = new ArrayList<JSONObject>();
	        JSONObject applink = new JSONObject();
	        applink.put("label","SMART App");
	        applink.put("url","http://localhost:3000/cd");
	        applink.put("type","smart");
	        applink.put("appContext",jsonObj.get("requirements"));
	        links.add(applink);
	        singleCard.put("links", links);
	        singleCard.put("suggestions", suggestions);
	        singleCard.put("summary","List of Requirements");
	        singleCard.put("indicator","info");
	        singleCard.put("detail","The requested procedure needs more documentation to process further");
	        cards.add(singleCard);
	        response.put("cards",cards);
	//        
	//        System.out.println("cards------\n\n\n");
	//        System.out.println(cards);
    	   }
    	else {
           throw new RequestIncompleteException("Invalid Oauth Token");
    	}
	  }
	 catch(RequestIncompleteException req_exception) {
		  JSONObject errorObj = new JSONObject();
	  	  errorObj.put("exception", req_exception.getMessage());
	 	  return errorObj.toString();
	 	}
	 catch (Exception exception) {
	        System.out.println("\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
	        exception.printStackTrace();
	    }
    
	 String result = response.toString();
	 return result;

  }
  
  @RequestMapping(value = "/prior_authorization", method = RequestMethod.POST, 
		  consumes = "application/json", produces = "application/json")
  @ResponseBody
  public String priorAuthorization(@RequestBody Map<String, Object> inputjson,@RequestHeader Map<String,String> headers) {

   // JSONObject obj = new JSONObject();
	  	StringBuilder sb = new StringBuilder();
	    File file;
	    JSONObject response = new JSONObject();
	    response.put("PriorAuthorization", false);
	    
	    /*
	    CloseableHttpClient client = HttpClients.createDefault();
	      // Get the token and drop the "Bearer"
	      final String authorization = headers.get("authorization");
	      String token = null;
	      try {
		       if(authorization != null && authorization.startsWith("Bearer"))
		       {
		        token = authorization.substring("Bearer".length()).trim();
		        System.out.println("token....");
		        System.out.println(token);
		          
		       }
		       else {
			       
		    	   throw new RequestIncompleteException("No valid authorization header was found");	       
		       }
	      }
	      catch(RequestIncompleteException req_exception) {
	    	  	JSONObject errorObj = new JSONObject();
	    	  	errorObj.put("exception", req_exception.getMessage());
		 		return errorObj.toString();
		 	}
	      catch (Exception exception) {
		        System.out.println("388 EXceptionnnnnn");
		        exception.printStackTrace();
		    }
	      
	      String client_Id = "app-token";
	      String client_secret = "237b167a-c4d0-4861-856d-6decf5426022";
	      HttpPost httpPost = new HttpPost("https://54.227.173.76:8443/auth/realms/ClientFhirServer/protocol/openid-connect/token/introspect");
	      List<NameValuePair> params = new ArrayList<NameValuePair>();
	      params.add(new BasicNameValuePair("client_id", client_Id));
	      params.add(new BasicNameValuePair("client_secret", client_secret));
	      params.add(new BasicNameValuePair("token", token));
	      try {
	        httpPost.setEntity(new UrlEncodedFormEntity(params));
	      } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	      }
	      
	      JsonObject tokenResponse;
	      try {
	        CloseableHttpResponse tokenResponceObj = client.execute(httpPost);
	        String jsonStr = EntityUtils.toString(tokenResponceObj.getEntity());
	        tokenResponse = new JsonParser().parse(jsonStr).getAsJsonObject();      
	        client.close();
	      }
	      catch (IOException e) {
	        System.out.println("412\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
	        e.printStackTrace();
	        tokenResponse = null;
	      }
	      */
		try {
			//if ((tokenResponse != null) && (tokenResponse.get("active").getAsBoolean())) {
				file = ResourceUtils.getFile("classpath:config/data.json");
				InputStream in = new FileInputStream(file);
				String jsonTxt = IOUtils.toString(in, StandardCharsets.UTF_8);
				JSONObject configData = new JSONObject(jsonTxt);
				ObjectMapper oMapper = new ObjectMapper();
				List<String> allowedResources = oMapper.convertValue(configData.get("PriorAuthorizationResources") , List.class);
				Map<String, Object> context = oMapper.convertValue(inputjson.get("context") , Map.class);
			    Map<String, Object> orders = oMapper.convertValue(context.get("orders") , Map.class);
			    List<LinkedHashMap> entries = oMapper.convertValue(orders.get("entry") , List.class);
			    entries.forEach((obj) -> {
					  System.out.println("\nresource:"+obj.get("resource").getClass());

					  LinkedHashMap jsonEntry = oMapper.convertValue(obj.get("resource") , LinkedHashMap.class);
				      String resType =(String)  jsonEntry.get("resourceType");
				      if(allowedResources.contains(resType)) {
				    	  response.put("PriorAuthorization", true);
				    	  
				      }
			    });
//			}
//			else {
//		       throw new RequestIncompleteException("Invalid Oauth Token");
//		    }
			
		   
		    
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		catch(RequestIncompleteException req_exception) {
//		  JSONObject errorObj = new JSONObject();
//	  	  errorObj.put("exception", req_exception.getMessage());
//	 	  return errorObj.toString();
//	 	}
		  
       
		return response.toString();
        


  }
  
  @PostMapping("/review")
  @ResponseBody
  public String review(@RequestBody Object inputjson) {
    
    
   // JSONObject obj = new JSONObject();
    StringBuilder sb = new StringBuilder();
 try{
       
        // execute method and handle any error responses.
    	URL url = new URL("http://localhost:3000/test");
        Gson gsonObj = new Gson();
        String jsonStr = gsonObj.toJson(inputjson);
        System.out.println(jsonStr);
        byte[] postDataBytes = jsonStr.getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept","application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line =null;
        while((line=in.readLine())!= null){
          sb.append(line);
        }
       
        
    }
    catch (Exception e) {
        System.out.println("\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
        e.printStackTrace();
    }
    
  String result = sb.toString();
  return result;

  }

  @GetMapping("/public")
  public String public_key(Model model) {
    return "public";
  }

  @GetMapping("/requests")
  public String request_log(Model model) {
    return "requests";
  }




}
