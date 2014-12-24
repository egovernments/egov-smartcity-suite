 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
 <s:if test="%{!negotiationDetails.isEmpty()}">
<div class="blueshadow"></div>
<head>
<style type="text/css">
.yui-skin-sam .yui-dt td.align-right  { 
		text-align:right;
	}
.yui-skin-sam .yui-dt td.align-center  { 
		text-align:center;
	}
</style>
<script>

var statusDropdownOptions=[
    <s:iterator var="s" value="negotiationStatusList" status="status">  
    {"label":"<s:property value="%{description}"/>" ,
    	"value":"<s:property value="%{code}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ];
    
 function openNegotiation(obj)
 {
     var submitForm = document.createElement("FORM");
	 document.body.appendChild(submitForm);
 	 submitForm.method = "POST";
	 submitForm.action= "${pageContext.request.contextPath}/tenderresponse/tenderNegotiation!view.action?responseId="+obj;
	 submitForm.submit();
	 return true;
 }
 
 
 
var indentDS,indentDT;
var data;

var negotiationStatus = function(obj){
   var records= indentDT.getRecordSet();
   var i=0;
	while(i<records.getLength())
	{
	    if(dom.get("status"+records.getRecord(i).getId()).value=='<s:property value="@org.egov.tender.utils.TenderConstants@TENDERRESPONSE_RENEGOTIATED"/>'
	        || dom.get("status"+records.getRecord(i).getId()).value=='<s:property value="@org.egov.tender.utils.TenderConstants@TENDERRESPONSE_ACCEPTED"/>')
  		  dom.get("status"+records.getRecord(i).getId()).disabled=true;
  		  <s:if test="%{mode=='view'}">
  		  dom.get("status"+records.getRecord(i).getId()).disabled=true;
  		  </s:if>
  		i++;
	}
}
  				
function initDataTable()
{
	var REDT = YAHOO.widget.RowExpansionDataTable,
	NESTED_DT = 'nestedDT';
	var idTemp=document.getElementById('idTemp').value;
	var sUrl1 = "/tender/tenderresponse/ajaxTenderResponse!getNegotiationDetailsAsJson.action?idTemp="+idTemp;
	var indentDS =  new YAHOO.util.XHRDataSource(sUrl1);
	indentDS.connXhrMode = "queueRequests";
	indentDS.responseType = YAHOO.util.DataSource.TYPE_JSON;
	indentDS.responseSchema = {
				resultsList: 'results',
				fields:[
					{key:'srlNo'},
					{key:'id'},
					{key:'responseNo'},
					{key:'date'},
					{key:'count'},
					{key:'status'},
					{key:'lines'}
				]
		};
	
	indentDS.doBeforeCallback = function(oRequest, oFullResponse, oParsedResponse) {
         data = oParsedResponse;
         return oParsedResponse;
    };
        
	var indentLineDS =  new YAHOO.util.FunctionDataSource(function(request, callback) {
	    return data.results[request].lines;
	});    
	indentLineDS.responseSchema = {
				fields:[
					{key:'slno' }, 
					{key:'item'},
					{key:'qty'},
					{key:'quotedrate'},
					{key:'totalquotedrate'}
				]
	};

	var linesDT;
	var showLines = function(state) {
			linesDT = new REDT(
			state.expLinerEl,
			[
				{key:"slno",label:'<div align="center"><s:text name="srlno.lbl"/></div>', sortable:false, className:'right',minWidth:10},
				{key:'item', label:'<div align="center"><s:text name="entity.lbl"/></div>', sortable:false, className:'right',minWidth:200}, 
				{key:'qty', label:'<div align="center"><s:text name="qty.lbl"/></div>', sortable:false, className:'right',minWidth:100,className:'align-right'},
				{key:"quotedrate",label:'<div align="center"><s:text name="quotedRatePerUnit"/></div>',sortable:false,minWidth:300 ,className:'align-right'},
				{key:"totalquotedrate",label:'<div align="center"><s:text name="totalBidRate"/></div>',sortable:false,minWidth:300,className:'align-right'}
			],
			indentLineDS,
			{initialRequest:state.record.getData('count')} 
		);

		// Store the reference to this datatable object for any further use 
		this.setExpansionState(state.record,NESTED_DT,linesDT);
	}
	
	indentDT = new REDT(
		"negotiationDetail",
		[
			{key:'srlNo', label:'<div align="center"><s:text name="srlno.lbl"/></div></div>',className:'align-center', sortable:false, minWidth:30},
			{key:'id', hidden:true,label:'<div align="center">Srl No</div>',className:'right', sortable:false, minWidth:2}, 
			{key:'count', hidden:true,label:'',className:'right', sortable:false, minWidth:1}, 
			{key:'responseNo', label:'<div align="center"><s:text name="responseno.lbl"/></div></div>', sortable:false, className:'align-center',minWidth:300},
			{key:'date', label:'<div align="center"><s:text name="negotiationdate.lbl"/></div>', sortable:false, className:'align-center',minWidth:300},
			{key:'status', label:'<div align="center"><s:text name="status.lbl"/><span class="mandatory">*</span></div>',className:'align-center',minWidth:200,formatter:createDropdownFormatter('statusList',''), dropdownOptions:statusDropdownOptions}
		],
		indentDS,
		{
		   rowExpansionTemplate : showLines,
		   initialRequest:""
		}
	);
	
	indentDT.on('rowExpansionDestroyEvent', function (state) {
		state[NESTED_DT].destroy();
	});	
	
	indentDT.subscribe("rowMouseoverEvent",indentDT.onEventHighlightRow);
	indentDT.subscribe("rowMouseoutEvent", indentDT.onEventUnhighlightRow);
	indentDT.subscribe("postRenderEvent",negotiationStatus);
	<s:if test="%{mode!='view'}">
	indentDT.subscribe("cellClickEvent",function(oArgs){
			      var row = oArgs.target;
     			  var elTargetRow = indentDT.getTrEl(row);
     			  var column = this.getColumn(row);
     			  if(elTargetRow){
     			  if(column.key == 'responseNo') {
  						var rec = indentDT.getRecord(elTargetRow);
  						var id = rec.getData("id");
  						openNegotiation(id);
  				   }
  				  }
  				});
  	</s:if>
  				
  	indentDT.subscribe('dropdownChangeEvent',function (oArgs) {
  	        var row = oArgs.target;
     		var column = this.getColumn(row);
	   		if(column.key=='status'){
	   		      var record=indentDT.getRecord(row);
	    	      var selectedIndex=row.selectedIndex;
	    	      dom.get("negotiationId").value=record.getData("id");
	    	      dom.get("negotiationStatus").value= statusDropdownOptions[selectedIndex].value;
	    	      validateWithHeaderStatus();
	    	    }
    		});		
  			
 }
 
 
 
</script>
</head>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" >   
         <tr>
               	<td colspan="5"><div align="center"><h1 class="subhead"><s:text name="Negotiation Details" /></h1></div></td>
         </tr>       
         <tr>
      			<td width="100%" >
					<div class="yui-skin-sam" align="center" >
						<div id="negotiationDetail"></div>
					</div>
				</td>
		</tr>
  </table>
	<script>
		initDataTable();
	</script>
</s:if>