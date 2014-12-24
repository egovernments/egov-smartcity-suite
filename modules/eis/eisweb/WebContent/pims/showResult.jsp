
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.apache.log4j.Logger,
		 java.sql.Date,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		 org.egov.pims.service.*,
		 org.egov.infstr.commons.*,
		 org.egov.pims.commons.client.*,
		 org.egov.infstr.commons.dao.*,
		 org.egov.lib.address.dao.AddressDAO,
		 org.egov.lib.rjbac.user.User,
		 org.egov.lib.rjbac.user.ejb.api.UserService,
		 org.egov.infstr.utils.*,
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

    <SCRIPT type="text/javascript" src="../javascript/PinSysvalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="../Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>


<script>

function goindex(arg)
{

	if(arg == "Index")
	{

		document.forms("searchForm").action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms("searchForm").submit();
	}


}



</script>




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
<html:form  action="/pims/AfterSearchAction.do?submitType=executeSearch" >
<div align="center">
<center>
<%

   	 try{
   	 	List employeeList = (List)request.getAttribute("employeeListReport");
   	 	  	LinkedList links = new LinkedList();
		  	request.setAttribute("links",links);
			System.out.println("employeeList"+employeeList);
		  	if(employeeList!= null && !employeeList.isEmpty())
		  	{
		  		Iterator iter = employeeList.iterator();
		  		String code = "";
		  		String name = "";
		  		String dept = "";
		  		String desig = "";
		  		String fromDate ="";
		  		String toDate ="";
		  		String ID = "";
		  		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		  		while (iter.hasNext())
		  		{
		  			Hashtable map=new Hashtable();

					SearchEmpDTO cataEl = (SearchEmpDTO)iter.next();

					fromDate = sdf.format(cataEl.getFromDate());
					System.out.println("fromDate"+fromDate);
					if(cataEl.getToDate()!=null)
					{
						toDate = sdf.format(cataEl.getToDate());

					}
					else
					{
					toDate = "";
					}

					System.out.println("toDate"+toDate);
					name = cataEl.getName();
					System.out.println("name"+name);
					code = cataEl.getCode();
					System.out.println("code"+code);
					dept = cataEl.getDepartment().toString();
					System.out.println("dept"+dept);
					desig = cataEl.getDesignation().toString();
					System.out.println("desig"+desig);
					ID = cataEl.getID().toString();
					map.put("name",name);
					map.put("code",code);
					map.put("dept",dept);
					map.put("desig",desig);
					map.put("fromDate",fromDate);
					map.put("toDate",toDate);
					map.put("Id",ID);
					links.add(map);

		  		}

		  	}
		}
		catch(Exception e){}
  %>



 <display:table name="links" id="eid" cellspacing="0" style="width: 100%;" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >
	<display:setProperty name="paging.banner.placement" value="bottom"/>
	 <display:column style="width:5%"   property="code" title="Employee Code" />
	 <display:column style="width:5%"   property="name" title="Employee Name" />
	 <display:column style="width:5%"   property="desig" title="Employee Designation" />
	 <display:column style="width:5%"   property="dept" title="Employee Department" />
	 <display:column style="width:5%"   property="fromDate" title="From Date" />
	 <display:column style="width:5%"   property="toDate" title="To Date" />

</display:table>
</div>



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