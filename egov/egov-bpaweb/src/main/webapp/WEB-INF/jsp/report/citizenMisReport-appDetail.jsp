<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>

<head>
<title>MIS Report Details</title>
</head>
<script type="text/javascript">
function viewRegisterBpa(registrationId){
	document.location.href="${pageContext.request.contextPath}/register/registerBpa!viewForm.action?registrationId="+registrationId;		
 }
</script>
<body>

	<s:form action="citizenMisReport" name="citizenReportForm"
		theme="simple">


		<table>
			<div id="tableData">
				<div id="displaytbl">
				 <table align="center" width="100%" border="0" cellpadding="0"
                    cellspacing="0">
                    <tr>
                        <td class="headingwk" colspan="5">
                            <div class="arrowiconwk">
                                <img src="../common/image/arrow.gif" />
                            </div>
                            <div style="font-size: 13;font-color: red; margin-top: 4px;" align="left">Building Plan Approval :MIS Report</div>
                        </td>
                    </tr>
                </table>
				<s:if test="{regList!=null}">
					<display:table name="regList" export="true" requestURI="" id="registrationId" class="its" uid="currentRowObject" pagesize="25">
					<div STYLE="display: table-header-group" align="center">
						<display:setProperty name="basic.show.header" value="true" />
                        <display:setProperty name="export.xml" value="false" />
                		<display:setProperty name="export.pdf" value="true" />
                        <display:setProperty name="export.excel" value="true" />
                    	<display:setProperty name="export.pdf.filename" value="citizenMisReport.pdf" />
                        <display:setProperty name="export.excel.filename" value="citizenMisReport.xls" />
                      	<display:setProperty name="export.csv" value="false" />
               		
							<display:column title=" Sl No" style="text-align:center;">
								<s:property value="#attr.currentRowObject_rowNum" />
							</display:column>
							<display:column title="Plan Submission Number "style="text-align:center;">
								<a href="#" onclick="viewRegisterBpa('${currentRowObject.id}')">${currentRowObject.planSubmissionNum} </a>
							</display:column>
							<display:column title="Ward" style="text-align:center;" property="adminboundaryid.name" />
							<display:column title="Division" style="text-align:center;"  property="locboundaryid.name" />
							<display:column title="Applicant Name " style="text-align:center;" property="owner.firstName" />
							<display:column title="Mobile Number" style="text-align:center;" property="owner.mobilePhone" />
							<display:column title="Communication Address " style="text-align:center;" property="bpaOwnerAddress" />
							<display:column title="Location" style="text-align:center;" property="bpaSiteAddress" />
							<display:column title="Current Owner" style="text-align:center;">
								<s:property value="%{getUsertName(#attr.currentRowObject.state.owner.id)}" />
							</display:column>
							<display:column title="Nature of Proposal" style="text-align:center;" property="appType" />
 							<display:column title="Status " style="text-align:center;" 	property="egwStatus.code" />
							<display:column title="Last Transaction Date " 	style="text-align:center;">
							<s:date name="#attr.currentRowObject.modifiedDate" format="dd/MM/yyyy" />
							</display:column>
						</div>	
 						</display:table>
					</s:if>
				</div>
			</div>
			<div></div>
		</table>
	</s:form>



</body>
</html>