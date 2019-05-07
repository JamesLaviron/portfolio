package jmcheynier.apps.portfolio.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jmcheynier.apps.portfolio.models.Message;
import jmcheynier.apps.portfolio.services.SAPService;



@RestController
@RequestMapping("/api/v1/sap")
public class SAPController {

	@Autowired
	SAPService SAPService;
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


	@GetMapping("/dialog/test")
	public String TEST() throws IOException {
		
		return "unplugged";

	}



}