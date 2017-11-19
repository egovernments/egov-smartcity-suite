<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<html>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<head>
<title>Bank Entries Not In Bank Book</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/bankEntriesNotInBankBookHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
</style>
<script>
	path = "${pageContext.request.contextPath}";
	var showMode = '<s:property value="showMode"/>';
	var totaldbamt = 0, totalcramt = 0;
	var OneFunctionCenter = <s:property value="isRestrictedtoOneFunctionCenter"/>;
	//bootbox.alert(">>.."+OneFunctionCenter);                 
	var glcodeOptions = [ {
		label : "--- Select ---",
		value : "0"
	} ];
	<s:iterator value="dropdownData.glcodeList">
	glcodeOptions.push({
		label : '<s:property value="glcode"/>'+'-'+'<s:property value="name"/>',
		value : '<s:property value="id"/>'
	})
	</s:iterator>
	var typeOptions = [ {
		label : "--- Select ---",
		value : "0"
	}, {
		label : 'Receipt',
		value : 'Receipt'
	}, {
		label : 'Payment',
		value : 'Payment'
	} ];

	var makeBankEntriesNotInBankBookTable = function() {
		var bankEntriesNotInBankBookColumns = [
				{
					key : "refNum",
					label : 'Ref No',
					formatter : createTextFieldFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, ".refNum")
				},
				{
					key : "beId",
					hidden : true,
					formatter : createHiddenFieldFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, ".beId")
				},
				{
					key : "type",
					label : 'Type  <span class="mandatory1">*</span>',
					formatter : createDropdownFormatterBENIBB(BANKENTRIESNOTINBANKBOOKLIST),
					dropdownOptions : typeOptions
				},
				{
					key : "date",
					label : 'Date <span class="mandatory1">*</span>',
					width:120,
					formatter : createDateFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, ".date")
				},
				{
					key : "amount",
					label : 'Amount <span class="mandatory1">*</span>',
					formatter : createAmountFieldFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, ".amount")
				},
				{
					key : "remarks",
					label : 'Remarks',
					formatter : createTextFieldFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, ".remarks")
				},
				{
					key : "glcodeIdDetail",
					hidden : true,
					formatter : createHiddenFieldFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, ".glcodeIdDetail")
				},
				{
					key : "glcodeDetail",
					label : 'Account Code <span class="mandatory1">*</span>',
					width:120,
					formatter : createDropdownFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, "loaddropdown(this)"),
					dropdownOptions : glcodeOptions
				},
				{
					key : "createVoucher",
					label : 'Create Voucher',
					formatter : createCheckBoxFormatterBENIBB(
							BANKENTRIESNOTINBANKBOOKLIST, ".createVoucher")
				},
				{
					key : 'Add',
					label : 'Add',
					formatter : createAddImageFormatter("${pageContext.request.contextPath}")
				},
				{
					key : 'Delete',
					label : 'Delete',
					formatter : createDeleteImageFormatter("${pageContext.request.contextPath}")
				} ];
		var bankEntriesNotInBankBookDS = new YAHOO.util.DataSource();
		bankEntriesNotInBankBooksTable = new YAHOO.widget.DataTable(
				"bankEntriesNotInBankBookTable",
				bankEntriesNotInBankBookColumns, bankEntriesNotInBankBookDS);
		bankEntriesNotInBankBooksTable.on('cellClickEvent', function(oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') {
				bankEntriesNotInBankBooksTable.addRow({
					SlNo : bankEntriesNotInBankBooksTable.getRecordSet()
							.getLength() + 1
				});
				jQuery(".datepicker").datepicker({
					format: "dd/mm/yyyy",
					autoclose:true
				}); 
				updateBENIBBTableIndex();
				check();
			}
			if (column.key == 'Delete') {
				console.log(record._oData.beId);
				if(record._oData.beId !=null && record._oData.beId!=""){
					jQuery.ajax({
						url : '/EGF/payment/ajaxDeleteBankEntries.action',
						type : "get",
						data : {
							beId : record._oData.beId						
						},
						success : function(data, textStatus, jqXHR) {
							bootbox.alert("Deleting data is successful");
						},
						error : function(jqXHR, textStatus, errorThrown) {
							bootbox.alert("Error while deleting data");
						}
					});
					this.deleteRow(record);
					allRecords = this.getRecordSet();
					for (var i = 0; i < allRecords.getLength(); i++) {
						this.updateCell(this.getRecord(i), this
								.getColumn('SlNo'), "" + (i + 1));
					}
				}else{

					if (this.getRecordSet().getLength() > 1) {
						this.deleteRow(record);
						allRecords = this.getRecordSet();
						for (var i = 0; i < allRecords.getLength(); i++) {
							this.updateCell(this.getRecord(i), this
									.getColumn('SlNo'), "" + (i + 1));
						}
					} else {
						bootbox.alert("This row can not be deleted");
					}
				}
				
			}
		});

		<s:iterator value="bankEntriesNotInBankBookList" status="stat">
		bankEntriesNotInBankBooksTable
				.addRow({
					SlNo : bankEntriesNotInBankBooksTable.getRecordSet()
							.getLength() + 1,
					"refNum" : '<s:property value="refNum"/>',
					"beId" : '<s:property value="beId"/>',
					"type" : '<s:property value="type"/>',
					"date" : '<s:property value="date"/>',
					"amount" : '<s:property value="amount"/>',
					"remarks" : '<s:property value="remarks"/>',
					"glcodeDetail" : '<s:property value="glcodeDetail"/>'
				});
		var index = '<s:property value="#stat.index"/>';
		updateGridBENIBB('beId',index,'<s:property value="beId"/>');
		updateGridBENIBB('refNum',index,'<s:property value="refNum"/>');
		updateGridBENIBB('type',index,'<s:property value="type"/>');
		updateGridBENIBB('dateId',index,'<s:property value="dateId"/>');
		updateGridDateBENIBB(index);
		updateGridBENIBB('remarks',index,'<s:property value="remarks"/>');
		updateGridBENIBB('glcodeDetail',index,'<s:property value="glcodeDetail"/>');
		updateGridBENIBB('amount',index,'<s:property value="amount"/>');
		updateBENIBBTableIndex();
		</s:iterator>


	}
	var amountshouldbenumeric = '<s:text  name="amount.should.be.numeric"/>';
	var succesMessage = '<s:text name="directbank.transaction.succcess"/>';
	var totalsnotmatchingamount = '<s:text name="totals.not.matching.amount"/>';
	var button = '<s:property value="button"/>';
</script>

</head>
<body onload="load();">
	<s:form action="bankEntriesNotInBankBook" theme="simple"
		name="bankEntriesNotInBankBookform" id="bankEntriesNotInBankBookform">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param value="Bank Entries Not In Bank Book" name="heading" />
			</jsp:include>
			<div class="formmainbox">
				<div class="subheadnew">Bank Entries Not In Bank Book</div>

				<div align="center">
					<font style='color: red;'>
						<p class="error-block" id="lblError"></p>
					</font>
				</div>
				<span class="mandatory1">
					<div id="Errors">
						<s:actionerror />
						<s:fielderror />
					</div> <s:actionmessage />
				</span>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<jsp:include page="../voucher/vouchertrans-filter-new.jsp" />
					<tr>
						<egov:ajaxdropdown id="bank" fields="['Text','Value']"
							dropdownId="bank"
							url="voucher/common-ajaxLoadAllBanksByFund.action" />
						<td class="greybox"></td>
						<td class="greybox"><s:text name="bank" /><span
							class="mandatory1">*</span></td>
						<td class="greybox"><s:select name="bank" id="bank"
								list="dropdownData.bankList" headerKey="-1" listKey="id"
								listValue="name" headerValue="----Choose----"
								onchange="loadBankBranch(this)" value="%{bank}" /></td>
						<egov:ajaxdropdown id="bank_branch" fields="['Text','Value']"
							dropdownId="bank_branch"
							url="voucher/common-ajaxLoadBankBranchFromBank.action" />
						<td class="greybox"><s:text name="arf.bankbranch" /><span
							class="mandatory1">*</span></td>
						<td class="greybox" colspan="2"><s:select name="bank_branch"
								id="bank_branch" list="dropdownData.bankBranchList"
								listKey="id"
								listValue="branchname"
								headerKey="-1" headerValue="----Choose----"
								onchange="loadBankAccount(this)" value="%{bank_branch}" /></td>
					</tr>
					<tr>
						<td class="greybox"></td>
						<egov:ajaxdropdown id="bankaccount" fields="['Text','Value']"
							dropdownId="bankaccount"
							url="voucher/common-ajaxLoadBankAccountsByBranch.action" />
						<td class="greybox"><s:text name="arf.bank.accountnumber" /><span
							class="mandatory1">*</span></td>
						<td class="greybox" colspan="2"><s:select name="bankaccount"
								id="bankaccount" list="dropdownData.bankAccountList"
								listKey="id"
								listValue="chartofaccounts.glcode+'--'+accountnumber"
								headerKey="-1" headerValue="----Choose----"
								value="%{bankaccount}" /></td>
						<td class="greybox"></td>
						<td class="greybox"></td>
					</tr>
				</table>
			</div>
			<div class="buttonbottom">
				<input type="button" value="Search" class="buttonsubmit"
					onclick="return onSearch();" /> <input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />

			</div>
			</br>
			</br>
			<table id="bankEntriesNotInBankBookuiTable" cellspacing="0"
				cellpadding="0" border="0" width="100%"
				style="border-right: 0px solid rgb(197, 197, 197);"
				class="tablebottom">
				<tbody>
					<tr>
						<td colspan="6">
							<div class="subheadsmallnew">
								<strong>Bank Entries Not In Bank Book Details </strong>
							</div>
							</div>

							<div class="yui-skin-sam" align="center">
								<div id="bankEntriesNotInBankBookTable"></div>


								<script>
											makeBankEntriesNotInBankBookTable();
											//initialise datepicker
												jQuery(".datepicker").datepicker({
													format: "dd/mm/yyyy",
													autoclose:true
												}); 
											document
													.getElementById(
															'bankEntriesNotInBankBookTable')
													.getElementsByTagName(
															'table')[0].width = "100%"
										</script>
						</td>
					</tr>
				</tbody>
			</table>
			</br>




			<div class="buttonbottom" id="saveButtion">
				<input type="button" value="Save" class="buttonsubmit"
					onclick="return onSave();" /> <input type="button" value="Close"
					onclick="javascript:window.close()" class="button" />

			</div>
		</s:push>
		<s:hidden name="showMode" />
	</s:form>
	<script type="text/javascript">
		function onSearch() {
			var bank = document.getElementById("bank").value;
			var bankBranch = document.getElementById("bank_branch").value;
			var bankAccount = document.getElementById("bankaccount").value;
			var fund = document.getElementById("fundId").value;
			var fund = document.getElementById("fundId").value;
			if (fund == null || fund == "" || fund == "-1") {
				bootbox.alert("Please select Fund");
				return false;
			} 
			if (bank == null || bank == "" || bank == "-1") {
				bootbox.alert("Please select bank");
				return false;
			} 
			 if (bankBranch == null || bankBranch == ""
					|| bankBranch == "-1") {
				bootbox.alert("Please select bank branch");

				return false;
			}  
			if (bankAccount == null || bankAccount == ""
					|| bankAccount == "-1") {
				bootbox.alert("Please select bank account");
				return false;
			}
			<s:if test="%{isFieldMandatory('fund')}"> 
			 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == ""){
				 bootbox.alert( "Please select a Fund");
				return false;
			 }    
		 </s:if>   
		 <s:if test="%{isFieldMandatory('function')}">                        
		 if(null != document.getElementById('vouchermis.function') && document.getElementById('vouchermis.function').value == -1){

			 bootbox.alert( "Please select a Function");
			return false;
		 }
	 	</s:if>
		<s:if test="%{isFieldMandatory('department')}"> 
			 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value ==""){

				 bootbox.alert( "Please select a Department");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('scheme')}"> 
			 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

				 bootbox.alert( "Please select a Scheme");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('subscheme')}"> 
			 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

				 bootbox.alert( "Please select a Subscheme");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('functionary')}"> 
			 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value == -1){

				 bootbox.alert( "Please select a Functionary");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('fundsource')}"> 
			 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

				 bootbox.alert( "Please select a Fundsource");
				return false;
			}
		</s:if>
		<s:if test="%{isFieldMandatory('field')}"> 
			 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

				 bootbox.alert( "Please select a Field");
				return false;
			 }
		</s:if>
			doLoadingMask();
			enableAll();
			document.bankEntriesNotInBankBookform.action = '/EGF/payment/bankEntriesNotInBankBook-search.action';
			document.bankEntriesNotInBankBookform.submit();

		}

		function onSave() {
			var bank = document.getElementById("bank").value;
			var bankBranch = document.getElementById("bank_branch").value;
			var bankAccount = document.getElementById("bankaccount").value;
			var fund = document.getElementById("fundId").value;
			var fund = document.getElementById("fundId").value;
			if (fund == null || fund == "" || fund == "-1") {
				bootbox.alert("Please select Fund");
				return false;
			} 
			if (bank == null || bank == "" || bank == "-1") {
				bootbox.alert("Please select bank");
				return false;
			} 
			 if (bankBranch == null || bankBranch == ""
					|| bankBranch == "-1") {
				bootbox.alert("Please select bank branch");

				return false;
			}  
			if (bankAccount == null || bankAccount == ""
					|| bankAccount == "-1") {
				bootbox.alert("Please select bank account");
				return false;
			}
			<s:if test="%{isFieldMandatory('fund')}"> 
			 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == ""){
				 bootbox.alert( "Please select a Fund");
				return false;
			 }    
		 </s:if>   
		 <s:if test="%{isFieldMandatory('function')}">                        
		 if(null != document.getElementById('vouchermis.function') && document.getElementById('vouchermis.function').value == -1){

			 bootbox.alert( "Please select a Function");
			return false;
		 }
	 	</s:if>
		<s:if test="%{isFieldMandatory('department')}"> 
			 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value ==""){

				 bootbox.alert( "Please select a Department");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('scheme')}"> 
			 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

				 bootbox.alert( "Please select a Scheme");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('subscheme')}"> 
			 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

				 bootbox.alert( "Please select a Subscheme");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('functionary')}"> 
			 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value == -1){

				 bootbox.alert( "Please select a Functionary");
				return false;
			 }
		</s:if>
		<s:if test="%{isFieldMandatory('fundsource')}"> 
			 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

				 bootbox.alert( "Please select a Fundsource");
				return false;
			}
		</s:if>
		<s:if test="%{isFieldMandatory('field')}"> 
			 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

				 bootbox.alert( "Please select a Field");
				return false;
			 }
		</s:if>
		console.log(jQuery("#bankEntriesNotInBankBookform").serialize());
		if(validate()){
			doLoadingMask();
			enableAll();
			document.bankEntriesNotInBankBookform.action = '/EGF/payment/bankEntriesNotInBankBook-save.action';
			document.bankEntriesNotInBankBookform.submit();
		}

		}
		
		function load(){
			<s:if test="%{mode == 'save'}"> 
				disableAll();
			</s:if>
		}	
			
	</SCRIPT>
</body>
</html>
