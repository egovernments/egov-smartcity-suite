<table hidden="true" >
    <tbody id="msheaderrowtemplate">
    <tr id="templatesorActivities[0].mstr" class="ms-sheet-row">
        <td colspan="9">
            <div class="view-content">Measurement Sheet <div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div>
            </div>

            <table class=" table table-bordered" id="templatesorActivities[0].mstable">
                <thead>
                <th><spring:message code="lbl.slno" /></th>
                <th><spring:message code="lbl.identifier" /></th>
                <th><spring:message code="lbl.remarks" /><span class="mandatory"></th>
                <th><spring:message code="lbl.no" /></th>
                <th><spring:message code="lbl.length" /></th>
                <th><spring:message code="lbl.width" /></th>
                <th><spring:message code="lbl.depthorheight" /></th>
                <th><spring:message code="lbl.quantity" /><span class="mandatory"></span></th>
                <th><spring:message code="lbl.delete" /></th>
                </thead>
                <tbody id="msrowtemplate1">
                <tr id="msrowtemplate">
                    <td>
                        <input  name="templatesorActivities[0].measurementSheetList[0].slNo" value="1" readonly="readonly" id="templatesorActivities[0].measurementSheetList[0].slNo" class="form-control text-right patternvalidation runtime-update spanslno" data-pattern="decimalvalue" />
                    </td>
                    <td>
                        <select name="templatesorActivities[0].measurementSheetList[0].identifier"  id="templatesorActivities[0].measurementSheetList[0].identifier"  onchange="findNet(this)" class="form-control runtime-update"   >
                            <option value="A">+</option>
                            <option value="D">-</option>
                            </select>
                    </td>
                    <td>
                        <textarea name="templatesorActivities[0].measurementSheetList[0].remarks" value=""  id="templatesorActivities[0].measurementSheetList[0].remarks" class="form-control text-left patternvalidation runtime-update"
                               data-pattern="alphanumeric" maxlength="1024" ></textarea>

                    </td>
                    <td>
                        <input name="templatesorActivities[0].measurementSheetList[0].no" value=""  id="templatesorActivities[0].measurementSheetList[0].no" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue" data-idx="0" />

                    </td>
                    <td>                                                                     
                        <input name="templatesorActivities[0].measurementSheetList[0].length" id="templatesorActivities[0].measurementSheetList[0].length" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue" data-idx="0" />

                    </td>
                    <td>
                        <input name="templatesorActivities[0].measurementSheetList[0].width" id="templatesorActivities[0].measurementSheetList[0].width" class="form-control text-right patternvalidation runtime-update"
                               data-pattern="decimalvalue"  data-idx="0" />

                    </td><td>
                    <input name="templatesorActivities[0].measurementSheetList[0].depthOrHeight" id="templatesorActivities[0].measurementSheetList[0].depthOrHeight" class="form-control text-right patternvalidation runtime-update"
                           data-pattern="decimalvalue"  data-idx="0" />

                </td><td>
                    <input name="templatesorActivities[0].measurementSheetList[0].quantity" id="templatesorActivities[0].measurementSheetList[0].quantity" class="form-control text-right patternvalidation runtime-update"
                           data-pattern="decimalvalue" required="required" onblur="findNet(this)" />

                </td>
                    <td><span class="glyphicon glyphicon-trash" onclick="deleteThisRow(this)" data-idx="${vs.index}"/></td>
                </tr>
                <tr>
                    <td colspan="6" class="text-right">
                        <input type="button" value ="Add Row" class="btn btn-xs btn-info add-msrow">
                        <button   class="btn btn-xs btn-danger">Reset</button>
                        <input type="button" value="Submit"  id="templatesorActivities[0].mssubmit" class="btn btn-xs btn-primary ms-submit"/> 
                    </td>
                    <td class="text-right">Grand Total</td>
                    <td id="templatesorActivities[0].msnet" class="text-right"></td>
                    <td></td>
                </tr>
                <tbody>
            </table>
        </td>
    </tr>
   </tbody>

</table>



