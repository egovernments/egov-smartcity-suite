<style type="text/css">
</style>
<script src="<egov:url path='js/works.js'/>"></script> 
<script type="text/javascript">

function minimizeMBMSheet(obj) {
       var msCont = document.getElementById('mbMSheetDetailsContainer');
       if (obj.innerHTML == '-'){
               obj.innerHTML = '^';
               obj.title='Maximize';
               msCont.style.width = '21%';
               msCont.style.height = '45px';
               msCont.style.left = '79%';
               msCont.style.top = '90%';
       } else {
               obj.innerHTML = '-';
               obj.title='Minimize';
               msCont.style.width = '75%';
               msCont.style.height = '40%';
               msCont.style.left = '50px';
               msCont.style.top = '750px'; 
       }
}

function closeMBMSheet() {
	dom.get("mbMSError").style.display='none';
	document.getElementById("mbMSError").innerHTML=''; 
	document.getElementById('mbMSheetDetailsContainer').style.display='none';
	document.getElementById('min').innerHTML='-';
	document.getElementById('min').title='Minimize';
}

var workOrderActivityIDFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
	fieldName = "mbMeasurementSheetList[" + oRecord.getCount() + "].mbDetails.workOrderActivity.id";
   	markup="<input type='text' id='"+id+"' name='"+fieldName+ "' value='"+value+"' size='6'/>"; 
    el.innerHTML = markup; 
}


function createTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
    if(oColumn.getKey()=="workOrderMSheetId" || oColumn.getKey()=="uomLength" || oColumn.getKey()=="width" || oColumn.getKey()=="depthOrHeight" || oColumn.getKey()=="no" || oColumn.getKey()=="quantity"){
			fieldName="mbMeasurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
			markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' />";
	}
	else if(oColumn.getKey()=="hiddenWorkOrderMSheetId"){
			fieldName="mbMeasurementSheetList[" + oRecord.getCount() + "].woMeasurementSheet.id";
			markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' />";
	}
	else{
		fieldName= oColumn.getKey()+oRecord.getId();
   		markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateCurMBMSheetQty(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   }
    el.innerHTML = markup;
	}
	return textboxFormatter;
} 
var textboxFormatter = createTextBoxFormatter(11,7);
var textboxQtyFormatter = createTextBoxFormatter(13,13);
var textboxNumberFormatter = createTextBoxFormatter(9,7);

var checkboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
     if(oColumn.getKey()=="curMBMSheetDeduction")
		markup="<input type='checkbox' 	 id='"+id+"' onClick='calculateMBMSheetTotal();' />"; 
	else
		markup="<input type='checkbox' 	 id='"+id+"' onClick='calculateCurMBMSheetTotal();' />"; 
    el.innerHTML = markup;
}

var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
 	var value = (YAHOO.lang.isValue(oData))?oData:"";
 	var fieldName;
 	if(oColumn.getKey()=="remarks")
 		 fieldName = "mbMeasurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
   	 else
   	   	 fieldName = oColumn.getKey()+"."+oRecord.getId(); 
	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='90' maxlength='1024' value='"+value+"' name='"+fieldName+"' />";
	el.innerHTML = markup;	 	
}

function createMSheetHiddenFormatter(size,maxlength){
	var hiddenFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
    if(oColumn.getKey()=="identifier" || oColumn.getKey()=="recId")
      	fieldName = "mbMeasurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
    else
       	fieldName =oColumn.getKey()+"."+oRecord.getId();
    	markup="<input type='text' id='"+id+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+"' />";
    el.innerHTML = markup;
	}  
return hiddenFormatter;
}
var mSheetHiddenFormatter = createMSheetHiddenFormatter(6,0);

var mbMSheetDataTable;
var mbMSheetColumnDefs;
var makeDetailedMBMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    mbMSheetColumnDefs = [ 
   	{key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
   	{key:"workOrderActivityId", hidden:true,formatter:workOrderActivityIDFormatter,sortable:false, resizeable:false},
   	{key:"workOrderMSheetId", hidden:true,sortable:false, resizeable:false},
   	{key:"estimateNo",label:'<s:text name="mb.measurementSheet.EstimateNo"/>',sortable:false, resizeable:false},
   	{key:"revisionType",label:'<s:text name="mb.measurementSheet.Estimate.RevisionType"/>',sortable:false, resizeable:false},
   	{key:"curMBMSheetRemarks",hidden:true, label:'<s:text name='estimate.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
 
	{key:"level1",label:'<b><s:text name="mbMSheet.approved.quantity"/></b>',
        children: [ 
            {key:"apprvdMSheetNo", label:'<s:text name="mb.measurementSheetDataTable.number"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetLength", label:'<s:text name="mb.measurementSheetDataTable.length"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetWidth", label:'<s:text name="mb.measurementSheetDataTable.width"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetDH", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetDeduction",label:'<s:text name="mb.measurementSheetDataTable.deduction" />',formatter:checkboxFormatter,sortable:false,resizeable:false,readOnly:true},
            {key:"apprvdMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',sortable:false, resizeable:false}
        ] 
	},

	{label:'<span class="mandatory">*</span><b><s:text name="measurementbook.mb.entry"/></b>',
		children: [
			{key:"curMBMSheetNo", label:'<s:text name="mb.measurementSheetDataTable.number"/>',formatter:textboxNumberFormatter,sortable:false, resizeable:false},
		    {key:"curMBMSheetLength", label:'<s:text name="mb.measurementSheetDataTable.length"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
		    {key:"curMBMSheetWidth", label:'<s:text name="mb.measurementSheetDataTable.width"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
		    {key:"curMBMSheetDH", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
		    {key:"curMBMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',formatter:textboxQtyFormatter,sortable:false, resizeable:false}
         ] 
	},
			                
	{key:"level2",label:'<b><s:text name="mbMSheet.cumul.current.entry"/></b>',
        children: [  
            {key:"curCumlvMBMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',sortable:false, resizeable:false},
            {key:"hiddenCurCumlvMBMSheetQuantity",hidden:true,sortable:false, resizeable:false},
            {key:"hiddenCurMBMSheetQuantity",hidden:true,sortable:false, resizeable:false}
        ] 
	},
	{key:"curMBMSheetDeduction",hidden:true,label:'<s:text name='estimate.measurementSheetDataTable.deduction' />',formatter:checkboxFormatter,sortable:false, resizeable:true},
	{key:'Delete',hidden:true,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
	{key:"curMBMSheetIdentifier",formatter:mSheetHiddenFormatter,hidden:true,sortable:false, resizeable:false}
  ];
    var mbMSheetDataSource = new YAHOO.util.DataSource();
    mbMSheetDataTable = new YAHOO.widget.DataTable("mbMSheetTable",mbMSheetColumnDefs, mbMSheetDataSource);
    mbMSheetDataTable.subscribe("cellClickEvent", mbMSheetDataTable.onEventShowCellEditor); 
    mbMSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target); 
        var column = this.getColumn(target); 
        <s:if test="%{!((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
	        if (column.key == 'Delete') {  
	            recalculateMbDtlsMSheetTotalOnDelete(record);
	            //removeDetails(dom.get("woActivityId").value);
	            this.deleteRow(record);
	            allRecords=this.getRecordSet();
	            for(i=0;i<allRecords.getLength();i++){
	              this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
	            }
	        }
	    </s:if>   
    });
    
    var tfoot = mbMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	tr.id="totalRow";
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan =10;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';
	
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'mbMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'apprvdQtyTotal','0.00');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	addCell(tr,5,'filler','');
	var td =tr.appendChild(document.createElement('td')); 
	td.className= 'whitebox4wk';
	td.id = 'zeroQtyTotal';
	addCell(tr,7,'curQtyTotal','0.00');
	var td =tr.appendChild(document.createElement('td'));
	td.className= 'selectamountwk whitebox4wk';
	td.id = 'curCumlvQtyTotal';
	td.innerHTML='0.00';
	td.colSpan=5; 
    return {
        oDS: mbMSheetDataSource,
        oDT: mbMSheetDataTable 
    };
}

function recalculateMbDtlsMSheetTotalOnDelete(record){
	var oldQuantity=dom.get("curMBMSheetQuantity" + record.getId()).value;
	var oldTotal=dom.get("curQtyTotal").innerHTML;
 	if(dom.get("curMBMSheetDeduction" + record.getId()).checked == true)
		dom.get("curQtyTotal").innerHTML=(eval(getNumber(oldTotal))+eval(getNumber(oldQuantity)));
	else
		dom.get("curQtyTotal").innerHTML=(getNumber(oldTotal)-getNumber(oldQuantity));
}


var mbMSheetHiddenDataTable;
var mbMSheetHiddenColumnDefs;
var makeDetailedMBMSheetHiddenDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    mbMSheetHiddenColumnDefs = [ 
   	{key:"workOrderActvId", formatter:workOrderActivityIDFormatter,sortable:false, resizeable:false},
	{key:"hiddenWorkOrderMSheetId", formatter:textboxNumberFormatter,sortable:false, resizeable:false},
	{key:"remarks", label:'<s:text name='estimate.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
	{key:"no", label:'<s:text name="mb.measurementSheetDataTable.number"/>',formatter:textboxNumberFormatter,sortable:false, resizeable:false},
	{key:"uomLength", label:'<s:text name="mb.measurementSheetDataTable.length"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
	{key:"width", label:'<s:text name="mb.measurementSheetDataTable.width"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
	{key:"depthOrHeight", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',formatter:textboxFormatter,sortable:false, resizeable:false},
	{key:"identifier",formatter:mSheetHiddenFormatter,sortable:false, resizeable:true},
	{key:"quantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',formatter:textboxQtyFormatter,sortable:false, resizeable:false}
	];
    var mbMSheetHiddenDataSource = new YAHOO.util.DataSource();
    mbMSheetHiddenDataTable = new YAHOO.widget.DataTable("mbMSheetHiddenTable",mbMSheetHiddenColumnDefs, mbMSheetHiddenDataSource,{MSG_EMPTY:""});
    mbMSheetHiddenDataTable.subscribe("cellClickEvent", mbMSheetHiddenDataTable.onEventShowCellEditor); 
   
    return {
        oDS: mbMSheetHiddenDataSource,
        oDT: mbMSheetHiddenDataTable 
    };
}

function calculateCurMBMSheetQty(elem,recId){
	var mbMsheetCurQuantElemId = 'curMBMSheetQuantity' + recId;
 	if(elem.id==mbMsheetCurQuantElemId){
  	if(!checkUptoFourDecimalPlace(elem,dom.get('mbMSError'),"Quantity")) 
  		return false;
  	}

	var name=(elem.name).split('.');
	dom.get('error'+elem.id).style.display='none';
	if(name==('curMBMSheetNo'+recId)){
		if(!validateNumberForNonDecimalPlace(elem)) return;
	}
	else{
		if(!validateNumberInMBTableCell(mbMSheetDataTable,elem,recId)) return;
	}
	if(elem.name==("curMBMSheetQuantity"+recId)){	
		var curQuantity;
		var record=mbMSheetDataTable.getRecord(recId);
		var tempCurQty=0;
		if(!isNaN(getNumber(dom.get("curMBMSheetQuantity"+recId).value)))
			tempCurQty=dom.get("curMBMSheetQuantity"+recId).value;
		var curCumQty=(eval(getNumber(record.getData("hiddenCurCumlvMBMSheetQuantity")))-eval(getNumber(record.getData("hiddenCurMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
		mbMSheetDataTable.updateCell(record,mbMSheetDataTable.getColumn('curCumlvMBMSheetQuantity'),curCumQty);
		
		calculateMBMSheetTotal();
	}
	else
		findQuantity(recId);
}

function validateNumberForNonDecimalPlace(elem){
	 if(validateNumberForDecimal(elem))
	 {
	   var mbMeasurement=elem.value;
	   var n=mbMeasurement.split('.');
	   if(n.length>1){
	       dom.get('error'+elem.id).style.display='';      
	       return false;
	   }
	 }
	 return true;
}

function validateNumberInMBTableCell(table,elem,recordId){
     record=table.getRecord(recordId);
      dom.get('error'+elem.id).style.display='none';
      if(elem.name==("curMBMSheetQuantity"+recordId)){
	      if(isNaN(elem.value) || getNumber(elem.value)==0){
	      	dom.get('error'+elem.id).style.display='';
	      	return false;
	      }
      }
      else{
      	if(isNaN(elem.value) || getNumber(elem.value)<=0){
	      	dom.get('error'+elem.id).style.display='';
	      	return false;
	      }
      }
      return true;
}

function findQuantity(recId){
	var record=mbMSheetDataTable.getRecord(recId);
	var curNo;
	var curLength;
	var curWidth;
	var curDepthOrHeight;
	var curQuantity=1;
	
	curNo=dom.get("curMBMSheetNo"+record.getId()).value;
	curLength=dom.get("curMBMSheetLength"+record.getId()).value;
	curWidth=dom.get("curMBMSheetWidth"+record.getId()).value;
	curDepthOrHeight=dom.get("curMBMSheetDH"+record.getId()).value;
	
	if(!isNaN(getNumber(curNo)))
		curQuantity*=curNo;
	
	if(!isNaN(getNumber(curLength)))
		curQuantity*=curLength;
	
	if(!isNaN(getNumber(curWidth)))	
		curQuantity*=curWidth;
	
	if(!isNaN(getNumber(curDepthOrHeight)))
		curQuantity*=curDepthOrHeight;
		
	if(validateForEmptyMsheet(recId))
		mbMSheetDataTable.updateCell(record,mbMSheetDataTable.getColumn('curMBMSheetQuantity'),"");
	else{
		mbMSheetDataTable.updateCell(record,mbMSheetDataTable.getColumn('curMBMSheetQuantity'),roundTo(curQuantity,5,'0'));
	}
	var tempCurQty=0;
	if(!isNaN(getNumber(dom.get("curMBMSheetQuantity"+record.getId()).value)))
		tempCurQty=dom.get("curMBMSheetQuantity"+record.getId()).value;
	
	var curCumQty=(eval(getNumber(record.getData("hiddenCurCumlvMBMSheetQuantity")))-eval(getNumber(record.getData("hiddenCurMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
	mbMSheetDataTable.updateCell(record,mbMSheetDataTable.getColumn('curCumlvMBMSheetQuantity'),curCumQty);
	calculateMBMSheetTotal();
}

function calculateMBMSheetTotal(){ 
	var deductionTotal=0;
	var nonDeductionTotal=0;
	var cumlvDeductionTotal=0;
	var cumlvNonDeductionTotal=0;
		
	var detailRec=mbDataTable.getRecord(dom.get("mbDtlsRecId").value);
    var approved_quantity = getNumber(detailRec.getData("approvedQuantity"));
		
	var records= mbMSheetDataTable.getRecordSet();
   	for(var i=0;i<mbMSheetDataTable.getRecordSet().getLength();i++){
	   	if(!isNaN(getNumber(dom.get("curMBMSheetQuantity"+records.getRecord(i).getId()).value))){
			if(approved_quantity>0){
	   			if(dom.get("apprvdMSheetDeduction" + records.getRecord(i).getId()).checked == true)
	   					deductionTotal=deductionTotal+getNumber(dom.get("curMBMSheetQuantity" + records.getRecord(i).getId()).value);
		    	else
		    		nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("curMBMSheetQuantity" + records.getRecord(i).getId()).value);
		    }
		    else if(approved_quantity==0){
		    	if(dom.get("curMBMSheetDeduction" + records.getRecord(i).getId()).checked == true){
	   					deductionTotal=deductionTotal+getNumber(dom.get("curMBMSheetQuantity" + records.getRecord(i).getId()).value);
	   					dom.get("curMBMSheetIdentifier" + records.getRecord(i).getId()).value="D"; 
	   			}		
		    	else{
		    		nonDeductionTotal=nonDeductionTotal+getNumber(dom.get("curMBMSheetQuantity" + records.getRecord(i).getId()).value);
		    		dom.get("curMBMSheetIdentifier" + records.getRecord(i).getId()).value="A";  
		    	}	
		    }
	   	}	
	    if(!isNaN(getNumber(records.getRecord(i).getData("curCumlvMBMSheetQuantity")))){  
	    	if(dom.get("apprvdMSheetDeduction" + records.getRecord(i).getId()).checked == true || dom.get("curMBMSheetDeduction" + records.getRecord(i).getId()).checked == true)
	   			cumlvDeductionTotal=cumlvDeductionTotal+getNumber(records.getRecord(i).getData("curCumlvMBMSheetQuantity"));
		    else
		    	cumlvNonDeductionTotal=cumlvNonDeductionTotal+getNumber(records.getRecord(i).getData("curCumlvMBMSheetQuantity"));
	   	}		
    }
  /* if(approved_quantity==0){
    	dom.get("zeroQtyTotal").innerHTML=(nonDeductionTotal-deductionTotal);
   }
    else*/
    var val1=roundTo((nonDeductionTotal-deductionTotal),5,'0');
    var val2=roundTo((cumlvNonDeductionTotal-cumlvDeductionTotal),5,'0');
		dom.get("curQtyTotal").innerHTML=val1;
	dom.get("curCumlvQtyTotal").innerHTML=val2;
}

function clearErrorMessage(){
	dom.get("mbMSError").style.display='none';
	document.getElementById("mbMSError").innerHTML=''; 
}

function validateBeforeSubmitForMBMSheet(){
	clearErrorMessage();
	var flag;
	var Records=mbMSheetDataTable.getRecordSet();
	for(var i=0;i<mbMSheetDataTable.getRecordSet().getLength();i++)
	{
			flag=false;
			if(!((dom.get("curMBMSheetNo"+Records.getRecord(i).getId()).value=="") &&
  				(dom.get("curMBMSheetLength"+Records.getRecord(i).getId()).value=="") &&
  				(dom.get("curMBMSheetWidth"+Records.getRecord(i).getId()).value=="") &&
  				(dom.get("curMBMSheetDH"+Records.getRecord(i).getId()).value=="") &&	
  				(dom.get("curMBMSheetQuantity"+Records.getRecord(i).getId()).value==""))){
  					flag=true;	
  			}
		 	if(dom.get("errorcurMBMSheetNo"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorcurMBMSheetNo"+Records.getRecord(i).getId()).style.display=="" || (flag==true && (Records.getRecord(i).getData("apprvdMSheetNo")!="" && Records.getRecord(i).getData("apprvdMSheetNo")!=undefined) &&  dom.get("curMBMSheetNo"+Records.getRecord(i).getId()).value=="")){
 				dom.get("mbMSError").style.display='';
			    dom.get("mbMSError").innerHTML='Please enter a valid value for the Number at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
			    dom.get("mbMSError").focus=true;
				return false;	
 			}
  		
	  		 if(dom.get("errorcurMBMSheetLength"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorcurMBMSheetLength"+Records.getRecord(i).getId()).style.display=="" || (flag==true && (Records.getRecord(i).getData("apprvdMSheetLength")!="" && Records.getRecord(i).getData("apprvdMSheetLength")!=undefined) &&  dom.get("curMBMSheetLength"+Records.getRecord(i).getId()).value=="")){
	  			dom.get("mbMSError").style.display='';
			    dom.get("mbMSError").innerHTML='Please enter a valid value for the Length at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false;	
	  		}
  		
	  		 if(dom.get("errorcurMBMSheetWidth"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorcurMBMSheetWidth"+Records.getRecord(i).getId()).style.display=="" || (flag==true && (Records.getRecord(i).getData("apprvdMSheetWidth")!="" && Records.getRecord(i).getData("apprvdMSheetWidth")!=undefined) &&  dom.get("curMBMSheetWidth"+Records.getRecord(i).getId()).value=="")){
	  			dom.get("mbMSError").style.display='';
			    dom.get("mbMSError").innerHTML='Please enter a valid value for the Width at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false;	
	  		}
  		
  		 	if(dom.get("errorcurMBMSheetDH"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorcurMBMSheetDH"+Records.getRecord(i).getId()).style.display=="" || (flag==true && (Records.getRecord(i).getData("apprvdMSheetDH")!="" &&  Records.getRecord(i).getData("apprvdMSheetDH")!=undefined) &&  dom.get("curMBMSheetDH"+Records.getRecord(i).getId()).value=="")){
	   			 dom.get("mbMSError").style.display='';
	   			 dom.get("mbMSError").innerHTML='Please enter a valid value for the Depth/Height at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false;	
  			}
  		
  			if(dom.get("errorcurMBMSheetQuantity"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorcurMBMSheetQuantity"+Records.getRecord(i).getId()).style.display=="" || (flag==true && dom.get("curMBMSheetQuantity"+Records.getRecord(i).getId()).value=="")){
			 	dom.get("mbMSError").style.display='';
			    dom.get("mbMSError").innerHTML='Please enter a valid value for Quantity at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false;
			 }
  	}
  	
  	var detailRec=mbDataTable.getRecord(dom.get("mbDtlsRecId").value); 
    var approved_quantity = getNumber(detailRec.getData("approvedQuantity"));
   	if(approved_quantity>0){
		if(dom.get("curQtyTotal").innerHTML<=0){
			dom.get("mbMSError").style.display='';
		    dom.get("mbMSError").innerHTML='<s:text name='mbMSheet.measurementSheetDataTable.negativeTotal' />';
			return false;
		}
	}
	else if(approved_quantity==0){
		if(dom.get("curQtyTotal").innerHTML<=0){
			dom.get("mbMSError").style.display='';
		    dom.get("mbMSError").innerHTML='<s:text name='mbMSheet.measurementSheetDataTable.negativeTotal' />';
			return false;
		}
	}
	
	goToMB();
	document.getElementById('mbMSheetDetailsContainer').style.display='none';
	if(approved_quantity>0)
		dom.get("quantity"+	dom.get("mbDtlsRecId").value).value=dom.get("curQtyTotal").innerHTML;
	else if(approved_quantity==0)
		dom.get("quantity"+	dom.get("mbDtlsRecId").value).value=dom.get("curQtyTotal").innerHTML;
	calculateMB(dom.get("quantity"+dom.get("mbDtlsRecId").value),dom.get("mbDtlsRecId").value);
    dom.get("quantity"+dom.get("mbDtlsRecId").value).disabled=true;
	return false;
}

function goToMB(){
	var detailRec=mbDataTable.getRecord(dom.get("mbDtlsRecId").value);
    var approved_quantity = getNumber(detailRec.getData("approvedQuantity"));
    
	removeDetails(dom.get("woActivityId").value);   
	var records= mbMSheetDataTable.getRecordSet();
	var temp;
	var curQuantity;
   	for(var i=0;i<mbMSheetDataTable.getRecordSet().getLength();i++){
   		if(!validateForEmptyMsheet(records.getRecord(i).getId())){
	   		 mbMSheetHiddenDataTable.addRow({
	   	 	 workOrderActvId:dom.get("woActivityId").value,
	   	 	 hiddenWorkOrderMSheetId:records.getRecord(i).getData("workOrderMSheetId"),
	   	 	 no: dom.get("curMBMSheetNo" + records.getRecord(i).getId()).value,
	   	 	 uomLength:dom.get("curMBMSheetLength" + records.getRecord(i).getId()).value,
	   	 	 width:dom.get("curMBMSheetWidth" + records.getRecord(i).getId()).value,
	   	 	 depthOrHeight:dom.get("curMBMSheetDH" + records.getRecord(i).getId()).value,
	   	 	 quantity:''
	   		});
	   		
	   		var record = mbMSheetHiddenDataTable.getRecord(parseInt(mbMSheetHiddenDataTable.getRecordSet().getLength()-1));
	   		if(approved_quantity==0){
	   			mbMSheetHiddenDataTable.updateCell(record,mbMSheetHiddenDataTable.getColumn('remarks'),dom.get("curMBMSheetRemarks" + records.getRecord(i).getId()).value);
	   			mbMSheetHiddenDataTable.updateCell(record,mbMSheetHiddenDataTable.getColumn('identifier'),dom.get("curMBMSheetIdentifier" + records.getRecord(i).getId()).value);
	   		}
	   		else
	   			mbMSheetHiddenDataTable.updateCell(record,mbMSheetHiddenDataTable.getColumn('identifier'),"0");
	   		mbMSheetHiddenDataTable.updateCell(record,mbMSheetHiddenDataTable.getColumn('quantity'),dom.get("curMBMSheetQuantity" + records.getRecord(i).getId()).value);
		}	
	}
}

function removeDetails(woActivity_Id){
	if(mbMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= mbMSheetHiddenDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			if(dom.get("workOrderActvId"+records.getRecord(i).getId()).value==woActivity_Id)
				mbMSheetHiddenDataTable.deleteRow(records.getRecord(i--)); 
		}
	}
}

function validateForEmptyMsheet(recordId){
	var curMBMSheet_No=dom.get("curMBMSheetNo" + recordId).value;
	var curMBMSheet_Length=dom.get("curMBMSheetLength" + recordId).value;
	var curMBMSheet_Width=dom.get("curMBMSheetWidth" + recordId).value;
	var curMBMSheet_DH=dom.get("curMBMSheetDH" + recordId).value;
	var curMBMSheet_Qty=dom.get("curMBMSheetQuantity" + recordId).value;
	if(((curMBMSheet_No!="" && curMBMSheet_No!=0) || (curMBMSheet_Length!="" && curMBMSheet_Length!=0) || (curMBMSheet_Width!="" && curMBMSheet_Width!=0) || (curMBMSheet_DH!="" && curMBMSheet_DH!=0) || (curMBMSheet_Qty!="" && curMBMSheet_Qty!=0)) &&
	 	 ((curMBMSheet_No!="" && curMBMSheet_No!=0) || (curMBMSheet_Length!="" && curMBMSheet_Length!=0) || (curMBMSheet_Width!="" && curMBMSheet_Width!=0) || (curMBMSheet_DH!="" && curMBMSheet_DH!=0)))		
		return false; 
	else
		return true;	
}

function reLoad(woActivity_Id){
	var recordIndex=0; 
	var flag;
	
	var detailRec=mbDataTable.getRecord(dom.get("mbDtlsRecId").value);
    var approved_quantity = getNumber(detailRec.getData("approvedQuantity"));
    if(approved_quantity==0){
    	mbMSheetDataTable.deleteRows(0,mbMSheetDataTable.getRecordSet().getLength());
    } 
	
	if(mbMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= mbMSheetHiddenDataTable.getRecordSet();
		var mbMsheetRecords= mbMSheetDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){  
			if(dom.get("workOrderActvId"+records.getRecord(i).getId()).value==woActivity_Id){ 
				if(approved_quantity>0){
					for(var j=0;j<mbMsheetRecords.getLength();j++){ 
						if(mbMsheetRecords.getRecord(j).getData("workOrderMSheetId")==dom.get("hiddenWorkOrderMSheetId"+records.getRecord(i).getId()).value){
							mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetNo'),records.getRecord(i).getData("no"));
							mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetLength'),records.getRecord(i).getData("uomLength"));
							mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetWidth'),records.getRecord(i).getData("width"));
							mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetDH'),records.getRecord(i).getData("depthOrHeight"));
							mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetQuantity'),records.getRecord(i).getData("quantity"));
	
		 					var tempCurQty=0;
							if(!isNaN(getNumber(dom.get("curMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value)))
								tempCurQty=dom.get("curMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value;
		 					var curCumQty=(eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurCumlvMBMSheetQuantity")))-eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
							mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curCumlvMBMSheetQuantity'),curCumQty);
		 					disableMBMsheetFields(mbMsheetRecords.getRecord(j));
		 				}
		 			}
	 			}
 				else if(approved_quantity==0){ 
 					 mbMSheetDataTable.addRow({SlNo:mbMSheetDataTable.getRecordSet().getLength()+1,
 					 	 curMBMSheetRemarks:records.getRecord(i).getData("remarks"),
	 					 curMBMSheetNo:records.getRecord(i).getData("no"),
						 curMBMSheetLength:records.getRecord(i).getData("uomLength"),
						 curMBMSheetWidth:records.getRecord(i).getData("width"),
						 curMBMSheetDH:records.getRecord(i).getData("depthOrHeight"),
						 curMBMSheetDeduction:'',
						 curMBMSheetIdentifier:records.getRecord(i).getData("identifier"),  
						 curMBMSheetQuantity:records.getRecord(i).getData("quantity"),
						 Delete:'X'
					 });
 					var record = mbMSheetDataTable.getRecord(parseInt(recordIndex++)); 
                   	if(dom.get("curMBMSheetIdentifier"+record.getId()).value=='D') 
                   	 dom.get("curMBMSheetDeduction"+record.getId()).checked=true;
                   	else
                   	 dom.get("curMBMSheetDeduction"+record.getId()).checked=false;
                   	 
                   	
                   	var tempCurQty=0;
					if(!isNaN(getNumber(dom.get("curMBMSheetQuantity"+record.getId()).value)))
						tempCurQty=dom.get("curMBMSheetQuantity"+record.getId()).value;
 					var curCumQty=(eval(getNumber(record.getData("hiddenCurCumlvMBMSheetQuantity")))-eval(getNumber(record.getData("hiddenCurMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
					mbMSheetDataTable.updateCell(record,mbMSheetDataTable.getColumn('curCumlvMBMSheetQuantity'),curCumQty);
 				}		 									  
   	 		}
		}
		
		// To Empty values from MBMsheet if there are no related record in Hidden table.
		
		if(approved_quantity>0){
			for(var j=0;j<mbMsheetRecords.getLength();j++){
				flag=true;
				for(var i=0;i<records.getLength();i++){
					if(woActivity_Id==dom.get("workOrderActvId"+records.getRecord(i).getId()).value){
						if(mbMsheetRecords.getRecord(j).getData("workOrderMSheetId")==dom.get("hiddenWorkOrderMSheetId"+records.getRecord(i).getId()).value){
							flag=false;
						}
					}  
				}
				if(flag){
					mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetNo'),"");
					mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetLength'),"");
					mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetWidth'),"");
					mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetDH'),"");
					mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curMBMSheetQuantity'),"");
					var tempCurQty=0;
					if(!isNaN(getNumber(dom.get("curMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value)))
						tempCurQty=dom.get("curMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value;
					var curCumQty=(eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurCumlvMBMSheetQuantity")))-eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
					mbMSheetDataTable.updateCell(mbMsheetRecords.getRecord(j),mbMSheetDataTable.getColumn('curCumlvMBMSheetQuantity'),curCumQty);
					disableMBMsheetFields(mbMsheetRecords.getRecord(j));
				}
			}
		}
	}
}


function disableMBMSheetTable(){
	document.getElementById('submitMBMSheet').style.display="none";
	var records= mbMSheetDataTable.getRecordSet();
	for(var i=0;i<records.getLength();i++){ 
		dom.get("curMBMSheetNo"+records.getRecord(i).getId()).disabled=true;
		dom.get("curMBMSheetNo"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("curMBMSheetLength"+records.getRecord(i).getId()).disabled=true;
		dom.get("curMBMSheetLength"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("curMBMSheetWidth"+records.getRecord(i).getId()).disabled=true;
		dom.get("curMBMSheetWidth"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("curMBMSheetDH"+records.getRecord(i).getId()).disabled=true;
		dom.get("curMBMSheetDH"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("curMBMSheetQuantity"+records.getRecord(i).getId()).disabled=true;
		dom.get("curMBMSheetQuantity"+records.getRecord(i).getId()).readonly=true;
		
		dom.get("curMBMSheetDeduction"+records.getRecord(i).getId()).disabled=true;
		dom.get("curMBMSheetDeduction"+records.getRecord(i).getId()).readonly=true;
	}  
} 

function disableMBMsheetFields(record){
	if(record.getData("apprvdMSheetNo")==""){
		dom.get("curMBMSheetNo"+record.getId()).readonly='true';
        dom.get("curMBMSheetNo"+record.getId()).disabled='true';
    }
    if(record.getData("apprvdMSheetLength")==""){
		dom.get("curMBMSheetLength"+record.getId()).readonly='true';
        dom.get("curMBMSheetLength"+record.getId()).disabled='true';
    }
  	if(record.getData("apprvdMSheetWidth")==""){
		dom.get("curMBMSheetWidth"+record.getId()).readonly='true';
        dom.get("curMBMSheetWidth"+record.getId()).disabled='true';
    }
    if(record.getData("apprvdMSheetDH")==""){
		dom.get("curMBMSheetDH"+record.getId()).readonly='true';
        dom.get("curMBMSheetDH"+record.getId()).disabled='true';
    }
    if((record.getData("apprvdMSheetNo")=="") &&
		(record.getData("apprvdMSheetLength")=="") &&
		(record.getData("apprvdMSheetWidth")=="") &&
		(record.getData("apprvdMSheetDH")=="")) {
	  		dom.get("curMBMSheetQuantity"+record.getId()).readonly='true';
	        dom.get("curMBMSheetQuantity"+record.getId()).disabled='true';	 
  	}
}

function addRowToMBDtlsMSheet(){  
	mbMSheetDataTable.addRow({SlNo:mbMSheetDataTable.getRecordSet().getLength()+1,workOrderActivityId:dom.get("woActivityId").value,curMBMSheetLength:'1', curMBMSheetWidth:'1'});
	calculateCurMBMSheetQty(dom.get("curMBMSheetLength"+mbMSheetDataTable.getRecordSet().getRecord(mbMSheetDataTable.getRecordSet().getLength()-1).getId()),mbMSheetDataTable.getRecordSet().getRecord(mbMSheetDataTable.getRecordSet().getLength()-1).getId());
	return false;
}


</script>
 
<s:hidden name="woActivityId" id="woActivityId" value="%{woActivityId}"></s:hidden>
<s:hidden name="mbDtlsRecId" id="mbDtlsRecId" value="%{mbDtlsRecId}"></s:hidden>
<div style="position: obsolute;" id="mbMSheetDetailsContainer" class="tableContainer" >
	<span class="titles msheettitle" >MB Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeMBMSheet(this)" id="min" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeMBMSheet()" title="Close">X</span> 
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>
	<div id="mbMSError" class="errorstyle" style="display:none;"></div> 
	<table id="detailedMBMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
           	<td colspan="8" class="shadowwk"></td>
        </tr>
        <tr>
          <td  class="headingwk" colspan="8" style="border-right-width: 0px;">
           <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
           <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
          </td>
          
          <td class="headingwk" id="showAdd" name="showAdd" align="right" style="border-left-width: 0px;">
			<a  id="addMBDtlsMSheet" name="addMBDtlsMSheet" onclick="addRowToMBDtlsMSheet();" href="#">  
			<img  width="16" height="16" border="0" src="/egworks/image/add.png"/> 
			</a>
		  </td>
          
        </tr>
      
        <tr>
          <td>
          <div class="yui-skin-sam">
              <div id="mbMSheetTable"></div>  
              <div id="mbMSheetTableTotals"></div>  
          </div>
          </td>
        </tr>
        
          <tr>
       		<td>
         		 <div class="buttonholderwk" id="buttons" name="buttons" style="display: none;">
            	 <br><input type="button" class="buttonadd" value="SUBMIT" id="submitMBMSheet" onclick="return validateBeforeSubmitForMBMSheet();" />
       			 </div>
        	</td>
        </tr>
	</table>
</div>	
			
<table id="detailedMBMSheetHiddenTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none;">
	<tr>
	  <td colspan="8" class="headingwk" style="border-right-width: 0px;">
	   <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
	   <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
	  </td>
	</tr>
         
    <tr>
      <td  colspan="10">
      <div class="yui-skin-sam">
       <div id="mbMSheetHiddenTable"></div> 
      </div>
      </td>
    </tr> 
</table> 

<script>
  makeDetailedMBMSheetDataTable();
  makeDetailedMBMSheetHiddenDataTable();
  count=1;
  recordIndex=0;
	 
  <s:iterator id="mbDetailsIterator" value="mbDetails" status="row_status">
  	<s:if test="%{mbMeasurementSheetList!=null}">	
  		<s:iterator id="mbMSheetIterator" value="mbMeasurementSheetList" status="row_status">
  			<s:if test="%{mbDetails!=null}">
		   		mbMSheetHiddenDataTable.addRow({
		   	 	 	 workOrderActvId:'<s:property value="mbDetails.workOrderActivity.id"/>',
		   	 	 	 hiddenWorkOrderMSheetId:'<s:property value="woMeasurementSheet.id"/>',
		   	 	 	 remarks:'<s:property value="remarks"/>', 
				   	<s:if test="%{no==0.0}">
			       	 	no:'',
			     	</s:if> 
			     	<s:elseif test="%{no>0.0}">
			     		no:'<s:property value="no"/>', 
			     	</s:elseif>
			     	
			     	<s:if test="%{uomLength==0.0}">
					 	uomLength:'',
					</s:if> 
					<s:elseif test="%{uomLength>0.0}">
					 	uomLength:'<s:property value="uomLength"/>',
					</s:elseif>
					
					<s:if test="%{width==0.0}">
					 	width:'',
					</s:if> 
					<s:elseif test="%{width>0.0}">
					 	width:'<s:property value="width"/>',
					</s:elseif>
					
					<s:if test="%{depthOrHeight==0.0}">
					 	depthOrHeight:'',
					</s:if> 
					<s:elseif test="%{depthOrHeight>0.0}">
					 	depthOrHeight:'<s:property value="depthOrHeight"/>',
					</s:elseif>
					identifier:'<s:property value="identifier"/>', 
					quantity:'<s:property value="quantity"/>'
			 	}); 
 	 		</s:if>
 	  </s:iterator>
  	</s:if>
  </s:iterator>
</script>
