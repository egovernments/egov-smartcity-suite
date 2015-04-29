				 <s:if test="%{searchResult.size()!=0}">
						 
						 <s:text id="spillOverEstimatesCount" name="%{getText('work.progress.abstract.report3.total.estimates.spillover')}"></s:text>
						 <s:text id="currentYearEstimatesCount" name="%{getText('work.progress.abstract.report3.total.estimates.current')}"></s:text>
						 <s:text id="tenderDocNotReleasedSpillOverLabel" name="%{getText('work.progress.abstract.report3.tender.docNotReleased.spillover')}"></s:text>
						 <s:text id="tenderDocNotReleasedCurrYearLabel" name="%{getText('work.progress.abstract.report3.tender.docNotReleased.current')}"></s:text>
						 <s:text id="tenderNotFinalizedSpillOverLabel" name="%{getText('work.progress.abstract.report3.tender.notFinalized.spillover')}"></s:text>
						 <s:text id="tenderNotFinalizedCurrYearLabel" name="%{getText('work.progress.abstract.report3.tender.notFinalized.current')}"></s:text>
						 
						 <s:text id="woNotIssuedSpillOverLabel" name="%{getText('work.progress.abstract.report3.WO.notIssued.spillover')}"></s:text>
						 <s:text id="woNotIssuedCurrYearLabel" name="%{getText('work.progress.abstract.report3.WO.notIssued.current')}"></s:text>
						 <s:text id="pymtNotReleasedSpillOverLabel" name="%{getText('work.progress.abstract.report3.pymtNotReleased.spillover')}"></s:text>
						 
						 <s:text id="pymtNotReleasedCurrYearLabel" name="%{getText('work.progress.abstract.report3.pymtNotReleased.current')}"></s:text>
						 <s:text id="worksNotCompletedSpilloverLabel" name="%{getText('work.progress.abstract.report3.worksNotCompleted.spillover')}"></s:text>						 
						 <s:text id="worksNotCompletedCurrYearLabel" name="%{getText('work.progress.abstract.report3.worksNotCompleted.current')}"></s:text>
						 <s:text id="worksCompletedSpilloverLabel" name="%{getText('work.progress.abstract.report3.worksCompleted.spillover')}"></s:text>
						 <s:text id="worksCompletedCurrYearLabel" name="%{getText('work.progress.abstract.report3.worksCompleted.current')}"></s:text>
						 
						 <s:text id="estimateValueSpilloverLabel" name="%{getText('work.progress.abstract.report3.estimateValue.spillover')}"></s:text>
						 <s:text id="estimateValueCurrYearLabel" name="%{getText('work.progress.abstract.report3.estimateValue.current')}"></s:text>
						 
						 <s:text id="tenderFinalizedValueSpilloverLabel" name="%{getText('work.progress.abstract.report3.tenderFinalizedValue.spillover')}"></s:text>
						 <s:text id="tenderFinalizedValueCurrYearLabel" name="%{getText('work.progress.abstract.report3.tenderFinalizedValue.current')}"></s:text>
						 <s:text id="paymentReleasedSpilloverLabel" name="%{getText('work.progress.abstract.report3.paymentReleased.spillover')}"></s:text>
						 <s:text id="paymentReleasedCurrYearLabel" name="%{getText('work.progress.abstract.report3.paymentReleased.current')}"></s:text>
						 <s:text id="savingsSpilloverLabel1" name="%{getText('work.progress.abstract.report3.savings.spillover.msg1')}"></s:text>
						 <s:text id="savingsSpilloverLabel2" name="%{getText('work.progress.abstract.report3.savings.spillover.msg2')}"></s:text>
						 <s:text id="savingsSpilloverLabel3" name="%{getText('work.progress.abstract.report3.savings.spillover.msg3')}"></s:text>
						 <s:text id="savingsCurrYearLabel1" name="%{getText('work.progress.abstract.report3.savings.current.msg1')}"></s:text>
						 <s:text id="savingsCurrYearLabel2" name="%{getText('work.progress.abstract.report3.savings.current.msg2')}"></s:text>				 
						 <s:text id="savingsCurrYearLabel3" name="%{getText('work.progress.abstract.report3.savings.current.msg3')}"></s:text>
						 
						 <div style='font-weight:bold;text-align:center' >
							<s:property value="%{subHeader}" /><br><s:property value="%{reportMessage}" />
	    				</div>
	    				
						 <div> 
	   				        <display:table name="searchResult" uid="currentRowObject" cellpadding="0" cellspacing="0" 
	  				        	export="false" id="currentRow" class="table-header-fix" requestURI="">
             						
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:left" title="Department"  property="department" />
       							
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${spillOverEstimatesCount}"
              							style="text-align:right;">
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.adminSancEstsPreparedSpillOver!=0}"> 
              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','spillOverEstimates')">
											<s:property  value='%{#attr.currentRow.adminSancEstsPreparedSpillOver}' />
										</a>     
									</s:if>
									<s:else>
										<s:property  value='%{#attr.currentRow.adminSancEstsPreparedSpillOver}' />
									</s:else>	               							
									 
								</display:column>
								<display:column  headerClass="pagetableth" class="pagetabletd" title="${currentYearEstimatesCount}"
              							style="text-align:right;">
              						<s:if test="%{#attr.currentRow.department!='Total' && #attr.currentRow.adminSancEstsPreparedCurr!=0}"> 
              							<a href="Javascript:viewEstimatesDrillDown('2','${pageContext.request.contextPath}','<s:property  value='%{#attr.currentRow.department}' />','currentYearEstimates')">
											<s:property  value='%{#attr.currentRow.adminSancEstsPreparedCurr}' />
										</a>     
									</s:if>
									<s:else>
										<s:property  value='%{#attr.currentRow.adminSancEstsPreparedCurr}' />
									</s:else>	               							
									 
								</display:column>
       							
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${tenderDocNotReleasedSpillOverLabel}"  property="tenderDocNotReleasedSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${tenderDocNotReleasedCurrYearLabel}"  property="tenderDocNotReleasedCurr" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${tenderNotFinalizedSpillOverLabel}"  property="tenderNotFinalizedSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${tenderNotFinalizedCurrYearLabel}"  property="tenderNotFinalizedCurr" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${woNotIssuedSpillOverLabel}"  property="woNotIssuedSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${woNotIssuedCurrYearLabel}"  property="woNotIssuedCurr" />
       							
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${pymtNotReleasedSpillOverLabel}"  property="paymentNotReleasedSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${pymtNotReleasedCurrYearLabel}"  property="paymentNotReleasedCurr" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${worksNotCompletedSpilloverLabel}"  property="noOfWorksNotCompletedSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${worksNotCompletedCurrYearLabel}"  property="noOfWorksNotCompletedCurr" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${worksCompletedSpilloverLabel}"  property="noOfWorksCompletedSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${worksCompletedCurrYearLabel}"  property="noOfWorksCompletedCurr" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${estimateValueSpilloverLabel}"  property="adminSancEstValueSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${estimateValueCurrYearLabel}"  property="adminSancEstValueCurr" />
       							
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${tenderFinalizedValueSpilloverLabel}" >
       								<s:text name="contractor.format.number">
										<s:param name="value" value="%{#attr.currentRow.tenderFinalizedValueSpillOver}" />
									</s:text>
       							</display:column>
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${tenderFinalizedValueCurrYearLabel}" >
       								<s:text name="contractor.format.number">
										<s:param name="value" value="%{#attr.currentRow.tenderFinalizedValueCurr}" />
									</s:text>
       							</display:column>
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${paymentReleasedSpilloverLabel}"  property="paymentReleasedSpillOver" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${paymentReleasedCurrYearLabel}"  property="paymentReleasedCurr" />
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${savingsSpilloverLabel1}${savingsSpilloverLabel2}-${savingsSpilloverLabel3}" >
				       				<s:text name="contractor.format.number">
										<s:param name="value" value="%{#attr.currentRow.savingsSpillOver}" />
									</s:text>
								</display:column>
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="font-size: 10px;text-align:right" title="${savingsCurrYearLabel1}${savingsCurrYearLabel2}-${savingsCurrYearLabel3}"  >
       								<s:text name="contractor.format.number">
										<s:param name="value" value="%{#attr.currentRow.savingsCurr}" />
									</s:text>
       							</display:column>
       								
       					</display:table>
						
						<br />
						<div class="buttonholderwk" id="divButRow1" name="divButRow1">
							<s:submit cssClass="buttonpdf" value="VIEW PDF" onclick="disableMasking();" id="pdfButton" name="pdfButton" method="generatePDF"/> 
							<s:submit cssClass="buttonpdf" value="VIEW XLS" onclick="disableMasking();" id="pdfButton" name="pdfButton" method="generateXLS"/>
						</div>
						
						</div>
						</s:if>
	 					
						<s:else>
							<s:if test="%{resultStatus=='afterSearch'}">
								<div align="center"><font color="red"><s:text name="no.record.found" /></font></div>
							</s:if>	
						</s:else>
						