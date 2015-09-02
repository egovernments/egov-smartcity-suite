<%@ include file="/includes/taglibs.jsp"%>
<html>

    <head>
        <title><s:text name="tradelicense.reports.laterenewals.title" /></title>
    </head>

    <body>
        <s:form action="#" theme="simple">
            <s:token />
        <div class="subheadnew">
            <s:text name="tradelicense.reports.laterenewals.subtitle" />
        </div>
		
		 <s:iterator value="%{totalList}" status="rowTotal">
      <table style="background-color:#e8edf1;width:98%;padding:0px;margin:10 0 0 5px;">
      <tr><td class="bluebox"><s:text name="totallaterenlicenses" /></td><td  class="bluebox" style="text-align:right;" ><s:property value="%{TOTAL_LATEREN}"/></td></tr>
   		</table>
      </s:iterator>
      
        <logic:empty name="paginateList">
            <s:actionerror />
        </logic:empty>

        <logic:notEmpty name="paginateList">

            <display:table name="paginateList" uid="currentRowObject"
                decorator="org.displaytag.decorator.TotalTableDecorator"
                cellpadding="0" cellspacing="0" export="true" id="reportsmodule"
                style="background-color:#e8edf1;width:98%;height:120px;padding:0px;margin:10 0 0 5px;"
                requestURI="">
                <display:caption> &nbsp;</display:caption>
                <display:column  class="blueborderfortd" title="Ward Number"
                     style="text-align:center;width:25%" property="WARD_NUM" />
                     
                <display:column  class="blueborderfortd" title="Ward Name"
                     style="text-align:center;width:50%" property="WARD_NAME" />     
                     
                <display:column class="blueborderfortd" title="No. of Late Renewals"
                    style="text-align:right;width:25%" property="NO_OF_LATE_RENEWALS" />

                <display:setProperty name="export.csv" value="false" />
                <display:setProperty name="export.excel" value="true" />
                <display:setProperty name="export.xml" value="false" />
                <display:setProperty name="export.pdf" value="true" />
                <display:setProperty name="export.pdf.filename" value="tradeLicense-LateRenewals-ward.pdf" />
		<display:setProperty name="export.excel.filename" value="tradeLicense-LateRenewals-ward.xls" />

            </display:table>

        </logic:notEmpty>

        <div class="buttonbottom">
        <input name="button2" type="button" class="button" id="button"
            onclick="window.close()" value="Close" />
        </div>

        </s:form>
    </body>
</html>
