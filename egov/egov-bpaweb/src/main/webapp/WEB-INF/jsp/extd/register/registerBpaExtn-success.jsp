<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<html>
   
<title><s:text name='NewBPA.title'/></title>   
<style>
input.buttonsubmit { 
 	background-image: url("../../images/buttonbg.gif");
    border-style: none;
    color: #333333;
    font-family: Arial,Helvetica,sans-serif;
    font-size: 12px;
    font-weight: bold;
    height: 27px;
    text-align: center;
    width: 133px;
	}
</style>

<script>
function showPageHeader(){
		dom.get("breadcrumbHeader").innerHTML='<s:text name="cocUser.header" />';
}
</script>
<body onload="refreshInbox();showPageHeader();">

	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>
<s:form name="LetterToPartyform"  action="registerBpaExtn" theme="simple">
<s:push value="model">
  <s:token/>
 <s:hidden id="registrationId" name="registrationId" value="%{id}"/>	
		    <s:hidden id="mode" name="mode" value="%{mode}"/>
		<div class="buttonbottom" align="center">
	
		<table>
		
			<s:if test="%{mode=='view'}">
			
  				<td>
	  			&nbsp;<input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  			</td>
	  		
	  		</s:if>
	  		
	  		</table>
	  		
	  		</div>
	  		</s:push>
	  		</s:form>
	  	
</body>
</html>
