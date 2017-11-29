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
.yui-dt-col-creditamount{
	text-align:right;
}
</style>

<script>
	var stdDeductionTypeDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="standardDeductionConfValues" status="status">    
    {"label":"<s:property />",
    "value":"<s:property />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ] 
    
    var stdAccHeadDropdownOptions=[{label:"--- Select ---", value:"0"}]
    
    var lang=YAHOO.lang;
	function createStdDeductTypeFormatter(){
	    return function(el, oRecord, oColumn, oData) {
	        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
	            options = (lang.isArray(oColumn.dropdownOptions)) ?
	                oColumn.dropdownOptions : null,
	            selectEl,
	            collection = el.getElementsByTagName("select");
	        if(collection.length === 0) {
	            selectEl = document.createElement("select");
	            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
	            selectEl.name = "standardDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
	            //selectEl.name = "";
				selectEl.id = oColumn.getKey()+oRecord.getId();
	            selectEl = el.appendChild(selectEl);
	            YAHOO.util.Event.addListener(selectEl,"change",this._onDropdownChange,this);
				
	        }
	
	        selectEl = collection[0];
	
	        if(selectEl) {
	            selectEl.innerHTML = "";
	            if(options) {
	                for(var i=0; i<options.length; i++) {
	                    var option = options[i];
	                    var optionEl = document.createElement("option");
	                    optionEl.value = (lang.isValue(option.value)) ?
	                            option.value : option;
	                    optionEl.innerHTML = (lang.isValue(option.text)) ?
	                            option.text : (lang.isValue(option.label)) ? option.label : option;
	                    optionEl = selectEl.appendChild(optionEl);
	                    if (optionEl.value == selectedValue) {
	                        optionEl.selected = true;
	                    }
	                }
	            }
	            else {
	                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
	            }
	        }
	        else {
	            el.innerHTML = lang.isValue(oData) ? oData : "";
	        }
	    }
	}
    function createDropdownFormatterWithTableLengthAsId(listObj,postfixFieldValue){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
             if(postfixFieldValue!="")
            	selectEl.name = listObj+'['+oRecord.getCount()+'].'+oColumn.getKey()+'.'+postfixFieldValue;
            else
            	selectEl.name = listObj+'['+oRecord.getCount()+'].'+oColumn.getKey();
			selectEl.id = oColumn.getKey()+standardDeductionsDataTable.getRecordSet().getLength();
            selectEl = el.appendChild(selectEl);
            YAHOO.util.Event.addListener(selectEl,"change",this._onDropdownChange,this);
			
        }

        selectEl = collection[0];

        if(selectEl) {
            selectEl.innerHTML = "";
            if(options) {
                for(var i=0; i<options.length; i++) {
                    var option = options[i];
                    var optionEl = document.createElement("option");
                    optionEl.value = (lang.isValue(option.value)) ?
                            option.value : option;
                    optionEl.innerHTML = (lang.isValue(option.text)) ?
                            option.text : (lang.isValue(option.label)) ? option.label : option;
                    optionEl = selectEl.appendChild(optionEl);
                    if (optionEl.value == selectedValue) {
                        optionEl.selected = true;
                    }
                }
            }
            else {
                selectEl.innerHTML = "<option selected value=\"" + selectedValue + "\">" + selectedValue + "</option>";
            }
        }
        else {
            el.innerHTML = lang.isValue(oData) ? oData : "";
        }
    }
}
    function convertToIntNumber(value){	
   	 	return parseInt(value);
   	}
    
    function createStdAmountTextboxFormatter(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		    var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "standardDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
		    markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='validateStdAmount(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
    function createStdAmountTextboxFormatterWhole(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		    var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "standardDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
		    markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='roundToRupees(this);validateStdAmount(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
	var stdAmountTextboxFormatter = createStdAmountTextboxFormatter(15,13);
	var stdAmountTextboxFormatterWhole = createStdAmountTextboxFormatterWhole(15,13);
	
	function validateStdAmount(elem,recordId){
	record=standardDeductionsDataTable.getRecord(recordId);
      if(!validateNumberInTableCell(standardDeductionsDataTable,elem,recordId)) return;
      	dom.get("creditamount"+record.getId()).value =  roundTo(dom.get("creditamount"+record.getId()).value);
      calculateTotal();
    }

	function createStdDescTextboxFormatter(size,maxlength) {
		var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
		   var value = (YAHOO.lang.isValue(oData))?oData:"";
		   var fieldName = "standardDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();   
		   var id = oColumn.getKey()+oRecord.getId();
		   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+ "'/>";
		   el.innerHTML = markup;
		   
		}
		return textboxDescFormatter;	
	}
	var stdDescTextboxFormatter = createStdDescTextboxFormatter(25,50);
	
	var standardDeductionsDataTable;
	var makeStandardDeductionsDataTable = function() {
			var standardDeductionsColumnDefs = [ 
				{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},				
				{key:"deductionType", label:'<s:text name="stdDedcution.column.deductions"/>', formatter:createStdDeductTypeFormatter(), dropdownOptions:stdDeductionTypeDropdownOptions},
				{key:"glcodeid", label:'<s:text name="stdDedcution.column.accHead"/><span class="mandatory">*</span>', formatter:createDropdownFormatterWithTableLengthAsId('standardDeductions',''), dropdownOptions:stdAccHeadDropdownOptions},
				{key:"creditamount",label:'<s:text name="stdDedcution.column.amount"/><span class="mandatory">*</span>', formatter:stdAmountTextboxFormatterWhole,sortable:false, resizeable:false},
				{key:"narration", label:'<s:text name="stdDedcution.column.comments"/>', formatter:stdDescTextboxFormatter, sortable:false, resizeable:true},				
				{key:'delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
			];
			var standardDeductionsDataSource = new YAHOO.util.DataSource(); 
			standardDeductionsDataTable = new YAHOO.widget.DataTable("standardDeductionsTable",standardDeductionsColumnDefs, standardDeductionsDataSource, {MSG_EMPTY:"<s:text name='contractorBill.stadarddedcution.initial.table.message'/>"});
			standardDeductionsDataTable.on('cellClickEvent',function (oArgs) {
				var target = oArgs.target;
				var record = this.getRecord(target);
				var column = this.getColumn(target);
				if (column.key == 'delete') { 		
						this.deleteRow(record);
						calculateTotal();
						allRecords=this.getRecordSet();
						for(i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}
				}        
			});
			
		standardDeductionsDataTable.on('dropdownChangeEvent',function (oArgs) {
    	var record = this.getRecord(oArgs.target);
    	var column = this.getColumn(oArgs.target);
	    if(column.key=='deductionType'){
    	    // Check for duplicate standard deduction
    	    var selectedIndex=oArgs.target.selectedIndex;
    	    var row_index = this.getRecordSet().getRecordIndex(record);
    	    for(i=0;i<this.getRecordSet().getLength() && selectedIndex!=0;i++){
    	       if(row_index!=i && stdDeductionTypeDropdownOptions[selectedIndex].value==dom.get("deductionType" + this.getRecordSet().getRecord(i).getId()).value){
    	       		dom.get("accountDetails_error").style.display=''
    	       		dom.get("accountDetails_error").innerHTML='The Standard Deduction Type is already added';
    	       		oArgs.target.selectedIndex=0;
    	       		return;
    	       }
    	    }
    	    dom.get("accountDetails_error").style.display='none'
    	    dom.get("accountDetails_error").innerHTML=''
    	    setupAccountHeadsForDeductionType(record,oArgs);
    	}
    	
		});		
		
		return {
		    oDS: standardDeductionsDataSource,
		    oDT: standardDeductionsDataTable
		}; 
 	}
 	
 	function validateForStdDeduction(){
	     var records = standardDeductionsDataTable.getRecordSet();
	     var error;
	     for(i=0;i<records.getLength();i++){ 
	       if(dom.get("deductionType" + records.getRecord(convertToIntNumber(i)).getId()).value!=0){    
	          if(dom.get("glcodeid" +eval(convertToIntNumber(i)+1)).value==0){  
	          	error='<s:text name="contractorBill.create.standardDeduction.accoundHeadRequired"/>';
	       		error=error+" for Row"+eval(i+1);
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
	          	return false;
	          }
	          if(dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value=='' || dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value==0){  
	          	error='<s:text name="contractorBill.create.standardDeduction.amountRequired"/>';
	       		error=error+" for Row"+eval(i+1);
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;
	          }
	       }
	       else if(dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value!='' || 
	      		dom.get("glcodeid" + eval(i+1)).value!=0){
	      		error='<s:text name="contractorBill.create.standardDeduction.deductionTypeRequired"/>';
	       		error=error+" for Row"+eval(i+1);
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;		
	       } 
	       else{
	       		error='<s:text name="contractorBill.create.emptyStandardDeductionDetails"/>';
	       		error=error+" for Row"+eval(i+1);
	       		dom.get("contractorBill_error").innerHTML=error;
	       	   	dom.get("contractorBill_error").style.display='';
	       	   	window.scroll(0,0);
		        return false;
	       }
	     }
	     dom.get("contractorBill_error").style.display='none';
	     dom.get("contractorBill_error").innerHTML='';
	     return true;
    }
    
    function setupAccountHeadsForDeductionType(record,oArgs){
	    deduction_Type = dom.get("deductionType" + record.getId()).value;
	    var records = standardDeductionsDataTable.getRecordSet();
	    var row_id = records.getRecordIndex(record);
	    makeJSONCall(["xRowId","Text","Value"], 
		    	'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!populateAccountHeadsForDeductionType.action',
		    	{deductionType:deduction_Type,rowId:row_id},myStdSuccessHandler,myStdFailureHandler) ;
	}
	
	myStdSuccessHandler = function(req,res){
	    var resultdatas=res.results;
	    var records = standardDeductionsDataTable.getRecordSet();
	    var row_id = resultdatas[0].xRowId;
	    var resLength =resultdatas.length;
	  	var dropDownLength = dom.get("glcodeid" + eval(convertToIntNumber(row_id)+1)).length;
	  	for(i=1;i<resultdatas.length;i++){
	   			dom.get("glcodeid" +eval(convertToIntNumber(row_id)+1)).options[i]=new Option(resultdatas[i].Text,resultdatas[i].Value);
	   	}
	   	while(dropDownLength>resLength){
		     dom.get("glcodeid" + eval(convertToIntNumber(row_id)+1)).options[resultdatas.length] = null;
		     dropDownLength=dropDownLength-1;
		}
		return true;
	}
	
	myStdFailureHandler= function(){
	    dom.get("accountDetails_error").style.display='block';
		dom.get("accountDetails_error").innerHTML='Unable to load account heads for standard deduction';
	}
</script>

	<tr>
	  	<td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
	   		<div class="headplacer"><s:text name="contractorBill.subheader.stadarddedcution" /></div>
		</td>
		<td align="right" class="headingwk" style="border-left-width: 0px">
       		<a id="standarddeductionrow" href="#" onclick="standardDeductionsDataTable.addRow({SlNo:standardDeductionsDataTable.getRecordSet().getLength()+1});return false;">
       		<img border="0" alt="Add Standard Deduction" src="/egworks/resources/erp2/images/add.png" /></a>
       	</td>
	</tr>
	
	<tr>
      <td colspan="4">
         <div class="yui-skin-sam">
           <div id="standardDeductionsTable"></div>    
         </div>
      </td>
    </tr>
  

 	<!--  populating data in to data table data  -->
 	<script>
		makeStandardDeductionsDataTable();
		url ='${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!populateStandardDeductionType.action';
		 <s:iterator id="standardDeductionsListiterator" value="StandardDeductionTypes" status="stdDed_row_status">
		  standardDeductionsDataTable.addRow({SlNo:'<s:property value="#stdDed_row_status.count"/>',
			           deductionType:'<s:property value="deductionType"/>',
			           glcodeid:'<s:property value="glcodeid"/>',
			           creditamount:'<s:property value="creditamount"/>',
			           narration:'<s:property value="narration"/>',
			           Delete:'X'}); 
		 	new Ajax.Request(url,
			{
 				method:'get',parameters: {deductionType:'<s:property value="deductionType"/>'},
 				onSuccess: function(transport){
	  				var coaDetails =  transport.responseText.split('$');
	  		  		for(var i=0;i<coaDetails.length;i++)
	 		 		{
				 	    dom.get('glcodeid'+'<s:property value="#stdDed_row_status.count"/>').options[i+1]=
	 			 		new Option(coaDetails[i].split('/')[1],coaDetails[i].split('/')[0]);
	 			 	}	
				 	dom.get('glcodeid'+'<s:property value="#stdDed_row_status.count"/>').value = '<s:property value="glcodeid"/>';   	  
		 		}
       		});			  							               				
       </s:iterator>
    
	</script> 


