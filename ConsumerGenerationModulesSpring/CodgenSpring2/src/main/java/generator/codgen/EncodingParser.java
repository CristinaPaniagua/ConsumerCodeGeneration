/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator.codgen;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import  com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.squareup.javapoet.CodeBlock;
/**
 *
 * @author cripan
 */
public class EncodingParser {

   
    
    
    /**
     * Creates a Jackson object mapper based on a media type. It supports JSON,
     * JSON Smile, XML, YAML and CSV.
     * 
     * @return The Jackson object mapper.
     */
 public static CodeBlock createObjectMapper(String MediaType) {
     
      CodeBlock.Builder BmapperBlock = CodeBlock.builder();
           
    String mapperCode="";
    
    if (MediaType.equalsIgnoreCase("JSON")) {
       BmapperBlock
               .addStatement("$T jsonFactory = new JsonFactory()",JsonFactory.class)
               .addStatement("$T objMapper=new ObjectMapper(jsonFactory)",ObjectMapper.class);
                
    } else if (MediaType.equalsIgnoreCase("JSON_SMILE")) {
         BmapperBlock
                  .addStatement("$T smileFactory = new SmileFactory()",SmileFactory.class)
                  .addStatement("ObjectMapper objMapper=new ObjectMapper(smileFactory)",ObjectMapper.class);
        
    } else if (MediaType.equalsIgnoreCase("CBOR")) {
         BmapperBlock
                .addStatement( "$T cborFactory = new CBORFactory()",CBORFactory.class)
               .addStatement("$T objMapper=new ObjectMapper(cborFactory)",ObjectMapper.class);
        

    } else if (MediaType.equalsIgnoreCase("XML")) { //TO DO: CHECK IF I NEED THE XMLFACTORY OR NOT
       //javax.xml.stream.XMLInputFactory xif = XmlFactoryProvider.newInputFactory();
        //xif.setProperty(javax.xml.stream.XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, isExpandingEntityRefs());
        //xif.setProperty(javax.xml.stream.XMLInputFactory.SUPPORT_DTD, isExpandingEntityRefs());
        //xif.setProperty(javax.xml.stream.XMLInputFactory.IS_VALIDATING, isValidatingDtd());
        //javax.xml.stream.XMLOutputFactory xof = XmlFactoryProvider.newOutputFactory();
        //XmlFactory xmlFactory = new XmlFactory(xif, xof);
       // xmlFactory.configure(Feature.AUTO_CLOSE_TARGET, false);
        //result = new XmlMapper(xmlFactory);
        
         BmapperBlock.addStatement( "$T objMapper=new $T()",ObjectMapper.class, XmlMapper.class);

    } else if (MediaType.equalsIgnoreCase("YAML")) {
          BmapperBlock
                 .addStatement( " $T yamlFactory = new YAMLFactory()",YAMLFactory.class)
                .addStatement( "$T objMapper=new ObjectMapper(yamlFactory)",ObjectMapper.class);
        
    } else if (MediaType.equalsIgnoreCase("CSV")) {
        BmapperBlock
                .addStatement("$T csvFactory = new CsvFactory()",CsvFactory.class)
                .addStatement( "$T objMapper=new $T(csvFactory))",ObjectMapper.class,CsvMapper.class);
        
  
    } else {
         BmapperBlock.addStatement("$T objMapper=new ObjectMapper()",ObjectMapper.class);
    }
    
    
    return  BmapperBlock.build();
}
    
}
