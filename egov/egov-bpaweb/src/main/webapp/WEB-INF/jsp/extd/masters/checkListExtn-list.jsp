<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>


<html>
<head>
<s:if test="%{mode=='edit'}">	
<title>Modify Check List Type</title>
</s:if>
<s:else>
<title> Check List Master</title>
</s:else>
	
</head>

<script language="javascript" type="text/javascript">
	
function closeWindow(){
    window.close();
   }       
            

function validationCheck()
	{
	var bpafee=document.getElementById("checklistType").value;
	var servfee=document.getElementById("serviceType").value;

	  if(bpafee=='-1' && servfee=='-1' || bpafee=='-1' ||servfee=='-1' )
	  {
	   dom.get("Bpaservice_error").style.display='';
	  document.getElementById("Bpaservice_error").innerHTML='CheckList Type And Servicetype are mandatory';
	  return false;
	  }
	 
	}       
            
            
	
</script>
<body >
 <s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
			<s:actionmessage />
		</div>
	</s:if>
<div class="errorstyle" id="Bpaservice_error" style="display:none;"></div>
        <s:form action="checkListExtn" name="checkListActionForm" theme="simple" >
        <s:token />
        <div class="formheading"/></div>
        
 <div class="formmainbox">
 			
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
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox">&nbsp;</td>
				</tr>
				<tr>
			          <td width="20%" class="bluebox">&nbsp;</td>
			           <td width="20%" class="bluebox">&nbsp;</td>
			          <td  class="bluebox"></td>
			           <td  class="bluebox"></td>
			          <td  class="bluebox"></td>
			           <td  class="bluebox"></td>
			          <td  class="bluebox"></td>
		              <td class="bluebox" width="15%"><s:text name="Check List Type"/>
		               <span class="mandatory">*</span></td>
		                <td class="bluebox"><s:select name="checklistType" id="checklistType"  value="%{checklistType}"
		                      list="dropdownData.checkIdList" listKey="code" listValue="code" headerKey="-1" headerValue="-----choose----"/></td>
				 
		                <td class="bluebox">&nbsp;</td>
	                   <td class="bluebox">&nbsp;</td>
		               <td class="bluebox" width="10%">
		                <s:text name="Service Type"/>
		                <span class="mandatory">*</span></td>
	                      <td class="bluebox"	 ><s:select  id="serviceType" name="serviceType.id" value="%{serviceType.id}" list="dropdownData.servicetypeList" listKey="id" listValue="code+'-'+description" headerKey="-1" headerValue="----Choose----" maxsize = "256"  /> </td>	
		                   <td width="20%"class="bluebox"></td>
		                   <td width="20%"class="bluebox"></td>
		                     <td width="20%"class="bluebox"></td>
		                         </tr>                       
		       <s:hidden id="mode" name="mode" /> 
		        </table>
          </div>
<div id="actionbuttons" align="center" class="buttonbottom"> 
								
    <s:submit type="submit" cssClass="buttonsubmit" value="New" id="New" name="New" method="newform" onclick="return validationCheck();"/>								
	<s:submit type="submit" cssClass="buttonsubmit" value="View" id="View" name="View"  method="view" onclick="return validationCheck();" />
	<s:submit type="submit" cssClass="buttonsubmit" value="Modify" id="Modify" name="Modify"  method="modify" onclick="return validationCheck();"/>
	
	<input type="button" name="close" id="close" class="button" value="Close" onclick=" window.close();" ></td>
</div>				      

</s:form >
</body>
</html>