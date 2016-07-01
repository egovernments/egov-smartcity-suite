/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

$(document).ready(function() {
	var sorTotal = 0;
	var nonSorTotal = 0;
	
	if($('#tenderFinalizedPerc').val() > 0) {
	  var tenderPercentage = $('#tenderPerc').html('<span class="sign-text">+</span>'+$('#tenderFinalizedPerc').val());
	}
	
	 $('#tblsor tr:not(:first)').each(function () {
		 var unitrate = $(this).find('input[name="unitrate"]').val(); 
		 var currMbEnrty = $(this).find('input[name="currMbEnrty"]').val(); 
		 var cumulativeQuantitycurrEnrty = $(this).find('input[name="cumulativeQuantitycurrEnrty"]').val(); 
		 var apprQuantity = $(this).find('input[name="apprQuantity"]').val();
         
         var amountCurrMBEntry = parseFloat(parseFloat(unitrate) * parseFloat(currMbEnrty)).toFixed(2);
         var cumulativeAmtCurrEnrty = parseFloat(parseFloat(unitrate) * parseFloat(cumulativeQuantitycurrEnrty)).toFixed(2);
         var apprAmt = parseFloat(parseFloat(unitrate) * parseFloat(apprQuantity)).toFixed(2);
         
         $(this).find('span[id="amountCurrentEntry"]').html(amountCurrMBEntry);
         $(this).find('span[id="cumulativeAmountCurrentEntry"]').html(amountCurrMBEntry);
         $(this).find('span[id="approvedAmount"]').html(apprAmt);
         if(cumulativeAmtCurrEnrty != 'NaN')
         sorTotal = parseFloat((parseFloat(sorTotal) + parseFloat(amountCurrMBEntry))).toFixed(2); 
        
	 });
	 
	 $('#tblnonsor tr:not(:first)').each(function () {
		 var unitrate = $(this).find('input[name="nonSorUnitrate"]').val(); 
		 var currMbEnrty = $(this).find('input[name="nonSorCurrMbEnrty"]').val(); 
		 var cumulativeQuantitycurrEnrty = $(this).find('input[name="nonSorCumulativeQuantityCurrEnrty"]').val(); 
		 var apprQuantity = $(this).find('input[name="nonSorApprQuantity"]').val();
         
         var amountCurrMBEntry = parseFloat(parseFloat(unitrate) * parseFloat(currMbEnrty)).toFixed(2);
         var cumulativeAmtCurrEnrty = parseFloat(parseFloat(unitrate) * parseFloat(cumulativeQuantitycurrEnrty)).toFixed(2);
         var apprAmt = parseFloat(parseFloat(unitrate) * parseFloat(apprQuantity)).toFixed(2);
         
         $(this).find('span[id="nonSorAmountCurrentEntry"]').html(amountCurrMBEntry);
         $(this).find('span[id="nonSorCumulativeAmountCurrentEntry"]').html(amountCurrMBEntry);
         $(this).find('span[id="nonSorApprovedAmount"]').html(apprAmt);
         if(cumulativeAmtCurrEnrty != 'NaN')
           nonSorTotal = parseFloat((parseFloat(nonSorTotal) + parseFloat(amountCurrMBEntry))).toFixed(2); 
	 });
	 $("#sorTotal").html(sorTotal);
	 $("#nonSorTotal").html(nonSorTotal);
	 var pageTotal = parseFloat((parseFloat(sorTotal) + parseFloat(nonSorTotal))).toFixed(2);  
	 $("#pageTotal").html(pageTotal);
});