<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<html>
 <head>
 	<TITLE><s:text name='estimate.measurementSheet.Title' /></TITLE>
</head>

<style type="text/css">
 .yui-dt table{
	 width:100%;
} 

td.yui-dt-hidden {
display:none;
}

th.yui-dt-hidden {
display:none;
}


</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>
<body onload="onBodyLoad();">

<script type="text/javascript">
  
function onBodyLoad(){
  	<s:if test="%{!measurementSheetList.isEmpty()}">
		goToParent();
	</s:if>
}
  
function createTextBoxFormatter(size,maxlength){
	var textboxFormatter = function(el, oRecord, oColumn, oData){
   var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
	var fieldName="measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
    if(oColumn.getKey()=="uomLength" || oColumn.getKey()=="width")
   	 	markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateQty(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
   	else
   	 	markup="<input type='text' 	class='selectamountwk' id='"+id+"' value='"+value+"' name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' onblur='calculateQty(this,\""+oRecord.getId()+"\");' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
	return textboxFormatter;
} 
var textboxFormatter = createTextBoxFormatter(11,13);
var textboxNumberFormatter = createTextBoxFormatter(9,3);

var checkboxFormatter = function(el, oRecord, oColumn, oData){
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
	//var fieldName=oColumn.getKey()+oRecord.getCount();
	 //var fieldName = "measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey(); 
	markup="<input type='checkbox' 	class='selectamountwk' id='"+id+"' onClick='calculateTotal();' />"; 
    el.innerHTML = markup; 
}
	
var textboxDescFormatter = function(el, oRecord, oColumn, oData) {
 	var value = (YAHOO.lang.isValue(oData))?oData:"";
   var fieldName = "measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
	markup="<input type='text' id='"+oColumn.getKey()+oRecord.getId()+"' class='selectmultilinewk' size='90' maxlength='1024' value='"+value+"'name='"+fieldName+"' />"
	el.innerHTML = markup;	 	
}

function createMSheetHiddenFormatter(size,maxlength){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    //var fieldName=oColumn.getKey()+oRecord.getCount();
    var fieldName = "measurementSheetList[" + oRecord.getCount() + "]." + oColumn.getKey();
    //var fieldValue=value;
    markup="<input type='text' id='"+id+"' size='"+size+"' value='"+value+"' maxlength='"+maxlength+"' name='"+fieldName+ "' readonly='readonly'/>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var mSheetHiddenFormatter = createMSheetHiddenFormatter(0,0);

var mSheetDataTable;
var mSheetColumnDefs;
var makeDetailedMSheetDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor()
    mSheetColumnDefs = [ 
   	{key:"SlNo", label:'<s:text name='estimate.measurementSheetDataTable.slno' />', sortable:false, resizeable:false},
   	{key:"remarks",label:'<s:text name='estimate.measurementSheetDataTable.description' />',formatter:textboxDescFormatter,sortable:false, resizeable:false},
   	{key:"no",label:'<s:text name='estimate.measurementSheetDataTable.number' />',formatter:textboxNumberFormatter,sortable:false, resizeable:false},
   	{key:"uomLength",label:'<s:text name='estimate.measurementSheetDataTable.length' />',formatter:textboxFormatter,sortable:false, resizeable:false},
   	{key:"width",label:'<s:text name='estimate.measurementSheetDataTable.width' />',sortable:false,formatter:textboxFormatter, resizeable:false},
   	{key:"depthOrHeight",label:'<s:text name='estimate.measurementSheetDataTable.depthorheight' />',formatter:textboxFormatter,sortable:false, resizeable:false},
   	{key:"Quantity",sortable:false, resizeable:false},
   	{key:"UOM",sortable:false, resizeable:false},
   	{key:"identifier", hidden:true,formatter:mSheetHiddenFormatter,sortable:false, resizeable:true},
   	{key:"Deduction",formatter:checkboxFormatter,sortable:false, resizeable:false},
   	{key:"recId",hidden:true,formatter:mSheetHiddenFormatter,sortable:false, resizeable:true},
   	{key:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
    ];
    
    var mSheetDataSource = new YAHOO.util.DataSource();
    mSheetDataTable = new YAHOO.widget.DataTable("mSheetTable",mSheetColumnDefs, mSheetDataSource,{MSG_EMPTY:"<s:text name='estimate.measurementSheet.initial.Detailedtable.message'/>"});
    mSheetDataTable.subscribe("cellClickEvent", mSheetDataTable.onEventShowCellEditor); 
    mSheetDataTable.on('cellClickEvent',function (oArgs) {
        var target = oArgs.target;
        var record = this.getRecord(target);
        var column = this.getColumn(target);
        if (column.key == 'Delete'){  
           	recalculateTotalOnDelete(record);
            this.deleteRow(record);
            allRecords=this.getRecordSet();
            for(i=0;i<allRecords.getLength();i++){
              this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
            }
        }
    });
    
    var tfoot = mSheetDataTable.getTbodyEl().parentNode.createTFoot();
	var tr = tfoot.insertRow(-1);
	var th = tr.appendChild(document.createElement('td'));
	th.colSpan = 1;
	th.className= 'whitebox4wk';
	th.innerHTML = '&nbsp;';

	var td = tr.insertCell(1);
	td.className= 'whitebox4wk';
	td.id = 'mSheetTotal';
	td.innerHTML = '<span class="bold">Total:</span>';
	addCell(tr,2,'filler','');
	addCell(tr,3,'filler','');
	addCell(tr,4,'filler','');
	addCell(tr,5,'filler','');
	addCell(tr,6,'qtyTotal','0.00');
	addCell(tr,7,'filler','');
	addCell(tr,8,'filler','');
	addCell(tr,9,'filler',''); 

    return {
        oDS: mSheetDataSource,
        oDT: mSheetDataTable
    };
}

function calculateQty(elem,recId){
	var name=(elem.name).split('.');
	dom.get('error'+elem.id).style.display='none';
	if(name[1]=='no'){
		if(!validateNumberForDecimalPlace(elem,dom.get('error'+elem.id))) return;
	}
	else{
		if(!validateNumberInTableCell(mSheetDataTable,elem,recId)) return;
	}
	if(elem.value==0){
		dom.get('error'+elem.id).style.display='block';
		return;
	}
	findQuantity(recId);
}

function findQuantity(recId){
	var record=mSheetDataTable.getRecord(recId);
	var msNo;
	var msLength;
	var msWidth;
	var msDepthOrHeight;
	var msQuantity=1;
	var flag=false;
	msNo=dom.get("no"+record.getId()).value;
	msLength=dom.get("uomLength"+record.getId()).value;
	msWidth=dom.get("width"+record.getId()).value;
	msDepthOrHeight=dom.get("depthOrHeight"+record.getId()).value;
	if(!isNaN(getNumber(msNo))){
		msQuantity*=msNo;
		flag=true;
	}
	if(!isNaN(getNumber(msLength))){
		msQuantity*=msLength;
		flag=true; 
	}
	if(!isNaN(getNumber(msWidth))){	
		msQuantity*=msWidth;
		flag=true;
	}
	if(!isNaN(getNumber(msDepthOrHeight))){
		msQuantity*=msDepthOrHeight;
		flag=true;	
	}
	oldQuantity=record.getData("Quantity");
	if(flag!=true)
		msQuantity=0.00; 
	mSheetDataTable.updateCell(record,mSheetDataTable.getColumn('Quantity'),roundTo(msQuantity));
	calculateTotal();
}

function calculateTotal(){
	var deductionTotal=0;
	var nonDeductionTotal=0;
	var Records= mSheetDataTable.getRecordSet();
   	for(var i=0;i<mSheetDataTable.getRecordSet().getLength();i++)
   	{
    	  if(dom.get("Deduction" + Records.getRecord(i).getId()).checked == true){
    	  		deductionTotal=deductionTotal+getNumber(Records.getRecord(i).getData("Quantity"));
    	  		dom.get("identifier" + Records.getRecord(i).getId()).value="D";
    	  }
    	  else{
    	  		nonDeductionTotal=nonDeductionTotal+getNumber(Records.getRecord(i).getData("Quantity"));
    	  		dom.get("identifier" + Records.getRecord(i).getId()).value="A";
    	  }	
   	}
	dom.get("qtyTotal").innerHTML=roundTo(nonDeductionTotal-deductionTotal);
	dom.get("totalMSheetQty").value=dom.get("qtyTotal").innerHTML;
}
 
function recalculateTotalOnDelete(record){
	var oldQuantity=record.getData("Quantity");
	var oldTotal=dom.get("qtyTotal").innerHTML;
 	if(dom.get("Deduction" + record.getId()).checked == true)
		dom.get("qtyTotal").innerHTML=roundTo(eval(getNumber(oldTotal))+eval(getNumber(oldQuantity)));
	else
		dom.get("qtyTotal").innerHTML=roundTo(getNumber(oldTotal)-getNumber(oldQuantity));
	dom.get("totalMSheetQty").value=dom.get("qtyTotal").innerHTML;	
}
     
function goToEstimate(){
	document.detailedMSheet.action='${pageContext.request.contextPath}/estimate/abstractEstimate!createMSheetList.action';
	document.detailedMSheet.submit();
}


function validateBeforeSubmit(){
	clearErrorMessage();
	if(mSheetDataTable.getRecordSet().getLength()==0){
		dom.get("msError").style.display='';
	    dom.get("msError").innerHTML='<s:text name='estimate.measurementSheetDataTable.zeroLength' />';
		return false;
	}
	else{
		var Records=mSheetDataTable.getRecordSet();
		for(var i=0;i<mSheetDataTable.getRecordSet().getLength();i++)
   		{
   	  		 for (var j = 2; j < mSheetColumnDefs.length-6; j++) {
   	  			 if(dom.get("error"+mSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display=="block" ||  dom.get("error"+mSheetColumnDefs[j].key+Records.getRecord(i).getId()).style.display==""){
   				  	dom.get("msError").style.display='';
				    dom.get("msError").innerHTML='Please enter a valid value for the '+mSheetColumnDefs[j].label.split('<br>')[0]+' at line '+Records.getRecord(i).getData("SlNo");
					return false;
   				 } 
  		  }
   		}
	}
	if(dom.get("qtyTotal").innerHTML<1){
		dom.get("msError").style.display='';
	    dom.get("msError").innerHTML='<s:text name='estimate.measurementSheetDataTable.negativeTotal' />';
		return false;
	}
	goToEstimate();
}

function clearErrorMessage(){
	dom.get("msError").style.display='none';
	document.getElementById("msError").innerHTML='';
}

function goToParent(){
		var wind;
		var data;
		var activityType;
	
		if(dom.get('sorId').value!="" && dom.get('sorId').value!=null)
			activityType="sor";
		else
			activityType="nonsor";
	
		wind=window.dialogArguments;
		if(wind==undefined){
			wind=window.opener;
			data=dom.get("totalMSheetQty").value+"`~`measurementSheetQty"+"`~`"+dom.get("recordId").value+"`~`"+activityType; 
			window.opener.update(data);
		}
		else{
			wind=window.dialogArguments;
			wind.result=dom.get("totalMSheetQty").value+"`~`measurementSheetQty"+"`~`"+dom.get("recordId").value+"`~`"+activityType; 
		} 
		window.close();
		return true;  
}

function resetMSheet() {
	window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!newMeasurementSheet.action?sorId="+'<s:property value="%{sorId}"/>'+"&recordId="+'<s:property value="%{recordId}"/>'+"&estimateUOM="+'<s:property value="%{&estimateUOM}"/>'+"&id="+'<s:property value="id"/>'+"&sourcepage="+'<s:property value="sourcepage"/>','_self');
}

</script> 
  
<div id="msError" class="errorstyle" style="display:none;"></div> 
<s:if test="%{hasErrors()}">
   <div id="errorstyle" class="errorstyle" >
     <s:actionerror/>
     <s:fielderror/>
   </div>
</s:if>
   
<s:form theme="simple" name="detailedMSheet">
<s:push value="model">
  <s:hidden name="totalMSheetQty" id="totalMSheetQty"/>
  <div class="navibarshadowwk"></div>
	<div class="formmainbox">
		<div class="insidecontent">
			<div class="rbroundbox2">
				<div class="rbtop2"><div></div></div>
				<div class="rbcontent2">
				    <div>
				    <B style="height: 25px;	width: 100%;font-family: sans-serif;font-size: 12px;">
					    <s:if test="%{sorId!=null}">
					       	<s:text name='estimate.measurementSheet.sorDescription'/>&nbsp;<s:property value="%{sorDesc}"/>
					    </s:if>
					    <s:else>
					    	<s:text name='estimate.measurementSheet.nonsorDescription'/>&nbsp;<s:property value="%{nonsorDesc}"/>
					    </s:else>
				    </B>
				    </div>
					<br>
					<s:hidden name="sorId" id="sorId" value="%{sorId}"></s:hidden>
					<s:hidden name="nonsorId" id="nonsorId" value="%{nonsorId}"></s:hidden>
					<s:hidden name="nonsorDesc" id="nonsorDesc" value="%{nonsorDesc}"></s:hidden>
					<s:hidden name="recordId" id="recordId" value="%{recordId}"></s:hidden>
					<s:hidden name="estimateUOM" id="estimateUOM" value="%{estimateUOM}"></s:hidden>
					<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}"></s:hidden>
					
					<table id="detailedEstTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			              <tr>
			                <td colspan="9" class="headingwk" style="border-right-width: 0px;">
				                <div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
				                <div class="headplacer"><s:text name='estimate.measurementSheet.detailedEstimate.name'/></div>
			                </td>
			                <td class="headingwk" id="showAdd" name="showAdd" align="right" style="border-left-width: 0px;">
			                		<a  id="addMSheet" name="addMSheet" onclick="mSheetDataTable.addRow({SlNo:mSheetDataTable.getRecordSet().getLength()+1,UOM:'<s:property value="%{estimateUOM}"/>',uomLength:'1',width:'1',recId:'<s:property value="%{recordId}"/>'});calculateQty(dom.get(mSheetColumnDefs[3].key+mSheetDataTable.getRecordSet().getRecord(mSheetDataTable.getRecordSet().getLength()-1).getId()),mSheetDataTable.getRecordSet().getRecord(mSheetDataTable.getRecordSet().getLength()-1).getId());return false;" href="#">  
									<img  width="16" height="16" border="0" src="/egworks/image/add.png"/>
									</a>
							</td>
			              </tr>
				          
			              <tr>
			                <td  colspan="10">
			                <div class="yui-skin-sam">
			                    <div id="mSheetTable"></div> 
			                    <div id="mSheetTableTotals"></div>  
			                </div>
			                </td>
			              </tr>
				    </table> 
	                <script>
						makeDetailedMSheetDataTable();
						count=1;
						recordIndex=0;
						 
						  <s:iterator id="mSheetIterator" value="estimateMeasurementSheetList" status="row_status">						  
						    <s:if test="%{((activity!=null && ((activity.schedule!=null && activity.schedule.id.equals(sorId)) || (activity.nonSor!=null && activity.nonSor.id.toString().equals(nonsorId)))) || (activity==null && recId!=null && recId.equals(recordId)))}">
						   	mSheetDataTable.addRow({
	                        SlNo:count++,
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
	                                     
	                        Quantity:'',
	                        UOM: '<s:property value="estimateUOM"/>',
	                        identifier:'<s:property value="identifier"/>',
	                        recId: '<s:property value="recordId" />',
	                        Delete:'X' 
	                    	}); 
	                    	var record = mSheetDataTable.getRecord(parseInt(recordIndex++));
	                    	if(dom.get("identifier"+record.getId()).value=='D')
	                    	 dom.get("Deduction"+record.getId()).checked=true;
	                    	else
	                    	 dom.get("Deduction"+record.getId()).checked=false;
	                    	findQuantity(record.getId());
						  </s:if>
						  </s:iterator>
						                   						
					</script> 
	 			</div>
				<div class="rbbot2"><div></div></div>
			</div>
		</div>
	</div>
	
	<div class="buttonholderwk" id="buttons" name="buttons"> 
		<input type="submit" class="buttonadd" value="SUBMIT" id="submitButton" onclick="return validateBeforeSubmit();" /> 
		<!-- <input type="button" class="buttonfinal" value="CLEAR" id="clearButton" name="clearbutton" onclick="resetMSheet()"/> -->
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='estimate.measurementSheet.close.confirm'/>');"/>
	</div>
	<script>
		 <s:if test="%{(model.currentState.value!='NEW' && (model.currentState.value!='REJECTED' )) || sourcepage=='search'}" >
		    /*   toggleFields(true,['closeButton']); 
		       mSheetDataTable.removeListener('cellClickEvent');
		       links=document.forms[0].getElementsByTagName("a"); 
			   disableLinks(links,[]); */
			   hideElements(['submitButton']);
	     </s:if>
	</script>
	</s:push>
	</s:form>  
  </body>
</html>
