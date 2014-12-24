<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryTypeServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.api.BoundaryTypeService"%>
<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl"%>
<%@page import="org.egov.lib.admbndry.ejb.api.BoundaryService"%>
<%@page
	import="java.util.ArrayList,
		java.text.SimpleDateFormat, 
	  	org.egov.infstr.utils.EgovMasterDataCaching,
	  	org.egov.infstr.utils.EGovConfig,
	  	org.egov.lib.admbndry.Boundary,
	  	org.egov.lib.admbndry.BoundaryType"%>
<%@ include file="/includes/taglibs.jsp"%>

<%
String mandatoryFields = "boundaryNum";
ArrayList hierarchyTypeList = (ArrayList)EgovMasterDataCaching.getInstance().get("egi-hierarchyType");

SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

BoundaryService bm =	new BoundaryServiceImpl();
BoundaryTypeService bmType =	new BoundaryTypeServiceImpl();

String BoundaryID = request.getParameter("bndryId");
Boundary bndry=null;
BoundaryType bndryType=null;

if(BoundaryID!=null) {
	bndry = bm.getBoundary(Integer.parseInt(BoundaryID));
}
String boundaryType = request.getParameter("boundaryType");
int bndryid = Integer.parseInt(boundaryType);

bndryType = bmType.getBoundaryType(new Integer(boundaryType));
String operation = request.getParameter("operation");
String heirarchyType = request.getParameter("heirarchyType");
String parentBoundary = request.getParameter("parent");
String pbnum = request.getParameter("parentBndryNum");
String topLevelBoundaryID =  request.getParameter("topLevelBoundaryID");
String bndryTypeHeirarchyLevel = request.getParameter("BndryTypeHeirarchyLevel");
String targetBoundaryNum = request.getParameter("TargetBoundaryNum");
String targetBoundaryName = request.getParameter("TargetBoundaryName");
String parentBoundaryName = request.getParameter("parentBoundaryName");

if(operation == null)
	operation = "";
session.setAttribute("operation",operation);

%>
<html>
	<head>
		<title><%=operation.replace(operation.charAt(0),Character.toTitleCase(operation.charAt(0))) %> Boundary</title>
		<link rel="stylesheet" type="text/css" href="/egi/css/egov.css" />
		<html:javascript formName="boundryForm" />
		<script type="text/javascript" src="/egi/script/calendar.js"></script>
	<script type="text/javascript">	
	
	function checkDuplicate() {
		
		if(document.getElementById("cityBaseURL").value == "") {
			alert("Please enter City Base URL");
			return false;
		} 
		var allRows = document.getElementsByName('cityBaseURL'); 
		for (var i=0;i<allRows.length;i++) {
			if (allRows[i].value == "") {
				alert("Please enter City Base URL");
				return false;
			} else {
				for (var  j = allRows.length-1 ; j > 0 ; j--) {
					if (j == i) {
						break;
					} else if (allRows[i].value == allRows[j].value) {
						alert("City Base URL can not be duplicated");
						return false;
					}
				}
			}
		}
		return true;
	}
		
	function addURL() {	
		
		if (checkDuplicate()) {
			var tbl     = document.getElementById('cityType');
			var tbody   = tbl.tBodies[0];
			var lastRow = tbl.rows.length;
			var rowObj   = document.getElementById('reasonrow').cloneNode(true);
			var x = 0;
			while (true) {
				if(rowObj.cells[0].childNodes[x].type == 'text' ) {
					rowObj.cells[0].childNodes[x].value = "";
					break;
				}
				x = x+1;
			}		
			tbody.appendChild(rowObj);	
		}					
	}	
	
	function checkFdateTdate(fromDate,toDate) {
		//ENTERED DATE FORMAT MM/DD/YYYY
		if(fromDate.substr(6,4) > toDate.substr(6,4)) {
			return false;
		} else if(fromDate.substr(6,4) == toDate.substr(6,4)) {
			if(fromDate.substr(3,2) > toDate.substr(3,2)) {
				return false;
			} else if(fromDate.substr(3,2) == toDate.substr(3,2)) {
				if(fromDate.substr(0,2) > toDate.substr(0,2)) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	function trimText1(obj,value) {
		
		value = value;
		if(value!=undefined) {
		   	while (value.charAt(value.length-1) == " ") {
				value = value.substring(0,value.length-1);
		   	}
		   	while(value.substring(0,1) == " ") {
				value = value.substring(1,value.length);
		   	}
		   	obj.value = value;
		}
   		return value ;
	}
	
	function bodyonLoad() {
		if (document.getElementById('cityBaseURL'))
			document.getElementById('cityBaseURL').value="";
		if (document.getElementById('cityNameLocal'))
			document.getElementById('cityNameLocal').value="";
	}
	
	function validateDateFormat(obj) {
	 	
	 	var dtStr=obj.value;
		 var year;
		 var day;
		 var month;
		 var leap=0;
		 var valid=true;
		 var oth_valid=true;
		 var feb=false;
		 var validDate=true;
	
	     if(dtStr!="" && dtStr!=null) {
	    	year=dtStr.substr(6,4);
	    	month=dtStr.substr(3,2);
	    	day=dtStr.substr(0,2);
	
	    	if (year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length<10 || dtStr.length>10) {
	    		validDate=false;
	    	}
	
	    	if (validDate) {
	    		leap=year%4;			
	 			if (leap == 0) {
	    			if (month=="2") {
	    				valid=false;
	    				feb=true;
	    			} else if(month=="02") {
	    				if(day>29) {
	    					valid=false;
	    				}
	    				feb=true;
	    			}
	   			} else {
	    			if(month=="2") {
	    				valid=false;
	    				feb=true;
	    			} else if(month=="02"){
	    				if(day>28){
	    					valid=false;
	    				}
	    				feb=true;
	    			}
				}
	
	    		if(!feb) {
	    			if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12") {
	    				if(day>31) {
	    					oth_valid=false;
	    				}
	    			} else if(month=="04" || month=="06" || month=="09" || month=="11") {
	    				if(day>30) {
	    					oth_valid=false;
	    				}
	    			} else {
	    				oth_valid=false;
	    			}
	    		}
	    	}
	    }
	
	    if(!valid || !oth_valid || !validDate) {
	    	alert("Please enter the valid date in the dd/MM/yyyy Format only");
	    	obj.value="";
	    	obj.focus();
	    }
	}
	
	var isUniqueBoundaryName = "";
	function checkUniqueForBoundaryName(boundaryId,bndryName) {
		var type='checkUniqueForBoundaryName';
		var url = "<c:url value='/commons/Process.jsp?type="+type+"&boundaryId="+boundaryId+"&bndryName="+bndryName+"'/> ";
		var req2 = initiateRequest();
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200) {
			var result=req2.responseText;
			result = result.split("/");
			if(result[0]!= null && result[0]!= "") {
				isUniqueBoundaryName = result[0];
			}
		}
		
		return isUniqueBoundaryName;			
	}
	
	function checkUnique() {
		var boundaryId = "<%= bndryid%>";
		var bndryName = document.getElementById('name').value;	
		var checking  = checkUniqueForBoundaryName(boundaryId,bndryName);	
		if(checking.indexOf("false") == -1 && boundaryName != bndryName){
			document.getElementById('uniqueBndry').style.display = "block";
			return false;
		} else {
			document.getElementById('uniqueBndry').style.display = "none";
			return true;
		}	
	}			
	
	function checkUnique(arg) {
		var boundaryId="<%= bndryid%>";
		var bndryName = document.getElementById('name').value;			
		var checking = checkUniqueForBoundaryName(boundaryId,bndryName);
		if(checking.indexOf("false") == -1 && arg!= document.getElementById('name').value) {
			document.getElementById('uniqueBndry').style.display = "block";
			return false;
		} else {
			document.getElementById('uniqueBndry').style.display = "none";
			return true;
		}
		
	
	}
	
	var isUniqueBoundaryNumber = "";
	function checkUniqueForBoundaryNum(boundaryId,bndryNum) {
		var type = 'checkUniqueForBoundaryNum';
		var url = "<c:url value='/commons/Process.jsp?type="+type+"&boundaryId="+boundaryId+"&bndryNum="+bndryNum+"'/> ";
		var req2 = initiateRequest();
		req2.open("GET", url, false);
		req2.send(null);
		if (req2.status == 200) {
			var result=req2.responseText;
			result = result.split("/");
			if(result[0]!= null && result[0]!= "") {
				isUniqueBoundaryNumber = result[0];
			}
		}
	
		return isUniqueBoundaryNumber;			
	}
	
	function checkUniqueforBndryNum() {
		var boundaryId="<%= bndryid%>";
		var bndryNum= document.getElementById('boundaryNum').value;			
		var checking=checkUniqueForBoundaryNum(boundaryId,bndryNum);
		if(checking.indexOf("false") == -1) {
			document.getElementById('uniqueBndryNum').style.display = "block";
			return false;
		} else {
			document.getElementById('uniqueBndryNum').style.display = "none";
			return true;
		}
	}
	
	function validate() {
		if(document.getElementById("isActiveValue") && document.getElementById("isActiveValue").checked) {
			document.getElementById('isActive').value = "1";
		} else {
			document.getElementById('isActive').value = "0";
		}	
		
		if(document.getElementById('name').value == ""){
			alert("Please enter the Boundary Name");
			return false;		
		}
		
		if(isNaN(document.getElementById('boundaryNum').value) || document.getElementById('boundaryNum').value == 0 ){	
			alert("Please enter a Boundary Number greater than Zero");
			return false;
		}
						
		if(document.getElementById('fromDate').value=="") {
		   alert("Please enter a From Date");
		   document.getElementById('fromDate').focus();
		   return false;
		}

		if(document.getElementById('fromDate').value != "" && document.getElementById('toDate').value != "") {
			rTF = checkFdateTdate(document.getElementById('fromDate').value,document.getElementById('toDate').value);
			if(!rTF) {
				alert('From Date should be less than or equal to To Date');
				document.getElementById('fromDate').value = "";
				return false;
			}
		}			
		
		if(document.getElementById('cityLogo') && document.getElementById('cityLogo').value == ""){
			alert("Please enter a City Logo name");
			return false;
		}
		
		if(document.getElementById('cityBaseURL') && document.getElementById('cityBaseURL').value == "") {
			alert("Please enter a City Base URL");
			return false;
		}	
		
		<%if(targetBoundaryName == null){%>
			if(checkUnique() == false) {
				return false;
			}
		<%} else {%>
			if(checkUnique("<%=targetBoundaryName%>") == false) {
				return false;
			}
		<%}%>
		  
		 if(document.getElementById('boundaryNum').value == ""){
			<%if("boundaryNum".equals(mandatoryFields))	{%>
				alert("Please enter the Boundary Number");
				return false;			
			<%}%>
		}
		<%if(targetBoundaryNum == null) {%>
			if(checkUniqueforBndryNum() == false ) {
				return false;
			}
		<%}	%>
		
		document.forms[0].submit();
	}			
	</script>		
	</head>

	<body bgcolor="#FFFFFF" onload="bodyonLoad();">
			<html:form action="/Boundry" >
				<table align="center"  width="400" >
					<%
					if(operation.equals("create")){%>					
					<tr>
						<td class="tableheader" width="100%" colspan="2" height="23">
							<p align="center">
								<%			
								if(parentBoundary==null || parentBoundary.trim().equalsIgnoreCase("null")) {%>
								<b>Create Boundary</b>
								<% } else { %>								
								<b>Create Boundary under <%=parentBoundary%></b>
								<%
								}%>
							</p>
						</td>
					</tr>
					<tr height="25px"><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td class="labelcell" width="50%" height="31">
							<p align="left">Hierarchy Type</p>
						</td>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="text" name="hierarchyTypeid" id="hierarchyTypeid" value="<%= bndryType.getHeirarchyType().getName()%>" readonly />
						</td>
					</tr>
					<tr>
						<td class="labelcell" width="50%" height="31">
							<p align="left">Boundary Type</p>
						</td>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="text" value="<%= bndryType.getName()%>" readonly />
							<input type="hidden" name="boundaryId" id="boundaryId" value="<%= bndryid%>">
						</td>
					</tr>
					<tr>
						<td class="labelcell">
							<p align="left">Boundary Name<font class="ErrorText">*</font></p>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="text" name="name" id="name" onblur="return checkUnique();trim(this,this.value);">
						</td>
					</tr>
					<tr>
						<td class="errorcell" colspan="2">
							<span style="display :none" id="uniqueBndry"><b><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Error : Boundary Name already exists!!!</font></b></span>
						</td>
					</tr>
					<tr>
						<td class="labelcell">
							<p align="left">Boundary Name Local</p>
						</td>
						<td class="labelcell" align="left" width="50%" height="31">
							<html:text property="cityNameLocal" styleId="cityNameLocal"/>
						</td>
					</tr>

					<tr>
						<td class="labelcell">
							<p align="left">Boundary Number<%if("boundaryNum".equals(mandatoryFields)){%> <font class="ErrorText">*</font> <%}%></p>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="text" name="boundaryNum" id="boundaryNum" onblur="return checkUniqueforBndryNum();trim(this,this.value);">
						</td>
					</tr>


					<tr>
						<td class="errorcell" colspan="2">
							<span style="display :none" id="uniqueBndryNum"><b><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Error : Boundary Number already exists!!!</font></b></span>
						</td>
					</tr>
					<tr>
						<td class="labelcellforsingletd">
							<p align="left">From Date(dd/mm/yyyy)<font class="ErrorText">*</font></p>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="text" name="fromDate" id="fromDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
						</td>
					</tr>
					<tr>
						<td class="labelcellforsingletd">
							<p align="left">To Date(dd/mm/yyyy)</p>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="text" name="toDate" id="toDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
						</td>
					</tr>
					<tr>
						<td class="labelcell">
							<p align="left">City Logo<font class="ErrorText">*</font></p>
						</td>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="text" name="cityLogo" id="cityLogo" value="">
						</td>
					</tr>
					<tr>
						<td class="labelcell">
							<p align="left">Is Active</p>
						</td>
						<td class="labelcell" align="left" width="50%" height="31">
							<input type="checkbox" name="isActiveValue" checked="checked" id="isActiveValue" value="ON">
							<input type="hidden" name="isActive" id="isActive">
						</td>
					</tr>	
					<tr>
						<td colspan="2">
							<input type="hidden" name="parentBoundaryNum" value="<%=pbnum%>" />
							<input type="hidden" name="heirarchyType" value="<%=heirarchyType%>" />
							<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>" />
							<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>" />
						</td>
					</tr>				
					<tr>
						<td class="labelcell" valign="middle">
							<p align="left">City Base URL<font class="ErrorText">*</font></p>
						</td>
						<td width="50%">
							<table id="cityType" width="100%">
								<tr id="reasonrow">
									<td class="labelcell" align="right" width="100%" height="31">
										<html:text property="cityBaseURL" styleId="cityBaseURL" onchange="return trimText1(this,this.value);" size="40" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="labelcellforsingletd" colspan="2">
							<p align="center">
								<input type=button value="Add More URL" onClick="addURL();" />
								<font size="1"><br>(eg:</font> <font size="1"color="blue"> www.mysorecity.gov.in or ip192:168:24:1)</font>
							</p>
						</td>
					</tr>
				<% }
				else if(operation.equals("delete")) {%>

					<tr>
						<td class="labelcellforsingletd" width="100%" colSpan="2" height="26">
							<p align="center">	<%
							if(parentBoundaryName!=null && !parentBoundaryName.trim().equalsIgnoreCase("null")) {
							%>
								<b>Are you sure you want to Delete <font class="ErrorText" size="3"><%=targetBoundaryName%></font> Boundary under <font class="ErrorText" size="3"><%=parentBoundaryName%></font>
								</b>
							<% }
							else {
							%>
								<b>Are you sure you want to Delete --- <font	class="ErrorText" size="3"><%=targetBoundaryName%></font> --- Boundary </b><%
							}
							%>							
							</p>
							<input type="hidden" name="name" value="<%=targetBoundaryName%>">
						</td>
					</tr>
					<tr>
						<td>
							<input type="hidden" name="boundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>" />
							<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>" />
							<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>" />
							<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>" />
							<input type="hidden" name="bndryId" value="<%=BoundaryID%>" />
							<input type="hidden" name="heirarchyType" value="<%=heirarchyType%>" />
						</td>
					</tr>
					<%
					} else if(operation.equals("edit")) {
					%>
					<tr>
						<td class="labelcell" vAlign="middle" align="center" width="100%" 		colSpan="2" height="56">
							<p align="center">
								<b>Change the properties of <%=targetBoundaryName%></b>
							</p>
						</td>
					</tr>

					<tr>
						<td class="labelcell" width="50%" height="31">
							<p align="left">Change Boundary Name <font class="ErrorText">*</font>&nbsp;&nbsp;</p>
						<td class="labelcell" align="left" width="50%">
							<input type="text" name="name" id="name" value="<%= targetBoundaryName%>" onblur="return checkUnique('<%= targetBoundaryName%>');trimText1(this,this.value);">
						</td>
					</tr>
					<tr>
						<td class="errorcell">
							<span style="visibility: hidden" id="uniqueBndry"><b><font	color="red">&nbsp;&nbsp;&nbsp;&nbsp;Error : Boundary Name already exists!!!</font>
							</b>
							</span>
						</td>
					</tr>
					<tr>
						<td class="labelcell" width="50%" height="31">
							<p align="left">
								Change Boundary Number
								<%if("boundaryNum".equals(mandatoryFields))
								{%>
								<font class="ErrorText">*</font>
								<%}%>
							</p>
						<td class="labelcell" align="left" width="50%">
							<input type="text" name="boundaryNum" id="boundaryNum" value="<%=targetBoundaryNum%>" onblur="uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_BOUNDARY', 'BNDRY_NUM', 'boundaryNum', 'no', 'no');">
						</td>
					</tr>
					<tr>
						<td class="labelcellforsingletd">
							<p align="left">
								From Date(dd/mm/yyyy)<font class="ErrorText">*</font>
								
							</p>
						<td align="left" width="50%" height="31">
							<%if(bndry.getFromDate()!=null)	{%>
							<input type="text" name="fromDate" id="fromDate"	value="<%=formatter.format(bndry.getFromDate())%>" 	onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" 	onblur="return validateDateFormat(this);">
							<%}
							else
							{%>
							<input type="text" name="fromDate" id="fromDate"	onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" 	onblur="return validateDateFormat(this);">
							<%}%>

						</td>
					</tr>
					<tr>
						<td class="labelcellforsingletd">
							<p align="right">
								To Date(dd/mm/yyyy)
							</p>
						</td>
						<td align="left" width="50%" height="31">
							<%if(bndry.getToDate()!=null)
							{%>
							<input type="text" name="toDate" id="toDate" value="<%=formatter.format(bndry.getToDate())%>" 	onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" 	onblur="return validateDateFormat(this);">
							<%}
				 			else {%>

							<input type="text" name="toDate" id="toDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');"  onblur="return validateDateFormat(this);">
							<%}%>
						</td>
					</tr>
					<tr bgColor="#dddddd">
						<td class="labelcellforsingletd" vAlign="bottom" align="left" width="100%" colSpan="2" height="20">
							<p align="center"> <font class="ErrorText"><bean:message key="note" /> 	<bean:message key="note.ll" />	</font></p>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="labelcell" width="100%"> 
							<input type="hidden" name="parentBoundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>" />
							<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>" />
							<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>" />
							<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>" />
							<input type="hidden" name="bndryId" value="<%=BoundaryID%>" />
							<input type="hidden" name="heirarchyType" value="<%=heirarchyType%>" />							
						</td>
					</tr>	
					<%	}%>
					<tr>
						<td><br/></td>
					</tr>
					<tr height="23px">
						<td  class="button2"  align="center" width="100%" height="23" colspan="2">
							<input type="button" value="Save" onclick="validate()" />
							<input type="button" value="Back" onclick = "window.location ='boundarySearch.jsp'"/>
						</td>
					</tr>					
				</table>
			</html:form>
	</body>	
</html>