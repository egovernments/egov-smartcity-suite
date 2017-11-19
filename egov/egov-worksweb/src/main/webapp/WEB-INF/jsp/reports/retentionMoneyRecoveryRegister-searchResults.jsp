<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td colspan="9" class="headingwk" align="left">
						<div class="arrowiconwk">
							<img src="/egworks/resources/erp2/images/arrow.gif"/>
						</div>
						<div class="headerplacer">
							<s:text name='label.search.result' />
						</div>
					</td>
				</tr>
				
				</table>
			</td>
		</tr>
</table>

<div>
<s:if test="%{searchResult.fullListSize != 0}">

		<s:text id="SlNo" name="%{getText('label.slno')}"></s:text>		
		<s:text id="billdepartment" name="%{getText('retentionMoneyRecoveryRegister.label.billdepartment')}"></s:text>
		<s:text id="contractorcode" name="%{getText('retentionMoneyRecoveryRegister.label.contractorcode')}"></s:text>
		<s:text id="contractorname" name="%{getText('retentionMoneyRecoveryRegister.label.contractorname')}"></s:text>
		<s:text id="projectcode" name="%{getText('retentionMoneyRecoveryRegister.label.projectcode')}"></s:text>
		<s:text id="projectname" name="%{getText('retentionMoneyRecoveryRegister.label.projectname')}"></s:text>
		<s:text id="billnumber" name="%{getText('retentionMoneyRecoveryRegister.label.billnumber')}"></s:text>
		<s:text id="billtype" name="%{getText('retentionMoneyRecoveryRegister.label.billtype')}"></s:text>
		<s:text id="billdate" name="%{getText('retentionMoneyRecoveryRegister.label.billdate')}"></s:text>
		<s:text id="vouchernumber" name="%{getText('retentionMoneyRecoveryRegister.label.vouchernumber')}"></s:text>
		<s:text id="billamount" name="%{getText('retentionMoneyRecoveryRegister.label.billamount')}"></s:text>
		<s:text id="retentionmoneyrecovered" name="%{getText('retentionMoneyRecoveryRegister.label.retentionmoneyrecovered')}"></s:text>
		<s:text id="dateOfRefund" name="%{getText('retentionMoneyRecoveryRegister.label.refundDate')}"></s:text>
		
<display:table name="searchResult" pagesize="30" uid="currentRow"
	cellpadding="0" cellspacing="0" requestURI=""
	style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

	<display:caption style='font-weight:bold'>
			<s:property value="%{reportSubTitle}"/>
	</display:caption>
	<display:column title="${SlNo}" headerClass="pagetableth" class="pagetabletd" style="width:3%;text-align:right" >
		<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
	</display:column> 

	<display:column headerClass="pagetableth" class="pagetabletd" title="${billdepartment}" style="width:10%;text-align:left" property="billDepartment" />
	<display:column headerClass="pagetableth" class="pagetabletd" title="${contractorcode}" style="width:8%;text-align:left" property='contractorCode' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${contractorname}" style="width:8%;text-align:left" property='contractorName' />
		
	<display:column headerClass="pagetableth" class="pagetabletd" title="${projectcode}" style="width:12%;text-align:left" property='projectCode' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="${projectname}" style="width:24%;text-align:left" property='projectName' />

	<display:column headerClass="pagetableth" class="pagetabletd" title="${billnumber}" style="width:10%;text-align:left" property='billNumber' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${billtype}" style="width:9%;text-align:left" property='billType' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${billdate}" style="width:9%;text-align:left" property='billDate' />
	
	<display:column headerClass="pagetableth" class='pagetabletd' title="${vouchernumber}" style="width:12%;text-align:left" property='voucherNumber' />
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${billamount}"	style="width:8%;text-align:right" >
		<s:text name="contractor.format.number">
			<s:param name="value" value="%{#attr.currentRow.billAmount}" />
		</s:text>
	</display:column>
	<display:column headerClass="pagetableth" class="pagetabletd" title="${retentionmoneyrecovered}" style="width:8%;text-align:right" > 
		<s:text name="contractor.format.number">
			<s:param name="value" value="%{#attr.currentRow.retentionMoneyRecoveredAmount}" />
		</s:text>
	</display:column>
	
	<display:column headerClass="pagetableth" class="pagetabletd" title="${dateOfRefund}" style="width:9%;text-align:left" >
			<s:property value="#attr.currentRow.refundDate" />
	</display:column>
	
	</display:table>
	<div class="buttonholdersearch" align = "center">
   			<s:submit value="Export to PDF" cssClass="buttonpdf" id="pdfButton" method="exportToPdf" name="button" onClick="disableMasking();" />
   			<s:submit value="Export to Excel" cssClass="buttonpdf" id="excelButton" method="exportToExcel" name="button" onClick="disableMasking();" />
   	</div>
		
	</s:if>
	<s:elseif test="%{searchResult.fullListSize == 0}">
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
</div>		
