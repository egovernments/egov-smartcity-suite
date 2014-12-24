<%@page import="org.egov.pims.model.EmployeeView"%>
<!--
	Program Name : relationMasterScreen.jsp
	Author		: DivyaShree MS
	Created	on	: 16-02-2010
	Purpose 	: Master Screen for Certification Type
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egovtags" tagdir="/WEB-INF/tags"%>



<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*"%>
<style>
#warning {
  display:none;
  color:blue;
}

.mandatoryone{
color:red;
font-weight:normal;
}

.mandatoryTwo{
color:green;
font-weight:normal;
}





</style>
<html>
  <head>
   <title>Relation Master</title>
   <script type="text/javascript">
  
</script>
  </head>
  <body>
   <div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
			  
			   <span id="msg">
			<s:if test="%{hasErrors()}">
				<div class="errorstyle">
				<s:fielderror  cssClass="mandatoryone"/>
				<s:actionerror  cssClass="mandatoryone"/>
				</div>
			</s:if>
		</span>
		
  <s:form action="relationTypeMaster" theme="simple" >  
  <s:token/>
  <table width="95%" cellpadding ="0" cellspacing ="0" border = "0">
	  <tbody>
<tr><td>&nbsp;</td></tr>
  <tr>
   <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
   <p><div class="headplacer">Relation Master</div></p></td><td></td> </tr>
   </tbody>
  
  </table>
  <table width="95%" cellpadding="0" cellspacing="0" border="0">
       <s:push value="model">
       <tr><td>&nbsp;</td></tr>
       
  		
  		<tr>
			<td  colspan="4"><s:hidden name="id"/></td>
		</tr>
  		<tr>
		<td  class="whiteboxwk" align='center'><span class="mandatory">*</span>Relation Type</td>
   			 <td   class="whitebox2wk" align="center" >
				<s:textfield id="nomineeType" name="nomineeType" class="selectwk" />
		</td>
		<tr>
		<tr>
		<td  class="greyboxwk" align='center'>Narration</td>
      	<td  class="greybox2wk" align="center">
  		<s:textfield id="narration" name="narration" class="selectwk"/>
 		 </td>
		</tr>
  		
  		<tr><td>&nbsp;</td></tr>
       </s:push>
       <tr>
	
		</tr>
		<tr><td colspan=2>&nbsp;</td></tr>
					<tr>
            		<td colspan="2"><div align="right" class="mandatory">* Mandatory Fields</div></td>
          			</tr>
          			
          	
   </table>
  
 
  
   <title><br>Relation Master</title>
   <script type="text/javascript">
  
</script>
 
  	<center><s:actionmessage cssClass="mandatoryTwo"/></center>	
  	</div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div> 
	<%
	    String mode=(String)request.getAttribute("modeType");
	    System.out.println("mode------"+mode);
	   if(mode!=null && !mode.equals("") && mode.equals("view")) 
	   {%>
	 <input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/>
	<%} else if(mode!=null && !mode.equals("") && mode.equals("modify")){%>
	<center>
	
	<s:submit method="update" value="Modify" cssClass="buttonfinal"/>
	<input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/>
	
	<%} else if(mode!=null && !mode.equals("") && mode.equals("delete")){%>
	<center>
      <s:submit method="remove" value="Delete" cssClass="buttonfinal"/>
      <input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/></center>
	<%} else {%>
	<center>
	<s:submit method="create" value="Create" cssClass="buttonfinal"/>
	<s:submit method="view" value="View" cssClass="buttonfinal"/>
	<s:submit method="modify" value="Modify" cssClass="buttonfinal"/>
	<s:submit method="delete" value="Delete" cssClass="buttonfinal"/>
	</center>
	<% }%>
</s:form>
  </body>
  </html>
