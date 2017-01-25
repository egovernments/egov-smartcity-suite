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

<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<script type="text/javascript">
			
			function getPropdetails(obj, assessmentNum) {
				var selectedValue = obj.options[obj.selectedIndex].value;
				if (selectedValue == "TransferProperty") {
					window.location = "../property/transfer/redirect.action?assessmentNo=" + assessmentNum;
				} else if (selectedValue == 'ADD_OR_ALTER') {
					window.location = "../modify/modifyProperty-modifyForm.action?modifyRsn=ADD_OR_ALTER&indexNumber=" + assessmentNum;
				} /* else if (selectedValue=='Bifurcation') {
					window.location="../modify/modifyProperty-modifyForm.action?modifyRsn=BIFURCATE&indexNumber="+assessmentNum;
				} else if (selectedValue == 'RevisionPetition') {
					window.location = "../revPetition/revPetition-newForm.action?propertyId=" + assessmentNum;
				}  */else if (selectedValue == 'CollectTax') {
					window.location = "/../ptis/search/searchProperty-searchOwnerDetails.action?assessmentNum=" + assessmentNum;
				}else if(selectedValue == 'VacancyRemissionMonthlyUpdate'){
					window.location = "/ptis/vacancyremission/monthlyupdate/" + assessmentNum;
				}else if(selectedValue == 'VacancyRemissionFinalApproval'){
					window.location = "/ptis/vacancyremissionapproval/create/" + assessmentNum;
				}
				/* else if (selectedValue == 'EDIT_DATAENTRY') {
					window.location = "../modify/modifyProperty-modifyDataEntry.action?modifyRsn=EDIT_DATA_ENTRY&indexNumber=" + assessmentNum;
				} *//*  else if (selectedValue == 'ADD_EDIT_DEMAND') {
					window.location = "../edit/editDemand-newEditForm.action?propertyId=" + assessmentNum;
				} */ /* else if (selectedValue == 'VacancyRemission') {
					window.location = "/ptis/vacancyremission/create/" + assessmentNum+",normalSearch";
				} else if(selectedValue == 'VacancyRemissionMonthlyUpdate'){
					window.location = "/ptis/vacancyremission/monthlyupdate/" + assessmentNum;
				} else if(selectedValue == 'VacancyRemissionFinalApproval'){
					window.location = "/ptis/vacancyremissionapproval/create/" + assessmentNum;
				} else if(selectedValue == 'Demolition'){
					window.location = "/ptis/property/demolition/" + assessmentNum;
				} else if(selectedValue == 'TaxExemption'){
					window.location = "/ptis/exemption/form/" + assessmentNum;
				} else if(selectedValue == 'addArrears'){
					window.location = "/ptis/addarrears/form/" + assessmentNum;
				} else if(selectedValue == 'GeneralRevisionPetition'){
					window.location="../modify/modifyProperty-modifyForm.action?modifyRsn=GRP&indexNumber="+assessmentNum;
				} */ /* else if(selectedValue == 'WriteOff'){
					window.location = "/ptis/writeOff/form/" + assessmentNum;
				} */ /* else if(selectedValue == 'editOwner'){
			      window.location = "/ptis/editowner/" + assessmentNum;
			    }  */
			}

			function gotoSearchForm(){
				document.viewform.action='${pageContext.request.contextPath}/search/searchProperty-searchForm.action';
				document.viewform.submit(); 
			}

		</script>
		<title><s:text name="searchResults.title" /></title>
	</head>
	<body>
		<div class="formmainbox">
		    <div class="headingbg">Search Results</div>
			<table width=100% border="0" class="tablebottom">
				<s:form name="viewform" theme="simple">
					<div class="headingsmallbgnew">
						<s:text name="scrhCriteria"></s:text>
						<span class="mandatory">
							<s:property	value="%{searchCriteria}" />
						</span> / <s:text name="totProp"></s:text>
						<span class="mandatory">
							<s:property value="%{searchResultList.size}" /> 
							<s:text name="matchRecFound" /> 
						</span>
						<div class="searchvalue1">
							<s:text name="scrhVal"></s:text>
							<s:property value="%{searchValue}" />
						</div>
					</div>
					<s:if test="%{searchResultList != null && searchResultList.size >0}">
						<tr>
							<display:table name="searchResultList" id="linksTables"
								pagesize="10" export="true" requestURI="" class="tablebottom"
								style="width:100%" uid="currentRowObject">								
								<display:column
									title="Assessment Number" media="html"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center">
									<a href="../view/viewProperty-viewForm.action?propertyId=${currentRowObject.assessmentNum}" >
										${currentRowObject.assessmentNum} </a>
								</display:column>
								<display:column	title="Assessment Number" property="assessmentNum" media="csv" />
								<display:column	title="Assessment Number" property="assessmentNum" media="xml" />
								<display:column	title="Assessment Number" property="assessmentNum" media="excel" />
								<display:column property="ownerName"
									title="Owner Name"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:left" />
								<display:column property="address"
									title="Address"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:left" />
								<display:column property="currFirstHalfDemand"
									title="Current FirstHalf Tax"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column property="currFirstHalfDemandDue"
									title="Current FirstHalf Tax Due"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="width:10%;text-align:center" />
									<display:column property="currSecondHalfDemand"
									title="Current SecondHalf Tax"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column property="currSecondHalfDemandDue"
									title="Current SecondHalf Tax Due"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="width:10%;text-align:center" />
								<display:column property="arrDemandDue"
									title="Arrear Tax Due"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
									
								<display:column title="Action" headerClass="bluebgheadtd"
									media="html" class="blueborderfortd" style="text-align:center">
									<select id="actionValue" name="actionValue"
										style="align: center;width:100%"
										onchange="getPropdetails(this,'<s:property value="%{#attr.currentRowObject.assessmentNum}"/>')">
										<%-- <s:property value="%{#attr.currentRowObject.isTaxExempte}"/> --%>
										<option value="">
											----Choose----
										</option>
								
										<%-- <s:if test="%{#attr.currentRowObject.isDemandActive && #attr.currentRowObject.propType!=@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS}">
											<c:if test="${currentRowObject.isTaxExempted == true}">
												<option value="TaxExemption">
															<s:text name="TaxExemption"></s:text>
												</option>
											</c:if>
										</s:if> --%>
										<s:if test="%{(roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_ULB_OPERATOR.toUpperCase()) ||
										roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@CSC_OPERATOR_ROLE.toUpperCase()))
										&& #attr.currentRowObject.propType!=@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS}">
										<c:if test="${currentRowObject.isTaxExempted == false }">
											<%-- <c:if test="${currentRowObject.source == 'D'}">
												<option value="EDIT_DATAENTRY">
													<s:text name="editdataentry.title"></s:text>
												</option>
												
												<option value="ADD_EDIT_DEMAND">
													<s:text name="addeditDemand"></s:text> 
												</option>
											</c:if>
											<c:if test="${currentRowObject.source == 'M'}">
											<option value="ADD_EDIT_DEMAND">
													<s:text name="addeditDemand"></s:text> 
												</option>
											</c:if> --%>
											<s:if test="%{#attr.currentRowObject.isDemandActive}">
												<option value="ADD_OR_ALTER">
													<s:text name="viewprop.option.alter"></s:text>
												</option>
												<%-- <option value="Bifurcation">
													<s:text name="Bifurcation"></s:text>
												</option> --%>
												<option value="TransferProperty">
													<s:text name="transferProperty"></s:text>
												</option>
												<%-- <option value="TaxExemption">
													<s:text name="TaxExemption"></s:text>
										        </option>
										        <option value="GeneralRevisionPetition">
													<s:text name="GeneralRevisionPetition"></s:text>
										        </option> --%>
										        <%-- <option value="WriteOff">
													<s:text name="WriteOff"/>
										        </option> --%>
												<%-- <c:if test="${currentRowObject.isUnderWorkflow == false && currentRowObject.enableVacancyRemission == true}">
													<s:if test="%{(#attr.currentRowObject.propType!=@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND || !#attr.currentRowObject.isTaxExempted)}">
														<option value="VacancyRemission">
															<s:text name="vacancyRemission"></s:text>
														</option>
														<option value="Demolition">
															<s:text name="Demolition"></s:text>
														</option>
													</s:if>
												</c:if> --%>
												<%-- <s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_ULB_OPERATOR.toUpperCase()) && isNagarPanchayat}">
													<c:if test="${currentRowObject.isUnderWorkflow == false && currentRowObject.source == 'M'}">
														<option value="addArrears">
															<s:text name="addArrears"></s:text>
														</option>
													</c:if>
												</s:if> --%>
											</s:if>
											<%-- <s:else>
												<option value="RevisionPetition">
													<s:text name="revisionPetition"></s:text>
												</option>
											</s:else>--%>
											</c:if> 
										</s:if>
										<%-- <s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@PTAPPROVER_ROLE.toUpperCase())}">
												<option value="editOwner">
													<s:text name="editOwner"></s:text>
												</option>
										</s:if> --%>
										<s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@ROLE_COLLECTION_OPERATOR.toUpperCase())}">
											<c:if test="${currentRowObject.isTaxExempted == false }">
												<option value="CollectTax">
													<s:text name="collectTax"></s:text>
												</option>
									   </c:if>
									   
									</s:if>
									<s:if test="%{roleName.contains(@org.egov.ptis.constants.PropertyTaxConstants@PTVERIFIER_ROLE.toUpperCase()) && #attr.currentRowObject.isDemandActive
									&& #attr.currentRowObject.propType!=@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_EWSHS}">
										<c:if test="${currentRowObject.propType != 'VAC_LAND' && currentRowObject.isTaxExempted == false}">
											<c:if test="${currentRowObject.enableMonthlyUpdate == true && currentRowObject.enableVRApproval == false}">
												<option value="VacancyRemissionMonthlyUpdate">
													<s:text name="vacancyRemission.monthly.update"></s:text>
												</option>
											</c:if>
											<c:if test="${currentRowObject.enableVRApproval == true}">
												<option value="VacancyRemissionFinalApproval">
													<s:text name="vacancyRemission.final.approval"></s:text>
												</option>
											</c:if>
										</c:if>
									</s:if> 
								</select>
								</display:column>
								<display:setProperty name="paging.banner.item" value="Record" />
								<display:setProperty name="paging.banner.items_name"
									value="Records" />
								<display:setProperty name="export.pdf" value="false" />
								<display:setProperty name="export.rtf" value="false" />
								<display:setProperty name="export.xml.filename" value="SearchProperty.xml" />
								<display:setProperty name="export.xml" value="true" />
								<display:setProperty name="export.csv.filename" value="SearchProperty.csv" />
								<display:setProperty name="export.csv" value="true" />
								<display:setProperty name="export.excel.filename" value="SearchProperty.xls" />
								<display:setProperty name="export.excel" value="true" />
							</display:table>
						</tr>
					</s:if>
					<s:else>
						<tr>
							<td align="center">
								<span class="mandatory"><s:text name="noRecFound"></s:text>
								</span>
							</td>
						</tr>
					</s:else>
					<tr>
						<td>
							<div class="buttonsearch" align="center">
							    <input type="submit" value="Search Again" class="buttonsubmit"
									onClick="gotoSearchForm();" />
								<input type="button" value="Close" class="button"
									onClick="window.close()" />
								
							</div>
						</td>
					</tr>
				</s:form>
			</table>
		</div>
	</body>
</html>
