 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
 <%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="simple" scope="page" />
<SCRIPT>
  jQuery.noConflict();

</SCRIPT>
 <div id="prevfeedtls" align="center"> 
<s:if test="%{existingFeeDetails.size>0}">
  <h1 class="subhead" ><s:text name="Previous Fee Details"/></h1>
	
	<sj:dialog 
    	id="prevfeedtl" 
    	autoOpen="false" 
    	modal="true" 
    	title="Previous Fee Details"
    	openTopics="openprevfeeDialog"
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
	    <table id="previousfeedetailtable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	       
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Date</div></th>	
		    <th  class="bluebgheadtd" width="8%"><div align="center">Challan Number</div></th>	
		     <th  class="bluebgheadtd" width="8%"><div align="center">Status</div></th>		   
		    <th  class="bluebgheadtd" width="8%"><div align="center">Fee Details</div></th>
		  </tr>
		  <s:iterator value="existingFeeDetails" status="rowfee_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#rowfee_status.count" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{formattedFeeDate}" /></div></td>
		   <td  class="blueborderfortd"><div align="center"><s:property value="%{challanNumber}" /></div></td>
		   <td  class="blueborderfortd"><div align="center"><s:property value="%{egwStatus.code}" /></div></td>
		   
		    <s:if test="%{legacyFee!='true'}">
		   <s:url id="prevfeelink" value="/fee/revisedFee!showFeeDetails.action" escapeAmp="false">
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		      <s:param name="registrationFeeId" value="%{id}"></s:param>	
		   </s:url> 
		   </s:if>
		   <s:else>
		     <s:url id="prevfeelink" value="/fee/feeDetails!showFeeDetailsinRegistration.action" escapeAmp="false">
		       <s:param name="registrationId" value="%{registration.id}"></s:param>	
		        <s:param name="registrationFeeId" value="%{id}"></s:param>	
		      
		   </s:url> 	   
		   </s:else>
		  	<td  class="blueborderfortd"><div align="center"> <sj:a  onClickTopics="openprevfeeDialog" href="%{prevfeelink}" button="true" buttonIcon="ui-icon-newwin">View</sj:a></div></td>		  
		      
		    </tr>
		    </s:iterator>
		   
	  </table>
	 </div>
	 
 </s:if>
</div>