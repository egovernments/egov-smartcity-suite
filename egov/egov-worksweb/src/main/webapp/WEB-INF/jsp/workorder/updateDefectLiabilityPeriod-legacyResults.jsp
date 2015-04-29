<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td></td>
				</tr>
				<s:if test="%{searchResultList.size() != 0}">
					<tr>
						<td>
							<table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td class="headingwk" align="left">
										<div class="arrowiconwk">
											<img
												src="${pageContext.request.contextPath}/image/arrow.gif" />
										</div>
										<div class="headerplacer">
											<s:text name='search.result' />
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					
					<tr>
						<td>
							<display:table name="searchResultList" uid="currentRow"
								cellpadding="0" cellspacing="0" requestURI=""
								style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
						
								<display:caption style='font-weight:bold'>
									<left>
										<span class="mandatory">
										<s:text name="projectCompletionReport.projectCode" /> : </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:if test="%{pcCode != ''}">
											<s:property value="pcCode"/>
										</s:if>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<span class="mandatory"><s:text name="projectCompletionReport.estimate.number" /> : </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:if test="%{estimateNo != ''}">
											<a href="Javascript:viewEstimate('<s:property  value='estimateId' />')">
												<s:property value="estimateNo"/>
											</a> 
										</s:if>
										<s:else>
											<s:text name="dlp.search.result.not.applicable" />
										</s:else>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<span class="mandatory"><s:text name="workorder.search.workordernumber" /> : </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<s:text name="dlp.search.result.not.applicable" />
										<s:if test="%{mode=='edit'}">
												<br />
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												
												<span class="mandatory"><s:text name="defect.liability.period" /> : </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
												<s:property value="%{model.defectLiabilityPeriod}" />
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<span class="mandatory"><s:text name="contractorBill.completionDate"/> : </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<s:date name="model.workCompletionDate" format="dd/MM/yyyy" />
										</s:if>
									</left>
								</display:caption>
								
								<s:if test="%{finalBillPresent!='yes' && mode!='edit'}">
									<display:column headerClass="pagetableth" class="pagetabletd" title="Select" titleKey="column.title.select" style="width:1%;text-align:center">
										<input name="radio" type="radio" id="radio"
											value="<s:property value='%{#attr.currentRow.idBill}'/>"
											 onClick="setValues(this);"/>
										<s:hidden name="billRegisterId" id="billRegisterId" value="%{#attr.currentRow.idBill}" />
										<input type='hidden' name="billDate" id="billDate" value='<s:date name="#attr.currentRow.billDate" format="dd/MM/yyyy" />' />
										<input type='hidden' name="multipleProjCodesPresent" id="multipleProjCodesPresent" value='<s:property value="#attr.currentRow.areMultipleProjCodesAttached"/>' />
									</display:column>
								</s:if>
										
								<display:column title="Sl.No"
									titleKey='estimate.search.slno' headerClass="pagetableth"
									class="pagetabletd" style="width:2%;text-align:right">
									<s:property value="#attr.currentRow_rowNum"/>
								</display:column>
								<s:if test="%{mode=='edit'}">
									<display:column title="Bill Number"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:5%;text-align:left">
										<s:property value="#attr.currentRow.billNumber"/>
									</display:column>
									<display:column title="Bill Date"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:2%;text-align:left">
										<s:date name="#attr.currentRow.billDate" format="dd/MM/yyyy" />
									</display:column>
									<display:column title="Bill Type"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:5%;text-align:left">
										<s:property value="#attr.currentRow.billType"/>
									</display:column>
									<display:column title="Amount(Rs)"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:5%;text-align:right">
										<s:text name="contractor.format.number">
											<s:param name="value" value="%{#attr.currentRow.billAmount}" />
										</s:text>
									</display:column>
									<display:column title="Voucher Number/Date"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:10%;text-align:left">
										<s:if test="%{#attr.currentRow.voucherNumber!=null && #attr.currentRow.voucherDate!=null}">
											<a href="Javascript:viewVoucher('<s:property  value="#attr.currentRow.voucherId" />')">
											<s:property value="#attr.currentRow.voucherNumber"/> </a>
											 /  <s:date name="#attr.currentRow.voucherDate" format="dd/MM/yyyy" />
										</s:if>
									</display:column>
								</s:if>
								<s:else>
									<display:column title="Voucher Number/Date"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:10%;text-align:left">
										<s:if test="%{#attr.currentRow.voucherNumber!=null && #attr.currentRow.voucherDate!=null}">
											<a href="Javascript:viewVoucher('<s:property  value="#attr.currentRow.voucherId" />')">
											<s:property value="#attr.currentRow.voucherNumber"/> </a>
											 /  <s:date name="#attr.currentRow.voucherDate" format="dd/MM/yyyy" />
										</s:if>
									</display:column>
									<display:column title="Amount(Rs)"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:5%;text-align:right">
										<s:text name="contractor.format.number">
											<s:param name="value" value="%{#attr.currentRow.billAmount}" />
										</s:text>
									</display:column>
									<display:column title="Bill Number"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:5%;text-align:left">
										<s:property value="#attr.currentRow.billNumber"/>
									</display:column>
									<display:column title="Bill Date"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:2%;text-align:left">
										<s:date name="#attr.currentRow.billDate" format="dd/MM/yyyy" />
									</display:column>
									<display:column title="Bill Type"
										titleKey='estimate.search.slno' headerClass="pagetableth"
										class="pagetabletd" style="width:2%;text-align:left">
										<s:property value="#attr.currentRow.billType"/>
									</display:column>
								</s:else>
								
							</display:table>
						
						</td>
					</tr>
					
				</s:if>
				<s:elseif test="%{searchResultList.size() == 0}">
						<div>
							<table width="100%" border="0" cellpadding="0"
								cellspacing="0">
								<tr>
									<td align="center">
										<font color="red"><s:text name="label.no.records.found" /></font>
									</td>
								</tr>
							</table>
						</div>
				</s:elseif>
			</table>