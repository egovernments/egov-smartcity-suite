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

/**
 * 
 */

var allGlcodes = {};
var slDetailTableIndex = 1;
var billDetailTableIndex = 0;
var TRANSACTIONSUMMARYLIST = 'transactionSummaryList';
var entities;
var detailTypeId = 0;
var codeObj;
var $currRow;
var validCheck = true;
function loadDropDownCodes() {

	if (codeObj) {
		codeObj = null;
		if (oAutoCompCode != null)
			oAutoCompCode.destroy();
	}
	var url = "";
	if (document.getElementById("minor").value != "") {
		var url = "/EGF/voucher/common-ajaxCoaDetailCode.action?glCode="
				+ document.getElementById("minor").value;
	} else if (document.getElementById("major").value != "") {
		url = "/EGF/voucher/common-ajaxCoaDetailCode.action?glCode="
				+ document.getElementById("major").value;
	} else if (document.getElementById("type").value == "A") {
		url = "/EGF/voucher/common-ajaxGetAllAssetCodes.action";
	} else {
		url = "/EGF/voucher/common-ajaxGetAllLiabCodes.action";
	}

	var req2 = initiateRequest();
	req2.onreadystatechange = function() {
		if (req2.readyState == 4) {
			if (req2.status == 200) {
				var codes2 = req2.responseText;
				var a = codes2.split("^");
				var codes = a[0];
				acccodeArray = codes.split("+");
				for (i = 0; i < acccodeArray.length; i++) {
					data = acccodeArray[i].split("`~`")
					acccodeArray[i] = data[0];
					var key = data[0];
					var value = data[1]
					allGlcodes[key] = value;
				}
				codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
			}
		}
	};
	req2.open("GET", url, true);
	req2.send(null);
}

var funcObj;
var funcArray;
function loadDropDownCodesFunction() {
	var url = "/EGF/voucher/common-ajaxGetAllFunctionName.action";
	var req2 = initiateRequest();
	req2.onreadystatechange = function() {
		if (req2.readyState == 4) {
			if (req2.status == 200) {
				var codes2 = req2.responseText;
				var a = codes2.split("^");
				var codes = a[0];
				funcArray = codes.split("+");
				funcObj = new YAHOO.widget.DS_JSArray(funcArray);
			}
		}
	};
	req2.open("GET", url, true);
	req2.send(null);
}

var yuiflag = new Array();
var oAutoCompCode;
function autocompletecode(obj, myEvent) {

	var type = document.getElementById('type').value;

	if (type == '') {
		validCheck = false;
		bootbox.alert("Please select type")
	} else {

		var src = obj;
		var target = document.getElementById('codescontainer');
		var coaCodeObj = obj;
		$currRow = getRowIndex(obj);
		// 40 --> Down arrow, 38 --> Up arrow
		if (yuiflag[$currRow] == undefined) {
			var key = window.event ? window.event.keyCode : myEvent.charCode;
			if (key != 40) {
				if (key != 38) {
					oAutoCompCode = new YAHOO.widget.AutoComplete(coaCodeObj,
							'codescontainer', codeObj);
					oAutoCompCode.queryDelay = 0;
					oAutoCompCode.prehighlightClassName = "yui-ac-prehighlight";
					oAutoCompCode.useShadow = true;
					oAutoCompCode.maxResultsDisplayed = 15;
					oAutoCompCode.useIFrame = true;
					oAutoCompCode.applyLocalFilter = true;
					oAutoCompCode.queryMatchContains = true;
					oAutoCompCode.minQueryLength = 0;
					oAutoCompCode.doBeforeExpandContainer = function(oTextbox,
							oContainer, sQDetauery, aResults) {
						var pos = YAHOO.util.Dom.getXY(oTextbox);
						pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
						oContainer.style.width = 300;
						YAHOO.util.Dom.setXY(oContainer, pos);
						return true;

					};

					oAutoCompCode.formatResult = function(oResultData, sQuery,
							sResultMatch) {
						var data = oResultData.toString();
						return data.split("`~`")[0];
					};
				}
			}
			yuiflag[$currRow] = 1;
		}
	}
}

function findPos(ob) {
	var obj = eval(ob);
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft;
		curtop = obj.offsetTop;
		while (obj = obj.offsetParent) { // alert(obj.nodeName+"---"+obj.offsetTop+"--"+obj.offsetLeft+"-----"+curtop);
			curleft = curleft + obj.offsetLeft;
			curtop = curtop + obj.offsetTop;
			// alert(curtop);
		}
	}
	// alert(curleft+" "+curtop);
	return [ curleft, curtop ];

}

function fillNeibrAfterSplitGlcode(obj) {
	if (validSearch()) {
		var glcodeid;
		var key = obj.value;
		var temp = obj.value;
		temp = temp.split("`-`");
		$currRow = getRowIndex(obj);
		var accCodeid = allGlcodes[key];
		var glcodeid = document.getElementById('transactionSummaryList['
				+ $currRow + '].glcodeid.id').value;
		if (temp.length > 1) {
			obj.value = temp[0];
			glcodeid = temp[2];
			$currRow = getRowIndex(obj);
			document.getElementById('transactionSummaryList[' + $currRow
					+ '].glcodeid.id').value = temp[2];
			document.getElementById('transactionSummaryList[' + $currRow
					+ '].id').value = "";
			document.getElementById('transactionSummaryList[' + $currRow
					+ '].accounthead').innerHTML = temp[1];// .split("`~`")[0];
			var flag = false;
			for (var i = 0; i < slDetailTableIndex; i++) {
				for (var j = 0; j < billDetailTableIndex; j++) {
					if (null != document.getElementById(TRANSACTIONSUMMARYLIST
							+ '[' + i + '].glcode.id')) {
						var subledgerSel = document
								.getElementById(TRANSACTIONSUMMARYLIST + '['
										+ i + '].glcode.id').value;

					}
					if (null != document.getElementById(TRANSACTIONSUMMARYLIST
							+ '[' + j + '].glcodeid.id')) {
						var billDetailSel = document
								.getElementById(TRANSACTIONSUMMARYLIST + '['
										+ j + '].glcodeid.id').value;
					}
					if (subledgerSel == billDetailSel) {

						flag = true;
						break;
					}

				}
				if (!flag) {
					// document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].glcode.id').value=0;
					// document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].accountdetailtype.id').value=0;
					// document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].detailTypeName').value="";
					// document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].accountdetailkey').value="";
					// document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].detailKeyId').value="";
					// document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].detailKey').value="";
					// document.getElementById(TRANSACTIONSUMMARYLIST+'['+i+'].amount').value="";
				}

			}
			for (var i = 0; i < slDetailTableIndex; i++) {
				d = document.getElementById(TRANSACTIONSUMMARYLIST + '[' + i
						+ '].glcode.id');
				if (null != d) {
					for (p = d.options.length - 1; p >= 0; p--) {
						var flag1 = false;
						for (var j = 0; j < billDetailTableIndex; j++) {
							if (null != document
									.getElementById(TRANSACTIONSUMMARYLIST
											+ '[' + j + '].glcodeid.id')) {
								if (d.options[p].value == document
										.getElementById(TRANSACTIONSUMMARYLIST
												+ '[' + j + '].glcodeid.id').value) {
									flag1 = true;
								}
							}

						}
						if (!flag1 && d.options[p].value != 0) {
							d.remove(p);
						}
					}
				}

			}
			check();
		} else if (temp != "" && (accCodeid == null || accCodeid == "")
				&& (glcodeid == null || glcodeid == "")) {
			/*bootbox
					.alert("Invalid Account Code selected .Please select code from auto complete.");*/
			obj.value = "";
			document.getElementById('transactionSummaryList[' + $currRow
					+ '].glcodeid.id').value = "";
		}

		var subledgerid = document.getElementById('transactionSummaryList['
				+ $currRow + '].glcodeDetail');
		var accountCode = subledgerid.value;
		// document.getElementById('subLedgerlist['+$currRow+'].subledgerCode').value
		// =accountCode;
		if (accountCode != 'Select') {
			var url = '/EGF/voucher/common-getDetailType.action?accountCode='
					+ accountCode + '&index=0';
			var transaction = YAHOO.util.Connect.asyncRequest('POST', url,
					postType, null);
		} else {
			var d = document.getElementById('transactionSummaryList['
					+ $currRow + '].accountdetailtype.id');
			d.options.length = 1;
			d.options[0].text = 'Select';
			d.options[0].value = '';
		}

		$currRow = getRowIndex(obj);
		var funcObj = document.getElementById('transactionSummaryList['
				+ $currRow + '].functionDetail');

		$
				.ajax({
					url : 'ajax/searchTransactionSummariesForNonSubledger',
					type : "get",
					data : {
						finYear : document.getElementById('financialyear.id').value,
						fund : document.getElementById('fund.id').value,
						functn : document.getElementById('functionid.id').value,
						department : document.getElementById('departmentid.id').value,
						glcodeId : glcodeid,
					},
					success : function(data, textStatus, jqXHR) {
						$
								.each(
										data,
										function(index) {
											document
													.getElementById('transactionSummaryList['
															+ $currRow + '].id').value = data[index].tsid;
											document
													.getElementById('transactionSummaryList['
															+ $currRow
															+ '].openingdebitbalance').value = data[index].openingdebitbalance;
											document
													.getElementById('transactionSummaryList['
															+ $currRow
															+ '].openingcreditbalance').value = data[index].openingcreditbalance;
											document
													.getElementById('transactionSummaryList['
															+ $currRow
															+ '].narration').value = data[index].narration;
										});
					},
					error : function(jqXHR, textStatus, errorThrown) {
						bootbox.alert("Error searching data");
					}
				});

	} else if (validCheck) {
		obj.value = "";
		bootbox.alert("Please select all mandatory fields");
	}
	validCheck = true;
}

function getRowIndex(obj) {
	var temp = obj.name.split('[');
	var temp1 = temp[1].split(']');
	return temp1[0];
}

function check() {
	var accountCodes = new Array();
	for (var i = 0; i < billDetailTableIndex + 1; i++) {
		if (null != document.getElementById('transactionSummaryList[' + i
				+ '].glcodeDetail')) {
			accountCodes[i] = document.getElementById('transactionSummaryList['
					+ i + '].glcodeDetail').value;
		}
	}
	var url = '/EGF/voucher/common-getDetailCode.action?accountCodes='
			+ accountCodes;
	var transaction = YAHOO.util.Connect.asyncRequest('POST', url, callbackJV,
			null);
}

var callbackJV = {
	success : function(o) {
		var test = o.responseText;
		test = test.split('~');
		for (var j = 0; j < slDetailTableIndex; j++) {

			if (null != document.getElementById('transactionSummaryList[' + j
					+ '].glcode.id')
					&& test.length > 1) {
				d = document.getElementById('transactionSummaryList[' + j
						+ '].glcode.id');
				d.options.length = ((test.length) / 2) + 1;
				for (var i = 1; i < ((test.length) / 2) + 1; i++) {
					d.options[i].text = test[i * 2 - 2];
					d.options[i].value = test[i * 2 - 1];

				}
			}
			if (test.length < 2) {
				var d = document.getElementById('transactionSummaryList[' + j
						+ '].glcode.id');
				if (d) {
					d.options.length = 1;
					d.options[0].text = 'Select';
					d.options[0].value = '';
				}
			}
		}

	},
	failure : function(o) {
		bootbox.alert('failure');
	}
}

var postType = {
	success : function(o) {
		var detailType = o.responseText;
		var detailRecord = detailType.split('#');
		var eachItem;
		var obj;
		for (var i = 0; i < detailRecord.length; i++) {
			eachItem = detailRecord[i].split('~');
			if (obj == null) {
				obj = document.getElementById('transactionSummaryList['
						+ $currRow + ']' + '.accountdetailtype.id');
				if (obj != null)
					obj.options.length = detailRecord.length + 1;
			}
			if (obj != null) {
				obj.options[i + 1].text = eachItem[1];
				obj.options[i + 1].value = eachItem[2];
				// document.getElementById('subLedgerlist['+parseInt(eachItem[0])+']'+'.detailTypeName').value
				// = eachItem[1];
			}
			if (eachItem.length > 1) {
				var entityObj = document
						.getElementById('transactionSummaryList[' + $currRow
								+ ']' + '.accountdetailkeyValue');
				$(entityObj).addClass("mandatory");
				$(obj).addClass("mandatory");
			}
			if (eachItem.length == 1) // for deselect the subledger code
			{
				var d = document.getElementById('transactionSummaryList['
						+ $currRow + '].accountdetailtype.id');
				d.options.length = 1;
				d.options[0].text = 'Select';
				d.options[0].value = '';

				// Validate row exist already
				var glcodeid = document
						.getElementById('transactionSummaryList[' + $currRow
								+ '].glcodeid.id').value;
				var accountCode = document
						.getElementById('transactionSummaryList[' + $currRow
								+ '].glcodeDetail').value;
				var resultLength = $('#result tr').length - 1;
				var index;
				for (var i = 1; i <= resultLength; i++) {
					index = i - 1;
					var tempglcodeid = document
							.getElementById('transactionSummaryList[' + index
									+ '].glcodeid.id').value;
					var tempaccountCode = document
							.getElementById('transactionSummaryList[' + index
									+ '].glcodeDetail').value;
					if (glcodeid == tempglcodeid
							&& accountCode == tempaccountCode
							&& $currRow != index) {
						bootbox.alert(accountCode
								+ " Account code already selected ");
						document.getElementById('transactionSummaryList['
								+ $currRow + '].glcodeDetail').value = "";
						document.getElementById('transactionSummaryList['
								+ $currRow + '].glcodeid.id').value = 0;
						document.getElementById('transactionSummaryList['
								+ $currRow + '].accounthead').innerHTML = "";
						document.getElementById('transactionSummaryList['
								+ $currRow + '].id').value = "";
					}
				}
			}

		}
	},
	failure : function(o) {
		bootbox.alert('failure');
	}
}

function splitEntitiesDetailCode(obj) {
	$currRow = getRowIndex(obj);
	var entity = obj.value;
	if (entity.trim() != "") {
		var entity_array = entity.trim().split("`~`");

		if (entity_array.length == 2) {
			document.getElementById(TRANSACTIONSUMMARYLIST + '[' + $currRow
					+ ']' + '.accountdetailkeyValue').value = entity_array[0]
					.split("`-`")[0];
			document.getElementById(TRANSACTIONSUMMARYLIST + '[' + $currRow
					+ ']' + '.accountdetailkey').value = entity_array[1];
		}
	}

	var glcodeid = document.getElementById(TRANSACTIONSUMMARYLIST + '['
			+ $currRow + ']' + '.glcodeid.id').value;
	var accountCode = document.getElementById('transactionSummaryList['
			+ $currRow + '].glcodeDetail').value;
	var accountdetailtypeid = document.getElementById(TRANSACTIONSUMMARYLIST
			+ '[' + $currRow + ']' + '.accountdetailtype.id').value;
	var accountdetailkey = document.getElementById(TRANSACTIONSUMMARYLIST + '['
			+ $currRow + ']' + '.accountdetailkey').value;
	var accountdetailkeyValue = document.getElementById(TRANSACTIONSUMMARYLIST
			+ '[' + $currRow + ']' + '.accountdetailkeyValue').value;

	if (glcodeid != '' && accountdetailtypeid != '' && accountdetailkey != ''
			&& accountdetailkeyValue != '') {
		var resultLength = $('#result tr').length - 1;
		var index;
		for (var i = 1; i <= resultLength; i++) {
			index = i - 1;
			var tempglcodeid = document.getElementById(TRANSACTIONSUMMARYLIST
					+ '[' + index + ']' + '.glcodeid.id').value;

			var tempaccountdetailtypeid = document
					.getElementById(TRANSACTIONSUMMARYLIST + '[' + index + ']'
							+ '.accountdetailtype.id').value;
			var tempaccountdetailkey = document
					.getElementById(TRANSACTIONSUMMARYLIST + '[' + index + ']'
							+ '.accountdetailkey').value;
			var tempaccountdetailkeyValue = document
					.getElementById(TRANSACTIONSUMMARYLIST + '[' + index + ']'
							+ '.accountdetailkeyValue').value;

			if (glcodeid == tempglcodeid
					&& accountdetailtypeid == tempaccountdetailtypeid
					&& accountdetailkey == tempaccountdetailkey
					&& accountdetailkeyValue == tempaccountdetailkeyValue
					&& $currRow != index) {
				bootbox.alert(accountCode + " Account code already selected ");
				document.getElementById('transactionSummaryList[' + $currRow
						+ '].glcodeDetail').value = "";
				document.getElementById('transactionSummaryList[' + $currRow
						+ '].glcodeid.id').value = 0;
				document.getElementById('transactionSummaryList[' + $currRow
						+ '].accounthead').innerHTML = "";
				document.getElementById('transactionSummaryList[' + $currRow
						+ '].id').value = "";
				document.getElementById('transactionSummaryList[' + $currRow
						+ '].accountdetailkey').value = "";
				document.getElementById('transactionSummaryList[' + $currRow
						+ '].accountdetailkeyValue').value = "";
				var d = document.getElementById('transactionSummaryList['
						+ $currRow + '].accountdetailtype.id');
				d.options.length = 1;
				d.options[0].text = 'Select';
				d.options[0].value = '';
				var entityObj = document
						.getElementById('transactionSummaryList[' + $currRow
								+ ']' + '.accountdetailkeyValue');
				var subLedgerObj = document
						.getElementById('transactionSummaryList[' + $currRow
								+ ']' + '.accountdetailtype.id');
				$(entityObj).removeClass("mandatory");
				$(subLedgerObj).removeClass("mandatory");
			}
		}
		$
				.ajax({
					url : 'ajax/searchTransactionSummariesForSubledger',
					type : "get",
					data : {
						finYear : document.getElementById('financialyear.id').value,
						fund : document.getElementById('fund.id').value,
						functn : document.getElementById('functionid.id').value,
						department : document.getElementById('departmentid.id').value,
						glcodeId : glcodeid,
						accountDetailTypeId : accountdetailtypeid.trim(),
						accountDetailKeyId : accountdetailkey
					},
					success : function(data, textStatus, jqXHR) {
						$
								.each(
										data,
										function(index) {
											if (document
													.getElementById('transactionSummaryList'
															+ '['
															+ $currRow
															+ '].glcodeDetail').value != "") {
												document
														.getElementById('transactionSummaryList['
																+ $currRow
																+ '].id').value = data[index].tsid;
												document
														.getElementById('transactionSummaryList['
																+ $currRow
																+ '].openingdebitbalance').value = data[index].openingdebitbalance;
												document
														.getElementById('transactionSummaryList['
																+ $currRow
																+ '].openingcreditbalance').value = data[index].openingcreditbalance;
												document
														.getElementById('transactionSummaryList['
																+ $currRow
																+ '].narration').value = data[index].narration;
											}
										});
					},
					error : function(jqXHR, textStatus, errorThrown) {
						bootbox.alert("Error searching data");
					}
				});
	}
}
function changeaccountdetailkey(obj) {
	document.getElementById('transactionSummaryList[' + $currRow
			+ '].accountdetailkeyValue').readOnly = false;
	document.getElementById('transactionSummaryList[' + $currRow
			+ '].accountdetailkeyValue').value = "";
	document.getElementById('transactionSummaryList[' + $currRow
			+ '].accountdetailkey').value = "";
}
var oAutoCompEntityForJV;
function autocompleteEntities(obj) {
	oACDS = new YAHOO.widget.DS_XHR(
			"/EGF/voucher/common-ajaxLoadEntitesBy20.action", [ "~^" ]);
	oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	oACDS.scriptQueryParam = "startsWith";
	// alert(obj.name);
	if (oAutoCompEntityForJV != undefined) {
		oAutoCompEntityForJV.destroy();
		oAutoCompEntityForJV = null;
	}

	oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,
			'codescontainer', oACDS);
	oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery) {
		loadWaitingImage();
		var detailTypeName = obj.name.replace('accountdetailkeyValue',
				'accountdetailtype.id');
		return sQuery + "&accountDetailType="
				+ document.getElementById(detailTypeName).value;
	}
	oAutoCompEntityForJV.queryDelay = 0.5;
	oAutoCompEntityForJV.minQueryLength = 1;
	oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight";
	oAutoCompEntityForJV.useShadow = true;
	// oAutoCompEntityForJV.forceSelection = true;
	oAutoCompEntityForJV.maxResultsDisplayed = 10;
	oAutoCompEntityForJV.useIFrame = true;
	oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox,
			oContainer, sQDetauery, aResults) {
		clearWaitingImage();
		var pos = YAHOO.util.Dom.getXY(oTextbox);
		pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
		oContainer.style.width = 100;
		YAHOO.util.Dom.setXY(oContainer, pos);
		return true;
	};
}

function onFocusDetailCode(obj) {
	$currRow = getRowIndex(obj);
	var subledger = document.getElementById('transactionSummaryList['
			+ $currRow + '].accountdetailtype.id');
	var subledgerlength = $(subledger).children('option').length;
	console.log("subledgerlength" + subledgerlength);
	if (subledgerlength > 1
			&& (subledger.value == null || subledger.value == "")) {
		bootbox.alert("Please select subledger type")
		return false;
	}
	if (detailTypeId != subledger.value) {
		detailTypeId = subledger.value;
		loadDropDownCodesForEntities(subledger);
	}
}

function loadDropDownCodesForEntities(obj) {
	if (entities) {
		entities = null;
	}
	var url = "/EGF/voucher/common-ajaxLoadEntites.action?accountDetailType="
			+ obj.value;
	var req2 = initiateRequest();
	req2.onreadystatechange = function() {
		if (req2.readyState == 4) {
			if (req2.status == 200) {
				var entity = req2.responseText;

				var a = entity.split("^");
				var eachEntity = a[0];
				entitiesArray = eachEntity.split("+");
				// alert(":"+entitiesArray[0]+":");
				entities = new YAHOO.widget.DS_JSArray(entitiesArray);
			}
		}
	};
	req2.open("GET", url, true);
	req2.send(null);
}

$('body').on(
		'click',
		'#add-row',
		function() {
			if (validateOnAdd()) {
				var rowCount = $('#result tr').length - 1;
				var content = $('#resultrow0').html();
				resultContent = content.replace(/0/g, rowCount);
				$(resultContent).find("input").val("");
				$('#result > tbody:last').append(
						"<tr>" + resultContent + "</tr>");
				$('#result tr:last').find("input").val("");
				var obj = document.getElementById('transactionSummaryList['
						+ rowCount + '].accountdetailtype.id');
				$(obj).html('');
				$(obj).append(
						$("<option></option>").attr("value", '').text(
								'---Select---'));
				document.getElementById('transactionSummaryList[' + rowCount
						+ '].accounthead').innerHTML = '';
				var openingcreditbalance = document
						.getElementById('transactionSummaryList[' + rowCount
								+ '].openingcreditbalance');
				$(openingcreditbalance).removeAttr("disabled");
				var openingdebitbalance = document
						.getElementById('transactionSummaryList[' + rowCount
								+ '].openingdebitbalance');
				$(openingdebitbalance).removeAttr("disabled");
				document.getElementById('transactionSummaryList[' + rowCount
						+ '].accountdetailkeyValue').readOnly = true;
				var entityObj = document
						.getElementById('transactionSummaryList[' + rowCount
								+ ']' + '.accountdetailkeyValue');
				var detailTypeObj = document
						.getElementById('transactionSummaryList[' + rowCount
								+ ']' + '.accountdetailtype.id');
				$(entityObj).removeClass("mandatory");
				$(detailTypeObj).removeClass("mandatory");
				$("#result tr:last #remove-row").show();
				var major = $('#major').val();
				var minor = $('#minor').val();
				if (major != "" && major != null)
					document.getElementById('transactionSummaryList['
							+ rowCount + '].glcodeDetail').value = major;
				if (minor != "" && minor != null)
					document.getElementById('transactionSummaryList['
							+ rowCount + '].glcodeDetail').value = minor;
			}
		});

$('body').on('click', '#remove-row', function() {

	var count = $("#result > tbody tr").length;
	if (count > 1) {
		var obj = $(this).closest("tr").get();
		var id = $(obj).children(':first-child').val();
		if (id != '') {
			$.ajax({
				url : 'ajax/deleteTransaction',
				type : "get",
				data : {
					id : id
				},
				success : function(data, textStatus, jqXHR) {
					// alert("Success deleted data")
				},
				error : function(jqXHR, textStatus, errorThrown) {
					bootbox.alert("Error deleting data");
				}
			});
		}

		$(obj).remove();
	} else
		bootbox.alert("Can not remove this row");
	return false;
});

$('#buttonSubmit').click(function(e) {
	if (validateOnCreate()) {
		
		var myform = $('#transactionSummaryform');

		// Find disabled inputs, and remove the "disabled"
		// attribute
		var disabled = myform.find(':input:disabled').removeAttr('disabled');
		// serialize the form
		var postData = $("#transactionSummaryform").serializeArray();
		// re-disabled the set of inputs that you previously
		// enabled
		disabled.attr('disabled', 'disabled');
		var formURL = $("#transactionSummaryform").attr("action");
		$.ajax({
			url : formURL,
			type : "post",
			data : postData,
			beforeSend : function() {
				jQuery('.loader-class').modal('show', {backdrop: 'static'});
			},
			success : function(data) {
				bootbox.alert('Data Saved Successfully', function() {
					location.reload();
				});
				$.each(data, function(index) {
					var obj = $("#result tbody tr").get(index);
					$(obj).children(':first-child').val(data[index].id);
				});
			},
			error : function() {
				bootbox.alert("Error while saving data");
			},
			complete : function() {
				jQuery('.loader-class').modal('hide');
			}
		});
		e.preventDefault(); // STOP default action
	} else {
		window.scrollTo(0, 0);
		e.preventDefault();
	}
});
$('#buttonProceed')
		.click(
				function(e) {
					if (validSearch()) {
						document.getElementById('errors').innerHTML = "";
						$('#divProceed').addClass("display-hide");
						$('#result').removeClass("display-hide");
						$('#buttonCreate').removeClass("display-hide");
						$("#type").attr('disabled', 'disabled');
						var financialyear = document
								.getElementById('financialyear.id');
						var departmentid = document
								.getElementById('departmentid.id');
						var fund = document.getElementById('fund.id');
						var functionid = document
								.getElementById('functionid.id');
						$(financialyear).attr('disabled', 'disabled');
						$(departmentid).attr('disabled', 'disabled');
						$(fund).attr('disabled', 'disabled');
						$(functionid).attr('disabled', 'disabled');
						var major = $('#major').val();
						if (major != null && major != "")
							document
									.getElementById('transactionSummaryList[0].glcodeDetail').value = major;
						var minor = $('#minor').val();
						if (minor != null && minor != "")
							document
									.getElementById('transactionSummaryList[0].glcodeDetail').value = minor;
					}
				});
function validateOnAdd() {
	var flag = true;
	var resultLength = $('#result tr').length - 1;
	var index;
	for (var i = 1; i <= resultLength; i++) {
		index = i - 1;
		var glcodeid = document.getElementById('transactionSummaryList['
				+ index + '].glcodeid.id').value;
		var glcode = document.getElementById('transactionSummaryList[' + index
				+ '].glcodeDetail').value;
		var subledger = document.getElementById('transactionSummaryList['
				+ index + '].accountdetailtype.id');
		var subledgerlength = $(subledger).children('option').length;
		console.log(glcode);
		var entity = document.getElementById('transactionSummaryList[' + index
				+ '].accountdetailkeyValue').value;
		var debit = document.getElementById('transactionSummaryList[' + index
				+ '].openingdebitbalance').value;
		var credit = document.getElementById('transactionSummaryList[' + index
				+ '].openingcreditbalance').value;
		if ((glcode != '') && (glcodeid == null || glcodeid == '')) {
			bootbox
					.alert('Please select account code from auto complete for row '
							+ i);
			flag = false;
		} else if (glcode == null || glcode == '') {
			bootbox.alert('Please select account code for row ' + i);
			flag = false;
		} else if (subledgerlength > 1
				&& (subledger.value == null || subledger.value == '')) {
			bootbox.alert('Please select subledger type for account code  '
					+ glcode);
			flag = false;
		} else if (subledgerlength > 1
				&& (subledger.value != null || subledger.value != '')
				&& (entity == null || entity == '')) {
			bootbox.alert('Please select entity for account code  ' + glcode);
			flag = false;
		} else if ((debit == '' && credit == '') || (debit < 0 && credit < 0)) {
			bootbox
					.alert('Please select debit amount or credit amount for account code  '
							+ glcode);
			flag = false;
		} else if (debit > 0 && credit > 0) {
			bootbox
					.alert("Opening debit amount and credit amount cannot be there for the account code   "
							+ glcode);
			flag = false;
		}
	}

	return flag;
}
function validateOnCreate() {
	var flag = true;
	var resultLength = $('#result tr').length - 2;
	var index;
	for (var i = 0; i <= resultLength; i++) {
		index = i;
		var glcodeid = document.getElementById('transactionSummaryList['
				+ index + '].glcodeid.id').value;
		var glcode = document.getElementById('transactionSummaryList[' + index
				+ '].glcodeDetail').value;
		var subledger = document.getElementById('transactionSummaryList['
				+ index + '].accountdetailtype.id');
		var subledgerlength = $(subledger).children('option').length;
		console.log(glcode);
		var entity = document.getElementById('transactionSummaryList[' + index
				+ '].accountdetailkeyValue').value;
		var debit = document.getElementById('transactionSummaryList[' + index
				+ '].openingdebitbalance').value;
		var credit = document.getElementById('transactionSummaryList[' + index
				+ '].openingcreditbalance').value;
		var transactionId=document.getElementById('transactionSummaryList['
				+ index + '].id').value;
		if ((glcode != '') && (glcodeid == null || glcodeid == '')) {
			bootbox
					.alert('Please select account code from auto complete for row '
							+ (i + 1));
			flag = false;
		} else if (glcode == null || glcode == '') {
			bootbox.alert('Please select account code for row ' + (i + 1));
			flag = false;
		} else if (subledgerlength > 1
				&& (subledger.value == null || subledger.value == '')) {
			bootbox.alert('Please select subledger type for account code  '
					+ glcode);
			flag = false;
		} else if (subledgerlength > 1
				&& (subledger.value != null || subledger.value != '')
				&& (entity == null || entity == '')) {
			bootbox.alert('Please select entity for account code  ' + glcode);
			flag = false;
		} else if (((debit == '' && credit == '') || (debit < 1 && credit < 1)) && transactionId=="") {
			bootbox
					.alert('Please select debit amount or credit amount for account code  '
							+ glcode);
			flag = false;
		} else if (debit > 0 && credit > 0) {
			bootbox
					.alert("Opening debit amount and credit amount cannot be there for the account code   "
							+ glcode);
			flag = false;
		}
	}
	if (resultLength > 0) {
		// Validate last row if glcode is there
		var lastrow = resultLength;
		var lastrowglcodeid = document.getElementById('transactionSummaryList['
				+ lastrow + '].glcodeid.id').value;
		var lastrowglcode = document.getElementById('transactionSummaryList['
				+ lastrow + '].glcodeDetail').value;
		var lastrowsubledger = document
				.getElementById('transactionSummaryList[' + lastrow
						+ '].accountdetailtype.id');
		var lastrowsubledgerlength = $(lastrowsubledger).children('option').length;
		console.log(glcode);
		var lastrowentity = document.getElementById('transactionSummaryList['
				+ lastrow + '].accountdetailkeyValue').value;
		var lastrowdebit = document.getElementById('transactionSummaryList['
				+ lastrow + '].openingdebitbalance').value;
		var lastrowcredit = document.getElementById('transactionSummaryList['
				+ lastrow + '].openingcreditbalance').value;
		if (lastrowglcode != null && lastrowglcode != '') {
			if ((lastrowglcode != '')
					&& (lastrowglcodeid == null || lastrowglcodeid == '')) {
				bootbox
						.alert('Please select account code from auto complete for row '
								+ i);
				flag = false;
			} else if (lastrowsubledgerlength > 1
					&& (lastrowsubledger.value == null || lastrowsubledger.value == '')) {
				bootbox.alert('Please select subledger type for account code  '
						+ lastrowglcode);
				flag = false;
			} else if (lastrowsubledgerlength > 1
					&& (lastrowsubledger.value != null || lastrowsubledger.value != '')
					&& (lastrowentity == null || lastrowentity == '')) {
				bootbox.alert('Please select entity for account code  '
						+ lastrowglcode);
				flag = false;
			} else if ((lastrowdebit == '' && lastrowcredit == '')
					|| (lastrowdebit < 0 && lastrowcredit < 0)) {
				bootbox
						.alert('Please select debit amount or credit amount for account code  '
								+ lastrowglcode);
				flag = false;
			} else if (lastrowdebit > 0 && lastrowcredit > 0) {
				bootbox
						.alert("Opening debit amount and credit amount cannot be there for the account code   "
								+ lastrowglcode);
				flag = false;
			}
		} else {
			document.getElementById('transactionSummaryList[' + lastrow
					+ '].id').value = "";
			document.getElementById('transactionSummaryList[' + lastrow
					+ '].glcodeDetail').value = "";
			document.getElementById('transactionSummaryList[' + lastrow
					+ '].glcodeid.id').value = 0;
			document.getElementById('transactionSummaryList[' + lastrow
					+ '].accounthead').innerHTML = "";
			document.getElementById('transactionSummaryList[' + lastrow
					+ '].accountdetailkey').value = "";
			document.getElementById('transactionSummaryList[' + lastrow
					+ '].accountdetailkeyValue').value = "";
		}
	}
	return flag;
}
function validSearch() {
	var finYear = document.getElementById('financialyear.id').value;
	var fund = document.getElementById('fund.id').value;
	var functn = document.getElementById('functionid.id').value;
	var department = document.getElementById('departmentid.id').value;
	var type = document.getElementById('type').value;
	var flag = true;

	if (finYear == '' || fund == '' || type == '' || functn == ''
			|| department == '')
		flag = false;
	if (!flag)
		document.getElementById('errors').innerHTML = 'Please select all mandatory fields';

	return flag;
}

$('#type').change(
		function() {
			loadDropDownCodes();
			$.ajax({
				method : "GET",
				url : "ajax/getMajorHeads",
				data : {
					type : $('#type').val()
				},
				async : true
			}).done(
					function(msg) {
						$('#major').empty();
						$('#minor').empty();
						var output = '<option value="">Select</option>';
						$.each(msg, function(index, value) {
							output += '<option value=' + value.majorCode + '>'
									+ value.majorCode + ' - ' + value.name
									+ '</option>';
						});
						$('#major').append(output);
					});
			$('#major').trigger('change');
		});

$('#major')
		.change(

				function() {
					// loadDropDownCodes();
					$
							.ajax({
								method : "GET",
								url : "ajax/getMinorHeads",
								data : {
									majorCode : $('#major').val(),
									classification : 2
								},
								async : true
							})
							.done(
									function(msg) {
										$('#minor').empty();
										var output = '<option value="">Select</option>';
										$.each(msg, function(index, value) {
											output += '<option value='
													+ value.glcode + '>'
													+ value.glcode + ' - '
													+ value.name + '</option>';
										});
										$('#minor').append(output);
									});
					var major = $('#major').val();
					var accountCode = document
							.getElementById('transactionSummaryList[0].glcodeDetail').value;
					if ((accountCode == ""
							|| accountCode.length == major.length || accountCode.length == (major.length + 2))
							&& major != "" && major != null)
						document
								.getElementById('transactionSummaryList[0].glcodeDetail').value = major;
					if ($("#divProceed").hasClass("display-hide")) {
						var resultLength = $('#result tr').length - 2;
						var latestAccountCode = document
								.getElementById('transactionSummaryList['
										+ resultLength + '].glcodeDetail').value;
						if (latestAccountCode == ""
								|| latestAccountCode.length == major.length
								|| latestAccountCode.length == (major.length + 2))
							document.getElementById('transactionSummaryList['
									+ resultLength + '].glcodeDetail').value = major;
					}

				});
$('#minor')
		.change(

				function() {
					var minor = $('#minor').val();
					var major = $('#major').val();
					var accountCode = document
							.getElementById('transactionSummaryList[0].glcodeDetail').value;
					if ((accountCode == major || accountCode.length == minor.length)
							&& minor != "" && minor != null)
						document
								.getElementById('transactionSummaryList[0].glcodeDetail').value = minor;
					if ($("#divProceed").hasClass("display-hide")) {
						var resultLength = $('#result tr').length - 2;
						var latestAccountCode = document
								.getElementById('transactionSummaryList['
										+ resultLength + '].glcodeDetail').value;
						if (latestAccountCode == ""
								|| latestAccountCode == major
								|| latestAccountCode.length == minor.length)
							document.getElementById('transactionSummaryList['
									+ resultLength + '].glcodeDetail').value = minor;
					}

				});
function makeMandatory(obj) {
	var id = $(obj).attr('id');
	var rowCount = getRowIndex(obj);
	var val = $(obj).val();
	if (val != '') {
		if (id.indexOf("openingdebitbalance") > -1) {
			var openingcreditbalance = document
					.getElementById('transactionSummaryList[' + rowCount
							+ '].openingcreditbalance');
			$(openingcreditbalance).removeClass("mandatory");
		} else {
			var openingdebitbalance = document
					.getElementById('transactionSummaryList[' + rowCount
							+ '].openingdebitbalance');
			$(openingdebitbalance).removeClass("mandatory");
		}
	}
}
var acctTypeCurrRow;
function openSearchWindowFromOB(obj) {
	var index = getRowIndex(obj);
	acctTypeCurrRow = index;
	var element = document.getElementById('transactionSummaryList' + '['
			+ index + ']' + '.accountdetailtype.id');
	var detailtypeid = element.options[element.selectedIndex].value;
	if (detailtypeid != null && detailtypeid != 0) {
		var url = "../voucher/common-searchEntites.action?accountDetailType="
				+ detailtypeid;
		window
				.open(url, 'EntitySearch',
						'resizable=no,scrollbars=yes,left=300,top=40, width=400, height=500');
	} else {
		bootbox.alert("Select the subledger type.");
	}
}

function popupCallback(arg0, srchType) {
	var entity_array = arg0.split("^#");
	if (srchType == 'EntitySearch') {
		if (entity_array.length == 3) {
			document.getElementById('transactionSummaryList' + '['
					+ acctTypeCurrRow + ']' + '.accountdetailkey').value = entity_array[2];
			var accountdetailkeyValue = document
					.getElementById('transactionSummaryList' + '['
							+ acctTypeCurrRow + ']' + '.accountdetailkeyValue');
			$(accountdetailkeyValue).val(entity_array[0]).blur();
		} else {
			bootbox.alert("Invalid entity selected.");
			document.getElementById('transactionSummaryList' + '['
					+ acctTypeCurrRow + ']' + '.accountdetailkeyValue').value = "";
			document.getElementById('transactionSummaryList' + '['
					+ acctTypeCurrRow + ']' + '.accountdetailkey').value = "";
		}
	}
}
