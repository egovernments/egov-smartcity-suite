
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
		 		 org.egov.pims.utils.*,
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
<%
	           String  currentfinYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
	           CFinancialYear financialY =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(currentfinYear));
	           int f_id =  financialY.getId().intValue();
	           List fYMasterList=EisManagersUtill.getCommonsService().getAllFinancialYearList();
	           Map finMap = getFinMap(fYMasterList);


           %>
<%!
public Map getFinMap(List list)
	{
		Map finMap = new HashMap();
		for(Iterator iter = list.iterator();iter.hasNext();)
		{
			CFinancialYear cFinancialYear = (CFinancialYear)iter.next();
			finMap.put(cFinancialYear.getId(), cFinancialYear.getFinYearRange());
		}
		return finMap;
	}
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Leave Transaction Details</title>

    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>
<%
EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();
		List leaveTranList=null;


%>

<script>





</script>




<div align="center">
<table align='center'>
<body >

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


   	 try{
   	 	leaveTranList= empLeaveServiceImpl.getListOfLeaveTranx(new Integer(request.getParameter("Id")),financialY);
   	 	EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
		Map statusMap = (Map)request.getSession().getAttribute("statusMap");
   	 	LinkedList links = new LinkedList();
   	 	PersonalInformation personalInformation = employeeServiceImpl.getEmloyeeById(new Integer(request.getParameter("Id")));
		request.setAttribute("links",links);

		if(leaveTranList!= null && !leaveTranList.isEmpty())
		{

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
				map.put("san",san==null?"":san);//sanction_no might be null for the leaves which has less than approved status
				map.put("total",total.toString());
				map.put("desname",desname);
				if(fromDate!=null)
				{
					map.put("fromDate",sdf.format(fromDate));
				}
				else
				{
					map.put("fromDate","------");
				}
				if(toDate!=null)
				{
				map.put("toDate",sdf.format(toDate));
				}
				else
				{
					map.put("toDate","------");
				}
				links.add(map);


			}

		}

	}
	catch(Exception e){e.printStackTrace();}//to know especially runtime exceptions
  %>



 <display:table name="links" id="eid"  uid = "TPRow" cellspacing="0" style="width: 100%;" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >
	<display:setProperty name="paging.banner.placement" value="bottom"/>
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