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
import static generator.codgen.ClassGenComplex.complexelement;
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
public class ClassGen {
private static ArrayList<String> classesDummy= new ArrayList<String>();
    public ClassGen() {
    }
    
    public static MethodSpec  constructor (String name){
        
     MethodSpec consructor = MethodSpec.constructorBuilder()
     .addModifiers(Modifier.PUBLIC)
    .build();
     
    
     return consructor;
    }
    
        
    public static MethodSpec  fullConstructor ( ArrayList<String[]> elements,String className){
      classesDummy=complexelement(elements);
      ArrayList<String[]> var= new ArrayList<String[]>();
       
      String[] ele2= new String[2];  
     MethodSpec.Builder BFullConsructor = MethodSpec.constructorBuilder()
     .addModifiers(Modifier.PUBLIC);
   
      for (int i = 1; i < elements.size(); i++){ 
        String name=elements.get(i)[0];
        String type=elements.get(i)[1];
        System.out.println(i+" "+name+" "+type );

        
            if(name.equals("Newclass")){
                    name=elements.get(i)[1];
                    type=elements.get(i)[2];
                    
                      ele2[0]=name;
                      ele2[1]=type;
                     var.add(ele2);
                    
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
                }else if(name.equals("child")||name.equals("child:Newclass")|| name.equals("StopClass") ){
           //NOTHING :D
                }else{
                String[] ele= new String[2]; 
                ele[0]=name;
                ele[1]=type;
                System.out.println(i+" "+ele[0]);
                var.add(ele); 
                //System.out.println(i+" "+ele[0]);
                   BFullConsructor
                      .addParameter(getType(type),name)
                     .addStatement("this.$N = $N", name, name);
                     
        }
            

     }
      
        System.out.println(var.size());
        String CS =dummyobject(className,var);
        classesDummy.add(CS);
       MethodSpec FullConsructor = BFullConsructor.build();
      return FullConsructor;
    }
    
    public static MethodSpec  get (String name, String type){
        
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
    
    
    public static MethodSpec  toString (ArrayList<String[]> elements){
        
          String S="";
          
     for (int i = 1; i < elements.size(); i++){ 
         String name=elements.get(i)[0];
         if(name.equals("Newclass")){
             name=elements.get(i)[1];
             S=S+"+ \""+name+"=\" + "+name;
          }else if(name.equals("child")||name.equals("child:Newclass")|| name.equals("StopClass") ){
           //NOTHING :D
         }else{
        
        S=S+"+ \""+name+"=\" + "+name;
     }
     }
        
     MethodSpec toString  = MethodSpec.methodBuilder("toString")
    .addModifiers(Modifier.PUBLIC)
     .addAnnotation(Override.class)
     .returns(String.class)
     .addStatement("return \"ProviderPayload{\" $L +\"}\"",S)
     .build();
    
     
      
     return toString;
    }
    
    
    public static MethodSpec  set (String name, String type){
        
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
    
    
    
     public static ArrayList<String> classGen ( ArrayList<String[]> elements , String className){
         
         

        MethodSpec constructor= constructor(className);
        MethodSpec fullConstructor=fullConstructor(elements,className);
        MethodSpec toString=toString(elements);
      
     TypeSpec.Builder BclassGen =TypeSpec.classBuilder(className)
              .addModifiers(Modifier.PUBLIC)
             .addMethod(constructor)
             .addMethod(fullConstructor)
             .addMethod(toString); 
               
      
      
      
    for (int i = 1; i < elements.size(); i++){ 
        String name=elements.get(i)[0];
        String type=elements.get(i)[1];
       
        if(name.equals("Newclass")){
            name=elements.get(i)[1];
            type=elements.get(i)[2];
            //ele[0]=name;
            //ele[1]=type;
            //var.add(ele);
            MethodSpec methodget= get(name,type);
            MethodSpec methodset= set(name,type);
             System.out.println("type " +type+" name "+name);
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
          
        }else if(name.equals("child")||name.equals("child:Newclass")|| name.equals("StopClass") ){
           //NOTHING :D
        }else{
    
        MethodSpec methodget= get(name,type);
        MethodSpec methodset= set(name,type);
        Type T=getType(type);
        BclassGen.addField(T, name, Modifier.PRIVATE)
                 .addMethod(methodget)
                 .addMethod(methodset);
        
        }
        
       
    }   
     
         
   
        TypeSpec classGen  = BclassGen.build();

               
 
  
        JavaFile javaFile2 = JavaFile.builder("eu.arrowhead.client.consumer",classGen)
                .addFileComment("Auto generated")
                .build();
        try{
            javaFile2.writeTo(Paths.get("C:\\Users\\cripan\\Desktop\\Code_generation\\ConsumerCode-Generation\\ConsumerGenerationModulesSpring\\TesterSpring\\src\\main\\java"));
        }catch (IOException ex){
            System.out.print("Exception:" + ex.getMessage());
        }
        return classesDummy;
     }
     
     public static String dummyobject (String name, ArrayList<String[]> var ){
        
         String s=null;
         s=""+name+" OBJ"+name+" = new "+name+"( ";
         int a=0;
         boolean ListFlag=false;
         System.out.println(var.size());
         if(var.size()>1){
         for (int i=0;i<var.size();i++){
          
             if(var.get(i)[1].equalsIgnoreCase("String")){
                s=s+" \""+var.get(i)[0]+"\""; 
             }else if(var.get(i)[1].equalsIgnoreCase("Double")||var.get(i)[1].equalsIgnoreCase("Float")){
                  s=s+""+0.0+"";
             }else if(var.get(i)[1].equalsIgnoreCase("Integer")||var.get(i)[1].equalsIgnoreCase("Short")||var.get(i)[1].equalsIgnoreCase("Long")){
                 s=s+""+0+"";
             }else if(var.get(i)[1].equalsIgnoreCase("Boolean")){
                      s=s+""+true+"";
             }else if(var.get(i)[1].startsWith("List")){
                 s=s+"ListObject";
                 ListFlag=true;
                 a=i;
                     }else 
                     s=s+"OBJ"+var.get(i)[0]+"";
             
            if((i+1)<var.size()) s=s+" , ";
         }
         
         }
         s=s+")";
         
         if(ListFlag){
             s="List<"+var.get(a)[0]+"> ListObject=null; \n ListObject.add(OBJ"+var.get(a)[0]+"); \n"+s;
    
         }
        System.out.println(s);
         return s;
        
     }
     
     public static Type getType(String type){
         Type t;
         
         if(type.equalsIgnoreCase("String")) t=String.class;
         else if (type.equalsIgnoreCase("Boolean")) t=Boolean.class;
         else if (type.equalsIgnoreCase("Integer")) t=Integer.class;
         else if (type.equalsIgnoreCase("Byte")) t=Byte.class;
         else if (type.equalsIgnoreCase("Double")) t=Double.class;
         else if (type.equalsIgnoreCase("Float")) t=Float.class;
         else if (type.equalsIgnoreCase("Short")) t=Short.class;
         else if (type.equalsIgnoreCase("Long")) t=Long.class;
          else if (type.equalsIgnoreCase("Single")) t=Object.class;
         else if (type.startsWith("List")){
             //ParameterizedTypeName ListType =ParameterizedTypeName.get(List.class, Object.class);
             t=List.class;
         }
         
        
         
         //TODO: ADD MORE TYPES
        
         else t=Object.class;
         return t;
     }
     
      public static TypeName getTypeCom(String name, String type){
         TypeName t;
         
         
         if (type.equalsIgnoreCase("List"))   t = ParameterizedTypeName.get(ClassName.get(List.class),TypeVariableName.get(name) );
         else if (type.equalsIgnoreCase("single"))  t =TypeVariableName.get(name); 
         else  t =TypeVariableName.get(name);
         
         //TODO: ADD MORE COMPLEX TYPES
             return t;
             }
}
