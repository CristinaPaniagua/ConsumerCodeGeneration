// Auto generated
package eu.generator.resources;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.underscore.lodash.U;
import eu.generator.consumer.ResponseDTO_C0;
import eu.generator.provider.ResponseDTO_P0;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.lang.String;
import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaultHandler;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.exception.ConnectorException;

public class RESTResources extends CoapResource {
  public RESTResources() {
     super("publish");
         getAttributes().setTitle("Publish Resource");
  }

  public void handleGET(CoapExchange exchange) {
    System.out.println(exchange.getRequestText());
    String receivedPayload=exchange.getRequestText();
     String response_out=" ";
    ResponseDTO_P0 response=new ResponseDTO_P0();
    JsonFactory jsonFactory_objMapper_Response = new JsonFactory();
    ObjectMapper objMapper_Response=new ObjectMapper(jsonFactory_objMapper_Response);
    try {
      String payload=null;
      response=consumeService("http://127.0.0.1:8889/demo/indoortemperature",payload);
      ResponseDTO_C0 response_C=new ResponseDTO_C0();
       response_C= responseAdaptor(response);
      if(response==null) {
        exchange.respond( "ERROR: EMPTY RESPONSE");
      }
      else {
        response_out= objMapper_Response.writeValueAsString(response_C);
         exchange.respond(CoAP.ResponseCode.CONTENT,response_out,41);
      }
    }
    catch (Exception e) {
       e.printStackTrace();
    }
  }

  public static ResponseDTO_C0 responseAdaptor(ResponseDTO_P0 payload_P) {
     ResponseDTO_C0 payload_C= new ResponseDTO_C0();
    String n=payload_P.getn() ;
     String n_C=n;
    payload_C.setname(n_C);
    float v=payload_P.getv() ;
    int v_C=(int)v;
     eu.generator.consumer.Value value = new  eu.generator.consumer.Value ();
     value.settemp(v_C);
    payload_C.setvalue(value);
    String u=payload_P.getu() ;
     String u_C=u;
     value.setunit(u_C);
    payload_C.setvalue(value);
    return payload_C;
  }

  private static ResponseDTO_P0 consumeService(String url, String payload) throws IOException {
     String payloadP=payload;
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
      uri = new URI("coap://192.168.1.35:5555/weatherstation/indoortemperature");
    } catch(URISyntaxException e) {
      System.err.println("Invalid URI: " + e.getMessage());
      System.exit(-1);
    }
    CoapClient client= new CoapClient(uri);
    CoapResponse response = null;
    try {
      response = client.get();
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
    return responseObj;
  }
}
