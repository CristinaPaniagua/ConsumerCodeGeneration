package ai.aitia.demo.car_provider.controller;

import Codgen_provider.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aitia.demo.dto.CarRequestDTO;
import com.aitia.demo.dto.CarResponseDTO;

import Codgen_provider.CarProviderConstants;
import ai.aitia.demo.car_provider.database.DTOConverter;
import ai.aitia.demo.car_provider.database.InMemoryCarDB;
import ai.aitia.demo.car_provider.entity.Car;
import eu.arrowhead.common.exception.BadPayloadException;
import generator.codgen.Generator;

@RestController
@RequestMapping("/codgen")
public class ServiceController {
	
	//=================================================================================================
	// members

	
	
	@Autowired
	private InMemoryCarDB carDB;

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
		
	Generator.startGeneration(RF);
		return "Generation complete";
	}
         //-------------------------------------------------------------------------------------------------   
}
