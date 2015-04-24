<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
 <style>
	 .dropdown
{
background: #fff;
position: absolute;
color: #555;
margin: 0px 0px 0 0;
width: 120px;
position: relative;
height: 25px;
text-align:left;
}
.submenu
{
background: #fff;
position: absolute;
top: 30px;
left: 0px;
z-index: 100;
width: 135px;
display: none;
margin-left: 0px;
padding: 0px 0 5px;
border-radius: 6px;
box-shadow: 0 2px 8px rgba(0, 0, 0, 0.45);
}
.dropdown li a
{
color: #555555;
display: block;
font-family: arial;
font-weight: bold;
padding: 6px 15px;
cursor: pointer;
text-decoration:none;
}

.dropdown li a:hover
{
background:#155FB0;
color: #FFFFFF;
text-decoration: none;
}
a.account 
{
font-size: 11px;
line-height: 16px;
color: #555;
position: absolute;
z-index: 110;
display: block;
padding: 11px 0 0 20px;
height: 28px;
width: 121px;
margin: -11px 0 0 -10px;
text-decoration: none;
background: url(icons/arrow.png) 116px 17px no-repeat;
cursor:pointer;
}
.root
{
list-style:none;
margin:0px;
padding:0px;
font-size: 11px;
padding: 11px 0 0 0px;
border-top:1px solid #dedede;
}

 </style>

 
  
  <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
  <script type="text/javascript" src="<c:url value='/common/js/jquerycombobox.js'/>"></script>
     <title><s:text name="RegisterBpaHeader"/></title>
<script type="text/javascript">

jQuery.noConflict();
jQuery(document).ready(function() {
	
	hideExistingApplDetails();
	hideShowBuildingCategory();	
	hideShowCMDAfields();
	 
}

);

function bodyOnLoad(){

	
	var mode=document.getElementById('mode').value;
	
	
	 if(mode=='viewForOtherModule' )
	{	
			 jQuery("#villageName").parent().find("a.ui-button").button("disable");
		    jQuery('#showjsp').show();
			jQuery('#hidejsp').hide();
		for(var i=0;i<document.forms[0].length;i++)
		{
			if( document.forms[0].elements[i].name!='close' )
				document.forms[0].elements[i].disabled =true;
		}
	} 

			 
}


	
</script>
  </head>
  <body onload="bodyOnLoad();refreshInbox();">

  
	<div class="errorstyle" id="bpa_error_area" style="display: none;"></div>
	<div class="errorstyle" style="display: none" ></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>

<s:form name="RegisterBpaForm" action="registerBpa" theme="simple" >
  <s:push value="model">
  <s:token/> 
  <div class="formmainbox">
    <s:hidden id="mode" name="mode" value="%{mode}"/>
  <s:hidden id="additionalMode" name="additionalMode" value="%{additionalMode}"/>
  		
  			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  			  <tr>
  				
		            <td>
			            <div id="regForm">
			                <%@ include file="registerBpaForm.jsp"%>      
			            </div>
		            </td>
		           
		           
	          </tr>
	          
		</table>
		  
	<div class="buttonbottom" align="center">
		<table>
			<td >&nbsp;</td>
	  		<td>
	  			<input type="button" name="close" id="close" class="button"  value="Close" onclick="window.close();"/>
	  		</td>
	  		</table>	    			    	
			    </div>
			    
 </div>
       </s:push>
  </s:form>
   
  
  </body>
  </html>