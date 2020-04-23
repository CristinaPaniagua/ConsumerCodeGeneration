// Auto generated
package resources;

import java.io.IOException;
import java.lang.String;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Path("/demo")
public class RESTResources {
  @GET
  @Path("/test")
  @Produces(MediaType.TEXT_PLAIN)
  public String echo() {
    return "Ready to go";
  }

  @POST
  @Path("/negotiation/session-data")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String offer(String jsonPayload) {
     String response=null;
    try {
      response=sendPOST("http://127.0.0.1:8889/demo/negotiation/session-data",jsonPayload);
    }
    catch (IOException e) {
       e.printStackTrace();
    }
    if(response==null) {
        return "ERROR: EMPTY RESPONSE";
    }
    else {
      return "\"Response\":\""+response+"\"";
    }
  }

  private static String sendPOST(String url, String payload) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
     String result = "";
    try {
       HttpPost request = new HttpPost(url);
       request.addHeader("content-type", "application/json");
       request.setEntity(new StringEntity(payload));
       CloseableHttpResponse response = httpClient.execute(request);
      try {
         HttpEntity entity = response.getEntity();
        if (entity != null) {
           result = EntityUtils.toString(entity);
           System.out.println(result);
        }
      }
      finally {
         response.close();
      }
    }
    finally {
      httpClient.close();
    }
    return result;
  }
}
