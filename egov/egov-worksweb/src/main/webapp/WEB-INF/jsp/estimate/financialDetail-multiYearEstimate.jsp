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
    Width:100%;
} 
.yui-dt0-col-TaxAmt {
  width: 5%;
}
.yui-dt-col-percentage{
	text-align:right;
}

</style> 
<script>
var financialYearDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.financialYearList" status="status">  
    {"label":"<s:property value="%{finYearRange}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
function validateMultiYearEstimateForm(){
	var totalMultiyearPercentage=calculateTotal();
	if(getNumber(totalMultiyearPercentage)>100){
	    document.getElementById("multiyear_error").style.display='';
  		document.getElementById("multiyear_error").innerHTML='The Total Percentage cannot be greater than 100'; 
     	return false;
    }
	return true;
}       
       
function calculateTotalMultiyearPercentage(elem,recordId){
	record=multiYearEstimateDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
 	if(isNaN(elem.value) || getNumber(elem.value)<0){
		dom.get('error'+elem.id).style.display='';
		return;
  	}
  	if(getNumber(dom.get("percentage"+record.getId()).value)>100){
		dom.get('error'+elem.id).style.display='';
		return;
  	}

	var totalMultiyearPercentage=calculateTotal();
	
	if(getNumber(totalMultiyearPercentage)>100){
	    
	    document.getElementById("multiyear_error").style.display='';
  		document.getElementById("multiyear_error").innerHTML='The Total Percentage cannot be greater than 100';
  		dom.get("totalMultiyearPercentage").innerHTML=totalMultiyearPercentage;	 
     	return;
    }
    else if(getNumber(totalMultiyearPercentage)>100 && document.getElementById("multiyear_error").innerHTML=='The Total Percentage cannot be greater than 100'){
    	document.getElementById("multiyear_error").innerHTML='';   
    	document.getElementById("multiyear_error").style.display='none';
    }
	dom.get("totalMultiyearPercentage").innerHTML=totalMultiyearPercentage;	  		
}

function calculateTotal(){
	var records= multiYearEstimateDataTable.getRecordSet();
	var totalMultiyearPercentage=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("percentage"+records.getRecord(i).getId()).value!=0.0){
			totalMultiyearPercentage=  totalMultiyearPercentage + eval(getNumber(dom.get("percentage"+records.getRecord(i).getId()).value));
		}
	}
	if(totalMultiyearPercentage<=100 && document.getElementById("multiyear_error").innerHTML=='The Total Percentage cannot be greater than 100'){
		document.getElementById("multiyear_error").style.display='none';
		document.getElementById("multiyear_error").innerHTML='';
	}
	return totalMultiyearPercentage;
}

function createMultiyearPercFormatter(size,maxlength){
	var multiyearPercFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();    
	    var fieldName = "actionMultiYearEstimateValues[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='calculateTotalMultiyearPercentage(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return multiyearPercFormatter;
}
var multiyearPercFormatter = createMultiyearPercFormatter(5,5);



function createFinancialYearIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "actionMultiYearEstimateValues[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var financialYearIdHiddenFormatter= createFinancialYearIDFormatter(10,10);

var multiYearEstimateDataTable;
var makeMultiYearEstimateDataTable= function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var multiYearEstimateColumnDefs = [		
		{key:"financialYear", hidden:true, formatter:financialYearIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"Year", label:'Year', formatter:"dropdown", dropdownOptions:financialYearDropdownOptions, width : 275},	
		{key:"percentage",label:'Percentage',formatter:multiyearPercFormatter, sortable:false, resizeable:false, width : 275},
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}"),width : 83},
		{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}"), width : 82}
	];
		
	var multiYearEstimateDataSource = new YAHOO.util.DataSource(); 
	multiYearEstimateDataTable= new YAHOO.widget.DataTable("multiYearEstimateTable",multiYearEstimateColumnDefs, multiYearEstimateDataSource , {initialRequest:"query=orders&results=10"});	
	multiYearEstimateDataTable.subscribe("cellClickEvent", multiYearEstimateDataTable.onEventShowCellEditor); 
	multiYearEstimateDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			multiYearEstimateDataTable.addRow({SlNo:multiYearEstimateDataTable.getRecordSet().getLength()+1});
		}

		if (column.key == 'Delete') { 			
			if(this.getRecordSet().getLength()>1){	
					this.deleteRow(record);
					var totalPerc=calculateTotal();					
					dom.get("totalMultiyearPercentage").innerHTML=totalPerc;						
					allRecords=this.getRecordSet();
					for(i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			}
			else
			{
				alert("This row can not be deleted");
			}

		}        
	});	
	
	multiYearEstimateDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='Year'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
    	    validateDuplicate(this.getRecordSet(),oArgs); 
        	this.updateCell(record,this.getColumn('financialYear'),financialYearDropdownOptions[selectedIndex].value);
        }
	});
	if(document.getElementById('abc')==null) {
		multiYearEstimateDataTable.addRow({SlNo:multiYearEstimateDataTable.getRecordSet().getLength()+1,Year:"${currentFinancialYearId}",financialYear:"${currentFinancialYearId}"});
	}
	else if(document.getElementById('abc')!=null && document.getElementById('abc').value!='<s:text name="multiYeareEstimate.financialYear.null" />') {
		multiYearEstimateDataTable.addRow({SlNo:multiYearEstimateDataTable.getRecordSet().getLength()+1,Year:"${currentFinancialYearId}",financialYear:"${currentFinancialYearId}"});
	}
	else 
		multiYearEstimateDataTable.addRow({SlNo:multiYearEstimateDataTable.getRecordSet().getLength()+1,Year:"0",financialYear:"0"});
			
	dom.get("percentage"+multiYearEstimateDataTable.getRecordSet().getRecord(0).getId()).value="100";
	
	var tfoot = multiYearEstimateDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 2;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'Total';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'totalMultiyearPercentage','100');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler',''); 	

	return {
	    oDS: multiYearEstimateDataSource,
	    oDT: multiYearEstimateDataTable
	};        

}

function validateDuplicate(records,oArgs){
    for(i=0;i<records.getLength();i++){ 
     var selectedIndex=oArgs.target.selectedIndex;
       if(dom.get("financialYear"+records.getRecord(i).getId()).value==financialYearDropdownOptions[selectedIndex].value){
          document.getElementById("multiyear_error").innerHTML='The Year '+financialYearDropdownOptions[selectedIndex].label+' is already selected'; 
          document.getElementById("multiyear_error").style.display='';
          oArgs.target.selectedIndex=0;
          return;
       } 
       else if(dom.get("financialYear"+records.getRecord(i).getId()).value=='' && financialYearDropdownOptions[selectedIndex].value=="${currentFinancialYearId}" && i==0){
       	  document.getElementById("multiyear_error").innerHTML='The Year '+financialYearDropdownOptions[selectedIndex].label+' is already selected'; 
          document.getElementById("multiyear_error").style.display='';
          oArgs.target.selectedIndex=0;
          return;
       }       
       else{
       	 document.getElementById("multiyear_error").style.display='none';
       	 document.getElementById("multiyear_error").innerHTML='';	
       	 }
    }
}

</script>

        <div class="errorstyle" id="multiyear_error" style="display:none;"></div>
        <s:iterator value="getFieldErrors().entrySet()" var="entry">
		    <s:iterator value="#entry.value">
				<input type="hidden" name="abc" id="abc" value='<s:property value="%{top}"/>'/>	
		     </s:iterator>
		 </s:iterator>
		<table id="multiYearHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
              	<tr>
                	<td colspan="5" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/resources/erp2/images/arrow.gif'/>" /></div><div class="headplacer"><s:text name="multiYearEstimate.yearwiseEstimate" /></div></td>
              	</tr>
              	<tr>
                	<td colspan="5">
                	<div class="yui-skin-sam">
                    	<div id="multiYearEstimateTable"></div>
                    	<div id="multiYearEstimateTotals"></div>  
                	</div>
                	</td>
                </tr>
                 <tr>
                	<td colspan="5" class="shadowwk"></td>
                </tr>
		</table> 
	<script>
		makeMultiYearEstimateDataTable();
        <s:iterator id="multiYearIterator" value="abstractEstimate.multiYearEstimates" status="row_status">
          <s:if test="#row_status.count == 1">
             multiYearEstimateDataTable.updateRow(0,
                                   {financialYear:'<s:property value="financialYear.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Year:'<s:property value="financialYear.finYearRange"/>',   
                                    percentage:'<s:property value="percentage"/>',
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
          </s:if>
          <s:else>
             multiYearEstimateDataTable.addRow(
                                   {financialYear:'<s:property value="financialYear.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Year:'<s:property value="financialYear.finYearRange"/>',   
                                    percentage:'<s:property value="percentage"/>',
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
          </s:else>
          
        var record = multiYearEstimateDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
    
        var column = multiYearEstimateDataTable.getColumn('Year');
        for(i=0; i < financialYearDropdownOptions.length; i++) {
            if (financialYearDropdownOptions[i].value == '<s:property value="financialYear.id"/>') {
                multiYearEstimateDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
            }
        } 
 
        var column = multiYearEstimateDataTable.getColumn('percentage');  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage"/>';
        calculateTotalMultiyearPercentage(dom.get(column.getKey()+record.getId()), record.getId());
        </s:iterator>		
	</script>       
