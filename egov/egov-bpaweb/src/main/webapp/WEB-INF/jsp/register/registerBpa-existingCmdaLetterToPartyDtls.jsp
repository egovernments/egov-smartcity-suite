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
 
 

		

		
		 function  printLetterToPartyCmda(letterToPartyId){
			document.location.href="${pageContext.request.contextPath}/report/bpaReport!printReport.action?letterToPartyCmdaId="+letterToPartyId;
			 }
		 
		 function printLPReplyCmdaAck(registrationId,letterToPartyId) {
			 document.location.href="${pageContext.request.contextPath}/lettertoparty/lpReplyCmda!ackPrint.action?registrationId="+registrationId+"&letterToPartyId="+letterToPartyId;
			 }
			
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

    
<div id="header" class="formmainbox" align="center">
	    <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Number</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">LP Reason</div></th>
		     <th  class="bluebgheadtd" width="8%"><div align="center">LP Description</div></th>
		     <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Date</div></th>		
		    <th  class="bluebgheadtd" width="8%"><div align="center">Print</div></th>	
		    	 <th  class="bluebgheadtd" width="8%"><div align="center">LP Sent Date</div></th>
			    	 <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Desciption</div></th>
			    	 <th  class="bluebgheadtd" width="8%"><div align="center">LP Reply Ack Print</div></th>		
			    	 <th  class="bluebgheadtd" width="8%"><div align="center">Uploaded Document</div></th>	
			    		
		 </tr> 
		  	 <s:iterator value="existingCmdaLetterToPartyDetails" status="cmda_Row_count">
		      <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#cmda_Row_count.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{letterToPartyNumber}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:date name="createdDate" format="dd/MM/yyyy" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{lpReason}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{lpReplyRemarks}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:date name="replyDate" format="dd/MM/yyyy" /></div></td>
		  	
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
		    
			
	  </table>
	 </div>
	
</div>
</div>