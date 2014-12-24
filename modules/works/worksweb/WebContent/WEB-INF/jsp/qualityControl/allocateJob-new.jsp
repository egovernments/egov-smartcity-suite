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
			<s:text name="qualityControl.createJob.title" />
		</title>
	</head>	
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script type="text/javascript"> 

	function createHiddenFormatter(el, oRecord, oColumn, oData){
        var hiddenFormatter = function(el, oRecord, oColumn, oData) {
            var value = (YAHOO.lang.isValue(oData))?oData:"";
            var id=oColumn.getKey()+oRecord.getId();
            var fieldName;
            if(oColumn.getKey()=="jobHeader")
            	fieldName = "actionJobDetails[" + oRecord.getCount() + "]." + oColumn.getKey()+ ".id";
            if(oColumn.getKey()=="sampleLetterDetails")
           	 	fieldName = "actionJobDetails[" + oRecord.getCount() + "]." + oColumn.getKey()+ ".id";
            var fieldValue=value;
            markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
            el.innerHTML = markup;
        }
        return hiddenFormatter;
        }
        var jobHeaderHiddenFormatter = createHiddenFormatter(10,10);
        var sampleLetterDetailsHiddenFormatter = createHiddenFormatter(10,10);

        var textboxFormatter = function(el, oRecord, oColumn, oData) {
       	 var value = (YAHOO.lang.isValue(oData))?oData:"";
       	    var fieldName = oColumn.getKey();
       	    if(fieldName=="total")
       			markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' disabled=true class='selectwk' size='10' value='"+value+"'  name='"+fieldName+"' />"
       		else
       			markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' disabled=true class='selectamountwk'  size='10' value='"+value+"'  name='"+fieldName+"' />"
           			
            el.innerHTML = markup;	 	
       	}

        function createTextBoxQtyFormatter(size,maxlength){
        	var textboxFormatter = function(el, oRecord, oColumn, oData) {
        	    var value = (YAHOO.lang.isValue(oData))?oData:"";
       	        var id = oColumn.getKey()+oRecord.getId();
        	    var fieldName = "actionJobDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
        	    markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' value='"+value+"' class='selectamountwk' onblur='calculateTotalAmount(this,\""+oRecord.getId()+"\");'/>";
        	    el.innerHTML = markup;
        	}
        	return textboxFormatter;
        	}
        	var textboxQtyFormatter = createTextBoxQtyFormatter(10,13);

        	function documentFormatter(){
        		return function(el, oRecord, oColumn, oData) {
        			var id = oColumn.getKey()+oRecord.getId();
            	    var fieldName = oColumn.getKey();
       				markup="<input type='submit' class='buttonadd' id='"+id+"' name='"+fieldName+"' value='View Doc'  onclick='showDocumentAttached(\""+oRecord.getId()+"\");return false;'/>";
        			el.innerHTML = markup;
        		}
            }

	var allocateJobDataTable;
    var makeAllocateJobDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var allocateJobColumnDefs = [
            {key:"jobHeader", hidden:true,formatter:jobHeaderHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"sampleLetterDetails", hidden:true,formatter:sampleLetterDetailsHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:20},
            {key:"materialType", label:'Material Type', disabled:true,resizeable:true},
            {key:"testName", label:'Name of Test',disabled:true,resizeable:true},
            {key:"uom", label:'UOM',  sortable:false,disabled:true,resizeable:false},	
            {key:"description", label:'Description', sortable:false, disabled:true, resizeable:false},	
            {key:"testCharges",label:'Rate', formatter:textboxFormatter,sortable:false, disabled:true,resizeable:false},
            {key:"sampleQuantity",label:'Sample Quantity', formatter:textboxFormatter,sortable:false, disabled:true,resizeable:false},
            {key:"receivedQuantity",label:'Received Quantity', formatter:textboxQtyFormatter,sortable:false, resizeable:false},
            {key:"total",label:'Total Charges', sortable:false, formatter:textboxFormatter, disabled:true, resizeable:false},
            {key:"testSheetDoc",label:'View Attachment',formatter:documentFormatter(),width:80,resizeable:false},
            {key:"testSheetDocId", hidden:true, sortable:false, resizeable:false}
        ];  
       
        var allocateJobDataSource = new YAHOO.util.DataSource();
        allocateJobDataTable = new YAHOO.widget.DataTable("allocateJobTable",allocateJobColumnDefs, allocateJobDataSource, {MSG_EMPTY:""});
        allocateJobDataTable.subscribe("cellClickEvent", allocateJobDataTable.onEventShowCellEditor);
        allocateJobDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
        });

        var tfoot = allocateJobDataTable.getTbodyEl().parentNode.createTFoot();
    	var tr = tfoot.insertRow(-1);
    	var th = tr.appendChild(document.createElement('td'));
    	th.colSpan =9;
    	th.className= 'whitebox4wk';
    	th.innerHTML = '&nbsp;';

    	var td = tr.insertCell(1);
    	td.className= 'whitebox4wk';
    	td.id = 'sLetterTotal';
    	td.innerHTML = '<span class="bold">Total:</span>';

    	addCell(tr,2,'allocateJobTotal','0.00');
    	addCell(tr,3,'filler','');
        return {
            oDS: allocateJobDataSource,
            oDT: allocateJobDataTable
        }; 
    }

    function showDocumentAttached(recordId){
		record=allocateJobDataTable.getRecord(recordId);
		viewDocumentManager(record.getData('testSheetDocId'));
		return true;
     }

    function calculateTotalAmount(elem,recordId){
    	clearErrorMessage();
     	record=allocateJobDataTable.getRecord(recordId);
     	var charges=getNumber(record.getData("testCharges"));
     	var sampleQty=getNumber(record.getData("sampleQuantity"));
     	
     	if(!checkUptoFourDecimalPlace(elem,dom.get('allocateJob_error'),"Received Quantity"))
 			return false;  

     	if(isNaN(elem.value))
		{
			dom.get("allocateJob_error").style.display='';
			document.getElementById("allocateJob_error").innerHTML='Received Quantity for Sl No '+record.getData("SlNo")+' should be Numeric';
			allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('receivedQuantity'),'');
			return false;
		}

     	if(elem.value!='' && elem.value<0){
			dom.get("allocateJob_error").style.display='';
			document.getElementById("allocateJob_error").innerHTML='Negative Quantity!! Received Quantity for Sl No '+record.getData("SlNo")+' is Negative';
			allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('receivedQuantity'),'');
			return false;
		}
		
		if(elem.value!='' && getNumber(elem.value)>getNumber(sampleQty)){
			dom.get("allocateJob_error").style.display='';
			document.getElementById("allocateJob_error").innerHTML='Received Quantity for Sl No '+record.getData("SlNo")+' cannot be greated than  Sample Quantity';
			allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('receivedQuantity'),'');
			return false;
		}

     	allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('total'),roundTo(charges*elem.value));
     	calculateFinalTotal();
    }


    function calculateFinalTotal(){ 
    	var finTotal=0;
    	var Records= allocateJobDataTable.getRecordSet();
       	for(var i=0;i<allocateJobDataTable.getRecordSet().getLength();i++)
       	{
       		if(!isNaN(getNumber(dom.get("total" + Records.getRecord(i).getId()).value)))
    	    		  finTotal=finTotal+getNumber(dom.get("total" + Records.getRecord(i).getId()).value);
       	}
       	var val=roundTo((finTotal),2,'0');
    	dom.get("allocateJobTotal").innerHTML=val;
    } 

    function clearErrorMessage(){
    	dom.get("allocateJob_error").style.display='none';
    	document.getElementById("allocateJob_error").innerHTML=''; 
    }

	function validateInput(){
		clearErrorMessage();
		if(!validateTableData())
       		return false;
		return true;
	}

	function validateTableData(){
    	var received_Qty;
    	var sampleQty;
    	var flag=0;
    	var Records=allocateJobDataTable.getRecordSet();
		for(var i=0;i<allocateJobDataTable.getRecordSet().getLength();i++)
   		{
			received_Qty=dom.get("receivedQuantity" +  Records.getRecord(i).getId()).value;
			sampleQty=dom.get("sampleQuantity" +  Records.getRecord(i).getId()).value;
			if(isNaN(received_Qty))
			{
				dom.get("allocateJob_error").style.display='';
				document.getElementById("allocateJob_error").innerHTML='Received Quantity for Sl No '+Records.getRecord(i).getData("SlNo")+' should be Numeric';
				return false;
			}

			if(!checkUptoFourDecimalPlace(dom.get("receivedQuantity" +  Records.getRecord(i).getId()),dom.get('allocateJob_error'),"Received Quantity"))
	 			return false;  
			
			if(received_Qty!='' && received_Qty<0){
				dom.get("allocateJob_error").style.display='';
				document.getElementById("allocateJob_error").innerHTML='Negative Quantity!! Received Quantity for Sl No '+Records.getRecord(i).getData("SlNo")+' is Negative';
				return false;
			}
			if(received_Qty!='' && getNumber(received_Qty)>getNumber(sampleQty)){
				dom.get("allocateJob_error").style.display='';
				document.getElementById("allocateJob_error").innerHTML='Received Quantity for Sl No '+Records.getRecord(i).getData("SlNo")+' cannot be greated than  Sample Quantity';
				return false;
			}
			
			if((received_Qty=='' || received_Qty==0) && flag==0){
				flag=0;
			}
			else
				flag=1;
   		}
   		if(flag==0){
   			dom.get("allocateJob_error").style.display='';
			document.getElementById("allocateJob_error").innerHTML='<s:text name="jobNumber.table.validation.message"/>';
			return false;
   	   	}
   		return true; 
    }

    function setCurrentdate(){
    	var jDate=dom.get("jobDate").value;
    	if(jDate=='') {
    		dom.get("jobDate").value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
    	}else{	
    		dom.get('jobDate').value=jDate;
    	}
    }


    function enableOrdisableElements()
    {
        <s:if test="%{sourcePage=='search'}">
        var Records=allocateJobDataTable.getRecordSet();
		for(var i=0;i<allocateJobDataTable.getRecordSet().getLength();i++)
   		{
        	dom.get('receivedQuantity'+ Records.getRecord(i).getId()).readonly=true;
        	dom.get('receivedQuantity'+ Records.getRecord(i).getId()).disabled=true;
   		}
        </s:if>
    }

    function copySampleQtytoReceivedQty(){
	    if(document.allocateJobForm.copySampleQty.checked==true){
		    copyQuantity("true");
	  		document.allocateJobForm.copySampleQty.checked=true;
	 		document.allocateJobForm.copySampleQty.value=true;
	    }else{  
	    	copyQuantity("false");
	    	document.allocateJobForm.copySampleQty.checked=false;
	 		document.allocateJobForm.copySampleQty.value=false;
	    }
	}

	function copyQuantity(flag){
		if(flag=='true'){
			var Records=allocateJobDataTable.getRecordSet();
			var record;
			for(var i=0;i<allocateJobDataTable.getRecordSet().getLength();i++) 
	   		{
		   		record=Records.getRecord(i);
				allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('receivedQuantity'),record.getData("sampleQuantity"));
				calculateTotalAmount(dom.get("receivedQuantity" + record.getId()),record.getId());
	   		}
			calculateFinalTotal();
		}
		else if(flag=='false'){
			var Records=allocateJobDataTable.getRecordSet();
			var record;
			for(var i=0;i<allocateJobDataTable.getRecordSet().getLength();i++) 
	   		{
		   		record=Records.getRecord(i);
				allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('receivedQuantity'),'');
				allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('total'),'');
	   		}
			dom.get("allocateJobTotal").innerHTML='0.00';
		}	
	}

	function gotoWorkOrderView(){
		var woId = dom.get("workOrderId").value;
		window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+woId+
				"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function gotoTestSheetView(){
		var tsHeaderId = dom.get("testSheetId").value;
		window.open("${pageContext.request.contextPath}/qualityControl/testSheet!edit.action?Id="+tsHeaderId+
				"&sourcePage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function gotoSampleLetterView(){
		var slId = dom.get("sampleLetterId").value;
		window.open("${pageContext.request.contextPath}/qualityControl/sampleLetter!edit.action?id="+slId+
				"&sourcePage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes'); 
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
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
   	<div class="errorstyle" id="allocateJob_error" style="display: none;"></div>
	<s:form action="allocateJob" name="allocateJobForm"  theme="simple">
	<s:if test="%{sourcePage != 'search'}"> 
	<s:token />
	</s:if>
	<s:push value="model">
	<s:hidden name="id" id="id" />
	<s:hidden name="sampleLetterId" id="sampleLetterId" />
	<s:hidden name="sourcePage" id="sourcePage" />  
	<s:hidden name="actionName" id="actionName"/>
	<s:hidden name="jobDate" id="jobDate" />
	<s:hidden name="workOrderNumber" id="workOrderNumber"/>
	<s:hidden name="testSheetNumber" id="testSheetNumber"/>
	<s:hidden name="sampleLetterNumber" id="sampleLetterNumber"/>
	<s:hidden name="coveringLetterNumber" id="coveringLetterNumber"/>
	<s:hidden name="workOrderId" id="workOrderId"/>
	<s:hidden name="testSheetId" id="testSheetId"/>
	
	<div class="navibarshadowwk"></div>
	<div class="formmainbox"> 
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div> 
	<div class="rbcontent2">
	
	<div class="datewk">
	       <div class="estimateno"><s:text name="qualityControl.allocateJobNo.jobNumber" />: <s:if test="%{not model.JobNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.jobNumber" /></div>
	</div>	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">         
     <tr>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
              <tr>
                   <td colspan="4" class="headingwk">
                       <div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                     <div class="headplacer"><s:text name="jobNumber.header" /></div>
                  </td>
              </tr>
              
              <tr>
              	<td class="greyboxwk"><s:text name="qualityControl.allocateJobNo.woNumber" />:</td>
                <td class="greybox2wk"><a onclick="gotoWorkOrderView()" href="#"><s:property value='%{workOrderNumber}'/></a></td>
                
                <td class="greyboxwk"><s:text name="qualityControl.allocateJobNo.tsNumber" />:</td>
                <td class="greybox2wk"><a onclick="gotoTestSheetView()" href="#"><s:property value="testSheetNumber" /></a></td>
              </tr>
              
              <tr>
              	<td class="whiteboxwk"><s:text name="qualityControl.allocateJobNo.slNumber" />:</td>
                <td class="whitebox2wk"><a onclick="gotoSampleLetterView()" href="#"><s:property value="sampleLetterNumber" /></a></td>
                
                <td class="whiteboxwk"><s:text name="qualityControl.allocateJobNo.clNumber" />:</td>
                <td class="whitebox2wk"><s:property value="coveringLetterNumber" /></td>
              </tr>
              
              <tr>
              	<td class="greyboxwk"><s:text name="qualityControl.allocateJobNo.jobDate" />:</td>
                <td class="greybox2wk"><s:if test="%{sourcePage=='allocateJob'}"><s:property value="new java.text.SimpleDateFormat('dd/MM/yyyy').format(new java.util.Date())" /></s:if><s:else><s:property value="new java.text.SimpleDateFormat('dd/MM/yyyy').format(jobDate)" /></s:else></td>
                 <td class="greyboxwk"></td>
	             <td class="greybox2wk"></td>
              </tr>
         </table>
       </td>
     </tr>
    
     <s:if test="%{sourcePage != 'search'}"> 
     <tr>
	    <td><input name="copySampleQty" type="checkbox" id="copySampleQty" onclick="copySampleQtytoReceivedQty();" /> 
	    	<s:text name="createJob.checkBox.copySampleQty" />
	    </td>
	 </tr>
     </s:if>
     <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
        <table id="slTable" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
            <div class="headplacer"><s:text name="allocateJob.Details" /></div>
            </td>
                <td align="right" class="headingwk" style="border-left-width: 0px"></td>
        </tr>
        <tr>
            <td colspan="4">
            <div class="yui-skin-sam">
            <div id="allocateJobTable"></div>
            <script> 
            makeAllocateJobDataTable();
           
            <s:if test="%{sourcePage=='allocateJob' && id==null}">
            var rowCount = 0;
            <s:iterator var="slDetailsList" value="slHeader.sampleLetterDetails" status="row_status">
            allocateJobDataTable.addRow({sampleLetterDetails:'<s:property value="id"/>',
                                        SlNo:'<s:property value="#row_status.count"/>',
                                        materialType:'<s:property value="testSheetDetails.testMaster.materialType.name"/>', 
                                        testName:'<s:property value="testSheetDetails.testMaster.testName"/>',
                                        uom:'<s:property value="testSheetDetails.uom"/>',
                                        description:'<s:property value="testSheetDetails.descriptionJS" escape="false"/>',  
                                        testCharges:roundTo('<s:property value="testSheetDetails.testCharges"/>'),
                                        sampleQuantity:'<s:property value="sampleQuantity"/>',
                                        testSheetDocId:'<s:property value="testSheetDetails.documentNumber"/>'
                                       });
            var record = allocateJobDataTable.getRecord(parseInt(rowCount));
         // Important to use escape=false. Otherwise struts will replace double quotes with &quote; 
            allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('description'),'<s:property value="testSheetDetails.descriptionJS" escape="false"/>');
            rowCount++;
	        </s:iterator>
	        </s:if>
	        <s:else>
	        var rowCount = 0;
            <s:iterator var="jobDetailsList" value="jobDetails" status="row_status">
            allocateJobDataTable.addRow({
            							sampleLetterDetails:'<s:property value="sampleLetterDetails.id"/>',
                                        SlNo:'<s:property value="#row_status.count"/>',
                                        materialType:'<s:property value="sampleLetterDetails.testSheetDetails.testMaster.materialType.name"/>', 
                                        testName:'<s:property value="sampleLetterDetails.testSheetDetails.testMaster.testName"/>',
                                        uom:'<s:property value="sampleLetterDetails.testSheetDetails.uom"/>',
                                        description:'<s:property value="sampleLetterDetails.testSheetDetails.descriptionJS" escape="false"/>',  
                                        testCharges:roundTo('<s:property value="sampleLetterDetails.testSheetDetails.testCharges"/>'),
                                        sampleQuantity:'<s:property value="sampleLetterDetails.sampleQuantity"/>',
                                        receivedQuantity:'<s:property value="receivedQuantity"/>',
                                        testSheetDocId:'<s:property value="sampleLetterDetails.testSheetDetails.documentNumber"/>'
                                       });
            var record = allocateJobDataTable.getRecord(parseInt(rowCount));
         // Important to use escape=false. Otherwise struts will replace double quotes with &quote; 
            allocateJobDataTable.updateCell(record,allocateJobDataTable.getColumn('description'),'<s:property value="sampleLetterDetails.testSheetDetails.descriptionJS" escape="false"/>');
            calculateTotalAmount(dom.get("receivedQuantity"+record.getId()),record.getId()); 
            rowCount++;
	        </s:iterator>
	        </s:else>
               </script> 
              </div>
            </td>
        </tr>
        </table>
        </td>
    </tr>
    <tr> 
          <td colspan="4" class="shadowwk"></td>
      </tr>
    </table>
		    
	</div>
	<div class="buttonholderwk">
         <s:if test="%{sourcePage != 'search'}"> 
	 		<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.allocateJobForm.actionName.value='Save';return validateInput();" />
	 	
	 		 <s:submit type="submit" cssClass="buttonfinal"
					value="Approve" id="Approve" name="Approve"
					method="save"
					onclick="document.allocateJobForm.actionName.value='Approve';return validateInput();" />
			 <s:if test="%{model.egwStatus!=null && model.egwStatus.code=='NEW'}">
			  		<s:submit type="submit" cssClass="buttonfinal"
					value="Cancel" id="Cancel" name="Cancel"
					method="save"
					onclick="document.allocateJobForm.actionName.value='Cancel';return validateInput();" />
			 </s:if> 
			 <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='allocateJob.close.confirm'/>');"/>
         </s:if>
         <s:elseif test="%{sourcePage == 'search'}">
         	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
     	 </s:elseif>
         
         
    </div>
	<div class="rbbot2"><div></div></div>
	</div>
	</div>
	</div>
	</s:push>
	</s:form>
	</body>
</html>