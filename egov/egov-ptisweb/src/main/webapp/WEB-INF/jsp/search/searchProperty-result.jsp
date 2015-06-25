<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------   -->
<%@ include file="/includes/taglibs.jsp"%>

<html>
	<head>
		<script type="text/javascript">
		
		function viewPropDetails(indexNum)
		{
				window.location="../view/viewProperty-viewForm.action?propertyId="+indexNum;
		}
			
		function getPropdetails(obj,indexNum)
		{
			var selectedValue = obj.options[obj.selectedIndex].value;
	       if(selectedValue=="ViewProperty")
			{
				window.location="../view/viewProperty-viewForm.action?propertyId="+indexNum;
			}
			else if(selectedValue=="TransferProperty")
			{
			window.location="../transfer/transferProperty-transferForm.action?indexNumber="+indexNum;
			}
			else if(selectedValue=="ChangeAddress")
			{
			window.location="../modify/changePropertyAddress-newForm.action?indexNumber="+indexNum;
			}
			else if(selectedValue=="DeactivateProperty")
			{
			window.location="../deactivate/deactivateProperty-newForm.action?indexNumber="+indexNum;
			}
			else if(selectedValue=='Amalgamation') {
				window.location="../modify/modifyProperty-modifyForm.action?modifyRsn=AMALG&indexNumber="+indexNum;
			}
			else if(selectedValue=='Bifurcation') {
				window.location="../modify/modifyProperty-modifyForm.action?modifyRsn=BIFURCATE&indexNumber="+indexNum;
			}
			else if(selectedValue=='Modification') {
				window.location="../modify/modifyProperty-modifyOrDataUpdateForm.action?modifyRsn=MODIFY&indexNumber="+indexNum;
			}
			else if(selectedValue=='Objection') {
				window.location="../objection/objection-newForm.action?propertyId="+indexNum;
			}
			else if(selectedValue=='Notice 125') {
				window.location="../notice/propertyTaxNotice-generateNotice.action?noticeType=Notice125&indexNumber="+indexNum;
			}
			else if(selectedValue=='Recovery') {
				window.location="../recovery/recovery-newform.action?propertyId="+indexNum;
			} else if (selectedValue == 'Assessment Data update') {				
				window.location="../modify/modifyProperty-modifyOrDataUpdateForm.action?modifyRsn=DATA_UPDATE&indexNumber="+indexNum;
			} else if (selectedValue == 'Edit Demand') {				
				window.location="../edit/editDemand-newEditForm.action?propertyId="+indexNum;
			} else if (selectedValue == 'Edit Property Data') {				
				window.location="../modify/modifyProperty-editOwnerForm.action?modifyRsn=EDIT_OWNER&indexNumber="+indexNum;
			}
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
			<table width=100% border="0" class="tablebottom">
				<s:form name="viewform" theme="simple">
					<div class="headingsmallbgnew">
						<s:text name="scrhCriteria"></s:text>
						<span class="mandatory"><s:property
								value="%{searchCreteria}" /> </span> /
						<s:text name="totProp"></s:text>
						<span class="mandatory"><s:property
								value="%{searchResultList.size}" /> <s:text
								name="matchRecFound" /> </span>
						<div class="searchvalue1">
							<s:text name="scrhVal"></s:text>
							<s:property value="%{searchValue}" />
						</div>
					</div>
					<s:if
						test="%{searchResultList != null && searchResultList.size >0}">
						<tr>
							<display:table name="searchResultList" id="linksTables"
								pagesize="10" export="true" requestURI="" class="tablebottom"
								style="width:100%" uid="currentRowObject">								
								<display:column
									title="Index Number/ &#2311;&#2306;&#2337;&#2375;&#2325;&#2381;&#2360; &#2325;&#2381;&#2352;&#2350;&#2366;&#2306;&#2325;"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center">
									<a href="../view/viewProperty!viewForm.action?propertyId=${currentRowObject.indexNum}" >
										${currentRowObject.indexNum} </a>
								</display:column>
								<display:column property="parcelId"
									title="Parcel Id/ &#2346;&#2366;&#2352;&#2381;&#2360;&#2354; &#2310;&#2351;.&#2337;&#2368;"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column property="ownerName"
									title="Owner Name/ &#2328;&#2352;&#2350;&#2366;&#2354;&#2325;&#2366;&#2330;&#2375; &#2344;&#2366;&#2306;&#2357;"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:left" />
								<display:column property="address"
									title="Address/ &#2346;&#2340;&#2381;&#2340;&#2366;"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:left" />
								<display:column property="currDemand"
									title="Current Tax/ &#2330;&#2366;&#2354;&#2370; &#2325;&#2352;"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column property="currDemandDue"
									title="Current Tax Due/ &#2330;&#2366;&#2354;&#2370; &#2342;&#2375;&#2351; &#2325;&#2352;"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="width:10%;text-align:center" />
								<display:column property="arrDemandDue"
									title="Arrear Tax Due/ &#2341;&#2325;&#2368;&#2340; &#2342;&#2375;&#2351; &#2325;&#2352;"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" />
								<display:column title="Action" headerClass="bluebgheadtd"
									media="html" class="blueborderfortd" style="text-align:center">
									<select id="actionValue" name="actionValue"
										style="align: center"
										onchange="getPropdetails(this,'<s:property value="%{#attr.currentRowObject.indexNum}"/>')">
										<option value="">
											<br>
											----Choose----
										</option>
										<option value="ViewProperty">
											<s:text name="viewProp"></s:text>
										</option>
										<s:if test="isDemandActive == true">
											<c:if test="${fn:contains(roleName,'OPERATOR') && markedForDeactive == 'N'}">
												<option value="TransferProperty">
													<s:text name="transferProperty"></s:text>
												</option>
											</c:if>
											<c:if test="${fn:contains(roleName,'ASSISTANT')}">
												<c:if test="${markedForDeactive == 'N'}">	
												<s:property value="%{markedForDeactive}" />
													<option value="ChangeAddress">
														<s:text name="chPropAdd"></s:text>
													</option>
													<option value="DeactivateProperty">
														<s:text name="deactivate.prop"></s:text>
													</option>
													<option value="Amalgamation">
														<s:text name="Amalgamation"></s:text>
													</option>
													<option value="Bifurcation">
														<s:text name="Bifurcation"></s:text>
													</option>
													<option value="Modification">
														<s:text name="Modification"></s:text>
													</option>
													<option value="Objection">
														<s:text name="Objection"></s:text>
													</option>
													<option value="Notice 125">
														<s:text name="Notice125"></s:text>
													</option>
													<option value="Recovery">
														<s:text name="recovery"></s:text>
													</option>
													<option value="Assessment Data update">
														<s:text name="assessmentDataUpdate"></s:text>
													</option>
													<option value="Edit Property Data">
														<s:text name="edit.propertyData"></s:text>
													</option>
												</c:if>
											</c:if>
											<c:if test="${fn:contains(roleName,'PTADMINISTRATOR')}">
												<c:if test="${markedForDeactive == 'N'}">												
													<option value="Edit Demand">
														<s:text name="editDemand" />
													</option>
												</c:if>
											</c:if>
										</s:if>
										<s:else>
											<c:if test="${fn:contains(roleName,'ASSISTANT') && markedForDeactive == 'N'}">
												<option value="Objection">
													<s:text name="Objection"></s:text>
												</option>
											</c:if>
										</s:else>
									</select>
								</display:column>
								<display:setProperty name="paging.banner.item" value="Record" />
								<display:setProperty name="paging.banner.items_name"
									value="Records" />
								<display:setProperty name="export.pdf" value="false" />
								<display:setProperty name="export.rtf" value="false" />
								<display:setProperty name="export.xml" value="true" />
								<display:setProperty name="export.csv" value="true" />
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
								<input type="button" value="Close" class="button"
									onClick="window.close()" />
								<input type="submit" value="Search Again" class="button"
									onClick="gotoSearchForm();" />
								
							</div>
						</td>
					</tr>
				</s:form>
			</table>
		</div>
	</body>
</html>
