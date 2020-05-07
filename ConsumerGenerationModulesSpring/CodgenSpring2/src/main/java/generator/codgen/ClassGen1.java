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
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.List;
/**
 *
 * @author cripan
 */
public class ClassGen1 {

    public ClassGen1() {
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
        S=S+"+ \""+name+"=\" + "+name;
     }
        
     MethodSpec toString  = MethodSpec.methodBuilder("toString")
    .addModifiers(Modifier.PUBLIC)
     .addAnnotation(Override.class)
     .returns(String.class)
     .addStatement("return \"ProviderPayload{\" $L +\"}\"",S)
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
         
        
     
        MethodSpec constructor= constructor(className);
        MethodSpec fullConstructor=fullConstructor(elements,className);
        MethodSpec toString=toString(elements);
      
     TypeSpec.Builder BclassGen =TypeSpec.classBuilder(className)
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
        
     
              
               
 
  
        JavaFile javaFile2 = JavaFile.builder("resources",classGen)
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
}
