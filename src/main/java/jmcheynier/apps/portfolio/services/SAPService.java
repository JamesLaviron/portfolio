package jmcheynier.apps.portfolio.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jmcheynier.apps.portfolio.models.SAP.conversationalAI.DialogRequest;
import jmcheynier.apps.portfolio.models.SAP.conversationalAI.DialogResponse;
import jmcheynier.apps.portfolio.models.SAP.conversationalAI.Message;
import jmcheynier.apps.portfolio.models.SAP.conversationalAI.MessageText;

@Service
public class SAPService {
	
	private Logger logger = LoggerFactory.getLogger(SAPService.class);

	@Autowired 
	ResourceLoader resourceLoader;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	SocketService SocketService;

	@Value("${api.sap.dialog.url}")
	private String apiSAPDialogUrl;

	@Value("${api.sap.secret}")
	private String apiSAPSecret;

	public List<Message> sendDialogRequestV2(DialogRequest dialogRequest) {
		
		
		logger.error("coucou");
		List<Message> listMessage = new ArrayList<Message>();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiSAPSecret);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity <DialogRequest> entity = new HttpEntity<DialogRequest>(dialogRequest, headers);

		ResponseEntity<DialogResponse> response = null;
		try {
			response = restTemplate.exchange(apiSAPDialogUrl, HttpMethod.POST, entity, DialogResponse.class);
		}catch(Exception e) {
			MessageText m = new MessageText();
			m.setContent(e.getStackTrace().toString());
			listMessage.add(m);
		}finally {
			MessageText m2 = new MessageText();
			m2.setContent("finally");
			listMessage.add(m2);
		}
		if(response.getStatusCode().equals(HttpStatus.OK)) {
			DialogResponse dialogResponse = response.getBody();
			listMessage.addAll(dialogResponse.getResults().getMessages());
		}
		return listMessage;


	}
	
	public void sendDialogRequestV3(String to, DialogRequest dialogRequest) {
		
		SocketService.sendPrivateMessageText(to, "1");
		
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, apiSAPSecret);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity <DialogRequest> entity = new HttpEntity<DialogRequest>(dialogRequest, headers);

		ResponseEntity<String> response = null;
		ResponseEntity<DialogResponse> response2 = null;
		SocketService.sendPrivateMessageText(to, "2");
		try {
			response = restTemplate.exchange(apiSAPDialogUrl, HttpMethod.POST, entity, String.class);
			SocketService.sendPrivateMessageText(to, response.getBody());
			SocketService.sendPrivateMessageText(to, "3");
			ObjectMapper objectMapper = new ObjectMapper();
			DialogResponse d = objectMapper.readValue(response.getBody(), DialogResponse.class); 
			MessageText m = (MessageText) d.getResults().getMessages().get(0);
			SocketService.sendPrivateMessageText(to, m.getContent());
			
		}catch(Exception e) {
			SocketService.sendPrivateMessageText(to, "4");
			SocketService.sendPrivateMessageText(to, e.getMessage());
			SocketService.sendPrivateMessageText(to, e.getStackTrace().toString());
			logger.error("COUCOU");
			logger.error(e.getMessage());
			e.printStackTrace();

			
		}finally {
			SocketService.sendPrivateMessageText(to, "5");
		}
		if(response.getStatusCode().equals(HttpStatus.OK)) {
			
			SocketService.sendPrivateMessageText(to, "6");			
		}


	}

}
