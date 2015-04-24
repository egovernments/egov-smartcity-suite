<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>

<head>
 <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
	<title>View Letter To Party Details</title>
	
<script type="text/javascript">

 function  printLetterToParty(letterToPartyId){
 
			 document.location.href="${pageContext.request.contextPath}/citizen/citizenReport!printReport.action?letterToPartyId="+letterToPartyId;
			 }
			 
 function printLPReplyAck(registrationId) {
			 document.location.href="${pageContext.request.contextPath}/citizen/citizenSearch!ackPrint.action?registrationId="+registrationId;
			 }
			 
			 			 
  jQuery.noConflict();
  
  jQuery(document).ready(function() {
//  jQuery("a").button();
   });
	function viewDocumentManager(docNumber){
	  
	   var url= "/egi/docmgmt/basicDocumentManager!viewDocument.action?docNumber="+docNumber+"&moduleName=BPA";
	   var wdth = 1000;
	    var hght = 400;
	    window.open(url,'docupload','width='+wdth+',height='+hght);
	}

	 function  printLetterToPartyCmda(letterToPartyId){
		 //add LP Print Link here.
		//document.location.href="${pageContext.request.contextPath}/extd/report/bpaReportExtn!printReport.action?letterToPartyId="+letterToPartyId;
		 }
	 
	 
	 	function printLPReplyCmdaAck(registrationId,letterToPartyId) {
		 document.location.href="${pageContext.request.contextPath}/extd/lettertoparty/lpReplyCmdaExtn!ackPrint.action?registrationId="+registrationId+"&letterToPartyId="+letterToPartyId;
		 }
  
</script>
</head>



<body>
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
    	errorText="Permission Denied"
    	onCompleteTopics="completedchecklistDiv" cssStyle="BACKGROUND-COLOR: #ffffff"
    />
    
<div id="header" class="formmainbox" align="center"><div> 
	    </div><table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%" align="right">Sl No</th>
		    <th  class="bluebgheadtd" width="8%" align="right">LP Number</th>
		    <th  class="bluebgheadtd" width="8%" align="right">LP Date</th>
		    <th  class="bluebgheadtd" width="8%" align="right">LP Reason</th>
		    <th  class="bluebgheadtd" width="8%" align="right">LP Reply Date</th>		
		    <th  class="bluebgheadtd" width="8%" align="right">Checklist</th>	
		    <th  class="bluebgheadtd" width="8%" align="right">LP Sent Date</th>	
		    <th  class="bluebgheadtd" width="8%" align="right">Print</th>	  
		    <th  class="bluebgheadtd" width="8%" align="right">LP Reply Checklist</th>
			<th  class="bluebgheadtd" width="8%" align="right">LP Reply Ack Print</th>	
			<th  class="bluebgheadtd" width="8%" align="right">Uploaded Document</th>	
		
		 </tr> 
		  <s:iterator value="existingLetterToPartyDetails" status="row_status" var="letter">
		  <s:if test="%{sentDate!=null}">
		    <tr>
		  	<td  class="blueborderfortd"><div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{letterToPartyNumber}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:date name="letterDate" format="dd/MM/yyyy" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{letterToPartyReason.description}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:date name="replyDate" format="dd/MM/yyyy" /></div></td>
		  	
		   <s:url id="checkListlink" value="/citizen/citizenSearch!showLetterToPartyCheckList.action" escapeAmp="false">     
		       <s:param name="serviceTypeIdTemp" value="%{registration.serviceType.id}"></s:param>	
		        <s:param name="letterToPartyId" value="%{id}"></s:param>	
		   </s:url> 
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openChecklistDialog" href="%{checkListlink}" button="true" buttonIcon="ui-icon-newwin">View</sj:a></div></td>		  
 		    <td  class="blueborderfortd"> <s:date name="sentDate" format="dd/MM/yyyy" /></td>
 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLetterToParty('<s:property value="%{id}"/>')" >Print</a></div></td>			 		
 		   
 		    
 		    <s:if test="%{replyDate!=null}">
	 			<s:url id="lpReplycheckListlink" value="/citizen/citizenSearch!showLetterToPartyReplyCheckList.action" escapeAmp="false">
			       <s:param name="serviceTypeIdTemp" value="%{registration.serviceType.id}"></s:param>	
			        <s:param name="letterToPartyId" value="%{id}"></s:param>	
			       <s:param name="mode" value="%{mode}"></s:param>	
			   </s:url> 
			  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openChecklistDialog" href="%{lpReplycheckListlink}" button="true" buttonIcon="ui-icon-newwin">View Lp Reply Checklist</sj:a></div></td>		  
	 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLPReplyAck('<s:property value="%{registration.id}"/>')"  >
	 			Print LP Reply Ack</a></div></td>
	 			
				<td  class="blueborderfortd">
				<s:if test="documentid==null || documentid==''">
									<div align="center">
					 			<p>No Documents Attached</p>
	
							 </div>
						</s:if>
						<s:else>			
							
						<span class="bold"> 
							
						
							<a href='#' target="_parent" onclick="viewDocumentManager(<s:property  value="%{documentid}" />)">View Document</a>

						</span> 
						</s:else>
					</td>
				
			</s:if>	
	 		<s:else>
	 			<td  class="blueborderfortd"></td>		  
	 			<td  class="blueborderfortd"></td>		
	 		</s:else>
	 		 </tr>
	 		  <s:if test="%{cmdaLetterToPartySet.size!=0}">
 		      <s:if test="%{existingCmdaLetterToPartyDetails.size!=0}">
		    <s:iterator value="existingCmdaLetterToPartyDetails" status="cmda_Row_count">
		      <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:property value="%{existingLetterToPartyDetails.size+ 1}"/></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{letterToPartyNumber}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:date name="createdDate" format="dd/MM/yyyy" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{lpReason}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{lpReplyRemarks}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:date name="replyDate" format="dd/MM/yyyy" /></div></td>
		  	
		  	<td  class="blueborderfortd"></td>
		  	 
 						<td  class="blueborderfortd">&nbsp;</td>
 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLetterToPartyCmda('<s:property value="%{id}"/>')"  >
 						 		CMDA Print</a></div></td>
 			
 						 		<td  class="blueborderfortd"><s:date name="letterToParty.sentDate" format="dd/MM/yyyy"/></td>
 						 	
 			<s:if test="%{replyDate!=null}">
	 			
			  <td  class="blueborderfortd">  	<s:property value="%{lpReplyDescription}"/>	</td>
	 			 
	 			<td  class="blueborderfortd"> <div align="center"><a href="#" onclick="printLPReplyCmdaAck('<s:property value="%{registration.id}"/>','<s:property value="%{id}"/>')"  >
	 						 		Print CMDA LP Reply Ack</a></div></td>
	 						
	 			<td class="blueborderfortd">
	 			<s:if test="documentid==null || documentid==''">
									<div align="center">
					 			<p>No Documents Attached</p>
	
							 </div>
						</s:if>
						<s:else>			
						
							<a href='#' target="_parent" onclick="viewDocumentManager(<s:property  value="%{documentid}" />)">View Document</a>

						
						</s:else>
					</td>
				
				
			</s:if>
	 		<s:else>
	 			<td  class="blueborderfortd">&nbsp;</td>
	 			<td  class="blueborderfortd">&nbsp;</td>
	 			<td  class="blueborderfortd">&nbsp;</td>
	 		</s:else>
 						 		
 		    </tr>
		    </s:iterator>
		    </s:if>
		    </s:if>
	 		 </s:if>
		    </s:iterator>
		</table>
	 </div>
	 </s:if>
	 <s:else>
	 <div align="center">
	 <p>No Details Found.</p>
	
	 </div>
	 
	 </s:else>
</div>
<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><input type="button" name="back" id="back" class="button" value="Back" onclick="history.back()"/></td>
			  		
			  	</tr>
	        </table>
	   </div>
	  </body>
</html>