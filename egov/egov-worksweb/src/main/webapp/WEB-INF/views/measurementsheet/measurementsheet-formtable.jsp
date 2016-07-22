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



