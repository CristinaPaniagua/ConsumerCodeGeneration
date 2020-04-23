/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.io.IOException;
import java.sql.Timestamp;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;


import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;



/**
 *
 * @author cripan
 */

@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloResource {
    
    
    @GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String echo() {

		//String test="{\"Response\":\"hello world\"}";
                String test="Ready to go";
		return test; 

	}
        
    @POST
        @Path("/negotiation/session-data")
        	public String offer(String jsonPayload) {

   Timestamp timestamp = new Timestamp(System.currentTimeMillis());
   long tstart =timestamp.getTime();
    String response=null;
     try {
            response=sendPOST("http://127.0.0.1:8889/demo/negotiation/session-data",jsonPayload);
    } catch (IOException e) {
            e.printStackTrace();
        }
    
    if(response==null){
        return "ERROR: EMPTY RESPONSE";
    }else{
        Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
        long tend = timestampEnd.getTime();
        System.out.println(tend-tstart);    
        
    return "\""+response+"\"";
    }
  }

private static String sendPOST(String url, String payload) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        
        try {

            HttpPost request = new HttpPost(url);

       // add request headers
            request.addHeader("content-type", "application/json");
             
        // send a JSON data
        
        
            request.setEntity(new StringEntity(payload.toString()));
            CloseableHttpResponse response = httpClient.execute(request);
           
            try {

                // Get HttpResponse Status
                //System.out.println(response.getProtocolVersion());              // HTTP/1.1
                //System.out.println(response.getStatusLine().getStatusCode());   // 200
                //System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                     result = EntityUtils.toString(entity);
                    System.out.println(result);
                }

            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    
        return result;
    }
        
}

        
        
        
        
