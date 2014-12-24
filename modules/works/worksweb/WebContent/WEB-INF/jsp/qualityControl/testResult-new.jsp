<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
    Width: 100%;
}
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
<html> 
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<title>
			<s:text name="test.result.test.result.details" />
		</title>
	</head>	
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script type="text/javascript"> 
	var TESTRESULT_DETAILS_PREFIX ='testResultDetails';
	var TESTRESULT_MIS_PREFIX ='testResultMis';
	var TESTRESULT_DETAILS_INDEX =0;
	var TESTRESULT_MIS_INDEX =0;
	
	function createHiddenFormatter(el, oRecord, oColumn, oData){
        var hiddenFormatter = function(el, oRecord, oColumn, oData) {
            var value = (YAHOO.lang.isValue(oData))?oData:"";
            var id=oColumn.getKey()+oRecord.getId();
            var fieldName;
            if(oColumn.getKey()=="testResultDetailsId")
            {
                fieldName = TESTRESULT_DETAILS_PREFIX+"[" + TESTRESULT_DETAILS_INDEX + "].id";
            }    
            if(oColumn.getKey()=="jobDetailsId")
            { 	
                fieldName = TESTRESULT_DETAILS_PREFIX+"[" + TESTRESULT_DETAILS_INDEX + "].jobDetails.id";
            }    
            if(oColumn.getKey()=="testResultMisId")
            { 	
                fieldName = TESTRESULT_MIS_PREFIX+"[" + TESTRESULT_MIS_INDEX + "].id";
            }    
            if(oColumn.getKey()=="testResultDetailDocId")
            {	
                fieldName = TESTRESULT_DETAILS_PREFIX+"[" + TESTRESULT_DETAILS_INDEX + "].documentNumber";
            }    
            var fieldValue=value;
            markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
            el.innerHTML = markup;
        };
        return hiddenFormatter;
    }

    var textboxFormatter = function(el, oRecord, oColumn, oData) {
   	 var value = (YAHOO.lang.isValue(oData))?oData:"";
   	    var fieldName = oColumn.getKey();
   			markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' disabled=true class='selectamountwk'  size='10' value='"+value+"'  name='"+fieldName+"' />";
        el.innerHTML = markup;	 	
   	};

	function createDetailsTableTextAreaFormatter(suffix){
		var textAreaFormatter = function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			var fieldName;
			if(oColumn.getKey()=="misRemarks")
			{	
				fieldName = TESTRESULT_MIS_PREFIX+"[" + TESTRESULT_MIS_INDEX + "]." + suffix;
			}	
			else
			{	
				fieldName = TESTRESULT_DETAILS_PREFIX+"[" + TESTRESULT_DETAILS_INDEX + "]." + suffix;
			}	
			if(value!=null)
				markup="<textarea rows='2' cols='50' id='"+oColumn.getKey()+oRecord.getId()+"'   style=width:160px  class='selectmultilinewk'  name='"+fieldName+"' >"+value+"</textarea>";
			else
				markup="<textarea rows='2' cols='50' id='"+oColumn.getKey()+oRecord.getId()+"'   style=width:160px  class='selectmultilinewk'  name='"+fieldName+"' ></textarea>";
			el.innerHTML = markup;
		};
		return textAreaFormatter;
	}

    function createTextBoxQtyFormatter(size,maxlength){
    	var textQtyBoxFormatter = function(el, oRecord, oColumn, oData) {
    	    var value = (YAHOO.lang.isValue(oData))?oData:"";
   	        var id = oColumn.getKey()+oRecord.getId();
    	    var fieldName = TESTRESULT_MIS_PREFIX +"[" + TESTRESULT_MIS_INDEX + "]." + oColumn.getKey();
    	    
    	    markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' value='"+value+"' class='selectamountwk' onblur='calculateTotalAmount(this,\""+oRecord.getId()+"\");'/>";
    	    el.innerHTML = markup;
    	};
    	return textQtyBoxFormatter;
    }

    var testResultDetailsHiddenFormatter = createHiddenFormatter(10,10);
	var textAreaFormatter = createDetailsTableTextAreaFormatter('remarks');
    var textboxQtyFormatter = createTextBoxQtyFormatter(10,13);

   	function docAttachmentFormatter(){
   		return function(el, oRecord, oColumn, oData) {
   			var id = oColumn.getKey()+oRecord.getId();
       	    var fieldName = "docAttacher["+TESTRESULT_DETAILS_INDEX+"]";
       	 	
       	 	var mode="view";
       	 	<s:if test="%{sourcePage=='search' || (sourcePage=='inbox' && (model.egwStatus!=null && !(model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')))}">
	        	markup="<input type='submit' class='buttonadd' id='"+id+"' name='"+fieldName+"' value='View Doc'  onclick='showDocumentAttached(\""+oRecord.getId()+"\",\""+mode+"\",\""+fieldName+"\");return false;'/>";
	        </s:if>	
	       	<s:else >
				mode="attach";
	       		markup="<input type='submit' class='buttonadd' id='"+id+"' name='"+fieldName+"' value='Attach Doc'  onclick='showDocumentAttached(\""+oRecord.getId()+"\",\""+mode+"\",\""+fieldName+"\");return false;'/>";
	       	</s:else>
   			el.innerHTML = markup;
   		};
    }
	
	var testResultDetailsDataTable;
    var makeTestResultDetailsDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor();
        var testResultDetailsColumnDefs = [
            {key:"testResultDetailsId", hidden:true,formatter:testResultDetailsHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"jobDetailsId", hidden:true,formatter:testResultDetailsHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:20},
            {key:"materialType", label:'Material Type', disabled:true,resizeable:true},
            {key:"testName", label:'Name of Test',disabled:true,resizeable:true},
            {key:"uom", label:'UOM',  sortable:false,disabled:true,resizeable:false},	
            {key:"rate",label:'Rate', formatter:textboxFormatter, sortable:false, disabled:true,resizeable:false},
            {key:"receivedQuantity",label:'Received Quantity',formatter:textboxFormatter, sortable:false,disabled:true,resizeable:false},
            {key:"totalCharges",label:'Total Charges', formatter:textboxFormatter, sortable:false,disabled:true, resizeable:false},
            {key:"remarks",label:'Remarks',formatter:textAreaFormatter, sortable:false, resizeable:false},
            {key:"testResultDetailDoc",label:'View Doc',formatter:docAttachmentFormatter(),width:76,resizeable:false},
            {key:"testResultDetailDocId", formatter:testResultDetailsHiddenFormatter,hidden:true, sortable:false, resizeable:false}
        ]; 
       
        var testResultDetailsDataSource = new YAHOO.util.DataSource();
        testResultDetailsDataTable = new YAHOO.widget.DataTable("testResultDetailsTable",testResultDetailsColumnDefs, testResultDetailsDataSource, {MSG_EMPTY:""});
        testResultDetailsDataTable.subscribe("cellClickEvent", testResultDetailsDataTable.onEventShowCellEditor);
        testResultDetailsDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
        });

        var tfoot = testResultDetailsDataTable.getTbodyEl().parentNode.createTFoot();
    	var tr = tfoot.insertRow(-1);
    	var th = tr.appendChild(document.createElement('td'));
    	th.colSpan =7;
    	th.className= 'whitebox4wk';
    	th.innerHTML = '&nbsp;';

    	var td = tr.insertCell(1);
    	td.className= 'whitebox4wk';
    	td.id = 'sLetterTotal';
    	td.innerHTML = '<span class="bold">Total:</span>';

    	addCell(tr,2,'testResultDetailsTotal','0.00');
    	addCell(tr,3,'filler','');
    	addCell(tr,4,'filler','');
        return {
            oDS: testResultDetailsDataSource,
            oDT: testResultDetailsDataTable
        }; 
    }

    var testResultMisDataTable;
    var makeTestReceiptMisDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor();
        var testResultMisColumnDefs = [
            {key:"testResultMisId", hidden:true,formatter:testResultDetailsHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:20},
            {key:"instrumentType", label:'Instrument Type<span class="mandatory">*</span>', formatter:dataTableInstrumentTypeDD,  sortable:false,resizeable:false},	
            {key:"instrumentNo",label:'Cheque/DD Number', formatter:misTextboxFormatter, sortable:false, resizeable:false},
            {key:"instrumentDate",label:'Cheque/DD Date',formatter:misTextboxFormatter, sortable:false,resizeable:false},
            {key:"amountReceived",label:'Receipt Amount(Rs.)<span class="mandatory">*</span>', formatter:textboxQtyFormatter, sortable:false, resizeable:false},
            {key:"misRemarks",label:'Remarks',formatter:textAreaFormatter, sortable:false, resizeable:false},
			{key:'Delete',label:'Delete', width:22,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
        ]; 
       
        var testResultMisDataSource = new YAHOO.util.DataSource();
        testResultMisDataTable = new YAHOO.widget.DataTable("testResultMisTable",testResultMisColumnDefs, testResultMisDataSource, {MSG_EMPTY:""});
        testResultMisDataTable.subscribe("cellClickEvent", testResultMisDataTable.onEventShowCellEditor);
        testResultMisDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            <s:if test="%{sourcePage=='search' || (sourcePage=='inbox' && (model.egwStatus!=null && !(model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')))}">
				var ele="search";
            </s:if>
            <s:else>

			if (column.key == 'Delete') {  	
				if(this.getRecordSet().getLength()>1){	
					this.deleteRow(record);
					var allRecords=this.getRecordSet();
					var i;
					for(i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1)); 
					}
				}
				else
				{
					alert("This row cannot be deleted");
				}
			}
		</s:else>
        });

        var tfoot = testResultMisDataTable.getTbodyEl().parentNode.createTFoot();
    	var tr = tfoot.insertRow(-1);
    	var th = tr.appendChild(document.createElement('td'));
    	th.colSpan =4;
    	th.className= 'whitebox4wk';
    	th.innerHTML = '&nbsp;';

    	var td = tr.insertCell(1);
    	td.className= 'whitebox4wk';
    	td.id = 'sLetterTotal';
    	td.innerHTML = '<span class="bold">Total:</span>';

    	addCell(tr,2,'testResultMisTotal','0.00');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
        return {
            oDS: testResultMisDataSource,
            oDT: testResultMisDataTable
        }; 
    };

    var misTextboxFormatter = function(el, oRecord, oColumn, oData) {
      	var value = (YAHOO.lang.isValue(oData))?oData:"";
   	    var fieldName = oColumn.getKey();
   	    var cssClass = "selectwk";
   	    var dateFunc = "";
		if(fieldName=="instrumentNo")
		{	
			fieldName = TESTRESULT_MIS_PREFIX+"[" + TESTRESULT_MIS_INDEX + "].chequeNumber";
			cssClass = "selectamountwk"; 
		}	
		if(fieldName=="instrumentDate")
		{
			fieldName = TESTRESULT_MIS_PREFIX+"[" + TESTRESULT_MIS_INDEX + "].chequeDate";
			dateFunc  = "onfocus=javascript:vDateType='3'; onkeyup=DateFormat(this,this.value,event,false,'3'); ";
		}	
    			      			
    	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"'  class='"+cssClass+"' size='10' value='"+value+"'  name='"+fieldName+"' "+dateFunc+" />";          				
        el.innerHTML = markup;	 	
    };

	function disableForCash(obj)
	{
		var rowIndex = getRowIndexWithRowName(obj.name);
		var insTypeTest = obj.options[obj.selectedIndex].text;
		if(insTypeTest=='cash')
		{
			document.getElementsByName(TESTRESULT_MIS_PREFIX+"[" + rowIndex + "].chequeDate")[0].value='';
			document.getElementsByName(TESTRESULT_MIS_PREFIX+"[" + rowIndex + "].chequeNumber")[0].value='';
			document.getElementsByName(TESTRESULT_MIS_PREFIX+"[" + rowIndex + "].chequeDate")[0].disabled=true;
			document.getElementsByName(TESTRESULT_MIS_PREFIX+"[" + rowIndex + "].chequeNumber")[0].disabled=true;
		}
		else
		{
			document.getElementsByName(TESTRESULT_MIS_PREFIX+"[" + rowIndex + "].chequeDate")[0].disabled=false;
			document.getElementsByName(TESTRESULT_MIS_PREFIX+"[" + rowIndex + "].chequeNumber")[0].disabled=false;
		}
	
	}
	var ELEMENTID;
	//Document Upload Starts
	function showDocumentManagerTR(fieldName)
	{
	    var rowIndex = getRowIndexWithRowName(fieldName);
	    ELEMENTID = TESTRESULT_DETAILS_PREFIX+"["+rowIndex + "].documentNumber";
	    var v= document.getElementsByName(ELEMENTID)[0].value;
	    var url;
	    if(v==null||v==''||v=='To be assigned')
	    {
	      url="/egi/docmgmt/basicDocumentManager.action?moduleName=Works";
	    }
	    else
	    {
	      url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+v+"&moduleName=Works";
	    }
	    var wdth = 1000;
	    var hght = 400;
	    window.open(url,'docupload','width='+wdth+',height='+hght);
	}
	var docNumberUpdater = function (docNumber){
		document.getElementsByName(ELEMENTID)[0].value = docNumber;
	}
    function createInstrumentTypeDD(suffix){
    	return function(el, oRecord, oColumn, oData) {
    		var fieldName = TESTRESULT_MIS_PREFIX+"[" + TESTRESULT_MIS_INDEX + "]." + suffix;
    		
    		var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:90px onChange='disableForCash(this)'   >";
    		element=element+"<option value=-1 selected='selected' >Choose</option>  ";
    		<s:iterator value="dropdownData.instrumentTypeList" status="stat">
    			var name='<s:property value="type"/>';
    			var id1='<s:property value="id" />';
    			element=element+" <option value="+id1 +" > "+ name+" </option>  ";
    		</s:iterator>
    		element=element+" </select>";
    		el.innerHTML =element ;
    	};
    }
    var dataTableInstrumentTypeDD = createInstrumentTypeDD('instrumentType.id');
    
    function showDocumentAttached(recordId,type,fieldName){
		record=testResultDetailsDataTable.getRecord(recordId);
       	var rowIndex = getRowIndexWithRowName(fieldName);
        var v= document.getElementsByName(TESTRESULT_DETAILS_PREFIX+"["+rowIndex + "].documentNumber")[0].value;
		if(type=="view") 
			viewDocumentManager(v);
		if(type=="attach")
			showDocumentManagerTR(fieldName);
		return true;
     }

    function calculateTotalAmount(elem,recordId){
     	record=testResultDetailsDataTable.getRecord(recordId);
     	calculateFinalTotal();
    }


    function calculateFinalTotal(){ 
    	var finTotal=0;
    	var Records= testResultMisDataTable.getRecordSet();
    	var totalObj;
       	for(var i=0;i<=testResultMisDataTable.getRecordSet().getLength();i++)
       	{
       		totalObj = document.getElementsByName(TESTRESULT_MIS_PREFIX +"[" + i + "].amountReceived")[0];
       		if(totalObj!=null && totalObj!==undefined && totalObj.value!='')
 	    		finTotal=finTotal+getNumber(totalObj.value);
       	}
       	var val=roundTo((finTotal),2,'0');
    	dom.get("testResultMisTotal").innerHTML=val;
    }
    
    function validateCancel() {
    	var msg='<s:text name="test.result.cancel.confirm"/>';
    	 
    	if(!confirmCancel(msg)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }

    function setCurrentdate(){
    	var trDate=dom.get("testResultDate").value;
    	if(trDate=='') {
    		dom.get("testResultDate").value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
    	}else{	
    		dom.get('testResultDate').value=trDate;
    	}
    }
    
    function validateInput(text){
    	if(document.getElementById("actionName").value=='Cancel'){
    		if(!validateCancel()){
    			return false;
    		}
    	}
    	clearErrorMessage();
    	var tResult_instrumentType;
    	var tResult_insNo;
    	var tResult_instrumentDt;
    	var tResult_receiptAmt;
    	var totalReceiptAmt, testResultDetailsTotal;
    	var insTypeTest;
        testResultDetailsTotal =  getNumber(dom.get("testResultDetailsTotal").innerHTML);
    	totalReceiptAmt  = getNumber(dom.get("testResultMisTotal").innerHTML);
    	if(document.getElementById("testResultDate").value=='')
        {
    		dom.get("testResultDetails_error").style.display='';
    	    dom.get("testResultDetails_error").innerHTML="<s:text name='test.result.date.null' />";
    		return false;
        }	
    	if(testResultMisDataTable.getRecordSet().getLength()==0){
    		dom.get("testResultDetails_error").style.display='';
    	    dom.get("testResultDetails_error").innerHTML="<s:text name='test.result.datatable.zerolength' />";
    		return false;
    	}
    	else{
    		var Records=testResultMisDataTable.getRecordSet();
    		for(var i=0;i<testResultMisDataTable.getRecordSet().getLength();i++)
       		{
    			tResult_instrumentType=dom.get("instrumentType" +  Records.getRecord(i).getId());
    	    	tResult_insNo=dom.get("instrumentNo" +  Records.getRecord(i).getId()).value;
    	    	tResult_instrumentDt=dom.get("instrumentDate" +  Records.getRecord(i).getId()).value;
    	    	insTypeTest = tResult_instrumentType.options[tResult_instrumentType.selectedIndex].text;
    	    	tResult_receiptAmt = dom.get("amountReceived" +  Records.getRecord(i).getId()).value;

    	    	if(tResult_instrumentType.value == -1){
    	    		dom.get("testResultDetails_error").style.display='';
    	    	    dom.get("testResultDetails_error").innerHTML='Please choose instrument type in row '+Records.getRecord(i).getData("SlNo");
    	    	    return false;
    	        }
    	        if(insTypeTest != 'cash')
        	    {
    	        	if(tResult_insNo == "" || isNaN(tResult_insNo)){
        	    		dom.get("testResultDetails_error").style.display='';
        	    	    dom.get("testResultDetails_error").innerHTML='Please enter valid instrument number in row '+Records.getRecord(i).getData("SlNo");
        	    	    return false;
        	        }
        	    	if(tResult_instrumentDt == "" ){
        	    		dom.get("testResultDetails_error").style.display='';
        	    	    dom.get("testResultDetails_error").innerHTML='Please enter instrument date in row '+Records.getRecord(i).getData("SlNo");
        	    	    return false;
        	        }            	    
            	}
    	    	if(tResult_receiptAmt == "" || tResult_receiptAmt<=0 || isNaN(tResult_receiptAmt)){
    	    		dom.get("testResultDetails_error").style.display='';
    	    	    dom.get("testResultDetails_error").innerHTML='Please enter valid receipt amount in row '+Records.getRecord(i).getData("SlNo");
    	    	    return false;
    	        }
       		}
       		if(testResultDetailsTotal>totalReceiptAmt )
       		{
       			dom.get("testResultDetails_error").style.display='';
	    	    dom.get("testResultDetails_error").innerHTML='Receipt details sum amount should not be less than test sum amount ';
           		return false;
       		}		
    	}
    	
    	if(text!='Approve' && text!='Reject'){
    		if(!validateWorkFlowApprover(text))
    			return false;
    	}
    	
    	enableElements();
    	return true;  
    }
    
    function clearErrorMessage(){
    	dom.get("testResultDetails_error").style.display='none';
    	document.getElementById("testResultDetails_error").innerHTML=''; 
    }

    function loadDesignationFromMatrix(){  
   		var dept=dom.get('departmentName').value;
      	var currentState = dom.get('currentState').value;
   		var amountRule =  dom.get('amountRule').value;
   		var additionalRuleValue =  dom.get('additionalRule').value; 
   		var pendingAction=document.getElementById('pendingActions').value;
   		loadDesignationByDeptAndType('TestResultHeader',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
    }

    function populateApprover() {
    	getUsersByDesignationAndDept(); 
    }

    function enableElements(){
		for(var i=0;i<document.forms[0].length;i++) 
      		document.forms[0].elements[i].disabled =false;
    }
    function enableOrdisableElements()
    {
    	<s:if test="%{sourcePage=='search'}"> 
        	var cnp, cdt, amtr,inst;
        	var flag=false;
    		for(var cnta=0;cnta<testResultMisDataTable.getRecordSet().getLength();cnta++)
       		{
    			cnp = document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cnta+'].chequeNumber')[0];
    			cdt = document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cnta+'].chequeDate')[0];
				amtr = document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cnta+'].amountReceived')[0];
				inst = document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cnta+'].instrumentType.id')[0];
        		if(cnp.value!='' || cdt.value!='' || amtr!='' || inst.value!=-1)
            	{
                	flag=true;
                }	
       		}
       		if(flag==true)
           	{
       			for(var cntb=0;cntb<testResultMisDataTable.getRecordSet().getLength();cntb++)
           		{
       				document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cntb+'].chequeNumber')[0].disabled=true;
        			document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cntb+'].chequeDate')[0].disabled=true;
        			document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cntb+'].remarks')[0].disabled=true;
    				document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cntb+'].amountReceived')[0].disabled=true;
    				document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ cntb+'].instrumentType.id')[0].disabled=true;
           		}	
            }		
        </s:if>	
        <s:if test="%{sourcePage=='search' || (sourcePage=='inbox' && (model.egwStatus!=null && !(model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')))}">
	        links=document.testResultForm.getElementsByTagName("a");
            for(i=0;i<links.length;i++){
                if(links[i].name!='woLink' &&  links[i].name!='slLink' && links[i].name!='tsLink')
                     links[i].onclick=function(){return false;};  
            }
            
            document.getElementById("testResultDate").disabled=true;
            var Records=testResultMisDataTable.getRecordSet();
    		for(var i=0;i<testResultMisDataTable.getRecordSet().getLength();i++)
       		{
    			document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ i+'].chequeNumber')[0].disabled=true;
    			document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ i+'].chequeDate')[0].disabled=true;
    			document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ i+'].remarks')[0].disabled=true;
				document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ i+'].amountReceived')[0].disabled=true;
				document.getElementsByName(TESTRESULT_MIS_PREFIX+'['+ i+'].instrumentType.id')[0].disabled=true;
       		}
       		var remarksObj;
    		for(var j=0;j<testResultDetailsDataTable.getRecordSet().getLength();j++)
       		{
    			remarksObj = document.getElementsByName(TESTRESULT_DETAILS_PREFIX+'['+j+'].remarks')[0];
           		if(remarksObj!=null && remarksObj!==undefined)
           		{
	               	    remarksObj.disabled=true;
           		}		
       		}
    		document.testResultForm.closeButton.disabled=false;
            
       		var closeObj = document.getElementById("Close");
       		if(closeObj!=null && closeObj!==undefined)
           	{
       			document.testResultForm.closeButton.style.visibility="hidden";
			}
        </s:if>
    }
    function openWO()
    {
        var woId = '<s:property value="%{jobHeaderObj.sampleLetterHeader.testSheetHeader.workOrder.id}"/>';
    	window.open('${pageContext.request.contextPath}/workorder/workOrder!edit.action?id='+woId+'&mode=search', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    }
    function openSL()
    {
        var slId = '<s:property value="%{jobHeaderObj.sampleLetterHeader.id}" />';
    	window.open("${pageContext.request.contextPath}/qualityControl/sampleLetter!edit.action?id="+slId+
				"&sourcePage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    }
    function openTS()
    {
        var tsId = '<s:property value="%{jobHeaderObj.sampleLetterHeader.testSheetHeader.id}"/>';
    	window.open('${pageContext.request.contextPath}/qualityControl/testSheet!edit.action?id='+tsId+'&sourcePage=search', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    }
</script>    
<body onload="enableOrdisableElements();setCurrentdate();"> 
		<div class="errorstyle" id="testResultDetails_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="testResultForm" 
			id="testResult" theme="simple">
			<s:if test="%{sourcePage != 'search'}"> 
				<s:token />
			</s:if>
			<s:hidden name="headerId" id="headerId" />
			<s:hidden name="sourcePage" id="sourcePage" />
			<s:hidden name="jobHeaderId" id="jobHeaderId" />
			<input type="hidden" name="actionName" id="actionName"/>
			
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
						
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="test.result.job.details" />
													</div>
												</td>
											</tr>
								             <tr>
								              	<td class="greyboxwk"><s:text name="test.result.work.order.number" />:</td>
								                <td class="greybox2wk"><a href="#" name="woLink" onclick="openWO()"><s:property value="%{jobHeaderObj.sampleLetterHeader.testSheetHeader.workOrder.workOrderNumber}" /></a></td>
								                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="test.result.date" />:</td>
									            <td class="greybox2wk">
									            	<s:date name="testResultDate" var="testResultDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="testResultDate" id="testResultDate" disabled="true"
														cssClass="selectwk" value="%{testResultDateFormat}"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
									            </td>
								             </tr>
								             
								             <tr>
								              	<td class="whiteboxwk"><s:text name="test.result.sample.letter.no" />:</td>
								                <td class="whitebox2wk"><a href="#" name="slLink" onclick="openSL()"><s:property value="%{jobHeaderObj.sampleLetterHeader.sampleLetterNumber}" /></a></td>
								                <td class="whiteboxwk"><s:text name="test.result.test.sheet.number" />:</td>
								                <td class="whitebox2wk"><a href="#" name="tsLink" onclick="openTS()"><s:property value="%{jobHeaderObj.sampleLetterHeader.testSheetHeader.testSheetNumber}" /></a></td>
								            </tr>
								            
								            <tr >
												<td class="greyboxwk"><s:text name="test.result.job.number" />:</td>
												<td class="greybox2wk"><s:property  value="%{jobHeaderObj.jobNumber}" /></td>
												<td class="greyboxwk"><s:text name="test.result.job.date" />:</td>
												<td class="greybox2wk"><s:property  value="new java.text.SimpleDateFormat('dd/MM/yyyy').format(jobHeaderObj.jobDate)" /></td>
											</tr>
								            
								            <tr>
								              	<td class="whiteboxwk"><s:text name="test.result.covering.letter.no" />:</td>
								                <td class="whitebox2wk"><s:property value="%{jobHeaderObj.sampleLetterHeader.coveringLetterNumber}" /></td>
								                <td class="whiteboxwk"></td>
								                <td class="whitebox2wk"></td>
								            </tr>    
											
											<tr><td colspan="4">&nbsp;</td></tr>
											<tr>
									            <td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
									            <div class="headplacer"><s:text name="test.result.receipt.details" /></div>
									            </td>
									            <td align="right" class="headingwk" style="border-left-width: 0px">
									            	<a href="#" onclick="testResultMisDataTable.addRow({SlNo:testResultMisDataTable.getRecordSet().getLength()+1});TESTRESULT_MIS_INDEX++;return false;"><img border="0" alt="Add Rate" src="${pageContext.request.contextPath}/image/add.png" /></a>
									            </td>
									        </tr>
											<tr >
												<td colspan="4">
											        <table id="trDtlTable" width="100%" border="0" cellspacing="0" cellpadding="0">
												        <tr>
												            <td >
												            <div class="yui-skin-sam">
												            <div id="testResultMisTable"></div>
												            <script> 
												            makeTestReceiptMisDataTable();
												            var misGrandTotal;
												            misGrandTotal=0;
												            <s:if test="%{testResultMis!=null && testResultMis.size()!=0}">
												            	var rowCount = 0;
												            	var instrumentTypeDD,remarks;
													            <s:iterator var="trMisList" value="testResultMis" status="row_status">
													            	remarks = '<s:property escape="false" value="remarks.replace(\"\n\", \"<br>\").replace(\"\'\", \"<bq>\")" />';
														            testResultMisDataTable.addRow({testResultMisId:'<s:property value="id"/>',
														                                        SlNo:'<s:property value="#row_status.count"/>',
														                                        instrumentNo:'<s:property value="chequeNumber"/>',
														                                        instrumentDate:'<s:property   value="new java.text.SimpleDateFormat(\'dd/MM/yyyy\').format(chequeDate)" />',
														                                        amountReceived:roundTo('<s:property value="amountReceived"/>'),
														                                        misRemarks:remarks.replace(/<br>/g,"\n").replace(/<bq>/g,"'")
														                                       });
														            TESTRESULT_MIS_INDEX++;   
														            misGrandTotal = misGrandTotal + getNumber(<s:property value="amountReceived"/>);
														            instrumentTypeDD = document.getElementsByName(TESTRESULT_MIS_PREFIX+"["+rowCount+"].instrumentType.id")[0];
														            instrumentTypeDD.value=<s:property value="#trMisList.instrumentType.id" />;
														            rowCount++;
														        </s:iterator>
														        dom.get("testResultMisTotal").innerHTML=roundTo((misGrandTotal),2,'0');
													        </s:if>
												               </script> 
												              </div>
												            </td>
												        </tr>
											        </table>
        										</td>
											</tr>
											<tr><td colspan="4">&nbsp;</td></tr>
											<tr>
									            <td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
									            <div class="headplacer"><s:text name="test.result.test.result.details" /></div>
									            </td>
									            <td align="right" class="headingwk" style="border-left-width: 0px"></td>
									        </tr>
											<tr >
												<td colspan="4">
											        <table id="trDtlTable" width="100%" border="0" cellspacing="0" cellpadding="0">
												        <tr>
												            <td >
												            <div class="yui-skin-sam">
												            <div id="testResultDetailsTable"></div>
												            <script> 
												            makeTestResultDetailsDataTable();
													        var detailsGrandTotal , detailsTotal, rowCount;
													        var record ;
												            	detailsGrandTotal = 0;
												            	detailsTotal = 0;
												            	rowCount = 0;
													            <s:iterator var="trDetailsList" value="testResultDetails" status="row_status">
													            remarks = '<s:property escape="false" value="remarks.replace(\"\n\", \"<br>\").replace(\"\'\", \"<bq>\")" />';
													            testResultDetailsDataTable.addRow({
													            							testResultDetailsId:'<s:property value="id"/>',
														            						jobDetailsId:'<s:property value="jobDetails.id"/>',
													                                        SlNo:'<s:property value="#row_status.count"/>',
													                                        materialType:'<s:property value="jobDetails.sampleLetterDetails.testSheetDetails.testMaster.materialType.name"/>', 
													                                        testName:'<s:property value="jobDetails.sampleLetterDetails.testSheetDetails.testMaster.testName"/>',
													                                        uom:'<s:property value="jobDetails.sampleLetterDetails.testSheetDetails.uom"/>',
													                                        rate:roundTo('<s:property value="jobDetails.sampleLetterDetails.testSheetDetails.testCharges"/>'),
													                                        receivedQuantity:'<s:property value="jobDetails.receivedQuantity"/>',
													                                        remarks:remarks.replace(/<br>/g,"\n").replace(/<bq>/g,"'"),
													                                        testResultDetailDocId:'<s:property value="documentNumber"/>'
													                                       });
													            TESTRESULT_DETAILS_INDEX++;
													            detailsTotal = getNumber(<s:property value="jobDetails.sampleLetterDetails.testSheetDetails.testCharges"/>) * getNumber(<s:property value="jobDetails.receivedQuantity"/>) ;
													            detailsTotal = roundTo((detailsTotal),2,'0');
													            detailsGrandTotal =  getNumber( detailsGrandTotal ) + getNumber(detailsTotal);
													            record = testResultDetailsDataTable.getRecord(parseInt(rowCount));
													            // Important to use escape=false. Otherwise struts will replace double quotes with &quote; 
													            testResultDetailsDataTable.updateCell(record,testResultDetailsDataTable.getColumn('totalCharges'),detailsTotal);
													            rowCount++;
														        </s:iterator>
														        dom.get("testResultDetailsTotal").innerHTML=roundTo((detailsGrandTotal),2,'0');
												               </script> 
												              </div>
												            </td>
												        </tr>
											        </table>
        										</td>
											</tr>
										    <tr><td colspan="4">&nbsp;</td></tr>
										    <tr>
												<td class="shadowwk" colspan="4"></td>  
											</tr>
											<s:if test="%{sourcePage!='search'}">   
											 <tr> 
												<td colspan="4">
													<div id="manual_workflow">
														<c:set var="approverHeadCSS" value="headingwk" scope="request" />
														<c:set var="approverCSS" value="bluebox" scope="request" />
														<s:hidden name="departmentName" id="departmentName" value="%{departmentName}"/>
														<%@ include file="/commons/commonWorkflow.jsp"%>
													</div>
												</td>
										    </tr>
										    </s:if>
											<tr>
												<td colspan="4"  class="shadowwk"></td>
											</tr> 
										</table>
									</td>
								</tr>
							</table>
							<div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">* 
								<s:text name="message.mandatory" />
							</div>
								
						</div>
						<!-- end of rbroundbox2 -->
						
					</div>
					<!-- end of insidecontent -->
					<div class="buttonholderwk" id="buttons">
						<s:if test="%{(sourcePage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
						|| model.egwStatus.code=='REJECTED')  && (sourcePage !='search') || hasErrors() || hasActionMessages()}">  
							<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
								<s:submit type="submit" cssClass="buttonfinal"
								value="Save" id="Save" name="Save"
								method="save"
								onclick="document.testResultForm.actionName.value='Save';return validateInput('%{name}');" />
							</s:if>	 						
							<s:iterator value="%{getValidActions()}" var="name">
								<s:if test="%{name!=''}">
									<s:submit type="submit" cssClass="buttonfinal"
										value="%{name}" id="%{name}" name="%{name}"
										method="save"
										onclick="document.testResultForm.actionName.value='%{name}';return validateInput('%{name}');" />
								</s:if>
							</s:iterator>				
							</s:if>
											
							<s:if test="%{(sourcePage=='inbox' || sourcePage=='search')}">
								<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value="model.state.id"/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"
									class="buttonfinal" value="Workflow History" id="history" name="History" />
							</s:if>		
							<s:if test="%{sourcePage != 'search'}"> 				
								<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='test.result.cancel.confirm'/>');"/>
							</s:if>
						     <s:else>
						         <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
						     </s:else>
						</div>
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>