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

private static InterfaceMetadata MD_Provider=null;
private static InterfaceMetadata MD_Consumer=null;
private static TypeSafeProperties request = CodgenUtil.getProp("request_config");
private static ArrayList<ArrayList<String>> classesRequestP= new ArrayList<ArrayList<String>>();
private static ArrayList<String> classesResponseP= new ArrayList<String>();
private static ArrayList<ArrayList<String>> classesRequestC= new ArrayList<ArrayList<String>>();
private static ArrayList<String> classesResponseC= new ArrayList<String>();

   public static void startGeneration(RequestForm rf){
     System.out.print("CODGEGEN START********************************************************* \n");   
        
     
     
        String service = request.getProperty("Service", "null");
        String system="providerTest";
        readCDL readConsumer = new readCDL();
        MD_Consumer= readConsumer.read("offer","consumer");
        readCDL readProvider = new readCDL();
        MD_Provider = readProvider.read("offer","provider");
        System.out.println(MD_Provider.toString());
        System.out.println(MD_Consumer.toString());
      
      
             
     if(MD_Provider.getRequest()){
           ClassGen Request=new ClassGen();
       
             for(int i=0; i<MD_Provider.elements_request.size();i++)
             {
                ArrayList<String[]> elements_requestP=MD_Provider.elements_request.get(i).getElements();
               classesRequestP.add(Request.classGen(elements_requestP,"RequestDTO_P"+i));
             }
            for(int h=0;h<classesRequestP.size();h++)
                System.out.println("..........."+classesRequestP.get(h));
  
       }
        
       
        if(MD_Provider.getResponse()){
            ClassGen Response=new ClassGen();
      
                 for(int j=0; j<MD_Provider.elements_response.size();j++)
                 {
                    ArrayList<String[]> elements_responseP=MD_Provider.elements_response.get(j).getElements();
                    classesResponseP=Response.classGen(elements_responseP,"ResponseDTO_P"+j);
                 }
           
        }
        
        
         if(MD_Consumer.getRequest()){
           ClassGen Request=new ClassGen();
       
             for(int i=0; i<MD_Consumer.elements_request.size();i++)
             {
                ArrayList<String[]> elements_requestC=MD_Consumer.elements_request.get(i).getElements();
               classesRequestC.add(Request.classGen(elements_requestC,"RequestDTO_C"+i));
             }
            for(int h=0;h<classesRequestC.size();h++)
                System.out.println("..........."+classesRequestC.get(h));
  
       }
        
       
        if(MD_Consumer.getResponse()){
            ClassGen Response=new ClassGen();
      
                 for(int j=0; j<MD_Consumer.elements_response.size();j++)
                 {
                    ArrayList<String[]> elements_responseC=MD_Consumer.elements_response.get(j).getElements();
                    classesResponseC=Response.classGen(elements_responseC,"ResponseDTO_C"+j);
                 }
           
        }
        
        
        
        
        //if(MD_Provider.getProtocol().equalsIgnoreCase("COAP"))
        //oapGenerator.coap(MD_Provider);
        
        ResourceLWGen.ResourcesLWGen(MD_Consumer, MD_Provider);
        
        
        
        
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

        processBuilder.command(("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\CodgenSpring2\\config\\Init.bat"));

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
   
   
   
        
        
         
}

    

