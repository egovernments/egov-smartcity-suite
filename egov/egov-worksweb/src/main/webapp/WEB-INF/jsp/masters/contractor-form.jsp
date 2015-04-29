
<!--<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>-->

<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>
<script src="<egov:url path='/js/works.js'/>"></script>
<script>
function validateLoggedInUser(){
	var userHasRole = dom.get("hasRoleMapped").value; 
	if(userHasRole=="true" && dom.get("mode").value!="view"){
		document.getElementById('isEditEnabled').disabled=false;
	}
	else{
		document.getElementById('isEditEnabled').disabled=true;
	}
	enableDisableFields(userHasRole);
}

function enableDisableFields(userHasRole) {
	var isChecked = document.getElementById("isEditEnabled");
	var panNo = document.getElementById("panNumber");
	var bankId = document.getElementById("bank");
	var codeIFSC = document.getElementById("ifscCode");
	var bankAcctNo = document.getElementById("bankAccount");
		
	if(dom.get("mode").value=="edit" && userHasRole!="true"){
		if(isChecked.checked){
				panNo.disabled = false;
				bankId.disabled=false;
				codeIFSC.disabled = false;
				bankAcctNo.disabled = false;
		}
		else{
			panNo.disabled = true;
			bankId.disabled=true;
			codeIFSC.disabled = true;
			bankAcctNo.disabled = true;
		}
	}
}

function validateAllFields(){

	validateContractorFormAndSubmit();
	var panNo = document.getElementById("panNumber").value;
	var bankId = document.getElementById("bank").value;
	var codeIFSC = document.getElementById("ifscCode").value;
	var bankAcctNo = document.getElementById("bankAccount").value;
	var userHasRole = dom.get("hasRoleMapped").value;
	var isChecked =document.getElementById("isEditEnabled");

	if(userHasRole=="true" && !isChecked.checked){
		if(panNo=="" || bankId==-1 || codeIFSC=="" || bankAcctNo==""){
			document.getElementById("contractor_error").innerHTML='<s:text name="contractor.check.allDetails" />';
			document.getElementById("contractor_error").style.display='';
			return false;
		}
		else
		{
			document.getElementById("contractor_error").innerHTML='';
			document.getElementById("contractor_error").style.display="none";
		}
	}
	return true;
}

function validateContractorFormAndSubmit() {
    clearMessage('contractor_error')
	links=document.contractor.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++) {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    
    if(errors) {
        dom.get("contractor_error").style.display='';
    	document.getElementById("contractor_error").innerHTML='<s:text name="contractor.validate_x.message" />';
    	return false;
    }
 /* else {
    	document.contractor.action='${pageContext.request.contextPath}/masters/contractor!save.action';
    	document.contractor.submit();
   	} */ 
}

var departmentDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.departmentList" status="status">  
    {"label":"<s:property value="%{deptName}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
var gradeDropdownOptions=[{label:"--- Select ---", value:"0"},
    <s:iterator var="s" value="dropdownData.gradeList" status="status">   
    {"label":"<s:property value="%{grade}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
    
var statusDropdownOptions=[
    <s:iterator var="s" value="dropdownData.statusList" status="status">   
    {"label":"<s:property value="%{description}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
     
function createTextBoxFormatter(size,maxlength) {
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   var fieldName = "actionContractorDetails[" + oRecord.getCount() + "]." +  oColumn.getKey();   
	   var id = oColumn.getKey()+oRecord.getId();
	
       markup="<input type='text' id='"+id+"'  class='selectmultilinewk' size='"+size+"' maxlength='"+maxlength+"' name='"+fieldName+ "'/>";
	   el.innerHTML = markup; 
   
	  
	}
	return textboxFormatter;	
}

var regNoTextboxFormatter= createTextBoxFormatter(11,50);

var dateFormatter = function(e2, oRecord, oColumn, oData) {
	var fieldName = "actionContractorDetails[" + oRecord.getCount() + "].validity." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();
	
    var markup= "<input type='text' id='"+id+"'   class='selectmultilinewk' size='15' maxlength='10' style=\"width:60px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
  
	
}

function createHiddenFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "actionContractorDetails[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}
var departmentIdHiddenFormatter= createHiddenFormatter(5,5);
var gradeIdHiddenFormatter= createHiddenFormatter(5,5);
var statusIdHiddenFormatter= createHiddenFormatter(5,5);

var contractorDataTable;
var makeContractorDataTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var contractorColumnDefs = [ 
		{key:"department", hidden:true, formatter:departmentIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"grade", hidden:true, formatter:gradeIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"status", hidden:true, formatter:statusIdHiddenFormatter, sortable:false, resizeable:false},
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"departmentName", label:'<span class="mandatory">*</span><s:text name="contractor.department"/>', formatter:"dropdown",dropdownOptions:departmentDropdownOptions, sortable:false, resizeable:false},	
		{key:"registrationNumber", label:'<s:text name="contractor.registrationNo"/>', formatter:regNoTextboxFormatter, sortable:false, resizeable:false},		
		{key:"gradeName", label:'<s:text name="contractor.grade" />',formatter:createDropdownFormatter('contractorGradedetails','id'), dropdownOptions:gradeDropdownOptions, sortable:false, resizeable:false},			
		{key:"statusDesc", label:'<span class="mandatory">*</span><s:text name="contractor.status" />',formatter:createDropdownFormatter('contractorStatusdetails','id'),dropdownOptions:statusDropdownOptions, sortable:false, resizeable:false},		
		{key:"startDate", label:'<span class="mandatory">*</span><s:text name="contractor.fromDate" />', formatter:dateFormatter,sortable:false, resizeable:false},
		{key:"endDate",label:'<s:text name="contractor.toDate"/>', formatter:dateFormatter,sortable:false, resizeable:false},
		{key:'deleteRow',label:'<s:text name="contractorDetails.row.delete"/>',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}  
	];
	
	var contractorDataSource = new YAHOO.util.DataSource(); 
	contractorDataTable = new YAHOO.widget.DataTable("contractorTable",contractorColumnDefs, contractorDataSource, {MSG_EMPTY:"<s:text name='contractor.initial.table.message'/>"});
	contractorDataTable.subscribe("cellClickEvent", contractorDataTable.onEventShowCellEditor); 
	contractorDataTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'deleteRow') { 	
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
		}        
	});
	contractorDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='departmentName'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('department'),departmentDropdownOptions[selectedIndex].value);
        }
        if(column.key=='gradeName'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('grade'),gradeDropdownOptions[selectedIndex].value);
        }
        if(column.key=='statusDesc'){
    	    var selectedIndex=oArgs.target.selectedIndex; 
        	this.updateCell(record,this.getColumn('status'),statusDropdownOptions[selectedIndex].value);
        }
	});
	
	contractorDataTable.addRow({SlNo:contractorDataTable.getRecordSet().getLength()+1,status:statusDropdownOptions[0].value});	
	dom.get("status"+contractorDataTable.getRecordSet().getRecord(0).getId()).value=statusDropdownOptions[0].value;

	return {
	    oDS: contractorDataSource,
	    oDT: contractorDataTable
	};  
}

--></script>
<div class="errorstyle" id="contractor_error" style="display: none;"></div>

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
            		<div class="headplacer"><s:text name="contractor.header" /></div>
            	</td>
        	</tr>
        
			<tr>
				<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="contractor.code" />:</td>
            	<td class="whitebox2wk"><s:textfield name="code" disabled="%{sDisabled}" id="code" maxlength="50" cssClass="selectwk" value = "%{code}" /></td>
            	<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="contractor.name" />:</td>
            	<td class="whitebox2wk"><s:textfield name="name" id="name" size="40" maxlength="100" cssClass="selectwk" value = "%{name}" /></td>
            </tr>
            <tr>
                <td class="greyboxwk"><s:text name="contractor.correspondenceAddress" />:</td>
                <td class="greybox2wk"><s:textarea name="correspondenceAddress" cols="35" cssClass="selectwk" id="correspondenceAddress" value="%{correspondenceAddress}"/></td>
                <td class="greyboxwk"><s:text name="contractor.paymentAddress" />:</td>
                <td class="greybox2wk"><s:textarea name="paymentAddress" cols="35" cssClass="selectwk" id="paymentAddress" value="%{paymentAddress}"/></td>
           </tr>
		   <tr>
				<td class="whiteboxwk"><s:text name="contractor.contactPerson" />:</td>
            	<td class="whitebox2wk"><s:textfield name="contactPerson" id="contactPerson" size="40" maxlength="100" cssClass="selectwk" value = "%{contactPerson}" /></td>
            	<td class="whiteboxwk"><s:text name="contractor.email" />:</td>
            	<td class="whitebox2wk"><s:textfield name="email" id="email" maxlength="100" cssClass="selectwk" value = "%{email}" /></td>
           </tr>
		   <tr>
                <td class="greyboxwk"><s:text name="contractor.narration" />:</td>
                <td class="greybox2wk" colspan="3"><s:textarea name="narration" cols="35" cssClass="selectwk" id="narration" value="%{narration}"/></td>
           </tr>
		   <tr>
				<td class="whiteboxwk"><s:text name="contractor.panNo" />:</td>
            	<td class="whitebox2wk"><s:textfield name="panNumber" id="panNumber" maxlength="14" cssClass="selectwk" value = "%{panNumber}" /></td>
            	<td class="whiteboxwk"><s:text name="contractor.tinNo" />:</td>
            	<td class="whitebox2wk"><s:textfield name="tinNumber" id="tinNumber" maxlength="14" cssClass="selectwk" value = "%{tinNumber}" /></td>
           </tr>		
           <tr>
                <td class="greyboxwk"><s:text name="contractor.bank" />:</td>
          		<td class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="bank" id="bank" cssClass="selectwk" list="dropdownData.bankList" listKey="id" listValue="name" value="%{bank.id}" /></td>
          		<td class="greyboxwk"><s:text name="contractor.ifscCode" />:</td>
            	<td class="greybox2wk"><s:textfield name="ifscCode" id="ifscCode" maxlength="15" cssClass="selectwk" value = "%{ifscCode}" /></td>
           </tr>
           <tr>
				<td class="whiteboxwk"><s:text name="contractor.bankAccount" />:</td>
            	<td class="whitebox2wk"><s:textfield name="bankAccount" id="bankAccount" maxlength="22" size="24" cssClass="selectwk" value = "%{bankAccount}" /></td>
            	<td class="whiteboxwk"><s:text name="contractor.pwdApprovalCode" />:</td>
            	<td class="whitebox2wk"><s:textfield name="pwdApprovalCode" id="pwdApprovalCode" maxlength="50" cssClass="selectwk" value = "%{pwdApprovalCode}" /></td>
           </tr>
           <tr>
           <td class="greyboxwk">
					<s:text name="contractor.editEnable.checkbox" />:</td>
	   			<td class="greybox2wk">
   					<s:checkbox name="isEditEnabled" id="isEditEnabled" value="%{isEditEnabled}"  />
	   			</td>
	   			<td class="greyboxwk" colspan="2" />
	   		
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
            <table id="detailsTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
				<div class="headplacer"><s:text name="contractor.contDetails" /></div>
				</td>
				<td align="right" class="headingwk" style="border-left-width: 0px"><a href="#" onclick="contractorDataTable.addRow({SlNo:contractorDataTable.getRecordSet().getLength()+1,status:statusDropdownOptions[0].value});return false;"><img border="0" alt="Add Row" src="${pageContext.request.contextPath}/image/add.png" /></a>
				</td>
			</tr>
		<tr>
			<td colspan="4">
			<div class="yui-skin-sam">
			<div id="contractorTable"></div>
	<script>
            makeContractorDataTable();
         <s:iterator id="detailsIterator" value="model.contractorDetails" status="row_status">
	       <s:if test="#row_status.count == 1">
	             contractorDataTable.updateRow(0,
	                                {department:'<s:property value="department.id"/>',
	        						grade:'<s:property value="grade.id"/>',
	        						status:'<s:property value="status.id"/>',
	                                SlNo:'<s:property value="#row_status.count"/>',
	                                departmentName:'<s:property value="department.deptName"/>',
	                                registrationNumber:'<s:property value="registrationNumber"/>',
	                                gradeName:'<s:property value="grade.grade"/>',
	                                statusDesc:'<s:property value="status.description"/>',
	                                startDate:'<s:property value="validity.startDate"/>',
	                                endDate:'<s:property value="validity.endDate"/>'});
	          </s:if>
	          <s:else>
		        contractorDataTable.addRow(
	        						{department:'<s:property value="department.id"/>',
	        						grade:'<s:property value="grade.id"/>',
	        						status:'<s:property value="status.id"/>',
	                                SlNo:'<s:property value="#row_status.count"/>',
	                                departmentName:'<s:property value="department.deptName"/>',
	                                registrationNumber:'<s:property value="registrationNumber"/>',
	                                gradeName:'<s:property value="grade.grade"/>',
	                                statusDesc:'<s:property value="status.description"/>',
	                                startDate:'<s:property value="validity.startDate"/>',
	                                endDate:'<s:property value="validity.endDate"/>'});
	            </s:else>  
	              
			        var record = contractorDataTable.getRecord(parseInt('<s:property value="#row_status.index"/>'));
			    				        
			        var  column = contractorDataTable.getColumn('registrationNumber');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="registrationNumber"/>';
			      
			        column = contractorDataTable.getColumn('startDate');
			        <s:date name="validity.startDate" var="startDateFormat" format="dd/MM/yyyy"/>  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';

			        <s:if test="%{validity.endDate!=null}">				        
				        column = contractorDataTable.getColumn('endDate');  
				        <s:date name="validity.endDate" var="endDateFormat" format="dd/MM/yyyy"/>  
				        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';
			        </s:if>
			     	 column = contractorDataTable.getColumn('departmentName');
			        for(i=0; i < departmentDropdownOptions.length; i++) {
			            if (departmentDropdownOptions[i].value == '<s:property value="department.id"/>') {
			                contractorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
			            }
			        }
			        column = contractorDataTable.getColumn('gradeName');
			        for(i=0; i < gradeDropdownOptions.length; i++) {
			            if (gradeDropdownOptions[i].value == '<s:property value="grade.id"  />') {
			                contractorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
			            }
			        }
			        column = contractorDataTable.getColumn('statusDesc');
			        for(i=0; i < statusDropdownOptions.length; i++) {
			            if (statusDropdownOptions[i].value == '<s:property value="status.id"/>') {
			                contractorDataTable.getTdEl({record:record, column:column}).getElementsByTagName("select").item(0).selectedIndex = i;
			            }
			        }

			       </s:iterator>         
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

  <tr>
    <td><div align="right" class="mandatory">* <s:text name="message.mandatory" /></div></td>
  </tr>
 </table>	    
</div>

<div class="rbbot2"><div></div></div>

</div>
</div>
</div>
<script>
<s:if test="%{mode=='view'}">
	for(i=0;i<document.contractor.elements.length;i++){
		document.contractor.elements[i].disabled=true;
		document.contractor.elements[i].readonly=true;
	} 
	
	contractorDataTable.removeListener('cellClickEvent');		       
	links=document.contractor.getElementsByTagName("a");
	for(i=0;i<links.length;i++){    
	links[i].onclick=function(){return false;};
	}
</s:if>


</script>
 
