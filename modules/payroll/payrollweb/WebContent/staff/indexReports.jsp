<%@page import="org.egov.infstr.utils.EgovUtils,org.egov.lib.address.model.Address,
org.egov.lib.rjbac.user.User,org.egov.lib.rjbac.user.ejb.api.UserService,
org.egov.lib.rjbac.user.ejb.server.UserServiceImpl" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ include file="/staff/egovHeader.jsp" %>
<html>
<head>
<title>Reports</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>WelCome to eGov PTIS</title>
		<LINK REL=stylesheet HREF="/egov.css" TYPE="text/css">
				<LINK REL=stylesheet HREF="/ccMenu.css" TYPE="text/css">

 <!--[if gte IE 5.5]>
 <script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
 <![endif]-->

</head>

<body>


<table align='center'>
<tr>
<td>
<BR>
<!-- Tab Navigation Begins -->
<jsp:include page="/staff/tabmenu.html" />
<!-- Tab Navigation Ends -->


<!-- Body Begins -->
<div id="main"><div id="m2"><div id="m3">
<form name="signup" id="signup" method="post" action="#">
<!--img  align=absmiddle src="img/c_top.gif"-->
 <div align="center">
 <b><font size="4" color="#000080">Reports<br></font></b>
 <table align='center' width="600" height="83">




   <tr><td align="center"><b>Collection Reports</b></td></tr>

   <!--<tr>
   	                 <td class="biglabelcell" height="1"  width="100%">
   	                    <p align="left">
   	                    <font size="2" face="Verdana" color="#800000"><b><a href="/reports/BeforeAggrigateDateExecute.do">Aggregate collection reports by  date</a></b></font></p>
   	                 </td>
   </tr>
<tr><td><font color="white" >&nbsp;</font>    </td></tr>
   <tr>
   	                 <td class="biglabelcell" height="1"  width="100%">
   	                    <p align="left">
   	                    <font size="2" face="Verdana" color="#800000"><b><a href="/reports/BeforeAggrigateModeExecute.do">Aggregate collection reports by  date and collectionMode</a></b></font></p>
   	                 </td>
   </tr>
<tr><td><font color="white" >&nbsp;</font>    </td></tr>
   <tr>
      	                 <td class="biglabelcell" height="1"  width="100%">
      	                    <p align="left">
      	                    <font size="2" face="Verdana" color="#800000"><b><a href="/reports/BeforeAggrigateBndryExecute.do">Aggregate collection reports by Adminstrative Boundary </a></b></font></p>
      	                 </td>
   </tr>
<tr><td><font color="white" >&nbsp;</font>    </td></tr>
   <tr>
         	                 <td class="biglabelcell" height="1"  width="100%">
         	                    <p align="left">
         	                    <font size="2" face="Verdana" color="#800000"><b><a href="/reports/ReporteesAction.do">Aggregate collection reports by login user</a></b></font></p>
         	                 </td>
   </tr>-->
   <!--this is commented because there is no hierarchy for bill collector(access only for ARO's)-->

 <tr><td><font color="white" >&nbsp;</font>    </td></tr>
  <!--<tr>
               	                 <td class="biglabelcell" height="1"  width="100%">
               	                    <p align="left">
               	                    <font size="2" face="Verdana" color="#800000"><b><a href="/reports/BeforeCollectionRegisterDateExecute.do">Collection Register (KMF-16) by date</a></b></font></p>
               	                 </td>
      </tr>

 <tr><td><font color="white" >&nbsp;</font>    </td></tr>-->
  <tr>
               	                 <% Integer userId=(Integer)session.getAttribute("com.egov.user.LoginUserId");
				 UserServiceImpl userServiceImpl=new UserServiceImpl();
				 String roleName="";
			
	       				 User user=userServiceImpl.getUserByID(userId);
	       				 String userName = user.getUserName();
	       				 Integer userid = user.getId();
					System.out.println("USER userid : " + userid);
					System.out.println("User Name :"+ userName);

					java.util.Set setRole = user.getRoles();

					System.out.println("USER setRole : " + setRole);
					java.util.Iterator itr = setRole.iterator();
					System.out.println("USER itr : " + itr);

					while(itr.hasNext() )
					{
						org.egov.lib.rjbac.role.Role role = (org.egov.lib.rjbac.role.Role)itr.next();
						roleName = role.getRoleName();

					}
					System.out.println("roleName --> " + roleName);%>

               	                 <td class="biglabelcell" height="1"  width="100%">
               	                    <%if(roleName.equals("Collection Operator"))
               	                    {%>
               	                    <p align="left">
               	                    <font size="2" face="Verdana" color="#800000"><b><a href="/reports/collection/BeforeCollectionRegisterDateUserExecute.do"> Detailed Collection Register</a></b></font></p>
               	                    <%}
               	                    if(roleName.equals("Recovery Inspectors") || roleName.equals("AO"))
               	                    {%>
               	                    <p align="left">
               	                    <font size="2" face="Verdana" color="#800000"><b><a href="/reports/collection/BeforeCollectionRegisterDateUser.do"> Detailed Collection Register</a></b></font></p>
               	                    
               	                 </td>
      </tr>
  
 
  
         
         <tr><td><font color="white" >&nbsp;</font>    </td></tr>
         
         <tr>
	 		<td class="biglabelcell" height="1"  width="100%">
	 		<p align="left">
	 		<font size="2" face="Verdana" color="#800000"><b><a href="/reports/jasper/BeforeGenerateBill.do">Generate Bill</a></b></font></p>
	 		</td>
	</tr>
	<tr><td><font color="white" >&nbsp;</font>    </td></tr>
	<tr>
		<td class="biglabelcell" height="1"  width="100%">
		<p align="left">
		<font size="2" face="Verdana" color="#800000"><b><a href="/reports/BeforeCancelledChequeReport.do">Seat Wise Cancelled Cheques</a></b></font></p>
		</td>
	</tr>
	<%}%>
	<%if(roleName.equals("Recovery Inspectors") || roleName.equals("AO"))
   {%>
    <tr><td><font color="white" >&nbsp;</font>    </td></tr>
<tr>
	 <td class="biglabelcell" height="1"  width="100%">
		<p align="left">
		<font size="2" face="Verdana" color="#800000"><b><a href="/reports/aggrigate/BeforeCollectionRegisterDateExecute.do">Seat Wise Aggregate Collections</a></b></font></p>
	 </td>
</tr>
         <%}%>

<%if(roleName.equals("Director"))
{%>
<tr>
	 <td class="biglabelcell" height="1"  width="100%">
		<p align="left">
		<font size="2" face="Verdana" color="#800000"><b><a href="/reports/collection/BeforeCollectionRegisterDateUser.do"> Detailed Collection Register</a></b></font></p>
	 </td>
</tr>
<tr><td><font color="white" >&nbsp;</font>    </td></tr>
<tr>
	 <td class="biglabelcell" height="1"  width="100%">
		<p align="left">
		<font size="2" face="Verdana" color="#800000"><b><a href="/reports/aggrigate/BeforeCollectionRegisterDateExecute.do">Seat Wise Aggregate Collections</a></b></font></p>
	 </td>
</tr>
<tr><td><font color="white" >&nbsp;</font>    </td></tr>
<tr>
	 <td class="biglabelcell" height="1"  width="100%">
		<p align="left">
		<font size="2" face="Verdana" color="#800000"><b><a href="/reports/BeforeCancelledChequeReport.do">Seat Wise Cancelled Cheques</a></b></font></p>
	 </td>
</tr>
<tr><td><font color="white" >&nbsp;</font>    </td></tr>
<%}%>
	<tr><td><font color="white" >&nbsp;</font>    </td></tr>       

 </table>
</div>
</form>
<!--img align=absmiddle src="img/c_bot.gif"-->
</div></div></div>
</td></tr>
</table>
</body>
</html>
