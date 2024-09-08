package com.magna.emailconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@ResponseBody
@RequestMapping("/")
public class EmailController {

	@Autowired
	private EmailServiceImpl emailService;

	@PostMapping("/sendMail")
	public String sendMail(@RequestBody EmailDetails details) {
		String status = emailService.sendSimpleMail(details);

		return status;
	}

	@PostMapping("/sendMailaction")
	public String sendMailaction(@RequestBody EmailDetails details) {
		String status = emailService.sendMailaction(details);

		return status;
	}

	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(@RequestBody EmailDetails details) {
		String status = emailService.sendMailWithAttachment(details);

		return status;
	}

	@Scheduled(cron = "0 0 6 * * MON")
//	@Scheduled(cron = "2 * * * * *")
	@PostMapping("/sendMonthlyReport")
	public String sendMonthlyReport() {
		String status = emailService.sendMonthlyReport();

		return status;
	}

	@Scheduled(cron = "0 0 6 * * MON")
	@Scheduled(cron = "0 0 1 * * *")
//	@Scheduled(cron = "2 * * * * *")
	@PostMapping("/sendWeeklyReport")
	public String sendWeeklyReport() {
		String status = emailService.sendWeeklyReport();

		return status;
	}

	@Scheduled(cron = "30 3 * * * ?")
	@PostMapping("/senddailyMailRemainder")
	public String senddailyMailRemainder() {
		String status = emailService.senddailyMailRemainder();

		return status;
	}

	@Scheduled(cron = "30 3 * * * ?")
//	@Scheduled(cron = "2 * * * * *")
	@PostMapping("/senddailyMailRemain")
	public String senddailyMailRemain() {
		String status = emailService.senddailyMailRemain();

		return status;
	}
	
	@Scheduled(cron = "30 3 * * * ?")
//	@Scheduled(cron = "2 * * * * *")
	@PostMapping("/senddailyMailRemainderForApprove")
	public String senddailyMailRemainderForApprove() {
		String status = emailService.senddailyMailRemainderForApprove();

		return status;
	}
	
	@Scheduled(cron = "30 8 * * * ?")
//	@Scheduled(cron = "2 * * * * *")
	@PostMapping("/sendDailyReport")
	public String sendDailyReport() {
		String status = emailService.sendDailyReport();

		return status;
	}

   
}