<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<script type="text/javascript">
	function generateWardReport(bndryId) {
		document.forms[0].action = "activeDemandReport!search.action?reportType=Ward&boundaryId=" + bndryId;
		document.forms[0].submit();
	}
	function generatePartNoReport(bndryId) {
		document.forms[0].action = "activeDemandReport!search.action?reportType=PartNo&boundaryId=" + bndryId;
		document.forms[0].submit();
	}
</script>
<logic:empty name="resultList">
	<div class="headingsmallbgnew" style="text-align: center; width: 100%;">
		<s:text name="searchresult.norecord" />
	</div>
</logic:empty>
<logic:notEmpty name="resultList">
	<display:table name="resultList" uid="currentRowObject"
		class="tablebottom" style="width:99%;" cellpadding="0"
		cellspacing="0" export="true" requestURI="">
		<display:caption>
			<div class="headingsmallbgnew" align="center"
				style="text-align: center; width: 98%;">
				<span class="searchvalue1"><s:text name="activeDemandReport.title"/></span><br>
				<s:text name="txtReport" /> : <span class="mandatory"><s:property value="%{reportType}" /></span>, 
				<s:text name="PropertyType" /> : <span class="mandatory"><s:property value="%{selectedPropertyTypes}" /> </span>
			</div>
		</display:caption>
		<s:if test="%{reportType == 'Ward'}">
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Ward" style="text-align:center;width:10%;">
				<c:if test="${currentRowObject.boundaryName != 'Total' }">
					<a href="javascript:generatePartNoReport(<c:out value="${currentRowObject.boundaryId}"/>);"><c:out value="${currentRowObject.boundaryName}"/></a>
				</c:if>
				<c:if test="${currentRowObject.boundaryName == 'Total'}">
					<b><c:out value="${currentRowObject.boundaryName}"/><b>
				</c:if>
			</display:column>
		</s:if>
		<s:elseif test="%{reportType == 'PartNo'}">
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Part No" style="text-align:center;width:10%;">
				<c:if test="${currentRowObject.partNo != 'Total' }">
					<c:out value="${currentRowObject.partNo}"/>
				</c:if>
				<c:if test="${currentRowObject.partNo == 'Total'}">
					<b><c:out value="${currentRowObject.partNo}"/></b>
				</c:if>
			</display:column>
		</s:elseif>
		<s:else>
			<display:column headerClass="bluebgheadtd" class="blueborderfortd"
				title="Zone" style="text-align:center;width:10%;">
				<c:if test="${currentRowObject.boundaryName != 'Total' }">
					<a href="javascript:generateWardReport(<c:out value="${currentRowObject.boundaryId}"/>);"><c:out value="${currentRowObject.boundaryName}"/></a>
				</c:if>
				<c:if test="${currentRowObject.boundaryName == 'Total'}">
					<b><c:out value="${currentRowObject.boundaryName}"/></b>
				</c:if>
			</display:column>
		</s:else>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Property Count" style="text-align:center;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><c:out value="${currentRowObject.count}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<c:out value="${currentRowObject.count}"/>
			</c:if>
		</display:column>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Arrears Demand" style="text-align:right;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.arrDmd}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.arrDmd}"/>
			</c:if>
		</display:column>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Current Demand" style="text-align:right;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.currDmd}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.currDmd}"/>
			</c:if>
		</display:column>
		<display:column headerClass="bluebgheadtd" class="blueborderfortd"
			title="Total Demand" style="text-align:right;width:10%;">
			<c:if test="${currentRowObject.boundaryName == 'Total' || currentRowObject.partNo == 'Total'}">
				<b><fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.totDmd}"/></b>
			</c:if>
			<c:if test="${currentRowObject.boundaryName != 'Total' && currentRowObject.partNo != 'Total'}">
				<fmt:formatNumber pattern="#,##0.00" value="${currentRowObject.totDmd}"/>
			</c:if>
		</display:column>

		<display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.excel" value="true" />
		<display:setProperty name="export.xml" value="false" />
		<display:setProperty name="export.pdf" value="true" />
	</display:table>
</logic:notEmpty>