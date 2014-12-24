<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">
#yui-dt0-bodytable, #yui-dt1-bodytable, #yui-dt2-bodytable {
    Width:100%;
} 
.yui-dt0-col-TaxAmt {
  width: 5%;
}
.yui-dt-col-Percentage{
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
	var totalPercentage=calculateTotalPer();
	if(getNumber(totalPercentage)>100){
	    document.getElementById("multiyear_error").style.display='';
  		document.getElementById("multiyear_error").innerHTML='The Total Percentage cannot be greater than 100'; 
     	return false;
    }
	return true;
}       
       
function calculateTotalPercentage(elem,recordId){
	record=multiYearEstimateDataTable.getRecord(recordId);
	dom.get('error'+elem.id).style.display='none';
 	if(isNaN(elem.value) || getNumber(elem.value)<0){
		dom.get('error'+elem.id).style.display='';
		return;
  	}
  	if(getNumber(dom.get("Percentage"+record.getId()).value)>100){
		dom.get('error'+elem.id).style.display='';
		return;
  	}

	var totalPercentage=calculateTotalPer();
	
	if(getNumber(totalPercentage)>100){
	    
	    document.getElementById("multiyear_error").style.display='';
  		document.getElementById("multiyear_error").innerHTML='The Total Percentage cannot be greater than 100'; 
     	return;
    }
    else if(getNumber(totalPercentage)>100 && document.getElementById("multiyear_error").innerHTML=='The Total Percentage cannot be greater than 100'){
    	document.getElementById("multiyear_error").innerHTML='';   
    	document.getElementById("multiyear_error").style.display='none';
    }
	dom.get("totalPercentage").innerHTML=totalPercentage;	  		
}

function calculateTotalPer(){
	var records= multiYearEstimateDataTable.getRecordSet();
	var totalPercentage=0.0; 
	for(i=0;i<records.getLength();i++){
		if(dom.get("Percentage"+records.getRecord(i).getId()).value!=0.0){
			totalPercentage=  totalPercentage + eval(getNumber(dom.get("Percentage"+records.getRecord(i).getId()).value));
		}
	}
	if(totalPercentage<=100 && document.getElementById("multiyear_error").innerHTML=='The Total Percentage cannot be greater than 100'){
		document.getElementById("multiyear_error").style.display='none';
		document.getElementById("multiyear_error").innerHTML='';
	}
	return totalPercentage;
}

function createTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();    
	    var fieldName = "actionMultiYearEstimateValues[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='text' class='selectamountwk' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' class='selectamountwk' onblur='calculateTotalPercentage(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var textboxFormatter = createTextBoxFormatter(5,5);

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
		{key:"Year", label:'Year', formatter:"dropdown", dropdownOptions:financialYearDropdownOptions},		
		{key:"Percentage",label:'Percentage',formatter:textboxFormatter, sortable:false, resizeable:false},
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
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
					var totalPerc=calculateTotalPer();					
					dom.get("totalPercentage").innerHTML=totalPerc;						
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
    	    validateDuplicateYear(this.getRecordSet(),oArgs); 
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
			
	dom.get("Percentage"+multiYearEstimateDataTable.getRecordSet().getRecord(0).getId()).value="100";
	
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
	addCell(tr,2,'totalPercentage','100');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler',''); 	

	return {
	    oDS: multiYearEstimateDataSource,
	    oDT: multiYearEstimateDataTable
	};        

}

function validateDuplicateYear(records,oArgs){
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
                	<td colspan="5" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div><div class="headplacer"><s:text name="multiYearEstimate.yearwiseEstimate" /></div></td>
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
        <s:iterator id="multiYearIterator" value="multiYearEstimates" status="row_status">
          <s:if test="#row_status.count == 1">
             multiYearEstimateDataTable.updateRow(0,
                                   {financialYear:'<s:property value="financialYear.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Year:'<s:property value="financialYear.finYearRange"/>',   
                                    Percentage:'<s:property value="percentage"/>',
                                    Add:createAddImageFormatter("${pageContext.request.contextPath}"),
                                    Delete:createDeleteImageFormatter("${pageContext.request.contextPath}")});
          </s:if>
          <s:else>
             multiYearEstimateDataTable.addRow(
                                   {financialYear:'<s:property value="financialYear.id"/>',
                                    SlNo:'<s:property value="#row_status.count"/>',
                                    Year:'<s:property value="financialYear.finYearRange"/>',   
                                    Percentage:'<s:property value="percentage"/>',
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
 
        var column = multiYearEstimateDataTable.getColumn('Percentage');  
        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage"/>';
        calculateTotalPercentage(dom.get(column.getKey()+record.getId()), record.getId());
        </s:iterator>		
	</script>       

