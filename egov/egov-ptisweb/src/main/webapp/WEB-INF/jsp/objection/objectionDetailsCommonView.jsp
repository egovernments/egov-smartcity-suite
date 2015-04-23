<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<!--View Property Details -  Start   -->
<script>
  function generateRejectionLetter(){ 
	alert("Generating Rejection Letter");
	window.location = "${pageContext.request.contextPath}/objection/rejectionLetter!print.action?model.id="+'<s:property value="objection.id"/>';
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
				<s:text name="objection.details.heading" />
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th class="bluebgheadtd">
						<s:text name="objection.number" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="objection.received.date" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="objection.received.by" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="objection.remarks" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="objection.document" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="objection.details.heading" />
					</th>
					<th class="bluebgheadtd">
						<s:text name="objection.status" />
					</th>
				</tr>
				<tr>
					<td class="greybox">
						<div align="center">
							<s:property value="%{objectionNumber}" />
						</div>
					</td>
					<s:date name="recievedOn" var="recievedOnId" format="dd/MM/yyyy" />
					<td class="greybox">
						<div align="center">
							<s:property default="N/A" value="%{recievedOnId}" />
						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property default="N/A" value="%{recievedBy}" />
						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property default="N/A" value="%{remarks}" />
						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:if test="docNumberObjection!=null">
								<a
									href='#'
									target="_parent" 
									onclick="window.open('/egi/docmgmt/documentManager!viewDocument.action?docNumber=${docNumberObjection}&moduleName=ptis'
									,'dataitem','resizable=yes,scrollbars=yes,height=700,width=800,status=yes');">${docNumberObjection}</a>
							</s:if>
							<s:else>N/A</s:else>
						</div>
					</td>
					<td class="greybox">
						<div align="center">
							<s:property default="N/A" value="%{details}" />
						</div>
					</td>
					<td class="greybox">
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
							<s:text name="hearing.number" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="objection.planned.hearingDate" />
						</th>
						
						<th class="bluebgheadtd">
							<s:text name="hearing.inspection.required" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="objection.document" />
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
						<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{hearingNumber}" />
								</div>
							</td>
							<s:date name="plannedHearingDt" var="plannedHearingDtId" format="dd/MM/yyyy" />
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{plannedHearingDtId}" />
								</div>
							</td>
							
							<td class="greybox">
								<div align="center">
									<s:if test="%{inspectionRequired}">Yes</s:if>
									<s:elseif test="%{inspectionRequired == null}">N/A</s:elseif>
									<s:else>No</s:else>
									
								</div>
							</td>
							<td class="greybox">
							<div align="center">
								<s:if test="documentNumber!=null">
									<a
									href='#'
									target="_parent" 
									onclick="window.open('/egi/docmgmt/documentManager!viewDocument.action?docNumber=${documentNumber}&moduleName=ptis'
									,'dataitem','resizable=yes,scrollbars=yes,height=700,width=800,status=yes');">${documentNumber}</a>
							</s:if>
							<s:else>N/A</s:else></div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{hearingDetails}" />
								</div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{egwStatus.description}" />
								</div>
							</td>
							<td class="greybox"></td>
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
							<s:text name="objection.document" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="objection.status" />
						</th>
					</tr>
					<s:iterator value="%{inspections}">
						<tr>
						<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{inspectionRemarks}" />
								</div>
							</td>
							<td class="greybox">
							<div align="center">
								<s:if test="documentNumber!=null">
								<a
									href='#'
									target="_parent" 
									onclick="window.open('/egi/docmgmt/documentManager!viewDocument.action?docNumber=${documentNumber}&moduleName=ptis'
									,'dataitem','resizable=yes,scrollbars=yes,height=700,width=800,status=yes');">${documentNumber}</a>
							</s:if>
							<s:else>N/A</s:else></div>
							</td>
							<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{egwStatus.description}" />
								</div>
							</td>
							<td class="greybox"></td>
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
						<th class="bluebgheadtd">
							<s:text name="outcome.rejected" />
						</th>
						<th class="bluebgheadtd">
							<s:text name="objection.document" />
						</th>
						<th class="bluebgheadtd">
							
						</th>
					</tr>
				
						<tr>
						<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{remarks}" />
								</div>
						</td>
						<s:date name="dateOfOutcome" var="dateOfOutcomeFmt" format="dd/MM/yyyy" />
						<td class="greybox">
								<div align="center">
									<s:property default="N/A" value="%{dateOfOutcomeFmt}" />
								</div>
						</td>
							<td class="greybox">
								<div align="center">
									<s:if test="%{objectionRejected}">Yes</s:if>
									<s:else>No</s:else>
									
								</div>
							</td>	
							
							<td class="greybox">
							<div align="center">
								<s:if test="docNumberOutcome!=null">
									<a
									href='#'
									target="_parent" 
									onclick="window.open('/egi/docmgmt/documentManager!viewDocument.action?docNumber=${docNumberOutcome}&moduleName=ptis'
									,'dataitem','resizable=yes,scrollbars=yes,height=700,width=800,status=yes');">${docNumberOutcome}</a>
							</s:if>
							<s:else>N/A</s:else></div>
							</td>
							<td>
							<div align="center">
							<a href='../objection/rejectionLetter!print.action?model.id =<s:property value="objection.id"/>'>										
									<s:text name="rejection.memo"></s:text> </a>			
									</div>			
					</td>
						</tr>
					
				</table>
			</td>
		</tr>
	</table>
</s:if>
