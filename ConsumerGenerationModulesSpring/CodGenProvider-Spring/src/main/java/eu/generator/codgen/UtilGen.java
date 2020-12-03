/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.generator.codgen;

import com.squareup.javapoet.MethodSpec;
import eu.arrowhead.common.exception.ArrowheadException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.lang.model.element.Modifier;

/**
 *
 * @author cripan
 */
public  class UtilGen {
    
    
    public static   MethodSpec commandLine(){
  
      MethodSpec.Builder BcommandLine= MethodSpec.methodBuilder("commandLineUI")
     .addModifiers(Modifier.PUBLIC)
     .addParameter(Scanner.class, "sc")
     .returns(String.class)
     //.addException(IOException.class)
     //.addException(InterruptedException.class)
     
     .addStatement("String DataEntry=null")
     .addStatement("String BlockInstance=null")       
     .beginControlFlow("while(true)")
     .beginControlFlow("try")
     .addStatement("System.out.print(\"\\n\" + \"Trigger a new service request? (y/n): \")")
     .addStatement("final String answer = sc.nextLine()")
     .beginControlFlow("if(!answer.equalsIgnoreCase(\"y\") && !answer.equalsIgnoreCase(\"n\"))") 
     .addStatement("throw new $T()",InputMismatchException.class)
     .endControlFlow()
     .beginControlFlow("if(!answer.equalsIgnoreCase(\"y\"))") 
       .addStatement("break")
     .endControlFlow()
     .addStatement("System.out.print(\"BlockInstance (Options:GeneralPumpBlock): \")")      
     .addStatement("BlockInstance = sc.nextLine()")
     .beginControlFlow("if(!BlockInstance.equals(\"GeneralPumpBlock\"))")
     .addStatement("System.out.println(\"BlockInstance not valid, use options available.\")")
     .endControlFlow() 
     .beginControlFlow("else")
     .beginControlFlow("if(BlockInstance.equals(\"GeneralPumpBlock\"))")
     .addStatement("System.out.print(\"DataEntry (Options: pressure_information, rotation_speed, static_motor_information, dynamic_motor_information): \")")      
     .addStatement("DataEntry = sc.nextLine()")
     .beginControlFlow("if((!DataEntry.equals(\"pressure_information\"))||(!DataEntry.equals(\"dynamic_motor_information\")) ||(!DataEntry.equals(\"rotation_speed\")) ||(!DataEntry.equals(\"static_motor_information\")))")
     .addStatement("System.out.println(\"Data Entry not valid, use options available.\")")
     .endControlFlow() 
     //.beginControlFlow("else")
     .addStatement("break")
     .endControlFlow() 
     .endControlFlow() 
     .endControlFlow()
     
     .beginControlFlow("catch (final $T | $T ex)",InputMismatchException.class,NumberFormatException.class) 
     .addStatement("System.out.println(\"Wrong input, try again!\")")
     //.beginControlFlow("catch (final $T e)",ArrowheadException.class)
     //.addStatement("System.out.println(\"Arrowhead exception ocurried!\")")
     //.endControlFlow()
     .endControlFlow()
     .endControlFlow()
     .addStatement("String subpath=\"/\"+BlockInstance+\"/\"+DataEntry")
      .addStatement("System.out.println(\"Subpath: \"+subpath)")
     .addStatement("return subpath");
     
     
     
             
             
             
     MethodSpec commandLine = BcommandLine.build();
             return commandLine;
    }//END METHOD commandLine
    
    
    
    
}// END CLASS
