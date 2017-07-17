<div class="panel-heading" style="text-align: left">
	<div class="panel-title">Location details</div>
</div>
<div class="panel-body">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="locality" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property default="N/A"
				value="%{basicProp.propertyID.locality.name}" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="zone" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property default="N/A" value="%{basicProp.propertyID.zone.name}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="Ward" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property default="N/A" value="%{basicProp.propertyID.ward.name}" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="block" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property default="N/A" value="%{basicProp.propertyID.area.name}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="Street" />:
		</label>
		<div class="col-sm-3 add-margin">
			<s:property default="N/A" value="%{basicProp.propertyID.Street.name}" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="elec.wardno" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property default="N/A"
				value="%{basicProp.propertyID.electionBoundary.name}" />
		</div>
	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="doorNo" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:if test="%{(doorNo != null && doorNo != '')}">
				<s:property value="%{doorNo}" />
			</s:if>
			<s:else>
				N/A
			</s:else>
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="PinCode" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property value="%{pinCode}" />
		</div>
	</div>
</div>