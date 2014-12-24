<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- <%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%> -->
<!-- <%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%> -->

<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}
</style>

<script src="<egov:url path='js/works.js'/>"></script>
<script>

function validateOverheadFormAndSubmit(){
    clearMessage('overheads_error')
	links=document.overhead.getElementsByTagName("span");
	errors=false;
	for(i=0;i<links.length;i++)
    {
        if(links[i].innerHTML=='&nbsp;x' && links[i].style.display!='none'){
            errors=true;
            break;
        }
    }
    
    if(errors){
        dom.get("overheads_error").style.display='';
    	document.getElementById("overheads_error").innerHTML='<s:text name="overhead.validate_x.message" />';
    	return;
    }
    else{
    document.overhead.action='${pageContext.request.contextPath}/masters/overhead!create.action';
    	document.overhead.submit();
    }
}

function createTextBoxFormatter(size,maxlength){
var textboxFormatter = function(el, oRecord, oColumn, oData) {
    var fieldName = "overheadRates[" + oRecord.getCount() + "]." +  oColumn.getKey();
   var id = oColumn.getKey()+oRecord.getId();
   markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='"+maxlength+"' style=\"width:100px; text-align:right\" name='"+fieldName+ "'" 
   + " onblur='validateNumberInTableCell(overheadRateDataTable,this,\"" + oRecord.getId()+ "\");'/>"
   + " <span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
    el.innerHTML = markup;
	}
return textboxFormatter;
}
var percentageTextboxFormatter = createTextBoxFormatter(11,5);
var lumpsumAmountTextboxFormatter = createTextBoxFormatter(11,7);

var dateFormatter = function(e2, oRecord, oColumn, oData) {
	var fieldName = "overheadRates[" + oRecord.getCount() + "].validity." +  oColumn.getKey();
	var id = oColumn.getKey() + oRecord.getId();
	
	var markup= "<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='10' style=\"width:100px\" name='"+fieldName 
	            + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this)\" />"
				+ " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
	 e2.innerHTML = markup;
}

var overheadRateDataTable;
var makeOverheadRateDataTable = function() 
{
	var cellEditor=new YAHOO.widget.TextboxCellEditor()
	var overheadRateColumnDefs = [ 
		{key:"overheadId", hidden:true,sortable:false, resizeable:false} ,
		{key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:50},
		{key:"lumpsumAmount", label:'Lump sum Amount', formatter:lumpsumAmountTextboxFormatter, sortable:false, resizeable:false, width:180},		
		{key:"percentage", label:'Percentage', formatter:percentageTextboxFormatter, sortable:false, width:180},
		{key:"startDate", label:'Start Date<span class="mandatory">*</span>', formatter:dateFormatter,sortable:false, resizeable:false, width:160},
		{key:"endDate",label:'End Date', formatter:dateFormatter,sortable:false, resizeable:false, width:160}		  
	];
	
	var overheadRateDataSource = new YAHOO.util.DataSource(); 
	overheadRateDataTable = new YAHOO.widget.DataTable("overheadRateTable",overheadRateColumnDefs, overheadRateDataSource, {MSG_EMPTY:"<s:text name='master.overhead.initial.table.message'/>"});
	overheadRateDataTable.subscribe("cellClickEvent", overheadRateDataTable.onEventShowCellEditor); 
}
	
</script>
<div class="errorstyle" id="overhead_error" style="display: none;"></div>
<div class="navibarshadowwk"></div>
<div class="formmainbox">
<div class="insidecontent">
<div class="rbroundbox2">
<div class="rbtop2"><div> </div></div>  

<div class="rbcontent2">
<!--<div class="datewk"><span class="bold">Today</span> <egov:now/>
</div>  -->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td>
	<table id="overheadTable" width="100%" border="0" cellspacing="0" cellpadding="0">
				 <tr>
				      	<td colspan="4" class="headingwk">
				      		<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
				      		<div class="headplacer"><s:text name='page.title.overheads' /></div>
				     	 </td>
				 </tr>
				 <tr>
				 		<td width="11%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="master.overhead.name" />:</td>
						<td width="21%" class="whitebox2wk"><s:textfield label="name" name="name" value="%{name}" id="name" cssClass="selectwk" maxlength="255"/></td>
						<td width="15%" class="whiteboxwk"><span class="mandatory">*</span><s:text name="master.overhead.description" />:</td>
						<td width="53%" class="whitebox2wk"><s:textfield label="Description" name="description" value="%{description}" id="description" cssClass="selectwk" maxlength="255"/></td>
				 </tr>
				 <tr>
				 		<td width="11%" class="greyboxwk"><span class="whiteboxwk"><s:text name="master.overhead.account" />:</td>
						<td width="21%" class="greybox2wk"><s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="account" id="account" cssClass="selectwk" list="dropdownData.accountList"
										listKey="id" listValue='glcode  + " : " + name' value="%{account.id}" /></td>
						<td width="15%" class="greyboxwk"><span class="mandatory">*</span><s:text name="master.overhead.expenditure" />:</td>
						<td width="53%" class="greybox2wk"><s:select headerKey="-1"  headerValue="%{getText('estimate.default.select')}" name="expenditure" id="expenditure" cssClass="selectwk" list="expenditureTypeList" listKey="value" listValue="value" value="%{expenditureType.value}" /></td>
				 </tr>
				 <tr>
			            <td colspan="4" class="shadowwk"></td>
			     </tr>
	</table>
</td></tr>
 <tr><td>&nbsp;</td></tr>
	
<tr><td>
     <table id="overheadRatesTable" width="100%" border="0" cellspacing="0" cellpadding="0">
			 <tr><td colspan="5" class="headingwk">
			 	<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
			 	<div class="headplacer"><s:text name='page.title.overheadrate' /></div>
			 </td>
			 <td id="addButtn" align="right" class="headingwk"><a href="#"
					onclick="overheadRateDataTable.addRow({SlNo:overheadRateDataTable.getRecordSet().getLength()+1});return false;"><img
					border="0" alt="Add Overhead Rate"
					src="${pageContext.request.contextPath}/image/add.png" /></a>
			</td></tr>
   		    <tr>
   		    <td colspan="6" class="shadowwk">
   		    <div class="yui-skin-sam"><div id="overheadRateTable"></div></div>
   		    <script>
					makeOverheadRateDataTable();		    	
					<s:iterator id="overheadRateIterator" value="model.overheadRates" status="rate_row_status">
				        overheadRateDataTable.addRow(
			        						{overheadId:'<s:property value="id"/>',
			                                SlNo:'<s:property value="#rate_row_status.count"/>',
			                                lumpsumAmount:'<s:property value="lumpsumAmount"/>',
			                                percentage:'<s:property value="percentage"/>',
			                                startDate:'<s:property value="validity.startDate"/>',
			                                endDate:'<s:property value="validity.endDate"/>'});
			                                    
			        var record = overheadRateDataTable.getRecord(parseInt('<s:property value="#rate_row_status.index"/>'));
			    
			        var column = overheadRateDataTable.getColumn('lumpsumAmount');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="lumpsumAmount"/>';
			        
			        var column = overheadRateDataTable.getColumn('percentage');  
			        dom.get(column.getKey()+record.getId()).value = '<s:property value="percentage"/>';
			        
			        var column = overheadRateDataTable.getColumn('startDate');  
			        dom.get(column.getKey()+record.getId()).value ='<s:date name="validity.startDate" format="dd/MM/yyyy"/>';
			        
			        <s:if test="%{id!=null}">
			        	dom.get(column.getKey()+record.getId()).disabled = true;
			        </s:if>
			        var imgId1 = "img" + column.getKey()+record.getId();
			        
			        var column = overheadRateDataTable.getColumn('endDate');  
			        dom.get(column.getKey()+record.getId()).value ='<s:date name="validity.endDate" format="dd/MM/yyyy"/>';
			        <s:if test="%{id!=null}">
			       		dom.get(column.getKey()+record.getId()).disabled = true;
			        </s:if>
			                
			        var imgId2 = "";
			        
			        links=document.overhead.getElementsByTagName("img");
			        
					for(i=0;i<links.length;i++)
			        {
			            <s:if test="%{id!=null}">
			        		if(links[i].id.indexOf(imgId1)==0)
			        		{
			        			links[i].onclick=function(){return false;};
			        		}
			        		if(links[i].id.indexOf(imgId2)==0)
			        		{
			        			links[i].onclick=function(){return false;};
			        		}
			            </s:if>
			        }
			       </s:iterator>
        </script>
   		   </td> 
   		    </tr>
	</table></td></tr>
</table>
</div> <!--  end of rbcontent2 --> 
<div class="rbbot2"><div></div></div>
</div> <!--  end of  rbroundbox2 -->
</div> <!--  end of formmainbox -->
</div> <!--  end of formmainbox -->
