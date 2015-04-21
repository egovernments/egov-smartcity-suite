<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 org.egov.pims.commons.*,
		 java.text.SimpleDateFormat,
		 org.egov.pims.commons.dao.*"
%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252 ">
	<title>Designation</title>
	<SCRIPT type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></SCRIPT>
	
	<style type="text/css">
		#codescontainer {position:absolute;left:11em;}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    </style>
     
	
	<script type="text/javascript">		
	</script>
<%

Set positionSet = new HashSet();
String designationId = (String)session.getAttribute("Id");

DesignationMaster designationMaster = new DesignationMaster();
positionSet = designationMaster.getPositionSet();
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
{
	String id = (String)session.getAttribute("Id");
	id = id.trim();
	DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
	designationMaster = designationMasterDAO.getDesignationMaster(new Integer(id).intValue());
    positionSet = designationMaster.getPositionSet();


}
Set delPositions = new HashSet();	
session.setAttribute("delPositions", delPositions);
%>

<script>
  	
	var rowLength = 0;
	function populateRowLength(obj)
	{
		rowLength = obj.value;
		checkAlphaNumeric(obj);
	}

	function populateLength()
	{
		if(document.desigForm.outsourcedPostsDesig.value!=null && document.desigForm.outsourcedPostsDesig.value!="")
		{
			outLenLength = document.desigForm.outsourcedPostsDesig.value;
		}
	}

	var a="";
	function checkForUniqueDesignationName(designationName,designationId)
	{
		var type='checkForUniqueDesignationName';
		var url = "<%=request.getContextPath()%>/commons/Process.jsp?type="+type+"&designationName="+designationName+"&designationId="+designationId;
		var req2 = initiateRequest();
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200)
		{
			var result=req2.responseText;
			result = result.split("/");
			if(result[0]!= null && result[0]!= "")
			 {
				a=result[0];
			}
		}
		return a;
	
	}
	
	function checkForUnique()
	{
		var designationId="<%= designationId%>";
		var designationName= document.desigForm.designationName.value;
		var checking=checkForUniqueDesignationName(designationName,designationId);
		if(checking=='true')
		{
			alert("Designation Name already exists");
			document.desigForm.designationName.focus();
			return false;
		}
		
	}

	var b="";
	function checkForUniquePositionName(positionName,positionId)
	{
		var type='checkForUniquePositionName';
		var url = "<c:url value='/commons/Process.jsp?type="+type+"&positionName="+positionName+"&positionId="+positionId+"'/> ";
	
		var req2 = initiateRequest();
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200)
		{
			var result=req2.responseText;
			result = result.split("/");
			if(result[0]!= null && result[0]!= "")
			 {
				b=result[0];
			}
		}
	
		return b;
	
	}
	function addRow(obj, tableName, row) {
    	var tbl=tableName;
    	var rowO=tbl.rows.length;
	
		if(document.desigForm.sanctionedPostsDesig.value < rowO) {
			alert("Cannot Add row more than Sanctioned Posts");
			document.desigForm.sanctionedPostsDesig.focus();
			return false;
		}
	
    	var name=obj.value;
        var tname = "resetRowValues" + name;
        
       	if (row != null) {
       		var tbody=tbl.tBodies[0];
       		rIndex = 0;
   			var lastRow = tbl.rows.length;
   			var rowObj = row.cloneNode(true);   			
   			var id = 'codescontainer' + lastRow;
   			rowObj.cells[4].childNodes[1].setAttribute("id",id);   			   			   			   			   			   		
   			rIndex = rowObj.rowIndex;      			
   			tbody.appendChild(rowObj);
   			resetRowValues(lastRow, name);
		} else {
			alert("row Object is null");
			return false;
		}
	}
	
	function checkAlphaNumeric(obj)
	{
	    if(obj.value!="")
	    {
	    var num=obj.value;
	    var objRegExp  = /^[0-9]+$/;
	        if(!objRegExp.test(num))
	        {
	        alert('<bean:message key="alertEnterValNme"/>');
	        obj.value="";
	        obj.focus();
	        }
	    }
	}
	
	function resetRowValues(lastRow,name)
	{
		var table=document.getElementById('TPTable');
		getControlInBranch(table.rows[table.rows.length-1],'positionName').value="";
		getControlInBranch(table.rows[table.rows.length-1],'effectiveDate').value="";
		getControlInBranch(table.rows[table.rows.length-1],'outsourcedPosts').value="";
		getControlInBranch(table.rows[table.rows.length-1],'tpId').value=0;
		getControlInBranch(table.rows[table.rows.length-1],'tpIdName').value="";
		getControlInBranch(table.rows[table.rows.length-1],'drawingOfficer').value="";
		getControlInBranch(table.rows[table.rows.length-1],'billNumbers').value = "";
	}
	
	var firstLength = 0;
	var rIndex = 0;
	
	function setLength()
	{
	 	var tbl=document.getElementById('TPTable');
	   	var rowo=tbl.rows.length;
	  	firstLength = rowo;
	  	rowLength = eval(rowo-1);
	}
  
  	function checkForEmployeePositionId(obj,table)
    {
  		var ispositionAssigned='false';
  		var tpIdIndex=getRow(obj).rowIndex;
  		var tbl=table;
  		if( getControlInBranch(tbl.rows[tpIdIndex],'tpId')!=null &&	getControlInBranch(tbl.rows[tpIdIndex],'tpId').value!=0)
		{
 			var  posId	=getControlInBranch(tbl.rows[tpIdIndex],'tpId').value;
  			var type='checkForEmployeePositionId';
		  	var url = "<%=request.getContextPath()%>/commons/Process.jsp?type="+type+"&posId="+posId;
		  	var req2 = initiateRequest();
		  	req2.open("GET", url, false);
		  	req2.send(null);
		  	if (req2.status == 200)
		  	{
		  		var result=req2.responseText;
		  		result = result.split("/");
		  		if(result[0]!= null && result[0]!= "")
		  		{
			  		 if(result[0]=='true')
			  		 {
			  		 	alert("This position is already assigned to employee");
			  		 	ispositionAssigned='true';
						return false;
			  		 }
		  			//ispositionAssigned=result[0];
		  		}
		  	}
  		}
  		return ispositionAssigned;
	}
	
  	function deleteRow(obj,tableName,addButtion)
  	{
      	var tbl=tableName;
      	var rowLength=tbl.rows.length;
      	var objRowIndex=getRow(obj).rowIndex;
      	var isPosAssigned=false;
      	isPosAssigned= checkForEmployeePositionId(obj,tableName);
		if(rowLength>2)
		{
			if(isPosAssigned=='false')
			{
				<%
				if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ) 
				{
				%>
					populateDeleteSet("delPositions" , getControlInBranch(tbl.rows[objRowIndex],'tpId').value);
				<%
				}
				%>
				
				tbl.deleteRow(objRowIndex);
			}
		}
		else
		{
			alert("Last Row Cannot be deleted");
			return false;
		}

  	}
  	
  	function populateDeleteSet(setName, delId)
	{
		var http = initiateRequest();   
		var url = "${pageContext.request.contextPath}/leave/updateDelSets.jsp?type="+setName+"&id="+delId;		      
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
  	
	function checkPost(obj)
	{
		checkAlphaNumeric(obj);
		var row=getRow(obj);
		var out = eval(obj.value);
		var san =document.desigForm.sanctionedPostsDesig.value;
		var sanValue = eval(san.value);
		if(sanValue<out)
		{
			alert("sanctioned Posts should be greater than outSourced Posts")
			obj.value = "";
			san.value = "";
			return false;
		}
	}
	function checkPostDsig(obj)
	{
	
		checkAlphaNumeric(obj);
		var row=getRow(obj);
	
		var out = eval(obj.value);
		var san = getControlInBranch(row,"sanctionedPostsDesig");
		var sanValue = eval(san.value);
		if(sanValue<out)
		{
			alert("sanctioned Posts should be greater than outSourced Posts")
			obj.value = "";
			san.value = "";
			return false;
	
		}
	}
	
	var outLenLength = 0;
	function populateOutLen(obj)
	{
		outLenLength = obj.value;
	}

	function checkNoPost(obj)
	{
		checkAlphaNumeric(obj);
		var val =obj.value;
		if(val!="")
		{
			if(val!=1)
			{
				alert('Posts have to be one');
				obj.value = "";
			}
		}
	}
	
	function checkNoOutPost(obj)
	{
		checkAlphaNumeric(obj);
		var val = eval(obj.value);
		if(val!="")
		{
			if(val!=1)
			{
				alert('Posts have to be either one or zero');
				obj.value = "";
			}
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
				if(document.desigForm.designationName.value == "" )
				{
					alert("Pls Fill in the Designation Name");
					document.desigForm.designationName.focus();
					return false;
				}
				if(document.desigForm.designationDescription.value == "" )
				{
					alert("Pls Fill in the Designation Description");
					document.desigForm.designationDescription.focus();
					return false;
				}
				if(document.desigForm.sanctionedPostsDesig.value == "" )
				{
					alert("Pls Fill in the Sanctioned Posts");
					document.desigForm.sanctionedPostsDesig.focus();
					return false;
			
				}
				if(document.desigForm.outsourcedPostsDesig.value == "" )
				{
					alert("Pls Fill in the Outsourced Posts");
					document.desigForm.outsourcedPostsDesig.focus();
					return false;
				}
	
				var tbl= document.getElementById("TPTable");
				var rows= tbl.rows.length;
				var rowneeded = eval(tbl.rows.length - 1);
				if(rowneeded<document.desigForm.sanctionedPostsDesig.value || rowneeded>document.desigForm.sanctionedPostsDesig.value)
				{
					alert("Number of positions do not match the Sanctioned Posts");
					return false;
				}
	
				var outPosts = 0;
		
				if(rows>2)
				{
					for(var i=0 ;i<rows-1;i++)
					{
						if(getControlInBranch(tbl.rows[i+1],'outsourcedPosts').value=="1" )
						{
							outPosts = outPosts+1;
						}
					}
				}
				else
				{
					outPosts = document.desigForm.outsourcedPosts.value;
				}
	
				if(outPosts<outLenLength || outPosts>outLenLength)
				{
					alert("Number of positions do not match the Outsourced Posts");
					return false;
				}
			
				if(rows>2)
				{
					for(var i=0 ;i<rows-1;i++)
					{
						var posObj = getControlInBranch(tbl.rows[i+1],'positionName');
						var effDtObj = getControlInBranch(tbl.rows[i+1],'effectiveDate');
						var outSourObj = getControlInBranch(tbl.rows[i+1],'outsourcedPosts');
						var billNumObj = getControlInBranch(tbl.rows[i+1],'billNumbers');

						if(posObj.value=="" )
						{
							alert("Please fill positionName");
							posObj.focus();
							return false;
						}
						if(effDtObj.value=="" )
						{
							alert("Please fill efferctive Date");
							effDtObj.focus();
							return false;
						}
						
						if(outSourObj.value=="" )
						{
							alert("Please fill outsourcedPosts");
							outSourObj.focus();
							return false;
						}
						if(billNumObj.value=="" )
						{
							alert("Please fill Bill Number");
							billNumObj.focus();
							return false;
						}
					}
				}
				else
				{
					if(document.desigForm.positionName.value=="" )
					{
						alert("Please fill positionName");
						document.desigForm.positionName.focus();
						return false;
					}
					if(document.desigForm.effectiveDate.value=="" )
					{
						alert("Please fill effective Date");
						document.desigForm.effectiveDate.focus();
						return false;
					}
					
					if(document.desigForm.outsourcedPosts.value=="" )
					{
						alert("Please fill outsourcedPosts");
						document.desigForm.outsourcedPosts.focus();
						return false;
					}
					if(document.forms[0].billNumbers.value=="" )
					{
						alert("Please fill Bill Number");
						document.forms[0].billNumbers.focus();
						return false;
					}
		         }
	
				submitType="modifyDetails";
		<%
			}
			else if(mode1.equalsIgnoreCase("create"))
			{
		%>
				if(document.desigForm.designationName.value == "" )
				{
					alert("Please Fill in the Designation Name");
					document.desigForm.designationName.focus();
					return false;
				}
				if(document.desigForm.designationDescription.value == "" )
				{
					alert("Please Fill in the Designation Description");
					document.desigForm.designationDescription.focus();
					return false;
				}
		
				if(document.desigForm.sanctionedPostsDesig.value == "" )
				{
					alert("Please Fill in the Sanctioned Posts");
					document.desigForm.sanctionedPostsDesig.focus();
					return false;
			
				}
				if(document.desigForm.outsourcedPostsDesig.value == "" )
				{
					alert("Please Fill in the Outsourced Posts");
					document.desigForm.outsourcedPostsDesig.focus();
					return false;
				}
	
				var tbl= document.getElementById("TPTable");
				var rows= tbl.rows.length;
				var rowneeded = eval(tbl.rows.length - 1);
				
				
				if(rowneeded<rowLength)
				{
					alert("Number of positions do not match the sanctioned posts");
					return false;
		
				}
				if(rowneeded>rowLength)
				{
					alert("Number of positions do not match the sanctioned posts");
					return false;
		
				}
				var outPosts = 0;
		
				if(rows>2)
				{
					for(var i=0 ;i<rows-1;i++)
					{
						if(getControlInBranch(tbl.rows[i+1],'outsourcedPosts').value=="1" )
						{
							outPosts = outPosts+1;
						}
					}
				}
				else
				{
					outPosts = document.desigForm.outsourcedPosts.value;
				}
		
				if(outPosts<outLenLength)
				{
					alert("Number of positions do not match the Outsourced Posts");
					return false;
				}
				
				if(outPosts>outLenLength)
				{
					alert("Number of positions do not match the Outsourced Posts");
					return false;
		
				}
				if(rows>2)
				{
					for(var i=0 ;i<rows-1;i++)
					{
						var posObj = getControlInBranch(tbl.rows[i+1],'positionName');
						var effDtObj = getControlInBranch(tbl.rows[i+1],'effectiveDate');
						var outSourObj = getControlInBranch(tbl.rows[i+1],'outsourcedPosts');
						var billNumObj = getControlInBranch(tbl.rows[i+1],'billNumbers');
						if(posObj.value=="" )
						{
							alert("Please fill positionName");
							posObj.focus();
							return false;
						}
						if(effDtObj.value=="" )
						{
							alert("Please fill effective Date");
							effDtObj.focus();
							return false;
						}
						
						if(outSourObj.value=="" )
						{
							alert("Please fill outsourcedPosts");
							outSourObj.focus();
							return false;
						}
						if(billNumObj.value=="" )
						{
							alert("Please fill Bill Number");
							billNumObj.focus();
							return false;
						}
					}
				}
				else
				{
					if(document.desigForm.positionName.value=="" )
					{
						alert("Please fill positionName");
						document.desigForm.positionName.focus();
						return false;
					}
					if(document.desigForm.effectiveDate.value=="" )
					{
						alert("Please fill effective Date");
						document.desigForm.effectiveDate.focus();
						return false;
					}
					
					if(document.desigForm.outsourcedPosts.value=="" )
					{
						alert("Please fill outsourcedPosts");
						document.desigForm.outsourcedPosts.focus();
						return false;
					}
					if(document.forms[0].billNumbers.value=="" )
					{
						alert("Please fill Bill Number");
						document.forms[0].billNumbers.focus();
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

			document.desigForm.action = "${pageContext.request.contextPath}/commons/AfterDesignationMasterAction.do?submitType="+submitType;
			document.desigForm.submit();
		}
		if(arg == "close")
		{
			window.close();
		}
	
	}

	function onClickCancel()
	{
		window.location="<%=request.getContextPath()%>/pims/DesignationMasterCreate.jsp";
	}

	function checkMode()
	{
		var target="<%=(request.getAttribute("alertMessage"))%>";
	
		if(target!="null")
		{
			alert("<%=request.getAttribute("alertMessage")%>");
			document.desigForm.designationName.value="";
			document.desigForm.designationDescription.value="";
			document.desigForm.outsourcedPosts.value="";

		}
	}
		
	function checkUnique(obj)
	{
		var rowObj=getRow(obj);
		var table= document.getElementById("TPTable");
	
		if(getControlInBranch(table.rows[rowObj.rowIndex],'tpId').value=="0")
		{
				var checking=checkForUniquePositionName(getControlInBranch(table.rows[rowObj.rowIndex],'positionName').value,getControlInBranch(table.rows[rowObj.rowIndex],'tpId').value);
				if(checking=='true')
				{
					alert("Position Name already exists");
					obj.focus();
					return false;
				}
		}
		else if(getControlInBranch(table.rows[rowObj.rowIndex],'tpId').value!="0"
		&& getControlInBranch(table.rows[rowObj.rowIndex],'tpIdName').value!= getControlInBranch(table.rows[rowObj.rowIndex],'positionName').value)
		{
			var checking=checkForUniquePositionName(getControlInBranch(table.rows[rowObj.rowIndex],'positionName').value,getControlInBranch(table.rows[rowObj.rowIndex],'tpId').value);
			if(checking=='true')
			{
				alert("Position Name already exists");
				obj.focus();
				return false;
			}
		}
		var tbl= document.getElementById('TPTable');
		var rCount=tbl.rows.length-1;
		for(j=1;j<=rCount;j++)
		{
			for( k=1;k<=rCount;k++)
			{
			if(j != k && getControlInBranch(tbl.rows[j],'positionName').value != ""
				&& getControlInBranch(tbl.rows[k],'positionName').value != ""
				&& (getControlInBranch(tbl.rows[j],'positionName').value == getControlInBranch(tbl.rows[k],'positionName').value))
				{
					alert(" Same Position Name exists in more than one row ");
					getControlInBranch(tbl.rows[k],'positionName').focus();
					return false;
				}
			}
		}
	}
	
	function disableFields()
	{
		<%
		   if(((String)(session.getAttribute("viewMode"))).trim().equals("view"))
		   {
	   	%>
	   			var tbl = document.getElementById("TPTable");
				var len = tbl.rows.length;
	
				//Disable header portions
				document.forms[0].designationName.disabled=true;
				document.forms[0].designationDescription.disabled=true;
				document.forms[0].sanctionedPostsDesig.disabled=true;
				document.forms[0].outsourcedPostsDesig.disabled=true;
				
				//Disable detail portions
				if (len == 2) {
					document.forms[0].positionName.disabled = true;
					document.forms[0].effectiveDate.disabled = true;
					document.forms[0].outsourcedPosts.disabled = true;
					document.forms[0].drawingOfficer.disabled = true;
					document.forms[0].billNumbers.disabled = true;
				} else {
					for (var i = 0; i < len; i++) {
						
						document.forms[0].positionName[i].disabled = true;
						document.forms[0].effectiveDate[i].disabled = true;
						document.forms[0].outsourcedPosts[i].disabled = true;
						document.forms[0].drawingOfficer[i].disabled = true;
						document.forms[0].billNumbers[i].disabled = true;
					}
				}
	   	<%
	   		}
	   	%>
	}
	
	function CheckForDuplicateDO(obj,posId) {
		var value = obj.value;
		if("" != value){		
			var rowObj = getRow(obj);
			var selectedRow = rowObj.rowIndex;
			var tbl = document.getElementById("TPTable");
			var len = tbl.rows.length;
			//alert("table len--"+len);
			//alert("selectd index--"+selectedRow);
			if(len > 2){
				for(var i=0; i< len-1; i++){
					if(i != selectedRow-1){
						if(value == document.forms[0].drawingOfficer[i].value){
							alert("Duplicate selection of Drawing officer!!!");
							obj.value = "";
							obj.focus();
							return false;
						}
					}	
				}				
			}		
			var url = "${pageContext.request.contextPath}/commons/BeforeDesignationMasterAction.do?submitType=checkForDuplicateDO&doId="+value+"&posId="+posId;
			var callback = {success:function (oResponse) {
				var responseData = oResponse.responseText;
				//alert(responseData);
				if("false" == responseData){
					alert("This Drawing officer code already assigned to position!!!. Please select some other drawing officer code ");
					obj.value = "";
					obj.focus();
					return false;
				}
			}, failure:function (oResponse) {
				alert("Internal server error occurred !!!");
			}, timeout:30000, cache:false};
			YAHOO.util.Connect.asyncRequest("GET", url, callback);
		}
		return true;
	}
	
	var yuiflag1 = new Array();
	var allBillNumbers;
	var oAutoCompBillNum;
	
	function loadBillNumbers() { 
		var type='getAllBillNumbers';
		var url = "${pageContext.request.contextPath}/pims/employeeGradeAjax.jsp?type="+type;
		var req2 = initiateRequest();
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200) {
			var billNums = req2.responseText;
			var billNo = billNums.split("^");
			var a = billNo[0].split("+");
			allBillNumbers = new YAHOO.widget.DS_JSArray(a);		
		}
	}


	//Used for Auto Complete
	function autocompleteForBillNumber(obj,event) {
	// set position of dropdown
		var src = obj;
		var currRow = getRow(obj);
		var target;
		var body;
       var target = document.getElementById('codescontainer');
       var autocomp = document.createElement("div");
       var targetId = 'codescontainer' + currRow.rowIndex;
       autocomp.setAttribute("id", targetId);
       target.appendChild(autocomp);
		
		
		var posSrc = findPos(src);
		target.style.left = posSrc[0];	
		target.style.top = posSrc[1] + 20;
		if (obj.name == 'billNumbers') {
			target.style.left = posSrc[0] + 0;
		}
		
		target.style.width = 146;
		
		var currRow = getRow(obj);
		var coaCodeObj = obj;
		
		if(yuiflag1[currRow.rowIndex] == undefined) {	
			//40 --> Down arrow, 38 --> Up arrow
			if(event.keyCode != 40 ) {		
				if(event.keyCode != 38 ) {					
					//alert('target.id: ' + targetId);
					oAutoCompBillNum = new YAHOO.widget.AutoComplete(coaCodeObj, targetId, allBillNumbers);
					oAutoCompBillNum.queryDelay = 0;
					oAutoCompBillNum.useShadow = true;
					oAutoCompBillNum.maxResultsDisplayed = 15;						
				}		
			}
		}
	}

	function fillBillNumberAfterSplit(obj, neibrObjName) {	
		var currRow = getRow(obj);
		yuiflag1[currRow.rowIndex] = undefined;
		neibrObj = getControlInBranch(currRow, neibrObjName);
		var temp = obj.value;	
		temp = temp.split("`-`");
		
		if(temp[1] != '' && temp[1] != undefined) {
			neibrObj.value = temp[1];
		}	
		obj.value = temp[0];			
	}
	

</SCRIPT>
</head><!-- Header Section Begins -->

<!-- Header Section Ends -->

<body onload="checkMode();setLength();populateLength();disableFields(); loadBillNumbers();" >
<div id="container">
	<div id="codescontainer" style="text-align:left; width: 120px;" ></div>
</div>
<html:form  action="/commons/AfterDesignationMasterAction" >

   	<div class="navibarshadowwk"></div>
				<div class="formmainbox">
					<div class="insidecontent">
				  		<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">	
								    <span class="bold">Today:</span> <egovtags:now/>
								</div>	
								
								<table width ="100%" cellspacing="0" border="0" cellpadding ="0">
									
									<tr>
										<td  class="headingwk" colspan="7">
											<div class="arrowiconwk"><img src="<%=request.getContextPath()%>/common/image/arrow.gif" /></div>
											<div class="headplacer">Designation Information</div>
										</td>
									</tr>
									<tr>
										<td>
										<%
										
								
											String id = (String)session.getAttribute("Id");
								
								   			if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("modify") ||  ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("view"))
								   			{
											%>
												<input type="hidden" name="designationId" id="designationId" value = "<%=id.trim()%>" />
											<%
											}
											%>
											<table  cellpadding ="0" cellspacing ="0" border = "0" name ="pisTable" id ="pisTable" width="100%" >
								  
									    		<tr>
													<td class="whiteboxwk"  ><SPAN class="mandatory">*</SPAN>Designation Name</td>
											       <%
											       if( ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("create") )
											       {
											       %>
											
											       	<td class="whitebox2wk" >
												   		<html:text property="designationName" styleId="designationName" onblur="uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp',  'EG_DESIGNATION', 'DESIGNATION_NAME', 'designationName', 'yes', 'yes')" />
												  	</td>
											       <%
											       }
											       else
											       {
											       %>
											       	<td class="whitebox2wk" >
												   		<html:text property="designationName" styleId="designationName"  value = "<%=designationMaster.getDesignationName()==null?"":designationMaster.getDesignationName()%>" onblur="return checkForUnique();"/>
												   	</td>
											
											       <%
											       }
											       %>
										       		<td class="whiteboxwk"><SPAN class="mandatory">*</SPAN>Designation Description</td>
										         	<td class="whitebox2wk"><html:text property="designationDescription" styleId="designationDescription" value="<%=designationMaster.getDesignationDescription()==null?"":designationMaster.getDesignationDescription().toString()%>"/></td>
									   			</tr>
									     		
												<tr>
													<td class="greyboxwk"><SPAN class="mandatory">*</SPAN>Sanctioned Posts</td>
													<td class="greybox2wk" >
														<html:text property="sanctionedPostsDesig" styleId="sanctionedPostsDesig" value = "<%=designationMaster.getSanctionedPosts()==null?
														"":designationMaster.getSanctionedPosts().toString()%>" onblur = "populateRowLength(this)"/>
													</td>
													<td class="greyboxwk" >
														<SPAN class="mandatory">*</SPAN>Outsourced Posts&nbsp;</td>
													<td class="greybox2wk" >
														<html:text property="outsourcedPostsDesig" styleId="outsourcedPostsDesig" value = "<%=designationMaster.getOutsourcedPosts()==null?"":designationMaster.getOutsourcedPosts().toString()%>" onblur = "checkPostDsig(this);populateOutLen(this)" />
													</td>
												</tr>
											</table>
								
								   			<br>
								
											   <table width="100%"  cellpadding ="0" cellspacing ="0" border = "0"   id="TPTable" name="TPTable" >
											     
											     <tbody>
												     
												     <tr>
													     <td class="greybox2wk" ><SPAN class="mandatory">*</SPAN>PositionName</td>
													     <td class="greybox2wk" ><SPAN class="mandatory">*</SPAN>Effective Date </td>     
													     <td class="greybox2wk" ><SPAN class="mandatory">*</SPAN>Outsourced Posts</td>
													     <td class="greybox2wk" >Drawing officer code</td>
													     <td class="greybox2wk" ><SPAN class="mandatory">*</SPAN>Bill Number</td>
													     <td class="greybox2wk" />
												     </tr>
									
													   <%
													   	 if(positionSet.isEmpty()){
															positionSet.add(new Position());
														 }
													   	 
													   	 if(positionSet != null && !positionSet.isEmpty())
													   	 {
													   		Iterator itr1 = positionSet.iterator();
													  		for(int i=0;itr1.hasNext();i++)
													  		{
													  			Position position = (Position)itr1.next();
													   %>
															 	<tr id="TPRow">																	
																	<input type = "hidden" name="tpId" id="tpId" value="<%=position.getId()==null?"0":position.getId().toString()%>" />
																		<input type = "hidden" name="tpIdName" id="tpIdName" value="<%=position.getName()==null?"":position.getName()%>" />
																		<c:set var="posId" value="<%=position.getId()==null?"":position.getId().toString()%>" />
																	<td class="whitebox2wk">
																		<input   type="text"  name="positionName" id="positionName" value="<%=position.getName()==null?"":position.getName()%>"  onblur="checkUnique(this)" >
																	</td>
																    <td class="whitebox2wk">
																		<input   type="text" name="effectiveDate" id="effectiveDate"  onblur="validateDateFormat(this);" value="<%=position.getEfferctiveDate()==null?"":sdf.format(position.getEfferctiveDate())%>" onfocus="javascript:vDateType='3';"   onkeyup="DateFormat(this,this.value,event,false,'3')">
																	</td>
																  	
																  	<td class="whitebox2wk">
																		<input   type="text"  name="outsourcedPosts" id="outsourcedPosts"  value="<%=position.getOutsourcedPosts()==null?"":position.getOutsourcedPosts().toString()%>" onblur = "checkPost(this);checkNoOutPost(this)">
																	</td>																	
																	<td class="whitebox2wk">
																		<html:select style="width:150px" property="drawingOfficer" value="<%=position.getDrawingOfficer()==null?"":position.getDrawingOfficer().getEntityId().toString()%>" onchange="return CheckForDuplicateDO(this,'${posId}');">
																			<html:option value="">-------------Choose-------------</html:option>
																			<c:forEach var="doObj" items="${drawingOffList}">
																				<html:option value="${doObj.id}">${doObj.code}</html:option>
																			</c:forEach>
																		</html:select>
																	</td>
																	<td class="whitebox2wk">																		
																		<input  type="hidden" name="billNumberId" id="billNumberId"  />
																		<input id="billNumbers"
																			class="selectwk yui-ac-input" type="text"
																			onblur="fillBillNumberAfterSplit(this,'billNumberId');"
																			onfocus="autocompleteForBillNumber(this,event);"
																			onkeyup="autocompleteForBillNumber(this,event);"
																			style="width: 120px;" autocomplete="off" size="20"
																			name="billNumbers"																			
																			value="<%=(position.getBillNumber() == null) ? "" : position.getBillNumber().getBillNumber()%>">
																	</td>
												
															 	<%
																     if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
																     {
														  		%>
														  				<td class="whitebox2wk" >
																			<div align="center">
																				<a href="#"><img src="../common/image/add.png" alt="Add" width="16" height="16" border="0"
																			      onclick="javascript:addRow(this,document.getElementById('TPTable'),document.getElementById('TPRow'));"/></a>
																				<a href="#"><img src="../common/image/cancel.png" alt="Del" width="16" height="16" border="0"
																				onclick="javascript:deleteRow(this,document.getElementById('TPTable'));"/></a>
																			</div>
																		</td>
														  
																	<%
																	}
																	else
																	{
																	%>
																		<td class="whitebox2wk">&nbsp;</td>
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
										</td>
									</tr>
									
									<tr>
								    	<td><div align="right" class="mandatory">* Mandatory Fields</div></td>
								    </tr>
								</table>
  							</div>
						</div>
					</div>
				</div>
	
				<div class="buttonholderwk">
					<%
						if(!((String)(session.getAttribute("viewMode"))).trim().equals("view"))
						{
						%>
						<html:button styleClass="buttonfinal" value="Save" property="b4" onclick="ButtonPress('savenew')" />
						<%
						}
					 %>
					<input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close()" />
				</div>
  		</html:form>
 	</body>
</html>
