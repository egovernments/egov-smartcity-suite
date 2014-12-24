 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<script>
	var responseLineTable;
	var PERCENTAGE="Percentage";
	
	function updateTextBoxFormatterForName(field,index,value)
  	{
  	  	var records= responseLineTable.getRecordSet();
  		dom.get(field+records.getRecord(eval(index)).getId()).value=escapeSpecialChars(value);
  	}
  	
  	function escapeSpecialChars(str) {
   		str1 = str.replace(/\'/g, "&#39;");
		str2 = str1.replace(/\"/g, '&quot;');
		str3 = str2.replace(/\r\n/g, "&#13;");
		return str3;
	}
	
	// For Temparary Variable...
	function createTextBoxFormatterwithoutList(size,maxlength,name){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
   			var fieldName=name+"["+oRecord.getCount()+"]";
   			markup="<div align='center'><input type='label' id='"+id+"' readonly='true' name='"+fieldName+"' value='"+value+"' size='"+size+"'  /></div>";
   			el.innerHTML = markup;
  		}
		return textboxFormatter;
	}
	
	function createTextBoxFormatterwithoutListWithNumeric(size,maxlength,name){
		var textboxFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
   			var fieldName=name+"["+oRecord.getCount()+"]";
   			markup="<div align='center'><input type='text' id='"+id+"' readonly='true' name='"+fieldName+"' value='"+value+"' size='"+size+"' style='text-align:right'  /></div>";
   			el.innerHTML = markup;
  		}
		return textboxFormatter;
	}
   
   
	function createTenderResponseLineIdFormatter(el, oRecord, oColumn){
		var hiddenFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
    		var fieldName = "responseLineList[" + oRecord.getCount() + "]." + oColumn.getKey();
    		markup="<div align='center'><input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/></div>";
    		el.innerHTML = markup;
		}
		return hiddenFormatter;
	}
	var tenderResponseIdFormatter = createTenderResponseLineIdFormatter(10,10);
	
	function createTenderResponseKeyIdFormatter(el, oRecord, oColumn){
		var hiddenFormatter = function(el, oRecord, oColumn, oData) {
   			var value = (YAHOO.lang.isValue(oData))?oData:"";
    		var id=oColumn.getKey()+oRecord.getId();
    		var fieldName = "responseLineList[" + oRecord.getCount() + "]." + oColumn.getKey()+".id";
    		markup="<div align='center'><input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+value+"'/></div>";
    		el.innerHTML = markup;
		}
		return hiddenFormatter;
	}
	var tenderResponseKeyIdFormatter = createTenderResponseKeyIdFormatter(10,10);

   /**
    *   For Numeric field
    */
   function createTextBoxFormatterForNumeric(size,maxlength) {
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   var id = oColumn.getKey()+oRecord.getId();
	   var value = (YAHOO.lang.isValue(oData))?oData:"";
	   var fieldName = "responseLineList[" + oRecord.getCount() + "]." + oColumn.getKey();
	   var method="checkQuotedRate(\""+oRecord.getId()+"\",this);";
       markup="<input type='text' id='"+id+"'  value='"+value+"'  name='"+fieldName+"' size='"+size+"' maxlength='"+maxlength+"' style='text-align:right' onblur='"+method+"'/>";
	   el.innerHTML = markup; 
	  }
		return textboxFormatter;	
 	}	
	var textboxFormatterWithNumeric= createTextBoxFormatterForNumeric(15,15);
	
	  
	/**
	 *     For Text field
	 */
	function createTextBoxFormatter(size,maxlength) {
	var textboxFormatter = function(el, oRecord, oColumn, oData) {
	   var id = oColumn.getKey()+oRecord.getId();
	   var value = (YAHOO.lang.isValue(oData))?oData:"";
	   var fieldName = "responseLineList[" + oRecord.getCount() + "]." + oColumn.getKey();
       markup="<input type='text' id='"+id+"'  value='"+value+"' size='"+size+"' name='"+fieldName+"' maxlength='"+maxlength+"'  />";
	   el.innerHTML = markup; 
	}
	return textboxFormatter;	
 }

	var textboxFormatter= createTextBoxFormatter(30,50);
	
	var makeResponseLineTable = function() {
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var contractorColumnDefs = [ 
	    {key:"id", hidden:true,sortable:false, resizeable:false,formatter:tenderResponseIdFormatter},
	    {key:"uom", hidden:true,sortable:false, resizeable:false,formatter:tenderResponseKeyIdFormatter},
	    {key:"tenderableEntity",hidden:true, sortable:false, resizeable:false,formatter:tenderResponseKeyIdFormatter},
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false},
		{key:"item", label:'<div align="center"><s:text name="entity"/></div>', sortable:false, resizeable:false,formatter:createTextBoxFormatterwithoutList(30,11,"item")},
		{key:"uomName",  label:'<div align="center"><s:text name="uom"/></div>',sortable:false, resizeable:false,formatter:createTextBoxFormatterwithoutList(15,11,"uomName")},
		{key:"quantity", label:'<div align="center"><s:text name="quantity"/></div>', formatter:textboxFormatterWithNumeric, sortable:false, resizeable:false,readonly:true},
		{key:"estimatedRate", label:'<div align="center"><s:text name="estimatedRatePerUnit"/></div>', formatter:createTextBoxFormatterwithoutListWithNumeric(15,11,"estimatedRate"), sortable:false, resizeable:false},
		{key:"totalEstimatedRate", label:'<div align="center"><s:text name="totalEstimatedRate"/></div>', formatter:createTextBoxFormatterwithoutListWithNumeric(15,11,"totalEstimatedRate"), sortable:false, resizeable:false},
		{key:"bidRate", label:'<div align="center"><s:text name="quotedRatePerUnit"/><span class="mandatory"> *</span></div>', formatter:textboxFormatterWithNumeric, sortable:false, resizeable:false},
		{key:"totalBidRate", label:'<div align="center"><s:text name="totalBidRate"/></div>', formatter:createTextBoxFormatterwithoutListWithNumeric(15,11,"totalBidRate"), sortable:false, resizeable:false}
		
	];
	
	var contractorDataSource = new YAHOO.util.DataSource(); 
	responseLineTable = new YAHOO.widget.DataTable("responseLineTable",contractorColumnDefs, contractorDataSource, {MSG_EMPTY:"<s:text name='nothing to display'/>"});
	responseLineTable.subscribe("cellClickEvent", responseLineTable.onEventShowCellEditor); 
	responseLineTable.on('cellClickEvent',function (oArgs) {
		var target = oArgs.target;
		var record = this.getRecord(target);
		var column = this.getColumn(target);
		
	});
	
	return {
	    oDS: contractorDataSource,
	    oDT: responseLineTable
	};  
}	

	//This method is called on the load of the page
	function init()
	{

		document.getElementById('noticeType').disabled = true;
		
		<s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
		      if(document.getElementById('tenderType').value == PERCENTAGE){
		         	dom.get('tenderQuotedratelbldiv').style.display='';
		         	dom.get('tenderQuotedrateDiv').style.display='';
		         }
		</s:if>
		if(document.getElementById('autoGenerateNumberFlag').value=="false")
			document.getElementById('number').readOnly=false;
		 
		 var records= responseLineTable.getRecordSet();
		 var i=0;
  		 while(i<records.getLength())
  		 {
   			dom.get("quantity"+records.getRecord(i).getId()).readOnly=true;
			i++;
  		 }
  		 
  		 if(document.getElementById('mode').value == "view")
  		 {
  		      for ( var i = 0; i < document.forms[0].length; i++) {
				if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].name != 'generateNotice'
				     && document.forms[0].elements[i].name != 'generatejusNotice')
					document.forms[0].elements[i].disabled = true;
			}
  		 }
  		 else if(document.getElementById('mode').value == "notmodify")
  		 {
  		
  		      disablingFieldsInModify();
  		 }
  		 
  		<s:if test="%{wfStatus!='APPROVED'}">	
			document.getElementById('status').disabled = true;
		</s:if>
		<s:elseif test="%{mode!='view'}">
			document.getElementById('status').disabled = false;
		</s:elseif>
		
		 if(document.getElementById('tenderType').value == PERCENTAGE){
		    i=0;
	    	while(i<records.getLength())
	    	{
			    dom.get("bidRate"+records.getRecord(i).getId()).readOnly=true;
       			i++;
	    	}
		 }
		 if(document.getElementById('idTemp').value!=null 
		 			&& document.getElementById('idTemp').value!='' )
		     document.getElementById('responseDate').disabled =true;
		 
		 
	}
	
	
	//This method is to reset the form
	function resetForm()
	{
	    document.getElementById('responseDate').value="";
	    if(dom.get('tenderTypediv')!=null)
	          document.getElementById('tenderType').value="";
	    if(document.getElementById('autoGenerateNumberFlag').value=="false")
	       document.getElementById('number').value="";
	    <s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
	         document.getElementById('bidderId').value="-1";
	         showPercentage();
	    </s:if>
	    <s:else>
	      	 document.getElementById('owner.firstName').value="";
	      	 document.getElementById('bidderAddress.streetAddress1').value="";
	      	 document.getElementById('owner.officePhone').value="";
	      	 document.getElementById('ratePerUnit').value="";
	     </s:else>
	     
	    var records= responseLineTable.getRecordSet();
		var i=0;
	    while(i<records.getLength())
	    {
			    dom.get("bidRate"+records.getRecord(i).getId()).value="";
			    dom.get("totalBidRate"+records.getRecord(i).getId()).value="";
       			i++;
	    }
	}
	
	//For enabling all the field before submitting the form
	function enablingFields()
	{
		for(var i=0;i<document.forms[0].length;i++)
		{
			document.forms[0].elements[i].disabled =false;
		}
	}
	
	//This method is to validate the form
	function validateForm(workflow)
	{
	  document.getElementById('workFlowType').value=workflow;
		if(dom.get('fielderror')!=null)
			dom.get("fielderror").style.display = 'none';
		
		if(!checkStringMandatoryField('responseDate','Date','tenderResponse_error'))
	    		return false;
	    
	    if(document.getElementById('autoGenerateNumberFlag').value=="false")
	    {
	        if(!checkStringMandatoryField('number','Bid Response Number','tenderResponse_error'))
	    		return false;
	    }
	    
	    if(!checkStringMandatoryField('status','<s:text name="status.lbl"/>','tenderResponse_error'))
	    		return false;
	    
	     if(!validateResponseWithNoticeDate(document.getElementById('responseDate')))
	         return false;
	   
	    <s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
		//for supplier and contractor field.	
			if(document.getElementById('bidderId').value=="-1" || trimAll(document.getElementById('bidderId').value)=="")
			{
				dom.get("tenderResponse_error").style.display = '';
				document.getElementById("tenderResponse_error").innerHTML = '<s:property value="%{notice.tenderFileType.bidderType}" />' +' is Required';
			    return false;  
			}
		</s:if>
		<s:else>
			if(!checkStringMandatoryField('owner.firstName','Name Of Bidder','tenderResponse_error'))
	    		return false;
	    	if(!checkStringMandatoryField('bidderAddress.streetAddress1','Address Of Bidder','tenderResponse_error'))
	    		return false;
	    	if(!checkStringMandatoryField('owner.officePhone','Contact Number','tenderResponse_error'))
	    		return false;
	    	if(!checkStringMandatoryField('ratePerUnit','Rate Per Sq.ft','tenderResponse_error'))
	    		return false;
		</s:else>
		
		//For Tender Type=Percentage Quoted Rate Validation 
		if(dom.get('tenderTypediv')!=null){
		   	if(!checkStringMandatoryField('tenderType','Tender Type','tenderResponse_error'))
	    		return false;
	    	else if(document.getElementById('tenderType').value == PERCENTAGE && trimAll(document.getElementById('percentage').value)==""){
	    		dom.get("tenderResponse_error").style.display = '';
	    		document.getElementById("tenderResponse_error").innerHTML = '<s:text name="quotedRate.required" />';
	    		return false;
	    	}
	    }
	    
	    
	    var noticetypeselected= document.getElementById('noticeType').options[document.getElementById('noticeType').selectedIndex].text;
	    
	    if(noticetypeselected=="Works Indent"){
	    	    
	    //Modified Line level Quoted rate Validation for Works Indent...
	    if(dom.get('tenderTypediv')==null || (dom.get('tenderTypediv')!=null && document.getElementById('tenderType').value != PERCENTAGE))
	    {
	    	  var wfStatus = document.getElementById("wfStatus").value;
	    	 if((workflow =='approve' || workflow =='savesubmit' || workflow=='forward' || workflow =='Forward' || workflow =='Approve' || wfStatus=='APPROVED'))
	    	 
	    	  {
	    	     var statusselected= document.getElementById('status').options[document.getElementById('status').selectedIndex].text;
	    	   
		          var records= responseLineTable.getRecordSet();
			 	  var i=0;
			 	  var count =records.getLength();
	  		      while(i<records.getLength())
	  		      {
	   			      if(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value)=="" )
				       {
				           count=count-1;
				       }
				       else if(eval(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value))==0)
				       {
				            count=count-1;
				       }
				       i++;
	  		       }
	  		     
	  		       if(count<1){
	  		       dom.get("tenderResponse_error").style.display = '';
		    	   document.getElementById("tenderResponse_error").innerHTML = 'Select Quoted Rate for atleast one line';
	  		          return false;
	  		       }
	    	  }
	    }
	    
	    }else{
	    
	    //Line level Quoted rate Validation...
	    if(dom.get('tenderTypediv')==null || (dom.get('tenderTypediv')!=null && document.getElementById('tenderType').value != PERCENTAGE))
	    {
	    	  var wfStatus = document.getElementById("wfStatus").value;
	    	  if((workflow =='approve' || workflow =='savesubmit' || workflow=='forward' || workflow =='Forward' || workflow =='Approve' || wfStatus=='APPROVED'))
	    	  {
		          var records= responseLineTable.getRecordSet();
			 	  var i=0;
	  		      while(i<records.getLength())
	  		      {
	   			      if(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value)=="" )
				       {
				            dom.get("tenderResponse_error").style.display = '';
		    				document.getElementById("tenderResponse_error").innerHTML = '<s:text name="quotedRate.validate"/>'+(i+1);
		    				dom.get("bidRate"+records.getRecord(i).getId()).value="";
		    				return false;
				       }
				       else if(eval(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value))==0)
				       {
				            dom.get("tenderResponse_error").style.display = '';
		    				document.getElementById("tenderResponse_error").innerHTML = '<s:text name="quotedRate.validate.zero"/>'+(i+1);
		    				dom.get("bidRate"+records.getRecord(i).getId()).value="";
		    				return false;
				       }
				       i++;
	  		       }
	    	  }
	    }
	    
	    }
    
	       if(noticetypeselected=="Works estimate"){
	    	    
	    //Modified Line level Quoted rate Validation for Works Indent...
	    if(dom.get('tenderTypediv')==null || (dom.get('tenderTypediv')!=null && document.getElementById('tenderType').value == PERCENTAGE))
	    {
	      var records= responseLineTable.getRecordSet();
			 	  var i=0;
	  		      while(i<records.getLength())
	  		      {
	   			      if(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value)=="" )
				       {
				            dom.get("tenderResponse_error").style.display = '';
		    				document.getElementById("tenderResponse_error").innerHTML = 'Rates for activity in the line'+(i+1)+' is blank,please cancel the Tender Notice,Tender File and edit the estimate to proceed again';
		    				return false;
				       }
				       else if(eval(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value))==0)
				       {
				            dom.get("tenderResponse_error").style.display = '';
		    				document.getElementById("tenderResponse_error").innerHTML = 'Rates for activity in the line'+(i+1)+' is zero,please cancel the Tender Notice,Tender File and edit the estimate to proceed again';		    				
		    				return false;
				       }
				       i++;
	  		       }
	    }
	    }
	   if((workflow =='approve' || workflow =='savesubmit') && document.getElementById('approverName').value=="-1")
	   {
	       dom.get("tenderResponse_error").style.display = '';
	       document.getElementById("tenderResponse_error").innerHTML = '<s:text name="tenderresponse.approver"/>';
	       return false;
	   }
	   
	   
	  // return false;
	}
	
	
	// This method is called on change of Tendertype..
	
	function showPercentage()
	{
	 	 if(document.getElementById('tenderType').value == PERCENTAGE){
		   	dom.get('tenderQuotedratelbldiv').style.display='';
		   	dom.get('tenderQuotedrateDiv').style.display='';
		 }
		 else
		 {
		    dom.get('tenderQuotedratelbldiv').style.display='none';
		   	dom.get('tenderQuotedrateDiv').style.display='none';
		   	document.getElementById('percentage').value="";
		 }
		 calculateBidRateWithPercentage();
	}
	
	function checkQuotedRate(recordId,obj)
	{
	       checkUptoFourDecimalPlace(obj,'tenderResponse_error','Quoted Rate');
	       
	       if(obj.value !="" &&  dom.get("quantity"+recordId).value != ""){
	         // dom.get("totalBidRate"+recordId).value=Math.round(eval((dom.get("bidRate"+recordId).value) * eval(dom.get("quantity"+recordId).value))*100/100);
	         dom.get("totalBidRate"+recordId).value=(eval(dom.get("bidRate"+recordId).value) * eval(dom.get("quantity"+recordId).value)).toFixed(4);
	       }
	       else if(obj.value !="" && (dom.get("quantity"+recordId).value == "" || eval(dom.get("quantity"+recordId).value ==0)))
	           dom.get("totalBidRate"+recordId).value=eval(dom.get("bidRate"+recordId).value);
	       else
	           dom.get("totalBidRate"+recordId).value="";
	}
	
	/**
	  *  This method is called on load of the Negotiation page
	  */
	  
	function initNegotiation()
	{
	    var records= responseLineTable.getRecordSet();
		var i=0;
	    var mode=document.getElementById('mode').value;
	    <s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
		      if(document.getElementById('tenderType').value == PERCENTAGE){
		         	dom.get('tenderQuotedratelbldiv').style.display='';
		         	dom.get('tenderQuotedrateDiv').style.display='';
		         }
		</s:if>
	    
	    if(mode == "new"){
	       for ( var i = 0; i < document.forms[0].length; i++) {
			 if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].id != 'save' 
			      && document.forms[0].elements[i].id != 'responseDate')
				document.forms[0].elements[i].disabled = true;
		   }
		   i=0;
  		   while(i<records.getLength())
  		   {
   			  dom.get("bidRate"+records.getRecord(i).getId()).disabled = false;
			  i++;
  		   }
  		   
  		 <s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
		      if(document.getElementById('tenderType').value == PERCENTAGE){
		         	dom.get('sign').disabled=false;
		         	dom.get('percentage').disabled=false;
		         }
		</s:if>
  		}
  		
  		if(mode == "modify")
  		{
  		   for ( var i = 0; i < document.forms[0].length; i++) {
			 if (document.forms[0].elements[i].name != 'close' && document.forms[0].elements[i].id != 'save'
			            && document.forms[0].elements[i].name != 'status') 
				document.forms[0].elements[i].disabled = true;
		   }
  		}
  		
  		if(mode == "view")
  		{
  		   for ( var i = 0; i < document.forms[0].length; i++) {
			 if (document.forms[0].elements[i].name != 'close' ) 
				document.forms[0].elements[i].disabled = true;
		   }
  		}
  		
  		 <s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >
  		  if(document.getElementById('tenderType').value == PERCENTAGE){
		    i=0;
	    	while(i<records.getLength())
	    	{
			    dom.get("bidRate"+records.getRecord(i).getId()).readOnly=true;
			   // dom.get("totalBidRate"+records.getRecord(i).getId()).value="";
       			i++;
	    	}
		 }
		 </s:if>
  		 
	}
	
	/**
	  *  This method is to validate Negotiation form
	  */
	  
	function validateNegotiationForm(workflow)
	{
	    if(dom.get('fielderror')!=null)
			dom.get("fielderror").style.display = 'none';
		
		if(!checkStringMandatoryField('responseDate','Negotiation Date','tenderResponse_error'))
	    	return false;
	    if(dom.get('tenderTypediv')==null || (dom.get('tenderTypediv')!=null && document.getElementById('tenderType').value != PERCENTAGE))
	    {
	          var records= responseLineTable.getRecordSet();
		 	  var i=0;
  		      while(i<records.getLength())
  		      {
   			      if(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value)=="")
			       {
			            dom.get("tenderResponse_error").style.display = '';
	    				document.getElementById("tenderResponse_error").innerHTML = '<s:text name="quotedRate.validate" />'+(i+1);
	    				return false;
			       }
			       else if(eval(trimAll(dom.get("bidRate"+records.getRecord(i).getId()).value))==0)
			       {
			            dom.get("tenderResponse_error").style.display = '';
	    				document.getElementById("tenderResponse_error").innerHTML = '<s:text name="quotedRate.validate.zero"/>'+(i+1);
	    				dom.get("bidRate"+records.getRecord(i).getId()).value="";
	    				return false;
			       }
			       i++;
  		       }
	    }
	    document.getElementById('workFlowType').value=workflow;
	}
	
	/*
	 *    This method is to generate Negotiation report
	 */
	 
	 function generateNegotiationReport()
	 {
	      var responseId=document.getElementById('idTemp').value;
	      var submitForm = document.createElement("FORM");
	 	  document.body.appendChild(submitForm);
 	 	  submitForm.method = "POST";
	      submitForm.action= "${pageContext.request.contextPath}/tenderresponse/tenderNegotiation!print.action?responseId="+responseId;
	      submitForm.submit();
	      return false;
	 }
	
	/**
	 *    This method is to generate Justification Report.
	 */
	  
	function generateJustificationReport()
	{
	     var responseId=document.getElementById('idTemp').value;
	     var submitForm = document.createElement("FORM");
	 	 document.body.appendChild(submitForm);
 	 	 submitForm.method = "POST";
	     submitForm.action= "${pageContext.request.contextPath}/tenderjustification/tenderJustification!print.action?responseId="+responseId;
	     submitForm.submit();
    	 return false;
	}
	
	function validateBidderForResponse(obj)
	{
	    if(obj.value!=-1){
	     	var id = document.getElementById('id').value;
	    	var bidderId = obj.value;
	    	var tenderUnit = document.getElementById('tenderUnit').value; 
	    	var url = "${pageContext.request.contextPath}/tenderresponse/ajaxTenderResponse!checkResponseForBidder.action?idTemp="+id+"&bidderId="+bidderId+"&tenderUnit="+tenderUnit;
			var req = initiateRequest();
        	req.onreadystatechange = function()
			{
	       		if(req.readyState==4)
	       		{
	           		 if (req.status == 200){
                      if(req.responseText=="true"){
                        dom.get("tenderResponse_error").style.display = '';
	    				document.getElementById("tenderResponse_error").innerHTML = '<s:property value="%{notice.tenderFileType.bidderType}" />'+' '+'<s:text name="response.exist.bidder.ui"/>';
	    				obj.value = -1;
	    				resetcombo();
	    				return false;
                      }
                      else{
                        dom.get("tenderResponse_error").style.display = 'none';
                      }
				     }
				           
	       	     }
		  	};
         	req.open("GET", url, true);
		 	req.send(null);
		 }   
	}
	
	function disablingFieldsInModify()
	{
	     document.getElementById('responseDate').disabled=true;
	     document.getElementById('number').disabled=true;
	     if(dom.get('tenderTypediv')!=null ){
	        if(document.getElementById('tenderType').value == PERCENTAGE) {
	             document.getElementById('percentage').disabled=true;
	             document.getElementById('sign').disabled=true;
	          }
	        document.getElementById('tenderType').disabled=true;
	     }
	     var records= responseLineTable.getRecordSet();
		 var i=0;
  		 while(i<records.getLength())
  		 {
   				dom.get("bidRate"+records.getRecord(i).getId()).disabled=true;
   				i++;
         }
        <s:if test="%{tenderBidderType.BIDDERTYPE.get(0)!='owner'}" >

		   document.getElementById('bidderId').disabled=true;
		   disablecombo();
		  

		</s:if>
		<s:else>
		     document.getElementById('owner.firstName').disabled=true;
		     document.getElementById('bidderAddress.streetAddress1').disabled=true;
		     document.getElementById('owner.officePhone').disabled=true;
		     document.getElementById('ratePerUnit').disabled=true;
		</s:else>
	}
	
	
	// This is called to auto calculate quoted rate based on percentage value...
	
	
	function validatePercentage(obj)
	{
	   if(!checkUptoFourDecimalPlace(obj,'tenderResponse_error','Tender/Quoted Rate'))
	      return false;
	   if(!checkOnlyFourDigitBeforeDecimal(obj,'tenderResponse_error','<s:text name="validate.tenderPercentage"/>'))
	      return false;
	    return true;
	} 
	
	function calculateBidRateWithPercentage()
	{
	    var obj=document.getElementById("percentage");
	    var records= responseLineTable.getRecordSet();
		 var i=0;
	     if(obj.value!="" && validatePercentage(obj)){
	      var percentage=eval(obj.value);
		  var sign=document.getElementById("sign").value;
		  var estimatedRate;
	  		 while(i<records.getLength())
	  		 {
	  		        estimatedRate=0;
	  		        estimatedRate = eval(dom.get("estimatedRate"+records.getRecord(i).getId()).value);
	  		        if(sign=="+")
	  		            estimatedRate=estimatedRate + (estimatedRate*percentage/100);
	  		        else
	  		            estimatedRate=estimatedRate - (estimatedRate*percentage/100);
	  		        dom.get("bidRate"+records.getRecord(i).getId()).value=(Math.round(estimatedRate*10000)/10000).toFixed(4);
	   				dom.get("bidRate"+records.getRecord(i).getId()).readOnly=true;
	   				 //updating totalbidrate
	   				checkQuotedRate(records.getRecord(i).getId(),dom.get("bidRate"+records.getRecord(i).getId()));
	   				i++;
	         }
        }
        else{
            i=0;
            var tType=document.getElementById('tenderType').value;
            while(i<records.getLength())
	  		{
	  			 dom.get("bidRate"+records.getRecord(i).getId()).value=0;
	  			 if(tType==PERCENTAGE)
	   			    dom.get("bidRate"+records.getRecord(i).getId()).readOnly=true;
	   			 else
	   			     dom.get("bidRate"+records.getRecord(i).getId()).readOnly=false;
	   			 dom.get("totalBidRate"+records.getRecord(i).getId()).value=0;
	   			  //updating totalbidrate
	   			 //checkQuotedRate(records.getRecord(i).getId(),dom.get("bidRate"+records.getRecord(i).getId()));
	   			 i++;
	        }
        }
        
       
        
        
 }
	
// This method is to check header status with negotiation status

 function validateWithHeaderStatus()
 {
     var status=document.getElementById('status').options[document.getElementById('status').selectedIndex].text;
     if(dom.get("negotiationStatus").value=='<s:property value="@org.egov.tender.utils.TenderConstants@TENDERRESPONSE_RENEGOTIATED"/>'
               && status=='<s:property value="@org.egov.tender.utils.TenderConstants@TENDERRESPONSE_JUSTIFIED"/>')
     {
         dom.get("tenderResponse_error").style.display = '';
	     document.getElementById("tenderResponse_error").innerHTML = '<s:text name="renegotiate.justification.status" />';
	     document.getElementById('status').value='<s:property value="%{status.id}"/>';
	     return false;
     }
     else{
      	dom.get("tenderResponse_error").style.display = 'none';
     }
 }
 
 // Validate response with notice date
 
 function validateResponseWithNoticeDate(responseDate)
 {
    var noticeDate = document.getElementById("noticeDate").value;
    dom.get("tenderResponse_error").style.display = 'none';
 	if(compareDate(responseDate.value,noticeDate)>0)
  	{
  	    dom.get("tenderResponse_error").style.display = '';
	    document.getElementById("tenderResponse_error").innerHTML = '<s:text name="responsedate.noticedate.validate" />';
	    responseDate.value="";
  	    return false;
    }
    else 
       return true;
 }
	
</script>