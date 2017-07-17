<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<!--View Property Details -  Start   -->
<script>
  function generateRejectionLetter(){ 
	bootbox.alert("Generating Rejection Letter");
	window.location = "${pageContext.request.contextPath}/objection/rejectionLetter-print.action?model.id="+'<s:property value="objection.id"/>';
	}
	</script>
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="propDtls" />
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th class="bluebgheadtd">
						<s:text name="propId" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="OwnerName" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="Address" />
					</th>
				</tr>
				<tr>
					<td class="greybox">
						<div align="center">
							<s:property value="%{basicProperty.upicNo}" />
						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property value="%{ownerName}" />
						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property value="%{propertyAddress}" />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

	<!--View Property Details -  End   -->


<!--View Objection Details -  Start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
			<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
					<s:text name="objection.grp.details.heading" />
					</s:if>
					<s:else>
				<s:text name="objection.details.heading" />
				</s:else>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th class="bluebgheadtd">
					<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
					<s:text name="objection.grp.number" />
					</s:if>
					<s:else>
						<s:text name="objection.number" />
						</s:else>
					</th>
					<th class="bluebgheadtd">
					<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
					<s:text name="objection.grp.received.date" />
					</s:if>
					<s:else>
						<s:text name="objection.received.date" />
						</s:else>
					</th>
					<th class="bluebgheadtd">
					<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
					<s:text name="objection.grp.received.by" />
					</s:if>
					<s:else>
						<s:text name="objection.received.by" />
						</s:else>
					</th>
					<th class="bluebgheadtd">
					<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
					<s:text name="objection.grp.remarks" />
					</s:if>
					<s:else>
						<s:text name="objection.remarks" />
						</s:else>
					</th>
					<th class="bluebgheadtd">
					<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
					<s:text name="objection.grp.details.heading" />
					</s:if>
					<s:else>
						<s:text name="objection.details.heading" />
						</s:else>
					</th>
					<th class="bluebgheadtd">
						<s:text name="objection.status" />
					</th>
				</tr>
				<tr>
					<td class="blueborderfortd">
						<div align="center">
							<s:property value="%{objectionNumber}" />
						</div>
					</td>
					<s:date name="recievedOn" var="recievedOnId" format="dd/MM/yyyy" />
					<td class="blueborderfortd">
						<div align="center">
							<s:property default="N/A" value="%{recievedOnId}" />
						</div>
					</td>
					<td class="blueborderfortd">
						<div align="center">
							<s:property default="N/A" value="%{recievedBy}" />
						</div>
					</td>
					<td class="blueborderfortd">
						<div align="center">
							<s:property default="N/A" value="%{remarks}" />
						</div>
					</td>
					
					<td class="blueborderfortd">
						<div align="center">
							<s:property default="N/A" value="%{details}" />
						</div>
					</td>
					<td class="blueborderfortd">
						<div align="center">
							<s:property default="N/A" value="%{egwStatus.description}" />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table> <!--View Objection Details -  End -->


<!--View Hearing Details -  Start -->
<s:if test="hearings!=null && hearings.size() >0 ">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="10">
				<div class="headingsmallbg">
					<s:text name="objection.hearingDet.header" />
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="10">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

					<tr>
					
						<th class="bluebgheadtd">
							<s:text name="objection.planned.hearingDate" />-<s:text name="objection.planned.hearingTime" />-<s:text name="objection.planned.hearingVenue" />
						</th>
						
						<th class="bluebgheadtd">
							<s:text name="hearing.inspection.required" />
						</th>
					
						<th class="bluebgheadtd">
							<s:text name="objection.hearingDet.header"  />
						</th>
						<th class="bluebgheadtd">
							<s:text name="objection.status" />
						</th>
					</tr>
					<s:iterator value="%{hearings}">
						<tr>
				
							<s:date name="plannedHearingDt" var="plannedHearingDtId" format="dd/MM/yyyy" />
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{plannedHearingDtId}" />-
										<s:property default=" " value="%{hearingTime}" />-
											<s:property default=" " value="%{hearingVenue}" />
								</div>
							</td>
							
							<td class="blueborderfortd">
								<div align="center">
									<s:if test="%{inspectionRequired}">Yes</s:if>
									<s:elseif test="%{inspectionRequired == null}">N/A</s:elseif>
									<s:else>No</s:else>
									
								</div>
							</td>
							
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{hearingDetails}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{egwStatus.description}" />
								</div>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>

</s:if> <!--View Hearing Details -  End -->

<!--View Inspection Details -  Start -->

<s:if test="inspections !=null && inspections.size() >0">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3">
				<div class="headingsmallbg">
					<s:text name="objection.inspectionDet.header" />
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

					<tr>
						<th class="bluebgheadtd">
							<s:text name="inspection.remarks" />
						</th>
						
						<th class="bluebgheadtd">
							<s:text name="objection.status" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="revisionPetition.generateSpecialNotice" />
						</th>
			
					</tr>
					<s:iterator value="%{inspections}">
						<tr>
						<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{inspectionRemarks}" />
								</div>
							</td>
							
							<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{egwStatus.description}" />
								</div>
							</td>
							<td class="blueborderfortd">	<div align="center">
							<s:if test="%{generateSpecialNotice}">Yes</s:if>
									<s:else>No</s:else>
							</div></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>

</s:if> <!--View Inspection Details -  End -->
<s:if test="dateOfOutcome !=null">

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3">
				<div class="headingsmallbg">
					<s:text name="objection.outcome.header" />
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

					<tr>
						<th class="bluebgheadtd">
							<s:text name="outcome.remarks" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="outcome.date" />
						</th>
						
					</tr>
				
						<tr>
						<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{remarks}" />
								</div>
						</td>
						<s:date name="dateOfOutcome" var="dateOfOutcomeFmt" format="dd/MM/yyyy" />
						<td class="blueborderfortd">
								<div align="center">
									<s:property default="N/A" value="%{dateOfOutcomeFmt}" />
								</div>
						</td>
						
						</tr>
					
				</table>
			</td>
		</tr>
	</table>
</s:if>
