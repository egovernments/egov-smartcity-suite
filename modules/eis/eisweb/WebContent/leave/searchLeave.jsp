<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.*,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		 org.egov.pims.service.*,
		 org.egov.infstr.commons.*,
		 org.egov.pims.commons.client.*,
		 org.egov.infstr.commons.dao.*,
		 org.egov.lib.address.dao.AddressDAO,
		 org.egov.lib.rjbac.user.User,
		 org.egov.lib.rjbac.dept.ejb.api.*,
		 org.egov.infstr.utils.*,
		 org.egov.commons.utils.EgovInfrastrUtilInteface,
		 org.egov.commons.utils.EgovInfrastrUtil,
		 org.egov.infstr.utils.*,
		 org.egov.lib.rjbac.dept.ejb.api.*,
		 org.egov.lib.address.dao.AddressTypeDAO,
		 org.egov.lib.address.model.*,
		 org.egov.lib.address.dao.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.empLeave.service.EmpLeaveServiceImpl,
		 org.egov.pims.empLeave.model.LeaveApplication,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Personal Information System</title>


    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>

<script>
 function goindex(arg)
 {

 	if(arg == "Index")
 	{

 		document.forms("desiSearchForm").action = "<%=request.getContextPath()%>/staff/index.jsp";
 		document.forms("desiSearchForm").submit();
 	}


}
function submit(arg)
 {

 	if(arg == "sub")
 	{

 		document.forms("searchForm").action = "<%=request.getContextPath()%>/pims/AfterSearchDisiplinaryAction.do";
 		document.forms("searchForm").submit();
 	}


}

</script>




<div align="center">
<table align='center'>

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

<table align='center' id="table2" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->


<center>
<html:form  action="/pims/AfterSearchDisiplinaryAction.do" >
<%!
	
	EmpLeaveServiceImpl 	empLeaveServiceImpl=new EmpLeaveServiceImpl();

%>

<%

   	 try{

   	 	List leaveAppList = (List)empLeaveServiceImpl.getLeaveApplicationsAppliedEmpID(new Integer(request.getParameter("Id")));

   	 	  	LinkedList links = new LinkedList();
   	 	  	System.out.println("^^^^^^^^^^^^^^^^leaveAppList"+leaveAppList);
		  	request.setAttribute("links",links);

		  	if(leaveAppList!= null && !leaveAppList.isEmpty())
		  	{

		  		Iterator iter = leaveAppList.iterator();
		  		String fromDate = "";

		  		String toDate = "";
		  		String workingDays = "";
		  		String reason = "";
		  		String applicationNumber = "";
		  		String typeOfLeaveMstr = "";
		  		String statusId = "";
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		  		while (iter.hasNext())
		  		{
		  			Hashtable map=new Hashtable();
		  			LeaveApplication cataEl = (LeaveApplication)iter.next();
		  			fromDate = sdf.format(cataEl.getFromDate());
		  	 		toDate = sdf.format(cataEl.getToDate());
		  	 		workingDays = cataEl.getWorkingDays();
		  	 		applicationNumber = cataEl.getApplicationNumber();
		  	 		typeOfLeaveMstr =cataEl.getTypeOfLeaveMstr().getName();
		  	 		statusId = cataEl.getStatusId().getName();
		  	 		map.put("fromDate",fromDate);
					map.put("toDate",toDate);
					map.put("workingDays",workingDays);
					map.put("appNo",applicationNumber);
					map.put("typeOfLeaveMstr",typeOfLeaveMstr);
					map.put("statusId",statusId);
					map.put("LeaveapplicationId",cataEl.getId().toString());
					links.add(map);

		  		}

		  	}
		}
		catch(Exception e){}
  %>

<div  class = "normaltext"> <bean:message key="EmployeeDetails"/>   </div>
<div class="tbl2-container" id="tbl-container" >
 <display:table name="links" id="eid" cellspacing="0" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >



	 <display:column media="html" style="width:5%">
	  <%


	  		String LeaveapplicationId = ((Hashtable)pageContext.getAttribute("eid")).get("LeaveapplicationId").toString();
	  		String link ="";
	  		if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify"))
	  		{

	  			 link=request.getContextPath()+"/pims/BeforeLeaveAction.do?submitType=setIdForDetailsModify"+"&LeaveapplicationId="+LeaveapplicationId+"&master=LeaveApplication";


	  		}

	  		if(((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
	  		{


	  			link =request.getContextPath()+"/pims/BeforeLeaveAction.do?submitType=setIdForDetailsView"+"&LeaveapplicationId="+LeaveapplicationId+"&master=LeaveApplication";
			}


    			String code = ((Hashtable)pageContext.getAttribute("eid")).get("chargeMemoNo").toString();
    			System.out.println("^^^^^^^^^^^^^^^^code"+code);
	%>
	<a href="<%=link%>"><%=appNo%></a>
	</display:column>
	 <display:column style="width:5%"   property="chargeMemoDate" title="Charge Memo Date" />
	 <display:column style="width:5%"   property="code" title="Charge Memo Number" />
	 <display:column style="width:5%"   property="workingDays" title="Nature Of Alligation" />
	 <display:setProperty name="export.pdf" value="false" />
	 <display:setProperty name="paging.banner.placement" value="false" />


</display:table>
</div>





 </html:form>
</table>
</center>
</div>
</div>
</td>
</tr>
<!-- Body Section Ends -->
</<table>
</body>