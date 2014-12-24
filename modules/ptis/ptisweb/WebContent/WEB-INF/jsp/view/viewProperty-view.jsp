<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
	<head>
		<title><s:text name="viewPropDet.title" /></title>
		<script type="text/javascript">
		
			function loadOnStartup () {
				var btnCheckbox = document.getElementById('taxEnsureCheckbox');
				var btnPayTax = document.getElementById('display');
				if (btnCheckbox != null && btnPayTax != null) {
					btnPayTax.disabled = (btnCheckbox.checked) ? false : true;
				}
			}
			
			function switchPayTaxButton (ensureCheckbox) {
				var buttonPayTax = document.getElementById('display');
				buttonPayTax.disabled = (ensureCheckbox.checked) ? false : true;			
			}

			function setLevyPenalyBeforeSubmit() {
				var propertyId = '<s:property value="%{basicProperty.upicNo}"/>';
				window.location='../collection/collectPropertyTax!showPenalty.action?propertyId='+propertyId;
			}
		</script>
	</head>
	<body onload="loadOnStartup(); ">
		<s:form action="searchProperty" method="post" name="indexform"
				theme="simple" >
		<div class="formmainbox">
			<div class="formheading"></div>
			<div class="headingbg"><s:text name="PropertyDetail" /></div>
			<jsp:include page="viewProperty.jsp"/>
			<jsp:include page="viewObjection.jsp"/>
			<jsp:include page="../recovery/viewRecovery.jsp"/>
			<div class="buttonbottom" align="center">
			<c:if test="${fn:contains(roleName,'PTCREATOR')}">
			<c:if test="${markedForDeactive == 'N'}">	
			
				<input type="button" class="button" name="btnDeactivate" id="btnDeactivate" value="Deactivate Property"	onclick="window.location='../deactivate/deactivateProperty!newForm.action?indexNumber=<s:property value="%{basicProperty.upicNo}" />';" />

				<input type="button" class="button2" name="btnChAddrProperty"
					id="btnDeactivate" value="Change Property Address" onclick="window.location='../modify/changePropertyAddress!newForm.action?indexNumber=<s:property value="%{basicProperty.upicNo}" />';" />

				<input type="button" class="button" name="btnTrnsProperty"
					id="btnTrnsProperty" value="Mutation" onclick="window.location='../transfer/transferProperty!transferForm.action?indexNumber=<s:property value="%{basicProperty.upicNo}" />';" />

				<input type="button" class="button" name="btnModifyProperty"
					id="btnModifyProperty" value="Modification" onclick="window.location='../modify/modifyProperty!modifyOrDataUpdateForm.action?modifyRsn=MODIFY&indexNumber=<s:property value="%{basicProperty.upicNo}"/>';" />

				<input type="button" class="button" name="btnAmalgProperty"
					id="btnAmalgProperty" value="Amalgamation" onclick="window.location='../modify/modifyProperty!modifyForm.action?modifyRsn=AMALG&indexNumber=<s:property value="%{basicProperty.upicNo}"/>';" />

				<input type="button" class="button" name="btnModifyProperty"
					id="btnModifyProperty" value="Bifurcation" onclick="window.location='../modify/modifyProperty!modifyForm.action?modifyRsn=BIFURCATE&indexNumber=<s:property value="%{basicProperty.upicNo}"/>';" />
					
				<input type="button" class="button" name="objection" 
					id="objection" value="Objection" onclick="window.location='../objection/objection!newForm.action?propertyId=<s:property value="%{basicProperty.upicNo}" />';" />
					
				<input type="button" class="button" name="recovery" 
					id="recovery" value="Recovery" onclick="window.location='../recovery/recovery!newform.action?propertyId=<s:property value="%{basicProperty.upicNo}" />';" />

				<s:if test="%{basicProp.isMigrated != null && basicProp.isMigrated == 'Y'">
					<input type="button" class="button2" name="assessmentDataUpdate"
						id="assessmentDataUpdate" value="Assessment Data update" 
						onclick="window.location='../modify/modifyProperty!modifyOrDataUpdateForm.action?modifyRsn=DATA_UPDATE&indexNumber=<s:property value="%{basicProperty.upicNo}"/>';" />
				</s:if>

					</c:if>
			</c:if>
			<c:if test="${fn:contains(roleName,'OPERATOR')}">
				<div align="center">
									<s:checkbox name="taxEnsureCheckbox" id="taxEnsureCheckbox" onclick="switchPayTaxButton(this);" required="true" />
									<span style="font-size:15px; color:red">
										I have verified the tax details indicated above before proceeding to Pay Bill <br>
Note: In case of any doubts about your taxes, please contact the concerned ward officer.
									</span> 
				</div><br>
					<div align="center">
						<table>
							<tr>
								<td align="center"><input type="button" name="display"
									id="display" value="Pay Bill" class="button"
									onclick="setLevyPenalyBeforeSubmit();" /></td>
							</tr>
						</table>
					</div><br>
				</c:if>
				<input type="button" class="button" name="btnViewDCB"
					id="btnViewDCB" value="View DCB"
					onclick="window.location='../view/viewDCBProperty!displayPropInfo.action?propertyId=<s:property value="%{basicProperty.upicNo}" />';" />
				<input type="button" class="button" name="SearchProperty"
					id="SearchProperty" value="Search Property" onclick="window.location='../search/searchProperty!searchForm.action';" />
					
				<c:if test="${fn:contains(roleName,'PTCREATOR') || fn:contains(roleName,'PTVALIDATOR') || fn:contains(roleName,'PTAPPROVER')}">	
					<input type="button" class="button" name="Notice125"  
						id="Notice125" value="Notice 125" onclick="window.location='../notice/propertyTaxNotice!generateNotice.action?noticeType=Notice125&indexNumber=<s:property value="%{basicProperty.upicNo}" />';" />
					<input type="button" name="button2" id="button2" value="GenerateCalSheet" class="button"
							onclick="window.location='../notice/propertyIndividualCalSheet!generateCalSheet.action?indexNum=<s:property value="%{basicProperty.upicNo}"/>';" ) />
					<input type="button" name="generateBill" id="generateBill" value="Generate Bill" class="button"
						onclick="window.location='../bills/billGeneration!generateBill.action?indexNumber=<s:property value="%{basicProperty.upicNo}" />';" />
				</c:if>		
					
				<input type="button" name="button2" id="button2" value="Close"
					class="button" onclick="window.close();" />
				<s:hidden label="upicNo" id="upicNo" name="upicNo"
					value="%{basicProperty.upicNo}" />
			</div>				
		</div>
		</s:form>
	</body>
</html>
