
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*,
		         org.egov.infstr.utils.*,
		 		 org.apache.log4j.Logger,
		 		 org.egov.pims.*,
		 		 org.egov.pims.empLeave.dao.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 org.egov.pims.dao.*,
 		 		 org.egov.pims.model.*,
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
		 		 org.egov.exceptions.EGOVRuntimeException,
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

		document.forms("employeeSearchForm").action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms("employeeSearchForm").submit();
	}


}



</script>




<div align="center">
<table align='center'>
<body onload = "setLength()">
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

<DIV id=main>
      <DIV id=m2>
      <DIV id=m3 style="width: 810px; ">

<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/pims/AfterSearchAction.do?submitType=executeSearch" >
<div align="center">
<center>
<%

   	 try{
   	 	
   	    EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
    	List leaveTranList= empLeaveServiceImpl.getMapOfLeaveTranx(new Integer(request.getParameter("employeeId")));
   	    EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl(); 
		Map statusMap = (Map)request.getSession().getAttribute("statusMap");
   	 	LinkedList links = new LinkedList();
   	 	PersonalInformation personalInformation = employeeServiceImpl.getEmloyeeById(new Integer(request.getParameter("employeeId")));
		request.setAttribute("links",links);

		if(leaveTranList!= null && !leaveTranList.isEmpty())
		{
			System.out.println("^^^^^^^^^^^^^^^^leaveTranList"+leaveTranList);
			Iterator iter = leaveTranList.iterator();
			String san = "";
			String type = "";
			String total = "";
			String desname = "";
			Date fromDate =null;
			Date toDate =null;
			String ID = "";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			while (iter.hasNext())
			{
				Hashtable map=new Hashtable();
				ViewLeaveTxns cataEl = (ViewLeaveTxns)iter.next();
				fromDate = cataEl.getFrom();
				toDate = cataEl.getTo();
				san = cataEl.getSan();
				desname = cataEl.getDesname();
				total = cataEl.getTotal().toString();

				map.put("name",cataEl.getAppNo());
				map.put("payEligible",cataEl.getPayElegible().toString());
				map.put("type",cataEl.getType());
				map.put("status",(String)statusMap.get(cataEl.getStatusId()));
				map.put("san",san);
				map.put("total",total.toString());
				map.put("desname",desname);
				map.put("fromDate",sdf.format(fromDate));
				map.put("toDate",sdf.format(toDate));
				links.add(map);


			}

		}
	}
	catch(Exception e){throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);}
  %>



 <display:table name="links" id="eid" cellspacing="0" style="width: 750px;" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >

	 <display:column style="width:5%"   property="name" title="Application Number" />
	 <display:column style="width:5%"   property="san" title="Sanction No" />
	 <display:column style="width:5%"   property="type" title="Leave Type" />
	 <display:column style="width:5%"   property="desname" title="Employee Designation" />
	 <display:column style="width:5%"   property="status" title="Status" />
	 <display:column style="width:5%"   property="fromDate" title="From Date" />
	 <display:column style="width:5%"   property="toDate" title="To Date" />
	 <display:column style="width:5%"   property="total" title="Working Days" />
	 <display:column style="width:5%"   property="payEligible" title="Pay Eligible" />

</display:table>
</div>
<table style="width: 750px;"  cellpadding ="0" cellspacing ="0" border = "0" >
<tr align = "center">
<td><html:button styleClass="button" value="Search" property="b4" onclick="goindex('Index')" /></td>
</tr>
</table>



</table>
</html:form>
</center>
</div>
</div></div></div>
</td>
</tr>
<!-- Body Section Ends -->
</<table>
</body>