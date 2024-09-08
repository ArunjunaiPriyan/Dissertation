package com.magna.emailconfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.PatternDocument.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.magna.FairnessActionPoint.controller.actionpointController;
import com.magna.fairnessAction.Entity.actionPointApproveEntity;
import com.magna.fairnessAction.Entity.actionPointCountEntity;
import com.magna.fairnessAction.Rowmapper.actionPointApproveRowmapper;
import com.magna.fairnessAction.Rowmapper.actionpointcountRowmapper;

@Service
public class EmailServiceImpl {
	Logger logger = LoggerFactory.getLogger(actionpointController.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Value("${spring.datasource.url}")
	private String dburl;

	@Value("${spring.datasource.username}")
	private String dbrlname;

	@Value("${spring.datasource.password}")
	private String dburlpsd;

	public String sendSimpleMail(EmailDetails details) {

		try {
			String getLastCode = "";
			boolean ChckNum = isInt(details.getRecipient());
			if (ChckNum) {
				getLastCode = "select Responsibility from fairness_action_mst where ID = '" + details.getRecipient()
						+ "';";
			} else {
				getLastCode = "select emp_Code from users_mst where username = '" + details.getRecipient() + "';";
			}

			String lastCode = this.jdbcTemplate.queryForObject(getLastCode, String.class);
			String getEmailid = "select Emp_Mail from Employee_mst where Emp_Code = '" + lastCode + "';";
			String recptMail = this.jdbcTemplate.queryForObject(getEmailid, String.class);
			Properties properties = new Properties();
			properties.put("mail.transport.protocol", "smtp");
			properties.put("mail.smtp.host", "smtp.magna.global");
			properties.put("mail.smtp.port", "25");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.auth", "true");
			Authenticator authenticator = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
				}
			};
			Session session = Session.getInstance(properties, authenticator);
			Message mailMessage = new MimeMessage(session);
			mailMessage.setFrom(new InternetAddress(sender));
			mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
			mailMessage.setContent(details.getMsgBody(), "text/html; charset=utf-8");
			mailMessage.setSubject(details.getSubject());
			Transport.send(mailMessage);
			if (ChckNum) {
				String getAdminCode = "select Evdnce_File from fairness_action_mst where ID = '"
						+ details.getRecipient() + "';";
				String adminCode = this.jdbcTemplate.queryForObject(getAdminCode, String.class);
				String getEmplyCode = "select emp_Code from users_mst where username = '" + adminCode + "';";
				String lastEmplyCode = this.jdbcTemplate.queryForObject(getEmplyCode, String.class);
				String getAdminEmailid = "select Emp_Mail from Employee_mst where Emp_Code = '" + lastEmplyCode + "';";
				String adminrecptMail = this.jdbcTemplate.queryForObject(getAdminEmailid, String.class);

				Session sessionForAdmin = Session.getInstance(properties, authenticator);
				Message mailMessageforAdmin = new MimeMessage(sessionForAdmin);
				mailMessageforAdmin.setFrom(new InternetAddress(sender));
				mailMessageforAdmin.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(adminrecptMail, false));
				mailMessageforAdmin.setContent(details.getMsgBody(), "text/html; charset=utf-8");
				mailMessageforAdmin.setSubject(details.getSubject());
				Transport.send(mailMessage);
			}
			return "Mail Sent Successfully...";
		}

		catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}

	static boolean isInt(String s) {
		try {
			int i = Integer.parseInt(s);
			return true;
		}

		catch (NumberFormatException er) {
			return false;
		}
	}

	public String sendMailWithAttachment(EmailDetails details) {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;

		try {

			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());

			FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

			mimeMessageHelper.addAttachment(file.getFilename(), file);

			javaMailSender.send(mimeMessage);
			return "Mail sent Successfully";
		}

		catch (MessagingException e) {

			return "Error while sending mail!!!";
		}
	}

	public String sendMonthlyReport() {
		try {
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			String comQry = "SELECT id as count, Department as dept FROM dept_mst order by id;";
			List<actionPointCountEntity> deptPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			for (actionPointCountEntity dept : deptPoint) {
				String getEmailid = "select Emp_Mail from Employee_mst where Dept = '" + dept.getDept()
						+ "' and Emp_Type = '" + 2 + "' limit 1;";
				String recptMail = this.jdbcTemplate.queryForObject(getEmailid, String.class);
				Connection con = null;
				con = DriverManager.getConnection(dburl, dbrlname, dburlpsd);
				Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet results = stmt.executeQuery("SELECT \r\n" + "    cs.Com_Name AS Com_Section,\r\n"
						+ "    fac.comment AS comment,\r\n" + "    cs.Sec_Type AS Sec_Type,\r\n"
						+"     fac.Point_Type AS Point_Type, " + "    fac.Date AS Initiated_Date,\r\n"
						+ "    fac.Action_point AS Action_point,\r\n" + "    fac.Target_date AS Target_date,\r\n"
						+ "    fac.is_active AS is_active,\r\n" + "    fac.Closed_date AS Closed_date,\r\n"
						+ "    dm.Dept_code AS Dept,\r\n" + "    em.Emp_Name AS Responsibility\r\n" + "FROM\r\n"
						+ "    fairness_action_mst AS fac\r\n" + "        INNER JOIN\r\n"
						+ "    dept_mst AS dm ON dm.Department = fac.Dept\r\n" + "        INNER JOIN\r\n"
						+ "    employee_mst AS em ON em.Emp_Code = fac.Responsibility\r\n" + "        INNER JOIN\r\n"
						+ "    community_section AS cs ON cs.Com_Code = fac.Com_Section where fac.Dept = '"
						+ dept.getDept() + "'");
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Report");
				writeHeaderLine(sheet);
				writeDataLines(results, workbook, sheet);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				workbook.write(baos);
				workbook.close();
				InputStream reportXlsx = new ByteArrayInputStream(baos.toByteArray());
				System.out.println(workbook);
				Properties properties = new Properties();
				properties.put("mail.transport.protocol", "smtp");
				properties.put("mail.smtp.host", "smtp.magna.global");
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.starttls.enable", "true");
				properties.put("mail.smtp.auth", "true");
				Authenticator authenticator = new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
					}
				};
				Session session = Session.getInstance(properties, authenticator);
				Message mimeMessageHelper = new MimeMessage(session);
				mimeMessageHelper.setFrom(new InternetAddress(sender));
				mimeMessageHelper.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
				mimeMessageHelper.setText("Please find the Attachment for OverAll Report");
				mimeMessageHelper.setSubject("OverAll Action Point Report");
				mimeMessageHelper.setFileName("WeeklyReport.xlsx");
				mimeMessageHelper.setDataHandler(new DataHandler(new ByteArrayDataSource(reportXlsx,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")));
				Transport.send(mimeMessageHelper);
			}
			return "Mail Sent to Responsible person...";
		} catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}

	public String sendWeeklyReport() {
		try {
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			String comQry = "SELECT id as count, Emp_Mail as dept FROM management_admin_mst order by id;";
			List<actionPointCountEntity> deptPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			for (actionPointCountEntity dept : deptPoint) {
				String recptMail = dept.getDept();
				Connection con = null;
				con = DriverManager.getConnection(dburl, dbrlname, dburlpsd);
				Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet results = stmt.executeQuery("SELECT \r\n" + "    cs.Com_Name AS Com_Section,\r\n"
						+ "    fac.comment AS comment,\r\n" + "    cs.Sec_Type AS Sec_Type,\r\n"
						+"     fac.Point_Type AS Point_Type, "
						+ "    fac.Action_point AS Action_point,\r\n" + "    fac.Target_date AS Target_date,\r\n"
						+ "    fac.is_active AS is_active,\r\n" + "    fac.Closed_date AS Closed_date,\r\n"
						+ "    fac.Date AS Initiated_Date,\r\n"
						+ "    dm.Dept_code AS Dept,\r\n" + "    em.Emp_Name AS Responsibility\r\n" + "FROM\r\n"
						+ "    fairness_action_mst AS fac\r\n" + "        INNER JOIN\r\n"
						+ "    dept_mst AS dm ON dm.Department = fac.Dept\r\n" + "        INNER JOIN\r\n"
						+ "    employee_mst AS em ON em.Emp_Code = fac.Responsibility\r\n" + "        INNER JOIN\r\n"
						+ "    community_section AS cs ON cs.Com_Code = fac.Com_Section");
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Report");
				writeHeaderLine(sheet);
				writeDataLines(results, workbook, sheet);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				workbook.write(baos);
				workbook.close();
				InputStream reportXlsx = new ByteArrayInputStream(baos.toByteArray());
				System.out.println(workbook);
				Properties properties = new Properties();
				properties.put("mail.transport.protocol", "smtp");
				properties.put("mail.smtp.host", "smtp.magna.global");
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.starttls.enable", "true");
				properties.put("mail.smtp.auth", "true");
				Authenticator authenticator = new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
					}
				};
				Session session = Session.getInstance(properties, authenticator);
				Message mimeMessageHelper = new MimeMessage(session);
				mimeMessageHelper.setFrom(new InternetAddress(sender));
				mimeMessageHelper.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
				mimeMessageHelper.setText("Please find the Attachment for OverAll Report");
				mimeMessageHelper.setSubject("OverAll Action Point Report");
				mimeMessageHelper.setFileName("WeeklyOverAllReport.xlsx");
				mimeMessageHelper.setDataHandler(new DataHandler(new ByteArrayDataSource(reportXlsx,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")));
				Transport.send(mimeMessageHelper);
			}
			return "Mail Sent to Responsible person...";
		} catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}
	
	public String sendDailyReport() {
		try {
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			String mail = "MA005";
			String comQry = "SELECT id as count, Emp_Mail as dept FROM management_admin_mst where Emp_Code = '" + mail +"' ;";
			List<actionPointCountEntity> deptPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			for (actionPointCountEntity dept : deptPoint) {
				String recptMail = dept.getDept();
				Connection con = null;
				con = DriverManager.getConnection(dburl, dbrlname, dburlpsd);
				Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet results = stmt.executeQuery("SELECT \r\n" + "    cs.Com_Name AS Com_Section,\r\n"
						+ "    fac.comment AS comment,\r\n" + "    cs.Sec_Type AS Sec_Type,\r\n"
						+"     fac.Point_Type AS Point_Type, "
						+ "    fac.Action_point AS Action_point,\r\n" + "    fac.Target_date AS Target_date,\r\n"
						+ "    fac.is_active AS is_active,\r\n" + "    fac.Closed_date AS Closed_date,\r\n"
						+ "    fac.Date AS Initiated_Date,\r\n"
						+ "    dm.Dept_code AS Dept,\r\n" + "    em.Emp_Name AS Responsibility\r\n" + "FROM\r\n"
						+ "    fairness_action_mst AS fac\r\n" + "        INNER JOIN\r\n"
						+ "    dept_mst AS dm ON dm.Department = fac.Dept\r\n" + "        INNER JOIN\r\n"
						+ "    employee_mst AS em ON em.Emp_Code = fac.Responsibility\r\n" + "        INNER JOIN\r\n"
						+ "    community_section AS cs ON cs.Com_Code = fac.Com_Section where fac.Dept IN ('D0001','D0002') ");
				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Report");
				writeHeaderLine(sheet);
				writeDataLines(results, workbook, sheet);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				workbook.write(baos);
				workbook.close();
				InputStream reportXlsx = new ByteArrayInputStream(baos.toByteArray());
				System.out.println(workbook);
				Properties properties = new Properties();
				properties.put("mail.transport.protocol", "smtp");
				properties.put("mail.smtp.host", "smtp.magna.global");
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.starttls.enable", "true");
				properties.put("mail.smtp.auth", "true");
				Authenticator authenticator = new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
					}
				};
				Session session = Session.getInstance(properties, authenticator);
				Message mimeMessageHelper = new MimeMessage(session);
				mimeMessageHelper.setFrom(new InternetAddress(sender));
				mimeMessageHelper.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
				mimeMessageHelper.setText("Please find the Attachment for OverAll Report");
				mimeMessageHelper.setSubject("ME & Maintenance Action Point Report");
				mimeMessageHelper.setFileName("DailyActionPointReport.xlsx");
				mimeMessageHelper.setDataHandler(new DataHandler(new ByteArrayDataSource(reportXlsx,
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")));
				Transport.send(mimeMessageHelper);
			}
			return "Mail Sent to Responsible person...";
		} catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}

	private void writeHeaderLine(XSSFSheet sheet) {

		Row headerRow = sheet.createRow(0);

		
//		Cell headerCell = headerRow.createCell(0);
//		headerCell.setCellValue("Action Point");
//
//		headerCell = headerRow.createCell(1);
//		headerCell.setCellValue("Department");
//
//		headerCell = headerRow.createCell(2);
//		headerCell.setCellValue("Responsibility");
//
//		headerCell = headerRow.createCell(3);
//		headerCell.setCellValue("Target Date");
//
//		headerCell = headerRow.createCell(4);
//		headerCell.setCellValue("Sec_Type");
//
//		headerCell = headerRow.createCell(5);
//		headerCell.setCellValue("Closed_date");
//
//		headerCell = headerRow.createCell(6);
//		headerCell.setCellValue("Com_Section");
//
//		headerCell = headerRow.createCell(7);
//		headerCell.setCellValue("Review");
//
//		headerCell = headerRow.createCell();
//		headerCell.setCellValue("Status");
		
		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("Action Point");

		headerCell = headerRow.createCell(1);
		headerCell.setCellValue("Department");
		
		headerCell = headerRow.createCell(2);
		headerCell.setCellValue("Section Name");

		headerCell = headerRow.createCell(3);
		headerCell.setCellValue("Point Type");
		
		headerCell = headerRow.createCell(4);
		headerCell.setCellValue("Assigned");

		headerCell = headerRow.createCell(5);
		headerCell.setCellValue("Internal / External");

//		headerCell = headerRow.createCell(4);
//		headerCell.setCellValue("Assigned");

		headerCell = headerRow.createCell(6);
		headerCell.setCellValue("Initiated Date");

		headerCell = headerRow.createCell(7);
		headerCell.setCellValue("Target Date");

		headerCell = headerRow.createCell(8);
		headerCell.setCellValue("Review");

		headerCell = headerRow.createCell(9);
		headerCell.setCellValue("Status");
		
		headerCell = headerRow.createCell(10);
		headerCell.setCellValue("Overdue days");
	}

	private void writeDataLines(ResultSet result, XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
		int rowCount = 1;
		String comment = "";
		while (result.next()) {
			String courseName = result.getString("Action_point");
			String studentName = result.getString("Dept");
			String rating = result.getString("Responsibility");
			Timestamp timestamp = result.getTimestamp("Target_date");
			String secType = result.getString("Sec_Type");
			Timestamp initiateDate = result.getTimestamp("Initiated_Date");		
			String secName = result.getString("Com_Section");
			String review = result.getString("comment");
			String pointType = result.getString("Point_Type");
			
			if (result.getString("is_active").equalsIgnoreCase("1")) {
				comment = "Open";
			} else {
				comment = "Closed";
			}
			
			Date currDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("d-MM-yyyy");
			String curdate = formatter.format(currDate);
			String[] curdateArr = curdate.split("-");
			int curYear = Integer.parseInt(curdateArr[2]);
			int curMon = Integer.parseInt(curdateArr[1]);
			int curDay = Integer.parseInt(curdateArr[0]);
			
			String tardate = formatter.format(timestamp);
			String[] tarDateArr = tardate.split("-");
			int tarYear = Integer.parseInt(tarDateArr[2]);
			int tarMon = Integer.parseInt(tarDateArr[1]);
			int tarDay = Integer.parseInt(tarDateArr[0]);
			
			Calendar calendar1 = Calendar.getInstance();
	        calendar1.set(curYear, curMon - 1, curDay);

	        Calendar calendar2 = Calendar.getInstance();
	        calendar2.set(tarYear, tarMon - 1 , tarDay);

	        Date date1 = calendar1.getTime();
	        Date date2 = calendar2.getTime();


	        long millisecondsBetween = date1.getTime() - date2.getTime();
	        long millisec = Math.abs(millisecondsBetween);
	        
	        double daysBetween = Math.ceil(millisec / (1000 * 60 * 60 * 24));
			
			int duedays = 0;
			if(daysBetween > 0) {
				duedays = (int)daysBetween;
			}
	        
			Row row = sheet.createRow(rowCount++);
			
			int columnCount = 0;
			Cell cell = row.createCell(columnCount++);//Action point
			cell.setCellValue(courseName);

			cell = row.createCell(columnCount++);//section
			cell.setCellValue(studentName);
			
				
			cell = row.createCell(columnCount++);//Section
			cell.setCellValue(secName);
			
			cell = row.createCell(columnCount++);//Point type
			cell.setCellValue(pointType);

			cell = row.createCell(columnCount++);//Responsibility
			cell.setCellValue(rating);
			
			cell = row.createCell(columnCount++);//Internal/External
			cell.setCellValue(secType);
			
			cell = row.createCell(columnCount++);//Initiated Date
			CellStyle cellStyleCls = workbook.createCellStyle();
			CreationHelper creationHelperCls = workbook.getCreationHelper();
			cellStyleCls.setDataFormat(creationHelperCls.createDataFormat().getFormat("dd-MM-yyyy"));
			cell.setCellStyle(cellStyleCls);
            cell.setCellValue(initiateDate);
			
			cell = row.createCell(columnCount++);//Target Date
			CellStyle cellStyle = workbook.createCellStyle();
			CreationHelper creationHelper = workbook.getCreationHelper();
			cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
			cell.setCellStyle(cellStyle);
			cell.setCellValue(timestamp);

//			cell.setCellValue(timestampCls);

//			cell = row.createCell(columnCount);
//			cell.setCellValue(secName);

			cell = row.createCell(columnCount++);
			cell.setCellValue(review);

			cell = row.createCell(columnCount++);
			cell.setCellValue(comment);

			cell = row.createCell(columnCount);//count
			cell.setCellValue(duedays);
			
//			int columnCount = 0;
//			Cell cell = row.createCell(columnCount++);
//			cell.setCellValue(courseName);
//
//			cell = row.createCell(columnCount++);
//			cell.setCellValue(studentName);
//
//			cell = row.createCell(columnCount++);
//			cell.setCellValue(rating);
//			cell = row.createCell(columnCount++);
//
//			CellStyle cellStyle = workbook.createCellStyle();
//			CreationHelper creationHelper = workbook.getCreationHelper();
//			cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));
//			cell.setCellStyle(cellStyle);
//
//			cell.setCellValue(timestamp);
//
//			cell = row.createCell(columnCount);
//			cell.setCellValue(secType);
//
//			CellStyle cellStyleCls = workbook.createCellStyle();
//			CreationHelper creationHelperCls = workbook.getCreationHelper();
//			cellStyleCls.setDataFormat(creationHelperCls.createDataFormat().getFormat("dd-MM-yyyy"));
//			cell.setCellStyle(cellStyleCls);
//
//			cell.setCellValue(timestampCls);
//
//			cell = row.createCell(columnCount);
//			cell.setCellValue(secName);
//
//			cell = row.createCell(columnCount);
//			cell.setCellValue(review);
//
//			cell = row.createCell(columnCount);
//			cell.setCellValue(comment);

		}
	}

	public String sendMailaction(EmailDetails details) {
		try {

			String getEmailid = "select Emp_Mail from Employee_mst where Emp_Code = '" + details.getRecipient() + "';";
			String recptMail = this.jdbcTemplate.queryForObject(getEmailid, String.class);
			Properties properties = new Properties();
			properties.put("mail.transport.protocol", "smtp");
			properties.put("mail.smtp.host", "smtp.magna.global");
			properties.put("mail.smtp.port", "25");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.auth", "true");
			Authenticator authenticator = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
				}
			};
			Session session = Session.getInstance(properties, authenticator);
			Message mailMessage = new MimeMessage(session);
			mailMessage.setFrom(new InternetAddress(sender));
			mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
			mailMessage.setContent(details.getMsgBody(), "text/html; charset=utf-8");
			mailMessage.setSubject(details.getSubject());
			Transport.send(mailMessage);

			String getAdminCode = "select Dept from employee_mst where Emp_Code = '" + details.getRecipient() + "';";
			String adminCode = this.jdbcTemplate.queryForObject(getAdminCode, String.class);
			String getAdminEmailid = "select Emp_Mail from Employee_mst where Dept = '" + adminCode
					+ "' and Emp_Type = '" + 2 + "' and is_active = '" + 1 + "';";
			String adminrecptMail = this.jdbcTemplate.queryForObject(getAdminEmailid, String.class);

			Session sessionForAdmin = Session.getInstance(properties, authenticator);
			Message mailMessageforAdmin = new MimeMessage(sessionForAdmin);
			mailMessageforAdmin.setFrom(new InternetAddress(sender));
			mailMessageforAdmin.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminrecptMail, false));
			mailMessageforAdmin.setContent(details.getMsgBody(), "text/html; charset=utf-8");
			mailMessageforAdmin.setSubject(details.getSubject());
			Transport.send(mailMessage);

			return "Mail Sent Successfully...";
		}

		catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}

	public String senddailyMailRemainder() {
		try {
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			String comQry = "SELECT ID as count, Responsibility as dept FROM fairness_action_mst WHERE Target_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) and is_active = '"
					+ 1 + "';";
			List<actionPointCountEntity> deptPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			if (deptPoint.size() != 0) {
				for (actionPointCountEntity dept : deptPoint) {
					String getEmailid = "select Emp_Mail from Employee_mst where Dept = '" + dept.getDept()
							+ "' and Emp_Type = '" + 2 + "' limit 1;";
					String recptMail = this.jdbcTemplate.queryForObject(getEmailid, String.class);

					Properties properties = new Properties();
					properties.put("mail.transport.protocol", "smtp");
					properties.put("mail.smtp.host", "smtp.magna.global");
					properties.put("mail.smtp.port", "25");
					properties.put("mail.smtp.starttls.enable", "true");
					properties.put("mail.smtp.auth", "true");
					Authenticator authenticator = new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
						}
					};
					Session session = Session.getInstance(properties, authenticator);
					Message mailMessage = new MimeMessage(session);
					mailMessage.setFrom(new InternetAddress(sender));
					mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
					mailMessage.setContent(
							"<p>Hi,</p><br><h5>This is Remainder mail for some Action points of yours, getting closer to the <b>Target Date</b> <br> </h5> <p>Please check</p> <a href='http://10.215.8.94:8789/magna/main.html'>Magna Project Dashboard</a>",
							"text/html; charset=utf-8");
					mailMessage.setSubject("Action Point Target date Remainder");
					Transport.send(mailMessage);
				}
			}
			return "Mail Sent to Responsible person...";
		} catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}

	public String senddailyMailRemain() {
		try {
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			String comQry = "SELECT ID as count, Responsibility as dept FROM fairness_action_mst WHERE Target_date BETWEEN (NOW() - INTERVAL 10 DAY) AND NOW() and is_active = '"
					+ 1 + "';";
			List<actionPointCountEntity> deptPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			if (deptPoint.size() != 0) {
				for (actionPointCountEntity dept : deptPoint) {
					String getEmailid = "select Emp_Mail from Employee_mst where Dept = '" + dept.getDept()
							+ "' and Emp_Type = '" + 2 + "' limit 1;";
					String recptMail = this.jdbcTemplate.queryForObject(getEmailid, String.class);

					Properties properties = new Properties();
					properties.put("mail.transport.protocol", "smtp");
					properties.put("mail.smtp.host", "smtp.magna.global");
					properties.put("mail.smtp.port", "25");
					properties.put("mail.smtp.starttls.enable", "true");
					properties.put("mail.smtp.auth", "true");
					Authenticator authenticator = new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
						}
					};
					Session session = Session.getInstance(properties, authenticator);
					Message mailMessage = new MimeMessage(session);
					mailMessage.setFrom(new InternetAddress(sender));
					mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
					mailMessage.setContent(
							"<p>Hi,</p><br><h5>This is Remainder mail for some Action points of yours, getting <b>OverDue</b> <br> </h5> <p>Please check</p> <a href='http://10.215.8.94:8789/magna/main.html'>Magna Project Dashboard</a>",
							"text/html; charset=utf-8");
					mailMessage.setSubject("Action Point OverDue date Remainder");
					Transport.send(mailMessage);
				}
			}
			return "Mail Sent to Responsible person...";
		} catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}

 
	public String senddailyMailRemainderForApprove() {
		try {
			RowMapper<actionPointApproveEntity> opencountRowmapper = new actionPointApproveRowmapper();
			String comQry = "select em.Emp_Mail, fam.Action_point, fam.Target_date, cs.Com_Name from fairness_action_mst as fam inner join users_mst as um on fam.Evdnce_File = um.username inner join employee_mst as em on um.emp_Code = em.Emp_Code inner join community_section as cs on fam.Com_Section = cs.Com_Code  where fam.App_Status != '"+ 0 +"' and fam.is_active = '"+ 1 +"';";
			List<actionPointApproveEntity> deptPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			String EmpnameQry = "select fam.Action_point, em.Emp_Name , dpt.Dept_code  from fairness_action_mst as fam inner join employee_mst as em on fam.Responsibility = em.Emp_Code inner join dept_mst as dpt on fam.Dept = dpt.Department  where fam.App_Status != '" + 0 + "' and fam.is_active = '"+ 1 +"';";
			Map<String, Object> Empname = this.jdbcTemplate.queryForMap(EmpnameQry);
			String datevalFormat = "";
			
			if (deptPoint.size() != 0) {
				for (actionPointApproveEntity Emp : deptPoint) {
				
					SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
					SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
					Date FmtDate = inputDateFormat.parse(Emp.getTargetdate()); 
					datevalFormat = outputDateFormat.format(FmtDate);
					
					String recptMail = Emp.getEmpMail();

					Properties properties = new Properties();
					properties.put("mail.transport.protocol", "smtp");
					properties.put("mail.smtp.host", "smtp.magna.global");
					properties.put("mail.smtp.port", "25");
					properties.put("mail.smtp.starttls.enable", "true");
					properties.put("mail.smtp.auth", "true");
					Authenticator authenticator = new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication("Mcapl.Hr@magna.com", "Magna@098");
						}
					};
					Session session = Session.getInstance(properties, authenticator);
					Message mailMessage = new MimeMessage(session);
					mailMessage.setFrom(new InternetAddress(sender));
					mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recptMail, false));
					mailMessage.setContent("<!DOCTYPE html>\r\n" + "									<html>\r\n"
							+ "									<head>\r\n" + "										<style>\r\n"
							+ "											h5{\r\n"
							+ "									            margin-top:10px;\r\n"
							+ "									            margin-bottom:2px;\r\n"
							+ "									        }\r\n"
							+ "									        p{\r\n"
							+ "									            margin-top:5px\r\n"
							+ "									        }\r\n"
							+ "									        img{\r\n"
							+ "									            width:15%\r\n"
							+ "									        }\r\n"
							+ "											#customers {\r\n"
							+ "												font-family: Arial, Helvetica, sans-serif;\r\n"
							+ "												border-collapse: collapse;\r\n"
							+ "												width: 100%;\r\n"
							+ "											}\r\n"
							+ "											#customers td,\r\n"
							+ "											#customers th {\r\n"
							+ "												border: 1px solid #ddd;\r\n"
							+ "												padding: 8px;\r\n"
							+ "											}\r\n"
							+ "											#customers tr:nth-child(even) {\r\n"
							+ "												background-color: #f2f2f2;\r\n"
							+ "											}\r\n"
							+ "											#customers tr:hover {\r\n"
							+ "												background-color: #ddd;\r\n"
							+ "											}\r\n"
							+ "											#customers th {\r\n"
							+ "												padding-top: 12px;\r\n"
							+ "												padding-bottom: 12px;\r\n"
							+ "												text-align: left;\r\n"
							+ "												background-color: #5A5A5A;\r\n"
							+ "												color: white;\r\n"
							+ "												text-align: center;\r\n"
							+ "											}\r\n"
							+ "											#customers td{\r\n"
							+ "												text-align: center;\r\n"
							+ "											}\r\n"
							+ "										</style>\r\n"
							+ "									</head>\r\n" + "									<body>\r\n"
							+ "                                     <p>Hi,</p>\r\n"
							+ "                                     <p>&nbsp;&nbsp; The Responsible person has closed that points. He is waiting for your action.<span style='font-weight:bolder;color:red'>  Kindly Approve / Reject. </span>  </p>\r\n"
							+ "                                     <br>\r\n"
							+ "										<div>\r\n"
							+ "											<table id=\"customers\">\r\n"
							+ "												<tr>\r\n"
							+ "													<th colspan=4>\r\n"
							+ "														Action Point Notification</th>\r\n"
							+ "												</tr>\r\n"
							+ "												<tr>\r\n"
							+ "													<td>Action Point</td>\r\n"
							+ "													<td>" + Emp.getActionPoint() + "</td>\r\n"
							+ "												</tr>\r\n"
							+ "												<tr>\r\n"
							+ "													<td>Assigned to</td>\r\n"
							+ "													<td> "+ Empname.get("Emp_Name")+ "</td>\r\n"
							+ "												</tr>\r\n"
							+ "												<tr>\r\n"
							+ "													<td>Department</td>\r\n"
							+ "													<td> "+ Empname.get("Dept_code")+ "</td>\r\n"
							+ "												</tr>\r\n"
							+ "												<tr>\r\n"
							+ "													<td>Target Date</td>\r\n"
							+ "													<td>" + datevalFormat + "</td>\r\n"
							+ "												</tr>\r\n"
							+ "												<tr>\r\n"
							+ "													<td>Section</td>\r\n"
							+ "													<td>"+ Emp.getSecName()+"</td>\r\n"
							+ "												</tr>\r\n"
							+ "												<tr>\r\n"
							+ "													<td>Link</td>\r\n"
							+ "													<td><a href='http://10.215.8.94:8789/magna/'>Magna Project Dashboard</a></td>\r\n"
							+ "												</tr>\r\n"
							+ "											</table>\r\n"
							+ "										</div>\r\n"
							+ "										<div>\r\n"
							+ "											<br>\r\n"
							+ "											<p>Regards,</p>\r\n"
							+ "											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>\r\n"
							+ "											<p>\r\n"
							+ "												Plot No:7B, 8th Avenue, 1st Cross Road,<br>\r\n"
							+ "												Mahindra World City,<br>\r\n"
							+ "												Chengalpattu District - 603004.<br>\r\n"
							+ "												Tamil Nadu, India<br>\r\n"
							+ "											</p>\r\n"
							+ "											<img src=\"./img/Magna-Logo.png\" alt=\"Magna-Logo\">\r\n"
							+ "										</div>\r\n" + "									</body>\r\n"
							+ "									</html>", "text/html; charset=utf-8"
					);

					mailMessage.setSubject("Remainder For Approval / Reject");
					Transport.send(mailMessage);
				}
			}
			return "Mail Sent to Responsible person...";
		} catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Error while Sending Mail" + e;
		}
	}
}
