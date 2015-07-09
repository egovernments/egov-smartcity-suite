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
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<s:set name="theme" value="'simple'" scope="page" />

<div align="center"> 
  <div id="docketSheetDiv" class="formmainbox">
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
   
    <div align="center" id="checklistdiv">
    <div id="feesdetailsid1" >
					         <table id="dochistorydetails1" width="60%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
		                 	<div class="blueshadow"></div>
	                    	<div class="headingsmallbg"><span class="bold"><s:text name="Document History Sheet"/></span></div>
	 						<div align="center">
	     					 <tr>
	     					   <th  class="bluebgheadtd" width="10%"><div align="center">SI NO<span class="mandatory">*</span>:</div></th>
	     					  <th  class="bluebgheadtd" width="10%"><div align="center">Nature of Document<span class="mandatory">*</span>:</div></th>
					         <th  class="bluebgheadtd" width="10%"><div align="center">Date of Registration<span class="mandatory">*</span>:</div></th>
					       <th  class="bluebgheadtd" width="10%"><div align="center">Reference Number<span class="mandatory">*</span>:</div></th>
								<th  class="bluebgheadtd" width="10%"><div align="center">Vendor<span class="mandatory">*</span>:</div></th>
					            <th  class="bluebgheadtd" width="10%"><div align="center">Purchaser<span class="mandatory">*</span>:</div></th>
					           <th  class="bluebgheadtd" width="10%"><div align="center">Extent in Sq.Mtr<span class="mandatory">*</span>:</div></th>
					    <th  class="bluebgheadtd" width="10%"><div align="center">Schedule of Property<span class="mandatory">*</span></div></th>
					        <th  class="bluebgheadtd" width="10%"><div align="center">North Boundary<span class="mandatory">*</span>:</div></th>
					        <th  class="bluebgheadtd" width="10%"><div align="center">South Boundary<span class="mandatory">*</span>:</div></th>
					         <th  class="bluebgheadtd" width="10%"><div align="center">West Boundary<span class="mandatory">*</span>:</div></th>
					        <th  class="bluebgheadtd" width="10%"><div align="center">East Boundary<span class="mandatory">*</span>:</div></th>
					         <th  class="bluebgheadtd" width=10%"><div align="center">Remarks</div></th>
					        
					         	<s:if test="%{(mode!='view' && stateForValidate!='' && stateForValidate=='Forward to AEORAEE' || (registration.state==null && stateForValidate==''))}">
					  <th  class="bluebgheadtd" width="2%"><div align="center"> Delete</div></th>
						</s:if>
						  </tr>
						     <s:iterator value="documentHistoryDetailList" status="row_status">
							    <tr>
							    <td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].srlNo" id="srlNo" readonly="true"  cssClass="tablerow" value="%{#row_status.count}" cssStyle="text-align:center"/></td>
							   <td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].natureOfDeed" id="natureOfDeed" cssClass="tablerow" maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td class="blueborderfortd"><div align="center"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].registartionDate" id="registartionDate"  readonly="true" displayFormat="dd/mm/yy" showOn="focus" cssClass="tablerow" /></div></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].referenceNumber" id="referenceNumber" cssClass="tablerow"  maxlength="256"  cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].vendor" id="vendor" cssClass="tablerow"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].purchaser" id="purchaser" cssClass="tablerow"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].extentInsqmt" id="extentInsqmt" cssClass="tablerow"   cssStyle="text-align:right" onblur="validateIsNan(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].surveyNumber" id="surveyNumber" cssClass="tablerow"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
					          	<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].northBoundary" id="northBoundary" cssClass="tablerow"  maxlength="256"  cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].southBoundary" id="southBoundary" cssClass="tablerow"  maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].westBoundary" id="westBoundary" cssClass="tablerow"  maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].eastBoundary" id="eastBoundary" cssClass="tablerow" maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textarea  name="documentHistoryDetailList[%{#row_status.index}].remarks" id="remarks" cssClass="tablerow"   cssStyle="text-align:right" maxlength="256"/></td>
								
							<s:if test="%{(mode!='view' && stateForValidate!='' && stateForValidate=='Forward to AEORAEE' || (registration.state==null && stateForValidate==''))}">
							   		
							<td id="deleteRowId" class="blueborderfortd"><div align="center"><a id = "deleteFloorId"  href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removeRow(this);" ></a></div></td>
							   </s:if>
							   <s:hidden id="dochistoryDetailId" name="documentHistoryDetailList[%{#row_status.index}].id" />
							   </tr>
							
							
							   </s:iterator>
							   
							  
	  					</table>
	  					
	  					
	  				</div>
 <div id="docEnclosedDiv"> 
	  					<table id="dochistoryEnclosedDetails" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
	  						
	  						<div class="blueshadow"></div>
	  						<tr id="docandLayoutDiv">
	 						<td class="greybox" width="20%">&nbsp;</td>
	 						<td class="greybox" width="20%"><s:text name="Whether Document Prior to 05/08/1975 enclosed?" /> : <span class="mandatory" >*</span></td>
        					 <td   width="20%" class="greybox"><s:radio id="wheatherdocumentEnclosed" name="documentHistory.wheatherdocumentEnclosed"  value="%{documentHistory.wheatherdocumentEnclosed}" list="#{'true':'YES','false':'NO'}" onchange="onChangeOfDocumentEnclosed();"></s:radio></td>
        						
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        					</tr>
        					<tr id="documentNumberDiv">
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="10%"><s:text name="Document Number" /> : <span class="mandatory" >*</span></td>
       						 <td class="bluebox"><s:textfield id="documentNum" name="documentHistory.documentNum" value="%{documentHistory.documentNum}" onblur="checkNotSpecial(this);" maxlength="256"   /></td>
	 						<td class="bluebox" width="10%"><s:text name="Document Date" /> : <span class="mandatory" >*</span></td>
        					<td  class="bluebox" ><sj:datepicker id="documentDate" name="documentHistory.documentDate" value="%{documentHistory.documentDate}" cssClass="tablerow"  readonly="true" displayFormat="dd/mm/yy" showOn="focus"/></td>
							<td class="bluebox" >&nbsp;</td>
						 	<td class="bluebox" width="10%"><s:text name="Document Enclosed Extent in Sq.Mtr" /> : <span class="mandatory" >*</span></td>
					        <td class="bluebox"><s:textfield id="docEnclosedextentInsqmt" name="documentHistory.docEnclosedextentInsqmt" value="%{documentHistory.docEnclosedextentInsqmt}"  onblur="validateIsNan(this);"  /></td>
	 					<td class="bluebox" >&nbsp;</td>
	 					<td class="bluebox" width="20%">&nbsp;</td>
	 					<td class="bluebox" width="20%">&nbsp;</td>
	 					</tr>
	 					
        				<tr id="docapprovedDiv">
        				<td class="bluebox" width="20%">&nbsp;</td>
        				<td class="bluebox" width="20%"><s:text name="Whether Plot lies in an approved layout ?" /> : <span class="mandatory" >*</span></td>
        				 <td   class="bluebox"><s:radio id="partOfLayout" name="documentHistory.wheatherpartOfLayout"  value="%{documentHistory.wheatherpartOfLayout}"  list="#{'true':'YES','false':'NO'}" onchange="onChangeOfPartOfLayout();"></s:radio></td>
        					<td class="bluebox" >&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					</tr>
        					
        					<tr id="plotDevelopNewDiv">
        					<td class="greybox" width="20%">&nbsp;</td>
        				<td  class="greybox" width="20%"><s:text name="Developed by?" /> : <span class="mandatory" >*</span></td>
        				 <td   width="20%" class="greybox"><s:radio id="plotDevelopBy" name="documentHistory.plotDevelopedBy"  value="%{documentHistory.plotDevelopedBy}"  list="#{'true':'Government','false':'Individual'}" onchange="onChangeOfPlotDevelopedBy();" ></s:radio></td>
        					<td class="greybox" >&nbsp;</td>
        					<td class="greybox" >&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					<td class="greybox" width="20%">&nbsp;</td>
        					</tr>
	 						
	 						<tr id="plotdevFmsSketchDiv">
	 						<td class="bluebox" width="20%">&nbsp;</td>
	 						<td class="bluebox" width="20%"><s:text name="Who has Developed?" /> : <span class="mandatory" >*</span></td>
        					 <td   width="20%" class="bluebox"><s:radio id="wheatherplotDevelopedBy" name="documentHistory.wheatherplotDevelopedBy"  value="%{documentHistory.wheatherplotDevelopedBy}"  list="#{'TNHB':'TNHB','TNSCB':'TNSCB','CMDA':'CMDA','SIDCO':'SIDCO'}"  ></s:radio></td>
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
	 						<td class="greybox" ><s:text name="Whether FMS Sketch / Copy of A Registered prior to 05/08/1975 enclosed?" /> : <span class="mandatory" >*</span></td>
        					<td class="greybox"><s:radio  id="FmsOrSketchCopyOfReg" value="%{documentHistory.wheatherFmsOrSketchCopyOfReg}" name="documentHistory.wheatherFmsOrSketchCopyOfReg" list="#{'true':'YES','false':'NO' }" onchange="onChangeOfFMSSketchORCopyofRegn();"  /></td>
        					<td class="greybox" >&nbsp;</td>
        					<td id="showmes" class="greybox" align="left" width="15%"><span class="mandatory">* 10 % of OSR Charges will to pay</span></td>
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
					        <td class="bluebox"><s:textfield id="layoutdextentInsqmt" name="documentHistory.layoutdextentInsqmt" value="%{documentHistory.layoutdextentInsqmt}"  onblur="validateIsNan(this);"  /></td>
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
	  				<tr>
	  				
	  				
 		  <td class="greybox" width="15%">	
						 						<div align="center" >  
						 						<a href="#" onclick="printDocumentHistorySheet('<s:property value="%{registrationId}"/>')"  >
						 						 <b>	Print Official DocumentHistory </b></a></div></td>
			
			</tr>
	  				<div id="docketHistoryDivId">
	<s:if test="%{serviceTypeCode}">
		<%@ include file="../register/registerBpaExtn-commonDocketSheet.jsp"%>
	
<h1 class="subhead" ><s:text name="inspectionlbl.docket.constructionstageHeader"/></h1>
	   <s:if test="%{constructionStages.size!=0}">
	   	  <s:iterator value="constructionStages" status="row_status1">
		   <table id="constStagechecklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>
			    <td class="greybox" width="10%">&nbsp;</td>	 		
			   	<td class="greybox" width="50%"><s:text name="constructionStages[%{#row_status1.index}].checkListDetails.description"  /></td>		   	
		     	 <td class="blueborderfortd" >
			     <s:select id="constStage" name="constructionStages[%{#row_status1.index}].value"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"        />
			     </td>
			 	<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.id"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.checkList.id"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.description"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.docket.id"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].id"/>
			    	
		    </tr>   
		   </table>
		  </s:iterator>
	  </s:if>
	
	   <h1 class="subhead" ><s:text name="inspectionlbl.docket.natuveorViolationLbl"/></h1>
	
	  <s:if test="%{devContrlList.size!=0}">
		  
		   <table id="generalViolationlists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>		   
				<td class="greybox" width="30%"><s:text name="inspectionlbl.docket.devrule"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.required"/>  </td>
				<td class="greybox" width="20%"><s:text name="inspectionlbl.docket.provided"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.extofviolation"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.percOfviolation"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.remarks"/>  </td>
		    </tr>
		    <s:iterator value="devContrlList" status="row_status">
		    <tr>
			    <td class="greybox" width="30%"><s:text name="devContrlList[%{#row_status.index}].checkListDetails.description"/></td>			
			   
			   
		     	<s:if  test="%{checkListDetails.description=='No. of Floors'}">
		     	<td class="greybox" width="20%"><s:textfield  name="devContrlList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox" width="15%"><s:textfield  name="devContrlList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			   
			    </s:if>
			    <s:else>
			   	<td class="greybox"  id="numbers" width="20%"><s:textfield  name="devContrlList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="15%"><s:textfield  name="devContrlList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox"   width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			    
			     </s:else>
			    </td>
			 	<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.description"/>
				<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="devContrlList[%{#row_status.index}].id"/>
		    </tr>
		    </s:iterator>   
		   </table>
		  
	  </s:if>
	  
	    <h1 class="subhead" ><s:text name="inspectionlbl.docket.setBack"/></h1>
	
	  <s:if test="%{setBackList.size!=0}">
		  
		   <table id="generalsetbacklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>		   
				<td class="greybox" width="30%">&nbsp;  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.required"/>  </td>
				<td class="greybox" width="20%"><s:text name="inspectionlbl.docket.provided"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.extofviolation"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.percOfviolation"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.remarks"/>  </td>
		    </tr>
		    <s:iterator value="setBackList" status="row_status">
		    <tr>
			    <td class="greybox" width="30%"><s:text name="setBackList[%{#row_status.index}].checkListDetails.description"/></td>			
			   
			   
		     	<s:if  test="%{checkListDetails.description=='Plot Coverage'}">
		     	<td class="greybox" width="20%"><s:textfield  name="setBackList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox" width="15%"><s:textfield  name="setBackList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			   
			    </s:if>
			    <s:else>
			   	<td class="greybox"  id="numbers" width="20%"><s:textfield  name="setBackList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="15%"><s:textfield  name="setBackList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox"   width="10%"><s:textfield  name="setBackList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			    
			     </s:else>
			    </td>
			 	<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.description"/>
				<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="setBackList[%{#row_status.index}].id"/>
		    </tr>
		    </s:iterator>   
		   </table>
		  
	  </s:if>
	      <h1 class="subhead" ><s:text name="inspectionlbl.docket.parkingLbl"/></h1>
	      
	    <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="abbutingRoad_Tbl">
	    <tr>
 			<td class="bluebox">&nbsp;</td>
			<td class="bluebox" width="13%"><s:text name="inspectionlbl.docket.parkingGeneralVoilation.plotWidthRear"/></td> 
   			<td class="bluebox"  id="numbers"><s:textfield id="plotWidthRear" name="docket.plotWidthRear"  value="%{docket.plotWidthRear}"/></td>
   			
   			<td class="bluebox">&nbsp;</td>
			<td class="bluebox" width="13%"><s:text name="inspectionlbl.docket.parkingGeneralVoilation.constructionWidthRear"/></td> 
   			<td class="bluebox"  id="numbers"><s:textfield id="constructionWidthRear" name="docket.constructionWidthRear"  value="%{docket.constructionWidthRear}"/></td>
		 </tr>
		 
		  <tr>
	 		<td class="greybox">&nbsp;</td>
			<td class="greybox" width="13%"><s:text name="inspectionlbl.docket.parkingGeneralVoilation.constructionHeightRear"/></td>  
   			<td class="greybox"  id="numbers"><s:textfield id="constructionHeightRear" name="docket.constructionHeightRear"  value="%{docket.constructionHeightRear}"/></td>
   			
   			<td class="greybox">&nbsp;</td>
   			<td class="greybox">&nbsp;</td>
   			<td class="greybox">&nbsp;</td>
		 </tr>
		 </table>
	
	  <s:if test="%{parkingList.size!=0}">
		  
		   <table id="generalparkinglists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>		   
				<td class="greybox" width="30%">&nbsp;  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.required"/>  </td>
				<td class="greybox" width="20%"><s:text name="inspectionlbl.docket.provided"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.extofviolation"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.percOfviolation"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.remarks"/>  </td>
		    </tr>
		    <s:iterator value="parkingList" status="row_status">
		    <tr>
			    <td class="greybox" width="30%"><s:text name="parkingList[%{#row_status.index}].checkListDetails.description"/></td>			
			   	<td class="greybox"  id="numbers" width="20%"><s:textfield  name="parkingList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="parkingList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="15%"><s:textfield  name="parkingList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="parkingList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox"   width="10%"><s:textfield  name="parkingList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			 	<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.description"/>
				<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="parkingList[%{#row_status.index}].id"/>
		    </tr>
		    </s:iterator>   
		   </table>
		  
	  </s:if>
	  <s:if test="%{generalList.size!=0}">
		  <s:iterator value="generalList" status="row_status">
		   <table id="generalViolationlists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>
			    <td class="greybox" width="10%">&nbsp;</td>			
			   	<td class="greybox" width="50%"><s:text name="generalList[%{#row_status.index}].checkListDetails.description"/></td>		   	
		     	 <td class="blueborderfortd">
		     	  <s:if test="%{checkListDetails.description!=@org.egov.bpa.constants.BpaConstants@DOCKET_PARKINGGNRLVOILATION_CONSTRUCTIONSTAGEREAR}">
			      	<s:select id="genviolist" name="generalList[%{#row_status.index}].value"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"       />
			      </s:if>
			      <s:else>
			      	<s:select id="genviolist" name="generalList[%{#row_status.index}].value"  list="dropdownData.usageOfConstructionList" headerKey="NA" headerValue="NA"/>
			      </s:else>
			     </td>
			 	<s:hidden name="generalList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="generalList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="generalList[%{#row_status.index}].checkListDetails.description" />
				<s:hidden name="generalList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="generalList[%{#row_status.index}].id"/>
		    </tr>   
		   </table>
		  </s:iterator>
	  </s:if>
		
		<h1 class="subhead" ><s:text name="inspectionlbl.docket.minDisFromPowerLineLbl"/></h1>
	  <s:if test="%{minDistancePowerLineList.size!=0}">
		  <s:iterator value="minDistancePowerLineList" status="row_status">
		   <table id="generalViolationlists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>
			    <td class="greybox" width="10%">&nbsp;</td>			
			   	<td class="greybox" width="50%"><s:text name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.description"/>
			   	</td>		   	
			    <td class="blueborderfortd">	
		     	  <s:select id="genviolist" name="minDistancePowerLineList[%{#row_status.index}].value"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"      />
			    </td>
			 	<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.description" />
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].id"/>
		    </tr>   
		   </table>
		  </s:iterator>
	  </s:if> 
		
	    <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="otherinfo_Tbl">
	       <tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.docket.aeereport"/></td> 
	   			<td class="greybox"><s:textarea id="aeeInspectionReport" name="docket.aeeInspectionReport"  value="%{docket.aeeInspectionReport}"  cols="20" rows="2"/></td>
				
				<td class="greybox" width="13%" ><s:text name="inspectionlbl.docket.otherDetails"/></td> 
	   			<td class="greybox" >  <s:textarea id="remarks" name="docket.remarks"  value="%{docket.remarks}" cols="20" rows="2"/></td>
		 </tr>
	    </table> 
	  </s:if>
	    </div>
	</div>
  
   </div> 
</div>

