// Auto generated
package eu.arrowhead.client.consumer;

import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.common.Utilities;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.util.List;
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
  public ArrowheadService arrowheadService;

  public ConsumerResources() {
  }

  @GetMapping(
      path= "/test",
      produces = MediaType.TEXT_PLAIN_VALUE
  )
  public String echo() {
    return "Ready to go";
  }

  @PostMapping(
      path = "/generate",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseBody
  public Object offer(@RequestBody final RequestDTO DTO) {
    RequestDTO response= new RequestDTO();
    consumeService("127.0.0.1",8888, "/demo/negotiation/session-data");
    return response;
  }

  public void consumeService(String address, Integer port, String serviceUri) {
    //Adding Import List;
    HttpMethod httpMethod=HttpMethod.POST;
    Arguments OBJArguments = new Arguments(  "name" ,  "value");
    List<Arguments> ListObject=null; 
         ListObject.add(OBJArguments); 
        Contracts OBJContracts = new Contracts(  "TemplatedID" , ListObject);
    RequestDTO OBJRequestDTO = new RequestDTO( 0 ,  "OfferorName" ,  "RecieverName" , OBJContracts);
    final Object offer = arrowheadService.consumeServiceHTTP(Object.class,httpMethod,address,port,serviceUri,"HTTP-INSECURE-JSON",null,OBJRequestDTO,null,null);
    System.out.println(Utilities.toPrettyJson(Utilities.toJson(offer)));
  }
}
