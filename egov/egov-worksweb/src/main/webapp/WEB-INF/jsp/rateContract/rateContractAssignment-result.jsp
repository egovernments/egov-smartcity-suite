
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr height="5">
			<td></td>
		</tr>
		<tr>
			<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="9" class="headingwk" align="left">
							<div class="arrowiconwk">
								<img src="${pageContext.request.contextPath}/image/arrow.gif" />
							</div>

							<div class="headerplacer">
								<s:text name='search.result' />
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
		<s:text id="slNo" name="%{getText('label.slno')}"></s:text>
		<display:table name="searchResult" pagesize="30" uid="currentRow"
			cellpadding="0" cellspacing="0" requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

			<display:column title=" Sl No" style="text-align:center;"
				headerClass="pagetableth" class="pagetabletd">
				<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Contractor Name / Code" style="text-align:center;"
				property="contractorName">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="No.of WO issued - Regular Work" style="text-align:center;"
				property="noOfWOIssuedRegular">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="No.of.Estimate - Regular Work" style="text-align:center;"
				property="noOfEstimateRegular">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Total Work value(Lakhs) - Regular Work" style="text-align:center;"
				property="totalWorkValueRegular">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Bills realized so far(Lakhs) - Regular Work" style="text-align:center;"
				property="billsRealizedSofarRegular">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="No.of.Work completed - Regular Work" style="text-align:center;"
				property="noOfWorkcompletedRegular">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="No.of WO issued - Rate Contract Work" style="text-align:center;"
				property="noOfWOIssuedRate">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Total Work value(Lakhs) - Rate Contract Work" style="text-align:center;"
				property="totalWorkValueRate">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="Bills realized so far(Lakhs) - Rate Contract Work" style="text-align:center;"
				property="billsRealizedSoFarRate">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="No.of.Works in progress - Rate Contract Work" style="text-align:center;"
				property="noOfWorksInProgressRate">
			</display:column>
			<display:column headerClass="pagetableth" class="pagetabletd"
				title="No.of.Work completed - Rate Contract Work" style="text-align:center;"
				property="noOfWorkCompletedRate">
			</display:column>


		</display:table>
		<tr>
			<td colspan="4">
				<div class="buttonholderwk" align="center">
					<input type="button" class="buttonfinal" value="CLOSE"
						id="closeButton" name="button" onclick="window.close();" />
				</div>
			</td>
		</tr>
	</s:if>
	<s:elseif test="%{searchResult.fullListSize == 0 && !hasErrors()}">
		<div>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center"><font color="red"><s:text
								name="search.result.no.record" /></font></td>
				</tr>
			</table>
		</div>
	</s:elseif>
</div>