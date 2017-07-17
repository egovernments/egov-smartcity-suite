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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<html>
	<head>
		<title><s:text name="page.title.viewtrade" /></title>
	</head>
	<body>
		<div id="content" class="printable">
			<div class="formmainbox panel-primary">
										<div class="subheadnew text-center" id="headingdiv">
											<s:text name="page.title.viewtrade" />
										</div>
										<table>
											<tr>
												<td align="left" style="color: #FF0000">
													<s:actionerror cssStyle="color: #FF0000" />
													<s:fielderror />
													<s:actionmessage />
												</td>
											</tr>
										</table>
										<s:form action="viewTradeLicense" theme="simple" name="viewForm">
											<s:push value="model">
												<s:hidden name="actionName" value="create" />
												<s:hidden id="detailChanged" name="detailChanged"></s:hidden>
												<c:set var="trclass" value="greybox" />
												<div class="text-right error-msg" style="font-size:14px;">
													<s:text name="dateofapplication.lbl" /> : <s:date name="applicationDate"  
													format="dd/MM/yyyy"/></div>
	                   							<s:if test="%{applicationNumber!=null}">
	                    	 						<div class="text-right error-msg" style="font-size:14px;">
	                    	 							<s:text name="application.num" /> : <s:property value="%{applicationNumber}" />
	                    	 						</div>
	                 							</s:if>
												<table width="100%">
													<%@ include file='../common/license-detail-view.jsp'%>
												</table>
											</s:push>
										</s:form>
		
		
		</div>
		</div>
		<div align="center" class="buttonbottom" id="buttondiv">
			<table>
				<tr>
					<s:if test="%{enableViewDcb()==true}">
						<td>
							<input name="viewdcb" type="button" class="buttonsubmit" id="button" value="View DCB"
								   onclick="window.open('/tl/dcb/view/'+ <s:property value="%{id}"/>, '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"/>
						</td>
					</s:if>
					<td>
						<input name="button1" type="button" class="buttonsubmit printbtn" id="button" value="Print" />
					</td>
					<td>
						<input name="button2" type="button" class="button" id="button" onclick="window.close();" value="Close" />
					</td>
				</tr>
			</table>
		</div>
		<script src="<cdn:url  value='/resources/global/js/jquery/plugins/jQuery.print.js' context='/egi'/>"></script>
	</body>
</html>
