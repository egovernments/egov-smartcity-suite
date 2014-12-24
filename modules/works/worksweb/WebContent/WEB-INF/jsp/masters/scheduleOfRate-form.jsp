<%@ taglib prefix="s" uri="/struts-tags"%>
<!--<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>-->
<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>
<script src="<egov:url path='/js/works.js'/>"></script>
<script>

function validateSORFormAndSubmit() {
	clearMessage('sor_error')
	links=document.scheduleOfRate.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    
    if(errors) {
        dom.get("sor_error").style.display='';
    	document.getElementById("sor_error").innerHTML='<s:text name="sor.validate_x.message" />';
    	return;
    }
    else {
	
		var lenFrm=document.scheduleOfRate.elements.length;
		for(i=0;i<lenFrm;i++){
			document.scheduleOfRate.elements[i].readonly=false;	
			document.scheduleOfRate.elements[i].disabled=false;	
		} 
    	document.scheduleOfRate.action='${pageContext.request.contextPath}/masters/scheduleOfRate!create.action';
    	document.scheduleOfRate.submit();
   	}
}
function validateLineBreaks(){
	var codeName = dom.get('code').value;
	codeName = codeName.replace(/([\n]|<br \>)/g,'');
	dom.get("code").value = codeName;
}

function validateLineBreaksInDescription(){
	var descriptionText = dom.get('description').value;
	descriptionText = descriptionText.replace(/([\n]|<br \>)/g,'');
	dom.get("description").value = descriptionText;
}

function UniqueCheckOnCodenumber() {
	codeno = dom.get('code').value;
	categoryType = dom.get('category').value;
	if(categoryType==-1 && codeno!='')
		dom.get("selectcategory").style.display = "";
	else {
		populatenumberunique({codeNo:codeno,categoryTypeId:categoryType});
		dom.get("selectcategory").style.display = "none";
	}
}

function checkForCode() {	
	 if(dom.get("numberunique").style.display =="" ){
		 document.getElementById('code').value="";
	 }	 
}
 
function createHiddenFormatter(el, oRecord, oColumn, oData){
var hiddenFormatter = function(el, oRecord, oColumn, oData) {
    var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    var fieldName = "actionRates[" + oRecord.getCount() + "]." + oColumn.getKey();
    var fieldValue=value;
    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
}
return hiddenFormatter;
}
var hiddenFormatter = createHiddenFormatter(10,10); 
 
 
 
function createTextBoxFormatter(size,maxlength) {
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   var fieldName = "actionRates[" + oRecord.getCount() + "]." +  oColumn.getKey();   
	   var id = oColumn.getKey()+oRecord.getId();
	   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='"+maxlength+"' style=\"width:100px; text-align:right\" name='"+fieldName+ "'" 
	   + " onblur='validateNumberInTableCell(scheduleOfRateDataTable,this,\"" + oRecord.getId()+ "\");'/>"
	   + " <span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	   el.innerHTML = markup; 
	}
	return textboxFormatter;	
}

var rateTextboxFormatter = createTextBoxFormatter(11,10);
var dateFormatter = function(e2, oRecord, oColumn, oData) {
	var fieldName = "actionRates[" + oRecord.getCount() + "].validity." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();
	
	var markup= "<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='10' style=\"width:100px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
}

var scheduleOfRateDataTable;
var makeScheduleOfRateDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var scheduleOfRateColumnDefs = [ 
		{key:"id", hidden:true,formatter:hiddenFormatter,sortable:false, resizeable:false} ,
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:50},
		{key:"rate", label:'<span class="mandatory">*</span>Rate', formatter:rateTextboxFormatter, sortable:false, resizeable:false, width:180},		
		{key:"startDate", label:'<span class="mandatory">*</span>Start Date', formatter:dateFormatter,sortable:false, resizeable:false, width:130},
		{key:"endDate",label:'End Date', formatter:dateFormatter,sortable:false, resizeable:false, width:130},
		{key:'deleteRate',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	
	var scheduleOfRateDataSource = new YAHOO.util.DataSource(); 
	scheduleOfRateDataTable = new YAHOO.widget.DataTable("scheduleOfRateTable",scheduleOfRateColumnDefs, scheduleOfRateDataSource, {MSG_EMPTY:"<s:text name='master.sor.initial.table.message'/>"});
	scheduleOfRateDataTable.subscribe("cellClickEvent", scheduleOfRateDataTable.onEventShowCellEditor); 
	scheduleOfRateDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'deleteRate') { 	
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}        
	});
	return {
	    oDS: scheduleOfRateDataSource,
	    oDT: scheduleOfRateDataTable
	};  
}

</script>
<div class="errorstyle" id="sor_error" style="display: none;"></div>

<span align="center" style="display:none" id="selectcategory">
 	<div class="errorstyle" >
         <s:text name="sor.code.categoryType.null"/>
   </div>
</span>
<span align="center" style="display:none" id="numberunique">
  <div class="errorstyle" >
         <s:text name="codeno.exists"/>
  </div>
</span>

<div class="navibarshadowwk"></div>
<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
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
            		<div class="headplacer"><s:text name="sor.header" /></div>
            	</td>
        	</tr>
        	<tr>
        		<td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="master.sor.category" />:</td>
          		<td width="21%" class="whitebox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="category" id="category" cssClass="selectwk" list="dropdownData.categorylist" listKey="id" listValue="code" value="%{category.id}" onchange="UniqueCheckOnCodenumber();"/> </td>
                <td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="master.sor.code" />:</td>
          		<td width="53%" class="whitebox2wk"><s:textfield name="code" cssClass="selectwk" id="code" value = "%{code}" maxlength = "50" autocomplete="off" 	onkeyup="UniqueCheckOnCodenumber();" onblur="checkForCode();validateLineBreaks();"/>
					<egov:uniquecheck id="numberunique" fields="['Value']" url='/masters/ajaxScheduleOfRate!codeNumberUniqueCheck.action' key='codenumber.already.exists' />
				</td>
			</tr>
			<tr>
				<td width="11%" class="greyboxwk"><span class="mandatory">*</span><s:text name="master.sor.description" />:</td>
            	<td width="21%" class="greybox2wk"><span class="greybox2wk">
            	<s:textarea name="description" cols="45"  rows="4" cssClass="selectwk" id="description" value = "%{description}"  maxlength = "4000" onblur="validateLineBreaksInDescription();"/></span>
            	</td>
                <td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name="master.sor.uom" />:</td>
          		<td width="53%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="uom" id="uom" cssClass="selectwk" list="dropdownData.uomlist" listKey="id" listValue="uom" value="%{uom.id}" /></td>
            </tr>
	        <tr>
	          	<td colspan="4" class="shadowwk"></td>
	        </tr>
 			</table>
 		</td>
     </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
          <tr>
            <td>
            <table id="ratesTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
				<div class="headplacer"><s:text name="sor.rateDetails" /></div>
				</td>
				<td align="right" class="headingwk" style="border-left-width: 0px"><a href="#" onclick="scheduleOfRateDataTable.addRow({SlNo:scheduleOfRateDataTable.getRecordSet().getLength()+1});return false;"><img border="0" alt="Add SOR Rate" src="${pageContext.request.contextPath}/image/add.png" /></a>
				</td>
			</tr>
		<tr>
			<td colspan="4">
			<div class="yui-skin-sam">
			<div id="scheduleOfRateTable"></div>
	<script>
            makeScheduleOfRateDataTable();
         <s:iterator id="rateIterator" value="model.rates" status="rate_row_status">
				        scheduleOfRateDataTable.addRow(
			        						{id:'<s:property value="id"/>',											
			                                SlNo:'<s:property value="#rate_row_status.count"/>',
			                                rate:'<s:property value="rate"/>',
			                                startDate:'<s:property value="validity.startDate"/>',
			                                endDate:'<s:property value="validity.endDate"/>'
											}
											);
				var record = scheduleOfRateDataTable.getRecord(parseInt('<s:property value="#rate_row_status.index"/>'));			  									
				var rateidValue='<s:property value="id"/>';
			<s:if test="%{estimateDtFlag=='yes'}">
				<s:iterator status="stat" value="deletFlagMap" >				
					var key='<s:property value="key"/>';
					var value='<s:property value="value"/>';	
				if(key==rateidValue && value=='yes'){
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';					
					dom.get(column.getKey()+record.getId()).readonly='true';
			        dom.get(column.getKey()+record.getId()).disabled='true';
					
			        var column = scheduleOfRateDataTable.getColumn('startDate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.startDate"/>';
					dom.get(column.getKey()+record.getId()).readonly='true';
					dom.get(column.getKey()+record.getId()).disabled='true';
					<s:if test="%{(validity.endDate!=null) && (validity.endDate.compareTo(today)<0)}">
						var column = scheduleOfRateDataTable.getColumn('endDate');  
				        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.endDate"/>';
						dom.get(column.getKey()+record.getId()).readonly='true';
						dom.get(column.getKey()+record.getId()).disabled='true';
					</s:if>
					<s:else>
						var column = scheduleOfRateDataTable.getColumn('endDate');  
				        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.endDate"/>';
					</s:else>
				}else{	
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';
			        
			        var column = scheduleOfRateDataTable.getColumn('startDate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.startDate"/>';
			       			        
			        var column = scheduleOfRateDataTable.getColumn('endDate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.endDate"/>';	  				
				}
				</s:iterator>		
			</s:if>			
			<s:else>		
					var column = scheduleOfRateDataTable.getColumn('rate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';
			        
			        var column = scheduleOfRateDataTable.getColumn('startDate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.startDate"/>';
			       			        
			        var column = scheduleOfRateDataTable.getColumn('endDate');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="validity.endDate"/>';
			</s:else>

		</s:iterator>
       </script>
  		</div>
		</td>
	</tr>
    </table>
    </td>
   </tr>
   <%@ include file='scheduleOfRate-marketRate.jsp'%>
  <tr>
  	<td colspan="4" class="shadowwk"></td>
  </tr>
  <tr>
    <td><div align="right" class="mandatory">* <s:text name="message.mandatory" /></div></td>
  </tr>
 </table>	    
</div>

<div class="rbbot2"><div></div></div>
<!-- 
<input type="button" value="Call1" onclick="testAjaxCall1()"/>
<input type="button" value="Call2" onclick="testAjaxCall2()"/>
 -->
</div>
</div>
</div>
<script>
<s:if test="%{mode=='view'}">
	for(i=0;i<document.scheduleOfRate.elements.length;i++){
		document.scheduleOfRate.elements[i].disabled=true;
		document.scheduleOfRate.elements[i].readonly=true;
	} 
	scheduleOfRateDataTable.removeListener('cellClickEvent');		       
	links=document.scheduleOfRate.getElementsByTagName("a");
	for(i=0;i<links.length;i++){    
	links[i].onclick=function(){return false;};
	}
</s:if>
<s:if test="%{mode=='edit'}">
	scheduleOfRateDataTable.removeListener('cellClickEvent');
</s:if>
<s:if test="%{estimateDtFlag=='yes'}">
		scheduleOfRateDataTable.removeListener('cellClickEvent');	
		var len=document.scheduleOfRate.elements.length;		
		for(i=0;i<5;i++){		
		if(i==3){
		}
		else
		{			
			document.scheduleOfRate.elements[i].readonly=true;	
			document.scheduleOfRate.elements[i].disabled=true;	
		}
		}
</s:if>	

	

	function testAjaxCall1() {
		var url1 = '${pageContext.request.contextPath}/masters/ajaxScheduleOfRate!getByResponseAware.action';
        var ajaxcall = new Ajax.Request(url1, {
            method:'get',onSuccess:handleSuccess });
	}
	
	function testAjaxCall2() {
		var url2 = '${pageContext.request.contextPath}/masters/ajaxScheduleOfRate!getByDirectResponse.action';
        var ajaxcall = new Ajax.Request(url2, {
            method:'get',onSuccess:handleSuccess });
	}
	
	function handleSuccess(transport) {
		alert('status :'+transport.status+' ,Result: '+transport.responseText);
	}
</script>
 
