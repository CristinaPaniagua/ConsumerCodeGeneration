   /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import com.squareup.javapoet.*;


import generator.codgen.ClassGen;
import static generator.codgen.ClassGenComplex.*;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.lang.model.element.Modifier;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.ws.rs.core.Response;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;



import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.common.CodgenUtil;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.TypeSafeProperties;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.exception.ArrowheadException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import org.springframework.http.HttpMethod;





/**
 *
 * @author cripan
 */
public class Main {

private static InterfaceMetadata MD=null;
private static TypeSafeProperties request = CodgenUtil.getProp("request_config");
private static ArrayList<String> classesRequest= new ArrayList<String>();
private static ArrayList<String> classesResponse= new ArrayList<String>();
    public static void main(String[] args) {
        
        String service = request.getProperty("Service", "null");
        String system="getCars";
        MD = readCDL.read("getDataManufacturer",system);
        System.out.print(MD.toString());
            
        
       if(MD.getRequest()){
           ClassGen Request=new ClassGen();
           ArrayList<String[]> elements_request=MD.elements_request.get(0).getElements();
          
           classesRequest=Request.classGen(elements_request,"RequestDTO");
                     
       }
        
       
        if(MD.getResponse()){
            ClassGen Response=new ClassGen();
            ArrayList<String[]> elements_response=MD.elements_response.get(0).getElements();
            classesResponse=Response.classGen(elements_response,"ResponseDTO"); 
        }
        
        System.out.println("-----");
        
        ConsumerMain ();
        
        if(MD.getProtocol().equalsIgnoreCase("COAP"))
        CoapGenerator.coap(MD);
        
        
       
        
  
        
        //r=complexelement(MD.getElements_response());
                //System.out.println("DONDE:" + r);
        
        //readList(MD.getElements_response());

        //classGen(MD.getElements_request(),"RequestDTO");
        
        System.out.print(new File("config/exe.sh").getAbsoluteFile());
        try {
           ProcessBuilder processBuilder = new ProcessBuilder();
           processBuilder.command("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerGenerationModulesSpring\\CodgenSpring1\\config\\Init.bat");
          
       Process process = processBuilder.start();

		StringBuilder output = new StringBuilder();

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}

		int exitVal = process.waitFor();
		if (exitVal == 0) {
			System.out.println("Success!");
			System.out.println(output);
			System.exit(0);
		} else {
			//abnormal...
		}

	} catch (IOException e) {
		e.printStackTrace();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
        
       
    

    }
   
    
    
    
  
    // METHOD TO CREAT THE METHOD cosumeService//
    

    
     private static   MethodSpec consumeService(){
  
      
    String arg = request.getProperty("Argument", null);
    String protocol=MD.getProtocol();
     MethodSpec.Builder BconsumeService= MethodSpec.methodBuilder("consumeService")
     .addModifiers(Modifier.PRIVATE)
     .addParameter(String.class, "address")
     .addParameter(Integer.class, "port")
     .addParameter(String.class, "serviceUri");
     
    if (protocol.equalsIgnoreCase("HTTP")){
    //CODE FOR HTTP REST
   BconsumeService 
     //.returns(Response.class)
     .addStatement("$T httpMethod=HttpMethod.$N",HttpMethod.class,MD.getMethod());
    
    
    if(MD.getRequest()&&MD.getResponse()){
        
        System.out.println(" Both request and response");
        
        String complextype_request =MD.getComplexType_request();
        String complextype_response =MD.getComplexType_response();
    
    //OBJECT REQUEST
    
    
    for(int i=0; i<classesRequest.size();i++){
                  String s= classesRequest.get(i);
                  System.out.println("Request s: "+s);
                  BconsumeService.addStatement("$L" ,s );
            }      
               
    
    //HTTP REQUEST
    
     if(complextype_request.equals("null") && complextype_response.equals("null")){        
              
         System.out.println("NO complex types in the request or response");
         
            BconsumeService.addStatement("final ResponseDTO $L= arrowheadService.consumeServiceHTTP(ResponseDTO.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",MD.ID) 
            .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
     
            //TODO: CHECK AND DEBUG THIS PART
     }else if(complextype_request.equals("null") && !complextype_response.equals("null")){
         
         String ctype=MD.getComplexType_response();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",t,MD.ID,t)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
     
     }else if(!complextype_request.equals("null") && complextype_response.equals("null")){
         
         String ctype=MD.getComplexType_request();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",t,MD.ID,t)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
     
     }else{
         //TODO
     }
    
    }else{
        
        if(MD.getRequest()){
            
            System.out.println(" Only request ");
             String complextype =MD.getComplexType_request();
             
          System.out.println("size final: "+classesRequest.size());
          for(int i=0; i<classesRequest.size();i++){
                  String s= classesRequest.get(i);
                  System.out.println("s: "+s);
                  BconsumeService.addStatement("$L" ,s );
            }      
               
          
          if(complextype==null){        
              
            BconsumeService.addStatement("final String $L= arrowheadService.consumeServiceHTTP(String.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",MD.ID) 
            .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
     
             }else{
                 String ctype=MD.getComplexType_request();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",t,MD.ID,t)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
                 }
         
            
            
            
        } //if request
    
        if(MD.getResponse()){
          
          System.out.println(" Only request ");
          String complextype =MD.getComplexType_response();
          
         
          if(complextype==null){        
              
            BconsumeService.addStatement("final ResponseDTO $L= arrowheadService.consumeServiceHTTP(ResponseDTO.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,null,null,null)",MD.ID) 
            .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
     
             }else{
                 String ctype=MD.getComplexType_response();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,null,null,null)",t,MD.ID,t)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
                 }
         
            
        }//if Response
    
    }//else Single Case
    
   
     
    }else{
     
      //FOR COAP
     BconsumeService
     //.returns(CoapResponse.class)
     .addStatement("CoapClientGen c= new CoapClientGen()")
     .addStatement("$T coapresponse=c.coapRequest(address+\":\"+port+serviceUri)",CoapResponse.class)  
     .addStatement("System.out.println(coapresponse.getResponseText())");         
    // .addStatement("return coapresponse");
    
    }

    
    
    
  return BconsumeService.build();
        
  }   
  
  
  
  /*
   // METHOD TO CREAT THE METHOD payloadInterpreter//
  private static   MethodSpec payloadInterpreter(){
      
      
      String ReadOutDeclaration= null;
      String GetReadOut=null;
      String ReadOutPrint=null;
      String Representation=null;
      String Import=null;
      
      
       ArrayList<String[]> elements =MD.getElements_request(); //CAREFULLY HERE RESPONSE OR REQUEST?
      
   // ENCONDINGS
  if (MD.getMediatype().equals("TEXT_PLAIN")){
      ReadOutDeclaration= "String readout=null";
       if(MD.getProtocol().equalsIgnoreCase("HTTP")){
      GetReadOut="readout = getResponse.readEntity(String.class)";}
        
      if(MD.getProtocol().equalsIgnoreCase("COAP")){
      GetReadOut="readout = getResponse.getResponseText()";}
        
        ReadOutPrint="readout";
        Representation="String value=readout";
        
  }else if (MD.getMediatype().equals("JSON")&& MD.getProtocol().equalsIgnoreCase("COAP")){ 

      ReadOutDeclaration="JSONObject readout= null";
      GetReadOut="readout = new JSONObject(getResponse.getResponseText())";
      Representation= " String value=readout.getString(\"Humidity\")";
      ReadOutPrint="readout";
      
  }else if (MD.getMediatype().equals("JSON")&& MD.getProtocol().equalsIgnoreCase("HTTP")){ 

      ReadOutDeclaration="JSONObject readout= null";
      GetReadOut="readout = getResponse.readEntity(JSONObject.class)";
      Representation= " String value=readout.getString(\"Humidity\")";
      ReadOutPrint="readout";
      
  }else if(MD.getProtocol().equalsIgnoreCase("HTTP")){
         ReadOutDeclaration="ProviderPayload readout= null";
        GetReadOut="readout = getResponse.readEntity(ProviderPayload.class)";
        Representation= ""+elements.get(0)[1]+" value=readout.get"+elements.get(0)[0]+"()";
         ReadOutPrint="readout";
     }
        
   
  
  ClassName JsonObject = ClassName.get("org.json","JSONOnject");
    String arg = request.getProperty("Argument", null);
  //CODE
  


      MethodSpec.Builder payloadInterpreterM = MethodSpec.methodBuilder("payloadInterpreter")
     .addModifiers(Modifier.PRIVATE);
    
     
     if (MD.getProtocol().equalsIgnoreCase("HTTP")) {
         payloadInterpreterM.addParameter(Response.class, "getResponse");
    } else {
         payloadInterpreterM.addParameter(CoapResponse.class, "getResponse");
    }
     
     
     payloadInterpreterM.addStatement("//Adding Import $T",JSONObject.class)
             
     .addStatement("$L",ReadOutDeclaration)
     .beginControlFlow("try")
     .addStatement("$L", GetReadOut)
     .addStatement("System.out.println(\"Provider Response payload: \" + $L)",ReadOutPrint)     
     .endControlFlow()  
     .beginControlFlow("catch (RuntimeException e)")
     .addStatement("e.printStackTrace()")
     .addStatement("System.out.println(\"Provider did not send the  measurement\")")     
     .endControlFlow() 
     .addStatement("$L",Representation)
    .beginControlFlow("if (value == null)")
     .addStatement("System.out.println(\"Provider did not send any MeasurementEntry.\")")   
     .endControlFlow()  
    .beginControlFlow("else")
     .addStatement("System.out.println(\"The measurement is \" + value )")
     .addStatement("$T label = new JLabel(\"The measurement is \" + value )",JLabel.class) 
     .addStatement("label.setFont(new $T(\"Arial\", Font.BOLD, 18))",Font.class)   
     .addStatement("$T.showMessageDialog(null, label,\"Provider Response\", JOptionPane.INFORMATION_MESSAGE)",JOptionPane.class)   
     .endControlFlow()  
     .build();
 MethodSpec payloadInterpreter=payloadInterpreterM.build();
     
  return payloadInterpreter;
        
  }   
  */
     
//METHOD SPRING RUN 
  
    private static MethodSpec run(){
        
     String Rprotocol;
     if(MD.getProtocol().equalsIgnoreCase("HTTP")){
       Rprotocol="Response";
     } else
        Rprotocol="CoapResponse";   
        
    MethodSpec.Builder Run = MethodSpec.methodBuilder("run")
     .addModifiers(Modifier.PUBLIC)
     .addParameter(ApplicationArguments.class, "providerUrl")
     .addAnnotation(Override.class)
     .addException(Exception.class)
     .addStatement("//Adding Import $T",List.class)
     .addStatement("final $T orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder()",Builder.class)
     .addStatement("final $T requestedService = new ServiceQueryFormDTO()",ServiceQueryFormDTO.class)
     .addStatement("requestedService.setServiceDefinitionRequirement(\"$L\")",MD.ID)
     .addStatement("orchestrationFormBuilder.requestedService(requestedService)\n" +
"    					.flag($T.MATCHMAKING, false)\n" +
"                                            .flag($T.OVERRIDE_STORE, true)\n" +
"                                            .flag($T.TRIGGER_INTER_CLOUD, false)",Flag.class,Flag.class,Flag.class)
      .addStatement("final OrchestrationFormRequestDTO orchestrationRequest = orchestrationFormBuilder.build()")
      .addStatement(" System.out.println(Utilities.toPrettyJson(Utilities.toJson(orchestrationRequest)))")
      .addStatement("$T response = null",OrchestrationResponseDTO.class)
      .beginControlFlow("try")
      .addStatement("response = arrowheadService.proceedOrchestration(orchestrationRequest)")
      .endControlFlow()
      .beginControlFlow("catch ($T e)",ArrowheadException.class)
      .addComment("TO DO")
      .endControlFlow()
      .beginControlFlow("if(response==null||response.getResponse().isEmpty())")
     .addComment("TO DO")
     .endControlFlow() 
     .addStatement("final $T provider=response.getResponse().get(0)",OrchestrationResultDTO.class)
     .addStatement(" System.out.println($T.toPrettyJson(Utilities.toJson(provider)))",Utilities.class)
      .addStatement("String address=provider.getProvider().getAddress()")
      .addStatement("int port=provider.getProvider().getPort()")
      .addStatement("String serviceUri=provider.getServiceUri()")
      .addStatement("consumeService(address, port, serviceUri)",Rprotocol)
     // .addStatement("payloadInterpreter(r)")
      ;
    
    return Run.build();
    
}
  
    
    public static Type getComplexType(String type){
         Type t;
         if(type.equalsIgnoreCase("List")) t=List.class;
    
         //TODO: ADD MORE TYPES
        
         else t=Object.class;
         return t;
     }
    
    
    
  //METHOD TO GENERATE THE MAIN AND CREATE THE MAINCLASS      
     private static void ConsumerMain (){
            MethodSpec consumeService=consumeService();
   
  
       
     //MethodSpec payloadInterpreter =payloadInterpreter();
     MethodSpec run =run();
     String Rprotocol;
     if(MD.getProtocol().equalsIgnoreCase("HTTP")){
       Rprotocol="Response";
     } else
        Rprotocol="CoapResponse";
     
     MethodSpec main = MethodSpec.methodBuilder("main")
    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .returns(void.class)
    .addParameter(String[].class, "args")
    .addStatement("$T.run(ConsumerMain.class, args)",SpringApplication.class)
    .build();
         
         
         AnnotationSpec Component = AnnotationSpec
              .builder(ComponentScan.class)
               .addMember("basePackages", "{$T.BASE_PACKAGE}",CommonConstants.class)
               .build();
         
        TypeSpec ConsumerMain = TypeSpec.classBuilder("ConsumerMain")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(SpringBootApplication.class)
                .addAnnotation(Component)
                .addSuperinterface(ApplicationRunner.class) 
                .addField( FieldSpec.builder(ArrowheadService.class, "arrowheadService")
                  .addAnnotation(Autowired.class)
                  .addModifiers(Modifier.PUBLIC)
                  .build())
                .addMethod(main)
                .addMethod(run)
                .addMethod(consumeService)
                .build();
 
  
        JavaFile javaFile2 = JavaFile.builder("eu.arrowhead.client.consumer",ConsumerMain)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\TesterSpring\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        
     }
        
        
        
         
}

    

