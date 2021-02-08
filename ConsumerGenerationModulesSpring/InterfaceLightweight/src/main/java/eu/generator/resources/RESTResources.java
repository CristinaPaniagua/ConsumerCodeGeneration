// Auto generated
package eu.generator.resources;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.underscore.lodash.U;
import eu.generator.consumer.RequestDTO_C0;
import eu.generator.provider.RequestDTO_P0;
import eu.generator.provider.ResponseDTO_P0;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.lang.String;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.Consumes;
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
  @POST
  @Path("/testTranslation")
  @Produces(MediaType.APPLICATION_XML)
  @Consumes(MediaType.APPLICATION_XML)
  public String offer(String receivedPayload) {
    RequestDTO_C0 payload=new RequestDTO_C0();
     String response=null;
    ObjectMapper objMapper=new XmlMapper();
    try {
      payload=objMapper.readValue(receivedPayload, RequestDTO_C0.class);
      System.out.println(payload.toString());
      response=consumeService("http://127.0.0.1:8889/demo/testTranslation",payload);
    }
    catch (Exception e) {
       e.printStackTrace();
    }
    if(response==null) {
        return response;
    }
    else {
      return response;
    }
  }

  public static RequestDTO_P0 requestAdaptor(RequestDTO_C0 payload_C) {
     RequestDTO_P0 payload_P = new RequestDTO_P0();
    String name=payload_C.getname() ;
     String name_P=name;
    payload_P.setn(name_P);
    float temp=payload_C.getvalue().gettemp() ;
    int temp_P=(int)temp;
    payload_P.setv(temp_P);
    String unit=payload_C.getvalue().getunit() ;
     String unit_P=unit;
    payload_P.setu(unit_P);
    return payload_P;
  }

  private static String consumeService(String url, RequestDTO_C0 payload) throws IOException {
     RequestDTO_P0 payloadP= requestAdaptor(payload);
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
    String responseText= " ";
    try {
      uri = new URI("coap://localhost:5555/publish");
    } catch(URISyntaxException e) {
      System.err.println("Invalid URI: " + e.getMessage());
      System.exit(-1);
    }
    CoapClient client= new CoapClient(uri);
    CoapResponse response = null;
    try {
      String payloadS=new ObjectMapper().writeValueAsString(payloadP);
      System.out.println("Payload Sent: " + payloadS);
      response = client.post(payloadS,MediaTypeRegistry.APPLICATION_JSON);
    } catch(ConnectorException|IOException e) {
      System.err.println("Got an error: " + e);
    }
    if(response!=null) {
      System.out.println(response.getCode());
      responseText= response.getResponseText();
      System.out.println(Utils.prettyPrint(response));
    } else {
      System.out.println("No response received.");
    }
    client.shutdown();
    JsonFactory jsonFactory_objMapperP = new JsonFactory();
    ObjectMapper objMapperP=new ObjectMapper(jsonFactory_objMapperP);
    ResponseDTO_P0 responseObj=objMapperP.readValue(responseText,ResponseDTO_P0.class);
    ObjectMapper objMapperC=new XmlMapper();
     String result_out = "";
    result_out= U.jsonToXml(responseText);
    return result_out;
  }
}
