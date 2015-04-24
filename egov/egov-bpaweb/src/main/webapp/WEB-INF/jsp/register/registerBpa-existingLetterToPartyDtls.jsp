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
	 document.location.href="${pageContext.request.contextPath}/register/registerBpa!modifyLetterToPartyForm.action?letterToPartyId="+letterToPartyId+"&mode=modify";
	 }

		 function  printLetterToParty(letterToPartyId){
			 document.location.href="${pageContext.request.contextPath}/report/bpaReport!printReport.action?letterToPartyId="+letterToPartyId;
			 }

		 function enterSendLetterToPartyDate(letterToPartyId){
			 document.location.href="${pageContext.request.contextPath}/register/registerBpa!modifyLetterToPartyForm.action?letterToPartyId="+letterToPartyId+"&mode=enterSentDate";
			 }

		 function printLPReplyAck(registrationId,letterToPartyId) {
			 document.location.href="${pageContext.request.contextPath}/lettertoparty/lpReply!ackPrint.action?registrationId="+registrationId+"&letterToPartyId="+letterToPartyId;
			 }
		 function  printLetterToPartyCmda(letterToPartyId){
			 //add LP Print Link here.
			//document.location.href="${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?letterToPartyId="+letterToPartyId;
			 }
		 
		 function printLPReplyCmdaAck(registrationId,letterToPartyId) {
			 document.location.href="${pageContext.request.contextPath}/lettertoparty/lpReplyCmda!ackPrint.action?registrationId="+registrationId+"&letterToPartyId="+letterToPartyId;
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
		  	
		   <s:url id="checkListlink" value="/register/registerBpa!showLetterToPartyCheckList.action" escapeAmp="false">
		     <!--  <s:param name="inspectionId" value="%{id}"></s:param>	  -->
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		       <s:param name="serviceTypeIdTemp" value="%{registration.serviceType.id}"></s:param>	
		        <s:param name="letterToPartyId" value="%{id}"></s:param>	
		       <s:param name="mode" value="%{mode}"></s:param>	
		   </s:url> 
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openChecklistDialog" href="%{checkListlink}" button="true" buttonIcon="ui-icon-newwin">View Lp Checklist</sj:a></div></td>
		  	
 			 <s:if test="%{sentDate!=null}">
 			<td  class="blueborderfortd"> LP Sent</td>
 			</s:if><s:else>
 			  <td  class="blueborderfortd"> <div id="modifyLetterDiv" align="center"><s:if test="%{#row_status.count==1}" ><a href="#" onclick="modifyLetterToParty('<s:property value="id"/>')"  >
 						 		Modify</a></s:if></div></td>
 						 </s:else>		
 						 <s:if test="%{sentDate!=null}">
 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLetterToParty('<s:property value="%{id}"/>')"  >
 						 		Print</a></div></td>
 					</s:if><s:else>	 
 					<td  class="blueborderfortd">&nbsp;</td>
 					</s:else>		
 						 	<s:if test="%{sentDate!=null}">
 						 		<td  class="blueborderfortd"> <s:date name="sentDate" format="dd/MM/yyyy" /></td>
 						 		</s:if><s:else>
 						 		<s:if test="%{(registration.state.value=='LetterToParty saved' || registration.state.value=='created') && sentDate==null}">
 						 		<td  class="blueborderfortd"> <div id="enterDateDiv" align="center"><s:if test="%{#row_status.count==1}" >
 						 		Enter Lp Sent Date</s:if></div></td>
 						 		</s:if><s:else>
 						 		<s:if test="%{registration.egwStatus.code=='Letter To Party Created' && sentDate==null}">
 						 		<td  class="blueborderfortd"> <div id="enterDateDiv" align="center"><s:if test="%{#row_status.count==1}" ><a href="#" onclick="enterSendLetterToPartyDate('<s:property value="id"/>')"  >
 						 		Enter Lp Sent Date</a></s:if></div></td>
 						 		</s:if>
 						 		</s:else>
 						 		</s:else>
 			
 			<s:if test="%{replyDate!=null}">
	 			<s:url id="lpReplycheckListlink" value="/register/registerBpa!showLetterToPartyReplyCheckList.action" escapeAmp="false">
			       <s:param name="registrationId" value="%{registration.id}"></s:param>	
			       <s:param name="serviceTypeIdTemp" value="%{registration.serviceType.id}"></s:param>	
			        <s:param name="letterToPartyId" value="%{id}"></s:param>	
			       <s:param name="mode" value="%{mode}"></s:param>	
			   </s:url> 
			  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openChecklistDialog" href="%{lpReplycheckListlink}" button="true" buttonIcon="ui-icon-newwin">View Lp Reply Checklist</sj:a></div></td>		  
	 			 
	 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLPReplyAck('<s:property value="%{registration.id}"/>','<s:property value="%{id}"/>')"  >
	 						 		Print LP Reply Ack</a></div></td>
	 						 		
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
 		        <s:url id="cmdadetLink" value="/register/registerBpa!showExistingLetterToPartyCMDADetails.action" escapeAmp="false">
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

