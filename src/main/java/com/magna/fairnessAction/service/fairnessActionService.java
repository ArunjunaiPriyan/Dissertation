package com.magna.fairnessAction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.magna.fairnessAction.Entity.actionOverallCountEntity;
import com.magna.fairnessAction.Entity.actionPointCountEntity;
import com.magna.fairnessAction.Entity.fairnessActionEntity;
import com.magna.fairnessAction.Rowmapper.actionOverallcountRowmapper;
import com.magna.fairnessAction.Rowmapper.actionpointcountRowmapper;
import com.magna.fairnessAction.Rowmapper.fairnessActionRowmapper;
import com.magna.user.controller.UserController;

@Service
public class fairnessActionService {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<fairnessActionEntity> getfairactiondata(String dept, String isactive, String userName) {
		List<fairnessActionEntity> frQurry = null;
		try {
			RowMapper<fairnessActionEntity> fairnessRowmapper = new fairnessActionRowmapper();
			if (isactive.equalsIgnoreCase("GETALL") && !dept.equalsIgnoreCase("D0017") && !dept.equalsIgnoreCase("1")) {
				String FrQry = "select fac.Date, fac.Reject_remark as Reject_remark, fac.Before_path as Before_path, cs.Com_Name as Com_Section, fac.comment as comment, cs.Sec_Type as Sec_Type,fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join employee_mst as em on em.Emp_Code= fac.Responsibility inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.Dept = ? and fac.App_Status in ('"+0+"','"+2+"') and fac.Target_date != '"+null+"' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' order by ID asc;";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper, dept);
			} else if(userName.equalsIgnoreCase("Admin") && dept.equalsIgnoreCase("1")) {
				String FrQry = "select fac.Date, fac.Reject_remark as Reject_remark, fac.Before_path as Before_path, cs.Com_Name as Com_Section, fac.comment as comment, cs.Sec_Type as Sec_Type,fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join employee_mst as em on em.Emp_Code= fac.Responsibility inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.App_Status in ('"+0+"','"+2+"') and fac.Target_date != '"+null+"' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' order by ID asc;";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper);
			} else if(dept.equalsIgnoreCase("D0017")) {
				String FrQry = "\r\n"
						+ "select fac.Date, fac.Reject_remark as Reject_remark, fac.Before_path as Before_path, cs.Com_Name as Com_Section,\r\n"
						+ "fac.comment as comment, cs.Sec_Type as Sec_Type, fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, \r\n"
						+ "fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, \r\n"
						+ "dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on fac.Com_Section = cs.Com_Code \r\n"
						+ "inner join employee_mst as em on fac.Responsibility = em.Emp_Code\r\n"
						+ " where fac.Com_Section in (select Com_Code from community_section where Creator in \r\n"
						+ "(select username from users_mst where Dept='" + dept +"')) and fac.is_active = '"+ 1 +"' and cs.is_active='"+1+"' order by ID asc";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper);
			}  else {
				String FrQry = "select fac.Date, fac.Reject_remark as Reject_remark, fac.Before_path as Before_path, cs.Com_Name as Com_Section, fac.comment as comment, cs.Sec_Type as Sec_Type,fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join employee_mst as em on em.Emp_Code= fac.Responsibility inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.Dept = ? and fac.is_active = ? and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' order by ID asc";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper, dept, isactive);
			}

		} catch (Exception ex) {
			logger.error("getfairactiondata method exception" + ex);
		}
		return frQurry;
	}

	public List<fairnessActionEntity> getfairactiondataApp(String dept) {
		List<fairnessActionEntity> frQurry = null;
		try {
			RowMapper<fairnessActionEntity> fairnessRowmapper = new fairnessActionRowmapper();
			if (dept.equalsIgnoreCase("GETALL")) {
				String FrQry = "select fac.Date, fac.Reject_remark as Reject_remark, fac.Before_path as Before_path, cs.Com_Name as Com_Section, fac.comment as comment, cs.Sec_Type as Sec_Type,fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join employee_mst as em on em.Emp_Code= fac.Responsibility inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.Return_Status = '"+1+"' and fac.is_active = '"+1+"' and fac.App_Status = '"
						+ 1 + "' order by ID asc;";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper);
			} else {
				String FrQry = "select fac.Date, fac.Reject_remark as Reject_remark, fac.Before_path as Before_path, cs.Com_Name as Com_Section, fac.comment as comment, cs.Sec_Type as Sec_Type,fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join employee_mst as em on em.Emp_Code= fac.Responsibility inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.Dept = ? and fac.Return_Status = '"+1+"' and fac.is_active = ? and fac.App_Status = '"
						+ 1 + "' order by ID asc";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper, dept, 1);
			}

		} catch (Exception ex) {
			logger.error("getfairactiondataApp method exception" + ex);
		}
		return frQurry;
	}

	public List<fairnessActionEntity> getfairactiondataReturn(String user) {
		List<fairnessActionEntity> frQurry = null;
		try {
			RowMapper<fairnessActionEntity> fairnessRowmapper = new fairnessActionRowmapper();
			
				String FrQry = "select fac.Date, fac.Reject_remark as Reject_remark, fac.Before_path as Before_path, cs.Com_Name as Com_Section, fac.comment as comment, cs.Sec_Type as Sec_Type,fac.Target_Status as Target_Status,fac.Resp_Status as Resp_Status, fac.Action_point, cs.Sec_Type, fac.Target_date, fac.Evdnce_File, fac.is_active, fac.ID, fac.Closed_date, dm.Dept_code as Dept, em.Emp_Name as Responsibility, fac.App_Status, fac.Send_Back_Comment, cs.Creator from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join employee_mst as em on em.Emp_Code= fac.Responsibility inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.Return_Status = '"+0+"' and fac.Evdnce_File = ? and fac.is_active = '"
						+ 1 + "' order by ID asc;";
				frQurry = this.jdbcTemplate.query(FrQry, fairnessRowmapper, user);
		} catch (Exception ex) {
			logger.error("getfairactiondataReturn method exception" + ex);
		}
		return frQurry;
	}

	public Map<String, HashMap<String, Integer>> getOverallDetails() {
		Map<String, HashMap<String, Integer>> frQurry = new HashMap<String, HashMap<String, Integer>>();
		try {

			
			HashMap<String, Integer> map3 = new HashMap<String, Integer>();
			HashMap<String, Integer> map4 = new HashMap<String, Integer>();
			HashMap<String, Integer> map5 = new HashMap<String, Integer>();
			HashMap<String, Integer> map6 = new HashMap<String, Integer>();
			HashMap<String, Integer> map7 = new HashMap<String, Integer>();
			HashMap<String, Integer> map8 = new HashMap<String, Integer>();
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			RowMapper<actionPointCountEntity> closedcountRowmapper = new actionpointcountRowmapper();
			RowMapper<actionPointCountEntity> totalcountRowmapper = new actionpointcountRowmapper();

			String comQry = "SELECT id as count, Department as dept FROM dept_mst order by id;";
			List<actionPointCountEntity> comPoint = this.jdbcTemplate.query(comQry, opencountRowmapper);
			int i=1;
			int il=1;
			for (actionPointCountEntity code : comPoint) {
				HashMap<String, Integer> map1 = new HashMap<String, Integer>();
				String openQry = "select count(*) as count, cs.Com_Name as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and dept = '" + code.getDept() + "' and cs.is_active='"+1+"' group by Com_Section order by cs.id desc;";
				List<actionPointCountEntity> openPoint = this.jdbcTemplate.query(openQry, opencountRowmapper);
				for (actionPointCountEntity single : openPoint) {
					map1.put(single.getDept(), single.getOpenCount());
				}
				frQurry.put("open_"+i, map1);
				i++;
			}

			String secQry = "SELECT id as count, Com_Name as dept FROM community_section where is_active = '" + 1
					+ "' order by id;";
			List<actionPointCountEntity> secPoint = this.jdbcTemplate.query(secQry, opencountRowmapper);
			for (actionPointCountEntity single : secPoint) {
				map6.put(single.getDept(), single.getOpenCount());
			}

			for (actionPointCountEntity code : comPoint) {
				HashMap<String, Integer> map2 = new HashMap<String, Integer>();
				String closedQry = "select count(*) as count, cs.Com_Name as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and dept = '" + code.getDept() + "' and cs.is_active='"+1+"' group by Com_Section";
				List<actionPointCountEntity> closedPoint = this.jdbcTemplate.query(closedQry, closedcountRowmapper);
				for (actionPointCountEntity single : closedPoint) {
					map2.put(single.getDept(), single.getOpenCount());
				}
				frQurry.put("close_"+il, map2);
				il++;
			}
			String totalQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on fac.Com_Section=cs.Com_Code where fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' group by Dept";
			List<actionPointCountEntity> totalPoint = this.jdbcTemplate.query(totalQry, totalcountRowmapper);
			for (actionPointCountEntity single : totalPoint) {
				map3.put(single.getDept(), single.getOpenCount());
			}
			String overallQry = "SELECT count(*) as count , cs.Com_Name as dept  FROM fairness_action_mst as fac \r\n"
					+ "inner join community_section as cs on fac.Com_Section=cs.Com_Code where fac.is_active = '"+1+"' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' group by Com_Section order by cs.id asc;";
			List<actionPointCountEntity> overallPoint = this.jdbcTemplate.query(overallQry, totalcountRowmapper);
			for (actionPointCountEntity single : overallPoint) {
				map4.put(single.getDept(), single.getOpenCount());
			}
			String overallClsQry = "SELECT count(*) as count , cs.Com_Name as dept  FROM fairness_action_mst as fac \r\n"
					+ "inner join community_section as cs on fac.Com_Section=cs.Com_Code where fac.is_active = '"+0+"' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' group by Com_Section order by cs.id asc;";
			List<actionPointCountEntity> overallclsPoint = this.jdbcTemplate.query(overallClsQry,
					totalcountRowmapper);
			for (actionPointCountEntity single : overallclsPoint) {
				map5.put(single.getDept(), single.getOpenCount());
			}
			String overalldeptOpnQry = "SELECT count(*) as count , cs.Dept_code as dept  FROM fairness_action_mst as fac \r\n"
					+ "inner join dept_mst as cs on fac.Dept=cs.Department inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"+1+"' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' group by Dept order by cs.id asc;";
			List<actionPointCountEntity> overalldeptOpnPoint = this.jdbcTemplate.query(overalldeptOpnQry,
					totalcountRowmapper);
			for (actionPointCountEntity single : overalldeptOpnPoint) {
				map7.put(single.getDept(), single.getOpenCount());
			}
			String overalldeptClsQry = "SELECT count(*) as count , cs.Dept_code as dept  FROM fairness_action_mst as fac \r\n"
					+ "inner join dept_mst as cs on fac.Dept=cs.Department inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"+0+"' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' group by Dept order by cs.id asc;";
			List<actionPointCountEntity> overalldeptclsPoint = this.jdbcTemplate.query(overalldeptClsQry,
					totalcountRowmapper);
			for (actionPointCountEntity single : overalldeptclsPoint) {
				map8.put(single.getDept(), single.getOpenCount());
			}
			
			frQurry.put("total", map3);
			frQurry.put("overallOpen", map4);
			frQurry.put("overallClosed", map5);
			frQurry.put("section", map6);
			frQurry.put("overDeptOpen", map7);
			frQurry.put("overDeptClose", map8);
		} catch (Exception ex) {
			logger.error("getOverallDetails method exception" + ex);
		}
		return frQurry;
	}

	public Map<String, HashMap<String, Integer>> getFilteredOverallDetails(String dept, String month, String section) {
		Map<String, HashMap<String, Integer>> frQurry = new HashMap<String, HashMap<String, Integer>>();
		try {
			HashMap<String, Integer> map1 = new HashMap<String, Integer>();
			HashMap<String, Integer> map2 = new HashMap<String, Integer>();
			HashMap<String, Integer> map3 = new HashMap<String, Integer>();
			HashMap<String, Integer> map4 = new HashMap<String, Integer>();
			HashMap<String, Integer> map5 = new HashMap<String, Integer>();
			HashMap<String, Integer> map6 = new HashMap<String, Integer>();
			HashMap<String, Integer> map7 = new HashMap<String, Integer>();
			HashMap<String, Integer> map8 = new HashMap<String, Integer>();
			HashMap<String, Integer> map9 = new HashMap<String, Integer>();
			HashMap<String, Integer> map10 = new HashMap<String, Integer>();
			RowMapper<actionPointCountEntity> opencountRowmapper = new actionpointcountRowmapper();
			RowMapper<actionPointCountEntity> closedcountRowmapper = new actionpointcountRowmapper();
			RowMapper<actionPointCountEntity> totalcountRowmapper = new actionpointcountRowmapper();
			RowMapper<actionOverallCountEntity> overallcountRowmapper = new actionOverallcountRowmapper();

			if (dept.equalsIgnoreCase("") && month.equalsIgnoreCase("") && section.equalsIgnoreCase("")) {
				String openQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+0+"' and cs1.is_active='"+1+"' group by fac.Dept";
				List<actionPointCountEntity> openPoint = this.jdbcTemplate.query(openQry, opencountRowmapper);
				for (actionPointCountEntity single : openPoint) {
					map1.put(single.getDept(), single.getOpenCount());
				}
				String pendingQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+1+"' and cs1.is_active='"+1+"' group by fac.Dept";
				List<actionPointCountEntity> pendingPoint = this.jdbcTemplate.query(pendingQry, opencountRowmapper);
				for (actionPointCountEntity single : pendingPoint) {
					map10.put(single.getDept(), single.getOpenCount());
				}
				String overDueQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date and cs1.is_active='"+1+"' group by fac.Dept;";
				List<actionPointCountEntity> overDuePoint = this.jdbcTemplate.query(overDueQry, opencountRowmapper);
				for (actionPointCountEntity single : overDuePoint) {
					map8.put(single.getDept(), single.getOpenCount());
				}
				String closedQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' group by fac.Dept";
				List<actionPointCountEntity> closedPoint = this.jdbcTemplate.query(closedQry, closedcountRowmapper);
				for (actionPointCountEntity single : closedPoint) {
					map2.put(single.getDept(), single.getOpenCount());
				}
				String totalQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' group by fac.Dept";
				List<actionPointCountEntity> totalPoint = this.jdbcTemplate.query(totalQry, totalcountRowmapper);
				for (actionPointCountEntity single : totalPoint) {
					map3.put(single.getDept(), single.getOpenCount());
				}
				String overallQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 1 + "' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallPoint = this.jdbcTemplate.query(overallQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallPoint) {
					map4.put("TotalOpen", single.getOpenCount());
				}
				String overallDueQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallDuePoint = this.jdbcTemplate.query(overallDueQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallDuePoint) {
					map9.put("TotalOverdue", single.getOpenCount());
				}
				String overallClsQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' ;";
				List<actionOverallCountEntity> overallclsPoint = this.jdbcTemplate.query(overallClsQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallclsPoint) {
					map5.put("TotalClosed", single.getOpenCount());
				}
				String overallBestQry = "SELECT DISTINCT COUNT(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code WHERE fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' GROUP BY Dept ORDER BY count DESC limit 3;";
				List<actionPointCountEntity> overallBestList = this.jdbcTemplate.query(overallBestQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallBestList) {
					map6.put(single.getDept(), single.getOpenCount());
				}
				String overallTrendQry = "select count(*) as count, month(fac.date) as dept from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' group by month(fac.date) ;";
				List<actionPointCountEntity> overallTrendList = this.jdbcTemplate.query(overallTrendQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallTrendList) {
					map7.put(single.getDept(), single.getOpenCount());
				}
				map7.putIfAbsent("1", 0);
				map7.putIfAbsent("2", 0);
				map7.putIfAbsent("3", 0);
				map7.putIfAbsent("4", 0);
				map7.putIfAbsent("5", 0);
				map7.putIfAbsent("6", 0);
				map7.putIfAbsent("7", 0);
				map7.putIfAbsent("8", 0);
				map7.putIfAbsent("9", 0);
				map7.putIfAbsent("10", 0);
				map7.putIfAbsent("11", 0);
				map7.putIfAbsent("12", 0);
				map7.putIfAbsent("13", 0);
				map7.putIfAbsent("14", 0);
				map7.putIfAbsent("15", 0);
				map7.putIfAbsent("16", 0);
				map7.putIfAbsent("17", 0);

				frQurry.put("open", map1);
				frQurry.put("closed", map2);
				frQurry.put("total", map3);
				frQurry.put("overallOpen", map4);
				frQurry.put("overallClosed", map5);
				frQurry.put("overallBest", map6);
				frQurry.put("overallTrend", map7);
				frQurry.put("overDue", map8);
				frQurry.put("TotaloverDue", map9);
				frQurry.put("pending", map10);

			} else if (!dept.equalsIgnoreCase("GET ALL") && !dept.equalsIgnoreCase("") && month.equalsIgnoreCase("")) {
				String openQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and dept = '" + dept + "' and fac.App_Status = '"+0+"' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> openPoint = this.jdbcTemplate.query(openQry, opencountRowmapper);
				for (actionPointCountEntity single : openPoint) {
					map1.put(single.getDept(), single.getOpenCount());
				}
				String pendingQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "'  and fac.App_Status = '"+1+"' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> pendingPoint = this.jdbcTemplate.query(pendingQry, opencountRowmapper);
				for (actionPointCountEntity single : pendingPoint) {
					map10.put(single.getDept(), single.getOpenCount());
				}
				String overDueQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date  and fac.dept = '" + dept + "' and cs1.is_active='"+1+"' group by Dept;";
				List<actionPointCountEntity> overDuePoint = this.jdbcTemplate.query(overDueQry, opencountRowmapper);
				for (actionPointCountEntity single : overDuePoint) {
					map8.put(single.getDept(), single.getOpenCount());
				}
				String closedQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> closedPoint = this.jdbcTemplate.query(closedQry, closedcountRowmapper);
				for (actionPointCountEntity single : closedPoint) {
					map2.put(single.getDept(), single.getOpenCount());
				}
				String totalQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.dept = '"
						+ dept + "' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> totalPoint = this.jdbcTemplate.query(totalQry, totalcountRowmapper);
				for (actionPointCountEntity single : totalPoint) {
					map3.put(single.getDept(), single.getOpenCount());
				}
				String overallQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 1
						+ "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallPoint = this.jdbcTemplate.query(overallQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallPoint) {
					map4.put("TotalOpen", single.getOpenCount());
				}
				String overallDueQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code  where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and fac.Closed_date > fac.Target_date and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallDuePoint = this.jdbcTemplate.query(overallDueQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallDuePoint) {
					map9.put("TotalOverdue", single.getOpenCount());
				}
				String overallClsQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallclsPoint = this.jdbcTemplate.query(overallClsQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallclsPoint) {
					map5.put("TotalClosed", single.getOpenCount());
				}
				String overallBestQry = "SELECT DISTINCT COUNT(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code WHERE fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Dept = '" + dept + "' and cs1.is_active='"+1+"' GROUP BY Dept ORDER BY count DESC limit 3;";
				List<actionPointCountEntity> overallBestList = this.jdbcTemplate.query(overallBestQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallBestList) {
					map6.put(single.getDept(), single.getOpenCount());
				}
				String overallTrendQry = "select count(*) as count, month(fac.date) as dept from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Dept = '" + dept + "' group by month(fac.date) and cs1.is_active='"+1+"';";
				List<actionPointCountEntity> overallTrendList = this.jdbcTemplate.query(overallTrendQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallTrendList) {
					map7.put(single.getDept(), single.getOpenCount());
				}
				map7.putIfAbsent("1", 0);
				map7.putIfAbsent("2", 0);
				map7.putIfAbsent("3", 0);
				map7.putIfAbsent("4", 0);
				map7.putIfAbsent("5", 0);
				map7.putIfAbsent("6", 0);
				map7.putIfAbsent("7", 0);
				map7.putIfAbsent("8", 0);
				map7.putIfAbsent("9", 0);
				map7.putIfAbsent("10", 0);
				map7.putIfAbsent("11", 0);
				map7.putIfAbsent("12", 0);
				map7.putIfAbsent("13", 0);
				map7.putIfAbsent("14", 0);
				map7.putIfAbsent("15", 0);
				map7.putIfAbsent("16", 0);

				frQurry.put("open", map1);
				frQurry.put("closed", map2);
				frQurry.put("total", map3);
				frQurry.put("overallOpen", map4);
				frQurry.put("overallClosed", map5);
				frQurry.put("overallBest", map6);
				frQurry.put("overallTrend", map7);
				frQurry.put("overDue", map8);
				frQurry.put("TotaloverDue", map9);
				frQurry.put("pending", map10);

			} else if (dept.equalsIgnoreCase("GET ALL") && !month.equalsIgnoreCase("")) {
				String[] dateSplt = month.split("-");
				String openQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+0+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]
						+ "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> openPoint = this.jdbcTemplate.query(openQry, opencountRowmapper);
				for (actionPointCountEntity single : openPoint) {
					map1.put(single.getDept(), single.getOpenCount());
				}
				String pendingQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0] + "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> pendingPoint = this.jdbcTemplate.query(pendingQry, opencountRowmapper);
				for (actionPointCountEntity single : pendingPoint) {
					map10.put(single.getDept(), single.getOpenCount());
				}
				String overDueQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date and month(fac.Date) = '" + dateSplt[1]
						+ "'and year(Date) = '" + dateSplt[0] + "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> overDuePoint = this.jdbcTemplate.query(overDueQry, opencountRowmapper);
				for (actionPointCountEntity single : overDuePoint) {
					map8.put(single.getDept(), single.getOpenCount());
				}
				String closedQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]
						+ "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> closedPoint = this.jdbcTemplate.query(closedQry, closedcountRowmapper);
				for (actionPointCountEntity single : closedPoint) {
					map2.put(single.getDept(), single.getOpenCount());
				}
				String totalQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where month(fac.Date) = '"
						+ dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0] + "' and fac.Return_Status = '"+1+"' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> totalPoint = this.jdbcTemplate.query(totalQry, totalcountRowmapper);
				for (actionPointCountEntity single : totalPoint) {
					map3.put(single.getDept(), single.getOpenCount());
				}
				String overallQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 1
						+ "' and fac.Return_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0] + "' and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallPoint = this.jdbcTemplate.query(overallQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallPoint) {
					map4.put("TotalOpen", single.getOpenCount());
				}
				String overallDueQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code  where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]
						+ "' and fac.Closed_date > fac.Target_date and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallDuePoint = this.jdbcTemplate.query(overallDueQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallDuePoint) {
					map9.put("TotalOverdue", single.getOpenCount());
				}
				String overallClsQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0] + "' and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallclsPoint = this.jdbcTemplate.query(overallClsQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallclsPoint) {
					map5.put("TotalClosed", single.getOpenCount());
				}
				String overallBestQry = "SELECT DISTINCT COUNT(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code WHERE fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]
						+ "' and cs1.is_active='"+1+"' GROUP BY Dept ORDER BY count DESC limit 3;";
				List<actionPointCountEntity> overallBestList = this.jdbcTemplate.query(overallBestQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallBestList) {
					map6.put(single.getDept(), single.getOpenCount());
				}
				String overallTrendQry = "select count(*) as count, month(fac.date) as dept from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]
						+ "' and cs1.is_active='"+1+"' group by month(fac.date);";
				List<actionPointCountEntity> overallTrendList = this.jdbcTemplate.query(overallTrendQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallTrendList) {
					map7.put(single.getDept(), single.getOpenCount());
				}
				map7.putIfAbsent("1", 0);
				map7.putIfAbsent("2", 0);
				map7.putIfAbsent("3", 0);
				map7.putIfAbsent("4", 0);
				map7.putIfAbsent("5", 0);
				map7.putIfAbsent("6", 0);
				map7.putIfAbsent("7", 0);
				map7.putIfAbsent("8", 0);
				map7.putIfAbsent("9", 0);
				map7.putIfAbsent("10", 0);
				map7.putIfAbsent("11", 0);
				map7.putIfAbsent("12", 0);
				map7.putIfAbsent("13", 0);
				map7.putIfAbsent("14", 0);
				map7.putIfAbsent("15", 0);
				map7.putIfAbsent("16", 0);

				frQurry.put("open", map1);
				frQurry.put("closed", map2);
				frQurry.put("total", map3);
				frQurry.put("overallOpen", map4);
				frQurry.put("overallClosed", map5);
				frQurry.put("overallBest", map6);
				frQurry.put("overallTrend", map7);
				frQurry.put("overDue", map8);
				frQurry.put("TotaloverDue", map9);
				frQurry.put("pending", map10);

			} else if (!dept.equalsIgnoreCase("GET ALL") && !dept.equalsIgnoreCase("") && !month.equalsIgnoreCase("")) {
				String[] dateSplt = month.split("-");
				String openQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+0+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]
						+ "' and fac.dept = '" + dept + "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> openPoint = this.jdbcTemplate.query(openQry, opencountRowmapper);
				for (actionPointCountEntity single : openPoint) {
					map1.put(single.getDept(), single.getOpenCount());
				}
				String pendingQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]+ "' and fac.dept = '" + dept + "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> pendingPoint = this.jdbcTemplate.query(pendingQry, opencountRowmapper);
				for (actionPointCountEntity single : pendingPoint) {
					map10.put(single.getDept(), single.getOpenCount());
				}
				String overDueQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date and month(fac.Date) = '" + dateSplt[1]
						+ "'and year(Date) = '" + dateSplt[0] + "' and fac.dept = '" + dept + "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> overDuePoint = this.jdbcTemplate.query(overDueQry, opencountRowmapper);
				for (actionPointCountEntity single : overDuePoint) {
					map8.put(single.getDept(), single.getOpenCount());
				}
				String closedQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '"
						+ dateSplt[0] + "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> closedPoint = this.jdbcTemplate.query(closedQry, closedcountRowmapper);
				for (actionPointCountEntity single : closedPoint) {
					map2.put(single.getDept(), single.getOpenCount());
				}
				String totalQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.dept = '"
						+ dept + "' and fac.Return_Status = '"+1+"' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '" + dateSplt[0]
						+ "' and cs1.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> totalPoint = this.jdbcTemplate.query(totalQry, totalcountRowmapper);
				for (actionPointCountEntity single : totalPoint) {
					map3.put(single.getDept(), single.getOpenCount());
				}
				String overallQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 1
						+ "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '"
						+ dateSplt[0] + "' and cs1.is_active='"+1+"' ;";
				List<actionOverallCountEntity> overallPoint = this.jdbcTemplate.query(overallQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallPoint) {
					map4.put("TotalOpen", single.getOpenCount());
				}
				String overallDueQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '"
						+ dateSplt[0] + "' and fac.Closed_date > fac.Target_date and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallDuePoint = this.jdbcTemplate.query(overallDueQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallDuePoint) {
					map9.put("TotalOverdue", single.getOpenCount());
				}
				String overallClsQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '"
						+ dateSplt[0] + "' and cs1.is_active='"+1+"';";
				List<actionOverallCountEntity> overallclsPoint = this.jdbcTemplate.query(overallClsQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallclsPoint) {
					map5.put("TotalClosed", single.getOpenCount());
				}
				String overallBestQry = "SELECT DISTINCT COUNT(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code WHERE fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '"
						+ dateSplt[0] + "' and cs1.is_active='"+1+"' GROUP BY Dept ORDER BY count DESC limit 3;";
				List<actionPointCountEntity> overallBestList = this.jdbcTemplate.query(overallBestQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallBestList) {
					map6.put(single.getDept(), single.getOpenCount());
				}
				String overallTrendQry = "select count(*) as count, month(date) as dept from fairness_action_mst as fac inner join community_section as cs1 on fac.Com_Section=cs1.Com_Code where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.dept = '" + dept + "' and month(fac.Date) = '" + dateSplt[1] + "'and year(fac.Date) = '"
						+ dateSplt[0] + "' and cs1.is_active='"+1+"' group by month(fac.date);";
				List<actionPointCountEntity> overallTrendList = this.jdbcTemplate.query(overallTrendQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallTrendList) {
					map7.put(single.getDept(), single.getOpenCount());
				}
				map7.putIfAbsent("1", 0);
				map7.putIfAbsent("2", 0);
				map7.putIfAbsent("3", 0);
				map7.putIfAbsent("4", 0);
				map7.putIfAbsent("5", 0);
				map7.putIfAbsent("6", 0);
				map7.putIfAbsent("7", 0);
				map7.putIfAbsent("8", 0);
				map7.putIfAbsent("9", 0);
				map7.putIfAbsent("10", 0);
				map7.putIfAbsent("11", 0);
				map7.putIfAbsent("12", 0);
				map7.putIfAbsent("13", 0);
				map7.putIfAbsent("14", 0);
				map7.putIfAbsent("15", 0);
				map7.putIfAbsent("16", 0);

				frQurry.put("open", map1);
				frQurry.put("closed", map2);
				frQurry.put("total", map3);
				frQurry.put("overallOpen", map4);
				frQurry.put("overallClosed", map5);
				frQurry.put("overallBest", map6);
				frQurry.put("overallTrend", map7);
				frQurry.put("overDue", map8);
				frQurry.put("TotaloverDue", map9);
				frQurry.put("pending", map10);

			} 
			else if (!section.equalsIgnoreCase("GET ALL") && !section.equalsIgnoreCase("") && month.equalsIgnoreCase("") && dept.equalsIgnoreCase("GET ALL") ) {
				String openQry = "select count(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join  community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '"
			            + 1 +"' and fac.Return_Status = '"+ 1 +"' and fac.Com_Section = '"+ section +"' and fac.App_Status = '"+ 0 +"' and cs.is_active='"+1+"' group by fac.Dept;";
				List<actionPointCountEntity> openPoint = this.jdbcTemplate.query(openQry, opencountRowmapper);
				for (actionPointCountEntity single : openPoint) {
					map1.put(single.getDept(), single.getOpenCount());
				}
				String pendingQry = "select count(*) as count,  dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.Com_Section = '"+ section +"'  and fac.App_Status = '"+1+"' and cs.is_active='"+1+"' group by fac.Dept;";
				List<actionPointCountEntity> pendingPoint = this.jdbcTemplate.query(pendingQry, opencountRowmapper);
				for (actionPointCountEntity single : pendingPoint) {
					map10.put(single.getDept(), single.getOpenCount());
				}
				String overDueQry = "select count(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date  and fac.Com_Section = '"+ section +"' and cs.is_active='"+1+"' group by fac.Dept;";
				List<actionPointCountEntity> overDuePoint = this.jdbcTemplate.query(overDueQry, opencountRowmapper);
				for (actionPointCountEntity single : overDuePoint) {
					map8.put(single.getDept(), single.getOpenCount());
				}
				String closedQry = "select count(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Com_Section = '"+ section +"' and cs.is_active='"+1+"' group by fac.Dept;";
				List<actionPointCountEntity> closedPoint = this.jdbcTemplate.query(closedQry, closedcountRowmapper);
				for (actionPointCountEntity single : closedPoint) {
					map2.put(single.getDept(), single.getOpenCount());
				}
				String totalQry = "select count(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.Com_Section = '"
				        + section +"' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' group by fac.Dept;";
				List<actionPointCountEntity> totalPoint = this.jdbcTemplate.query(totalQry, totalcountRowmapper);
				for (actionPointCountEntity single : totalPoint) {
					map3.put(single.getDept(), single.getOpenCount());
				}
				String overallQry = "select count(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '" + 1
						+ "' and fac.Return_Status = '"+1+"' and fac.Com_Section = '"+ section +"' and cs.is_active='"+1+"' ;";
				List<actionOverallCountEntity> overallPoint = this.jdbcTemplate.query(overallQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallPoint) {
					map4.put("TotalOpen", single.getOpenCount());
				}
				String overallDueQry = "select count(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.Com_Section = '"+ section +"' and fac.Closed_date > fac.Target_date and cs.is_active='"+1+"';";
				List<actionOverallCountEntity> overallDuePoint = this.jdbcTemplate.query(overallDueQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallDuePoint) {
					map9.put("TotalOverdue", single.getOpenCount());
				}
				String overallClsQry = "select count(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.Com_Section = '"+ section +"' and cs.is_active='"+1+"';";
				List<actionOverallCountEntity> overallclsPoint = this.jdbcTemplate.query(overallClsQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallclsPoint) {
					map5.put("TotalClosed", single.getOpenCount());
				}
				String overallBestQry = "SELECT DISTINCT COUNT(*) as count, dms.Dept_code as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept WHERE fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Com_Section = '"+ section +"' and cs.is_active='"+1+"' GROUP BY fac.Dept ORDER BY count DESC limit 3;";
				List<actionPointCountEntity> overallBestList = this.jdbcTemplate.query(overallBestQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallBestList) {
					map6.put(single.getDept(), single.getOpenCount());
				}
				String overallTrendQry = "select count(*) as count, month(date) as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section inner join dept_mst as dms on dms.Department = fac.Dept where fac.is_active = '"
				        + 0 +"' and fac.Return_Status = '"+ 1 +"' and fac.Com_Section = '"+ section +"' and cs.is_active='"+1+"' group by month(fac.date);";
				List<actionPointCountEntity> overallTrendList = this.jdbcTemplate.query(overallTrendQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallTrendList) {
					map7.put(single.getDept(), single.getOpenCount());
				}
				map7.putIfAbsent("1", 0);
				map7.putIfAbsent("2", 0);
				map7.putIfAbsent("3", 0);
				map7.putIfAbsent("4", 0);
				map7.putIfAbsent("5", 0);
				map7.putIfAbsent("6", 0);
				map7.putIfAbsent("7", 0);
				map7.putIfAbsent("8", 0);
				map7.putIfAbsent("9", 0);
				map7.putIfAbsent("10", 0);
				map7.putIfAbsent("11", 0);
				map7.putIfAbsent("12", 0);
				map7.putIfAbsent("13", 0);
				map7.putIfAbsent("14", 0);
				map7.putIfAbsent("15", 0);
				map7.putIfAbsent("16", 0);

				frQurry.put("open", map1);
				frQurry.put("closed", map2);
				frQurry.put("total", map3);
				frQurry.put("overallOpen", map4);
				frQurry.put("overallClosed", map5);
				frQurry.put("overallBest", map6);
				frQurry.put("overallTrend", map7);
				frQurry.put("overDue", map8);
				frQurry.put("TotaloverDue", map9);
				frQurry.put("pending", map10);

			}
			else if (dept.equalsIgnoreCase("GET ALL") && month.equalsIgnoreCase("")) {
				String openQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+0+"' and cs.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> openPoint = this.jdbcTemplate.query(openQry, opencountRowmapper);
				for (actionPointCountEntity single : openPoint) {
					map1.put(single.getDept(), single.getOpenCount());
				}
				String pendingQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '"
						+ 1 + "' and fac.Return_Status = '"+1+"' and fac.App_Status = '"+1+"' and cs.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> pendingPoint = this.jdbcTemplate.query(pendingQry, opencountRowmapper);
				for (actionPointCountEntity single : pendingPoint) {
					map10.put(single.getDept(), single.getOpenCount());
				}
				String overDueQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date and cs.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> overDuePoint = this.jdbcTemplate.query(overDueQry, opencountRowmapper);
				for (actionPointCountEntity single : overDuePoint) {
					map8.put(single.getDept(), single.getOpenCount());
				}
				String closedQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> closedPoint = this.jdbcTemplate.query(closedQry, closedcountRowmapper);
				for (actionPointCountEntity single : closedPoint) {
					map2.put(single.getDept(), single.getOpenCount());
				}
				String totalQry = "select count(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' group by Dept";
				List<actionPointCountEntity> totalPoint = this.jdbcTemplate.query(totalQry, totalcountRowmapper);
				for (actionPointCountEntity single : totalPoint) {
					map3.put(single.getDept(), single.getOpenCount());
				}
				String overallQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '" + 1 + "' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' ;";
				List<actionOverallCountEntity> overallPoint = this.jdbcTemplate.query(overallQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallPoint) {
					map4.put("TotalOpen", single.getOpenCount());
				}
				String overallDueQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '" + 0
						+ "' and fac.Return_Status = '"+1+"' and fac.Closed_date > fac.Target_date and cs.is_active='"+1+"';";
				List<actionOverallCountEntity> overallDuePoint = this.jdbcTemplate.query(overallDueQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallDuePoint) {
					map9.put("TotalOverdue", single.getOpenCount());
				}
				String overallClsQry = "select count(*) as count from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '" + 0
						+ "' and Return_Status = '"+1+"' and cs.is_active='"+1+"';";
				List<actionOverallCountEntity> overallclsPoint = this.jdbcTemplate.query(overallClsQry,
						overallcountRowmapper);
				for (actionOverallCountEntity single : overallclsPoint) {
					map5.put("TotalClosed", single.getOpenCount());
				}
				String overallBestQry = "SELECT DISTINCT COUNT(*) as count, dm.Dept_code as dept from fairness_action_mst as fac inner join dept_mst as dm on dm.Department=fac.Dept inner join community_section as cs on cs.Com_Code = fac.Com_Section WHERE fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' GROUP BY Dept ORDER BY count DESC limit 3;";
				List<actionPointCountEntity> overallBestList = this.jdbcTemplate.query(overallBestQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallBestList) {
					map6.put(single.getDept(), single.getOpenCount());
				}
				String overallTrendQry = "select count(*) as count, month(date) as dept from fairness_action_mst as fac inner join community_section as cs on cs.Com_Code = fac.Com_Section where fac.is_active = '"
						+ 0 + "' and fac.Return_Status = '"+1+"' and cs.is_active='"+1+"' group by month(date);";
				List<actionPointCountEntity> overallTrendList = this.jdbcTemplate.query(overallTrendQry,
						opencountRowmapper);
				for (actionPointCountEntity single : overallTrendList) {
					map7.put(single.getDept(), single.getOpenCount());
				}
				map7.putIfAbsent("1", 0);
				map7.putIfAbsent("2", 0);
				map7.putIfAbsent("3", 0);
				map7.putIfAbsent("4", 0);
				map7.putIfAbsent("5", 0);
				map7.putIfAbsent("6", 0);
				map7.putIfAbsent("7", 0);
				map7.putIfAbsent("8", 0);
				map7.putIfAbsent("9", 0);
				map7.putIfAbsent("10", 0);
				map7.putIfAbsent("11", 0);
				map7.putIfAbsent("12", 0);
				map7.putIfAbsent("13", 0);
				map7.putIfAbsent("14", 0);
				map7.putIfAbsent("15", 0);
				map7.putIfAbsent("16", 0);

				frQurry.put("open", map1);
				frQurry.put("closed", map2);
				frQurry.put("total", map3);
				frQurry.put("overallOpen", map4);
				frQurry.put("overallClosed", map5);
				frQurry.put("overallBest", map6);
				frQurry.put("overallTrend", map7);
				frQurry.put("overDue", map8);
				frQurry.put("TotaloverDue", map9);
				frQurry.put("pending", map10);

			} 

		} catch (Exception ex) {
			logger.error("getFilteredOverallDetails method exception" + ex);
		}
		return frQurry;
	}


}
