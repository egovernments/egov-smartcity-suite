<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<html>
<title><s:text name='page.title.workprogressabstract.tenderNotFinal.est' /><s:property value="departmentName" /></title>
<style type="text/css">
th.pagetableth {
	background-color: #E8EDF1;
	height: 30px;
	overflow: hidden;
	border-left: 1px solid #D1D9E1;
	color: #00639B;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 13px;
	font-weight: bold;
	line-height: 14px;
	padding: 3px;
	text-align: left;
	word-wrap: break-word;
}

td.pagetabletd{
	color: #333333;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	text-align: left;
	line-height: 13px;
	overflow: hidden;
	border: 1px solid #D1D9E1;
	padding: 3px;
}	

div.t_fixed_header {
	position		: relative;
	margin 			: 0; 
	width			: 100%;
}

div.t_fixed_header.ui .headtable th {
	padding			: 5px;
	text-align 		: center;
	border-width 	: 0 1px 0 0;
	border-style 	: solid;
	line-height		: 12px;
	font-size		: 11px;
	word-wrap       : normal;
}
.ui-state-default a, .ui-state-default a:link { 
	 text-decoration: underline;
	 font-size: 11px; 
}

.ui-state-default a:visited {
	color: #2e6e9e;
} 

div.t_fixed_header div.body {
	padding			: 0;
	width			: 100%;
	overflow-x		: hidden;
}
</style>
<script type="text/javascript">
	jQuery.noConflict();
	
	jQuery(document).ready(function() {
	    jQuery('.table-header-fix').fixheadertable({
	         caption : '<s:property value="%{subHeader}" />', 
	         height  : 400,
	         width : 1100, 
	         minColWidth	 : 10 , 
	         resizeCol	 : true, 
	         colratio    : [40,150,100,130,360,100,100,100]
	    });
	});
	function viewWP(id) {
		window.open("${pageContext.request.contextPath}/tender/worksPackage!edit.action?id="+id,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	function viewEstimate(id) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWPDetailsPdf(){
		var parameter='fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value+'&natureOfWork='+dom.get("natureOfWork").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&exportType=PDF';	
		
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!exportTenderYetToBeFinalizedEst.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWPDetailsExcel(){
		var parameter='fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value+'&natureOfWork='+dom.get("natureOfWork").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&exportType=XLS';	
	
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!exportTenderYetToBeFinalizedEst.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	function openWorkPrgRegisterReport(estId,woId){
		if(woId=='-1')
		{
			alert('<s:text name="no.workorder.for.estimate.alert" />');
		}
		else
		{
			window.open("${pageContext.request.contextPath}/reports/workProgressRegister!searchDetails.action?woId="+woId+
					"&sourcePage=deptWiseReport&estId="+estId,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');				
		}		
	}
	function getWOIDfromStr(estId)
	{
		var retval = "-1";
		var strval = dom.get("woIDAndEstIDStr").value;
		var woArr;
		var singleStr;
		if(strval!='')
		{
			woArr=strval.split(",");
			for(i=0;i<woArr.length;i++)
			{
				singleStr = woArr[i].split(":=");
				if(singleStr[0]==estId)
				{
					retval= singleStr[1];
				}	
			}	
		}
		return retval;
	}
</script>

<body>
	<s:form name="workProgressAbstract2Form" id="workProgressAbstract2Form" theme="simple">
	 	<s:hidden name="fromDate" id="fromDate" />
		<s:hidden name="toDate" id="toDate" />
		<s:hidden name="worksType" id="worksType" />
		<s:hidden name="executingDepartment" id="executingDepartment" />
		<s:hidden name="natureOfWork" id="natureOfWork" />
		<s:hidden name="worksSubType" id="worksSubType" />
		<s:hidden name="fund" id="fund" />
		<s:hidden name="function" id="function" />
		<s:hidden name="preparedBy" id="preparedBy" />
		<s:hidden name="scheme" id="scheme" />
		<s:hidden name="subScheme" id="subScheme" />
		<s:hidden name="budgetHeads" id="budgetHeads" />
		<s:hidden name="coa" id="coa" />
		<s:hidden name="budgetHeadsStr" id="budgetHeadsStr" />
		<s:hidden name="depositCodesStr" id="depositCodesStr" />
	 	<s:hidden name="depositCodeIdsStr" id="depositCodeIdsStr" />
		<s:hidden name="budgetHeadIdsStr" id="budgetHeadIdsStr" />
	 	
		<s:hidden name="departmentName" id="departmentName" />
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="woIDAndEstIDStr" id="woIDAndEstIDStr" />
				
		<div class="formmainbox">
		<div class="insidecontent">
		<div id="printContent" class="rbroundbox2">
		<div class="rbtop2"><div></div></div>
		<div class="rbcontent2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr id="resultRow" ><td colspan="4"> 
						 <s:if test="%{pagedResults.fullListSize!=0}">
						 	<s:text id="nameOfEst" name="%{getText('label.work.name')}"></s:text>
						 	<s:text id="wpDate" name="%{getText('label.wp.date')}"></s:text>
						 	<s:text id="adminSancDate" name="%{getText('label.estimate.admin.sanction.date')}"></s:text>
	  				        <display:table name="pagedResults" uid="currentRow" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI="" class="table-header-fix" >
             						             						
             					 <display:column headerClass="pagetableth" class="pagetabletd" title="Sl No"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								 			<s:property value="%{#attr.currentRow_rowNum + (page-1)*pageSize}"/>
								</display:column>
								
       							<display:column headerClass="pagetableth" class="pagetabletd"    title="Works Package Number"  style="text-align:left;">
								 		<a href="#" onclick="viewWP('<s:property value="%{#attr.currentRow.worksPackage.id}" />')" >
						 					<s:property value="%{#attr.currentRow.worksPackage.wpNumber}" />	</a>
						 		</display:column>
						 		<display:column headerClass="pagetableth" class="pagetabletd"    title="${wpDate}"  style="text-align:left;">
					 					<s:date name="#attr.currentRow.worksPackage.packageDate" format="dd/MM/yyyy" />
						 		</display:column>
       							
       							<display:column headerClass="pagetableth" class="pagetabletd"    title="Estimate Number"  style="text-align:left;">
									<a href="#" onclick="viewEstimate('<s:property value="%{#attr.currentRow.estimate.id}" />')">
						 					<s:property value="%{#attr.currentRow.estimate.estimateNumber}" />	</a>
						  		</display:column>
						  		<display:column headerClass="pagetableth" class="pagetabletd"    title="${nameOfEst}"  style="text-align:left;">
						 			<s:property value="%{#attr.currentRow.estimate.name}" />
						  		</display:column>
						  		
						  		<display:column headerClass="pagetableth" class="pagetabletd" title="${adminSancDate}" titleKey='estimate.search.approvedDate' style="width:2%;text-align:left" >
       								<s:if test="%{#attr.currentRow.estimate.approvedDate!=null}">
										<s:date name="#attr.currentRow.estimate.approvedDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
  		
						     	<display:column headerClass="pagetableth" class="pagetabletd"    title="Estimate Amount(Rs)"  style="text-align:right;" >
							  		<s:text name="contractor.format.number" >
										<s:param name="rate" value="%{#attr.currentRow.estimate.workValue.value}"/>
									</s:text>
							  	</display:column>
							  	<display:footer>
									<tr style="font-weight: bold;">
								  		<td colspan="6" align="right"><s:text name="workprogressabstract.grandtotal"/> :&nbsp;&nbsp;&nbsp;&nbsp; </td>
								  		<td align="right"><s:property value="%{totalEstAmount}"/></td>
								  	<tr>
								</display:footer>
								<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Progress Estimate Link"  style="text-align:center;">
						 				<a href="Javascript:openWorkPrgRegisterReport(<s:property  value='%{#attr.currentRow.estimate.id}' />,getWOIDfromStr(<s:property  value='%{#attr.currentRow.estimate.id}' />))">
						    				<img src="${pageContext.request.contextPath}/image/book_open.png"
												alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
										</a>
						  		</display:column>
									  		
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewWPDetailsPdf();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewWPDetailsExcel();" align="center" />
								<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();"/>
							</div>
						</s:if>
						<s:else>
							<div align="center"><font color="red">No record Found.</font></div>
						</s:else>
						</td></tr>
				</table>
			</td>
		</tr>
		</table>

	</div>
	</div>
	</div>
	</div>									
</s:form>
</body>
</html>