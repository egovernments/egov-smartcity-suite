<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
Integer heirarchyTypeId = (Integer) session.getAttribute("heirarchyTypeId");
%>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Update Hierarchy</title>
		<script>	
		 var a = "";			
		function checkForUniqueHeirarchyName(name, heirarchyID) {
			var type = "checkForUniqueHeirarchyName";
			var url = "<c:url value='/commons/Process.jsp?type=" + type + "&name=" + name + "&heirarchyID=" + heirarchyID + "'/> ";
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
		function checkForUniqueHeirarchyCode(code, heirarchyID) {
			var type = "checkForUniqueHeirarchyCode";
			var url = "<c:url value='/commons/Process.jsp?type=" + type + "&code=" + code + "&heirarchyID=" + heirarchyID + "'/> ";
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
		function validation() {
			var heirarchyID = "<%= heirarchyTypeId%>";
			var name = document.getElementById("name").value;
			var code = document.getElementById("code").value;
			var checking = checkForUniqueHeirarchyName(name, heirarchyID);
			if (checking == "true") {
				alert("Hierarchy Name already exists");
				document.getElementById("name").focus();
				return false;
			}
			var checking = checkForUniqueHeirarchyCode(code, heirarchyID);
			if (checking == "true") {
				alert("Hierarchy Code already exists");
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
	<body>
		<html:form action="CreateHierarchy?bool=UPDATE" onsubmit="return validation()">

			<table align="center" width="500">
				<tr>
					<td class="tableheader" align="center"  height="23">
						Update Hierarchy
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
					<td class="labelcell">
						Hierarchy Name
					</td>
					<td class="labelcell" align="left" >
						<html:text property="name" styleId="name" styleClass="ControlText" onchange="return (trimText1(this,this.value)&& checkAlphaNumeric(this));" />
					</td>
				</tr>
				<tr>
					<td class="labelcell" >
						Hierarchy Code
					</td>
					<td class="labelcell" align="left" width="40%" height="23">
						<html:text property="code" styleId="code" styleClass="ControlText" onchange="return (trimText1(this,this.value)&& checkAlphaNumeric(this));" />
					</td>
				</tr>
			</table>
			<br/>
			<table align="center" width="500">
				<tr>
					<td class="button2"  align="center">
						<html:submit value="Save" />
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>