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
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<s:set name="theme" value="'simple'" scope="page" />

<SCRIPT>
  jQuery.noConflict();

jQuery(document).ready(function(){
 
 jQuery('#lpdetails').find('input,select').attr('disabled','true');	 

  }); 
  </SCRIPT>
  
<div id="lpdetails" align="center"> 

<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />
 <SCRIPT>
  jQuery.noConflict();


 jQuery(document).ready(function(){
	 if( jQuery('#mode').val()=="view"){
	 if(document.getElementById('modifyLetterDiv')!=null)
		 document.getElementById('modifyLetterDiv').style.display='none';
		 if(document.getElementById('enterDateDiv')!=null)
		 document.getElementById('enterDateDiv').style.display='none';
	 }
 jQuery("[id=datedetails]").each(function(index) {
			jQuery(this).find('input').css("border", "");
			});
 
 
   jQuery( "#accordion" ).each(function(){
   alert(jQuery(this));
   jQuery(this).accordion({
            heightStyle: ""
        });
   
   });

  });
 function modifyLetterToParty(letterToPartyId){
	 document.location.href="${pageContext.request.contextPath}/extd/register/registerBpaExtn!modifyLetterToPartyForm.action?letterToPartyId="+letterToPartyId+"&mode=modify";
	 }

		 function  printLetterToParty(letterToPartyId){
			document.location.href="${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?letterToPartyId="+letterToPartyId;
			 }

		 function  printLetterToPartyCmda(letterToPartyId){
			 //add LP Print Link here.
			document.location.href="${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?letterToPartyId="+letterToPartyId;
			 }
		 
		 function printLPReplyCmdaAck(registrationId,letterToPartyId) {
			 document.location.href="${pageContext.request.contextPath}/extd/lettertoparty/lpReplyCmdaExtn!ackPrint.action?registrationId="+registrationId+"&letterToPartyId="+letterToPartyId;
			 }
		 function printLPReplyAck(registrationId,letterToPartyId) {
			 document.location.href="${pageContext.request.contextPath}/extd/lettertoparty/lpReplyExtn!ackPrint.action?registrationId="+registrationId+"&letterToPartyId="+letterToPartyId;
			 }
		
		 
		 
			 jQuery.subscribe('completedchecklistDiv', function(event,ui) {
		
	       jQuery('#lpchecketails').find('input,select').attr('disabled','true');	 
		});
			 function viewDocumentManager(docNumber){
				
				  
				   var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+docNumber+"&moduleName=BPA";
				   var wdth = 1000;
				    var hght = 400;
				    window.open(url,'docupload','width='+wdth+',height='+hght);
				}
 
</SCRIPT>
<s:hidden id="registrationId" name="registrationId" value="%{registrationId}"/>
<s:hidden id="letterToPartyId" name="letterToPartyId" value="%{letterToPartyId}"/>

			<s:hidden id="createdBy" name="createdBy" value="%{createdBy.id}"/>
	   		<s:hidden id="modifiedBy" name="modifiedBy" value="%{modifiedBy.id}"/>
	   		<s:hidden id="createdDate" name="createdDate" value="%{createdDate}"/>
	   		<s:hidden id="modifiedDate" name="modifiedDate" value="%{modifiedDate}"/>
<div id="checklistdetails" align="center"> 
<s:if test="existingLetterToPartyDetails.size!=0">
<sj:dialog 
    	id="lpchecklist" 
    	autoOpen="false" 
    	modal="true" 
    	title="CheckList Details"
    	openTopics="openChecklistDialog"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	loadingText="Please Wait ...."
    	onCompleteTopics="completedchecklistDiv" cssStyle="BACKGROUND-COLOR: #ffffff"
    />
    
<div id="header" class="formmainbox" align="center">
	    <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Number</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Reason</div></th>
		     <th  class="bluebgheadtd" width="8%"><div align="center">LP Description</div></th>
		     <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Date</div></th>		
		      <th  class="bluebgheadtd" width="8%"><div align="center">Checklist</div></th>	
	  		<th  class="bluebgheadtd" width="8%"><div align="center">Modify</div></th>	
		        <th  class="bluebgheadtd" width="8%"><div align="center">Print</div></th>	
		    	 <th  class="bluebgheadtd" width="8%"><div align="center">LP Sent Date</div></th>
			    	 <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Checklist</div></th>
			    	 <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Ack Print</div></th>		
			    	 <th  class="bluebgheadtd" width="8%"><div align="center">Uploaded Document</div></th>	
			    	 <th  class="bluebgheadtd" width="8%"><div align="center">CMDA LP Details</div></th>	
		 </tr> 
		  <s:iterator value="existingLetterToPartyDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{letterToPartyNumber}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:date name="letterDate" format="dd/MM/yyyy" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{letterToPartyReason.description}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{letterToPartyRemarks}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:date name="replyDate" format="dd/MM/yyyy" /></div></td>
		  	
		   <s:url id="checkListlink" value="/extd/register/registerBpaExtn!showLetterToPartyCheckList.action" escapeAmp="false">
		     <!--  <s:param name="inspectionId" value="%{id}"></s:param>	  -->
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		       <s:param name="serviceTypeIdTemp" value="%{registration.serviceType.id}"></s:param>	
		        <s:param name="letterToPartyId" value="%{id}"></s:param>	
		       <s:param name="mode" value="%{mode}"></s:param>	
		   </s:url> 
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openChecklistDialog" href="%{checkListlink}" button="true" buttonIcon="ui-icon-newwin">View Lp Checklist</sj:a></div></td>
		  	 <s:if test="%{sentDate==null}">
 			
 			<td  class="blueborderfortd"> <div id="modifyLetterDiv" align="center"><s:if test="%{#row_status.count==1}" ><a href="#" onclick="modifyLetterToParty('<s:property value="id"/>')"  >
 						 		Modify</a></s:if></div></td>
 						</s:if>
 						<s:else>
 						<td  class="blueborderfortd">&nbsp;</td>
 						</s:else>
 	 						 <s:if test="%{sentDate!=null}">
 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLetterToParty('<s:property value="%{id}"/>')"  >
 						 		Print</a></div></td>
 					</s:if><s:else>	 
 					<td  class="blueborderfortd">&nbsp;</td>
 					</s:else>		
 					
 						 	<s:if test="%{sentDate!=null}">
 						 		<td  class="blueborderfortd"> <s:date name="sentDate" format="dd/MM/yyyy" /></td>
 						 		</s:if>
 			
 			<s:if test="%{replyDate!=null}">
	 			<s:url id="lpReplycheckListlink" value="/extd/register/registerBpaExtn!showLetterToPartyReplyCheckList.action" escapeAmp="false">
			       <s:param name="registrationId" value="%{registration.id}"></s:param>	
			       <s:param name="serviceTypeIdTemp" value="%{registration.serviceType.id}"></s:param>	
			        <s:param name="letterToPartyId" value="%{id}"></s:param>	
			       <s:param name="mode" value="%{mode}"></s:param>	
			   </s:url> 
			  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openChecklistDialog" href="%{lpReplycheckListlink}" button="true" buttonIcon="ui-icon-newwin">View Lp Reply Checklist</sj:a></div></td>		  
	 			 
	 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLPReplyAck('<s:property value="%{registration.id}"/>','<s:property value="%{id}"/>')"  >
	 						 		Print  LP Reply Ack</a></div></td>
	 						 		
	 			<td class="blueborderfortd">
	 			<s:if test="documentid==null || documentid==''">
									<div align="center">
					 			<p>No Documents Attached</p>
	
							 </div>
						</s:if>
						<s:else>			
						
							<a href='#' target="_parent" onclick="viewDocumentManager(<s:property  value="%{documentid}" />)">View Document</a>

						</span> 
						</s:else>
					</td>
				
				
			</s:if>
	 		<s:else>
	 			<td  class="blueborderfortd">&nbsp;</td>
	 			<td  class="blueborderfortd">&nbsp;</td>
	 			<td  class="blueborderfortd">&nbsp;</td>
	 		</s:else>
 				<s:if test="%{cmdaLetterToPartySet.size!=0}">
 				<sj:dialog 
    	id="ordDet1" 
    	autoOpen="false" 
    	modal="true" 
    	title="CMDA LP Details"
    	openTopics="openOrdDetailsDialog1"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
    	onCompleteTopics="dialogopentopic" 
    	loadingText="Please Wait...."  
    	errorText="Permission denied."
    />
 		        <s:url id="cmdadetLink" value="/extd/register/registerBpaExtn!showExistingLetterToPartyCMDADetails.action" escapeAmp="false">
		     <!--  <s:param name="inspectionId" value="%{id}"></s:param>	  -->
		      <s:param name="letterToPartyId" value="%{id}"></s:param>	
		       <s:param name="mode" value="%{mode}"></s:param>	
		   </s:url> 
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openOrdDetailsDialog1" href="%{cmdadetLink}" button="true" buttonIcon="ui-icon-newwin">View CMDA LP </sj:a></div></td>
		  	</s:if>
		  	<s:else>
		  	<td  class="blueborderfortd">&nbsp;</td>
		  	</s:else> 
		  	 </tr>
		    </s:iterator>
		   
	  </table>
	 </div>
	 </s:if>
</div>
</div>
