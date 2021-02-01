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
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.underscore.lodash.U;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import static generator.codgen.EncodingParser.createObjectMapper;
import eu.generator.consumer.RequestDTO_C0;
import eu.generator.provider.RequestDTO_P0;
import eu.generator.consumer.ResponseDTO_C0;
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
import eu.generator.provider.ResponseDTO_P0;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaultHandler;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.elements.exception.ConnectorException;
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
    //String addressP=MD_P.ge
    
    MethodSpec.Builder methodgen;
    CodeBlock mapperCode=createObjectMapper(MD_C.getMediatype_request(),"objMapper");
    
    
    if(MD_C.Protocol.equalsIgnoreCase("HTTP")){
    
    
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
         
    methodgen = MethodSpec.methodBuilder(MD_C.getID())
     .addModifiers(Modifier.PUBLIC);
     if(MD_C.getMediatype_response().equalsIgnoreCase("CBOR")){
          methodgen.returns(byte[].class);
      }else {
          methodgen.returns(String.class);
      }
     
     //TODO: ADD THE REST OF THE METHODS (PUT AND DELETE)
     if(MD_C.getMethod().equalsIgnoreCase("POST")){
         methodgen.addAnnotation(POST.class);
     }else if(MD_C.getMethod().equalsIgnoreCase("GET"))  methodgen.addAnnotation(GET.class);
     
     
     
      methodgen.addAnnotation(path)
     .addAnnotation(produces)
     .addAnnotation(consumes);
      if(MD_C.getMediatype_request().equalsIgnoreCase("CBOR")){
          methodgen.addParameter(byte[].class,"receivedPayload");
      }else{
           methodgen.addParameter(String.class,"receivedPayload");
      }
      
    } else{ //COAP
        
        if(MD_C.getMethod().equalsIgnoreCase("POST")){
             methodgen = MethodSpec.methodBuilder("handlePOST")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(CoapExchange.class,"exchange")
            .addStatement("exchange.getRequestText()");
          
        }else 
            {//if(MD_C.getMethod().equalsIgnoreCase("GET"))  
             methodgen = MethodSpec.methodBuilder("handleGET")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(CoapExchange.class,"exchange");
       
            }
        
        
        methodgen.addStatement("System.out.println(exchange.getRequestText())")
               .addStatement("String receivedPayload=exchange.getRequestText()");
               
        
         }
       methodgen 
     .addStatement("RequestDTO_C0 payload=new RequestDTO_C0()");
       
       if(MD_C.getMediatype_response().equalsIgnoreCase("CBOR")){
           methodgen.addStatement(" byte[] response=null");
      }else {
          methodgen.addStatement(" String response=null");
      }
               
       
     
     methodgen.addCode(mapperCode)
     .beginControlFlow("try");
       if(!MD_C.getMethod().equalsIgnoreCase("GET")){
           methodgen.addStatement("payload=objMapper.readValue(receivedPayload, RequestDTO_C0.class)")
             .addStatement("System.out.println(payload.toString())");
       }
     
      methodgen.addStatement("response=consumeService(\"http://127.0.0.1:8889/demo$L\",payload)",pathP)
     .endControlFlow()
     .beginControlFlow("catch ($T e)",Exception.class)      
     .addStatement(" e.printStackTrace()")
     .endControlFlow() 
     .beginControlFlow("if(response==null)");
     
     
       if(MD_C.Protocol.equalsIgnoreCase("HTTP")){
           methodgen.addStatement("  return response")
            .endControlFlow() 
            .beginControlFlow("else")
            .addStatement("return response");
       }else{ //COAP
         methodgen.addStatement("exchange.respond( \"ERROR: EMPTY RESPONSE\")")
            .endControlFlow() 
            .beginControlFlow("else");
        if(MD_C.getMediatype_request().equalsIgnoreCase("JSON")){
            methodgen.addStatement(" exchange.respond($T.ResponseCode.CONTENT,response,50)",CoAP.class);
        }else if(MD_C.getMediatype_request().equalsIgnoreCase("XML")){
          methodgen.addStatement(" exchange.respond($T.ResponseCode.CONTENT,response,41)",CoAP.class);
        }else
             methodgen.addStatement(" exchange.respond(response) ");
    }
      methodgen.endControlFlow() 
     ;
   
    
    
  
           // exchange.respond(CoAP.ResponseCode.CONTENT, "{\"Response\":\"POST_REQUEST_SUCCESS\"}", 50);
    
    
    
          return methodgen.build();
     }
    
 public static MethodSpec  publishResourceConst(){
  
     MethodSpec.Builder constResource = MethodSpec.constructorBuilder()
    .addModifiers(Modifier.PUBLIC)
    .addStatement(" super(\"publish\");\n" +

                    " getAttributes().setTitle(\"Publish Resource\")" );
           
     MethodSpec constructor= constResource.build();
        return constructor;      
  } 
          
    
     public static MethodSpec consumeService (InterfaceMetadata MD_C, InterfaceMetadata MD_P){
         
         
     //TRANSLATION BLOCK OF THE RESPONSE TO THE APPROPIATE ENCODING 
      CodeBlock mapperResponseProvider=createObjectMapper(MD_P.getMediatype_response(),"objMapperP");
      CodeBlock mapperResponseConsumer=createObjectMapper(MD_C.getMediatype_response(),"objMapperC");    
         
         
     
         
      MethodSpec.Builder consumer = MethodSpec.methodBuilder("consumeService")
     .addModifiers(Modifier.PRIVATE)
     .addModifiers(Modifier.STATIC);
      if(MD_C.getMediatype_response().equalsIgnoreCase("CBOR")){
          consumer.returns(byte[].class);
      }else {
          consumer.returns(String.class);
      }
     
     
     consumer.addParameter(String.class, "url")
     .addParameter(RequestDTO_C0.class, "payload")
     .addException(IOException.class)
     .addStatement(" RequestDTO_P0 payloadP= requestAdaptor(payload)");
         
           if(MD_P.getProtocol().equalsIgnoreCase("COAP")){ //CONSUMER SIZE is COAP
               
                consumer.addStatement("//Adding Import $T",NetworkConfig.class)
                        .addStatement(" $T CONFIG_FILE = new File(\"Californium.properties\");\n" +
"	 String CONFIG_HEADER = \"Californium CoAP Properties file for Fileclient\";\n" +
"	 int DEFAULT_MAX_RESOURCE_SIZE = 2 * 1024 * 1024; // 2 MB\n" +
"	 int DEFAULT_BLOCK_SIZE = 512",File.class)
         .addStatement(" $T DEFAULTS = new NetworkConfigDefaultHandler() {\n" +
"\n" +
"		@Override\n" +
"		public void applyDefaults($T config) {\n" +
"			config.setInt(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, DEFAULT_MAX_RESOURCE_SIZE);\n" +
"			config.setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, DEFAULT_BLOCK_SIZE);\n" +
"			config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, DEFAULT_BLOCK_SIZE);\n" +
"		};}",NetworkConfigDefaultHandler.class,NetworkConfig.class)
     .addStatement("$T config = $T.createWithFile(CONFIG_FILE, CONFIG_HEADER, DEFAULTS)", NetworkConfig.class,NetworkConfig.class)
     .addStatement("NetworkConfig.setStandard(config)")
     .addStatement("$T uri=null", URI.class)
     //.addStatement("String[] args={\"0\"}")
     .addStatement("String responseText= \" \"")
     .beginControlFlow("try")
     .addStatement("uri = new URI(\"coap://localhost:5555/publish\")")
     .nextControlFlow("catch($T e)",URISyntaxException.class)
     .addStatement("System.err.println(\"Invalid URI: \" + e.getMessage())")
     .addStatement("System.exit(-1)")
     .endControlFlow()
     .addStatement("$T client= new $T(uri)",CoapClient.class,CoapClient.class)
     .addStatement("$T response = null",CoapResponse.class)
     .beginControlFlow("try");
     
     if(MD_P.getMethod().equalsIgnoreCase("GET")){
     consumer.addStatement("response = client.get()");
     }else
     { 
         
         
         if(MD_P.getMediatype_request().equalsIgnoreCase("JSON"))
         { 
          consumer.addStatement("String payloadS=new $T().writeValueAsString(payloadP)",ObjectMapper.class)
          .addStatement("System.out.println(\"Payload Sent: \" + payloadS)");
          
            if(MD_P.getMethod().equalsIgnoreCase("POST"))
            {
             consumer.addStatement("response = client.post(payloadS,$T.APPLICATION_JSON)",MediaTypeRegistry.class);
              }
            if(MD_P.getMethod().equalsIgnoreCase("PUT"))
            {
            consumer.addStatement("response = client.put(payloadS,$T.APPLICATION_JSON)",MediaTypeRegistry.class);
              }
         }
         
             if(MD_P.getMediatype_request().equalsIgnoreCase("XML"))
         { 
          consumer
          .addStatement("$T xmlMapper = new XmlMapper()",XmlMapper.class)
          .addStatement("String payloadS=xmlMapper.writeValueAsString(payload)")
          .addStatement("System.out.println(\"Payload Sent: \" + payloadS)");
             
             
            if(MD_P.getMethod().equalsIgnoreCase("POST"))
            {
             consumer.addStatement("response = client.post(payloadS,$T.APPLICATION_XML)",MediaTypeRegistry.class);
              }
            if(MD_P.getMethod().equalsIgnoreCase("PUT"))
            {
            consumer.addStatement("response = client.put(payloadS,$T.APPLICATION_XML)",MediaTypeRegistry.class);
              }
         }if(MD_P.getMediatype_request().equalsIgnoreCase("CBOR"))
         { 
             
         }
            
            
     }
    
      
     
     consumer.nextControlFlow("catch($T|$T e)",ConnectorException.class,IOException.class)
     .addStatement("System.err.println(\"Got an error: \" + e)")
     .endControlFlow()
     .beginControlFlow("if(response!=null)")
     .addStatement("System.out.println(response.getCode())")
    // .beginControlFlow("if (args.length > 1)")
     // .beginControlFlow("try ($T out = new $T(args[1]))",FileOutputStream.class,FileOutputStream.class)
     // .addStatement("out.write(response.getPayload())")
     // .nextControlFlow("catch ($T e)",IOException.class)
     // .addStatement("e.printStackTrace()")
    //  .endControlFlow()   
    //  .nextControlFlow("else")      
     .addStatement("responseText= response.getResponseText()") 
     .addStatement("System.out.println($T.prettyPrint(response))",Utils.class) 
    //  .endControlFlow() 
     .nextControlFlow("else")      
     .addStatement("System.out.println(\"No response received.\")")
     .endControlFlow() 
     .addStatement("client.shutdown()")
     .addCode(mapperResponseProvider)
     .addStatement("$T responseObj=objMapperP.readValue(responseText,ResponseDTO_P0.class)",ResponseDTO_P0.class)
     .addCode(mapperResponseConsumer);
     
     
      Boolean Payload=true;
       if(Payload==true){
            if(MD_C.getMediatype_response().equalsIgnoreCase("XML") && MD_P.getMediatype_response().equalsIgnoreCase("JSON")){
                 consumer.addStatement(" String result_out = \"\"") 
                         .addStatement("result_out= $T.jsonToXml(responseText)",U.class);
             }else if(MD_C.getMediatype_response().equalsIgnoreCase("JSON") && MD_P.getMediatype_response().equalsIgnoreCase("XML")){
                  consumer.addStatement(" String result_out = \"\"")
                          .addStatement("result_out= $T.xmlToJson(responseText)",U.class);
             }else if(MD_C.getMediatype_response().equalsIgnoreCase("CBOR")){
                  consumer.addStatement(" byte[] result_out")
                          .addStatement("result_out=objMapperC.writeValueAsBytes(responseObj)");
             }else{
                  consumer.addStatement("result_out=responseText");
            }
     
     
      consumer.addStatement("return result_out");

  
       }        
               
           
       }else{ //HTTP is the protocol by default --> TODO: add the error if there is not a permited protocol
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
       .addStatement("String createdPayload=new $T().writeValueAsString(payloadP)",ObjectMapper.class);
       
        
    }else if(MD_P.getMediatype_request().equalsIgnoreCase("XML")){
        consumer.addStatement(" request.addHeader(\"content-type\", \"application/xml\")") 
       .addStatement("$T xmlMapper = new XmlMapper()",XmlMapper.class)
       .addStatement("String createdPayload=xmlMapper.writeValueAsString(payloadP)",ObjectMapper.class);
      
    
      }else if(MD_P.getMediatype_request().equalsIgnoreCase("CBOR")){
        consumer.addStatement(" request.addHeader(\"content-type\", \"pplication/cbor\")") 
       .addStatement("$T f = new CBORFactory()",CBORFactory.class)
       .addStatement("$T cborMapper = new ObjectMapper(f)",ObjectMapper.class)
       .addStatement("byte[] createdPayload = cborMapper.writeValueAsBytes(payloadP)");
      
    }
       
        consumer.addStatement(" System.out.println(\"Payload: \"+ createdPayload )");
                
       if(MD_P.getMediatype_request().equalsIgnoreCase("CBOR")){
            consumer.addStatement("$T eb = EntityBuilder.create()",EntityBuilder.class)
                    .addStatement("eb.setBinary(createdPayload)")
                    .addStatement("request.setEntity(eb.build())");
       }else{
                
       consumer.addStatement(" request.setEntity(new $T(createdPayload))",StringEntity.class);  
               
               } 

            //CodeBlock mapperResponseProvider=createObjectMapper(MD_P.getMediatype_response(),"objMapperP");
            //CodeBlock mapperResponseConsumer=createObjectMapper(MD_C.getMediatype_response(),"objMapperC");
           
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
            
       Boolean PayloadH=true;// TODO look if there is payload and change the flag according
       if(PayloadH==true){
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
     
      
       }
         
    
      
                return consumer.build();
     }
     
     
     
    public static MethodSpec requestTransform(InterfaceMetadata MD_C, InterfaceMetadata MD_P){
  
    MethodSpec.Builder payload = MethodSpec.methodBuilder("requestAdaptor")
    .addModifiers(Modifier.PUBLIC)
    .addModifiers(Modifier.STATIC)
    .returns(RequestDTO_P0.class)
    .addParameter(RequestDTO_C0.class, "payload_C")
    .addStatement(" $T payload_P = new RequestDTO_P0()",RequestDTO_P0.class);
    
     
      ArrayList<String[]> elements_requestC=MD_C.elements_request.get(0).getElements();
      ArrayList<String[]> elements_requestP=MD_P.elements_request.get(0).getElements();
      Boolean NestedP=false;
      Boolean NestedC=false;
      String NewClassP=null;
      String NewClassC=null;
     for (int i = 0; i < elements_requestC.size()-1; i++){ 
        String nameC=elements_requestC.get(i)[0];
        String typeC=elements_requestC.get(i)[1];
       
        
       if(nameC.equals("Newclass")){
               NewClassC= typeC;
               NestedC=true;
               
       }else{
           
           
         
           if(nameC.equals("child")){
                     nameC=elements_requestP.get(i)[1];
                     typeC=elements_requestP.get(i)[2];
           }else NestedC=false;
        
        
       System.out.println("consumer: " +nameC +" - "+ typeC);
        
            for (int j = 0; j < elements_requestP.size()-1; j++){ 
                 String nameP=elements_requestP.get(j)[0];
                 String typeP=elements_requestP.get(j)[1];
                 
                 
                if(nameP.equals("Newclass")){
                    NewClassP= typeP;
                    if(NestedP==false) payload.addStatement(" eu.generator.provider.$L $L = new  eu.generator.provider.$L ()", Capitalize(NewClassP), NewClassP, Capitalize(NewClassP));
                        NestedP=true;
                        
                 }else{
                        if(nameP.equals("child")){
                        nameP=elements_requestP.get(j)[1];
                        typeP=elements_requestP.get(j)[2];
                    }//else NestedP=false;
                        
                 
                 
                 System.out.println("Provider: " +nameP +" - "+ typeP);
                 
                System.out.println("NestedC: " +NestedC +", NestedP: "+ NestedP);
                 
                 if(nameC.equals(nameP)){
                     
                    if( NestedP && NestedC){
                        if(typeC.equalsIgnoreCase(typeP)|| (numberTypeDef(typeP)>numberTypeDef(typeC))){
                         payload.addStatement(" $L.set$L(payload_C.get$L().get$L() )",NewClassP, nameP,NewClassC,nameC)
                                 .addStatement("payload_P.set$L($L)",NewClassP,NewClassP);
                         j= elements_requestP.size();
                        }
                    }else if( !NestedP && NestedC){
                          if(typeC.equalsIgnoreCase(typeP)|| (numberTypeDef(typeP)>numberTypeDef(typeC))){
                         payload.addStatement("payload_P.set$L(payload_C.get$L().get$L())",nameP,NewClassC,nameC) ;
                         j= elements_requestP.size();
                        }
                        
                    }else if( NestedP && !NestedC){
                         if(typeC.equalsIgnoreCase(typeP)|| (numberTypeDef(typeP)>numberTypeDef(typeC))){
                          payload.addStatement("$L.set$L(payload_C.get$L() )",NewClassP,nameP,nameC)
                                 .addStatement("payload_P.set$L($L)",NewClassP,NewClassP);
                         j= elements_requestP.size();
                        }
                        
                    }else{
                          if(typeC.equalsIgnoreCase(typeP)|| (numberTypeDef(typeP)>numberTypeDef(typeC))){
                         payload.addStatement("payload_P.set$L(payload_C.get$L() )",nameP,nameC);
                         j= elements_requestP.size();
                            } 
                    }
                        
                       
                     
                     
                     
                
                     
                    
                      
                        }
                    
                     
                 }
            }
          }
    }
        
     
     
     payload.addStatement("return payload_P");
           
     MethodSpec payloadTrans=payload.build();
        return payloadTrans;      
  } 
    
      public static MethodSpec responseTransform(InterfaceMetadata MD_C, InterfaceMetadata MD_P){
  
     MethodSpec.Builder payload = MethodSpec.methodBuilder("responseAdaptor")
    .addModifiers(Modifier.PUBLIC)
    .returns(ResponseDTO_C0.class)
    .addParameter(ResponseDTO_P0.class, "payload_P")
    .addStatement(" $T payload_C;",ResponseDTO_C0.class)
    .addStatement("return payload_C");
     
   
           
     MethodSpec payloadTrans=payload.build();
        return payloadTrans;      
  } 
     
      public static String Capitalize( String name){
          name =name.substring(0, 1).toUpperCase() + name.substring(1,name.length()); 
          return name;
      }
     
     
      public static int numberTypeDef( String type){
          int typeNumber;
          
          
          if(type.equalsIgnoreCase("String")) typeNumber=-1;
         else if (type.equalsIgnoreCase("Boolean")) typeNumber=-2;
         else if (type.equalsIgnoreCase("Integer")) typeNumber=2;
         else if (type.equalsIgnoreCase("Byte")) typeNumber=0;
         else if (type.equalsIgnoreCase("Double")) typeNumber=5;
         else if (type.equalsIgnoreCase("Float")) typeNumber=4;
         else if (type.equalsIgnoreCase("Short")) typeNumber=1;
         else if (type.equalsIgnoreCase("Long"))typeNumber=3;
         else typeNumber=-100;
          
          return typeNumber;
      }
      
      
      
    
     public static void ResourcesLWGen (InterfaceMetadata MD_C, InterfaceMetadata MD_P ){
         
  
       MethodSpec testEcho=  testEcho();
       MethodSpec methodgen=methodgen(MD_C, MD_P);
       MethodSpec consumeService =consumeService(MD_C,MD_P); 
       MethodSpec  constructor =publishResourceConst();
       
      MethodSpec  requestAdaptor = requestTransform(MD_C,MD_P);
      MethodSpec  responseAdaptor = responseTransform(MD_C,MD_P);
        
        
        
      AnnotationSpec path= AnnotationSpec
                 .builder(Path.class)
                 .addMember("value", "$S", "/demo")
                 .build();
      
      
             
     TypeSpec.Builder classGen =TypeSpec.classBuilder("RESTResources")
              .addModifiers(Modifier.PUBLIC)
             //.addMethod(testEcho)
             .addMethod(methodgen)
             
             .addMethod(requestAdaptor)
             .addMethod(consumeService);
     
     if(MD_C.Protocol.equalsIgnoreCase("COAP")){
         classGen.superclass(CoapResource.class)
                 .addMethod(constructor);
     }
        
     
     if(MD_C.Protocol.equalsIgnoreCase("HTTP")){
         classGen.addAnnotation(path);
     }
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
