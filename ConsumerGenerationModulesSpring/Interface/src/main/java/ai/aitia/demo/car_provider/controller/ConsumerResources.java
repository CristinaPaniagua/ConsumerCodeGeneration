// Auto generated
package ai.aitia.demo.car_provider.controller;

import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.common.Utilities;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ConsumerResources {
  @Autowired
  public ArrowheadService arrowheadService;

 
  
  @GetMapping(
      path= "/test",
      produces = MediaType.TEXT_PLAIN_VALUE
  )
  public String echo() {
    return "Ready to go";
  }

  @PostMapping(
      path = "demo/negotiation/session-data",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public String offer(@RequestBody final RequestDTO DTO) {
   Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       // System.out.println(timestamp);
        long tstart =timestamp.getTime();
     //System.out.println("Request Payload:"+Utilities.toJson(DTO));
    String response=consumeService("127.0.0.1",8889, "/demo/negotiation/session-data",DTO);
    if(response==null){
        return "ERROR: EMPTY RESPONSE";
    }else{
        Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
        //System.out.println(timestampEnd);
        long tend = timestampEnd.getTime();
         
        System.out.println(tend-tstart);    
        
    return "provider response:"+response;}
  }

  public String consumeService(String address, Integer port, String serviceUri,RequestDTO OBJRequestDTO) {
  HttpMethod httpMethod=HttpMethod.POST;
     //System.out.println("ConsumeService...");
    
    /*Arguments OBJArguments = new Arguments("name" ,"value");
    // System.out.println("Arguments:"+OBJArguments.toString());
    List<Arguments> ListObject= new ArrayList<Arguments>(); 
         ListObject.add(OBJArguments); 
        Contracts OBJContracts = new Contracts("TemplatedID" , ListObject);
         //System.out.println("Contracts:"+OBJContracts.toString());
    RequestDTO OBJRequestDTO = new RequestDTO( 0 ,  "OfferorName" ,  "RecieverName" , OBJContracts);
    System.out.println("Payload:"+Utilities.toJson(OBJRequestDTO));
*/
    String offer="";
    //System.out.println(address+", "+port+", "+serviceUri);
    offer = arrowheadService.consumeServiceHTTP(String.class,httpMethod,address,port,serviceUri,"HTTP-INSECURE-JSON",null,OBJRequestDTO,null,null);
    System.out.println(offer);
    return offer;
  }
}
