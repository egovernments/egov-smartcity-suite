
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 
		 		 org.egov.commons.ObjectType,
		 		 org.apache.log4j.Logger,
		 		 org.egov.pims.*,
		 		 org.egov.pims.empLeave.dao.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 org.egov.pims.dao.*,
		 		 		 org.egov.pims.model.*,
		 		 		 org.egov.pims.service.*,
		 		 		 org.egov.pims.utils.*,
		 		 org.egov.commons.CFinancialYear,
		 		 org.egov.lib.rjbac.user.User,
		 		 org.egov.lib.rjbac.dept.ejb.api.*,
		 		 org.egov.commons.dao.CommonsDaoFactory,
		 		 org.egov.commons.dao.FinancialYearDAO,
		 		 org.egov.infstr.commons.service.*,
		 		 org.egov.infstr.client.filter.EGOVThreadLocals,
		 		 org.egov.lib.address.dao.AddressDAO,
		 		 org.egov.lib.address.dao.AddressTypeDAO,
		 		 org.egov.lib.address.model.*,
		 		 org.egov.lib.address.dao.*,
		 		 java.text.SimpleDateFormat,
		 		 org.egov.infstr.commons.*,
				 org.egov.pims.commons.*,
		 		 org.egov.infstr.commons.client.*,
		 		 org.egov.infstr.commons.dao.*,

		 org.egov.pims.client.*"

%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><bean:message key="PersonalInfoSys"/></title>

    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>


<%
Integer Id=new Integer(request.getParameter("Id")==null || request.getParameter("ess")!=null?(String)request.getAttribute("Id"):request.getParameter("Id"));
 %>

<script>

function goindex(arg)
{

	if(arg == "Index")
	{
	
		document.forms("employeeSearchForm").action = "LeaveApplication/staff/index.jsp";
		document.forms("employeeSearchForm").submit();
	}


}


</script>


</head>

<div align="center">
<table align='center'>
<body>
<tr>
<td style="buffer:0pt; padding:0pt; margin:0pt">

</table>
</div>

<Center>



			<!-- Tab Navigation Begins -->
				<table align='center'>
				<tr>
				<td align="center">
				<!-- Tab Navigation Begins -->

<center>
</center>
<!-- Tab Navigation Ends -->
				</td>
				</tr>
				</table>
		<!-- Tab Navigation Ends -->




<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table align='center' id="table2" width="810" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/leave/BeforeLeaveAction.do?master=LeaveApproval" >
<input type=hidden name="empId" id="empId" value="<%= Id %>" />
<div align="center">
<center>

<%!
	Integer userId = new Integer(0);
%>
<%
	String master = (String)request.getParameter("master");
	userId =(Integer)session.getAttribute("com.egov.user.LoginUserId");
	
%>
<%!

	public boolean checkSupEmployee(Integer emp,ObjectType objectType)
	{
	   boolean isApprover = false;

		PersonalInformation loggedInEmp =null;
		
		if(userId!=null)
		{
			loggedInEmp = EisManagersUtill.getEmployeeService().getEmpForUserId(userId);
		}
		

		Position pos = null;
		pos =EisManagersUtill.getEmployeeService().getPositionforEmp(emp);
		System.out.println("POSITION ID==="+pos);
		User supUser = null;
		//PersonalInformation supEmp = null;
		//Integer supId = new Integer(0);

		if(pos!=null) {
			supUser = EisManagersUtill.getEisCommonsService().getSupUserforPositionandObjectType(pos,objectType);
			
		}

		
		if(loggedInEmp!=null && supUser!=null )
		{
		   System.out.println("USER ID==="+userId+"SUPER USER ID===="+supUser.getId());
			 if(userId.equals(supUser.getId()))
			 {
				isApprover=true;
				System.out.println("yes Correct");
			 }
			 else
			 {
				isApprover=false;
				System.out.println("yes Wrong");
			 }
		 }
		 else
		 {
			isApprover=false;
			System.out.println("loggedInEmp/supUser is null");
		 }
		 return isApprover;
	}
%>




<%

   	 try{


   	 		EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
		Set applicationList1 = null;
		
       
        applicationList1 = empLeaveServiceImpl.getLeaveApplicationsForEmpID(Id);
        
        LeaveApplication leaveApplication = (LeaveApplication)applicationList1.iterator().next();

		/*
			changes for single step approval
		*/
			// List applicationList = lManager.getLeaveApplicationsAppliedEmpID(new Integer(request.getParameter("Id")));

   	 		LinkedList links = new LinkedList();
		  	request.setAttribute("links",links);
			/*
				change applicationList to applicationList1
			*/
		  	if(applicationList1!= null && !applicationList1.isEmpty())
		  	{
		  	      System.out.println("coming to if loop iterator------"+request.getParameter("action"));
		  		Iterator iter = applicationList1.iterator();
		  		java.util.Date fromDate = null;
		  		java.util.Date toDate = null;
		  		Integer workingDays = new Integer(0);
		  		String reason = "";
		  		TypeOfLeaveMaster typeOfLeaveMstr = null;
		  		String leaveapplicationNo = "";
		  		StatusMaster  statusId = null;
		  		DesignationMaster desigId = null;
		  		Long leaveapplicationId = null;
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		  		while (iter.hasNext())
		  		{
					/*
						Make to Null
					*/
					fromDate = null;
					toDate = null;
		  			workingDays = new Integer(0);




		  			LeaveApplication cataEl = (LeaveApplication)iter.next();

				  if(!cataEl.getStatusId().getName().equals(EisConstants.STATUS_CANCELLED))
				  {
		  			if(request.getParameter("action").equalsIgnoreCase("Modify") && (cataEl.getIsEncashment()!=null && cataEl.getIsEncashment().intValue()!=1))
		  			{
						if((cataEl.getStatusId().getName().equals(EisConstants.STATUS_APPROVED) || cataEl.getStatusId().getName().equals(EisConstants.STATUS_REJECTED)))
						{


							Hashtable map=new Hashtable();


							if(cataEl.getFromDate() != null)
							 fromDate = cataEl.getFromDate();
							if(cataEl.getToDate() != null)
							  toDate = cataEl.getToDate();
							if(cataEl.getWorkingDays() != null)
							 workingDays = cataEl.getWorkingDays();
							typeOfLeaveMstr = cataEl.getTypeOfLeaveMstr();
							leaveapplicationNo = cataEl.getApplicationNumber();
							statusId = cataEl.getStatusId();
							desigId = cataEl.getDesigId();
							leaveapplicationId = cataEl.getId();

							if(fromDate != null)
							map.put("fromDate",sdf.format(fromDate));
							else
								map.put("fromDate","N/A");

							if(toDate != null)
							map.put("toDate",sdf.format(toDate));
							else
								map.put("toDate","N/A");

							if(workingDays != null)
							map.put("workingDays",workingDays.toString());
							else
								map.put("workingDays","0");

							map.put("typeOfLeaveMstr",typeOfLeaveMstr.getName());
							map.put("leaveapplicationNo",leaveapplicationNo);
							map.put("statusId",statusId.getName());
							map.put("desigId",desigId.getDesignationName());
							map.put("leaveapplicationId",leaveapplicationId.toString());
							links.add(map);


					  }
				  }
					else if(request.getParameter("action").equalsIgnoreCase("View"))
					{
						if((cataEl.getStatusId().getName().equals(EisConstants.STATUS_APPLIED) || cataEl.getStatusId().getName().equals(EisConstants.STATUS_APPROVED) || cataEl.getStatusId().getName().equals(EisConstants.STATUS_REJECTED)))
						{


							Hashtable map=new Hashtable();
							if(cataEl.getFromDate() != null)
							fromDate = cataEl.getFromDate();
							if(cataEl.getToDate() != null)
							toDate = cataEl.getToDate();
							if(cataEl.getWorkingDays() != null)

							workingDays = cataEl.getWorkingDays();
							typeOfLeaveMstr = cataEl.getTypeOfLeaveMstr();
							leaveapplicationNo = cataEl.getApplicationNumber();
							statusId = cataEl.getStatusId();


							desigId = cataEl.getDesigId();
							leaveapplicationId = cataEl.getId();
							if(fromDate != null)
							map.put("fromDate",sdf.format(fromDate));
							else
								map.put("fromDate","N/A");
							if(toDate != null)
							map.put("toDate",sdf.format(toDate));
							else
								map.put("toDate","N/A");
							if(workingDays != null)
							    map.put("workingDays",workingDays.toString());
						    else
								map.put("workingDays","0");

							map.put("typeOfLeaveMstr",typeOfLeaveMstr.getName());
							map.put("leaveapplicationNo",leaveapplicationNo);
							map.put("statusId",statusId.getName());
							map.put("desigId",desigId.getDesignationName());
							map.put("leaveapplicationId",leaveapplicationId.toString());
							links.add(map);

						}
					}
					
					else if(request.getParameter("action").equalsIgnoreCase("approve"))
					{
						if((cataEl.getStatusId().getName().equals(EisConstants.STATUS_APPLIED) || cataEl.getStatusId().getName().equals(EisConstants.STATUS_APPROVED) || cataEl.getStatusId().getName().equals(EisConstants.STATUS_REJECTED)))
						{


							Hashtable map=new Hashtable();
							if(cataEl.getFromDate() != null)
							fromDate = cataEl.getFromDate();
							if(cataEl.getToDate() != null)
							toDate = cataEl.getToDate();
							if(cataEl.getWorkingDays() != null)

							workingDays = cataEl.getWorkingDays();
							typeOfLeaveMstr = cataEl.getTypeOfLeaveMstr();
							leaveapplicationNo = cataEl.getApplicationNumber();
							statusId = cataEl.getStatusId();


							desigId = cataEl.getDesigId();
							leaveapplicationId = cataEl.getId();
							if(fromDate != null)
							map.put("fromDate",sdf.format(fromDate));
							else
								map.put("fromDate","N/A");
							if(toDate != null)
							map.put("toDate",sdf.format(toDate));
							else
								map.put("toDate","N/A");
							if(workingDays != null)
							    map.put("workingDays",workingDays.toString());
						    else
								map.put("workingDays","0");

							map.put("typeOfLeaveMstr",typeOfLeaveMstr.getName());
							map.put("leaveapplicationNo",leaveapplicationNo);
							map.put("statusId",statusId.getName());
							map.put("desigId",desigId.getDesignationName());
							map.put("leaveapplicationId",leaveapplicationId.toString());
							links.add(map);

						}
					}
					
					
				  }

		  		}

		  	}
		}
		catch(Exception e){}
  %>



 <display:table name="links" id="eid" cellspacing="0" style="width: 750px;" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >


	 <display:column style="width:5%"   property="fromDate" title="From Date" />
	 <display:column style="width:5%"   property="toDate" title="To Date" />
	 <display:column style="width:5%"   property="workingDays" title="Working Days" />
	 <display:column style="width:5%"   property="typeOfLeaveMstr" title="Leave Type" />
	 <display:column style="width:5%"   property="statusId" title="Status" />
	 <display:column style="width:5%"   property="desigId" title="Designation" />
	 <display:column media="html" style="width:5%">
	 <%
	 
		
		//Add the logic to get superior emp based on positional hierarchy
		ObjectType objectType = null;
		if(master.equals("LeaveApplication"))
		{
			objectType = EisManagersUtill.getCommonsService().getObjectTypeByType(EisConstants.OBJECTTYPE_LEAVE);
		}
		

		boolean bApprover = checkSupEmployee(Id,objectType);
		System.out.println("^^^^^^^^^^^^^^^^bApprover"+bApprover);
		
		
	 	String leaveapplicationId = (String)((Map)pageContext.getAttribute("eid")).get("leaveapplicationId");
	 	String leaveapplicationNo = (String)((Map)pageContext.getAttribute("eid")).get("leaveapplicationNo");
		String link = "";
	 	if(request.getParameter("action").equals("approve"))
	 	{
	 		link =request.getContextPath()+"/leave/BeforeLeaveAction.do?submitType=beforeCreate&master=LeaveApproval&leaveapplicationId="+leaveapplicationId+"&Id="+Id;
	 	}
	 	else if(request.getParameter("action").equalsIgnoreCase("view"))
	 	{
		
            String ess=request.getParameter("ess")!=null?("ess="+request.getParameter("ess")+"&"):"";
	 		link =request.getContextPath()+"/leave/BeforeLeaveAction.do?"+ess+"submitType=setIdForDetailsView&master=LeaveApplication"+"&Id="+Id+"&leaveapplicationId="+leaveapplicationId;

	 	}
	 	else
	 	{
	 		link =request.getContextPath()+"/leave/BeforeLeaveAction.do?submitType=setIdForDetailsModify&master=LeaveApplication"+"&Id="+Id+"&leaveapplicationId="+leaveapplicationId;
	 	}
	 %>

<%
	
if(request.getParameter("action").equalsIgnoreCase("View"))
	 {
		 System.out.println(leaveapplicationNo);
	%>
	 <a href="<%=link%>"><%=request.getParameter("action")%> Details </a>
	 <%}else if(request.getParameter("action").equalsIgnoreCase("Modify")){
	 if(EisManagersUtill.getEmpLeaveService().isSelfApproval())
        {%>

	 <a href="<%=link%>">Cancel Details </a>
	 <%}else{ 
	 
	    if(EisManagersUtill.getEmpLeaveService().isLeaveWfAutoOrManaul())
	    {%>
	    <a href="<%=link%>">Cancel Details </a>
	    <%} else{
	   if(bApprover){%>
	   
	   <a href="<%=link%>">Cancel Details </a>
	   <% }else{%>
	 <font color="blue">Cancel Details </font>
	 <%}}%>
	 <%} %>
	 
	 <%}else if(request.getParameter("action").equalsIgnoreCase("approve")){%>
	 
	  <a href="<%=link%>"><%=request.getParameter("action")%> Details </a>
	  <%}%>
	 </display:column>


</display:table>
</div>
<% if(request.getParameter("ess")==null ){%>
<table style="width: 750px;"  cellpadding ="0" cellspacing ="0" border = "0" >
<tr align = "center"> <td align = "center">
<input class="button" type="button"  value="Search Again" onclick="window.location = '${pageContext.request.contextPath}/pims/BeforeSearchAction.do?module=Employee&masters=LeaveApplication&mode=<%=request.getParameter("action")%>';"/></td>
</td></tr>
</table>
<%} %>



</table>
</html:form>
</center>
</div>
</div>
</td>
</tr>
<!-- Body Section Ends -->
</<table>
</body>
