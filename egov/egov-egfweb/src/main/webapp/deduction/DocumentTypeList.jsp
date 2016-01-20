<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<%@ page import="java.util.*,
		org.apache.log4j.Logger,org.egov.infstr.utils.EgovMasterDataCaching,
		org.egov.commons.EgPartytype,org.egov.commons.EgwTypeOfWork,
		java.text.DecimalFormat,org.egov.lib.deduction.client.DocumentTypeForm"
%>

<html>
<head>
	<title>Document Type Master Setup</title>
	
<script>
var documentTypeId;
function ButtonPress(arg)
{
	if(arg == "getDocumentTypeSearchList")
	{
		var mode=document.DocumentTypeForm.mode.value;	
		document.DocumentTypeForm.action = "../deduction/DocumentTypeMaster.do?submitType=getDocumentTypeSearchList&mode="+mode;
		document.DocumentTypeForm.submit();
	}
	if(arg == "showDetails")
	{
		var mode=document.DocumentTypeForm.mode.value;		
		if(mode == "modify")
		{
			window.open("../deduction/DocumentTypeMaster.do?submitType=beforeViewAndModifyDocumentType&mode="+mode+"&documentTypeId="+documentTypeId,"","height=650,width=980,scrollbars=yes,left=30,top=30,status=yes");
		}
		if(mode == "view")
		{
			window.open("../deduction/DocumentTypeMaster.do?submitType=beforeViewAndModifyDocumentType&mode="+mode+"&documentTypeId="+documentTypeId,"","height=650,width=980,scrollbars=yes,left=30,top=30,status=yes");
		}		
	}
}

function setDefault()
{


   //var mode=document.DocumentTypeForm.mode.value;
	var mode="${mode}";
	if(mode=="view")
	{
		document.title="View Document Type";
		//document.getElementById('screenName').innerHTML="View Document Type";
	            document.getElementById("row1").style.display="block";
	        	document.getElementById("row2").style.display="none";

		
	}
	if(mode=="modify" )
	
	{   	
	
	document.title="Modify Document Type";
	//document.getElementById('screenName').innerHTML="Modify Document Type"
	document.getElementById("row1").style.display="none";
	document.getElementById("row2").style.display="block";
	document.title="Document Type";
	//document.getElementById('screenName').innerHTML="Modify Document Type";
	}
	

	<%
	
	   ArrayList documentTypeDetailList=(ArrayList)request.getAttribute("documentTypeDetailList");
		if(documentTypeDetailList!=null && documentTypeDetailList.size()>0)		
		{
	%>
		hideColumn();
	<%
		}
	%>
	

	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		bootbox.alert("<%=request.getAttribute("alertMessage")%>");
	}
	
}
function hideColumn()
{
	var table=document.getElementById('documentTypeGrid');
   	for(var i=0;i<table.rows.length;i++)
   	{
   		table.rows[i].cells[0].style.display="none";
   		table.rows[i].cells[1].style.display="none";
       } 
  	}
function getDetails(obj)
{
	var rowobj=getRow(obj);
	var table=document.getElementById('documentTypeGrid');
	documentTypeId=getControlInBranch(table.rows[rowobj.rowIndex],"documentTypeIdRow").innerHTML;
	ButtonPress("showDetails");
}
</script>
</head>
<body OnLoad = "setDefault()">

<html:form   action="/deduction/DocumentTypeMaster">
<html:hidden property="submitType" value=""/>
<html:hidden property="mode" value="${mode}"/>


<table align='center' class="tableStyle" id="table3"> 

 <tr>
 <td class="labelcell" colspan="4">&nbsp;</td> 
</tr>
<tr>
	<html:hidden property="id" />
	<td class="labelcell" align="right">Code&nbsp;&nbsp;</td> 
	<td class="fieldcell"><html:text property= "code" maxlength="20"/></td>
	
	<td class="labelcell" align="right">Parent Code&nbsp;&nbsp;</td> 
	<td class="fieldcell"><html:text property= "parentCode" maxlength="20"/></td>
</tr>

<tr>
	<td class="labelcell" align="right">Desciption&nbsp;&nbsp;</td> 
	<td class="fieldcell"><html:text property= "description" maxlength="1000"/></td>
	<td class="labelcell" align="right">Applied To&nbsp;&nbsp;</td> 
	<td class="fieldcell"><html:text property= "appliedToCode" maxlength="20"/></td>
</tr>
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr style="DISPLAY: block" id="row1" name="row1">
<td  colspan=4 align="center">
<html:button styleClass="button" value="Search" property="b2" onclick="ButtonPress('getDocumentTypeSearchList')" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close()" /></td>
</td>
</tr>
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr style="DISPLAY: None" id="row2" name="row2">
<td  colspan=4 align="center">
<html:button styleClass="button" value="Search" property="b2" onclick="ButtonPress('getDocumentTypeSearchList')" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close()" /></td>
</td>
</tr>

<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<%	
	if(documentTypeDetailList!=null && documentTypeDetailList.size()>0)
	{
%>
	

<tr>
<td colspan=4 align="center">
<table  cellpadding="0" cellspacing="0" align="center" id="documentTypeGrid" name="documentTypeGrid" >
<tr>
<td class="thStlyle" width="5%"><div align="center">Document Type Id</div></td>
<td class="thStlyle" width="5%"><div align="center">Document Type Parent Id</div></td>
<td class="thStlyle" width="8%"><div align="center"> Code</div></td>
<td class="thStlyle" width="8%"><div align="center"> Parent Code</div></td>
<td class="thStlyle" width="30%"><div align="center">Description</div></td>
<td class="thStlyle" width="30%"><div align="center">Applied To(Party Type)</div></td>
</tr>
	<%	
	for (Iterator it = documentTypeDetailList.iterator(); it.hasNext(); ) 
	{
		EgwTypeOfWork typeOfWork=(EgwTypeOfWork)it.next();
		
	%>
<tr>
	<td class="tdStlyle" width="5%"><div align="left" name="documentTypeIdRow" id="documentTypeIdRow"><%= typeOfWork.getId() %></div> </td>
	<td class="tdStlyle" width="5%"><div align="left" name="documentTypeParentIdRow" id="documentTypeParentIdRow"><%= (typeOfWork.getParentid()!=null)? typeOfWork.getParentid().getId().toString() : "&nbsp;"%></div> </td>	
	<td class="tdStlyle" width="15%"><A style="text-decoration: none;" href="#" onClick="getDetails(this)" ><div  class="txt" align="left" name="documentTypeCodeRow" id="documentTypeCodeRow"><%= typeOfWork.getCode() %></div></A> </td>	
	<% if(typeOfWork.getParentid()!=null)
	{	%>
	<td class="tdStlyle" width="15%"><div align="left" name="documentTypeParentCode" id="documentTypeParentCode"><%= (typeOfWork.getParentid() != null) ? typeOfWork.getParentid().getCode() : "&nbsp;" %></div> </td>

<% }
	else 
	{ %>
	<td class="tdStlyle" width="15%"><div align="left" name="documentTypeParentCode" id="documentTypeParentCode"><%= (typeOfWork.getParentid() != null) ? typeOfWork.getParentid().getCode() : "&nbsp;" %></div> </td>
  <% 
	} 
  %>
	<td class="tdStlyle" width="30%"><div align="left" name="description" id="description"><%= typeOfWork.getDescription() %></div> </td>	
	<td class="tdStlyle" width="15%"><div align="left" name="partyType" id="partyType"><%= typeOfWork.getEgPartytype().getCode() %></div> </td>	
</tr>
	<%
	}
	%>      
</table>
</td>
</tr>
<%
}
%>
</table>
</html:form>

</body>
</html>	
