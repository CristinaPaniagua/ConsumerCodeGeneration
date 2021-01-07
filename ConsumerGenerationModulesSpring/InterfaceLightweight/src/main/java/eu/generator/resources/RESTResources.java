// Auto generated
package eu.generator.resources;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Exception;
import java.lang.String;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaultHandler;
import org.eclipse.californium.elements.exception.ConnectorException;

@Path("/demo")
public class RESTResources {
  @GET
  @Path("/test")
  @Produces(MediaType.TEXT_PLAIN)
  public String echo() {
    return "Ready to go";
  }

  @POST
  @Path("/testTranslation")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String offer(String receivedPayload) {
    RequestDTO_C0 payload=new RequestDTO_C0();
     String response=null;
    JsonFactory jsonFactory_objMapper = new JsonFactory();
    ObjectMapper objMapper=new ObjectMapper(jsonFactory_objMapper);
    try {
      payload=objMapper.readValue(receivedPayload, RequestDTO_C0.class);
      System.out.println(payload.toString());
      response=consumeService("http://127.0.0.1:8889/demo/testTranslationB",payload);
    }
    catch (Exception e) {
       e.printStackTrace();
    }
    if(response==null) {
        return "ERROR: EMPTY RESPONSE";
    }
    else {
      return response;
    }
  }

  private static String consumeService(String url, RequestDTO_C0 payload) throws IOException {
    //Adding Import NetworkConfig;
     File CONFIG_FILE = new File("Californium.properties");
        	 String CONFIG_HEADER = "Californium CoAP Properties file for Fileclient";
        	 int DEFAULT_MAX_RESOURCE_SIZE = 2 * 1024 * 1024; // 2 MB
        	 int DEFAULT_BLOCK_SIZE = 512;
     NetworkConfigDefaultHandler DEFAULTS = new NetworkConfigDefaultHandler() {

        		@Override
        		public void applyDefaults(NetworkConfig config) {
        			config.setInt(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, DEFAULT_MAX_RESOURCE_SIZE);
        			config.setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, DEFAULT_BLOCK_SIZE);
        			config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, DEFAULT_BLOCK_SIZE);
        		};};
    NetworkConfig config = NetworkConfig.createWithFile(CONFIG_FILE, CONFIG_HEADER, DEFAULTS);
    NetworkConfig.setStandard(config);
    URI uri=null;
    String[] args={"0"};
    try {
      uri = new URI("coap://localhost:5683/publish");
    } catch(URISyntaxException e) {
      System.err.println("Invalid URI: " + e.getMessage());
      System.exit(-1);
    }
    CoapClient client= new CoapClient(uri);
    CoapResponse response = null;
    try {
      String payloadS=new ObjectMapper().writeValueAsString(payload);
      System.out.println("Payload Sent: " + payloadS);
      response = client.post(payloadS,MediaTypeRegistry.APPLICATION_JSON);
    } catch(ConnectorException|IOException e) {
      System.err.println("Got an error: " + e);
    }
    if(response!=null) {
      System.out.println(response.getCode());
      if (args.length > 1) {
        try (FileOutputStream out = new FileOutputStream(args[1])) {
          out.write(response.getPayload());
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println(response.getResponseText());
        System.out.println(Utils.prettyPrint(response));
      }
    } else {
      System.out.println("No response received.");
    }
    client.shutdown();
    return response.getResponseText();
  }
}
