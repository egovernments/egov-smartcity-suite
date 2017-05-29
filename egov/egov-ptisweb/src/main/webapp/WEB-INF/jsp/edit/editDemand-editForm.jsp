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

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="editDemand" /></title>
<link rel="stylesheet"
	href="<cdn:url value='/resources/global/css/font-icons/font-awesome/css/font-awesome.min.css' context='/egi'/>">
<script type="text/javascript">
	jQuery.noConflict();
	jQuery("#loadingMask").remove();
	var newInstallmentCount = 0;
	var mapSize = '<s:property value="%{demandDetailBeanList.size()}"/>';
	var instDetailsRowIndex = 2;
	var newDemandRsnsCount = mapSize;
	var lastIndex;
	var lastIndexOnError;
	var noOfDemandRsns = mapSize;
	var isFirstInstVisible = true;
	var isAddedFirstNewInsRow = false;

	function addNewInstallment() {

		var validationsuccess = true;
		jQuery("#instDetails tbody tr:visible").find('input, select')
				.each(
						function() {

							if ((jQuery(this).data('optional') === 0)
									&& ((!jQuery(this).val()) || jQuery(this)
											.val() === '-1')) {
								jQuery(this).focus();
								bootbox.alert(jQuery(this).data('errormsg'));
								validationsuccess = false;// set validation failure
								return false;
							}

						});

		if (!validationsuccess) {
			return false
		}

		newDemandRsnsCount = document.getElementById("mapSize").value;
		var rowIndex = document.getElementById("newInstallmentRow").rowIndex;
		var trClones = new Array();
		var instDetailsTable = document.getElementById("instDetails");
		if (newInstallmentCount == 0) {
			for (var i = 0; i < noOfDemandRsns; i++) {
				var row = instDetailsTable.rows[i + instDetailsRowIndex];
				row.style.display = "table-row";
			}
			newInstallmentCount++;
			isFirstInstVisible = true;
		} else {
			var newRows = Number(rowIndex) + Number(newDemandRsnsCount);
			for (var i = rowIndex, k = 0; i < newRows; i++, k++) {
				trClones[k] = instDetailsTable.rows[i].cloneNode(true);
			}

			for (var j = 0; j < k; j++) {
				trClones[j].style.display = "table-row";
				if (j == 0) {
					instDetailsTable.tBodies[0].insertBefore(trClones[j],
							instDetailsTable.rows[rowIndex]);
				} else {
					instDetailsTable.tBodies[0].insertBefore(trClones[j],
							instDetailsTable.rows[rowIndex + j]);
				}
			}
			newInstallmentCount++;
		}

		if (!isAddedFirstNewInsRow) {
			setDefaultTemplateRowCount();
		}

		rearrangeIndexes();
	}

	function deleteRecentInstallment() {

		var instDetailsTable = document.getElementById("instDetails");

		//if for existing demand info row delete prevention and else if new installment first delete prevention
		if ((!isAddedFirstNewInsRow) && (jQuery('#demandinfos').length > 0)) {
			return;
		} else if ((noOfDemandRsns === jQuery('#instDetails tbody tr:visible[id="newInstallmentRow"]').length)
				&& (jQuery('#demandinfos').length === 0)) {
			return;
		}

		if (newInstallmentCount > 1) {
			for (var i = 0; i < newDemandRsnsCount; i++) {
				instDetailsTable.tBodies[0].deleteRow(instDetailsRowIndex);
				lastIndex--;
			}
			newInstallmentCount--;

		} else if (newInstallmentCount == 1) {
			isFirstInstVisible = false;
			var instDetailsTable = document.getElementById("instDetails");
			for (var i = 0; i < noOfDemandRsns; i++) {
				var row = instDetailsTable.rows[i + instDetailsRowIndex];
				if (jQuery(row).attr('id') === 'newInstallmentRow') {
					row.style.display = "none";
				}
				lastIndex--;
			}
			setDefaultTemplateRowCount();
		}

		if (lastIndex) {
			document.getElementById("lastIdx").value = lastIndex;
		}

	}

	function rearrangeIndexes() {

		var instDetailsTable = document.getElementById("instDetails");
		// New index for textfield name & value attribute	
		var newInstallments = newInstallmentCount;
		var z = 0;
		var li = 0;
		if (document.getElementById("lastIdx").value == "") {
			li = (parseInt(document.getElementById("mapSize").value));
		} else {
			li = (parseInt(document.getElementById("lastIdx").value) + 1);
		}

		for (var i = 0; i < noOfDemandRsns; i++) {
			var row = instDetailsTable.rows[i + instDetailsRowIndex];
			var attrValueInstallment = "demandDetailBeanList[" + li
					+ "].installment.id";
			var attrNameReasonMaster = "demandDetailBeanList[" + li
					+ "].reasonMaster";
			var attrValueActAmnt = "demandDetailBeanList[" + li
					+ "].actualAmount";
			var attrValueRevsdAmnt = "demandDetailBeanList[" + li
					+ "].revisedAmount";
			var attrValueActColl = "demandDetailBeanList[" + li
					+ "].actualCollection";
			var attrValueRevsdColl = "demandDetailBeanList[" + li
					+ "].revisedCollection";
			var isNew = "demandDetailBeanList[" + li + "].isNew";
			row.cells[1].childNodes[1].setAttribute("name",
					attrNameReasonMaster);
			row.cells[2].childNodes[1].childNodes[1].setAttribute("name",
					attrValueActAmnt);
			row.cells[2].childNodes[1].childNodes[1].value = "";
			row.cells[4].childNodes[1].childNodes[1].setAttribute("name",
					attrValueActColl);
			row.cells[4].childNodes[1].childNodes[1].value = "";
			if (newInstallments == 0) {
				row.cells[0].childNodes[1].childNodes[5].setAttribute("name",
						attrValueInstallment);
				row.cells[0].childNodes[1].childNodes[7].setAttribute("name",
						isNew);
			} else {
				row.cells[0].childNodes[1].childNodes[1].setAttribute("name",
						attrValueInstallment);
				if (z == 0) {
					row.cells[0].childNodes[3].setAttribute("name", isNew);
					row.cells[0].childNodes[3].setAttribute("value", true);
				} else {
					row.cells[0].childNodes[1].childNodes[3].setAttribute(
							"name", isNew);
					row.cells[0].childNodes[1].childNodes[3].setAttribute(
							"value", true);
				}

			}

			if (newInstallments >= 1
					&& (z != 0 && (z % newDemandRsnsCount) == 0)) {
				newInstallments--;
				z = 0;
			} else {
				z++;
			}
			li++;
		}
		lastIndex = parseInt(li) - 1;
		document.getElementById("lastIdx").value = lastIndex;
	}

	function assignInstallmentId(obj, id) {
		var instDetailsTable = document.getElementById("instDetails");
		var selRowIndex = obj.parentNode.parentNode.parentNode.rowIndex;
		var mapSize = document.getElementById("mapSize").value;
		for (var i = 1; i <= mapSize; i++) {
			var row = instDetailsTable.rows[i + selRowIndex];
			row.cells[0].childNodes[1].childNodes[1].setAttribute("value", id);
		}
	}

	jQuery(document).ready(function($) {
		setDefaultTemplateRowCount();
	});

	function setDefaultTemplateRowCount() {
		var rowTemplateCount = jQuery('#instDetails tbody tr:visible[id="newInstallmentRow"]').length;
		if (rowTemplateCount !== 0) {
			isAddedFirstNewInsRow = true;
			noOfDemandRsns = rowTemplateCount;
		} else if (rowTemplateCount === 0) {
			isAddedFirstNewInsRow = false;
		}
	}
</script>
</head>
<body>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle" id="property_error_area">
			<div class="errortext">
				<s:actionerror />
			</div>
		</div>
	</s:if>
	<div class="formmainbox">
		<div class="headingbg">
			<s:text name="editDemand" />
		</div>
		<s:form name="editDemandForm" action="editDemand-update"
			theme="simple">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox"></td>
					<td class="bluebox" colspan="2">&nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <s:text
							name="prop.Id" />:
					</td>
					<td class="bluebox" colspan="2"><span class="bold"> <s:property
								value="%{propertyId}" /> <s:hidden name="propertyId"
								value="%{propertyId}" />
					</span> <s:hidden name="lastIdx" id="lastIdx" /> <s:hidden name="mapSize"
							value="%{demandReasonMap.size()}" id="mapSize" /></td>
				</tr>
				<tr>
					<td class="greybox"></td>
					<td class="greybox" colspan="2">&nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <s:text
							name="OwnerName" />:
					</td>
					<td class="greybox" colspan="2"><span class="bold"> <s:property
								value="%{ownerName}" /> <s:hidden name="ownerName"
								value="%{ownerName}" />
					</span></td>
				</tr>
				<tr>
					<td class="greybox"></td>
					<td class="greybox" colspan="2">&nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <s:text
							name="MobileNumber" />:
					</td>
					<td class="greybox" colspan="2"><span class="bold"> <s:property
								value="%{mobileNumber}" /> <s:hidden name="mobileNumber"
								value="%{mobileNumber}" />
					</span></td>
				</tr>
				<tr>
					<td class="bluebox"></td>
					<td class="bluebox" colspan="2">&nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <s:text
							name="PropertyAddress" />:
					</td>
					<td class="bluebox" colspan="2"><span class="bold"> <s:property
								value="%{propertyAddress}" /> <s:hidden name="propertyAddress"
								value="%{propertyAddress}" />
					</span></td>
				</tr>
				<tr>
					<td colspan="5" class="greybox">
						<div align="center">
							<table width="80%" border="0" cellpadding="0" cellspacing="0"
								class="tablebottom" id="instDetails">
								<tr>
									<th class="bluebgheadtd" width="2%"><s:text
											name="installment" /></th>
									<th class="bluebgheadtd" width="3%"><s:text name="taxName" />
									</th>
									<th class="bluebgheadtd" width="2%"><s:text
											name="actualTax" /></th>
									<th class="bluebgheadtd" width="2%"><s:text
											name="revisedTax" /></th>
									<th class="bluebgheadtd" width="2%"><s:text
											name="actualCollection" /></th>
									<th class="bluebgheadtd" width="2%"><s:text
											name="revisedCollection" /></th>
								</tr>
								<!-- <tr id="actionoptions">										
										<td colspan="8" align="right" style="border-right: 1px solid #E9E9E9;padding: 3px;border-left: 1px solid #E9E9E9;background: #fcf8e3;">
										   <span style="vertical-align: top;">Add/Remove Installment</span>
										   &nbsp;
										   <span id="addInstallment" name="addInstallment" class="tblactionicon add" alt="addOwnerBtn" onclick="addNewInstallment();">
											    <i class="fa fa-plus-circle"></i>
										   </span>
										  &nbsp;
										  <span id="delInstallment" name="delInstallment" class="tblactionicon delete" alt="removeOwnerBtn" onclick="deleteRecentInstallment();">
										        <i class="fa fa-minus-circle"></i>
										  </span>
											
										</td>
									</tr> 		 -->
								<%-- <s:if test="%{hasActionErrors() == false}"> --%>
								<%-- <s:set
										value="{@org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_WARRANT_FEE, @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_NOTICE_FEE, @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_COURT_FEE, @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_PENALTY_FINES}"
										var="demandRsnToExclude" /> --%>
								<%-- <s:iterator value="demandReasonMap" status="itrStatus" var="rsn">
											<s:if test="%{#demandRsnToExclude.contains(key) == false}" >
											<tr id="newInstallmentRow">
												<s:if test="%{#itrStatus.count == 1}" >												
												<td class="blueborderfortd">												
													<div align="center">
														<s:select id="installments" name="installments"
														list="dropdownData.allInstallments" headerKey="-1"
														headerValue="%{getText('default.select')}" listKey="id"
														data-optional="0" data-errormsg="Please select installment!"
														listValue="description" onchange="assignInstallmentId(this, this.value)"/>
													</div>
													<s:hidden />
												</td>
												</s:if>
												<s:else>
													<td class="blueborderfortd">
														<div align="center">
															<s:hidden />
															<s:hidden />
														</div>
													</td>
												</s:else>
												<td class="blueborderfortd" align="left">			
														<s:hidden value="%{key}" />
														<div align="left">
															<s:property value="key" />
														</div>
												</td>		
												<td class="blueborderfortd" align="center">
													<div align="right">
													  <s:if test="%{#itrStatus.count == 1}" >
														<s:textfield
															name="demandDetailBeanList[%{#idx}].actualAmount"
															id="revisedTax" size="10" maxlength="10"
															onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Tax');"
															value="%{demandDetailBeanList[#idx].actualAmount}"	
															data-optional="0" data-errormsg="Actual amount is required!"												
															style="text-align: right" />
													  </s:if>
													  <s:else>
													  	<s:textfield
															name="demandDetailBeanList[%{#idx}].actualAmount"
															id="revisedTax" size="10" maxlength="10"
															onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Tax');"
															value="%{demandDetailBeanList[#idx].actualAmount}"	
															style="text-align: right" />
													  </s:else>
													</div>
												</td>
												<td class="blueborderfortd">
													<div align="center">
														N/A
													</div>
												</td>
												<td class="blueborderfortd">
													<div align="right">
														<s:textfield
														name="demandDetailBeanList[%{#idx}].actualCollection"
														id="revisedCollection" size="10" maxlength="10"
														onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Collection');"
														style="text-align: right" 
														value="%{demandDetailBeanList[#idx].actualCollection}"/>
													</div>
												</td>
												<td class="blueborderfortd" align="center">
													<div align="center">
														N/A
													</div>
												</td>
											</tr>
											</s:if>
										</s:iterator> --%>
								<script type="text/javascript">
									if (isFirstInstVisible == true) {
										isFirstInstVisible = false;
										var instDetailsTable = document
												.getElementById("instDetails");
										for (var i = 0; i < noOfDemandRsns; i++) {
											var row = instDetailsTable.rows[i
													+ instDetailsRowIndex];
											row.style.display = "none";
										}
									}
								</script>
								<%-- </s:if>	 --%>
								<script type="text/javascript">
									var newInstCountOnError = 0;
								</script>
								<s:set value="%{demandDetailBeanList.size()}" var="listSize" />
								<s:set value="0" var="count" />
								<s:set value="#listSize" var="j" />
								<%-- j is the each new installment start index --%>
								<s:set value="%{#j - demandDetailBeanList.size}" var="j" />
								<%-- idx index value for the installmentss demand reason --%>
								<s:set value="%{#j}" var="idx" />
								<s:iterator value="demandDetailBeanList"
									status="demandInfoStatus">

									<!-- #idx > 0 && ((#idx % 10) == 0) && demandDetailBeanList[#idx].installment != demandDetailBeanList[#idx - 1].installment -->
									<s:if test="%{demandDetailBeanList[#idx].isNew == true}">
										<tr id="newInstallmentRow">
											<s:if
												test="%{demandDetailBeanList[#idx].reasonMaster == @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_GENERAL_TAX ||
												demandDetailBeanList[#idx].reasonMaster == @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_VACANT_TAX}">
												<td class="blueborderfortd">
													<div align="center">
														<s:select id="installments"
															name="demandDetailBeanList[%{#idx}].installment.id"
															value="%{demandDetailBeanList[#idx].installment.id}"
															list="dropdownData.allInstallments" headerKey="-1"
															headerValue="%{getText('default.select')}" listKey="id"
															listValue="description" data-optional="0"
															data-errormsg="Please select installment!"
															onchange="assignInstallmentId(this, this.value)" />
													</div> <s:hidden name="demandDetailBeanList[%{#idx}].isNew"
														value="%{demandDetailBeanList[#idx].isNew}" />
												</td>
												<script type="text/javascript">
													var newInstCountOnError = 0;
													newInstCountOnError++;
													isFirstInstVisible = true;
												</script>
											</s:if>
											<s:else>
												<td class="blueborderfortd">
													<div align="center">
														<s:hidden
															name="demandDetailBeanList[%{#idx}].installment.id"
															value="%{demandDetailBeanList[#idx].installment.id}" />
														<s:hidden name="demandDetailBeanList[%{#idx}].isNew"
															value="%{demandDetailBeanList[#idx].isNew}" />
													</div>
												</td>
											</s:else>
											<td class="blueborderfortd">
												<div align="left">
													<s:property
														value="%{demandDetailBeanList[#idx].reasonMaster}" />
													<s:hidden name="demandDetailBeanList[%{#idx}].reasonMaster"
														value="%{demandDetailBeanList[#idx].reasonMaster}" />
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:if test="%{#demandInfoStatus.count == 1}">
														<s:textfield
															name="demandDetailBeanList[%{#idx}].actualAmount"
															id="revisedTax" size="10" maxlength="7"
															onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Tax');"
															value="%{demandDetailBeanList[#idx].actualAmount}"
															data-optional="0"
															data-errormsg="Actual amount is required"
															style="text-align: right" />
													</s:if>
													<s:else>
														<s:textfield
															name="demandDetailBeanList[%{#idx}].actualAmount"
															id="revisedTax" size="10" maxlength="7"
															onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Tax');"
															value="%{demandDetailBeanList[#idx].actualAmount}"
															style="text-align: right" />
													</s:else>
												</div>
											</td>
											<td class="blueborderfortd">
												<div align="center">N/A</div>
											</td>
											<td class="blueborderfortd">
												<div align="right">
													<s:textfield
														name="demandDetailBeanList[%{#idx}].actualCollection"
														id="revisedCollection" size="10" maxlength="7"
														onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Collection');"
														style="text-align: right"
														value="%{demandDetailBeanList[#idx].actualCollection}" />
												</div>
											</td>
											<td class="blueborderfortd" align="center">
												<div align="center">N/A</div>
											</td>
										</tr>
									</s:if>
									<s:else>
										<%@ include file="editDemandInstallmentDetail.jsp"%>
									</s:else>
									<s:if test="%{#count == demandDetailBeanList.size}">
										<s:set value="0" var="count" />
										<s:set value="%{#j - demandDetailBeanList.size +1}" var="j" />
										<s:set value="#j" var="idx" />
									</s:if>
									<s:else>
										<s:set value="%{#count + 1}" var="count" />
										<s:set value="%{#idx + 1}" var="idx" />
									</s:else>
								</s:iterator>
								<script type="text/javascript">
									lastIndex = lastIndexOnError;
									newInstallmentCount = newInstCountOnError;
								</script>
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td class="bluebox"></td>
					<td class="bluebox" colspan="2">&nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <s:text
							name="remarks.head"></s:text><span class="mandatory1">*</span> :
					</td>
					<td class="bluebox" colspan="2"><s:textarea name="remarks"
							id="remarks" cols="60" rows="5"
							onkeypress="checkTextAreaLength(this, 256)"></s:textarea></td>
				</tr>
				<tr>
					<td class="bluebox" colspan="5"><font color="red"><s:text
								name="editdemamd.note.message" /></font></td>
				</tr>
			</table>
			<br />
	</div>
	<div class="buttonbottom" align="center">
		<s:submit name="Update" value="Update" cssClass="buttonsubmit"
			method="update" />
		<input type="reset" value="Reset" class="button">
		<input type="button" name="button2" id="button2" value="Close"
			class="button" onclick="window.close();" />
	</div>
	</s:form>
	<s:if
		test="%{hasActionErrors() == false || isFirstInstVisible == false}">
		<script type="text/javascript">
			
		</script>
	</s:if>
</body>
</html>