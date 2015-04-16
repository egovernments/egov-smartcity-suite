<script type="text/javascript">
var dom=YAHOO.util.Dom;
var assignmentDataTable;
var recordToModifyAssignment;

$(function() {
    $( "#assignmentFromDateId").datepicker({ dateFormat: "dd/mm/yy" ,changeYear: true,changeMonth: true,yearRange:"1900:c+100"});
    $( "#assignmentToDateId" ).datepicker({ dateFormat: "dd/mm/yy" ,changeYear: true,changeMonth: true,yearRange:"1900:c+100"});
    
  });


var designationSelectionHandler = function(sType, arguments) {
		var oData = arguments[2];
		document.getElementById('empDesig').value = oData[0];
		document.getElementById('empDesigId').value = oData[1];
		
	}

	var designationSelectionEnforceHandler = function(sType, arguments) {
		warn('impropercodeSelection');
	}


var posSelectionHandler = function(sType, arguments) {
	var oData = arguments[2];
	document.getElementById('position').value = oData[0];
	document.getElementById('positionId').value = oData[1];
}

function addAssignment()
{
	if(!vaidateTopFieldAssignment())
		return false;
	
	var isHodDeptSelected=false;
	var hodIdRadio = document.getElementsByName("hod");
	if(hodIdRadio[0].checked)
		isHodDeptSelected=true;
	var hodDepts = document.getElementById("hodDeptId");
	var selectedValue = new Array();
	if(isHodDeptSelected)
	{	
		for(var i=0;i<hodDepts.options.length;i++)
		{
			if(hodDepts.options[i].selected)
			{	
				selectedValue.push(hodDepts.options[i].value);
			}	
		}
	}	
	var pirmary;
	var radio=document.getElementsByName("isPrimary");
	if(radio[0].checked)
		primary='Y';
	else
		primary='N';
	if(recordToModifyAssignment!=null)
	{
		dom.get("id"+recordToModifyAssignment.getId()).value=document.getElementById("assignmentId").value;
		dom.get("isPrimary"+recordToModifyAssignment.getId()).value=primary;
		dom.get("fromDate"+recordToModifyAssignment.getId()).value=document.getElementById("assignmentFromDateId").value;
		dom.get("toDate"+recordToModifyAssignment.getId()).value=document.getElementById("assignmentToDateId").value;
		dom.get("prdId"+recordToModifyAssignment.getId()).value=document.getElementById("assignmentPrdId").value;
		dom.get("deptIdDisplay"+recordToModifyAssignment.getId()).value=document.getElementById("deptId").options[document.getElementById("deptId").selectedIndex].text;
		dom.get("deptId"+recordToModifyAssignment.getId()).value=document.getElementById("deptId").options[document.getElementById("deptId").selectedIndex].value;
		dom.get("desigIdDisplay"+recordToModifyAssignment.getId()).value=document.getElementById("empDesig").value;
		dom.get("desigId"+recordToModifyAssignment.getId()).value=document.getElementById("empDesigId").value;
		dom.get("positionDisplay"+recordToModifyAssignment.getId()).value=document.getElementById("position").value;
		dom.get("position"+recordToModifyAssignment.getId()).value=document.getElementById("positionId").value;
		dom.get("fundIdDisplay"+recordToModifyAssignment.getId()).value=document.getElementById("fundId").options[document.getElementById("fundId").selectedIndex].text;
		dom.get("fundId"+recordToModifyAssignment.getId()).value=document.getElementById("fundId").options[document.getElementById("fundId").selectedIndex].value;
		dom.get("functionIdDisplay"+recordToModifyAssignment.getId()).value=document.getElementById("functionId").options[document.getElementById("functionId").selectedIndex].text;
		dom.get("functionId"+recordToModifyAssignment.getId()).value=document.getElementById("functionId").options[document.getElementById("functionId").selectedIndex].value;
		dom.get("functionaryDisplay"+recordToModifyAssignment.getId()).value=document.getElementById("functionaryId").options[document.getElementById("functionaryId").selectedIndex].text;
		dom.get("functionary"+recordToModifyAssignment.getId()).value=document.getElementById("functionaryId").options[document.getElementById("functionaryId").selectedIndex].value;
		dom.get("gradeIdDisplay"+recordToModifyAssignment.getId()).value=document.getElementById("gradeId").options[document.getElementById("gradeId").selectedIndex].text;
		dom.get("gradeId"+recordToModifyAssignment.getId()).value=document.getElementById("gradeId").options[document.getElementById("gradeId").selectedIndex].value;
		dom.get("govtOrderNo"+recordToModifyAssignment.getId()).value=document.getElementById("govtOrderNoId").value;
		assignmentDataTable.updateCell(recordToModifyAssignment,assignmentDataTable.getColumn('hodDeptListDisplay'),selectedValue);
		
	}	
	else
	{	
	
	assignmentDataTable.addRow({
		    id:'',
		    isPrimary:primary,
			fromDate:document.getElementById("assignmentFromDateId").value,
			toDate:document.getElementById("assignmentToDateId").value,
			prdId:document.getElementById("assignmentPrdId").value,
			deptIdDisplay:document.getElementById("deptId").options[document.getElementById("deptId").selectedIndex].text,
     		deptId:document.getElementById("deptId").options[document.getElementById("deptId").selectedIndex].value,
     		desigIdDisplay:document.getElementById("empDesig").value,
     		desigId:document.getElementById("empDesigId").value,
     		positionDisplay:document.getElementById("position").value,
     		position:document.getElementById("positionId").value,
     		fundIdDisplay:document.getElementById("fundId").options[document.getElementById("fundId").selectedIndex].text,
     		fundId:document.getElementById("fundId").options[document.getElementById("fundId").selectedIndex].value,
     		functionIdDisplay:document.getElementById("functionId").options[document.getElementById("functionId").selectedIndex].text,
     		functionId:document.getElementById("functionId").options[document.getElementById("functionId").selectedIndex].value,
     		functionaryDisplay:document.getElementById("functionaryId").options[document.getElementById("functionaryId").selectedIndex].text,
     		functionary:document.getElementById("functionaryId").options[document.getElementById("functionaryId").selectedIndex].value,
     		gradeIdDisplay:document.getElementById("gradeId").options[document.getElementById("gradeId").selectedIndex].text,
     		gradeId:document.getElementById("gradeId").options[document.getElementById("gradeId").selectedIndex].value,
     		govtOrderNo:document.getElementById("govtOrderNoId").value,
			hodDeptListDisplay:selectedValue,
			checked:'N'
		});
	}	
	recordToModifyAssignment=null;//reset the record set
	clearTopFields();
}

function generateParameter()
{
	var status = "";
	var employeeId;
	var dept=document.getElementById("deptId").value;
	var desig=document.getElementById("empDesigId").value;
	var radio=document.getElementsByName("isPrimary");
	var primary;

	if(document.getElementById("mode").value!='Create')
	{
		employeeId=document.getElementById("id").value;
	}	
	else
		employeeId="";	
	
	if(radio[0].checked)
		primary='Y';
	else
		primary='N';
	var fromDate=document.getElementById("assignmentFromDateId").value;
	var toDate=document.getElementById("assignmentToDateId").value;
	if(primary=='Y')
	{	
		status ="approverDeptId="+dept+"&approverDesigId="+desig+"&isPrimary="+primary+"&fromDate="+fromDate+"&toDate="+toDate+"&employeeId="+employeeId;
	}
	else
	{
		status="isPrimary="+primary;
	}		
	return status;
}	

function showHodDepts()
{
	var radio =document.getElementsByName("hod");
	if(radio[0].checked)
	{
		document.getElementById("isHodlbl").style.display="";
		document.getElementById("hodDept").style.display="";	
		document.getElementById("hodId").colSpan="0";
	}
	else
	{
		document.getElementById("isHodlbl").style.display="none";
		document.getElementById("hodDept").style.display="none";
		document.getElementById("hodId").colSpan="3";
	}	
}

// YUI data table for assignment

function createDeleteImageFormatter(baseURL)
{
	var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
    var imageURL=baseURL+"/common/image/cancel.png";
    markup='<img height="14" border="0" width="14" alt="Delete" src="'+imageURL+'"/>';
    el.innerHTML = markup;
	}
	return deleteImageFormatter;
}

function createHiddenFomatter(suffix)
{
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	var value = (YAHOO.lang.isValue(oData))?oData:"";
    var id=oColumn.getKey()+oRecord.getId();
    markup="<input  type='hidden'  id='"+id+"' name='"+id+"'  value='"+value+"'/>";
    el.innerHTML = markup;
	}
	return hiddenFormatter;
}

function createHiddenIdFormatter(suffix)
{
            
             var hiddenFormatter = function(el, oRecord, oColumn, oData) {
               var value = (YAHOO.lang.isValue(oData))?oData:"";
            var id=oColumn.getKey()+oRecord.getId();
            var fieldName="assignmentList["+oRecord.getCount()+"]."+ suffix;
            markup="<input  type='hidden'  id='"+id+"' name='"+fieldName+"'  value='"+value+"'/>";
               el.innerHTML = markup;
          }
        return hiddenFormatter;
}

function createTextFormatter(suffix,size)
{
	var textboxFormatter = function(el,oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			var id=oColumn.getKey()+oRecord.getId();
			var fieldName = "assignmentList["+oRecord.getCount()+"]."+suffix;
			markup = "<input type='text' size='"+size+"' class='whitebox4wk' id='"+id+"' name='"+fieldName+"' value='"+value+"' readonly='true'/>";
			el.innerHTML = markup;
		}	
	return textboxFormatter;
}


function createEditButtonFormatter(baseURL)
{
	var imageFormatter = function(el, oRecord, oColumn, oData) 
    {
        var imageURL=baseURL+"/common/image/book_edit.png";
        markup='<a href="#"><img height="16" border="0" width="14" alt="Edit" src="'+imageURL+'"/></a>';
        el.innerHTML = markup;
    }
    return imageFormatter;
}



var lang=YAHOO.lang;


function createDropdownFormatter(){
    return function(el, oRecord, oColumn, oData) {
       var selectedValue = new Array();
       selectedValue = (lang.isValue(oData)) ? oData : oRecord.getData(oColumn.field);
       var options = (lang.isArray(oColumn.dropdownOptions)) ?oColumn.dropdownOptions : null;
       var selectEl;
       var collection = el.getElementsByTagName("select");
       if(selectedValue!=null)
       {    
	       if(collection.length === 0) {
	           selectEl = document.createElement("select");
	           selectEl.className = YAHOO.widget.DataTable.CLASS_DROPDOWN;
	           selectEl.name = "assignmentList["+oRecord.getCount()+"].hodDeptIds";
	           selectEl.id = oColumn.getKey()+oRecord.getId();
	           selectEl = el.appendChild(selectEl);
	           YAHOO.util.Event.addListener(selectEl,"change",this._onDropdownChange,this);
	           
	       }
	
	       selectEl = collection[0];
		   selectEl.multiple=true;
		   selectEl.size=2;	
		   selectEl.disabled=true;
	       if(selectEl) {
	           selectEl.innerHTML = "";
	           if(options) {
	              for(var i=0; i<options.length; i++) {
	                   var option = options[i];
	                   var optionEl = document.createElement("option");
	                   optionEl.value = (lang.isValue(option.value)) ?option.value : option;
	                   for(var j=0;j<selectedValue.length;j++)
	                   {
	                    	if(!isNaN(selectedValue[j]) && (selectedValue[j]!=" ")  )
	                     	{
	                         	if(optionEl.value==selectedValue[j] && (optionEl.value!=0))
	                            { 	   
				                   optionEl.innerHTML = (lang.isValue(option.text)) ?option.text : (lang.isValue(option.label)) ? option.label : option;
				                   optionEl.selected = true;
				                   optionEl = selectEl.appendChild(optionEl);
				                   break;
	                            }
	                     	}
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
}

var hodDropdownOptions=[{label:"--- Select ---", value:"0"}
		<s:if test="dropdownData.deptMasterList!=null && !dropdownData.deptMasterList.isEmpty()">,
		<s:iterator var="s" value="dropdownData.deptMasterList" status="status">  
		{"label":"<s:property value="%{deptName}"/>" ,
		 "value":"<s:property value="%{id}" />"
		}<s:if test="!#status.last">,</s:if>
		</s:iterator>  
		</s:if>     
];




var makeAssignmentDataTable = function() {
    var cellEditor=new YAHOO.widget.TextboxCellEditor();
    var assignReportColumnDefs = [
									{key:"id",hidden:"true", sortable:false, formatter:createHiddenIdFormatter("id"),resizeable:false},
									{key:"fromDate",label:"From Date",sortable:false,formatter:createTextFormatter("assignmentPrd.fromDate","8"),resizeable:false},
									{key:"prdId",hidden:"true",formatter:createHiddenIdFormatter("assignmentPrd.id")},
									{key:"toDate",label:"To Date",sortable:false,formatter:createTextFormatter("assignmentPrd.toDate","8"),resizeable:false},
									{key:"fundIdDisplay",label:"Fund",sortable:false,formatter:createTextFormatter("fundId.name","20"),resizeable:false},
									{key:"fundId",hidden:"true",sortable:false,formatter:createHiddenIdFormatter("fundId.id"),resizeable:false},
									{key:"functionIdDisplay",label:"Function",sortable:false,formatter:createTextFormatter("functionId.name","20"),resizeable:false},
									{key:"functionId",hidden:"true",sortable:false,formatter:createHiddenIdFormatter("functionId.id"),resizeable:false},
									{key:"gradeIdDisplay",label:"Grade",sortable:false,formatter:createTextFormatter("gradeId.name","5"),resizeable:false},
									{key:"gradeId",hidden:"true",sortable:false,formatter:createHiddenIdFormatter("gradeId.id"),resizeable:false},
									{key:"desigIdDisplay",label:"Designation",sortable:false,formatter:createTextFormatter("desigId.designationName","20"),resizeable:false},
									{key:"desigId",hidden:"true",sortable:false,formatter:createHiddenIdFormatter("desigId.designationId"),resizeable:false},
									{key:"positionDisplay",label:"Position",sortable:false,formatter:createTextFormatter("position.name","20"),resizeable:false},
									{key:"position",hidden:"label",sortable:false,formatter:createHiddenIdFormatter("position.id"),resizeable:false},
									{key:"functionaryDisplay",label:"Funcationary",sortable:false,formatter:createTextFormatter("functionary.name","30"),resizeable:false},
									{key:"functionary",hidden:"true",sortable:false,formatter:createHiddenIdFormatter("functionary.id"),resizeable:false},
									{key:"deptIdDisplay",label:"Main Department",sortable:false,formatter:createTextFormatter("deptId.deptName","20"),resizeable:false},
									{key:"deptId",hidden:"true",sortable:false,formatter:createHiddenIdFormatter("deptId.id"),resizeable:false},
									{key:"hodDeptListDisplay",label:"Departments",sortable:false,formatter:createDropdownFormatter(),dropdownOptions:hodDropdownOptions,resizeable:true},
									{key:"govtOrderNo",label:"Govt Order No",sortable:false,formatter:createTextFormatter("govtOrderNo","8"),resizeable:false},
									{key:"isPrimary",label:"Primary",sortable:false,formatter:createTextFormatter("isPrimary","3"),resizeable:false}
									<s:if test="%{mode!='View'}">
										,
											{key:"edit",label:"Edit",sortable:false,formatter:createEditButtonFormatter("${pageContext.request.contextPath}"),resizeable:false},
											{key:'Delete',label:"Del",formatter:createDeleteImageFormatter("${pageContext.request.contextPath}"),resizeable:false}
									</s:if>	
										,
									{key:"checked",hidden:"true", sortable:false, formatter:createHiddenFomatter("assignChecked"),resizeable:false},
									{key:"createdBy",hidden:"true",sortable:false, formatter:createHiddenIdFormatter("createdBy.id")},
									{key:"createdDate",hidden:"true",sortable:false, formatter:createHiddenIdFormatter("createdDate")}
					];

    var assignmentDataSource = new YAHOO.util.DataSource();
    assignmentDataTable  = new YAHOO.widget.DataTable("assignmentCreateTable",assignReportColumnDefs, assignmentDataSource,{MSG_EMPTY:""});    
    assignmentDataTable.subscribe("cellClickEvent", assignmentDataTable.onEventShowCellEditor); 

    assignmentDataTable.on('cellClickEvent',function (oArgs) {

		var target = oArgs.target;
		var record = this.getRecord(target);
		recordToModifyAssignment = this.getRecord(target);
		var records= this.getRecordSet();
		var column = this.getColumn(target);
		if (column.key == 'Delete') { 	
			if(record.getData('checked')!='Y'){
				this.deleteRow(record);
			}
				else{
				   showError('<bean:message key="alertRowDel"/>');
				}
				
		}

		if (column.key == 'edit') {
			document.getElementById("assignmentId").value=record.getData('id');
			if(record.getData('isPrimary')=='Y')
			{
				document.getElementById("isPrimaryIdY").checked=true;
			}	
			else
			{
				document.getElementById("isPrimaryIdN").checked=true;
			}	
			document.getElementById("assignmentPrdId").value=record.getData('prdId');
			document.getElementById("assignmentFromDateId").value=record.getData('fromDate');
			document.getElementById("assignmentToDateId").value=record.getData('toDate');
			document.getElementById("deptId").value=record.getData('deptId');
			document.getElementById("empDesigId").value=record.getData('desigId');
			document.getElementById("empDesig").value=record.getData('desigIdDisplay');
			document.getElementById("position").value=record.getData('positionDisplay');
			document.getElementById("positionId").value=record.getData('position');
			document.getElementById("fundId").value=record.getData('fundId');
			document.getElementById("functionId").value=record.getData('functionId');
			document.getElementById("functionaryId").value=record.getData('functionary');
			document.getElementById("gradeId").value=record.getData('gradeId');
			document.getElementById("govtOrderNoId").value=record.getData('govtOrderNo');
			var isHodSelected=false;
			if(record.getData('hodDeptListDisplay')!=null)
			{
				var selectedValue = record.getData('hodDeptListDisplay');
				var hodDepts = document.getElementById("hodDeptId");
				for(var i=0;i<hodDepts.options.length;i++)
				{
					for(var j=0;j<selectedValue.length;j++)
					{
						if(!isNaN(selectedValue[j]) && (selectedValue[j]!=" "))
						{	
							if(hodDepts.options[i].value==selectedValue[j])
							{
								hodDepts.options[i].selected=true;
								isHodSelected=true;
								break;
							}	
						}		
					}	
				}
				if(isHodSelected)
				{	
					document.getElementById("isHodlbl").style.display="";
					document.getElementById("hodDept").style.display="";	
					document.getElementById("hodId").colSpan="0";
					document.getElementById("hodRadioId1").checked=true;
				}	
			}
			this.updateCell(record,this.getColumn('checked'),record.getData('id')==null?"N":"Y");
		}
		
		});   
     
    
    return {
        oDS: assignmentDataSource,
        oDT: assignmentDataTable
        };
        
};

 </script> 
<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tbody><tr><td>&nbsp;</td></tr>
			<tr>
				<td  class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
					</div>
					<div class="headplacer">
						<s:text name="assign.details"/>
					</div>
				</td>
				<td></td>
			</tr>
		</tbody>
</table>
<div id="topTable">
<table width="100%" cellpadding="0" cellspacing="0" border="0" >
			<tbody>	
			<s:hidden id="assignmentId"/>
				<!-- Is Assignment Primary -->	
				<tr>
							<td class="whiteboxwk" width="16%"><span class="mandatory">*</span><s:text name="is.primary"/></td>
							<td class="whitebox2wk" colspan="3">
									<s:radio name="isPrimary" value="'Y'" list="#{'Y':'Yes','N':'No' }" id="isPrimaryId" />
							</td>
				</tr>	
				<!-- Assignment From Date and To Date -->
				<tr>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="assign.fromDate"/></td>
						<td class="greybox2wk" width="16%">
							<s:date id="assignmentFromDate"  name='assignmentFromDate' format='dd/MM/yyyy' var="assignFromDate"/>
							<s:textfield  id="assignmentFromDateId"  name="assignmentFromDate" cssClass="selectwk" value="%{assignFromDate}"
								onblur = "validateDateFormat(this);checkDOBforAssignment(this);CompfaDate(this);CompeffecDate(this);checkDOBforAssignment(this);populateJoinDate(this);" 
									onfocus="setFocus(this,'dd/mm/yyyy');javascript:vDateType='3'"
									onkeyup="DateFormat(this,this.value,event,false,'3') "/>
						</td>	
						<s:hidden id="assignmentPrdId" name="assignmentPrdId" />
						<td class="greyboxwk" width="16%" ><span class="mandatory">*</span><s:text name="assign.toDate"/></td>
						<td class="greybox2wk">
							<s:date id="assignmentToDate"  name='assignmentToDate' format='dd/MM/yyyy' var="assignToDate"/>
							<s:textfield  id="assignmentToDateId"  name="assignmentToDate" cssClass="selectwk" value="%{assignToDate}"
								onblur = "validateDateFormat(this);checkDOBforAssignment(this);CompfaDate(this);CompeffecDate(this);checkDOBforAssignment(this);populateJoinDate(this);compareFromDateAndToDate();" 
									onfocus="setFocus(this,'dd/mm/yyyy');javascript:vDateType='3'"
									onkeyup="DateFormat(this,this.value,event,false,'3') "/>
						</td>    
				</tr>	
				<!-- Department and Designation -->
				<tr>
						<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="assign.dept"/></td>
						<td class="whitebox2wk" ><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.deptMasterList" listKey="id"
									listValue="deptName" id="deptId"
									name="deptName" cssClass="selectwk" onchange="clearFields('Department');" />
						</td>
						<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="assign.desig"/></td>
									<td class="whitebox2wk" valign="top" align="left">
										<div class="yui-skin-sam">
											<div id="desigSearch_autocomplete" class="yui-ac">
												<s:textfield id="empDesig" name="empDesig" 
													size="20" cssClass="selectwk" onblur="clearFields('Designation');" />
												<div id="desigSearchResults"></div>
											</div>
										</div> 
										<egovtags:autocomplete name="empDesig" field="empDesig"
													url="${pageContext.request.contextPath}/common/employeeSearch!getAllDesignations.action" 
													queryQuestionMark="true" results="desigSearchResults"
													handler="designationSelectionHandler"
													forceSelectionHandler="designationSelectionEnforceHandler" />
										<span class='warning' id="impropercodeSelectionWarning"></span>
										<s:hidden id="empDesigId" />
									</td>
				</tr>
				<!-- Position and Fund -->
				<tr>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="assign.pos"/></td>
						<td class="greybox2wk" valign="top" align="left">
							<div class="yui-skin-sam">
								<div id="posSearch_autocomplete" class="yui-ac">
									<s:textfield name="position" id="position" cssClass="selectwk" size="20" onkeyup="checkDeptDesig(this);"/>
									<div id="posSearchResults"/>
								</div>
							</div>		
							<egovtags:autocomplete name="position" field="position"
													url="${pageContext.request.contextPath}/common/employeeSearch!getPositionsForDeptDesig.action" 
													queryQuestionMark="true" results="posSearchResults"
													handler="posSelectionHandler"
													forceSelectionHandler="true" paramsFunction="generateParameter"/>
										<s:hidden id="positionId" />	
									
						</td>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="assign.fund"/></td>
						<td class="greybox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.fundlist" listKey="id"
									listValue="name" id="fundId"
									name="fundName" cssClass="selectwk" />
						</td>
				</tr>
				<!-- Function -->
				<tr>
						<td class="whiteboxwk"><span class="mandatory">*</span><s:text name="assign.function"/></td>
						<td class="whitebox2wk" colspan="3"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.functionlist" listKey="id"
									listValue="name" id="functionId"
									name="functionName" cssClass="selectwk" />
						</td>
				</tr>
				<!-- Functionary and Grade -->
				<tr>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="assign.functionary"/></td>
						<td class="greybox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.functionaryMasterList" listKey="id"
									listValue="name" id="functionaryId"
									name="functionaryName" cssClass="selectwk" />
						</td>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="assign.grade"/></td>
						<td class="greybox2wk"><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.gradeMasterList" listKey="id"
									listValue="name" id="gradeId"
									name="gradeName" cssClass="selectwk" />
						</td>
				</tr>
				
				<!-- Govt Order No -->
				<tr>
					<td class="whiteboxwk"><s:text name="govt.order.no"/></td>
					<td class="whitebox2wk" colspan="3">
									<s:textfield name="govtOrderNo" id="govtOrderNoId"/>
					</td>
				</tr>	
				<!-- Govt Order No -->	
				<tr>
					<td class="greyboxwk"><s:text name="hod"/></td>
					<td id="hodId" class="greybox2wk" colspan="3">
							<s:radio name="hod" value="0" list="#{'1':'Yes','0':'No' }" id="hodRadioId" onclick="showHodDepts()"/>
					</td>	
					<td id="isHodlbl" class="greyboxwk" style="display:none"><s:text name="hod.dept"/></td>	
					<td id="hodDept" class="greybox2wk" style="display:none" ><s:select
									headerValue="-------choose-------" headerKey="0"
									list="dropdownData.deptMasterList" listKey="id"
									listValue="deptName" id="hodDeptId"
									name="deptName" cssClass="selectwk" multiple="true" size="5"/>
						</td>		    
				</tr>
			</tbody>
</table>
</div>
<br/>
<s:if test="%{mode!='View'}">
<div align="center">
	<input type="button"  class="buttonfinal" name="Save" value="SAVE/MODIFY" id="addrow" onclick="addAssignment();"/>
	<input type="button"  class="buttonfinal" name="Clear" value="CLEAR" onclick="clearTopFields();"/>
</div>
</s:if>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tbody><tr><td>&nbsp;</td></tr>
			<tr>
				<td  class="headingwk">
					<div class="arrowiconwk">
						<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
					</div>
					<div class="headplacer">
						<s:text name="assign.details.list"/>
					</div>
				</td>
				<td></td>
			</tr>
		</tbody>
</table>
<div class="scroll" style="overflow-y:auto;overflow-x:auto  ">
		<div id="codescontainer"></div>
     <div class="yui-skin-sam">
            <div id="assignmentCreateTable" ></div>
    </div>
<table>        
    <script>
   				var table = makeAssignmentDataTable();
	   			 <s:if test="%{null!=assignmentList || !assignmentList.isEmpty()}">    
		   			<s:iterator id="assignmentList" value="assignmentList" status="row_status" var="assign_status">
		   			assignmentDataTable.addRow({
			             		id:'<s:property value="id"/>',
			             		fromDate:'<s:date name="assignmentPrd.fromDate" format="dd/MM/yyyy"/>',
			             		toDate:'<s:date name="assignmentPrd.toDate" format="dd/MM/yyyy"/>',
			             		prdId:'<s:property value="assignmentPrd.id"/>',
			             		fundIdDisplay:'<s:property value="fundId.name"/>',
			             		fundId:'<s:property value="fundId.id"/>',
			             		functionIdDisplay:'<s:property value="functionId.name"/>',
			             		functionId:'<s:property value="functionId.id"/>',
			             		gradeIdDisplay:'<s:property value="gradeId.name"/>',
			             		gradeId:'<s:property value="gradeId.id"/>',
			             		positionDisplay:'<s:property value="position.name"/>',
			             		position:'<s:property value="position.id"/>',
			             		functionaryDisplay:'<s:property value="functionary.name"/>',
			             		functionary:'<s:property value="functionary.id"/>',
			             		deptIdDisplay:'<s:property value="deptId.deptName"/>',
			             		deptId:'<s:property value="deptId.id"/>',
			             		desigIdDisplay:'<s:property value="desigId.designationName"/>',
			             		desigId:'<s:property value="desigId.designationId"/>',
			             		hodDeptListDisplay:'<s:property value="hodDeptIds"/>',
			             		govtOrderNo:'<s:property value="govtOrderNo"/>',
			             		isPrimary:'<s:property value="isPrimary"/>',
			             		checked:'Y',
			             		createdBy:'<s:property value="createdBy.id"/>',
			             		createdDate:'<s:property value="createdDate"/>'});
	         		</s:iterator>	
	         	</s:if>	
    </script>
		
</table>
</div>

