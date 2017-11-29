
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


		<div class="subheadsmallnew"><s:text name="challan.title.challanDetails"/></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" id="bobcontent3">
			
		      	<tr>   
			        <td width="9%" class="bluebox">&nbsp;</td>
			        <td width="19%" class="bluebox"><s:text name="challan.date"/></td>
			        <td width="22%" class="bluebox">
			        	<s:date name="challan.challanDate" var="challanDateFormat" format="dd/MM/yyyy"/>
			        	<s:textfield id="challanDate" name="challan.challanDate" value="%{challanDateFormat}" data-inputmask="'mask': 'd/m/y'"/></td>
			        <td width="17%" class="bluebox"><s:text name="challan.validuptodate"/></td>
			        <td width="33%" class="bluebox">
			        	<s:date name="challan.validUpto" var="challanDateFormat" format="dd/MM/yyyy"/>
						<s:textfield id="challanDate" name="challan.validUpto" value="%{challanDateFormat}" readonly="true" /></td>
		      	</tr>
		      	<tr>
			        <td class="greybox">&nbsp;</td>
			        <td class="greybox"><s:text name="challan.payeename"/></td>
			        <td class="greybox"><s:textfield name="payeeName" id="payeeName" value="%{payeeName}" readonly="true" /></td>
			        <td class="greybox">&nbsp;</td>
			        <td class="greybox">&nbsp;</td>
			        <!-- <td class="greybox"><s:text name="challan.payeeAddress"/></td>
			        <td class="greybox"><s:textarea name="payeeAddress" id="payeeAddress" value="%{payeeAddress}" cols="30" rows="2" readonly="true"/></td> -->
		      	</tr>
		      	<tr>
			        <td class="bluebox">&nbsp;</td>
			        <td class="bluebox"><s:text name="challan.payeeAddress"/></td>
			        <td class="bluebox"><s:textarea name="payeeAddress" id="payeeAddress" value="%{payeeAddress}" cols="18" rows="2" readonly="true"/></td>
			        <td class="bluebox"><span class="bluebox"><s:text name="challan.narration"/></span></td>
			        <td class="bluebox"><s:textarea name="referenceDesc" id="referenceDesc"  value="%{referenceDesc}" cols="18" rows="2" readonly="true"/></td>
		      	</tr>
		      	
		      	<tr>
			        <td class="greybox">&nbsp;</td>
			        <td class="greybox"><s:text name="challan.voucherNumber"/></td>
			        <td class="greybox"><s:textfield name="voucherNumber" id="voucherNumber" value="%{voucherNumber}" readonly="true"/></td>
			        <s:if test="%{shouldShowHeaderField('billNumber')}">
			        	<td class="greybox"><s:text name="challan.billNumber"/></td>
			        	<td class="greybox"><s:textfield name="referencenumber" id="referencenumber" value="%{referencenumber}" readonly="true"/></td>
			        </s:if>
			        <s:else>
			   			<td class="greybox">&nbsp;</td>
			   			<td class="greybox">&nbsp;</td>
			   		</s:else>
			   	</tr>
			   	
		     	<s:if test="%{shouldShowHeaderField('fund') || shouldShowHeaderField('department')}">
		      	<tr>
			        <td class="bluebox">&nbsp;</td>
			        <s:if test="%{shouldShowHeaderField('fund')}">
			        	<td class="bluebox"><s:text name="challan.fund"/></td>
			        	<td class="bluebox"><span class="greybox">
			          		<s:textfield name="fundName" id="fundName" value="%{receiptMisc.fund.name}" readonly="true"/>
			          		</span></td>
			        </s:if>
			        <s:else>
  						<td class="bluebox" colspan="2"></td>
  					</s:else>
  					<s:if test="%{shouldShowHeaderField('department')}">
				        <td class="bluebox"><s:text name="challan.department"/></td>
				        <td class="bluebox"><s:textfield name="receiptMisc.department.name" id="receiptMisc.department.name" value="%{receiptMisc.department.name}" readonly="true"/></td>
			        </s:if>
		   			<s:else>
  						<td class="bluebox" colspan="2"></td>
  					</s:else>
		      	</tr>
		      	</s:if>
		      	<s:if test="%{shouldShowHeaderField('field')}">
		      	<tr>
                        <td class="greybox">&nbsp;</td>
			        	<td class="greybox"><s:text name="challan.field"/></td>
			       		<td class="greybox"><span class="bluebox"><s:textfield name="receiptMisc.boundary.name" id="boundaryId" value="%{receiptMisc.boundary.localName}" readonly="true"/> 
			          </span></td>
			            <td class="greybox" colspan="2"></td>
  				</tr>
		      	</s:if>
		      	<s:if test="%{shouldShowHeaderField('service')}">
		      	<tr>
			        <td class="bluebox">&nbsp;</td>
			        <td class="bluebox"><span class="bluebox"><s:text name="challan.service"/></span></td>
			        <td class="bluebox"><s:textfield name="serviceName" id="serviceName"  value="%{challan.service.name}" readonly="true"/></td>
			        <td class="bluebox">&nbsp;</td>
			        <td class="bluebox">&nbsp;</td>
			    </tr>
			    </s:if>
		      	<tr>
			        <td class="bluebox">&nbsp;</td>
			        <td class="bluebox">&nbsp;</td>
			        <td class="bluebox">
			        	<input type="button" name="Show Account Details" id="ssd" value="Account Details"  onclick="displaydiv('capsearch');" class="button" /></td> 
			        		<td class="bluebox">
							<input type="button" name="Show Subledger Details" id="ssd" value="Subledger Details"  onclick="displaydiv('capsearch2');" class="button" /></td>
			        		<td class="bluebox">&nbsp;</td>
		      	</tr>
	    	</table> <hr /> <!-- End of table with challan header details -->
		</td>
		</tr>
		<tr >
		<td colspan="5">
		<div class="switchgroup1" id="capsearch" style="display:none" >
			<div class="subheadsmallnew"><s:text name="challan.billdetails"/></div>
      		<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
	        <tr>
	          <td class="blueborderfortd"><div class="billscroller">
	            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
	              <tr>
	                <th class="bluebgheadtd" width="14%" >Function</th>
	                <th class="bluebgheadtd" width="14%" >Account Code</th>
	                <th class="bluebgheadtd" width="64%" >Description</th>
	                <th class="bluebgheadtd" width="11%" >Amount (Rs.)</th>
	                <th class="bluebgheadtd" width="11%" >Financial Year</th>
	              </tr>
	              <s:iterator value="billDetailslist" >
		              <tr>
		              		 <td class="blueborderfortd"><s:property value="%{functionDetail}" />&nbsp;</td>
			                <td class="blueborderfortd"><s:property value="%{glcodeDetail}" /></td>
			                <td class="blueborderfortd"><s:property value="%{accounthead}" /></td>
			                <td class="blueborderfortd"><div align="center">
			                  <input name="textfield3" type="text" class="amount" value=<s:property value="%{creditAmountDetail}" /> readonly="true" size="12"/>
			                  </div></td>
			                <td class="blueborderfortd"><s:property value="%{financialYearRange}" /></td>
		               </tr>
	              </s:iterator>
              </table> <!-- End of account details contents table -->
            </div>
            </td>
          	</tr>
         </table> <!-- End of account details table -->
      </div> <!-- end of switchgroup1 div -->
	</td>
	</tr>
	<tr>
    <td  colspan="5">
    	<div  class="switchgroup1" id="capsearch2" style="display:none">
	    	<div class="subheadsmallnew">Subledger Details</div>
	      	<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
	        <tr>
	        <td class="blueborderfortd"><div class="billscroller">
	            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
	              <tr>
	                <th class="bluebgheadtd" width="14%" >Account Code</th>
	                <th class="bluebgheadtd" width="64%" >Type</th>
	                <th class="bluebgheadtd" width="11%" >Code</th>
	                <th class="bluebgheadtd" width="11%" >Name</th>
	                <th class="bluebgheadtd" width="11%" >Amount (Rs.)</th>
	                </tr>
	                <s:iterator value="subLedgerlist" >
			            <tr>
			                <td class="blueborderfortd"><s:property value="%{glcodeDetail}" />&nbsp;</td>
			                <td class="blueborderfortd"><s:property value="%{detailTypeName}" />&nbsp;</td>
			                <td class="blueborderfortd"><s:property value="%{detailCode}" />&nbsp;</td>  
			                <td class="blueborderfortd"><s:property value="%{detailKey}" />&nbsp;</td>
			                <td class="blueborderfortd"><div align="center">
				                   <input name="textfield3" type="text" class="amount" value=<s:property value="%{amount}" /> readonly="true" size="12"/>
				                  </div></td>
		                </tr>
	                </s:iterator>
	              </table> <!-- End of subledger details scroller table -->
	            </div>
	        </td>
	        </tr>
	        </table>
      </div>
