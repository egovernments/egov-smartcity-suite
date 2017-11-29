<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<s:if test="%{chequeIssueRegisterList.size()>0}">
	<table width="99%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td class="bluebox">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="tablebottom">
					<tr>
						<td>
							<div class="subheadsmallnew">
								<span class="subheadnew"><s:property value="ulbName" /></span>
							</div>
						</td>
					</tr>
					<tr>
						<th class="bluebgheadtd" width="100%"><strong
							style="font-size: 15px;"> Cheque issue register report
								for <b><s:property value="formattedBankName" /></b> with <br />account
								no:<b><s:property value="accountNumber.accountnumber" /></b>
								from <b><s:property value="fromDate" /></b> to <b><s:property
										value="toDate" /></b>
						</strong></th>
					</tr>
					<tr>
						<td class="blueborderfortd">
							<div>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="tablebottom">

									<tr>
										<th class="bluebgheadtd" width="2%">Sl No</th>
										<th class="bluebgheadtd" width="2%">Cheque Number</th>
										<th class="bluebgheadtd" width="8%">Cheque Date</th>
										<th class="bluebgheadtd" width="10%">Name of the Payee</th>
										<th class="bluebgheadtd" width="10%">Cheque Amount(Rs)</th>
										<th class="bluebgheadtd" width="10%">Nature of Payment</th>
										<th class="bluebgheadtd" width="10%">Cheque Status</th>
										<th class="bluebgheadtd" width="15%">Payment Order No.
											&amp; Date</th>
										<th class="bluebgheadtd" width="15%">Bank Payment Voucher
											No. &amp; Date</th>
										<th class="bluebgheadtd" width="15%">Bank Advice Report</th>
										<s:if
											test="%{chequePrintingEnabled && chequePrintAvailableAt=='assignment'}">
											<th class="bluebgheadtd" width="8%">Print Cheque</th>
										</s:if>
									</tr>
									<s:iterator value="chequeIssueRegisterList" status="stat">
										<tr>
											<td class="blueborderfortd"><div align="center">
													<s:property value="#stat.index+1" />
													&nbsp;
												</div></td>
											<td class="blueborderfortd"><div align="center">
													<s:property value="chequeNumber" />
													&nbsp;
												</div></td>
											<td class="blueborderfortd"><s:property
													value="chequeDate" />&nbsp;</td>
											<td class="blueborderfortd"><div align="center">
													<s:property value="payTo" />
													&nbsp;
												</div></td>
											<td class="blueborderfortd"><div align="right">
													<s:text name="format.number">
														<s:param name="value" value="chequeAmount" />
													</s:text>
													&nbsp;
												</div></td>
											<td class="blueborderfortd"><div align="center">
													<s:property value="voucherName" />
													&nbsp;
												</div></td>
											<td class="blueborderfortd"><div align="center">
													<s:property value="chequeStatus" />
													&nbsp;
												</div></td>
											<td class="blueborderfortd"><div align="center">
													<s:property value="billNumberAndDate" />
													&nbsp;
												</div></td>
											<td class="blueborderfortd"><div align="center">
													<a href="javascript:void(0);"
														onclick='viewVoucher(<s:property value="vhId"/>);'><s:property
															value="voucherNumberAndDate" /></a>&nbsp;
												</div></td>
											<td class="blueborderfortd"><div align="center">
													<a
														href='/EGF/report/chequeIssueRegisterReport-bankAdviceExcel.action?instrumentHeaderId=<s:property value="instrumentHeaderId" />'>
														<s:text name="instrument.bankadvice" />
													</a>
												</div></td>
											<s:if
												test="%{chequePrintingEnabled && chequePrintAvailableAt=='assignment' && chequeStatus=='New'}">
												<td class="blueborderfortd"><div align="center">
														<input type="button" value="Print"
															onclick="return printCheque(<s:property
						                           value="%{instrumentHeaderId}" />);"
															class="button" />
													</div></td>
											</s:if>
											<s:else>
												<td class="blueborderfortd"></td>
											</s:else>
										</tr>
									</s:iterator>

									<input type="hidden" name='chequeFormatId' id="chequeFormatId"
										value="<s:property value="chequeFormat"/>" />
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="excelpdf">
								<a
									href='/EGF/report/chequeIssueRegisterReport-generateXls.action?fromDate=<s:property value="fromDate"/>&toDate=<s:property value="toDate"/>&accountNumber.id=<s:property value="accountNumber.id"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&functionary.id=<s:property value="functionary.id"/>&fund.id=<s:property value="fund.id"/>&field.id=<s:property value="field.id"/>&bank=<s:property value="bank"/>'>Excel</a>
								<img align="absmiddle"
									src="/egi/resources/erp2/images/excel.png"> | <a
									href='/EGF/report/chequeIssueRegisterReport-generatePdf.action?fromDate=<s:property value="fromDate"/>&toDate=<s:property value="toDate"/>&accountNumber.id=<s:property value="accountNumber.id"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&functionary.id=<s:property value="functionary.id"/>&fund.id=<s:property value="fund.id"/>&field.id=<s:property value="field.id"/>&bank=<s:property value="bank"/>'>PDF</a>
								<img align="absmiddle" src="/egi/resources/erp2/images/pdf.png">
							</div>
						</td>
					</tr>

				</table>
			</td>
		</tr>
	</table>
</s:if>
<s:else>
	<h5 style="color: red">No data found</h5>
</s:else>
