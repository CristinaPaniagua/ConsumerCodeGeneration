/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import eu.arrowhead.client.library.ArrowheadService;
import eu.arrowhead.common.CodgenUtil;
import eu.arrowhead.common.TypeSafeProperties;
import eu.arrowhead.common.Utilities;
import static generator.codgen.Main.getComplexType;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author cripan
 */
public class ResourceGen {
    
  
   private static TypeSafeProperties request = CodgenUtil.getProp("request_config");
     public static MethodSpec  constructor (){
        
     MethodSpec consructor = MethodSpec.constructorBuilder()
     .addModifiers(Modifier.PUBLIC)
    .build();
     
   
     
    
     return consructor;
    }
     
   public static MethodSpec testEcho (){
         AnnotationSpec mapper= AnnotationSpec
                 .builder(GetMapping.class)
                 .addMember("value", "\"/test\"")
                 .addMember("produces","$T.TEXT_PLAIN_VALUE",MediaType.class)
                 .build();
         
     MethodSpec.Builder methodgen = MethodSpec.methodBuilder("echo")
     .addModifiers(Modifier.PUBLIC)
     .returns(String.class)
     .addAnnotation(mapper)
     .addStatement("return \"Ready to go\"");
   
          return methodgen.build();
     }
     
      public static MethodSpec methodgen  (InterfaceMetadata MD ){
        AnnotationSpec mapper;
     if(MD.Method.equalsIgnoreCase("POST")){
           mapper = AnnotationSpec
              .builder(PostMapping.class)
               .addMember("path","\"/generate\"")
               .addMember("consumes","$T.APPLICATION_JSON_VALUE",MediaType.class)
               .addMember("produces","$T.APPLICATION_JSON_VALUE",MediaType.class)
               .build();    
     }else{
           mapper = AnnotationSpec
              .builder(PostMapping.class)
               //.addMember("interface_URI","\"http://localhost:8080/interface\"")
               .build();    
     }     
     
          
          
     MethodSpec.Builder methodgen = MethodSpec.methodBuilder(MD.ID)
     .addModifiers(Modifier.PUBLIC)
    // .returns(TypeVariableName.get("ResponseDTO"))
             .returns(Object.class)
     .addAnnotation(mapper)
      .addAnnotation(ResponseBody.class);
     
     ParameterSpec param = ParameterSpec.builder(TypeVariableName.get("RequestDTO"),"DTO",Modifier.FINAL)
             .addAnnotation(RequestBody.class)
             .build();
     if(MD.Method.equalsIgnoreCase("POST")){
     
      methodgen.addParameter(param)
              .addStatement("$T response= new $T()",TypeVariableName.get("RequestDTO"),TypeVariableName.get("RequestDTO"))
              .addStatement("consumeService(\"address\",8000,\" serviceUri\")")//TODO: RETRIEVE THAT DATA
              .addStatement("return response");
                
     }

     
     
      
     return methodgen.build();
    }
     
     
    // METHOD TO CREAT THE METHOD cosumeService//
  private static   MethodSpec consumeService( ArrayList<String> classesDummy,  InterfaceMetadata MD ){


      
    String arg = request.getProperty("Argument", null);
    String protocol=MD.getProtocol();
     MethodSpec.Builder BconsumeService= MethodSpec.methodBuilder("consumeService")
     .addModifiers(Modifier.PUBLIC)
     .addParameter(String.class, "address")
     .addParameter(Integer.class, "port")
     .addParameter(String.class, "serviceUri")
     .addStatement("//Adding Import $T",List.class);
     
    if (protocol.equalsIgnoreCase("HTTP")){
    //CODE FOR HTTP REST
   BconsumeService 
     //.returns(Response.class)
     .addStatement("$T httpMethod=HttpMethod.$N",HttpMethod.class,MD.getMethod());
    String complextype =MD.getComplexType_response();
   if(MD.getMethod().equalsIgnoreCase("GET")){
        for(int i=0; i<classesDummy.size();i++){
          String s= classesDummy.get(i);
          System.out.println("s: "+s);
          BconsumeService.addStatement("$L",s );
      }
       
        
         if(complextype.equals(null)){  
                         
          BconsumeService.addStatement("final ResponseDTO getResponse = arrowheadService.consumeServiceHTTP(ResponseDTO.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)")
          .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson(getResponse)))",Utilities.class);  
           }else{
             
             Type t=getComplexType(complextype);
            BconsumeService.addStatement("final $T carCreated = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",t,t) 
     .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson(carCreated)))",Utilities.class);   
          
         }
          
    }else{
       //String s=" ";
        System.out.println("dummysize final: "+classesDummy.size());
      for(int i=0; i<classesDummy.size();i++){
            String s= classesDummy.get(i);
          System.out.println("s: "+s);
          BconsumeService.addStatement("$L" ,s );
         
      }
          //s=s.substring(0, s.length()-1);
          
        
       
      
              
       if(MD.elements_response.equals("null")){        
              
     BconsumeService.addStatement("final ResponseDTO $L= arrowheadService.consumeServiceHTTP(ResponseDTO.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",MD.ID) 
     .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
     
       }else{
           String ctype=MD.getComplexType_response();
            Type t=getComplexType(ctype);
            BconsumeService.addStatement("final $T $L = arrowheadService.consumeServiceHTTP($T.class,httpMethod,address,port,serviceUri,\"HTTP-INSECURE-JSON\",null,OBJRequestDTO,null,null)",t,MD.ID,t)
     .addStatement("System.out.println($T.toPrettyJson(Utilities.toJson($L)))",Utilities.class,MD.ID);   
       }
   }
     
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
    
    
     public static void ResourcesGen (InterfaceMetadata MD,  ArrayList<String> classesRequest){
         
  

        
   
        MethodSpec constructor= constructor();
         MethodSpec testEcho=  testEcho();
        MethodSpec methodgen=methodgen(MD);
        MethodSpec consumeService =consumeService(classesRequest, MD); 
        
        
        //TO DO : SOLVE THIS, DOESNOT WORK
      AnnotationSpec URI = AnnotationSpec
              .builder(RequestMapping.class)
               //.addMember("interface_URI","\"http://localhost:8080/interface\"")
               .build();   
        
     TypeSpec.Builder classGen =TypeSpec.classBuilder("ConsumerResources")
             .addAnnotation(RestController.class)
             .addAnnotation(URI)
              .addModifiers(Modifier.PUBLIC)
             .addField( FieldSpec.builder(ArrowheadService.class, "arrowheadService")
                  .addModifiers(Modifier.PUBLIC)
                  .build())
             .addMethod(constructor)
             .addMethod(testEcho)
             .addMethod(methodgen)
             .addMethod(consumeService);
        
        TypeSpec RclassGen  = classGen.build();

               
 
  
        JavaFile javaFile2 = JavaFile.builder("eu.arrowhead.client.consumer",RclassGen)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCode-Generation\\ConsumerGenerationModulesSpring\\Interface\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        
     }
}
