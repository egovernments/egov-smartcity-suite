#-------------------------------------------------------------------------------
# <!-- #-------------------------------------------------------------------------------
# # eGov suite of products aim to improve the internal efficiency,transparency, 
# #    accountability and the service delivery of the government  organizations.
# # 
# #     Copyright (C) <2015>  eGovernments Foundation
# # 
# #     The updated version of eGov suite of products as by eGovernments Foundation 
# #     is available at http://www.egovernments.org
# # 
# #     This program is free software: you can redistribute it and/or modify
# #     it under the terms of the GNU General Public License as published by
# #     the Free Software Foundation, either version 3 of the License, or
# #     any later version.
# # 
# #     This program is distributed in the hope that it will be useful,
# #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# #     GNU General Public License for more details.
# # 
# #     You should have received a copy of the GNU General Public License
# #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# #     http://www.gnu.org/licenses/gpl.html .
# # 
# #     In addition to the terms of the GPL license to be adhered to in using this
# #     program, the following additional terms are to be complied with:
# # 
# # 	1) All versions of this program, verbatim or modified must carry this 
# # 	   Legal Notice.
# # 
# # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # 	   is required that all modified versions of this material be marked in 
# # 	   reasonable ways as different from the original version.
# # 
# # 	3) This license does not grant any rights to any user of the program 
# # 	   with regards to rights under trademark law for use of the trade names 
# # 	   or trademarks of eGovernments Foundation.
# # 
# #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# #------------------------------------------------------------------------------- -->
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>
<title>
	<s:text name="letterToPartyReply"/>
</title>

<head>
<sj:head jqueryui="true" jquerytheme="blitzer" loadAtOnce="true" />


<script type="text/javascript">
jQuery.noConflict();

function validateForm(){
	var count =0;
	var j=0;
	var documentFile =dom.get("docNumber").value;
jQuery("[id=mandatorycheck]").each(function(index) {	
   		jQuery(this).find(':checkbox').css('outline-color', '');
		jQuery(this).find(':checkbox').css('outline-style', '');
		jQuery(this).find(':checkbox').css('outline-width', '');
   		jQuery(this).next("td").find('textarea').css("border", '');
   		});	
   		
   		
	dom.get("lp_error").style.display='none';
	
	if(document.getElementById('letterToPartyReplyDesc').value==null || document.getElementById('letterToPartyReplyDesc').value==""){
		dom.get("lp_error").style.display = '';
		document.getElementById("lp_error").innerHTML = '<s:text name="lpreply.desp.mandatory" />';
		return false;
	}
	
		 if(documentFile==null || documentFile=="")
			 {
			 	dom.get("lp_error").style.display = '';
				document.getElementById("lp_error").innerHTML = '<s:text name="lpreply.Document.required" />';
				return false;
			 }
	
	jQuery("[id=mandatorycheck]").each(function(index) {	
		
       if(jQuery(this).find(':checkbox').prop('checked')){
    	  	dom.get("lp_error").style.display = 'none';
    		document.getElementById("lp_error").innerHTML='';
   	   	;
   		
   		}else{ 		
   		  j++;
   		
   		dom.get("lp_error").style.display = '';
   		jQuery(this).find(':checkbox').css('outline-color', 'red');
		jQuery(this).find(':checkbox').css('outline-style', 'solid');
		jQuery(this).find(':checkbox').css('outline-width', 'thin');
		
		}
		});		
	var size=jQuery('#checklistsize').val();
	
	if(size==j){
	document.getElementById("lp_error").innerHTML = 'Atleast one checkList needs be select';
			return false;
	}

	jQuery("[id=mandatorycheck]").each(function(index) {	
			if(jQuery(this).find(':checkbox').prop('checked')){
							var rem=jQuery(this).next("td").find('textarea').attr("value");
   							if(rem==""){
   								dom.get("lp_error").style.display = '';
   									document.getElementById("lp_error").innerHTML = 'Please enter the remarks for the checked checkList';
   									jQuery(this).next("td").find('textarea').css("border", "1px solid red");
   									count++;
   									}
   					}
   		});	
   		if(count!=0)
			return false;
   		
}

function confirmClose(){
	var result = confirm("Do you want to close the window?");
	if(result==true){
		window.close();
		return true;
	}else{
		return false;
	}
}


function onreportupload()
	 {
	     var v= dom.get("docNumber").value;
	     var url;
	     if(v==null||v==''||v=='To be assigned')
	     {
		   url="/egi/docmgmt/basicDocumentManager.action?moduleName=BPA";
	     }
	     else
	     {
		   
	       url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=BPA";
	     }
	     var wdth = 1000;
	     var hght = 400;
	     window.open(url,'docupload','width='+wdth+',height='+hght);
	 }	
</script>

</head>
<body onload="refreshInbox();">
<div class="errorstyle" id="lp_error" style="display:none;" >
</div>
<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="fieldError">
			<s:actionerror />
			<s:fielderror />
		</div>
</s:if>

<s:if test="%{hasActionMessages()}">
		<div class="errorstyle">
				<s:actionmessage />
		</div>
</s:if>

<s:form action="lpReplyExtn" theme="simple">
<s:token/>
<s:push value="model">

<s:hidden id="mode" name="mode" value="%{mode}"/>
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="requestID" name="requestID" value="%{requestID}"/>

<div  id="lpdetails" class="formmainbox">
<s:if test="%{letterParty != null && letterParty.isHistory != 'Y'}">
<div class="headingbg"><s:text name="lettertoparty.header"/></div>
 <s:if test="%{mode !='noEditMode'}">
 <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
 		
	   	 	<tr>
	   	 		<td class="bluebox" ><s:text name="lpNum"/> : </td>
				<td class="bluebox" >
					<s:property value="existLpNum"/>
				</td>
	   	 		<td class="bluebox" colspan="2">&nbsp;</td>
          </tr>
          <tr>
	   	 		<td class="bluebox" ><s:text name="lpreason"/> : </td>
				<td class="bluebox" >
					<s:property value="existLpReason"/>
				</td>
	   	 		<td class="bluebox" ><s:text name="lpdescription"/></td>
	 			<td class="bluebox">
	 				<s:property value="existLpRemarks"/>
	 			</td>
          </tr>
       
          <tr>
	   	 	     <td class="bluebox" ><s:text name="lpReplyDesc"/> <span class="mandatory" >*</span>: </td>
	 			<td class="bluebox">
	 				<s:textarea id="letterToPartyReplyDesc" name="letterToPartyReplyDesc" value="%{letterToPartyReplyDesc}" cols="20" rows="3"/>
	 			</td>  
	 			<td class="bluebox" ><s:text name="lpReplyRemarks"/> : </td>
	 			<td class="bluebox">
	 				<s:textarea id="letterToPartyReplyRemarks" name="letterToPartyReplyRemarks" value="%{letterToPartyReplyRemarks}" cols="20" rows="3"/>
	 			</td>   			
          </tr>
         
		<tr> 
			<td class="bluebox">
								<s:text name="Document upload: " />
						</td>
						<s:if test="documentNum==null || documentNum==''">
								<td id="addlink" class="bluebox">
									<div class="buttonholderrnew">
										<input type="button" id="browse" value="Browse"
											onclick="onreportupload();return false;"
											class="buttoncreatenewcase" />
									</div>
								</td>
							</s:if>
					
					   		 <td class="bluebox"/>
							  <td class="bluebox"/>
                       
				<s:hidden name="documentNum" id="docNumber" value="%{documentNum}"></s:hidden>
                      
                </tr>

	 </table>
	 </s:if>
	 </s:if>
	 </div>
  	<s:if test="%{mode !='noEditMode'  && mode!='view'}">
		 <s:url id="checklistajax" value="/extd/lettertoparty/lpReplyExtn!showCheckList.action"  escapeAmp="false">
		 	<s:param name="serviceTypeId" value=""></s:param>	
		 	<s:param name="registrationId" value="%{registrationId}"></s:param>	
		 </s:url>
	     <sj:div href="%{checklistajax}" indicator="indicator"  cssClass="" id="tab1"  dataType="html" onCompleteTopics="completedchecklistDiv">
	       <img id="indicator" src="<egov:url path='/images/loading.gif'/>" alt="Loading..." style="display:none"/>
	     </sj:div>
	</s:if>
	 <div class="buttonbottom" align="center"> 
		<table>
		<tr> 
		 <s:if test="%{(mode !='noEditMode' && mode!='view') && (letterParty != null && letterParty.isHistory != 'Y')}">
		  	<td><s:submit  cssClass="buttonsubmit" id="save" name="save" value="Save" method="createLpReply" onclick="return validateForm()"/></td>	
	  	 </s:if>	         
	  	 <s:if test="%{mode=='view' && mode !='noEditMode'}">
	  		<td>
	  			<s:submit type="submit" cssClass="buttonsubmit" value="Print Ack" id="print" name="print" method="ackPrint"/>
	  		</td>
	  	</s:if>
	  		 <td><input type="button" name="close" id="close" class="button"  value="Close" onclick="confirmClose();"/>
	  		</td>
	  	</tr>
        </table>
    </div>
</s:push>
</s:form>

</body>
</html>
