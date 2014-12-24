<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">

.yui-dt table{
  width:100%;
}
.yui-dt-col-Add{
  width:5%;
}
.yui-dt-col-Delete{
  width:5%;
}

</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>

function createContractorIDFormatter(el, oRecord, oColumn){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = oColumn.getKey();
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}	
	return hiddenFormatter;
}
var contractorIDFormatter = createContractorIDFormatter(10,10);

function createContractorNameTextboxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = oColumn.getKey();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' disabled='true' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var contractorNameTextboxFormatter = createContractorNameTextboxFormatter(45,100);

function createContractorCodeTextboxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = oColumn.getKey();
    markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' disabled='true' size='"+size+"' maxlength='"+maxlength+"' class='selectwk' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
}
var contractorCodeTextboxFormatter = createContractorCodeTextboxFormatter(14,22);

var contractorsDataTable;
var makeContractorsDataTable = function() {
	var contractorColumns = [ 
		{key:"contractor", hidden:true, formatter:contractorIDFormatter, sortable:false, resizeable:false} ,
		{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
		{key:"code",label:'<s:text name="column.title.code"/>', formatter:contractorCodeTextboxFormatter,sortable:false, resizeable:false},
		{key:"contName",label:'<s:text name="column.title.Name"/>', formatter:contractorNameTextboxFormatter,sortable:false, resizeable:false},	
		{key:'Search',label:'<s:text name="column.title.asset.search"/>',formatter:createSearchImageFormatter("${pageContext.request.contextPath}")},	
		{key:'Add',label:'<s:text name="column.title.add"/>',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	var contractorsDS = new YAHOO.util.DataSource(); 
	contractorsDataTable = new YAHOO.widget.DataTable("contractorsTable",contractorColumns, contractorsDS);	
			
	contractorsDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
		}

		if (column.key == 'Delete') { 
		if(this.getRecordSet().getLength()>1){				
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}	
			}
		}	
		
		var records = contractorsDataTable.getRecordSet();
		if (column.key == 'Search') {
			rowId=records.getRecordIndex(record);
			searchAddContractor(rowId);
		}
	});
	contractorsDataTable.addRow({SlNo:contractorsDataTable.getRecordSet().getLength()+1});
}

 var rateContractDropdownOptions=[{label:"--- Select ---", value:"0"}
 <s:if test="%{!estimateRateContractList.isEmpty()}"> 
 ,<s:iterator var="s" value="estimateRateContractList" status="status"> 	 
    {"label":"<s:property value="%{rateContract.rcNumber}"/> '~' <s:property value="%{rateContract.contractor.code}"/>" ,
    "value":"<s:property value="%{rateContract.id}" />"    
    }<s:if test="!#status.last">,</s:if>   
    </s:iterator>
    </s:if>       
    ]
    
    var lang=YAHOO.lang;
	
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
            if(document.abstractEstimateForm.isRateContract.checked==true){        
             	if(postfixFieldValue!="")
            		selectEl.name = listObj+'['+oRecord.getCount()+'].'+oColumn.getKey()+'.'+postfixFieldValue;
            	else
            		selectEl.name = listObj+'['+oRecord.getCount()+'].'+oColumn.getKey();
            }
			selectEl.id = oColumn.getKey()+rateContractDataTable.getRecordSet().getLength();
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

  function loadRateContractForContractor(record){  
	    //var contractorId = dom.get("contractor" + record).value;
	    var contractorIds=new Array();
	     var records = contractorsDataTable.getRecordSet();
	    for(i=0;i<records.getLength();i++){  				
			contractorIds.push(dom.get("contractor"+records.getRecord(i).getId()).value);
		}
		var est_department=dom.get("executingDepartment").value;
		var est_zone=dom.get("wardSearch").value;
		var est_natureOfWrk=dom.get("type").value;
		var est_date=dom.get("estimateDate").value;
		var est_function=document.getElementById('function').value;
		var est_fund=document.getElementById('fund').value;
		var est_budgetGroup=document.getElementById('budgetGroup').value;
	    makeJSONCall(["Text","Value"], '${pageContext.request.contextPath}/rateContract/ajaxRateContract!populateRateContractForContractor.action',
		    	{contractorIds:contractorIds,departmentId:est_department,zoneId:dom.get("wardID").value,estimateDate:est_date,
		    	rcType:dom.get("rcType").value,functionId:est_function,fundId:est_fund, budgetGroupId:est_budgetGroup },myStdSuccessHandler,myStdFailureHandler) ;	  
		
		
	}
	
	myStdSuccessHandler = function(req,res){
	    var resultdatas=res.results;
	    var records = rateContractDataTable.getRecordSet();
	  //  var row_id = resultdatas[0].xRowId;
	   var row_id = 0;
	    var resLength =resultdatas.length;
	  	var dropDownLength = dom.get("rateContract" + eval(convertToIntNumber(row_id)+1)).length;
	  	for(i=0;i<resultdatas.length;i++){
	   			dom.get("rateContract" +eval(convertToIntNumber(row_id)+1)).options[i+1]=new Option(resultdatas[i].Text,resultdatas[i].Value);
	   	}
	   	while(dropDownLength>resLength){
		     dom.get("rateContract" + eval(convertToIntNumber(row_id)+1)).options[resultdatas.length] = null;
		     dropDownLength=dropDownLength-1;
		}
		return true;
	}
	
	myStdFailureHandler= function(){
	    dom.get("worktypeerror").style.display='block';
		dom.get("worktypeerror").innerHTML='Unable to Load Rate Contracts for Contractor';
	}
	
var rateContractDataTable;
var makeRateContractDataTable = function() {
	var rateContractColumns = [ 
		{key:"contractor", hidden:true, formatter:contractorIDFormatter, sortable:false, resizeable:false} ,
		{key:"SlNo", label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
		{key:"rateContract", label:'<s:text name="estimate.rcNumber.label"/>', formatter:createDropdownFormatterWithTableLengthAsId('actionEstimateRateContractList','id'), dropdownOptions:rateContractDropdownOptions},		
		{key:'Add',label:'<s:text name="column.title.add"/>',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'<s:text name="column.title.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	var rateContractDS = new YAHOO.util.DataSource(); 	
	rateContractDataTable = new YAHOO.widget.DataTable("rateContractTable",rateContractColumns, rateContractDS);
	
	rateContractDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			rateContractDataTable.addRow({SlNo:rateContractDataTable.getRecordSet().getLength()+1});
			for(i=1;i<dom.get("rateContract" +eval(1)).options.length;i++) {
				dom.get("rateContract" +eval(rateContractDataTable.getRecordSet().getLength())).options[i]=new Option(dom.get("rateContract" +eval(1)).options[i].text,dom.get("rateContract" +eval(1)).options[i].value);
			}			
		}

		if (column.key == 'Delete') { 
		if(this.getRecordSet().getLength()>1){				
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				globalRcIdList=new Array();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));					
					globalRcIdList.push(dom.get("rateContract" +eval(i+1)).value);
				}	
			}
		}	
		
	});
	
	rateContractDataTable.on('dropdownChangeEvent',function (oArgs) { 
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		
		var selectedIndex=oArgs.target.selectedIndex;
		if (column.key == 'rateContract') {				
			var selectedIndex=oArgs.target.selectedIndex;
    	    var row_index = this.getRecordSet().getRecordIndex(record);    	   
    	    for (var j=0;j<globalRcIdList.length;j++) {    	    	 
    	    	if(globalRcIdList[j]==dom.get("rateContract" +eval(row_index+1)).options[selectedIndex].value && this.getRecordSet().getLength()>1) {
    	    		dom.get("worktypeerror").style.display=''
    	       		dom.get("worktypeerror").innerHTML='<s:text name="estimate.rateContract.duplicate"/>';
    	       		oArgs.target.selectedIndex=0;
    	       		return;
    	       	}    	    
    	    }    	  
    	    dom.get("worktypeerror").style.display='none'
    	    dom.get("worktypeerror").innerHTML=''	
    	    allRecords=this.getRecordSet();	
    	    globalRcIdList=new Array();
    	    for(i=0;i<allRecords.getLength();i++){
				globalRcIdList.push(dom.get("rateContract" +eval(i+1)).value);
		  	}
		}
	});	
	rateContractDataTable.addRow({SlNo:rateContractDataTable.getRecordSet().getLength()+1});	
}

function searchAddContractor(rowId){
	dom.get("worktypeerror").style.display='none';
	document.getElementById("worktypeerror").innerHTML=""; 
	
	var est_department=dom.get("executingDepartment").value;
	var est_zone=dom.get("wardSearch").value;
	var est_natureOfWrk=dom.get("type").value;
	var est_date=dom.get("estimateDate").value;
	var est_function=document.getElementById('function').value;
	var est_fund=document.getElementById('fund').value;
	var est_budgetGroup=document.getElementById('budgetGroup').value;
	
	 if(dom.get("rcType").value == -1){
		dom.get("worktypeerror").style.display='';
	 	document.getElementById("worktypeerror").innerHTML='<s:text name="estimate.rcType.null" />';	
	 	return false;
	 }
	/*	
	if(!checkRCType()){
		return false;
	}
		*/ 
		
	 if(est_department == -1){
		dom.get("worktypeerror").style.display='';
	 	document.getElementById("worktypeerror").innerHTML='<s:text name="estimate.executingDept.null" />';	
	 	return false;
	 }

 	if (est_zone.length == 0) {
        dom.get("worktypeerror").style.display='';
	 	document.getElementById("worktypeerror").innerHTML='<s:text name="estimate.ward.null" />';	
	 	return false;
     }
       
	if(est_natureOfWrk == -1){
		dom.get("worktypeerror").style.display='';
	 	document.getElementById("worktypeerror").innerHTML='<s:text name="estimate.natureofwork.null" />';	
	 	return false;
	}
	
	if(est_date == ""){
		dom.get("worktypeerror").style.display='';
	 	document.getElementById("worktypeerror").innerHTML='<s:text name="estimate.date.null" />';	
	 	return false;
	}
	
	if(dom.get("rcType").value != -1 && dom.get("rcType").value=='Amount'){ 
		if(est_function=='-1' || est_fund=='-1' || est_budgetGroup=='-1'){
			dom.get("worktypeerror").style.display='';
	 		document.getElementById("worktypeerror").innerHTML='<s:text name="estimate.financialDetails.null" />';	
	 		return false;
		}
	}
	
	if(dom.get("rcType").value=='Item'){
		window.open("../masters/contractor!searchApprvdContractor.action?sourcepage=estimate"+"&est_department="+est_department+"&est_zone="+dom.get("wardID").value+"&est_date="+est_date+"&rcType="+dom.get("rcType").value+"&rowId="+rowId,"", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
	}
	if(dom.get("rcType").value=='Amount'){
		window.open("../masters/contractor!searchApprvdContractor.action?sourcepage=estimate"+"&est_department="+est_department+"&est_zone="+dom.get("wardID").value+"&est_date="+est_date+"&est_function="+est_function+"&est_fund="+est_fund+"&est_budgetGroup="+est_budgetGroup+"&rcType="+dom.get("rcType").value+"&rowId="+rowId,"", "height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
	}
} 

function checkRCType(){
	dom.get("worktypeerror").style.display='none';
	document.getElementById("worktypeerror").innerHTML=""; 
	var natureOfWrk = document.getElementById('expenditureType').options[document.getElementById('expenditureType').selectedIndex].innerHTML;
	if(dom.get("expenditureType").value!= -1 && natureOfWrk=='OTHERS'){
		if(dom.get("rcType").value != -1 && dom.get("rcType").value=='Amount'){
			dom.get("worktypeerror").style.display='';
	 		document.getElementById("worktypeerror").innerHTML='<s:text name="estimate.depositCode.rcType.amount.NA" />';	
	 		return false;	
		}
	} 
}

function update(elemValue) {	
	if(elemValue!="" || elemValue!=null) {
		var a = elemValue.split("`~`");
		var records= assetsTable.getRecordSet();
		if(a[0]=="loadContractor"){
			var records= contractorsDataTable.getRecordSet();
			var row_id=a[1];
			var contractor_id=a[2];
			var contractor_code=a[3];
			var contractor_name=a[4];
			if(!validateDuplicateContractor(records, contractor_id))
				return false;
			if(dom.get("code"+records.getRecord(getNumber(row_id)).getId()).value!=""){
				var ans=confirm('<s:text name="negotiation.contractorDelete.warning"/>');
				if(!ans)
					return false;
			}
			
			dom.get("code"+records.getRecord(getNumber(row_id)).getId()).value=contractor_code;
			dom.get("contName"+records.getRecord(getNumber(row_id)).getId()).value=contractor_name;
			dom.get("contractor"+records.getRecord(getNumber(row_id)).getId()).value=contractor_id;
			dom.get("code"+records.getRecord(getNumber(row_id)).getId()).disabled=true;		
			dom.get("contName"+records.getRecord(getNumber(row_id)).getId()).disabled=true;	
			loadRateContractForContractor(records.getRecord(getNumber(row_id)).getId());
		}
		else{
		var row_id=a[0];
		var asset_id=a[1];
		var asset_code=a[2];
		var asset_name=a[3];
		dom.get("code"+records.getRecord(getNumber(row_id)).getId()).value=asset_code;
		dom.get("name"+records.getRecord(getNumber(row_id)).getId()).value=asset_name;
		dom.get("asset"+records.getRecord(getNumber(row_id)).getId()).value=asset_id;
		dom.get("code"+records.getRecord(getNumber(row_id)).getId()).disabled=true;		
		dom.get("name"+records.getRecord(getNumber(row_id)).getId()).disabled=true;		
	}
	}
}

function validateDuplicateContractor(records, contractorId) {
    for(i=0;i<records.getLength();i++){       
       if(dom.get("contractor"+records.getRecord(i).getId()).value==contractorId){
          dom.get("contractor_error").style.display='';
          document.getElementById("contractor_error").innerHTML='<s:text name="negotiation.contractor.duplicate"/>';
          window.scroll(0,0);
          return false;
       }
    }
    return true;
}

function createSearchImageFormatter(baseURL){
	var searchImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/image/magnifier.png";
	    markup='<a href="#"><img src="'+imageURL+'" height=16  width=16 border="0" alt="Search" align="absmiddle"></a>';
	    el.innerHTML = markup;
	}
	return searchImageFormatter;
}

function resetRCTable(){
	contractorsDataTable.deleteRows(0,contractorsDataTable.getRecordSet().getLength());
	rateContractDataTable.deleteRows(0,rateContractDataTable.getRecordSet().getLength());
}

function convertToIntNumber(value){	
 	 return parseInt(value);
 }

</script>

<div id="estimateRCtab" name="estimateRCtab" style="display: none">
<div class="errorstyle" id="contractor_error" style="display:none;"></div>
<table id="contractorsHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="7" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
			<div class="headplacer"><s:text name="negotiation.contractor.details"/></div>
		</td>
	</tr>
	<tr>
		<td colspan="7">
		<div class="yui-skin-sam">
        	<div id="contractorsTable"></div>
        </div>
		</td>
	</tr>
	<tr>
		<td colspan="7" class="shadowwk"></td>
	</tr>
</table>

<table id="rateContractHeaderTable" width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="7" class="headingwk"><div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
			<div class="headplacer">RC Details</div>
		</td>
	</tr>
	<tr>
		<td colspan="7">
		<div class="yui-skin-sam">
        	<div id="rateContractTable"></div>
        </div>
		</td>
	</tr>
	<tr>
		<td colspan="7" class="shadowwk"></td>
	</tr>
</table>
</div>
<br/>

<script>
	makeContractorsDataTable();
   	makeRateContractDataTable();  
   	 <s:iterator id="contractorsIterator" value="contractorList" status="row_status">
		          <s:if test="#row_status.count == 1">
		              contractorsDataTable.updateRow(0, 
		                                   {contractor:'<s:property value="id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    code:'<s:property value="code"/>',
		                                    contName:'<s:property value="name"/>',
		                                    Search:'X',
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:if>
		          <s:else>
                      contractorsDataTable.addRow({contractor:'<s:property value="id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    code:'<s:property value="code"/>',
		                                    contName:'<s:property value="name"/>',
		                                    Search:'X',
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:else> 
	</s:iterator>
	 <s:iterator id="rcIterator" value="estimateRateContractList" status="row_status">
		          <s:if test="#row_status.count == 1">
		              rateContractDataTable.updateRow(0, 
		                                   {contractor:'<s:property value="rateContract.contractor.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    rateContract:'<s:property value="rateContract.id"/>',	
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:if>
		          <s:else>
                      rateContractDataTable.addRow({
		                                    contractor:'<s:property value="rateContract.contractor.id"/>',
		                                    SlNo:'<s:property value="#row_status.count"/>',
		                                    rateContract:'<s:property value="rateContract.id"/>',	
		                                    Add:'X',
		                                    Delete:'X'});
		          </s:else> 
		           globalRcIdList.push('<s:property value="%{rateContract.id}" />');
	</s:iterator>
</script>
		