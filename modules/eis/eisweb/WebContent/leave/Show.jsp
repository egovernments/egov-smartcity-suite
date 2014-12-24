<%@page contentType="text/html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 
		 org.egov.budget.services.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.*,
		 org.egov.pims.empLeave.dao.*,
		 org.egov.pims.empLeave.client.*,
		 org.egov.pims.empLeave.model.*,
		 org.egov.pims.empLeave.service.*,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		org.egov.pims.utils.*,
		 org.egov.pims.service.*,
		 org.egov.commons.CFinancialYear,
		 org.egov.commons.dao.CommonsDaoFactory,
		 org.egov.commons.dao.FinancialYearDAO,
		 org.egov.infstr.commons.*,
		 org.egov.pims.commons.client.*,
		 org.egov.infstr.commons.dao.*,
		 org.egov.infstr.commons.service.*,
		 org.egov.lib.address.dao.AddressDAO,
		 org.egov.lib.address.dao.AddressTypeDAO,
		 org.egov.lib.address.model.*,
		 org.egov.lib.address.dao.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"


%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Acknowledgement Page</title>

</head>
<body>
<table align='center'  id="signups">
<tr>
<td>
<center>
</center>

<form method="post" action="#">

 <div align="center">
 <font color="#800000" face="Verdana"><b></b></font>
</div>
<%
String mainString  ="";
if(request.getParameter("holvalue")!=null)
{

	mainString = (String)request.getParameter("holvalue");
}
else if(request.getParameter("leavevalue")!=null)
{
	String leaveId=(String)request.getParameter("leavevalue");
	EmpLeaveService empLeaveManager = EisManagersUtill.getEmpLeaveService();
	LeaveApplication leaveApplication = empLeaveManager.getLeaveApplicationById(new Integer(leaveId));
	mainString = "Leave Type Of "+leaveApplication.getTypeOfLeaveMstr().getName()+" between "+leaveApplication.getFromDate()+"to"+leaveApplication.getToDate();

}



%>
<div align="center">
 <br>
  <center>
  <table align='center' width="400" height="70" style="border: 1px solid #D7E5F2">

    <tr >
        <td class="labelcell" style="width:650">
	<p><font size="2"><b><center><%=mainString%>
          </center></b></font>
     	</p>
     	</td>
     	</tr>
     	<tr>
		     	<td><center>
		     	<input class="button" type="button" name="back" value="Back" onclick="window.location = '/leave/BeforeAttendenceMasterAction.do?submitType=beforeCreate';"/>
		     	</center>
		     	</td>
     	</tr>
     	</table>


      </center>
     </div>
     </div>
     </form>
    </div></div></div>
    </td></tr>
    <!-- Body Section Ends -->
    </table>
    </body>
   </html>
