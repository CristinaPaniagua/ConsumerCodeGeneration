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
    ArrayList<ElementsPayload> elements_request ; 
    ArrayList<ElementsPayload> elements_response ;
    boolean request;
    boolean response;
    boolean param; 
    ArrayList<Param> parameters;
    
    
    public InterfaceMetadata() {
    }

    public InterfaceMetadata(String Protocol, String PathResource, String Method, String Mediatype, String ID, String complexType_response, String complexType_request, ArrayList<ElementsPayload> elements_request, ArrayList<ElementsPayload> elements_response, boolean request, boolean response, boolean param, ArrayList<Param> parameters ) {
        this.Protocol = Protocol;
        this.PathResource = PathResource;
        this.Method = Method;
        this.Mediatype = Mediatype;
        this.ID = ID;
        this.complexType_response = complexType_response;
        this.complexType_request = complexType_request;
        this.elements_request = elements_request;
        this.elements_response = elements_response;
        this.request = request;
        this.response = response;
        this.param=param; 
        this.parameters=parameters;
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

    public ArrayList<ElementsPayload> getElements_request() {
        return elements_request;
    }

    public ArrayList<ElementsPayload> getElements_response() {
        return elements_response;
    }

    public void setElements_request(ArrayList<ElementsPayload> elements_request) {
        this.elements_request = elements_request;
    }

    public void setElements_response(ArrayList<ElementsPayload> elements_response) {
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

    public boolean getRequest() {
        return request;
    }

    public boolean getResponse() {
        return response;
    }



    public void setRequest(boolean request) {
        this.request = request;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public boolean isParam() {
        return param;
    }

    public void setParam(boolean param) {
        this.param = param;
    }

    public ArrayList<Param> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Param> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "InterfaceMetadata{" + "Protocol=" + Protocol + ", PathResource=" + PathResource + ", Method=" + Method + ", Mediatype=" + Mediatype + ", ID=" + ID + ", complexType_response=" + complexType_response + ", complexType_request=" + complexType_request + ", elements_request=" + elements_request + ", elements_response=" + elements_response + ", request=" + request + ", response=" + response + ", param=" + param + ", parameters=" + parameters + '}';
    }



    
    
   
  

    
    
 







  

}
