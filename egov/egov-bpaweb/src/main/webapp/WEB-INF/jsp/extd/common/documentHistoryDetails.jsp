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
<%@ include file="/includes/taglibs.jsp" %>

 <sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true"/>
<s:set name="theme" value="'simple'" scope="page" />
	<s:if test="%{serviceTypeCode}">
	<sj:dialog 
    	id="measure1" 
    	autoOpen="false" 
    	modal="true" 
    	title="Document History Details"
    	openTopics="openViewReceiptDialog1"
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
	<h1 class="headingsmallbg" align="center"><s:text name="Surveyor details"/></h1>
 <div id="header" class="formmainbox">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center"> 
	    <tr>
          <s:url id="receiptlink1" value="/extd/register/registerBpaExtn!showSurveyorDocHistoryDetails.action" escapeAmp="false">
			       <s:param name="registrationId" value="%{id}"></s:param>	
			   </s:url> 
			   
			  	<td  class="bluebox">
			  		<div align="center"> 
			  			<sj:a  onClickTopics="openViewReceiptDialog1" href="%{receiptlink1}" button="true" buttonIcon="ui-icon-newwin">
			  				View Document History
			  				
			  			</sj:a>
			  		</div>
			  	</td>
			  	<td class="bluebox"/>
			  	<td  class="bluebox">
			  	<s:if test="%{registration.documenthistorySet.size!=null}">
							  <div align="center" >  
						 		<a href="#" onclick="printDocumentHistorySheetForSurveyor('<s:property value="%{registrationId}"/>')"  >
						 			<b>	Print Surveyor DocumentHistory </b></a></div>
						 		</s:if>
						 		</td>
						 		
		
          </tr>
          </table>
        </div> 
        </s:if>
        <table>
        <tr>
        <s:if test=" isDocHistoryByAEorAEE ">
 		  <td class="greybox" width="15%">	
						 				<div align="center" >  
						 						<a href="#" onclick="printDocumentHistorySheet('<s:property value="%{registrationId}"/>')"  >
						 						 <b>	Print Official DocumentHistory </b></a></div></td>
						 			 </s:if>
        </tr>
        </table>
         			
	  		<div id="feesdetailsid" >
					        <table id="dochistorydetails" width="60%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
		                 	<div class="blueshadow"></div>
	                    	<div class="headingsmallbg"><span class="bold"><s:text name="Document History Sheet"/></span></div>
	 						<div align="center">
	     					 <tr>
	     					  <th  class="bluebgheadtd" width="10%"><div align="center">sl No<span class="mandatory">*</span>:</div></th>
	     					  <th  class="bluebgheadtd" width="10%"><div align="center">Nature of Document<span class="mandatory">*</span>:</div></th>
					         <th  class="bluebgheadtd" width="10%"><div align="center">Date of Registration<span class="mandatory">*</span>:</div></th>
					       <th  class="bluebgheadtd" width="10%"><div align="center">Document Number<span class="mandatory">*</span>:</div></th>
								<th  class="bluebgheadtd" width="10%"><div align="center">Vendor<span class="mandatory">*</span>:</div></th>
					            <th  class="bluebgheadtd" width="10%"><div align="center">Purchaser<span class="mandatory">*</span>:</div></th>
					           <th  class="bluebgheadtd" width="10%"><div align="center">Extent in Sq.Mtr<span class="mandatory">*</span>:</div></th>
					    <th  class="bluebgheadtd" width="10%"><div align="center">Survey Number<span class="mandatory">*</span></div></th>
					        <th  class="bluebgheadtd" width="10%"><div align="center">North Boundary<span class="mandatory">*</span>:</div></th>
					        <th  class="bluebgheadtd" width="10%"><div align="center">South Boundary<span class="mandatory">*</span>:</div></th>
					        <th  class="bluebgheadtd" width="10%"><div align="center">East Boundary<span class="mandatory">*</span>:</div></th>
					         
					         <th  class="bluebgheadtd" width="10%"><div align="center">West Boundary<span class="mandatory">*</span>:</div></th>
					        <th  class="bluebgheadtd" width=10%"><div align="center">Remarks</div></th>
					        
					         	<s:if test="%{(mode!='view' && stateForValidate!='' && stateForValidate=='Forward to AEORAEE' || (registration.state==null && stateForValidate==''))}">
					  <th  class="bluebgheadtd" width="2%"><div align="center"> Delete</div></th>
						</s:if>
						  </tr>
						     <s:iterator value="documentHistoryDetailList" status="row_status">
							    <tr>
							     <td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].srlNo" id="srlNo" readonly="true"  cssClass="tablerow" value="%{#row_status.count}" cssStyle="text-align:center"/></td>
							    <td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].natureOfDeed" id="natureOfDeed" cssClass="tablerow" maxlength="20"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td class="blueborderfortd"><div align="center"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].registartionDate" id="registartionDate" maxlength="10"  onchange="validateExpFromDt(this);" cssClass="tablerow" /></div></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].referenceNumber" id="referenceNumber" cssClass="tablerow"  maxlength="20"  cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].vendor" id="vendor" cssClass="tablerow"  maxlength="20" cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].purchaser" id="purchaser" cssClass="tablerow"   maxlength="20"cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].extentInsqmt" id="extentInsqmt" maxlength="20" cssClass="tablerow"   cssStyle="text-align:right" onblur="validateIsNan(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].surveyNumber" id="surveyNumber" maxlength="20" cssClass="tablerow"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
					          	<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].northBoundary" id="northBoundary" cssClass="tablerow"  maxlength="20"  cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].southBoundary" id="southBoundary" cssClass="tablerow"  maxlength="20"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].eastBoundary" id="eastBoundary" cssClass="tablerow" maxlength="20"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].westBoundary" id="westBoundary" cssClass="tablerow"  maxlength="20"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								
								<td  class="blueborderfortd"><s:textarea  name="documentHistoryDetailList[%{#row_status.index}].remarks" id="remarks" cssClass="tablerow"   cssStyle="text-align:right" maxlength="25"/></td>
								
							<s:if test="%{(mode!='view' && stateForValidate!='' && stateForValidate=='Forward to AEORAEE' || (registration.state==null && stateForValidate==''))}">
							   		
							<td id="deleteRowId" class="blueborderfortd"><div align="center"><a id = "deleteFloorId"  href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removeRow(this);" ></a></div></td>
							   </s:if>
							   <s:hidden id="dochistoryDetailId" name="documentHistoryDetailList[%{#row_status.index}].id" />
							   </tr>
							
							
							   </s:iterator>
							   
							  
	  					</table> 
	  					 <table id="docTempDivForShadowtable" width="65%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
	  					<tr id="docTempDivForShadow" >
	  					  <td  class="blueborderfortd"  style="background-color:#e7e9e9"><s:textfield  id="srlNoStatic" readonly="true" value="%{documentHistoryDetailList.size+ 1}" cssClass="tablerow"  onclick="addRow()" cssStyle="text-align:center"/></td>
							   
							   <td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield id="natureOfDeed1" cssClass="tablerow" maxlength="256"   cssStyle="text-align:right" readonly="true" onclick="addRow()"/></td>
								<td class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield id="registartionDate1"  readonly="true" cssStyle="text-align:right"  cssClass="tablerow" onclick="addRow()"/></div></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield id="referenceNumber1" cssClass="tablerow"  maxlength="256"  cssStyle="text-align:right" onclick="addRow()" readonly="true"/></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield  id="vendor1" cssClass="tablerow"   cssStyle="text-align:right"  onclick="addRow()" readonly="true"/></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield  id="purchaser1" cssClass="tablerow"   cssStyle="text-align:right" onclick="addRow()" readonly="true"/></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield id="extentInsqmt1" cssClass="tablerow"   cssStyle="text-align:right" onclick="addRow()" readonly="true"/></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield  id="surveyNumber1" cssClass="tablerow"   cssStyle="text-align:right"readonly="true" onclick="addRow()"/></td>
					          	<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield  id="northBoundary" cssClass="tablerow"  maxlength="256"  readonly="true" cssStyle="text-align:right" onclick="addRow()" /></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield id="southBoundary" cssClass="tablerow"  maxlength="256"   readonly="true" cssStyle="text-align:right" onclick="addRow()"/></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9" ><s:textfield id="eastBoundary" cssClass="tablerow" maxlength="256"   readonly="true" cssStyle="text-align:right" onclick="addRow()"/></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textfield id="westBoundary" cssClass="tablerow"  maxlength="256"  readonly="true" cssStyle="text-align:right" onclick="addRow()" /></td>
								<td  class="blueborderfortd" style="background-color:#e7e9e9"><s:textarea   id="remarks" cssClass="tablerow"   cssStyle="text-align:right" readonly="true" maxlength="256" onclick="addRow()"/></td>
								<td id="deleteRowId" style="background-color:#e7e9e9" class="blueborderfortd"><div align="center"><a id = "deleteFloorId"  href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" ></a></div></td>
								<td  class="blueborderfortd"><span class="mandatory">* Click on this Line To Add Row..</span></td>
						</tr>
						
	  					</table>
	  					
	  				</div>
	  				<div id="docEnclosedDiv"> 
	  					<table id="dochistoryEnclosedDetails" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
	  						
	  						<div class="blueshadow"></div>
	  						<tr id="docandLayoutDiv">
	 						<td class="greybox" width="20%">&nbsp;</td>
	 						<td class="greybox" width="20%"><s:text name="Whether Document Prior to 05/08/1975 enclosed?" /> : <span class="mandatory" >*</span></td>
        					 <td   width="20%" class="greybox"><s:radio id="wheatherdocumentEnclosed" name="documentHistory.wheatherdocumentEnclosed"  value="%{documentHistory.wheatherdocumentEnclosed}" list="#{'true':'YES','false':'NO'}" onchange="onChangeOfDocumentEnclosed();checkdocumentDetailCheckBoxOnload();"></s:radio></td>
        						
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        					</tr>
        					<tr id="documentNumberDiv">
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="10%"><s:text name="Document Number" /> : <span class="mandatory" >*</span></td>
       						 <td class="bluebox"><s:textfield id="documentNum" name="documentHistory.documentNum" value="%{documentHistory.documentNum}" onblur="checkNotSpecial(this);" maxlength="256"  onchange="checkdocumentDetailCheckBoxOnload();" /></td>
	 						<td class="bluebox" width="10%"><s:text name="Date of Registration" /> : <span class="mandatory" >*</span></td>
        					<td  class="bluebox" ><sj:datepicker id="documentDate" name="documentHistory.documentDate" value="%{documentHistory.documentDate}" cssClass="tablerow"  maxlength="10" displayFormat="dd/mm/yy"  showOn="focus" onchange="validateExpFromDt(this);checkdocumentDetailCheckBoxOnload();"/></td>
							<td class="bluebox" >&nbsp;</td>
						 	<td class="bluebox" width="10%"><s:text name="Extent of Land in Sq.Mtr as per document" /> : <span class="mandatory" >*</span></td>
					        <td class="bluebox"><s:textfield id="docEnclosedextentInsqmt" name="documentHistory.docEnclosedextentInsqmt" value="%{documentHistory.docEnclosedextentInsqmt}"  onblur="validateIsNan(this);" onchange="checkdocumentDetailCheckBoxOnload();" /></td>
	 					<td class="bluebox" >&nbsp;</td>
	 					<td class="bluebox" width="20%">&nbsp;</td>
	 					<td class="bluebox" width="20%">&nbsp;</td>
	 					</tr>
	 					
        				<tr id="docapprovedDiv">
        				<td class="bluebox" width="20%">&nbsp;</td>
        				<td class="bluebox" width="20%"><s:text name="Whether Plot lies in an approved layout ?" /> : <span class="mandatory" >*</span></td>
        				 <td   class="bluebox"><s:radio id="partOfLayout" name="documentHistory.wheatherpartOfLayout"  value="%{documentHistory.wheatherpartOfLayout}"  list="#{'true':'YES','false':'NO'}" onchange="onChangeOfPartOfLayout();checkdocumentDetailCheckBoxOnload();" ></s:radio></td>
        					<td class="bluebox" >&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					</tr>
        					
        					<tr id="plotDevelopNewDiv">
        					<td class="greybox" width="20%">&nbsp;</td>
        				<td  class="greybox" width="20%"><s:text name="Developed by?" /> : <span class="mandatory" >*</span></td>
        				 <td   width="20%" class="greybox"><s:radio id="plotDevelopBy" name="documentHistory.plotDevelopedBy"  value="%{documentHistory.plotDevelopedBy}"  list="#{'true':'Government','false':'Individual'}" onchange="onChangeOfPlotDevelopedBy();checkdocumentDetailCheckBoxOnload();" ></s:radio></td>
        					<td class="greybox" >&nbsp;</td>
        					<td class="greybox" >&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					</tr>
	 						
	 						<tr id="plotdevFmsSketchDiv">
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%"><s:text name="Who has Developed?" /> : <span class="mandatory" >*</span></td>
        					 <td   width="20%" class="bluebox"><s:radio id="wheatherplotDevelopedBy" name="documentHistory.wheatherplotDevelopedBy"  value="%{documentHistory.wheatherplotDevelopedBy}"  list="#{'TNHB':'TNHB','TNSCB':'TNSCB','CMDA':'CMDA','SIDCO':'SIDCO'}" onchange="checkdocumentDetailCheckBoxOnload();"></s:radio></td>
        				<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 				
	 						</tr>
        					<tr id="fmsfullDiv">
	 						<td class="greybox" width="20%">&nbsp;</td>
	 						<td class="greybox" ><s:text name="Whether FMB Sketch / Copy of A Register prior to 05/08/1975 enclosed?" /> : <span class="mandatory" >*</span></td>
        					<td class="greybox"><s:radio  id="FmsOrSketchCopyOfReg" value="%{documentHistory.wheatherFmsOrSketchCopyOfReg}" name="documentHistory.wheatherFmsOrSketchCopyOfReg" list="#{'true':'YES','false':'NO' }" onchange="onChangeOfFMSSketchORCopyofRegn();checkdocumentDetailCheckBoxOnload();" /></td>
        					<td class="greybox" >&nbsp;</td>
        					<td id="showmes" class="greybox" align="left" width="15%"><span class="mandatory">* 10 % of OSR Charges have to be paid.</span></td>
        					</div>
        						<td class="greybox" width="20%">&nbsp;</td>
        							<td class="greybox" width="20%">&nbsp;</td>
        							<td class="greybox" width="20%">&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					
	 						</tr>
					<tr id="partofLaout">
        				<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="10%"><s:text name="Layout Number" /> : <span class="mandatory" >*</span></td>
					        <td class="bluebox"><s:textfield id="layoutdextentInsqmt" name="documentHistory.layoutdextentInsqmt" value="%{documentHistory.layoutdextentInsqmt}"  onblur="validateIsNan(this);" onchange="checkDocuMentCheckBox(this);"/></td>
	 	 			<td class="bluebox" width="20%">&nbsp;</td>
	 	 			<td class="bluebox" width="20%">&nbsp;</td>
	 	 			<td class="bluebox" width="20%">&nbsp;</td>
	 	 			<td class="bluebox" width="20%">&nbsp;</td>
	 	 			<td class="bluebox" width="20%">&nbsp;</td>
	 	 			<td class="bluebox" width="20%">&nbsp;</td>
	 	 			<td class="bluebox" width="20%">&nbsp;</td>
	 	 			</tr>
	 	 			<s:hidden id="documentHistoryId" name="documentHistory.id" value="%{documentHistory.id}" />
	 	 			</table>
	  				</div >
	  				
 <script>

 
var index=0;
jQuery.noConflict();

jQuery(document).ready(function(){

	
	  if(jQuery('#mode').val()!='view'){
		   jQuery("[id=registartionDate]").each(function(index){
		   jQuery(this).datepicker({ dateFormat: 'dd/mm/yy'});
		   jQuery(this).datepicker( "option", "maxDate", "0" );
		    });
		    }else{
		    jQuery("[id=registartionDate]").each(function(index){
		    jQuery(this).datepicker({ dateFormat: 'dd/mm/yy'});
		    jQuery(this).datepicker('getDate');   
		    jQuery(this).datepicker( "option", "dateFormat", "dd/mm/yy" );
		    jQuery(this).datepicker( "option", "maxDate", "0" );
		    });
		    }
	  jQuery('#documentDate').datepicker( "option", "maxDate", "0" );
	  
});


function dtval(d,e) {
	var pK = e ? e.which : window.event.keyCode;
	if (pK == 8) {d.value = substr(0,d.value.length-1); return;}
	var dt = d.value;
	var da = dt.split('/');
	for (var a = 0; a < da.length; a++) {if (da[a] != +da[a]) da[a] = da[a].substr(0,da[a].length-1);}
	if (da[0] > 31) {da[1] = da[0].substr(da[0].length-1,1);da[0] = '0'+da[0].substr(0,da[0].length-1);}
	if (da[1] > 12) {da[2] = da[1].substr(da[1].length-1,1);da[1] = '0'+da[1].substr(0,da[1].length-1);}
	if (da[2] > 9999) da[1] = da[2].substr(0,da[2].length-1);
	dt = da.join('/');
	if (dt.length == 2 || dt.length == 5) dt += '/';
	d.value = dt;

	
	}

function onChangeOfDocumentEnclosed()
{
	var fixAmt = jQuery("input[name='documentHistory.wheatherdocumentEnclosed']:checked").val();
		if( fixAmt!= "true"){
			jQuery('#docapprovedDiv').show();
			jQuery('#documentNumberDiv').hide();
			jQuery('#fmsfullDiv').hide();
			jQuery('#plotDevelopNewDiv').hide();
			jQuery('#partofLaout').hide();
			document.getElementById("documentDate").value="";
			document.getElementById("docEnclosedextentInsqmt").value="";
			document.getElementById("documentNum").value="";
		}		
		else{
			jQuery("input[name='documentHistory.wheatherpartOfLayout']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.plotDevelopedBy']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.wheatherplotDevelopedBy']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.wheatherFmsOrSketchCopyOfReg']:checked").prop('checked', false);
			jQuery('#documentNumberDiv').show();
			jQuery('#docapprovedDiv').hide();
			jQuery('#plotDevelopNewDiv').hide();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#fmsfullDiv').hide();
			jQuery('#partofLaout').hide();
			
			document.getElementById("layoutdextentInsqmt").value="";
		}
		
	}
function onChangeOfPartOfLayout()
{
	 var fixAmt = jQuery("input[name='documentHistory.wheatherpartOfLayout']:checked").val();
		//alert("serviceTypeValue "+fee);
		if(fixAmt != "true"){
			jQuery('#fmsfullDiv').show();
			jQuery('#partofLaout').hide();
			jQuery('#documentNumberDiv').hide();
			jQuery('#plotDevelopNewDiv').hide();
			jQuery('#plotdevFmsSketchDiv').hide();
			document.getElementById("layoutdextentInsqmt").value="";
			
			jQuery('#showmes').hide();
			
		}		
		else{
			jQuery("input[name='documentHistory.plotDevelopedBy']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.wheatherplotDevelopedBy']:checked").prop('checked', false);
			jQuery("input[name='documentHistory.wheatherFmsOrSketchCopyOfReg']:checked").prop('checked', false);
			jQuery('#partofLaout').hide();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#partofLaout').hide();
			jQuery('#fmsfullDiv').hide();
			jQuery('#plotDevelopNewDiv').show();
			jQuery('#docapprovedDiv').show();
			jQuery('#docandLayoutDiv').show(); 
			jQuery('#documentNumberDiv').hide();
			
		}
		
		
	}


function onChangeOfFMSSketchORCopyofRegn()
{
	var fixAmt = jQuery("input[name='documentHistory.wheatherFmsOrSketchCopyOfReg']:checked").val();
		if(fixAmt == "false" ||  fixAmt ==  undefined){
			jQuery('#showmes').show();
			}		
		else{
			jQuery('#showmes').hide();
			
		}
		
	}
function onChangeOfPlotDevelopedBy()
{
	var fixAmt = jQuery("input[name='documentHistory.plotDevelopedBy']:checked").val();
		if(fixAmt != "true"){
			jQuery('#partofLaout').show();
			jQuery('#plotdevFmsSketchDiv').hide();
			jQuery('#docapprovedDiv').show();
			jQuery('#plotDevelopNewDiv').show();
			jQuery('#docandLayoutDiv').show();
			jQuery("input[name='documentHistory.wheatherplotDevelopedBy']:checked").prop('checked', false);
			}		
		else{
			jQuery('#plotDevelopNewDiv').show();
			jQuery('#docapprovedDiv').show();
			jQuery('#docandLayoutDiv').show();
			jQuery('#partofLaout').hide();
			jQuery('#fmsfullDiv').hide();
			jQuery('#plotdevFmsSketchDiv').show();
			document.getElementById("layoutdextentInsqmt").value="";
		}
		
		
	}

 function checkNotSpecial(obj)
 {

 	var iChars = "!@$%^*+=[']`~';{}|\":<>?#_";
 	for (var i = 0; i < obj.value.length; i++)
 	{
 		if ((iChars.indexOf(obj.value.charAt(i)) != -1)||(obj.value.charAt(0)==" "))
 	{
     dom.get("bpa_error_area").style.display='';
 	document.getElementById("bpa_error_area").innerHTML='Special characters are not allowed';
 	obj.value="";
 	obj.focus();
 	return false;
 	}
    }
 	return true;
 }	
 function validateIsNan(obj)
 {
	 dom.get("bpa_error_area").style.display='none';
	    if(obj!=null && obj.value!=null && isNaN(obj.value))
	    {
	    	dom.get("bpa_error_area").style.display='';
			document.getElementById("bpa_error_area").innerHTML='Pleas Enter only Numbers'
			 dom.get(obj).value = ""; 
		 return false;
	    }
	   return true; 
	 }		
	function addRow()
 { 
		  if(validateDocHistoryDetail()){
		 jQuery("[id^=registartionDate]").each(function(index){
		    	 jQuery(this).datepicker('destroy');
		    });
		 
 	    	var tableObj=document.getElementById('dochistorydetails');
 			var tbody=tableObj.tBodies[0];
 			var lastRow = tableObj.rows.length;
 			document.getElementById('srlNoStatic').value=lastRow+1;
 			var rowObj = tableObj.rows[1].cloneNode(true);
 			tbody.appendChild(rowObj);
 			var rowno = parseInt(tableObj.rows.length)-2;
 			document.forms[0].srlNo[lastRow-1].value=tableObj.rows.length - 1;								
 			document.forms[0].surveyNumber[lastRow-1].value="";
 			document.forms[0].vendor[lastRow-1].value="";
 			document.forms[0].purchaser[lastRow-1].value="";
 			document.forms[0].extentInsqmt[lastRow-1].value="";
            document.forms[0].dochistoryDetailId[lastRow-1].value="";

        	document.forms[0].natureOfDeed[lastRow-1].value="";
 			document.forms[0].registartionDate[lastRow-1].value="";
 			document.forms[0].referenceNumber[lastRow-1].value="";
 			document.forms[0].northBoundary[lastRow-1].value="";
            document.forms[0].southBoundary[lastRow-1].value="";
        	document.forms[0].westBoundary[lastRow-1].value="";
 			document.forms[0].eastBoundary[lastRow-1].value="";
 			document.forms[0].remarks[lastRow-1].value="";
 		    document.forms[0].srlNo[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].srlNo");
 			//document.forms[0].documentNumAndYear[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].documentNumAndYear");
 			document.forms[0].surveyNumber[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].surveyNumber");
            document.forms[0].vendor[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].vendor");
            document.forms[0].purchaser[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].purchaser");
            document.forms[0].extentInsqmt[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].extentInsqmt");
            document.forms[0].natureOfDeed[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].natureOfDeed");
            document.forms[0].registartionDate[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].registartionDate");
            document.forms[0].referenceNumber[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].referenceNumber");
            document.forms[0].northBoundary[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].northBoundary");
			document.forms[0].southBoundary[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].southBoundary");
            document.forms[0].westBoundary[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].westBoundary");
            document.forms[0].eastBoundary[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].eastBoundary");
			document.forms[0].remarks[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].remarks");

     		document.forms[0].dochistoryDetailId[lastRow-1].setAttribute("name","documentHistoryDetailList["+index+"].id");
 			index++;
 			  jQuery("[id=registartionDate]").each(function(index){
 				 jQuery(this).datepicker({
 				  dateFormat: 'dd/mm/yy',
 				 beforeShow: function(date) {
 				            jQuery("[id=registartionDate]").each(function(index){
 				               jQuery(this).attr('id','newid');
 				            });
 				            jQuery(this).attr('id','registartionDate');
 				           jQuery(this).datepicker( "option", "maxDate", "0" );	
 				        },
 				  onClose: function(date) {
 				            jQuery("[id=newid]").each(function(index){
 				               jQuery(this).attr('id','registartionDate');
 				            });
 				          
 				        }       
 				});
 				 });
 			
 	
		}
 		
   }
	   
	/*
	 * this method is for validate the floore details...
	 */

		function validateDocHistoryDetail()
		{
		
	    var tableObj=document.getElementById('dochistorydetails');
	    var lastRow = tableObj.rows.length;
	    var docyear,survenum,vendor,purchaser,extentInsqmt;
	    var i;
	    if(lastRow>2)
	    {
	    	
	    	for(i=0;i<lastRow-1;i++)
	    	{
	        	
	    		//docyear=document.forms[0].documentNumAndYear[i].value;
	    		survenum=document.forms[0].surveyNumber[i].value;
	    		vendor=document.forms[0].vendor[i].value;
	    		purchaser=document.forms[0].purchaser[i].value;
	    		extentInsqmt=document.forms[0].extentInsqmt[i].value;
	    		natureOfDeed=document.forms[0].natureOfDeed[i].value;
	    		registartionDate=document.forms[0].registartionDate[i].value;
	    		referenceNumber=document.forms[0].referenceNumber[i].value;

	    		northBoundary=document.forms[0].northBoundary[i].value;
	    		southBoundary=document.forms[0].southBoundary[i].value;
	    		westBoundary=document.forms[0].westBoundary[i].value;
	    		eastBoundary=document.forms[0].eastBoundary[i].value;
	    		remarks=document.forms[0].remarks[i].value;
	    		
	         	
	          	 if(!validatedocHistoryLines(survenum,vendor,purchaser,extentInsqmt,natureOfDeed,registartionDate,referenceNumber
	    	          	 ,northBoundary,southBoundary,westBoundary,eastBoundary,remarks,i+1))
	            	   return false;
	     		}
	   		
	   		return true;
	   	}
	   	else
	   	{
	   		//docyear=document.getElementById('documentNumAndYear').value;
	   		survenum=document.getElementById('surveyNumber').value;
	   		vendor=document.getElementById('vendor').value;
	   		purchaser=document.getElementById('purchaser').value;
	   		extentInsqmt=document.getElementById('extentInsqmt').value;
	   		natureOfDeed=document.getElementById('natureOfDeed').value;
	   		registartionDate=document.getElementById('registartionDate').value;
	   		referenceNumber=document.getElementById('referenceNumber').value;
	   		northBoundary=document.getElementById('northBoundary').value;
	   		southBoundary=document.getElementById('southBoundary').value;
	   		westBoundary=document.getElementById('westBoundary').value;
	   		eastBoundary=document.getElementById('eastBoundary').value;
	   		remarks=document.getElementById('remarks').value;
	   		
	   	    if(!validatedocHistoryLines(survenum,vendor,purchaser,extentInsqmt,natureOfDeed,registartionDate,referenceNumber
   	          	 ,northBoundary,southBoundary,westBoundary,eastBoundary,remarks,1))
	   	       return false;
	   	     else
	   	       return true;
	   
	   	}
	   	
	  }

		function validateExpFromDt(inputExpFromDt) {
		   var validformat=/^\d{2}\/\d{2}\/\d{4}$/; //Basic check for format validity
		  if (!validformat.test(inputExpFromDt.value)){
			  alert("Invalid Date. Please ensure it is in dd/mm/yyyy format.");
				return false;
		  }
		  
		}
	function ValidateDate(obj)
	{
		var todaysDate=getTodayDate();
		//alert("validate "+todaysDate);
		var docDate=obj;
			if((docDate!=null && docDate!="" && docDate!=undefined )){
				if ((Date.parse(docDate) > Date.parse(todaysDate))) {
			        alert("Document Date Should not be greater than Todays Date");
			        document.getElementById("docDate").value = "";
			    }
				/*if(compareDate(docDate,todaysDate) == -1)
				{						  	 	
					 dom.get("bpa_error_area").style.display = '';
					 document.getElementById("bpa_error_area").innerHTML = 'Document Date Should not be greater than Todays Date'	;	  					 
					 document.getElementById('documentDate').value=""; 
					 document.getElementById('documentDate').focus();
					 return false;
				}
				else{
					 dom.get("bpa_error_area").style.display = '';
					 document.getElementById("bpa_error_area").innerHTML ="";
					}*/
				
			}
		}		  
	function validateDocumentEvidence()
	{
	var docEnclosed=jQuery("input[name='documentHistory.wheatherdocumentEnclosed']:checked").val();
	var partlayout=jQuery("input[name='documentHistory.wheatherpartOfLayout']:checked").val();
	var plotdevelop=jQuery("input[name='documentHistory.plotDevelopedBy']:checked").val();
	var fixAmt = jQuery("input[name='documentHistory.wheatherFmsOrSketchCopyOfReg']:checked").val();
	var plotDevebywhom=jQuery("input[name='documentHistory.wheatherplotDevelopedBy']:checked").val();
	if(docEnclosed== undefined){
		document.getElementById("bpa_error_area").innerHTML = '';
		document.getElementById("bpa_error_area").innerHTML = 'Please Select Whether Document Prior to 05/08/1975 enclosed? In Document Details tab';
		dom.get("bpa_error_area").style.display = '';
		return false;
		}
  	if(docEnclosed=="true")
	   {
				if (document.getElementById('documentNum').value == ""|| document.getElementById('documentNum').value == null) {

					document.getElementById("bpa_error_area").innerHTML = '';
					document.getElementById("bpa_error_area").innerHTML = 'Please Enter Document Number In Document History ';
					dom.get("bpa_error_area").style.display = '';
					return false;
				}
				if ( document.getElementById('documentDate').value == null ||document.getElementById('documentDate').value == "" ||document.getElementById('documentDate').value == undefined) {
					document.getElementById("bpa_error_area").innerHTML = '';
					document.getElementById("bpa_error_area").innerHTML = 'Please Enter Document Date In Document History';
					dom.get("bpa_error_area").style.display = '';
					return false;
				}
				  var validformat=/^\d{2}\/\d{2}\/\d{4}$/; //Basic check for format validity
				  if (!validformat.test(document.getElementById('documentDate').value)){
					  alert( "Invalid Date. Please ensure it is in dd/mm/yyyy format and submit again.");
						return false;
				  }
				//ValidateDate(document.getElementById('documentDate').value);

				if (document.getElementById('docEnclosedextentInsqmt').value == ""|| document.getElementById('docEnclosedextentInsqmt').value == null) {

					document.getElementById("bpa_error_area").innerHTML = '';
					document.getElementById("bpa_error_area").innerHTML = 'Please Enter Document Enclosed Extent In Sq.mtr in Document History';
					dom.get("bpa_error_area").style.display = '';
					return false;
				}
			}
  		if(docEnclosed=="false" && partlayout== undefined){
		document.getElementById("bpa_error_area").innerHTML = '';
		document.getElementById("bpa_error_area").innerHTML = 'Please Select Whether Plot lies in an approved layout ? In Document List tab';
		dom.get("bpa_error_area").style.display = '';
		return false;
		}
  		if(docEnclosed=="false" && partlayout== "true" && plotdevelop== undefined){
  			document.getElementById("bpa_error_area").innerHTML = '';
  			document.getElementById("bpa_error_area").innerHTML = 'Please Select Developed by? in Document List tab';
  			dom.get("bpa_error_area").style.display = '';
  			return false;
  			}
  		if(docEnclosed=="false" && partlayout== "true" && plotdevelop== "true" && plotDevebywhom== undefined)
  	  		{
  			document.getElementById("bpa_error_area").innerHTML = '';
  			document.getElementById("bpa_error_area").innerHTML = 'Please Select Who has Developed?  in Document List tab';
  			dom.get("bpa_error_area").style.display = '';
  			return false;
  	  		}
			if (docEnclosed =="false" && partlayout == "true" && plotdevelop =="false" && (document.getElementById('layoutdextentInsqmt').value == "" || document.getElementById('layoutdextentInsqmt').value == null)) 
				{
				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Please Enter Part Of Layout In Document History ';
				dom.get("bpa_error_area").style.display = '';
				return false;

			}
			if(docEnclosed=="false" && partlayout== "false" && fixAmt== undefined){
	  			document.getElementById("bpa_error_area").innerHTML = '';
	  			document.getElementById("bpa_error_area").innerHTML = 'Please Select Whether FMS Sketch / Copy of "A" Registered? in Document List tab';
	  			dom.get("bpa_error_area").style.display = '';
	  			return false;
	  			}
			return true;
		}
		function validatedocHistoryLines(survenum, vendor, purchaser,
				extentInsqmt, natureOfDeed, registartionDate, referenceNumber,
				northBoundary, southBoundary, westBoundary, eastBoundary,
				remarks, row) {
			dom.get("bpa_error_area").style.display = 'none';
			/*if(docyear=="" || docyear==null)
			 	{
			     	
			    	document.getElementById("bpa_error_area").innerHTML='';
					document.getElementById("bpa_error_area").innerHTML='Enter DocumentNumber/Year for row'+" :"+row;
					dom.get("bpa_error_area").style.display='';
					return false;
			    }*/
			
				if (natureOfDeed == "" || natureOfDeed == null) {

					document.getElementById("bpa_error_area").innerHTML = '';
					document.getElementById("bpa_error_area").innerHTML = 'Enter Nature Of Document for row'
							+ " :" + row +' for Document History in Document details tab';
					dom.get("bpa_error_area").style.display = '';
					return false;
				}
				if (registartionDate == "" || registartionDate == null) {

					document.getElementById("bpa_error_area").innerHTML = '';
					document.getElementById("bpa_error_area").innerHTML = 'Enter Document Date of Registration  for row'
							+ " :" +  row  +' for Document History in Document details tab';
					dom.get("bpa_error_area").style.display = '';
					return false;
				}
				 var validformat=/^\d{2}\/\d{2}\/\d{4}$/; //Basic check for format validity
				  if (!validformat.test(registartionDate)){
					 alert("Invalid Date. Please ensure it is in dd/mm/yyyy format and submit again.");
						return false;
				  }
					//ValidateDate(registartionDate);
				if (referenceNumber == "" || natureOfDeed == null) {

					document.getElementById("bpa_error_area").innerHTML = '';
					document.getElementById("bpa_error_area").innerHTML = 'Enter Document Number for row'
							+ " :" + row +' for Document History in Document List tab';
					dom.get("bpa_error_area").style.display = '';
					return false;
				}

			
			if (purchaser == "" || purchaser == null) {

				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter Purchaser for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
			if (vendor == "" || vendor == null) {

				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter Vendor for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
			if (extentInsqmt == "" || extentInsqmt == null) {

				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter ExtentInSqMt for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
			if (survenum == "" || survenum == null) {

				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter Survey Number for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
		
			if (northBoundary == "" || northBoundary == null) {

				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter North Boundary for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
			if (southBoundary == "" || southBoundary == null) {
				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter South Boundary  for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
			if (westBoundary == "" || westBoundary == null) {

				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter West Boundary for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
			if (eastBoundary == "" || eastBoundary == null) {

				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Enter East Boundary for row'
						+ " :" + row +' for Document History in Document List tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}
			/*if(remarks=="" || remarks==null)
				{
			     	
			   	document.getElementById("bpa_error_area").innerHTML='';
					document.getElementById("bpa_error_area").innerHTML='Enter Remarks for row'+" :"+row;
					dom.get("bpa_error_area").style.display='';
					return false;
			   }*/
			if (survenum == "" || vendor == "" || purchaser == ""
					|| extentInsqmt == "" || eastBoundary == ""
					|| westBoundary == "" || northBoundary == ""
					|| southBoundary == "" || referenceNumber == ""
					|| registartionDate == "" || natureOfDeed == "") {
				// alert("FromAreasqmt");
				document.getElementById("bpa_error_area").innerHTML = '';
				document.getElementById("bpa_error_area").innerHTML = 'Document History Details is Required for row:'
						+ " " + row +'in Document List Tab';
				dom.get("bpa_error_area").style.display = '';
				return false;
			}

			return true;
		}

		//This method is to remove rows from shopdetail table 
		function removeRow(obj) {
			// alert("hello");
			var tb1 = document.getElementById("dochistorydetails");
			var lastRow = (tb1.rows.length) - 1;
			var curRow = getRow(obj).rowIndex;
			// dom.get("bpa_error_area").style.display='none';
			if (lastRow == 1) {
				dom.get("bpa_error_area").style.display = 'none';
				document.getElementById("bpa_error_area").innerHTML = 'This row can not be deleted';
				dom.get("bpa_error_area").style.display = '';
				return false;
			} else {

				var updateserialnumber = curRow;
				for (updateserialnumber; updateserialnumber < tb1.rows.length - 1; updateserialnumber++) {
					if (document.forms[0].srlNo[updateserialnumber] != null)
						document.forms[0].srlNo[updateserialnumber].value = updateserialnumber;
					
				}
				tb1.deleteRow(curRow);
				document.getElementById('srlNoStatic').value=lastRow;
				return true;
			}
		}
	</script>
