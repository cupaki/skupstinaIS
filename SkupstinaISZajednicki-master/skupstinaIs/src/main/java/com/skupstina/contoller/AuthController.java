package com.skupstina.contoller;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.xml.bind.JAXBException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.skupstina.model.users.Korisnici.Korisnik;
import com.skupstina.model.users.User;
import com.skupstina.service.KorisnikService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	KorisnikService korisnikService;

	@Autowired
	private HttpSession sesison;
	
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> registerUser(@RequestBody String json)
			throws JAXBException, IOException {
		Korisnik korisnik = null;
		korisnik = korisnikService.createKorisnik(json);
		if (korisnikService.saveToDB(korisnik)) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
		}

	}
	
	@RequestMapping(value = "/login", method= RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Korisnik loginUser(@RequestBody String loginParam) throws JAXBException, IOException {
		JSONObject params = new JSONObject(loginParam);
		String username = params.getString("username");
		String password = params.getString("password");
		
		
		
		Korisnik korisnik = korisnikService.getUserFromDB(username, password);
		sesison.setAttribute("korisnik", korisnik);
		
		
		if (korisnik != null) {
			sesison.setAttribute("korisnik", korisnik);
			return korisnik;
		} else {
			return korisnik;
		}
		
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> logout() {
		sesison.invalidate();
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/loggedUser", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Korisnik getLoggedUser() {
		Korisnik korisnik = (Korisnik)sesison.getAttribute("korisnik");
		
		return korisnik;
	}
}
