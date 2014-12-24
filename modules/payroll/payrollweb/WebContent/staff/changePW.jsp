<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import= "org.egov.lib.rjbac.dept.ejb.api.*,
		org.egov.lib.rjbac.dept.*,
		org.egov.lib.admbndry.ejb.api.*,
		org.egov.lib.admbndry.*,
		org.egov.infstr.utils.*,
 		java.sql.Date,
 		java.text.SimpleDateFormat,
 		java.util.*,
 		javax.naming.*,
 		java.text.ParsePosition"
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ include file="/staff/egovHeader.jsp" %>
<%!

Integer userId = null;
String userName= null;
%>

<%
	//String uName=(String) session.getAttribute("com.egov.
        //Object obj1 = session.getAttribute("com.egov.user.LoginUserId");
        if(session.getAttribute("com.egov.user.LoginUserId")!=null && session.getAttribute("com.egov.user.LoginUserId")!="")
        {
        	userId = (Integer) session.getAttribute("com.egov.user.LoginUserId");
        	//request.setParameter("userId",userId);
        }

        if(session.getAttribute("com.egov.user.LoginUserName") != null)
        {
        	userName= (String)session.getAttribute("com.egov.user.LoginUserName") ;
        }

%>

<html>
<head>
<title>Index Page</title>
		<meta http-equiv="Content-Type" content="text/html; charset=Cp1252"/>
		<title>WelCome to eGov Property Tax System</title>

		<LINK REL=stylesheet HREF="/egov.css" TYPE="text/css">
		<LINK REL=stylesheet HREF="/ccMenu.css" TYPE="text/css">

 <!--[if gte IE 5.5]>
 <script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
 <![endif]-->
<script>

function checkBeforeSubmit()
{
	//alert("PWD="+document.DynaChangePWForm.pwd.value+ document.DynaChangePWForm.pwd.value.length);
	//window.location = '/staff/ChangePassWord.do?userId=<%=userId%>';//&receiptNumber='+recNo;

	if(document.DynaChangePWForm.oldPwd.value=="")
	{
		alert("Please enter the Old Password !!");
		document.DynaChangePWForm.oldPwd.focus();
		return false;
	}

	if(document.DynaChangePWForm.pwd.value=="")
	{
		alert("Please enter the new Password !!");
		document.DynaChangePWForm.pwdReminder.value="";
		document.DynaChangePWForm.pwd.focus();		
		return false;
	}
	
	if(document.DynaChangePWForm.pwdReminder.value=="")
	{
		alert("Please Re-Type the new Password !!");
		document.DynaChangePWForm.pwdReminder.focus();
		return false;
	}
	
	if(document.DynaChangePWForm.pwd.value!="" && document.DynaChangePWForm.pwdReminder.value!="")
	{
		//alert(document.DynaChangePWForm.pwd.value!=document.DynaChangePWForm.pwdReminder.value);
		if(document.DynaChangePWForm.pwd.value.length<6)
		{
			alert("Enter atleast 6 characters!!");
			document.DynaChangePWForm.pwd.value="";
			document.DynaChangePWForm.pwdReminder.value="";
			document.DynaChangePWForm.pwd.focus();
			return false;			
		
		}
		
		if(document.DynaChangePWForm.pwd.value!=document.DynaChangePWForm.pwdReminder.value)
		{
			alert("Mismatch in re-entering the password...enter the password again!!");
			document.DynaChangePWForm.pwd.value="";
			document.DynaChangePWForm.pwdReminder.value="";
			document.DynaChangePWForm.pwd.focus();
			return false;			
		}
	}
	else
	{
		return false;
	}

	document.DynaChangePWForm.action="/staff/ChangePassWord.do?userId=<%=userId%>";
	document.DynaChangePWForm.submit(); 

}
function setFocus()
{
	document.DynaChangePWForm.oldPwd.focus();
}
function checkPassWord(obj)
{
   passWordLen = obj.value.length;
   if(passWordLen<6)
   {
     alert("Please Enter At Least 6 characters for Password");
   }
}

</script>



</head>

<body onload="setFocus()">
   <!-- Header Section Begins -->
   <html:form method="POST" action="/staff/ChangePassWord" >
   <!-- Header Section Ends -->

   <table align="center" >
   	<tr>
   	  <td><BR>
		<!-- Tab Navigation Begins -->
		<jsp:include page="/staff/tabmenu.html" />
		<!-- Tab Navigation Ends -->
	
		<!-- Body Begins -->
  		  <div id="main"><div id="m2"><div id="m3"><!-- m1 top border,m2 bottom,m3 side-->
		  <!--img  align=absmiddle src="img/c_top.gif"-->
 		 
 		<div align="center">
 		<table width="70%" colspan="5" style="border: 1px solid #D7E5F2">

			<% if(request.getAttribute("alertMessage")!=null)
			   {%>			   	
        		 	<tr  height="20">
	 		 		<td  class="tablesubcaption"  colspan="5">
	 				<p align="center"> <font color="red"><b> <%=request.getAttribute("alertMessage")%> </b></font></p></td>
				</tr>			   	
			   	
			   <%}%>
        		 <tr  height="20">
	 		 	<td  class="tablesubcaption"  colspan="5">
	 			<p align="center">  Change PassWord Form</p></td>
			</tr>

			<tr>
				<td  class="labelcell6" colspan="1">
					User Name
				</td>
			
				<td class="fieldcell" colspan="4">
					<font face="Times New Roman"><b><%=userName%></b></font>
				</td>
			</tr>

			<tr>
				<td  class="labelcell6" >
					Old Password
				</td>
			
				<td class="fieldcell" colspan="4">
					<input type="password" name="oldPwd" size="20" maxlength="10" />
				</td>
			</tr>

			<tr>
				<td  class="labelcell6" >
					New Password
				</td>
			
				<td class="fieldcell"  colspan="4">
					<input type="password" name="pwd" size="20" maxlength="10" onblur="return checkPassWord(this);"/>
				</td>
			</tr>
			<tr>
				<td  class="labelcell6" style="width:200">
					Re-Type New PassWord
				</td>
			
				<td class="fieldcell" colspan="4">
					<input type="password" name="pwdReminder" size="20" maxlength="10"/>
				</td>
			</tr>
	


 <tr>
     <td class="labelcell6" colspan="5" style="text-align:'right'; font-weight:'bold'; width:'600px';">
		<p style="text-align: center"><input class="button" type="button" name="Submit" value="Submit" onclick="return checkBeforeSubmit();"/></td>

     </tr>
     <table width="70%" colspan="5" style="border: 1px solid #D7E5F2">
     <tr colspan="6" >
     <td class="labelcellmedium" >
     <font color="red"><p style="text-align: left">
     Note :
     <ul>
     <li>Password should be between 6-10 characters long</li>
     <li>Please use a mixture of number and characters for your pasword</li>
     <li>Set your password in such a way that it shouldnt be easily replicable and neither too hard that you forget it.</li></p>
     <font>
     </td>
     </table>
<tr>
	</table>
		</div>

<!--img align=absmiddle src="img/c_bot.gif"-->
</div></div></div>
</div>
<!-- Body Section Ends -->
</body>
</td></tr>
</table>

</html:form>
</html>
