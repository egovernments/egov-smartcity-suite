
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.*,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		 org.egov.pims.service.*,
		 org.egov.pims.utils.*,
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
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Personal Information System</title>




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

<div align="center">
<center>
<html:form  action="/pims/BeforeSearchAction.do?module=Employee" >
<%!
	EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
%>

<%

   	 try{

   	 	List disiplinaryList = (List)employeeServiceImpl.getDisiplinaryApplicationsAppliedEmpID(new Integer(request.getParameter("Id")));



   	 	  	LinkedList links = new LinkedList();
   	 	  	System.out.println("^^^^^^^^^^^^^^^^disiplinaryList"+disiplinaryList);
		  	request.setAttribute("links",links);

		  	if(disiplinaryList!= null && !disiplinaryList.isEmpty())
		  	{

		  		Iterator iter = disiplinaryList.iterator();
		  		String chargeMemoDate = "";

		  		String chargeMemoNo = "";
		  		String natureOfAlligation = "";
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		  		while (iter.hasNext())
		  		{

		  			DisciplinaryPunishment cataEl = (DisciplinaryPunishment)iter.next();
		  			if(request.getParameter("action").equals("modify") && (cataEl.getStatusId().getName().equals(EisConstants.STATUS_APPLIED) || cataEl.getStatusId().getName().equals(EisConstants.STATUS_REJECTED)))
		  			{
						Hashtable map=new Hashtable();

						chargeMemoDate = sdf.format(cataEl.getChargeMemoDate());
						chargeMemoNo = cataEl.getChargeMemoNo();
						natureOfAlligation = cataEl.getNatureOfAlligations();
						map.put("chargeMemoDate",chargeMemoDate);
						map.put("chargeMemoNo",chargeMemoNo);
						map.put("natureOfAlligation",natureOfAlligation);
						map.put("disiplinaryId",cataEl.getDisciplinaryPunishmentId().toString());
						map.put("leaveapplicationNo",cataEl.getApplicationNumber());
						links.add(map);
					}
					else
					{
						Hashtable map=new Hashtable();
						chargeMemoDate = sdf.format(cataEl.getChargeMemoDate());
						chargeMemoNo = cataEl.getChargeMemoNo();
						natureOfAlligation = cataEl.getNatureOfAlligations();
						map.put("chargeMemoDate",chargeMemoDate);
						map.put("chargeMemoNo",chargeMemoNo);
						map.put("natureOfAlligation",natureOfAlligation);
						map.put("disiplinaryId",cataEl.getDisciplinaryPunishmentId().toString());
						map.put("leaveapplicationNo",cataEl.getApplicationNumber());
						links.add(map);

					}

		  		}

		  	}
		}
		catch(Exception e){}
  %>

<div  class = "normaltext"> Employee Details   </div>
<div class="tbl2-container" id="tbl-container" >
 <display:table name="links" id="eid" cellspacing="0" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its" >
<display:column media="html" style="width:5%">
<%
	String disiplinaryId = (String)((Map)pageContext.getAttribute("eid")).get("disiplinaryId");

	String leaveapplicationNo = (String)((Map)pageContext.getAttribute("eid")).get("leaveapplicationNo");
	String link = "";
	if(request.getParameter("action").equals("approve"))
	{
		link=request.getContextPath()+"/pims/BeforePIMSMasterAction.do?submitType=beforeCreate&master=DisciplinaryApproval&disiplinaryId="+disiplinaryId;
	}
	else if(request.getParameter("action").equalsIgnoreCase("view"))
	{


		link =request.getContextPath()+"/pims/BeforePIMSMasterAction.do?submitType=setIdForDetailsView&master=Disciplinary"+"&disiplinaryId="+disiplinaryId;

	}
	else
	{
		link =request.getContextPath()+"/pims/BeforePIMSMasterAction.do?submitType=setIdForDetailsModify&master=Disciplinary"+"&disiplinaryId="+disiplinaryId;
	}
%>

<a href="<%=link%>"><%=leaveapplicationNo%></a>
</display:column>



	 <display:column style="width:5%"   property="chargeMemoDate" title="Charge Memo Date" />
	 <display:column style="width:5%"   property="chargeMemoNo" title="Charge Memo Number" />
	 <display:column style="width:5%"   property="natureOfAlligation" title="Nature Of Alligation" />
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