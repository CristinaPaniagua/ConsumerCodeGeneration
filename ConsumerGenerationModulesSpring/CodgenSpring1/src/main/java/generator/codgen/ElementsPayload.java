/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import java.util.ArrayList;

/**
 *
 * @author cripan
 */
public class ElementsPayload {
     ArrayList<String[]> elements;

    public ElementsPayload() {
    }

     
     
    public ElementsPayload(ArrayList<String[]> elements) {
        this.elements = elements;
    }

    public ArrayList<String[]> getElements() {
        return elements;
    }

    public void setElements(ArrayList<String[]> elements) {
        this.elements = elements;
    }
     
    
     
     
     
     
}
