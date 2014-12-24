<%@page import="org.egov.lib.admbndry.ejb.server.BoundaryServiceImpl"%>
<%@page import="java.util.*,java.text.*, org.egov.infstr.utils.*,
	  org.egov.lib.admbndry.ejb.api.*,
	  org.egov.lib.admbndry.*" %>
<%@ include file="/includes/taglibs.jsp" %>
<%
String mandatoryFields = "boundaryNum";
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
BoundaryService bm =	new BoundaryServiceImpl();

String BoundaryID = request.getParameter("bndryId");
Boundary bndry=null;
if(BoundaryID!=null) {
	bndry = bm.getBoundary(Integer.parseInt(BoundaryID));
}
int bndryid = Integer.parseInt(BoundaryID);
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
		<html:javascript formName="boundryForm"/>
		<script type="text/javascript" src="/egi/script/calendar.js" type="text/javascript" ></script>
		
		<script>
		
			function validateDateFormat(obj) {
				var dtStr = obj.value;
				var year;
				var day;
				var month;
				var leap = 0;
				var valid = true;
				var oth_valid = true;
				var feb = false;
				var validDate = true;
				if (dtStr != "" && dtStr != null) {
					year = dtStr.substr(6, 4);
					month = dtStr.substr(3, 2);
					day = dtStr.substr(0, 2);
					if (year == "0000" || year < 1900 || month == "00" || day == "00" || dtStr.length < 10 || dtStr.length > 10) {
						validDate = false;
					}
					if (validDate == true) {
						leap = year % 4;
						if (leap == 0) {
							if (month == "2") {
								valid = false;
								feb = true;
							} else {
								if (month == "02") {
									if (day > 29) {
										valid = false;
									}
									feb = true;
								}
							}
						} else {
							if (month == "2") {
								valid = false;
								feb = true;
							} else {
								if (month == "02") {
									if (day > 28) {
										valid = false;
									}
									feb = true;
								}
							}
						}
						if (feb == false) {
							if (month == "03" || month == "01" || month == "05" || month == "07" || month == "08" || month == "10" || month == "12") {
								if (day > 31) {
									oth_valid = false;
								}
							} else {
								if (month == "04" || month == "06" || month == "09" || month == "11") {
									if (day > 30) {
										oth_valid = false;
									}
								} else {
									oth_valid = false;
								}
							}
						}
					}
				}
				if (valid == false || oth_valid == false || validDate == false) {
					alert("Please enter the valid date in the dd/MM/yyyy Format only");
					obj.value = "";
					obj.focus();
				}
			    //validateDate123(obj.value)
				return;
			}
			
			var a = "false";
			function checkUniqueChildBoundaryName(boundaryId, bndryName) {
				var type = "checkUniqueChildBoundaryName";
				var url = "<c:url value='/commons/Process.jsp?type=" + type + "&boundaryId=" + boundaryId + "&bndryName=" + bndryName + "'/> ";
				var req2 = initiateRequest();
				req2.open("GET", url, false);
				req2.send(null);
				if (req2.status == 200) {
					var result = req2.responseText;
					result = result.split("/");
					if (result[0] != null && result[0] != "") {
						a = result[0];
					}
				}
				return a;
			}

			function checkUnique() {
				var boundaryId="<%= bndryid%>";
				var bndryName = document.getElementById("name").value;
				var checking = eval(checkUniqueChildBoundaryName(boundaryId, bndryName));
				if (checking == true) {
					document.getElementById("uniqueBndry").style.display = "block";
					return false;
				} else {					
					document.getElementById("uniqueBndry").style.display = "none";
					return true;
					
				}
			}
			function checkUnique(arg) {
				var boundaryId="<%= bndryid%>";
				var bndryName = document.getElementById("name").value;
				var checking = eval(checkUniqueChildBoundaryName(boundaryId, bndryName));
				if (checking == true && arg != document.getElementById("name").value) {
					document.getElementById("uniqueBndry").style.display = "block";
					return false;
				} else {
					document.getElementById("uniqueBndry").style.display = "none";
					return true;
				}
			}
			var b = "";
			function checkUniqueChildBoundaryNum(boundaryId, bndryNum) {
				var type = "checkUniqueChildBoundaryNum";
				var url = "<c:url value='/commons/Process.jsp?type=" + type + "&boundaryId=" + boundaryId + "&bndryNum=" + bndryNum + "'/> ";
				var req2 = initiateRequest();
				req2.open("GET", url, false);
				req2.send(null);
				if (req2.status == 200) {
					var result = req2.responseText;
					result = result.split("/");
					if (result[0] != null && result[0] != "") {
						b = result[0];
					}
				}
				return b;
			}
			function checkUniqueforBndryNum() {
				var boundaryId="<%= bndryid%>";
				var bndryNum = document.getElementById("boundaryNum").value;
				var checking = eval(checkUniqueChildBoundaryNum(boundaryId, bndryNum));
				if (checking == true) {
					document.getElementById("uniqueBndryNum").style.display = "block";
					return false;
				} else {
					document.getElementById("uniqueBndryNum").style.display = "none";
					return true;
				}
			}
			function checkUniqueforBndryNum(arg) {
				var boundaryId="<%= bndryid%>";
				var bndryName = document.getElementById("boundaryNum").value;
				var checking = eval(checkUniqueChildBoundaryNum(boundaryId, bndryName));
				if (checking == true && arg != document.getElementById("name").value) {
					document.getElementById("uniqueBndryNum").style.display = "block";
					return false;
				} else {
					document.getElementById("uniqueBndryNum").style.display = "none";
					return true;
				}
			}
			function validate() {
			
				if(document.getElementById('name').value == "" ) {
					alert ("Please enter the Boundary Name");
					return false;
				}
				<%
				if(targetBoundaryName==null) {%>
					if(checkUnique()==false) {
					return false;
				}
				<%} else if(targetBoundaryName!=null) {%>
				if(checkUnique("<%=targetBoundaryName%>")==false) {
					return false;
				}
				<%} %>
				
				if(document.getElementById("boundaryNum").value == "") {
				<%if("boundaryNum".equals(mandatoryFields)) {%>
					alert("Please enter the Boundary Number");
					return false;	
				<%}%>
				}
				if(document.getElementById("boundaryNum").value!="" && document.getElementById("boundaryNum").value==0 ){
					alert("Please enter a Boundary Number grater than Zero");
					return false;
				}
				
				<%if(targetBoundaryNum==null) {%>
				if(checkUniqueforBndryNum()==false) {
					return false;
				}
				<%} else if(targetBoundaryNum!=null) {%>
				if(checkUniqueforBndryNum("<%=targetBoundaryNum%>")==false) {
					return false;
				}<%}%>
				
				if (document.getElementById("fromDate").value == "") {
					alert("Please enter From Date");
					document.getElementById("fromDate").focus();
					return false;
				}
				if (document.getElementById("fromDate").value != "" && document.getElementById("toDate").value != "") {
					rTF = checkFdateTdate(document.getElementById("fromDate").value, document.getElementById("toDate").value);
					if (rTF == false) {
						alert("From Date should be less than or equal to To Date");
						document.getElementById("fromDate").value = "";
						return false;
					}
				}
				document.forms[0].submit();
				return true;
			}
	</script>
	</head>
	<body>
		<center>
		<html:form  action="/Boundry">
		<table align="center"   width="400">
		<% if(operation.equals("create")) { %>
			<tr>
				<td  class="tableheader"  width="100%" colspan="2" height="23">
					<p align="center">
					<% if(parentBoundary==null || parentBoundary.trim().equalsIgnoreCase("null")) { %>
					<b>Create Boundary</b>
					<% } else { %>
					<b>Create Boundary under <%=parentBoundary%></b>
					<% }%>
					</p>
				</td>
			</tr>
			<tr>
				<td class="labelcell" >
					<p align="left" >Boundary Name<font class="ErrorText">*</font></p>
				</td>
				<td class="labelcell" align="left" width="50%" height="31">
					<input type="text" name="name" id="name" onblur="return checkUnique();trim(this,this.value);">
				</td>
			</tr>
			<tr>
				<td class="errorcell" colspan="2">
					<span style="display:none" id="uniqueBndry"><b><font color="red">&nbsp;&nbsp;&nbsp;&nbsp;Error : Boundary Name Already Exists!!!</font></b></span>
				</td>
			</tr>
			<tr>
				<td class="labelcell" >
					<p align="left" >Boundary Number
					<%if("boundaryNum".equals(mandatoryFields))
					{%>
						<font class="ErrorText">*</font>
	
					<%}%>
					</p>
				</td>
				<td  class="labelcell" align="left" width="50%" height="31">
					<input type="text" name="boundaryNum" id="boundaryNum" onblur="checkUniqueforBndryNum();">
				</td>
			</tr>
			<tr>
				<td class="errorcell" colspan="2">
					<span style="display:none" id="uniqueBndryNum"><b><font color="red">&nbsp;&nbsp;&nbsp;Error : Boundary Number Already Exists!!!</font></b></span>
				</td>
			</tr>
			<tr>
				<td class="labelcellforsingletd">
					<p align="left">From Date(dd/mm/yyyy)<font class="ErrorText">*</font></p>
				</td>
				<td class="labelcell" align="left" width="50%" height="31">
					<input type="text" name="fromDate" id="fromDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
				</td>
			</tr>
			<tr>
				<td class="labelcellforsingletd">
					<p align="left">To Date(dd/mm/yyyy)</p>
				</td>
				<td class="labelcell" align="left" width="50%" height="31">
					<input type="text" name="toDate" id="toDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="hidden" name="parentBoundaryNum" value="<%=pbnum%>">
					<input type="hidden" name="heirarchyType" value="<%=heirarchyType%>">
					<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
					<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">
				</td>
			</tr>
			<tr bgColor="#dddddd">
				<td  valign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="left"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font></p>
				</td>
			</tr>
		<% } else if(operation.equals("delete")){%>

			<tr>
				<td class="labelcellforsingletd" valign="center" align="middle" width="100%" colSpan="2" height="26">
					<p align="center">
					<% if(parentBoundaryName!=null && !parentBoundaryName.trim().equalsIgnoreCase("null")) { %>				<
						<b>Are you sure you want to Delete   <font class="ErrorText" size = "3"><%=targetBoundaryName%></font>  Boundary under <font class="ErrorText" size = "3"><%=parentBoundaryName%></font></b>
					<% } else { %>
						<b>Are you sure you want to Delete   --- <font class="ErrorText" size = "3"><%=targetBoundaryName%></font> ---  Boundary
					<%}%>
					</p>
					<input type="hidden" name="name" value="<%=targetBoundaryName%>">
					<input type="hidden" name="boundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>">
					<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>">
					<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
					<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">
					<input type="hidden" name="bndryId" value="<%=BoundaryID%>">
					<input type="hidden" name="heirarchyType" value="<%=heirarchyType%>">
				</td>
			</tr>
			<%} else if(operation.equals("edit")){%>
			<tr>
				<td class="tableheader" vAlign="middle" align="center" width="100%" colSpan="2" height="56">
					<p align="center">
						<b>Change the properties of <%=targetBoundaryName%></b>
					</p>
				</td>
			</tr>
			<tr>
				<td class="labelcell" width="50%" height="76">
					<p align="left">
						Change Boundary Name<font class="ErrorText">*</font>
					</p>
				</td>
				<td class="labelcell" align="left" width="50%">
					<input type="text" name="name" id="name" value="<%=targetBoundaryName%>" onblur="return checkUnique("<%= targetBoundaryName%>");trim(this,this.value);">
				</td>
			</tr>
			<tr>
				<td class="errorcell">
					<span style="display:none" id="uniqueBndry"><b><font color="red">&nbsp;&nbsp;&nbsp;Error : Boundary Name Already Exists!!!</font></b></span>
				</td>
			</tr>
			<tr>
				<td  class="labelcell" width="50%" height="76">
				<p align="left">Change Boundary Number 
					<%if("boundaryNum".equals(mandatoryFields))
						{%>
							<font class="ErrorText">*</font>

					<%}%>
				</p>
				</td>
				<td class="labelcell"  align="left" width="50%">
					<input type="text" name="boundaryNum" id="boundaryNum" value="<%=targetBoundaryNum%>" onblur="checkUniqueforBndryNum("<%=targetBoundaryNum%>");">
				</td>
			</tr>			
			<tr>
				<td class="errorcell" colspan="2">
					<span style="display:none" id="uniqueBndryNum"><b><font color="red">&nbsp;&nbsp;&nbsp;Error : Boundary Number Already Exists!!!</font></b></span>
				</td>
			</tr>
			<tr>
				<td class="labelcellforsingletd">
					<p align="left">
						From Date(dd/mm/yyyy)<font class="ErrorText">*</font>
					</p>
				</td>
				<td  class="labelcell" align="left" width="50%" height="76">
					<%if(bndry.getFromDate()!=null)	{%>
					<input type="text" name="fromDate" id="fromDate" value="<%=formatter.format(bndry.getFromDate())%>" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
					<%}else{%>
					<input type="text" name="fromDate" id="fromDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
					<%}%>
				</td>
			 </tr>
			 <tr>
				<td class="labelcellforsingletd">
					<p align="left">
					To Date(dd/mm/yyyy)
					</p>
				</td>
				<td class="labelcell" align="left" width="50%" height="76">
				 <%if(bndry.getToDate()!=null){%>
				 	<input type="text" name="toDate" id="toDate" value="<%=formatter.format(bndry.getToDate())%>" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
				 <%} else{%>
				 	<input type="text" name="toDate" id="toDate" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);">
				<%}%>
				</td>
			</tr>
			<tr bgColor="#dddddd">
				<td class="labelcellforsingletd" valign="bottom" align="left" width="100%" colSpan="2" height="20">
					<p align="center"><font class="ErrorText"><bean:message key="note"/><bean:message key="note.ll"/></font></p>
					<input type="hidden" name="parentBoundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>">
					<input type="hidden" name="TargetBoundaryNum" value="<%=targetBoundaryNum==null?"":targetBoundaryNum%>">
					<input type="hidden" name="topLevelBoundaryID" value="<%=topLevelBoundaryID%>">
					<input type="hidden" name="bndryTypeHeirarchyLevel" value="<%=bndryTypeHeirarchyLevel%>">
					<input type="hidden" name="bndryId" value="<%=BoundaryID%>">
					<input type="hidden" name="heirarchyType" value="<%=heirarchyType%>">
				</td>
			</tr>
		<%	} %>

			<tr bgColor="#dddddd">
				<td class="button2" vAlign="bottom" align="center" width="100%" colSpan="2" height="23">
					<input type="button" value="Save" onclick="validate()" />
				</td>
			</tr>
			
			</table>
		</html:form>
		</center>
	</body>
</html>
