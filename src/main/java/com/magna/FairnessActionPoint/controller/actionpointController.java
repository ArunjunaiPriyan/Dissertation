package com.magna.FairnessActionPoint.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.magna.fairnessAction.Entity.fairnessActionEntity;
import com.magna.fairnessAction.service.fairnessActionService;

@Controller
@ResponseBody
@RequestMapping("/")
public class actionpointController {

	@Autowired
	private fairnessActionService fairnessActionService;

	Logger logger = LoggerFactory.getLogger(actionpointController.class);

	@CrossOrigin
	@PostMapping(value = "/getfairactiondata")
	public List<fairnessActionEntity> getfairactiondata(@RequestParam("dept") String dept,
			@RequestParam("isactive") String isactive, @RequestParam("userName") String userName) {

		List<fairnessActionEntity> result = null;
		try {
			result = fairnessActionService.getfairactiondata(dept, isactive, userName);
		} catch (Exception ex) {
			logger.error("getfairactiondata method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getfairactiondataApp")
	public List<fairnessActionEntity> getfairactiondataApp(@RequestParam("dept") String dept) {

		List<fairnessActionEntity> result = null;
		try {
			result = fairnessActionService.getfairactiondataApp(dept);
		} catch (Exception ex) {
			logger.error("getfairactiondataApp method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getfairactiondataReturn")
	public List<fairnessActionEntity> getfairactiondataReturn(@RequestParam("user") String user) {

		List<fairnessActionEntity> result = null;
		try {
			result = fairnessActionService.getfairactiondataReturn(user);
		} catch (Exception ex) {
			logger.error("getfairactiondataReturn method exception" + ex);
		}
		return result;
	}

	// @Scheduled(fixedDelay = 1000, initialDelay = 1000)
	@CrossOrigin
	@PostMapping(value = "/getOverallDetails")
	public Map<String, HashMap<String, Integer>> getOverallDetails() {

		Map<String, HashMap<String, Integer>> result = null;
		try {
			result = fairnessActionService.getOverallDetails();
		} catch (Exception ex) {
			logger.error("getOverallDetails method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getFilteredOverallDetails")
	public Map<String, HashMap<String, Integer>> getFilteredOverallDetails(@RequestParam("dept") String dept,
			@RequestParam("month") String month, @RequestParam("section") String section) {

		Map<String, HashMap<String, Integer>> result = null;
		try {
			result = fairnessActionService.getFilteredOverallDetails(dept, month, section);
		} catch (Exception ex) {
			logger.error("getFilteredOverallDetails method exception" + ex);
		}
		return result;
	}

}
