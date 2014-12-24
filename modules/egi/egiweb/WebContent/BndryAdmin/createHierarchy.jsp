<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>Create Hierarchy </title>
		<script>
					
		function bodyonload() {
			document.getElementById("name").value = "";
			document.getElementById("code").value = "";
		}
		function validation() {
			var result = uniqueCheckingBoolean("${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp", "EG_HEIRARCHY_TYPE", "TYPE_NAME", "name", "no", "no");
			if (result == false) {
				alert("Heirarchy Name already exists");
				document.getElementById("name").value = "";
				document.getElementById("name").focus();
				return false;
			}
			var result1 = uniqueCheckingBoolean("${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp", "EG_HEIRARCHY_TYPE", "TYPE_CODE", "code", "no", "no");
			if (result1 == false) {
				alert("Heirarchy Code already exists");
				document.getElementById("code").value = "";
				document.getElementById("code").focus();
				return false;
			}
			if (document.getElementById("name").value == "") {
				alert("Hierarchy Name is required");
				return false;
			}
			if (document.getElementById("code").value == "") {
				alert("Hierarchy Code is required");
				return false;
			}
			if (!isNaN(document.getElementById("name").value)) {
				alert("Hierarchy Name cannot be a numeric value.");
				document.getElementById("name").value = "";
				document.getElementById("name").focus();
				return false;
			}
		}
		function trimText1(obj, value) {
			value = value;
			if (value != undefined) {
				while (value.charAt(value.length - 1) == " ") {
					value = value.substring(0, value.length - 1);
				}
				while (value.substring(0, 1) == " ") {
					value = value.substring(1, value.length);
				}
				obj.value = value;
			}
			return value;
		}
		function checkAlphaNumeric(obj) {
			var isNotAlphaNumric = "false";
			var str = obj.value;
			var len = str.length;
			var i = 0, j = 0;
			var character;
			var finalStr;
			var validchars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
			if (obj.value != null || obj.value != "") {
				for (i = 0; i < len && isNotAlphaNumric == "false"; i++) {
					if (str.charAt(0) == "" || str.charAt(0) == null) {
						str = trimAll(obj.value);
					}
					character = str.charAt(i);
					if (validchars.indexOf(str.charAt(i)) != -1) {
						j++;
					} else {
						isNotAlphaNumric = "true";
					}
				}
				if (isNotAlphaNumric == "true") {
					alert("Please enter a valid character!!");
					obj.value = str.substr(0, j);
					obj.focus();
					return false;
				}
			}
			return;
		}			
		</script>
	</head>
	<body onload="bodyonload()">
		<html:form action="CreateHierarchy?bool=CREATE" onsubmit="return validation()">
			<table align="center" width="500">
				<tr>
					<td class="tableheader" align="center"  height="23">
						Create Hierarchy
					</td>
				</tr>
				<tr>
					<td colspan=4>
						&nbsp;
					</td>
				</tr>
			</table>

			<table align="center" width="300">

				<tr>
					<td class="labelcell"  height="23">
						Hierarchy Name
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left"  height="23">
						<html:text property="name" styleId="name" styleClass="ControlText"
							onchange="return (trimText1(this,this.value)&& checkAlphaNumeric(this));" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" height="23">
						Hierarchy Code
						<font class="ErrorText">*</font>
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<html:text property="code" styleId="code" styleClass="ControlText"
							onchange="return (trimText1(this,this.value)&& checkAlphaNumeric(this));" />
					</td>
				</tr>
			</table>
			<br/>
			<table width="500" align="center">
				<tr>
					<td class="button2" align="center">
						<html:submit value="Save" />
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>