<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>Create Department</title>
		<script>
			function bodyonload() {
				document.getElementById('deptName').value="";
				document.getElementById('deptCode').value="";
				document.getElementById('deptDetails').value="";
				document.getElementById('deptAddress').value="";
			}

			function validation() {
				if(document.getElementById('deptName').value=="") {
					alert("Please enter a Department Name.");
					return false;
				}
				if(!isNaN(document.deptForm.deptName.value)) {
					alert("Department Name cannot be a numeric value.");
					document.getElementById('deptName').value="";
					document.getElementById('deptName').focus();
					return false;
				}
				result = eval(uniqueCheckingBoolean('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp' , 'EG_DEPARTMENT', 'DEPT_NAME', 'deptName', 'no', 'no'));
   				if(result == false) {
   					alert("Department Name already exists");
   					document.getElementById('deptName').value="";
   					document.getElementById('deptName').focus();
   					return false;
				}
			}

			function trimText1(obj,value) {
	    		value = value;
	    		if(value!=undefined) {
		   			while (value.charAt(value.length-1) == " ") {
						value = value.substring(0,value.length-1);
		   			}
		   			while(value.substring(0,1) ==" "){
						value = value.substring(1,value.length);
		   			}
		   			obj.value = value;
				}
	   			return value ;
			}
		</script>
	</head>
	<body onload="bodyonload()" >
		<html:form action="Department?bool=CREATE" onsubmit="return validation()">
			<table align="center" class="tableStyle">
				<tr>
					<td class="tableheader" align="middle" width="728" height="6">Create Department</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center"  class="tableStyle">
				<tr>
					<td  class="labelcell" width="40%" height="23" >Department Name<font class="ErrorText">*</font></td>
					<td  class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptName"  styleId="deptName" styleClass="ControlText" onchange="return trimText1(this,this.value);" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Code</td>
					<td  class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptCode" styleId="deptCode" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Details</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<html:text property="deptDetails" styleId="deptDetails" styleClass="ControlText"  />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Department Address</td>
					<td  class="labelcell" align="left" width="40%" height="23" >
						<html:textarea rows="5" cols="20" styleId="deptAddress" styleClass="ControlText"  property="deptAddress" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  width="40%" height="23" >Is Billing Location</td>
					<td class="labelcell" align="left" width="40%" height="23" >
						<input type="checkbox" name="billingLocation" id="billingLocation" value="on">
					</td>
				</tr>
			</table>
			<br/><br/>
			<table align="center" class="tableStyle">
				<tr>
					<td align="middle" width="728">
						<html:submit value="  Save  " />&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" value="  Close  "  onclick="javascript:window.close();"/>
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>