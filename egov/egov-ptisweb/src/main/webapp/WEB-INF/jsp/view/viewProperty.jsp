<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->

<%@page import="org.egov.ptis.domain.entity.property.PropertyOwnerInfo"%>
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<table style="width:100%;">
	<!-- Body Begins -->
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="greybox" width="20%"><s:text name="prop.Id" /> :</td>
		<td class="greybox" width="25%"><span class="bold"><s:property
					default="N/A" value="%{basicProperty.upicNo}" /> </span></td>
		<td class="greybox" width="25"><s:text name="prntPropIndexNum" /> :</td>
		<td class="greybox" width="25%"><span class="bold"><s:property default="" value="%{}" /> </span></td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="bluebox" width="20%"><s:text name="fatherhusbandname"></s:text> :</td>
		<td class="bluebox">
			<span class="bold"><s:property default="" value="%{}" /> </span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="greybox"><s:text name="PropertyAddress" /> :</td>
		<td class="greybox">
			<span class="bold">
			  <s:property default="N/A" value="%{viewMap.propAddress}" /> 
		    </span>
		</td>
		<td class="bluebox"><s:text name="CorrAddr" /> :</td>
		<td class="bluebox">
			<span class="bold">
			 <s:property default="N/A" value="%{viewMap.ownerAddress}" /> 
			</span>
		</td>
		
	</tr>
	<tr>
	<td class="greybox" width="5%"></td>

		<td class="bluebox" width="20%"><s:text name="Zone" /> :</td>
		<td class="bluebox">
			<span class="bold">
			 <s:property
					value="%{viewMap.propID.zone.id}" />-
			 <s:property
					default="N/A" value="%{viewMap.propID.zone.name}" /> 
			</span>
		</td>

		<td class="greybox"><s:text name="Ward" /> :</td>
		<td class="greybox">
			<span class="bold"><s:property
					value="%{viewMap.propID.ward.id}" />-<s:property
					default="N/A" value="%{viewMap.propID.ward.name}" /> </span>
		</td>
	
	</tr>
	
	<tr>
		<td class="greybox" width="5%"></td>

		<td class="bluebox"><s:text name="block" /> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="N/A" value="%{viewMap.propID.area.id}" /> -
				<s:property value="%{viewMap.propID.area.name}" />
			</span>
		</td>

		<td class="bluebox"><s:text name="locality"></s:text> :
		</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="N/A" value="%{basicProperty.vacantLandAssmtNo}" /> 
			</span>
		</td>
		
	</tr>
	
	<tr>
	 <td class="greybox" width="5%"></td>
	 <td class="bluebox"><s:text name="vacantland.assmtno"></s:text> :
		</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="N/A" value="%{basicProperty.vacantLandAssmtNo}" /> 
			</span>
		</td>
		
	</tr>
	
	<tr>
		<td class="greybox" width="5%"></td>

		<td class="bluebox"><s:text name="extent.site"></s:text> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="N/A" value="%{viewMap.propDetail.extentSite}" /> 
			</span>
		</td>

		<td class="bluebox"><s:text name="extent.appurtntland"></s:text> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="N/A" value="%{viewMap.propDetail.extentAppartenauntLand}" /> 
			</span>
		</td>
		
	</tr>
	
	
	<tr>
		<td class="greybox" width="5%"></td>


		<td class="bluebox"><s:text name="reg.docno"></s:text> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="N/A" value="%{basicProperty.regdDocNo}" /> 
			</span>
		</td>

		<td class="bluebox"><s:text name="reg.docdate"></s:text> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:if test="%{basicProperty.regdDocDate != null}">
					<s:date name="%{basicProperty.regdDocDate}" format="d/M/yy"/>
				</s:if>
				<s:else>
					N/A
				</s:else>
			</span>
		</td>
		
	</tr>
	
	
	<tr>
		<td class="greybox" width="5%"></td>


		<td class="bluebox"><s:text name="ownership.type"></s:text> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="N/A" value="%{viewMap.ownershipType}" /> 
			</span>
		</td>
	</tr>
	
	
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="bluebox"><s:text name="apartcomplex.name"></s:text> :
		</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="" value="%{}" /> 
			</span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="bluebox"><s:text name="annualvalue" /> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:property	default="" value="%{}" /> 
			</span>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="bluebox"><s:text name="effectivedt" /> :</td>
		<td class="bluebox">
			<span class="bold">
				<s:if test="%{viewMap.propDetail.effective_date != null}">
					<s:date name="%{viewMap.propDetail.effective_date}" format="d/M/yy"/>
				</s:if>
				<s:else>
					N/A
				</s:else>
			</span>
		</td>
	</tr>
	
	<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<span class="bold"><s:text name="ownerdetails.title"></s:text></span>
				</div>
			</td>
	</tr>
	
	<tr>
	
	<td colspan="5">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable" >
		    <tr>
			    <th class="bluebgheadtd"><s:text name="adharno"/><span class="mandatory1">*</span></th>
			    <th class="bluebgheadtd"><s:text name="OwnerName"/><span class="mandatory1">*</span></th>
				<th class="bluebgheadtd"><s:text name="MobileNumber" /> (without +91)</th>
				<th class="bluebgheadtd"><s:text name="EmailAddress"/></th>
		    </tr>
		    
		    <s:iterator value="propertyOwners" var="propOwner">
		    	<tr>
			        <td class="blueborderfortd"><s:property value="%{aadhaarNumber}"/></td>
			        <td class="blueborderfortd"><s:property value="%{name}"/></td>
			        <td class="blueborderfortd"><s:property value="%{mobileNumber}"/></td>
			        <td class="blueborderfortd"><s:property value="%{emailId}"/></td>
			    </tr>
		    </s:iterator>
		</table>
	</td>
	</tr>
	
	<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<span class="bold"><s:text name="title.constructiontypes"/></span>
				</div>
			</td>
	</tr>
	
		
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="greybox"><s:text name="floortype"></s:text> :</td>
		<td class="greybox">
			<span class="bold"> <s:property default="N/A"
					value="%{viewMap.propDetail.floorType.name}" /> </span>
		</td>

		<td class="greybox"><s:text name="rooftype"></s:text> :</td>
		<td class="greybox">
			<span class="bold"> <s:property default="N/A"
					value="%{viewMap.propDetail.roofType.name}" /> </span>
		</td>
		
	</tr>
	
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="greybox"><s:text name="walltype"></s:text> :</td>
		<td class="greybox">
			<span class="bold"> <s:property default="N/A"
					value="%{viewMap.propDetail.wallType.name}" /> </span>
		</td>

		<td class="greybox"><s:text name="woodtype"></s:text> :</td>
		<td class="greybox">
			<span class="bold"> <s:property default="N/A"
					value="%{viewMap.propDetail.woodType.name}" /> </span>
		</td>
		
	</tr>


    <tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> <s:text name="amenities"></s:text> </span>
			</div>
		</td>
	</tr>
	
	<tr>
	
		<td colspan="5">
          
          <table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					
			<tr>
				<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="lift"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="toilets"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="watertap"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="superstructure"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="drainage"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="electricity"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="attachbathroom"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="waterharvesting"></s:text>
		       	</th>
		       	<th class="bluebgheadtd" width="11.1111111111%">
				   <s:text name="cableconnection"></s:text>
		       	</th>
		    </tr>
		    
		    <tr>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.lift}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.toilets}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.watertap}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.superstructure}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.drainage}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.electricity}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.attachbathroom}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.waterharvesting}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		     <td class="blueborderfortd">
		     	<s:if test="%{viewMap.propDetail.cableconnection}">Yes</s:if>
		     	<s:else>No</s:else>
		     </td>
		    </tr>
		</table>
          
		</td>
	
	</tr>
	
	<%--s:if
		test="basicProperty.property.propertyDetail.floorDetails.size()>0"--%>
		<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<s:text name="FloorDetails" />
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="5" class="bluebgheadtd">

				<table style="width:100%;" class="tablebottom">
					
					<tr>
						<th class="bluebgheadtd"><s:text name="FloorNo" /><span	class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="ConstructionType" /><span class="mandatory1" id="constTypeMdtry">*</span></th>
						<th class="bluebgheadtd"><s:text name="Usage" /><span class="mandatory1" id="usageMdtry">*</span></th>
						<th class="bluebgheadtd"><s:text name="Occupancy" /><span class="mandatory1" id="occMdtry">*</span></th>
						<th class="bluebgheadtd"><s:text name="Occupantname" /></th>
						<th class="bluebgheadtd"><s:text name="Bldgage" /><span	class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="constrdate" /><span	class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="Width" /></th>
						<th class="bluebgheadtd"><s:text name="Length" /></th>
						<th class="bluebgheadtd"><s:text name="PlinthArea" /><span class="mandatory1">*</span></th>
						<th class="bluebgheadtd"><s:text name="capitalvalue"></s:text></th>
						<th class="bluebgheadtd"><s:text name="planappr" /></th>
						<th class="bluebgheadtd"><s:text name="exemption" /></th>
					</tr>
					<s:iterator
						value="basicProperty.property.propertyDetail.floorDetails"
						status="floorsstatus">
						<tr>
							<s:set value="basicProperty.property.propertyDetail.floorDetails[#floorsstatus.index]" var="floor" />
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{floorNoStr[#floorsstatus.index]}" />
								</div>
							</td>
							<td class="blueborderfortd">
							    <div align="center">
									<s:property default="N/A" value="%{#floor.unitType.type}" />
								</div>
							</td>
						    <td class="blueborderfortd">
						    	<div align="center">
						    		<s:property default="N/A" value="%{#floor.propertyUsage.usageName}" />
						    		<%-- <script type="text/javascript">
							    		var category = '<s:property default="N/A" value="%{getUnitTypeCategory(unitType.code , unitTypeCategory)}" />';
						    			var categoryStrings = category.split(" ");
						    			for (var k = 0; k < categoryStrings.length; k++) {
						    				document.write(" " + categoryStrings[k]);	
						    			}						    			
						    		</script> --%>									
								</div>
						    </td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{floor.occupancy}"/>
								</div>
							</td>
							<td class="blueborderfortd" width="5%">
						    	<div align="center"> 	    		
						    		<s:property default="N/A" value="%{#floor.occupantname}" />
						        </div>
						    </td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField7}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:if test="%{#floor.createdDate != null}">
										<s:date name="%{#floor.createdDate}" format="d/M/yy"/>
									</s:if>
									<s:else>
										N/A
									</s:else>
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{#floor.builtUpArea.breadth}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{#floor.propertyUsage.length}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{#floor.builtUpArea.area}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:if
										test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_OPEN_PLOT) 
													|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_STATE_GOVT) 
													|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@PROPTYPE_CENTRAL_GOVT)}">
										N/A
									</s:if>
									<s:else>
										<s:property default="N/A" value="%{getWaterMeterDtls(#floor.waterRate)}" />
									</s:else>
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:if test="%{floor.planApproved}">
										Yes
									</s:if>
									<s:else>
										No
									</s:else>
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{}"/>
								</div>
							</td>
						</tr>
					</s:iterator>
				</table>
				<br/><br/>
			</td>
		</tr>
	<%--/s:if--%>

	<tr>
	   <td class="bluebox" width="5%"></td>
		<td class="greybox" colspan="2"><s:text name="CurrentTax" /> :</td>
		<td class="greybox" colspan="2">
			<span class="bold">Rs. <s:property default="N/A"
					value="%{viewMap.currTax}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox" width="5%"></td>
		<td class="bluebox" colspan="2"><s:text name="CurrentTaxDue" /> :</td>
		<td class="bluebox" colspan="2">
			<span class="bold">Rs. <s:property default="N/A"
					value="%{viewMap.currTaxDue}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="5%"></td>
		<td class="greybox" colspan="2"><s:text name="ArrearsDue" /> :</td>
		<td class="greybox" colspan="2">
			<span class="bold">Rs. <s:property default="N/A"
					value="%{viewMap.totalArrDue}" /> </span>
		</td>
	</tr>
	
	
</table>
<br/>
<script type="text/javascript">
	function showDocumentManagerView(indexNum) {
		var url = "../view/viewProperty!viewDoc.action?propertyId="+indexNum;
		window.open(url, 'docview', 'width=1000,height=400');
	}
</script>
