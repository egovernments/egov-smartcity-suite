/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
var defaultAccCode;
var initialNetPayGLCode;
function selectDefaultAccountCode(){
	var accountCodeList=document.getElementById('chartOfAccounts_glConetPay');
	accountCodesLen=accountCodeList.options.length;
	//bootbox.alert('accountCodesLen='+accountCodesLen+',defaultAccCode='+defaultAccCode);	
	var i=0;
	while(i<accountCodesLen){	
		if(accountCodeList.options[i].text == defaultAccCode){	
			accountCodeList.selectedIndex=i;
			break;
		}
		i++;
	}
	setTimeout(updateAccountDescription,500);
}

function updateAccountDescription(){
	var accountCodeList = document.getElementById('chartOfAccounts_glConetPay');
	PageManager.DataService.setQueryField("parent",accountCodeList.options[accountCodeList.selectedIndex].value);
	PageManager.DataService.callDataService('getAccountDescription');
}

function clearCombo(id){
	var combo = document.getElementById(id);
	for(var i = combo.options.length-1; i>=0 ;i--){
		combo.remove(i);
	}
}

function getschemelist(obj){
	var opt = obj.value;
	var obj = document.getElementById('scheme');
	clearCombo('scheme');
	clearCombo('subscheme');
	obj.removeAttribute('exilListSource');
	obj.setAttribute('exilListSource','schemelist');
	PageManager.DataService.setQueryField("fundId",opt);
	PageManager.DataService.callDataService("schemelist");
}

function getsubschemelist(obj){
	var opt = obj.value;
	var obj = document.getElementById('subscheme');
	obj.removeAttribute('exilListSource');
	obj.setAttribute('exilListSource','subschemelist');
	PageManager.DataService.setQueryField("schemeId",opt);
	PageManager.DataService.callDataService("subschemelist");
}

function calculateTotal(){
	var tNetPayObj = document.getElementById('netPay');
	var tNetPayTotalObj = document.getElementById('netTotalPay');
	var ernAmt = parseFloat(document.getElementById('ernTotalAmount').value);
	var dedAmt = parseFloat(document.getElementById('dedTotalAmount').value);
	if(isNaN(ernAmt)) ernAmt=0;
	if(isNaN(dedAmt)) dedAmt=0;
	var result = ernAmt-dedAmt;
	if(!isNaN(result)){
		result = Math.round(result*100)/100 ;
		tNetPayObj.value = result;
		tNetPayTotalObj.value = result;
		document.getElementById('netPayAmount').value = result;
		document.getElementById('netTotalPay').value = result;
	}
}


function validateSubledgerCodes() {
	if (!CalculateRowTotal("earningTable", "entities_grid", "", "earningAmount"))
		return false;
	if (!CalculateRowTotal("deductionTable", "entitiesNew_grid", "new_",
			"deductionAmount"))
		return false;
	if (!CalculateRowTotal("netPayTable", "entitiesNewPay_grid", "newPay_",
			"netPay"))
		return false;
	else
		return true;
}

var eg_userId = "";
var booleanA = true;

/*
 * // this is added to rowDetailsNew.js function
 * CalculateRowTotal(maintab,entitytab,prefix,columnname) { var gltable=
 * document.getElementById(maintab); //var gltable=
 * document.getElementById(gridName);
 * 
 * var entitygltable= document.getElementById(entitytab); //var entitygltable=
 * document.getElementById(gn);
 * 
 * var entitytotal1 =0 ; var val ;
 * 
 * for(var i =1 ; i<gltable.rows.length;i++) { entitytotal1 =0 ; var dedAmount1 =
 * PageManager.DataService.getControlInBranch(gltable.rows[i],columnname).value;
 * //bootbox.alert(dedAmount1); if(dedAmount1 =='undefined' || dedAmount1=="")
 * dedAmount1 =0; //bootbox.alert("Amount in Row is: "+i+" "+dedAmount1 );
 * //if(eval(dedAmount1) > 0) { //for(var j =2 ; j<entitygltable.rows.length;j++)
 * //bootbox.alert("Table Length of "+maintab+" " +entitygltable.rows.length); for(var j
 * =1 ; j<entitygltable.rows.length;j++) {
 * 
 * //bootbox.alert("hi"); val =
 * PageManager.DataService.getControlInBranch(entitygltable.rows[j],prefix+"grid_amount");
 * 
 * if(val!=null) { var rowIndex1 =
 * PageManager.DataService.getControlInBranch(entitygltable.rows[j],prefix+"grid_rowIndex").value;
 * if(rowIndex1 == i && eval(val.value)>0) { entitytotal1 = eval(entitytotal1)+
 * eval(val.value) ; // bootbox.alert(eval(entitytotal1));
 *  }
 *  } } // bootbox.alert(eval(dedAmount1)); // bootbox.alert(eval(entitytotal1));
 *  // if only payee details entered and amount is not entered
 * if(eval(dedAmount1)<eval(entitytotal1)) { //bootbox.alert("got u"); bootbox.alert(" amount
 * should be equal to the total payee amount in Row "+ i);
 * 
 * PageManager.DataService.getControlInBranch(gltable.rows[i],columnname).focus();
 * return false; }
 *  }
 *  // Both Total matches then call calTotal & calNetPayment
 * 
 * if(eval(dedAmount1) == eval(entitytotal1)) { //bootbox.alert("Payees amount is equal
 * to the Total Amoount"); // var obj =
 * PageManager.DataService.getControlInBranch(gltable.rows[i],columname); return
 * true; //return true;
 *  } else if(dedAmount1 !='' && eval(dedAmount1)>0 && eval(entitytotal1)!=0 &&
 * entitytotal1!= "-0" ) { if(eval(dedAmount1) > eval(entitytotal1) ||
 * eval(dedAmount1) < eval(entitytotal1) && entitytotal1!= "-0") { bootbox.alert("Payees
 * amount should be equal to the total amount in Row "+ i);
 * 
 * PageManager.DataService.getControlInBranch(gltable.rows[i],columnname).focus();
 * return false; } } }
 * 
 * return true; }
 */

function afterRefreshPage(dc) {

	
	var sm = PageManager.DataService.getQueryField('showMode');
	
	if(sm == "view" || sm == "modify"){
			if(dc.grids['netPayTable']) {
				defaultAccCode=dc.grids['netPayTable'][1][0]
				initialNetPayGLCode = defaultAccCode;
				setTimeout(selectDefaultAccountCode,500);		
			}
		calculateEarningTotal();
		calculateDeductionTotal();

		}

	if (dc.values['serviceID'] == 'getTableName')

	{
		var billId = PageManager.DataService.getQueryField('billId');
		var deptId = PageManager.DataService.getQueryField('departmentId');

		if (billId && (sm == "view")) 
		{
			document.getElementById('modeOfExec').value = "view";
			isDrillDown = true;

			document.getElementById("department_id").setAttribute('exilListSource', "jvsal_department");
			PageManager.ListService.callListService();

			document.getElementById("hideCol1").style.display = "block";
			document.getElementById("hideCol2").style.display = "block";
			document.getElementById("hideCol3").style.display = "none";

			PageManager.DataService.setQueryField('billId', billId);
			PageManager.DataService.setQueryField('deptId', deptId);
			PageManager.DataService.setQueryField("table",dc.values['TABLENAME']);
			PageManager.DataService.setQueryField("salaryBillPurposeIds", getPurposeIds());
			PageManager.DataService.callDataService('getSalBillRegSearchDetails');

			document.getElementById('submitGrid').style.display = "none";
			document.getElementById('reverseSubmit').style.display = "none";
			document.getElementById('backbutton').style.display = 'block';
			for ( var i = 0; i < document.forms[0].length; i++) {
				if (document.forms[0].elements[i].value != " Back ") {
					document.forms[0].elements[i].disabled = true;
				}
			}
		}
		else if (billId && (sm == "modify")) {
			document.getElementById('modeOfExec').value = "modify";
			isDrillDown = true;
			document.getElementById("hideCol1").style.display = "block";
			document.getElementById("hideCol2").style.display = "block";
			document.getElementById("hideCol3").style.display = "none";

			PageManager.DataService.setQueryField('billId', billId);
			PageManager.DataService.setQueryField('deptId', deptId);
			PageManager.DataService.setQueryField("table",dc.values['TABLENAME']);
			PageManager.DataService.setQueryField("salaryBillPurposeIds", getPurposeIds());
			PageManager.DataService.callDataService('getSalBillRegSearchDetails');

			document.getElementById('submitGrid').style.display = "none";
			document.getElementById('backbutton').style.display = 'none';
			document.getElementById('reverseSubmit').style.display = "block";
			// disable all controles of netPayGrid
			disableControls(0, false);
			document.getElementById('salary_accountHeadnetPay0').disabled = true;
			document.getElementById('netPay').disabled = true;
		}
		else 
		{
			//bootbox.alert('here');
			document.getElementById('modeOfExec').value = "new";
			PageManager.DataService.setQueryField("keyValue",defaultAccountCode);
			PageManager.DataService.callDataService('getDefaultAccountCode');

			document.getElementById('backbutton').style.display = "none";
			document.getElementById('reverseSubmit').style.display = "none";

			// This callDataService is used for getting the current database
			// date
			PageManager.DataService.callDataService('getCurrentDatabaseDate');
			PageManager.DataService.setQueryField("table",dc.values['TABLENAME']);
			PageManager.DataService.callDataService('getSalaryGridDetails');
		}
		// for yui
		loadDropDownCodes();
		loadDropDownFuncNames();
		loadDropDownDeductionCodes();

		var earnTable = document.getElementById("earningTable");
		var dedTable = document.getElementById("deductionTable");
	}

	//bootbox.alert("Inside afterRefreshPage"+sm);
	if (dc.values['serviceID'] = 'getDefaultAccountCode' && (typeof(dc.values['defaultAccountCode']) != "undefined" && sm == "new")) {
		defaultAccCode = dc.values['defaultAccountCode'];
	}

	eg_userId = dc.values['egUser_id']
	if (eg_userId && eg_userId.length != 0) {
		if (booleanA && (sm == "modify" || sm == "new")) {
			PageManager.DataService.setQueryField('eg_userId', eg_userId);
			PageManager.DataService.callDataService("jvsal_departmentNew");
			document.getElementById("department_id").setAttribute('exilListSource', "jvsal_departmentNew");
			booleanA = false;
			document.getElementById('chartOfAccounts_glConetPay').disabled = false;
			if (sm == "new"){
				setTimeout(selectDefaultAccountCode, 500);
			}
		}
	}

	if (dc.values['serviceID'] == 'getCurrentDatabaseDate') {
		var dt = dc.values['databaseDate']
		document.getElementById('salaryBill_Date').value = dt;
	}

	var billdate = dc.values['salaryBill_Date'];
	if (billdate) {
		billdate = formatDate2(billdate);
		document.getElementById('salaryBill_Date').value = billdate;
	}

	//var oldbilldate=dc.values['oldbill_Date'];
	var oldbilldate = dc.values['salaryBill_Date'];
	if (oldbilldate) {
		oldbilldate = formatDate2(oldbilldate);
		document.getElementById('oldbill_Date').value = oldbilldate;
	}
	if (dc.values['serviceID'] == 'getSalBillRegSearchDetails') {
		calculateEarningTotal();
		calculateDeductionTotal();

	}
	//if(dc.values['serviceID']=='getSalaryGridDetails' && sm=="new")
	if (dc.values['serviceID'] == 'getSalaryGridDetails') { //bootbox.alert("Inside New");
		var earnTable = document.getElementById("earningTable");
		var dedTable = document.getElementById("deductionTable");
		for ( var i = 1; i < earnTable.rows.length; i++) {
			PageManager.DataService.getControlInBranch(earnTable.rows[i],
					"salary_accountHeadEar").disabled = "true";
			PageManager.DataService.getControlInBranch(earnTable.rows[i],
					"chartOfAccounts_glCodeearning").disabled = "true";
		}
		for ( var i = 1; i < dedTable.rows.length; i++) {
			PageManager.DataService.getControlInBranch(dedTable.rows[i],
					"salary_accountHeadDed").disabled = "true";
			PageManager.DataService.getControlInBranch(dedTable.rows[i],
					"chartOfAccounts_glCodeduction").disabled = "true";
		}
	}
	if (dc.values['serviceID'] = 'getAccountDescription' && (sm == "new" || sm == "modify")) {
		document.getElementById('chartOfAccounts_namenetPay').value = dc.values['description'];
	}
	if (dc.values['serviceID'] == 'getSalBillRegSearchDetails'
			&& sm == "modify") {
		var earnTable = document.getElementById("earningTable");
		var dedTable = document.getElementById("deductionTable");
		for ( var i = 1; i < earnTable.rows.length; i++) {
			//bootbox.alert(PageManager.DataService.getControlInBranch(earnTable.rows[i],"salary_CodeIdEar").value) ;
			if (PageManager.DataService.getControlInBranch(earnTable.rows[i],
					"salary_CodeIdEar").value != "")
				PageManager.DataService.getControlInBranch(earnTable.rows[i],
						"chartOfAccounts_glCodeearning").disabled = "true";
		}
		for ( var i = 1; i < dedTable.rows.length; i++) { //bootbox.alert(PageManager.DataService.getControlInBranch(dedTable.rows[i],"salary_CodeIdDed").value);
			if (PageManager.DataService.getControlInBranch(dedTable.rows[i],
					"salary_CodeIdDed").value != "")
				PageManager.DataService.getControlInBranch(dedTable.rows[i],
						"chartOfAccounts_glCodeduction").disabled = "true";
		}
	}
	if (dc.values['serviceID'] == 'getSalBillRegSearchDetails' && sm == "view") { //bootbox.alert("Inside View");
		// disableControls(0,true);
		for ( var i = 0; i < document.forms[0].length; i++) {
			if (document.forms[0].elements[i].value != " Back ") {
				document.forms[0].elements[i].disabled = true;
			}
		}
	}

	// Set the salary bill status based on the statusid in eg_billregister

	var salBillStatus = dc.values['salaryBill_status'];
	if (salBillStatus == '28')
		document.getElementById('salaryBill_status').value = "CREATED";
	else if (salBillStatus == '29')
		document.getElementById('salaryBill_status').value = "APPROVED";
	else if (salBillStatus == '30')
		document.getElementById('salaryBill_status').value = "PASSED";
	else if (salBillStatus == '31')
		document.getElementById('salaryBill_status').value = "PAID";

	if (sm == "modify" ) 
	{
		if (dc.grids['deductionTable'] == null || dc.grids['deductionTable'].length == 0	|| dc.grids['deductionTable'].length == 1) 
		{
			//bootbox.alert("inside the length  0 ");
			PageManager.DataService.addNewRow('deductionTable');

			// PageManager.DataService.addNewRow('deductionTable');

		}
	}
	if (dc.values['serviceID'] == 'getSalBillRegSearchDetails' && sm == "view") {
		if (dc.grids['deductionTable'].length == 0 || dc.grids['deductionTable'].length == 1) {
			//bootbox.alert("inside the length  0 ");
			PageManager.DataService.addNewRow('deductionTable');

			// PageManager.DataService.addNewRow('deductionTable');
			//disableControls(0,true);
			for ( var i = 0; i < document.forms[0].length; i++) {
				if (document.forms[0].elements[i].value != " Back ") {
					document.forms[0].elements[i].disabled = true;
				}
			}

		}
	}

} // after refresh
 


function beforeRefreshPage(dc) {
	if (dc.values['serviceID'] == 'getSalBillRegSearchDetails') {
		dc = createRowIndexForGrid(dc, 'deductionTable', 'entitiesNew_grid',
				'New_');
		dc = createRowIndexForGrid(dc, 'earningTable', 'entities_grid', '');
		dc = createRowIndexForGrid(dc, 'netPayTable', 'entitiesNewPay_grid',
				'NewPay_');
	}
}

function afterUpdateService(dc) {
	disableControls(0, true);
	if (str == "new")
		window.location = "SalaryBillRegister_VMC.jsp?showMode=new";
	if (str == "close")
		window.close();

}
function onloadTasks() {
	var sm = PageManager.DataService.getQueryField('showMode');
	var billId = PageManager.DataService.getQueryField('billId');
	var deptId = PageManager.DataService.getQueryField('departmentId');
	
	PageManager.DataService.setQueryField("salaryBillPurposeIds", getPurposeIds());
	PageManager.ListService.callListService();
	// PageManager.DataService.callDataService('getSalaryGridDetails');
	PageManager.DataService.addNewRow('earningTable');
	PageManager.DataService.addNewRow('deductionTable');
	PageManager.DataService.setQueryField("name", "Employee");
	PageManager.DataService.callDataService("getTableName");
	
	//the below code is commented it is put in the afterrefreshpage method since the payee details must be fetched dynamically 
	/*if(billId && (sm=="view"))
		{
			document.getElementById('modeOfExec').value="view";
		document.getElementById('screenName').innerHTML="Salary Bill Register  -View";
		
		isDrillDown=true;
		
		document.getElementById("department_id").setAttribute('exilListSource',"jvsal_department");
		PageManager.ListService.callListService();
				
		document.getElementById("hideCol1").style.display="block";
		document.getElementById("hideCol2").style.display="block";
		
		PageManager.DataService.setQueryField('billId',billId);
		PageManager.DataService.setQueryField('deptId',deptId);
		PageManager.DataService.callDataService('getSalBillRegSearchDetails');

		document.getElementById('submitGrid').style.display="none";
		document.getElementById('reverseSubmit').style.display ="none";
		document.getElementById('backbutton').style.display='block';
		disableControls(0,true);
		
		}
		
		else if(billId && (sm=="modify"))
		{
			document.getElementById('modeOfExec').value="modify";
			document.getElementById('screenName').innerHTML="Salary Bill Register  -Modify";
			isDrillDown=true;
			
			//document.getElementById("salaryBill_Number").focus();
			document.getElementById("hideCol1").style.display="block";
		document.getElementById("hideCol2").style.display="block";
			
		PageManager.DataService.setQueryField('billId',billId);
		PageManager.DataService.setQueryField('deptId',deptId);
		
		PageManager.DataService.callDataService('getSalBillRegSearchDetails');
	
		document.getElementById('submitGrid').style.display="none";
		document.getElementById('backbutton').style.display='none';
		document.getElementById('reverseSubmit').style.display ="block";
		disableControls(0,false);
		}
		
	else			
	{
	//
	document.getElementById('modeOfExec').value="new";
	document.getElementById('screenName').innerHTML="Salary Bill Register  -Create";
	//document.getElementById("salaryBill_Number").focus();
		
	//document.getElementById('statusInfo').style.display ="none";
	document.getElementById('backbutton').style.display="none";
	document.getElementById('reverseSubmit').style.display ="none";
	
	// This callDataService is used for getting the current database date
	PageManager.DataService.callDataService('getCurrentDatabaseDate');
	PageManager.DataService.callDataService('getSalaryGridDetails');
	}

	// for yui
	loadDropDownCodes();
	loadDropDownFuncNames();
	loadDropDownDeductionCodes();
	
	var earnTable= document.getElementById("earningTable");
	var dedTable= document.getElementById("deductionTable");
	//bootbox.alert(earnTable.rows.length);
	//bootbox.alert(dedTable.rows.length);*/

}

function ButtonPress(name) {
	//bootbox.alert(document.getElementById('modeOfExec').value);
	if (name.toLowerCase() == 'savenew')
		str = "new";
	if (name.toLowerCase() == 'saveclose')
		str = "close";

	if (!isValidUser(2, CookieManager.getCookie('userRole')))
		return false;

	document.getElementById('egUser_id').value = CookieManager
			.getCookie('currentUserId');
	/*
	if(document.getElementById("salaryBill_Number").value == 0)
	{
	bootbox.alert("Please Enter Bill Number");
	return;
	}
	 */
	if (document.getElementById('salaryBill_Date').value == 0) {
		bootbox.alert("Please Enter The Bill Date");
		return;
	}

	if (document.getElementById("month_id").value == 0) {
		bootbox.alert("Please Select Month ");
		return;
	}

	if (document.getElementById('financialYear_id').value == 0) {
		bootbox.alert("Please Select The Financial Year");
		return;
	}
	/*
	if(document.getElementById("field_name").value == 0)
	{
		bootbox.alert("Please Select The Field");
		return;
	}
	 */

	if (document.getElementById('department_id').value == 0) {
		bootbox.alert("Please Select The Department");
		return;
	}

	if (PageValidator.validateForm()) {
		//if(!checkDuplicates("earningTable","chartOfAccounts_glCodeearning")) return;
		//if(!checkDuplicatesDeduction("deductionTable","chartOfAccounts_glCodeduction")) return;
		//if(!checkDuplicatesAmongTables())return;

		var netPay = document.getElementById('netPay');
		if (isNaN(netPay.value) || netPay.value <= 0) {
			bootbox.alert("Entries Invalid:Net Pay Can't be Less than or equal to zero");
			return;
		}

		if (isRowsEmpty())
			return;
		if (isDedRowsEmpty())
			return;
		if (!validateSubledgerCodes())
			return true;
		;
		//  validateControleCodes(dc,"earningTable","entity_new_grid","2","earningTable")
		disableControls(0, false);

	}
	
	PageManager.UpdateService.submitForm('jvSalaryBillRegister');

}// ButtonPress Method

function loadDropDownCodes() {

	var type = 'getAllCoaCodes';
	var url = "/EGF/voucher/common-ajaxGetAllCoaCodes.action";
	var req2 = initiateRequest();
	req2.onreadystatechange = function() {
		if (req2.readyState == 4) {
			if (req2.status == 200) {
				var codes2 = req2.responseText;
				var a = codes2.split("^");
				var codes = a[0];
				acccodeArray = codes.split("+");
				codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);

			}
		}
	};
	req2.open("GET", url, true);
	req2.send(null);
}
function loadDropDownFuncNames() {

	var type = 'getAllFunctionName';
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

function loadDropDownDeductionCodes() {

	var type = 'getAllCoaCodes';
	var url = "/EGF/voucher/common-ajaxGetAllCoaCodes.action";
	var req2 = initiateRequest();
	req2.onreadystatechange = function() {
		if (req2.readyState == 4) {
			if (req2.status == 200) {
				var codes2 = req2.responseText;
				var a = codes2.split("^");
				var codes = a[0];
				dedAcccodeArray = codes.split("+");
				dedCodeObj = new YAHOO.widget.DS_JSArray(dedAcccodeArray);

			}
		}
	};
	req2.open("GET", url, true);
	req2.send(null);
}
function calculateEarningTotal() {
	var table = document.getElementById("earningTable");
	var tAmountObj;
	var tAmount = 0;
	tAmountObj = document.getElementsByName('earningAmount');
	for ( var i = 0; i < tAmountObj.length; i++) {
		temp = parseFloat(tAmountObj[i].value);
		if (!isNaN(temp))
			tAmount += temp;
	}
	tAmountObj = document.getElementById('ernTotalAmount');
	if (!isNaN(tAmount))
		tAmountObj.value = Math.round(tAmount * 100) / 100;
	calculateTotal();
}

function calculateDeductionTotal() {
	var table = document.getElementById("deductionTable");
	var tAmountObj;
	var tAmount = 0;
	tAmountObj = document.getElementsByName('deductionAmount');
	for ( var i = 0; i < tAmountObj.length; i++) {
		temp = parseFloat(tAmountObj[i].value);
		if (!isNaN(temp))
			tAmount += temp;
	}
	tAmountObj = document.getElementById('dedTotalAmount');
	if (!isNaN(tAmount)) {
		tAmount = Math.round(tAmount * 100) / 100;
		tAmountObj.value = tAmount;
	}
	calculateTotal();
}

/* Check the DispersementType for details grid*/
function checkDispersementType(obj) {
	var mode = PageManager.DataService.getQueryField("showMode");

	//	bootbox.alert("calling ");
	openDetails(obj, 'chartOfAccounts_glCodeduction', mode, 'entitiesNew_grid',
			'new_');

}
/* Check the DispersementType for deduction grid*/
function checkDispersementTypeDed(obj) {
	var mode = PageManager.DataService.getQueryField("showMode");

	//bootbox.alert("calling ");

	openDetails(obj, 'chartOfAccounts_glCodeearning', mode, 'entities_grid', '')

}

function funClear(obj, tableNo) {
	if (tableNo == 1) {
		var v = PageManager.DataService.getControlInBranch(
				obj.parentNode.parentNode, 'function_code')
		var w = PageManager.DataService.getControlInBranch(
				obj.parentNode.parentNode, 'cv_fromFunctionCodeId')

		if (v.value == "") {
			v.value = "";
			w.value = "";
		}

	}

	if (tableNo == 2) {
		var v = PageManager.DataService.getControlInBranch(
				obj.parentNode.parentNode, 'function_code1')
		var w = PageManager.DataService.getControlInBranch(
				obj.parentNode.parentNode, 'cv_fromFunctionCodeId1')

		if (v.value == "") {
			v.value = "";
			w.value = "";
		}

	}
}

function openSearch(obj, tableNo) {
	var a = new Array(2);
	var sRtn;
	if (tableNo == 1) {
		sRtn = showModalDialog("../Search.html?filterServiceID=9", "",
				"dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");

	} else if (tableNo == 2) {
		sRtn = showModalDialog("../Search.html?filterServiceID=10", "",
				"dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	}

	if (sRtn != '') {
		a = sRtn.split("`~`");
		// bootbox.alert(a);
		var x, y, z;
		if (tableNo == 1) {
			x = PageManager.DataService.getControlInBranch(
					obj.parentNode.parentNode, 'chartOfAccounts_glCodeearning');
			y = PageManager.DataService.getControlInBranch(
					obj.parentNode.parentNode.parentNode,
					'chartOfAccounts_nameearning');
			z = PageManager.DataService.getControlInBranch(
					obj.parentNode.parentNode.parentNode, 'glCodeId_Earning');
			//bootbox.alert(document.getElementById('glCodeId_Earning').value);	

		} else {
			x = PageManager.DataService.getControlInBranch(obj.parentNode,
					'chartOfAccounts_glCodeduction');
			y = PageManager.DataService.getControlInBranch(
					obj.parentNode.parentNode.parentNode,
					'chartOfAccounts_namededuction');
			z = PageManager.DataService.getControlInBranch(
					obj.parentNode.parentNode.parentNode, 'glCodeId_Ded');
			//bootbox.alert(document.getElementById('glCodeId_Ded').value);
		}
		x.value = a[0];
		y.value = a[1];
		z.value = a[2];

		//bootbox.alert(a[0]);
		// bootbox.alert(a[1]);
		// bootbox.alert(a[2]);
	}
}

function openSearch1(obj, tableName, tableNo) {
	var a = new Array(2);
	var sRtn;
	var str = "";
	if (tableName.toLowerCase() == 'function') {
		str = "../Search.html?tableNameForCode=function";

		sRtn = showModalDialog(str, "",
				"dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
		if (sRtn != '') {
			a = sRtn.split("`~`");

			if (tableName.toLowerCase() == 'function' && tableNo == 1) {
				//bootbox.alert("a 0 value= "+a[0]+" a 1 value = " +a[1]+" a 2 value ="+a[2]);
				var y = PageManager.DataService.getControlInBranch(
						obj.parentNode, 'function_code');
				var x = PageManager.DataService.getControlInBranch(
						obj.parentNode, 'cv_fromFunctionCodeId');

			}

			else {
				var y = PageManager.DataService.getControlInBranch(
						obj.parentNode, 'function_code1');
				var x = PageManager.DataService.getControlInBranch(
						obj.parentNode, 'cv_fromFunctionCodeId1');

			}
			y.value = a[1];
			x.value = a[2];
			document.getElementById('fnctionCodeId').value = a[2];
			//bootbox.alert(document.getElementById('fnctionCodeId').value);
			//bootbox.alert(a[1]);
			//bootbox.alert(a[2]);
		}
	}
}

function checkDuplicatesAmongTables() {
	var earTable = document.getElementById("earningTable");
	var dedTable = document.getElementById("deductionTable");
	var row1Data, rowData;
	for ( var i = 1; i < earTable.rows.length; i++) {
		row1Data = PageManager.DataService.getControlInBranch(earTable.rows[i],
				"chartOfAccounts_glCodeearning");
		for ( var j = 1; j < dedTable.rows.length; j++) {
			rowData = PageManager.DataService.getControlInBranch(
					dedTable.rows[j], "chartOfAccounts_glCodeduction");
			if ((row1Data.value.toLowerCase() == rowData.value.toLowerCase())
					&& rowData.value.length > 0) {
				bootbox.alert("Record "
						+ j
						+ " Account Code Of Deductions  Cannot be Same as Record "
						+ i + " of Earnings");
				rowData.value = "";
				rowData.focus();
				return false;
			}
		}
	}
	return true;
}

function isRowsEmpty() {
	var table = document.getElementById("earningTable");
	var tCodeObj, tAmtObj;
	for ( var i = 1; i < table.rows.length; i++) {
		tCodeObj = PageManager.DataService.getControlInBranch(table.rows[i],
				'chartOfAccounts_glCodeearning');
		tAmtObj = PageManager.DataService.getControlInBranch(table.rows[i],
				'earningAmount');
		/*
		if(tCodeObj.value.length>0 && tAmtObj.value.length==0){
		  bootbox.alert("Fill the Amount For Earnings");
		  tAmtObj.focus();
		  return true;
		}
		 */

		if (tCodeObj.value.length == 0 && tAmtObj.value.length > 0) {
			bootbox.alert("Fill the Account Code For Earnings Otherwise Amount make it as empty");
			tCodeObj.focus();
			return true;
		}

		// This is for if added any extra Empty Rows
		/*
		if(tCodeObj.value.length==0 && tAmtObj.value.length==0){
		  bootbox.alert("Fill the Code For Earnings");
		  tCodeObj.focus();
		  return true;
		}
		 */
	}

	var tAmount = document.getElementById('ernTotalAmount').value;
	var netPayGLCode = document.getElementById('chartOfAccounts_glConetPay')
	if (netPayGLCode.options[netPayGLCode.selectedIndex].text == initialNetPayGLCode && (isNaN(tAmount) || parseInt(tAmount) <= 0 || tAmount.length == 0)) {
		bootbox.alert("No  Data To Post");
		// tCodeObj=document.getElementById('chartOfAccounts_glCodeearning');
		// tCodeObj.focus();
		return true;
	}
	return false;
}

function isDedRowsEmpty() {
	var table = document.getElementById("deductionTable");
	var tCodeObj, tAmtObj;
	for ( var i = 1; i < table.rows.length; i++) {
		tCodeObj = PageManager.DataService.getControlInBranch(table.rows[i],
				'chartOfAccounts_glCodeduction');
		tAmtObj = PageManager.DataService.getControlInBranch(table.rows[i],
				'deductionAmount');
		/*
		if(tCodeObj.value.length>0 && tAmtObj.value.length==0){
		  bootbox.alert("Fill the Amount For Deductions");
		  tAmtObj.focus();
		  return true;
		}
		 */

		if (tCodeObj.value.length == 0 && tAmtObj.value.length > 0) {
			bootbox.alert("Fill the Account Code For Deductions Otherwise Amount make it as empty");
			tCodeObj.focus();
			return true;
		}

		// This is for if added any extra Empty Rows
		/*
		if(tCodeObj.value.length==0 && tAmtObj.value.length==0){
				  bootbox.alert("Fill the Code For Deductions");
				  tCodeObj.focus();
				  return true;
		}
		 */
	}
	return false;
}

function checkDuplicates(tableId, ctlToSearch) {
	var table = document.getElementById(tableId);
	var row1Data, rowData;
	for ( var i = 1; i < table.rows.length - 1; i++) {
		row1Data = PageManager.DataService.getControlInBranch(table.rows[i],
				ctlToSearch);
		for ( var j = i + 1; j < table.rows.length; j++) {
			rowData = PageManager.DataService.getControlInBranch(table.rows[j],
					ctlToSearch);
			if ((row1Data.value.toLowerCase() == rowData.value.toLowerCase())
					&& rowData.value.length > 0) {
				bootbox.alert("In Earnings Record " + j
						+ " Account Code  Cannot be Same as Record " + i);
				rowData.value = "";
				rowData.focus();
				return false;
			}
		}

	}
	return true;
}

function checkSalaryCode() {
	var table = document.getElementById("earningTable");
	var rowData;
	var foundCode = 0;
	for ( var i = 1; i < table.rows.length; i++) {
		rowData = PageManager.DataService.getControlInBranch(table.rows[i],
				"chartOfAccounts_glCodeearning");
		for ( var j = 0; j < g_salCodesRequired.length; j++) {
			if (rowData.value == g_salCodesRequired[j]) {
				foundCode++;
			}
		}
	}
	if (foundCode == 0) {
		bootbox.alert("please select salary code in earnings");
		return false;
	} else if (foundCode > 1) {
		bootbox.alert("select only one salary code");
		return false;
	}
	return true;
}

function disableControls(frmIndex, isDisable) {
	for ( var i = 0; i < document.forms[frmIndex].length; i++)
		document.forms[frmIndex].elements[i].disabled = isDisable;
}

function onClickCancel() {
	window.location = "SalaryBillRegister_VMC.jsp?showMode=new"
}

function hideButtons() {
	document.getElementById('modGrid').style.display = 'block';
	document.getElementById('submitGrid').style.display = "none";
}

function checkDuplicatesDeduction(tableId, ctlToSearch) {
	var table = document.getElementById(tableId);
	var row1Data, rowData;
	for ( var i = 1; i < table.rows.length - 1; i++) {
		row1Data = PageManager.DataService.getControlInBranch(table.rows[i],
				ctlToSearch);
		for ( var j = i + 1; j < table.rows.length; j++) {
			rowData = PageManager.DataService.getControlInBranch(table.rows[j],
					ctlToSearch);
			if ((row1Data.value.toLowerCase() == rowData.value.toLowerCase())
					&& rowData.value.length > 0) {
				if (!checkEntityDuplication(i, j, 'entities_grid', ''))
					continue;
				bootbox.alert("In Deductions Record " + j
						+ " Account Code  Cannot be Same as Record " + i);
				rowData.value = "";
				rowData.focus();
				return false;

			}
		}

	}
	return true;
}

function hideColumn(index) {
	var table = document.getElementById('entries');
	for ( var i = 0; i < table.rows.length; i++) {
		table.rows[i].cells[index].style.display = "none";
	}
}

function getAccNameEarn(obj, neibrObjName) {
	var temp = obj.value;
	temp = temp.split("`-`");
	obj.value = temp[0];

	//bootbox.alert(temp[0]);
	//bootbox.alert(temp[1]);
	//bootbox.alert(temp[2]);

	PageManager.DescService.onblur(neibrObjName);
	var currRow = PageManager.DataService.getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj = PageManager.DataService
			.getControlInBranch(currRow, neibrObjName);
	if (temp[1] == null)
		return;
	else {
		neibrObj.value = temp[1];
		PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,
				"glCodeId_Earning").value = temp[2];
		//bootbox.alert(PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,"glCodeId_Earning").value);
		//document.getElementById('glCodeId_Earning').value=temp[2];
	}
}

function getAccNameDed(obj, neibrObjName) {
	var temp = obj.value;
	temp = temp.split("`-`");
	obj.value = temp[0];

	//bootbox.alert(temp[0]);
	//bootbox.alert(temp[1]);
	//bootbox.alert(temp[2]);

	PageManager.DescService.onblur(neibrObjName);
	var currRow = PageManager.DataService.getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj = PageManager.DataService
			.getControlInBranch(currRow, neibrObjName);
	if (temp[1] == null)
		return;
	else {
		neibrObj.value = temp[1];
		PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,
				"glCodeId_Ded").value = temp[2];
		//bootbox.alert(PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,"glCodeId_Ded").value);
		//document.getElementById('glCodeId_Ded').value=temp[2];
	}
}

// added by raja for yui
function getFunId1(obj, neibrObjName) {
	PageManager.DescService.onblur(neibrObjName);
	var src = obj;
	var currRow = PageManager.DataService.getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj = PageManager.DataService
			.getControlInBranch(currRow, neibrObjName);
	//obj=PageManager.DataService.getControlInBranch(obj.parentNode.parentNode,"function_code");
	var temp = obj.value;
	temp = temp.split("`-`");

	obj.value = temp[0];
	if (temp[1] == null)
		return;
	else {
		neibrObj.value = temp[1];
		document.getElementById('fnctionCodeId').value = temp[1];
		//bootbox.alert(temp[0]);
		//bootbox.alert(temp[1]);
	}
	neibrObj.disabled = true;

}

/*
 We implement AJAX here. The remaining functions are present in resources/javascript/jsCommonMethods.js.
 Whenever user enters some value this function will be triggered.
 */
//(FOR EARNINGS---FUNCTION CODE)
// added by raja for yui
function autocompletecodeFunction(obj) {
	// set position of dropdown 
	var src = obj;
	var target = document.getElementById('codescontainer');
	//target.style.position="absolute";
	var posSrc = findPos(src);
	target.style.left = posSrc[0];
	target.style.top = posSrc[1] + 25;
	target.style.width = 200;

	var currRow = PageManager.DataService.getRow(obj);
	var coaCodeObj = obj;//PageManager.DataService.getControlInBranch(currRow,'function_code');
	//40 --> Down arrow, 38 --> Up arrow
	if (yuiflag[currRow.rowIndex] == undefined) {
		if (event.keyCode != 40) {
			if (event.keyCode != 38) {
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,
						'codescontainer', funcObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
			}
		}
		yuiflag[currRow.rowIndex] = 1;
	}
}

//(FOR EARNINGS---ACCOUNT CODE)
function autocompletecode(obj) {
	// set position of dropdown 
	var src = obj;
	var target = document.getElementById('codescontainer');
	var posSrc = findPos(src);
	target.style.left = posSrc[0];
	target.style.top = posSrc[1] + 25;
	target.style.width = 500;

	var currRow = PageManager.DataService.getRow(obj);
	var coaCodeObj = obj;//PageManager.DataService.getControlInBranch(currRow,'chartOfAccounts_glCodeearning');
	//40 --> Down arrow, 38 --> Up arrow
	if (yuiflag[currRow.rowIndex] == undefined) {
		if (event.keyCode != 40) {
			if (event.keyCode != 38) {
				var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,
						'codescontainer', codeObj);
				oAutoComp.queryDelay = 0;
				oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
				oAutoComp.useShadow = true;
				oAutoComp.maxResultsDisplayed = 15;
				oAutoComp.useIFrame = true;
			}
		}
		yuiflag[currRow.rowIndex] = 1;
	}

}
//(FOR DEDUCTIONS---FUNCTION CODE)

//(FOR DEDUCTIONS---ACCOUNT CODE)

function addNodes() {
	tbody1 = document.getElementById("entities_grid");
	tbody1 = tbody1.firstChild;
	newRow = tbody1.rows[1].cloneNode(true);
	tbody1.appendChild(newRow);

}

function fillDate1(objName) {
	PageValidator.showCalendar('selectedDate');
	document.getElementById(objName).value = document
			.getElementById('selectedDate').value;
	document.getElementById('selectedDate').value = "";
}

function checkfund() {
	var tempfund = document.getElementById("fund_id").value;
	if (tempfund == "")
		bootbox.alert("Select Fund First");
}

function checkscheme() {
	var tempfund = document.getElementById("scheme").value;
	//bootbox.alert(tempfund);
	if (tempfund == "")
		bootbox.alert("Select Scheme First");
}

function getPurposeIds()
{
	var temp= salaryBillPurposeIds.split(',');
	var purposeids='(';
	for(var k=0;k<temp.length;k++)
	{
		purposeids = purposeids+temp[k]+",";
	}
	purposeids=purposeids.substr(0,purposeids.length-1);
	purposeids = purposeids+")";
	return purposeids;
}
