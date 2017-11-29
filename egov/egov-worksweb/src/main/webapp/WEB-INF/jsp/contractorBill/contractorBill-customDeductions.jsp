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
.yui-dt-col-debitamount{
	text-align:right;
}
</style>
<script>
	var custDeductionDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="customDeductionAccountList" status="status">    
    {"label":"<s:property value="%{glcode}"/>-<s:property value="%{name}"/>",
    "value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
        
    function createCustAmountTextboxFormatter(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		    var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "customDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
		    markup="<input type='text' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='validateCustAmount(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
    function createCustAmountTextboxFormatterWhole(size,maxlength){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
		    var value = (YAHOO.lang.isValue(oData))?oData:"";
		    var id=oColumn.getKey()+oRecord.getId();
		    var fieldName = "customDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();
		    markup="<input type='text' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='roundToRupees(this);validateCustAmount(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		    el.innerHTML = markup;
		}
		return textboxFormatter;
	}
	var custAmountTextboxFormatter = createCustAmountTextboxFormatter(15,13);
	var custAmountTextboxFormatterWhole = createCustAmountTextboxFormatterWhole(15,13);
	
	function validateCustAmount(elem,recordId){
	record=customDeductionsDataTable.getRecord(recordId);
      if(!validateNumberInTableCell(customDeductionsDataTable,elem,recordId)) return;
      	dom.get("creditamount"+record.getId()).value =  roundTo(dom.get("creditamount"+record.getId()).value);
      calculateTotal();
    }
	
	function createCustDescTextboxFormatter(size,maxlength) {
		var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
		   var value = (YAHOO.lang.isValue(oData))?oData:"";
		   var fieldName = "customDeductions[" + oRecord.getCount() + "]." + oColumn.getKey();   
		   var id = oColumn.getKey()+oRecord.getId();
		   markup="<input type='text' id='"+id+"' class='selectmultilinewk' value='"+value+"' size='"+size+"' maxlength='"+maxlength+"' name='"+fieldName+ "'/>";
		   el.innerHTML = markup; 
		}
		return textboxDescFormatter;	
	}
	var custDescTextboxFormatter = createCustDescTextboxFormatter(25,50);
    
  
    
    var customDeductionsDataTable;
	var makeCustomDeductionsDataTable = function() {
			var customDeductionsColumnDefs = [ 
				{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},				
				{key:"glcodeid", label:'<s:text name="custDedcution.column.accCode"/>', formatter:createDropdownFormatter('customDeductions',''), dropdownOptions:custDeductionDropdownOptions},
				{key:"creditamount",label:'<s:text name="custDedcution.column.amount"/><span class="mandatory">*</span>', formatter:custAmountTextboxFormatterWhole,sortable:false, resizeable:false},
				{key:"narration", label:'<s:text name="custDedcution.column.comments"/>', formatter:custDescTextboxFormatter, sortable:false, resizeable:true},				
				{key:'delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
			];
			var customDeductionsDataSource = new YAHOO.util.DataSource(); 
			customDeductionsDataTable = new YAHOO.widget.DataTable("customDeductionsTable",customDeductionsColumnDefs, customDeductionsDataSource, {MSG_EMPTY:"<s:text name='contractorBill.customdedcution.initial.table.message'/>"});
			customDeductionsDataTable.on('cellClickEvent',function (oArgs) {
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
			customDeductionsDataTable.on('dropdownChangeEvent',function (oArgs) {
    		var record = this.getRecord(oArgs.target);
    		var column = this.getColumn(oArgs.target);
	   		if(column.key=='glcodeid'){
	   			// Check for duplicate custom deduction 
	    	    var selectedIndex=oArgs.target.selectedIndex;
	    	    var row_index = this.getRecordSet().getRecordIndex(record);
	    	    for(i=0;i<this.getRecordSet().getLength() && selectedIndex!=0;i++){
	    	       if(row_index!=i && custDeductionDropdownOptions[selectedIndex].value==dom.get("glcodeid" + this.getRecordSet().getRecord(i).getId()).value){
	    	       		dom.get("accountDetails_error").style.display=''
	    	       		dom.get("accountDetails_error").innerHTML='The Custom Deduction Type is already added';
	    	       		oArgs.target.selectedIndex=0;
	    	       		return;
	    	       }
	    	    }
	    	    dom.get("accountDetails_error").style.display='none'
	    	    dom.get("accountDetails_error").innerHTML=''
	    	    }
    		});		
			return {
			    oDS: customDeductionsDataSource,
			    oDT: customDeductionsDataTable
			}; 
 	}
 	
 	function validateForCustDeduction(){
	     var records = customDeductionsDataTable.getRecordSet();
	     var error;
	     for(i=0;i<records.getLength();i++){ 
	       if(dom.get("glcodeid" + records.getRecord(convertToIntNumber(i)).getId()).value!=0){           
	          if(dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value=='' || dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value==0){  
	          	error='<s:text name="contractorBill.create.customDeduction.amountRequired"/>';
	       		error=error+" for Row"+eval(i+1);
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;
	          }
	       }else if(dom.get("creditamount" + records.getRecord(convertToIntNumber(i)).getId()).value!=''){
	       		error='<s:text name="contractorBill.create.customDeduction.deductionTypeRequired"/>';
	       		error=error+" for Row"+eval(i+1);
	       		dom.get("contractorBill_error").innerHTML=error;
	          	dom.get("contractorBill_error").style.display='';
	          	window.scroll(0,0);
		        return false;		
	       } 
	       else{
	       		var error='<s:text name="contractorBill.create.emptyCustomDeductionDetails"/>';
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
 	
</script>

	<tr>
	  	<td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
	   		<div class="headplacer"><s:text name="contractorBill.subheader.customdedcution" /></div>
		</td>
		<td align="right" class="headingwk" style="border-left-width: 0px">
       		<a id="customdeductionrow" href="#" onclick="customDeductionsDataTable.addRow({SlNo:customDeductionsDataTable.getRecordSet().getLength()+1});return false;">
       		<img border="0" alt="Add Custom Deduction" src="/egworks/resources/erp2/images/add.png" /></a>
       	</td>
	</tr>
	<tr>
      <td colspan="4">
         <div class="yui-skin-sam">
           <div id="customDeductionsTable"></div>    
         </div>
      </td>
    </tr>
  

 	<!--  populating data in to data table data  -->
 	<script>
		makeCustomDeductionsDataTable();
		  <s:iterator id="customDeductionsListiterator" value="CustomDeductionTypes" status="row_status">
		              customDeductionsDataTable.addRow({SlNo:'<s:property value="#row_status.count"/>',
                      					    glcodeid:'<s:property value="glcodeid"/>',
		                                    creditamount:'<s:property value="creditamount"/>',
		                                    narration:'<s:property value="narration"/>',
		                                    Delete:'X'});
		 
		 </s:iterator>
		 
	</script> 


