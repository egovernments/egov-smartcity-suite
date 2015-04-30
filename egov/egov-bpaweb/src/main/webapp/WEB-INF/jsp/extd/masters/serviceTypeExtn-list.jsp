<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>


<html>
<head>
<s:if test="%{mode=='edit'}">	
<title>Modify Service Type</title>
</s:if>
<s:else>
<title>Service Type Master</title>
</s:else>
	
</head>

<script language="javascript" type="text/javascript">
	
function closeWindow(){
    window.close();
   }       
            

function validationCheck()
	{
	
	var serviceType=document.getElementById("idTemp").value;

	  if(serviceType=='-1')
	  {
	   dom.get("Bpaservice_error").style.display='';
	  document.getElementById("Bpaservice_error").innerHTML='ServiceType is mandatory';
	  return false;
	  }
	  else
	  	return true;
	}       
            
            
	
</script>
<body ><br>
       <s:if test="%{hasErrors()}">
		<div class="errorcss" id="fieldError">
			<s:actionerror cssClass="errorcss"/>
			<s:fielderror cssClass="errorcss"/>
		</div>
	</s:if>
<div class="errorstyle" id="Bpaservice_error" style="display:none;"></div>
   <s:form action="serviceTypeExtn" name="serviceTypeForm" theme="simple">
   
        <s:token />
        <div class="formheading"/></div>
 <div class="formmainbox">
 		
 			<s:hidden name="id" id="id"/>
 		
 		
		  <table width="100%" border="0" cellspacing="0" cellpadding="2">
		   
				<tr>
					<td width="20%" class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
				</tr>
				
			
		
		<tr>
		<td  class="bluebox" width="33%">&nbsp;</td>			
		<td class="bluebox"><s:text name="Service Type"/>
		<span class="mandatory">*</span>
		<s:select  id="idTemp" name="idTemp" list="dropdownData.servicetypeList" listKey="id" listValue="code+'-'+description" headerKey="-1" headerValue="----Choose----" maxsize = "256"  /> 	
		
			
		 <td class="bluebox">&nbsp;</td>
		  <td class="bluebox">&nbsp;</td>
		   <td class="bluebox">&nbsp;</td>
		    <td class="bluebox">&nbsp;</td>
		     <td class="bluebox">&nbsp;</td>
		      <td class="bluebox">&nbsp;</td>
		  <s:hidden id="mode" name="mode" /> 
		</td>
		</tr>
		  </table>
</div><div id="actionbuttons" align="center" class="buttonbottom"> 
								
									
	<s:submit type="submit" cssClass="buttonsubmit" value="View" id="View" name="View"  method="view" onclick="return validationCheck();" />
	<s:submit type="submit" cssClass="buttonsubmit" value="Modify" id="Modify" name="Modify"  method="modify" onclick="return validationCheck();"/>
	
	<input type="button" name="close" id="close" class="button" value="Close" onclick=" window.close();" ></td>
								
	</div>				      

</s:form >
</body>
</html>