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
			<s:if
				test="#demandInfoStatus.index > 0 && demandDetailBeanList[#demandInfoStatus.index].installment == demandDetailBeanList[#demandInfoStatus.index - 1].installment">
					&nbsp;
				</s:if>
			<s:else>
				<s:property
					value="%{demandDetailBeanList[#demandInfoStatus.index].installment}" />
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
				<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.nmc.constants.NMCPTISConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
					<s:textfield
						name="demandDetailBeanList[%{#demandInfoStatus.index}].actualAmount"
						id="revisedTax" size="10" maxlength="10"
						onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Actual Tax');"
						value="%{demandDetailBeanList[#demandInfoStatus.index].actualAmount}"													
						style="text-align: right" />
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
				<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.nmc.constants.NMCPTISConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
					N/A
				</s:if>
				<s:else>
					<s:textfield
						name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedAmount"
						id="revisedTax" size="10" maxlength="10"
						onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Tax');"
						value="%{demandDetailBeanList[#demandInfoStatus.index].revisedAmount}"
						style="text-align: right" />
				</s:else>
			</div>
		</td>		
		<td class="blueborderfortd" style="padding-right: 10px">
			<div align="right">						
				<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.nmc.constants.NMCPTISConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
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
			<s:if test="%{demandDetailBeanList[#demandInfoStatus.index].reasonMaster == @org.egov.ptis.nmc.constants.NMCPTISConstants@DEMANDRSN_STR_CHQ_BOUNCE_PENALTY && demandDetailBeanList[#demandInfoStatus.index].isNew == true}" >
				N/A
			</s:if>
			<s:else>
				<s:textfield
					name="demandDetailBeanList[%{#demandInfoStatus.index}].revisedCollection"
					id="revisedCollection" size="10" maxlength="10"
					onblur="trim(this,this.value); checkNumber(this); isPositiveNumber(this, 'Revised Collection');"
					style="text-align: right" 
					value="%{demandDetailBeanList[#demandInfoStatus.index].revisedCollection}"
					cssClass="%{demandDetailBeanList[#demandInfoStatus.index].isCollectionEditable == true ? '' : 'hiddentext'}" 
					disabled="!demandDetailBeanList[#demandInfoStatus.index].isCollectionEditable"/>
				<s:hidden
						value="%{demandDetailBeanList[#demandInfoStatus.index].isCollectionEditable}"
						name="demandDetailBeanList[%{#demandInfoStatus.index}].isCollectionEditable" />
			</s:else>
		</div>
		</td>
</tr>
<script type="text/javascript">
	lastIndex = '<s:property value="%{#demandInfoStatus.index}" />';										
</script>