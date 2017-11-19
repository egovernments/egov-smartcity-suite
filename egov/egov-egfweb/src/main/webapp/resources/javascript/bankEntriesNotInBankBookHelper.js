/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
var BANKENTRIESNOTINBANKBOOKLIST = 'bankEntriesNotInBankBookList';
var bankEntriesNotInBankBookTableIndex = 0;
var bankEntriesNotInBankBooksTable;
function updateGridBENIBB(field, index, value) {

	document.getElementById(BANKENTRIESNOTINBANKBOOKLIST + '[' + index + '].'
			+ field).value = value;
	var obj = document.getElementById(BANKENTRIESNOTINBANKBOOKLIST + '['
			+ index + '].' + field);
	var beId = document.getElementById('bankEntriesNotInBankBookList[' + index
			+ '].beId').value;
	if (field != "beId" && beId != "")
		jQuery(obj).attr("disabled", true);

}
function updateGridDateBENIBB(index) {

	document.getElementById(BANKENTRIESNOTINBANKBOOKLIST + '[' + index
			+ '].date').value = document
			.getElementById(BANKENTRIESNOTINBANKBOOKLIST + '[' + index
					+ '].dateId').value;
	var beId = document.getElementById('bankEntriesNotInBankBookList[' + index
			+ '].beId').value;
	if (beId != "")
		jQuery(
				document.getElementById(BANKENTRIESNOTINBANKBOOKLIST + '['
						+ index + '].date')).attr("disabled", true);

}

function createTextFieldFormatterBENIBB(prefix, suffix) {
	return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData)) ? oData : "";
		el.innerHTML = "<input type='type' id='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix
				+ "' name='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix
				+ "' style='width:90px;' />";
	}
}

function createAmountFieldFormatterBENIBB(prefix, suffix) {
	return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData)) ? oData : "";
		el.innerHTML = "<input type='text'  id='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix
				+ "'  name='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix
				+ "' style='text-align:right;width:90px;'/>";

	}
}

function createHiddenFieldFormatterBENIBB(prefix, suffix) {
	return function(el, oRecord, oColumn, oData) {
		el.innerHTML = "<input type='text' id='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix
				+ "' name='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix + "'/>";
	}
}

function createCheckBoxFormatterBENIBB(prefix, suffix) {
	return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData)) ? oData : "";
		el.innerHTML = "<input type='checkbox' id='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix
				+ "' name='" + prefix + "["
				+ bankEntriesNotInBankBookTableIndex + "]" + suffix
				+ "'  style='width:90px;' onclick='setValue(this)' />";
	}
}

function createDateFormatterBENIBB(prefix, suffix) {
	return function(el, oRecord, oColumn, oData) {
		var value = (YAHOO.lang.isValue(oData)) ? oData : "";
		el.innerHTML = '<s:date name="'
				+ prefix
				+ '['
				+ bankEntriesNotInBankBookTableIndex
				+ ']'
				+ suffix
				+ 'Id" id="'
				+ prefix
				+ '['
				+ bankEntriesNotInBankBookTableIndex
				+ ']'
				+ suffix
				+ 'Id" format="dd/MM/yyyy" /> <input type="text" id="'
				+ prefix
				+ '['
				+ bankEntriesNotInBankBookTableIndex
				+ ']'
				+ suffix
				+ '"	name="'
				+ prefix
				+ '['
				+ bankEntriesNotInBankBookTableIndex
				+ ']'
				+ suffix
				+ '" data-date-end-date="0d" onkeyup="DateFormat(this,this.value,event,false,\'3\')"	placeholder="DD/MM/YYYY" class="form-control datepicker" data-inputmask="\'mask\': \'d/m/y\'"  />';
	}
}

function createDropdownFormatterBENIBB(prefix) {
	return function(el, oRecord, oColumn, oData) {
		var selectedValue = (lang.isValue(oData)) ? oData : oRecord
				.getData(oColumn.field), options = (lang
				.isArray(oColumn.dropdownOptions)) ? oColumn.dropdownOptions
				: null, selectEl, collection = el
				.getElementsByTagName("select");
		if (collection.length === 0) {
			selectEl = document.createElement("select");
			selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
			selectEl.name = prefix + '[' + bankEntriesNotInBankBookTableIndex
					+ '].' + oColumn.getKey();
			selectEl.id = prefix + '[' + bankEntriesNotInBankBookTableIndex
					+ '].' + oColumn.getKey();
			// selectEl.onfocus=check;
			selectEl = el.appendChild(selectEl);
			var selectedIndex = {
				value : bankEntriesNotInBankBookTableIndex
			};

			/*
			 * YAHOO.util.Event.addListener(selectEl, "change",
			 * onDropdownChange, selectedIndex, this);
			 */

		}

		selectEl = collection[0];

		if (selectEl) {
			selectEl.innerHTML = "";
			if (options) {
				for (var i = 0; i < options.length; i++) {
					var option = options[i];
					var optionEl = document.createElement("option");
					optionEl.value = (lang.isValue(option.value)) ? option.value
							: option;
					optionEl.innerHTML = (lang.isValue(option.text)) ? option.text
							: (lang.isValue(option.label)) ? option.label
									: option;
					optionEl = selectEl.appendChild(optionEl);
					if (optionEl.value == selectedValue) {
						optionEl.selected = true;
					}
				}
			} else {
				selectEl.innerHTML = "<option selected value=\""
						+ selectedValue + "\">" + selectedValue + "</option>";
			}
		} else {
			el.innerHTML = lang.isValue(oData) ? oData : "";
		}
	}
}
function updateBENIBBTableIndex() {

	bankEntriesNotInBankBookTableIndex = bankEntriesNotInBankBookTableIndex + 1;
}

function setValue(obj) {
	console.log("inside fun");
	if (obj.checked) {
		console.log("true");
		obj.value = true;
	} else {
		obj.value = false;
	}
}

function loadBankAccount(obj) {
	if (obj.options[obj.selectedIndex].value != -1) {
		var branchId = obj.options[obj.selectedIndex].value;
		// bootbox.alert("heelo"+x);
		populatebankaccount({
			branchId : branchId
		});
	}

}

function loadBankBranch(obj) {
	if (obj.options[obj.selectedIndex].value != -1) {
		var bankId = obj.options[obj.selectedIndex].value;
		// bootbox.alert("heelo"+x);
		populatebank_branch({
			bankId : bankId
		});
	}

}
function loadBank(obj) {
	if (obj.options[obj.selectedIndex].value != -1) {
		var fundId = obj.options[obj.selectedIndex].value;
		// bootbox.alert("heelo"+x);
		populatebank({
			fundId : fundId
		});
	}

}

function validate() {
	var flag = true;
	for (var i = 0; i < bankEntriesNotInBankBookTableIndex; i++) {
		var type = document.getElementById('bankEntriesNotInBankBookList[' + i
				+ '].type').value;
		var glcode = document.getElementById('bankEntriesNotInBankBookList['
				+ i + '].glcodeDetail').value;
		var date = document.getElementById('bankEntriesNotInBankBookList[' + i
				+ '].date').value;
		var amount = document.getElementById('bankEntriesNotInBankBookList['
				+ i + '].amount').value;
		if (glcode == null || glcode == 0) {
			bootbox.alert('Please select account code  for row ' + (i + 1));
			flag = false;
		} else if (type == null || type == 0) {
			bootbox.alert('Please select type for row ' + (i + 1));
			flag = false;
		} else if (date == null || date == '') {
			bootbox.alert('Please select date for row ' + (i + 1));
			flag = false;
		} else if (amount == null || amount == '') {
			bootbox.alert('Please enter amount for row  ' + (i + 1));
			flag = false;
		}
	}
	return flag;
}


function enableAll() {
	for (var i = 0; i < document.bankEntriesNotInBankBookform.length; i++)
		document.bankEntriesNotInBankBookform.elements[i].disabled = false;
}
function disableAll() {
	if(document.getElementById('fundId'))
		document.getElementById('fundId').disabled = true;
	
	if(document.getElementById('vouchermis.function'))
		document.getElementById('vouchermis.function').disabled = true;
	
	if(document.getElementById('vouchermis.departmentid'))
		document.getElementById('vouchermis.departmentid').disabled = true;
	
	if(document.getElementById('schemeid'))
		document.getElementById('schemeid').disabled = true;
	
	if(document.getElementById('subschemeid'))
		document.getElementById('subschemeid').disabled = true;
	
	if(document.getElementById('vouchermis.functionary'))
		document.getElementById('vouchermis.functionary').disabled = true;
	
	if(document.getElementById('fundsourceId'))
		document.getElementById('fundsourceId').disabled = true;
	
	if(document.getElementById('vouchermis.divisionid'))
		document.getElementById('vouchermis.divisionid').disabled = true;
	
	if(document.getElementById('bank'))
		document.getElementById('bank').disabled = true;
	
	if(document.getElementById('bank_branch'))
		document.getElementById('bank_branch').disabled = true;
	
	if(document.getElementById('bankaccount'))
		document.getElementById('bankaccount').disabled = true;
	
}
