package com.magna.fairnessAction.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.magna.fairnessAction.Entity.SectionEntity;
import com.magna.fairnessAction.Entity.actionPointCountEntity;
import com.magna.fairnessAction.Entity.departmentDropEntity;
import com.magna.fairnessAction.Entity.departmentListEntity;
import com.magna.fairnessAction.Entity.empUserEntity;
import com.magna.fairnessAction.Entity.employeeListEntity;
import com.magna.fairnessAction.Entity.fairnessActionEntity;
import com.magna.fairnessAction.Entity.filemanagerEntity;
import com.magna.fairnessAction.Entity.sapmesrequestlist;
import com.magna.fairnessAction.Rowmapper.actionpointcountRowmapper;
import com.magna.fairnessAction.Rowmapper.departmentDropRowmapper;
import com.magna.fairnessAction.Rowmapper.departmentListRowmapper;
import com.magna.fairnessAction.Rowmapper.empUseRowmapper;
import com.magna.fairnessAction.Rowmapper.employeeListRowmapper;
import com.magna.fairnessAction.Rowmapper.fairnessActionRowmapper;
import com.magna.fairnessAction.Rowmapper.filemanagerRowmapper;
import com.magna.fairnessAction.Rowmapper.sapmesrequestRowmapper;
import com.magna.fairnessAction.Rowmapper.sectionDropRowmapper;
import com.magna.user.controller.UserController;

@Service
public class departmentListService {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	private String newIDCode = "";

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Value("${root_path}")
	private String pathToUpload;

	@Value("${spring.mail.username}")
	private String sender;

	@Value("${root_path_before}")
	private String pathToUploadBefore;

	public List<departmentListEntity> getDepartmentList(String dept) {
		List<departmentListEntity> dptrQurry = null;
		try {
			RowMapper<departmentListEntity> deptRowmapper = new departmentListRowmapper();
			if (dept.equalsIgnoreCase("GETALL")) {
				String FrQry = "select EMP.id AS ID,emp.Emp_Mail as Emp_Mail,UM.username as Emp_Code,emp.Emp_Name as Emp_Name,emp.Dept as Dept,emp.Emp_Type as Emp_Type, dmt.Dept_code as is_active from users_mst as um inner join employee_mst as emp on um.Dept = emp.Dept and um.emp_Code = emp.Emp_Code inner join dept_mst as dmt on emp.Dept = dmt.Department where emp.Emp_Type = '"
						+ 2 + "' and um.User_Type = '" + 2 + "' group by EMP.id;";
				dptrQurry = this.jdbcTemplate.query(FrQry, deptRowmapper);
			} else {
				String FrQry = "SELECT * FROM Employee_mst where Dept = ?";
				dptrQurry = this.jdbcTemplate.query(FrQry, deptRowmapper, dept);
			}
		} catch (Exception ex) {
			logger.error("getDepartmentList method exception" + ex);
		}
		return dptrQurry;
	}

	public List<departmentListEntity> getEmployeeDetails(String empType) {
		List<departmentListEntity> dptrQurry = null;
		try {
			RowMapper<departmentListEntity> deptRowmapper = new departmentListRowmapper();
			
			if(empType.equalsIgnoreCase("GETALL")) {
				String FrQry = "SELECT emp.ID AS ID,emp.Emp_Code as Emp_Code,emp.Emp_Mail as Emp_Mail,emp.is_active as is_active,dm.Dept_code as Dept,emp.Emp_Name as Emp_Name,emp.Emp_Type as Emp_Type FROM Employee_mst as emp inner join dept_mst as dm on dm.Department=emp.Dept";
				dptrQurry = this.jdbcTemplate.query(FrQry, deptRowmapper);
			}else {
				String FrQry = "SELECT * FROM Employee_mst where Emp_Type = ?";
				dptrQurry = this.jdbcTemplate.query(FrQry, deptRowmapper, empType);
			}

		

		} catch (Exception ex) {
			logger.error("getEmployeeDetails method exception" + ex);
		}
		return dptrQurry;
	}

	public String updateFairnessAction(String targetDate, String responsibility, String actionPoint, String depart,
			String section, String user, MultipartFile file, String file_status, String pointtype, String empCode) {
		String returnResponse = null;
		try {
			String beforeUP = "";
			String fixDate = null;
			if (!targetDate.equalsIgnoreCase("")) {
				fixDate = targetDate;
			}
			if (file_status.equalsIgnoreCase("1")) {
				String fileName = file.getOriginalFilename();
				String dirPath = "\\" + depart + "\\" + responsibility + "\\" + targetDate;
				Path theDir = Files.createDirectories(Paths.get(pathToUploadBefore + dirPath + "\\" + fileName));
				logger.info(file.getContentType());
				File save = new File(String.valueOf(theDir));
				file.transferTo(new File(String.valueOf(theDir)));
				beforeUP = dirPath + "\\" + fileName;
			}
			String getLastCode = "select username from users_mst where Dept = '" + user + "' and User_Type = '" + 2 + "' and emp_Code = '" + empCode + "'  ;";
			String lastCode = this.jdbcTemplate.queryForObject(getLastCode, String.class);
			int updatests = 0;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			String curdate = formatter.format(date);
			String FrupdateQry = "insert into fairness_action_mst (is_active, Date,Target_date,Action_point, Responsibility, Dept, Com_Section, Evdnce_File, Before_path, Point_Type) VALUES (?,?,?,?,?,?,?,?,?,?);";
			updatests = this.jdbcTemplate.update(FrupdateQry, 1, curdate, fixDate, actionPoint, responsibility, depart, section, lastCode, beforeUP, pointtype);
			if (updatests > 0) {
				returnResponse = "Success";
			} else {
				returnResponse = "Error";
			}
		} catch (Exception ex) {
			logger.error("updateFairnessAction method exception" + ex);
		}
		return returnResponse;
	}

	public List<fairnessActionEntity> updateFairnessTarget(String targetDate, String rowId, String status) {
		List<fairnessActionEntity> frQurry = null;
		try {
			int updatarg = 0;
			String FrupdateQry = "update fairness_action_mst set Target_date = ?, Target_Status = ? where ID = ?;";
			updatarg = this.jdbcTemplate.update(FrupdateQry, targetDate, status, rowId);
			if (updatarg > 0) {
				RowMapper<fairnessActionEntity> fairnessRowmapper = new fairnessActionRowmapper();
				String FrQry = "select fac.Date, fac.Before_path as Before_path, fac.Reject_remark as Reject_remark, cs.Com_Name as Com_Section, fac.comment as comment, cs.Sec_Type as Sec_Type,fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join employee_mst as em on em.Emp_Code= fac.Responsibility inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.ID=?";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper, rowId);

			} else {
				logger.error("updateFairnessTarget method exception");
			}
		} catch (Exception ex) {
			logger.error("updateFairnessTarget method exception" + ex);
		}
		return frQurry;
	}

	public String updateResponsePerson(String respn, String rowId, String status) {
		String frQurry = null;
		try {
			int updatarg = 0;
			if (status.equalsIgnoreCase("0")) {
				String getLastCode = "select Emp_Code from employee_mst where Emp_Name = '" + respn + "';";
				String lastCode = this.jdbcTemplate.queryForObject(getLastCode, String.class);
				String FrupdateQry = "update fairness_action_mst set Responsibility = ?, Resp_Status = '" + 1
						+ "' where ID = ?;";
				updatarg = this.jdbcTemplate.update(FrupdateQry, lastCode, rowId);
			} else {
				String FrupdateQry = "update fairness_action_mst set Responsibility = ?, Resp_Status = '" + 1
						+ "' where ID = ?;";
				updatarg = this.jdbcTemplate.update(FrupdateQry, respn, rowId);
			}

			if (updatarg > 0) {
				frQurry = "success";

			} else {
				frQurry = "error";
				logger.error("updateResponsePerson method exception");
			}
		} catch (Exception ex) {
			logger.error("updateResponsePerson method exception" + ex);
		}
		return frQurry;
	}

	public List<departmentDropEntity> getDepartmentDrop() {
		List<departmentDropEntity> dptrQurry = null;
		try {
			RowMapper<departmentDropEntity> deptRowmapper = new departmentDropRowmapper();
			String FrQry = "SELECT * FROM dept_mst;";
			dptrQurry = this.jdbcTemplate.query(FrQry, deptRowmapper);
		} catch (Exception ex) {
			logger.error("getDepartmentDrop method exception" + ex);
		}
		return dptrQurry;
	}
	
	public List<SectionEntity> getSectionList() {
		List<SectionEntity> dptrQurry = null;
		try {
			RowMapper<SectionEntity> secRowmapper = new sectionDropRowmapper();
			String FrQry = "SELECT * FROM community_section where is_active = '" + 1 + "';";
			dptrQurry = this.jdbcTemplate.query(FrQry, secRowmapper);
		} catch (Exception ex) {
			logger.error("getSectionList method exception" + ex);
		}
		return dptrQurry;
	}

	public String updateNewSection(String secName, String user, String secType) {
		String secUpdate = null;
		try {
			String preIndex = "CS00";
			String getLastCode = "select Com_Code from community_section order by id desc limit 1;";
			String lastCode = this.jdbcTemplate.queryForObject(getLastCode, String.class);
			if (!lastCode.equalsIgnoreCase("") && lastCode != null) {
				String[] part = lastCode.split("(?<=\\D)(?=\\d)");
				preIndex = preIndex + (Integer.parseInt(part[1]) + 1);
			}
			int updatarg = 0;
			String scUpdateQry = "INSERT INTO community_section (Com_Code,Com_Name,is_active,Creator,Sec_Type) VALUES (?,?,?,?,?);";
			updatarg = this.jdbcTemplate.update(scUpdateQry, preIndex, secName, 1, user, secType);
			if (updatarg > 0) {
				secUpdate = "success";
			} else {
				secUpdate = "error";
				logger.error("updateNewSection method exception");
			}
		} catch (Exception ex) {
			logger.error("updateNewSection method exception" + ex);
		}
		return secUpdate;
	}

	public String getRemoveSection(String secName) {
		String secUpdate = null;
		try {
			int updatarg = 0;
			String scUpdateQry = "UPDATE community_section SET is_active = '" + 0 + "' WHERE Com_Code = ?;";
			updatarg = this.jdbcTemplate.update(scUpdateQry, secName);
			if (updatarg > 0) {
				secUpdate = "success";
			} else {
				secUpdate = "error";
				logger.error("getRemoveSection method exception");
			}
		} catch (Exception ex) {
			logger.error("getRemoveSection method exception" + ex);
		}
		return secUpdate;
	}

	public String updateEvidenceFile(MultipartFile file, String dept, String target_date, String resp, String id,
			String comment) {
		String secUpdate = null;
		try {
			int updatarg = 0;
			String fileName = file.getOriginalFilename();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat formatter_blob = new SimpleDateFormat("yyyy/MM/dd HH:MM:SS");
			Date date = new Date();
			String curdate = formatter.format(date);
			String curdate_blob = formatter_blob.format(date);
			if (fileName.equalsIgnoreCase("blob")) {
				String mSec = curdate_blob.split(" ")[1];
				fileName = "blob_" + mSec.split(":")[1] + "_" + mSec.split(":")[2];
			}
			String file_type = file.getContentType();
			String dirPath = "\\" + dept + "\\" + resp + "\\" + target_date;
			Path theDir = Files.createDirectories(Paths.get(pathToUpload + dirPath + "\\" + fileName));
			logger.info(file.getContentType());
			File save = new File(String.valueOf(theDir));
			file.transferTo(new File(String.valueOf(theDir)));
			String scUpdateQry = "INSERT INTO filemanager\r\n"
					+ "(Mst_id,File_Type,File_Name,Source_Path,upload_date)\r\n" + "VALUES (? ,? ,? ,? ,? );";
			updatarg = this.jdbcTemplate.update(scUpdateQry, id, file_type, fileName, dirPath, curdate);
			if (updatarg > 0) {
				String updateAppQry = "update fairness_action_mst set App_Status = ?, comment = ?, Resp_Status = ?, Target_Status = ? where ID = ?;";
				int updateAppStatus = this.jdbcTemplate.update(updateAppQry, 1, comment, 1, 1, id);
				if (updateAppStatus > 0) {
					secUpdate = "success";
				}
			} else {
				secUpdate = "error";
				logger.error("updateEvidenceFile method exception");
			}
		} catch (Exception ex) {
			logger.error("updateEvidenceFile method exception" + ex);
		}
		return secUpdate;
	}

	public String getbase64Content(String rowId) {
		String result = null;
		try {
			List<filemanagerEntity> fileManagerResult = null;
			RowMapper<filemanagerEntity> fileRowmapper = new filemanagerRowmapper();
			String FrQry = "select * from filemanager where Mst_id = ? and Is_active = ?;";
			fileManagerResult = this.jdbcTemplate.query(FrQry, fileRowmapper, rowId, 1);
			String filePath = "";
			String fileName = "";
			String fileType = "";
			
			if(!fileManagerResult.isEmpty()) {
				filePath = fileManagerResult.get(0).getSource_path();
				fileName = fileManagerResult.get(0).getFile_name();
			    fileType = fileManagerResult.get(0).getFile_type();
			}
			
			byte[] fileContent = FileUtils.readFileToByteArray(new File(pathToUpload + filePath + "\\" + fileName));
			String encodedString = Base64.getEncoder().encodeToString(fileContent);
			result = "data:" + fileType + ";base64," + encodedString;
		} catch (Exception ex) {
			logger.error("getbase64Content method exception" + ex);
		}
		return result;
	}

	public String getUpdateApprove(String rowId) {
		String secUpdate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			String curdate = formatter.format(date);
			String getLastCode = "select App_active from fairness_action_mst WHERE ID = '" + rowId + "';";
			String empCode = this.jdbcTemplate.queryForObject(getLastCode, String.class);
			if (empCode.equalsIgnoreCase("0")) {
				String getComCode = "select Com_Section from fairness_action_mst WHERE ID = '" + rowId + "';";
				String comCode = this.jdbcTemplate.queryForObject(getComCode, String.class);
				String getdeptQuery = "select Creator from community_section WHERE Com_Code = '" + comCode + "';";
				String comHead = this.jdbcTemplate.queryForObject(getdeptQuery, String.class);
				String getEvedence = "select Evdnce_File from fairness_action_mst WHERE ID = '" + rowId + "';";
				String eveName = this.jdbcTemplate.queryForObject(getEvedence, String.class);
				int updatarg = 0;
				if (eveName.equalsIgnoreCase(comHead)) {
					String scUpdateQry = "UPDATE fairness_action_mst\r\n"
							+ "SET App_Status = ?, Closed_date = ?, is_active = ? WHERE ID = ?;";
					updatarg = this.jdbcTemplate.update(scUpdateQry, 2, curdate, 0, rowId);
					if (updatarg > 0) {
						secUpdate = "success";
					} else {
						secUpdate = "error";
						logger.error("getUpdateApprove method exception");
					}
				} else {
					String scUpdateQry = "UPDATE fairness_action_mst\r\n"
							+ "SET Evdnce_File = ?, App_active = ? WHERE ID = ?;";
					updatarg = this.jdbcTemplate.update(scUpdateQry, comHead, 1, rowId);
					if (updatarg > 0) {
						secUpdate = "success";
					} else {
						secUpdate = "error";
						logger.error("getUpdateApprove method exception");
					}
				}

			} else {
				int updatarg = 0;
				String scUpdateQry = "UPDATE fairness_action_mst\r\n"
						+ "SET App_Status = ?, Closed_date = ?, is_active = ? WHERE ID = ?;";
				updatarg = this.jdbcTemplate.update(scUpdateQry, 2, curdate, 0, rowId);
				if (updatarg > 0) {
					secUpdate = "success";
				} else {
					secUpdate = "error";
					logger.error("getUpdateApprove method exception");
				}
			}

		} catch (Exception ex) {
			logger.error("getUpdateApprove method exception" + ex);
		}
		return secUpdate;
	}

	public String getRejectApprove(String rowId, String reason) {
		String secUpdate = null;
		try {
			int updatarg = 0;
			String scUpdateQry = "UPDATE fairness_action_mst\r\n"
					+ "SET App_Status = ?, Reject_remark = ? WHERE ID = ?;";
			updatarg = this.jdbcTemplate.update(scUpdateQry, 0, reason, rowId);
			if (updatarg > 0) {

				String FrQry = "DELETE FROM filemanager\r\n" + "WHERE Mst_id = ?;";
				int FileResult = this.jdbcTemplate.update(FrQry, rowId);
				if (FileResult > 0) {
					secUpdate = "success";
					;
				} else {
					secUpdate = "error";
				}
			} else {
				secUpdate = "error";
				logger.error("getRejectApprove method exception");
			}
		} catch (Exception ex) {
			logger.error("getRejectApprove method exception" + ex);
		}
		return secUpdate;
	}

	public List<empUserEntity> getempUserList(String userType) {
		List<empUserEntity> dptrQurry = null;
		try {
			RowMapper<empUserEntity> userRowmapper = new empUseRowmapper();
			String FrQry = "SELECT * FROM users_mst where User_Type = '" + userType + "';";
			dptrQurry = this.jdbcTemplate.query(FrQry, userRowmapper);
		} catch (Exception ex) {
			logger.error("getempUserList method exception" + ex);
		}
		return dptrQurry;
	}

	public String getreturnListUpdate(String rowId, String dept) {
		String secUpdate = null;
		try {

			int updatarg = 0;
			String getComCode = "select Emp_Code from employee_mst WHERE Dept = '" + dept + "' and Emp_Type = '" + 2
					+ "' and is_active = '" + 1 + "' limit 1;";
			String empCode = this.jdbcTemplate.queryForObject(getComCode, String.class);
			String scUpdateQry = "UPDATE fairness_action_mst\r\n"
					+ "SET Return_Status = ?,Dept = ?, Responsibility = ? WHERE ID = ?;";
			updatarg = this.jdbcTemplate.update(scUpdateQry, 1, dept, empCode, rowId);
			if (updatarg > 0) {

				secUpdate = "success";

			} else {
				secUpdate = "error";
				logger.error("getreturnListUpdate method exception");
			}
		} catch (Exception ex) {
			logger.error("getreturnListUpdate method exception" + ex);
		}
		return secUpdate;
	}

	public String getUpdatereturnlist(String rowId, String comment) {
		String secUpdate = null;
		try {
			int updatarg = 0;
			String getComCode = "select Com_Section from fairness_action_mst WHERE ID = '" + rowId + "';";
			String comCode = this.jdbcTemplate.queryForObject(getComCode, String.class);
			String getdeptQuery = "select Creator from community_section WHERE Com_Code = '" + comCode + "';";
			String comHead = this.jdbcTemplate.queryForObject(getdeptQuery, String.class);
			String scUpdateQry = "UPDATE fairness_action_mst\r\n"
					+ "SET Return_Status = ?, Evdnce_File = ?, Send_Back_Comment = ? WHERE ID = ?;";
			updatarg = this.jdbcTemplate.update(scUpdateQry, 0, comHead, comment, rowId);
			if (updatarg > 0) {

				secUpdate = "success";

			} else {
				secUpdate = "error";
				logger.error("getUpdatereturnlist method exception");
			}
		} catch (Exception ex) {
			logger.error("getUpdatereturnlist method exception" + ex);
		}
		return secUpdate;
	}

	public String getUpdateEmpDetail(String name, String deptCode, String dept, String mail, String empType,
			String status, String code) {
		String secUpdate = null;
		try {
			int updateEmp = 0;
			String preIndex = "E00";
			String getLastCode = "select Emp_Code from employee_mst order by id desc limit 1;";
			String lastCode = this.jdbcTemplate.queryForObject(getLastCode, String.class);
			if (!lastCode.equalsIgnoreCase("") && lastCode != null) {
				String[] part = lastCode.split("(?<=\\D)(?=\\d)");
				preIndex = preIndex + (Integer.parseInt(part[1]) + 1);
			}
			if (code.equalsIgnoreCase("")) {
				String scUpdateQry = "INSERT INTO employee_mst\r\n"
						+ "(Emp_Code,Emp_Name,Dept,is_active,Emp_Type,Emp_Mail)\r\n" + "VALUES (?,?,?,?,?,?);";
				updateEmp = this.jdbcTemplate.update(scUpdateQry, preIndex, name, deptCode, status, empType, mail);
			} else {
				String scUpdateQry = "UPDATE employee_mst SET\r\n"
						+ "Emp_Name = ?,Dept = ?,is_active = ?,Emp_Type = ?,Emp_Mail = ? \r\n" + " where Emp_Code = '"
						+ code + "';";
				updateEmp = this.jdbcTemplate.update(scUpdateQry, name, deptCode, status, empType, mail);
			}
			if (updateEmp > 0) {

				secUpdate = "success";

			} else {
				secUpdate = "error";
				logger.error("getUpdateEmpDetail method exception");
			}
		} catch (Exception ex) {
			logger.error("getUpdateEmpDetail method exception" + ex);
		}
		return secUpdate;
	}

	public String getPathbase64Content(String path) {
		String result = null;
		try {

			byte[] fileContent = FileUtils.readFileToByteArray(new File(pathToUploadBefore + "\\" + path));
			String encodedString = Base64.getEncoder().encodeToString(fileContent);
			result = "data:image/jpg;base64," + encodedString;
		} catch (Exception ex) {
			logger.error("getPathbase64Content method exception" + ex);
		}
		return result;
	}

	public String getUpdatedActionPoint(String action, String id) {
		String secUpdate = null;
		try {
			int updateActionSub = 0;
			String scUpdateQry = "UPDATE fairness_action_mst\r\n" + "SET Action_point = ? WHERE ID = ?;";
			updateActionSub = this.jdbcTemplate.update(scUpdateQry, action, id);
			if (updateActionSub > 0) {

				secUpdate = "success";

			} else {
				secUpdate = "error";
				logger.error("getUpdatedActionPoint method exception");
			}
		} catch (Exception ex) {
			logger.error("getUpdatedActionPoint method exception" + ex);
		}
		return secUpdate;
	}

	public String Getupdatesapmeschangerequest(String proName, String project, String createdby, String impby,
			String busisco, String EncdeptCode, String dateval, String hdrid, String scowork, String bugfix,
			String excpro, String prepro, String benofch, String desc, String critical, String low, String empName, String normal,
			String high) throws ParseException {
		String secUpdate = null;
		String datevalFormat = "";
		
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date FmtDate = inputDateFormat.parse(dateval); 
		datevalFormat = outputDateFormat.format(FmtDate);
		
		try {
			int updateActionHdr = 0;
			int updateActionDtl = 0;
			Date date = new Date();
			String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//			String datevalue = new SimpleDateFormat("yyyy-MM-dd").format(dateval);
			String[] finalDate = modifiedDate.split("-");
			String preIndex = "TKT";
			String getCountID = "select count(*) from CHANGE_REGUEST_SAP_MES_HDR where REQUEST_DATE = curdate();";
			int lastCount = this.jdbcTemplate.queryForObject(getCountID, Integer.class);
			if (lastCount == 0) {
				newIDCode = preIndex + finalDate[0] + finalDate[1] + finalDate[2] + "R0" + 1;
			} else {
				String getCurID = "select PROJECT_ID from CHANGE_REGUEST_SAP_MES_HDR where REQUEST_DATE = curdate() order by ID desc limit 1;";
				String curId = this.jdbcTemplate.queryForObject(getCurID, String.class);
				String[] part = curId.split("R");
				newIDCode = part[0] + "R0" + (Integer.parseInt(part[1]) + 1);
			}
			int alreadyExist = 0;
			if (!hdrid.equalsIgnoreCase("")) {
				String alrExist = "select count(*) from CHANGE_REGUEST_SAP_MES_HDR where ID = '" + hdrid + "' ";
				alreadyExist = this.jdbcTemplate.queryForObject(alrExist, Integer.class);
			}

			if (alreadyExist > 0) {
				String scUpdateHdrQry = "UPDATE change_reguest_sap_mes_hdr SET PROJECT_NAME = ?, CREATED_BY = ?, REQUEST_DATE = ?, PROJECT = ?, IMPLEMENT_BY = ?, DEPARTMENT_CODE = ? , EMPNAME = ? WHERE ID = '"
						+ hdrid + "' ";
				updateActionHdr = this.jdbcTemplate.update(scUpdateHdrQry, proName, createdby, dateval, project, impby, empName,
						EncdeptCode);
				String scUpdateDtlQry = "UPDATE change_reguest_sap_mes_dtl SET CRITICAL = ?, HIGH = ?, NORMAL = ?, LOW = ?, BUSINESS_SCOPE = ? , BUG_FIX = ? , DESCRIPTION = ? , SCOPE_WORK = ? , EXISTING = ?, PREPARED = ?, BENEFITS = ? WHERE HDR_ID = '"
						+ hdrid + "' ";
				updateActionDtl = this.jdbcTemplate.update(scUpdateDtlQry, critical, high, normal, low, busisco, bugfix,
						desc, scowork, excpro, prepro, benofch);

				if (updateActionHdr > 0 && updateActionDtl > 0) {
					secUpdate = String.valueOf(hdrid) + "_" + "200" + "_" + newIDCode;
				} else {
					secUpdate = "400";
				}
			} else {
				String scInsertQry = "INSERT INTO change_reguest_sap_mes_hdr(PROJECT_NAME,PROJECT_ID,CREATED_BY,REQUEST_DATE,PROJECT,IMPLEMENT_BY,DEPARTMENT_CODE,EMPNAME)\r\n"
						+ "VALUES(? ,? ,? ,? ,? ,?, ?, ?);";
				GeneratedKeyHolder holder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement preparedStmt = con.prepareStatement(scInsertQry,
								Statement.RETURN_GENERATED_KEYS);
						preparedStmt.setString(1, proName);
						preparedStmt.setString(2, newIDCode);
						preparedStmt.setString(3, createdby);
						preparedStmt.setDate(4, java.sql.Date.valueOf(dateval));
						preparedStmt.setString(5, project);
						preparedStmt.setString(6, impby);
						preparedStmt.setString(7, EncdeptCode);
						preparedStmt.setString(8, empName);
						return preparedStmt;
					}
				}, holder);

				int primaryKey = holder.getKey().intValue();

				if (primaryKey > 0) {
					String insertqueryDtl = "INSERT INTO change_reguest_sap_mes_dtl\r\n"
							+ "(HDR_ID,CRITICAL,HIGH,NORMAL,LOW,BUSINESS_SCOPE,BUG_FIX,DESCRIPTION,SCOPE_WORK,EXISTING,PREPARED,BENEFITS)\r\n"
							+ "VALUES(? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? );";
					int insertDtl = this.jdbcTemplate.update(insertqueryDtl, primaryKey, critical, high, normal, low,
							busisco, bugfix, desc, scowork, excpro, prepro, benofch);
					if (insertDtl > 0) {
						secUpdate = String.valueOf(primaryKey) + "_" + "200" + "_" + newIDCode;
						sendMailSAPMESUpdateReq(proName, project, createdby, datevalFormat,EncdeptCode, 0, newIDCode);
					} else {
						secUpdate = "400";
					}
				} else {
					secUpdate = "400";
					logger.error("Getupdatesapmeschangerequest method exception");
				}

			}

		} catch (Exception ex) {
			logger.error("Getupdatesapmeschangerequest method exception" + ex);
		}
		return secUpdate;
	}

	public String updateSAPfile(MultipartFile file, String id, String projectId, String type) {
		String secUpdate = null;
		try {
			int updatarg = 0;
			String fileName = file.getOriginalFilename();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String curdate = formatter.format(date);
			String file_type = file.getContentType();
			String dirPath = "\\" + projectId + "\\" + curdate + "_" + type;
			Path theDir = Files.createDirectories(Paths.get(pathToUpload + dirPath + "\\" + fileName));
			logger.info(file.getContentType());
			File save = new File(String.valueOf(theDir));
			file.transferTo(new File(String.valueOf(theDir)));
			String scUpdateQry = "INSERT INTO filemanager_sap\r\n"
					+ "(Mst_id,File_Type,File_Name,Source_Path,upload_date)\r\n" + "VALUES (? ,? ,? ,? ,? );";
			updatarg = this.jdbcTemplate.update(scUpdateQry, id, file_type, fileName, dirPath, curdate);
			if (updatarg > 0) {
				secUpdate = "Success";
			}

		} catch (Exception ex) {
			logger.error("updateEvidenceFile method exception" + ex);
		}
		return secUpdate;
	}

	public List<String> getbase64ContentforSap(String Id) {
		List<String> result = new ArrayList<>();
		try {
			logger.info("getbase64Content Method Start");
			String ExistingImgfilePath = null;
			String ExistingImgfileName = null;
			String ExistingImgfileType = null;

			String PreparedImgfilePath = null;
			String PreparedImgfileName = null;
			String PreparedImgfileType = null;
			
			List<filemanagerEntity> fileManagerResult = null;
			RowMapper<filemanagerEntity> fileRowmapper = new filemanagerRowmapper();
			String FrQry = "select * from filemanager_sap where Mst_id = ? and Is_active = ?;";
			fileManagerResult = this.jdbcTemplate.query(FrQry, fileRowmapper, Id, 1);
			if (fileManagerResult.isEmpty() == false) {
				ExistingImgfilePath = fileManagerResult.get(0).getSource_path();
				ExistingImgfileName = fileManagerResult.get(0).getFile_name();
				ExistingImgfileType = fileManagerResult.get(0).getFile_type();

				PreparedImgfilePath = fileManagerResult.get(1).getSource_path();
				PreparedImgfileName = fileManagerResult.get(1).getFile_name();
				PreparedImgfileType = fileManagerResult.get(1).getFile_type();

				byte[] fileContent1 = FileUtils
						.readFileToByteArray(new File(pathToUpload + ExistingImgfilePath + "\\" + ExistingImgfileName));
				byte[] fileContent2 = FileUtils
						.readFileToByteArray(new File(pathToUpload + PreparedImgfilePath + "\\" + PreparedImgfileName));
				String encodedString1 = Base64.getEncoder().encodeToString(fileContent1);
				String encodedString2 = Base64.getEncoder().encodeToString(fileContent2);
				String existing = "data:" + ExistingImgfileType + ";base64," + encodedString1;
				String prepared = "data:" + PreparedImgfileType + ";base64," + encodedString2;
				result.add(existing);
				result.add(ExistingImgfileType);
				result.add(ExistingImgfilePath);
				
				result.add(prepared);
				result.add(PreparedImgfileType);
				result.add(PreparedImgfilePath);
			} else {
				result.add("");
				result.add("");
			}

			logger.info("getbase64Content Method End");
		} catch (Exception ex) {
			logger.error("getbase64Content method exception" + ex);
		}
		return result;
	}

	public List<sapmesrequestlist> getsapmesrequestlist(String Dept) {
		List<sapmesrequestlist> dptrQurry = null;
		try {
			String FrQry = null;
			RowMapper<sapmesrequestlist> userRowmapper = new sapmesrequestRowmapper();
			if (Dept.equalsIgnoreCase("getall")) {// User
				FrQry = "select * from change_reguest_sap_mes_hdr as hdr inner join change_reguest_sap_mes_dtl as dtl on hdr.ID = dtl.HDR_ID inner join dept_mst_sap as dms on dms.Department = hdr.DEPARTMENT_CODE where hdr.DEPARTMENT_CODE like '%%' order by hdr.ID desc ";
				dptrQurry = this.jdbcTemplate.query(FrQry, userRowmapper);
			} else {
				FrQry = "select * from change_reguest_sap_mes_hdr as hdr inner join change_reguest_sap_mes_dtl as dtl on hdr.ID = dtl.HDR_ID inner join dept_mst_sap as dms on dms.Department = hdr.DEPARTMENT_CODE where hdr.DEPARTMENT_CODE= ? order by hdr.ID desc ";
				dptrQurry = this.jdbcTemplate.query(FrQry, userRowmapper, Dept);
			}

		} catch (Exception ex) {
			logger.error("getsapmesrequestlist method exception" + ex);
		}
		return dptrQurry;
	}

	public String Updatestatuspointforsap(String status, String id, String rej) {
		String secUpdate = null;
		try {
			int updateActionSub = 0;
			String scUpdateQry = "UPDATE change_reguest_sap_mes_hdr\r\n"
					+ "SET IS_ACTIVE = ?,REJ_REASON = ? WHERE ID = '" + id + "';";
			updateActionSub = this.jdbcTemplate.update(scUpdateQry, status, rej);
			if (updateActionSub > 0) {

				secUpdate = "success";

			} else {
				secUpdate = "error";
				logger.error("Updatestatuspointforsap method exception");
			}
		} catch (Exception ex) {
			logger.error("Updatestatuspointforsap method exception" + ex);
		}
		return secUpdate;
	}

	public List<employeeListEntity> getEmployeelist(String Dept) {
		List<employeeListEntity> dptrQurry = null;
		try {
			
			if(!Dept.equalsIgnoreCase("null")) {
				String FrQry = null;
				RowMapper<employeeListEntity> userRowmapper = new employeeListRowmapper();
				FrQry = "select ems.Id, ems.Emp_Code, ems.Emp_Name, ems.Dept, ems.Emp_Type, ems.Emp_Mail, ems.is_active, dms.Dept_code from employee_mst_sap as ems inner join dept_mst_sap dms on ems.Dept = dms.Department where dms.Department = '"
						+ Dept + "'  ";
				dptrQurry = this.jdbcTemplate.query(FrQry, userRowmapper);
			}else {
				logger.info("getEmployeelist"+Dept);
			}
			

		} catch (Exception ex) {
			logger.error("getEmployeeList method exception" + ex);
		}
		return dptrQurry;
	}

	public String sendMailSAPMESUpdateReq(String proName, String project, String createdby, String dateval,
			String EncdeptCode, int id, String prjctID) {

		try {
			logger.info("sendMailSAPMESAddReq method start");
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			String comQry = "SELECT id as count, Emp_Mail as dept FROM employee_mst_sap where Emp_type = '"+ 0 +"' order by id;";
			List<actionPointCountEntity> deptPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			for (actionPointCountEntity dept : deptPoint) {
//			String lastCode = "E0121";
//			String getEmailid = "select Emp_Mail from employee_mst_sap where Emp_Code = '" + dept + "';";
			
			String recptMail = dept.getDept();
//			String recptMail = this.jdbcTemplate.queryForObject(getEmailid, String.class);
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
					+ "                                     <br>\r\n"
					+ "										<div>\r\n"
					+ "											<table id=\"customers\">\r\n"
					+ "												<tr>\r\n"
					+ "													<th colspan=4>\r\n"
					+ "														Change Request Notification</th>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Project ID</td>\r\n"
					+ "													<td>" + prjctID + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Project Name</td>\r\n"
					+ "													<td>" + proName + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Project</td>\r\n"
					+ "													<td>" + project + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Created By</td>\r\n"
					+ "													<td>" + createdby + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Request Date</td>\r\n"
					+ "													<td>" + dateval + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Department</td>\r\n"
					+ "													<td>" + EncdeptCode + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Link</td>\r\n"
					+ "													<td><a href='http://10.215.8.94:5005'>Magna Project Dashboard</a></td>\r\n"
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
			if(id == 0)  //insert
			{
				mailMessage.setSubject("One Change Request Added");	
			}else if(id == 1) {
				mailMessage.setSubject("Update Request Added");
			}
			
			Transport.send(mailMessage);
			}
			logger.info("Mail Sent Successfully...");

			return "Mail Sent Successfully...";
		}

		catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Mail Sent Successfully...";
		}
	}

	public String sendMailSAPMESAppRejReq(String prjctID, String proName, String project, String dateval, int id, String empCode) {
		List<employeeListEntity> qry = null;
		try {
			String recptMail = null;
			String Dept = null;
			String Empname = null;
			
			RowMapper<employeeListEntity> userRowmapper = new employeeListRowmapper();
			logger.info("sendMailSAPMESAppRejReq method start");
			String getEmailid = "select ems.Id, ems.Emp_Code, ems.Emp_Name, ems.Dept, ems.Emp_Type, ems.Emp_Mail, ems.is_active, dms.Dept_code from employee_mst_sap as ems inner join dept_mst_sap dms on ems.Dept = dms.Department  where ems.Emp_Code = '"+empCode+"';";
			qry = this.jdbcTemplate.query(getEmailid, userRowmapper);
			if(!qry.isEmpty()) {
				recptMail = qry.get(0).getMail();
				Dept= qry.get(0).getDept_code();
				Empname = qry.get(0).getEmp();
			}
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
					+ "                                     <br>\r\n"
					+ "										<div>\r\n"
					+ "											<table id=\"customers\">\r\n"
					+ "												<tr>\r\n"
					+ "													<th colspan=4>\r\n"
					+ "														Change Request Notification</th>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Project ID</td>\r\n"
					+ "													<td>" + prjctID + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Project Name</td>\r\n"
					+ "													<td>" + proName + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Project</td>\r\n"
					+ "													<td>" + project + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Approved By</td>\r\n"
					+ "													<td>RAMESH R</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Employee Name</td>\r\n"
					+ "													<td>"+ Empname +"</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Department</td>\r\n"
					+ "													<td>"+ Dept +"</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Request Date</td>\r\n"
					+ "													<td>" + dateval + "</td>\r\n"
					+ "												</tr>\r\n"
					+ "												<tr>\r\n"
					+ "													<td>Link</td>\r\n"
					+ "													<td><a href='http://10.215.8.94:5005'>Magna Project Dashboard</a></td>\r\n"
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
			if(id == 1)  //approve
			{
				mailMessage.setSubject("Your Request has been Approved");	
			}else if(id == 0) { // reject
				mailMessage.setSubject("Your Request has been Rejected");
			}
			
			Transport.send(mailMessage);
			logger.info("Mail Sent Successfully...");

			return "Mail Sent Successfully...";
		}

		catch (Exception e) {
			logger.error("Error while Sending Mail" + e);
			return "Mail Sent Successfully...";
		}
	}

}
