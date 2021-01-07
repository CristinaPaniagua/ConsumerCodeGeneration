// Auto generated
package eu.arrowhead.client.consumer;

import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFlags;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.exception.ArrowheadException;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@ComponentScan(
    basePackages = {CommonConstants.BASE_PACKAGE}
)
public class ConsumerMain implements ApplicationRunner {
  @Autowired
  public ArrowheadService arrowheadService;

  public static void main(String[] args) {
    SpringApplication.run(ConsumerMain.class, args);
  }

  @Override
  public void run(ApplicationArguments providerUrl) throws Exception {
    //Adding Import List;
    final OrchestrationFormRequestDTO.Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
    final ServiceQueryFormDTO requestedService = new ServiceQueryFormDTO();
    requestedService.setServiceDefinitionRequirement("offer");
    orchestrationFormBuilder.requestedService(requestedService)
            					.flag(OrchestrationFlags.Flag.MATCHMAKING, false)
                                                    .flag(OrchestrationFlags.Flag.OVERRIDE_STORE, true)
                                                    .flag(OrchestrationFlags.Flag.TRIGGER_INTER_CLOUD, false);
    final OrchestrationFormRequestDTO orchestrationRequest = orchestrationFormBuilder.build();
     System.out.println(Utilities.toPrettyJson(Utilities.toJson(orchestrationRequest)));
    OrchestrationResponseDTO response = null;
    try {
      response = arrowheadService.proceedOrchestration(orchestrationRequest);
    }
    catch (ArrowheadException e) {
      // TO DO
    }
    if(response==null||response.getResponse().isEmpty()) {
      // TO DO
    }
    final OrchestrationResultDTO provider=response.getResponse().get(0);
     System.out.println(Utilities.toPrettyJson(Utilities.toJson(provider)));
    String address=provider.getProvider().getAddress();
    int port=provider.getProvider().getPort();
    String serviceUri=provider.getServiceUri();
    consumeService(address, port, serviceUri);
  }

  private void consumeService(String address, Integer port, String serviceUri) {
    HttpMethod httpMethod=HttpMethod.POST;
    //Import ArrayList;
    Arguments OBJArguments = new Arguments(  "name" ,  "value");
    List<Arguments> ListObject=new ArrayList<>(); 
         ListObject.add(OBJArguments); 
        Contracts OBJContracts = new Contracts(  "TemplatedID" , ListObject);
    RequestDTO0 OBJRequestDTO0 = new RequestDTO0( 0 ,  "OfferorName" ,  "RecieverName" , OBJContracts);
    final String offer0= arrowheadService.consumeServiceHTTP(String.class,httpMethod,address,port,ServicePath,"HTTP-INSECURE-JSON",null,OBJRequestDTO0,null,null);
    System.out.println(Utilities.toPrettyJson(Utilities.toJson(offer0)));
  }
}
