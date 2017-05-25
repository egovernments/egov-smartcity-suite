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
		function gotoSearchForm(){
			document.viewform.action='${pageContext.request.contextPath}/citizen/search/search-searchForm.action';
			document.viewform.submit(); 
		}

		function onlinePayTaxForm(propertyId){
			document.viewform.action='${pageContext.request.contextPath}/citizen/collection/collection-searchOwnerDetails.action?assessmentNumber='+propertyId+'&isCitizen=true';
			document.viewform.submit(); 
		}

		function onlineViewDCBForm(propertyId){
			document.viewform.action='${pageContext.request.contextPath}/view/viewDCBProperty-displayPropInfo.action?propertyId='+propertyId+'&isCitizen=true';
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
							value="%{searchResultList.size}" /> <s:text name="matchRecFound" />
					</span>
					<div class="searchvalue1">
						<s:text name="scrhVal"></s:text>
						<s:property value="%{searchValue}" />
					</div>
				</div>
				<s:if test="%{searchResultList != null && searchResultList.size >0}">
					<tr>
						<display:table name="searchResultList" id="linksTables"
							pagesize="10" requestURI="" class="tablebottom"
							style="width:100%" uid="currentRowObject">
							<display:column title="Assessment Number"
								headerClass="bluebgheadtd" media="html" class="blueborderfortd"
								style="text-align:center">
								<a
									href="../../view/viewProperty-viewForm.action?propertyId=${currentRowObject.assessmentNum}&isCitizen=true">
									${currentRowObject.assessmentNum} </a>
							</display:column>
							<%-- <display:column property="parcelId"
									title="Parcel Id"
									headerClass="bluebgheadtd" class="blueborderfortd"
									style="text-align:center" /> --%>
							<display:column property="ownerName" title="Owner Name"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="text-align:left" />
							<display:column property="address" title="Address"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="width:10%;text-align:left" />
							<display:column property="currFirstHalfDemand"
								title="Current First Half Tax" headerClass="bluebgheadtd"
								class="blueborderfortd" style="width:6%;text-align:center" />
							<display:column property="currFirstHalfDemandDue"
								title="Current First Half Tax Due" headerClass="bluebgheadtd"
								class="blueborderfortd" style="width:5%;text-align:center" />
							<display:column property="interestDueOnCurrFirstHalfDemandDue"
								title="Interest Due on Current First Half Tax Due"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="width:6%;text-align:center" />
							<display:column property="currSecondHalfDemand"
								title="Current Second Half Tax" headerClass="bluebgheadtd"
								class="blueborderfortd" style="width:6%;text-align:center" />
							<display:column property="currSecondHalfDemandDue"
								title="Current Second Half Tax Due" headerClass="bluebgheadtd"
								class="blueborderfortd" style="width:6%;text-align:center" />
							<display:column property="interestDueOnCurrSecondHalfDemandDue"
								title="Interest Due on Current Second Half Tax Due"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="width:6%;text-align:center" />
							<display:column property="arrDemandDue" title="Arrear Tax Due"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="width:6%;text-align:center" />
							<display:column property="interestDueOnArrDemandDue"
								title="Interest Due On Arrear Tax Due"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="width:6%;text-align:center" />
							<display:column property="rebateAmt" title="Rebate Amount"
								headerClass="bluebgheadtd" class="blueborderfortd"
								style="width:6%;text-align:center" />
							<display:column property="netPayAmt" media="html"
								title="Net Payable Amount" headerClass="bluebgheadtd"
								class="blueborderfortd" style="width:6%;text-align:center" />
							<display:column title="Actions" headerClass="bluebgheadtd"
								class="blueborderfortd" style="text-align:center">
							<c:if test="${currentRowObject.isTaxExempted == false }">
								<input name="action" type="submit" value="Pay Tax"
									class="buttonsubmit" onclick="onlinePayTaxForm(${currentRowObject.assessmentNum});" />
									</c:if>
								<input name="action1" type="submit" value="View DCB"
									class="buttonsubmit"  onclick="onlineViewDCBForm(${currentRowObject.assessmentNum});"/>
							</display:column>

							<!-- <input type="button" value="Pay Tax"  /> -->
							<display:setProperty name="paging.banner.item" value="Record" />
							<display:setProperty name="paging.banner.items_name"
								value="Records" />
						</display:table>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td align="center"><span class="mandatory"><s:text
									name="noRecFound"></s:text> </span></td>
					</tr>
				</s:else>
				<tr>
					<td>
						<div class="buttonsearch" align="center">
							<input type="button" value="Close" class="button"
								onClick="window.close()" /> <input type="submit"
								value="Search Again" class="button" onClick="gotoSearchForm();" />
						</div>
					</td>
				</tr>
			</s:form>
		</table>
	</div>
</body>
</html>
