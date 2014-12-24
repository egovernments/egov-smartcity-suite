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
			<s:text name="qualityControl.sampleLetter.title" />
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
            if(oColumn.getKey()=="sampleLetterHeader")
            	fieldName = "actionSampleLetterDetails[" + oRecord.getCount() + "]." + oColumn.getKey()+ ".id";
            if(oColumn.getKey()=="testSheetDetails")
           	 	fieldName = "actionSampleLetterDetails[" + oRecord.getCount() + "]." + oColumn.getKey()+ ".id";
            var fieldValue=value;
            markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' />";
            el.innerHTML = markup;
        }
        return hiddenFormatter;
        }
        var sampleLetterHiddenFormatter = createHiddenFormatter(10,10);
        var testSheetDetailsHiddenFormatter = createHiddenFormatter(10,10);

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
        	    var fieldName = "actionSampleLetterDetails[" + oRecord.getCount() + "]." + oColumn.getKey();
        	    markup="<input type='text' id='"+id+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' value='"+value+"' class='selectamountwk' onblur='calculateTotalAmount(this,\""+oRecord.getId()+"\");'/>";
        	    el.innerHTML = markup;
        	}
        	return textboxFormatter;
        	}
        	var textboxQtyFormatter = createTextBoxQtyFormatter(10,13);

        	function materialTypeDocFormatter(){
        		return function(el, oRecord, oColumn, oData) {
        			var id = oColumn.getKey()+oRecord.getId();
            	    var fieldName = oColumn.getKey();
            	    if(fieldName=="freqChart")
        				markup="<input type='submit' class='buttonadd' id='"+id+"' name='"+fieldName+"' value='Frequency Chart'  onclick='showDocumentAttached(\""+oRecord.getId()+"\",\""+fieldName+"\");return false;'/>";
        			else if(fieldName=="testSheetDoc")	
        				markup="<input type='submit' class='buttonadd' id='"+id+"' name='"+fieldName+"' value='View Doc'  onclick='showDocumentAttached(\""+oRecord.getId()+"\",\""+fieldName+"\");return false;'/>";
        			el.innerHTML = markup;
        		}
            }

	var sampleLetterDataTable;
    var makeSampleLetterDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var sampleLetterColumnDefs = [
            {key:"sampleLetterHeader", hidden:true,formatter:sampleLetterHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"testSheetDetails", hidden:true,formatter:testSheetDetailsHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:20},
            {key:"materialType", label:'Material Type', disabled:true,resizeable:true},
            {key:"testName", label:'Name of Test',disabled:true,resizeable:true},
            {key:"uom", label:'UOM',  sortable:false,disabled:true,resizeable:false},	
            {key:"description", label:'Description', sortable:false, disabled:true, resizeable:false},	
            {key:"testCharges",label:'Testing Charges', formatter:textboxFormatter,sortable:false, disabled:true,resizeable:false},
            {key:"sampleQuantity",label:'Sample Quantity', formatter:textboxQtyFormatter,sortable:false, resizeable:false},
            {key:"total",label:'Total Amount', sortable:false, formatter:textboxFormatter, disabled:true, resizeable:false},
            {key:"testSheetDoc",label:'View Doc',formatter:materialTypeDocFormatter(),width:60,resizeable:false},
            {key:"freqChart",label:'View Frequency Chart',formatter:materialTypeDocFormatter(),width:100,resizeable:false},
            {key:"testSheetDocId", hidden:true, sortable:false, resizeable:false},
            {key:"materialTypeDocId", hidden:true, sortable:false, resizeable:false}
        ]; 
       
        var sampleLetterDataSource = new YAHOO.util.DataSource();
        sampleLetterDataTable = new YAHOO.widget.DataTable("sampleLetterTable",sampleLetterColumnDefs, sampleLetterDataSource, {MSG_EMPTY:""});
        sampleLetterDataTable.subscribe("cellClickEvent", sampleLetterDataTable.onEventShowCellEditor);
        sampleLetterDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
        });

        var tfoot = sampleLetterDataTable.getTbodyEl().parentNode.createTFoot();
    	var tr = tfoot.insertRow(-1);
    	var th = tr.appendChild(document.createElement('td'));
    	th.colSpan =8;
    	th.className= 'whitebox4wk';
    	th.innerHTML = '&nbsp;';

    	var td = tr.insertCell(1);
    	td.className= 'whitebox4wk';
    	td.id = 'sLetterTotal';
    	td.innerHTML = '<span class="bold">Total:</span>';

    	addCellRightAligned(tr,2,'sampleLetterTotal','0.00');
    	addCell(tr,3,'filler','');
    	addCell(tr,4,'filler','');
        return {
            oDS: sampleLetterDataSource,
            oDT: sampleLetterDataTable
        }; 
    }

    function showDocumentAttached(recordId,type){
		record=sampleLetterDataTable.getRecord(recordId);
		if(type=="freqChart")
			viewDocumentManager(record.getData('materialTypeDocId'));
		else if(type=="testSheetDoc") 
			viewDocumentManager(record.getData('testSheetDocId'));
		return true;
     }

    function calculateTotalAmount(elem,recordId){
    	clearErrorMessage();
     	record=sampleLetterDataTable.getRecord(recordId);
     	var charges=getNumber(record.getData("testCharges"));
     	
     	if(!checkUptoFourDecimalPlace(elem,dom.get('sampleLetter_error'),"Sample Quantity"))
 			return false;  

     	if(isNaN(elem.value))
		{
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='Sample Quantity for Sl No '+record.getData("SlNo")+' should be Numeric';
			return false;
		}

     	if(elem.value!='' && elem.value<0){
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='Negative Quantity!! Sample Quantity for Sl No '+record.getData("SlNo")+' is Negative';
			return false;
		}

     	sampleLetterDataTable.updateCell(record,sampleLetterDataTable.getColumn('total'),roundTo(charges*elem.value));
     	calculateFinalTotal();
    }


    function calculateFinalTotal(){ 
    	var finTotal=0;
    	var Records= sampleLetterDataTable.getRecordSet();
       	for(var i=0;i<sampleLetterDataTable.getRecordSet().getLength();i++)
       	{
       		if(!isNaN(getNumber(dom.get("total" + Records.getRecord(i).getId()).value)))
    	    		  finTotal=finTotal+getNumber(dom.get("total" + Records.getRecord(i).getId()).value);
       	}
       	var val=roundTo((finTotal),2,'0');
    	dom.get("sampleLetterTotal").innerHTML=val;
    } 

    function clearErrorMessage(){
    	dom.get("sampleLetter_error").style.display='none';
    	document.getElementById("sampleLetter_error").innerHTML=''; 
    }

	function validateInput(){
		clearErrorMessage();
		if(document.getElementById("actionName").value=='Cancel'){
    		if(!validateCancel()){
    			return false;
    		}
    	}
		if(dom.get("designation1").value== -1){
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.designation1.null"/>';
			dom.get("designation1").focus();
			return false;
		}

		if(dom.get("designation2").value== -1){
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.designation2.null"/>';
			dom.get("designation2").focus();
			return false;
		}

		if(dom.get("designation1").value != -1 && dom.get("sampleCollectedBy1").value== -1){
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.sampleCollectedBy1.null"/>';
			dom.get("sampleCollectedBy1").focus();
			return false;
		}

		if(dom.get("designation2").value != -1 && dom.get("sampleCollectedBy2").value== -1){
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.sampleCollectedBy2.null"/>';
			dom.get("sampleCollectedBy2").focus();
			return false;
		}
		
		var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
		if(dom.get('dispatchDate').value!='')
		{
			if(compareDate(dom.get('dispatchDate').value,currentDate) == 1 ){
				dom.get("sampleLetter_error").style.display='';
				document.getElementById("sampleLetter_error").innerHTML='<s:text name="invalid.dispatchDate" />';
				dom.get("dispatchDate").value='';
				dom.get("dispatchDate").focus(); 
				return false;
			} 
		}

		if(dom.get('castingDate').value!='')
		{
			if(compareDate(dom.get('castingDate').value,currentDate) == -1 ){
				dom.get("sampleLetter_error").style.display='';
				document.getElementById("sampleLetter_error").innerHTML='<s:text name="invalid.castingDate" />';
				dom.get("castingDate").value='';
				dom.get("castingDate").focus(); 
				return false;
			} 
		}

		if(dom.get('samplingDate').value=='')
		{
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.samplingDate.null" />';
			dom.get("samplingDate").focus(); 
			return false;
		}

		if(dom.get('samplingDate').value!='')
		{
			if(compareDate(dom.get('samplingDate').value,currentDate) == -1 ){
				dom.get("sampleLetter_error").style.display='';
				document.getElementById("sampleLetter_error").innerHTML='<s:text name="invalid.samplingDate" />';
				dom.get("samplingDate").value='';
				dom.get("samplingDate").focus(); 
				return false;
			} 
		}

		if(dom.get("location").value=='' || dom.get("location").value==null){
			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.location.null"/>';
			dom.get("location").focus();
			return false;
		}
		var desgn1=dom.get("designation1").value;
		var desgn2=dom.get("designation2").value;
		if(desgn1 != -1  && desgn2!= -1 ){
			if(desgn1==desgn2){
				dom.get("sampleLetter_error").style.display='';
				document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.designations.notUnique"/>';
				dom.get("designation1").focus();
				return false;
			}
		} 
		if(!validateTableData())
       		return false;
   		
		if(document.getElementById("actionName").value=='Approve'){
			if(dom.get("notifyUserId").value== -1){
				dom.get("sampleLetter_error").style.display='';
				document.getElementById("sampleLetter_error").innerHTML='<s:text name="sampleLetter.notifyUser.null"/>';
				dom.get("notifyUserId").focus();
				return false;
			}
		}
		return true;
	}

	function validateCancel() {
  		var msg='<s:text name="sampleLetter.cancel.confirm"/>';
  	 
	  	if(!confirmCancel(msg)) {
	  		return false;
	  	}
	  	else {
	  		return true;
	  	}
  	}

	function validateTableData(){
    	var sample_Qty;
    	var flag=0;
    	var Records=sampleLetterDataTable.getRecordSet();
		for(var i=0;i<sampleLetterDataTable.getRecordSet().getLength();i++)
   		{
			sample_Qty=dom.get("sampleQuantity" +  Records.getRecord(i).getId()).value;
			if(isNaN(sample_Qty))
			{
				dom.get("sampleLetter_error").style.display='';
				document.getElementById("sampleLetter_error").innerHTML='Sample Quantity for Sl No '+Records.getRecord(i).getData("SlNo")+' should be Numeric';
				return false;
			}

			if(!checkUptoFourDecimalPlace(dom.get("sampleQuantity" +  Records.getRecord(i).getId()),dom.get('sampleLetter_error'),"Sample Quantity"))
	 			return false;  
			
			if(sample_Qty!='' && sample_Qty<0){
				dom.get("sampleLetter_error").style.display='';
				document.getElementById("sampleLetter_error").innerHTML='Negative Quantity!! Sample Quantity for Sl No '+Records.getRecord(i).getData("SlNo")+' is Negative';
				return false;
			}
			if((sample_Qty=='' || sample_Qty==0) && flag==0){
				flag=0;
			}
			else
				flag=1;
   		}
   		if(flag==0){
   			dom.get("sampleLetter_error").style.display='';
			document.getElementById("sampleLetter_error").innerHTML='Sample Quantity Cannot be 0 / Empty for all the Details. Please Enter Sample Quantity.';
			return false;
   	   	}
   		return true; 
    }

	function populateUser1(obj){
        var deptId=document.getElementById('departmentId').value;
        populatesampleCollectedBy1({desgId:obj.value,executingDepartment:deptId})
    }
    function populateUser2(obj){
        var deptId=document.getElementById('departmentId').value;
        populatesampleCollectedBy2({desgId:obj.value,executingDepartment:deptId})
    }


    function setCurrentdate(){
    	var slDate=dom.get("sampleLetterDate").value;
    	if(slDate=='') {
    		dom.get("sampleLetterDate").value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
    	}else{	
    		dom.get('sampleLetterDate').value=slDate;
    	}

    	<s:if test="%{sourcePage=='createSampleLetter' && id==null}"> 
	   		var dispatch_date=dom.get("dispatchDate").value;
	   		if(dispatch_date==''){
	   			dom.get("dispatchDate").value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
			} 
			var sampling_date=dom.get("samplingDate").value;
	   		if(sampling_date==''){
	   			dom.get("samplingDate").value='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
			} 
		</s:if>	
    }

    function loadDesignations(){
    	<s:if test="%{sourcePage=='createSampleLetter' && id==null}"> 
    	   	<s:if test="%{defaultDesgnId1!=null && defaultDesgnId1!=''}">
	    		dom.get("designation1").value= '<s:property value="defaultDesgnId1" />';
	    		populateUser1(dom.get("designation1")); 
	    	</s:if>
	    	<s:if test="%{defaultDesgnId2!=null && defaultDesgnId2!=''}">
				dom.get("designation2").value= '<s:property value="defaultDesgnId2" />';
				populateUser2(dom.get("designation2"));
			</s:if>
		</s:if>
    }

    function enableOrdisableElements()
    {
        <s:if test="%{sourcePage=='search'}">
	        links=document.sampleLetterForm.getElementsByTagName("a");
            for(i=0;i<links.length;i++){
                     links[i].onclick=function(){return false;};  
            }

            for(var i=0;i<document.forms[0].length;i++) {
                document.forms[0].elements[i].disabled =true;
            }

            var Records=sampleLetterDataTable.getRecordSet();
    		for(var i=0;i<sampleLetterDataTable.getRecordSet().getLength();i++)
       		{
    			dom.get('freqChart'+ Records.getRecord(i).getId()).readonly=false;
            	dom.get('freqChart'+ Records.getRecord(i).getId()).disabled=false; 

            	dom.get('testSheetDoc'+ Records.getRecord(i).getId()).readonly=false;
            	dom.get('testSheetDoc'+ Records.getRecord(i).getId()).disabled=false;
       		}
	        document.sampleLetterForm.closeButton.readonly=false;
            document.sampleLetterForm.closeButton.disabled=false; 
			
			if(document.sampleLetterForm.slPdfButton!=null){
            	document.sampleLetterForm.slPdfButton.readonly=false;
            	document.sampleLetterForm.slPdfButton.disabled=false;
			}
			if(document.sampleLetterForm.clPdfButton!=null){
           	 	document.sampleLetterForm.clPdfButton.readonly=false;
            	document.sampleLetterForm.clPdfButton.disabled=false; 
			}
        </s:if>
    }
    
	</script>
	
	<body onload="setCurrentdate();loadDesignations();enableOrdisableElements();">	
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
   	<div class="errorstyle" id="sampleLetter_error" style="display: none;"></div>
	<s:form action="sampleLetter" name="sampleLetterForm"  theme="simple">
	<s:if test="%{sourcePage != 'search'}"> 
	<s:token />
	</s:if>
	<s:push value="model">
	<s:hidden name="id" id="id" />
	<s:hidden name="testSheetId" id="testSheetId" />
	<s:hidden name="sourcePage" id="sourcePage" />  
	<s:hidden name="departmentId" id="departmentId" />
	<s:hidden name="actionName" id="actionName"/>
	<s:hidden name="sampleLetterDate" id="sampleLetterDate" />
	<s:hidden name="defaultDesgnId1" id="defaultDesgnId1" />
	<s:hidden name="defaultDesgnId2" id="defaultDesgnId2" />
	
	<div class="navibarshadowwk"></div>
	<div class="formmainbox"> 
	<div class="insidecontent">
	<div class="rbroundbox2">
	<div class="rbtop2"><div></div></div> 
	<div class="rbcontent2">
	<div class="datewk">
	       <div class="estimateno"><s:text name="qualityControl.sampleLetter.sampleLetterNumber" />: <s:if test="%{not model.sampleLetterNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.sampleLetterNumber" /></div>
			<div class="estimateno" style="text-align: right"><s:text name="qualityControl.sampleLetter.coveringLetterNumber" />: <s:if test="%{not model.coveringLetterNumber}">&lt; Not Assigned &gt;</s:if><s:property value="model.coveringLetterNumber" /></div>
	</div>
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">         
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
              <tr>
                   <td colspan="4" class="headingwk">
                       <div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                     <div class="headplacer"><s:text name="sampleLetter.header" /></div>
                  </td>
              </tr>
               
               <tr>
              	<td class="greyboxwk"><s:text name="qualityControl.sampleLetter.woNumber" />:</td>
                <td class="greybox2wk"><s:property value='%{workOrderNumber}'/></td>
                
                <td class="greyboxwk"><s:text name="qualityControl.sampleLetter.date" />:</td>
                <td class="greybox2wk"><s:if test="%{sourcePage=='createSampleLetter'}"><s:property value="new java.text.SimpleDateFormat('dd/MM/yyyy').format(new java.util.Date())" /></s:if><s:else><s:property value="new java.text.SimpleDateFormat('dd/MM/yyyy').format(sampleLetterDate)" /></s:else></td>
              </tr>
               
              <tr>
              	<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="qualityControl.sampleLetter.designation1" />:</td>
                <td class="whitebox2wk">
                <s:select headerKey="-1" headerValue="%{getText('testSheet.default.select')}" name="designation1" 
		         id="designation1" value="%{designation1.designationId}" cssClass="selectwk" onchange="populateUser1(this)"
		         list="dropdownData.designationMasterList" listKey="designationId" listValue="designationName" />
		         <egov:ajaxdropdown id="sampleCollectedBy1" fields="['Text','Value']" dropdownId='sampleCollectedBy1'
                              url='qualityControl/ajaxSampleLetter!getUsersForDesg.action'/>
		         </td>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="qualityControl.sampleLetter.sampleCollectedBy1" />:</td>
                <td class="whitebox2wk">
                 <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="sampleCollectedBy1"
                     id="sampleCollectedBy1"  cssClass="selectwk"
                     list="dropdownData.sampleCollectedBy1" listKey="id" listValue="employeeName" value="%{sampleCollectedBy1.id}"/>
                </td>
              </tr>
              
              
              <tr>
              	<td class="greyboxwk"><span class="mandatory">*</span><s:text name="qualityControl.sampleLetter.designation2" />:</td>
                <td class="greybox2wk">
                <s:select headerKey="-1" headerValue="%{getText('testSheet.default.select')}" name="designation2" 
		         id="designation2" value="%{designation2.designationId}" cssClass="selectwk" 
		         list="dropdownData.designationMasterList1" listKey="designationId" listValue="designationName" onchange="populateUser2(this)"/>
                 <egov:ajaxdropdown id="sampleCollectedBy2" fields="['Text','Value']" dropdownId='sampleCollectedBy2'
                              url='qualityControl/ajaxSampleLetter!getUsersForDesg.action'/>
                </td>
                
                <td class="greyboxwk"><span class="mandatory">*</span><s:text name="qualityControl.sampleLetter.sampleCollectedBy2" />:</td>
                <td class="greybox2wk">
                 <s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="sampleCollectedBy2"
                     id="sampleCollectedBy2"  cssClass="selectwk"
                     list="dropdownData.sampleCollectedBy2" listKey="id" listValue="employeeName" value="%{sampleCollectedBy2.id}"/>
                </td>
              </tr>  
              
             <tr>
				<td class="whiteboxwk">
					<s:text name="qualityControl.sampleLetter.dispatchDate" />:
				</td>
				<td class="whitebox2wk">
					<s:date name="dispatchDate" var="dispatchDateFormat"
						format="dd/MM/yyyy" />
					<s:textfield name="dispatchDate" id="dispatchDate"
						cssClass="selectwk" value="%{dispatchDateFormat}"
						onfocus="javascript:vDateType='3';"
						onkeyup="DateFormat(this,this.value,event,false,'3')" />
					<a
						href="javascript:show_calendar('forms[0].dispatchDate',null,null,'DD/MM/YYYY');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;"> <img
							src="${pageContext.request.contextPath}/image/calendar.png"
							alt="Calendar" width="16" height="16" border="0"
							align="absmiddle" />
					</a>

				</td>
				<td class="whiteboxwk">
					<s:text name="qualityControl.sampleLetter.castingDate" />:
				</td>
				<td class="whitebox2wk">
					<s:date name="castingDate" var="castingDateFormat"
						format="dd/MM/yyyy" />
					<s:textfield name="castingDate" id="castingDate"
						value="%{castingDateFormat}" cssClass="selectwk"
						onfocus="javascript:vDateType='3';"
						onkeyup="DateFormat(this,this.value,event,false,'3')" />
					<a
						href="javascript:show_calendar('forms[0].castingDate',null,null,'DD/MM/YYYY');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;"> <img
							src="${pageContext.request.contextPath}/image/calendar.png"
							alt="Calendar" width="16" height="16" border="0"
							align="absmiddle" />
					</a>
				</td>
			</tr>
                
            <tr>
				<td class="greyboxwk">
					<span class="mandatory">*</span><s:text name="qualityControl.sampleLetter.samplingDate" />:
				</td>
				<td class="greybox2wk">
					<s:date name="samplingDate" var="samplingDateFormat"
						format="dd/MM/yyyy" />
					<s:textfield name="samplingDate" id="samplingDate"
						cssClass="selectwk" value="%{samplingDateFormat}"
						onfocus="javascript:vDateType='3';"
						onkeyup="DateFormat(this,this.value,event,false,'3')" />
					<a
						href="javascript:show_calendar('forms[0].samplingDate',null,null,'DD/MM/YYYY');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;"> <img
							src="${pageContext.request.contextPath}/image/calendar.png"
							alt="Calendar" width="16" height="16" border="0"
							align="absmiddle" />
					</a>
				</td>
				
				<td class="greyboxwk"><span class="mandatory">*</span><s:text name="qualityControl.sampleLetter.location" />:</td>
                <td class="greybox2wk"><s:textfield name="location" type="text" cssClass="selectwk" id="location" value="%{location}"/></td>
				
             <tr>
                <td class="whiteboxwk"><s:text name="qualityControl.sampleLetter.remarks" />:</td>
                <td class="whitebox2wk"><s:textarea name="remarks" cols="35" cssClass="selectwk" id="remarks" value="%{remarks}"/></td>
                             
               <td class="whiteboxwk"></td>
			   <td class="whitebox2wk"></td> 
            
            </tr>
         </table>
        </td>
     </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
        <table id="slTable" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
            <div class="headplacer"><s:text name="sampleLetter.Details" /></div>
            </td>
                <td align="right" class="headingwk" style="border-left-width: 0px"></td>
        </tr>
        <tr>
            <td colspan="4">
            <div class="yui-skin-sam">
            <div id="sampleLetterTable"></div>
            <script> 
            makeSampleLetterDataTable();
            <s:if test="%{sourcePage=='createSampleLetter' && id==null}">
            var rowCount = 0;
            <s:iterator var="tsDetailsList" value="tSheetHeader.testSheetDetails" status="row_status">
            sampleLetterDataTable.addRow({testSheetHeader:'<s:property value="testSheetHeader.id"/>',
            							testSheetDetails:'<s:property value="id"/>',
                                        SlNo:'<s:property value="#row_status.count"/>',
                                        materialType:'<s:property value="testMaster.materialType.name"/>', 
                                        testName:'<s:property value="testMaster.testName"/>',
                                        uom:'<s:property value="uom"/>',
                                        description:'<s:property value="descriptionJS" escape="false"/>',  
                                        testCharges:roundTo('<s:property value="testCharges"/>'),
                                        testSheetDocId:'<s:property value="documentNumber"/>',
                                        materialTypeDocId:'<s:property value="testMaster.materialType.documentNumber"/>'                                     
                                       });
            var record = sampleLetterDataTable.getRecord(parseInt(rowCount));
         // Important to use escape=false. Otherwise struts will replace double quotes with &quote; 
            sampleLetterDataTable.updateCell(record,sampleLetterDataTable.getColumn('description'),'<s:property value="descriptionJS" escape="false"/>');
            rowCount++;
	        </s:iterator>
	        </s:if>
	        <s:else>
	        var rowCount = 0;
            <s:iterator var="slDetailsList" value="sampleLetterDetails" status="row_status">
            sampleLetterDataTable.addRow({testSheetHeader:'<s:property value="testSheetDetails.testSheetHeader.id"/>',
            							testSheetDetails:'<s:property value="testSheetDetails.id"/>',
                                        SlNo:'<s:property value="#row_status.count"/>',
                                        materialType:'<s:property value="testSheetDetails.testMaster.materialType.name"/>', 
                                        testName:'<s:property value="testSheetDetails.testMaster.testName"/>',
                                        uom:'<s:property value="testSheetDetails.uom"/>',
                                        description:'<s:property value="testSheetDetails.descriptionJS" escape="false"/>',  
                                        testCharges:roundTo('<s:property value="testSheetDetails.testCharges"/>'),
                                        sampleQuantity:'<s:property value="sampleQuantity"/>',
                                        testSheetDocId:'<s:property value="testSheetDetails.documentNumber"/>',
                                        materialTypeDocId:'<s:property value="testSheetDetails.testMaster.materialType.documentNumber"/>'                                     
                                       });
            var record = sampleLetterDataTable.getRecord(parseInt(rowCount));
         // Important to use escape=false. Otherwise struts will replace double quotes with &quote; 
            sampleLetterDataTable.updateCell(record,sampleLetterDataTable.getColumn('description'),'<s:property value="testSheetDetails.descriptionJS" escape="false"/>');
            calculateTotalAmount(dom.get("sampleQuantity"+record.getId()),record.getId()); 
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
     <s:if test="%{sourcePage != 'search'}"> 
     <tr>
     	<td colspan="4">
     	<table>
		      <tr>
		        <td class="whiteboxwk"><s:text name="sampleLetter.label.notify.user" />:</td>
		        <td class="whitebox2wk" colspan="3">
		         <s:select headerKey="-1" headerValue="%{getText('sampleLetter.default.select')}" name="notifyUserId"
		             id="notifyUserId"  cssClass="selectwk"
		             list="dropdownData.fileNotifyUsersList" listKey="id" listValue="employeeName" value="%{notifyUserId}"/>
		        </td>
		      </tr>
		 </table>
		 </td>
	  </tr>
	 </s:if>
    </table>
	
	 <div id="mandatary" align="right" class="mandatory" style="font-size: 11px; padding-right: 20px;">* 
		<s:text name="message.mandatory" />
	</div>    
	</div>
	
	<div class="rbbot2"><div></div></div>
	</div>
	</div>
	</div>
	<div class="buttonholderwk">
         <s:if test="%{sourcePage != 'search'}"> 
	 		<s:submit type="submit" cssClass="buttonfinal"
					value="Save" id="Save" name="Save"
					method="save"
					onclick="document.sampleLetterForm.actionName.value='Save';return validateInput();" />
	 	
	 		 <s:submit type="submit" cssClass="buttonfinal"
					value="Notify & Approve" id="Approve" name="Approve"
					method="save"
					onclick="document.sampleLetterForm.actionName.value='Approve';return validateInput();" />
			 <s:if test="%{model.egwStatus!=null && model.egwStatus.code=='NEW'}">
			  		<s:submit type="submit" cssClass="buttonfinal"
					value="Cancel" id="Cancel" name="Cancel"
					method="save"
					onclick="document.sampleLetterForm.actionName.value='Cancel';return validateInput();" />
			 </s:if> 
			 <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='sampleLetter.close.confirm'/>');"/>
         </s:if>
         <s:elseif test="%{sourcePage == 'search'}">
          	<s:if test="%{model.egwStatus!=null && model.egwStatus.code!='CANCELLED'}">
        	 	<input type="button" onclick="window.open('${pageContext.request.contextPath}/qualityControl/sampleLetter!generateSampleLetterPdf.action?slId=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW SAMPLE LETTER PDF" id="slPdfButton" name="slPdfButton"/>
        	 	<input type="button" onclick="window.open('${pageContext.request.contextPath}/qualityControl/sampleLetter!generateCoveringLetterPdf.action?slId=<s:property value='%{model.id}'/>');" class="buttonpdf" value="VIEW COVERING LETTER PDF" id="clPdfButton" name="clPdfButton"/>
         	</s:if>
         	 <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
     	 </s:elseif>     
         
    </div>
	</s:push>
	</s:form>
	</body>
</html>