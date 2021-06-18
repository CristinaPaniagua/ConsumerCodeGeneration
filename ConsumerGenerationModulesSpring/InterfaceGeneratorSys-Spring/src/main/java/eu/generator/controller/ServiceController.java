package eu.generator.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.generator.codgen.Generator;
import java.sql.Timestamp;

@RestController
@RequestMapping("/codgen")
public class ServiceController {
	
	//=================================================================================================
	// members

	
	

	//=================================================================================================
	// methods

	

	
	//-------------------------------------------------------------------------------------------------
	 @GetMapping(value="/test", produces = MediaType.TEXT_PLAIN_VALUE)
            public String index() {

                return "Ready to go";
                }
	
	//-------------------------------------------------------------------------------------------------
	
            @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody public String generate(@RequestBody final RequestForm RF) {
	 try{
         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
          long tstart =timestamp.getTime();
        Generator.startGeneration(RF);
        Timestamp timestampEnd = new Timestamp(System.currentTimeMillis());
        long tend = timestampEnd.getTime();
           System.out.println((tend-tstart));
		return "Generation complete";
        } catch (Exception e) {
		e.printStackTrace();	
                return "Generation aborted. ERROR";
                }
	}
         //-------------------------------------------------------------------------------------------------   
}
