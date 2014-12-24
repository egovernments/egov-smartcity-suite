
<%@page import="org.egov.pims.model.EmployeeView"%><!--
	Program Name : Search Employee.jsp
	Author		: DivyaShree MS
	Created	on	: 08-02-2010
	Purpose 	: Search Employee by designation,dept,code,name,type or status
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egovtags" tagdir="/WEB-INF/tags"%>



<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*"%>



<html>
  <head>
   <title>Employee Search</title>
   <script type="text/javascript">
   function onsubmit()
   {
   if(!checkDeptMandatory( document.searchForm.deptId.options.selectedIndex)){
				return false;
		}
		else 
		return true;
	}
  
</script>
  </head>
  
  
  <body> 
  <%
      String mode=(String)request.getAttribute("mode");
  		
      String name="";
  	  if(mode.equals("view"))
  	  {
  		  name="View";
  	  }
  	  else
  	  {
  		  name="Create/Update";
  	  }
  	
  %>
   <s:form action="searchForEmployee" theme="simple" name="searchForm">  
   <div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
<table width="95%" cellpadding ="0" cellspacing ="0" border = "0">
	  <tbody>
<tr><td>&nbsp;</td></tr>

  <tr>
   <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
   <p><div class="headplacer">Search Employee</div></p></td><td></td> </tr>
   </tbody>
  
  </table>
       <table width="95%" cellpadding="0" cellspacing="0" border="0">
       <tr>
       <td>
       <input type="hidden" name="mode" value="<%=mode%>"/>
       </td>
       </tr>
  		<%@ include file='searchForEmployee-form.jsp'%>
  		
  		<tr><td>&nbsp;</td></tr>
	<tr>
	
		</tr>
		</table>
		<c:if test="${employeeList != null}">
		<table align="center" width="95%" border="0" cellpadding="0" cellspacing="0">
														<tr>
															<td class="headingwk" colspan="5">
														  		<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
														  		<div align="left" style="margin-top:4px;">Employee Details</div>
														  	</td>
														</tr>
				</table>
				<center><display:table name="${employeeList}" id="EmployeeView" cellspacing="0" style="width: 750;" 
										  				export="false" pagesize = "15" sort="list"  class="its" 
										  				requestURI="${pageContext.request.contextPath}/nomineeMaster/searchForEmployee.action">

													
												<display:column style="tablesubheadwk:5%"  property="employeeCode" title="Employee Code" />
											 	<display:column style="tablesubheadwk:10%"   property="employeeName" title="Employee Name" />
											 		<display:column style="tablesubheadwk:10%"   property="desigId.designationName" title="Employee Designation" />
											 		<display:column style="tablesubheadwk:10%"  property="deptId.deptName" title="Employee Department" />
													<display:column style="tablesubheadwk:5%" title="Masters" >
													<% 
														Integer id=(((EmployeeView)pageContext.getAttribute("EmployeeView")).getId());
														
													%>
													
													 <a  href="${pageContext.request.contextPath}/nomineeMaster/nomineeCreateModify!Create.action?mode=<%=mode%>&Id=<%=id%>"><FONT class="labelcell"><%=name%></FONT></a>
													
									                </display:column>
														
														
	</display:table></center>
												
					</c:if>							
	
  		
 		
   	</div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>
	
	<center><s:submit method="search" value="SEARCH" cssClass="buttonfinal"/></center>
	</s:form>
	
  </body>
</html>
