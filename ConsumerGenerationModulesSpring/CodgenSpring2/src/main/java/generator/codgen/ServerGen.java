/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;
/**
 *
 * @author cripan
 */
public class ServerGen {
    
    public static MethodSpec  httpServer(){
  
    
     MethodSpec.Builder mainHttp = MethodSpec.methodBuilder("main")
    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
    .returns(void.class)
    .addParameter(String[].class, "args")
    .addException(Exception.class);
             
    mainHttp
            .addStatement("$T address = InetAddress.getByName(\"127.0.0.1\")",InetAddress.class )
            .addStatement("$T socketAddress = new InetSocketAddress(address, 8088)",InetSocketAddress.class )
            .addStatement(" $T server = new Server(socketAddress)", Server.class)
            .addStatement(" $T  servletContextHandler = new ServletContextHandler(NO_SESSIONS)", ServletContextHandler.class)
            .addStatement("servletContextHandler.setContextPath(\"/\")")
            .addStatement(" server.setHandler(servletContextHandler)")
    ;
        
     MethodSpec main= mainHttp.build();
        return main;
        
  } 

 
     
   
  
  //METHOD TO GENERATE THE MAIN AND CREATE THE MAINCLASS      
     public static void Server (InterfaceMetadata MD){
         
    
     MethodSpec httpMain=httpServer();
     
         
   
        TypeSpec ServerApplication = TypeSpec.classBuilder("ServerApplication")  
                .addMethod(httpMain)
                .build();
 
  
        JavaFile javaFile2 = JavaFile.builder("Server",ServerApplication)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\InterfaceLightweight\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        
     }
}
