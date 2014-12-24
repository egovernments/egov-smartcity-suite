<%@page import="org.egov.pims.model.EmployeeView,
			java.text.SimpleDateFormat,
			org.apache.log4j.Logger,
			java.util.*,
        org.egov.pims.model.PersonalInformation"%>
<%@page import="org.egov.pims.model.EmployeeNomineeMaster"%>
<!--
	Program Name : NomineeCreateModify.jsp
	Author		: DivyaShree MS
	Created	on	: 08-02-2010
	Purpose 	: Search Employee by designation,dept,code,name,type or status
 -->
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egovtags" tagdir="/WEB-INF/tags"%>



<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*"%>
<style>
#warning {
  display:none;
  color:blue;
}

.mandatoryone{
color:red;
font-weight:normal;
}

.mandatoryTwo{
color:green;
font-weight:normal;
}
.scroll
{
overflow-x:auto;
overflow-y:auto;
}




</style>
<s:push value="model">

<html>
  <head>
  
  <style type="text/css">
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
		.yui-dt-liner .yui-ac-input{
			position:relative;
			width: 100%;	
			min-width: 150px;	
		}
    </style>
  
  <script type="text/javascript" src="<c:url value='/nomineeMasterjs/nomineeMaster.js'/>"></script>
  
   <title>Nominee Master</title>
   
   
   
   <script type="text/javascript">
     var dom=YAHOO.util.Dom;
     


	
	function setFocus(obj,defaultval)
{
	if(obj.value==defaultval ){
		document.getElementById(obj.id).value="";
		document.getElementById(obj.id).style.color='black';
		}
}
function setBlur(obj,defaultval)
{
	if(obj.value == ""){
		document.getElementById(obj.id).value=defaultval;
		document.getElementById(obj.id).style.color='';
	}
}
function mandatoryFields()
{
   if(!checkValueForPreviousRow())
   {
	   return false;
   }	   	
   var records= nomineeDataTable.getRecordSet();
   var nomineeName,relationType,dob,martialStatus,isActive,bank,branch,accountNum,isWorking,age,branchName,disburse,disburseName;
   var i=records.getLength()-1;

   			nomineeName=dom.get("nomineeName"+records.getRecord(i).getId()).value;
    	    relationType=dom.get("relationType"+records.getRecord(i).getId()).value;
    	    dob=dom.get("nomineeDob"+records.getRecord(i).getId()).value;
    	    martialStatus=dom.get("maritalStatus"+records.getRecord(i).getId()).value;
    	    isActive=dom.get("isActive"+records.getRecord(i).getId()).value;
    	    branchName=dom.get("branchname"+records.getRecord(i).getId()).value;
    	    bank=dom.get("bank"+records.getRecord(i).getId()).value;
    	    branch=dom.get("bankBranch"+records.getRecord(i).getId()).value;
    	    accountNum=dom.get("accountNumber"+records.getRecord(i).getId()).value;
    	    isWorking=dom.get("isWorking"+records.getRecord(i).getId()).value;
    	    guardianName=dom.get("guardianName"+records.getRecord(i).getId()).value;
    	    guardianRelationship=dom.get("guardianRelationship"+records.getRecord(i).getId()).value;
    	    age=dom.get("nomineeAge"+records.getRecord(i).getId()).value;
    	    disburse = dom.get("disburseType"+records.getRecord(i).getId()).value;

    		if(nomineeName=="")
    		{
    			alert("Enter Nominee Name for Last Line :"+(i+1));
    			return false;
    		}
    		if(dob=="")
    		{
    		   alert("Enter Nominee Date of birth for Last Line :"+(i+1));
    		   return false;
    		}
    		if(relationType=="0" || relationType=="")
    		{
    		   alert("Please select Relation type for Last Line :"+(i+1));
    		   return false;
    		}
    		if(martialStatus=="0" || martialStatus=="")
    		{
    		   alert("Please select Martial status for Last Line :"+(i+1));
    		   return false;
    		}
    		if(isActive=="0" || isActive=="")
    		{
    		   alert("Please select IsActive for Last Line :"+(i+1));
    		   return false;
    		}

    		if(disburse=="0" || disburse=="")
        	{
				alert("Please select the disbursement type for lint :"+(i+1));
				return false;
            }

    		 <s:iterator var="s" value="dropdownData.disburesementTypeList" status="status">
 			var temp='<s:property value="%{id}"/>';
 			if(temp==disburse)
 			{
 				disburseName='<s:property value="%{type}"/>';
 			}
 			if(trimAll(disburseName).toUpperCase()!=trimAll('DIRECTBANK'))
 			{
 				dom.get("bank"+records.getRecord(i).getId()).value="";
 				dom.get("bankBranch"+records.getRecord(i).getId()).value="";
 				dom.get("branchname"+records.getRecord(i).getId()).value="";
 				dom.get("accountNumber"+records.getRecord(i).getId()).value="";
 				
 			}	
 			</s:iterator>

    		if(disburse!="0" && trimAll(disburseName).toUpperCase()==trimAll("DIRECTBANK"))
        	{	
    			if(bank=="")
        		{
        		   alert("Please select bank for Line :"+(i+1));
        		  	return false;
        		}
	    		if(bank!="")
	    		{
	    		  if(branchName=="")
	    		  { 
	    		     alert("Please select branch for Last Line :"+(i+1));
	    		  	 return false;
	    		  }
	
	    		  if(accountNum=="")
	    		  { 
	    		     alert("Please select account number for Line :"+(i+1));
	    		  	 return false;
	    		  }
	    		  if(accountNum.length>16)
	        	  {
	    			  alert("Account number cannot be greater than 16 digits");
	            	  return false;	  
	        	  }
	    		}
        	}	

    		
    		if(isWorking=="0" || isWorking=="")
    		{ 
    		     alert("Please select IsWorking for Last Line :"+(i+1));
    		  	 return false;
    		}
    		if(guardianName=="" && age<18)
        	{
				alert("Enter Gauardian Name for the line :"+(i+1));
				return false;
           	}
    	    if(guardianRelationship=="" && age<18)
        	{
				alert("Enter Gauardian Relationship for the line :"+(i+1));
				return false;
           	}
    		
   
   return true;
}

Array.prototype.contains = function (element) 
			{
				for (var i = 0; i < this.length; i++) {
				if (this[i] == element) {
				return true;
				}
				}
				return false;
			}
function validateUniqueName(obj)
{
   var scripts = new Array();
   var enteredName = obj.value;
   var nomineeName;
   var records= nomineeDataTable.getRecordSet(); 
   var row=getRow(obj);
   var rowIndex = row.rowIndex;
    for(i=0;i<records.getLength();i++)
    	{
    	   nomineeName=dom.get("nomineeName"+records.getRecord(i).getId()).value;
    	   
    	   if(i!=(rowIndex-2))
    	   {
    	   	 scripts.push(trimAll(nomineeName.toLowerCase().replace(/\s/g,"")));
    	   }
    	}
    	
    	
    	if(scripts!='' && !scripts.isEmpty() && scripts.contains(trimAll(enteredName.toLowerCase().replace(/\s/g,""))))
						{
						    
							alert('Nominee Name should be unique');
							obj.value='';
							return false;
						}
}

    function checkValueForPreviousRow()
    {
       
    	var records= nomineeDataTable.getRecordSet();
    	var i,indexRow;
    	var nomineeName,relationType,dob,martialStatus,isActive,bank,branch,accountNum,isWorking,age,branchName,disburse,disburseName;
    	for(i=0;i<records.getLength();i++)
    	{
        	indexRow = records.getRecord(i).getId();
    	    nomineeName=dom.get("nomineeName"+indexRow).value;
    	    relationType=dom.get("relationType"+indexRow).value;
    	    dob=dom.get("nomineeDob"+indexRow).value;
    	    martialStatus=dom.get("maritalStatus"+indexRow).value;
    	    isActive=dom.get("isActive"+indexRow).value;
    	    branchName=dom.get("branchname"+indexRow).value;
    	    bank=dom.get("bank"+indexRow).value;
    	    branch=dom.get("bankBranch"+indexRow).value;
    	    accountNum=dom.get("accountNumber"+indexRow).value;
    	    isWorking=dom.get("isWorking"+indexRow).value;
    	    guardianName=dom.get("guardianName"+indexRow).value;
    	    guardianRelationship=dom.get("guardianRelationship"+indexRow).value;
			age=dom.get("nomineeAge"+indexRow).value;
			disburse = dom.get("disburseType"+indexRow).value;

    		if(nomineeName=="")
    		{
    			alert("Enter Nominee Name for Line :"+(i+1));
    			return false;
    		}
    		
    		if(dob=="")
    		{
    		   alert("Enter Nominee Date of birth for Line :"+(i+1));
    		   return false;
    		}
    		if(relationType=="0" || relationType=="")
    		{
    		   alert("Please select Relation type for Line :"+(i+1));
    		   return false;
    		}
    		if(martialStatus=="0" || martialStatus=="")
    		{
    		   alert("Please select Martial status for Line :"+(i+1));
    		   return false;
    		}
    		if(isActive=="0" || isActive=="")
    		{
    		   alert("Please select IsActive for Line :"+(i+1));
    		   return false;
    		}
    		
    		if(disburse=="0" || disburse=="")
        	{
				alert("Please select the disbursement type for lint :"+(i+1));
				return false;
            }

    		<s:iterator var="s" value="dropdownData.disburesementTypeList" status="status">
			var temp='<s:property value="%{id}"/>';
			if(temp==disburse)
			{
				disburseName='<s:property value="%{type}"/>';
			}
			</s:iterator>

			if(trimAll(disburseName).toUpperCase()==trimAll('CASH')||trimAll(disburseName).toUpperCase()==trimAll('CHEQUE'))
			{
				dom.get("bank"+records.getRecord(i).getId()).value="";
				dom.get("bankBranch"+records.getRecord(i).getId()).value="";
				dom.get("branchname"+records.getRecord(i).getId()).value="";
				dom.get("accountNumber"+records.getRecord(i).getId()).value="";
				
			}
    		
    		if(disburse!="0" && trimAll(disburseName).toUpperCase()==trimAll("DIRECTBANK"))
        	{	
    			if(bank=="")
        		{
        		   alert("Please select bank for Line :"+(i+1));
        		  	return false;
        		}
	    		if(bank!="")
	    		{
	    		  if(branchName=="")
	    		  { 
	    		     alert("Please select branch for Last Line :"+(i+1));
	    		  	 return false;
	    		  }
	
	    		  if(accountNum=="")
	    		  { 
	    		     alert("Please select account number for Line :"+(i+1));
	    		  	 return false;
	    		  }
	    		  if(accountNum.length>16)
	        	  {
	    			  alert("Account number cannot be greater than 16 digits");
	            	  return false;	  
	        	  }
	    		}
        	}	
    		if(isWorking=="0" ||isWorking=="")
    		{ 
    		     alert("Please select IsWorking for Line :"+(i+1));
    		  	 return false;
    		}
    		if(guardianName=="" && age<18)
        	{
				alert("Enter Gauardian Name for the line :"+(i+1));
				return false;
           	}
    	    if(guardianRelationship=="" && age<18)
        	{
				alert("Enter Gauardian Relationship for the line :"+(i+1));
				return false;
           	}
    		
    	}
    	return true;
    }


    
    var relationDropdownOptions=[{label:"--- Select ---", value:"0"}
    <s:if test="dropdownData.relationTypeList!=null && !dropdownData.relationTypeList.isEmpty()">
    ,
    <s:iterator var="s" value="dropdownData.relationTypeList" status="status">  
    {"label":"<s:property value="%{nomineeType}"/>" ,
    	"value":"<s:property value="%{id}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator> 
    </s:if>      
    ]
   
   ///populate Branch 
    
    
   var populateDropdownOptions = [{label:"--Select--", value:"0"},
    {"label":"Yes" ,
    	"value":"1"
    	
    },{"label":"No" ,
		"value":"2"}
   ]


    var populateDisbursementType = [{label:"---Select---",value:"0"}
   <s:if test="dropdownData.disburesementTypeList!=null && !dropdownData.disburesementTypeList.isEmpty()">,
    <s:iterator var="s" value="dropdownData.disburesementTypeList" status="status">
    	{"label":"<s:property value="%{type}"/>","value":"<s:property value="%{id}"/>"}
    	<s:if test="!#status.last">,</s:if>
    </s:iterator>
    </s:if>
    ]	
    
function createRelationIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "egpimsNomineeMaster[" + oRecord.getCount() + "]." + oColumn.getKey() + ".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}

function createIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "egpimsNomineeMaster[" + oRecord.getCount() + "]." + oColumn.getKey();
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}

function createDisbursementIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "egpimsNomineeMaster[" + oRecord.getCount() + "]." + oColumn.getKey()+".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}

function createBankBranchIDFormatter(el, oRecord, oColumn){
	var hiddenFormatter = function(el, oRecord, oColumn, oData) {
	    var value = (YAHOO.lang.isValue(oData))?oData:"";
	    var id=oColumn.getKey()+oRecord.getId();
	    var fieldName = "egpimsNomineeMaster[" + oRecord.getCount() + "]." + oColumn.getKey()+".id";
	    markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	    el.innerHTML = markup;
	}
	return hiddenFormatter;
}


var nomineeDataTable;
var IdHiddenFormatter= createRelationIDFormatter(10,10);
var IdForCheckValues=createIDFormatter(10,10);
var makeNomineeCreateDataTable = function() {
var cellEditor=new YAHOO.widget.TextboxCellEditor()
var nomineeReportColumnDefs = [
{key:"id",hidden:"true",  sortable:false, formatter:createNomineeTextBoxFormatter(21,15),resizeable:false},
{key:"nomineeName",label:'Nominee Name<span class="mandatory">*</span>', sortable:false, formatter:createNomineeTextBoxFormatterNameValidate(21,15),resizeable:false},
{key:"nomineeDob",label:'Date of Birth<span class="mandatory">*</span>', sortable:false, formatter:createNomineeTextBoxForDateFormatter(10,15), resizeable:false},
{key:"nomineeAge", label:'Age<span class="mandatory">*</span>', sortable:false, resizeable:false ,formatter:createNomineeTextBoxFormatter(2,8)},
{key:"relationType",hidden:"true",sortable:false, formatter:IdHiddenFormatter, resizeable:false},
{key:"relation",label:'Relation<span class="mandatory">*</span>', sortable:false, formatter:"dropdown", dropdownOptions:relationDropdownOptions,resizeable:false},
{key:"marital",label:'Marital Status<span class="mandatory">*</span>',sortable:false, formatter:"dropdown",dropdownOptions:populateDropdownOptions,resizeable:false},
{key:"maritalStatus",hidden:"true",sortable:false, formatter:IdForCheckValues,resizeable:false},
{key:"isActive", hidden:"true", sortable:false, formatter:IdForCheckValues,resizeable:false},
{key:"Active", label:'IsActive<span class="mandatory">*</span>', sortable:false, formatter:"dropdown",dropdownOptions:populateDropdownOptions,resizeable:false},
{key:"disburse",label:'Disbursement Type<span class="mandatory">*</span>',sortable:false, formatter:"dropdown",dropdownOptions:populateDisbursementType,resizeable:false},
{key:"disburseType",hidden:"true",sortable:false, formatter:createDisbursementIDFormatter(2,8),resizeable:false},
{key:"bank",label:'Bank', sortable:false,formatter:createNomineeTextBoxForBankAutoComplete(25,25),resizeable:true},
{key:"branchname", label:'Branch', formatter:createNomineeTextBoxForForBranchAutoComplete(25,25),resizeable:true},
{key:"bankBranch",hidden:"true",  sortable:false, formatter:createBankBranchIDFormatter(10,10),resizeable:false},
{key:"accountNumber",label:'Account number', sortable:false, formatter:createNomineeTextBoxFormatter(15,15),resizeable:false},
{key:"Working",label:'IsWorking<span class="mandatory">*</span>', sortable:false, formatter:"dropdown",dropdownOptions:populateDropdownOptions,resizeable:false},
{key:"isWorking",hidden:"true", sortable:false, formatter:IdForCheckValues,resizeable:false},
{key:"guardianName", label:'Gauardian Name',sortable:false, formatter:createNomineeTextBoxFormatter(15,15), resizeable:false},
{key:"guardianRelationship", label:'Gauardian Relationship',sortable:false, formatter:createNomineeTextBoxFormatter(15,15), resizeable:false},
{key:"nomineeAddress", label:'Nominee Address',sortable:false, formatter:createNomineeTextBoxFormatter(15,15), resizeable:false}

<s:if test="%{mode!='view'}">
,

{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
{key:'Delete',label:'Del',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
</s:if>

];

var nomineeDataSource = new YAHOO.util.DataSource();
nomineeDataTable = new YAHOO.widget.DataTable("nomineeCreateTable",nomineeReportColumnDefs, nomineeDataSource,{MSG_EMPTY:""});

nomineeDataTable.on('cellClickEvent',function (oArgs) {

			var target = oArgs.target;
			var record = this.getRecord(target);
			var records= this.getRecordSet();
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
			     if(checkValueForPreviousRow())
			     {
				  	nomineeDataTable.addRow({});
				 }
								
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){
				this.deleteRow(record);
				}
					else{
					   alert("You cann't delete this row");
					}
					
			}
			
			});


  		nomineeDataTable.on('dropdownChangeEvent', function (oArgs) {	
	    var record = this.getRecord(oArgs.target);
        var column = this.getColumn(oArgs.target);
        if(column.key=='relation'){
            var selectedIndex=oArgs.target.selectedIndex;
            this.updateCell(record,this.getColumn('relationType'),relationDropdownOptions[selectedIndex].value);
            
        }
        if(column.key=='Active'){
             var selectedIndex=oArgs.target.selectedIndex;
             this.updateCell(record,this.getColumn('isActive'),populateDropdownOptions[selectedIndex].value);
        }
        if(column.key=='marital'){
             var selectedIndex=oArgs.target.selectedIndex;
             this.updateCell(record,this.getColumn('maritalStatus'),populateDropdownOptions[selectedIndex].value);
        }
        
        if(column.key=='disburse'){
			var selectedIndex=oArgs.target.selectedIndex;
			this.updateCell(record,this.getColumn('disburseType'),populateDisbursementType[selectedIndex].value);
			<s:iterator var="s" value="dropdownData.disburesementTypeList" status="status">
			var temp='<s:property value="%{id}"/>';
			if(temp==populateDisbursementType[selectedIndex].value)
			{
				var disburseName='<s:property value="%{type}"/>';
				var tempValue="Direct Bank Transfer";
				if(trimAll(disburseName)!=trimAll(tempValue))
				{
					this.updateCell(record,this.getColumn('bank'),"");
					this.updateCell(record,this.getColumn('branchname'),"");
					this.updateCell(record,this.getColumn('bankBranch'),"");
					this.updateCell(record,this.getColumn('accountNumber'),"");
				}	
			}
			</s:iterator>
        }

        if(column.key=='Working'){
            var selectedIndex=oArgs.target.selectedIndex;
            this.updateCell(record,this.getColumn('isWorking'),populateDropdownOptions[selectedIndex].value);
       }
	});

return {
oDS: nomineeDataSource,
oDT: nomineeDataTable
};
}

function onView()
{
	<s:if test="%{mode=='view'}">
	{
		nomineeDataTable.disable();
	}
	</s:if>

}
   	    		 
  	</script>
  	<% 
			  Logger LOGGER = Logger.getLogger("nomineeCreateModify-new.jsp");
			  String mode=(String)request.getAttribute("mode");
			  LOGGER.info("INFO in jsp==="+mode); %>
  </head>
  <body onLoad="loadBank();onView();">
   <s:form action="nomineeCreateModify" theme="simple" >  
   <s:token/>
  <div class="formmainbox">
			<div class="insidecontent">
		  <div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			  <div class="rbcontent2">
			  
			   <span id="msg">
			<s:if test="%{hasErrors()}">
				<div class="errorstyle">
				<s:fielderror  cssClass="mandatoryone"/>
				<s:actionerror  cssClass="mandatoryone"/>
				</div>
			</s:if>
		</span>
			  
			  <table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
	  <tbody>
<tr><td>&nbsp;</td></tr>
  <tr>
   <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
   <p><div class="headplacer">Employee Details</div></p></td><td></td> </tr>
   </tbody>
  
  </table>
  
  <br>
  <table>
 
  <%PersonalInformation obj = (PersonalInformation)request.getAttribute("eisObj");
  LOGGER.info("obj in jsp==="+obj); %>
  <tr>
  <input type="hidden"  name="mode" class="selectwk" value=<%=mode%>>
 <input type="hidden"  name="idPersonalInformation" class="selectwk" value=<%=obj.getIdPersonalInformation()%> readonly>
  <td  class="whiteboxwk" align='center'>Employee Code:<br><br></td>
  <td class="whitebox2wk" align="center"><input type="text" name="code" class="selectwk" value=<%=obj.getCode()%> readonly /><br><br></td>
  <td  class="whiteboxwk" align='center'>Employee Name:<br><br></td>
  <td class="whitebox2wk" align="center"><input type="text"  name="code" class="selectwk" value=<%=obj.getEmployeeFirstName()%> readonly><br><br></td>
  </tr>
  
  
  <table width="100%" cellpadding ="0" cellspacing ="0" border = "0">
	  <tbody>
<tr><td>&nbsp;</td></tr>
  <tr>
   <td colspan="8" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
   <p><div class="headplacer">Nominee Master</div></p></td><td></td> </tr>
   </tbody>
  
  </table>
    <div id="codescontainer"></div>
    <div class="scroll" width="100%">
  <div class="yui-skin-sam">
			<div id="nomineeCreateTable" ></div>
		</div>
	<script>
				makeNomineeCreateDataTable();
									<s:if test="%{null==egpimsNomineeMaster || egpimsNomineeMaster.isEmpty()}">
            	         	         	 nomineeDataTable.addRow({
            	         	         	 id:'',
            	         	         	branchname:'',
		                                    nomineeName:'',		                                    
		                                    nomineeDob:'',
		                                     nomineeAge:'',
		                                     relation :'',
		                                     marital:'',	                               
		                                     Active:'',
		                                     disburse:'',
		                                     bank:'',
		                                     bankBranch:'',
		                                     accountNumber:'',
		                                     guardianName:'',
		                                     guardianRelationship:'',
		                                     nomineeAddress:'',
		                                     Working:''});
		          
                	                 </s:if>
                	                
                	                
                	                
                	                 <s:else>
                	                
                	                 <s:iterator id="egpimsNomineeMaster" value="egpimsNomineeMaster" status="row_status">
                	                 	 nomineeDataTable.addRow({
            	         	         		 id:'<s:property value="id"/>',
		                                    nomineeName:'<s:property value="nomineeName"/>',	
		                                    nomineeDob:'<s:date name="nomineeDob" format="dd/MM/yyyy"/>',
		                                     nomineeAge:'<s:property value="nomineeAge"/>',
		                                     relation :'<s:property value="relationType.id"/>',
		                                     relationType :'<s:property value="relationType.id"/>',
		                                     marital:'<s:property value="maritalStatus"/>',	 
		                                     maritalStatus:'<s:property value="maritalStatus"/>',	                               
		                                     Active:'<s:property value="isActive"/>',
		                                     isActive:'<s:property value="isActive"/>',
		                                     disburse:'<s:property value="disburseType.id"/>',
		                                     disburseType:'<s:property value="disburseType.id"/>',
		                                     bank:'<s:property value="bankBranch.bank.name"/>',
		                                     bankBranch:'<s:property value="bankBranch.id"/>',
		                                     branchname:'<s:property value="bankBranch.branchname"/>',
		                                     accountNumber:'<s:property value="accountNumber"/>',
		                                     guardianName:'<s:property value="guardianName"/>',
		                                     guardianRelationship:'<s:property value="guardianRelationship"/>',
		                                     isWorking:'<s:property value="isWorking"/>',
		                                     Working:'<s:property value="isWorking"/>',
		                                     nomineeAddress:'<s:property value="nomineeAddress"/>'});
		                                   </s:iterator>
		                                </s:else>
		                                 
                	                
                	                 
		      		
				
          </script>
          
          <br>
          <br>
	<center><s:actionmessage cssClass="mandatoryTwo"/></center>
</table>
 </div>
			<div class="rbbot2"><div></div></div>
		</div>
</div></div></div>
<s:if test="%{mode!='view'}">
  <center><s:submit method="createNominee" value="Save" cssClass="buttonfinal" onclick="return mandatoryFields();"/>
  </s:if>
  <input type="button" name="button" id="button" value="Close"  class="buttonfinal" onclick="window.close();"/></center>
</s:form>
  </body>
  </html>
  </s:push>&lt;</script><br>