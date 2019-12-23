package com.skupstina.contoller;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.skupstina.model.Amandman;
import com.skupstina.model.Propis;
import com.skupstina.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	
	@Autowired
	private SearchService searchService;

	@RequestMapping(value="/amandmani", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Amandman> searchAmandmani(@RequestBody String json) throws JAXBException {
		List<Amandman> amandmani = searchService.searchAmandmani(json);
		return amandmani;	
		
	}
	@RequestMapping(value="/propisi", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Propis> searchPropis(@RequestBody String json) throws JAXBException {
		List<Propis> propisi = searchService.searchPropis(json);
		System.out.println("Find " + propisi.size());
		return propisi;
	}
}
