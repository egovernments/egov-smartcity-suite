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

<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:200%;
} 	
.yui-dt-col-amount{
	text-align:right;
}
</style>
<script>
var coaDescFormatter = createTextFormatter(50,10);
var coaAmountFormatter = createTextFormatter(15,13);
var coaAmountFormatterWhole = createTextFormatterWhole(15,13);
var skipBudgObj;
var checkBudget;
var estimate_DropDown_Length;
var srcPage;

var assetDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.assestList" status="status">  
    {"label":"<s:property value="%{asset.code}" />" ,
    "value":"<s:property value="%{asset.id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
 <s:if test="%{skipBudget}">   
var coaDropdownOptions=[
    <s:iterator var="s" value="dropdownData.coaList" status="status">  
    {"label":"<s:property value="%{glcode}"/>-<s:property value="%{name}" />" ,
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
</s:if>
<s:else>
var coaDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.coaList" status="status">  
    {"label":"<s:property value="%{glcode}"/>-<s:property value="%{name}" />" ,
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
</s:else>
function createTextFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();   
	    if(oColumn.getKey() == 'description'){ 
	    	var fieldName = "accountDetailsForBill[" + oRecord.getCount() + "]." + oColumn.getKey();
	    	markup="<input type='text' class='selectmultilinewk' id='"+id+"' name='"+fieldName+"' size='"+
	    	size+"' maxlength='"+maxlength+"' value='"+value+"' readonly='readonly'/>";
	   	}
	   	else if(oColumn.getKey()=='amount')
	   	{
	   		var fieldName = "accountDetailsForBill[" + oRecord.getCount() + "]." + oColumn.getKey();
	    	markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='calculateTotalAmount(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	   	}
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
function createTextFormatterWhole(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();   
	    if(oColumn.getKey() == 'description'){ 
	    	var fieldName = "accountDetailsForBill[" + oRecord.getCount() + "]." + oColumn.getKey();
	    	markup="<input type='text' class='selectmultilinewk' id='"+id+"' name='"+fieldName+"' size='"+
	    	size+"' maxlength='"+maxlength+"' value='"+value+"' readonly='readonly'/>";
	   	}
	   	else if(oColumn.getKey()=='amount')
	   	{
	   		var fieldName = "accountDetailsForBill[" + oRecord.getCount() + "]." + oColumn.getKey();
	    	markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='roundToRupees(this);calculateTotalAmount(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	   	}
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
function totalworkValue(totalAmount)
{

	<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
		refVal = dom.get('grossAmount').value;
	</s:if>
	<s:else>
		refVal = dom.get('billamount').value;
	</s:else>
	if(getNumber(totalAmount)<refVal || getNumber(totalAmount)>refVal)
	{
		var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
		if(accountDetailsRecords.getLength()==1)
			dom.get("contractorBill_error").innerHTML='Amount should be equal to work value of bill'; 
		else 
			dom.get("contractorBill_error").innerHTML='Total Amount should be equal to the work value of bill'; 
        
        dom.get("contractorBill_error").style.display='';
		return false;
	}
	 dom.get("contractorBill_error").innerHTML='';
	 dom.get("contractorBill_error").style.display='none';
	return true;
}
function calculateTotalAmount(elem,recordId){
	record=accountDetailsDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
 	
 	if(!validateNumberInTableCell(accountDetailsDataTable,elem,recordId)) return;
    
   	dom.get("amount"+record.getId()).value =  roundTo(dom.get("amount"+record.getId()).value);
	var totalAmount=calculateTotal();
	totalworkValue(totalAmount);
	dom.get("totalAmount").innerHTML=roundTo(totalAmount);	  		
}

function createBalanceTextboxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+accountDetailsDataTable.getRecordSet().getLength();
	    var fieldName="accountDetailsForBill[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='text' class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' readonly='true' />";
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var balanceTextboxFormatter = createBalanceTextboxFormatter(11,8);



function show_Hide_BudgetInfo(srcPage,accountDetailsDataTable)
{
	<s:if test="%{(sourcepage!='search' && sourcepage=='inbox' && model.status.code!='NEW')}">
		var value=accountDetailsDataTable.getColumn('viewBalance');
		var index=value.getIndex();
		var value1=accountDetailsDataTable.getColumn('budgAmount');
		var index1=value1.getIndex();
		var value2=accountDetailsDataTable.getColumn('budgBalance');
		var index2=value2.getIndex()
		accountDetailsDataTable.hideColumn(parseInt(index));
		accountDetailsDataTable.hideColumn(parseInt(index1));
		accountDetailsDataTable.hideColumn(parseInt(index2)); 
	</s:if>	
	<s:else>
		var value=accountDetailsDataTable.getColumn('viewBalance');
		var index=value.getIndex();
		var value1=accountDetailsDataTable.getColumn('budgAmount');
		var index1=value1.getIndex();
		var value2=accountDetailsDataTable.getColumn('budgBalance');
		var index2=value2.getIndex()
		accountDetailsDataTable.showColumn(parseInt(index));
		accountDetailsDataTable.showColumn(parseInt(index1));
		accountDetailsDataTable.showColumn(parseInt(index2)); 
	</s:else>
	return;
}



var accountDetailsDataTable;
var makeAccountDetailsDataTable = function() {
	var accountDetailsColumnDefs = [ 
		{key:"SlNo", label:'<s:text name='tenderNegotiation.slNo'/>', sortable:false, resizeable:false, width:20},
		{key:"asset", label:'<s:text name='contractorBill.asset'/>', formatter:createDropdownFormatter('accountDetailsForBill','id'), dropdownOptions:assetDropdownOptions,resizeable:true},
		{key:"coa", label:'<s:text name='contractorBill.accountCode'/><span class="mandatory">*</span>', formatter:createDropdownFormatter('accountDetailsForBill','id'), dropdownOptions:coaDropdownOptions,resizeable:true},
		{key:"description",label:'<s:text name='contractorBill.description'/>', formatter:coaDescFormatter,sortable:false, resizeable:false},
		{key:"amount",label:'<s:text name='contractorBill.amount'/><span class="mandatory">*</span>', formatter:coaAmountFormatterWhole,sortable:false, resizeable:false},
		{key:"viewBalance",label:'<s:text name='contractorBill.viewAccntBalance'/>',formatter:viewImageFormatter("${pageContext.request.contextPath}")},
		{key:"budgAmount",label:'<s:text name='contractorBill.budgetAmount'/>',formatter:balanceTextboxFormatter},
		{key:"budgBalance",label:'<s:text name='contractorBill.budgetBalance'/>',formatter:balanceTextboxFormatter},
		{key:'delete',label:'<s:text name='contractorBill.delete'/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
	];

	var accountDetailsDataSource = new YAHOO.util.DataSource(); 
	accountDetailsDataTable = new YAHOO.widget.DataTable("accountDetailsDataTable",accountDetailsColumnDefs, accountDetailsDataSource,
	{MSG_EMPTY:"<s:text name='accountDetails.initial.table.message'/>"});
	accountDetailsDataTable.subscribe("cellClickEvent", accountDetailsDataTable.onEventShowCellEditor); 
	skipBudgObj='<s:property value="%{skipBudget}"/>';
	checkBudget=dom.get('checkBudget').value;
	estimate_DropDown_Length='<s:property value="%{dropdownData.workOrderEstimateList.size()}"/>';
	srcPage='<s:property value="%{sourcepage}" />';
		
	show_Hide_BudgetInfo(srcPage,accountDetailsDataTable);	
	
	accountDetailsDataTable.on('dropdownChangeEvent',function (oArgs) {
   	var record = this.getRecord(oArgs.target);
   	var column = this.getColumn(oArgs.target);
   	var selectedIndex=oArgs.target.selectedIndex;
   	if(selectedIndex!=0){
   		validateDuplicateAssestName(this.getRecordSet(),oArgs);
   	  if(column.key=='coa'){
   	    	var obj=dom.get("coa"+record.getId());
	    	dom.get("description"+record.getId()).value=obj.options[obj.selectedIndex].text.split('-')[1];
	    	<s:if test="%{(sourcepage!='search' && sourcepage=='inbox' && model.status.code=='NEW') || model.id==null}">
	    	if(estimate_DropDown_Length==1 || estimate_DropDown_Length>1){
		    	var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
		    	for(var i=0;i<accountDetailsDataTable.getRecordSet().getLength();i++)
		    	{
		    	  if((dom.get("coa" + record.getId()).value) == (dom.get("coa"+accountDetailsRecords.getRecord(i).getId()).value))
		    	  {
		        	     dom.get("budgAmount"+(eval(i)+1)).value="";
		        	     dom.get("budgBalance"+(eval(i)+1)).value="";
		    	  }
		    	}
		    }
		    </s:if>
	  }
 	}
 	else{
 		if(column.key=='coa') {
 				dom.get("description"+record.getId()).value=""; 
 				var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
		    	for(var i=0;i<accountDetailsDataTable.getRecordSet().getLength();i++)
		    	{
		    	  if((dom.get("coa" + record.getId()).value) == (dom.get("coa"+accountDetailsRecords.getRecord(i).getId()).value))
		    	  {
		        	     dom.get("budgAmount"+(eval(i)+1)).value="";
		        	     dom.get("budgBalance"+(eval(i)+1)).value="";
		    	  }
		    	}
		 
	    	return;
 			}
	    }
  });
   accountDetailsDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'delete') { 	
				this.deleteRow(record);
				var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
				checkValues(accountDetailsRecords.getLength());
				dom.get("totalAmount").innerHTML=roundTo(calculateTotal());
				validateDuplicateAssestName(this.getRecordSet(),oArgs);		
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}
		<s:if test="%{(sourcepage!='search' && sourcepage=='inbox' && model.status.code=='NEW') || model.id==null}">
		if((estimate_DropDown_Length==1 || estimate_DropDown_Length>1) || dom.get('estimateNo').value!=''){
			if (column.key == 'viewBalance') {
			 	if(dom.get("coa"+record.getId()).value=="0" || dom.get('billdate').value=="")
			 	{
			 	    dom.get("contractorBill_error").style.display="block";
			  		dom.get('contractorBill_error').innerHTML='Account Code and BillDate are mandatory to view BudgetDetails'; 
			  		return false;
			  	}
			  	dom.get("contractorBill_error").style.display="none";
			  	dom.get('contractorBill_error').innerHTML=''; 
			  	var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
		    	for(var i=0;i<accountDetailsDataTable.getRecordSet().getLength();i++)
		    	{
		    	  if((dom.get("coa" + record.getId()).value) == (dom.get("coa"+accountDetailsRecords.getRecord(i).getId()).value))
		    	  {
		        	     dom.get("budgAmount"+(eval(i)+1)).value="";
		        	     dom.get("budgBalance"+(eval(i)+1)).value="";
		    	  }
		    	}
		    	calculateBudgetBalance(record);
			}	
		}
		</s:if>	        
	});
	
	var tfoot = accountDetailsDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 3;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'Total';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'totalAmount','0.00');
	addCell(tr,3,'filler1','');
	addCell(tr,3,'filler2','');
	addCell(tr,3,'filler3','');
	addCell(tr,3,'filler4','');
	
	<s:if test="%{(sourcepage!='search' && sourcepage=='inbox' && model.status.code!='NEW') || model.id==null}">
		document.getElementById("filler2").style.display="none";
		document.getElementById("filler3").style.skipBudgObjdisplay="none"; 
		document.getElementById("filler4").style.display="none";
	</s:if>
	<s:else>
		document.getElementById("filler2").style.display="block";
		document.getElementById("filler3").style.display="block"; 
		document.getElementById("filler4").style.display="block";
	</s:else>
	
	return {
	    oDS: accountDetailsDataSource,
	    oDT: accountDetailsDataTable
	};  
}

	function calculateBudgetBalance(record){
		var glCodeId = dom.get("coa" + record.getId()).value;
		var billDate=dom.get('billdate').value;
		var estimateId=dom.get('estimateId').value;
		var records = accountDetailsDataTable.getRecordSet();
		var row_id = records.getRecordIndex(record);
		var url='';
		url = '${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getBudgetDetails.action';
		makeJSONCall(["xRowId","budgAmount","budgBalance","errorMsg","checkBudget"],  
		    	url,
		    	{glCodeId:glCodeId,billDate:billDate,estimateId:estimateId,rowId:row_id},budgetDetailsLoadHandler,budgetDetailsLoadFailureHandler);
	}  	
    	
	/*
		If the balance is -ve that means it debit balance , 
		if it is a credit balance then the API will return a +ve balance.
		
	*/
	 budgetDetailsLoadHandler = function(req,res){
	    var results=res.results;
	    var records = accountDetailsDataTable.getRecordSet();
	    if(!(results[0].errorMsg==""))
	    {
	    	dom.get("contractorBill_error").style.display="block";
    		dom.get('contractorBill_error').innerHTML=results[0].errorMsg; 
    		return;
        } 
	    
	    var row_id = results[0].xRowId;
	    /*if(results[0].checkBudget=="true"){*/
	      if(results[0].budgBalance<=0){
	     	dom.get("contractorBill_error").style.display="block";
	     	dom.get('contractorBill_error').innerHTML='No budget available';
	      } 
	      else{
	        dom.get("budgAmount" +  eval(convertToIntNumber(row_id)+1)).value=results[0].budgAmount;
	    	dom.get("budgBalance" + eval(convertToIntNumber(row_id)+1)).value=results[0].budgBalance;
	 	  }
	 	/*}else{
	 		if(eval(convertToIntNumber(results[0].budgBalance))<dom.get("totalAmount").innerHTML){
	 		dom.get("budgAmount" +  eval(convertToIntNumber(row_id)+1)).value=results[0].budgAmount;
	    	dom.get("budgBalance" + eval(convertToIntNumber(row_id)+1)).value=eval(convertToIntNumber(results[0].budgBalance));
	      } 
	      else{
	        dom.get("budgAmount" +  eval(convertToIntNumber(row_id)+1)).value=results[0].budgAmount;
	    	dom.get("budgBalance" + eval(convertToIntNumber(row_id)+1)).value=eval(convertToIntNumber(results[0].budgBalance)*-1);
	 	  }
	 	}*/
	  	return true;
	 }

   budgetDetailsLoadFailureHandler= function(){
    dom.get("contractorBill_error").style.display="block";
    dom.get('contractorBill_error').innerHTML='Error in loading Budget Details'; 
   }	
    	

function validateDuplicateAssestName(records,oArgs){
     for(i=0;i<records.getLength();i++){ 
     for(j=0;j<records.getLength();j++){ 
     var selectedIndex=oArgs.target.selectedIndex;
     if(i!=j && dom.get("asset"+records.getRecord(i).getId()).value== dom.get("asset"+records.getRecord(j).getId()).value
     && dom.get("asset"+records.getRecord(i).getId()).value>0 && dom.get("asset"+records.getRecord(j).getId()).value>0){
     	  dom.get("accountDetails_error").innerHTML='Asset '+assetDropdownOptions[selectedIndex].label+' is Duplicated'; 
          dom.get("accountDetails_error").style.display='';
          oArgs.target.selectedIndex=0;
          return;
     }
     <s:if test="%{skipBudget}">
	     if(i!=j && dom.get("coa"+records.getRecord(i).getId()).value== dom.get("coa"+records.getRecord(j).getId()).value &&
	     dom.get("coa"+records.getRecord(i).getId()).value>0 && dom.get("coa"+records.getRecord(j).getId()).value>0){
	     	  dom.get("accountDetails_error").innerHTML='Account Code '+coaDropdownOptions[selectedIndex].label+' is Duplicated'; 
	          dom.get("accountDetails_error").style.display='';
	          oArgs.target.selectedIndex=0;
	          return;
	     }
     </s:if>
     <s:else>
     	<s:if test="%{dropdownData.coaList.size()==1}">
     		dom.get("coa"+records.getRecord(i).getId()).selectedIndex=1;
     		dom.get("description"+records.getRecord(i).getId()).value=dom.get("coa"+records.getRecord(i).getId()).options[1].text.split('-')[1];
     	</s:if>
     </s:else>
      dom.get("accountDetails_error").style.display='none';
      dom.get("accountDetails_error").innerHTML='';	
      }
   }
}
function checkValues(val)
{
	var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
	for(i=0;i<val;i++){ 
		 if(assetDropdownOptions.length>1 && dom.get("asset"+accountDetailsRecords.getRecord(i).getId()).value<=0) {
		 	dom.get("contractorBill_error").innerHTML='Please Select the Asset'; 
        	dom.get("contractorBill_error").style.display='';
        	window.scroll(0,0);
         	return false;
		} 
		if(dom.get("coa"+accountDetailsRecords.getRecord(i).getId()).value<=0)
		{
			 dom.get("contractorBill_error").innerHTML='Please Select the Account Code'; 
         	 dom.get("contractorBill_error").style.display='';
         	 window.scroll(0,0);
        	 return false;
		}
		if(dom.get("amount"+accountDetailsRecords.getRecord(i).getId()).value<=0)
		{
		 	dom.get("contractorBill_error").innerHTML='Please Specify the amount'; 
        	dom.get("contractorBill_error").style.display='';
        	window.scroll(0,0);
         	return false;
		}
	}
	 dom.get("contractorBill_error").style.display='none';
     dom.get("contractorBill_error").innerHTML='';	
   return true;
}
function workValue(){
	loadBillAmount();
	var accountDetailsRecords= accountDetailsDataTable.getRecordSet();
	if(!checkValues(accountDetailsDataTable.getRecordSet().getLength()))
		 return false;
	if(accountDetailsDataTable.getRecordSet().getLength()>1 && !totalworkValue(calculateTotal()))
		return false;
	accountDetailsDataTable.addRow({SlNo:accountDetailsDataTable.getRecordSet().getLength()+1});
	var len =accountDetailsRecords.getLength();
	<s:if test="%{isRebatePremLevelBill=='yes' && tenderResponse.tenderEstimate.tenderType==percTenderType}">
		refVal = dom.get('grossAmount').value;
	</s:if>
	<s:else>
		refVal = dom.get('billamount').value;
	</s:else>
	if(accountDetailsRecords.getLength()==1)
	{
		dom.get("amount"+accountDetailsRecords.getRecord(0).getId()).value=roundTo(refVal);
		dom.get("totalAmount").innerHTML=roundTo(refVal);
		dom.get("netPayableAmount").value =roundTo(refVal);
		<s:if test="%{skipBudget && dropdownData.assestList.isEmpty() && showValidationMsg!='yes'}">
			var obj=dom.get("coa"+accountDetailsRecords.getRecord(0).getId());
			dom.get("description"+accountDetailsRecords.getRecord(0).getId()).value=obj.options[obj.selectedIndex].text.split('-')[1];
			dom.get('addRowImgDiv').style.display="none";
			dom.get("delete"+accountDetailsRecords.getRecord(0).getId()).style.display="none";
	   </s:if>
	     <s:elseif test="%{skipBudget && !dropdownData.assestList.isEmpty() && showValidationMsg!='yes'}">
	   		var obj=dom.get("coa"+accountDetailsRecords.getRecord(0).getId());
			dom.get("description"+accountDetailsRecords.getRecord(0).getId()).value=obj.options[obj.selectedIndex].text.split('-')[1];
	    </s:elseif>
	}else{
	   <s:if test="%{skipBudget && !dropdownData.assestList.isEmpty() && showValidationMsg!='yes'}">
	   		var obj=dom.get("coa"+accountDetailsRecords.getRecord(eval(len-1)).getId());
			dom.get("description"+accountDetailsRecords.getRecord(eval(len-1)).getId()).value=obj.options[obj.selectedIndex].text.split('-')[1];
	   </s:if> 
	   
	}
	calculateTotal();
	return false;
}
</script>
 <tr><td colspan="4">

<table border="0" cellpadding="0" cellspacing="0" align="center" width="100%" >
<tr>
		<td colspan="3" class="headingwk">
			<div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
			<div class="headplacer"><s:text name="contractorBill.tab.accountDetails" /></div>
		</td>
		<td align="right" class="headingwk" style="border-left-width: 0px">
       		<a id="accountdetailsrow" href="#" onclick="workValue();defaultBudgetHead();return false;">
       		<img border="0" id="addRowImgDiv" alt="Add Account Details Rate" src="/egworks/resources/erp2/images/add.png" /></a>
       		</td>
     	</tr>
     	<tr>
       	<td colspan="4">
       	<div class="yui-skin-sam">
       	    <div id="accountDetailsDataTable"></div>                    	
       	</div>
       	<script>
                makeAccountDetailsDataTable();
               
               
                <s:iterator id="assestListiterator" value="AssestAndAccountDetails" status="row_status">
                      accountDetailsDataTable.addRow({SlNo:'<s:property value="#row_status.count"/>',
                      					    asset:'<s:property value="asset.id"/>',
		                                    coa:'<s:property value="coa.id"/>',
		                                    description:'<s:property value="description"/>',
		                                    amount:'<s:property value="amount"/>',
		                                    Delete:'X'});
		       </s:iterator>
        </script>
       	</td>
       </tr>

</table>
</tr></td>
