package com.magna.FairnessActionPoint.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.magna.fairnessAction.Entity.SectionEntity;
import com.magna.fairnessAction.Entity.departmentDropEntity;
import com.magna.fairnessAction.Entity.departmentListEntity;
import com.magna.fairnessAction.Entity.empUserEntity;
import com.magna.fairnessAction.Entity.employeeListEntity;
import com.magna.fairnessAction.Entity.fairnessActionEntity;
import com.magna.fairnessAction.Entity.sapmesrequestlist;
import com.magna.fairnessAction.service.departmentListService;

@Controller
@ResponseBody
@RequestMapping("/")
public class departmentListController {
	Logger logger = LoggerFactory.getLogger(departmentListController.class);

	@Autowired
	private departmentListService departmentListService;

	@CrossOrigin
	@PostMapping(value = "/getDepartmentList")
	public List<departmentListEntity> getDepartmentList(@RequestParam("dept") String dept) {

		List<departmentListEntity> result = null;
		try {
			result = departmentListService.getDepartmentList(dept);
		} catch (Exception ex) {
			logger.error("getDepartmentList method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getEmployeeDetails")
	public List<departmentListEntity> getEmployeeDetails(@RequestParam("empType") String empType) {

		List<departmentListEntity> result = null;
		try {
			result = departmentListService.getEmployeeDetails(empType);
		} catch (Exception ex) {
			logger.error("getEmployeeDetails method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getSectionList")
	public List<SectionEntity> getSectionList() {

		List<SectionEntity> result = null;
		try {
			result = departmentListService.getSectionList();
		} catch (Exception ex) {
			logger.error("getSectionList method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getDepartmentDrop")
	public List<departmentDropEntity> getDepartmentDrop() {

		List<departmentDropEntity> result = null;
		try {
			result = departmentListService.getDepartmentDrop();
		} catch (Exception ex) {
			logger.error("getDepartmentDrop method exception" + ex);
		}
		return result;
	}
	

	@CrossOrigin
	@PostMapping(value = "/getempUserList")
	public List<empUserEntity> getempUserList(@RequestParam("userType") String userType) {

		List<empUserEntity> result = null;
		try {
			result = departmentListService.getempUserList(userType);
		} catch (Exception ex) {
			logger.error("getempUserList method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/updateFairnessAction")
	public String updateFairnessAction(@RequestParam("targetDate") String targetDate,
			@RequestParam("responsibility") String responsibility, @RequestParam("actionPoint") String actionPoint,
			@RequestParam("depart") String depart, @RequestParam("section") String section,
			@RequestParam("user") String user, @RequestParam("file") MultipartFile file,
			@RequestParam("file_status") String file_status,
			@RequestParam("pointtype") String pointtype,
			@RequestParam("empCode") String empCode
			) {

		String result = "";
		try {
			result = departmentListService.updateFairnessAction(targetDate, responsibility, actionPoint, depart,
					section, user, file, file_status, pointtype, empCode);
		} catch (Exception ex) {
			logger.error("updateFairnessAction method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/updateFairnessTarget")
	public List<fairnessActionEntity> updateFairnessTarget(@RequestParam("targetDate") String targetDate,
			@RequestParam("rowId") String rowId, @RequestParam("status") String status) {

		List<fairnessActionEntity> result = null;
		try {
			result = departmentListService.updateFairnessTarget(targetDate, rowId, status);
		} catch (Exception ex) {
			logger.error("updateFairnessTarget method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/updateResponsePerson")
	public String updateResponsePerson(@RequestParam("respn") String respn, @RequestParam("status") String status,
			@RequestParam("rowId") String rowId) {

		String result = null;
		try {
			result = departmentListService.updateResponsePerson(respn, rowId, status);
		} catch (Exception ex) {
			logger.error("updateResponsePerson method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/updateNewSection")
	public String updateNewSection(@RequestParam("SecName") String SecName, @RequestParam("User") String User,
			@RequestParam("SecType") String SecType) {

		String result = null;
		try {
			result = departmentListService.updateNewSection(SecName, User, SecType);
		} catch (Exception ex) {
			logger.error("updateNewSection method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getRemoveSection")
	public String getRemoveSection(@RequestParam("SecName") String SecName) {

		String result = null;
		try {
			result = departmentListService.getRemoveSection(SecName);
		} catch (Exception ex) {
			logger.error("getRemoveSection method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/updateEvidenceFile")
	public String updateEvidenceFile(@RequestParam("file") MultipartFile file, @RequestParam("dept") String dept,
			@RequestParam("target_date") String target_date, @RequestParam("resp") String resp,
			@RequestParam("id") String id, @RequestParam("comment") String comment) {

		String result = null;
		try {
			result = departmentListService.updateEvidenceFile(file, dept, target_date, resp, id, comment);
		} catch (Exception ex) {
			logger.error("updateEvidenceFile method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getbase64Content")
	public String getbase64Content(@RequestParam("rowId") String rowId) {

		String result = null;
		try {
			result = departmentListService.getbase64Content(rowId);
		} catch (Exception ex) {
			logger.error("getbase64Content method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getPathbase64Content")
	public String getPathbase64Content(@RequestParam("path") String path) {

		String result = null;
		try {
			result = departmentListService.getPathbase64Content(path);
		} catch (Exception ex) {
			logger.error("getbase64Content method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getUpdateApprove")
	public String getUpdateApprove(@RequestParam("rowId") String rowId) {

		String result = null;
		try {
			result = departmentListService.getUpdateApprove(rowId);
		} catch (Exception ex) {
			logger.error("getUpdateApprove method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getRejectApprove")
	public String getRejectApprove(@RequestParam("rowId") String rowId, @RequestParam("reason") String reason) {

		String result = null;
		try {
			result = departmentListService.getRejectApprove(rowId, reason);
		} catch (Exception ex) {
			logger.error("getRejectApprove method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getreturnListUpdate")
	public String getreturnListUpdate(@RequestParam("rowId") String rowId, @RequestParam("dept") String dept) {

		String result = null;
		try {
			result = departmentListService.getreturnListUpdate(rowId, dept);
		} catch (Exception ex) {
			logger.error("getreturnListUpdate method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getUpdatereturnlist")
	public String getUpdatereturnlist(@RequestParam("rowId") String rowId, @RequestParam("comment") String comment) {

		String result = null;
		try {
			result = departmentListService.getUpdatereturnlist(rowId, comment);
		} catch (Exception ex) {
			logger.error("getUpdatereturnlist method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getUpdateEmpDetail")
	public String getUpdateEmpDetail(@RequestParam("name") String name, @RequestParam("deptCode") String deptCode,
			@RequestParam("dept") String dept, @RequestParam("mail") String mail,
			@RequestParam("empType") String empType, @RequestParam("status") String status,
			@RequestParam("code") String code) {

		String result = null;
		try {
			result = departmentListService.getUpdateEmpDetail(name, deptCode, dept, mail, empType, status, code);
		} catch (Exception ex) {
			logger.error("getUpdatereturnlist method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getUpdatedActionPoint")
	public String getUpdatedActionPoint(@RequestParam("action") String action, @RequestParam("id") String id) {

		String result = null;
		try {
			result = departmentListService.getUpdatedActionPoint(action, id);
		} catch (Exception ex) {
			logger.error("getUpdatedActionPoint method exception" + ex);
		}
		return result;
	}

	
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/Getupdatesapmeschangerequest")
	public String Getupdatesapmeschangerequest(
			@RequestParam("proName") String proName, @RequestParam("project") String project,
			@RequestParam("createdby") String createdby, @RequestParam("impby") String impby,
			 @RequestParam("busisco") String busisco, @RequestParam("EncdeptCode") String EncdeptCode,
			 @RequestParam("dateval") String dateval, @RequestParam("hdrid") String hdrid,
			@RequestParam("scowork") String scowork, @RequestParam("bugfix") String bugfix,
			@RequestParam("excpro") String excpro, @RequestParam("prepro") String prepro,
			@RequestParam("benofch") String benofch, @RequestParam("desc") String desc,
			@RequestParam("critical") String critical, @RequestParam("low") String low,
			@RequestParam("empName") String empName,
			@RequestParam("normal") String normal, @RequestParam("high") String high)
	       {

		String result = null;
		try {
			result = departmentListService.Getupdatesapmeschangerequest(proName, project, createdby, impby, busisco, EncdeptCode, dateval, hdrid, scowork, bugfix, excpro, prepro, benofch, desc, critical, low, empName, normal, high);
		} catch (Exception ex) {
			logger.error("Getupdatesapmeschangerequest method exception" + ex);
		}
		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/updateSAPfile")
	public String updateSAPfile(@RequestParam("file") MultipartFile file, @RequestParam("id") String id,
			@RequestParam("projectId") String projectId, @RequestParam("type") String type) {

		String result = null;
		try {
			result = departmentListService.updateSAPfile(file, id, projectId, type);
		} catch (Exception ex) {
			logger.error("updateEvidenceFile method exception" + ex);
		}
		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/getbase64ContentforSap")
	public List<String> getbase64ContentforSap(@RequestParam("Id") String Id) {

		List<String> result = null;
		try {
			result = departmentListService.getbase64ContentforSap(Id);
		} catch (Exception ex) {
			logger.error("getbase64Content method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value = "/getsapmesrequestlist")
	public List<sapmesrequestlist> getsapmesrequestlist(@RequestParam("Dept") String Dept) {

		List<sapmesrequestlist> result = null;
		try {
			result = departmentListService.getsapmesrequestlist(Dept);
		} catch (Exception ex) {
			logger.error("getsapmesrequestlist method exception" + ex);
		}
		return result;
	}

	@CrossOrigin
	@PostMapping(value="/UpdateStatusPointForSAP")
	public String Updatestatuspointforsap(@RequestParam("status") String status,@RequestParam("id") String id, @RequestParam("rej") String rej) {
		
		String result = null;
		try {
			result = departmentListService.Updatestatuspointforsap(status, id, rej);
		} catch(Exception ex) {
			logger.error("Updatestatuspointforsap method exception" + ex);
		}
		return result;
	}
	
	@CrossOrigin
	@PostMapping(value="/getEmployeelist")
	public List<employeeListEntity> getEmployeeList(@RequestParam("Dept") String Dept) {
		
		List<employeeListEntity> result = null;
		try {
			result = departmentListService.getEmployeelist(Dept);
		} catch(Exception ex) {
			logger.error("Updatestatuspointforsap method exception" + ex);
		}
		return result;
	}
	
	@CrossOrigin
	@PostMapping(value="/sendMailForUpdateRequest")
	public String sendMailSAPMESUpdateReq(@RequestParam("proName") String proName,@RequestParam("project") String project,@RequestParam("createdby") String createdby,@RequestParam("dateval") String dateval,@RequestParam("EncdeptCode") String EncdeptCode, @RequestParam("id") int id, @RequestParam("prjctID") String prjctID) {
		String status = departmentListService.sendMailSAPMESUpdateReq( proName,project,createdby,dateval,EncdeptCode,id,prjctID);

		return status;
	}
	
	@CrossOrigin
	@PostMapping(value="/sendMailForAppRejRequest")
	public String sendMailSAPMESAppRejReq(@RequestParam("prjctID") String prjctID, @RequestParam("proName") String proName,@RequestParam("project") String project,@RequestParam("dateval") String dateval,@RequestParam("id") int id,@RequestParam("empCode") String empCode) {
		String status = departmentListService.sendMailSAPMESAppRejReq( prjctID, proName,project,dateval,id,empCode);

		return status;
	}
}
