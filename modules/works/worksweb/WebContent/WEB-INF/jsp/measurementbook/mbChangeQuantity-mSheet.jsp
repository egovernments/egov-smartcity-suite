<style type="text/css">
</style>
<script src="<egov:url path='js/works.js'/>"></script> 
<script type="text/javascript">

var woEstimateMSheetDataTable;
var woEstimateMSheetColumnDefs;
var makeWOEstimateMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    woEstimateMSheetColumnDefs = [ 
	   	{key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
   		{key:"estimateNo",label:'<s:text name="mb.measurementSheet.EstimateNo"/>',sortable:false, resizeable:false},
   		{key:"revisionType",label:'<s:text name="mb.measurementSheet.Estimate.RevisionType"/>',sortable:false, resizeable:false}, 
	   	{key:"description",label:'<s:text name="workorder.estimate.msheet.description"/>',sortable:false, resizeable:false},
	   	{key:"number",label:'<s:text name="workorder.estimate.msheet.no"/>',sortable:false, resizeable:false},
	   	{key:"length",label:'<s:text name="workorder.estimate.msheet.length"/>',sortable:false, resizeable:false},
	   	{key:"width",label:'<s:text name="workorder.estimate.msheet.width"/>',sortable:false, resizeable:false},
	   	{key:"depthOrHeight",label:'<s:text name="workorder.estimate.msheet.depthorheight"/>',sortable:false, resizeable:false},
	   	{key:"quantity",label:'<s:text name="workorder.estimate.msheet.quantity"/>',sortable:false, resizeable:false},
	   	{key:"uom",label:'<s:text name="workorder.estimate.msheet.uom"/>',sortable:false, resizeable:false},
	   	{key:"deduction",label:'<s:text name="workorder.estimate.msheet.deduction"/>',formatter:extraItemCheckboxFormatter,sortable:false, resizeable:false}
	 ];
    var woEstimateMSheetDataSource = new YAHOO.util.DataSource();
    woEstimateMSheetDataTable = new YAHOO.widget.DataTable("woEstimateMSheetTable",woEstimateMSheetColumnDefs, woEstimateMSheetDataSource);
    woEstimateMSheetDataTable.subscribe("cellClickEvent", woEstimateMSheetDataTable.onEventShowCellEditor); 
    woEstimateMSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target); 
    });
    var tfoot = woEstimateMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 7;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';
	
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'woEstimateMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'quantityTotal','0'); 
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	return {
        oDS: woEstimateMSheetDataSource,
        oDT: woEstimateMSheetDataTable 
    };
}


var mbExtraCQMSheetDataTable;
var mbExtraCQMSheetColumnDefs;
var makeDetailedMBCQMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    mbExtraCQMSheetColumnDefs = [ 
   	{key:"Sl_No", label:'<s:text name='column.title.SLNo' />', sortable:false, resizeable:false},
   	{key:"description",label:'<s:text name='mb.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetNo",label:'<s:text name='mb.measurementSheetDataTable.number' />',formatter:extraItemNoFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetUomLength",label:'<s:text name='mb.measurementSheetDataTable.length' />',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetWidth",label:'<s:text name='mb.measurementSheetDataTable.width' />',formatter:extraItemTextBoxFormatter,sortable:false,resizeable:false},
   	{key:"mbExtraCQMSheetDepthOrHeight",label:'<s:text name='mb.measurementSheetDataTable.depthorheight' />',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetQuantity",label:'<s:text name='mb.measurementSheetDataTable.quantity' />',formatter:extraItemQtyFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetUOM",label:'<s:text name='mb.measurementSheetDataTable.uom' />',sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetDeduction",label:'<s:text name='mb.measurementSheetDataTable.deduction' />',formatter:extraItemCheckboxFormatter,sortable:false, resizeable:false},
   	{key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
   	
   	{key:"mbExtraCQWOAId",formatter:sorOrNonSorIdHiddenFormatter,sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetSlNo",label:"mbExtraCQMSheetSlNo",formatter:nonSorSlNoFormatter,hidden:true,sortable:false, resizeable:false},
   	{key:"mbExtraCQMSheetIdentifier",formatter:mbExtraMSheetHiddenFormatter,sortable:false, resizeable:false}
    ];
    
    var mbExtraCQMSheetDataSource = new YAHOO.util.DataSource();
    mbExtraCQMSheetDataTable = new YAHOO.widget.DataTable("mbExtraCQMSheetTable",mbExtraCQMSheetColumnDefs, mbExtraCQMSheetDataSource,{MSG_EMPTY:"<s:text name='mb.measurementSheet.initial.Detailedtable.message'/>"});
    mbExtraCQMSheetDataTable.subscribe("cellClickEvent", mbExtraCQMSheetDataTable.onEventShowCellEditor); 
    
    mbExtraCQMSheetDataTable.hideColumn("mbExtraCQMSheetIdentifier");
    mbExtraCQMSheetDataTable.hideColumn("mbExtraCQWOAId"); 
    mbExtraCQMSheetDataTable.hideColumn("mbExtraCQMSheetSlNo");
    
    mbExtraCQMSheetDataTable.on('cellClickEvent',function (oArgs) { 
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target); 
        <s:if test="%{!((sourcepage=='search' || mode=='search' || mode=='view') || (sourcepage=='inbox' && model.currentState.value!='NEW' && model.currentState.value!='REJECTED'))}" >
	        if (column.key == 'Delete'){  
	        	var type='ChangeQty';
	           	recalculateTotalOnDelete(record,type);
	            this.deleteRow(record);
	            allRecords=this.getRecordSet();
	            for(i=0;i<allRecords.getLength();i++){
	              this.updateCell(this.getRecord(i),this.getColumn('Sl_No'),""+(i+1)); 
	            }
	        }
       </s:if> 
    });
    
    var tfoot = mbExtraCQMSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 5;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'mbExtraCQMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';

	addCell(tr,2,'changeQtyTotal','0.00');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	addCell(tr,5,'filler','');
    return {
        oDS: mbExtraCQMSheetDataSource,
        oDT: mbExtraCQMSheetDataTable 
    };
}

var changeQtyTextboxDescFormatter = function(el, oRecord, oColumn, oData) {
 	var value = (YAHOO.lang.isValue(oData))?oData:"";
 	var fieldName;
	fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet." + oColumn.getKey();
	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='90' maxlength='1024' value='"+value+"' name='"+fieldName+"' />";
	el.innerHTML = markup;	 	
}

function createTextBoxFormatter_ChangeQty(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
	 if(oColumn.getKey()=="consumedCQNo")
  		fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet.consumedNo";
  	else if(oColumn.getKey()=="consumedCQLength")
  		fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet.consumedLength";
  	else if(oColumn.getKey()=="consumedCQWidth")
  		fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet.consumedWidth";
  	else if(oColumn.getKey()=="consumedCQDH")
  		fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet.consumedDH";
  	else if(oColumn.getKey()=="consumedCQQuantity")
  		fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet.consumedQuantity"; 
  	else
  		fieldName="changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet." + oColumn.getKey();
	markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup; 
	}
	return textboxFormatter;
} 
var changeQuantityTextBoxFormatter = createTextBoxFormatter_ChangeQty(11,7);
var changetQtyFormatter = createTextBoxFormatter_ChangeQty(13,13);
var changeQuantityNoFormatter = createTextBoxFormatter_ChangeQty(9,7);

function createMBChangeQtyMSheetHiddenFormatter(size,maxlength){
	var hiddenFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName;
  	fieldName = "changeQuantityMSheetList[" + oRecord.getCount() + "].measurementSheet." + oColumn.getKey();
  	markup="<input type='text' id='"+id+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+"' />";
    el.innerHTML = markup;
	}  
return hiddenFormatter;
}
var mbCQMSheetHiddenFormatter = createMBChangeQtyMSheetHiddenFormatter(6,0);

var mbExtraCQMSheetHiddenDataTable; 
var mbExtraCQMSheetHiddenColumnDefs;changetQtyFormatter
var makeDetailedMBCQMSheetHiddenDataTable = function() { 
    var cellEditor=new YAHOO.widget.TextboxCellEditor(); 
    mbExtraCQMSheetHiddenColumnDefs = [ 
   	{key:"remarks", label:'<s:text name='mb.measurementSheetDataTable.description' />',formatter:changeQtyTextboxDescFormatter,sortable:false, resizeable:false},
   	{key:"no",label:'<s:text name='mb.measurementSheetDataTable.number' />',formatter:changeQuantityNoFormatter,sortable:false, resizeable:false},
   	{key:"uomLength",label:'<s:text name='mb.measurementSheetDataTable.length' />',formatter:changeQuantityTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"width",label:'<s:text name='mb.measurementSheetDataTable.width' />',formatter:changeQuantityTextBoxFormatter, resizeable:false},
   	{key:"depthOrHeight",label:'<s:text name='mb.measurementSheetDataTable.depthorheight' />',formatter:changeQuantityTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"quantity",label:'<s:text name='mb.measurementSheetDataTable.quantity' />',formatter:changetQtyFormatter,sortable:false, resizeable:false},
   	{key:"UOM",label:'<s:text name='mb.measurementSheetDataTable.uom' />',sortable:false, resizeable:false},
   	{key:"identifier",formatter:mbCQMSheetHiddenFormatter,sortable:false, resizeable:true},
   	
   	{key:"changeQtyWOActivityId",formatter:activityIDFormatter,sortable:false, resizeable:false},
   	{key:"changeQuantityMsheetSlNo",formatter:nonSorSlNoFormatter,sortable:false, resizeable:true},
   	{key:"hiddenEstimateMSheetId", formatter:nonSorSlNoFormatter,sortable:false, resizeable:false},
   	
   	{key:"consumedCQNo",formatter:changeQuantityNoFormatter,sortable:false, resizeable:false},
   	{key:"consumedCQLength",formatter:changeQuantityTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"consumedCQWidth",formatter:changeQuantityTextBoxFormatter, resizeable:false},
   	{key:"consumedCQDH",formatter:changeQuantityTextBoxFormatter,sortable:false, resizeable:false},
   	{key:"consumedCQQuantity",formatter:changetQtyFormatter,sortable:false, resizeable:false}   
    ];
    var mbExtraCQMSheetHiddenDataSource = new YAHOO.util.DataSource();
    mbExtraCQMSheetHiddenDataTable = new YAHOO.widget.DataTable("mbExtraCQMSheetHiddenTable",mbExtraCQMSheetHiddenColumnDefs, mbExtraCQMSheetHiddenDataSource,{MSG_EMPTY:""}); 
    mbExtraCQMSheetHiddenDataTable.subscribe("cellClickEvent", mbExtraCQMSheetHiddenDataTable.onEventShowCellEditor); 
       
    return {
        oDS: mbExtraCQMSheetHiddenDataSource,
        oDT: mbExtraCQMSheetHiddenDataTable
    };
}

function removeChangeQtyItemDetails(woActivityId){
	if(mbExtraCQMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= mbExtraCQMSheetHiddenDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			if(dom.get("changeQtyWOActivityId"+records.getRecord(i).getId()).value==woActivityId)
				mbExtraCQMSheetHiddenDataTable.deleteRow(records.getRecord(i--));  
		}
	}
}

function reLoadMBChangeQtyMsheetTable(woActivityId){
	var recordIndex=0; 
	if(mbExtraCQMSheetHiddenDataTable.getRecordSet().getLength()>0){
		mbExtraCQMSheetDataTable.deleteRows(0,mbExtraCQMSheetDataTable.getRecordSet().getLength()); 
		dom.get("changeQtyTotal").innerHTML="0.00";
		var records= mbExtraCQMSheetHiddenDataTable.getRecordSet();
   		for(var i=0;i<records.getLength();i++){  
			if(dom.get("changeQtyWOActivityId"+records.getRecord(i).getId()).value==woActivityId && dom.get("hiddenEstimateMSheetId"+records.getRecord(i).getId()).value==''){
				mbExtraCQMSheetDataTable.addRow({
									      Sl_No:mbExtraCQMSheetDataTable.getRecordSet().getLength()+1,
										  description:dom.get("remarks" + records.getRecord(i).getId()).value, 
				 						  mbExtraCQMSheetNo:dom.get("no" + records.getRecord(i).getId()).value,
				 						  mbExtraCQMSheetUomLength:dom.get("uomLength" + records.getRecord(i).getId()).value,
				 						  mbExtraCQMSheetWidth:dom.get("width" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraCQMSheetDepthOrHeight:dom.get("depthOrHeight" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraCQMSheetQuantity:dom.get("quantity" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraCQMSheetUOM: dom.get("activityUOMForCQ").value,
   	 	 								  mbExtraCQMSheetIdentifier:dom.get("identifier" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraCQMSheetDeduction:'',
   	 	 								  mbExtraCQWOAId:dom.get("changeQtyWOActivityId" + records.getRecord(i).getId()).value,
   	 	 								  mbExtraCQMSheetSlNo:dom.get("changeQuantityMsheetSlNo" + records.getRecord(i).getId()).value,
   	 	 								  Delete:'X' 
   	 	 								});
						 				var record = mbExtraCQMSheetDataTable.getRecord(parseInt(recordIndex++));
					                   	if(dom.get("mbExtraCQMSheetIdentifier"+record.getId()).value=='D')
					                   	 dom.get("mbExtraCQMSheetDeduction"+record.getId()).checked=true;
					                   	else
					                   	 dom.get("mbExtraCQMSheetDeduction"+record.getId()).checked=false;
					                   	calculateExtraMSheetTotal("ChangeQty");  
   	 			}
		} 
   	}
   	else{
	   	mbExtraCQMSheetDataTable.deleteRows(0,mbExtraCQMSheetDataTable.getRecordSet().getLength());
	   	dom.get("changeQtyTotal").innerHTML="0.00";
   	}
}

function resetMBChangeQtyMSheet() {
	var msg='<s:text name="mb.measurementSheetDataTable.resetEntries"/>';
	var ans=confirm(msg);
	if(ans) {
		if(mbExtraCQMSheetDataTable.getRecordSet().getLength()>0){
			removeChangeQtyItemDetails(dom.get("wOActivityIdCQ").value);
		}
		mbExtraCQMSheetDataTable.deleteRows(0,mbExtraCQMSheetDataTable.getRecordSet().getLength());
		document.getElementById('mbExtraCQMSheetDetailsContainer').style.display='none';
		if(dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value)!=null) {
			var record = sorDataTable.getRecord(dom.get("recordIdForCQ").value);
	   		dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value).value="";
	   		calculateExtraItems(dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value),dom.get("recordIdForCQ").value); 
	   		dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value).disabled=false;
	   		dom.get("extraItemsquantity"+dom.get("recordIdForCQ").value).readOnly=false;
	   	 	extraItemsDataTable.updateCell(record,extraItemsDataTable.getColumn("workOrderMsheetSize"),0);
	   	}
	}
	else {
		return false;		
	}
}

function addRowToMBExtraCQEstimatedMSheet(){
	if(mbExtraCQMSheetDataTable.getRecordSet().getLength()=="" || mbExtraCQMSheetDataTable.getRecordSet().getLength()<1){
		mbExtraCQMSheetDataTable.addRow({Sl_No:mbExtraCQMSheetDataTable.getRecordSet().getLength()+1,
		mbExtraCQMSheetUomLength:'1',mbExtraCQMSheetWidth:'1',mbExtraCQWOAId:dom.get('wOActivityIdCQ').value,
		mbExtraCQMSheetSlNo:mbExtraCQMSheetDataTable.getRecordSet().getLength()+1,
		mbExtraCQMSheetUOM:dom.get('activityUOMForCQ').value});calculateExtraMsheetQty(dom.get(mbExtraCQMSheetColumnDefs[3].key+mbExtraCQMSheetDataTable.getRecordSet().getRecord(mbExtraCQMSheetDataTable.getRecordSet().getLength()-1).getId()),"ChangeQty",mbExtraCQMSheetDataTable.getRecordSet().getRecord(mbExtraCQMSheetDataTable.getRecordSet().getLength()-1).getId());return false;
	}else{ 
			var Records= mbExtraCQMSheetDataTable.getRecordSet();
			mbExtraCQMSheetDataTable.addRow({Sl_No:mbExtraCQMSheetDataTable.getRecordSet().getLength()+1,
			mbExtraCQMSheetUomLength:'1',mbExtraCQMSheetWidth:'1',
			mbExtraCQWOAId:dom.get('wOActivityIdCQ').value,
			mbExtraCQMSheetSlNo:eval(dom.get('mbExtraCQMSheetSlNo' + Records.getRecord(Records.getLength()-1).getId()).value)+1,
			mbExtraCQMSheetUOM:dom.get('activityUOMForCQ').value});calculateExtraMsheetQty(dom.get(mbExtraCQMSheetColumnDefs[3].key+mbExtraCQMSheetDataTable.getRecordSet().getRecord(mbExtraCQMSheetDataTable.getRecordSet().getLength()-1).getId()),"ChangeQty",mbExtraCQMSheetDataTable.getRecordSet().getRecord(mbExtraCQMSheetDataTable.getRecordSet().getLength()-1).getId());return false;
		}
}



// For Consumed Quantity
var mbChangeQtyConsumedMSheetTable;
var mbCQConsumedMSheetColumnDefs;
var makeCQConsumedMBMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    mbCQConsumedMSheetColumnDefs = [ 
   	{key:"SlNo",label:'<s:text name="column.title.SLNo"/>', sortable:false, resizeable:false},
   	{key:"estimateNo",label:'<s:text name="mb.measurementSheet.EstimateNo"/>',sortable:false, resizeable:false},
   	{key:"revisionType",label:'<s:text name="mb.measurementSheet.Estimate.RevisionType"/>',sortable:false, resizeable:false}, 
   	{key:"mbCQConsumedMSheetSlNo",label:"mbExtraMSheetSlNo",formatter:nonSorSlNoFormatter,hidden:true,sortable:false, resizeable:false},
   	{key:"mbCQConsumedWOAId",formatter:sorOrNonSorIdHiddenFormatter,hidden:true,sortable:false, resizeable:false}, 
   	{key:"estimateMSheetId", hidden:true,sortable:false, resizeable:false},
   	
	{label:'<b><s:text name="mbMSheet.approved.quantity"/></b>',
        children: [ 
         	{key:"apprvdMSheetDesc", label:'<s:text name="mb.measurementSheetDataTable.number"/>',hidden:true,sortable:false, resizeable:false},
            {key:"apprvdMSheetNo", label:'<s:text name="mb.measurementSheetDataTable.number"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetLength", label:'<s:text name="mb.measurementSheetDataTable.length"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetWidth", label:'<s:text name="mb.measurementSheetDataTable.width"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetDH", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',sortable:false, resizeable:false},
            {key:"apprvdMSheetDeduction",label:'<s:text name="mb.measurementSheetDataTable.deduction" />',formatter:extraItemCheckboxFormatter,sortable:false,resizeable:false,readOnly:true},
            {key:"apprvdMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',sortable:false, resizeable:false}
        ]  
	},

	{label:'<span class="mandatory">*</span><b><s:text name="measurementbook.mb.entry"/></b>',
		children: [ 
			{key:"consumedCQMBMSheetNo", label:'<s:text name="mb.measurementSheetDataTable.number"/>',formatter:extraItemNoFormatter,sortable:false, resizeable:false},
		    {key:"consumedCQMBMSheetLength", label:'<s:text name="mb.measurementSheetDataTable.length"/>',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
		    {key:"consumedCQMBMSheetWidth", label:'<s:text name="mb.measurementSheetDataTable.width"/>',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
		    {key:"consumedCQMBMSheetDH", label:'<s:text name="mb.measurementSheetDataTable.depthorheight"/>',formatter:extraItemTextBoxFormatter,sortable:false, resizeable:false},
		    {key:"consumedCQMBMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',formatter:extraItemQtyFormatter,sortable:false, resizeable:false}
         ] 
	},
	
	{label:'<b><s:text name="mbMSheet.cumul.current.entry"/></b>',
        children: [ 
            {key:"curCumlvCQMBMSheetQuantity", label:'<s:text name="mb.measurementSheetDataTable.quantity"/>',sortable:false, resizeable:false},
            {key:"hiddenCurCumlvCQMBMSheetQuantity",hidden:true,sortable:false, resizeable:false},
            {key:"hiddenCurCQMBMSheetQuantity",hidden:true,sortable:false, resizeable:false} 
        ] 
	} 
  ];
    var mbCQConsumedMSheetDataSource = new YAHOO.util.DataSource();
    mbChangeQtyConsumedMSheetTable = new YAHOO.widget.DataTable("mbCQConsumedMSheetTable",mbCQConsumedMSheetColumnDefs, mbCQConsumedMSheetDataSource);
    mbChangeQtyConsumedMSheetTable.subscribe("cellClickEvent", mbChangeQtyConsumedMSheetTable.onEventShowCellEditor); 
    mbChangeQtyConsumedMSheetTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target); 
        var column = this.getColumn(target); 
    });
    var tfoot = mbChangeQtyConsumedMSheetTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 11;
	th.className= 'whitebox4wk'; 
	th.innerHTML = '&nbsp;';
	
	var td = tr.insertCell(1);
	td.className= 'whitebox4wk'; 
	td.id = 'mbCQConsumedMSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	
	addCell(tr,2,'estimatedChangeQtyTotal','0.00'); 
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	addCell(tr,5,'filler','');
	addCell(tr,6,'filler','');
	addCell(tr,7,'consumedChangeQtyTotal','0.00');
	addCell(tr,8,'cumulativeCQTotal','0.00');
    return {
        oDS: mbCQConsumedMSheetDataSource,
        oDT: mbChangeQtyConsumedMSheetTable 
    };
}


function validateBeforeSubmitForMBCQMSheet(){
	clearMBExtraMsheetErrorMessage("ConsumedChangeQty");
	var flag;
	var Records=mbChangeQtyConsumedMSheetTable.getRecordSet();
	for(var i=0;i<mbChangeQtyConsumedMSheetTable.getRecordSet().getLength();i++)
	{
			flag=false;
			if(!((dom.get("consumedCQMBMSheetNo"+Records.getRecord(i).getId()).value=="") &&
  				(dom.get("consumedCQMBMSheetLength"+Records.getRecord(i).getId()).value=="") &&
  				(dom.get("consumedCQMBMSheetWidth"+Records.getRecord(i).getId()).value=="") &&
  				(dom.get("consumedCQMBMSheetDH"+Records.getRecord(i).getId()).value=="") &&	
  				(dom.get("consumedCQMBMSheetQuantity"+Records.getRecord(i).getId()).value==""))){
  					flag=true;	
  			}
		 	if(dom.get("errorconsumedCQMBMSheetNo"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedCQMBMSheetNo"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetNo")!="" &&  dom.get("consumedCQMBMSheetNo"+Records.getRecord(i).getId()).value=="")){
 				dom.get("mbCQConsumedMSheetError").style.display='';
			    dom.get("mbCQConsumedMSheetError").innerHTML='Please enter a valid value for the Number at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
			    dom.get("mbCQConsumedMSheetError").focus=true;
				return false;	
 			}
  		
	  		 if(dom.get("errorconsumedCQMBMSheetLength"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedCQMBMSheetLength"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetLength")!="" &&  dom.get("consumedCQMBMSheetLength"+Records.getRecord(i).getId()).value=="")){
	  			dom.get("mbCQConsumedMSheetError").style.display='';
			    dom.get("mbCQConsumedMSheetError").innerHTML='Please enter a valid value for the Length at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false;	
	  		}
  		
	  		 if(dom.get("errorconsumedCQMBMSheetWidth"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedCQMBMSheetWidth"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetWidth")!="" &&  dom.get("consumedCQMBMSheetWidth"+Records.getRecord(i).getId()).value=="")){
	  			dom.get("mbCQConsumedMSheetError").style.display='';
			    dom.get("mbCQConsumedMSheetError").innerHTML='Please enter a valid value for the Width at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false;	
	  		}
  		
  		 	if(dom.get("errorconsumedCQMBMSheetDH"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedCQMBMSheetDH"+Records.getRecord(i).getId()).style.display=="" || (flag==true && Records.getRecord(i).getData("apprvdMSheetDH")!="" &&  dom.get("consumedCQMBMSheetDH"+Records.getRecord(i).getId()).value=="")){
	   			 dom.get("mbCQConsumedMSheetError").style.display='';
	   			 dom.get("mbCQConsumedMSheetError").innerHTML='Please enter a valid value for the Depth/Height at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false;	
  			}
  		
  			if(dom.get("errorconsumedCQMBMSheetQuantity"+Records.getRecord(i).getId()).style.display=="block" || dom.get("errorconsumedCQMBMSheetQuantity"+Records.getRecord(i).getId()).style.display=="" || (flag==true && dom.get("consumedCQMBMSheetQuantity"+Records.getRecord(i).getId()).value=="")){
			 	dom.get("mbCQConsumedMSheetError").style.display='';
			    dom.get("mbCQConsumedMSheetError").innerHTML='Please enter a valid value for Quantity at line '+Records.getRecord(i).getData("SlNo")+'<br>Value Should be greater than 0';
				return false; 
			 } 
  	}
	if(dom.get("consumedChangeQtyTotal").innerHTML<=0){
		dom.get("mbCQConsumedMSheetError").style.display='';
	    dom.get("mbCQConsumedMSheetError").innerHTML='<s:text name='mbMSheet.measurementSheetDataTable.negativeTotal' />';
		return false;
	}
	goToMBConsumedChangeQty();
	document.getElementById('mbCQConsumedMSheetDetailsContainer').style.display='none';
	return false;
}


function goToMBConsumedChangeQty(){
	removeChangeQtyItemDetails(dom.get("wOActivityIdCQ").value);
	var records= mbChangeQtyConsumedMSheetTable.getRecordSet();
	var temp;
	var curQuantity;
	var count=mbExtraCQMSheetHiddenDataTable.getRecordSet().getLength(); 
   	for(var i=0;i<mbChangeQtyConsumedMSheetTable.getRecordSet().getLength();i++){
	   		 mbExtraCQMSheetHiddenDataTable.addRow({
	   	 	 	changeQtyWOActivityId:dom.get("wOActivityIdCQ").value,
				changeQuantityMsheetSlNo:dom.get("mbCQConsumedMSheetSlNo" + records.getRecord(i).getId()).value,
				hiddenEstimateMSheetId:records.getRecord(i).getData("estimateMSheetId"),
				
				remarks:records.getRecord(i).getData("apprvdMSheetDesc"),
				no: records.getRecord(i).getData("apprvdMSheetNo"),
				uomLength:records.getRecord(i).getData("apprvdMSheetLength"),
				width:records.getRecord(i).getData("apprvdMSheetWidth"),
				depthOrHeight:records.getRecord(i).getData("apprvdMSheetDH"),
				quantity:records.getRecord(i).getData("apprvdMSheetQuantity"),
				UOM: dom.get("activityUOMForCQ").value,
				identifier:'',
				
				consumedCQNo:dom.get("consumedCQMBMSheetNo" + records.getRecord(i).getId()).value,
				consumedCQLength:dom.get("consumedCQMBMSheetLength" + records.getRecord(i).getId()).value,
				consumedCQWidth:dom.get("consumedCQMBMSheetWidth" + records.getRecord(i).getId()).value,
				consumedCQDH:dom.get("consumedCQMBMSheetDH" + records.getRecord(i).getId()).value,
				consumedCQQuantity:dom.get("consumedCQMBMSheetQuantity" + records.getRecord(i).getId()).value
	   		});
	   		var rec=mbExtraCQMSheetHiddenDataTable.getRecord(parseInt(count));
	   		if(dom.get("apprvdMSheetDeduction" + records.getRecord(i).getId()).checked == true){
    	  		dom.get("identifier" + rec.getId()).value="D";
		    }
    	  else{
    	  		dom.get("identifier" + rec.getId()).value="A";
    	  }	
    	  count++;
	}
	dom.get("extraItemsmbQuantity"+dom.get("recordIdForCQ").value).value=dom.get("consumedChangeQtyTotal").innerHTML;
	calculateExtraItems(dom.get("extraItemsmbQuantity"+dom.get("recordIdForCQ").value),dom.get("recordIdForCQ").value); 
	dom.get("extraItemsmbQuantity"+dom.get("recordIdForCQ").value).disabled=true;
	dom.get("extraItemsmbQuantity"+dom.get("recordIdForCQ").value).disabled=true; 
}

function removeChangeQtyItemDetails(woActivityId){ 
	if(mbExtraCQMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= mbExtraCQMSheetHiddenDataTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){ 
			if(dom.get("changeQtyWOActivityId"+records.getRecord(i).getId()).value==woActivityId)
				mbExtraCQMSheetHiddenDataTable.deleteRow(records.getRecord(i--)); 
		}
	}
}	


function reLoadCQConsumedMsheet(woActivity_Id){
	var recordIndex=0; 
	var flag;
	if(mbExtraCQMSheetHiddenDataTable.getRecordSet().getLength()>0){
		var records= mbExtraCQMSheetHiddenDataTable.getRecordSet();
		var mbMsheetRecords= mbChangeQtyConsumedMSheetTable.getRecordSet();
		for(var i=0;i<records.getLength();i++){  
			if(dom.get("changeQtyWOActivityId"+records.getRecord(i).getId()).value==woActivity_Id){ 
				for(var j=0;j<mbMsheetRecords.getLength();j++){
					if(dom.get("hiddenEstimateMSheetId"+records.getRecord(i).getId()).value!=''){
						if(mbMsheetRecords.getRecord(j).getData("estimateMSheetId")==dom.get("hiddenEstimateMSheetId"+records.getRecord(i).getId()).value){
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetNo'),records.getRecord(i).getData("consumedCQNo"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetLength'),records.getRecord(i).getData("consumedCQLength"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetWidth'),records.getRecord(i).getData("consumedCQWidth"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetDH'),records.getRecord(i).getData("consumedCQDH"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetQuantity'),records.getRecord(i).getData("consumedCQQuantity"));
					
		 					var tempCurQty=0;
							if(!isNaN(getNumber(dom.get("consumedCQMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value)))
								tempCurQty=dom.get("consumedCQMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value;
		 					var curCumQty=(eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurCumlvCQMBMSheetQuantity")))-eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurCQMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
							mbChangeQtyConsumedMSheetTable.updateCell(mbChangeQtyConsumedMSheetTable.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('curCumlvCQMBMSheetQuantity'),curCumQty);
		 					disableMBExtraConsumedMsheetFields(mbMsheetRecords.getRecord(j),"ConsumedChangeQty");
	 			}
	 				
	 			}
	 			else if(dom.get("hiddenEstimateMSheetId"+records.getRecord(i).getId()).value==''){
	 				if(mbMsheetRecords.getRecord(j).getData("estimateMSheetId")=='' && (dom.get("mbCQConsumedMSheetSlNo" + mbChangeQtyConsumedMSheetTable.getRecordSet().getRecord(j).getId()).value==dom.get("changeQuantityMsheetSlNo"+records.getRecord(i).getId()).value)){
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('apprvdMSheetDesc'),records.getRecord(i).getData("remarks"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetNo'),records.getRecord(i).getData("consumedCQNo"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetLength'),records.getRecord(i).getData("consumedCQLength"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetWidth'),records.getRecord(i).getData("consumedCQWidth"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetDH'),records.getRecord(i).getData("consumedCQDH"));
							mbChangeQtyConsumedMSheetTable.updateCell(mbMsheetRecords.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('consumedCQMBMSheetQuantity'),records.getRecord(i).getData("consumedCQQuantity"));
							
							var tempCurQty=0;
							if(!isNaN(getNumber(dom.get("consumedCQMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value)))
								tempCurQty=dom.get("consumedCQMBMSheetQuantity"+mbMsheetRecords.getRecord(j).getId()).value;
		 					var curCumQty=(eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurCumlvCQMBMSheetQuantity")))-eval(getNumber(mbMsheetRecords.getRecord(j).getData("hiddenCurCQMBMSheetQuantity"))))+eval(getNumber(tempCurQty));
							mbChangeQtyConsumedMSheetTable.updateCell(mbChangeQtyConsumedMSheetTable.getRecord(j),mbChangeQtyConsumedMSheetTable.getColumn('curCumlvCQMBMSheetQuantity'),curCumQty);
		 					disableMBExtraConsumedMsheetFields(mbMsheetRecords.getRecord(j),"ConsumedChangeQty");
					} 			 						  
   	 		}
		}
	}
}
}
}


function disableMBExtraConsumedMsheetFields(record,type){
	if(type=="ConsumedChangeQty"){
		if(record.getData("apprvdMSheetNo")==""){
			dom.get("consumedCQMBMSheetNo"+record.getId()).readonly='true';
	        dom.get("consumedCQMBMSheetNo"+record.getId()).disabled='true';
	    }
	    if(record.getData("apprvdMSheetLength")==""){
			dom.get("consumedCQMBMSheetLength"+record.getId()).readonly='true';
	        dom.get("consumedCQMBMSheetLength"+record.getId()).disabled='true';
	    }
	  	if(record.getData("apprvdMSheetWidth")==""){
			dom.get("consumedCQMBMSheetWidth"+record.getId()).readonly='true';
	        dom.get("consumedCQMBMSheetWidth"+record.getId()).disabled='true';
	    }
	    if(record.getData("apprvdMSheetDH")==""){
			dom.get("consumedCQMBMSheetDH"+record.getId()).readonly='true';
	        dom.get("consumedCQMBMSheetDH"+record.getId()).disabled='true';
	    }
	    if((record.getData("apprvdMSheetNo")=="") &&
			(record.getData("apprvdMSheetLength")=="") &&
			(record.getData("apprvdMSheetWidth")=="") &&
			(record.getData("apprvdMSheetDH")=="")) {
		  		dom.get("consumedCQMBMSheetQuantity"+record.getId()).readonly='true';
		        dom.get("consumedCQMBMSheetQuantity"+record.getId()).disabled='true';	 
	  	}
 	}
 	if(type=="ConsumedQty"){
 		if(record.getData("apprvdMSheetNo")==""){
			dom.get("consumedMBMSheetNo"+record.getId()).readonly='true';
	        dom.get("consumedMBMSheetNo"+record.getId()).disabled='true';
	    }
	    if(record.getData("apprvdMSheetLength")==""){
			dom.get("consumedMBMSheetLength"+record.getId()).readonly='true';
	        dom.get("consumedMBMSheetLength"+record.getId()).disabled='true';
	    }
	  	if(record.getData("apprvdMSheetWidth")==""){
			dom.get("consumedMBMSheetWidth"+record.getId()).readonly='true';
	        dom.get("consumedMBMSheetWidth"+record.getId()).disabled='true';
	    }
	    if(record.getData("apprvdMSheetDH")==""){
			dom.get("consumedMBMSheetDH"+record.getId()).readonly='true';
	        dom.get("consumedMBMSheetDH"+record.getId()).disabled='true';
	    }
	    if((record.getData("apprvdMSheetNo")=="") &&
			(record.getData("apprvdMSheetLength")=="") &&
			(record.getData("apprvdMSheetWidth")=="") &&
			(record.getData("apprvdMSheetDH")=="")) {
		  		dom.get("consumedMBMSheetQuantity"+record.getId()).readonly='true';
		        dom.get("consumedMBMSheetQuantity"+record.getId()).disabled='true';	 
	  	}
 	}
}

</script>

<s:hidden name="scheduleIdForCQ" id="scheduleIdForCQ" value="%{scheduleIdForCQ}"></s:hidden>
<s:hidden name="nonSorIdForCQ" id="nonSorIdForCQ" value="%{nonSorIdForCQ}"></s:hidden>

<s:hidden name="activityUOMForCQ" id="activityUOMForCQ" value="%{activityUOMForCQ}"></s:hidden>
<s:hidden name="recordIdForCQ" id="recordIdForCQ" value="%{recordIdForCQ}"></s:hidden>
<s:hidden name="wOActivityIdCQ" id="wOActivityIdCQ" value="%{wOActivityIdCQ}"></s:hidden>

<div style="position: obsolute;" id="estimateMSheetContainer" class="tableContainer">
 	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeMBExtraMSheet(this);" id="minEstimateMSheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeMBExtraMSheet(this);" id="closeEstimateMSheet" title="Close">X</span>
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>

	<table id="estimateMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
         	<td colspan="8" class="shadowwk"></td>
     	</tr> 
       	<tr>
         <td  class="headingwk" colspan="8" style="border-right-width: 0px;">
          <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
          <div class="headplacer"><s:text name='workorder.estimate.msheet'/></div>
         </td>
       	</tr>  
      
        <tr>
          <td>
          <div class="yui-skin-sam">
              <div id="woEstimateMSheetTable"></div>   
              <div id="woEstimateMSheetTotal"></div>  
          </div>
          </td>
        </tr>
	</table>
</div>



<div style="position: obsolute;" id="mbExtraCQMSheetDetailsContainer" class="tableContainer">
	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeMBExtraMSheet(this)" id="minimizeMBExtraCQMsheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeMBExtraMSheet(this)" id="closeMBExtraCQMsheet" title="Close">X</span> 
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>
		  
	<div id="mbExtraCQMSheetError" class="errorstyle" style="display:none;"></div> 
	<table id="detailedMBExtraCQMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
           	<td colspan="19" class="shadowwk"></td>
        </tr>
        <tr>
          <td colspan="19" class="headingwk" style="border-right-width: 0px;">
           <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
           <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
          </td>
          <td class="headingwk" id="showAdd" name="showAdd" align="right" style="border-left-width: 0px;">
			<a  id="addCQMBMSheet" name="addCQMBMSheet" onclick="addRowToMBExtraCQEstimatedMSheet();" href="#">  
			<img  width="16" height="16" border="0" src="/egworks/image/add.png"/> 
			</a>
		  </td>
        </tr>
      
        <tr>
          <td  colspan="10">
          <div class="yui-skin-sam">
              <div id="mbExtraCQMSheetTable"></div> 
              <div id="mbExtraCQMSheetTableTotals"></div>  
          </div>
          </td>
        </tr>
        
          <tr>
       		<td>
         		 <div class="buttonholderwk" id="buttonsExtraCQItem" name="buttonsExtraCQItem" style="display: none;">
            	 <br><input type="button" class="buttonadd" value="SUBMIT" id="submitMBExtraCQMSheet" onclick="return validateBeforeSubmitForMBExtraMSheet('ChangeQty');" /> 
	 				<input type="button" class="buttonfinal" value="RESET" id="resetCQButton" name="resetCQButton" onclick="resetMBChangeQtyMSheet()"/>  
       			 </div>
        	</td>
        </tr>
        
	</table>
</div>	


<table id="detailedMBExtraCQMSheetHiddenTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none;">
	<tr>
	  <td colspan="9" class="headingwk" style="border-right-width: 0px;">
	   <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
	   <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
	  </td>
	</tr>
         
    <tr>
      <td  colspan="10">
      <div class="yui-skin-sam">
          <div id="mbExtraCQMSheetHiddenTable"></div> 
      </div>
      </td>
    </tr>
</table> 


<div style="position: obsolute;" id="mbCQConsumedMSheetDetailsContainer" class="tableContainer">
	
	<span class="titles msheettitle" >Measurement Sheet Details</span>
	<span class="titler msheetminimize" onclick="minimizeMBExtraMSheet(this)" id="minimizeCQConsumedMsheet" title="Minimize">-</span>
	<span class="titler msheetclose" onclick="closeMBExtraMSheet(this)" id="closeCQConsumedMsheet" title="Close">X</span> 
	<table  height="25px" border="0" cellspacing="0" width="100%" id="movebar">
		<tr height="11px" class="titlrTr1" width="100%"><td width="100%" class="bordertopb" align="right"></td></tr>
		<tr height="14px" class="titlrTr2" width="100%"><td width="100%" class="borderbtmb"></td></tr>
	</table>
		  
	<div id="mbCQConsumedMSheetError" class="errorstyle" style="display:none;"></div> 
	<table id="detailedMBCQConsumedMSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0" style="display: none"> 
		<tr>
           	<td colspan="19" class="shadowwk"></td>
        </tr>
        <tr>
          <td colspan="19" class="headingwk" style="border-right-width: 0px;">
           <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
           <div class="headplacer"><s:text name='mb.measurementSheet.headerName'/></div>
          </td>
        </tr>
      
        <tr>
          <td  colspan="10">
          <div class="yui-skin-sam">
              <div id="mbCQConsumedMSheetTable"></div> 
              <div id="mbCQConsumedMSheetTableTotals"></div>  
          </div>
          </td>
        </tr>
        
          <tr>
       		<td>
         		 <div class="buttonholderwk" id="buttonsCQConsumedItem" name="buttonsCQConsumedItem" style="display: none;">
            	 <br><input type="button" class="buttonadd" value="SUBMIT" id="submitMBCQConsumedMSheet" onclick="return validateBeforeSubmitForMBCQMSheet();" /> 
       			 </div>
        	</td>
        </tr>
        
	</table>
</div>
 

<script>
  makeWOEstimateMSheetDataTable();
  makeDetailedMBCQMSheetDataTable();
  makeDetailedMBCQMSheetHiddenDataTable();
  makeCQConsumedMBMSheetDataTable();
  count=1;
  recordIndex=0;
								  <s:iterator id="mSheetIterator" value="changeQuantityMSheetList" status="row_status">
								  <s:if test="%{woActivity}">		
						   	mbExtraCQMSheetHiddenDataTable.addRow({	                        
	                        remarks:'<s:property value="measurementSheet.remarks"/>', 
	                        <s:if test="%{measurementSheet.no==0.0}">
	                       	 no:'',
	                       	</s:if> 
	                       	<s:elseif test="%{measurementSheet.no>0.0}">
	                       	 no:'<s:property value="measurementSheet.no"/>',
	                       	</s:elseif>
	                       	
	                       	<s:if test="%{measurementSheet.uomLength==0.0}">
							 uomLength:'',
							</s:if> 
							<s:elseif test="%{measurementSheet.uomLength>0.0}">
							 uomLength:'<s:property value="measurementSheet.uomLength"/>',
							</s:elseif>
							
							<s:if test="%{measurementSheet.width==0.0}">
							 width:'',
							</s:if> 
							<s:elseif test="%{measurementSheet.width>0.0}">
							 width:'<s:property value="measurementSheet.width"/>',
							</s:elseif>
							
	                      	<s:if test="%{measurementSheet.depthOrHeight==0.0}">
							 depthOrHeight:'',
							</s:if> 
							<s:elseif test="%{measurementSheet.depthOrHeight>0.0}">
							 depthOrHeight:'<s:property value="measurementSheet.depthOrHeight"/>',
							</s:elseif>
							
							<s:if test="%{measurementSheet.quantity==0.0}">
							 quantity:'',
							</s:if> 
							<s:elseif test="%{measurementSheet.quantity>0.0}">
							 quantity:'<s:property value="measurementSheet.quantity"/>',
							</s:elseif>
	                             
	                        UOM: '<s:property value="measurementSheet.activity.uom.uom"/>', 
	                        identifier:'<s:property value="measurementSheet.identifier"/>',  
	                    
	                        
	                        changeQuantityMsheetSlNo: '<s:property value="measurementSheet.mbExtraItemSlNo" />',
	                       
	                       <s:if test="%{woActivity.parent.id}">
	                        changeQtyWOActivityId: '<s:property value="woActivity.parent.id" />',
	                         hiddenEstimateMSheetId: '',
	                        </s:if>
	                        <s:else>
	                         changeQtyWOActivityId: '<s:property value="woActivity.id" />',
	                         hiddenEstimateMSheetId: '<s:property value="measurementSheet.id" />',
	                        </s:else>                      
	                        	                        
	                        <s:if test="%{measurementSheet.consumedNo==0.0}">
	                       	 consumedCQNo:'',
	                       	</s:if> 
	                       	<s:elseif test="%{measurementSheet.consumedNo>0.0}">
	                       	 consumedCQNo:'<s:property value="measurementSheet.consumedNo"/>',
	                       	</s:elseif>
	                       	
	                       	<s:if test="%{measurementSheet.consumedLength==0.0}">
							 consumedCQLength:'',
							</s:if> 
							<s:elseif test="%{measurementSheet.consumedLength>0.0}">
							 consumedCQLength:'<s:property value="measurementSheet.consumedLength"/>',
							</s:elseif>
							
							<s:if test="%{measurementSheet.consumedWidth==0.0}">
							 consumedCQWidth:'',
							</s:if> 
							<s:elseif test="%{measurementSheet.consumedWidth>0.0}">
							 consumedCQWidth:'<s:property value="measurementSheet.consumedWidth"/>',
							</s:elseif>
							
	                      	<s:if test="%{measurementSheet.consumedDH==0.0}">
							 consumedCQDH:'',
							</s:if> 
							<s:elseif test="%{measurementSheet.consumedDH>0.0}">
							 consumedCQDH:'<s:property value="measurementSheet.consumedDH"/>',
							</s:elseif>
							
							<s:if test="%{measurementSheet.consumedQuantity==0.0}">
							 consumedCQQuantity:''
							</s:if> 
							<s:elseif test="%{measurementSheet.consumedQuantity>0.0}">
							 consumedCQQuantity:'<s:property value="measurementSheet.consumedQuantity"/>' 
							</s:elseif>
							 
	                    	}); 
	                    	</s:if>
						  </s:iterator>
</script>
