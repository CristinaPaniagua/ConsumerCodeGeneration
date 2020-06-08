/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

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
     .addStatement("System.out.print(\"BlockInstance(Options: EquipmentInfo, HydraulicPumpBlock, TemperatureSensorBlock): \")")      
     .addStatement("BlockInstance = sc.nextLine()")
     .beginControlFlow("if(!BlockInstance.equals(\"EquipmentInfo\") && !BlockInstance.equals(\"HydraulicPumpBlock\") && !BlockInstance.equals(\"TemperatureSensorBlock\"))")
     .addStatement("System.out.println(\"BlockInstance not valid, use options available.\")")
     .endControlFlow() 
     .beginControlFlow("else")
     .beginControlFlow("if(BlockInstance.equals(\"TemperatureSensorBlock\"))")
     .addStatement("System.out.print(\"DataEntry (Option: temperature ): \")")      
     .addStatement("DataEntry = sc.nextLine()")
     .addStatement("break")
     .endControlFlow() 
     .beginControlFlow("if(BlockInstance.equals(\"EquipmentInfo\"))")
     .addStatement("System.out.print(\"DataEntry (Options: manufacturer, device_type, serial_number):\")")      
     .addStatement("DataEntry = sc.nextLine()")
     .addStatement("break")
     .endControlFlow() 
     .beginControlFlow("if(BlockInstance.equals(\"HydraulicPumpBlock\") )")
     .addStatement("System.out.print(\"DataEntry (Options: fluid_temperature, output_pressure):\")")      
     .addStatement("DataEntry = sc.nextLine()")
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
