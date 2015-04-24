<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<s:set name="theme" value="'simple'" scope="page" />
 <SCRIPT>
  jQuery.noConflict();

jQuery(document).ready(function(){

 jQuery('#measuredetails').find('input,select').attr('disabled','true');	 

  });


</SCRIPT>
  
<div id="measuredetails" align="center"> 


<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />
 <SCRIPT>
  jQuery.noConflict();



 jQuery(document).ready(function(){
jQuery.subscribe('dialogopentopic', function(event,ui) {
			
	       jQuery('#measure').find('input,select').attr('disabled','true');	 
		});
 
jQuery.subscribe('errortopic', function(event,ui) {
			
	      alert("You have no permission to view this detail")
	        jQuery('#measure').dialog('close');   
		});
  

  });
 



function modifyinspection(inspectionid){
	document.location.href="${pageContext.request.contextPath}/extd/inspection/inspectionExtn!modifyInspectionForm.action?inspectionId="+inspectionid;
}

function printDocketSheet(inspectionid){
	document.location.href="${pageContext.request.contextPath}/extd/inspection/inspectionExtn!printDocketSheet.action?registrationId="+inspectionid;
}


</SCRIPT>
  
<div id="measuredetailsreg" align="center"> 

 <s:if test="existingSiteInspectionDetails.size!=0">
 <s:hidden id="serviceTypeCode" name="serviceTypeCode" value="%{registration.serviceType.code}"/>
  <h1 class="subhead" ><s:text name="Existing Inspection Details"/></h1>
	
	<sj:dialog 
    	id="measure" 
    	autoOpen="false" 
    	modal="true" 
    	title="Measurement Details"
    	openTopics="openRemoteMeasurementDialog"
    	height="500"
    	width="700"
    	dialogClass="formmainbox"
    	showEffect="slide" 
    	hideEffect="slide" 
    	onOpenTopics="openTopicDialog" cssStyle="BACKGROUND-COLOR: #ffffff"
    	onCompleteTopics="dialogopentopic" 
    	loadingText="Please Wait ...."
    	errorText="Permission Denied"
    />

  <div id="header" class="formmainbox" align="center">
	    <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Site Inspection Number</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Site Inspection Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Site InspectedBy</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Measurement Details</div></th>
		   	
		      <s:if  test="allowModifyInspection">
		    
		     <th  class="bluebgheadtd" width="8%"><div align="center">Modify </div></th>		
		   </s:if>
		 </tr>
		  <s:iterator value="existingSiteInspectionDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{inspectionNum}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{inspectionDateString}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{inspectedBy.firstName}" /></div></td>
		   <s:url id="measurementlink" value="/extd/inspection/inspectionExtn!showMeasurementDetails.action" escapeAmp="false">
		      <s:param name="inspectionId" value="%{id}"></s:param>	 
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		       <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	  
		   </s:url> 
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openRemoteMeasurementDialog" href="%{measurementlink}" button="true" buttonIcon="ui-icon-newwin">View</sj:a></div></td>		  
		       <s:if  test="allowModifyInspection">
		    <td  class="blueborderfortd"> <div align="center"><s:if test="%{#row_status.count==1}" ><a href="#" onclick="modifyinspection('<s:property value="id"/>')">
 						 		Modify</a></s:if></div></td>
 			</s:if>
		    </tr>
		    </s:iterator>
		  
		   
	  </table>
	 </div>
	
	 </s:if>
	  <div id="header" class="formmainbox" align="center">
	    <table id="postpsgdasonetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	    <tr>
	    <td class="bluebox" width="15%">&nbsp;</td>
	    <td class="bluebox" width="15%">&nbsp;</td>
	    <td class="bluebox" width="15%">&nbsp;</td>
	    <td class="bluebox" width="15%">&nbsp;</td>
	     <td class="bluebox" width="15%">&nbsp;</td>
	    </tr>
	 <tr>
	 <td class="greybox" width="10%">&nbsp;</td>
	
		  
 			<s:if test="%{ existingSiteInspectionDetails.size!=0   && !isUserMappedToSurveyorRole()}">
 		<s:if test="isInspectedByAEorAEE">
 			<td class="greybox" width="15%">
							  		 			<div align="center" >  
						 						<a href="#" onclick="printInspectionForm('<s:property value="%{registrationId}"/>')"  >
						 						 	<b>	Print Official InspectionDetails</b></a></div></td>
						 						  <td>
	   <div align="center"> 	<s:if test="%{(serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
 	serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
 	serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE) }"> 
		 <div align="center" >  
 			<a href="#" onclick="printDocketSheet('<s:property value="%{registrationId}"/>')"  >
 						 	<b>	Print DocketSheet </b></a></div>
 			</s:if></div>
 			</td>
						 						 	</s:if>
						 						<s:if test="%{!isUserMappedToSurveyorRole()  &&  (serviceTypeCode==@org.egov.bpa.constants.BpaConstants@NEWBUILDINGONVACANTPLOTCODE ||
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@DEMOLITIONRECONSTRUCTIONCODE || 
 serviceTypeCode==@org.egov.bpa.constants.BpaConstants@ADDITIONALCONSTRUCTIONCODE) }"> 
						 						 <td class="greybox" width="15%">
							  		 			<div align="center" >  
						 						<a href="#" onclick="printSurveyorInspectionForm('<s:property value="%{registrationId}"/>')"  >
						 						 	<b>	Print Surveyor InspectionDetails</b></a></div></td></s:if>
 			</s:if>
 		
						 			
 			</tr>
	 </table>
	 </div>

</div>
</div>