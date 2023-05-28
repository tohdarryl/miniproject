package com.example.server.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.models.Account;

import com.example.server.services.AccountService;
import com.example.server.utils.PlaceUtils;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private AccountService accSvc;

	@Autowired
	private PlaceUtils placeUtils;

	@PostMapping("/signin")
	public ResponseEntity<String> generateJwtToken(@RequestBody String payload) {
		log.info(">>>> inside login {}", payload);
		try {
			return accSvc.login(payload);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>("Server failed to process payload", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/signup")
	public ResponseEntity<Account> signup(@RequestBody Account account) {
		try {
			Account a = account;
			Boolean saved = accSvc.saveUser(a);
			System.out.println("Account id: " + a.getAccountId());
			System.out.println("Account saved: " + saved);

			if (saved) {
				return new ResponseEntity<>(a, HttpStatus.CREATED);
			} else {
				// "Account failed to create."
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		} catch (Exception ex) {
			// "Server failed to process saveAccount"
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/checkToken")
	public ResponseEntity<String> checkToken() {
		try {
			return accSvc.checkToken();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestBody String payload){
		log.info(">>>> inside forgot password {}", payload);
		JsonObject obj = placeUtils.jsonStringToJsonObj(payload);
		String email = obj.getString("email");
		try {
			return accSvc.forgotPassword(email);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody String payload) {
		log.info(">>>> inside change password {}", payload);
		JsonObject obj = placeUtils.jsonStringToJsonObj(payload);
		String email = obj.getString("email");
		String oldPassword = obj.getString("oldPassword");
		String newPassword = obj.getString("newPassword");

		try {
			return accSvc.changePassword(email, oldPassword, newPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/users/{query}")
	public ResponseEntity<String> getUsers(@PathVariable(required = true) String query) {
		System.out.println("Query >>> " + query);
		String q = query;
		String firstName = "";
		String lastName = "";

		if (q.matches(".*\\s.*")) {
			String[] splitStr = q.split("\\s+");
			firstName = splitStr[0];
			lastName = splitStr[1];

			System.out.println(firstName);
			System.out.println(lastName);
		} else {
			firstName = q;

			System.out.println(firstName);
			System.out.println(lastName);
		}

		List<Account> aList = this.accSvc.findByName(firstName, lastName);

		// Build JsonArray for response
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (Account a : aList) {
			a.setPassword("CONFIDENTIAL");
			arrBuilder.add(a.toJSON());

		}
		JsonArray result = arrBuilder.build();

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(result.toString());
	}

	// @PostMapping("/register")
	// public ResponseEntity<Account> createAccount(@RequestBody Account account) {
	// 	try {
	// 		Account a = account;
	// 		Boolean saved = accSvc.save(a);
	// 		System.out.println("Account id: " + a.getAccountId());
	// 		System.out.println("Account saved: " + saved);

	// 		if (saved) {
	// 			return new ResponseEntity<>(a, HttpStatus.CREATED);
	// 		} else {
	// 			// "Account failed to create."
	// 			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	// 		}
	// 	} catch (Exception ex) {
	// 		// "Server failed to process saveAccount"
	// 		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// 	}

	// }

	// @GetMapping("/login/{credentials}")
	// public ResponseEntity<String> getAccDetails(@PathVariable(required = true) String credentials) {
	// 	String cred = credentials;
	// 	String[] splitStr = cred.split("\\s+");
	// 	String email = splitStr[0];
	// 	String password = splitStr[1];

	// 	System.out.println(email);
	// 	System.out.println(password);

	// 	Account acc = this.accSvc.findByCred(email, password);

	// 	// JsonObject result = acc.toJSON();
	// 	JsonObject result = null;
	// 	JsonObjectBuilder objBuilder = Json.createObjectBuilder();
	// 	objBuilder.add("details", acc.toJSON());
	// 	result = objBuilder.build();

	// 	System.out.println(result);
	// 	return ResponseEntity
	// 			.status(HttpStatus.OK)
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.body(result.toString());

	// }

}
