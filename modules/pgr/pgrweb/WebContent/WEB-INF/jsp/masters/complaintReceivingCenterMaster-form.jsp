
			
			<div>
				<s:textfield class="labelcell" name="model.receivingCenterName"  label="%{getText('Masters.complaintreceivingcenter.name')}" required="true"/>
			</div>
			<div>
				<s:textfield class="labelcell" name="model.receivingCenterLocalName"  label="%{getText('Masters.complaintreceivingcenter.name.local')}" />

			</div>
			<div>
				<s:textfield class="labelcell" name="model.receivingCenterType" label="%{getText('Masters.complaintreceivingcenter.type')}" />
			</div>
			<div>
				<s:textarea class="labelcell" name="model.receivingCenterAddress"  label="%{getText('Masters.complaintreceivingcenter.address')}" cols="17"/>
			</div>
			<div>
				<s:textarea class="labelcell" name="model.receivingCenterLocalAddress"  label="%{getText('Masters.complaintreceivingcenter.address.local')}" cols="17"/>
			</div>
			<div>
				<s:select class="labelcell" required="true" list="cityDropDownList" label="%{getText('Masters.complaintreceivingcenter.city')}"  name="model.receivingCenterBoundryId" listKey="id" listValue="name" headerKey="0"   headerValue="-------please Select-------" ></s:select>

			</div>

			