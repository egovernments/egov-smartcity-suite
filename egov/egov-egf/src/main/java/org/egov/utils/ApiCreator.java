package org.egov.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ApiCreator {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * remove existing setter which adds the quotes  and put new normal setter.
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		args = new String[10];
		//name of the domain object
		args[1] = "com.exilant.eGov.src.domain.FinancialYear";
		//name of the table
		args[2] = "financialyear";
		//List of egov and other classes which are declared 
		// no need to remove these if file does not contain these just keep adding
		List<String> excludeList = new ArrayList<String>();
		excludeList.add("EGovernCommon");
		excludeList.add("Logger");
		excludeList.add("TaskFailedException");
		excludeList.add("SimpleDateFormat");
		excludeList.add("Statement");
		excludeList.add("GenericHibernateDaoFactory");
		//List of name which doesnt participate in insertion or updation
		// no need to remove these if file does not contain these just keep adding
		List<String> excludeNameList=new ArrayList<String>();
		excludeNameList.add("isId");
		//if id has different name like user_id replace the below id with actual id name	
		excludeNameList.add("id");
		excludeNameList.add("isId");
		excludeNameList.add("insertQuery");
		excludeNameList.add("updateQuery");
		excludeNameList.add("isField");
		
		
		Class<?> apiClass = Class.forName(args[1]);
		Field[] fields = apiClass.getDeclaredFields();
		StringBuilder apiString = new StringBuilder(500);
		apiString.append("public void newUpdate(Connection con) throws TaskFailedException,SQLException{");
		apiString.append("EGovernCommon commommethods = new EGovernCommon();\n created = commommethods.getCurrentDate(con);\n PreparedStatement pstmt=null;");
		apiString.append("try {\n created = formatter.format(sdf.parse(created));\n } catch (ParseException parseExp) " +
				"{ \n if(LOGGER.isDebugEnabled())     LOGGER.debug(parseExp.getMessage(),parseExp);}	\n	setCreated(created); \n setLastModified(created);");
		apiString
				.append(" StringBuilder query=new StringBuilder(500); \n query.append(\"update "
						+ args[2] + " set \");\n");
		int i = 0;
		//System.out.println(fields.length);
		for (Field f : fields) {
			
			if (excludeList.contains(f.getType().getSimpleName())||excludeNameList.contains(f.getName())) {
				continue;
			} else {
				apiString.append(" if(" + f.getName() + "!=null)	");
					apiString.append("query.append(\"" + f.getName() + "=?,\");\n");
			
			}

		}
	//These two statements remove the last comma in the query 
		apiString.append("int lastIndexOfComma = query.lastIndexOf(\",\");\n");
		apiString.append("query.deleteCharAt(lastIndexOfComma);\n");
		//if id has different name like user_id replace the below id with actual id name
		apiString.append("query.append(\" where id=?\");\n try{");
		apiString
				.append(" int i=1;\n pstmt=con.prepareStatement(query.toString());\n");
		for (Field f : fields) {
			if (excludeList.contains(f.getType().getSimpleName())||excludeNameList.contains(f.getName())) {
				continue;
			} else {
			apiString.append(" if(" + f.getName() + "!=null)	");
			apiString.append("pstmt.set" + f.getType().getSimpleName()
					+ "(i++," + f.getName() + ");\n");

		}
		}
		//if id has different name like user_id replace the below id with actual id name
		apiString.append("pstmt.setString(i++,id);\n");
		apiString.append("\n pstmt.executeQuery();\n");
		apiString.append("}catch(Exception e){	LOGGER.error(\"Exp in update: \"+e.getMessage()); throw taskExc;} finally{");
		apiString.append("try{ pstmt.close(); }catch(Exception e){LOGGER.error(\"Inside finally block of update\");}} \n }");
		System.out.println(apiString.toString());

	}

}
