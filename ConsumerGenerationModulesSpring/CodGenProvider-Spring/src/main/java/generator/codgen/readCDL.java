/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;
/**
 *
 * @author Cristina Paniagua
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class readCDL {
      public static  ArrayList<String[]> elements_request = new ArrayList<String[]>(); 
    public static  ArrayList<String[]> elements_response = new ArrayList<String[]>();
    public static boolean request;
    public static boolean response;
public static InterfaceMetadata read(String service,String System){
    String sec=null;
    String protocol=null;
    String path=null;
    String method=null;
    String mediatype=null;
    String ID=null;
     String complexType_response=null;
    String complexType_request=null;
    

    
    Map<String,String> CDLstorage= new HashMap<>();
    CDLstorage.put("providerTest","cdl_cars.xml");
    CDLstorage.put("laptop4-dashboard-0","cdl_consumer.xml");
    
    
    String CDLName=CDLstorage.get(System);
   
   

    
      try  {
          
    	
	 File File1 = new File("cdl_MD.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc= dBuilder.parse(File1);
        
        boolean serviceFound=false;  
         
        int interfaceNumber=0;
    
        int  methodNumber=0;
        
        
     //PROVIDER   
        NodeList interfaces = doc.getElementsByTagName("interface");
    
       for(int j= 0; j<interfaces.getLength(); j++){

        Node interf = interfaces.item(j);
        Element ele1 = (Element) interf;
        NodeList methods = ele1.getElementsByTagName("method");
         for (int h= 0; h<methods.getLength(); h++){
              Node nMethod = methods.item(h);
              Element eMethod= (Element) nMethod;
              if(eMethod.getAttribute("id").equalsIgnoreCase(service)){  //check if the interface contains the service that we want to consume
             interfaceNumber=j;
             methodNumber=h;
            serviceFound=true;
              j=interfaces.getLength();
              h=methods.getLength();
                }
         }
       }
       
     
       
       
       if(serviceFound==true) {
 //The interface anaylize is the one that have the service. TODO what happend if we have two interfaces for the same service, the service name is different?       
       
        Node inter1 = interfaces.item(interfaceNumber);
              if (inter1.getNodeType() == Node.ELEMENT_NODE) {
                  Element ele1 = (Element) inter1;
                NodeList methods = ele1.getElementsByTagName("method");
        
       
//Protocol
             protocol=ele1.getAttribute("protocol");
       
//Method 
            
   
        Node nMethod1 =  methods.item(methodNumber);
        if(nMethod1.getNodeType() == Node.ELEMENT_NODE){
                Element eMethod= (Element) nMethod1;
                        ID=eMethod.getAttribute("id");
                        method=eMethod.getAttribute("name");
                        path=eMethod.getAttribute("path");
                        
                        
          //REQUEST                         
                         
                NodeList  lR=eMethod.getElementsByTagName("request");
                            
                         if(lR.getLength()==0){
                           request=false;
                         }else{
                           request=true;
                         
                         
                      
                     Node nR = lR.item(0);
                     if(nR.getNodeType() == Node.ELEMENT_NODE){
                            Element eR= (Element) nR;
                                
                              

//Format comparision
//Encoding
                            Node nEncode1 =eR.getElementsByTagName("encode").item(0);
                            Element eEncode1= (Element) nEncode1;
                            
                            mediatype=eEncode1.getAttribute("name");

                            // Payload
                             Node npayload1 =eR.getElementsByTagName("payload").item(0);
                             Element epayload1=(Element) npayload1;
                             NodeList complex1=epayload1.getElementsByTagName("complextype");
                             //if(!complex1.equals(null)){
                                  Node ncomplex1=complex1.item(0); 
                                  Element ecomplex= (Element)ncomplex1;
                              complexType_request=ecomplex.getAttribute("type");   
                            // } 

                             Node childNode=epayload1.getFirstChild();
                             
                             while(childNode.getNextSibling()!=null){          
                                    childNode = childNode.getNextSibling(); 
                                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {    
                                   Element e =(Element) childNode;
                                   String[] ele;
                                   
                                   String tagname= e.getTagName();

                                    if("complexelement".equals(tagname)){
    
                                        complexelFunction ("REQUEST",e,"Newclass");
                                        
         
                                 
                                   }else{
                                   ele=new String[2];
                                   ele[0]=e.getAttribute("name");
                                   ele[1]=e.getAttribute("type");
                                   elements_request.add(ele);
                                   }
                                    //elements_response.add(ele);
                               }
                              
                             }

                               
                           
                            
                                   
                           
                     }
                               
                         
        } //close else    
           //RESPONSE    
                NodeList  lRR=eMethod.getElementsByTagName("response");
                            
                         if(lRR.getLength()==0){
                           response=false;
                         } else{
                             response=true;
                      
                     Node nRR = lRR.item(0);
                     if(nRR.getNodeType() == Node.ELEMENT_NODE){
                            Element eRR= (Element) nRR;
                                
                              

//Format comparision
//Encoding
                            Node nEncode1 =eRR.getElementsByTagName("encode").item(0);
                            Element eEncode1= (Element) nEncode1;
                            
                            mediatype=eEncode1.getAttribute("name");
  
                            // Payload
                             Node npayload1 =eRR.getElementsByTagName("payload").item(0);
                             Element epayload1=(Element) npayload1;
                             
                             NodeList complex2=epayload1.getElementsByTagName("complextype");
                             //if(!complex2.equals(null)){
                                  Node ncomplex1=complex2.item(0); 
                                  Element ecomplex= (Element)ncomplex1;
                              complexType_response=ecomplex.getAttribute("type");   
                             //} 
                             
                   
                             Node childNode=epayload1.getFirstChild();

                             while(childNode.getNextSibling()!=null){          
                                    childNode = childNode.getNextSibling(); 
                                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {    
                                   Element e =(Element) childNode;
                                   String[] ele;
                                   
                                   String tagname= e.getTagName();

                                    if("complexelement".equals(tagname)){
    
                                        complexelFunction ("RESPONSE",e,"Newclass");
                                        
         
                                 
                                   }else{
                                   ele=new String[2];
                                   ele[0]=e.getAttribute("name");
                                   ele[1]=e.getAttribute("type");
                                   elements_response.add(ele);
                                   }
                                    //elements_response.add(ele);
                               }
                              
                             }

                                   
                           }
                     }
                            
                            
                     
        }      
                            
                            
                  }
       
           
                   
       }//else{ System.out.println( "ERROR: Service interface not found");}
      
       
       
    } catch (Exception e) {
	e.printStackTrace();
    }
                   
        
    
      
 InterfaceMetadata MD = new InterfaceMetadata(protocol,path,method,mediatype,ID,complexType_request,complexType_response,elements_request, elements_response, request,response);  
    return MD;
    
}

public static void complexelFunction ( String r, Element e, String level){
    
                                     String [] c=new String[3];
                                     c[0]=level;
                                     String classname=e.getAttribute("name");
                                     c[1]=classname;
                                     c[2]=e.getAttribute("type");
                                     if (r.equalsIgnoreCase("REQUEST"))  elements_request.add(c);
                                      else elements_response.add(c);

                                     Node elechild=e.getFirstChild();


                                        while(elechild.getNextSibling()!=null){ 
                                            elechild=elechild.getNextSibling();
                                            if (elechild.getNodeType() == Node.ELEMENT_NODE) { 
                                             
                                             Element e2 =(Element) elechild;
                                          String tagname= e2.getTagName();
                                          String ename=e2.getAttribute("name");


                                    if("complexelement".equals(tagname)){
                                              complexelFunction (r,e2,"child:Newclass");
                                              elechild=elechild.getNextSibling();
                                              //if(elechild!=null)
                                             //while(elechild.getNodeType() != Node.ELEMENT_NODE) { 
                                               //elechild=elechild.getNextSibling();
                                             //}
                                          }else{
                                             String [] f=new String[3];
                                             f[0]="child";
                                             f[1]=e2.getAttribute("name");
                                             f[2]=e2.getAttribute("type");
                                             
                                              if (r.equalsIgnoreCase("REQUEST"))
                                             elements_request.add(f);
                                             else elements_response.add(f);
                                             
 
                                          }
                                                                                    
                                         }  
                                     
                                     }      
                                         //elements_response.add(c);  
                                       
                                     String [] StopClass=new String[2];
                                     StopClass[0]="StopClass"; 
                                     
                                      if (r.equalsIgnoreCase("REQUEST"))
                                    elements_request.add(StopClass);
                                     else elements_response.add(StopClass);
                                     
                                       
}
    }

  

