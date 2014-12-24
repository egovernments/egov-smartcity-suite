
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 
		 		 org.egov.budget.services.*,
		 		 org.apache.log4j.Logger,
		 		 org.egov.pims.*,
		 		 org.egov.pims.utils.*,
		 		 org.egov.pims.empLeave.dao.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 org.egov.pims.dao.*,
		 		 		 org.egov.pims.model.*,
		 		 		 org.egov.pims.service.*,
		 		 org.egov.commons.CFinancialYear,
		 		 org.egov.lib.rjbac.user.User,
				 org.egov.lib.rjbac.dept.ejb.api.*,
				 org.egov.commons.ObjectType,
		 		 org.egov.commons.dao.CommonsDaoFactory,
		 		 org.egov.commons.dao.FinancialYearDAO,
		 		 org.egov.infstr.commons.service.*,
				 org.egov.pims.commons.*,
		 		 org.egov.lib.address.dao.AddressDAO,
		 		 org.egov.lib.address.dao.AddressTypeDAO,
		 		 org.egov.lib.address.model.*,
		 		 org.egov.lib.address.dao.*,
		 		 java.text.SimpleDateFormat,
		 		 org.hibernate.LockMode,
		 		 org.egov.infstr.commons.*,
		 		 org.egov.infstr.commons.client.*,
		 		 org.egov.infstr.commons.dao.*,
		 		 org.egov.infstr.commons.service.*,

		 org.egov.pims.client.*"

%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><bean:message key="PersonalInfoSys"/></title>

    <SCRIPT type="text/javascript" src="../javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="../Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>
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
		System.out.println("userId"+userId);
		if(userId!=null)
			loggedInEmp = EisManagersUtill.getEmployeeService().getEmpForUserId(userId);
		System.out.println("userId"+loggedInEmp);

		Position pos = null;
		pos =EisManagersUtill.getEmployeeService().getPositionforEmp(emp);

		User supUser = null;
		//PersonalInformation supEmp = null;
		//Integer supId = new Integer(0);

		if(pos!=null) {
			supUser = EisManagersUtill.getEisCommonsService().getSupUserforPositionandObjectType(pos,objectType);
			/*if(supUser != null)
				supEmp = EisManagersUtill.getEisManager().getEmpForUserId(supUser.getId());
			if(supEmp!=null)
			{
				supId = supEmp.getIdPersonalInformation();
				System.out.println("supEmp"+supId);
			} */
		}

		if(loggedInEmp!=null && supUser!=null )
		{
			 /* if(supId.equals(loggedInEmp.getIdPersonalInformation()))
			 {
				isApprover=true;
				System.out.println("yes1");
			 }
			 else
			 {
				isApprover=false;
				System.out.println("yes2");
			 } */
			 if(userId.equals(supUser.getId()))
			 {
				isApprover=true;
				System.out.println("yes1");
			 }
			 else
			 {
				isApprover=false;
				System.out.println("yes2");
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

<div align="center">
<table align='center'>
<body>

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
<input type=hidden name="empId" id="empId" value="<%= request.getParameter("Id") %>" />
<div align="center">
<center>
<%

   	 try{


	   	 	System.out.println("rrrrrrrrrrrrr");
			EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
			System.out.println("request.getParameter(Id)="+(String)request.getParameter("Id"));
			List compOffList = empLeaveServiceImpl.getListCompOffObjects(new Integer(request.getParameter("Id")));
   	 		LinkedList links = new LinkedList();
   	 		System.out.println("compOffList.size="+compOffList.size());
		  	request.setAttribute("links",links);
		  	if(compOffList!= null && !compOffList.isEmpty())
		  	{
		  		Iterator iter = compOffList.iterator();
		  		StatusMaster  statusId = null;
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		  		Attendence att = null;
		  		while (iter.hasNext())
		  		{
		  			Hashtable map=new Hashtable();
		  			CompOff cataEl = (CompOff)iter.next();
		  			att = cataEl.getAttObj();
		  			map.put("date",sdf.format(att.getAttDate()));
		  	 		map.put("name",att.getEmployee().getEmployeeName());
		  	 		map.put("code",att.getEmployee().getEmployeeCode());
		  	 		if(cataEl.getStatus()!=null)
									HibernateUtil.getCurrentSession().lock(cataEl.getStatus(),LockMode.NONE);
		  	 		map.put("status",cataEl.getStatus().getName());
		  	 		map.put("compOffId",cataEl.getId());
		  	 		links.add(map);

		  		}

		  	}
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		
		Integer empId = new Integer(request.getParameter("Id"));
		
		//Add the logic to get superior emp based on positional hierarchy
		ObjectType objectType = null;
		if(master.equals("CompOff"))
		{
			objectType = EisManagersUtill.getCommonsService().getObjectTypeByType(EisConstants.OBJECTTYPE_LEAVE);
		}
		System.out.println("^^^^^^^^^^^^^^^^objectType"+objectType);

		boolean bApprover = checkSupEmployee(empId,objectType);


		System.out.println("^^^^^^^^^^^^^^^^bApprover"+bApprover);

  %>



 <display:table name="links" id="eid" cellspacing="0" style="width: 750px;" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >


	 <display:column style="width:5%"   property="date" title="CompOff Date" />
	 <display:column style="width:5%"   property="name" title="Employee name" />
	 <display:column style="width:5%"   property="code" title="Employee code" />
	 <display:column style="width:5%"   property="status" title="Status" />
	 <display:column media="html" style="width:5%">
	 <%
	 	String date = (String)((Map)pageContext.getAttribute("eid")).get("date");
	 	Long idComOff = (Long)((Map)pageContext.getAttribute("eid")).get("compOffId");
	 	String link = "";

	 	if(request.getParameter("action").equals("approve"))
	 	{
	 		link =request.getContextPath()+"/leave/BeforeCompOffAction.do?submitType=CompOffApprove&idComOff="+idComOff;
	 	}

	 %>

	 <%

	if(bApprover)
	 {
	 %>

		<a href="<%=link%>"><%=date%></a>

	 <%
	 }
	 else
	 {
	 %>
		<a ><%=date%></a>

	 <%
	}
	%>
	 </display:column>


</display:table>
</div>
<table style="width: 750px;"  cellpadding ="0" cellspacing ="0" border = "0" >
<tr align = "center">
<td><input class="button" type="button"  value="Search Again" onclick="window.location = '${pageContext.request.contextPath}/pims/BeforeSearchAction.do?module=Employee&masters=CompOff&mode=approve';"/></td>
</tr>
</table>



</table>
</html:form>
</center>
<!-- Body Section Ends -->
</<table>
</body>