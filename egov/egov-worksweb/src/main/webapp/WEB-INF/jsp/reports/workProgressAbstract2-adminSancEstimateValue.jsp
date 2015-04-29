<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/workProgressAbstractReportHelper.js'/>"></script>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />

<html>
<title>
	<s:if test="%{estStatus=='adminSancEstValue'}">
		<s:text name='page.title.workprogressabstract.showASEstimateList' />
	</s:if>
	<s:else>
		<s:text name='page.title.workprogressabstract.showSpillOverEstimateList' />
	</s:else>	
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
	<s:if test="%{estStatus=='adminSancEstValue'}">
		width			: 164%;
	</s:if>
	<s:else>
		width			: 170%;
	</s:else>
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
	    jQuery('.table-header-fix').fixheadertable({
	    	caption : '<s:property value="%{subHeader}" />', 
	    	height  : 400,
	        resizeCol	 : true, 
	        whiteSpace     : 'normal',
	        width : 1180 ,	    
	        wrapper        : false, 
	        <s:if test="%{estStatus=='adminSancEstValue'}">
	        	colratio    : [40,160,100,280,140,140,80,80,160,80,80,80,80,80,80,80,80,80]
	        </s:if>
	        <s:else>
	        	colratio    : [40,160,100,280,140,140,80,80,160,80,80,80,80,80,80,80,80,80,80]
	        </s:else>
	        
	    });
	});

	function viewEstimate(estId) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function openWP(id){
		window.open("${pageContext.request.contextPath}/tender/worksPackage!edit.action?id="+id+
		"&sourcepage=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}

	function viewEstimatePDF(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&estStatus='+dom.get("estStatus").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
        <s:if test="%{estStatus=='adminSancEstValue'}">
			window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!viewAdminSancEstPDF.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		</s:if>
		<s:else>
			window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!viewSpillOverEstPDF.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		</s:else>	
	}

	function viewEstimateXLS(){
		var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value+'&budgetHeadsFirstReportStr='+dom.get("budgetHeadsFirstReportStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&estStatus='+dom.get("estStatus").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
		<s:if test="%{estStatus=='adminSancEstValue'}">
			window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!viewAdminSancEstXLS.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		</s:if>
		<s:else>
			window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!viewSpillOverEstXLS.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
		</s:else>
	}
</script>

<body>
	<s:form name="workProgressAbstractForm" id="workProgressAbstractForm" theme="simple">
		<s:hidden name="fromDate" id="fromDate" />
		<s:hidden name="toDate" id="toDate" />
		<s:hidden name="worksType" id="worksType" />
		<s:hidden name="worksSubType" id="worksSubType" />
		<s:hidden name="fund" id="fund" />
		<s:hidden name="function" id="function" />
		<s:hidden name="preparedBy" id="preparedBy" />
		<s:hidden name="scheme" id="scheme" />
		<s:hidden name="subScheme" id="subScheme" />
		<s:hidden name="coa" id="coa" />
		<s:hidden name="departmentName" id="departmentName" />
		<s:hidden name="estStatus" id="estStatus" />
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="natureOfWork" id="natureOfWork" />
		<s:hidden name="budgetHeadsStr" id="budgetHeadsStr" />
		<s:hidden name="budgetHeadsFirstReportStr" id="budgetHeadsFirstReportStr" />
		<s:hidden name="depositCodesStr" id="depositCodesStr" />
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
						 <s:if test="%{paginatedList.fullListSize!=0}">
						 
						 	<s:text id="SlNo" name="%{getText('label.slno')}"></s:text>		
							<s:text id="estNumber" name="%{getText('label.estimateNumber')}"></s:text>
							<s:text id="nameOfEst" name="%{getText('label.work.name')}"></s:text>
							<s:text id="estDate" name="%{getText('label.estimateDate')}"></s:text>
							<s:text id="adminSancDate" name="%{getText('label.estimate.admin.sanction.date')}"></s:text>
							<s:text id="estAmount" name="%{getText('label.estimate.as.amount')}"></s:text>
							<s:text id="estWard" name="%{getText('workprogressabstract.ward')}"></s:text>
							<s:text id="estType" name="%{getText('workprogressabstract.estType')}"></s:text>
							<s:text id="estSubType" name="%{getText('workprogressabstract.estSubType')}"></s:text>
							<s:text id="estValue" name="%{getText('workprogressabstract.estValue')}"></s:text>
							<s:text id="tenderDocReleasedDt" name="%{getText('workprogressabstract.tenderDocReleasedDt')}"></s:text>		
							<s:text id="woApprovedDt" name="%{getText('workprogressabstract.woApprovedDt')}"></s:text>
							<s:text id="tenderFinalisedValue" name="%{getText('workprogressabstract.tenderFinalisedValue')}"></s:text>
							<s:text id="workPhysicalPerc" name="%{getText('workprogressabstract.workPhysicalPerc')}"></s:text>
							<s:text id="contractperiod" name="%{getText('workprogressabstract.contractperiod')}"></s:text>
							<s:text id="expecCompletionDate" name="%{getText('workprogressabstract.expecCompletionDate')}"></s:text>
							<s:text id="paymentReleased" name="%{getText('workprogressabstract.paymentReleased')}"></s:text>
							<s:text id="status" name="%{getText('workprogressabstract.status')}"></s:text>
							<s:text id="completionDate" name="%{getText('workprogressabstract.completionDate')}"></s:text>
							<s:text id="savings" name="%{getText('workprogressabstract.savings')}"></s:text>
							<s:text id="wpNo" name="%{getText('label.wp.number')}"></s:text>
							
						    <display:table name="paginatedList" uid="currentRowObject" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI="" export="false" class="table-header-fix" 
             						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;align:center">
             						
             					 <display:column headerClass="pagetableth" class="pagetabletd" title="${SlNo}"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								     	<s:property value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}"/>
								</display:column>
								
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="${estNumber}">
       								<s:if test="%{#attr.currentRowObject.estNumber!=null}">
										<a href="Javascript:viewEstimate('<s:property  value='%{#attr.currentRowObject.estimateId}' />')">
												<s:property value="#attr.currentRowObject.estNumber" />
										</a> 
									</s:if>
									<s:else>&nbsp;</s:else>
       							</display:column>
       							
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Ward/Zone/HQ">
										<s:property value="#attr.currentRowObject.wardName" />
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="${nameOfEst}" >
									<s:if test="%{#attr.currentRowObject.estName!=null}">
										<s:property value="#attr.currentRowObject.estName" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="${estType}">
									<s:if test="%{#attr.currentRowObject.typeOfWork!=null}">
										<s:property value="#attr.currentRowObject.typeOfWork" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="${estSubType}">
									<s:if test="%{#attr.currentRowObject.subTypeOfWork!=null}">
										<s:property value="#attr.currentRowObject.subTypeOfWork" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
       							
								<display:column headerClass="pagetableth" class="pagetabletdAmount" title="${estAmount}" style="width:3%;">
									<s:text name="contractor.format.number" >
										<s:param name="rate" value="#attr.currentRowObject.roundedOffEstAmount"/>
									</s:text>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${adminSancDate}" titleKey='estimate.search.approvedDate'  style="text-align:left" >
       								<s:if test="%{#attr.currentRowObject.estApprovedDate!=null}">
										<s:date name="#attr.currentRowObject.estApprovedDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${wpNo}" titleKey='label.wp.number'  style="text-align:left" >
       								<s:if test="%{#attr.currentRowObject.worksPackageNo!=null}">
       									<a href="Javascript:openWP('<s:property  value='%{#attr.currentRowObject.worksPackageId}' />')">
       										<s:property value="#attr.currentRowObject.worksPackageNo" />
       									</a>	
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${tenderDocReleasedDt}"  style="text-align:left" >
       								<s:if test="%{#attr.currentRowObject.tenderDocReleaseDate!=null}">
										<s:date name="#attr.currentRowObject.tenderDocReleaseDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletdAmount" title="${tenderFinalisedValue}"  >
       								<s:if test="%{#attr.currentRowObject.tenderValueAfterNegotiation!=null}">
       									<s:text name="contractor.format.number" >
											<s:param name="rate" value="#attr.currentRowObject.tenderValueAfterNegotiation"/>
										</s:text>
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								<s:if test="%{estStatus=='spilloverWorksEstimate'}">
									<display:column headerClass="pagetableth" class="pagetabletd" title="${woApprovedDt}"  style="text-align:left" >
										<s:if test="%{#attr.currentRowObject.workOrderApprovedDate!=null}">
											<s:date name="#attr.currentRowObject.workOrderApprovedDate" format="dd/MM/yyyy" />
										</s:if>
										<s:else>&nbsp;</s:else>
									</display:column>	
								</s:if>
								
								<display:column headerClass="pagetableth" class="pagetabletdAmount" title="${workPhysicalPerc}" >
       								<s:if test="%{#attr.currentRowObject.milestonePerc!=null}">
										<s:property value="#attr.currentRowObject.milestonePerc" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletdAmount" title="${contractperiod}" >
       								<s:if test="%{#attr.currentRowObject.contractPeriod!=0}">
										<s:property value="#attr.currentRowObject.contractPeriod" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${expecCompletionDate}"  style="text-align:left" >
       								<s:if test="%{#attr.currentRowObject.expectedCompletionDate!=null}">
										<s:date name="#attr.currentRowObject.expectedCompletionDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletdAmount" title="${paymentReleased}" >
       								<s:if test="%{#attr.currentRowObject.paymentReleasedAmount!=0}">
										<s:text name="contractor.format.number" >
											<s:param name="rate" value="#attr.currentRowObject.paymentReleasedAmount"/>
										</s:text>
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${status}"  style="text-align:left" >
       								<s:if test="%{#attr.currentRowObject.finalBillStatus!=null}">
										<s:property value="#attr.currentRowObject.finalBillStatus" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletd" title="${completionDate}"  style="text-align:left" >
       								<s:if test="%{#attr.currentRowObject.completionDate!=null}">
										<s:date name="#attr.currentRowObject.completionDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column headerClass="pagetableth" class="pagetabletdAmount" title="${savings}"  >
       								<s:if test="%{#attr.currentRowObject.savings!=null}">
       									<s:text name="contractor.format.number" >
											<s:param name="rate" value="#attr.currentRowObject.savings"/>
										</s:text>
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
							</display:table>
							<br />
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewEstimatePDF();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewEstimateXLS();" align="center" />
								
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