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
    <tbody id="cqmsheaderrowtemplate">
    <tr id="templatesorActivities[0].mstr" class="msheet-tr">
        <td colspan="15">
            <div class="view-content" style="color:#f2851f"><spring:message code="lbl.measurementsheet" /> <div class="pull-right"><span class="glyphicon glyphicon-remove-circle error-msg hide-ms" style="cursor:pointer;font-size:16px;"></span></div></div>
            <table class=" table table-bordered msheet-table" id="templatesorActivities[0].mstable">
                <thead>
                	<tr>
	                    <th colspan="1"></th>
	                    <th colspan="7"><spring:message code="lbl.approved.qty" /></th>
	                    <th colspan="5"><spring:message code="lbl.revised.qty" /></th>
                    </tr>
                    <tr>
	                    <th><spring:message code="lbl.slno" /></th>
	                    <th><spring:message code="lbl.description" /></th>
	                    <th><spring:message code="lbl.number" /></th>
	                    <th><spring:message code="lbl.length" /></th>
	                    <th><spring:message code="lbl.width" /></th>
	                    <th><spring:message code="lbl.depthorheight" /></th>
	                    <th><spring:message code="lbl.quantity" /></th>
	                    <th><spring:message code="lbl.identifier" /></th>
	                    <th><spring:message code="lbl.number" /></th>
	                    <th><spring:message code="lbl.length" /></th>
	                    <th><spring:message code="lbl.width" /></th>
	                    <th><spring:message code="lbl.depthorheight" /></th>
	                    <th><spring:message code="lbl.quantity" /></th>
                    </tr>
                </thead>
                <tbody id="cqmsrowtemplate1">
	                <tr id="cqmsrowtemplate">
	                	<td hidden="true">
	                        <input name="templatesorActivities[0].measurementSheetList[0].id" class="clearthis" id="templatesorActivities_0_measurementSheetList_0_id"></input>
	                        <input name="templatesorActivities[0].measurementSheetList[0].parent" id="templatesorActivities_0_measurementSheetList_0_parent"></input>
	                    </td>
	                	<td id="msrowslNo_0_0"></td>
	                    <td id="msrowremarks_0_0"></td>
	                    <td id="msrowno_0_0" align="right"></td>
	                    <td id="msrowlength_0_0" align="right"></td>
	                    <td id="msrowwidth_0_0" align="right"></td>
	                    <td id="msrowdepthOrHeight_0_0" align="right"></td>
	                    <td id="msrowquantity_0_0" align="right"></td>
	                    <td id="msrowidentifier_0_0"></td>
	                    <td>
	                        <input name="templatesorActivities[0].measurementSheetList[0].no" value="" onkeyup="limitCharatersBy3_2(this);"  id="templatesorActivities[0].measurementSheetList[0].no" class="form-control text-right patternvalidation runtime-update"
	                               data-pattern="decimalvalue" data-idx="0" data-no="" />
	
	                    </td>
	                    <td>                                                                     
	                        <input name="templatesorActivities[0].measurementSheetList[0].length" onkeyup="limitCharatersBy10_4(this);" id="templatesorActivities[0].measurementSheetList[0].length" class="form-control text-right patternvalidation runtime-update"
	                               data-pattern="decimalvalue" data-idx="0" data-length="" />
	
	                    </td>
	                    <td>
	                        <input name="templatesorActivities[0].measurementSheetList[0].width" onkeyup="limitCharatersBy10_4(this);" id="templatesorActivities[0].measurementSheetList[0].width" class="form-control text-right patternvalidation runtime-update"
	                               data-pattern="decimalvalue"  data-idx="0" data-width="" />
	
	                    </td>
	                    <td>
		                    <input name="templatesorActivities[0].measurementSheetList[0].depthOrHeight" onkeyup="limitCharatersBy10_4(this);" id="templatesorActivities[0].measurementSheetList[0].depthOrHeight" class="form-control text-right patternvalidation runtime-update"
		                           data-pattern="decimalvalue"  data-idx="0" data-depthOrHeight="" />
	
	                	</td>
	                	<td>
		                    <input name="templatesorActivities[0].measurementSheetList[0].quantity" onkeyup="limitCharatersBy10_4(this);" id="templatesorActivities[0].measurementSheetList[0].quantity" class="form-control text-right patternvalidation runtime-update"
		                           data-pattern="decimalvalue" onblur="findNet(this)" data-quantity="" />
	
	                	</td>
	                </tr>
	                <tr>
	                	<td colspan="5"></td>
	                    <td class="text-right"><spring:message code="lbl.total" /></td>
	                    <td class="text-right view-content cqtotal_0_0"></td>
	                    <td colspan="4" class="text-right">
	                        <input type="button" name="resetButton" value="Reset" id="resetButton" class="btn btn-xs btn-danger reset-cq" />
	                        <input type="button" value="Submit"  id="templatesorActivities[0].mssubmit" class="btn btn-xs btn-primary ms-submit templatemssubmit_0"/> 
	                    </td>
	                    <td class="text-right"><spring:message code="lbl.subtotal" /></td>
	                    <td id="templatesorActivities[0].msnet" class="text-right changequantity-msnet"></td>
	                </tr>
                </tbody>
            </table>
        </td>
    </tr>
   </tbody>
</table>



