/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import static generator.codgen.ClassGen.dummyobject;
import static generator.codgen.ClassGen1.classGen;
import java.util.ArrayList;

/**
 *
 * @author cripan
 */
public class ClassGenComplex {
    
   public static ArrayList<String> classesDummy= new ArrayList<String>();
    
    public static ArrayList<String> complexelement (ArrayList<String[]> elements){
        boolean boo=false;
       System.out.println("\n\nARRAYLIST SIZE:"+elements.size());
       for (int i = 0; i < elements.size(); i++){ 
        String e =elements.get(i)[0];
        System.out.println(i+" 1 " + elements.get(i)[0]);
        System.out.println(i+" 2 " +elements.get(i)[1]);
        
        if(e!=null){
         if(e.equals("Newclass")){
             if(("StopClass".equals(elements.get(i+1)[0]))){
                 i=elements.size()+1;
             }else{
               i=genClomplex (elements,i);  
             }
             boo=true;
            
         }
        } 
     
         }
        return classesDummy;
              
    
    }
    
    
    public static int genClomplex (ArrayList<String[]> elements, int i){
        //ArrayList<String[]> var= new ArrayList<String[]>();
        ArrayList<String[]> newclass = new ArrayList<String[]>();
        boolean out=false;
              int j=i+1;
              
            String className=elements.get(i)[1];  
            System.out.println("class name "+className);
           //while(!((className.equals(elements.get(j)[0]))&& ("StopClass".equals(elements.get(j+1)[0])))){ 
          // while((!(className.equals(elements.get(j)[1])))&& out==false){
          while(out==false){
              if("StopClass".equals(elements.get(j)[0])){
                  out=true;
                  }else{
                  
            
                  
               System.out.println(elements.get(j)[0]);
              if("child:Newclass".equals(elements.get(j)[0])){
                  int current_j=genClomplex (elements,j);
                  System.out.println("Clase creada vuelvo "+current_j);
                  String[] ele=new String[2];
                  if("single".equals(elements.get(j)[2])){
                  
                  ele[0]=elements.get(j)[1];
                  ele[1]=elements.get(j)[1];
                  System.out.println("ele0:" +ele[0]);
                  System.out.println("ele1:" +ele[1]); 
                  
                  newclass.add(ele);
                  
                  }else if ("list".equals(elements.get(j)[2])){
                  
                  ele[0]=elements.get(j)[1];
                  ele[1]="List";
                  System.out.println("ele0:" +ele[0]);
                  System.out.println("ele1:" +ele[1]); 
                  newclass.add(ele);
                  
                  }
                  
                  
                  
                  if("StopClass".equals(elements.get(current_j)[0])){
                        out=true;
                   }else{
                        j++;
                  }
                  
              }else{
                  
                  String value=elements.get(j)[0];
                 while("child".equals(value)){
                   System.out.println(elements.get(j)[1]);
                   System.out.println(elements.get(j)[2]);
                  
                  String[] ele=new String[2];
                  ele[0]=elements.get(j)[1];
                  ele[1]=elements.get(j)[2];
                  System.out.println("ele0:" +ele[0]);
                  System.out.println("ele1:" +ele[1]); 
                  newclass.add(ele);
                   System.out.println(j);
               
                        j++;
                  
                 value=elements.get(j)[0];
                 System.out.println("new value: "+value);
              }
                
              }
                }
           
        }   
          
        String CS =dummyobject(className,newclass);
        classesDummy.add(CS); 
        //System.out.println(newclass.get(0)[0]+" , "+newclass.get(1)[0]);
        classGen(newclass,className);
        return j;
    }
    
    
    public static void readList (ArrayList<String[]> elements){
        
        for (int i = 0; i < elements.size(); i++){ 
            String[] ele=elements.get(i);
            for (int j = 0; j < ele.length; j++){
                System.out.println(i+"."+j+" :"+elements.get(i)[j]);
            }
            
        }
        
    }
}
