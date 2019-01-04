package org.hl7.davinci.endpoint.cdshooks.services.crd;

import org.json.JSONObject;
import org.json.JSONArray;

import org.json.JSONTokener;
import org.json.JSONException;



public class CoverageService {
	
	
//	public class resourceObject{
//		public Object resource;
//		public resourceObject() {
//			
//		}
//		public resourceObject(Object resource) {
//			super();
//			this.resource = resource;
//		}
//		
//		public Object getResource() {
//			return resource;
//		}
//		
//	}
//	
//	public class CoverageServiceContext {
//		public String patientId ;
//		public Object[] entry;
//		public  CoverageServiceContext() {
//			
//		}
//		public  CoverageServiceContext(String patientId,Object[] entry) {
//			super();
//			this.patientId = patientId;
//			this.entry = entry;
//			
//		}
//		
//		public String getPatientId() {
//			return patientId;
//		}
//		
//		public Object[] getEntry() {
//			return entry;
//		}
//	}
//	Object context;
//	public CoverageService() {
//		
//	}
//	
//	public CoverageService(CoverageServiceContext context) {
//		super();
//		this.context = context;
//		
//	}
//	
//	public Object getContext() {
//	     return context;
//	}
//	
//	
//	public static JSONObject objectToJSONObject(Object object){
//	    Object json = null;
//	    JSONObject jsonObject = null;
//	    try {
//	        json = new JSONTokener(object.toString()).nextValue();
//	    } catch (JSONException e) {
//	        e.printStackTrace();
//	    }
//	    if (json instanceof JSONObject) {
//	        jsonObject = (JSONObject) json;
//	    }
//	    return jsonObject;
//	}
//	
////	public JSONObject getReqJson() {
////		System.out.println("\n\n");
////		System.out.println(this.context);
//////		System.out.println(context.getPatientId());
//////		System.out.println(context.getEntry());
////
////		
////		JSONObject reqJson = new JSONObject();
////		JSONObject patientFhir = new JSONObject();
////		CoverageServiceContext context = this.getContext();
////		
////		patientFhir.put("resourceType","Bundle");
////		patientFhir.put("id",context.getPatientId());
////		patientFhir.put("type","collection");
////		patientFhir.put("entry",context.get('entry'));
////		reqJson.put("cql","AdultLiverTransplantation");
////		reqJson.put("patientFhir",patientFhir);
////		return reqJson ;
////	}
//	
	
	
}



