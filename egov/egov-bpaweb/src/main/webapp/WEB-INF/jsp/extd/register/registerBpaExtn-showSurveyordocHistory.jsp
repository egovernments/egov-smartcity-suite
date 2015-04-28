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
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />
 <SCRIPT>
jQuery('#measuredetails1').find('input,select,textarea').attr('disabled',true);


</SCRIPT>
  
<div id="measuredetails1" align="center"> 

 <div class="formmainbox">
	<div align="center" >
	<div id="feesdetailsid1" >
	<s:if  test="%{documentHistory==null && documentHistoryDetailList.size==0}">
	 	<div class="errorstyle"><span class="bold"><s:text name="nodochistoryDetal"/></span></div>
	 </s:if>
	 <s:else>
	<s:if test="%{documentHistoryDetailList.size!=0}">
					        <table id="dochistorydetails" width="60%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
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
							    <td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].natureOfDeed" id="natureOfDeed" cssClass="tablerow" maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td class="blueborderfortd"><div align="center"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].registartionDate" id="registartionDate"  readonly="true" displayFormat="dd/mm/yy" showOn="focus" cssClass="tablerow" /></div></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].referenceNumber" id="referenceNumber" cssClass="tablerow"  maxlength="256"  cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].vendor" id="vendor" cssClass="tablerow"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].purchaser" id="purchaser" cssClass="tablerow"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].extentInsqmt" id="extentInsqmt" cssClass="tablerow"   cssStyle="text-align:right" onblur="validateIsNan(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].surveyNumber" id="surveyNumber" cssClass="tablerow"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
					          	<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].northBoundary" id="northBoundary" cssClass="tablerow"  maxlength="256"  cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].southBoundary" id="southBoundary" cssClass="tablerow"  maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].eastBoundary" id="eastBoundary" cssClass="tablerow" maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textfield name="documentHistoryDetailList[%{#row_status.index}].westBoundary" id="westBoundary" cssClass="tablerow"  maxlength="256"   cssStyle="text-align:right" onblur="checkNotSpecial(this);"/></td>
								<td  class="blueborderfortd"><s:textarea  name="documentHistoryDetailList[%{#row_status.index}].remarks" id="remarks" cssClass="tablerow"   cssStyle="text-align:right" maxlength="256"/></td>
								
							<s:if test="%{(mode!='view' && stateForValidate!='' && stateForValidate=='Forward to AEORAEE' || (registration.state==null && stateForValidate==''))}">
							   		
							<td id="deleteRowId" class="blueborderfortd"><div align="center"><a id = "deleteFloorId"  href="#"><img src="${pageContext.request.contextPath}/images/removerow.gif" height="16"  width="16" border="2" alt="Delete" onclick="removeRow(this);" ></a></div></td>
							   </s:if>
							   <s:hidden id="dochistoryDetailId" name="documentHistoryDetailList[%{#row_status.index}].id" />
							   </tr>
							
							
							   </s:iterator>
							   
							  
	  					</table> 
	  					</s:if>
	  			</div>
	  				<div id="docEnclosedDiv1"> 
	  					<table id="dochistoryEnclosedDetails" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" align="center">
	  						
	  						<div class="blueshadow"></div>
	  						<s:if test="%{documentHistory.wheatherdocumentEnclosed !=null}">
	  						<tr id="docandLayoutDiv">
	 						<td class="greybox" width="20%">&nbsp;</td>
	 						<td class="greybox" width="20%"><s:text name="Whether Document Prior to 05/08/1975 enclosed?" /> : <span class="mandatory" >*</span></td>
        					 <td   width="20%" class="greybox"><s:radio id="wheatherdocumentEnclosed" name="documentHistory.wheatherdocumentEnclosed"  value="%{documentHistory.wheatherdocumentEnclosed}" list="#{'true':'YES','false':'NO'}" onchange="onChangeOfDocumentEnclosed();"></s:radio></td>
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        						<td class="greybox" width="20%">&nbsp;</td>
        					</tr>
        					</s:if>
        					<s:if test="%{documentHistory.documentNum !=null ||  documentHistory.docEnclosedextentInsqmt!=null ||documentHistory.documentDate!=null }">
        					<tr id="documentNumberDiv">
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="10%"><s:text name="Document Number" /> : <span class="mandatory" >*</span></td>
       						 <td class="bluebox"><s:textfield id="documentNum" name="documentHistory.documentNum" value="%{documentHistory.documentNum}" onblur="checkNotSpecial(this);" maxlength="256"   /></td>
	 						<td class="bluebox" width="10%"><s:text name="Document Date" /> : <span class="mandatory" >*</span></td>
        					<td  class="bluebox" width="20%"><s:textfield name="documentHistory.documentDate" value="%{documentHistory.documentDate}" id="documentDate"  readonly="true" displayFormat="dd/mm/yy" showOn="focus" cssClass="tablerow" /></div></td>
							<td class="bluebox" >&nbsp;</td>
							<td class="bluebox" width="10%"><s:text name="Document Enclosed Extent in Sq.Mtr" /> : <span class="mandatory" >*</span></td>
					        <td class="bluebox"><s:textfield id="docEnclosedextentInsqmt" name="documentHistory.docEnclosedextentInsqmt" value="%{documentHistory.docEnclosedextentInsqmt}"  onblur="validateIsNan(this);"  /></td>
	 					
	 					<td class="bluebox" >&nbsp;</td>
	 					<td class="bluebox" width="20%">&nbsp;</td>
	 					
	 					</tr>
	 					
	 					</s:if>
        				<s:if test="%{documentHistory.wheatherpartOfLayout !=null }">
        				<tr id="docapprovedDiv">
        				<td class="bluebox" width="20%">&nbsp;</td>
        				<td class="bluebox" width="20%"><s:text name="Whether Plot lies in an approved layout ?" /> : <span class="mandatory" >*</span></td>
        				 <td   class="bluebox"><s:radio id="partOfLayout" name="documentHistory.wheatherpartOfLayout"  value="%{documentHistory.wheatherpartOfLayout}"  list="#{'true':'YES','false':'NO'}" onchange="onChangeOfPartOfLayout();"></s:radio></td>
        					
        					<td class="bluebox" >&nbsp;</td>
        					<td class="bluebox" >&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					<td class="bluebox" width="20%">&nbsp;</td>
        					</tr>
        				</s:if>	
        				<s:if test="%{documentHistory.plotDevelopedBy !=null}">
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
	 						</s:if>
	 						<s:if test="%{documentHistory.wheatherplotDevelopedBy !=null}">
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
	 						</s:if>
	 						<s:if test="%{documentHistory.wheatherFmsOrSketchCopyOfReg !=null}">
        				
        					<tr id="fmsfullDiv">
        					<td class="greybox" width="20%">&nbsp;</td>
	 						<td class="greybox" ><s:text name="Whether FMS Sketch / Copy of 'A' Registered prior to 05/08/1975 enclosed?" /> : <span class="mandatory" >*</span></td>
        					<td class="greybox"><s:radio  id="FmsOrSketchCopyOfReg" value="%{documentHistory.wheatherFmsOrSketchCopyOfReg}" name="documentHistory.wheatherFmsOrSketchCopyOfReg" list="#{'true':'YES','false':'NO' }" onchange="onChangeOfFMSSketchORCopyofRegn();"  /></td>
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
	 						</s:if>
	 						<s:if test="%{documentHistory.layoutdextentInsqmt !=null}">
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
	 	 			</s:if>
	 	 			<s:hidden id="documentHistoryId" name="documentHistory.id" value="%{documentHistory.id}" />
	 	 			</table>
	  				</div >
	  				</s:else>
	  				</div>
	  				
	  				</div>
	  				</div>
