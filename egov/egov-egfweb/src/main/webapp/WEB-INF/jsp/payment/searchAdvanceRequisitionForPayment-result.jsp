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
						<td colspan="6" align="left"><div class="subheadsmallnew">
								<s:text name='search.result' />
							</div></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
		<s:text id="slNo" name="%{getText('label.slno')}"></s:text>
		<s:text id="select" name="%{getText('label.select')}"></s:text>
		<s:text id="arfnumber" name="%{getText('arf.arfnumber')}"></s:text>
		<s:text id="arfdate" name="%{getText('arf.arfdate')}"></s:text>
		<s:text id="advanceamount" name="%{getText('arf.advanceamount')}"></s:text>
		<s:text id="department" name="%{getText('arf.department')}"></s:text>

		<display:table name="searchResult" pagesize="30" uid="currentRow"
			cellpadding="0" cellspacing="0" requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
			<display:column headerClass="bluebgheadtdnew"
				class="blueborderfortdnew" title="${select}" style="width:5%;">
				<input name="radio" type="radio" id="radio"
					value="<s:property value='%{#attr.currentRow.id}'/>" />
				<s:hidden id="arfId" name="arfId" value="%{#attr.currentRow.id}" />
			</display:column>
			<display:column headerClass="bluebgheadtdnew"
				class="blueborderfortdnew" title="${slNo}"
				style="width:5%;text-align:left">
				<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize" />
			</display:column>
			<display:column headerClass="bluebgheadtdnew"
				class="blueborderfortdnew" title="${arfnumber}"
				style="width:20%;text-align:left">
				<a href="#"
					onclick="viewARF('<s:property value='%{#attr.currentRow.egAdvanceReqMises.sourcePath}'/>')">
					<s:property value="#attr.currentRow.advanceRequisitionNumber" />
				</a>
			</display:column>
			<display:column headerClass="bluebgheadtdnew"
				class="blueborderfortdnew" title="${arfdate}"
				style="width:10%;text-align:left">
				<s:date name="#attr.currentRow.advanceRequisitionDate"
					format="dd/MM/yyyy" />
			</display:column>
			<display:column headerClass="bluebgheadtdnew"
				class="blueborderfortdnew" title="${advanceamount}"
				style="width:10%;text-align:right">
				<s:text name="payment.format.number">
					<s:param name="advanceRequisitionAmount"
						value='%{#attr.currentRow.advanceRequisitionAmount}' />
				</s:text>
			</display:column>
			<display:column headerClass="bluebgheadtdnew"
				class="blueborderfortdnew" title="${department}"
				style="width:20%;text-align:left">
				<s:property
					value="#attr.currentRow.egAdvanceReqMises.egDepartment.deptName" />
			</display:column>
		</display:table>
		<P align="center">
			<input type="button" style="width: 200px" class="buttongeneral"
				value="Generate Advance Payment" id="addButton"
				name="createAdvancePaymentButton"
				onclick="goToCreateAdvancePayment();" align="center" />
		</P>
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
<script>

	function viewARF(sourcePath){
		window.open(sourcePath,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	} 

	function goToCreateAdvancePayment(){
		var table=document.getElementById('currentRow');
		var lastRow = table.rows.length-1;
		var arfId="";
		
		if(lastRow==1){
			if(document.getElementById("radio").checked)
				arfId=document.getElementById("arfId").value;		
		}
		else{
			for(i=0;i<lastRow;i++){			
				if(document.forms[0].radio[i].checked){
					arfId=document.forms[0].arfId[i].value;				
				}
			}
		}

		if(arfId != ''){ 
			window.open('${pageContext.request.contextPath}/payment/advancePayment!newform.action?advanceRequisitionId='+arfId,'_self');
		}
		else{
			dom.get("searchAdvanceRequisition_error").style.display='';
			document.getElementById("searchAdvanceRequisition_error").innerHTML='<s:text name="advance.payment.create.select.arf.validate" />';
			window.scroll(0, 0);
			return false;
		  }
		  dom.get("searchAdvanceRequisition_error").style.display='none';
		  document.getElementById("searchAdvanceRequisition_error").innerHTML=''; 	  
	}
			     
</script>
