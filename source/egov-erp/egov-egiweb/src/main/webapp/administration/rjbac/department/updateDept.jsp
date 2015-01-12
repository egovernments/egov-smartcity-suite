<%@page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%
	Integer bl = null;
	Integer deptid=(Integer) session.getAttribute("deptid");

	if(request.getSession().getAttribute("billinglocation")!="") {
		bl = (Integer)request.getSession().getAttribute("billinglocation");
	}
%>
<html>
	<head>
		<title>Update Department</title>
		<script>
			var check = false;
			function checkForUniqueDeptName(deptName,deptid) {
    			var url = "/egi/commons/Process.jsp?type=checkForUniqueDeptName&deptName="+deptName+"&deptid="+deptid;
				var req2 = initiateRequest();
				req2.open("GET", url, false);
				req2.send(null);
				if (req2.status == 200){
					var result=req2.responseText;
					result = result.split("/");
					if(result[0]!= null && result[0]!= "")  {
						check=eval(result[0]);
					}
				}
				return check;
			}
			
			function validation() {
				if(document.getElementById('deptName').value=="") {
					alert("Please enter a Department Name");
					return false;
				}
				if(!isNaN(document.getElementById('deptName').value)) {
					alert("Department Name cannot be a numeric value.");
					document.getElementById('deptName').value="";
					document.getElementById('deptName').focus();
					return false;
				}
				var deptName= document.getElementById('deptName').value;
				var deptid="<%=deptid%>";
				if(checkForUniqueDeptName(deptName,deptid)) {
					alert("Department Name already exists");
					document.getElementById('deptName').focus();
					return false;
				}
			}
			
			function trimText1(obj,value) {
	    		value = value;
	    		if(value!=undefined) {
		   			while (value.charAt(value.length-1) == " "){
						value = value.substring(0,value.length-1);
		   			}
		   			while(value.substring(0,1) ==" "){
						value = value.substring(1,value.length);
		   			}
		   			obj.value = value;
				}
	   			return value ;
			}

			function bodyonload() {
				var billinglocation ="<%= bl%>";
				if(billinglocation==1) {
					document.getElementById("billingLocation").checked=true;
				}
			}
		</script>
	</head>
	<body onload="bodyonload()" >
		<html:form action="Department?bool=UPDATE" onsubmit="return validation()">
			<table align="center" class="tableStyle">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">Update Department</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center"  class="tableStyle">
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Name<font class="ErrorText">*</font></td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptName"  styleId="deptName" styleClass="ControlText"  onchange="return trimText1(this,this.value);"/>
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Code</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptCode" styleId="deptCode" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Details</td>
					<td  class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptDetails" styleId="deptDetails" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Address</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:textarea rows="5" cols="20" styleId="deptAddress" styleClass="ControlText"  property="deptAddress" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Is Billing Location</td>
					<td class="labelcell"  align="left" width="40%" height="23" >
						<input type="checkbox" name="billingLocation" id="billingLocation" value="on">
					</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center" class="tableStyle" width="728">
				<tr>
					<td align="center"><html:submit value="  Save  " /></td>
				</tr>
			</table>
		</html:form>
	</body>
</html>