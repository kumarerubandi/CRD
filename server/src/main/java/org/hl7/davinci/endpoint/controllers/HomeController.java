package org.hl7.davinci.endpoint.controllers;

import org.hl7.davinci.endpoint.database.CoverageRequirementRule;
import org.hl7.davinci.endpoint.database.DataService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.JsonObject;
import com.google.gson.*;
// import org.json.simple.parser.*;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
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
  
  @PostMapping("/coverage_determination")
  @ResponseBody
  public String coverageDetermination(@RequestBody Object inputjson) {
    
    
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
