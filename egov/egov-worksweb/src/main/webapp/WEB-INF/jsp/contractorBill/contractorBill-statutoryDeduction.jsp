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
.yui-dt-col-creditAmount{
	text-align:right;
}
</style>

<script>
	var dedTypeFlag; 
	var recoveryType;
	var deductionDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.statutoryDeductionsList" status="status">    
    {"label":"<s:property value="%{type}" />",
    "value":"<s:property value="%{id}" />",
    "mode":"<s:property value="%{recoveryMode}" />",
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]   
    
    var subPartyTypeDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.subPartyTypeList" status="status">    
    {"label":"<s:property value="%{description}" />",
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]  
    
    var typeOfWorkDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.typeOfWorkList" status="status">    
    {"label":"<s:property value="%{description}" />",
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]   
        
   function getNumbers(value){	
   	 return isNaN(value)?0.0:parseFloat(value);
   }
    
    function createStatAmountTextBoxFormatter(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		  	var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "actionStatutorydetails[" + oRecord.getCount() + "].egBillPayeeDtls." + oColumn.getKey();
		    <s:if test="%{editStatutoryAmount!='yes'}"> 
				markup="<input type='text' id='"+id+"' name='"+fieldName+"' readonly='true'  size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
			</s:if>
			<s:else>
				markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='validateEgBillPayeedetails(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
			</s:else>
		   	
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}

    function createStatAmountTextBoxFormatterWhole(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		  	var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "actionStatutorydetails[" + oRecord.getCount() + "].egBillPayeeDtls." + oColumn.getKey();
		    <s:if test="%{editStatutoryAmount!='yes'}"> 
				markup="<input type='text' id='"+id+"' name='"+fieldName+"' readonly='true'  size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
			</s:if>
			<s:else>
				markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='roundToRupees(this);validateEgBillPayeedetails(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
			</s:else>
		   	
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
	
	function validateEgBillPayeedetails(elem,recordId){	
	   	dom.get('error'+elem.id).style.display='none';
		record=statutoryDeductionsDataTable.getRecord(recordId);
		dom.get('error'+elem.id).style.display='none';
	
		if(elem.value=='' || isNaN(elem.value) || getNumber(elem.value)<= 0){		
		    dom.get('error'+elem.id).style.display='';
	 		dom.get("contractorBill_error").style.display='';
			dom.get("contractorBill_error").innerHTML='<s:text name="contractorBill.statutoryDeductions.creditamount.null" />';
			window.scroll(0,0);
		    return false;
		 }
		
		 
		 dom.get("creditAmount"+record.getId()).value=roundTo(dom.get("creditAmount"+record.getId()).value);
		 if(document.getElementById("billamount")!=null){
		 	var totalStatAmt=calculateTotalStatutoryDeductions();
		 	if(totalStatAmt > getNumber(document.getElementById("billamount").value)){
		 	    dom.get('error'+elem.id).style.display='';
	 			dom.get("contractorBill_error").style.display='';
				dom.get("contractorBill_error").innerHTML='<s:text name="contractorBill.statutoryDeductions.creditamount.greater" />';
				window.scroll(0,0);
				return false;
		 	}
		 }
		 calculateTotal();
		 dom.get("contractorBill_error").innerHTML='';
		 dom.get("contractorBill_error").style.display='none';
		 return true;
    }
	
	function createStatDescTextboxFormatter(size,maxlength) {
		var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
		   var value = (YAHOO.lang.isValue(oData))?oData:"";
		   var fieldName = "actionStatutorydetails[" + oRecord.getCount() + "].egBillPayeeDtls." + oColumn.getKey();   
		   var id = oColumn.getKey()+oRecord.getId();
		   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+ "'/>";
		   el.innerHTML = markup; 
		}
		return textboxDescFormatter;	
	}
	
	function createStatAcntCodeFormatter(size,maxlength) {
		var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
		   var value = (YAHOO.lang.isValue(oData))?oData:"";
		   var fieldName = "actionStatutorydetails[" + oRecord.getCount() + "].egBillPayeeDtls.recovery.chartofaccounts." + oColumn.getKey(); 
		   var id = oColumn.getKey()+oRecord.getId();
		   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+ "' readonly='readonly'/>";
		   el.innerHTML = markup; 
		}
		return textboxDescFormatter;	
	}
	
	function createStatAcntCodeIDHiddenFormatter(size,maxlength) {
		var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
		   var value = (YAHOO.lang.isValue(oData))?oData:"";
		   var fieldName = "actionStatutorydetails[" + oRecord.getCount() + "].egBillPayeeDtls.recovery.chartofaccounts." + oColumn.getKey(); 
		   var id = oColumn.getKey()+oRecord.getId();
		   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+ "' readonly='readonly'/>";
		   el.innerHTML = markup; 
		}
		return textboxDescFormatter;	
	}
	
	var statAmountTextBoxFormatter = createStatAmountTextBoxFormatter(15,13);
	var statAmountTextBoxFormatterWhole = createStatAmountTextBoxFormatterWhole(15,13);
	var statDescTextboxFormatter=createStatDescTextboxFormatter(25,50);
	var statAcntCodeIDHiddenFormatter=createStatAcntCodeIDHiddenFormatter(20,50);
	var statAcntCodeFormatter=createStatAcntCodeFormatter(20,50);
	
var lang=YAHOO.lang;
function createDropdownFormatterForRecovery(listObj,postfixFieldValue, prefixkey){
    return function(el, oRecord, oColumn, oData) {
        var selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field),
            options = (lang.isArray(oColumn.dropdownOptions)) ?
                oColumn.dropdownOptions : null,
            selectEl,
            collection = el.getElementsByTagName("select");
        if(collection.length === 0) {
            selectEl = document.createElement("select");
            selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
            	selectEl.name = listObj+'['+oRecord.getCount()+'].'+prefixkey+'.'+oColumn.getKey()+'.'+postfixFieldValue;
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
	
    var statutoryDeductionsDataTable;
	var makeStatutoryDeductionsDataTable = function() {
			var statutoryDeductionsColumnDefs = [ 
				{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},				
				{key:"recovery", label:'Deduction Name', formatter:createDropdownFormatterForRecovery('actionStatutorydetails','id','egBillPayeeDtls'), dropdownOptions:deductionDropdownOptions},
				{key:"name", label:'Account Code and Head', formatter:statAcntCodeFormatter, sortable:false, resizeable:true},
				{key:"id", hidden:true,formatter:statAcntCodeIDHiddenFormatter, sortable:false, resizeable:true},
				{key:"subPartyType", label:'<s:text name="contractorBill.StatutoryDeductions.subPartyTypeLbl"/> <span class="mandatory">*</span>', formatter:createDropdownFormatter('actionStatutorydetails','id'), dropdownOptions:subPartyTypeDropdownOptions},
				{key:"typeOfWork",label:'<s:text name="contractorBill.StatutoryDeductions.typeOfWorkLbl"/>', formatter:createDropdownFormatter('actionStatutorydetails','id'), dropdownOptions:typeOfWorkDropdownOptions},
				{key:"creditAmount",label:'Amount <span class="mandatory">*</span>', formatter:statAmountTextBoxFormatterWhole,sortable:false, resizeable:false},
				{key:"narration", label:'Comments', formatter:statDescTextboxFormatter, sortable:false, resizeable:true},				
				{key:'delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
			];
			var statutoryDeductionsDataSource = new YAHOO.util.DataSource(); 
			statutoryDeductionsDataTable = new YAHOO.widget.DataTable("statutoryDeductionsTable",statutoryDeductionsColumnDefs, statutoryDeductionsDataSource, {MSG_EMPTY:"<s:text name='contractorBill.statutoryDeductions.initial.table.message'/>"});
			
			statutoryDeductionsDataTable.on('cellClickEvent',function (oArgs) {
				var target = oArgs.target;
				var record = this.getRecord(target);
				var column = this.getColumn(target);
				if (column.key == 'delete') { 		
						this.deleteRow(record);
						show_hide_dropDowns();
						calculateTotal();
						validateForStatutoryDeduction();
						allRecords=this.getRecordSet();
						for(i=0;i<allRecords.getLength();i++){
							this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
						}
				}        
			});
			
			statutoryDeductionsDataTable.on('dropdownChangeEvent', function (oArgs) {	
					
			    var record = this.getRecord(oArgs.target);
		        var column = this.getColumn(oArgs.target);
		        if(column.key=='recovery'){
		            var selectedIndex=oArgs.target.selectedIndex;
		            var row_index = this.getRecordSet().getRecordIndex(record);
		            
		    	    for(i=0;i<this.getRecordSet().getLength() && selectedIndex!=0;i++){
		    	       if(row_index!=i && deductionDropdownOptions[selectedIndex].value==dom.get("recovery" + this.getRecordSet().getRecord(i).getId()).value){
		    	       		dom.get("accountDetails_error").style.display=''
		    	       		dom.get("accountDetails_error").innerHTML='The Standard Deduction Type is already added';
		    	       		oArgs.target.selectedIndex=0;
		    	       		return;
		    	       }
		    	    }
		    	 		   
				   var automaticRecoveryPresent=false;
				   for(i=0;i<this.getRecordSet().getLength();i++){
				   		if(dom.get("recovery" + this.getRecordSet().getRecord(i).getId()).value!=0){
				   			if(deductionDropdownOptions[dom.get("recovery" + this.getRecordSet().getRecord(i).getId()).selectedIndex].mode=='M'){
								dom.get("subPartyType" + this.getRecordSet().getRecord(i).getId()).style.display="none";
				 	   			dom.get("typeOfWork" + this.getRecordSet().getRecord(i).getId()).style.display="none"; 
				 	   			dom.get("subPartyType" + this.getRecordSet().getRecord(i).getId()).value="0";
				 	   			dom.get("typeOfWork" + this.getRecordSet().getRecord(i).getId()).value="0";
				 	   			dom.get("creditAmount" + this.getRecordSet().getRecord(i).getId()).readOnly=false;  
				   			}
				   			else if(deductionDropdownOptions[dom.get("recovery" + this.getRecordSet().getRecord(i).getId()).selectedIndex].mode=='A'){
				   			  automaticRecoveryPresent=true;
				   			  <s:if test="%{showSubPartyType=='yes'}">
				   			 	 dom.get("subPartyType" + this.getRecordSet().getRecord(i).getId()).style.display="block";
				   			  </s:if>
				   			   <s:else>
				   			   	 dom.get("subPartyType" + this.getRecordSet().getRecord(i).getId()).value="0";
				   			 	 dom.get("subPartyType" + this.getRecordSet().getRecord(i).getId()).style.display="none";
				   			  </s:else>
				   			  
				   			  <s:if test="%{showTypeOfWork=='yes'}">
				 	   			dom.get("typeOfWork" + this.getRecordSet().getRecord(i).getId()).style.display="block"; 
				 	   		  </s:if>
				 	   		   <s:else>
				 	   		   	dom.get("typeOfWork" + this.getRecordSet().getRecord(i).getId()).value="0";
				 	   			dom.get("typeOfWork" + this.getRecordSet().getRecord(i).getId()).style.display="none"; 
				 	   		  </s:else>
				 	   		  
				 	   		  <s:if test="%{editStatutoryAmount=='yes'}">
				 	   		 	 dom.get("creditAmount" + this.getRecordSet().getRecord(i).getId()).readOnly=false; 
				 	   		  </s:if>
				 	   		  <s:else>
				 	   		 	 dom.get("creditAmount" + this.getRecordSet().getRecord(i).getId()).readOnly=true; 
				 	   		  </s:else>
				   			}
				   		}
				   		else{
				   				dom.get("subPartyType" + this.getRecordSet().getRecord(i).getId()).value="0";
				 	   			dom.get("typeOfWork" + this.getRecordSet().getRecord(i).getId()).value="0";
				   		}	
				   }
				   if(automaticRecoveryPresent==false){
				   		var value=statutoryDeductionsDataTable.getColumn('subPartyType');
						var index=value.getIndex();
						statutoryDeductionsDataTable.hideColumn(parseInt(index));
						
						var value=statutoryDeductionsDataTable.getColumn('typeOfWork');
						var index=value.getIndex();
						statutoryDeductionsDataTable.hideColumn(parseInt(index));
				   }
				   else if(automaticRecoveryPresent==true){
					   <s:if test="%{showSubPartyType=='yes'}">
							var value=statutoryDeductionsDataTable.getColumn('subPartyType');
							var index=value.getIndex();
							statutoryDeductionsDataTable.showColumn(parseInt(index));
						</s:if>
						<s:if test="%{showTypeOfWork=='yes'}">
							var value=statutoryDeductionsDataTable.getColumn('typeOfWork');
							var index=value.getIndex();
							statutoryDeductionsDataTable.showColumn(parseInt(index));
						</s:if>
				   }
		    	   	
		    	   	recoveryType=deductionDropdownOptions[selectedIndex].label;
		    	    dom.get("accountDetails_error").style.display='none';
		    	    dom.get("accountDetails_error").innerHTML='';
		            <s:iterator var="s" value="dropdownData.statutoryDeductionsList" status="status">    
				    	if(deductionDropdownOptions[selectedIndex].value=="<s:property value="%{id}" />"){
		            		this.updateCell(record,this.getColumn('name'),'<s:property value="%{chartofaccounts.glcode}" />-<s:property value="%{chartofaccounts.name}" />');
		            		this.updateCell(record,this.getColumn('id'),'<s:property value="%{chartofaccounts.id}" />');
		            	}
				    </s:iterator>  
				    
				    if(dom.get("recovery" + record.getId()).value==0){
	            		dom.get("name" + record.getId()).value="";
	            		
	            	}
		        }
	    	   	dom.get("creditAmount" + record.getId()).value="";
	    	   	calculateTotal();
		       if(dom.get("recovery" + record.getId()).value!=0){
		       	 recoveryType=deductionDropdownOptions[dom.get("recovery" + record.getId()).selectedIndex].label;
		       	 var recoveryMode=deductionDropdownOptions[dom.get("recovery" + record.getId()).selectedIndex].mode;
		      	 calculateStatutoryDeductionAmount(record,recoveryType,recoveryMode);
		      	}
		   	});	 
			<s:if test="%{showSubPartyType!='yes'}">
				var value=statutoryDeductionsDataTable.getColumn('subPartyType');
				var index=value.getIndex();
				statutoryDeductionsDataTable.hideColumn(parseInt(index));
			</s:if>
			<s:if test="%{showTypeOfWork!='yes'}">
				var value=statutoryDeductionsDataTable.getColumn('typeOfWork');
				var index=value.getIndex();
				statutoryDeductionsDataTable.hideColumn(parseInt(index));
			</s:if>
			
			return {
			    oDS: statutoryDeductionsDataSource,
			    oDT: statutoryDeductionsDataTable
			}; 
 	}
 	
 	function calculateStatutoryDeductionAmount(record,recoveryType,recoveryMode){
  		if(recoveryMode!='M'){
 			<s:if test="%{showSubPartyType=='yes'}">
	     		if(dom.get("subPartyType" + record.getId()).value=="0")
	     			return;
	   		</s:if>
	   			   			
	 	    var subPartyType  =dom.get("subPartyType" + record.getId()).value;
	 	    var docType=dom.get("typeOfWork" + record.getId()).value; 
	 	    var grossAmount;
	 	    
	 	    <s:if test="%{isRebatePremLevelBill=='yes'  && tenderResponse.tenderEstimate.tenderType==percTenderType}">
	   			grossAmount=dom.get("grossAmount").value;
	   		</s:if>
	   		<s:else>
	   			grossAmount=dom.get("billamount").value;
	   		</s:else>
	   		
	 	    var asOnDate=dom.get("billdate").value;
	 	   	var row_id=record.getId();
	  	    url = '${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!getStatutoryDeductionAmount.action';
			makeJSONCall(["xRowId","statutoryAmount","errorMsg"],url,
	    	{recoveryType:recoveryType,subPartyType:subPartyType,typeOfWork:docType,grossAmount:grossAmount,billDate:asOnDate,rowId:row_id},statutoryDeductionSuccessHandler,statutoryDeductionFailureHandler);
    	}
 	}
 	
 	statutoryDeductionSuccessHandler=function(req,res){
 		var results=res.results;
 		var row_id = results[0].xRowId;
	    if(!(results[0].errorMsg==""))
	    {
	    	<s:if test="%{editStatutoryAmount!='yes'}"> 
		   	 	dom.get("creditAmount" + row_id).readOnly=true;
		   	 </s:if>
		   	 <s:else>
		   	   dom.get("creditAmount" + row_id).readOnly=false;
		   	 </s:else>
	    
	    	dom.get("contractorBill_error").style.display="block";
    		dom.get('contractorBill_error').innerHTML=results[0].errorMsg; 
    		calculateTotal();
    		window.scroll(0,0);
    		return;
        } 
        dom.get('contractorBill_error').innerHTML="";
        dom.get("contractorBill_error").style.display="none";
        if(results[0].statutoryAmount=='-1'){
	   	 	dom.get("creditAmount" + row_id).readOnly=false; 
	   	}
	   	else{
	   		dom.get("creditAmount" + row_id).value=results[0].statutoryAmount;
		   	<s:if test="%{editStatutoryAmount!='yes'}"> 
		   	 	dom.get("creditAmount" + row_id).readOnly=true;
		   	 </s:if>
		   	 <s:else>
		   	   dom.get("creditAmount" + row_id).readOnly=false;
		   	 </s:else>
	   	 }
	    dom.get("creditAmount"+row_id).value=roundTo(dom.get("creditAmount"+row_id).value); 
	    calculateTotal();
 	}
 	
 	statutoryDeductionFailureHandler= function(){
	    dom.get("contractorBill_error").style.display="block";
	   	dom.get('contractorBill_error').innerHTML='<s:text name="contractorBill.statutoryDeductions.load.error" />'
	   	window.scroll(0,0);
   }
   
   /* To Load Statutory Deduction Amount onchange of Bill Date and Estimate Number(In case of workspackage) */
   function calculateStatutoryDeduction(){
	   var records = statutoryDeductionsDataTable.getRecordSet();
	   var recoveryMode;
	   for(i=0;i<records.getLength();i++){
	   		if(dom.get("recovery" + records.getRecord(i).getId()).value!=0){
	   			recoveryMode=deductionDropdownOptions[dom.get("recovery" + records.getRecord(i).getId()).selectedIndex].mode;
	   		  	recoveryType=deductionDropdownOptions[dom.get("recovery" + records.getRecord(i).getId()).selectedIndex].label;
   		   		calculateStatutoryDeductionAmount(records.getRecord(i),recoveryType,recoveryMode);	
	   		}
	   } 
   }
 	
 	function validateForStatutoryDeduction(){
	     var records = statutoryDeductionsDataTable.getRecordSet();
	     for(i=0;i<records.getLength();i++){ 
	     if(deductionDropdownOptions[dom.get("recovery" + records.getRecord(i).getId()).selectedIndex].mode!='M')
	     {
		      <s:if test="%{showSubPartyType=='yes'}">
		        	if(dom.get("subPartyType" + records.getRecord(i).getId()).value==0){
			        	dom.get("contractorBill_error").innerHTML='<s:text name="contractorBill.statutoryDeductions.subPartyType.null" />'; 
			          	dom.get("contractorBill_error").style.display='';
			       		window.scroll(0,0);
				        return false;
		        	}
					 
				</s:if>
   		 }
   			
	       if(dom.get("recovery" + records.getRecord(i).getId()).value!=0){     
	          if(dom.get("creditAmount" + records.getRecord(i).getId()).value=='' || dom.get("creditAmount" + records.getRecord(i).getId()).value==0){  
	          	dom.get("contractorBill_error").innerHTML='<s:text name="contractorBill.statutoryDeductions.creditamount.null" />'; 
	          	dom.get("contractorBill_error").style.display='';
	       		window.scroll(0,0);
		        return false;
	          } 
	       }
	       else
	       {
	       		dom.get("contractorBill_error").innerHTML='<s:text name="contractorBill.statutoryDeductions.type.null" />'; 
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;
	       } 
	     }
	     dom.get("contractorBill_error").style.display='none';
	     dom.get("contractorBill_error").innerHTML='';
	     return true;
    }
    
	function show_hide_dropDowns(){
	   var records = statutoryDeductionsDataTable.getRecordSet(); 
	   var recoveryMode;
	   var automaticRecoveryPresent=false;
	   for(i=0;i<records.getLength();i++){
	   		if(dom.get("recovery" + records.getRecord(i).getId()).value!=0){
	   			if(deductionDropdownOptions[dom.get("recovery" + records.getRecord(i).getId()).selectedIndex].mode=='M'){
					dom.get("subPartyType" + records.getRecord(i).getId()).style.display="none";
	 	   			dom.get("typeOfWork" + records.getRecord(i).getId()).style.display="none";
	 	   			dom.get("creditAmount" + records.getRecord(i).getId()).readOnly=false; 
	   			}
	   			else if(deductionDropdownOptions[dom.get("recovery" + records.getRecord(i).getId()).selectedIndex].mode=='A'){
	   			  automaticRecoveryPresent=true;
	   			}
	   		}
	   		else{
	   				dom.get("subPartyType" + records.getRecord(i).getId()).value="0";
	 	   			dom.get("typeOfWork" + records.getRecord(i).getId()).value="0";
	   		}
	   }
	   if(automaticRecoveryPresent==false && records.getLength()!=0){ 
   			var value=statutoryDeductionsDataTable.getColumn('subPartyType');
			var index=value.getIndex();
			statutoryDeductionsDataTable.hideColumn(parseInt(index));
			
			var value=statutoryDeductionsDataTable.getColumn('typeOfWork');
			var index=value.getIndex();
			statutoryDeductionsDataTable.hideColumn(parseInt(index));
	   }
	   
     else if(automaticRecoveryPresent==true){
		   <s:if test="%{showSubPartyType=='yes'}">
				var value=statutoryDeductionsDataTable.getColumn('subPartyType');
				var index=value.getIndex();
				statutoryDeductionsDataTable.showColumn(parseInt(index));
			</s:if>
			<s:if test="%{showTypeOfWork=='yes'}">
				var value=statutoryDeductionsDataTable.getColumn('typeOfWork');
				var index=value.getIndex(); 
				statutoryDeductionsDataTable.showColumn(parseInt(index));
			</s:if>
	   }
	}
    
</script>

	<tr>
	  	<td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
	   		<div class="headplacer"><s:text name="contractorBill.subheader.statutorydedcution" /></div>
		</td>
		<td align="right" class="headingwk" style="border-left-width: 0px">
	      <a id="statutorydeductionrow" href="#" onclick="statutoryDeductionsDataTable.addRow({SlNo:statutoryDeductionsDataTable.getRecordSet().getLength()+1});return false;"><img height="16" border="0" width="16" alt="Add Statutory-Deductions" src="/egworks/resources/erp2/images/add.png" /></a>
	    </td>
	</tr>
	<tr>
      <td colspan="4">
         <div class="yui-skin-sam">
           <div id="statutoryDeductionsTable"></div>    
         </div>
      </td>
    </tr>
  
 	<!--  populating data in to data table data  -->
 	<script>
		makeStatutoryDeductionsDataTable();
		 <s:iterator id="actionStatutorydetailsListiterator" value="StatutoryDeductions" status="row_status">
		 		statutoryDeductionsDataTable.addRow({SlNo:'<s:property value="#row_status.count"/>',
            				recovery:'<s:property value="egBillPayeeDtls.recovery.id"/>',
            				id:'<s:property value="egBillPayeeDtls.recovery.chartofaccounts.id"/>',
            				name:'<s:property value="egBillPayeeDtls.recovery.chartofaccounts.glcode"/> - <s:property value="egBillPayeeDtls.recovery.chartofaccounts.name"/>',
            				subPartyType:'<s:property value="subPartyType.id"/>',
            				typeOfWork:'<s:property value="typeOfWork.id"/>',
                           	creditAmount:'<s:property value="egBillPayeeDtls.creditAmount"/>',
                            narration:'<s:property value="egBillPayeeDtls.narration"/>',
                            Delete:'X'});
		  
		 </s:iterator>
		 show_hide_dropDowns();
	</script> 
	


