package com.welcohealth.email.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jboss.logging.Logger;


public class EmailDAO {
	
	private static final Logger log = Logger.getLogger(EmailDAO.class.getName());
	
	private static String jdbcHandle = "jdbc:sqlserver://10.157.95.101:1433;databaseName=StrongMail";
	private static String dbusername = "StrongMail";
	private static String dbpassword = "Str0ngMai!";
	
	private static String[] userfields = {
		"timestamp",
		"email",
		"firstname",
		"lastname",
		"address",
		"address2",
		"city",
		"state",
		"postalcode",
		"ipaddress",
		"dob",
		"subcampaignId",
		"campaignId",
		"registrationId",
		"dnsname",
		"daterec",
		"sessionId"
	};
	
	private static String[] thedailyjackpotuserfields = {
		"email",
		"encRegistrationId",
		"registrationId"
	};
	
	private static String[] surveyfields = {
	      "sessionid",
		  "children",
		  "active",
		  "healthinsurance",
		  "lifeinsurance",
		  "allergies",
		  "rxdrugs",
		  "diabetic",
		  "pain"
	};
	
	
	static public void loadData(String storedProc, User user){
		
		CallableStatement callableStatement = null;
		//PreparedStatement pstmt = null;
		Connection con = null;
		
		Field[] allFields = User.class.getDeclaredFields();
		
		String sprocParamCount = createParamPlaceholder(userfields.length);
		try{
		  String sproc = String.format("{call %s(%s)}", storedProc, sprocParamCount);
		  //String insert = "INSERT INTO [StrongMail].[dbo].[SUCCESS_LOG] ( [Datestamp], [Email], [OUTBOUNDIP], [OUTBOUNDHOST], [MXIP], [MXHOST], [TemplateId], [Uuid] ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
		  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		  con = DriverManager.getConnection(jdbcHandle, dbusername, dbpassword);
		  //con = DriverManager.getConnection(azureConnection);
		  callableStatement = con.prepareCall(sproc);
		  //pstmt = con.prepareStatement(insert);
		  
		  for (Field field : allFields) {
		    	
			 String fieldName = field.getName();
			 if (Arrays.asList(userfields).contains(fieldName)){
			  
			    String methodString = String.format("get%s", fieldName.replaceFirst(String.valueOf(fieldName.charAt(0)), String.valueOf(Character.toUpperCase(fieldName.charAt(0)))) );
			    Method method = Class.forName(User.class.getName()).getDeclaredMethod(methodString);
			  
		        if (field.getType() == String.class) {
		        	
		        	int param = Arrays.asList(userfields).indexOf(field.getName()) + 1;
		        	String userValue = (String) method.invoke(user);
		        	callableStatement.setString(param, userValue);
		        	
		        }
		        else if (field.getType() == Integer.class) {
		        	
		        	int param = Arrays.asList(userfields).indexOf(field.getName()) + 1;
		        	Integer userValue = (Integer) method.invoke(user);
		        	callableStatement.setInt(param, userValue);
		        	
		        }
		        else if (field.getType() == Long.class) {
		        	
		        	int param = Arrays.asList(userfields).indexOf(field.getName()) + 1;
		        	Long userValue = (Long) method.invoke(user);
		        	callableStatement.setLong(param, userValue);
		        	
		        }
		        else if (field.getType() == Timestamp.class) {
		           
		        	int param = Arrays.asList(userfields).indexOf(field.getName()) + 1;
		        	Timestamp userValue = (Timestamp) method.invoke(user);
		        	callableStatement.setTimestamp(param, userValue);
		        }
		        
			  }  
		    }

		    callableStatement.executeUpdate();
		      
		      /*
		      pstmt.setTimestamp(1, es.timestamp);
		      pstmt.setString(2, es.toAddress);
		      pstmt.setString(3, es.outboundIP);
		      pstmt.setString(4, es.outboundHost);
		      pstmt.setString(5, es.remoteIP);
		      pstmt.setString(6, es.remoteHost);
		      pstmt.setInt(7, Integer.valueOf(es.templateId));
		      pstmt.setString(8, es.uuid);
		      int insertResult = pstmt.executeUpdate();
		      */
		      //System.out.println("Remote Success " + String.valueOf(insertResult));
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			log.error("SQL EXCEPTION: " + sqle.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("EXCEPTION: " + e.getMessage());
		}
                finally{
                     if (callableStatement != null){
                       try {
						callableStatement.close();
					    } catch (SQLException e) {}
                      }
                     if (con != null){
                       try {
						con.close();
					    } catch (SQLException e) {}
                      }

                  }
             
	}
	
	
	
	
	
	
	
	
	
	static public void loadData(String storedProc, TheDailyJackpotUser user){
		
		CallableStatement callableStatement = null;
		//PreparedStatement pstmt = null;
		Connection con = null;
		
		Field[] allFields = TheDailyJackpotUser.class.getDeclaredFields();
		
		String sprocParamCount = createParamPlaceholder(thedailyjackpotuserfields.length);
		try{
		  String sproc = String.format("{call %s(%s)}", storedProc, sprocParamCount);
		  //String insert = "INSERT INTO [StrongMail].[dbo].[SUCCESS_LOG] ( [Datestamp], [Email], [OUTBOUNDIP], [OUTBOUNDHOST], [MXIP], [MXHOST], [TemplateId], [Uuid] ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
		  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		  con = DriverManager.getConnection(jdbcHandle, dbusername, dbpassword);
		  //con = DriverManager.getConnection(azureConnection);
		  callableStatement = con.prepareCall(sproc);
		  //pstmt = con.prepareStatement(insert);
		  
		  for (Field field : allFields) {
		    	
			 String fieldName = field.getName();
			 if (Arrays.asList(thedailyjackpotuserfields).contains(fieldName)){
			  
			    String methodString = String.format("get%s", fieldName.replaceFirst(String.valueOf(fieldName.charAt(0)), String.valueOf(Character.toUpperCase(fieldName.charAt(0)))) );
			    Method method = Class.forName(User.class.getName()).getDeclaredMethod(methodString);
			  
		        if (field.getType() == String.class) {
		        	
		        	int param = Arrays.asList(thedailyjackpotuserfields).indexOf(field.getName()) + 1;
		        	String userValue = (String) method.invoke(user);
		        	callableStatement.setString(param, userValue);
		        	
		        }
		        else if (field.getType() == Integer.class) {
		        	
		        	int param = Arrays.asList(thedailyjackpotuserfields).indexOf(field.getName()) + 1;
		        	Integer userValue = (Integer) method.invoke(user);
		        	callableStatement.setInt(param, userValue);
		        	
		        }
		        else if (field.getType() == Long.class) {
		        	
		        	int param = Arrays.asList(thedailyjackpotuserfields).indexOf(field.getName()) + 1;
		        	Long userValue = (Long) method.invoke(user);
		        	callableStatement.setLong(param, userValue);
		        	
		        }
		        else if (field.getType() == Timestamp.class) {
		           
		        	int param = Arrays.asList(thedailyjackpotuserfields).indexOf(field.getName()) + 1;
		        	Timestamp userValue = (Timestamp) method.invoke(user);
		        	callableStatement.setTimestamp(param, userValue);
		        }
		        
			  }  
		    }

		    callableStatement.executeUpdate();
		      
		      /*
		      pstmt.setTimestamp(1, es.timestamp);
		      pstmt.setString(2, es.toAddress);
		      pstmt.setString(3, es.outboundIP);
		      pstmt.setString(4, es.outboundHost);
		      pstmt.setString(5, es.remoteIP);
		      pstmt.setString(6, es.remoteHost);
		      pstmt.setInt(7, Integer.valueOf(es.templateId));
		      pstmt.setString(8, es.uuid);
		      int insertResult = pstmt.executeUpdate();
		      */
		      //System.out.println("Remote Success " + String.valueOf(insertResult));
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			log.error("SQL EXCEPTION: " + sqle.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("EXCEPTION: " + e.getMessage());
		}
                finally{
                     if (callableStatement != null){
                       try {
						callableStatement.close();
					    } catch (SQLException e) {}
                      }
                     if (con != null){
                       try {
						con.close();
					    } catch (SQLException e) {}
                      }

                  }
             
	}
	
	
	
	
	
	
	
	
	static public void loadData(String storedProc, HealthSurvey survey){
		
		CallableStatement callableStatement = null;
		//PreparedStatement pstmt = null;
		Connection con = null;
		
		Field[] allFields = HealthSurvey.class.getDeclaredFields();
		
		String sprocParamCount = createParamPlaceholder(surveyfields.length);
		try{
		  String sproc = String.format("{call %s(%s)}", storedProc, sprocParamCount);
		  //String insert = "INSERT INTO [StrongMail].[dbo].[SUCCESS_LOG] ( [Datestamp], [Email], [OUTBOUNDIP], [OUTBOUNDHOST], [MXIP], [MXHOST], [TemplateId], [Uuid] ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
		  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		  con = DriverManager.getConnection(jdbcHandle, dbusername, dbpassword);
		  //con = DriverManager.getConnection(azureConnection);
		  callableStatement = con.prepareCall(sproc);
		  //pstmt = con.prepareStatement(insert);
		  
		  for (Field field : allFields) {
		    	
			 String fieldName = field.getName();
			 if (Arrays.asList(surveyfields).contains(fieldName)){
			  
			    String methodString = String.format("get%s", fieldName.replaceFirst(String.valueOf(fieldName.charAt(0)), String.valueOf(Character.toUpperCase(fieldName.charAt(0)))) );
			    Method method = Class.forName(User.class.getName()).getDeclaredMethod(methodString);
			  
		        if (field.getType() == String.class) {
		        	
		        	int param = Arrays.asList(surveyfields).indexOf(field.getName()) + 1;
		        	String userValue = (String) method.invoke(survey);
		        	callableStatement.setString(param, userValue);
		        	
		        }
		        else if (field.getType() == Integer.class) {
		        	
		        	int param = Arrays.asList(surveyfields).indexOf(field.getName()) + 1;
		        	Integer userValue = (Integer) method.invoke(survey);
		        	callableStatement.setInt(param, userValue);
		        	
		        }
		        else if (field.getType() == Long.class) {
		        	
		        	int param = Arrays.asList(surveyfields).indexOf(field.getName()) + 1;
		        	Long userValue = (Long) method.invoke(survey);
		        	callableStatement.setLong(param, userValue);
		        	
		        }
		        else if (field.getType() == Timestamp.class) {
		           
		        	int param = Arrays.asList(surveyfields).indexOf(field.getName()) + 1;
		        	Timestamp userValue = (Timestamp) method.invoke(survey);
		        	callableStatement.setTimestamp(param, userValue);
		        }
		        
			  }  
		    }

		    callableStatement.executeUpdate();
		      
		      /*
		      pstmt.setTimestamp(1, es.timestamp);
		      pstmt.setString(2, es.toAddress);
		      pstmt.setString(3, es.outboundIP);
		      pstmt.setString(4, es.outboundHost);
		      pstmt.setString(5, es.remoteIP);
		      pstmt.setString(6, es.remoteHost);
		      pstmt.setInt(7, Integer.valueOf(es.templateId));
		      pstmt.setString(8, es.uuid);
		      int insertResult = pstmt.executeUpdate();
		      */
		      //System.out.println("Remote Success " + String.valueOf(insertResult));
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			log.error("SQL EXCEPTION: " + sqle.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("EXCEPTION: " + e.getMessage());
		}
                finally{
                     if (callableStatement != null){
                       try {
						callableStatement.close();
					    } catch (SQLException e) {}
                      }
                     if (con != null){
                       try {
						con.close();
					    } catch (SQLException e) {}
                      }

                  }
             
	}
	
	
	
	
	
	
	
	
	
	
	
	private static String createParamPlaceholder(int cnt){
		
		StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < cnt; i++){
	    	sb.append("?,");
	    }
	    return sb.toString().substring(0, sb.toString().length() - 1);
		
	}
	

}
