package com.cec.sbss.service;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cec.sbss.config.ConfigConstants;
import com.cec.sbss.model.EchoResponseModel;

@RestController
@RequestMapping("/echo")
public class EchoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EchoService.class);

	@Autowired
	@Qualifier("request.config")
	private Properties requestConfig;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EchoResponseModel> echo(@RequestParam(value = "value", required = false) String value) {

		EchoResponseModel response;

		if (value == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		LOGGER.debug("value param was %s", value);

		response = new EchoResponseModel(requestConfig.getProperty(ConfigConstants.EXAMPLE_PROP) + value);

		return new ResponseEntity<>(response, HttpStatus.OK);

	}



}