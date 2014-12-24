<%@ page language="java" errorPage="/error/error.jsp" pageEncoding="UTF-8"  %>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s" %>

<%@ taglib uri="/WEB-INF/egov-authz.tld" prefix="egov" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ page import="org.egov.pims.model.*" %>

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title> Search Employee </title>
		
		


     
    </head>
    

<body id="Home">


  
 
  
	<script>
	function execute()
	{
		var target="null";
		
	
		  
	     document.searchForm.departmentId.value=0;
	     
	    
		 if(target!=null && target != "null")
		 {
			alert("null");
			
		 }
	}

	function SetActionValue(value,master,mode,prdId)
  	{

	
  }

	function showMsg()
  	{
  	document.empHistoryForm.action='/eis/reports/employeeHistoryReport.do?submitType=historyReportDetails';
	  	if(! validateForMandatory())
	  	{
	  	return false;
	  	}
	  	if(!checkDeptMandatory(document.getElementById("departmentId").value)){
				return false;
		}
	  	return true;
  	}
 	function checkAlphaNumeric(obj)
 	{
  		if(obj.value!=""){
	  		var num=obj.value;
	  		var objRegExp  = /^[a-zA-Z0-9]+$/;
	  		if(!objRegExp.test(num)){
	  		alert('Please Enter the proper code');
	  		obj.value="";
	  		obj.focus();
	  		}
  		}
	}
	

</script>  

	<html:form action="/reports/employeeHistoryReport">
		<div class="formmainbox">
			<div class="insidecontent">
			  	<div class="rbroundbox2">
					<div class="rbtop2">
						<div></div>

					</div>
				  	<div class="rbcontent2">

						<!-- Header Section Begins -->
						
						<!-- Header Section Ends -->
						
						<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" id="table2">
							<tr>
								<td>
									<!-- Tab Navigation Begins -->

									
									<!-- Tab Navigation Ends -->
									
									<!-- Body Begins -->
									
									
									<!-- Body Begins -->

									<table width="100%" cellpadding ="0" cellspacing ="0" border = "0"  id="searchTable">
										<tbody>
											<tr>
												<td>&nbsp;</td>
											</tr>

											<tr>
									  			<td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
												 
												<div class="headplacer"><p>Search Employee Reports</p></div></td>
									  		</tr>
									  		<tr>
									  			<td>&nbsp;</td>
									  		</tr>
											<input type="hidden" name="submitType"/>
											<tr>
												<td class="whiteboxwk" >Designation</td>
												<td class="whitebox2wk" >
													<html:select  name="empHistoryForm" tabindex="1" styleId="designationId" property="designationId" styleClass="selectwk">
													<html:option value="0">choose</html:option>
													<c:forEach var="designation" items="${designationMasterList}">
														<html:option value="${designation.designationId}" >${designation.designationName}</html:option>
													</c:forEach>
													</html:select>													
												</td>										
												
												<td  class="whiteboxwk" >Status</td>
												<td  class="whitebox2wk" >
													<html:select name="empHistoryForm"  tabindex="2" styleId="status" property="status" styleClass="dropdownsize">
													<html:option value="0">choose</html:option>
													<c:forEach var="status" items="${statusMasterList}">
														<html:option value="${status.id}" >${status.description}</html:option>
													</c:forEach>
													</html:select>
												</td>
													
												
												<td  class="whiteboxwk" >Type</td>
												<td  class="whitebox2wk" width="305" colspan="3">
													<html:select  name="empHistoryForm" tabindex="3" styleId="empType" property="empType" styleClass="dropdownsize">
													<html:option  value="0">choose</html:option>
													<c:forEach var="empType" items="${employeeStatusMasterList}">
														<html:option value="${empType.id}" >${empType.name}</html:option>
													</c:forEach>
													</html:select>
												</td>
													
												
												
											</tr>
											<tr>
											    <td  class="greyboxwk">Employee Code</td>
											    <td   class="greybox2wk" width="10%" >
													<html:text name="empHistoryForm" styleId="code"  property="code" onblur="checkAlphaNumericForCode(this);" styleClass="selectwk" />
												</td>
												<td  class="greyboxwk">Employee Name</td>

												<td  class="greybox2wk" >
												  <html:text name="empHistoryForm" styleId="name"  property="name" styleClass="selectwk" />
												</td>
									  		</tr>
									  		<%@include file="/reports/mastersByAppconfig.jsp" %>
									  		
									 		
										</tbody>
									</table>
									
									<br>
								
									
									
									
								</td>
							</tr>

							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
							    <td><div align="right" class="mandatory">* Mandatory Fields</div></td>
							</tr>
						</table>
						<div align="center">
									<html:submit styleClass="buttonfinal"  property="submitType" onclick="return showMsg();" />

		  							<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
		  							</div>
					</div>

					<div class="rbbot2">
						<div></div>
					</div>
				</div>
			</div>
			
		</div>

		<div>
			<c:if test="${not empty empList }">
			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
				  <td class="headingwk" colspan="5">
				  <div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
				  <div align="left" style="margin-top:4px;">Employee Details</div>
				
				  </td>
				</tr>
	
				<tr>
					<td>
				 		<display:table name="empList" id="eid" cellspacing="0" style="width: 100%;" 
				 		requestURI="${pageContext.request.contextPath}/reports/employeeHistoryReport.do?submitType=gethistoryReportDetails"
				  			export="false" defaultsort="2" pagesize = "15" sort="list"  class="its" >
							<display:setProperty name="paging.banner.placement" value="bottom"/>
					
							 <display:column style="tablesubheadwk:10%"   property="employeeCode" title="Employee Code" />
							 <display:column style="tablesubheadwk:5%"   property="employeeName" title="Employee Name" />
							 <display:column style="tablesubheadwk:5%"   property="desigId.designationName" title="Employee Designation" />
							 <display:column style="tablesubheadwk:5%"   property="deptId.deptName" title="Employee Department" />
							 <% Integer id=((EmployeeView)pageContext.getAttribute("eid")).getId();
							 %>
							 <display:column style="width:5%" title="Reports" >
								 	<a  href="${pageContext.request.contextPath}/pims/AfterSearchAction.do?submitType=executeReportSearch&Id=<%=id %>">
								 		<FONT class="labelcell">Employee History</FONT>
								 	</a>
								</display:column>
						</display:table>
					</td>
				</tr>
			</table>	
			</c:if>	
		
		</div>
										 

	</html:form>

	</body>
</html>

