
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 
		 org.egov.budget.services.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.*,
		 org.hibernate.LockMode,
		 org.egov.pims.commons.*,
		 org.egov.pims.dao.*,
		 org.egov.pims.model.*,
		 org.egov.pims.service.*,
		 org.egov.pims.commons.dao.*,
		 org.egov.pims.utils.*,
		 org.egov.infstr.commons.*,
		 org.egov.pims.commons.*,
		 org.egov.pims.commons.client.*,
		 org.egov.infstr.commons.dao.*,
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
    <title><bean:message key="PersonalInfoSys"/></title>

        <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
        <SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
        <script language="text/JavaScript" src="<%=request.getContextPath()%>/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>


</head>
<%!
public Set getIntegerObj(Set set)
{
	Set  newset = new HashSet();
	for(Iterator iter = set.iterator();iter.hasNext();)
	{
	EmployeeDepartment employeeDepartment = (EmployeeDepartment)iter.next();
	newset.add(employeeDepartment.getDept().getId());
	}
	return newset;
}
%>
<%!

String id ="";
EmployeeNamePoJo employeeNamePoJo = null;




%>

<%
Map deptMap = (Map)session.getAttribute("deptmap");
	session.setAttribute("deptMap",deptMap);

employeeNamePoJo =(EmployeeNamePoJo)session.getAttribute("employeeName");
Set assPrdSet =null;
EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();
PersonalInformation egpimsPersonalInformation =null;
AssignmentPrd assignmentPrd = null;
if(request.getParameter("Id")!=null)
{
	id =request.getParameter("Id").trim();
}
 else
 {
 	PersonalInformation personalInformation = (PersonalInformation)session.getAttribute("personalInformation");
 	id =personalInformation.getIdPersonalInformation().toString();

 }
 egpimsPersonalInformation = employeeServiceImpl.getEmloyeeById(new Integer(id));
if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
{
	if( id!=null&&!id.equals(""))
	{

		assPrdSet= new HashSet();
		String prd = request.getParameter("prdId");
		assignmentPrd= employeeServiceImpl.getAssignmentPrdById(new Integer(prd));
		if(assignmentPrd!=null)
			assPrdSet.add(assignmentPrd);


	}
}
else if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("create"))
{
Assignment assignment = new Assignment();
Set assSet = new HashSet();
assSet.add(assignment);
assignmentPrd = new AssignmentPrd();
assignmentPrd.addAssignment(assignment);
assPrdSet= new HashSet();
assPrdSet.add(assignmentPrd);
}
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
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
var emplyeeId= "<%=id%>"
function goindex(arg)
{

	if(arg == "Index")
	{

		document.forms("pIMSForm").action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.forms("pIMSForm").submit();
	}


}

function populateList()
{
	var http = initiateRequest();
	var url = "${pageContext.request.contextPath}/leave/populateSession.jsp";
	http.open("GET", url, true);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			if (http.status == 200)
			{
			       var statusString =http.responseText.split("^");


			 }
		}
	};
	http.send(null);



}
function changePosTo(obj)
{
	var row=getRow(obj);
	var reports = getControlInBranch(row,"posId");
	var len = reports.length;

	for(var i=0;i<=len;i++)
	{
		reports.options[i] = null;
	}
	if(obj.options[obj.selectedIndex].text == "choose")
	{

		reports.options[0] = new Option("choose",0,true,true);
	}

	<%
	Map desigtmap =(Map)session.getAttribute("mapOfDesignation");
	System.out.println("desigtmap"+desigtmap);
	Set desigSet =new HashSet();
	for (Iterator it = desigtmap.entrySet().iterator(); it.hasNext(); )
	{
		Map.Entry entry = (Map.Entry) it.next();
		desigSet.add(entry.getKey());

	}
	DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
	if(desigSet != null && !desigSet.isEmpty())
	{	EmployeeNamePoJo employeeNam  =null;
		for(Iterator itr=desigSet.iterator(); itr.hasNext();)
		{
			Object obj = itr.next();
			Integer desigId = (Integer)obj;
			String desigVal = ((String)desigtmap.get(desigId)).trim();
			String valuestr = desigVal;
		%>

			if(obj.options[obj.selectedIndex].text == '<%=valuestr%>')
			{

		<%

		DesignationMaster designationMaster = designationMasterDAO.getDesignationMaster(desigId.intValue());

		Set emplist = new HashSet();
		if(designationMaster!=null)
			emplist = EisManagersUtill.getGenericCommonsService().getSetOfPosForDesignationId(designationMaster.getDesignationId());
				int i =1;
				if(emplist!=null && !emplist.isEmpty())
				{
					Iterator listiter = emplist.iterator();
					while (listiter.hasNext())
					{
						Position 	position = (Position)listiter.next();
						String x = position.getName();

		%>
							reports.options["+<%=i%>+"] = new Option("<%=x%>","<%=position.getId()%>",true,true);
		<%
							i++;
					}
				}
		%>
			reports.options[0] = new Option("choose",0,true,true)
			}
		<%
		}
	}

	%>
}



function resetRowValues(lastRow,name)
{


if(name=="AddRow")
{

	if(document.pIMSForm.toDate[lastRow-1].value!="")
	{
		var dte = document.pIMSForm.toDate[lastRow-1].value;
		var day = eval(dte.substr(0,2))+1;
		var month = dte.substr(3,2);
		var year = dte.substr(6,4);
		document.pIMSForm.fromDate[lastRow].value = day+"/"+month+"/"+year;
		document.pIMSForm.toDate[lastRow].value="";
		document.pIMSForm.fundId[lastRow].value="0";
		document.pIMSForm.functionId[lastRow].value="0";
		document.pIMSForm.designationId[lastRow].value="0";
		document.pIMSForm.posId[lastRow].value="0";
		document.pIMSForm.functionaryId[lastRow].value="0";

		document.pIMSForm.departmentId[lastRow].value="0";

	}
	else if(document.pIMSForm.toDate[lastRow-1].value=="" && document.pIMSForm.fromDate[lastRow-1].value !="")
	{
	    alert('<bean:message key="alertFillToDateBfAdd"/>');
	    var tbl= document.getElementById("EOTable");
	    tbl.deleteRow(lastRow);
	    return  false;
	}




}

}
function addRow(obj,tableName,row)
{


    	var tbl=tableName;
    	var rowO=tbl.rows.length;
    	var name=obj.value;
        var tname = "resetRowValues"+name;
        if(rowO<11)
        {
        	if(row != null)
        	{
        		var tbody=tbl.tBodies[0];
    			var lastRow = tbl.rows.length;
    			rIndex = 0;
    			var rowObj = row.cloneNode(true);
    			rowObj.document.getElementById('assignmentId').value = 0;
    			rIndex = rowObj.rowIndex;
    			tbody.appendChild(rowObj);
    			resetRowValues(lastRow-1,name);
		}


    }
}
var firstLength = 0;
var rIndex = 0;

  function setLength()
  {
 	 var tbl=document.getElementById('EOTable');
    	var rowo=tbl.rows.length;
  	firstLength = rowo;


  }
  function deleteRow(obj,tableName,addButtion)
  {
      var tbl=tableName;

      var rowo=tbl.rows.length;
      if(rowo<=firstLength)
  	{
  		alert('<bean:message key="alertCannotDelete"/>');
  		return false;
  	}
  	else
	  	{

	  		tbl.deleteRow(rIndex);
	  		return true;
  	}

  }
function validateNotEmpty( strValue )
{
   var strTemp = strValue;
   strTemp = trimAll(strTemp);
   if(strTemp.length > 0)
   {
      return true;
   }
   return false;
 }

function validateName( strValue)
{
var iChars = "!@#$%^&*()+=-[]\\\';,/{}|\":<>?0123456789";

for (var i = 0; i < strValue.value.length; i++)
{
	if (iChars.indexOf(strValue.value.charAt(i)) != -1)
	{
		alert('<bean:message key="alertValNme"/>');
		strValue.focus();
		return false;
	}
}

}



function ButtonPressNew(arg)
{


	if(arg == "savenew")
	{

		var tbl= document.getElementById("EOTable");
			var rows= tbl.rows.length;

			if(rows>2)
			{
				for(var i=0 ;i<rows-1;i++)
				{
					if(document.pIMSForm.fromDate[i].value=="" )
					{
						alert('<bean:message key="alertEnterFromDate"/>');
						document.pIMSForm.fromDate[i].focus();
						return false;
					}
					if(document.pIMSForm.fundId[i].value=="" )
					{
						alert('<bean:message key="alertChooseFund"/>');
						document.pIMSForm.fundId[i].focus();
						return false;
					}
					if(document.pIMSForm.designationId[i].value=="0" )
					{
						alert('<bean:message key="alertChooseDesg"/>');
						document.pIMSForm.designationId[i].focus();
						return false;
					}
					if(document.pIMSForm.posId[i].value=="0" )
					{
						alert('<bean:message key="alertChooseposition"/>');
						document.pIMSForm.posId[i].focus();
						return false;
					}

					if(document.pIMSForm.departmentId[i].value=="0" )
					{
						alert('<bean:message key="alertChoosedept"/>');
						document.pIMSForm.departmentId[i].focus();
						return false;
					}
					if(document.pIMSForm.toDate[i].value!="" )
					{
						if(compareDate(document.pIMSForm.toDate[i].value,document.pIMSForm.fromDate[i].value) == 1||compareDate(document.pIMSForm.toDate[i].value,document.pIMSForm.fromDate[i].value) == 0)
						{
							alert('<bean:message key="alertTodateLTFromDate"/>');
							document.pIMSForm.toDate[i].focus();
							document.pIMSForm.toDate[i].value="";
							return false;
						}
					}

				}
			}
			else
			{
				if(document.pIMSForm.fromDate.value=="" )
				{
					alert('<bean:message key="alertEnterFromDate"/>');
					document.pIMSForm.fromDate.focus();
					return false;
				}
				if(document.pIMSForm.fundId.value=="0" )
				{
					alert('<bean:message key="alertChooseFund"/>');
					document.pIMSForm.fundId.focus();
					return false;
				}

				if(document.pIMSForm.designationId.value=="0" )
				{
					alert('<bean:message key="alertChooseDesg"/>');
					document.pIMSForm.designationId.focus();
					return false;
				}
				if(document.pIMSForm.posId.value=="0" )
				{
					alert('<bean:message key="alertChooseposition"/>');
					document.pIMSForm.posId.focus();
					return false;
				}
				if(document.pIMSForm.departmentId.value=="0" )
				{
					alert('<bean:message key="alertChoosedept"/>');
					document.pIMSForm.departmentId.focus();
					return false;
				}
					if(document.pIMSForm.toDate.value!="" )
										{

		if(compareDate(document.pIMSForm.toDate.value,document.pIMSForm.fromDate.value) == 1||compareDate(document.pIMSForm.toDate.value,document.pIMSForm.fromDate.value) == 0)
					{
						alert('<bean:message key="alertTodateLTFromDate"/>');
						document.pIMSForm.toDate.focus();
						document.pIMSForm.toDate.value="";
						return false;
				}
				}
			}






		var submitType="";
		<%
			String mode1=((String)(session.getAttribute("viewMode"))).trim();
			if(mode1.equalsIgnoreCase("modify"))
			{
		%>
			submitType="modifyDetailsAssignment";

		<%
		 }
		 else if(mode1.equalsIgnoreCase("create"))
		 {
		 %>
		 	submitType="saveDetailsAssignment";
		 <%
		 }
		 %>
		document.forms("pIMSForm").action = "../pims/AfterPIMSAction.do?submitType="+submitType;
		document.forms("pIMSForm").submit();
	}



}

function checkPositionForAnEmp(obj)
{

	var row=getRow(obj);
	var date = getControlInBranch(row,"fromDate");
	var datefrom = date.value;
	var dateto = getControlInBranch(row,"toDate");
	var dteto = dateto.value;

	var http = initiateRequest();

	var url = "<%=request.getContextPath()%>/pims/checkPositionForAnDesig.jsp?pos="+obj.value+"&dateFrom="+datefrom+"&dateTo="+dteto;
	http.open("GET", url, true);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			if (http.status == 200)
			{
			       var statusString =http.responseText.split("^");
			       if(statusString[0]=="true")
				{
					var popup = statusString[0];
					if(popup!="")
					{

						alert('<bean:message key="alertPosAss"/>');
						document.forms[0].posId.value = "0";
						return false;
					}
			       }
			 }
		}
	};
		http.send(null);
}
</script>


<div align="center">

<body onload ="setLength();execute()">
</div>

<Center>



		<!-- Tab Navigation Ends -->






<table align='center' id="table2" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->

<div align="center">
<center>
<html:form  action="/pims/AfterPIMSAction.do?submitType=saveDetails" >
<input type=hidden name="Id" id="Id" value="<%= id %> " />
<table  style="width: 800;" colspan="0" cellpadding ="0" cellspacing ="0" border = "2" colspan="5" id="nomPir" name="nomPir">
<tbody>
<tr>
  <td colspan="2" height=20 bgcolor=#dddddd align=middle  class="tableheader">
<p><bean:message key="Assignment"/></p></td>
  </tr>
  <tr>
  	  		<td class="labelcell" >
  	  		<bean:message key="EmployeeName"/> </td>

  	  		<td  class="labelcell" ><%=egpimsPersonalInformation.getEmployeeFirstName()%></td>
  	  		<td class="labelcell" >
  				  		<bean:message key="EmployeeCode"/> </td>

  	  		<td  class="labelcell" ><%=egpimsPersonalInformation.getEmployeeCode()%></td>
</tr>
  </table>

 <table  style="width:800;" colspan="0" cellpadding ="0" cellspacing ="0" border = "1" id="EOTable" name="EOTable" >
   <tbody>
   <tr>
   <td   class="labelcell" ><bean:message key="FromDate"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" ><bean:message key="ToDate"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" width="70" ><bean:message key="EmployeeFund"/></td>
   <td   class="labelcell" width="70" ><bean:message key="EmployeeFunction"/></td>
   <td   class="labelcell" width="70"><bean:message key="Designation"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" width="70"><bean:message key="Position"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" width="117" ><bean:message key="Functionary"/></td>
   <td   class="labelcell" width="75" ><bean:message key="Department"/><SPAN class="leadon">*</SPAN></td>
   <td   class="labelcell" width="75" ><bean:message key="MainDept"/><SPAN class="leadon">*</SPAN></td>

  </tr>
  <%


	String assignmentId[]=null;
	String fromDate[]=null;
	String toDate[]=null;
	String fundId[]=null;
	String functionId[]=null;
	String designationId[]=null;
	String functionaryId[]=null;
	String ptcallocation[]=null;
	String posId[]=null;

	String departmentId[]=null;
	int setSize=assPrdSet.size();
	assignmentId=new String[setSize];
	fromDate=new String[setSize];
	toDate=new String[setSize];
	fundId=new String[setSize];
	functionId=new String[setSize];
	designationId=new String[setSize];
	posId=new String[setSize];
	functionaryId=new String[setSize];
	ptcallocation=new String[setSize];
	Set deptSet = new HashSet();
	departmentId=new String[setSize];
	Iterator itr1 = assPrdSet.iterator();
	for(int x=0;itr1.hasNext();x++)
	{
		AssignmentPrd egEmpAssignmentPrd = (AssignmentPrd)itr1.next();
		assignmentId[x]=egEmpAssignmentPrd.getId()==null?"":egEmpAssignmentPrd.getId().toString();
		fromDate[x]=egEmpAssignmentPrd.getFromDate()==null?"":sdf.format(egEmpAssignmentPrd.getFromDate());
		toDate[x]=egEmpAssignmentPrd.getToDate()==null?"":sdf.format(egEmpAssignmentPrd.getToDate());
		Set egEmpAssignmentSet = egEmpAssignmentPrd.getEgpimsAssignment();
		if(egEmpAssignmentPrd.getId()!=null)
			HibernateUtil.getCurrentSession().lock(egEmpAssignmentPrd,LockMode.NONE);
		Iterator egEmpAssignmentSetitr = egEmpAssignmentSet.iterator();
		for(int i=0;egEmpAssignmentSetitr.hasNext();i++)
		{

			Assignment egEmpAssignment= (Assignment)egEmpAssignmentSetitr.next();
			designationId[i]=egEmpAssignment.getDesigId()==null?"0":egEmpAssignment.getDesigId().getDesignationId().toString();
			fundId[i]=egEmpAssignment.getFundId()==null?"0":egEmpAssignment.getFundId().getId().toString();
			functionId[i]=egEmpAssignment.getFunctionId()==null?"0":egEmpAssignment.getFunctionId().getId().toString();
			functionaryId[i]=egEmpAssignment.getFunctionary()==null?"0":egEmpAssignment.getFunctionary().getId().toString();
			posId[i] = egEmpAssignment.getPosition()==null?"0":egEmpAssignment.getPosition().getId().toString();
			deptSet = getIntegerObj(egEmpAssignment.getDeptSet());
			ptcallocation[i]=egEmpAssignment.getPctAllocation()==null?"100":egEmpAssignment.getPctAllocation().toString();
			 %>
					<tr id="EORow">
					<input type = hidden name="assignmentId" id="assignmentId" value="<%=assignmentId[i]%>" />
					<input type = hidden name="ptcallocation" id="ptcallocation" value="<%="100"%>" />
					<%
					if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
					{
					%>
					<td class="fieldcell">
					<input type="text"  style = "width:85; height:20" name="fromDate" id="fromDate" onblur="validateDateFormat(this)" value="<%=fromDate[i]%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
					<%
					}
					else
					{
					%>
					<td class="fieldcell">
					<input type="text"  style = "width:94; height:20" name="fromDate" id="fromDate" onblur="validateDateFormat(this)" value="<%=sdf.format(egpimsPersonalInformation.getDateOfFirstAppointment())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" ></td>
					<%
					}
					%>
					<td class="fieldcell" width="70">
					<input type="text"  style = "width:87; height:20" name="toDate" id="toDate" onBlur = "validateDateFormat(this)" value="<%=toDate[i]%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onchange = "checkPositionForAnEmp(this)"></td>
					<td class="fieldcell" width="70" >
					<select  name="fundId"   id="fundId" style = "width:70px" onMouseOver="addTitleAttributes(this);">
					 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
						<%

							Map fundMap =(Map)session.getAttribute("fundMap");
							for (Iterator it = fundMap.entrySet().iterator(); it.hasNext(); )
							{
									Map.Entry entry = (Map.Entry) it.next();
							%>
							<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == new Integer(fundId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>
							<%
							}
							%>
					</select>
					</td>
					<td class="fieldcell" >
						<select  name="functionId"   id="functionId" style = "width:75; height:21" onMouseOver="addTitleAttributes(this);">
						 <option value='0' selected="selected">choose</option>
							<%

								Map functionMap =(Map)session.getAttribute("functionMap");
								for (Iterator it = functionMap.entrySet().iterator(); it.hasNext(); )
								{
										Map.Entry entry = (Map.Entry) it.next();
								%>
								<option  value = "<%= entry.getKey().toString() %>"<%=((((Long)entry.getKey()).intValue() == new Integer(functionId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>
								<%
								}
								%>
							</select>
					</td>
					<td class="fieldcell" width="117" >
							<select  name="designationId"   id="designationId" style = "width:112; height:21" onchange="loadSelectData('../commonyui/egov/loadComboAjax.jsp', 'EG_POSITION', 'ID', 'POSITION_NAME', 'DESIG_ID=#1 order by ID', 'designationId', 'posId');" onMouseOver="addTitleAttributes(this);" >
							 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
								<%

									Map mapOfDesignation =(Map)session.getAttribute("mapOfDesignation");
									for (Iterator it = mapOfDesignation.entrySet().iterator(); it.hasNext(); )
									{
											Map.Entry entry = (Map.Entry) it.next();
								%>
									<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == new Integer(designationId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>
								<%
									}
								%>
								</select>
					</td>
					<td class="fieldcell" width="117" >
						<select  name="posId"   id="posId" style = "width:112; height:21" onchange = "checkPositionForAnEmp(this)" onMouseOver="addTitleAttributes(this);">
						 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
							<%

								Map positionMap =(Map)session.getAttribute("positionMap");
								for (Iterator it = positionMap.entrySet().iterator(); it.hasNext(); )
								{
										Map.Entry entry = (Map.Entry) it.next();
							%>
								<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == new Integer(posId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>
							<%
								}
							%>
							</select>
					</td>

					<td class="fieldcell" width="70" >
						<select  name="functionaryId"   id="functionaryId" style = "width:70px" onMouseOver="addTitleAttributes(this);">
						 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
							<%

								Map functionaryMap =(Map)session.getAttribute("functionaryMap");
								for (Iterator it = functionaryMap.entrySet().iterator(); it.hasNext(); )
								{
										Map.Entry entry = (Map.Entry) it.next();

								%>
								<option  value = "<%= entry.getKey().toString() %>"<%=((((Integer)entry.getKey()).intValue() == new Integer(functionaryId[i]).intValue())? "selected":"")%>><%= entry.getValue() %></option>

								<%
								}
							%>
							</select>
					</td>
					<%
										 			String mod=((String)(session.getAttribute("viewMode"))).trim();
										 			if(mod.equalsIgnoreCase("modify") || mod.equalsIgnoreCase("view"))
										 			{
										 		%>
										 			<td class="fieldcell" width="100" >

													<select  name="departmentId"   id="departmentId" multiple="true" onMouseOver="addTitleAttributes(this);">

														<%

															Map depMap =(Map)session.getAttribute("deptmap");
															System.out.println("depMap"+depMap);
															for (Iterator it = depMap.entrySet().iterator(); it.hasNext(); )
															{
																	Map.Entry entry = (Map.Entry) it.next();
																	if(deptSet.contains(entry.getKey()))
																	{
																	System.out.println("entry"+entry);
															%>
															<option  value = "<%= entry.getKey().toString() %>"<%="selected"%>><%= entry.getValue() %></option>
															<%
																	}
																	else
																	{
															%>
															<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
															<%
																	}
															}
															%>
													</select>
										</td>

										 		<%
										 		 }
										 		 else if(mod.equalsIgnoreCase("create"))
										 		 {
										 		 %>
										 		 	<td class="fieldcell" width="100" >

													<select  name="departmentId"   id="departmentId" multiple="true"  onMouseOver="addTitleAttributes(this);">
													 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
														<%

															Map deMap =(Map)session.getAttribute("deptmap");
															System.out.println("deMap"+deMap);
															for (Iterator it = deMap.entrySet().iterator(); it.hasNext(); )
															{
																	Map.Entry entry = (Map.Entry) it.next();
																	System.out.println("entry"+entry);
															%>
															<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
															<%
															}
															%>
													</select>
										</td>
										 		 <%
										 		 }
					 					 %>

													<%
															String mode=((String)(session.getAttribute("viewMode"))).trim();
															if(mode.equalsIgnoreCase("modify") || mode.equalsIgnoreCase("view"))
															{
														%>
															<td class="fieldcell" width="100" >

															<select  name="mainDepartmentId"   id="mainDepartmentId"  onMouseOver="addTitleAttributes(this);">

																<%

																	Map MaindeptMap =(Map)session.getAttribute("deptMap");
																	System.out.println("MaindeptMap"+MaindeptMap);
																	for (Iterator it = MaindeptMap.entrySet().iterator(); it.hasNext(); )
																	{
																			Map.Entry entry = (Map.Entry) it.next();
																			if(egEmpAssignment.getDeptId() !=null && egEmpAssignment.getDeptId().getId().equals((Integer)entry.getKey()))
																			{
																			System.out.println("entry"+entry);
																	%>
																	<option  value = "<%= entry.getKey().toString() %>"<%="selected"%>><%= entry.getValue() %></option>
																	<%
																			}
																			else
																			{
																	%>
																	<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
																	<%
																			}
																	}
																	%>
															</select>
												</td>

														<%
														 }
														 else if(mode.equalsIgnoreCase("create"))
														 {
														 %>
															<td class="fieldcell" width="100" >

															<select  name="mainDepartmentId"   id="mainDepartmentId" onMouseOver="addTitleAttributes(this);">
															 <option value='0' selected="selected"><bean:message key="chooseType"/></option>
																<%

																	Map MaindeptMap =(Map)session.getAttribute("deptMap");

																	for (Iterator it = MaindeptMap.entrySet().iterator(); it.hasNext(); )
																	{
																			Map.Entry entry = (Map.Entry) it.next();

																	%>
																	<option  value = "<%= entry.getKey().toString() %>"><%= entry.getValue() %></option>
																	<%
																	}
																	%>
															</select>
												</td>
														 <%
														 }
	 %>


					</tr>
<%
		}
	}
%>
</tbody>
</table>
<table  style="width: 800;" id="EOTableBtn" name="EOTableBtn" colspan="5" cellpadding ="0" cellspacing ="0" border = "1" >
<tr>
   <%
   if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
   {
%>
   <td> <input class="button2" id="addeoBtn" name="addeoBtn"  align ="center"type="button" value="AddRow" onclick="javascript:addRow(this,document.getElementById('EOTable'),document.getElementById('EORow'))"></td>

   <td><input class="button2" id="deltp" name="deltp"  type="button" value="DeleteRow " onclick="javascript:deleteRow(this,document.getElementById('EOTable'),document.getElementById('addeoBtn'))" ></td>
   <%
   }
   %>
   </tr>

</table>

<table id = "submit" style="width: 800;"  cellpadding ="0" cellspacing ="0" border = "1" value = "submit">
<tr >
<%
if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
{
%>
<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPressNew('savenew');" /></td>
 <%
 }
 %>

  <%
  if(((String)(session.getAttribute("viewMode"))).trim().equals("view")||((String)(session.getAttribute("viewMode"))).trim().equals("modify"))
  {
  %>
  		<td><html:button styleClass="button" value="Search" property="b4" onclick="goindex('Index')" /></td>
   <%
   }
 %>

<tr>
</table>
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