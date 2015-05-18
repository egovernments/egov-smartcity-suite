#-------------------------------------------------------------------------------
# #-------------------------------------------------------------------------------
# # <!-- #-------------------------------------------------------------------------------
# # # eGov suite of products aim to improve the internal efficiency,transparency, 
# # #    accountability and the service delivery of the government  organizations.
# # # 
# # #     Copyright (C) <2015>  eGovernments Foundation
# # # 
# # #     The updated version of eGov suite of products as by eGovernments Foundation 
# # #     is available at http://www.egovernments.org
# # # 
# # #     This program is free software: you can redistribute it and/or modify
# # #     it under the terms of the GNU General Public License as published by
# # #     the Free Software Foundation, either version 3 of the License, or
# # #     any later version.
# # # 
# # #     This program is distributed in the hope that it will be useful,
# # #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# # #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# # #     GNU General Public License for more details.
# # # 
# # #     You should have received a copy of the GNU General Public License
# # #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# # #     http://www.gnu.org/licenses/gpl.html .
# # # 
# # #     In addition to the terms of the GPL license to be adhered to in using this
# # #     program, the following additional terms are to be complied with:
# # # 
# # # 	1) All versions of this program, verbatim or modified must carry this 
# # # 	   Legal Notice.
# # # 
# # # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # # 	   is required that all modified versions of this material be marked in 
# # # 	   reasonable ways as different from the original version.
# # # 
# # # 	3) This license does not grant any rights to any user of the program 
# # # 	   with regards to rights under trademark law for use of the trade names 
# # # 	   or trademarks of eGovernments Foundation.
# # # 
# # #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# # #------------------------------------------------------------------------------- -->
# #-------------------------------------------------------------------------------
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld"%>
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
