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
<tr id="demandinfos">
		<td class="blueborderfortd">
			<div align="center">
			<s:hidden
					value="%{demandDetails[#demandInfoStatus.index].id}"
					name="demandDetails[%{#demandInfoStatus.index}].id" />												
			<s:hidden
					value="%{demandDetails[#demandInfoStatus.index].egDemandReason.id}"
					name="demandDetails[%{#demandInfoStatus.index}].egDemandReason.id" />
			<s:hidden
					value="%{demandDetailBeanList[#demandInfoStatus.index].installment.id}"
					name="demandDetailBeanList[%{#demandInfoStatus.index}].installment.id" />
			<s:hidden name="demandDetailBeanList[%{#demandInfoStatus.index}].isNew" value="%{demandDetailBeanList[#demandInfoStatus.index].isNew}"/>
			<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].installment.id == demandDetailBeanList[#demandInfoStatus.index - 1].installment.id}">
					&nbsp;
			</s:if>
			<s:else>
				<s:property	value="%{demandDetailBeanList[#demandInfoStatus.index].installment}" /> 
			</s:else>												

		</div>
		</td>			
		<td class="blueborderfortd">
			<div align="left">
				<s:property
					value="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster}" />
				<s:hidden
					name="demandDetailBeanList[%{#demandInfoStatus.index}].reasonMaster"
					value="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster}" />
			</div>
	</td>								
		<td class="blueborderfortd" style="padding-right: 10px">
			<div align="right">				
				<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
					<s:textfield
						name="demandDetailBeanList[%{#demandInfoStatus.index}].actualAmount"
						id="revisedTax" size="10" maxlength="10"
						onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Tax');"
						value="%{demandDetailBeanList[#demandInfoStatus.index].actualAmount}"													
						style="text-align: right" class="form-control" />
				</s:if>
				<s:else>									
					<s:property
						value="%{demandDetailBeanList[#demandInfoStatus.index].actualAmount}" default="0"/>
					<s:hidden name="demandDetailBeanList[%{#demandInfoStatus.index}].actualAmount" 
				      value="%{demandDetailBeanList[#demandInfoStatus.index].actualAmount}" />
			    </s:else>
			</div>
		</td>	
		<td class="blueborderfortd">
			<div align="center">
				<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
					N/A
				</s:if>
				<s:elseif test="%{demandDetailBeanList[#demandInfoStatus.index].readOnly==true}">
					<s:textfield name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedAmount"
						id="revisedTax" size="10" maxlength="10"
						onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Tax');"
						value="%{demandDetailBeanList[#demandInfoStatus.index].revisedAmount}" class="form-control"
						style="text-align: right" readonly="true"/>
				</s:elseif>
				<s:elseif test="%{demandDetailBeanList[#demandInfoStatus.index].actualCollection==0}">
					<s:textfield name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedAmount"
						id="revisedTax" size="10" maxlength="10"
						onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Tax');"
						value="%{demandDetailBeanList[#demandInfoStatus.index].revisedAmount}" class="form-control"
						style="text-align: right" />
				</s:elseif>				
				<s:else>
				<s:textfield name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedAmount"
						id="revisedTax" size="10" maxlength="10"
						onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Tax');"
						value="%{demandDetailBeanList[#demandInfoStatus.index].revisedAmount}"
						style="text-align: right" class="form-control" readonly="true" />
				</s:else>
			</div>
		</td>		
		<td class="blueborderfortd" style="padding-right: 10px">
			<div align="right">						
				<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
					<s:textfield
						name="demandDetailBeanList[%{#demandInfoStatus.index}].actualCollection"
						id="revisedCollection" size="10" maxlength="10"
						onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Collection');"
						style="text-align: right" 
						value="%{demandDetailBeanList[#demandInfoStatus.index].actualCollection}"/>
				</s:if>
				<s:else>							
					<s:property
						value="%{demandDetailBeanList[#demandInfoStatus.index].actualCollection}" default="0"/>
					<s:hidden name="demandDetailBeanList[%{#demandInfoStatus.index}].actualCollection" 
				      value="%{demandDetailBeanList[#demandInfoStatus.index].actualCollection}" />
			     </s:else>
			</div>
		</td>									
		<td class="blueborderfortd">
			
		<div align="center">
			<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.constants.PropertyTaxConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
				N/A
			</s:if>
			<s:elseif test="%{demandDetailBeanList[#demandInfoStatus.index].readOnly==true}">
					<s:textfield
					name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedCollection"
					id="revisedCollection" size="10" maxlength="10"
					onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Collection');"
					style="text-align: right" 
					value="%{demandDetailBeanList[#demandInfoStatus.index].revisedCollection}" cssClass="form-control" readonly="true" />
					
			</s:elseif>
			<s:elseif test="%{demandDetailBeanList[#demandInfoStatus.index].actualCollection==0}">
				<s:textfield
					name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedCollection"
					id="revisedCollection" size="10" maxlength="10"
					onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Collection');"
					style="text-align: right" class="form-control" 
					value="%{demandDetailBeanList[#demandInfoStatus.index].revisedCollection}" />
				<s:hidden
						value="%{demandDetailBeanList[#demandInfoStatus.index].isCollectionEditable}"
						name="demandDetailBeanList[%{#demandInfoStatus.index}].isCollectionEditable" />
			</s:elseif>
			<s:else>
					<s:textfield
					name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedCollection"
					id="revisedCollection" size="10" maxlength="10"
					onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Collection');"
					style="text-align: right" 
					value="%{demandDetailBeanList[#demandInfoStatus.index].revisedCollection}" cssClass="form-control" readonly="true" />
					
			</s:else>
		</div>
		</td>
</tr>
<script type="text/javascript">
	lastIndex = '<s:property value="%{#demandInfoStatus.index}" />';
	document.getElementById("lastIdx").value = lastIndex;
</script>
