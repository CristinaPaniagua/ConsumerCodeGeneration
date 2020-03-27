/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import javax.lang.model.element.Modifier;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.config.NetworkConfigDefaultHandler;
import org.eclipse.californium.elements.exception.ConnectorException;
/**
 *
 * @author cripan
 */
public class CoapGenerator {

    public CoapGenerator() {
    }
    
      
public static MethodSpec  coapRequest (InterfaceMetadata MD){
  
    //CoapResponse response = client.put(payload, MediaTypeRegistry.TEXT_PLAIN); EXAMPLE PUT/POST
    
     MethodSpec.Builder sendCoapRequestB = MethodSpec.methodBuilder("coapRequest")
     .addModifiers(Modifier.PUBLIC)
     .addModifiers(Modifier.STATIC)
     .addParameter(String.class, "providerURI")
     .returns(CoapResponse.class);
             
     sendCoapRequestB.addStatement("//Adding Import $T",NetworkConfig.class)
     .addStatement("$T config = $T.createWithFile(CONFIG_FILE, CONFIG_HEADER, DEFAULTS)", NetworkConfig.class,NetworkConfig.class)
     .addStatement("NetworkConfig.setStandard(config)")
     .addStatement("$T uri=null", URI.class)
     .addStatement("String[] args={\"0\"}")
     .beginControlFlow("try")
     .addStatement("uri = new URI(\"coap://localhost:5683/publish\")")
     .nextControlFlow("catch($T e)",URISyntaxException.class)
     .addStatement("System.err.println(\"Invalid URI: \" + e.getMessage())")
     .addStatement("System.exit(-1)")
     .endControlFlow()
     .addStatement("$T client= new $T(uri)",CoapClient.class,CoapClient.class)
     .addStatement("$T response = null",CoapResponse.class)
     .beginControlFlow("try");
     
     if(MD.getMethod().equalsIgnoreCase("GET")){
     sendCoapRequestB.addStatement("response = client.get()");
             }
     if(MD.getMethod().equalsIgnoreCase("POST")){
     sendCoapRequestB.addStatement("String payload=args[0]")
     .addStatement("response = client.put(payload, $T.TEXT_PLAIN)",MediaTypeRegistry.class);
     }
     if(MD.getMethod().equalsIgnoreCase("PUT")){
     sendCoapRequestB.addStatement("String payload=\"\"")
     .addStatement("response = client.put(payload,$T.TEXT_PLAIN)",MediaTypeRegistry.class);
     }
     
     sendCoapRequestB.nextControlFlow("catch($T|$T e)",ConnectorException.class,IOException.class)
     .addStatement("System.err.println(\"Got an error: \" + e)")
     .endControlFlow()
     .beginControlFlow("if(response!=null)")
     .addStatement("System.out.println(response.getCode())")
     .beginControlFlow("if (args.length > 1)")
     .beginControlFlow("try ($T out = new $T(args[1]))",FileOutputStream.class,FileOutputStream.class)
     .addStatement("out.write(response.getPayload())")
     .nextControlFlow("catch ($T e)",IOException.class)
     .addStatement("e.printStackTrace()")
     .endControlFlow()   
     .nextControlFlow("else")      
     .addStatement("System.out.println(response.getResponseText())") 
     .addStatement("System.out.println($T.prettyPrint(response))",Utils.class) 
     .endControlFlow() 
     .nextControlFlow("else")      
     .addStatement("System.out.println(\"No response received.\")")
     .endControlFlow() 
     .addStatement("client.shutdown()")
     .addStatement("return response") 
     .build();
  
  MethodSpec sendCoapRequest= sendCoapRequestB.build();
        return sendCoapRequest;
        
  } 

  
  
   
  
  //METHOD TO GENERATE THE MAIN AND CREATE THE MAINCLASS      
     public static void coap (InterfaceMetadata MD){
         
    
     MethodSpec sendCoapRequest=coapRequest(MD);
     
         
   
        TypeSpec CoapClient = TypeSpec.classBuilder("CoapClientGen")
                .addModifiers(Modifier.PUBLIC)
                .addField((FieldSpec.builder(File.class, "CONFIG_FILE", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                     .initializer("new File(\"Californium.properties\")").build()))
                .addField((FieldSpec.builder(String.class, "CONFIG_HEADER", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                     .initializer("\"Californium CoAP Properties file for Fileclient\"").build()))
                .addField((FieldSpec.builder(Integer.class, "DEFAULT_MAX_RESOURCE_SIZE", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                     .initializer("2 * 1024 * 1024; // 2 MB").build()))
                .addField((FieldSpec.builder(Integer.class, "DEFAULT_BLOCK_SIZE", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                     .initializer("512").build()))
                .addField((FieldSpec.builder(NetworkConfigDefaultHandler.class, "DEFAULTS", Modifier.PRIVATE, Modifier.STATIC)
                     .initializer("new NetworkConfigDefaultHandler() {\n" +
                "\n" +
                "		@Override\n" +
                "		public void applyDefaults(NetworkConfig config) {\n" +
                "			config.setInt(NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, DEFAULT_MAX_RESOURCE_SIZE);\n" +
                "			config.setInt(NetworkConfig.Keys.MAX_MESSAGE_SIZE, DEFAULT_BLOCK_SIZE);\n" +
                "			config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, DEFAULT_BLOCK_SIZE);\n" +
                "		}\n" +
                "	}").build()))
                .addMethod(sendCoapRequest)
                .build();
 
  
        JavaFile javaFile2 = JavaFile.builder("eu.arrowhead.client.consumer",CoapClient)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerGenerationModulesSpring\\Interface\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        
     }
}
