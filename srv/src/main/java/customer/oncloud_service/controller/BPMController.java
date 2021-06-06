package customer.oncloud_service.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.sap.cds.feature.xsuaa.XsuaaUserInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import customer.oncloud_service.util.BPMSocketFactory;
import customer.oncloud_service.util.Constants;
import customer.oncloud_service.util.DBConnection;

@RestController
@RequestMapping("/api/bpm")
public class BPMController {
	
    private static final Logger logger = LoggerFactory.getLogger(BPMController.class);
    
    @Autowired
    XsuaaUserInfo xsuaaUserInfo;


	@GetMapping("/account")
	public ResponseEntity<String> getAccount() {
        logger.error("getId: " + xsuaaUserInfo.getId());
        logger.error("getName: " + xsuaaUserInfo.getName());
        logger.error("getGivenName: " + xsuaaUserInfo.getGivenName());
        logger.error("getEmail: " + xsuaaUserInfo.getEmail());
        logger.error("getOrigin: " + xsuaaUserInfo.getOrigin());
        logger.error("getSubDomain: " + xsuaaUserInfo.getSubDomain());
        logger.error("getFamilyName: " + xsuaaUserInfo.getFamilyName());
        logger.error("getTenant: " + xsuaaUserInfo.getTenant());
        logger.error("getRoles: " + xsuaaUserInfo.getRoles());
        logger.error("getAttributes: " + xsuaaUserInfo.getAttributes());
        logger.error("getAdditionalAttributes: " + xsuaaUserInfo.getAdditionalAttributes());
		return new ResponseEntity<String>(xsuaaUserInfo.getName(), HttpStatus.OK);
    }
	
	@GetMapping("/todoCount")
	public ResponseEntity<String> getTodoCountByEmpNo() {
        Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;

		try {
            String empNo = xsuaaUserInfo.getName();
            // String empNo = "10006799";
            if (StringUtils.isNotBlank(empNo)) {
                con = DBConnection.getSQLServerConnection(BPMSocketFactory.class.getCanonicalName(), Constants.BPM_DB_USERNAME, Constants.BPM_DB_PASSWORD, Constants.BPM_DB_DATABASENAME);
                if (con != null) {
                    logger.info("connect successfully");
                    
                    String sql = "select COUNT(LoginID) AS total from vw_task_all WHERE LoginID = ?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, empNo);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        count = rs.getInt("total");
                    }
                }
            }
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
		}

		if (count != -1) {
			return new ResponseEntity<String>(Integer.toString(count), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
     }


                 

    	@GetMapping("/todoNumber")
	public ResponseEntity<String> getTodoNumberByEmpNo() {
        Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = -1;

		try {
            String empNo = xsuaaUserInfo.getName();
            // String empNo = "10006799";
            if (StringUtils.isNotBlank(empNo)) {
                con = DBConnection.getSQLServerConnection(BPMSocketFactory.class.getCanonicalName(), Constants.BPM_DB_USERNAME, Constants.BPM_DB_PASSWORD, Constants.BPM_DB_DATABASENAME);
                if (con != null) {
                    logger.info("connect successfully");
                    
                    String sql = "select COUNT(LoginID) AS total from vw_task_all WHERE LoginID = ?";
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, empNo);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        count = rs.getInt("total");
                    }
                }
            }
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
		}
        JSONArray hashdetail = new JSONArray();    
        JSONObject itemObj = new JSONObject();
            itemObj.put("Number",Integer.toString(count));   
            hashdetail.put(itemObj);  
		// if (count != -1) {
			return new ResponseEntity<String>(hashdetail.toString(), HttpStatus.OK);
		// } else {
		// 	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		// }
     }        

 	@GetMapping("/todoDetail")
	public ResponseEntity<String> getTodoDetailByEmpNo() {
        Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        int count = -1;
        JSONArray hashdetail = new JSONArray();
        logger.info("get detail");

		try {
            String empNo = xsuaaUserInfo.getName();
            // String empNo = "10006799";
            if (StringUtils.isNotBlank(empNo)) {
                con = DBConnection.getSQLServerConnection(BPMSocketFactory.class.getCanonicalName(), Constants.BPM_DB_USERNAME, Constants.BPM_DB_PASSWORD, Constants.BPM_DB_DATABASENAME);
                if (con != null) {
                    logger.info("connect successfully");
                    
                    // String sql = "select COUNT(LoginID) AS total from vw_task_all WHERE LoginID = ?";
                     
                    // String sql = "select LoginID from vw_task_all WHERE LoginID = ?";
                     String sql = "select top 10 * from vw_task_all WHERE LoginID = ?";
                    // String sql = "select StartTime, Name, KeyWord, UserName from vw_task_all WHERE LoginID = ?"; 
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, empNo);
                    rs = pstmt.executeQuery();
                    // if (rs.next()) {

                    //     count = rs.getInt("total");
                    // }

                    while (rs.next()) {
                        JSONObject itemObj = new JSONObject();
                        // itemObj.put("LoginID",rs.getString("LoginID"));   
                        itemObj.put("StartTime",rs.getString("StartTime"));  
                        itemObj.put("Name",rs.getString("Name")); 
                        itemObj.put("KeyWord",rs.getString("KeyWord")); 
                        itemObj.put("UserName",rs.getString("UserName")); 
                        hashdetail.put(itemObj);
                    }
                }
            }
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
		}

		// if (count != -1) {
			// return new ResponseEntity<String>("success", HttpStatus.OK);
            return new ResponseEntity<String>(hashdetail.toString(), HttpStatus.OK);
        	
            // return new ResponseEntity<String>(Integer.toString(count), HttpStatus.OK);

        //     } else {
		// 	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		// }
    }   

         @GetMapping("/todoTop") 
	    public ResponseEntity<String> getTodoTopByEmpNo(@RequestParam  Integer todotop ) {
        Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
        //int count = -1;
        JSONArray hashdetail = new JSONArray();
        logger.info("get detail");

        StringBuffer result = new StringBuffer();

        String sql = new String();


		// try {
        //     String empNo = xsuaaUserInfo.getName();
        //     // String empNo = "10006799";
        //     if (StringUtils.isNotBlank(empNo)) {
        //         con = DBConnection.getSQLServerConnection(BPMSocketFactory.class.getCanonicalName(), Constants.BPM_DB_USERNAME, Constants.BPM_DB_PASSWORD, Constants.BPM_DB_DATABASENAME);
        //         if (con != null) {
        //             logger.info("connect successfully");
                    
        //             // String sql = "select COUNT(LoginID) AS total from vw_task_all WHERE LoginID = ?";
                     
        //             // String sql = "select LoginID from vw_task_all WHERE LoginID = ?";
        //             StringBuffer strSql = new StringBuffer();
        //             //strSql.append("select top " ).append(todotop.toString()).append(" * from vw_task_all WHERE LoginID = ?");                   
        //             // stringBuffer.append("select * from vw_task_all");
                    
        //             //sql = stringBuffer.toString();
        //             sql = "select top 10 * from vw_task_all WHERE LoginID = ?";
        //             // strSql.append("select top 10 * from vw_task_all WHERE LoginID = ?");
        //             // sql = strSql.toString();
        //             // result.append(sql);
        //             //sql = "select KeyWord, LoginId from vw_task_all WHERE LoginID like '%0%' limit 1" ;
        //             // String sql = "select StartTime, Name, KeyWord, UserName from vw_task_all WHERE LoginID = ?"; 
        //             // result.append("sql:").append(sql.toString());
        //             pstmt = con.prepareStatement(sql);
        //             pstmt.setString(1, empNo);
        //             // rs = pstmt.executeQuery();                    
        //             // result.append("rs:").append(rs == null ? " rs is null " : rs.toString());
                    
        //             // result.append(";rs.next:").append( rs.next() == true ? " rs.next is  null " : " rs.next is not null ");

        //             // // if (rs.next()) {

        //             // //     count = rs.getInt("total");
        //             // // }

        //             while (rs.next()) {

        //                 // result.append("Json Change :");

        //                 JSONObject itemObj = new JSONObject();

        //                 // result.append(";itemObj:").append( itemObj == null ? " itemObj is null " : itemObj.toString());
        //                 itemObj.put("LoginID",rs.getString("LoginID"));   
        //                 itemObj.put("StartTime",rs.getString("StartTime"));  
        //                 itemObj.put("Name",rs.getString("Name")); 
        //                 itemObj.put("KeyWord",rs.getString("KeyWord")); 
        //                 itemObj.put("UserName",rs.getString("LoginId")); 
        //                 hashdetail.put(itemObj);

        //             }
        //         }
        //     }
		// } catch (Exception e) {

            
		try {
            String empNo = xsuaaUserInfo.getName();
            // String empNo = "10006799";
            if (StringUtils.isNotBlank(empNo)) {
                con = DBConnection.getSQLServerConnection(BPMSocketFactory.class.getCanonicalName(), Constants.BPM_DB_USERNAME, Constants.BPM_DB_PASSWORD, Constants.BPM_DB_DATABASENAME);
                if (con != null) {
                    logger.info("connect successfully");
                    
                    // String sql = "select COUNT(LoginID) AS total from vw_task_all WHERE LoginID = ?";
                     
                    // String sql = "select LoginID from vw_task_all WHERE LoginID = ?";
                    // sql = "select top 10 * from vw_task_all WHERE LoginID = ?";
                    StringBuffer strSql = new StringBuffer();
                    strSql.append("select top " ).append(todotop.toString()).append(" * from vw_task_all WHERE LoginID = ?");                                      
                    sql = strSql.toString();  
                    // String sql = "select StartTime, Name, KeyWord, UserName from vw_task_all WHERE LoginID = ?"; 
                    pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, empNo);
                    rs = pstmt.executeQuery();
                    // if (rs.next()) {

                    //     count = rs.getInt("total");
                    // }

                    while (rs.next()) {
                        JSONObject itemObj = new JSONObject();
                        // itemObj.put("LoginID",rs.getString("LoginID"));   
                        itemObj.put("StartTime",rs.getString("StartTime"));  
                        itemObj.put("Name",rs.getString("Name")); 
                        itemObj.put("KeyWord",rs.getString("KeyWord")); 
                        itemObj.put("UserName",rs.getString("UserName")); 
                        hashdetail.put(itemObj);
                    }
                }
            }
		} catch (Exception e) {            
			logger.error(e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
					logger.error(e.getMessage());
				}
            }
		}

		// if (count != -1) {
            // return new ResponseEntity<String>("success", HttpStatus.OK);
            // StringBuffer str = new StringBuffer();
            // str.append("result:").append(result).append(hashdetail.toString()).append(hashdetail.length());
            // return new ResponseEntity<String>(str.toString(), HttpStatus.OK);
            return new ResponseEntity<String>(hashdetail.toString(), HttpStatus.OK);

            // return new ResponseEntity<String>(Integer.toString(count), HttpStatus.OK);

        //     } else {
		// 	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // }
        



    } 
}
