
<%@page import="org.egov.infstr.services.EISServeable"%>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.*,
		 org.egov.pims.dao.*,
		 org.egov.pims.service.*,
		 org.egov.pims.utils.*,
		 org.hibernate.LockMode,
		  org.egov.payroll.utils.*,
		  org.egov.pims.commons.client.*,
		 org.egov.pims.model.*,
		 org.egov.infstr.commons.*,
		 org.egov.infstr.commons.dao.*,
		 org.egov.pims.commons.dao.*,
		 org.egov.pims.commons.Position,
		org.egov.commons.*,
		org.egov.pims.commons.DesignationMaster,
		 org.egov.infstr.utils.*,
		 org.egov.lib.rjbac.dept.*,
		 org.egov.lib.address.model.*,
		 org.egov.payroll.model.*,
		 org.egov.lib.address.dao.*,
		 java.text.SimpleDateFormat,
		 java.util.StringTokenizer,
		 org.egov.pims.client.*"
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Employee Information</title>

<LINK rel="stylesheet" type="text/css" href="../css/ccMenu.css">

<SCRIPT type="text/javascript" src="../javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>

<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>

<%!

String id ="";
EmployeeNamePoJo employeeNamePoJo = null;

private Map getMapOfEmployeeForDesig(Integer desId)
{
	Map empMap = new HashMap();
	try
	{

		EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();

		if(desId!=null&&!desId.equals(new Integer(0)))
		{
			try
			{
				List emplist = employeeServiceImpl.getListOfEmpforDesignation(desId);
				EmployeeNamePoJo employeeName  =null;
				if(emplist!=null&&!emplist.isEmpty())
				{
					Iterator empitr = emplist.iterator();
					while(empitr.hasNext())
					{
						PersonalInformation personalInformation = (PersonalInformation)empitr.next();
						employeeName = employeeServiceImpl.getNameOfEmployee(personalInformation.getIdPersonalInformation());
						empMap.put(personalInformation.getIdPersonalInformation(),employeeName.getFirstName());
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}catch(Exception e){}
	return empMap;

}


%>
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
	EmployeeServiceImpl employeeServiceImpl1=new EmployeeServiceImpl();
%>
<%

	Map deptMap = (Map)session.getAttribute("deptmap");
	session.setAttribute("deptMap",deptMap);
	Set assPrdSet =null;

	AssignmentPrd assignmentPrd = null;
	PersonalInformation egpimsPersonalInformation =null;
	Department depObj = (Department)EisManagersUtill.getDeptService().getDepartment(new Integer(1));
	System.out.println("depObj"+depObj);
	String id ="";
	if(request.getParameter("Id")!=null)
	{

		id = request.getParameter("Id").trim();
		System.out.println("the id in the jsp ::::: " + id);
		egpimsPersonalInformation = employeeServiceImpl1.getEmloyeeById(new Integer(id));
		if( id!=null&&!id.equals(""))
		{
			AssignmentPrdDAO assDAO =  EisDAOFactory.getDAOFactory().getAssignmentPrdDAO();
			assPrdSet= new HashSet();
			String prd = request.getParameter("prdId");

			assignmentPrd= employeeServiceImpl1.getAssignmentPrdById(new Integer(prd));
			if(assignmentPrd!=null)
				assPrdSet.add(assignmentPrd);


		}


	}
	else
	{
		egpimsPersonalInformation = new PersonalInformation();
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

function populateList()
{
	var http = initiateRequest();
	var url = "/leave/populateSession.jsp";
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
function goindex(arg)
{

	if(arg == "Index")
	{

		document.forms("pIMSForm").action = "/staff/index.jsp";
		document.forms("pIMSForm").submit();
	}


}



function validateName( strValue)
{
var iChars = "!@#$%^&*()+=-[]\\\';,/{}|\":<>?0123456789";

for (var i = 0; i < strValue.value.length; i++)
{
	if (iChars.indexOf(strValue.value.charAt(i)) != -1)
	{
		alert("enter a valid name");
		strValue.focus();
		return false;
	}
}
}

function checkPositionForAnEmp(obj)
{
alert('hi');
	var row=getRow(obj);
	var date = getControlInBranch(row,"fromDate");
	var datefrom = date.value;
	var dateto = getControlInBranch(row,"toDate");
	var dteto = dateto.value;

	var http = initiateRequest();

	var url = "/pims/checkPositionForAnDesig.jsp?pos="+obj.value+"&dateFrom="+datefrom+"&dateTo="+dteto;
	http.open("GET", url, true);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			if (http.status == 200)
			{
			       var statusString =http.responseText.split("^");
				alert('hi'+statusString);
			       if(statusString[0]=="true")
				{
					var popup = statusString[0];
					if(popup!="")
					{

						alert('this Position is alredy assigned for this piroed');
						document.forms[0].posId.value = "0";
						return false;
					}
			       }
			 }
		}
	};
		http.send(null);
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
 function ButtonPressNew(arg)
 {


 	if(arg == "savenew")
 	{

				if(document.pIMSForm.employeeCode.value == "" )
				{
					alert("Pls Fill in the employee Code");
					document.pIMSForm.employeeCode.focus();
					return false;
				}
				if(document.pIMSForm.firstName.value == "")
				{
					alert("please fill the firstname ");
					document.pIMSForm.firstName.focus();
					return false;
				}
 				if(document.pIMSForm.fromDate.value=="" )
 				{
 					alert("Please fill fromDate ");
 					document.pIMSForm.fromDate.focus();
 					return false;
 				}

 				if(document.pIMSForm.designationId.value=="0" )
 				{
 					alert("Please choose the designation");
 					document.pIMSForm.designationId.focus();
 					return false;
 				}
 				if(document.pIMSForm.posId.value=="0" )
								{
									alert("Please choose the position");
									document.pIMSForm.posId.focus();
									return false;
				}
 				if(document.pIMSForm.departmentId.value=="0" )
				{
					alert("Please choose the department");
					document.pIMSForm.departmentId.focus();
					return false;
 				}

 					if(document.pIMSForm.toDate.value!="" )
 				{

 					if(compareDate(document.pIMSForm.toDate.value,document.pIMSForm.fromDate.value) == 1||compareDate(document.pIMSForm.toDate.value,document.pIMSForm.fromDate.value) == 0)
 					{
 						alert("ToDate is lesser or Equal than FromDate");
 						document.pIMSForm.toDate.focus();
 						document.pIMSForm.toDate.value="";
 						return false;
 					}
 				}



 		var submitType="";
 		<%
 			String mode1=((String)(session.getAttribute("viewMode"))).trim();
 			if(mode1.equalsIgnoreCase("modify"))
 			{
 		%>
 			submitType="modifyDetailsLightWeightEmployee";

 		<%
 		 }
 		 else if(mode1.equalsIgnoreCase("create"))
 		 {
 		 %>
 		 	submitType="saveDetailsLightWeightEmployee";
 		 <%
 		 }
 		 %>
 		document.forms("pIMSForm").action = "../pims/AfterPIMSAction.do?submitType="+submitType;
 		document.forms("pIMSForm").submit();
 	}



}
</script>
</head>
<!-- Header Section Begins -->

<!-- Header Section Ends -->

<body >

<table align=center>
<table align='center' id="table2">
<tr>
<td>

<html:form  action="/pims/AfterPIMSAction.do?submitType=saveDetailsLightWeightEmployee" >
<%
if(!((String)(session.getAttribute("viewMode"))).trim().equals("create"))
{
%>
<input type=hidden name="Id" id="Id" value="<%= request.getParameter("Id").trim() %> " />
<%
}
%>
<table align='center' class="tableStyle"cellpadding ="0" cellspacing ="0" border = "1"  name ="pisTable" id ="pisTable" width="785" style="border: 1px solid #D7E5F2">

  <tr>
        <td colspan="8"  class="tableheader" align="center">Employee Information System<span></td>
 </tr>

<tr>
    <td  class="labelcell" width="149">Employee Code<SPAN class="leadon">*</SPAN></td>
    <td  colspan="3" class="fieldcell" width="305" >
    <%
    if(!((String)(session.getAttribute("viewMode"))).trim().equals("create"))
    {
    %>
<input type="text"id="employeeCode" name="employeeCode" size="10" value="<%=egpimsPersonalInformation.getEmployeeCode()==null?null:egpimsPersonalInformation.getEmployeeCode() %>"  >
    <%
    }
    else
    {
%>
<input type="text"id="employeeCode" name="employeeCode" size="10" value="<%=egpimsPersonalInformation.getEmployeeCode()==null?null:egpimsPersonalInformation.getEmployeeCode() %>" onblur="uniqueChecking('/commonyui/egov/uniqueCheckAjax.jsp',  'EG_EMPLOYEE', 'CODE', 'employeeCode', 'no', 'no')" >

<%
}
%>

  </tr>


<tr>
    <td   class="labelcell" width="149">Employee Name&nbsp;<SPAN class="leadon"><SPAN class="leadon">*</SPAN></SPAN></td>
    <td  class="labelcell"colspan="2" width="306"> First Name<SPAN class="leadon">*</SPAN><input value="<%=egpimsPersonalInformation.getEmployeeFirstName()==null?"":egpimsPersonalInformation.getEmployeeFirstName() %>"  class="fieldcell" type="text" id="firstName" name="firstName" size="10"    >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
    <td  class="labelcell" width="166" colspan="3">Middle Name<input type="text" class="fieldcell" id="middleName"  name="middleName" size="10" onblur="validateName(this);"  value="<%=egpimsPersonalInformation.getEmployeeMiddleName()==null?"":egpimsPersonalInformation.getEmployeeMiddleName() %>"></td>
    <td  class="labelcell" width="184" colspan="2">Last Name<input type="text" class="fieldcell" name="lastName" id="lastName" size="10"  onblur="validateName(this);" value="<%=egpimsPersonalInformation.getEmployeeLastName()==null?"":egpimsPersonalInformation.getEmployeeLastName() %>"></td>
  </tr>


  <tr>

<tr>
    <td  class="labelcell" width="149">Is User Active</td>
       <%
          	String uac="0";
          	Integer uactive= new Integer(0);
          	if(egpimsPersonalInformation.getUserMaster()!=null)
          		HibernateUtil.getCurrentSession().lock(egpimsPersonalInformation.getUserMaster(), LockMode.NONE);
          	if(egpimsPersonalInformation.getUserMaster()!=null)
          		uactive= egpimsPersonalInformation.getUserMaster().getIsActive();
          	uac= uactive.toString();
    if(uac.equals("0"))
      	{
    %>

        <td  colspan="3" class="labelcellforsingletd" width="305"> yes
    <input type="radio" value="1" name="userStatus" id="userStatus"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No<input type="radio" value="0" checked name="userStatus" id="userStatus">
    </td>
    <%

    }
    else
    {
    %>
        <td  colspan="3" class="labelcellforsingletd" width="305"> Yes
    <input type="radio" value="1" name="userStatus" id="userStatus"  checked> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No<input type="radio" value="0"  name="userStatus" id="userStatus">
    </td>
    <%
    }
 %>



 <td  class="labelcell" width="149">Is Employee Active</td>
        <%
           	String ac="0";
           	Integer active = egpimsPersonalInformation.getIsActive();
           	ac= active.toString();
     if(ac.equals("0"))
       	{
     %>

         <td  colspan="3" class="labelcellforsingletd" width="305"> yes
     <input type="radio" value="1" name="isActive" id="isActive"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No<input type="radio" value="0" checked name="isActive" id="isActive">
     </td>
     <%

     }
     else
     {
     %>
         <td  colspan="3" class="labelcellforsingletd" width="305"> Yes
     <input type="radio" value="1" name="isActive" id="isActive"  checked> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No<input type="radio" value="0"  name="isActive" id="isActive">
     </td>
     <%
     }
  %>


 </tr>
<tr>
     <td   class="labelcell" width="149">User Name&nbsp;<SPAN class="leadon"></td>
     <td   class="fieldcell" width="149"><input value="<%=egpimsPersonalInformation.getUserMaster()==null?"":egpimsPersonalInformation.getUserMaster().getUserName() %>"  class="fieldcell" type="text" id="userFirstName" name="userFirstName" size="10"  onblur="validateName(this);uniqueChecking('/commonyui/egov/uniqueCheckAjax.jsp',  'EG_USER', 'USER_NAME', 'userFirstName', 'no', 'no');"  >
 	</td>
  </tr>
  </table>
 <table  style="width: 800;" colspan="5" align = "center" cellpadding ="0" cellspacing ="0" border = "1" id="EOTable" name="EOTable" >
    <tbody>
    <tr>

    <td   class="labelcell" >FromDate<SPAN class="leadon">*</SPAN></td>
    <td   class="labelcell" >ToDate<SPAN class="leadon">*</SPAN></td>
    <td   class="labelcell" >Designation<SPAN class="leadon">*</SPAN></td>
    <td   class="labelcell" >Position<SPAN class="leadon">*</SPAN></td>
    <td   class="labelcell" width="75" >Departmnent<SPAN class="leadon">*</SPAN></td>
    <td   class="labelcell" width="75" >MainDepartmnent<SPAN class="leadon">*</SPAN></td>

   </tr>
   <%

   System.out.println("sdfghsdg"+assPrdSet);
 	String assignmentId[]=null;
 	String fromDate[]=null;
 	String toDate[]=null;
 	String designationId[]=null;
 	String posId[]=null;

 	int setSize=assPrdSet.size();
 	assignmentId=new String[setSize];
 	fromDate=new String[setSize];
 	toDate=new String[setSize];
 	designationId=new String[setSize];
 	posId=new String[setSize];
 	Integer  assId=null;
 	Set deptSet = new HashSet();
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
 			posId[i] = egEmpAssignment.getPosition()==null?"0":egEmpAssignment.getPosition().getId().toString();


 			deptSet = getIntegerObj(egEmpAssignment.getDeptSet());
 			 %>
 					<tr id="EORow">

 					<input type = hidden name="assignmentId" id="assignmentId" value="<%=assignmentId[i]%>" />
 					<td class="fieldcell">
 					<input type="text"  style = "width:85; height:20" name="fromDate" id="fromDate" onblur="validateDateFormat(this)" value="<%=fromDate[i]%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"></td>
 					<td class="fieldcell" width="70">
					<input type="text"  style = "width:87; height:20" name="toDate" id="toDate" onBlur = "validateDateFormat(this)" value="<%=toDate[i]%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onchange = "checkPositionForAnEmp(this)"></td>
 					<td class="fieldcell" width="117" >
 							<select  name="designationId"   id="designationId" style = "width:112; height:21" onchange = "javascript:changePosTo(this)" >
 							 <option value='0' selected="selected">choose</option>
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
											<select  name="posId"   id="posId" style = "width:112; height:21" onchange = "checkPositionForAnEmp(this)" >
											 <option value='0' selected="selected">choose</option>
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
 					<%
					 			String mod=((String)(session.getAttribute("viewMode"))).trim();
					 			if(mod.equalsIgnoreCase("modify") || mod.equalsIgnoreCase("view"))
					 			{
					 		%>
					 			<td class="fieldcell" width="100" >

								<select  name="departmentId"   id="departmentId" multiple="true" >

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

								<select  name="departmentId"   id="departmentId" multiple="true"  >
								 <option value='0' selected="selected">choose</option>
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

										<select  name="mainDepartmentId"   id="mainDepartmentId"  >


											<%
													System.out.println("egEmpAssignment"+egEmpAssignment.getId());
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

										<select  name="mainDepartmentId"   id="mainDepartmentId" >
										 <option value='0' selected="selected">choose</option>
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

<table align='center' class="tableStyle" cellpadding ="0" cellspacing ="0" border = "1"  name ="submit" id ="submit" width="785" style="border: 1px solid #D7E5F2">
<tr >
  		<%
		if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		{
		%>
				<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPressNew('savenew');" /></td>
		 <%
		 }
		 %>
  		<td><html:button styleClass="button" value="Search" property="b4" onclick="goindex('Index')" /></td>
</tr>
</table>
 </html:form>
</table>
</center>
</div>
</div>
</td>
</tr>
<!-- Body Section Ends -->
</table>
</body>