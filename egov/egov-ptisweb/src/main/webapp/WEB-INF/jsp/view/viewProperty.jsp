<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<!-- Body Begins -->
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="prop.Id" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{basicProperty.upicNo}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="Zone" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property
					value="%{basicProperty.boundary.parent.boundaryNum}" />-<s:property
					default="N/A" value="%{basicProperty.boundary.parent.name}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="Ward" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold"><s:property
					value="%{basicProperty.boundary.boundaryNum}" />-<s:property
					default="N/A" value="%{basicProperty.boundary.name}" /> </span>
		</td>
	</tr>
		<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="partNo" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold">
				<s:property	default="N/A" value="%{basicProperty.partNo}" /> 
			</span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="ParcelID" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold"> <s:property default="N/A"
					value="%{basicProperty.gisReferenceNo}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="OwnerName" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"> <s:property default="N/A"
					value="%{viewMap.ownerName}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="PropertyAddress" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{viewMap.propAddress}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="CorrAddr" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{viewMap.ownerAddress}" /> </span>
		</td>
	</tr>
	<s:if test="%{basicProperty.property.isExemptedFromTax}">
	
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="TaxExmRsn" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold"><s:property value="%{basicProperty.property.taxExemptReason}" /> </span>
		</td>
	</tr>
	</s:if>
	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="NorthWard" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{basicProperty.propertyID.northBoundary}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="SouthWard" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{basicProperty.propertyID.southBoundary}" /> </span>
		</td>
	</tr>

	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="EastWard" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{basicProperty.propertyID.eastBoundary}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="WestWard" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{basicProperty.propertyID.westBoundary}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="PropertyType" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{ basicProperty.property.propertyDetail.propertyTypeMaster.type}" />
			</span>
		</td>

	</tr>
	<s:if
		test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_RESD) ||
					basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_NON_RESD) 
					|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_OPEN_PLOT)}">
		<tr>
			<td class="greybox">
				&nbsp;
			</td>
			<td class="greybox" colspan="2">
				<s:text name="PropertyTypeCategory" />
				:
			</td>
			<td class="greybox" colspan="2">
				<span class="bold"> <s:if
						test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_RESD)}">
					<s:property default="N/A" value="%{@org.egov.ptis.nmc.constants.NMCPTISConstants@RESIDENTIAL_PROPERTY_TYPE_CATEGORY[basicProperty.property.propertyDetail.extra_field5]}" />
				</s:if>
				<s:elseif test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_OPEN_PLOT)}">
					<s:property default="N/A" value="%{@org.egov.ptis.nmc.constants.NMCPTISConstants@OPEN_PLOT_PROPERTY_TYPE_CATEGORY[basicProperty.property.propertyDetail.extra_field5]}" />
				</s:elseif>
				<s:else>
					<s:property default="N/A" value="%{@org.egov.ptis.nmc.constants.NMCPTISConstants@NON_RESIDENTIAL_PROPERTY_TYPE_CATEGORY[basicProperty.property.propertyDetail.extra_field5]}" />
				</s:else>
				</span>
			</td>
		</tr>
	</s:if>
	<tr>
		<td class="bluebox" width="15%"></td>
		<s:if test="%{basicProperty.property.propertyDetail.extra_field5 == 'RESIDENTIAL_FLATS'}">
			<td class="bluebox" colspan="2">
				<s:text name="undivArea" />
				:
			</td>
		</s:if>
		<s:else>
			<td class="bluebox" colspan="2">
				<s:text name="PlotArea" />
				:
			</td>
		</s:else>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{basicProperty.property.propertyDetail.sitalArea.area}" />
			</span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="locationFactor" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold">
				<s:property default="N/A" value="%{viewMap.propertyCategory.categoryName}" /> 
			</span>
		</td>
	</tr>
	<s:if test="%{basicProperty.property.propertyDetail.extra_field5 == @org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_CAT_RESD_CUM_NON_RESD}">
		<tr>
			<td class="bluebox">&nbsp;</td>					
			<td class="bluebox" colspan="2">
				<s:text name="nonResPlotArea" />
				:
			</td>
			<td class="bluebox" colspan="2">
				<span class="bold"><s:property default="N/A"
						value="%{basicProperty.property.propertyDetail.nonResPlotArea.area}" /> </span>
			</td>
		</tr>
	</s:if>
	<s:if
		test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_OPEN_PLOT) 
					|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_STATE_GOVT) 
					|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_CENTRAL_GOVT)}">
		<tr>
			<td class="bluebox" width="15%"></td>
			<td class="bluebox" colspan="2"><s:text name="GenWaterRate" />
				:</td>
			<td class="bluebox" colspan="2"><span class="bold"><s:property
						default="N/A" value="%{viewMap.genWaterRate}" /> </span></td>
		</tr>
	</s:if>
	<s:if
		test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_OPEN_PLOT) }">
		<tr>
			<td class="greybox" width="15%"></td>
			<td class="greybox" colspan="2">
				<s:text name="Usage" />
				:
			</td>
			<td class="greybox" colspan="2">
				<span class="bold"><s:property default="N/A"
						value="%{basicProperty.property.propertyDetail.propertyUsage.usageName}" />
				</span>
			</td>
		</tr>
		<tr>
			<td class="bluebox" width="15%"></td>
			<td class="bluebox" colspan="2">
				<s:text name="Occupancy" />
				:
			</td>
			<td class="bluebox" colspan="2">
				<span class="bold"><s:property default="N/A"
						value="%{basicProperty.property.propertyDetail.propertyOccupation.occupation}" />
				</span>
			</td>

		</tr>
		<tr>
			<td class="greybox" width="15%"></td>
			<td class="greybox" colspan="2">
				<s:text name="OccupationDate" />
				:
			</td>
			<td class="greybox" colspan="2">
				<span class="bold">
					<s:date name="%{basicProperty.propCreateDate}" format="dd/MM/yyyy"/>
				</span>
			</td>

		</tr>
		
		<tr>
			<td class="bluebox" width="15%"></td>
			<td class="bluebox" colspan="2">
				<s:text name="openPLotManualAlv" />
				:
			</td>
			<td class="bluebox" colspan="2">
				<span class="bold">
				<s:if test="%{basicProperty.property.propertyDetail.manualAlv == null}" >
					<s:property default="N/A" value="%{basicProperty.property.propertyDetail.alv}" />
				</s:if>
				<s:else>
					<s:property default="N/A" value="%{basicProperty.property.propertyDetail.manualAlv}" />
				</s:else>				
				</span>
			</td>

		</tr>
		<tr>
			<td class="greybox" width="15%"></td>
			<td class="greybox" colspan="2">
				<s:text name="OccupierName" />
				:
			</td>
			<td class="greybox" colspan="2">
				<span class="bold">
				<s:property default="N/A"
						value="%{basicProperty.property.propertyDetail.occupierName}"/>
				</span>
			</td>

		</tr>
	</s:if>
    <s:if
		test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_CENTRAL_GOVT)
		|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_STATE_GOVT)}">
		<s:if test="basicProperty.property.propertyDetail.floorDetailsProxy.size() == 0">
			<tr>
			<td class="greybox" width="15%"></td>
			<td class="greybox" colspan="2">
				<s:text name="OccupationDate" />
				:
			</td>
			<td class="greybox" colspan="2">
				<span class="bold">
				<s:date name="%{basicProperty.propCreateDate}" format="dd/MM/yyyy"/>
				</span>
			</td>

			</tr>
		</s:if>
	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="bldngCost" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold"><s:property default="N/A"
					value="%{basicProperty.property.propertyDetail.extra_field3}" /> </span>
		</td>

	</tr>
	</s:if>
	<s:if
		test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_CENTRAL_GOVT)}">
		<tr>
			<td class="greybox" width="5%">
				&nbsp;
			</td>
			<td class="greybox" colspan="2">
				<s:text name="amenities" />
				:
			</td>
			<td class="greybox" colspan="2">
				<span class="bold"> <s:property default="N/A"
						value="%{getAmenitiesDtls(basicProperty.property.propertyDetail.extra_field4)}" />
				</span>
			</td>
		</tr>
	</s:if>
	<c:if test="${docNumber!=null && docNumber!=''}">
	<tr>
		<td class="bluebox2"  width="5%">&nbsp;</td>
		<td class="bluebox"  colspan="2">Documents Uploaded:</td>
		<td class="bluebox" colspan="2">
		<span class="bold">
       <a
									href='#'
									target="_parent" 
									onclick="window.open('/egi/docmgmt/basicDocumentManager!viewDocument.action?moduleName=ptis&docNumber=${docNumber}'
									,'ViewDocuments','resizable=yes,scrollbars=yes,height=650,width=700,status=yes');">View Document</a>
       
       </span></td>
	</tr>
	</c:if>
	
	<s:if
		test="basicProperty.property.propertyDetail.floorDetailsProxy.size()>0">
		<tr>
			<td colspan="5">
				<div class="headingsmallbg">
					<s:text name="FloorDetails" />
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="5" class="bluebgheadtd">

				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">
							<s:text name="UnitNo" />
						</th>
						<th class="bluebgheadtd"><s:text name="unitType"/></th>
						<th class="bluebgheadtd"><s:text name="unitTypeCategory"/></th>
						<th class="bluebgheadtd"><s:text name="FloorNo" /></th>
						<th class="bluebgheadtd"><s:text name="TaxExmRsn" /></th>
						<th class="bluebgheadtd"><s:text name="type" /></th>
						<th class="bluebgheadtd"><s:text name="OccupierName" /></th>
						<th class="bluebgheadtd"><s:text name="AssableArea" /></th>
						<th class="bluebgheadtd"><s:text name="Usage" /></th>
						<th class="bluebgheadtd"><s:text name="Occupancy" /></th>
						<th class="bluebgheadtd"><s:text name="GenWaterRate" /></th>
						<th class="bluebgheadtd"><s:text name="ConstructionType" /></th>
						<th class="bluebgheadtd"><s:text name="ConstructionYear" /></th>
						<th class="bluebgheadtd"><s:text name="OccupationDate" /></th>
						<th class="bluebgheadtd"><s:text name="Rent" /></th>
						<th class="bluebgheadtd"><s:text name="Width" /></th>
						<th class="bluebgheadtd"><s:text name="Length" /></th>
						<th class="bluebgheadtd"><s:text name="BuiltUpArea" /></th>
						<th class="bluebgheadtd"><s:text name="manualAlv" /></th>
					</tr>
					<s:iterator
						value="basicProperty.property.propertyDetail.floorDetailsProxy"
						status="floorsstatus">
						<tr>
							<s:set value="basicProperty.property.propertyDetail.floorDetailsProxy[#floorsstatus.index]" var="floor" />
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField1}" />
								</div>
							</td>
							<td class="greybox">
							    <div align="center">
									<s:property default="N/A" value="%{#floor.unitType.type}" />
								</div>
							</td>
						    <td class="greybox">
						    	<div align="center">
						    		<script type="text/javascript">
							    		var category = '<s:property default="N/A" value="%{getUnitTypeCategory(unitType.code , unitTypeCategory)}" />';
						    			var categoryStrings = category.split(" ");
						    			for (var k = 0; k < categoryStrings.length; k++) {
						    				document.write(" " + categoryStrings[k]);	
						    			}						    			
						    		</script>									
								</div>
						    </td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{floorNoStr[#floorsstatus.index]}" />
								</div>
							</td>
							<td class="greybox" width="5%">
						    	<div align="center"> 	    		
						    		<s:property default="N/A" value="%{#floor.taxExemptedReason}" />
						        </div>
						    </td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField7}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField2}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.builtUpArea.area}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.propertyUsage.usageName}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.propertyOccupation.occupation}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:if
										test="%{basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_OPEN_PLOT) 
													|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_STATE_GOVT) 
													|| basicProperty.property.propertyDetail.propertyTypeMaster.code.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PROPTYPE_CENTRAL_GOVT)}">
										N/A
									</s:if>
									<s:else>
										<s:property default="N/A" value="%{getWaterMeterDtls(#floor.waterRate)}" />
									</s:else>
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.structureClassification.typeName}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.depreciationMaster.depreciationName}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField3}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.rentPerMonth}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField4}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField5}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{#floor.extraField6}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">																									
									<s:if test="%{#floor.manualAlv == null}" >
										<s:property default="N/A" value="%{#floor.alv}" />
									</s:if>
									<s:else>									
										<s:property default="N/A" value="%{#floor.manualAlv}" />
									</s:else>
								</div>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</s:if>

	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="CurrentTax" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold">Rs. <s:property default="N/A"
					value="%{viewMap.currTax}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="bluebox" width="15%"></td>
		<td class="bluebox" colspan="2">
			<s:text name="CurrentTaxDue" />
			:
		</td>
		<td class="bluebox" colspan="2">
			<span class="bold">Rs. <s:property default="N/A"
					value="%{viewMap.currTaxDue}" /> </span>
		</td>
	</tr>
	<tr>
		<td class="greybox" width="15%"></td>
		<td class="greybox" colspan="2">
			<s:text name="ArrearsDue" />
			:
		</td>
		<td class="greybox" colspan="2">
			<span class="bold">Rs. <s:property default="N/A"
					value="%{viewMap.totalArrDue}" /> </span>
		</td>
	</tr>
	
</table>

<script type="text/javascript">
	function showDocumentManagerView(indexNum) {
				var url = "../view/viewProperty!viewDoc.action?propertyId="+indexNum;
			window.open(url, 'docview', 'width=1000,height=400');
		}
		</script>