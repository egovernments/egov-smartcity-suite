<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/jquery-1.7.2.min.js'/>"></script>
<script type="text/javascript" src="<egov:url path='js/jquery/jquery.fixheadertable.js'/>"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery/base.css" />
<link rel="stylesheet" type="text/css" href="../css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />
<html>
<title><s:text name='page.title.workprogressabstract2.woYetToBeGivenEst' /> <s:property value="departmentName" /></title>
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
	         width : 1150, 
	         minColWidth	 : 10 , 
	         resizeCol	 : true, 
	         colratio    : [30,80,80,90,90,250,90,90,80,100,90,60]
	    });
	});

	function viewEstimate(estId) {
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}
	
	function viewWOYetToBeGivenEstPDF(){
		var parameter='exportType=PDF';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&wpDrillDownType='+dom.get("wpDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
		window.open("${pageContext.request.contextPath}/reports/workProgressAbstract2!viewWOYetToBeGivenEstPDF.action?"+parameter,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes,resizable=yes');
	}

	function viewWOYetToBeGivenEstExcel(){
	 	var parameter='exportType=XLS';
		parameter=parameter+'&fromDate='+dom.get("fromDate").value+'&toDate='+dom.get("toDate").value+'&worksType='+dom.get("worksType").value;
		parameter=parameter+'&worksSubType='+dom.get("worksSubType").value+'&fund='+dom.get("fund").value+'&function='+dom.get("function").value;
		parameter=parameter+'&preparedBy='+dom.get("preparedBy").value+'&scheme='+dom.get("scheme").value+'&subScheme='+dom.get("subScheme").value+'&depositCodeIdsStr='+dom.get("depositCodeIdsStr").value+'&budgetHeadIdsStr='+dom.get("budgetHeadIdsStr").value;
		parameter=parameter+'&coa='+dom.get("coa").value+'&budgetHeadsStr='+dom.get("budgetHeadsStr").value+'&depositCodesStr='+dom.get("depositCodesStr").value;
		parameter=parameter+'&departmentName='+dom.get("departmentName").value+'&wpDrillDownType='+dom.get("wpDrillDownType").value;
		parameter=parameter+'&subHeader='+dom.get("subHeader").value+'&natureOfWork='+dom.get("natureOfWork").value;
		window.open('${pageContext.request.contextPath}/reports/workProgressAbstract2!viewWOYetToBeGivenEstPDF.action?'+parameter);
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
		<s:hidden name="subHeader" id="subHeader" />
		<s:hidden name="wpDrillDownType" id="wpDrillDownType"/>
		<s:hidden name="natureOfWork" id="natureOfWork"/>
		<s:hidden name="budgetHeadsStr" id="budgetHeadsStr" />
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
	  				        <display:table name="paginatedList" uid="currentRowObject" pagesize="30" 
            						 cellpadding="0" cellspacing="0"  requestURI="" export="false" class="table-header-fix" 
             						 style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;align:center">
             						
             					 <display:column headerClass="pagetableth" class="pagetabletd" title="Sl No"
								   		titleKey="column.title.SLNo" style="text-align:left" >
								     	<s:property value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}"/>
								</display:column>
								
       							<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Tender Negotiation Number">
       								<s:if test="%{#attr.currentRowObject.tenderNegotiationNum!=null 
													&& #attr.currentRowObject.tenderNegotiationNum!=''}">
						    				<s:property value="#attr.currentRowObject.tenderNegotiationNum" />
									</s:if>
									<s:else>&nbsp;</s:else>
       							</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Tender Negotiation Date" >
									<s:if test="%{#attr.currentRowObject.tenderNegotiationDate!=null}">
										<s:date name="#attr.currentRowObject.tenderNegotiationDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="WorksPackage Date" >
									<s:if test="%{#attr.currentRowObject.worksPackageDate!=null}">
										<s:date name="#attr.currentRowObject.worksPackageDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Estimate Number" >
									<s:if test="%{#attr.currentRowObject.estNumber!=null}">
										<a href="Javascript:viewEstimate('<s:property  value='%{#attr.currentRowObject.estimateId}' />')">
											<s:property value="#attr.currentRowObject.estNumber" />
										</a>
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Name of the work" >
									<s:if test="%{#attr.currentRowObject.estName!=null}">
											<s:property value="#attr.currentRowObject.estName" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Estimate Admin Sanctioned Date" >
									<s:if test="%{#attr.currentRowObject.adminSanctionDate!=null}">
										<s:date name="#attr.currentRowObject.adminSanctionDate" format="dd/MM/yyyy" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:right" title="Estimate Work Value(Rs.)" >
									<s:if test="%{#attr.currentRowObject.estAmount!=null}">
											<s:property value="#attr.currentRowObject.estAmount" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Tender Type" >
									<s:if test="%{#attr.currentRowObject.tenderType!=null}">
											<s:property value="#attr.currentRowObject.tenderType" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:left" title="Name of the Contractor" >
									<s:if test="%{#attr.currentRowObject.contractorName!=null}">
											<s:property value="#attr.currentRowObject.contractorName" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:right" title="Tender value after Negotiation" >
									<s:if test="%{#attr.currentRowObject.tenderValueAfterNegotiation!=null}">
											<s:property value="#attr.currentRowObject.tenderValueAfterNegotiation" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:column  headerClass="pagetableth" class="pagetabletd" style="text-align:right" title="Negotiation %" >
									<s:if test="%{#attr.currentRowObject.negotiationPerc!=null}">
											<s:property value="#attr.currentRowObject.negotiationPerc" />
									</s:if>
									<s:else>&nbsp;</s:else>
								</display:column>
								
								<display:footer>
									<tr align="right" style="font-weight: bold;">
								  		<td colspan="7"><s:text name="workprogressabstract.grandtotal"/> : </td>
								  		<td><s:property value="%{totalEstAmount}"/></td>
								  	<tr>
								</display:footer>
								
							</display:table>
							<div class="buttonholderwk" id="divButRow1" name="divButRow1">
								<input type="button" class="buttonpdf" value="VIEW PDF" id="viewpdf" name="viewpdf" onclick="viewWOYetToBeGivenEstPDF();" align="center" />
								<input type="button" class="buttonpdf" value="VIEW EXCEL" id="viewexcel" name="viewpdf" onclick="viewWOYetToBeGivenEstExcel();" align="center" />
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