<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<title><s:text name="Wardwise.billGen.status"></s:text></title>
<script type="text/javascript">
</script>
</head>

<body>
	<div class="formmainbox">
		<div class="formheading"></div>
		<div class="headingbg">
			<s:text name="Wardwise.billGen.status" />
		</div>
		<s:form theme="simple" action="billGeneration" name="BillGenerationForm">
			<s:if test="%{reportInfos.isEmpty()}">
				<span class="bold"  style="font-size: 14px"> <br> There are no bills generated for current financial year. </span>
			</s:if>
			<s:else>
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">

					<tr>
						<th class="bluebgheadtd" width="25%"/>
						<th class="bluebgheadtd" width="15%">
							<s:text name="Ward" />
						</th>
						<th class="bluebgheadtd" width="15%" align="center">
							<s:text name="noOfProps" />
						</th>
						<th class="bluebgheadtd" width="15%" align="center">
							<s:text name="noof.bills.generated" />
						</th>
						<th class="bluebgheadtd" width="25%"/>
					</tr>
						<s:iterator value="(reportInfos.size).{#this}" status="reportInfo">
						<tr>
							<td class="blueborderfortd" width="25%"></td>
							<td class="blueborderfortd" style="border-left: 1px solid #E9E9E9">
								<div align="center">
									<a href='../bills/billGeneration!billGenStatusByPartNo.action?wardNum=<s:property value="%{reportInfos[#reportInfo.index].wardNo}" />'>
										<s:property value="%{reportInfos[#reportInfo.index].wardNo}" />
									</a>
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property value="%{reportInfos[#reportInfo.index].totalNoProps}" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:property value="%{reportInfos[#reportInfo.index].totalGenBills}" />
								</div>
							</td>
							<td class="blueborderfortd" width="25%"></td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			<div class="buttonbottom" align="center">
				<input type="button" name="button2" id="button2" value="Close" class="button" onclick="return confirmClose();" />
			</div>
		</s:form>
	</div>
</body>

</html>
