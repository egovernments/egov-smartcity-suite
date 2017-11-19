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
.yui-dt0-col-TaxAmt {
  width: 5%;
}
.yui-dt-col-Percentage{
	text-align:right;
}

</style> 
<script>
var fundSourceDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="fundSourceList" status="status">  
    {"label":"<s:property value="%{name}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
    
function validateFinancialSourceForm(){
	var totalPercentage=calculateTotal();
	if(getNumber(totalPercentage)>100){
	    document.getElementById("finSource_error").style.display='';
  		document.getElementById("finSource_error").innerHTML='The Total Percentage cannot be greater than 100'; 
     	return false;
    }
	return true;
}       
       
function calculateTotalPercentage(elem,recordId){
	record=financialSourceDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
 	if(isNaN(elem.value) || getNumber(elem.value)<0){
		dom.get('error'+elem.id).style.display='';
		return;
  	}
  	if(getNumber(dom.get("Percentage"+record.getId()).value)>100){
		dom.get('error'+elem.id).style.display='';
		return;
  	}

	var totalPercentage=calculateTotal();
	
	if(getNumber(totalPercentage)>100){
	    document.getElementById("finSource_error").style.display='';
  		document.getElementById("finSource_error").innerHTML='The Total Percentage cannot be greater than 100'; 
     	return;
    }
    else if(getNumber(totalPercentage)>100 && document.getElementById("finSource_error").innerHTML=='The Total Percentage cannot be greater than 100'){
    	document.getElementById("finSource_error").innerHTML='';   
    	document.getElementById("finSource_error").style.display='none';
    }
	dom.get("totalPercentage").innerHTML=totalPercentage;	  		
}

function calculateTotal(){
	var records= financialSourceDataTable.getRecordSet();
	var totalPercentage=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("Percentage"+records.getRecord(i).getId()).value!=0.0){
			totalPercentage=  totalPercentage + eval(getNumber(dom.get("Percentage"+records.getRecord(i).getId()).value));
		}
	}
	if(totalPercentage<=100 && document.getElementById("finSource_error").innerHTML=='The Total Percentage cannot be greater than 100'){
		document.getElementById("finSource_error").style.display='none';
		document.getElementById("finSource_error").innerHTML='';
	}
	return totalPercentage;
}

function createTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();    
	    var fieldName = "financingSourceList[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onkeyup='validateDecimal(this)' onblur='calculateTotalPercentage(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(5,5);

function createFundSourceIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "financingSourceList[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var fundSourceIdHiddenFormatter= createFundSourceIDFormatter(10,10);

var financialSourceDataTable;
var makeFinancialSourceDataTable= function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var financialSourceColumnDefs = [		
		{key:"fundSource", hidden:true, formatter:fundSourceIdHiddenFormatter, sortable:false, resizeable:true},
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"Name", label:'<span class="mandatory">*</span>Source Name', formatter:createDropdownFormatter('financingSourceList','id'), dropdownOptions:fundSourceDropdownOptions, width : 275},		
		{key:"Percentage",label:'<span class="mandatory">*</span>Percentage',formatter:textboxFormatter, sortable:false, resizeable:false, width : 275},
	 <s:if test="%{!appConfigValuesToSkipBudget.contains(abstractEstimate.type.name)}">
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}"),width : 83},
	 </s:if>
	 	{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}"), width : 82}
	];
		
	var financialSourceDataSource = new YAHOO.util.DataSource(); 
	financialSourceDataTable= new YAHOO.widget.DataTable("financialSourceTable",financialSourceColumnDefs, financialSourceDataSource , {initialRequest:"query=orders&results=10"});	
	financialSourceDataTable.subscribe("cellClickEvent", financialSourceDataTable.onEventShowCellEditor); 
	financialSourceDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
		   financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1});
		}

if(column.key == 'Name'){ 

<s:if test="%{model.id !=null}">
	if(record.getCount() == 1){
		alert('can not be changed');
    }
</s:if>
<s:else>
	if(record.getCount() == 0){
		alert('can not be changed');
	}
</s:else>

}
		if (column.key == 'Delete') { 			
			if(this.getRecordSet().getLength()>1){	
			<s:if test="%{model.id !=null}">
			    if(record.getCount() == 1){
				alert('can not be deleted');
        		    }else{
				this.deleteRow(record);
				var totalPerc=calculateTotal();					
				dom.get("totalPercentage").innerHTML=totalPerc;						
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
				   this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			    }
			</s:if>
			<s:elseif test="%{model.id ==null}">
			   if(record.getCount() == 0){
				alert('can not be deleted');
			   }else{
				this.deleteRow(record);
				var totalPerc=calculateTotal();					
				dom.get("totalPercentage").innerHTML=totalPerc;						
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
				   this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			   }
			</s:elseif>
			<s:else>
				this.deleteRow(record);
				var totalPerc=calculateTotal();					
				dom.get("totalPercentage").innerHTML=totalPerc;						
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
				   this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			</s:else>

			}
			else
			{
				alert("This row can not be deleted");
			}
		}        
	});	
	
	financialSourceDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='Name'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
    	    validateDuplicate(this.getRecordSet(),oArgs); 
        	this.updateCell(record,this.getColumn('fundSource'),fundSourceDropdownOptions[selectedIndex].value);
        }
	});
	
	<!-- disabling 1st fund source -->
	
	if(document.getElementById('testError')==null) {
		financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1,Name:"${model.abstractEstimate.fundSource.id}",fundSource:"${model.abstractEstimate.fundSource.id}"});
	}
	else if(document.getElementById('testError')!=null && document.getElementById('testError').value!='<s:text name="financingsource.fundsource.null" />') {
		financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1,Name:"${model.abstractEstimate.fundSource.id}",fundSource:"${model.abstractEstimate.fundSource.id}"});
	}
	else 
		financialSourceDataTable.addRow({SlNo:financialSourceDataTable.getRecordSet().getLength()+1,Name:"0",fundSource:"0"});
	
	dom.get("Nameyui-rec0").disabled=true;
    dom.get("Nameyui-rec0").readonly=true;	
	dom.get("Percentage"+financialSourceDataTable.getRecordSet().getRecord(0).getId()).value="100";
	
	var tfoot = financialSourceDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 2;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'Total';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'totalPercentage','');
	addCell(tr,3,'filler','');
	 <s:if test="%{!appConfigValuesToSkipBudget.contains(abstractEstimate.type.name)}">
	addCell(tr,4,'filler',''); 	
	</s:if>
	return {
	    oDS: financialSourceDataSource,
	    oDT:financialSourceDataTable
	};    
}

function validateDuplicate(records,oArgs){
    for(i=0;i<records.getLength();i++){ 
     var selectedIndex=oArgs.target.selectedIndex;
       if(dom.get("fundSource"+records.getRecord(i).getId()).value==fundSourceDropdownOptions[selectedIndex].value){
          document.getElementById("finSource_error").innerHTML='The Source name '+fundSourceDropdownOptions[selectedIndex].label+' is already selected'; 
          document.getElementById("finSource_error").style.display='';
          oArgs.target.selectedIndex=0;
          return;
       } 
       else{
       	 document.getElementById("finSource_error").style.display='none';
       	 document.getElementById("finSource_error").innerHTML='';	
       	 }
    }
}

</script>
        <br/>
        <s:iterator value="getFieldErrors().entrySet()" var="entry">
		    <s:iterator value="#entry.value">
				<input type="hidden" name="testError" id="testError" value='<s:property value="%{top}"/>'/>	
		     </s:iterator>
		 </s:iterator>
		<table id="financialSourceHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
		 <div class="errorstyle" id="finSource_error" style="display:none;"></div>
              	<tr>
                	<td colspan="4" class="headingwk">
                		<div class="arrowiconwk"><image src="<egov:url path='/resources/erp2/images/arrow.gif'/>" /></div>
                		<div class="headplacer"><s:text name='page.header.financial.source.detail'/></div>
                	</td>
              	</tr>
              	<tr>
                	<td colspan="5">
                	<div class="yui-skin-sam">
                    	<div id="financialSourceTable"></div>
                    	<div id="financialSourceTotals"></div>  
                	</div>
                	</td>
                </tr>
                
                <tr>
                <td  colspan="5" class="shadowwk"></td>
                </tr>
		</table> 
	<script>
		makeFinancialSourceDataTable();
		<s:if test="%{source == 'UpdateFinancialDetail'}">

             financialSourceDataTable.updateRow(0,
                                   {fundSource:"${model.abstractEstimate.fundSource.id}",
                                    SlNo:1,
                                    Name:"${model.abstractEstimate.fundSource.name}",   
                                    Percentage:100,
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});

	        var record = financialSourceDataTable.getRecord(parseInt('0'));
	    
	        var column = financialSourceDataTable.getColumn('Name');

			for(i=0; i < fundSourceDropdownOptions.length; i++) {
	            if (fundSourceDropdownOptions[i].value == "${model.abstractEstimate.fundSource.id}") {
	                financialSourceDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
	            }
	        } 

	        column = financialSourceDataTable.getColumn('Percentage');  
        	dom.get(column.getKey()+record.getId()).value = 100;

	        calculateTotalPercentage(dom.get(column.getKey()+record.getId()), record.getId());

		</s:if>
		<s:else>
        <s:iterator id="finSourceIterator" value="financingSources" status="row_status">
          <s:if test="#row_status.count == 1">
             financialSourceDataTable.updateRow(0,
                                   {fundSource:'<s:property value="fundSource.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Name:'<s:property value="fundSource.name"/>',   
                                    Percentage:'<s:property value="percentage"/>',
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
          </s:if>
          <s:else>
             financialSourceDataTable.addRow(
                                   {fundSource:'<s:property value="fundSource.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Name:'<s:property value="fundSource.name"/>',   
                                    Percentage:'<s:property value="percentage"/>',
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
          </s:else>
          
        var record = financialSourceDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = financialSourceDataTable.getColumn('Name');
        for(i=0; i < fundSourceDropdownOptions.length; i++) {
            if (fundSourceDropdownOptions[i].value == '<s:property value="fundSource.id"/>') {
                financialSourceDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
            }
        } 
 
        var column = financialSourceDataTable.getColumn('Percentage');  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage"/>';
        calculateTotalPercentage(dom.get(column.getKey()+record.getId()), record.getId());
        </s:iterator>
       </s:else>
	</script>       
