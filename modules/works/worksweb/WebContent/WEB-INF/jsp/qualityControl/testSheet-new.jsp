<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
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
			<s:text name="qualityControl.testSheet.title" />
		</title>
	</head>	
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script type="text/javascript"> 

	var selectedDropdownObjectName;
	var suffixToBeReplaced;
	var suffixToReplaceWith;
	var testSheetTableIndex=0;
	var selectedTestChargesObject;

	function loadTestCharges(obj,recId)
	{
		var paramValue=obj.value;
		selectedTestChargesObject="testCharges"+recId;
		makeJSONCall(["testCharges"],'${pageContext.request.contextPath}/qualityControl/ajaxTestSheet!populateTestCharges.action',
		    	{testMasterID:paramValue},successHandler,failureHandler) ;
    }

	successHandler= function(req,res){
	  	results=res.results;
	 	var charges = roundTo(results[0].testCharges);
	 	dom.get(selectedTestChargesObject).value=charges;
	 	calculateTSheetTotal();
	}

	failureHandler= function(){
		alert('Unable to load test charges. Rate does not exists!!');
	}
	
	function resetDropDowns(obj,recordId)
    {
        var objName = obj.name;
        var rowIndex = getRowIndexWithRowName(objName);
        var namePrefix = "actionTestSheetDetails[" + rowIndex + "]." ;
        var dropdownObj;
        var i;
       	dropdownObj = document.getElementsByName(namePrefix+"testMaster.id")[0];
       //dropdownObj = document.getElementsByName(namePrefix+"testNamesIdList")[0];
     
       	for(i=dropdownObj.options.length-1;i>=1;i--)
       	{
       		dropdownObj.remove(i);
       	}
       	var paramValue=obj.value;
       	var record=testSheetDataTable.getRecord(recordId);
       	//dom.get("testCharges"+record.getId()).value='';
       	//calculateTSheetTotal();
       	selectedDropdownObjectName=obj.name;
		var url = '../qualityControl/ajaxTestSheet!populateTestName.action?materialTypeId='+paramValue+'&recId='+recordId;
		YAHOO.util.Connect.asyncRequest('POST', url, ajaxResultList, null);
    }
	var ajaxResultList={
			success: function(o) {
				if(o.responseText!="")
				{
					var docs=o.responseText;   
					var output=docs.split("#")
					res=output[0].split("$");
					var dropdownName=selectedDropdownObjectName;
					dropdownName=dropdownName.replace("testMaster.materialType","testMaster");
					//dropdownName=dropdownName.replace("materialType","testNamesIdList");
					//alert("dropdownName>>>>"+dropdownName);
					//alert("x>>>>>  "+document.getElementsByName(dropdownName));
					var x=document.getElementsByName(dropdownName)[0];
					x.length=0;
					x.options[0]=new Option("---Choose---","-1");   
					var j=0; 
					var mTypeDocId;        
					for(var i=0;i<res.length-1;i++)
					{
						var idandname=res[i].split('~'); 
						x.options[++j]=new Option(idandname[0],idandname[1]);
						mTypeDocId=idandname[2];
				    } 
					var record=testSheetDataTable.getRecord(output[1]);
					testSheetDataTable.updateCell(record,testSheetDataTable.getColumn('materialTypeDocId'),mTypeDocId);                    
				}
			},                                         
			failure: function(o) {
				alert('Unable to load test Name list');
			}
		};

    function createEmptyDropDown(suffix){
    	return function(el, oRecord, oColumn, oData) {
			var onchangeParam;
    		var fieldName = "actionTestSheetDetails[" + oRecord.getCount() + "]." + suffix; 
    		// multiple=true size=4
   			var	element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  onchange='loadTestCharges(this,\""+oRecord.getId()+"\");' ><option value=-1 selected='selected' > --- Choose --- </option>  ";
    		element=element+" </select>";
    		el.innerHTML =element ;
    		};
    }

	function createHiddenFormatter(el, oRecord, oColumn, oData){
        var hiddenFormatter = function(el, oRecord, oColumn, oData) {
            var value = (YAHOO.lang.isValue(oData))?oData:"";
            var id=oColumn.getKey()+oRecord.getId();
            var fieldName = "actionTestSheetDetails[" + oRecord.getCount() + "]." + oColumn.getKey()+ ".id";
            var fieldValue=value;
            markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
            el.innerHTML = markup;
        }
        return hiddenFormatter;
        }
        var testSheetHiddenFormatter = createHiddenFormatter(10,10);

        function createMaterialTypeDropDown(suffix){
        	return function(el, oRecord, oColumn, oData) {
        		var fieldName = "actionTestSheetDetails[" + oRecord.getCount() + "]." + suffix;
        		var element="<select  id='"+oColumn.getKey()+oRecord.getId()+"' name='"+fieldName+"'  style=width:90px  onchange='resetDropDowns(this,\""+oRecord.getId()+"\");'  >";
        		element=element+"<option value=-1 selected='selected' >---Choose---</option>  ";
        		<s:iterator value="dropdownData.materialTypeList" status="stat">
        			var name='<s:property value="name"/>';
        			var id1='<s:property value="id" />';
        			//element=element+" <option value="+id1 +" > "+ name+" </option>  ";
        			element=element+" <option value="+id1 +" > "+ name+" </option>  ";
        		</s:iterator>
        		element=element+" </select>";
        		el.innerHTML =element ;
        		};
        }
         //var dataTableDDMaterialType = createMaterialTypeDropDown('materialType.id');
         var dataTableDDMaterialType = createMaterialTypeDropDown('testMaster.materialType.id');
		 var dataTableDDTestName = createEmptyDropDown('testMaster.id');
        //var dataTableDDTestName = createEmptyDropDown('testNamesIdList');
       
        var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
        	 var value = (YAHOO.lang.isValue(oData))?oData:"";
        	   var fieldName = "actionTestSheetDetails[" + oRecord.getCount() + "]." +oColumn.getKey();
        	   if(oColumn.getKey()=='uom')
        		markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' value='"+value+"' size='10' maxlength='4000' name='"+fieldName+"' />"
        		else
        			markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk'  size='30' maxlength='4000' name='"+fieldName+"' value='"+value+"' />"	
        		el.innerHTML = markup;	 	
        	} 

        function createTextBoxFormatter(size,maxlength){
        	var textboxFormatter = function(el, oRecord, oColumn, oData) {
        	    var value = (YAHOO.lang.isValue(oData))?oData:"";
       	        var id = oColumn.getKey()+oRecord.getId();
        	    var fieldName = "actionTestSheetDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
        	    markup="<input type='text' id='"+id+"' name='"+fieldName+"' disabled=true size='"+size+"' maxlength='"+maxlength+"' value='"+value+"' class='selectamountwk' />"; 
        	    el.innerHTML = markup;
        	}
        	return textboxFormatter;
        	}
        	var textboxFormatter = createTextBoxFormatter(10,13);


        	function createDocUploadFormatterTestSheet(){
        			var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
            		var tableIndex =testSheetTableIndex;
            		var id ="actionTestSheetDetails[" + tableIndex + "].documentNumberButton";
        			var fieldName = "actionTestSheetDetails[" + tableIndex + "].documentNumberButton";
        			<s:if test="%{sourcePage=='createTestSheet' || (sourcePage=='inbox' && (model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'))}">
        			 markup="<input type='submit' class='buttonadd' value='Attach' id='"+id+"' name='"+fieldName+"' style='width:60px' onclick='showDocumentManagerTestSheet(this);return false;'/>";
        			</s:if>
        			<s:else> 
        			markup="<input type='submit' class='buttonadd' value='View Doc' id='"+id+"' name='"+fieldName+"' style='width:60px' onclick='viewDocumentManagerTestSheet(this);return false;'/>"; 
        			</s:else>
        			//markup='<input type="submit" class="buttonsubmit" value="Attach" id='"+id+"' name='"+fieldName+"' style="width:60px" onclick="showDocumentManagerTeshSheet(this);return false;" />';
        			el.innerHTML = markup;
        		}
        		return deleteImageFormatter;
        	}
        	var ELEMENTID;
        	function showDocumentManagerTestSheet(obj){
        		var index=getRowIndex(obj);
        		ELEMENTID = "actionTestSheetDetails["+index+"].documentNumber"; 
        	    docManager(document.getElementById(ELEMENTID).value);
        	} 
        	var docNumberUpdater = function (docNumber){
        		document.getElementById(ELEMENTID).value = docNumber;
        	}

        	function docManager(docNumber){
        	    var url;
        	    if(docNumber==null||docNumber==''||docNumber=='To be assigned'||docNumber==0){
        	      url="/egi/docmgmt/basicDocumentManager.action?moduleName=Works";
        	    }else{
        	      url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+docNumber+"&moduleName=Works";
        	    }
        	
        	    var wdth = 1000;
        	    var hght = 400;
        	    window.open(url,'docupload','width='+wdth+',height='+hght);
        	}

        	function viewDocumentManagerTestSheet(obj){
        		var index=getRowIndex(obj);
        		var elemId = "actionTestSheetDetails["+index+"].documentNumber"; 
        		viewDocumentManager(document.getElementById(elemId).value); 
            }

        	function showFreqChart(recordId){
        		record=testSheetDataTable.getRecord(recordId);
        		if(dom.get("materialType"+record.getId()).value== -1){  		
        	  		document.getElementById("testSheet_error").innerHTML='<s:text name="testSheet.materialType.null"/>';
        	  		document.getElementById("testSheet_error").style.display='';
        	      	return false;
        	  	}
        		else{
        			 document.getElementById("testSheet_error").style.display='none';
        	       	 document.getElementById("testSheet_error").innerHTML='';
        	       	 viewDocumentManager(record.getData('materialTypeDocId')); 
               	}
        		return true;
             }

        	function createHiddenDocAttachTextFieldFormatter(){
        	    return function(el, oRecord, oColumn, oData) {
        	    	var index=testSheetTableIndex;
        	    	var id ="actionTestSheetDetails[" + index + "].documentNumber";
        			var fieldName = "actionTestSheetDetails[" + index + "].documentNumber";
        			var value = (YAHOO.lang.isValue(oData))?oData:"";
        			el.innerHTML = " <input type='text' id='"+id+"' value='"+value+"' name='"+fieldName+"' style='width:90px;' />";
        		}
        	}

        	function materialTypeDocFormatter(){
        		return function(el, oRecord, oColumn, oData) {

        			var id = oColumn.getKey()+oRecord.getId();
            	    var fieldName = oColumn.getKey();
        			//markup='<input type="submit" class="buttonsubmit" value="Attach" id='"+id+"' name='"+fieldName+"' style="width:60px" onclick="showDocumentManagerTeshSheet(this);return false;" />';
        			markup="<input type='submit' class='buttonadd' id='"+id+"' name='"+fieldName+"' value='Frequency Chart'  onclick='showFreqChart(\""+oRecord.getId()+"\");return false;'/>";
        			el.innerHTML = markup;
        		}
            }

        	function getRowIndex(obj) 
        	{
        		var temp =obj.name.split('[');
        		var temp1 = temp[1].split(']');
        		return temp1[0];
        	}

	var testSheetDataTable;
    var makeTestSheetDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var testSheetColumnDefs = [
            {key:"testSheetHeader", hidden:true,formatter:testSheetHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:50},
            {key:"materialType", label:'Material Type<span class="mandatory">*</span>', width:90,formatter:dataTableDDMaterialType,resizeable:true},
            {key:"testName", label:'Name of Test<span class="mandatory">*</span>', width:90, formatter:dataTableDDTestName,resizeable:true},
            {key:"uom", label:'UOM', formatter:textboxDescFormatter, sortable:false, resizeable:false},	
            {key:"description", label:'Description', formatter:textboxDescFormatter, sortable:false, resizeable:false},		
            {key:"testCharges",label:'Testing Charges', formatter:textboxFormatter,sortable:false, disabled:true, resizeable:false},
            {key:"docNo",label:'Attach',width:60, formatter:createDocUploadFormatterTestSheet(),resizeable:false},
            {key:"freqChart",label:'View Frequency Chart',width:110, formatter:materialTypeDocFormatter(),resizeable:false},
            {key:'deleteTestSheet',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")},
            {key:"documentNumber",label:'documentNumber',hidden:true, formatter:createHiddenDocAttachTextFieldFormatter()},
            {key:"materialTypeDocId", hidden:true, sortable:false, resizeable:false}
        ];
       
        var testSheetDataSource = new YAHOO.util.DataSource();
        testSheetDataTable = new YAHOO.widget.DataTable("testSheetTable",testSheetColumnDefs, testSheetDataSource, {MSG_EMPTY:"<s:text name='testSheet.initial.table.message'/>"});
        testSheetDataTable.subscribe("cellClickEvent", testSheetDataTable.onEventShowCellEditor);
        testSheetDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            <s:if test="%{sourcePage=='createTestSheet' || (sourcePage=='inbox' && (model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED'))}">
            if (column.key == 'deleteTestSheet') { 
            		recalculateTotalOnDelete(record);   
                    this.deleteRow(record);
                    allRecords=this.getRecordSet();
                    for(i=0;i<allRecords.getLength();i++){
                        this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                    }
            }
            </s:if>       
        });

        var tfoot = testSheetDataTable.getTbodyEl().parentNode.createTFoot();
    	var tr = tfoot.insertRow(-1);
    	var th = tr.appendChild(document.createElement('td'));
    	th.colSpan = 5;
    	th.className= 'whitebox4wk';
    	th.innerHTML = '&nbsp;';

    	var td = tr.insertCell(1);
    	td.className= 'whitebox4wk';
    	td.id = 'tSheetTotal';
    	td.innerHTML = '<span class="bold">Total:</span>'; 

    	addCellRightAligned(tr,2,'testChargesTotal','0.00');    
    	addCell(tr,3,'filler','');
    	addCell(tr,4,'filler','');
    	addCell(tr,5,'filler','');  
        return {
            oDS: testSheetDataSource,
            oDT: testSheetDataTable
        }; 
    }
    
    function updateTestSheetTableIndex(){
    	testSheetTableIndex=testSheetTableIndex+1;
    }


    function calculateTSheetTotal(){ 
    	var chargesTotal=0;
    	var Records= testSheetDataTable.getRecordSet();
       	for(var i=0;i<testSheetDataTable.getRecordSet().getLength();i++)
       	{
       		if(!isNaN(getNumber(dom.get("testCharges" + Records.getRecord(i).getId()).value)))
    	    		  chargesTotal=chargesTotal+getNumber(dom.get("testCharges" + Records.getRecord(i).getId()).value);
       	}
       	var val=roundTo((chargesTotal),2,'0');
    	dom.get("testChargesTotal").innerHTML=val;
    }

    function recalculateTotalOnDelete(record){
    	var oldTestCharges=dom.get("testCharges" + record.getId()).value;
    	var oldTotal=dom.get("testChargesTotal").innerHTML;
   		dom.get("testChargesTotal").innerHTML=roundTo((getNumber(oldTotal)-getNumber(oldTestCharges)),2,'0');
    }
        

    function validateInput(text){
    	if(document.getElementById("actionName").value=='Cancel'){
    		if(!validateCancel()){
    			return false;
    		}
    	}
    	clearErrorMessage();
    	var tSheet_mType;
    	var tSheet_tName;
    	var tSheet_charges;
    	if(testSheetDataTable.getRecordSet().getLength()==0){
    		dom.get("testSheet_error").style.display='';
    	    dom.get("testSheet_error").innerHTML='<s:text name='testSheet.dataTable.zeroLength' />';
    		return false;
    	}
    	else{
    		var Records=testSheetDataTable.getRecordSet();
    		for(var i=0;i<testSheetDataTable.getRecordSet().getLength();i++)
       		{
    			tSheet_mType=dom.get("materialType" +  Records.getRecord(i).getId()).value;
    	    	tSheet_tName=dom.get("testName" +  Records.getRecord(i).getId()).value;
    	    	tSheet_charges=dom.get("testCharges" +  Records.getRecord(i).getId()).value;
    	    	if(tSheet_mType == -1){
    	    		dom.get("testSheet_error").style.display='';
    	    	    dom.get("testSheet_error").innerHTML='Please Choose Material Type at line '+Records.getRecord(i).getData("SlNo");
    	    	    return false;
    	        }
    	    	if(tSheet_mType != -1 && tSheet_tName == -1){
    	    		dom.get("testSheet_error").style.display='';
    	    	    dom.get("testSheet_error").innerHTML='Please Choose Test Name at line '+Records.getRecord(i).getData("SlNo");
    	    	    return false;
    	        }
    	    	if(tSheet_charges == "" || tSheet_charges <= 0){
    	    		dom.get("testSheet_error").style.display='';
    	    	    dom.get("testSheet_error").innerHTML='Test Charges cannot be zero. Please Delete Test Sheet details at line '+Records.getRecord(i).getData("SlNo");
    	    	    return false;
    	        }
       		} 
       		if(!validateDuplicateEntry())
           		return false;
    	}
    	
    	if(text!='Approve' && text!='Reject'){
    		if(!validateWorkFlowApprover(text))
    			return false;
    	}
    	
    	enableElements();
    	return true;  
    }

    function setCurrentdate(){
    	var tsDate=dom.get("testSheetDate").value;
    	if(tsDate=='') {
    		dom.get("testSheetDate").value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
    	}else{	
    		dom.get('testSheetDate').value=tsDate;
    	}
    }

    function enableElements(){
		for(var i=0;i<document.forms[0].length;i++) 
      		document.forms[0].elements[i].disabled =false;
    }

    function clearErrorMessage(){
    	dom.get("testSheet_error").style.display='none';
    	document.getElementById("testSheet_error").innerHTML=''; 
    }

    function validateDuplicateEntry(){
    	var tSheet_mType;
    	var tSheet_tName;
    	var mType;
    	var tName;
    	var Records=testSheetDataTable.getRecordSet();
		for(var i=0;i<testSheetDataTable.getRecordSet().getLength();i++)
   		{
			tSheet_mType=dom.get("materialType" +  Records.getRecord(i).getId()).value;
	    	tSheet_tName=dom.get("testName" +  Records.getRecord(i).getId()).value;
	    	if(tSheet_mType != -1 && tSheet_tName != -1){
		    	for(var j=i+1;j<testSheetDataTable.getRecordSet().getLength();j++){
		    		mType=dom.get("materialType" +  Records.getRecord(j).getId()).value;
			    	tName=dom.get("testName" +  Records.getRecord(j).getId()).value;
			    	if(mType != -1 && tName != -1){
				    	if(tSheet_mType==mType){
				    		if(tSheet_tName==tName){
				    			var obj=dom.get("testName"+Records.getRecord(i).getId());
				       	    	var objValue=obj.options[obj.selectedIndex].innerHTML;
				    			dom.get("testSheet_error").style.display='';
			    	    	    dom.get("testSheet_error").innerHTML='Duplicate Test Name!! Test Name '+objValue+' is selected multiple times between Sl No '+Records.getRecord(i).getData("SlNo")+' and '+Records.getRecord(j).getData("SlNo");
			    	    	    return false; 
							}
					    }
			    	}
				}	
	    	}
   		}
   		return true; 
    }

    function enableOrdisableElements()
    {
        <s:if test="%{sourcePage=='search' || (sourcePage=='inbox' && (model.egwStatus!=null && !(model.egwStatus.code=='NEW' || model.egwStatus.code=='REJECTED')))}">
	        links=document.testSheetHeaderForm.getElementsByTagName("a");
            for(i=0;i<links.length;i++){
                     links[i].onclick=function(){return false;};  
            }

            var Records=testSheetDataTable.getRecordSet();
    		for(var i=0;i<testSheetDataTable.getRecordSet().getLength();i++)
       		{
    			dom.get('materialType'+ Records.getRecord(i).getId()).readonly=true;
            	dom.get('materialType'+ Records.getRecord(i).getId()).disabled='true'; 

            	dom.get('testName'+ Records.getRecord(i).getId()).readonly=true;
            	dom.get('testName'+ Records.getRecord(i).getId()).disabled='true'; 

            	dom.get('uom'+ Records.getRecord(i).getId()).readonly=true;
            	dom.get('uom'+ Records.getRecord(i).getId()).disabled='true'; 

            	dom.get('description'+ Records.getRecord(i).getId()).readonly=true;
            	dom.get('description'+ Records.getRecord(i).getId()).disabled='true'; 
       		}
	        document.testSheetHeaderForm.closeButton.readonly=false;
            document.testSheetHeaderForm.closeButton.disabled=false;  
        </s:if>
    }

	
    function loadDesignationFromMatrix(){  
   		var dept=dom.get('departmentName').value;
      	var currentState = dom.get('currentState').value;
   		var amountRule =  dom.get('amountRule').value;
   		var additionalRuleValue =  dom.get('additionalRule').value; 
   		var pendingAction=document.getElementById('pendingActions').value;
   		loadDesignationByDeptAndType('TestSheetHeader',dept,currentState,amountRule,additionalRuleValue,pendingAction); 
    }

    function populateApprover() {
    	getUsersByDesignationAndDept(); 
    }

      
    function validateCancel() {
    	var msg='<s:text name="testSheet.cancel.confirm"/>';
    	 
    	if(!confirmCancel(msg)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
	</script>
	
	
	<body onload="setCurrentdate();enableOrdisableElements();">	
	<s:if test="%{hasErrors()}"> 
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
   
	<s:if test="%{hasActionMessages()}">
        <div class="messagestyle">  
        	<s:property value="%{model.testSheetNumber}"/> &nbsp; <s:actionmessage theme="simple"/>
        </div>
    </s:if>
   	<div class="errorstyle" id="testSheet_error" style="display: none;"></div>
	<s:form action="testSheet" name="testSheetHeaderForm"  theme="simple"> 
	<s:if test="%{sourcePage != 'search'}"> 
	<s:token />
	</s:if>
	<s:push value="model">
	<s:hidden name="id" id="id" />
	<s:hidden name="workOrderId" id="workOrderId" />
	<s:hidden name="sourcePage" id="sourcePage" />
	<s:hidden name="testSheetDate" id="testSheetDate" /> 
	<input type="hidden" name="actionName" id="actionName"/>
	 <div class="navibarshadowwk"></div>
	<div class="formmainbox">
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div> 
	<div class="rbcontent2"><div class="datewk"> 
		<div class="estimateno"><s:text name="qualityControl.testSheet.testSheetNumber" />: <s:if test="%{not model.testSheetNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.testSheetNumber" /></div>
		<div class="estimateno" style="text-align: right"><s:text name="qualityControl.testSheet.creationDate" />: <s:if test="%{sourcePage=='createTestSheet'}"><s:property value="new java.text.SimpleDateFormat('dd/MM/yyyy').format(new java.util.Date())" /></s:if><s:else><s:property value="new java.text.SimpleDateFormat('dd/MM/yyyy').format(testSheetDate)" /></s:else></div> 
		<div class="estimateno"><s:text name="qualityControl.testSheet.woNumber" />: <s:property value="workOrderNumber" /></div> 
	</div>
	
	 
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      	<tr>
       		<td colspan="4" class="headingwk">
       			<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
         		<div class="headplacer"><s:text name="testSheet.header" /></div>
          	</td>
      	</tr>
      	<tr>
        <td>&nbsp;</td>
      	</tr>
      	<tr>
        <td>
        <table id="qcTestSheetTable" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td align="right" class="headingwk" style="border-left-width: 0px"><a href="#" onclick="testSheetDataTable.addRow({SlNo:testSheetDataTable.getRecordSet().getLength()+1});updateTestSheetTableIndex();return false;"><img border="0" alt="Add Rate" src="${pageContext.request.contextPath}/image/add.png" /></a>
            </td>
        </tr>
        <tr>
            <td colspan="4">
            <div class="yui-skin-sam">
            <div id="testSheetTable"></div>
            <script>
                makeTestSheetDataTable();
                var rowCount = 0;
    			var iteratorIndex =0;
    			var materialTypeDD;
    			var testMasterDD;
                <s:iterator var="tsDetailsList" value="testSheetDetails" status="row_status">
                testSheetDataTable.addRow({testSheetHeader:'<s:property value="testSheetHeader.id"/>',
                                            SlNo:'<s:property value="#row_status.count"/>',
                                            materialType:'<s:property value="testMaster.materialType.id"/>', 
                                            testName:'<s:property value="testMaster.id"/>',
                                            uom:'<s:property value="uom"/>',
                                            description:'<s:property value="descriptionJS" escape="false"/>',    
                                            testCharges:roundTo('<s:property value="testCharges"/>'),
                                            documentNumber:'<s:property value="documentNumber"/>', 
                                            materialTypeDocId:'<s:property value="testMaster.materialType.documentNumber"/>'                                     
                                           });
                var record = testSheetDataTable.getRecord(parseInt(rowCount));
                
                materialTypeDD = document.getElementsByName("actionTestSheetDetails["+rowCount+"].testMaster.materialType.id")[0];
                materialTypeDD.value='<s:property value="testMaster.materialType.id" />';

                var column = testSheetDataTable.getColumn('description');
                // Important to use escape=false. Otherwise struts will replace double quotes with &quote;  
                dom.get(column.getKey()+record.getId()).value = '<s:property value="descriptionJS" escape="false"/>';
                
                testMasterDD = document.getElementsByName("actionTestSheetDetails["+rowCount+"].testMaster.id")[0];
             	testMasterDD.length=0;
             	
             	testMasterDD.options[0]=new Option("---Choose---","-1");
				iteratorIndex=1;
				
				<s:iterator id="testNameList" value="TestNamesIdList" status="tn_status">
					testMasterDD.options[iteratorIndex]=new Option("<s:property value='testName'/>",<s:property value="id"/>);
					iteratorIndex++;
				</s:iterator>
				testMasterDD.value=<s:property value="testMaster.id" />;

                rowCount++;
                updateTestSheetTableIndex();
    	        </s:iterator>
    	        calculateTSheetTotal();
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
		<td>
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
		<td  class="shadowwk"></td>
	</tr>   	
	 </table> 
	
	 <div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">* 
		<s:text name="message.mandatory" />
	</div>    
	</div>	
	<div class="rbbot2"><div></div></div>
	</div>
	</div>
	
	<div class="buttonholderwk" id="buttons">
			<s:if test="%{(sourcePage=='inbox' || model.egwStatus==null || model.egwStatus.code=='NEW'  
			|| model.egwStatus.code=='REJECTED')  && (sourcePage !='search') || hasErrors() || hasActionMessages()}">  
				<s:if test="%{model.id==null || model.egwStatus.code=='NEW'}">
					<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.testSheetHeaderForm.actionName.value='Save';return validateInput('%{name}');" />
				</s:if>	 						
				<s:iterator value="%{getValidActions()}" var="name">
					<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonfinal"
							value="%{name}" id="%{name}" name="%{name}"
							method="save"
							onclick="document.testSheetHeaderForm.actionName.value='%{name}';return validateInput('%{name}');" />
					</s:if>
				</s:iterator>				
				</s:if>
								
				<s:if test="%{(sourcePage=='inbox' || sourcePage=='search')}">
					<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue=<s:property value="model.state.id"/>', '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"
						class="buttonfinal" value="Workflow History" id="history" name="History" />
				</s:if>		
				<s:if test="%{sourcePage != 'search'}"> 				
					<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='testSheet.close.confirm'/>');"/>
				</s:if>
			     <s:else>
			         <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
			     </s:else>
			</div>
	
	</div>
	</s:push>
	</s:form>
	</body>
</html>