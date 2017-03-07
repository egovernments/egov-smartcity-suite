<%--
  Created by IntelliJ IDEA.
  User: jayashree
  Date: 7/3/17
  Time: 3:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div id="content" class="printable">
    <div class="formmainbox panel-primary">
        <div class="subheadnew text-center" id="headingdiv">
            <s:text name="page.title.viewtrade"/>
        </div>
        <table>
            <tr>
                <td align="left" style="color: #FF0000">
                    <s:actionerror cssStyle="color: #FF0000"/>
                    <s:fielderror/>
                    <s:actionmessage/>
                </td>
            </tr>
        </table>
        <s:form action="LicenseBillCollect" theme="simple" name="viewForm">
        <s:set value="outstandingFee" var="feeInfo"></s:set>
        <s:if test="%{#attr.feeInfo.size > 0}">
            <div class="panel-heading  custom_form_panel_heading subheadnew">
                <div class="panel-title"><s:text name='license.title.feedetail'/></div>
            </div>
            <table class="table table-bordered" style="width:97%;margin:0 auto;">
                <thead>
                <tr>
                    <th>Installment Year</th>
                    <th>Feetype</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <s:iterator value="feeInfo" var="installment" status="status">
                    <s:iterator value="%{#attr.installment.value}" var="val" status="status">
                        <tr>
                            <td>${installment.key}</td>
                            <td>${val.key}</td>
                            <td>${val.value}</td>
                        </tr>
                    </s:iterator>
                </s:iterator>
                </tbody>
            </table>
        </s:if>
    </div>
</div>

<div align="center" class="buttonbottom" id="buttondiv">
    <a href="#" class="btn btn-primary" onclick="window.open('/tl/integration/licenseBillCollect-collectfees.action?licenseId='+${licenseId}, '',
                    'scrollbars=yes,width=1000,height=700,status=yes'); return false;">Pay Fees</a>
    <a href='javascript:void(0)' class='btn btn-default' onclick='self.close()'>Close</a>
</div>
</s:form>
<script>

</script>
</body>
</html>