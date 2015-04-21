<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%> 
<html>
<head>  
<title>Position</title>                   
 <style type="text/css">
	    .yui-dt-liner .yui-ac-input {
			position:relative;
			width: 80px;	
			min-width: 50px;     	   
		}       	   
			
				#codescontainer {position:absolute;left:11em;width:40%}
				#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
				#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
				#codescontainer ul {padding:5px 0;width:100%;}
				#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
				#codescontainer li.yui-ac-highlight {background:#ff0;}
				#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
    </style> 
    <c:catch var ="catchException">     
<script type="text/javascript">	      
  

var selectBillNumber;    
var selectDO;
var totalOutsourcedPosts=0;
var totalSanctionedPosts=0;

	function validateForm(method)            
	{
		
		if (document.getElementById('designationName').value == "")
		{				
			alert("Please Enter Designation Name");
			return false;    
		}
		if (document.getElementById('departmentName').value == "" ) 
		{				
			alert("Please Enter Department Name");
			return false;
		}
		if(document.getElementById('billNumberId').value>0)
		{
			document.getElementById('billNo').value = document.getElementById('billNumberId').options[document.getElementById('billNumberId').selectedIndex].text;
		}
		document.positionMaster.action='/eis/positionMaster/positionMaster!'+method+'.action';
		document.positionMaster.submit();
		return true;
	}	

	
	function validateFormSave(){
	
		var posLength=positionDataTable.getRecordSet().getLength();

		if (DataTrim(document.getElementById('sanctionedPosts').value) == ""  )
		{				
			alert("Please Enter Sanctioned Posts");
			return false;
		}
		
		/*if(document.getElementById('sanctionedPosts').value!=(newPosts+<s:property value="%{sanctionedPosts}"/>))
		{
			alert("Sanctioned Posts should be equal to the number of positions created in a department and designation");
			return false;
		}*/
		if(document.getElementById('outsourcedPosts').value!=totalOutsourcedPosts)
		{
			alert("Please Enter Outsourced Posts Correctly");
			return false;
		}
		if(!checkValueForPreviousRow())
		{
			return false;//validate position mandatory fields
		}
		document.positionMaster.action='/eis/positionMaster/positionMaster!savePosition.action';
		document.positionMaster.submit();

			return true;
	
	}

	function onView()
	{
			document.getElementById('designationName').disabled = true;
			

	}
	var designationNameSelectionHandler = function(sType, arguments) {
		var oData = arguments[2];
		var designationName = oData[0];
		var splitResult = designationName.split("~");
		document.getElementById('designationName').value = splitResult[0];
		document.getElementById('designationId').value = splitResult[2];
	}

	var departmentNameSelectionHandler = function(sType, arguments) {
		var oData = arguments[2];
		var departmentName = oData[0];
		var splitResult = departmentName.split("~");
		document.getElementById('departmentName').value = splitResult[0];
		document.getElementById('departmentId').value = splitResult[1];
	}
	 var billNumberDropdownOptions=[{label:"--- Select ---", value:"0"}
		 
		
	    <s:if test="billNumberLineList!=null && !billNumberLineList.isEmpty()">
	    
	    ,
	    <s:iterator var="s" value="billNumberLineList" status="status">  
		   
					    {"label":"<s:property value="%{billNumber}"/>" ,
					    	"value":"<s:property value="%{id}" />"
					    }
		    <s:if test="!#status.last">,</s:if>
	    </s:iterator> 
	    </s:if>      
	    ]
	    
	function createHiddenIDFormatter(el, oRecord, oColumn){
	    var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	        var value = (YAHOO.lang.isValue(oData))?oData:"";
	        var id=oColumn.getKey()+oRecord.getId();
	        var fieldName = "positionList[" + oRecord.getCount() + "]." + oColumn.getKey();
	        markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	        el.innerHTML = markup;
	    }
	    return hiddenFormatter;
	}
	 function createHiddenBillIdFormatter(el, oRecord, oColumn){
		    var hiddenFormatter = function(el, oRecord, oColumn, oData) {
		        var value = (YAHOO.lang.isValue(oData))?oData:"";
		        var id=oColumn.getKey()+oRecord.getId();
		        var fieldName = "positionList[" + oRecord.getCount() + "]." + oColumn.getKey()+".id";
		        markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
		        el.innerHTML = markup;
		    }
		    return hiddenFormatter;
		}
	function createNameFormatter(el, oRecord, oColumn){
	    var nameFormatter = function(el, oRecord, oColumn, oData) {
	        var value = (YAHOO.lang.isValue(oData))?oData:"";
	        var id=oColumn.getKey()+oRecord.getId();
	        var fieldName = "positionList[" + oRecord.getCount() + "]." + oColumn.getKey();
	        markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"' autocomplete='off' onchange='validatePositionName(this)'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";	      
	        el.innerHTML =markup;
	    }
	    return nameFormatter;
	}
	function createCheckBoxFormatter(el, oRecord, oColumn){
	    var nameFormatter = function(el, oRecord, oColumn, oData) {
	        var value = (YAHOO.lang.isValue(oData))?oData:"";
	        var id=oColumn.getKey()+oRecord.getId();
	        var fieldName =  oColumn.getKey();
	        markup="<input type='checkbox' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	        el.innerHTML = markup;
	    }
	    return nameFormatter;
	}

	function IdForCheckValues(el, oRecord, oColumn){
	    var numberFormatter = function(el, oRecord, oColumn, oData) {
	        var value = (YAHOO.lang.isValue(oData))?oData:"";
	        var id=oColumn.getKey()+oRecord.getId();
	        var fieldName = "positionList[" + oRecord.getCount() + "]." + oColumn.getKey();
	        markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	        el.innerHTML = markup;
	    }
	    return numberFormatter;
	}

    
	function createAddImageFormatter(baseURL){
		var addImageFormatter = function(el, oRecord, oColumn, oData) {
		    var imageURL=baseURL+"/common/image/add.png";
		    markup='<img height="16" border="0" width="14" alt="Add" src="'+imageURL+'"/>';
		    el.innerHTML = markup;
			}
			return addImageFormatter;
		}
	function createBillAutocompleteFormatter(size,maxlength)
	{
				
	 			var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   			var value = (YAHOO.lang.isValue(oData))?oData:"";
	    		var id=oColumn.getKey()+oRecord.getId();
	    		var fieldName = "positionList[" + oRecord.getCount() + "]." + oColumn.getKey()+".billNumber";
	    		markup="<input type='text'  id='"+id+"' name='"+fieldName+"' size='"+size+"'  value='"+value+"'onkeyup='billnumberAutocomplete(this,event)' autocomplete='off' />";
	   			el.innerHTML = markup;
	  		}
			return textboxFormatter;
	}
	function createDOAutocompleteFormatter(size,maxlength)
	{
				
	 			var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   			var value = (YAHOO.lang.isValue(oData))?oData:"";
	    		var id=oColumn.getKey()+oRecord.getId();
	    		var fieldName = "positionList[" + oRecord.getCount() + "]." + oColumn.getKey()+".code";
	    		markup="<input type='text'  id='"+fieldName+"' name='"+fieldName+"' size='"+size+"'  value='"+value+"'onkeyup='doAutocomplete(this,event)' autocomplete='off'  />";
	   			el.innerHTML = markup;
	  		}
			return textboxFormatter;
	}
	
	
	function loadDrawingOfficers()
	{
		var url = "/eis/positionMaster/positionMaster!getDrawingOfficers.action";
		var req = initiateRequest();
		req.open("GET", url, false);
		req.send(null);
	    if (req.status == 200)
	    {
	  	    var codes=req.responseText;
	  	    var a = codes.split("^");
			var codes = a[0];
			codesArray=codes.split("+");
			selectDO = new YAHOO.widget.DS_JSArray(codesArray);
	    }
	}
	function checkValueForPreviousRow()
    {
       
    	var records= positionDataTable.getRecordSet();
    	var i,indexRow;
    	var posName;
    	var isoutsourced;
    	for(i=0;i<records.getLength();i++)
    	{
        	indexRow = records.getRecord(i).getId();
        	posName=dom.get("name"+indexRow).value;
    		if(posName=="")
    		{
    			alert("Enter Position Name for Line :"+(i+1));
    			return false;
    		}
    	}
    	
    	
    	return true;
    }
	function findPos(obj)
	{
		var curleft = curtop = 0;
		if (obj.offsetParent)
		{
			curleft = obj.offsetLeft;
			curtop = obj.offsetTop;
			while (obj = obj.offsetParent)
			{
				curleft =curleft + obj.offsetLeft;
				curtop =curtop + obj.offsetTop; 
			}
		}
		return [curleft,curtop];
	}
	
	function validatePositionName(obj)
	{	
		if(DataTrim(obj.value)!="")
		{
			uniqueChecking('${pageContext.request.contextPath}/commonyui/egov/uniqueCheckAjax.jsp',  'EG_POSITION', 'NAME',obj.id, 'no', 'no');
		}
		
	}

	function onViewMode()
	{
		<s:if test="%{mode=='view'}">
		{
			positionDataTable.disable();
		}
		</s:if>

	}
	
	function billnumberAutocomplete(obj,event)
	{
		
		var id = "autocom"+obj.id;
	    var target = document.getElementById('codescontainer');
	  var autocomp = document.createElement("div");
	   autocomp.setAttribute("id", id);
	    target.appendChild(autocomp);
		/* set position of dropdown*/
		var src = obj;
		var posSrc=findPos(src);
		target.style.left=posSrc[0]+"px";
		target.style.top=posSrc[1]+22+"px";
		target.style.width=300;
		var coaCodeObj = obj;
		var key = event ? event.keyCode : event.charCode;  
			//40 --> Down arrow, 38 --> Up arrow
			if(key != 40 )
			{
				if(key != 38 )
				{
					oAutoCompName = new YAHOO.widget.AutoComplete(coaCodeObj,id, selectBillNumber);
					oAutoCompName.queryDelay = 0;
					oAutoCompName.prehighlightClassName = "yui-ac-prehighlight";
					oAutoCompName.useShadow = true;
					oAutoCompName.maxResultsDisplayed = 30;
					oAutoCompName.minQueryLength = 0;
				}
			}
	}
	function doAutocomplete(obj,event)
	{
		
		var id = "autocom"+obj.id;
	    var target = document.getElementById('codescontainer');
	  	var autocomp = document.createElement("div");
	   autocomp.setAttribute("id", id);
	    target.appendChild(autocomp);
		/* set position of dropdown*/
		var src = obj;
		var posSrc=findPos(src);
		target.style.left=posSrc[0]+"px";
		target.style.top=posSrc[1]+22+"px";
		target.style.width=300;
		var coaCodeObj = obj;
		var key = window.event ? window.event.keyCode : event.charCode;  
			//40 --> Down arrow, 38 --> Up arrow
			if(key != 40 )
			{
				if(key != 38 )
				{
					oAutoCompName = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', selectDO);
					oAutoCompName.queryDelay = 0;
					oAutoCompName.prehighlightClassName = "yui-ac-prehighlight";
					oAutoCompName.useShadow = true;
					oAutoCompName.maxResultsDisplayed = 30;
					oAutoCompName.minQueryLength = 0;  
				}
			}
	}
	var makePositionDataTable = function() {
	    var cellEditor=new YAHOO.widget.TextboxCellEditor();
	    var positionColumnDefs = [
										{key:"id",hidden:"true",  sortable:false, formatter:createHiddenIDFormatter(8,8),resizeable:false},
										{key:"name",label:'Position Name<span class="mandatory">*</span>',  sortable:false, formatter:createNameFormatter(8,8),resizeable:false},
										{key:"isPostOutsourcedDisplay",label:" Outsourced Post", sortable:false, formatter:"checkbox"},
										{key:"isPostOutsourced", hidden:"true", sortable:false, formatter:IdForCheckValues(3,3),resizeable:false},
										{key:"drawingOfficer",label:'Drawing Officer', sortable:false, formatter:createDOAutocompleteFormatter(15,15), resizeable:false},
										{key:"billNumber",hidden:"true",  sortable:false, formatter:createHiddenBillIdFormatter(8,8),resizeable:false},
										{key:"billNumberDisplay",label:'Bill Number', sortable:false, formatter:"dropdown", dropdownOptions:billNumberDropdownOptions, resizeable:false}

								  ]; 
	    var positionDataSource = new YAHOO.util.DataSource(); 
	    positionDataTable  = new YAHOO.widget.DataTable("positionCreateTable",positionColumnDefs, positionDataSource,{MSG_EMPTY:""});
	    positionDataTable.on('cellClickEvent',function (oArgs) {
											        var target = oArgs.target;
											        var record = this.getRecord(target);
											        var records= this.getRecordSet();
											        var column = this.getColumn(target);
											        if (column.key == 'Add') { 
												        if(checkValueForPreviousRow()){
												        	totalSanctionedPosts=totalSanctionedPosts+1;
												            document.getElementById('sanctionedPosts').value=totalSanctionedPosts;
												        	positionDataTable.addRow({});
												        }
											        } 

		});
	    
	    positionDataTable.subscribe("cellClickEvent", positionDataTable.onEventShowCellEditor); 

	    positionDataTable.subscribe('checkboxClickEvent',function (oArgs) {


	    	var target = oArgs.target;
	    	var record = this.getRecord(target);  
	    	var column = this.getColumn(target);
			  
	    	
	    	if(column.key=='isPostOutsourcedDisplay'){
	    		if(oArgs.target.checked){	
	    			totalOutsourcedPosts=totalOutsourcedPosts+1; 
	            	this.updateCell(record,this.getColumn('isPostOutsourced'),1);
	    		}
	    		else{
	    			totalOutsourcedPosts=totalOutsourcedPosts-1;
	    			this.updateCell(record,this.getColumn('isPostOutsourced'),0);
	    		}	
	    			document.getElementById('outsourcedPosts').value= totalOutsourcedPosts;         	
	    				
	       }
	    });

	    positionDataTable.subscribe('dropdownChangeEvent',function (oArgs) {

	    	var target = oArgs.target;
	    	var record = this.getRecord(target);
	    	var column = this.getColumn(target);
	    	
	    	if(column.key=='billNumberDisplay'){
	            var selectedIndex=oArgs.target.selectedIndex;
	            this.updateCell(record,this.getColumn('billNumber'),billNumberDropdownOptions[selectedIndex].value);
	            
	        }
	        
	    });   
	    
	    return {
	        oDS: positionDataSource,
	        oDT: positionDataTable
	        };
	        
	};  
	   
	    function createDataTable(){
	    	
	    	if (document.getElementById('designationId').value >0 && document.getElementById('departmentId').value>0)
		    	{
	    		
	    		
	    			<s:if test="%{mode!='view'}">
	    			document.getElementById('saveButton').style.display="block";
	    			</s:if>
			    	var table ;
				    <s:if test="%{null!=positionList }">  
				    		table =makePositionDataTable();
                           <s:iterator id="positionList" value="positionList" status="row_status">
	                           table.oDT.addRow({
										id:'<s:property value="id"/>',
										name:'<s:property value="name"/>',
										 <s:if test="%{isPostOutsourced==1}">isPostOutsourcedDisplay:'1',</s:if>
	                              	 <s:if test="%{isPostOutsourced==0}">isPostOutsourcedDisplay:'',</s:if>
	                              	 isPostOutsourced:'<s:property value="isPostOutsourced"/>',
	                              	 drawingOfficer:'<s:property value="drawingOfficer.code"/>',
	                              		billNumber:'<s:property value="billNumber.id"/>',
										billNumberDisplay:'<s:property value="billNumber.id"/>'});
									
						</s:iterator>
                           totalSanctionedPosts=<s:property value="%{sanctionedPosts}"/>;
                           totalOutsourcedPosts=<s:property value="%{outsourcedPosts}"/>;
					
					</s:if>
	
			    }
	    }
	
	    function populateBillNumber(){		
			var deptId = document.getElementById('departmentId').value;

			if(deptId!='' ){
				populatebillNumberId({departmentId:deptId});
			}
	}	
	    billNumberIdFailureHandler=function(){
	    	  warn('Unable to load ${dropdownId}');
	    	}
function setQueryParams()
{
	var params="departmentId="+document.getElementById('departmentId').value;
	params=params+"&designationId="+document.getElementById('designationId').value;
	return params;

}
var positionNameSelectionHandler = function(sType, arguments) {
	var oData = arguments[2];
	var positionId = oData[1];
	document.getElementById('positionId').value = positionId;
}

function resetIdWhenNoPos()
{
	if(document.getElementById('positionName').value=="")
		document.getElementById('positionId').value = 0;
}

</script>
</head>

<body onload="loadDrawingOfficers();createDataTable();onViewMode();">  
<div class="formmainbox">
<div class="insidecontent">
<div class="rbroundbox2">
<s:form  theme="simple">
<s:token/>

<s:url id="createModify" action="positionMaster" method="createOrModify"/>
<s:url id="view" action="positionMaster" method="view"/> 
<s:url id="save" action="positionMaster" method="save"/>
<div class="mandatory" id="positionMaster_error"
				style="display: none;"></div>
			
			<s:if test="%{hasErrors()}">
				<div class="errorcss" id="fieldError">
					<s:actionerror cssClass="mandatory" />
					<s:fielderror cssClass="mandatory" />
				</div>
			</s:if>
			<div align="center">
			<s:if test="hasActionMessages()">
			<s:actionmessage cssClass="actionmessage" />
			</s:if>
			</div>
			
			<s:hidden id="mode" name="mode" value="%{mode}" />
			
				<div class="rbcontent2">
			<table width="95%" cellpadding="0" cellspacing="0" border="0">
				<tbody><tr><td>&nbsp;</td></tr>
				
					<tr>
						<td  class="headingwk">
						<div class="arrowiconwk">
								<img src="${pageContext.request.contextPath}/common/image/arrow.gif" />
							</div>
							
							<div class="headplacer">
								<s:text name="post.details" />
							</div>
							</td>
						<td></td>
					</tr>
				</tbody>
			</table>
			<br>
			
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td class="greyboxwk"><span class="mandatory">*</span><s:text name="Designation Name" /></td>
						<td class="greybox2wk" width="40%" valign="top" align="left" colspan="2">
								<div class="yui-skin-sam">
									<div id="DesignationSearch_autocomplete" class="yui-ac">
									<s:hidden  name="designationId" id="designationId" /> 
										<s:textfield id="designationName" name="designationName" value="%{designationName}"
											 size="50" cssClass="selectwk" autocomplete="off" />
										<div id="designationNameSearchResults"></div>
									</div>
								</div> 
								<egovtags:autocomplete name="designationName" field="designationName"
									url="${pageContext.request.contextPath}/positionMaster/positionMaster!getDesignations.action" 
									queryQuestionMark="true" results="designationNameSearchResults"
									handler="designationNameSelectionHandler"
									forceSelectionHandler="designationNameSelectionEnforceHandler" /> <span
								class='warning' id="improperdesignationNameSelectionWarning"></span>
						</td>
						
								
								<td class="greyboxwk"><span class="mandatory">*</span><s:text name="Department" /></td>
						<td class="greybox2wk" width="40%" valign="top" align="left" colspan="2">
								<div class="yui-skin-sam">
									<div id="DepartmentSearch_autocomplete" class="yui-ac">
									<s:hidden  name="departmentId" id="departmentId" />  
										<s:textfield id="departmentName" name="departmentName" value="%{departmentName}"
											 size="20" cssClass="selectwk" onblur="populateBillNumber();" autocomplete="off"/>
										<div id="departmentNameSearchResults"></div>
									</div>
								</div> 
								<egovtags:autocomplete name="departmentName" field="departmentName"
									url="${pageContext.request.contextPath}/positionMaster/positionMaster!getDepartments.action" 
									queryQuestionMark="true" results="departmentNameSearchResults"
									handler="departmentNameSelectionHandler"
									forceSelectionHandler="departmentNameSelectionEnforceHandler" /> <span
								class='warning' id="improperdepartmentNameSelectionWarning"></span>
						</td>
					</tr>
					
					<tr>
						<td class="whiteboxwk"><s:text name="positionsUnmapped.lbl" /></td>
						<td class="whitebox2wk">
						<s:checkbox name="showUnmappedPositions"  value="%{showUnmappedPositions}" > </s:checkbox>
						</td>
						
						
						<td class="whiteboxwk"><s:text name="Position" /></td>
						
						<td class="whitebox2wk" width="40%" valign="top" align="left" >
								<div class="yui-skin-sam">
									<div id="PositionSearch_autocomplete" class="yui-ac">
									
									<s:hidden  name="positionId" id="positionId" />  
										<s:textfield id="positionName" name="positionName" value="%{positionName}"
											 size="20" cssClass="selectwk"  autocomplete="off" onblur="resetIdWhenNoPos()"/>
										<div id="positionNameSearchResults"></div>
									</div>
								</div> 
								<egovtags:autocomplete name="positionName" field="positionName"
									url="${pageContext.request.contextPath}/positionMaster/positionMaster!positionsByDeptDesignation.action"
									
									queryQuestionMark="true" results="positionNameSearchResults" 
									paramsFunction="setQueryParams"
									handler="positionNameSelectionHandler"
									forceSelectionHandler="positionNameSelectionEnforceHandler" /> <span
								class='warning' id="improperpositionNameSelectionWarning"></span>
						</td>
						
						<td class="whiteboxwk" >Bill Number</td>
					    <td class="whitebox2wk">
					    	<egovtags:ajaxdropdown id="billNumberDropdown" fields="['Text','Value']" dropdownId="billNumberId" url="positionMaster/positionMaster!billNumbersByDept.action"/>
							    	
							
							<s:select name="billNumberId" id="billNumberId" list="%{billNumberHeaderList}" 
							listKey="id"	listValue="billNumber" 	headerKey="0" headerValue="------Select------"/>
							<s:hidden id="billNo" name="billNo" value="%{billNo}"/>
						</td> 
						
					</tr>
					
					<tr>
						<td class="greyboxwk">Sanctioned Posts</td>
						<td class="greybox2wk" width="20%" valign="top" align="left">
								
										<s:textfield id="sanctionedPosts" name="sanctionedPosts" value="%{sanctionedPosts}" readonly="true"
											onblur="checkAlphaNumeric(this);" size="10" cssClass="selectwk" maxLength="5" cssStyle="text-align:right"/>
										
						</td>
						<td class="greyboxwk">Outsourced Posts</td>
						<td class="greybox2wk"><s:textfield
								id="outsourcedPosts" name="outsourcedPosts" readonly="true"
								value="%{outsourcedPosts}"  maxLength="5" cssStyle="text-align:right"/></td>
								
								<td class="greybox2wk" colspan="2"></td>
					</tr>
				
				</tbody>
			</table>
			
			<table width="95%" cellpadding="0" cellspacing="0" border="0">	<tbody><tr>
								    	<td align="right">
								    	<div class="mandatory">* Mandatory Fields</div></td>
								    </tr></tbody></table>
								    
			
			
			<table align="center">
				<tr>
				
						<td align="center">							
								<input type="button" name="createModify" value="Modify" class="buttonfinal" onclick="return validateForm('createOrModify');"  />
						</td>
						<td align="center">	<s:submit  value="View"	cssClass="buttonfinal" onclick="return validateForm('view');" /></td>
						<td align="center"> <input type="button" name="close" id="close" value="Close"	
							onclick="window.close();" class="buttonfinal" />						
						</td>
						<td align="center"> &nbsp;</td>
				</tr>
			
			
			
				
			 
			</table>
			
			<br/>		
   			<div class="yui-skin-sam">
   					<div id="positionCreateTable" align="center">
   					</div>
    		</div>
   					<div id="codescontainer"></div>           	
    		
    	
    	
    	
    	<table>        
		   
		
		
	</table>
	
</div>
<br/>
<div align="center">      
<s:if test="%{mode!='view' && null!=positionList}">
				<s:submit  value="Save" cssStyle="display:none" id="saveButton" align="center"
									cssClass="buttonfinal" onclick="return validateFormSave();" />
		</s:if>
			
	</div>  		
</s:form>
</c:catch>
<c:if test = "${catchException!=null}">
The error  is : ${catchException}<br>
</c:if>	
</div>
</div>
</div>

</body>
</html>
