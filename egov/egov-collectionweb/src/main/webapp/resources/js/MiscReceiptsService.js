/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
  function  populateService(serviceCategory){
		    dom.get('fundId').value="-1";
		    dom.get('functionId').value="-1";
        	populateserviceId({serviceCatId:serviceCategory.options[serviceCategory.selectedIndex].value});	
        }
        
        function loadFinDetails(service){
        
        	var dept = dom.get('deptId').value;
        	var service = dom.get('serviceId').value;
        	
        	var path = '/collection';
        	
        	var url1 = path+"/receipts/ajaxReceiptCreate-ajaxFinMiscDtlsByService.action?serviceId="+service+"&deptId="+dept;
        	var transaction = YAHOO.util.Connect.asyncRequest('POST', url1,loadMiscDetails, null);
        	
    		
        	var url2 = path+"/receipts/ajaxReceiptCreate-ajaxFinAccDtlsByService.action";
        	makeJSONCall(["glcodeIdDetail","glcodeDetail","accounthead","creditAmountDetail"]
        	,url2,{serviceId:service,deptId:dept},loadFinAccSuccessHandler,loadFinAccFailureHandler);
        
        	var url3 = path+"/receipts/ajaxReceiptCreate-ajaxFinSubledgerByService.action";
        	makeJSONCall(["subledgerCode","glcodeId","detailTypeId","detailTypeName","detailCode","detailKeyId",
        	"detailKey","amount"],url3,{serviceId:service,deptId:dept},loadFinSubledgerSuccessHandler,loadFinSubledgerFailureHandler);
        }

var miscArray;
var loadMiscDetails = {
success: function(o) {

		var result = o.responseText;
		
		if(null != result && result.length !=0){
		
			 miscArray = result.split('~');
				if(null != dom.get('fundId') ) {	
						 dom.get('fundId').value = parseInt(miscArray[0]);		
						 setFundId();
				}
				if(null != dom.get('schemeId') ){
						var url= "/collection/receipts/ajaxReceiptCreate-ajaxLoadSchemes.action";
						var fundId = dom.get('fundId').value;
        				makeJSONCall(["Text","Value"],url,{fundId:miscArray[0]},schemeDropDownSuccessHandler,schemeDropDownFailureHandler);
				}
				if(null != dom.get('subschemeId')  ){

						var url= "/collection/receipts/ajaxReceiptCreate-ajaxLoadSubSchemes.action";
						var schemeId = dom.get('schemeId').value;
        				makeJSONCall(["Text","Value"],url,{schemeId:miscArray[1]},subschemeDropDownSuccessHandler,subschemeDropDownFailureHandler);
						
				}
				
				if(null != dom.get('fundSourceId') ){
						var url= "/EGF/voucher/common-ajaxLoadFundSource.action";
						var subschemeId = dom.get('subschemeId').value;
        				makeJSONCall(["Text","Value"],url,{subSchemeId:miscArray[2]},fundsourceDropDownSuccessHandler,fundsourceDropDownFailureHandler);
		
				}
				
				if(null != dom.get('receiptMisc.idFunctionary.id') ){
						 dom.get('receiptMisc.idFunctionary.id').selectedIndex = parseInt(miscArray[4]);
				}
				if(null != dom.get('functionId') ) {	
					 dom.get('functionId').value = parseInt(miscArray[5]);		
			}
				
		}
    }
}

schemeDropDownSuccessHandler=function(req,res){
	
	 var schemeid = dom.get('schemeId');
	 var dropDownLength = schemeid.length;
	 var resLength =res.results.length+1;
	 for(i=0;i<res.results.length;i++){
	 		 schemeid.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
	 }
	 while(dropDownLength>resLength)
	{
     	schemeid.options[res.results.length+1] = null;
     	dropDownLength=dropDownLength-1;
	 }
	
	schemeid.value = miscArray[1];
	setSchemeId();
}

schemeDropDownFailureHandler=function(){
  bootbox.alert('failure while loading scheme drop down');
}


subschemeDropDownSuccessHandler=function(req,res){
	
	 var subschemeId = dom.get('subschemeId');
	 var dropDownLength = subschemeId.length;
	 var resLength =res.results.length+1;
	 for(i=0;i<res.results.length;i++){
	 		 subschemeId.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
	 }
	 while(dropDownLength>resLength)
	{
     	subschemeId.options[res.results.length+1] = null;
     	dropDownLength=dropDownLength-1;
	 }
	subschemeId.value = miscArray[2];
	setFundSourceId();
}

subschemeDropDownFailureHandler=function(){
  bootbox.alert('failure while loading sub scheme drop down');
}


fundsourceDropDownSuccessHandler=function(req,res){
	
	 var fundSourceId = dom.get('fundSourceId');
	 var dropDownLength = fundSourceId.length;
	 var resLength =res.results.length+1;
	 for(i=0;i<res.results.length;i++){
	 		 fundSourceId.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
	 }
	 while(dropDownLength>resLength)
	{
     	fundSourceId.options[res.results.length+1] = null;
     	dropDownLength=dropDownLength-1;
	 }
	fundSourceId.value = miscArray[3];
	setSubSchemeId();
}

fundsourceDropDownFailureHandler=function(){
  bootbox.alert('failure while loading fundource drop down');
}



loadFinAccSuccessHandler=function(req,res){
      var noOfRows =  billCreditDetailsTable.getRecordSet().getLength();
      billCreditDetailsTable.deleteRows(0,noOfRows); 
      billDetailTableIndex = 0;
	  billCreditDetailsTable.addRow({SlNo:billCreditDetailsTable.getRecordSet().getLength()+1,
             "glcodeid":"",
             "glcode":"",
             "accounthead":"",
             "creditamount":""
         });      
	  updateGridMisc(VOUCHERCREDITDETAILLIST,'creditAmountDetail',0,"0");
	  totalcramt = "0";          
	  billDetailTableIndex = 1;
	for(i=0;i<res.results.length-1;i++){
	  	 billCreditDetailsTable.addRow({SlNo:billCreditDetailsTable.getRecordSet().getLength()+1,
                    "glcodeid":res.results[i].glcodeIdDetail,
                    "glcode":res.results[i].glcodeDetail,
                    "accounthead":res.results[i].accounthead,
                    "creditamount":res.results[i].creditAmountDetail
                });
                updateAccountTableIndex();  
       }
       
        for(i=0;i<res.results.length;i++){  
                updateGridMisc(VOUCHERCREDITDETAILLIST,'glcodeIdDetail',i,res.results[i].glcodeIdDetail);
                updateGridMisc(VOUCHERCREDITDETAILLIST,'glcodeDetail',i,res.results[i].glcodeDetail);
                updateGridMisc(VOUCHERCREDITDETAILLIST,'accounthead',i,res.results[i].accounthead);
                updateGridMisc(VOUCHERCREDITDETAILLIST,'creditAmountDetail',i,res.results[i].creditAmountDetail);
                totalcramt = parseFloat(totalcramt)+parseFloat(res.results[i].creditAmountDetail);
                if(totalcramt>0){
            		totalcramt=parseInt(totalcramt);
       		 	}
              
        }
		 document.getElementById('totalcramount').value=totalcramt;
		 updatetotalAmount();
		patternvalidation();
    }
 loadFinAccFailureHandler=function(){
  bootbox.alert('unable to load Function');
}


loadFinSubledgerSuccessHandler=function(req,res){

	 var noOfRows =  subLedgersTable.getRecordSet().getLength();
	 subLedgersTable.deleteRows(0,noOfRows); 
	 slDetailTableIndex = 0;
	 subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
         "glcode":"",
         "glcode.id":"",
         "detailType.id":"",
         "detailTypeName":"",
         "detailCode":"",
         "detailKeyId":"",
         "detailKey":"",
         "amount":""
       
     });
	 updateSLGrid('amount',0,"0");
	 slDetailTableIndex = 1;
	 for(i=0;i<res.results.length-1;i++){
	  			 subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
                    "glcode":res.results[i].subledgerCode,
                    "glcode.id":res.results[i].glcodeId,
                    "detailType.id":res.results[i].detailTypeId,
                    "detailTypeName":res.results[i].detailTypeName,
                    "detailCode":res.results[i].detailCode,
                    "detailKeyId":res.results[i].detailKeyId,
                    "detailKey":res.results[i].detailKey,
                    "amount":res.results[i].amount
                  
                });
                 updateSLTableIndex();
            }
        for(i=0;i<res.results.length;i++){
              
                updateGridSLDropdown('glcode.id',i,res.results[i].glcodeId,res.results[i].subledgerCode);
                updateGridSLDropdown('detailType.id',i,res.results[i].detailTypeId,res.results[i].detailTypeName);
                updateSLGrid('detailCode',i,res.results[i].detailCode);
                updateSLGrid('detailKeyId',i,res.results[i].detailKeyId);
                updateSLGrid('detailKey',i,res.results[i].detailKey);
                updateSLGrid('amount',i,res.results[i].amount);
               
                
        }
        patternvalidation();
}

 loadFinSubledgerFailureHandler=function(){
  bootbox.alert('Unable to load Sub Ledger');
}
