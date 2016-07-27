<table hidden="true" >
    <tbody id="msheaderrowtemplate">
    <tr id="templatesorMbDetails[0].mstr" class="ms-sheet-row">
        <td colspan="15">
            <div class="view-content">Measurement Sheet <div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div>
            <table class=" table table-bordered" id="templatesorMbDetails[0].mstable">
                <thead>
                	<tr>
	                    <th colspan="1"></th>
	                    <th colspan="7">Estimated</th>
	                    <th colspan="6">Actuals</th>
                    </tr>
                    <tr>
	                    <th>S.no</th>
	                    <th>Description</th>
	                    <th>Number</th>
	                    <th>Length</th>
	                    <th>Width</th>
	                    <th>Depth/Height</th>
	                    <th>Quantity</th>
	                    <th>Deduction</th>
	                    <th>Remarks</th>
	                    <th>Number</th>
	                    <th>Length</th>
	                    <th>Width</th>
	                    <th>Depth/Height</th>
	                    <th>Quantity</th>
                    </tr>
                </thead>
                <tbody id="msrowtemplate1">
	                <tr id="msrowtemplate">
	                	<td hidden="true">
	                        <input name="templatesorMbDetails[0].measurementSheets[0].id" id="templatesorMbDetails_0_measurementSheets_0_id"></input>
	                        <input name="templatesorMbDetails[0].measurementSheets[0].woMeasurementSheet" id="templatesorMbDetails_0_measurementSheets_0_woMeasurementSheet"></input>
	                    </td>
	                	<td id="msrowslNo_0_0"></td>
	                    <td id="msrowremarks_0_0"></td>
	                    <td id="msrowno_0_0"></td>
	                    <td id="msrowlength_0_0"></td>
	                    <td id="msrowwidth_0_0"></td>
	                    <td id="msrowdepthOrHeight_0_0"></td>
	                    <td id="msrowquantity_0_0"></td>
	                    <td id="msrowidentifier_0_0"></td>
	                    <td>
	                        <textarea name="templatesorMbDetails[0].measurementSheets[0].remarks" value=""  id="templatesorMbDetails[0].measurementSheets[0].remarks" class="form-control text-left patternvalidation runtime-update"
	                               data-pattern="alphanumeric" maxlength="1024" ></textarea>
	
	                    </td>
	                    <td>
	                        <input name="templatesorMbDetails[0].measurementSheets[0].no" value=""  id="templatesorMbDetails[0].measurementSheets[0].no" class="form-control text-right patternvalidation runtime-update"
	                               data-pattern="decimalvalue" data-idx="0" />
	
	                    </td>
	                    <td>                                                                     
	                        <input name="templatesorMbDetails[0].measurementSheets[0].length" id="templatesorMbDetails[0].measurementSheets[0].length" class="form-control text-right patternvalidation runtime-update"
	                               data-pattern="decimalvalue" data-idx="0" />
	
	                    </td>
	                    <td>
	                        <input name="templatesorMbDetails[0].measurementSheets[0].width" id="templatesorMbDetails[0].measurementSheets[0].width" class="form-control text-right patternvalidation runtime-update"
	                               data-pattern="decimalvalue"  data-idx="0" />
	
	                    </td>
	                    <td>
		                    <input name="templatesorMbDetails[0].measurementSheets[0].depthOrHeight" id="templatesorMbDetails[0].measurementSheets[0].depthOrHeight" class="form-control text-right patternvalidation runtime-update"
		                           data-pattern="decimalvalue"  data-idx="0" />
	
	                	</td>
	                	<td>
		                    <input name="templatesorMbDetails[0].measurementSheets[0].quantity" id="templatesorMbDetails[0].measurementSheets[0].quantity" class="form-control text-right patternvalidation runtime-update"
		                           data-pattern="decimalvalue" onblur="findNet(this)" />
	
	                	</td>
	                </tr>
	                <tr>
	                	<td colspan="5"></td>
	                    <td class="text-right">Total</td>
	                    <td class="text-right view-content"></td>
	                    <td colspan="4" class="text-right">
	                        <button name="resetButton" id="resetButton" class="btn btn-xs btn-danger reset-ms">Reset</button>
	                        <input type="button" value="Submit"  id="templatesorMbDetails[0].mssubmit" class="btn btn-xs btn-primary ms-submit templatemssubmit_0"/> 
	                    </td>
	                    <td class="text-right">Total</td>
	                    <td id="templatesorMbDetails[0].msnet" class="text-right"></td>
	                    <td></td>
	                </tr>
                </tbody>
            </table>
        </td>
    </tr>
   </tbody>
</table>



