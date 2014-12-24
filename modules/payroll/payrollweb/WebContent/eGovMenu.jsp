<%@page import="org.egov.lib.rjbac.user.ejb.server.UserServiceImpl"%>
<%@ page contentType="text/html; charset=Cp1252" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*,
				org.egov.infstr.utils.HibernateUtil,
				org.egov.lib.rjbac.user.ejb.api.*,
				org.egov.lib.rjbac.user.*,
				org.egov.lib.rjbac.role.Role
				"
			%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/sdmenu.css" />
	<script type="text/javascript" src="javascript/sdmenu.js"></script>

	<script type="text/javascript">
		// <![CDATA[
		var myMenu;
		window.onload = function() {
			myMenu = new SDMenu("egov_menu");
			myMenu.init();
		};
		// ]]>

	function execute(){
	  	var target="<%=(request.getAttribute("alertMessage"))%>";
		 if(target!="null") {
			alert("<%=request.getAttribute("alertMessage")%>");
			<%	request.setAttribute("alertMessage",null);	%>
		 }
	}
	</script>

	<%
		String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
		User user = null;
		UserServiceImpl userService=new UserServiceImpl();
		ArrayList roleNames = new ArrayList();
		if(user != null){
			System.out.println(user.getUserName());

			if (user.getRoles() != null) {
				for(Iterator iter= user.getRoles().iterator(); iter.hasNext();) {
					Role role = (Role)iter.next();
					roleNames.add(role.getRoleName());
				}
			}
		}
		boolean bSuperUser = roleNames.contains("SUPER USER");
		boolean bAdministrator = roleNames.contains("Administrator");
		boolean bAccountant = roleNames.contains("Accountant");
		boolean bPayroll = roleNames.contains("Payroll");
		System.out.println("bAdministrator = " + bAdministrator);
	%>


</head>

<body >
   <c:set var="Administrator" value="<%= new Boolean(bAdministrator) %>" />
   <c:set var="SuperUser" value="<%= new Boolean(bSuperUser) %>" />
   <c:set var="Payroll" value="<%= new Boolean(bPayroll) %>" />
   <c:set var="Accountant" value="<%= new Boolean(bAccountant) %>" />
   <div style="float: left" id="egov_menu" class="sdmenu">
         <div id="first-child">
           <span>Payroll</span>

            <c:if test="${Payroll || SuperUser || Administrator || Accountant}">
	        <div>
	   		<span>Payslip</span>
	   		<a href="<c:url value="/payslip/ManualGenPaySlipsCentralAction.do?mode=central" />" target="mainFrame">Create (for new employee)</a>
	   		<a href="<c:url value="/payslip/BeforeviewGenPaySlips.do?mode=central" />" target="mainFrame">View</a>
	   		<a href="<c:url value="/payslip/generatePDF.jsp" />" target="mainFrame">View Payslips(PDF format)</a>
	   		<a href="<c:url value="/inbox/payslipInbox.jsp" />" target="mainFrame">Approve</a>
			<a href="<c:url value="/payslipapprove/payslipApproveCriteria.jsp" />" target="mainFrame">Approve without jbpm</a>
	   		<a href="<c:url value="/payslip/genBatchPaySlips.jsp" />" target="mainFrame">Generate Batch PaySlips</a>
	   		<a href="<c:url value="/reports/paySlipsReport.jsp?mode=resolve" />" target="mainFrame">Failure PaySlips</a>
			<a href="<c:url value="/payslip/generateSuppPaySlip.do?submitType=beforeSuppPayslip" />" target="mainFrame">Supplementary PaySlips</a>

	        </div>
           </c:if>
           <c:if test="${Payroll || SuperUser}">
           <div>
		<span>Advance</span>
		<a href="<c:url value="/salaryadvance/beforeSalaryadvance.do" />" target="mainFrame">Create</a>
		<a href="<c:url value="/advance/searchAdvanceModify.jsp?mode=view" />" target="mainFrame">View</a>
		<a href="<c:url value="/advance/searchAdvanceModify.jsp?mode=modify" />" target="mainFrame">Modify</a>
		<a href="<c:url value="/advance/searchAdvances.jsp" />" target="mainFrame">Sanction</a>
           </div>
           </c:if>
           <c:if test="${Payroll || SuperUser}">
           <div>
		<span>Payroll Exceptions</span>
		<a href="<c:url value="/exception/beforeException.do?submitType=getExceptionInfo" />" target="mainFrame">Create</a>
		<a href="<c:url value="/exception/searchException.jsp" />" target="mainFrame">View</a>
           </div>
           </c:if>

		<c:if test="${Accountant || SuperUser}">
		   <div>
			<span>Adv Disbursement</span>
			<div>
				<span>By Cheque</span>
				<a href="<c:url value="/salaryadvance/disbursementByCheque.do?submitType=beforeCreateDisbursement"/>" target="mainFrame">Create</a>
				<a href="<c:url value="/salaryadvance/disbursementByCheque.do?submitType=beforeChequeDisbursementList&mode=view"/>" target="mainFrame">View</a>
				<a href="<c:url value="/salaryadvance/disbursementByCheque.do?submitType=beforeChequeDisbursementList&mode=modify"/>" target="mainFrame">Modify</a>

			</div>
			<div>
				<span>By Cash/Direct</span>
				<a href="<c:url value="/salaryadvance/advanceDisbursement.do?submitType=beforeCreateDisbursement"/>" target="mainFrame">Create</a>
				<a href="<c:url value="/salaryadvance/advanceDisbursement.do?submitType=beforeDisbursementList&mode=view"/>" target="mainFrame">View</a>
				<a href="<c:url value="/salaryadvance/advanceDisbursement.do?submitType=beforeDisbursementList&mode=modify"/>" target="mainFrame">Modify</a>
			</div>
		  </div>
	   </c:if>
           <c:if test="${Administrator || SuperUser}">
	      <div>
		<span>Payhead</span>
		<a href="<c:url value="/payhead/beforePayhead.do" />" target="mainFrame">Create</a>
		<a href="<c:url value="/payhead/searchPayhead.do?mode=view" />" target="mainFrame">View</a>
		<a href="<c:url value="/payhead/searchPayhead.do?mode=modify" />" target="mainFrame">Modify</a>
	      </div>
	    </c:if>
	    <c:if test="${Administrator || SuperUser}">
	      <div>
			<span>Payscale</span>
			<a href="<c:url value="/payslip/CreatePayScale.jsp" />" target="mainFrame">Create</a>
			<a href="<c:url value="/payslip/payScaleSearch.jsp?mode=view" />" target="mainFrame">View</a>
			<a href="<c:url value="/payslip/payScaleSearch.jsp?mode=modify" />" target="mainFrame">Modify</a>
	      </div>
           </c:if>

		    <c:if test="${Administrator || Accountant || SuperUser}">
		   <div>
			<span>Pension</span>
			<div>
				<span>Compute Gratuity</span>
				<a href="<c:url value="/pension/search.jsp?mode=create"/>" target="mainFrame">Create</a>
				<a href="<c:url value="/pension/search.jsp?mode=modify"/>" target="mainFrame">Modify</a>
				<a href="<c:url value="/pension/search.jsp?mode=view"/>" target="mainFrame">View</a>
			<!--<a href="<c:url value="/pension/searchForGratuityCreate.jsp?mode=search"/>" target="mainFrame">Create</a>
				<a href="<c:url value="/pension/searchForGratuityModify.jsp?mode=search"/>" target="mainFrame">Modify</a>
				<a href="<c:url value="/pension/serachForGratuityView.jsp?mode=searchForView"/>" target="mainFrame">View</a>	-->
				<a href="<c:url value="/pensionInbox/Inbox.jsp" />" target="mainFrame">Approve</a>				
			</div>
			<div>
				<span>Disburse Gratuity</span>			
				<a href="<c:url value="/pension/searchForGratuityDisburse.jsp?mode=create"/>" target="mainFrame">create</a>
				<a href="<c:url value="/pension/searchForGratuityDisburse.jsp?mode=view"/>" target="mainFrame">View</a>
			</div>
			<div>
				<span>Recoveries</span>
					<a href="<c:url value="/recordRecovery/search.jsp?mode=create"/>" target="mainFrame">Record Recoveries</a>					
				</div>
		  </div>
	   </c:if>

         </div>

         <div>
	    <span>Employee System</span>

	    <c:if test="${Payroll || SuperUser}">
	    <div>
		<span>Employee</span>
		<a href="<c:url value="/pims/BeforePIMSMasterAction.do?submitType=beforeCreate&master=Employee" />" target="mainFrame">Create</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Employee&mode=View" />" target="mainFrame">View</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Employee&mode=Modify" />" target="mainFrame">modify</a>
	    </div>
	    <div>
		<span>Disciplinary</span>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Disciplinary&mode=Create" />" target="mainFrame">Create</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Disciplinary&mode=View" />" target="mainFrame">View</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Disciplinary&mode=Modify" />" target="mainFrame">modify</a>
	    </div>
	    <div>
		<span>Assignment</span>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Assignment&mode=View" />" target="mainFrame">View</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Assignment&mode=Modify" />" target="mainFrame">Modify</a>

	    </div>
	    <div>
		<span>Training Particulars</span>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=TraningPirticulars&mode=Create" />" target="mainFrame">Create</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=TraningPirticulars&mode=View" />" target="mainFrame">View</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=TraningPirticulars&mode=Modify" />" target="mainFrame">modify</a>
	    </div>
	    <div>
		<span>Availed Particulars</span>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=AvailedParticulars&mode=Create" />" target="mainFrame">Create</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=AvailedParticulars&mode=View" />" target="mainFrame">View</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=AvailedParticulars&mode=Modify" />" target="mainFrame">modify</a>
	    </div>
	    </c:if>
	    <c:if test="${Payroll || SuperUser}">

	    <div>
		<span>Leave </span>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=LeaveApplication&mode=Create" />" target="mainFrame">Apply</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=LeaveApplication&mode=View" />" target="mainFrame">View</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=LeaveApplication&mode=Modify" />" target="mainFrame">Modify</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=LeaveApplication&mode=approve" />" target="mainFrame">Approve</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=CompOff&mode=approve" />" target="mainFrame">Approve CompensatoryOff</a>
	    </div>
	    <div>
		<span>Attendance</span>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Attendence&viewMode=modify" />" target="mainFrame">Create</a>
	    </div>
	    </c:if>
	    <c:if test="${Payroll || Administrator || Accountant || SuperUser}">
	    <div>
		<span>Reports</span>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Employee&mode=Reports" />" target="mainFrame">Employee History</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=AttReport" />" target="mainFrame">Attendance</a>
		<a href="<c:url value="/pims/BeforeSearchAction.do?module=Employee&masters=Leave&mode=Reports" />" target="mainFrame">Leave</a>
	    </div>
	    </c:if>
	     <c:if test="${Administrator || SuperUser}">
	    	    <div>
	    		<span>Employee Masters</span>
	    		<!-- <a href="<c:url value="/infstr/BeforeDesignationMasterAction.do?submitType=beforCreate" />" target="mainFrame">Designation</a>  -->
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=BloodGroupMaster" />" target="mainFrame">Blood Group</a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=CategoryMaster" />" target="mainFrame">Category</a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=CommunityMaster" />" target="mainFrame">Community</a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=HowAcquiredMaster" />" target="mainFrame">Property</a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=LanguagesKnownMaster" />" target="mainFrame">Languages known</a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=LanguagesQulifiedMaster" />" target="mainFrame">Local Languages Qulified </a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=PayFixedInMaster" />" target="mainFrame">Pay Fixed</a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=RecruimentMaster" />" target="mainFrame">Recruiment Mode </a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=ReligionMaster" />" target="mainFrame">Religion </a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=TestNameMaster" />" target="mainFrame">Departmental Test </a>
	    		<a href="<c:url value="/pims/BeforeGenericMasterAction.do?submitType=beforeCreate&className=EmployeeStatusMaster" />" target="mainFrame">Employee Status</a>
	    	    </div>
	    	    </c:if>
	    	    <c:if test="${Administrator || SuperUser}">
	    	    <div>
	    		<span>Leave Masters</span>
	    		<a href="<c:url value="/leave/BeforeLeaveTypeAction.do?submitType=beforCreate" />" target="mainFrame">Leave Type</a>
	    		<a href="<c:url value="/leave/BeforeLeaveMasterAction.do?submitType=view&mode=save" />" target="mainFrame">Designation-Leave Mapping</a>
				<a href="<c:url value="/leave/BeforeOpeningBalanceAction.do?submitType=view&mode=save" />" target="mainFrame">Create/Update Open Leave Balance</a>
	    	    </div>
	    	    <div>
	    		<span>Calender</span>
	    		<a href="<c:url value="/leave/chooseFinancialYear.jsp?mode=beforeCreate" />" target="mainFrame">Create</a>
	    		<a href="<c:url value="/leave/chooseFinancialYear.jsp?mode=modify" />" target="mainFrame">Modify</a>
	    		<a href="<c:url value="/leave/chooseFinancialYear.jsp?mode=setIdForDetailsView" />" target="mainFrame">View</a>
	    	    </div>

	    </c:if>
        </div>
    </div>
</body>
