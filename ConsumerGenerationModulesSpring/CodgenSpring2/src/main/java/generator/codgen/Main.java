   /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import eu.arrowhead.common.CodgenUtil;
import java.util.ArrayList;
import eu.arrowhead.common.TypeSafeProperties;






/**
 *
 * @author cripan
 */
public class Main {

private static InterfaceMetadata MD_Provider=null;
private static InterfaceMetadata MD_Consumer=null;
private static TypeSafeProperties request = CodgenUtil.getProp("request_config");
private static ArrayList<ArrayList<String>> classesRequestP= new ArrayList<ArrayList<String>>();
private static ArrayList<String> classesResponseP= new ArrayList<String>();
private static ArrayList<ArrayList<String>> classesRequestC= new ArrayList<ArrayList<String>>();
private static ArrayList<String> classesResponseC= new ArrayList<String>();
    public static void main(String[] args) {
        
        String service = request.getProperty("Service", "null");
        String system="providerTest";
        MD_Provider = readCDL.read("offer","provider");
        MD_Consumer= readCDL.read("offer","consumer");
        System.out.print(MD_Provider.toString());
             //eadList(MD.getElements_request());
             
          
      
             
     if(MD_Provider.getRequest()){
           ClassGen Request=new ClassGen();
       
             for(int i=0; i<MD_Provider.elements_request.size();i++)
             {
                ArrayList<String[]> elements_request=MD_Provider.elements_request.get(i).getElements();
               classesRequestP.add(Request.classGen(elements_request,"RequestDTO_P"+i));
             }
            //for(int h=0;h<classesRequest.size();h++)
                //System.out.println("..........."+classesRequest.get(h));
  
       }
        
       
        if(MD_Provider.getResponse()){
            ClassGen Response=new ClassGen();
      
                 for(int j=0; j<MD_Provider.elements_response.size();j++)
                 {
                    ArrayList<String[]> elements_response=MD_Provider.elements_response.get(j).getElements();
                    classesResponseP=Response.classGen(elements_response,"ResponseDTO_P"+j);
                 }
           
        }
        
        
         if(MD_Consumer.getRequest()){
           ClassGen Request=new ClassGen();
       
             for(int i=0; i<MD_Consumer.elements_request.size();i++)
             {
                ArrayList<String[]> elements_request=MD_Consumer.elements_request.get(i).getElements();
               classesRequestC.add(Request.classGen(elements_request,"RequestDTO_C"+i));
             }
            //for(int h=0;h<classesRequest.size();h++)
                //System.out.println("..........."+classesRequest.get(h));
  
       }
        
       
        if(MD_Consumer.getResponse()){
            ClassGen Response=new ClassGen();
      
                 for(int j=0; j<MD_Consumer.elements_response.size();j++)
                 {
                    ArrayList<String[]> elements_response=MD_Consumer.elements_response.get(j).getElements();
                    classesResponseC=Response.classGen(elements_response,"ResponseDTO_C"+j);
                 }
           
        }
        
        
        
        
        //if(MD_Provider.getProtocol().equalsIgnoreCase("COAP"))
        //oapGenerator.coap(MD_Provider);
        
        ResourceLWGen.ResourcesLWGen(MD_Consumer, MD_Provider);
       
    }
    
    
 }
    

       
    

