package org.hl7.davinci.endpoint.controllers.r4;

import org.hl7.davinci.endpoint.cdshooks.services.crd.r4.MedicationPrescribeService;
import org.hl7.davinci.endpoint.cdshooks.services.crd.r4.OrderReviewService;

import javax.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.*;
import org.cdshooks.CdsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.davinci.endpoint.cdshooks.services.crd.CdsServiceInformation;
import org.hl7.davinci.r4.crdhook.CrdPrefetch;
import org.hl7.davinci.r4.crdhook.medicationprescribe.MedicationPrescribeRequest;
import org.hl7.davinci.r4.crdhook.orderreview.OrderReviewRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ResourceUtils;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
// import org.json.simple.parser.*;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;

@RestController("r4_CdsHooksController")
public class CdsHooksController {

  static final Logger logger = LoggerFactory.getLogger(CdsHooksController.class);

  static final String FHIR_RELEASE = "/r4";
  static final String URL_BASE = "/cds-services";


  @Autowired private OrderReviewService orderReviewService;
  @Autowired private MedicationPrescribeService medicationPrescribeService;

  /**
   * The FHIR r4 services discovery endpoint.
   * @return A services object containing an array of all services available on this server
   */
  @CrossOrigin
 /* @GetMapping(value = FHIR_RELEASE + URL_BASE)
  public CdsServiceInformation serviceDiscovery() {
    logger.info("r4/serviceDiscovery");*/
    @RequestMapping(value = "/r4/cds-services", method = RequestMethod.GET, 
		   produces = "text/plain")
  @ResponseBody
    public String cdsService()
    {
      String jsonTxt="";
      try{
        File file;
      file = ResourceUtils.getFile("classpath:config/cds-services.json");
				InputStream in = new FileInputStream(file);
				jsonTxt = IOUtils.toString(in, StandardCharsets.UTF_8);
       
      }
      catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      return jsonTxt;   
    }
   /* CdsServiceInformation serviceInformation = new CdsServiceInformation();
    serviceInformation.addServicesItem(orderReviewService);
    serviceInformation.addServicesItem(medicationPrescribeService);
    System.out.println("serviceInformation"+serviceInformation);
    return serviceInformation;*/
    
   //return serviceInformation;
   
   /* ObjectMapper mapper = new ObjectMapper();
    String jsonString = mapper.defaultPrettyPrintingWriter().writeValueAsString(jsonStr);
    //return mapper.defaultPrettyPrintingWriter().writeValueAsString(jsonStr);
    return jsonString;*/
   /* ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(serviceInformation);*/
   // return json;
    //return mapper.defaultPrettyPrintingWriter().writeValueAsString(json);
   // return serviceInformation;
  }

  /**
   * The coverage requirement discovery endpoint for the order review hook.
   * @param request An order review triggered cds request
   * @return The card response
   */
 /* @CrossOrigin
  @PostMapping(value = FHIR_RELEASE + URL_BASE + "/" + OrderReviewService.ID,
      consumes = "application/json;charset=UTF-8")
  public CdsResponse handleOrderReview(@Valid @RequestBody OrderReviewRequest request) {
    logger.info("r4/handleOrderReview");
    if (request.getPrefetch() == null) {
      request.setPrefetch(new CrdPrefetch());
    }
    return orderReviewService.handleRequest(request);
  }

  /**
   * The coverage requirement discovery endpoint for the medication prescribe hook.
   * @param request A medication prescribe triggered cds request
   * @return The card response
   */
/*  @CrossOrigin
  @PostMapping(value = FHIR_RELEASE + URL_BASE + "/" + MedicationPrescribeService.ID,
      consumes = "application/json;charset=UTF-8")
  public CdsResponse handleMedicationPrescribe(@Valid @RequestBody MedicationPrescribeRequest request) {
    logger.info("r4/handleMedicationPrescribe");
    if (request.getPrefetch() == null) {
      request.setPrefetch(new CrdPrefetch());
    }
    return medicationPrescribeService.handleRequest(request);
  }
}*/
