
<%@page import="org.egov.pims.model.EmployeeView"%><!--
	Program Name : Search Employee.jsp
	Author		: DivyaShree MS
	Created	on	: 08-02-2010
	Purpose 	: Search Employee by designation,dept,code,name,type or status
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egovtags" tagdir="/WEB-INF/tags"%>



<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*"%>



<html>
  <head>
   <title>Search Certificate Type1</title>
   <script type="text/javascript">
  
</script>
  </head>
  
  
  <body> 
  
   <s:form action="certificationTypeMaster" theme="simple" >  
   <s:token/>
   <div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
<table width="95%" cellpadding ="0" cellspacing ="0" border = "0">
	  <tbody>
<tr><td>&nbsp;</td></tr>
  <tr>
   <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
   <p><div class="headplacer">Search Certificate Type</div></p></td></td> </tr>
   </tbody>
  
  </table>
       <table width="95%" cellpadding="0" cellspacing="0" border="0">
  		
  		<s:push value="model">
  		<tr>
  		<td>
  		<%
  		    String mode=(String)request.getAttribute("modeType");
  		 %>
  		<input type="hidden" name="modeType" value="<%=mode%>" />
  		</td>
  		</tr>
  		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span>Choose Certification Master</td>
			<td class="whitebox2wk">
			<s:select name="id" id="id" list="dropdownData.EligCertTypeList" listKey="id" 
			listValue="type" headerKey="-1" headerValue="----Select----"  value="%{id}"/> </td>
			
			
		</tr>
		
  		</s:push>
  		<tr><td>&nbsp;</td></tr>
	<tr>
	
		</tr>
		</table>
							
	
  		
 		
   	</div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div>
	
	<center><s:submit method="search" value="SEARCH" cssClass="buttonfinal"/></center>
	</s:form>
	
  </body>
  
</html>

