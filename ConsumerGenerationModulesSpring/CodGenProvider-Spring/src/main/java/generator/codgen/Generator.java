   /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import ai.aitia.demo.car_provider.controller.RequestForm;
import com.squareup.javapoet.*;



import java.awt.Font;
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

import java.lang.reflect.Type;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;





/**
 *
 * @author cripan
 */
public class Generator {

private static InterfaceMetadata MD=null;
private static TypeSafeProperties request = CodgenUtil.getProp("request_config");
private static ArrayList<ArrayList<String>> classesRequest= new ArrayList<ArrayList<String>>();
private static ArrayList<String> classesResponse= new ArrayList<String>();

   public static void startGeneration(RequestForm rf){
     System.out.print("CODGEGEN START********************************************************* \n");   
        
     
     
        String service = rf.getRequestedService();
        String system="providerTest";
        MD = readCDL.read(service,system);
        System.out.print(MD.toString());
        
         if(MD.getRequest()){
           ClassGen Request=new ClassGen();
        
        ArrayList<String[]> elements_request=new ArrayList<>();
             for(int i=0; i<MD.elements_request.size();i++)
             {
               elements_request.clear();
               elements_request=MD.elements_request.get(i).getElements();
               classesRequest.add(Request.classGen(elements_request,"RequestDTO"+i));
               
                 
             }
            
            //for(int h=0;h<classesRequest.size();h++)
                //System.out.println("..........."+classesRequest.get(h));
  
       }
        
       
        if(MD.getResponse()){
            ClassGen Response=new ClassGen();
      
                 for(int j=0; j<MD.elements_response.size();j++)
                 {
                    ArrayList<String[]> elements_response=MD.elements_response.get(j).getElements();
                    classesResponse=Response.classGen(elements_response,"ResponseDTO"+j);
                 }
           
        }
        
        
        
        ConsumerMain ();
        
        clean();
       
        
        if(MD.getProtocol().equalsIgnoreCase("COAP"))
        CoapGenerator.coap(MD);
        
        
        
        
        try{
        execute();
        } catch (IOException e) {
		e.printStackTrace();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
    

    }
   
  
   //EXECUTE BAT
   
   public static void execute() throws InterruptedException, IOException {
        
       
       ExecutorService executor = Executors.newSingleThreadExecutor();
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command(("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\CodGenProvider-Spring\\config\\Init.bat"));

        try {

            Process process = processBuilder.start();

            executor.submit(new ProcessTask(process.getInputStream()));
           

           
        } finally {
            executor.shutdown();
        }
    

   }
   
   private static class ProcessTask implements Callable<List<String>> {

        private InputStream inputStream;

        public ProcessTask(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public List<String> call() {
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.toList());
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
    
    CodeBlock consumeService=ServiceCode("OBJRequestDTO","OBJResponseDTO",0);
   BconsumeService 
     //.returns(Response.class)
     .addStatement("$T httpMethod=HttpMethod.$N",HttpMethod.class,MD.getMethod())
     .addCode(consumeService);
    
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
  
     
     
 //CONSUM SERVICE WITH PARAMETERS: 
     
// METHOD TO CREAT THE METHOD cosumeService//
    

    
 private static   MethodSpec consumeServiceParameters(){
  
      
    String arg = request.getProperty("Argument", null);
    String protocol=MD.getProtocol();
     MethodSpec.Builder BconsumeService= MethodSpec.methodBuilder("consumeService")
     .addModifiers(Modifier.PRIVATE)
     .addParameter(String.class, "address")
     .addParameter(Integer.class, "port")
     .addParameter(String.class, "serviceUri");
     
    if (protocol.equalsIgnoreCase("HTTP")){
    //CODE FOR HTTP REST
    
    //CommandLineScann
    BconsumeService 
    .beginControlFlow("while(true)")
    .addStatement("final Scanner sc = new Scanner(System.in)")
    .addStatement("String path=commandLineUI(sc)")
    ;
    
        
    
    //CODE PARAMETERS: 
   
     BconsumeService 
     .addStatement("$T httpMethod=HttpMethod.$N",HttpMethod.class,MD.getMethod())
      .addStatement(" String ServicePath=\"\"")
      .beginControlFlow("switch(path)");
    //System.out.println("Subpath size:"+ MD.subpaths.size()); 
    
    for(int m=0; m<MD.subpaths.size();m++){
        
        String subpath=MD.subpaths.get(m);
        CodeBlock optionCase=ServiceCode("OBJRequestDTO"+m,"OBJResponseDTO"+m,m);
        BconsumeService           
        .addCode("case \"$L\": \n",subpath)
        .addStatement("ServicePath=serviceUri+\"$L\"",subpath)
        .addCode(optionCase)
        .addStatement("break");
    }
        
    
     BconsumeService.addCode("default: \n")
     .addStatement("//TODO")
     .endControlFlow()
     .endControlFlow();
    
 
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
     
 //BLOCK OF CODE FOR THE CONSUME SERVICE METHOD (REQUEST-RESPONSE-REQUESTHTTP)
 private static CodeBlock ServiceCode(String RequestDTO, String ResponseDTO, int index){
     
   String arg = request.getProperty("Argument", null);
    String protocol=MD.getProtocol();
     
    CodeBlock.Builder BconsumeService  = CodeBlock.builder()
            .addStatement ("//Import $T",ArrayList.class);
           
    
     
    
    if(MD.getRequest()&&MD.getResponse()){
        
        //System.out.println(" Both request and response");
        
        String complextype_request =MD.getComplexType_request();
        String complextype_response =MD.getComplexType_response();
        if(complextype_request==null) complextype_request="null";
        if(complextype_response==null) complextype_response="null";
    
    //OBJECT REQUEST
    
     ArrayList<String> ValueRequest=classesRequest.get(index);
    
        
                for(int j=0; j<ValueRequest.size();j++){
                     String s= ValueRequest.get(j);
                     //System.out.println("Request s: "+s);
                     BconsumeService.addStatement("$L" ,s ); 
                }     
               
    
    //HTTP REQUEST
    
     if(complextype_request.equals("null") && complextype_response.equals("null")){        
              
        
         
            BconsumeService.addStatement("final ResponseDTO$L $L$L= arrowheadService.consumeServiceHTTP(ResponseDTO$L.class,httpMethod,address,port,ServicePath,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO$L,null,null)",index,MD.ID,index,index,index) 
            .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L$L)))",Utilities.class,MD.ID,index);   
     
            //TODO: CHECK AND DEBUG THIS PART
     }else if(complextype_request.equals("null") && !complextype_response.equals("null")){
         
         String ctype=MD.getComplexType_response();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L$L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,ServicePath,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO$L,null,null)",t,MD.ID,index,t,index)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L$L)))",Utilities.class,MD.ID,index);   
     
     }else if(!complextype_request.equals("null") && complextype_response.equals("null")){ //TODO: CHECK THIS CASE: PROBABLY IT DOES NOT WORK
         
         String ctype=MD.getComplexType_request();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L$L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,ServicePath,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO$L,null,null)",t,MD.ID,index,t,index)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L$L)))",Utilities.class,MD.ID,index);   
     
     }else{
         //TODO
     }
    
    }else{
        
        if(MD.getRequest()){
            
            //System.out.println(" Only request ");
             String complextype =MD.getComplexType_request();
             
          
         ArrayList<String> ValueRequest=classesRequest.get(index);
    
        
                for(int j=0; j<ValueRequest.size();j++){
                     String s= ValueRequest.get(j);
                    // System.out.println("Request s: "+s);
                     BconsumeService.addStatement("$L" ,s ); 
                }
               
               
          
          if(complextype==null){        
              
            BconsumeService.addStatement("final String $L$L= arrowheadService.consumeServiceHTTP(String.class,httpMethod,address,port,ServicePath,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO$L,null,null)",MD.ID,index,index) 
            .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L$L)))",Utilities.class,MD.ID,index);   
     
             }else{
                 String ctype=MD.getComplexType_request();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L$L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,ServicePath,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO$L,null,null)",t,MD.ID,index,t,index)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L$L)))",Utilities.class,MD.ID,index);   
                 }
         
            
            
            
        } //if request
    
        if(MD.getResponse()){
          
         // System.out.println(" Only response ");
          String complextype =MD.getComplexType_response();
          
         
          if(complextype==null){        
              
            BconsumeService.addStatement("final ResponseDTO$L $L$L= arrowheadService.consumeServiceHTTP(ResponseDTO$L.class,httpMethod,address,port,ServicePath,\"HTTP-INSECURE-JSON\",null,null,null,null)",index,MD.ID,index,index) 
            .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L$L)))",Utilities.class,MD.ID,index);   
     
             }else{
                 String ctype=MD.getComplexType_response();
                  Type t=getComplexType(ctype);
                  BconsumeService.addStatement("final $T $L$L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,ServicePath,\"HTTP-INSECURE-JSON\",null,null,null,null)",t,MD.ID,index,t)
                .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L$L)))",Utilities.class,MD.ID,index);   
                 }
         
            
        }//if Response
    
    }//else Single Case
    
   
   
    

    
       CodeBlock consumeService=BconsumeService.build();
     
     
     return consumeService;
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
  MethodSpec consumeService;
          if(MD.param){
              consumeService=consumeServiceParameters();
        }else consumeService=consumeService();
         
           
        MethodSpec commandLine =UtilGen.commandLine();    
  
       
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
         
        TypeSpec.Builder BConsumerMain = TypeSpec.classBuilder("ConsumerMain")
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
                .addMethod(consumeService);
        if(MD.param)
               BConsumerMain.addMethod(commandLine);
        
        
        TypeSpec ConsumerMain=BConsumerMain.build();
                
        
  
        JavaFile javaFile2 = JavaFile.builder("eu.arrowhead.client.consumer",ConsumerMain)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\Generated_consumer\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        
     }
 
 public static void clean(){
    MD.elements_request.clear();
 }
        
        
         
}

    

