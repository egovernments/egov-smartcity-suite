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
		 org.egov.pims.client.*"


%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>Asset Category</title>


			<LINK rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ccMenu.css">







<SCRIPT language="Javascript">
var unsignedInt = /^\d*$/;
var unsignedDecimal=/^\d*\.?\d*$/;
function ButtonPress(arg)
{
	if(arg == "savenew")
	{
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
			var TGTable= document.getElementById("TGTable");
			var rows= TGTable.rows.length;

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
				document.skillVsGradeForm.skillValue.focus();
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
	window.location="../pims/skillVsGradeMasterCreate.jsp";
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


<table align=center>
<table align='center' id="table2">
<tr>
<td>


<br>
<table align=center>
<tr><td>

<html:form  action="/pims/AfterSkillAndGradeAction" >
<input type=hidden name="id" id="id" />
<table align='center' class="tableStyle">
   <tr>
     <td colspan="2" width="100%" class="tableheader" align="center"><span id="screenName">Course Master<span></td>
   </tr>
    <tr><td class="labelcellforsingletd"  width="30%" align="right">Skill Name<SPAN class="leadon">*</SPAN>&nbsp;</td>
           <td class="fieldcell"   width="70%"  align="center"><html:text property="skillValue"  /></td>

       </tr>
       <tr><td class="labelcellforsingletd"  width="30%" align="right">From Date<SPAN class="leadon">*</SPAN>&nbsp;</td>

              <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="fromDate" name="fromDate"    onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
              <td class="labelcellforsingletd"  width="30%" align="right">To Date<SPAN class="leadon">*</SPAN>&nbsp;</td>
              <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="toDate" name="toDate"   onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
   </tr>
   </table>
   <table  style="width: 789;" colspan="2" id="TGTable" name="TGTable" style="border: 1px solid #D7E5F2">
   <tbody>
   <tr>
   <td   class="labelcellmedium" >Technical Grade*</font></td>
   <td   class="labelcell6" >grade</td>
   <td   class="labelcell6" >From Date</td>
      <td   class="labelcell6" >To Date</td>
   <td> <input class="button2" id="addTGBtn" name="addTGBtn"  type="button" value="AddRow" onclick="javascript:addTG();" ></td>
   </tr>
   <tr id="TGnameRow">
         <td class="labelcell">&nbsp;</td>
         <td class="labelcell">
         <td class="labelcell"><input type="text"  size="10" id="gradeValue" name="gradeValue" ></td>

         	   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="toDateGrd" name="toDateGrd" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
   	   <td class="fieldcell" align="center"><input type="text"  style="width: 160;" id="fromDateGrd" name="fromDateGrd" onBlur = "validateDateFormat(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td>
         <td><input class="button2" id="delTGBtn" name="delTGBtn"  type="button" value="DeleteRow" onclick="javascript:deleteTG(this)" ></td>
   </tr>
   </tbody>
   </table>



  <table align=center name="buttonTable" id="buttonTable">
   	<tr>
  		<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPress('savenew')" /></td>

   	<tr>
   </table>

  </html:form>
  </div>
  </td></tr>
  </table>
 </body>
</html>