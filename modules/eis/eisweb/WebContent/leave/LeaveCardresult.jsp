
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		
		 		 org.egov.budget.services.*,
		 		 org.apache.log4j.Logger,
		 		 org.egov.pims.*,
		 		 org.egov.pims.empLeave.dao.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 org.egov.pims.dao.*,
		 		 org.egov.pims.utils.*,
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
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Leave Card Details</title>

    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="${pageContext.request.contextPath}/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>


<script>

function goindex(arg)
{

	if(arg == "Index")
	{

		window.location ="${pageContext.request.contextPath}/staff/index.jsp";
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



<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/pims/AfterSearchAction.do?submitType=executeSearch" >
<div align="center">
<center>
<%
	           String  currentfinYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
	           CFinancialYear financialY =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(currentfinYear));
	           int f_id =  financialY.getId().intValue();



           %>
<%

   	 try{
   	 	
		EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
		List leaveCardList= empLeaveServiceImpl.getListOfLeaveCard(new Integer(request.getParameter("Id")),financialY);
		EmployeeServiceImpl employeeServiceImpl= new EmployeeServiceImpl();
   	 	LinkedList links = new LinkedList();
		request.setAttribute("links",links);
		Map statusMap = (Map)request.getSession().getAttribute("statusMap");
		PersonalInformation personalInformation = employeeServiceImpl.getEmloyeeById(new Integer(request.getParameter("Id")));
		  	if(leaveCardList!= null && !leaveCardList.isEmpty())
		  	{
		  		Iterator iter = leaveCardList.iterator();
		  		String san = "";
		  		String type = "";
		  		String balPreFy = "";
		  		String max = "";
		  		Date fromDate =null;
				Date toDate =null;
				String ear =null;
		  		String availed =null;
		  		String bal = "";
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		  		while (iter.hasNext())
		  		{
		  			Hashtable map=new Hashtable();
		  			LeaveCard cataEl = (LeaveCard)iter.next();
		  			ear = new Integer(Math.round(cataEl.getEar().floatValue())).toString();
		  			availed = new Integer(Math.round(cataEl.getAvailed().floatValue())).toString();
		  	 		san = cataEl.getSan();
		  	 		fromDate = cataEl.getFrom();
					toDate = cataEl.getTo();
					max = new Integer(Math.round(cataEl.getMax().floatValue())).toString();
		  	 		balPreFy = new Integer(Math.round(cataEl.getBalPreFy().floatValue())).toString();
		  	 		map.put("name",cataEl.getAppNo());
		  	 		map.put("type",cataEl.getType());
		  	 		map.put("san",san==null?"":san);
					map.put("balPreFy",balPreFy);
					map.put("max",max);
					map.put("ear",ear);
					map.put("availed",availed);
					if(fromDate!=null)
					{
					map.put("fromDate",sdf.format(fromDate));
					}
					else
					{
						map.put("fromDate","------");
					}
					map.put("status",(String)statusMap.get(cataEl.getStatusId()));
					if(toDate!=null)
					{
						map.put("toDate",sdf.format(toDate));
					}
					else
					{
						map.put("toDate","------");
					}
					map.put("bal",new Integer(Math.round(cataEl.getBal().floatValue())).toString());
					links.add(map);


		  		}

		  	}
		}
		catch(Exception e){}
  %>



 <display:table name="links" id="eid" cellspacing="0" style="width: 100%;" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >
	<display:setProperty name="paging.banner.placement" value="bottom"/>
	 <display:column style="width:5%"   property="name" title="Application Number" />
	 <display:column style="width:5%"   property="san" title="Sanction No" />
	 <display:column style="width:5%"   property="status" title="Status" />
	 <display:column style="width:5%"   property="fromDate" title="From Date" />
	 <display:column style="width:5%"   property="toDate" title="To Date" />
	 <display:column style="width:5%"   property="type" title="Leave Type" />
	 <display:column style="width:5%"   property="balPreFy" title="Balance From Previous Year B" />
	 <display:column style="width:5%"   property="ear" title="Leaves Earned This Year E" />
	 <display:column style="width:5%"   property="max" title="Maximum Leaves Available B+E" />
	 <display:column style="width:5%"   property="availed" title="Leaves Availed" />
	 <display:column style="width:5%"   property="bal" title="Available Balance" />


</display:table>
</div>
<table style="width: 100%;"  cellpadding ="0" cellspacing ="0" border = "0" >
<tr><td>&nbsp;</td></tr>
<tr align = "center">
<td><input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/>
</td>
</tr>
</table>



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