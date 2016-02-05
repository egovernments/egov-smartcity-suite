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
		var url = "/EGF/commons/Process.jsp?type=coaDetailCode?glCode="
				+ document.getElementById("minor").value;
	} else if (document.getElementById("major").value != "") {
		url = "/EGF/commons/Process.jsp?type=coaDetailCode?glCode="
				+ document.getElementById("major").value;
	} else if (document.getElementById("type").value == "A") {
		url = "/EGF/commons/Process.jsp?type=getAllAssetCodes";
	} else {
		url = "/EGF/commons/Process.jsp?type=getAllLiabCodes";
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
	var url = "/EGF/commons/Process.jsp?type=getAllFunctionName";
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
			bootbox
					.alert("Invalid Account Code selected .Please select code from auto complete.");
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
						console.log(data);
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
				console.log("glcodeid " + glcodeid);
				console.log("accountCode " + accountCode);
				var index;
				for (var i = 1; i <= resultLength; i++) {
					index = i - 1;
					var tempglcodeid = document
							.getElementById('transactionSummaryList[' + index
									+ '].glcodeid.id').value;
					var tempaccountCode = document
							.getElementById('transactionSummaryList[' + index
									+ '].glcodeDetail').value;
					console.log("tempglcodeid " + tempglcodeid);
					console.log("tempaccountCode " + tempaccountCode);
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
			// document.getElementById(TRANSACTIONSUMMARYLIST+'['+$currRow+']'+'.detailKey').value=entity_array[0].split("`-`")[1];
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
		/*
		 * $ .ajax({ url : 'ajax/getTransactionSummary', type : "get", data : {
		 * glcodeid : glcodeid, accountdetailtypeid :
		 * accountdetailtypeid.trim(), accountdetailkey : accountdetailkey },
		 * success : function(data, textStatus, jqXHR) { if (data.length != 0) {
		 * bootbox .alert("Transaction Summary for this combination already
		 * exists");
		 * 
		 * document.getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].glcodeid.id').value = 0;
		 * document.getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].glcodeDetail').value = "";
		 * document.getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].accounthead').innerHTML = ""; document
		 * .getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].accountdetailtype.id').value = 0;
		 * document.getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].accountdetailkey').value = "";
		 * document.getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].accountdetailkeyValue').value = "";
		 * document.getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].openingdebitbalance').value = ""; document
		 * .getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].openingcreditbalance').value = "";
		 * document.getElementById('transactionSummaryList' + '[' + $currRow +
		 * '].narration').value = ""; } }, error : function(jqXHR, textStatus,
		 * errorThrown) { // alert("Error validating duplicate Transaction"); }
		 * });
		 */
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
						console.log(data);
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
	oAutoCompEntityForJV.forceSelection = true;
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
	var detailtypeidObj = document.getElementById('transactionSummaryList['
			+ $currRow + '].accountdetailtype.id');
	console.log("subledger type id " + detailtypeidObj.value);
	if (detailtypeidObj.value == null || detailtypeidObj.value == "") {
		bootbox.alert("Please select subledger type")
		return false;
	}
	if (detailTypeId != detailtypeidObj.value) {
		detailTypeId = detailtypeidObj.value;
		loadDropDownCodesForEntities(detailtypeidObj);
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
			var rowCount = $('#result tr').length - 1;
			console.log(rowCount);
			var content = $('#resultrow0').html();
			resultContent = content.replace(/0/g, rowCount);
			$(resultContent).find("input").val("");
			$('#result > tbody:last').append("<tr>" + resultContent + "</tr>");
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
			var entityObj = document.getElementById('transactionSummaryList['
					+ rowCount + ']' + '.accountdetailkeyValue');
			var detailTypeObj = document
					.getElementById('transactionSummaryList[' + rowCount + ']'
							+ '.accountdetailtype.id');
			$(entityObj).removeClass("mandatory");
			$(detailTypeObj).removeClass("mandatory");
			$("#result tr:last #remove-row").show();
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

$('#buttonSubmit')
		.click(
				function(e) {
					if (validateInput()) {
						var postData = $("#transactionSummaryform")
								.serializeArray();
						var formURL = $("#transactionSummaryform").attr(
								"action");
						$
								.ajax({
									url : formURL,
									type : "post",
									data : postData,
									success : function(data, textStatus, jqXHR) {
										bootbox.alert(
												'Data Saved Successfully',
												function() {
													location.reload();
												});
										$.each(data, function(index) {
											var obj = $("#result tbody tr")
													.get(index);
											$(obj).children(':first-child')
													.val(data[index].id);
										});
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										bootbox
												.alert("Transaction Summary for this combination already exists");
									}
								});
						e.preventDefault(); // STOP default action
					} else {
						window.scrollTo(0, 0);
						e.preventDefault();
					}
				});
$('#buttonProceed').click(function(e) {
	if (validSearch()) {
		document.getElementById('errors').innerHTML = "";
		$('#divProceed').addClass("display-hide");
		$('#result').removeClass("display-hide");
		$('#buttonCreate').removeClass("display-hide");
		$("#type").attr('disabled', 'disabled');
		var financialyear = document.getElementById('financialyear.id');
		var departmentid = document.getElementById('departmentid.id');
		var fund = document.getElementById('fund.id');
		var functionid = document.getElementById('functionid.id');
		$(financialyear).attr('disabled', 'disabled');
		$(departmentid).attr('disabled', 'disabled');
		$(fund).attr('disabled', 'disabled');
		$(functionid).attr('disabled', 'disabled');
	}
});
function validateInput() {
	console.log("length:" + $('#result tr').length);
	var flag = true;
	var elems = document.getElementsByClassName("mandatory");
	for (var i = 0; i < elems.length; i++) {
		if (elems[i].id != '') {
			console.log(elems[i].id);
			var val = document.getElementById(elems[i].id).value;
			if (val == null || val == '') {
				document.getElementById('errors').innerHTML = 'Please check * marked fields are mandatory';
				flag = false;
			}
		}
	}

	var elems1 = document.getElementsByClassName("mandatoryField");
	var debit = document.getElementById(elems1[0].id).value;
	var credit = document.getElementById(elems1[1].id).value;
	if (debit == '' && credit == '') {
		document.getElementById('errors').innerHTML = 'Please check * marked fields are mandatory';
		flag = false;
	}
	var resultLength = $('#result tr').length - 1;
	var index;
	for (var i = 1; i <= resultLength; i++) {
		index = i - 1;
		var openingdebitbalance = document
				.getElementById('transactionSummaryList[' + index
						+ '].openingdebitbalance').value;
		var accountCode = document.getElementById('transactionSummaryList['
				+ index + '].glcodeDetail').value;
		var openingcreditbalance = document
				.getElementById('transactionSummaryList[' + index
						+ '].openingcreditbalance').value;
		console.log(accountCode);
		console.log(openingdebitbalance);
		console.log(openingcreditbalance);
		if (openingdebitbalance > 0 && openingcreditbalance > 0) {
			bootbox
					.alert("Opening debit amount and credit amount cannot be there for the account code   "
							+ accountCode);
			return false;
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

$('#major').change(

		function() {
			// loadDropDownCodes();
			$.ajax({
				method : "GET",
				url : "ajax/getMinorHeads",
				data : {
					majorCode : $('#major').val(),
					classification : 2
				},
				async : true
			}).done(
					function(msg) {
						$('#minor').empty();
						var output = '<option value="">Select</option>';
						$.each(msg, function(index, value) {
							output += '<option value=' + value.glcode + '>'
									+ value.glcode + ' - ' + value.name
									+ '</option>';
						});
						$('#minor').append(output);
					});
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