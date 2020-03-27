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
public class InterfaceMetadata {
    String Protocol;
    String PathResource;
    String Method;
    String Mediatype;
    String ID;
    String complexType_response;
    String complexType_request;
    ArrayList<String[]> elements_request ; 
    ArrayList<String[]> elements_response ;
    public InterfaceMetadata() {
    }

    public InterfaceMetadata(String Protocol, String PathResource, String Method, String Mediatype, String ID, String complexType_response, String complexType_request, ArrayList<String[]> elements_request, ArrayList<String[]> elements_response) {
        this.Protocol = Protocol;
        this.PathResource = PathResource;
        this.Method = Method;
        this.Mediatype = Mediatype;
        this.ID = ID;
        this.complexType_response = complexType_response;
        this.complexType_request = complexType_request;
        this.elements_request = elements_request;
        this.elements_response = elements_response;
    }

   



    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setProtocol(String Protocol) {
        this.Protocol = Protocol;
    }

    public void setPathResource(String PathResource) {
        this.PathResource = PathResource;
    }

    public void setMethod(String Method) {
        this.Method = Method;
    }

    public String getProtocol() {
        return Protocol;
    }

    public String getPathResource() {
        return PathResource;
    }

    public String getMethod() {
        return Method;
    }

    public void setMediatype(String Mediatype) {
        this.Mediatype = Mediatype;
    }

    public String getMediatype() {
        return Mediatype;
    }

    public ArrayList<String[]> getElements_request() {
        return elements_request;
    }

    public ArrayList<String[]> getElements_response() {
        return elements_response;
    }

    public void setElements_request(ArrayList<String[]> elements_request) {
        this.elements_request = elements_request;
    }

    public void setElements_response(ArrayList<String[]> elements_response) {
        this.elements_response = elements_response;
    }

    public String getComplexType_response() {
        return complexType_response;
    }

    public String getComplexType_request() {
        return complexType_request;
    }

    public void setComplexType_response(String complexType_response) {
        this.complexType_response = complexType_response;
    }

    public void setComplexType_request(String complexType_request) {
        this.complexType_request = complexType_request;
    }

    @Override
    public String toString() {
        return "InterfaceMetadata{" + "Protocol=" + Protocol + ", PathResource=" + PathResource + ", Method=" + Method + ", Mediatype=" + Mediatype + ", ID=" + ID + ", complexType_response=" + complexType_response + ", complexType_request=" + complexType_request + ", elements_request=" + elements_request + ", elements_response=" + elements_response + '}';
    }

    
    
 







  

}
