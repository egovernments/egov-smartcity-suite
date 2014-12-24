<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.egov.budget.services.*,
		 org.apache.log4j.Logger,
		 org.egov.pims.*,
		 org.egov.pims.utils.*,
		 org.egov.pims.dao.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.model.*,
		 org.egov.pims.client.*"


%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Asset Category</title>





	<%
	SkillMasterDAO skillMasterDAO =new  SkillMasterDAO();
	String idreq = request.getParameter("Id");
	SkillMaster skillMaster = skillMasterDAO.getSkillMaster(new Integer(idreq).intValue());
	Set gradeSet = skillMaster.getSettechnicalGradesMaster();
%>
<SCRIPT language="Javascript">

function resetRowValuesTG1(lastRow)
{
	document.skillVsGradeForm.gradeValue[lastRow].value="";
	document.skillVsGradeForm.gradeId[lastRow].value="0";
}

function addTG1()
{


    var tbl = document.getElementById('TGTable');
    var rowO=tbl.rows.length;

    if(rowO<11)
    {
    	if(document.getElementById('TGnameRow1') != null)
    	{

    			var tbody=tbl.tBodies[0];
			var lastRow = tbl.rows.length;
			var rowObj = document.getElementById('TGnameRow1').cloneNode(true);
			tbody.appendChild(rowObj);
			resetRowValuesTG1(lastRow-1);
	}

    }
  }

  var firstLength = 0;
  function setlen()
  {
  	var tbl=document.getElementById('TGTable');
  	firstLength=tbl.rows.length;
  }
function deleteTG1(obj)
{
	alert(">>>>>Inside deleteOwner SAS");
	var delRow = obj.parentNode.parentNode;

	//var tbl = delRow.parentNode.parentNode;
	var rIndex = delRow.rowIndex;

    //if(rIndex == 0)

    var tbl=document.getElementById('TGTable');
    var rowo=tbl.rows.length;

   alert("No. of Owners="+rowo);


    	if(rowo<=firstLength)
	{
		alert("This Grades can not be deleted");
		return false;
	}
	else
	{
		tbl.deleteRow(rIndex);
		return true;
	}
}


var unsignedInt = /^\d*$/;
var unsignedDecimal=/^\d*\.?\d*$/;
function ButtonPress(arg)
{
	if(arg == "savenew")
	{
		document.skillVsGradeForm.Id.value ="<%=skillMaster.getId()%>";
		var submitType="";
		<%
			String mode1=((String)(session.getAttribute("viewMode"))).trim();
			if(mode1.equalsIgnoreCase("modify"))
		{
		%>
			if(document.skillVsGradeForm.skillValue.value == "" )
			{
				alert("Pls Fill in the Name");
				document.skillValue.name.focus();
				return false;
			}
			if(document.skillVsGradeForm.fromDate.value == "" )
			{
				alert("Pls Fill in the from Date");
				document.skillVsGradeForm.fromDate.focus();
				return false;
			}
			if(document.skillVsGradeForm.toDate.value == "" )
			{
				alert("Pls Fill in the to Date");
				document.skillVsGradeForm.toDate.focus();
				return false;
			}
			var TGTable= document.getElementById("TGTable");
			var rows= TGTable.rows.length;
			alert("length of array"+rows)
			if(rows>2)
			{
				for(var i=0 ;i<rows-1;i++)
				{
					//alert("Name="+document.propWithTaxCollectionForm.firstName[i].value);
					if(document.skillVsGradeForm.gradeValue[i].value=="" )
					{
						alert("Please fill the grade Value");
						document.skillVsGradeForm.gradeValue[i].focus();
						return false;
					}
					if(document.skillVsGradeForm.fromDateGrd[i].value == "" )
					{
						alert("Pls Fill in the from Date");
						document.skillVsGradeForm.fromDateGrd[i].focus();
						return false;
					}
					if(document.skillVsGradeForm.toDateGrd[i].value == "" )
					{
						alert("Pls Fill in the to Date");
						document.skillVsGradeForm.toDateGrd[i].focus();
						return false;
					}


				}
			}
			else
			{
				if(document.skillVsGradeForm.gradeValue.value=="" )
				{
					alert("Please fill the grade Value");
					document.skillVsGradeForm.gradeValue.focus();
					return false;
				}
				if(document.skillVsGradeForm.fromDateGrd.value == "" )
				{
					alert("Pls Fill in the from Date");
					document.skillVsGradeForm.fromDateGrd.focus();
					return false;
				}
				if(document.skillVsGradeForm.toDateGrd.value == "" )
				{
					alert("Pls Fill in the to Date");
					document.skillVsGradeForm.toDateGrd.focus();
					return false;
				}

			}

			submitType="modifyDetails";
		 <%
		 }
		 	 else if(mode1.equalsIgnoreCase("create"))
		 {
		 %>
		 	if(document.skillVsGradeForm.skillValue.value == "" )
						{
							alert("Pls Fill in the Name");
							document.skillValue.name.focus();
							return false;
						}
						if(document.skillVsGradeForm.fromDate.value == "" )
						{
							alert("Pls Fill in the from Date");
							document.skillVsGradeForm.fromDate.focus();
							return false;
						}
						if(document.skillVsGradeForm.toDate.value == "" )
						{
							alert("Pls Fill in the to Date");
							document.skillVsGradeForm.toDate.focus();
							return false;
						}
						var TGTable= document.getElementById("TGTable");
						var rows= TGTable.rows.length;
						alert("length of array"+rows)
						if(rows>2)
						{
							for(var i=0 ;i<rows-1;i++)
							{
								//alert("Name="+document.propWithTaxCollectionForm.firstName[i].value);
								if(document.skillVsGradeForm.gradeValue[i].value=="" )
								{
									alert("Please fill the grade Value");
									document.skillVsGradeForm.gradeValue[i].focus();
									return false;
								}
								if(document.skillVsGradeForm.fromDateGrd[i].value == "" )
								{
									alert("Pls Fill in the from Date");
									document.skillVsGradeForm.fromDateGrd[i].focus();
									return false;
								}
								if(document.skillVsGradeForm.toDateGrd[i].value == "" )
								{
									alert("Pls Fill in the to Date");
									document.skillVsGradeForm.toDateGrd[i].focus();
									return false;
								}


							}
						}
						else
						{
							if(document.skillVsGradeForm.gradeValue.value=="" )
							{
								alert("Please fill the grade Value");
								document.skillVsGradeForm.gradeValue.focus();
								return false;
							}
							if(document.skillVsGradeForm.fromDateGrd.value == "" )
							{
								alert("Pls Fill in the from Date");
								document.skillVsGradeForm.fromDateGrd.focus();
								return false;
							}
							if(document.skillVsGradeForm.toDateGrd.value == "" )
							{
								alert("Pls Fill in the to Date");
								document.skillVsGradeForm.toDateGrd.focus();
								return false;
							}

			}
		 	submitType="saveDetails";
		 <%
		 }
		 	else
		 {
		 %>
		 	submitType="deleteDetails";
		 <%
		 }
		 %>
		 	document.forms("skillVsGradeForm").action = "${pageContext.request.contextPath}/pims/AfterSkillAndGradeAction.do?submitType="+submitType;
			document.forms("skillVsGradeForm").submit();
	}
	if(arg == "close")
	{
		window.close();
	}

}
function onClickCancel()
{
	window.location="${pageContext.request.contextPath}/pims/skillVsGradeMasterCreate.jsp";
}


</SCRIPT>
<%
	response.addHeader("Pragma" , "No-cache") ;
	response.addHeader("Cache-Control", "no-cache") ;
	response.addDateHeader("Expires", 1);
 %>
</head>

<body onload="checkMode()" >
<!-- Header Section Begins -->

<!-- Header Section Ends -->
<body onLoad = "setlen()">

<table align=center>
<table align='center' id="table2">
<tr>
<td>


<br>
<table align=center>
<tr><td>

<html:form  action="/pims/AfterSkillAndGradeAction" >
<input type=hidden name="Id" id="Id" />
<table align='center' class="tableStyle">


   <tr>
     <td colspan="2" width="100%" class="tableheader" align="center"><span id="screenName">Skill Master<span></td>
   </tr>
    <tr><td class="labelcellforsingletd"  width="30%" align="right">Skill Name<SPAN class="leadon">*</SPAN>&nbsp;</td>
       <td class="fieldcell"   width="70%"  align="center"><html:text property="skillValue"  value="<%=skillMaster.getName()%>"/></td>

   </tr>
   <tr><td class="labelcellforsingletd"  width="30%" align="right">From Date<SPAN class="leadon">*</SPAN>&nbsp;</td>

          <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="fromDate" name="fromDate"   value="<%=skillMaster.getFromDate()%>" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
          <td class="labelcellforsingletd"  width="30%" align="right">To Date<SPAN class="leadon">*</SPAN>&nbsp;</td>
          <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="toDate" name="toDate"   value="<%=skillMaster.getToDate()%>" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
   </tr>
   </table>

   <table  style="width: 740;" colspan="5" id="TGTable" name="TGTable" style="border: 1px solid #D7E5F2">
      <tbody>
      <tr>
      <td   class="labelcellmedium" >Technical Grade*</font></td>
      <td   class="labelcell6" >grade</td>
      <td   class="labelcell6" >From Date</td>
      <td   class="labelcell6" >To Date</td>
      <td> <input class="button2" id="addTGBtn" name="addTGBtn"  type="button" value="AddRow" onclick="javascript:addTG1()" ></td>
   </tr>

<%

if(gradeSet != null && !gradeSet.isEmpty())
{
	String name[]=null;
	String id[]=null;
	String fDte[]=null;
	String tDte[]=null;
	int setSize=gradeSet.size();
	name=new String[setSize];
	id=new String[setSize];
	Iterator itr = gradeSet.iterator();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	for(int i=0;itr.hasNext();i++)
	{
		TechnicalGradesMaster technicalGradesMaster = (TechnicalGradesMaster)itr.next();
		name[i]=technicalGradesMaster.getName()==null?"":technicalGradesMaster.getName();
		id[i]=technicalGradesMaster.getId()==null?"":technicalGradesMaster.getId().toString();
		fDte[i]=technicalGradesMaster.getFromDate()==null?"":sdf.format(technicalGradesMaster.getFromDate());
		tDte[i]=technicalGradesMaster.getToDate()==null?"":sdf.format(technicalGradesMaster.getToDate());


%>
   <tr id="TGnameRow">
   <td class="labelcell">&nbsp;</td>
   <td class="labelcell">
   <td class="labelcell"><input type="text"  size="10" id="gradeValue" name="gradeValue" value="<%=name[i]%>"></td>
   <input type = hidden name="gradeId" id="gradeId" value="<%=id[i]%>" />
   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="toDateGrd" name="toDateGrd" value = "<%=tDte[i]%>" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="fromDateGrd" name="fromDateGrd" value = "<%=fDte[i]%>" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
   <td><input class="button2" id="delTGBtn" name="delTGBtn"  type="button" value="DeleteRow" onclick="javascript:deleteTG(this)" STYLE = "DISPLAY: none"></td>
   </tr>
   <%
   	}
   	%>
   	<tr id="TGnameRow1">
	   <td class="labelcell">&nbsp;</td>
	   <td class="labelcell">
	   <td class="labelcell"><input type="text"  size="10" id="gradeValue" name="gradeValue" ></td>
	   <input type = hidden name="gradeId" id="gradeId"  />
	   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="toDateGrd" name="toDateGrd" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
	   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="fromDateGrd" name="fromDateGrd" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>

	   <td><input class="button2" id="delTGBtn" name="delTGBtn"  type="button" value="DeleteRow" onclick="javascript:deleteTG1(this)" ></td>
   </tr>
   	<%

   }
   else
   {

   %>
   <tr id="TGnameRow">
      <td class="labelcell">&nbsp;</td>
      <td class="labelcell">
      <td class="labelcell"><input type="text"  size="10" id="gradeValue" name="gradeValue" ></td>

      	   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="toDateGrd" name="toDateGrd" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
	   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="fromDateGrd" name="fromDateGrd" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
      <td><input class="button2" id="delTGBtn" name="delTGBtn"  type="button" value="DeleteRow" onclick="javascript:deleteTG(this)" ></td>
   </tr>
   <%
   }
   %>

   </tbody>
   </table>



  <table align=center name="buttonTable" id="buttonTable">
   	<tr>

  		<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPress('savenew')" /></td>
   	<tr>
   </table>
 <table align=center style="DISPLAY: none" id="row1" name="row1">
    	<tr >
 		<td ><html:button styleClass="button" value=" Back " property="b2" onclick="ButtonPress('close')" /></td>
 	<tr>
 </table>
  </html:form>
  </div>
  </td></tr>
  </table>
 </body>
</html>