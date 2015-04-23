<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<s:text name="objection.add.hearingDetails" />
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="5">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				 <s:set var="hearingIdx" value="%{hearings.size()-1}"/>
				<tr>
					<td class="bluebox" width="25%">
						<s:text name="hearing.details" />
						<span class="mandatory1">*</span>
					</td>
					<td class="bluebox" width="25%">
						<s:textarea name="objection.hearings[%{hearingIdx}].hearingDetails"
							id="hearingDetails" cols="40" rows="2" onblur="checkLength(this)"></s:textarea>
					</td>
					<td class="bluebox" width="25%">
						<s:text name="hearing.inspection.required" />
					</td>
					<td class="bluebox" width="25%">
						<s:radio name="objection.hearings[%{hearingIdx}].inspectionRequired"
							list="#{'true':'Yes','false':'No'}" id="inspectionRequired" />
					</td>

				</tr>
				<tr>
					<td class="greybox" width="25%"><s:text name="objection.upload.document"/></td>
					<td class="greybox" width="25%">
						<input type="button" class="button" value="Upload Document"
							id="docUploadButton" onclick="showDocumentManager();" />
							<s:hidden name="objection.hearings[%{hearingIdx}].documentNumber" id="docNumber" />
					</td>
				
				</tr>
			</table>
		</td>
	</tr>
</table>