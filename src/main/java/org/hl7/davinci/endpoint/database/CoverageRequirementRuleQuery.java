package org.hl7.davinci.endpoint.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
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



public class CoverageRequirementRuleQuery {

  private List<CoverageRequirementRule> response;
  private CoverageRequirementRuleCriteria criteria;
  private CoverageRequirementRuleFinder finder;

  public CoverageRequirementRuleQuery(CoverageRequirementRuleFinder finder) {
    this.finder = finder;
    this.criteria = new CoverageRequirementRuleCriteria();
  }

  public void execute() {
    // response = finder.findRules(criteria);
    System.out.println("----------Ressssp");
     System.out.println(response);
    
    // response  = new List();
    // List<CoverageRequirementRule> response = new List<CoverageRequirementRule>();
    CoverageRequirementRule response = new CoverageRequirementRule();
    response.setInfoLink("https://www.cms.gov/Outreach-and-Education/Medicare-Learning-Network-MLN/MLNProducts/downloads/PMDFactSheet07_Quark19.pdf");
    response.setEquipmentCode("abc123");
    response.setCodeSystem("https://bluebutton.cms.gov/resources/codesystem/hcpcs");
    response.setNoAuthNeeded(true);
    response.setAgeRangeHigh(42);
    response.setAgeRangeLow(0);
    response.setGenderCode('M');
    

    try{
//        JsonObject jsonResponse;
//        CloseableHttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost("http://localhost:3000/test");
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("aaa", "dd"));
//        params.add(new BasicNameValuePair("bbb", "sdj"));
//        params.add(new BasicNameValuePair("ccc", "sdcs"));
//        CloseableHttpResponse res= client.execute(post);
//        String jsonString = EntityUtils.toString(res.getEntity());
//        jsonResponse = new JsonParser().parse(jsonString).getAsJsonObject();
//        System.out.println("\n\n\n\n params");
//        System.out.println(params);
//        System.out.println(jsonResponse);
//        client.close();
        // execute method and handle any error responses.
    	URL url = new URL("http://localhost:3000/test");
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("name", "Freddie the Fish");
        params.put("email", "fishie@seamail.example.com");
        params.put("reply_to_thread", 10394);
        params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        for (int c; (c = in.read()) >= 0;) {
            System.out.print((char)c);
        }
    }
    catch (Exception e) {
        System.out.println("\n\n\\n\n\n\\n\n\n\n\nEXceptionnnnnn");
        e.printStackTrace();
    }
    
    
    
    
    
    // response.add(rule);

    // Outcome: [no_auth_needed: null, info_link false
    // response.add(retVal);
    System.out.println(response);
  }

  public List<CoverageRequirementRule> getResponse() {
    return response;
  }

  public void setResponse(
      List<CoverageRequirementRule> response) {
    this.response = response;
  }

  public CoverageRequirementRuleCriteria getCriteria() {
    return criteria;
  }

  public void setCriteria(CoverageRequirementRuleCriteria criteria) {
    this.criteria = criteria;
  }


}
