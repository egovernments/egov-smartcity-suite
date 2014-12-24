<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:200%;
} 	
.yui-dt-col-debitamount{
	text-align:right;
}
</style>
<script src="<egov:url path='js/works.js'/>"></script>

<script>
	var retMoneyDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="retentionMoneyAccountList" status="status">    
    {"label":"<s:property value="%{glcode}"/>-<s:property value="%{name}"/>",
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
        
    function createRetAmountTextboxFormatter(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		    var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "retentionMoneyDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
		    markup="<input type='text' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='validateRetAmount(this,\""+oRecord.getId()+"\")' onchange='calculateRetentionPercentDeductions(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
	var retAmountTextboxFormatter = createRetAmountTextboxFormatter(10,8);
	
	function validateRetAmount(elem,recordId){
	record=retentionMoneyDataTable.getRecord(recordId);
      if(!validateNumberInTableCell(retentionMoneyDataTable,elem,recordId)) return;
      	dom.get("creditamount"+record.getId()).value =  roundTo(dom.get("creditamount"+record.getId()).value);
      calculateTotal();
    }
	
   //sangamesh
	function loadRetPercentage(){
  		var creditAmt = calculateTotalRetentionMoneyDeductions();
  		var percent = ((creditAmt * 100.0)/roundTo(getNumber(document.getElementById("workRecordedAmount").value)));
    	var records = retentionMoneyDataTable.getRecordSet();
	    for(i=0;i<records.getLength();i++){ 
	    	if(percent!=0){
	       		dom.get("percentage" + records.getRecord(convertToIntNumber(i)).getId()).value =  roundTo(percent);
	       	}
	    }
    }
	
	function createRetPercentTextboxFormatter(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		    var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "retentionMoneyDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
		    markup="<input type='text' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='validateRetAmount(this,\""+oRecord.getId()+"\")' onchange='calculateRetentionMoneyDeductions(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
	var retPercentTextboxFormatter = createRetPercentTextboxFormatter(5,5);
	
	function calculateRetentionMoneyDeductions(elem,recordId)
	{
		if(!validateNumberInTableCell(retentionMoneyDataTable,elem,recordId)) return;
		var percent = elem.value;
		var totalAmount = roundTo(getNumber(document.getElementById("workRecordedAmount").value));
		if(percent <= 100.0){	
			var total = roundTo(totalAmount * percent)/100.0;
			retentionMoneyDataTable.updateCell(record,retentionMoneyDataTable.getColumn('creditamount'),roundTo(total));
			validateRetPercent(0);
		}
		else{
			validateRetPercent(1);
		}
	}
	
	function validateRetPercent(x){
		var error;	
		if(x == 1){   
	      error='<s:text name="contractorBill.create.retentionMoney.percentage"/>';
	      dom.get("contractorBill_error").innerHTML=error;
	      dom.get("contractorBill_error").style.display='';
	      window.scroll(0,0);
		  return false;
		}
		else{
		 	dom.get("contractorBill_error").style.display='none';
	     	dom.get("contractorBill_error").innerHTML='';
	     	return true;
	  	}
	}
	
	function calculateRetentionPercentDeductions(elem, recordId)
	{
		if(!validateNumberInTableCell(retentionMoneyDataTable,elem,recordId)) return;
		var amount = roundTo(elem.value);
		var totalAmount = roundTo(getNumber(document.getElementById("workRecordedAmount").value));
		if(eval(amount) <= eval(totalAmount)){
		var percent = roundTo(amount * 100.0/totalAmount);
		retentionMoneyDataTable.updateCell(record,retentionMoneyDataTable.getColumn('percentage'),roundTo(percent));
		validateRetMoney(0);
		}
		else{
			validateRetMoney(1);
		}
	}
	
	function validateRetMoney(x){
		var error;
		if(x == 1){
	      error='<s:text name="contractorBill.create.retentionMoney.retAmount"/>';
	      dom.get("contractorBill_error").innerHTML=error;
	      dom.get("contractorBill_error").style.display='';
	      window.scroll(0,0);
		  return false;
		  }
		  else{
		  	dom.get("contractorBill_error").style.display='none';
	     	dom.get("contractorBill_error").innerHTML='';
	      return true;
	      }
	}
	
	function createRetDescTextboxFormatter(size,maxlength) {
		var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
		   var value = (YAHOO.lang.isValue(oData))?oData:"";
		   var fieldName = "retentionMoneyDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();   
		   var id = oColumn.getKey()+oRecord.getId();
		   markup="<input type='text' id='"+id+"' class='selectmultilinewk' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' name='"+fieldName+ "'/>";
		   el.innerHTML = markup; 
		}
		return textboxDescFormatter;	
	}
	var retDescTextboxFormatter = createRetDescTextboxFormatter(25,50);
    
  
    
    var retentionMoneyDataTable;
	var makeretentionMoneyDataTable = function() {
			var retentionMoneyColumnDefs = [ 
				{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},				
				{key:"glcodeid", label:'<s:text name="retMoney.column.accCode"/>', formatter:createDropdownFormatter('retentionMoneyDeductions',''), dropdownOptions:retMoneyDropdownOptions},
				{key:"percentage",label:'<s:text name="retMoney.column.percentage"/>', formatter:retPercentTextboxFormatter,sortable:false, resizeable:false},
				{key:"creditamount",label:'<s:text name="retMoney.column.amount"/><span class="mandatory">*</span>', formatter:retAmountTextboxFormatter,sortable:false, resizeable:false},
				{key:"narration", label:'<s:text name="retMoney.column.comments"/>', formatter:retDescTextboxFormatter, sortable:false, resizeable:true},				
				{key:'delete',label:'Clear',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
			];
			var retentionMoneyDataSource = new YAHOO.util.DataSource(); 
			retentionMoneyDataTable = new YAHOO.widget.DataTable("retentionMoneyTable",retentionMoneyColumnDefs, retentionMoneyDataSource, {MSG_EMPTY:"<s:text name='contractorBill.retentionMoney.initial.table.message'/>"});
			retentionMoneyDataTable.addRow({SlNo:retentionMoneyDataTable.getRecordSet().getLength()+1});
			retentionMoneyDataTable.on('cellClickEvent',function (oArgs) {
				var target = oArgs.target;
				var record = this.getRecord(target);
				var column = this.getColumn(target);
				if (column.key == 'delete') { 		
						this.deleteRow(record);
						retentionMoneyDataTable.addRow({SlNo:retentionMoneyDataTable.getRecordSet().getLength()+1});
						calculateTotal();
						allRecords=this.getRecordSet();
						for(i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}
				}        
			});
			retentionMoneyDataTable.on('dropdownChangeEvent',function (oArgs) {
    		var record = this.getRecord(oArgs.target);
    		var column = this.getColumn(oArgs.target);
	   		if(column.key=='glcodeid'){
	   			// Check for duplicate retention deduction 
	    	    var selectedIndex=oArgs.target.selectedIndex;
	    	    var row_index = this.getRecordSet().getRecordIndex(record);
	    	    for(i=0;i<this.getRecordSet().getLength() && selectedIndex!=0;i++){
	    	       if(row_index!=i && retMoneyDropdownOptions[selectedIndex].value==dom.get("glcodeid" + this.getRecordSet().getRecord(i).getId()).value){
	    	       		dom.get("accountDetails_error").style.display=''
	    	       		dom.get("accountDetails_error").innerHTML='The Retention money Type is already added';
	    	       		oArgs.target.selectedIndex=0;
	    	       		return;
	    	       }
	    	    }
	    	    dom.get("accountDetails_error").style.display='none'
	    	    dom.get("accountDetails_error").innerHTML=''
	    	    }
    		});		
			return {
			    oDS: retentionMoneyDataSource,
			    oDT: retentionMoneyDataTable
			}; 
 	}
 	
 	function validateForRetMoney(){
	     var records = retentionMoneyDataTable.getRecordSet();
	     var error;
	      
	     for(i=0;i<records.getLength();i++){ 
	       if(dom.get("glcodeid" + records.getRecord(convertToIntNumber(i)).getId()).value!=0){           
	          if(dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value=='' || dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value==0){  
	          	error='<s:text name="contractorBill.create.retentionMoney.amountRequired"/>';
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;
	          }
	       }else if(dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value!=''){
	       		error='<s:text name="contractorBill.create.retentionMoney.deductionTypeRequired"/>';
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;		
	       } 
	       else{
	            <s:if test="%{retentionMoneyRequired=='yes'}"> 
	       		var error='<s:text name="contractorBill.create.emptyRetentionMoneyDetails"/>';
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;	
		   </s:if>	
	       }
	     }
		
	     dom.get("contractorBill_error").style.display='none';
	     dom.get("contractorBill_error").innerHTML='';
	     return true;
    }
 	
</script>

	<tr>
	  	<td colspan="4" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
	   		<div class="headplacer"><s:text name="contractorBill.subheader.retentionMoney" /></div>
		</td>
		<!-- td align="right" class="headingwk" style="border-left-width: 0px">
       		<a id="retentionmoneyrow" href="#" onclick="retentionMoneyDataTable.addRow({SlNo:retentionMoneyDataTable.getRecordSet().getLength()+1});return false;">
       		<img border="0" alt="Add Retention Money" src="${pageContext.request.contextPath}/image/add.png" /></a>
       	</td-->
	</tr>
	<tr>
      <td colspan="4">
         <div class="yui-skin-sam">
           <div id="retentionMoneyTable"></div>    
         </div>
      </td>
    </tr>
  

 	<!--  populating data in to data table data  -->
 	<script>
		makeretentionMoneyDataTable();
		<s:if test="%{RetentionMoneyTypes.size != 0}">
			retentionMoneyDataTable.deleteRows(0,retentionMoneyDataTable.getRecordSet().getLength());
		</s:if>
		  <s:iterator id="retentionMoneyListiterator" value="RetentionMoneyTypes" status="row_status">
		              retentionMoneyDataTable.addRow({SlNo:'<s:property value="#row_status.count"/>',
                      					    glcodeid:'<s:property value="glcodeid"/>',
		                                    creditamount:'<s:property value="creditamount"/>',
		                                    narration:'<s:property value="narration"/>',
		                                    Delete:'X'});
		 
		 </s:iterator>
		 
	</script> 


