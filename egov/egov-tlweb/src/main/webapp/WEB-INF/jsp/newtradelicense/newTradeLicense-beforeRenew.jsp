<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title><s:text name="page.title.renewtrade" /></title>
<sx:head />
<script>
	function closethis() {
		if (confirm("Do you want to close this window ?")) {
			window.close();
		}
	}

  /* function validateWorkFlowApprover(name,errorDivId) {
    document.getElementById("workFlowAction").value=name;
      var approverPosId = document.getElementById("approverPositionId");
      if(approverPosId) {
      var approver = approverPosId.options[approverPosId.selectedIndex].text; 
      document.getElementById("approverName").value= approver.split('~')[0];
    }     
     return  onSubmit();
  } */

  function onSubmitValidations() {
   return true;
	  //  return validateForm(this);
  }
  
	function printthis() {
		if (confirm("Do you want to print this screen ?")) {
			var html = "<html>";
			html += document.getElementById('content').innerHTML;
			html += "</html>";

			var printWin = window
					.open('', '',
							'left=0,top=0,width=1,height=1,toolbar=0,scrollbars=0,status=0');
			printWin.document.write(html);
			printWin.document.close();
			printWin.focus();
			printWin.print();
			printWin.close();
		}
	}

	function onSubmit() {
		document.forms[0].action = 'newTradeLicense-renewal.action';
		document.forms[0].submit;
	}
</script>
</head>
<body>
  <div id="content">
    <table align="center" width="100%">
      <tbody>
        <tr>
          <td>
            <div align="center">
                <div class="formmainbox">
                  <div class="headingbg" id="headingdiv">
                    <s:text name="page.title.renewtrade" />
                  </div>
                  <table>
                    <tr>
                      <td align="left" style="color: #FF0000"><s:actionerror cssStyle="color: #FF0000" /> <s:fielderror />
                        <s:actionmessage /></td>
                    </tr>
                  </table>
                  <s:form action="newTradeLicense" theme="css_xhtml" name="renewForm">
                    <s:token />
                    <s:push value="model">
                      <s:hidden name="docNumber" />
                      <s:hidden name="model.id" />
                      <s:hidden id="detailChanged" name="detailChanged"></s:hidden>
                      <c:set var="trclass" value="greybox" />
                        <%@ include file='../common/view.jsp'%>
                        <c:choose>
                          <c:when test="${trclass=='greybox'}">
                            <c:set var="trclass" value="bluebox" />
                          </c:when>
                          <c:when test="${trclass=='bluebox'}">
                            <c:set var="trclass" value="greybox" />
                          </c:when>
                        </c:choose>
                        

                        <div>
                          <%@ include file='../common/commonWorkflowMatrix.jsp'%>
                          <%@ include file='../common/commonWorkflowMatrix-button.jsp'%>
                        </divr>
                    </s:push>
                  </s:form>
                </div>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <div class="mandatory1" style="font-size: 11px;" align="left">* Mandatory Fields</div>
</body>
</html>
