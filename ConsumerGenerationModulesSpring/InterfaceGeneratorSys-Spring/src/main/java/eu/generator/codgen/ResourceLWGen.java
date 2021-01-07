/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.generator.codgen;

/**
 *
 * @author cripan
 */




import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.underscore.lodash.U;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import static eu.generator.codgen.EncodingParser.createObjectMapper;
import eu.generator.resources.RequestDTO_C0;
import java.io.IOException;
import java.nio.file.Paths;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.lang.model.element.Modifier;
import javax.ws.rs.GET;;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import eu.generator.resources.ResponseDTO_P0;
public class ResourceLWGen {
    
    
    
      public static MethodSpec testEcho (){
          
     AnnotationSpec produces= AnnotationSpec
                 .builder(Produces.class)
                 .addMember("value", "$T.TEXT_PLAIN", MediaType.class)
                 .build();
     
     /*
     AnnotationSpec consumes= AnnotationSpec
                 .builder(Consumes.class)
                 .addMember("value", "MediaType.APPLICATION_JSON")
                 .build();
     
     */
       AnnotationSpec path= AnnotationSpec
                 .builder(Path.class)
                 .addMember("value", "$S", "/test")
                 .build();
         
     MethodSpec.Builder methodgen = MethodSpec.methodBuilder("echo")
     .addModifiers(Modifier.PUBLIC)
     .returns(String.class)
     .addAnnotation(GET.class)
     .addAnnotation(path)
     .addAnnotation(produces)
     .addStatement("return \"Ready to go\"");
   
          return methodgen.build();
     }
    
    public static MethodSpec methodgen (InterfaceMetadata MD_C,InterfaceMetadata MD_P ){
 
    
    String pathP=MD_P.getPathResource();
    
    
    
    
     AnnotationSpec produces;
     
     
        if(MD_C.getMediatype_response().equalsIgnoreCase("JSON")){
       
         produces= AnnotationSpec
                 .builder(Produces.class)
                 .addMember("value", "$T.APPLICATION_JSON", MediaType.class)
                 .build();
        
    }else if(MD_C.getMediatype_response().equalsIgnoreCase("XML")){
        produces= AnnotationSpec
                 .builder(Produces.class)
                 .addMember("value", "$T.APPLICATION_XML", MediaType.class)
                 .build();
    }else if(MD_C.getMediatype_response().equalsIgnoreCase("CBOR")){
        produces= AnnotationSpec
                 .builder(Produces.class)
                 .addMember("value", "\"application/cbor\"")
                 .build();
    }else{
          produces= AnnotationSpec
                 .builder(Produces.class)
                 .addMember("value", "\"*/*\"", MediaType.class)
                 .build();
    } 
     
     
     
    
          String Mediatype="";
    if(MD_C.getMediatype_request().equalsIgnoreCase("JSON")){
        Mediatype="APPLICATION_JSON";
        
    }else if(MD_C.getMediatype_request().equalsIgnoreCase("XML")){
        Mediatype="APPLICATION_XML";
    }
    
     
     
     AnnotationSpec consumes;
     if(MD_C.getMediatype_request().equalsIgnoreCase("CBOR")){
             consumes= AnnotationSpec
                 .builder(Consumes.class)
                 .addMember("value", "\"application/cbor\"")
                 .build(); 
     }else{
         consumes= AnnotationSpec
                 .builder(Consumes.class)
                 .addMember("value", "$T.$L", MediaType.class,Mediatype)
                 .build(); 
     }
         
     String pathResource=MD_C.getPathResource();
       AnnotationSpec path= AnnotationSpec
                 .builder(Path.class)
                 .addMember("value", "$S", pathResource)
                 .build();
         
     MethodSpec.Builder methodgen = MethodSpec.methodBuilder(MD_C.getID())
     .addModifiers(Modifier.PUBLIC)
     .returns(String.class);
     
     //TODO: ADD THE REST OF THE METHODS (PUT AND DELETE)
     if(MD_C.getMethod().equalsIgnoreCase("POST")){
         methodgen.addAnnotation(POST.class);
     }else if(MD_C.getMethod().equalsIgnoreCase("GET"))  methodgen.addAnnotation(GET.class);
     
     CodeBlock mapperCode=createObjectMapper(MD_C.getMediatype_request(),"objMapper");
     
      methodgen.addAnnotation(path)
     .addAnnotation(produces)
     .addAnnotation(consumes);
      if(MD_C.getMediatype_request().equalsIgnoreCase("CBOR")){
          methodgen.addParameter(byte[].class,"receivedPayload");
      }else{
           methodgen.addParameter(String.class,"receivedPayload");
      }
       methodgen 
     .addStatement("System.out.println(\"Consumer Payload: \"+ receivedPayload)")
     .addStatement("RequestDTO_C0 payload=new RequestDTO_C0()")
     .addStatement(" String response=null")
     .addCode(mapperCode)
     .beginControlFlow("try")
      .addStatement("payload=objMapper.readValue(receivedPayload, RequestDTO_C0.class)")
      //.addStatement("System.out.println(payload.toString())")
      .addStatement("response=consumeService(\"http://127.0.0.1:8889/demo$L\",payload)",pathP)
     .endControlFlow()
     .beginControlFlow("catch ($T e)",Exception.class)      
     .addStatement(" e.printStackTrace()")
     .endControlFlow() 
     .beginControlFlow("if(response==null)")
     .addStatement("  return \"ERROR: EMPTY RESPONSE\"")
     .endControlFlow() 
     .beginControlFlow("else")
     .addStatement("return response")
     .endControlFlow() 
     ;
   
          return methodgen.build();
     }
    
     public static MethodSpec consumeService (InterfaceMetadata MD_C, InterfaceMetadata MD_P){
         
     MethodSpec.Builder consumer = MethodSpec.methodBuilder("consumeService")
     .addModifiers(Modifier.PRIVATE)
     .addModifiers(Modifier.STATIC)
     .returns(String.class)
     .addParameter(String.class, "url")
     .addParameter(RequestDTO_C0.class, "payload")
     .addException(IOException.class);
         
     
      consumer
     .addStatement("$T httpClient = $T.createDefault()",CloseableHttpClient.class,HttpClients.class)
     .addStatement(" String result_in = \"\"") 
     .addStatement(" String result_out = \"\"") 
     .beginControlFlow("try");
      //TODO: ADD THE REST OF THE METHODS (PUT AND DELETE)
     if(MD_P.getMethod().equalsIgnoreCase("POST")){
         consumer.addStatement(" $T request = new $T(url)",HttpPost.class,HttpPost.class);
     }else if(MD_P.getMethod().equalsIgnoreCase("GET"))
       consumer.addStatement(" $T request = new $T(url)",HttpGet.class,HttpGet.class);
     
     String Mediatype="";
      if(MD_P.getMediatype_request().equalsIgnoreCase("JSON")){
       consumer.addStatement(" request.addHeader(\"content-type\", \"application/json\")") 
       .addStatement("String createdPayload=new $T().writeValueAsString(payload)",ObjectMapper.class);
       
        
    }else if(MD_P.getMediatype_request().equalsIgnoreCase("XML")){
        consumer.addStatement(" request.addHeader(\"content-type\", \"application/xml\")") 
       .addStatement("$T xmlMapper = new XmlMapper()",XmlMapper.class)
       .addStatement("String createdPayload=xmlMapper.writeValueAsString(payload)",ObjectMapper.class);
      
    
      }else if(MD_P.getMediatype_request().equalsIgnoreCase("CBOR")){
        consumer.addStatement(" request.addHeader(\"content-type\", \"pplication/cbor\")") 
       .addStatement("$T f = new CBORFactory()",CBORFactory.class)
       .addStatement("$T cborMapper = new ObjectMapper(f)",ObjectMapper.class)
       .addStatement("byte[] createdPayload = cborMapper.writeValueAsBytes(payload)");
      
    }
       
        consumer.addStatement(" System.out.println(\"Payload: \"+ createdPayload )");
                
       if(MD_P.getMediatype_request().equalsIgnoreCase("CBOR")){
            consumer.addStatement("$T eb = EntityBuilder.create()",EntityBuilder.class)
                    .addStatement("eb.setBinary(createdPayload)")
                    .addStatement("request.setEntity(eb.build())");
       }else{
                
       consumer.addStatement(" request.setEntity(new $T(createdPayload))",StringEntity.class);  
               
               } 

            CodeBlock mapperResponseProvider=createObjectMapper(MD_P.getMediatype_response(),"objMapperP");
            CodeBlock mapperResponseConsumer=createObjectMapper(MD_C.getMediatype_response(),"objMapperC");
           
       consumer.addStatement(" $T response = httpClient.execute(request)",CloseableHttpResponse.class) 
       .beginControlFlow("try")  
            .addStatement(" $T entity = response.getEntity()", HttpEntity.class)
            .addCode(mapperResponseProvider)
            .addStatement("$T contentType = ContentType.getOrDefault(entity)",ContentType.class)
            .addStatement("String mimeType = contentType.getMimeType()") 
            .addStatement("System.out.println(\"Response MediaType: \"+mimeType)")
            .beginControlFlow("if (entity != null)")
            .addStatement(" result_in = $T.toString(entity)",EntityUtils.class)
            .addStatement("$T responseObj=objMapperP.readValue(result_in,ResponseDTO_P0.class)",ResponseDTO_P0.class)
            .addCode(mapperResponseConsumer)
            .addStatement("result_out=objMapperC.writeValueAsString(responseObj)");
            
       Boolean Payload=true;
       if(Payload==false){
            if(MD_C.getMediatype_response().equalsIgnoreCase("XML") && MD_P.getMediatype_response().equalsIgnoreCase("JSON")){
                 consumer.addStatement("result_out= $T.jsonToXml(result_in)",U.class);
             }else if(MD_C.getMediatype_response().equalsIgnoreCase("JSON") && MD_P.getMediatype_response().equalsIgnoreCase("XML")){
                  consumer.addStatement("result_out= $T.xmlToJson(result_in)",U.class);
            }else{
                  consumer.addStatement("result_out=result_in");
            }
       }
       
      
            
            consumer.addStatement("System.out.println(\"Response In: \"+ result_in)")
            .addStatement("System.out.println(\"Response Out: \"+ result_out)")
            .endControlFlow() //TODO: ADD ELSE 
       .endControlFlow() 
       .beginControlFlow("finally")
        .addStatement(" response.close()")
        .endControlFlow() 
      .endControlFlow() 
      .beginControlFlow("finally")
        .addStatement("httpClient.close()")
      .endControlFlow() 
      .addStatement("return result_out"); 
     
      
     
   
     
      return consumer.build();
     }
    
     public static void ResourcesLWGen (InterfaceMetadata MD_C, InterfaceMetadata MD_P ){
         
  
       MethodSpec testEcho=  testEcho();
       MethodSpec methodgen=methodgen(MD_C, MD_P);
       MethodSpec consumeService =consumeService(MD_C,MD_P); 
        
        
        
      AnnotationSpec path= AnnotationSpec
                 .builder(Path.class)
                 .addMember("value", "$S", "/demo")
                 .build();
      
      
             
     TypeSpec.Builder classGen =TypeSpec.classBuilder("RESTResources")
              .addAnnotation(path)
              .addModifiers(Modifier.PUBLIC)
               .addMethod(testEcho)
             .addMethod(methodgen)
             .addMethod(consumeService);
     
     
     
        TypeSpec RclassGen  = classGen.build();

               
 
  
        JavaFile javaFile2 = JavaFile.builder("eu.generator.resources",RclassGen)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\InterfaceLightweight\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        
     }
    
    
    
}
