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

<script type="text/javascript">
</script>

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
								<s:text name='page.title.search.bill' />
							</div>
					</td>
				</tr>
				</table>
			</td>
		</tr>
</table>	


<div id="header-container">
	<s:if test="%{pagedResults.fullListSize != 0}">
	<display:table name="pagedResults" pagesize="30" uid="currentRowObject" cellpadding="0" cellspacing="0" requestURI="" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
		<s:if test="%{sourcePage=='cancelBill'}">
				<s:text id="select" name="%{getText('column.title.select')}"></s:text>	
		</s:if>
	 			<s:text id="SlNo" name="%{getText('column.title.SLNo')}"></s:text>
				<s:text id="billNo" name="%{getText('contractorBill.search.billNo')}"></s:text>
				<s:text id="billType" name="%{getText('contractorBill.search.billType')}"></s:text>
				<s:text id="billDate" name="%{getText('contractorBill.search.billDate')}"></s:text>
				<s:text id="workorderCode" name="%{getText('contractorBill.search.workorderCode')}"></s:text>
				<s:text id="contractor" name="%{getText('contractorBill.search.contractor')}"></s:text>
				<s:text id="totalbillValue" name="%{getText('contractorBill.search.totalbillValue')}"></s:text>
				<s:text id="billcreatedBy" name="%{getText('contractorBill.search.billcreatedBy')}"></s:text>
				<s:text id="statusLabel" name="%{getText('estimate.search.status')}"></s:text>
				<s:text id="actions" name="%{getText('contractorBill.search.actions')}"></s:text>

				
				
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="billId" name="billId" value="%{#attr.currentRowObject.id}" />
					</display:column>
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="billStateId" name="billStateId" value="%{#attr.currentRowObject.state.id}" />
					</display:column>
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="docNo" name="docNo" value="%{#attr.currentRowObject.documentNumber}" />
					</display:column>
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="woId" name="woId" value="%{#attr.currentRowObject.workOrderId}" />
					</display:column>
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="billNo" name="billNo" value="%{#attr.currentRowObject.billnumber}" />
					</display:column>
					<display:column class="hidden" headerClass="hidden"  media="html">
	 					<s:hidden id="conBillType" name="conBillType" value="%{#attr.currentRowObject.billtype}" />
					</display:column>
			
				<s:if test="%{sourcePage=='cancelBill'}">
					 <display:column  headerClass="pagetableth" class="pagetabletd" title="Select" style="width:3%;text-align:center">
						<input name="radio" type="radio" id="radio"  value="<s:property value='%{#attr.currentRowObject.id}'/>" onClick="setBillId(this);" />
						<s:if test="%{#attr.currentRowObject.egBillregistermis.voucherHeader != null && #attr.currentRowObject.egBillregistermis.voucherHeader.status != 4}">
					 			<s:hidden name="voucherNo" id="voucherNo" value="%{#attr.currentRowObject.egBillregistermis.voucherHeader.voucherNumber}" />
					 	</s:if>				 	
					 	<s:else>
						 	<s:hidden name="voucherNo" id="voucherNo" value="" />
						</s:else>	
			  	        </display:column>
			  	   </s:if>   
					
				<display:column headerClass="pagetableth" class="pagetabletd"   title="${SlNo}" style="width:3%;text-align:center">
				<s:property value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}"/></display:column>	
 						 	 
  			 	<display:column  headerClass="pagetableth" class="pagetabletd"   title="${billNo}"  style="width:15%text-align:center;">
  			 	<s:property value="%{#attr.currentRowObject.billnumber}" /> </display:column>
  			 	
  			 	<display:column   headerClass="pagetableth" class="pagetabletd"  title="${billType}"  style="width:15%text-align:center;">
  			 	<s:property value="%{#attr.currentRowObject.billtype}" /> </display:column>
  			 	
  			 	<display:column headerClass="pagetableth" class="pagetabletd"    title="${billDate}"  style="width:15%text-align:center;">
  			 	<s:date name="%{#attr.currentRowObject.billdate}"  format="dd/MM/yyyy"/> </display:column>
  			 	
  			 	<display:column headerClass="pagetableth" class="pagetabletd"    title="${workorderCode}"  style="width:15%text-align:center;">
  			 	<s:property value="%{#attr.currentRowObject.workordernumber}" /> </display:column>
  			 	
  			 	<display:column headerClass="pagetableth" class="pagetabletd"    title="${contractor}"  style="width:15%text-align:center;">
  			 	<s:property value="%{#attr.currentRowObject.egBillregistermis.payto}" /> </display:column>
  			 	
  			 	<display:column   headerClass="pagetableth" class="pagetabletd" title="${totalbillValue}"  style="width:15%text-align:right;">
  			 	<s:text name="contractor.format.number" >
							<s:param name="rate" value="%{#attr.currentRowObject.billamount}"/>
				</s:text>
  			   </display:column>
  			   
  			    <s:if test="%{status!='APPROVED' && status!='CANCELLED'}">
	  			   	<display:column headerClass="pagetableth" class="pagetabletd"    title="${billcreatedBy}"  style="width:15%text-align:center;">
	  			 	<s:property value="%{#attr.currentRowObject.owner}" /> </display:column>
  			 	</s:if>
  			 	
  			 	<display:column headerClass="pagetableth" class="pagetabletd" title="Status"  style="width:15%text-align:right;">
  			 	<s:property value="%{#attr.currentRowObject.billstatus}" />
  			   </display:column>
  			 	
  		
			
			<display:column  headerClass="pagetableth" class="pagetabletd"   title="${actions}"  style="width:4%text-align:center;">
          			 	   		<s:select theme="simple"
						                list="#attr.currentRowObject.billActions"
						                name="showBillActions" id="showBillActions"
						                headerValue="%{getText('default.dropdown.select')}"
						                headerKey="-1" onchange="gotoPage(this);">
                          		 </s:select>             	
  			 </display:column>
  			   	
  			<display:column class="hidden" headerClass="hidden"  media="html">
	 				<s:hidden id="id" name="id" value="%{#attr.currentRowObject.id}" />
			</display:column>
			

</display:table>
</s:if>
<s:elseif test="%{results.size == 0}">
			<div >	
				<table width="100%" border="0" cellpadding="0"
					cellspacing="0">
				<tr>
				<td align="center">
				<font color="red">No record Found.</font>
				</td>
				</tr>
				</table>
			</div>
</s:elseif>	
				
