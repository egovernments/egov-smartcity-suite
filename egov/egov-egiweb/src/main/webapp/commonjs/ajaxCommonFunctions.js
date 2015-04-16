
/*
---------------------------------------------------------------------
 File Name	:  ajaxCommonFunctions.js
 Author		:  Prabhu
---------------------------------------------------------------------
*/
/* This function is used to get the current row object */
function getRow(obj) {
	if (!obj) {
		return null;
	}
	tag = obj.nodeName.toUpperCase();
	while (tag != "BODY") {
		if (tag == "TR") {
			return obj;
		}
		obj = obj.parentNode;
		tag = obj.nodeName.toUpperCase();
	}
	return null;
}
/* This function is used to trim the input value */
function trimText(value) {
	if (value != undefined) {
		while (value.charAt(value.length - 1) == " ") {
			value = value.substring(0, value.length - 1);
		}
		while (value.substring(0, 1) == " ") {
			value = value.substring(1, value.length);
		}
	}
	return value;
}
/* This function is used to get the column object in a table */
function getControlInBranch(tableobj, columnName) {
	if (!tableobj || !(tableobj.getAttribute)) {
		return null;
	}
	// check if the object itself has the name
	if (tableobj.getAttribute("name") == columnName) {
		return tableobj;
	}

	// try its children
	var children = tableobj.childNodes;
	var child;
	if (children && children.length > 0) {
		for (var i = 0; i < children.length; i++) {
			child = this.getControlInBranch(children[i], columnName);
			if (child) {
				return child;
			}
		}
	}
	return null;
}
/* XMLHttpRequest is a request object used in Ajax. We create a Request Object here */
function initiateRequest() {
	if (window.XMLHttpRequest) {
		var req = new XMLHttpRequest();
		if (req.overrideMimeType) {
			req.overrideMimeType("text/html;charset=utf-8");
		}
		return req;
	} else {
		if (window.ActiveXObject) {
			isIE = true;
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
}
/* This function checks whether entered field is unique */
function uniqueChecking(url, tablename, columnname, fieldobj, uppercase, lowercase) {
	var fieldvalue = document.getElementById(fieldobj).value;
	if (url != "" && tablename != "" && columnname != "" && fieldvalue != "" && uppercase != "" && lowercase != "") {
		fieldvalue = trimText(fieldvalue);
		var link = "" + url + "?tablename=" + tablename + "&columnname=" + columnname + "&fieldvalue=" + fieldvalue + "&uppercase=" + uppercase + "&lowercase=" + lowercase + " ";
		var request = initiateRequest();
		request.onreadystatechange = function () {
			if (request.readyState == 4) {
				if (request.status == 200) {
					var response = request.responseText.split("^");
					if (response[0] == "false") {
						alert("Entered " + columnname + " already exists. ");
						document.getElementById(fieldobj).value = "";
						document.getElementById(fieldobj).focus();
					}
				}
			}
		};
		request.open("GET", link, true);
		request.send(null);
	}
}
/* This function checks whether entered field is unique or not(It returns a boolean value)
if it is unique, it returns true else false */
function uniqueCheckingBoolean(url, tablename, columnname, fieldobj, uppercase, lowercase) {
	var fieldvalue = document.getElementById(fieldobj).value;
	var isUnique;
	if (url != "" && tablename != "" && columnname != "" && fieldvalue != "" && uppercase != "" && lowercase != "") {
		fieldvalue = trimText(fieldvalue);
		var link = "" + url + "?tablename=" + tablename + "&columnname=" + columnname + "&fieldvalue=" + fieldvalue + "&uppercase=" + uppercase + "&lowercase=" + lowercase + " ";
		var request = initiateRequest();
		request.open("GET", link, false);
		request.send(null);
		if (request.status == 200) {
			var response = request.responseText.split("^");
			if (response[0] == "false") {
		//document.getElementById(fieldobj).focus();
				isUnique = false;
			} else {
				isUnique = true;
			}
		}
				
		
	}
	return isUnique;
}
/* This function populates a combo-box (<select> data ) based on the input's given */
function loadSelectData(url, tablename, columnname1, columnname2, whereclause, sourceobj, destobj) {
	var value = document.getElementById(sourceobj).value;
	if (value != "" && value != " ") {
		whereclause = whereclause.replace("#1", value);
		if (url != "" && tablename != "" && columnname1 != "" && columnname2 != "" && whereclause != "") {
			var link = "" + url + "?tablename=" + tablename + "&columnname1=" + columnname1 + "&columnname2=" + columnname2 + "&whereclause=" + whereclause + " ";
			var request = initiateRequest();
			request.open("GET", link, false);
			request.send(null);
			if (request.status == 200) {
				var response = request.responseText.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
				var comboObj = document.getElementById(destobj);
				comboObj.options.length = 0;
				comboObj.options[0] = new Option("--Choose--", "");
				for (var i = 1; i <= id.length; i++) {
					comboObj.options[i] = new Option(name[i - 1], id[i - 1]);
				}
			}
		}
	} else {
		var comboObj = document.getElementById(destobj);
		comboObj.options.length = 0;
	}
}
/* This function is same as loadselectdata(), but here we load the values for a row in a table */
function loadSelectDataForCurrentRow(url, tablename, columnname1, columnname2, whereclause, sourceobj, destobj, currRowObj, htmlTableName) {
	var rowobj = getRow(currRowObj);
	var table = document.getElementById(htmlTableName);
	var sourceCol = getControlInBranch(table.rows[rowobj.rowIndex], sourceobj);
	var destCol = getControlInBranch(table.rows[rowobj.rowIndex], destobj);
	var value = sourceCol.value;
	if (value != "" && value != " ") {
		whereclause = whereclause.replace("#1", value);
		if (url != "" && tablename != "" && columnname1 != "" && columnname2 != "" && whereclause != "") {
			var link = "" + url + "?tablename=" + tablename + "&columnname1=" + columnname1 + "&columnname2=" + columnname2 + "&whereclause=" + whereclause + " ";
			var request = initiateRequest();
			request.open("GET", link, false);
			request.send(null);
			if (request.status == 200) {
				var response = request.responseText.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
				var comboObj = destCol;
				comboObj.options.length = 0;
				comboObj.options[0] = new Option("--Choose--", "");
				for (var i = 1; i <= id.length; i++) {
					comboObj.options[i] = new Option(name[i - 1], id[i - 1]);
				}
			}
		}
	} else {
		var comboObj = destCol;
		comboObj.options.length = 0;
		comboObj.options[0] = new Option("--Choose--", "");
	}
}
/*
 * This function will check whether the entered two fields combination is unique or not
 */
function checkUniqueForTwoKeys(url, tablename, columnname1, fieldobj1, columnname2, fieldobj2, uppercase, lowercase) {
	var type = "compUniqueness";
	var fieldvalue1 = document.getElementById(fieldobj1).value;
	var fieldvalue2 = document.getElementById(fieldobj2).value;
	if (url != "" && tablename != "" && columnname1 != "" && fieldvalue1 != "" && columnname2 != "" && fieldvalue2 != "" && uppercase != "" && lowercase != "") {
		var link = "" + url + "?tablename=" + tablename + "&columnname=" + columnname1 + "&fieldvalue=" + fieldvalue1 + "&columnname2=" + columnname2 + "&fieldvalue2=" + fieldvalue2 + "&uppercase=" + uppercase + "&lowercase=" + lowercase + "&type=" + type + " ";
		var request = initiateRequest();
		request.onreadystatechange = function () {
			if (request.readyState == 4) {
				if (request.status == 200) {
					var response = request.responseText.split("^");
					if (response[0] == "false") {
						alert("Entered " + columnname1 + " already exists for " + columnname2 + ". ");
						document.getElementById(fieldobj1).value = "";
						document.getElementById(fieldobj1).focus();
					}
				}
			}
		};
		request.open("GET", link, true);
		request.send(null);
	}
}
/* This function loads the data for YUI autocomplete  */
function loadYUIAjaxData(link) {
	if (link != "") {
		var yahooArrayObject;
		var req = initiateRequest();
		req.open("GET", link, false);
		if (!document.all) {
			req.send(null);
			if (req.status) {
				var values = req.responseText.split("^");
				var result = values[0];
				var resultArray = result.split("+");
				yahooArrayObject = new YAHOO.widget.DS_JSArray(resultArray);
			}
			return yahooArrayObject;
		}
		req.onreadystatechange = function () {
			if (req.readyState == 4) {
				if (req.status == 200) {
					var values = req.responseText.split("^");
					var result = values[0];
					var resultArray = result.split("+");
					yahooArrayObject = new YAHOO.widget.DS_JSArray(resultArray);
				}
			}
		};
		req.send(null);
		return yahooArrayObject;
	}
}
var yuiflag = new Array();
function doAutoComplete(currRowIndex, fieldObj, divElementName, yahooArrayObject, maxResultsDisplayed) {
	doAutoComplete(currRowIndex, fieldObj, divElementName, yahooArrayObject, maxResultsDisplayed, event);
}
function doAutoComplete(currRowIndex, fieldObj, divElementName, yahooArrayObject, maxResultsDisplayed, event) {

	//40 --> Down arrow, 38 --> Up arrow
	if (yuiflag[currRowIndex] == undefined) {
		var keyCode = event.keyCode ? event.keyCode : event.charCode;
		if (keyCode != 40) {
			if (keyCode != 38) {
				var oAutoComp = new YAHOO.widget.AutoComplete(fieldObj, divElementName, yahooArrayObject);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = maxResultsDisplayed;
				oAutoComp.useIFrame = true;
			}
		}
		yuiflag[currRowIndex] = 1;
	}
}
function markYuiflagUndefined(currRowIndex) {
	yuiflag[currRowIndex] = undefined;
}
/* this checks partivular record exists or not */
function isRecordExists(url, tableName, whereClause, fieldObj) {
	var value = document.getElementById(fieldObj).value;
	var columnname = document.getElementById(fieldObj).name;
	whereClause = whereClause.replace("#1", value);
	if (url != "" && tableName != "" && whereClause != "") {
		var link = "" + url + "?tableName=" + tableName + "&whereClause=" + whereClause + " ";
		var request = initiateRequest();
		request.onreadystatechange = function () {
			if (request.readyState == 4) {
				if (request.status == 200) {
					var response = request.responseText.split("^");
					if (response[0] == "false") {
						alert(columnname + " already Selected. ");
						document.getElementById(fieldObj).value = "";
						document.getElementById(fieldObj).focus();
					}
				}
			}
		};
		request.open("GET", link, true);
		request.send(null);
	}
}
/* This function populates a combo-box (<select> data ) based on the input's given */
function loadSyncronizedSelectData(url, tablename, columnname1, columnname2, whereclause, sourceobj, destobj) {
	var value = document.getElementById(sourceobj).value;
	if (value != "" && value != " ") {
		whereclause = whereclause.replace("#1", value);
		if (url != "" && tablename != "" && columnname1 != "" && columnname2 != "" && whereclause != "") {
			var link = "" + url + "?tablename=" + tablename + "&columnname1=" + columnname1 + "&columnname2=" + columnname2 + "&whereclause=" + whereclause + " ";
			var request = initiateRequest();
			request.onreadystatechange = function () {
				if (request.readyState == 4) {
					if (request.status == 200) {
						var response = request.responseText.split("^");
						var id = response[0].split("+");
						var name = response[1].split("+");
						var comboObj = document.getElementById(destobj);
						comboObj.options.length = 0;
						comboObj.options[0] = new Option("-----choose----", "");
						for (var i = 1; i <= id.length; i++) {
							comboObj.options[i] = new Option(name[i - 1], id[i - 1]);
						}
					}
				}
			};
			request.open("GET", link, false);
			request.send(null);
		}
	} else {
		var comboObj = document.getElementById(destobj);
		comboObj.options.length = 0;
	}
}
/*
loadSelectDataForThreeCols method use to show display in the dropdown as columname2-Columnname3
*/
function loadSelectDataForThreeCols(url, tablename, columnname1, columnname2, columnname3, whereclause, sourceobj, destobj) {
	var value = document.getElementById(sourceobj).value;
	if (value != "" && value != " ") {
		whereclause = whereclause.replace("#1", value);
		if (url != "" && tablename != "" && columnname1 != "" && columnname2 != "" && columnname3 != "" && whereclause != "") {
			var link = "" + url + "?tablename=" + tablename + "&columnname1=" + columnname1 + "&columnname2=" + columnname2 + "&columnname3=" + columnname3 + "&whereclause=" + whereclause + " ";
			var request = initiateRequest();
			request.open("GET", link, false);
			request.send(null);
			if (request.status == 200) {
				var response = request.responseText.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
				var comboObj = document.getElementById(destobj);
				comboObj.options.length = 0;
				comboObj.options[0] = new Option("--Choose--", "");
				for (var i = 1; i <= id.length; i++) {
					comboObj.options[i] = new Option(name[i - 1], id[i - 1]);
				}
			}
		}
	} else {
		var comboObj = document.getElementById(destobj);
		comboObj.options.length = 0;
	}
}
function trimAll(strValue) {
	var objRegExp = /^(\s*)$/;
      // alert("strValue"+strValue);

      //check for all spaces
	if (objRegExp.test(strValue)) {
		strValue = strValue.replace(objRegExp, "");
        // alert("strValue-------"+strValue);
		if (strValue.length == 0) {
			return strValue;
		}
	}

      //check for leading & trailing spaces
	objRegExp = /^(\s*)([\W\w]*)(\b\s*$)/;
	if (objRegExp.test(strValue)) {
         //remove leading and trailing whitespace characters
		strValue = strValue.replace(objRegExp, "$2");
	}
	return strValue;
}

/* This function populates a combo box of non-history data (<select> data ) based on the inputs given */
function loadNonHistorySelectedData(url, tablename, columnname1, columnname2, whereclause, sourceobj, destobj) {
	var value = document.getElementById(sourceobj).value;
	if (value != "" && value != " ") {
		whereclause = whereclause.replace("#1", value);
		whereclause = whereclause.replace("#2", "'N'");
		if (url != "" && tablename != "" && columnname1 != "" && columnname2 != "" && whereclause != "") {
			var link = "" + url + "?tablename=" + tablename + "&columnname1=" + columnname1 + "&columnname2=" + columnname2 + "&whereclause=" + whereclause + " ";
			var request = initiateRequest();
			request.open("GET", link, false);
			request.send(null);
			if (request.status == 200) {
				var response = request.responseText.split("^");
				var id = response[0].split("+");
				var name = response[1].split("+");
				var comboObj = document.getElementById(destobj);
				comboObj.options.length = 0;
				comboObj.options[0] = new Option("--Choose--", "");
				for (var i = 1; i <= id.length; i++) {
					comboObj.options[i] = new Option(name[i - 1], id[i - 1]);
				}
			}
		}
	} else {
		var comboObj = document.getElementById(destobj);
		comboObj.options.length = 0;
	}
}

