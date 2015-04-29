<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<html>
<title>
	<s:if test="%{woDrillDownType=='WorkNotStartedEstimates'}">
		<s:text name='page.title.workprogressabstract.showWorkNotStrtdEst' />
	</s:if>
	<s:if test="%{woDrillDownType=='SiteHandedOverAndWorkNotStartedEstimates'}">
		<s:text name='page.title.workprogressabstract.showSiteHandedOverAndWorkNotStrtdEst' />
	</s:if>
	<s:if test="%{woDrillDownType=='WorkStartedEstimates'}">
		<s:text name='page.title.workprogressabstract.showWorkStrtdEst' />
	</s:if>
	<s:if test="%{woDrillDownType=='WorkOrderEstimates'}">
		<s:text name='page.title.workprogressabstract.showWOEstimates' />
	</s:if>
	<s:property value="departmentName" />
</title>
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
	width			: 139%;
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

div.t_fixed_header_main_wrapper {
	position 		: relative; 
	overflow 		: auto; 
}

div.t_fixed_header div.headtable {
	border			: 0;	
	overflow-x		: hidden;
    overflow-y		: hidden;
}
</style>
<script type="text/javascript">
	jQuery.noConflict();
	
	jQuery(document).ready(function() {
		var drillDownType = document.getElementById("woDrillDownType").value;
		 if(drillDownType=='WorkStartedEstimates'){
		    jQuery('.table-header-fix').fixheadertable({
		    	caption : '<s:property value="%{subHeader}" />', 
		         height  : 400,
		         minColWidth	 : 10 , 
		         resizeCol	 : true, 
		         width : 1100, 
		         colratio    : [30,100,90,100,300,80,90,90,90,80,80,120,80,80,80]
		    });
	    }
		
		else {
			jQuery('.table-header-fix').fixheadertable({
			    	caption : '<s:property value="%{subHeader}" />', 
			         height  : 400,
			         minColWidth	 : 10 , 
			         resizeCol	 : true, 
			         width : 1100, 
			         colratio    : [35,110,90,110,390,90,100,100,90,130,90,90,90]
			    });
		 }	        
	});
	function viewWO(id){
		window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id+"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	function viewEstimate(id) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWODrillDownPdf(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value+'&natureOfWork='+dom.get("natureOfWork").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&woDrillDownType='+dom.get("woDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&reportSource='+dom.get("reportSource").value;
		
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewWODrillDownExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWODrillDownExcel(){
		var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value+'&executingDepartment='+dom.get("executingDepartment").value+'&natureOfWork='+dom.get("natureOfWork").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&woDrillDownType='+dom.get("woDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&reportSource='+dom.get("reportSource").value;
	
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract!viewWODrillDownExport.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function openWorkPrgReport(id,estId){
		window.open("${pageContext.request.contextPath}/reports/workProgressRegister!searchDetails.action?woId="+id+
			"&sourcePage=deptWiseReport&estId="+estId,'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

</script>

<body>
	<s:form name="workProgressAbstractForm" id="workProgressAbstractForm" theme="simple">
		<s:hidden name="fromDate" id="fromDate" />
		<s:hidden name="toDate" id="toDate" />
		<s:hidden name="worksType" id="worksType" />
		<s:hidden name="worksSubType" id="worksSubType" />
		<s:hidden name="executingDepartment" id="executingDepartment" />
		<s:hidden name="natureOfWork" id="natureOfWork" />
		<s:hidden name="fund" id="fund" />
		<s:hidden name="function" id="function" />
		<s:hidden name="preparedBy" id="preparedBy" />
		<s:hidden name="scheme" id="scheme" />
		<s:hidden name="subScheme" id="subScheme" />
		<s:hidden name="budgetHeads" id="budgetHeads" />
		<s:hidden name="coa" id="coa" />
		<s:hidden name="departmentName" id="departmentName" />
		<s:hidden name="estStatus" id="estStatus" />
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="woDrillDownType" id="woDrillDownType"/>
		<s:hidden name="budgetHeadsStr" id="budgetHeadsStr" />
		<s:hidden name="budgetHeadsFirstReportStr" id="budgetHeadsFirstReportStr" />
		<s:hidden name="depositCodesStr" id="depositCodesStr" />
		<s:hidden name="reportSource" id="reportSource"/>
		<s:hidden name="depositCodeIdsStr" id="depositCodeIdsStr" />
		<s:hidden name="budgetHeadIdsStr" id="budgetHeadIdsStr" />
		
		
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
	  				        <display:table name="pagedResults" uid="currentRow" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI="" class="table-header-fix" >
             						
             					<display:column headerClass="pagetableth" class="pagetabletd" title="Sl No"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								 			<s:property value="%{#attr.currentRow_rowNum + (page-1)*pageSize}"/>
								</display:column>
								
       							<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Order Number"  style="text-align:left;">
								 		<a href="#" onclick="viewWO('<s:property value="%{#attr.currentRow.workOrder.id}" />')" >
						 					<s:property value="%{#attr.currentRow.workOrder.workOrderNumber}" />	</a>
						 		</display:column>
						 		  
						 		<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Order Date"  style="text-align:left;">
						 				<s:date name="#attr.currentRow.workOrder.workOrderDate" format="dd/MM/yyyy" />
						 		</display:column>
						 		 
       							<display:column headerClass="pagetableth" class="pagetabletd"    title="Estimate Number"  style="text-align:left;">
									<a href="#" onclick="viewEstimate('<s:property value="%{#attr.currentRow.estimate.id}" />')">
						 					<s:property value="%{#attr.currentRow.estimate.estimateNumber}" />	</a>
						  		</display:column>
										
								<display:column headerClass="pagetableth" class="pagetabletd"    title="Name of the Work"  style="text-align:left;">
						 				<s:property value="%{#attr.currentRow.estimate.name}" />	
						  		</display:column>
						  			
						  		<display:column headerClass="pagetableth" class="pagetabletd"    title="Estimate Admin Sanctioned Date"  style="text-align:left;">
						  				<s:date name="#attr.currentRow.estimate.approvedDate" format="dd/MM/yyyy" />
						  		</display:column>

						     	<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Value(Rs)"  style="text-align:right;" >
						     			<s:property value="%{#attr.currentRow.estimate.workValue.formattedString}" />
							  	</display:column>	
							  	
							  	<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Order Value(Rs)"  style="text-align:right;" >
							  		<s:text name="contractor.format.number" >
										<s:param name="rate" value="%{#attr.currentRow.workOrder.workOrderAmount}"/>
									</s:text>
							  	</display:column>
							  	
							  	<display:column headerClass="pagetableth" class="pagetabletd"    title="Negotiation Percentage"  style="text-align:right;">
						 					<s:property value="%{#attr.currentRow.workOrder.negotiationPercentage}" />	
						  		</display:column>
						  		
						  		<s:if test="%{woDrillDownType=='WorkStartedEstimates' || woDrillDownType=='SiteHandedOverAndWorkNotStartedEstimates'}">
						  			<s:if test="%{#attr.currentRow.workOrder.siteHandOverDate!=null}"> 
								  		<display:column headerClass="pagetableth" class="pagetabletd"    title="Site Handed Over Date"  style="text-align:left;">
								  				<s:date name="#attr.currentRow.workOrder.siteHandOverDate" format="dd/MM/yyyy" />
								  		</display:column>
							  		</s:if>
							  	</s:if>
							  	<s:if test="%{woDrillDownType=='WorkStartedEstimates'}">	
							  		<s:if test="%{#attr.currentRow.workOrder.workCommencedDate!=null}"> 
								  		<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Commencement Date"  style="text-align:left;">
								  				<s:date name="#attr.currentRow.workOrder.workCommencedDate" format="dd/MM/yyyy" />
								  		</display:column>
							  		</s:if>
						  		</s:if>
						  		
						  		<display:column headerClass="pagetableth" class="pagetabletd"    title="Contractor Name"  style="text-align:left;">
						 					<s:property value="%{#attr.currentRow.workOrder.contractor.name}" />	
						  		</display:column>
						  		
						  		<display:column headerClass="pagetableth" class="pagetabletd"    title="Contractor Period in Days"  style="text-align:right;">
						 					<s:property value="%{#attr.currentRow.workOrder.contractPeriod}" />	
						  		</display:column>
						  		
						  		<display:column headerClass="pagetableth" class="pagetabletd"    title="Expected Date of completion"  style="text-align:left;">
											 <s:date name="#attr.currentRow.workOrder.expectedCompletionDate" format="dd/MM/yyyy" />
						  		</display:column>
						  		
						  		<display:column headerClass="pagetableth" class="pagetabletd"    title="Work Progress Estimate Link"  style="text-align:center;">
						 				<a href="Javascript:openWorkPrgReport(<s:property  value='%{#attr.currentRow.workOrder.id}' />, <s:property  value='%{#attr.currentRow.estimate.id}' />)">
						    				<img src="${pageContext.request.contextPath}/image/book_open.png"
												alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
										</a>
						  		</display:column>
						  		
							  	<display:footer>
									<tr style="font-weight: bold;">
								  		<td colspan="6" align="right"><s:text name="workprogressabstract.grandtotal"/> :&nbsp;</td>
								  		<td colspan="1" align="right"><s:property value="%{totalEstAmount}"/></td>
								  		<td colspan="1" align="right"><s:property value="%{totalWorkValueAmt}"/></td>
								  	<tr>
								</display:footer>
									  		
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewWODrillDownPdf();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewWODrillDownExcel();" align="center" />
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