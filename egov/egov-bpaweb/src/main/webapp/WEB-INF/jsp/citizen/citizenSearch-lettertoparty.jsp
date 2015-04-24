<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>

<head>
 <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
	<title>View Letter</title>
	
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
    
<div id="header" class="formmainbox" align="center">
	    <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Number</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Reason</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Date</div></th>		
		    <th  class="bluebgheadtd" width="8%"><div align="center">Checklist</div></th>	
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Sent Date</div></th>	
		    <th  class="bluebgheadtd" width="8%"><div align="center">Print</div></th>	  
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Checklist</div></th>
			<th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Ack Print</div></th>	
		
		 </tr> 
		  <s:iterator value="existingLetterToPartyDetails" status="row_status">
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
	 		</s:if>	
	 		<s:else>
	 			<td  class="blueborderfortd"></td>		  
	 			<td  class="blueborderfortd"></td>		
	 		</s:else>
	 		 </tr>
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