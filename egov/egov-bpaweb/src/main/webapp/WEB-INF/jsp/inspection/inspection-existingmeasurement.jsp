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
window.open("${pageContext.request.contextPath}/inspection/inspection!modifyInspectionForm.action?inspectionId="+inspectionid+"&fromreg=true","simple","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");
//document.location.href="${pageContext.request.contextPath}/inspection/inspection!modifyInspectionForm.action?inspectionId="+inspectionid;
}



</SCRIPT>
  
<div id="measuredetailsreg" align="center"> 

 <s:if test="existingSiteInspectionDetails.size!=0">
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
		   	
		    <s:if  test="%{registration.egwStatus.code=='Considered'}">
		    
		     <th  class="bluebgheadtd" width="8%"><div align="center">Modify </div></th>		
		   </s:if>
		 </tr>
		  <s:iterator value="existingSiteInspectionDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{inspectionNum}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{inspectionDateString}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{inspectedBy.firstName}" /></div></td>
		   <s:url id="measurementlink" value="/inspection/inspection!showMeasurementDetails.action" escapeAmp="false">
		      <s:param name="inspectionId" value="%{id}"></s:param>	 
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		       <s:param name="serviceTypeId" value="%{registration.serviceType.id}"></s:param>	  
		   </s:url> 
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openRemoteMeasurementDialog" href="%{measurementlink}" button="true" buttonIcon="ui-icon-newwin">View</sj:a></div></td>		  
		       <s:if  test="%{registration.egwStatus.code=='Considered'}">
		    
		    <td  class="blueborderfortd"> <div align="center"><s:if test="%{#row_status.count==1}" ><a href="#" onclick="modifyinspection('<s:property value="id"/>')">
 						 		Modify</a></s:if></div></td>
 			</s:if>
		    </tr>
		    </s:iterator>
	  </table>
	 </div>
	 </s:if>

</div>
</div>