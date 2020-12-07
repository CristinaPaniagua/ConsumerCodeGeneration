/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;

/**
 *
 * @author cripan
 */
public class ClassGenNested {
    
   public  ArrayList<String> classesDummy= new ArrayList<String>();

    public ClassGenNested() {
    }

    public ArrayList<String> getClassesDummy() {
        return classesDummy;
    }

    public void setClassesDummy(ArrayList<String> classesDummy) {
        this.classesDummy = classesDummy;
    }
   
   
    
    public ArrayList<String> complexelement (ArrayList<String[]> elements){
        boolean boo=false;
      // System.out.println("\n\nARRAYLIST SIZE:"+elements.size());
       for (int i = 0; i < elements.size(); i++){ 
        String e =elements.get(i)[0];
        //System.out.println("complexElement"+i+" 1 " + elements.get(i)[0]);
        //System.out.println("complexElement"+i+" 2 " +elements.get(i)[1]);
        
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
    
    
    public  int genClomplex (ArrayList<String[]> elements, int i){
        //ArrayList<String[]> var= new ArrayList<String[]>();
        ArrayList<String[]> newclass = new ArrayList<String[]>();
        boolean out=false;
              int j=i+1;
              
            String className=elements.get(i)[1];  
            //System.out.println("class name "+className);
           //while(!((className.equals(elements.get(j)[0]))&& ("StopClass".equals(elements.get(j+1)[0])))){ 
          // while((!(className.equals(elements.get(j)[1])))&& out==false){
          while(out==false){
              if("StopClass".equals(elements.get(j)[0])){
                  out=true;
                  }else{
                  
            
                  
               //System.out.println(elements.get(j)[0]);
              if("child:Newclass".equals(elements.get(j)[0])){
                  int current_j=genClomplex (elements,j);
                  //System.out.println("Clase creada vuelvo "+current_j);
                  String[] ele=new String[2];
                  if("single".equals(elements.get(j)[2])){
                  
                  ele[0]=elements.get(j)[1];
                  ele[1]=elements.get(j)[1];
                 // System.out.println("ele0:" +ele[0]);
                 // System.out.println("ele1:" +ele[1]); 
                  
                  newclass.add(ele);
                  
                  }else if ("list".equals(elements.get(j)[2])){
                  
                  ele[0]=elements.get(j)[1];
                  ele[1]="List";
                  //System.out.println("ele0:" +ele[0]);
                  //System.out.println("ele1:" +ele[1]); 
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
                   //System.out.println(elements.get(j)[1]);
                   //System.out.println(elements.get(j)[2]);
                  
                  String[] ele=new String[2];
                  ele[0]=elements.get(j)[1];
                  ele[1]=elements.get(j)[2];
                  //System.out.println("ele0:" +ele[0]);
                 // System.out.println("ele1:" +ele[1]); 
                  newclass.add(ele);
                  // System.out.println(j);
               
                        j++;
                  
                 value=elements.get(j)[0];
                // System.out.println("new value: "+value);
              }
                
              }
                }
           
        }   
        
        ClassGenSimple c=new ClassGenSimple();
        String CS =c.dummyobject(className,newclass);
        classesDummy.add(CS); 
       // System.out.println("ADDTION TO CS:"+CS);
        //System.out.println(newclass.get(0)[0]+" , "+newclass.get(1)[0]);
        //ClassGen1 c1=new ClassGen1();
        classGen(newclass,className);
        return j;
    }
    
    public  MethodSpec  constructor (String name){
        
     MethodSpec consructor = MethodSpec.constructorBuilder()
     .addModifiers(Modifier.PUBLIC)
    .build();
     
    
     return consructor;
    }
    
        
    public  MethodSpec  fullConstructor ( ArrayList<String[]> elements,String className){
        
        
     MethodSpec.Builder BFullConsructor = MethodSpec.constructorBuilder()
     .addModifiers(Modifier.PUBLIC);
   
      for (int i = 0; i < elements.size(); i++){ 
        String name=elements.get(i)[0];
        String type=elements.get(i)[1];
        
        
            if(type.equalsIgnoreCase("single")||type.startsWith("List")){

               TypeName t= getTypeCom(name, type);
                 BFullConsructor
                    .addParameter(t,name)
                    .addStatement("this.$N = $N", name, name);
            }
            else{  
    
                    BFullConsructor
                    .addParameter(getType(type),name)
                    .addStatement("this.$N = $N", name, name);
                    
              }
        
     
       
     }
       MethodSpec FullConsructor = BFullConsructor.build();
      return FullConsructor;
    }
    
 public  MethodSpec  get (String name, String type){
        
     MethodSpec.Builder get = MethodSpec.methodBuilder("get"+name)
     .addModifiers(Modifier.PUBLIC);
     
      if(type.equalsIgnoreCase("single")||type.startsWith("List")){
               
                get.returns(getTypeCom(name, type))
                .addStatement("return "+name);
                
      }else {
                get.returns(getType(type))
                .addStatement("return "+name);
                
      }

     
     
      
     return get.build();
    }
    
    
    public  MethodSpec  toString (ArrayList<String[]> elements){
        
          String S="";
     for (int i = 0; i < elements.size(); i++){ 
        String name=elements.get(i)[0];
        S=S+"+ \""+name+"=\" + "+name+"+ \", \"";
     }
        
     MethodSpec toString  = MethodSpec.methodBuilder("toString")
    .addModifiers(Modifier.PUBLIC)
     .addAnnotation(Override.class)
     .returns(String.class)
     .addStatement("return \"{\" $L +\"}\"",S)
     .build();
    
     
      
     return toString;
    }
    
   public  MethodSpec  set (String name, String type){
        
     MethodSpec.Builder set  = MethodSpec.methodBuilder("set"+name)
    .addModifiers(Modifier.PUBLIC);
     
      if(type.equalsIgnoreCase("single")||type.startsWith("List")){
               
                set.addParameter(getTypeCom(name, type),name)
                .addStatement("this."+name+"="+name);
                
      }else {
     set.addParameter(getType(type),name)
     .addStatement("this."+name+"="+name);
      }
     
     
      
     return set.build();
    }
    
    
    public  void classGen ( ArrayList<String[]> elements , String className){
         
        
     String ClassName =className.substring(0, 1).toUpperCase() + className.substring(1,className.length()); 
     
        MethodSpec constructor= constructor(ClassName);
        MethodSpec fullConstructor=fullConstructor(elements,ClassName);
        MethodSpec toString=toString(elements);
      
     TypeSpec.Builder BclassGen =TypeSpec.classBuilder(ClassName)
              .addModifiers(Modifier.PUBLIC)
             .addMethod(constructor)
             .addMethod(fullConstructor)
             .addMethod(toString); 
               
      
      
      
 for (int i = 0; i < elements.size(); i++){ 
        String name=elements.get(i)[0];
        String type=elements.get(i)[1];
        MethodSpec methodget= get(name,type);
        MethodSpec methodset= set(name,type);
            if(type.equalsIgnoreCase("single")||type.startsWith("List")){

               TypeName t= getTypeCom(name, type);
                  BclassGen.addField(t, name, Modifier.PRIVATE)
                 .addMethod(methodget)
                 .addMethod(methodset);
            }
            else{  
            
             Type T=getType(type);
            BclassGen.addField(T,name, Modifier.PRIVATE)
                 .addMethod(methodget)
                 .addMethod(methodset);
            }
          
       
        
    }   
     
         
   
        TypeSpec classGen  = BclassGen.build();
        
     
              
               
 
  
        JavaFile javaFile2 = JavaFile.builder("eu.generator.resources",classGen)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCodeGeneration\\ConsumerGenerationModulesSpring\\InterfaceLightweight\\src\\main\\java"));
        }catch (IOException ex){
           // System.out.print("Exception:" + ex.getMessage());
        }
        
     }
     
    public  Type getType(String type){
         Type t;
         
         if(type.equalsIgnoreCase("String")) t=String.class;
         else if (type.equalsIgnoreCase("Boolean")) t=Boolean.class;
         else if (type.equalsIgnoreCase("Integer")) t=Integer.class;
         else if (type.equalsIgnoreCase("Byte")) t=Byte.class;
         else if (type.equalsIgnoreCase("Double")) t=Double.class;
         else if (type.equalsIgnoreCase("Float")) t=Float.class;
         else if (type.equalsIgnoreCase("Short")) t=Short.class;
         else if (type.equalsIgnoreCase("Long")) t=Long.class;
         
         
        
         
         //TODO: ADD MORE TYPES
        
         else t=Object.class;
         return t;
     }
     
      public  TypeName getTypeCom(String name, String type){
         TypeName t;
         
         
         if (type.equalsIgnoreCase("List"))   t = ParameterizedTypeName.get(ClassName.get(List.class),TypeVariableName.get(name) );
         else if (type.equalsIgnoreCase("single"))  t =TypeVariableName.get(name); 
         else  t =TypeVariableName.get(name);
         
         //TODO: ADD MORE COMPLEX TYPES
             return t;
             }
    public void readList (ArrayList<String[]> elements){
        
        for (int i = 0; i < elements.size(); i++){ 
            String[] ele=elements.get(i);
            for (int j = 0; j < ele.length; j++){
                //System.out.println(i+"."+j+" :"+elements.get(i)[j]);
            }
            
        }
        
    }
}
