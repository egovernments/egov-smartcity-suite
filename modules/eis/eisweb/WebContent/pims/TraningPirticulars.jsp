
<%@ include file="/includes/taglibs.jsp" %>
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
		 org.egov.lib.address.dao.AddressTypeDAO,
		 org.egov.lib.address.model.*,
		 org.egov.lib.address.dao.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Training Particulars</title>


    <SCRIPT type="text/javascript" src="../javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>

     <script language="text/JavaScript" src="../Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>

<%
String id = request.getParameter("Id").trim();
Set tpSet = null;
EmployeeServiceImpl employeeServiceImpl=new EmployeeServiceImpl();

PersonalInformation egpimsPersonalInformation = null;
PersonalInformation empInformationDisp = null;

if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
{
	egpimsPersonalInformation = employeeServiceImpl.getEmloyeeById(new Integer(id));
	empInformationDisp = employeeServiceImpl.getEmloyeeById(new Integer(id));
	System.out.println("tpSet"+tpSet);
}
else
{
	egpimsPersonalInformation = new PersonalInformation();
	empInformationDisp= employeeServiceImpl.getEmloyeeById(new Integer(id));
	System.out.println("empInformationDisp"+empInformationDisp);
	
}
if(egpimsPersonalInformation.getEgpimsTrainingPirticularses().isEmpty()) {
	tpSet = new HashSet();
	tpSet.add(new TrainingPirticulars());
}
else
	tpSet = egpimsPersonalInformation.getEgpimsTrainingPirticularses();

SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


%>

<script>


function resetRowValues(lastRow,name)
{
       var table=document.getElementById('TPTable');
		getControlInBranch(table.rows[table.rows.length-1],'course').value="";
		getControlInBranch(table.rows[table.rows.length-1],'institution').value="";
		getControlInBranch(table.rows[table.rows.length-1],'city').value="";
		getControlInBranch(table.rows[table.rows.length-1],'fromTp').value="";
		getControlInBranch(table.rows[table.rows.length-1],'toTp').value="";
		getControlInBranch(table.rows[table.rows.length-1],'tpId').value=0;
		


}
function addRow(obj,tableName,row)
{

		
    	var tbl=tableName;
    	var rowO=tbl.rows.length;
    	var name=obj.value;
    	
        var tname = "resetRowValues"+name;

        	if(row != null)
        	{
        		var tbody=tbl.tBodies[0];
        		rIndex = 0;
    			var lastRow = tbl.rows.length;
    			var rowObj = row.cloneNode(true);
    			rIndex = rowObj.rowIndex;
    			tbody.appendChild(rowObj);
    			resetRowValues(lastRow-1,name);
		}


}
var firstLength = 0;
var rIndex = 0;

  function setLength()
  {

 	 var tbl=document.getElementById('TPTable');
    	var rowo=tbl.rows.length;
  	firstLength = rowo;

  }
  function deleteRow(obj,tableName,addButtion)
  {
      var tbl=tableName;
      var rowo=tbl.rows.length;
		firstLength = 2;
		 var rowNumber=getRow(obj).rowIndex;
		var tranningObj = getControlInBranch(tbl.rows[rowNumber],"tpId");
		
      
     if(rowo<=firstLength)
  	{
  		alert('<bean:message key="alertCannotDelete"/>');
  		return false;
  	}
  	else
  	{

  		tbl.deleteRow(rowo-1);
  		<%
  		String modeDel=((String)(session.getAttribute("viewMode"))).trim(); 
  		if(modeDel.equalsIgnoreCase("modify"))
			{%>
			   if(tranningObj!=null)
			   {
  					populateDeleteSet("tranningObjSet",tranningObj.value);
  				}
  				
  		<%}%>
  		return true;
  	}
  }
  
  function populateDeleteSet(setName, delId)
{
	if(delId != null && delId != "")
	{
	var http = initiateRequest();
    var url = "${pageContext.request.contextPath}/pims/updateDelSets.jsp?type="+setName+"&id="+delId;
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
}
  
  
  function setReadOnly()
  {

  <%
  		String modeon=((String)(session.getAttribute("viewMode"))).trim();
  		if(modeon.equalsIgnoreCase("view"))
  		{
  		%>
  			document.pIMSForm.course.readOnly= true;
  			document.pIMSForm.institution.readOnly=true;
  			document.pIMSForm.city.readOnly=true;
  			document.pIMSForm.fromTp.disabled=true;
  			document.pIMSForm.toTp.disabled=true;

  			var tpTable= document.getElementById("TPTable");
						var rows= tpTable.rows.length;

		if(rows==2)
				{
					document.pIMSForm.course.readOnly= true;
					document.pIMSForm.institution.readOnly=true;
					document.pIMSForm.city.readOnly=true;
					document.pIMSForm.fromTp.disabled=true;
					document.pIMSForm.toTp.disabled=true;

                 }
                 if(rows>2)
			{
			for(var i=0 ;i<rows-1;i++)
			{

				document.pIMSForm.course[i].readOnly= true;
				document.pIMSForm.institution[i].readOnly=true;
				document.pIMSForm.city[i].readOnly=true;
				document.pIMSForm.fromTp[i].disabled=true;
				document.pIMSForm.toTp[i].disabled=true;

			}
			}

  			<%
  		}
  		%>
}
function ButtonPressNew(arg)
{



	if(arg == "savenew")
	{

		var tbl= document.getElementById("TPTable");
			var rows= tbl.rows.length;

			if(rows>2)
			{
				for(var i=0 ;i<rows-1;i++)
				{
					//alert("Name="+document.propWithTaxCollectionForm.firstName[i].value);
					if(document.pIMSForm.course[i].value=="" )
					{
						alert('<bean:message key="alertFillCourseNme"/>');
						document.pIMSForm.course[i].focus();
						return false;
					}
					if(document.pIMSForm.institution[i].value =="" )
					{
						alert('<bean:message key="alertFillInst"/>');
						document.pIMSForm.institution[i].focus();
						return false;
					}
					if(document.pIMSForm.city[i].value=="" )
					{
						alert('<bean:message key="alertFillCity"/>');
						document.pIMSForm.city[i].focus();
						return false;
					}
					if(document.pIMSForm.fromTp[i].value=="" )
					{
						alert('<bean:message key="alertEnterFromDate"/>');
						document.pIMSForm.fromTp[i].focus();
						return false;
					}
					if(document.pIMSForm.toTp[i].value=="" )
					{
						alert('<bean:message key="alertFillToDate"/>');
						document.pIMSForm.toTp[i].focus();
						return false;
					}

					if(compareDate(document.pIMSForm.toTp[i].value,document.pIMSForm.fromTp[i].value) == 1||compareDate(document.pIMSForm.toTp[i].value,document.pIMSForm.fromTp[i].value) == 0)
					{
						alert('<bean:message key="alertTodateLTFromDate"/>');
						document.pIMSForm.toTp[i].focus();
						return false;
					}

				}
			}
			else
			{
				if(document.pIMSForm.course.value=="" )
				{
					alert('<bean:message key="alertFillCourseNme"/>');
					document.pIMSForm.course.focus();
					return false;
				}
				if(document.pIMSForm.institution.value=="" )
				{
					alert('<bean:message key="alertFillInst"/>');
					document.pIMSForm.institution.focus();
					return false;
				}
				if(document.pIMSForm.city.value=="" )
				{
					alert('<bean:message key="alertFillCity"/>');
					document.pIMSForm.city.focus();
					return false;
				}
				if(document.pIMSForm.fromTp.value=="" )
				{
					alert('<bean:message key="alertEnterFromDate"/>');
					document.pIMSForm.fromTp.focus();
					return false;
				}
				if(document.pIMSForm.toTp.value=="" )
				{
					alert('<bean:message key="alertFillToDate"/>');
					document.pIMSForm.toTp.focus();
					return false;
				}
				if(compareDate(document.pIMSForm.toTp.value,document.pIMSForm.fromTp.value) == 1||compareDate(document.pIMSForm.toTp.value,document.pIMSForm.fromTp.value) == 0)
				{
					alert("ToDate is lesser or Equal than FromDate");
					document.pIMSForm.toTp[i].focus();
					return false;
				}
	}
		var submitType="";
		<%
			String mode1=((String)(session.getAttribute("viewMode"))).trim();
			if(mode1.equalsIgnoreCase("modify"))
			{
		%>
			submitType="modifyTraningPirticulars";

		<%
		 }
		 else if(mode1.equalsIgnoreCase("create"))
		 {
		 %>
		 	submitType="saveTraningPirticulars";
		 <%
		 }
		 %>
		document.pIMSForm.action = "../pims/AfterPIMSAction.do?submitType="+submitType;
		document.pIMSForm.submit();
	}

}

function goindex(arg)
{

	if(arg == "Index")
	{

		document.forms("pIMSForm").action = "/staff/index.jsp";
		document.forms("pIMSForm").submit();
	}


}


</script>

</head> 

<div align="center">
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

<table width="95%" cellpadding ="0" cellspacing ="0" border = "0" align='center' id="table2" >
<tr>
<td>
<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->


<!-- Body Begins -->
<body onload = "setLength();setReadOnly();"/>
<div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">


<html:form  action="/pims/AfterPIMSAction.do?submitType=saveDetails" >
<input type=hidden name="Id" id="Id" value="<%= request.getParameter("Id").trim() %> " />
<div >
<center>
<table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" >
<tbody>
<tr>
 <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
<p><bean:message key="TrainingParticulars"/></p></td>

  </tr>
<tr>
<td class="whitebox2wk" colspan="8"><b><bean:message key="SponsoredCorp"/></td>
</tr>
<tr>
	<td class="greyboxwk" width="10%"><bean:message key="EmployeeName"/> </td>
    <td  class="greybox2wk" width="20%"><%=empInformationDisp.getEmployeeFirstName()%></td>
	 <td class="greyboxwk"  width="10%"><bean:message key="EmployeeCode"/> </td>
     <td  class="greybox2wk"><%=empInformationDisp.getEmployeeCode()%></td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>

 <table  width="95%" cellpadding ="0" cellspacing ="0" border = "0" align="center" id="TPTable" name="TPTable"  >
 
  
   <tbody>
   <tr>
   <td></td>
   <td width="15%" class="tablesubheadwk" ><span class="mandatory">*</span><bean:message key="Course"/></td>
   <td width="15%" class="tablesubheadwk" ><span class="mandatory">*</span><bean:message key="Institution"/></td>
   <td  width="15%" class="tablesubheadwk" ><span class="mandatory">*</span><bean:message key="City"/></td>
  
  <td  colspan ="2" width="50%">
  <table width="100%" cellpadding ="0" cellspacing ="0" border = "0" >
  <tr >  
  <td  colspan ="2" class="tablesubheadwknewTraning"><bean:message key="TrainingPeriod"/></td>    </tr>
  <tr> 
  <td width="50%" class="tablesubheadwk" ><span class="mandatory">*</span><bean:message key="fromDt"/></td> 
  <td width="50%" class="tablesubheadwk"  ><span class="mandatory">*</span><bean:message key="Todte"/></td>
  </tr>
  
   </table>
   <%
   if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
   {
%></td>
 <td width="2%" class="tablesubheadwk"  >Add/Del</td>
 
<%} %>

 <%
	Iterator itr1 = tpSet.iterator();
	for(int i=0;itr1.hasNext();i++)
	{
		TrainingPirticulars egpimsTrainingPirticulars = (TrainingPirticulars)itr1.next();
 %>


	<tr id="TPRow">

	<td><input type ="hidden" name="tpId" id="tpId" value="<%=egpimsTrainingPirticulars.getTrainingDetailsId()==null?"0":egpimsTrainingPirticulars.getTrainingDetailsId().toString()%>" /></td>
	<td class="whitebox3wknew"><input   type="text"   name="course" id="course" value="<%=egpimsTrainingPirticulars.getCourse()==null?"":egpimsTrainingPirticulars.getCourse()%>" ></td>
	<td class="whitebox3wknew"><input   type="text"   name="institution" id="institution" value="<%=egpimsTrainingPirticulars.getInstitution()==null?"":egpimsTrainingPirticulars.getInstitution()%>"  ></td>
	<td class="whitebox3wknew"><input   type="text"   name="city" id="city" value="<%=egpimsTrainingPirticulars.getCity()==null?"":egpimsTrainingPirticulars.getCity()%>" ></td>
	<td class="whitebox3wknew"><input   type="text"   name="fromTp" id="fromTp" onBlur = "validateDateFormat(this);validateDateJS(this)" value="<%=egpimsTrainingPirticulars.getPotFrom()==null?"":sdf.format(egpimsTrainingPirticulars.getPotFrom())%>" onBlur = "validateDateFormat(this);validateDateJS(this)" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"></td>
	<td class="whitebox3wknew"><input   type="text"   name="toTp" id="toTp" onBlur = "validateDateFormat(this);validateDateJS(this)" value="<%=egpimsTrainingPirticulars.getPotTo()==null?"":sdf.format(egpimsTrainingPirticulars.getPotTo())%>" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
	<%
   if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
   {
%>
	<td class="whitebox2wk" >
		<div align="center">
<a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0"onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'));"/></a>
<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0" onclick="javascript:deleteRow(this,document.getElementById('TPTable'));"/></a>
</div>
</td>
<%} %>
	      </td>



 



</tr>

<%
	}
%>

          

</tbody>

          
</table>



</center>
<br>
<div align="right" class="mandatory">* Mandatory Fields</div>

</div>


</div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>
 </html:form>

</table>
</center>

</tr>
<!-- Body Section Ends -->
<div>
 <%
   if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
   {
%>
<html:button styleClass="buttonfinal" value="Save" property="b2" onclick="ButtonPressNew('savenew');" />
<%
   }
   %>
<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/>
</html>
</div>


			
</body>