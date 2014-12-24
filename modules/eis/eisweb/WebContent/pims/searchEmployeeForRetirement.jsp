
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		
		 org.egov.budget.services.*,
		 org.apache.log4j.Logger,
		 org.egov.commons.CFinancialYear,
		 org.egov.pims.empLeave.dao.*,
		 org.egov.pims.empLeave.client.*,
		 org.egov.pims.empLeave.model.*,
 		org.egov.pims.empLeave.service.*,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		 org.egov.pims.service.*,
		 org.egov.pims.utils.*,
		 org.egov.infstr.utils.ServiceLocator,
		 org.egov.infstr.utils.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Personal Information System</title>


    <SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
     <script language="text/JavaScript" src="../Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

</head>


<script>
function execute()
{
	var target="<%=(request.getAttribute("alertMessage"))%>";
	 if(target!="null")
	 {
		alert("<%=request.getAttribute("alertMessage")%>");
		<%	request.setAttribute("alertMessage",null);	%>
	 }

}

function validateSearch()
{
	var bool ;
	var http = initiateRequest();
	var url = "${pageContext.request.contextPath}/leave/validateSearchAjax.jsp?month="+document.getElementById("monthId").value+"&fYear="+document.getElementById("finYear").value;
	http.open("GET", url, false);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			if (http.status == 200)
			{
			       var statusString =http.responseText.split("^");
			       if(statusString[0]=="false")
			       {
					bool = "false";
			       }
			       else
			       {
					bool = "true";

			       }
			}
		}
	};
	http.send(null);
	return bool;


}

function submitSearch()
 {
 	var boole = validateSearch();
 	if(boole=="true")
 	{
 		document.forms("searchForm").action = "${pageContext.request.contextPath}/pims/AfterSearchAction.do?submitType=executeSearchForRetirement";
 		document.forms("searchForm").submit();
 		return true;
 	}
 	else if(boole=="false")
 	{
 		alert('Cannot alter or view attendence of future dates');
 		return false;
 	}

}



</script>
	<body onload ="execute()" >
<!-- Header Section Begins -->

<!-- Header Section Ends -->

<table align='center' id="table2">
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->


<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/pims/AfterSearchAction.do?submitType=executeSearchForRetirement"  >
<table  style="width: 930;" cellpadding ="0" cellspacing ="0" border = "1"  >
<tbody>
<tr>
  <td colspan="8" height=20 bgcolor=#dddddd align=middle  class="tableheader">
<p>Search Employee</p></td>
  </tr>

<tr>
<td   class="labelcell" >Designation</td>
<td class="fieldcell" >
<select  name="designationId"   id="designationId" style = "width:200px">
 <option value='0' selected="selected">choose</option>
	<%

		Map mapOfDesignation =(Map)session.getAttribute("mapOfDesignation");
		for (Iterator it = mapOfDesignation.entrySet().iterator(); it.hasNext(); )
		{
				Map.Entry entry = (Map.Entry) it.next();

		%>
		<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (request.getParameter("designationId")==null?0:Integer.parseInt(request.getParameter("designationId").trim())))? "selected":"")%>><%= entry.getValue() %></option>

		<%
		}

		%>
	</select>
</td>
				<td   class="labelcell" >Department</td>
				<td class="fieldcell" >
				<select  name="departmentId"   id="departmentId" style = "width:200px">
				 <option value='0' selected="selected">choose</option>
					<%

						Map deptmap =(Map)session.getAttribute("deptmap");
						for (Iterator it = deptmap.entrySet().iterator(); it.hasNext(); )
						{
								Map.Entry entry = (Map.Entry) it.next();

						%>
						<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == (request.getParameter("departmentId")==null?0:Integer.parseInt(request.getParameter("departmentId").trim())))? "selected":"")%>><%= entry.getValue() %></option>

						<%
						}

						%>
				</select>
				</td>
</tr>

<tr>
    <td  class="labelcell" width="149" align = "center">Employee Code</td>
    <td   class="fieldcell" width="305" >
<input type="text"id="code" name="code" size="10" align = "center" value = "<%=(request.getParameter("code")==null?"":request.getParameter("code"))%>" >
</td>
 <td  class="labelcell" width="149" align = "center">Employee Name</td>
      <td   class="fieldcell" width="305" >
  <input type="text"id="name" name="name" size="10" align = "center"  value = "<%=(request.getParameter("name")==null?"":request.getParameter("name"))%>">
  </td>
  </tr>
  <tr>
        <td  class="labelcell" width="149" align = "center">Search All(active and inactive)

    <td  colspan="3" class="labelcellforsingletd" width="305"> Yes
    <input type="radio" value="true" name="searchAll" id="searchAll"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No<input type="radio" value="false" checked name="searchAll" id="searchAll">
  </td>
    </td>

  </tr>
  <tr id = "modifyAttendence">
  <%
          String  currentfinYear = EisManagersUtill.getCommonsService().getCurrYearFiscalId();
          CFinancialYear financialY =EisManagersUtill.getCommonsService().findFinancialYearById(new Long(currentfinYear));
          int currentmonth=Calendar.getInstance().get(Calendar.MONTH) + 1;
          String mon  = (String)EisManagersUtill.getMonthsStrVsDaysMap().get(new Integer(currentmonth));
          int monReq = currentmonth;
          String s= "sdfbgsd sdfg (+) ";
          System.out.println("dddddddddddddddddd"+s.lastIndexOf("(+)"));
          String monthStr = request.getParameter("monthId");
          if(monthStr!=null)
          	monReq = Integer.parseInt(monthStr.trim());
	  int fYearReq = Integer.parseInt(currentfinYear.trim());
	  String fYearStr = request.getParameter("finYear");
	  if(fYearStr!=null)
			fYearReq = Integer.parseInt(fYearStr.trim());

          %>

 <td  class="labelcell" width="149" align = "center">Month</td>
 <td class="fieldcell" >
   				<select  name="monthId"   id="monthId" style = "width:200px">
   				<option value="<%= new Integer(currentmonth).toString() %>" selected="selected"><%= mon %></option>

					<%

						Map mMap =EisManagersUtill.getMonthsStrVsDaysMap();
						TreeSet set = new TreeSet(mMap.keySet());
						for (Iterator it = set.iterator(); it.hasNext(); )
						{
								Integer id = (Integer) it.next();
					%>
						<option  value = "<%= id.toString() %>"<%=((((Integer)id).intValue() == monReq)? "selected":"")%>><%= (String)mMap.get(id) %></option>

						<%
						}

						%>
  				</select>
				</td>

  </td> <td  class="labelcell" width="149" align = "center">Finantial Year</td>
  <td class="fieldcell" >
  				<select  name="finYear"   id="finYear" style = "width:200px">
  				 <option value="<%= currentfinYear %>" selected="selected"><%= financialY.getFinYearRange() %></option>
  					<%

  						Map finMap =(Map)session.getAttribute("finMap");
  						for (Iterator it = finMap.entrySet().iterator(); it.hasNext(); )
  						{
  								Map.Entry entry = (Map.Entry) it.next();

  						%>
  						<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Long(fYearReq).intValue())? "selected":"")%>><%= entry.getValue() %></option>

  						<%
  						}

  						%>
  				</select>
				</td>

  </td>

  </tr>
</tbody>
</table>

<table id = "submit" style="width: 930;" cellpadding ="0" cellspacing ="0" border = "1" >
<tr >
  		<td><html:button styleClass="button" value="Submit" property="b4" onclick="return submitSearch();" /></td>
<tr>
</table>
<%

   	 try{
   	 	List employeeList = (List)request.getAttribute("employeeList");
   	 	System.out.println("^^^^^^^^^^^^^^^^employeeList"+employeeList);
   	 	System.out.println("^^^^^^^^^^^^^^^^employeeList"+employeeList);
		LinkedList links = new LinkedList();
		request.setAttribute("links",links);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(employeeList!= null && !employeeList.isEmpty())
		{
			System.out.println("^^^^^^^^^^^^^^^^employeeList"+employeeList);
			Iterator iter = employeeList.iterator();
			while(iter.hasNext())
			{
			Hashtable map=new Hashtable();
				EmployeeView cataEl = (EmployeeView)iter.next();
				String code = "";
				String name = "";
				String dept = "";
				String desig = "";
				String ID = "";
				name = cataEl.getEmployeeName();
				code = cataEl.getEmployeeCode();
				dept = cataEl.getDeptId().getDeptName();
				desig = cataEl.getDesigId().getDesignationName();
				ID = cataEl.getId().toString();
				map.put("name",name);
				map.put("code",code);
				map.put("dept",dept);
				map.put("desig",desig);
				map.put("Id",ID);
				links.add(map);
			}

		}


	    }
	catch(Exception e){}
  %>

 <display:table name="links" id="eid" cellspacing="0" style="width: 750;" export="false" defaultsort="2" pagesize = "15" sort="list"  class="its"  >

	 <display:column style="width:5%"   property="code" title="Employee Code" />
	 <display:column style="width:5%"   property="name" title="Employee Name" />
	 <display:column style="width:5%"   property="desig" title="Employee Designation" />
	 <display:column style="width:5%"   property="dept" title="Employee Department" />
</display:table>

</div>

</table>
</center>
</html:form>

</body>