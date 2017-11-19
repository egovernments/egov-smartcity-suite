<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<title>Loan Grant Master</title>
<link rel="stylesheet" href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript" src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/loanGrantHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<script type="text/javascript">
 
	</script>
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
</style>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<style type="text/css">
.yui-dt1-col-loanamount yui-dt-col-loanamount {
	overflow: hidden;
	width: 60px;
}

.yui-dt-liner {
	text-align: center;
	padding-right: 4px;
	padding-left: 4px;
}

.tabbertab {
	border: 1px solid #CCCCCC;
	height: 800px;
	margin-bottom: 8px;
	overflow: scroll;
}
</style>
</head>
<body>
	<span class="mandatory"> <s:actionerror /> <s:fielderror />
	</span>
	<s:form name="loanGrantMasterForm" action="loanGrant" theme="simple">
		<s:push value="model">
			<script>
   function checkuniquenesscode(){
    	document.getElementById('codeuniquecode').style.display ='none';
		var subSchemeId=document.getElementById('subSchemeId').value;
		var id=document.getElementById('id').value;
		populatecodeuniquecode({subSchemeId:subSchemeId,id:id});
    }
function createFundingAgencyDropDownFormatter(tableType,prefix,suffix){
	return function(el, oRecord, oColumn, oData) {
	var index=getIndexForTableType(tableType);
		var value = (YAHOO.lang.isValue(oData))?oData:"";
		var element=" <select  id='"+prefix+"["+index+"]"+suffix+"' name='"+prefix+"["+index+"]"+suffix+"'  >";
		element=element+"<option value=-1 > --- Choose --- </option>  ";
		<s:iterator value="fundingAgencyList" status="stat">
			var name='<s:property value="name"/>';
			var id='<s:property value="id" />';
			element=element+" <option value="+id +" > "+ name+" </option>  ";
		</s:iterator>
		element=element+" </select>";
		el.innerHTML =element ;
	}
}

function createDateFieldFormatter(tableType,prefix,suffix)
{
	return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
	var index=getIndexForTableType(tableType);
	var fieldName = prefix+"[" + index + "]" +  suffix;
	var idt=oColumn.getKey()+oRecord.getId();
	var id=idt.replace("-","");
	var CALENDERURL="/egi/resources/erp2/images/calendaricon.gif";
	var HREF='javascript:show_calendar("forms[0].'+id+'")';
	markup="<input type='text' id='"+id+"' name='"+fieldName+"' value='"+value+"'    maxlength='10' style=\"width:70px\" onkeyup='DateFormat(this,this.value,event,false,3);' onblur='checkDateLG(this);' /><a href='#' style='text-decoration:none' onclick='"+HREF+"'><img src='"+CALENDERURL+"' border='0'  /></a>";
	 el.innerHTML = markup;
	}
}
var reciptAccountId;
var makeProjectDetailTable = function() {
	var projectDetailColumns = [ 
		{key:"SlNo",label:'Sl No',width:30},
		{key:"projectcodeid",width:90,hidden:true, formatter:createTextFieldFormatterLG('projectcode',PROJECTCODELIST,".id","hidden")},
		{key:"projectcode",label:'Project Code'+'<span class="mandatory">*</span>',width:160, formatter:createTextFieldFormatterForProjectCode(PROJECTCODELIST,".code")},
		{key:"projectcodename",label:'Project Code Name'+'<span class="mandatory">*</span>',width:530, formatter:createTextFieldFormatterForProjectName(PROJECTCODELIST,".name")},
		{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
		{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
	];
    var projectCodeDetailDS = new YAHOO.util.DataSource(); 
	projectCodeDetailsTable = new YAHOO.widget.DataTable("projectCodeTable",projectDetailColumns, projectCodeDetailDS);
	projectCodeDetailsTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			projectCodeDetailsTable.addRow({SlNo:projectCodeDetailsTable.getRecordSet().getLength()+1});
			updateProjectCodeTableIndex();
		}
		if (column.key == 'Delete') { 	
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(var i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			}
			else{
				bootbox.alert("This row can not be deleted");
			}
		}
	});
	<s:iterator value="projectCodeList" status="stat">
	projectCodeDetailsTable.addRow({SlNo:projectCodeDetailsTable.getRecordSet().getLength()+1,
		"projectcodeid":'<s:property value="id"/>',
		"projectcode":'<s:property value="code"/>',
		"projectcodename":'<s:property value="name" escape="false" />'
	});
	var index = '<s:property value="#stat.index"/>';
	updateGridNames(PROJECTCODELIST,'id',index,'<s:property value="id"/>');
	updateGridNames(PROJECTCODELIST,'code',index,'<s:property value="code"/>');
	updateGridNames(PROJECTCODELIST,'name',index,'<s:property value="name" escape="false" />');
	updateProjectCodeTableIndex();
	</s:iterator>
}
var makeSanctionedAmountTable = function() {
		var amountDetailColumns = [ 
                    		{key:"SlNo",label:'Sl<br />No',width:20},
                       		{key:"fundingagency",label:'Funding Agency'+'<span class="mandatory">*</span>',width:110, formatter:createFundingAgencyDropDownFormatter('sanctioned',SANCTIONEDAMOUNTLIST,".fundingAgency.id","text")},
                    		{key:"loanamount",label:'Loan Amount<br />(A)(In Lacs)',width:85, formatter:createTextFieldLGAmount('sanctioned',SANCTIONEDAMOUNTLIST,".loanAmount","validateDigitsAndDecimal(this);validateLGAmount('sanctioned',this,'"+SANCTIONEDAMOUNTLIST+"');updateTotalAmount('sanctioned','loan')")},
                    		{key:"grantamount",label:'Grant Amount<br />(B)(In Lacs)',width:85, formatter:createTextFieldLGAmount('sanctioned',SANCTIONEDAMOUNTLIST,".grantAmount","validateDigitsAndDecimal(this);validateLGAmount('sanctioned',this,'"+SANCTIONEDAMOUNTLIST+"');updateTotalAmount('sanctioned','grant')")},
                    		{key:"percentageofcost",label:'Percentage<br />Contributed<br />Against<br />Project<br />Cost',width:60, formatter:createPercentageFieldFormatter('sanctioned',SANCTIONEDAMOUNTLIST,".percentage")},
                    		{key:"agencyschemeno",label:'Agency Scheme<br /> Number',width:85, formatter:createTextFieldFormatterWithStyle('sanctioned',SANCTIONEDAMOUNTLIST,".agencySchemeNo","width:85px")},
                    		{key:"councilreno",label:'Council Resolution<br /> Number & Date',width:100, formatter:createTextFieldFormatterWithStyle('sanctioned',SANCTIONEDAMOUNTLIST,".councilResNo","width:100px")},
                    		{key:"loansanctiono",label:'Loan Sanction Letter <br /> Number & Date',width:115, formatter:createTextFieldFormatterWithStyle('sanctioned',SANCTIONEDAMOUNTLIST,".loanSanctionNo","width:100px")},
                    		{key:"agreementdate",label:'Agreement Date<br />(dd/mm/yyyy)',width:100, formatter:createDateFieldFormatter('sanctioned',SANCTIONEDAMOUNTLIST,".agreementDate")},
                    		{key:"commissionerorderno",label:'Commissioner <br /> Order Number',width:80, formatter:createTextFieldFormatterWithStyle('sanctioned',SANCTIONEDAMOUNTLIST,".commOrderNo","width:70px")},
                    		{key:"repaymentschedule",label:'Attach',width:70, formatter:createDocUploadFormatterLGSanctioned('sanctioned',SANCTIONEDAMOUNTLIST,".docIdButton")},
                    		{key:"id",label:'id',hidden:true, formatter:createTextFieldFormatterLG('sanctioned',SANCTIONEDAMOUNTLIST,".id","hidden")},
                    	    {key:"fundingBy",label:'fundingBy',hidden:true, formatter:createTextFieldFormatterWithValue('sanctioned',SANCTIONEDAMOUNTLIST,".patternType","hidden",'sanctioned')},	
                              {key:"documentNumber",label:'documentNumber',hidden:true, formatter:createTextFieldFormatterLG('sanctioned',SANCTIONEDAMOUNTLIST,".docId","hidden")},
                    		{key:'Add',label:'Add',width:13,formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
                    		{key:'Delete',label:'Del',width:13,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
                    	];
	
    var sanctionedAmountDS = new YAHOO.util.DataSource(); 
	sanctionedAmountDT = new YAHOO.widget.DataTable("sanctionedAmountTable",amountDetailColumns, sanctionedAmountDS);
	sanctionedAmountDT.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			sanctionedAmountDT.addRow({SlNo:sanctionedAmountDT.getRecordSet().getLength()+1});
			updateSanctionedAmountTableIndex();
		}
		if (column.key == 'Delete') { 	
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(var i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
				updateTotalAmount('sanctioned','loan');
				updateTotalAmount('sanctioned','grant');
			}
			else{
				bootbox.alert("This row can not be deleted");
			}
		}
	});
	var index=0;
	<s:iterator var="dl" value="detailList" status="stat">
	<s:if test="%{patternType=='sanctioned'}">
		sanctionedAmountDT.addRow({SlNo:sanctionedAmountDT.getRecordSet().getLength()+1,
				"id":'<s:property value="id"/>',
				"fundingBy":'<s:property value="patternType"/>',
				"fundingagency":'<s:property value="fundingAgency.id"/>',
				"loanamount":'<s:property value="loanAmount"/>',
				"grantamount":'<s:property value="grantAmount"/>',
				"percentageofcost":'<s:property value="percentage"/>',
				"agencyschemeno":'<s:property value="agencySchemeNo"/>',
				"councilreno":'<s:property value="councilResNo"/>',
				"loansanctiono":'<s:property value="loanSanctionNo"/>',
				"agreementdate":'<s:date  name="agreementDate" format="dd/MM/yyyy" />',
				"commissionerorderno":'<s:property value="commOrderNo"/>',
				"documentNumber":'<s:property value="docId"/>'
			});
			updateGridNames(SANCTIONEDAMOUNTLIST,'id',index,'<s:property value="id"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'patternType',index,'<s:property value="patternType"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'fundingAgency.id',index,'<s:property value="fundingAgency.id"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'loanAmount',index,'<s:property value="loanAmount"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'grantAmount',index,'<s:property value="grantAmount"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'percentage',index,'<s:property value="percentage"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'agencySchemeNo',index,'<s:property value="agencySchemeNo"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'councilResNo',index,'<s:property value="councilResNo"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'loanSanctionNo',index,'<s:property value="loanSanctionNo"/>');
			updateGridNames(SANCTIONEDAMOUNTLIST,'agreementDate',index,'<s:date  name="agreementDate" format="dd/MM/yyyy" />');
			updateGridNames(SANCTIONEDAMOUNTLIST,'commOrderNo',index,'<s:property value="commOrderNo"/>'); 
			updateGridNames(SANCTIONEDAMOUNTLIST,'docId',index,'<s:property value="docId"/>');  
			var temp=parseFloat('<s:property value="loanAmount"/>');
			if(!isNaN(temp))
				totalsanctionedloan = totalsanctionedloan+temp;
			temp=parseFloat('<s:property value="grantAmount"/>');
			if(!isNaN(temp))
				totalsanctionedgrant = totalsanctionedgrant+temp;
			updateSanctionedAmountTableIndex();	
			index++;
			</s:if>
		</s:iterator>
		var tfoot = sanctionedAmountDT.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 2;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalsanctionedloan' name='totalsanctionedloan' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalsanctionedgrant' name='totalsanctionedgrant' readonly='true' tabindex='-1'/>";
		th = tr.appendChild(document.createElement('th'));
		th.colSpan = 1;
		th.innerHTML = 'Total(A+B)&nbsp;&nbsp;';
		th.align='right';
		td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalsanctioned' name='totalsanctioned' readonly='true' tabindex='-1'/>";
		document.getElementById('totalsanctionedloan').value=totalsanctionedloan;
		document.getElementById('totalsanctionedgrant').value=totalsanctionedgrant;
		document.getElementById('totalsanctioned').value=totalsanctionedgrant+totalsanctionedloan;
}
var makeUnsanctionedAmountTable = function() {
		var amountDetailColumns = [ 
                    		{key:"SlNo",label:'Sl<br />No',width:20},
                    		{key:"fundingagency",label:'Funding Agency'+'<span class="mandatory">*</span>',width:110, formatter:createFundingAgencyDropDownFormatter('unsanctioned',UNSANCTIONEDAMOUNTLIST,".fundingAgency.id","text")},
                    		{key:"loanamount",label:'Loan Amount<br />(A)(In Lacs)',width:85, formatter:createTextFieldLGAmount('unsanctioned',UNSANCTIONEDAMOUNTLIST,".loanAmount","validateDigitsAndDecimal(this);validateLGAmount('unsanctioned',this,'"+UNSANCTIONEDAMOUNTLIST+"');updateTotalAmount('unsanctioned','loan')")},
                    		{key:"grantamount",label:'Grant Amount<br />(B)(In Lacs)',width:85, formatter:createTextFieldLGAmount('unsanctioned',UNSANCTIONEDAMOUNTLIST,".grantAmount","validateDigitsAndDecimal(this);validateLGAmount('unsanctioned',this,'"+UNSANCTIONEDAMOUNTLIST+"');updateTotalAmount('unsanctioned','grant')")},
                    		{key:"percentageofcost",label:'Percentage<br />Contributed<br />Against<br />Project<br />Cost',width:60, formatter:createPercentageFieldFormatter('unsanctioned',UNSANCTIONEDAMOUNTLIST,".percentage")},
                    		{key:"agencyschemeno",label:'Agency Scheme<br /> Number',width:85, formatter:createTextFieldFormatterWithStyle('unsanctioned',UNSANCTIONEDAMOUNTLIST,".agencySchemeNo","width:85px")},
                    		{key:"councilreno",label:'Council Resolution<br /> Number & Date',width:100, formatter:createTextFieldFormatterWithStyle('unsanctioned',UNSANCTIONEDAMOUNTLIST,".councilResNo","width:100px")},
                    		{key:"loansanctiono",label:'Loan Sanction Letter <br /> Number & Date',width:115, formatter:createTextFieldFormatterWithStyle('unsanctioned',UNSANCTIONEDAMOUNTLIST,".loanSanctionNo","width:100px")},
                    		{key:"agreementdate",label:'Agreement Date<br />(dd/mm/yyyy)',width:100, formatter:createDateFieldFormatter('unsanctioned',UNSANCTIONEDAMOUNTLIST,".agreementDate")},
                    		{key:"commissionerorderno",label:'Commissioner <br /> Order Number',width:80, formatter:createTextFieldFormatterWithStyle('unsanctioned',UNSANCTIONEDAMOUNTLIST,".commOrderNo","width:70px")},
                    		{key:"repaymentschedule",label:'Attach',width:70, formatter:createDocUploadFormatterLGUnsanctioned('unsanctioned',UNSANCTIONEDAMOUNTLIST,".docIdButton")},
                    		{key:"id",label:'id',hidden:true, formatter:createTextFieldFormatterLG('unsanctioned',UNSANCTIONEDAMOUNTLIST,".id","hidden")},
                            {key:"fundingBy",label:'fundingBy',hidden:true, formatter:createTextFieldFormatterWithValue('unsanctioned',UNSANCTIONEDAMOUNTLIST,".patternType","hidden",'unsanctioned')},
                    		{key:"documentNumber",label:'documentNumber',hidden:true, formatter:createTextFieldFormatterLG('unsanctioned',UNSANCTIONEDAMOUNTLIST,".docId","hidden")},
                    		{key:'Add',label:'Add',width:13,formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
                    		{key:'Delete',label:'Del',width:13,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
                    	];
	
    var unsanctionedAmountDS = new YAHOO.util.DataSource(); 
	unsanctionedAmountDT = new YAHOO.widget.DataTable("unsanctionedAmountTable",amountDetailColumns, unsanctionedAmountDS);
	unsanctionedAmountDT.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			unsanctionedAmountDT.addRow({SlNo:unsanctionedAmountDT.getRecordSet().getLength()+1});
			updateUnsanctionedAmountTableIndex();
		}
		if (column.key == 'Delete') { 	
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(var i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
				updateTotalAmount('unsanctioned','loan');
				updateTotalAmount('unsanctioned','grant');
			}
			else{
				bootbox.alert("This row can not be deleted");
			}
		}
	});
	var index=0;
	<s:iterator value="detailList" status="stat">
	<s:if test="%{patternType=='unsanctioned'}">
			unsanctionedAmountDT.addRow({SlNo:unsanctionedAmountDT.getRecordSet().getLength()+1,
				"id":'<s:property value="id"/>',
				"fundingBy":'<s:property value="patternType"/>',
				"fundingagency":'<s:property value="fundingAgency.id"/>',
				"loanamount":'<s:property value="loanAmount"/>',
				"grantamount":'<s:property value="grantAmount"/>',
				"percentageofcost":'<s:property value="percentage"/>',
				"agencyschemeno":'<s:property value="agencySchemeNo"/>',
				"councilreno":'<s:property value="councilResNo"/>',
				"loansanctiono":'<s:property value="loanSanctionNo"/>',
				"agreementdate":'<s:date  name="agreementDate" format="dd/MM/yyyy" />',
				"commissionerorderno":'<s:property value="commOrderNo"/>',
				"documentNumber":'<s:property value="docId"/>'
			});
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'id',index,'<s:property value="id"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'patternType',index,'<s:property value="patternType"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'fundingAgency.id',index,'<s:property value="fundingAgency.id"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'loanAmount',index,'<s:property value="loanAmount"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'grantAmount',index,'<s:property value="grantAmount"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'percentage',index,'<s:property value="percentage"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'agencySchemeNo',index,'<s:property value="agencySchemeNo"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'councilResNo',index,'<s:property value="councilResNo"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'loanSanctionNo',index,'<s:property value="loanSanctionNo"/>');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'agreementDate',index,'<s:date  name="agreementDate" format="dd/MM/yyyy" />');
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'commOrderNo',index,'<s:property value="commOrderNo"/>'); 
			updateGridNames(UNSANCTIONEDAMOUNTLIST,'docId',index,'<s:property value="docId"/>');  
			var temp=parseFloat('<s:property value="loanAmount"/>');
			if(!isNaN(temp))
				totalunsanctionedloan = totalunsanctionedloan+temp;
			temp=parseFloat('<s:property value="grantAmount"/>');
			if(!isNaN(temp))
				totalunsanctionedgrant = totalunsanctionedgrant+temp;
			updateUnsanctionedAmountTableIndex();
			index++;
		</s:if>	
		</s:iterator>
		var tfoot = unsanctionedAmountDT.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 2;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalunsanctionedloan' name='totalunsanctionedloan' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalunsanctionedgrant' name='totalunsanctionedgrant' readonly='true' tabindex='-1'/>";
		th = tr.appendChild(document.createElement('th'));
		th.colSpan = 1;
		th.innerHTML = 'Total(A+B)&nbsp;&nbsp;';
		th.align='right';
		td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalunsanctioned' name='totalunsanctioned' readonly='true' tabindex='-1'/>";
		document.getElementById('totalunsanctionedloan').value=totalunsanctionedloan;
		document.getElementById('totalunsanctionedgrant').value=totalunsanctionedgrant;
		document.getElementById('totalunsanctioned').value=totalunsanctionedgrant+totalunsanctionedloan;
}
var makeRevisedAmountTable = function() {
		var amountDetailColumns = [ 
                    		{key:"SlNo",label:'Sl<br />No',width:20},
                    		{key:"fundingagency",label:'Funding Agency'+'<span class="mandatory">*</span>',width:110, formatter:createFundingAgencyDropDownFormatter('revised',REVISEDAMOUNTLIST,".fundingAgency.id","text")},
                    		{key:"loanamount",label:'Loan Amount<br />(A)(In Lacs)',width:85, formatter:createTextFieldLGAmount('revised',REVISEDAMOUNTLIST,".loanAmount","validateDigitsAndDecimal(this);validateLGAmount('revised',this,'"+REVISEDAMOUNTLIST+"');updateTotalAmount('revised','loan')")},
                    		{key:"grantamount",label:'Grant Amount<br />(B)(In Lacs)',width:85, formatter:createTextFieldLGAmount('revised',REVISEDAMOUNTLIST,".grantAmount","validateDigitsAndDecimal(this);validateLGAmount('revised',this,'"+REVISEDAMOUNTLIST+"');updateTotalAmount('revised','grant')")},
                    		{key:"percentageofcost",label:'Percentage<br />Contributed<br />Against<br />Project<br />Cost',width:60, formatter:createPercentageFieldFormatter('revised',REVISEDAMOUNTLIST,".percentage")},
                    		{key:"agencyschemeno",label:'Agency Scheme<br /> Number',width:85, formatter:createTextFieldFormatterWithStyle('revised',REVISEDAMOUNTLIST,".agencySchemeNo","width:85px")},
                    		{key:"councilreno",label:'Council Resolution<br /> Number & Date',width:100, formatter:createTextFieldFormatterWithStyle('revised',REVISEDAMOUNTLIST,".councilResNo","width:100px")},
                    		{key:"loansanctiono",label:'Loan Sanction Letter <br /> Number & Date',width:115, formatter:createTextFieldFormatterWithStyle('revised',REVISEDAMOUNTLIST,".loanSanctionNo","width:100px")},
                    		{key:"agreementdate",label:'Agreement Date<br />(dd/mm/yyyy)',width:100, formatter:createDateFieldFormatter('revised',REVISEDAMOUNTLIST,".agreementDate")},
                    		{key:"commissionerorderno",label:'Commissioner <br /> Order Number',width:80, formatter:createTextFieldFormatterWithStyle('revised',REVISEDAMOUNTLIST,".commOrderNo","width:70px")},
                    		{key:"repaymentschedule",label:'Attach',width:70, formatter:createDocUploadFormatterLGRevised('revised',REVISEDAMOUNTLIST,".docIdButton")},
                 		    {key:"id",label:'id',hidden:true, formatter:createTextFieldFormatterLG('revised',REVISEDAMOUNTLIST,".id","hidden")},
                            {key:"fundingBy",label:'fundingBy',hidden:true, formatter:createTextFieldFormatterWithValue('revised',REVISEDAMOUNTLIST,".patternType","hidden",'revised')},
                    		{key:"documentNumber",label:'documentNumber',hidden:true, formatter:createTextFieldFormatterLG('revised',REVISEDAMOUNTLIST,".docId","hidden")},
                    		{key:'Add',label:'Add',width:13,formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
                    		{key:'Delete',label:'Del',width:13,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
                    	];
	
    var revisedAmountDS = new YAHOO.util.DataSource(); 
	revisedAmountDT = new YAHOO.widget.DataTable("revisedAmountTable",amountDetailColumns, revisedAmountDS);
	revisedAmountDT.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			revisedAmountDT.addRow({SlNo:revisedAmountDT.getRecordSet().getLength()+1});
			updateRevisedAmountTableIndex();
		}
		if (column.key == 'Delete') { 	
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(var i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
				updateTotalAmount('revised','loan');
				updateTotalAmount('revised','grant');
			}
			else{
				bootbox.alert("This row can not be deleted");
			}
		}
	});
	var index=0;
<s:iterator value="detailList" status="stat">
	<s:if test="%{patternType=='revised'}">
			revisedAmountDT.addRow({SlNo:revisedAmountDT.getRecordSet().getLength()+1,
				"id":'<s:property value="id"/>',
				"fundingBy":'<s:property value="patternType"/>',
				"fundingagency":'<s:property value="fundingAgency.id"/>',
				"loanamount":'<s:property value="loanAmount"/>',
				"grantamount":'<s:property value="grantAmount"/>',
				"percentageofcost":'<s:property value="percentage"/>',
				"agencyschemeno":'<s:property value="agencySchemeNo"/>',
				"councilreno":'<s:property value="councilResNo"/>',
				"loansanctiono":'<s:property value="loanSanctionNo"/>',
				"agreementdate":'<s:date  name="agreementDate" format="dd/MM/yyyy" />',
				"commissionerorderno":'<s:property value="commOrderNo"/>',
				"documentNumber":'<s:property value="docId"/>'
			});
			updateGridNames(REVISEDAMOUNTLIST,'id',index,'<s:property value="id"/>');
			updateGridNames(REVISEDAMOUNTLIST,'patternType',index,'<s:property value="patternType"/>');
			updateGridNames(REVISEDAMOUNTLIST,'fundingAgency.id',index,'<s:property value="fundingAgency.id"/>');
			updateGridNames(REVISEDAMOUNTLIST,'loanAmount',index,'<s:property value="loanAmount"/>');
			updateGridNames(REVISEDAMOUNTLIST,'grantAmount',index,'<s:property value="grantAmount"/>');
			updateGridNames(REVISEDAMOUNTLIST,'percentage',index,'<s:property value="percentage"/>');
			updateGridNames(REVISEDAMOUNTLIST,'agencySchemeNo',index,'<s:property value="agencySchemeNo"/>');
			updateGridNames(REVISEDAMOUNTLIST,'councilResNo',index,'<s:property value="councilResNo"/>');
			updateGridNames(REVISEDAMOUNTLIST,'loanSanctionNo',index,'<s:property value="loanSanctionNo"/>');
			updateGridNames(REVISEDAMOUNTLIST,'agreementDate',index,'<s:date  name="agreementDate" format="dd/MM/yyyy" />');
			updateGridNames(REVISEDAMOUNTLIST,'commOrderNo',index,'<s:property value="commOrderNo"/>'); 
			updateGridNames(REVISEDAMOUNTLIST,'docId',index,'<s:property value="docId"/>');  
			var temp=parseFloat('<s:property value="loanAmount"/>');
			if(!isNaN(temp))
				totalrevisedloan = totalrevisedloan+temp;
			temp=parseFloat('<s:property value="grantAmount"/>');
			if(!isNaN(temp))
				totalrevisedgrant = totalrevisedgrant+temp;
			updateRevisedAmountTableIndex();	
			index++;
			</s:if>
		</s:iterator>
		var tfoot = revisedAmountDT.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 2;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalrevisedloan' name='totalrevisedloan' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalrevisedgrant' name='totalrevisedgrant' readonly='true' tabindex='-1'/>";
		th = tr.appendChild(document.createElement('th'));
		th.colSpan = 1;
		th.innerHTML = 'Total(A+B)&nbsp;&nbsp;';
		th.align='right';
		td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalrevised' name='totalrevised' readonly='true' tabindex='-1'/>";
		document.getElementById('totalrevisedloan').value=totalrevisedloan;
		document.getElementById('totalrevisedgrant').value=totalrevisedgrant;
		document.getElementById('totalrevised').value=totalrevisedgrant+totalrevisedloan;
}
var makeReceiptTable = function() {
		var receiptColumns = [ 
                    		{key:"SlNo",label:'Sl No',width:20},
                    		{key:"bankbranch",label:'Bank Branch',width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".bankBranch","text")},
                    		{key:"banckaccountid",label:'Bank Account Id',hidden:true,width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".bankaccount.id","hidden")},
                    		{key:"banckaccountno",label:'Account Number',width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".bankaccount.accountnumber","text")},
      		                {key:"amount",label:'Amount<br />(In Rs.)',width:100, formatter:createTextFieldLGAmount('receipt',RECEIPTLIST,".amount","validateDigitsAndDecimal(this);updateReceiptTotalAmount();")},
                    		{key:"id",label:'id',width:100,hidden:true, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".id","hidden")},
                    		{key:"description",label:'Description',width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".description","text")},
                    		{key:"vhid",label:'vhid',width:100,hidden:true, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".voucherHeader.id","hidden")},
                    		{key:"linkrefno",label:'Link Ref No.',width:100, formatter:createTextFieldFormatterImg('receipt',RECEIPTLIST,".voucherHeader.voucherNumber","text")},
                    		{key:"receiptchequeid",label:'Instrument Id',hidden:true,width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".instrumentHeader.id","hidden")},
                    		{key:"receiptchequeno",label:'Instrument No.',width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".instrumentHeader.instrumentNumber","text")},
                    		{key:"receiptchequedate",label:'Instrument Date',width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".instrumentHeader.instrumentDate","text")},
                    		{key:"fundingagency",label:'Funding Agency',hidden:true,width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".fundingAgency.id","hidden")},
                    		{key:"fundingagencyname",label:'Funding Agency',width:100, formatter:createTextFieldFormatter('receipt',RECEIPTLIST,".fundingAgency.name","text")},
                    		{key:'Add',label:'Add',width:13,formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
                    		{key:'Delete',label:'Del',width:13,formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
                    	];
	
    var receiptDS = new YAHOO.util.DataSource(); 
	receiptDT = new YAHOO.widget.DataTable("receiptTable",receiptColumns, receiptDS);
	receiptDT.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		if (column.key == 'Add') { 
			receiptDT.addRow({SlNo:receiptDT.getRecordSet().getLength()+1});
			receiptTableIndex=receiptTableIndex+1;
		}
		if (column.key == 'Delete') { 	
			if(this.getRecordSet().getLength()>1){			
				this.deleteRow(record);
				allRecords=this.getRecordSet();
				for(var i=0;i<allRecords.getLength();i++){
					this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
				}
			}
			else{
				bootbox.alert("This row can not be deleted");
			}
		}
	});
	<s:iterator value="receiptList" status="stat">
			receiptDT.addRow({SlNo:receiptDT.getRecordSet().getLength()+1,
				"id":'<s:property value="id"/>',
				"amount":'<s:property value="amount"/>',
				"linkrefno":'<s:property value="voucherHeader.voucherNumber"/>',
				"bankbranch":'<s:property value="bankaccount.bankbranch.branchname"/>-<s:property value="bankaccount.bankbranch.bank.name"/>',
				"banckaccountid":'<s:property value="bankaccount.id"/>',
				"banckaccountno":'<s:property value="bankaccount.accountnumber"/>',
				"description":'<s:property value="description"/>',
				"vhid":'<s:property value="voucherHeader.id"/>',
				"receiptchequeid":'<s:property value="instrumentHeader.id"/>',
				"receiptchequedate":'<s:property value="instrumentHeader.instrumentDate"/>',
				"receiptchequeno":'<s:property value="instrumentHeader.instrumentNumber"/>',
				"fundingagency":'<s:property value="fundingAgency.id"/>',
				"fundingagencyname":'<s:property value="fundingAgency.name"/>'
			});
			var index = '<s:property value="#stat.index"/>';
			updateGridNames(RECEIPTLIST,'id',index,'<s:property value="id"/>');
			updateGridNames(RECEIPTLIST,'amount',index,'<s:property value="amount"/>');
			updateGridNames(RECEIPTLIST,'description',index,'<s:property value="description"/>');
			updateGridNames(RECEIPTLIST,'voucherHeader.voucherNumber',index,'<s:property value="voucherHeader.voucherNumber"/>');
			updateGridNames(RECEIPTLIST,'voucherHeader.id',index,'<s:property value="voucherHeader.id"/>');
			updateGridNames(RECEIPTLIST,'instrumentHeader.id',index,'<s:property value="instrumentHeader.id"/>');
			updateGridNames(RECEIPTLIST,'instrumentHeader.instrumentNumber',index,'<s:property value="instrumentHeader.instrumentNumber"/>');
			updateGridNames(RECEIPTLIST,'instrumentHeader.instrumentDate',index,'<s:property value="instrumentHeader.instrumentDate"/>');
			updateGridNames(RECEIPTLIST,'fundingAgency.id',index,'<s:property value="fundingAgency.id"/>');
			updateGridNames(RECEIPTLIST,'fundingAgency.name',index,'<s:property value="fundingAgency.name"/>');
			updateGridNames(RECEIPTLIST,'bankaccount.id',index,'<s:property value="bankaccount.id"/>');
			updateGridNames(RECEIPTLIST,'bankBranch',index,'<s:property value="bankaccount.bankbranch.branchname"/>-<s:property value="bankaccount.bankbranch.bank.name"/>');
			updateGridNames(RECEIPTLIST,'bankaccount.accountnumber',index,'<s:property value="bankaccount.accountnumber"/>');
			var temp=parseFloat('<s:property value="amount"/>');
			if(!isNaN(temp))
				totalreceiptamount = totalreceiptamount+temp;
			receiptTableIndex=receiptTableIndex+1;
		</s:iterator>
		var tfoot = receiptDT.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 4;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalreceiptamount' name='totalreceiptamount' readonly='true' tabindex='-1'/>";
		document.getElementById('totalreceiptamount').value=totalreceiptamount; 
}
  </script>
			<div align="left">
				<div class="tabber">
					<div class="tabbertab" id="loantab">
						<h2>Loans</h2>
						<span>
							<div class="formmainbox">
								<div class="subheadnew">Loan Header Register</div>
							</div>
							<div class="mandatory" align="center" style="display: none"
								id="codeuniquecode">
								<s:text name="loangrant.subscheme.already.exists" />
							</div>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<jsp:include page="../report/loangrant/lgcommon.jsp" />
								<tr>
									</s:push>
									<td class="bluebox">Bank<span class="mandatory">*</span></td>
									<td class="bluebox"><egov:ajaxdropdown id="bank_branch"
											fields="['Text','Value']" dropdownId="bank_branch"
											url="voucher/common!ajaxLoadBankBranch.action" /> <s:select
											name="bank_branch" id="bank_branch"
											listValue="%{bank.name+'-'+branchname}" listKey="id"
											list="dropdownData.bankbranchList" headerKey="-1"
											headerValue="----Choose----" onchange="loadBankAccount(this)"
											value="%{bank_branch}" /></td>
									<td class="bluebox">Bank Account<span class="mandatory">*</span></td>
									<td class="bluebox"><egov:ajaxdropdown id="bankaccount"
											fields="['Text','Value']" dropdownId="bankaccount"
											url="voucher/common!ajaxLoadBankAccounts.action" /> <s:select
											name="bankaccount" id="bankaccount"
											list="dropdownData.bankaccountList" listKey="id"
											listValue="chartofaccounts.glcode+'--'+accountnumber+'---'+accounttype"
											onchange="checkIfFundIsSelected()" headerKey="-1"
											headerValue="----Choose----" value="%{bankaccount}" /> <s:push
											value="model"></td>
								</tr>
								<tr>
									<td class="greybox"><s:text
											name="masters.loangrant.councilresolutionnumber" /><span
										class="mandatory">*</span></td>
									<td class="greybox"><s:textfield id="councilResNo"
											name="councilResNo" /></td>
									<td class="greybox"><s:text
											name="masters.loangrant.councilresolutiondate" /><span
										class="mandatory">*</span></td>
									<td class="greybox"><s:date var="councilResDateId"
											name="councilResDate" format='dd/MM/yyyy' /> <s:textfield
											name="councilResDate" id="councilResDate"
											onkeyup="DateFormat(this,this.value,event,false,'3');"
											onblur="checkDateLG(this);" value="%{councilResDateId}" /> <a
										tabindex="-1"
										href="javascript:show_calendar('forms[0].councilResDate');"
										style="text-decoration: none">&nbsp;<img
											src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>
									</td>
								</tr>
								<tr>
									<td class="bluebox"><s:text
											name="masters.loangrant.governmentordernumber" /><span
										class="mandatory">*</span></td>
									<td class="bluebox"><s:textfield id="govtOrderNo"
											name="govtOrderNo" /></td>
									<td class="bluebox"><s:text
											name="masters.loangrant.governmentorderdate" /><span
										class="mandatory">*</span></td>
									<td class="bluebox"><s:date var="govtOrderDateId"
											name="govtOrderDate" format='dd/MM/yyyy' /> <s:textfield
											name="govtOrderDate" id="govtOrderDate"
											onkeyup="DateFormat(this,this.value,event,false,'3');"
											onblur="checkDateLG(this);" value="%{govtOrderDateId}" /> <a
										tabindex="-1"
										href="javascript:show_calendar('forms[0].govtOrderDate');"
										style="text-decoration: none">&nbsp;<img
											src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>
									</td>
								</tr>
								<tr>
									<td class="greybox"><s:text
											name="masters.loangrant.amendmentnumber" /><span
										class="mandatory">*</span></td>
									<td class="greybox"><s:textfield id="amendmentNo"
											name="amendmentNo" /></td>
									<td class="greybox"><s:text
											name="masters.loangrant.amendmentdate" /><span
										class="mandatory">*</span></td>
									<td class="greybox"><s:date var="amendmentDateId"
											name="amendmentDate" format='dd/MM/yyyy' /> <s:textfield
											name="amendmentDate" id="amendmentDate"
											onkeyup="DateFormat(this,this.value,event,false,'3');"
											onblur="checkDateLG(this);" value="%{amendmentDateId}" /> <a
										tabindex="-1"
										href="javascript:show_calendar('forms[0].amendmentDate');"
										style="text-decoration: none">&nbsp;<img
											src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></A>
									</td>
								</tr>
							</table>
							<div style="background-color: #FFFFFF; height: 24px">&nbsp</div>
							<div class="yui-skin-sam" align="center">
								<div id="projectCodeTable"></div>
							</div> <script>
		    makeProjectDetailTable();
			document.getElementById('projectCodeTable').getElementsByTagName('table')[0].width="80%";
		 </script>
							<div style="background-color: #F7F7F7; height: 24px">&nbsp</div>
							<table>
								<tr>
									<td colspan="4">
										<table>
											<td class="bluebox"><s:text
													name="masters.loangrant.projectcost" /><span
												class="mandatory">*</span></td>
											<td class="bluebox"><s:textfield id="projectCost"
													name="projectCost" style='text-align:right;' maxlength="20"
													onblur='validateDigitsAndDecimal(this);calculateAllPercentages();updateAllTotalAmounts()' /></td>
											<td class="bluebox"><s:text
													name="masters.loangrant.sanctionedcost" /><span
												class="mandatory">*</span></td>
											<td class="bluebox"><s:textfield id="sanctionedCost"
													name="sanctionedCost" style='text-align:right;'
													maxlength="20"
													onblur='validateDigitsAndDecimal(this);validateAmounts(this);' /></td>
											<td class="bluebox"><s:text
													name="masters.loangrant.revisedcost" /></td>
											<td class="bluebox"><s:textfield id="revisedCost"
													name="revisedCost" style='text-align:right;' maxlength="20"
													onblur='validateDigitsAndDecimal(this);validateAmounts(this);emptyRevisedCostIfZero(this);calculateAllPercentages();updateAllTotalAmounts()' /></td>
											<td class="bluebox">&nbsp&nbsp&nbsp(Amounts in Lacs)</td>
										</table>
									</td>
								</tr>
							</table>
							<div style="background-color: #F7F7F7; height: 24px">&nbsp</div>
							<div class="formmainbox">
								<div class="subheadnew">Funding pattern for the sanctioned
									amount</div>
							</div>
							<div class="yui-skin-sam" align="center">
								<div id="sanctionedAmountTable"></div>
							</div> <script>
		    makeSanctionedAmountTable();
			document.getElementById('sanctionedAmountTable').getElementsByTagName('table')[0].width="100%";
		 </script>
							<div id="codescontainer"></div>
						</span>
						<div align="center" class="buttonbottom">
							<input type="button" value="Close"
								onclick="javascript:window.close()" class="button" />
						</div>
					</div>
					<div class="tabbertab" id="receipttab">
						<h2>Unsanctioned Amount</h2>
						<div class="formmainbox">
							<div class="subheadnew">Funding pattern for the
								unsanctioned amount</div>
						</div>
						<div class="yui-skin-sam" align="center">
							<div id="unsanctionedAmountTable"></div>
						</div>
						<script>
				    makeUnsanctionedAmountTable();
					document.getElementById('unsanctionedAmountTable').getElementsByTagName('table')[0].width="100%";
				 </script>
					</div>
					<div class="tabbertab" id="receipttab">
						<h2>Revised Amount</h2>
						<div class="formmainbox">
							<div class="subheadnew">Funding pattern for the revised
								amount</div>
						</div>
						<div class="yui-skin-sam" align="left">
							<div id="revisedAmountTable"></div>
						</div>
						<script>
				    makeRevisedAmountTable();
					document.getElementById('revisedAmountTable').getElementsByTagName('table')[0].width="100%";
				 </script>
					</div>
					<div class="tabbertab" id="receipttab">
						<h2>Receipts</h2>
						<div class="yui-skin-sam" align="left">
							<div id="receiptTable"></div>
						</div>
						<script>
				    makeReceiptTable();
					document.getElementById('receiptTable').getElementsByTagName('table')[0].width="100%";
				 </script>
					</div>
				</div>
			</div>
			<script>
  	function loadBankAccount(obj)
	{
		var fund = document.getElementById('fundId');
		var accountObject= document.getElementById('bankaccount');
		accountObject.options[0].selected=true;
		var fundIdValue=fund.options[fund.selectedIndex].value;
		if(fundIdValue==-1)
		{
			bootbox.alert("Select fund first");
			document.getElementById('bank_branch').options[0].selected=true;
			return;		
		}
		var x=	obj.options[obj.selectedIndex].value;	
		if(x!=-1)
			populatebankaccount({branchId:x,fundId:fundIdValue});
	}
	function checkIfFundIsSelected()
	{
		var fund = document.getElementById('fundId');
		var fundIdValue=fund.options[fund.selectedIndex].value;
		if(fundIdValue==-1)
		{
			bootbox.alert("Select fund first");
			document.getElementById('bankaccount').options[0].selected=true;
			return;		
		}
	}
  	</script>
			<s:token />
		</s:push>
	</s:form>
	<script>
    </script>
</body>
</html>
