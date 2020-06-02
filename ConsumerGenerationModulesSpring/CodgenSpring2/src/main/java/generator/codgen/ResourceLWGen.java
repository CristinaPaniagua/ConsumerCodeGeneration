/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

/**
 *
 * @author cripan
 */




import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import static generator.codgen.EncodingParser.createObjectMapper;
import resources.RequestDTO_C0;
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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
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
    
    public static MethodSpec methodgen (InterfaceMetadata MD_C){
        String Mediatype="";
    if(MD_C.getMediatype().equalsIgnoreCase("JSON")){
        Mediatype="APPLICATION_JSON";
        
    }else if(MD_C.getMediatype().equalsIgnoreCase("XML")){
        Mediatype="APPLICATION_XML";
    }
    
     AnnotationSpec produces= AnnotationSpec
                 .builder(Produces.class)
                 .addMember("value", "$T.APPLICATION_JSON", MediaType.class)
                 .build();
     AnnotationSpec consumes;
     if(MD_C.getMediatype().equalsIgnoreCase("CBOR")){
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
     
     CodeBlock mapperCode=createObjectMapper(MD_C.getMediatype());
     
      methodgen.addAnnotation(path)
     .addAnnotation(produces)
     .addAnnotation(consumes);
      if(MD_C.getMediatype().equalsIgnoreCase("CBOR")){
          methodgen.addParameter(byte[].class,"receivedPayload");
      }else{
           methodgen.addParameter(String.class,"receivedPayload");
      }
       methodgen 
     .addStatement("RequestDTO_C0 payload=new RequestDTO_C0()")
     .addStatement(" String response=null")
     .addCode(mapperCode)
     .beginControlFlow("try")
      .addStatement("payload=objMapper.readValue(receivedPayload, RequestDTO_C0.class)")
      .addStatement("System.out.println(payload.toString())")
      .addStatement("response=consumeService(\"http://127.0.0.1:8889/demo/testTranslation\",payload)")
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
    
     public static MethodSpec consumeService (InterfaceMetadata MD_P){
         
     MethodSpec.Builder consumer = MethodSpec.methodBuilder("consumeService")
     .addModifiers(Modifier.PRIVATE)
     .addModifiers(Modifier.STATIC)
     .returns(String.class)
     .addParameter(String.class, "url")
     .addParameter(RequestDTO_C0.class, "payload")
     .addException(IOException.class);
         
     
      consumer
     .addStatement("$T httpClient = $T.createDefault()",CloseableHttpClient.class,HttpClients.class)
     .addStatement(" String result = \"\"")   
     .beginControlFlow("try");
      //TODO: ADD THE REST OF THE METHODS (PUT AND DELETE)
     if(MD_P.getMethod().equalsIgnoreCase("POST")){
         consumer.addStatement(" $T request = new $T(url)",HttpPost.class,HttpPost.class);
     }else if(MD_P.getMethod().equalsIgnoreCase("GET"))
       consumer.addStatement(" $T request = new $T(url)",HttpGet.class,HttpGet.class);
     
     String Mediatype="";
      if(MD_P.getMediatype().equalsIgnoreCase("JSON")){
        Mediatype="application/json";
        
    }  
       
       consumer.addStatement(" request.addHeader(\"content-type\", \"$L\")",Mediatype) 
       .addComment("I haven't try this writevalue. For a test change all to String Json")
       .addStatement("String jsonPayload=new $T().writeValueAsString(payload)",ObjectMapper.class)
       .addStatement(" request.setEntity(new $T(jsonPayload))",StringEntity.class) 
       .addStatement(" $T response = httpClient.execute(request)",CloseableHttpResponse.class) 
       .beginControlFlow("try")  
            .addStatement(" $T entity = response.getEntity()", HttpEntity.class)
            .beginControlFlow("if (entity != null)")
            .addStatement(" result = $T.toString(entity)",EntityUtils.class) 
            .addStatement(" System.out.println(result)")
            .endControlFlow() 
       .endControlFlow() 
       .beginControlFlow("finally")
        .addStatement(" response.close()")
        .endControlFlow() 
      .endControlFlow() 
      .beginControlFlow("finally")
        .addStatement("httpClient.close()")
      .endControlFlow() 
      .addStatement("return result"); 
     
      
     
   
     
      return consumer.build();
     }
    
     public static void ResourcesLWGen (InterfaceMetadata MD_C, InterfaceMetadata MD_P ){
         
  
       MethodSpec testEcho=  testEcho();
       MethodSpec methodgen=methodgen(MD_C);
       MethodSpec consumeService =consumeService(MD_P); 
        
        
        
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

               
 
  
        JavaFile javaFile2 = JavaFile.builder("resources",RclassGen)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\InterfaceLightweight\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        
     }
    
    
    
}
