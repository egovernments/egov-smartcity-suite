<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

	<head>
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		<title>Change Password</title>
		<script>
		function checkPasswordStrength(pwdObj) {
			if (pwdObj.value == '') {
				return true;
			}
			var ajaxRequest = initiateRequest();
			ajaxRequest.onreadystatechange = function () {
				if (ajaxRequest.readyState == 4) {
					if (ajaxRequest.status == 200) {
						var response = ajaxRequest.responseText;
						if (response.indexOf("true") != -1) {
							document.getElementById('strengthInfo').innerHTML = "<font color='green'>Password valid.</font>";
						} else {
							document.getElementById('strengthInfo').innerHTML = "<font color='red'>Password invalid.</font>";
						}
					}
				}
			}; 
			ajaxRequest.open("GET", "/egi/admin/checkPasswordStrength.do?"+"AJAX=true&pwd="+pwdObj.value, true);
			ajaxRequest.send(null);
		}
		</script>
	</head>
	<body onload="javascript:document.getElementById('strong').checked=false">
		<html:form action="/admin/chgPassword">
			<table align="center"  width="500px">
				<tr>
					<td class="tableheader" align="center" width="80%" height="15" colspan=4>
						Change Password
					</td>
				</tr>
				<tr>
					<td colspan=4>&nbsp;</td>
				</tr>
				<tr>
					<td class="labelcell"  height="23"><bean:message key="OldPwd"/><font class="ErrorText">*</font></td>
					<td class="labelcell" align="left" height="23" >
						<html:password size="36" property="oldPwd" maxlength="30" styleClass="ControlText" />
					</td>
				</tr>
				<tr>
					<td class="labelcell"  height="23"><bean:message key="NewPwd"/><font class="ErrorText">*</font></td>
					<td class="labelcell" align="left"  height="23" >
						<html:password size="36" property="pwd" styleId="pwd" maxlength="30" styleClass="ControlText" onblur="checkPasswordStrength(this)"/><div id="strengthInfo" style="font-size:10px"></div>
					</td>
				</tr>
				<tr>
					<td class="labelcell"  height="23"><bean:message key="ConfirmPwd"/><font class="ErrorText">*</font></td>
					<td class="labelcell" align="left" height="23" >
					<html:password size="36" property="pwdReminder" maxlength="30" styleClass="ControlText" /></td>
				</tr>
				<tr>
					<td colspan="4" style="color:red"><html:errors /></td>
				</tr>
				<tr height="50px">
					<td colspan="4"></td>
				</tr>
				<tr>
					<td class="button2" align="center" colspan=4>
					<input type=submit name="submit" value="Submit" />&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" value="Close" onclick="javascript:window.close()"/>
					</td>			
				</tr>
				<tr><td colspan="2"><div><span style="color:red;font-size:10px" id="pwdInfo">Password must be at least 8 to 30 characters long and must have one or more :- upper case and lower case alphabet,number and special character except [<font color='black'>&amp; &lt; &gt; # % " ' / \ and space</font>]</span></div></td></tr>
			</table>
		</html:form>
	</body>
</html>