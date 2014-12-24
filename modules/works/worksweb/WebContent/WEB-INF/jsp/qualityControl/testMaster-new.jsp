<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<style type="text/css">
#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
    Width: 100%;
}
.yui-dt table{
  width:100%;
}
.yui-dt-col-Add{
  width:5%;
}
.yui-dt-col-Delete{
  width:5%;
}
</style>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=8" />
        <title>
            <s:text name="qualityControl.testMaster.title" />
        </title>
    </head>   
    <script src="<egov:url path='js/works.js'/>"></script>
    <script src="<egov:url path='js/helper.js'/>"></script>
    <script type="text/javascript">
    function validateInput(){
        if(document.getElementById('testName').value=="")
        {
            dom.get("testMaster_error").style.display='';
            document.getElementById("testMaster_error").innerHTML='<s:text name="testMaster.enter.name" />';
            dom.get("testName").focus();
            return false;
        }
        // Enable Fields               
        for(var i=0;i<document.forms[0].length;i++) {
              document.forms[0].elements[i].disabled =false;
          }
        document.getElementById("testMaster_error").innerHTML='';
        dom.get("testMaster_error").style.display="none";
          return true;
    }

    function createHiddenFormatter(el, oRecord, oColumn, oData){
        var hiddenFormatter = function(el, oRecord, oColumn, oData) {
            var value = (YAHOO.lang.isValue(oData))?oData:"";
            var id=oColumn.getKey()+oRecord.getId();
            var fieldName = "testMasterRates[" + oRecord.getCount() + "]." + oColumn.getKey()+ ".id";
            var fieldValue=value;
            markup="<input type='hidden' id='"+id+"' name='"+fieldName+"' value='"+fieldValue+"' /><span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
            el.innerHTML = markup;
        }
        return hiddenFormatter;
        }
        var testMasterHiddenFormatter = createHiddenFormatter(10,10);
         
        function createTextBoxFormatter(size,maxlength) {
            var textboxFormatter = function(el, oRecord, oColumn, oData) {
               var fieldName = "testMasterRates[" + oRecord.getCount() + "]." +  oColumn.getKey();  
               var id = oColumn.getKey()+oRecord.getId();
               markup="<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='"+maxlength+"' style=\"width:100px; text-align:right\" name='"+fieldName+ "'"
               + " onblur='validateNumberInTableCell(testMasterRateDataTable,this,\"" + oRecord.getId()+ "\");'/>"
               + " <span id='error"+id+"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
               el.innerHTML = markup;
            }
            return textboxFormatter;   
        }
        var rateTextboxFormatter = createTextBoxFormatter(11,10);
       
        var dateFormatter = function(e2, oRecord, oColumn, oData) {
            var fieldName = "testMasterRates[" + oRecord.getCount() + "]." +  oColumn.getKey();
            var id = oColumn.getKey() + oRecord.getId();
            var markup= "<input type='text' id='"+id+"' class='selectmultilinewk' size='20' maxlength='10' style=\"width:100px\" name='"+fieldName
                        + "'  onkeyup=\"DateFormat(this,this.value,event,false,'3')\" onblur=\"validateDateFormat(this)\" />"
                        + " <span id='error"+ id +"' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>";
             e2.innerHTML = markup;
        }

    var testMasterRateDataTable;
    var makeTestMasterRateDataTable = function() {
        var cellEditor=new YAHOO.widget.TextboxCellEditor()
        var testMasterRateColumnDefs = [
            {key:"testMaster", hidden:true,formatter:testMasterHiddenFormatter,sortable:false, resizeable:false} ,
            {key:"SlNo", label:'Sl No', sortable:false, resizeable:false, width:50},
            {key:"rate", label:'<span class="mandatory">*</span>Rate', formatter:rateTextboxFormatter, sortable:false, resizeable:false, width:180},       
            {key:"startDate", label:'<span class="mandatory">*</span>Start Date', formatter:dateFormatter,sortable:false, resizeable:false, width:130},
            {key:"endDate",label:'End Date', formatter:dateFormatter,sortable:false, resizeable:false, width:130},
            {key:'deleteRate',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")} 
        ];
       
        var testMasterRateDataSource = new YAHOO.util.DataSource();
        testMasterRateDataTable = new YAHOO.widget.DataTable("testMasterRateTable",testMasterRateColumnDefs, testMasterRateDataSource, {MSG_EMPTY:"<s:text name='testMasterRate.initial.table.message'/>"});
        testMasterRateDataTable.subscribe("cellClickEvent", testMasterRateDataTable.onEventShowCellEditor);
        testMasterRateDataTable.on('cellClickEvent',function (oArgs) {
            var target = oArgs.target;
            var record = this.getRecord(target);
            var column = this.getColumn(target);
            if (column.key == 'deleteRate') {    
                    this.deleteRow(record);
                    allRecords=this.getRecordSet();
                    for(i=0;i<allRecords.getLength();i++){
                        this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
                    }
            }       
        });
        return {
            oDS: testMasterRateDataSource,
            oDT: testMasterRateDataTable
        }; 
    }


    function enableOrdisableElements()
    {
        <s:if test="%{sourcepage=='view'}">
            for(var i=0;i<document.forms[0].length;i++) {
                  document.forms[0].elements[i].disabled =true;
              }
            testMasterRateDataTable.removeListener('cellClickEvent');
           
            links=document.testMasterForm.getElementsByTagName("a");
            for(i=0;i<links.length;i++){
                     links[i].onclick=function(){return false;};
            }
             
            document.testMasterForm.closeButton.readonly=false;
            document.testMasterForm.closeButton.disabled=false;
        </s:if>
    }

    function UniqueCheckOnTestName() {
    	testName = dom.get('testName').value;
    	materialTypeId = dom.get('materialTypeId').value;
    	if(testName!=''){
    		populatetestNameunique({testName:testName,materialTypeId:materialTypeId});
    	}
    }

    function checkForTestName() {	
    	 if(dom.get("testNameunique").style.display =="" ){
    		 document.getElementById('testName').value="";
    	 }	 
    }

    
    </script>
   
    <body onload="enableOrdisableElements();">   
    <s:if test="%{hasErrors()}">
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
  
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
            <s:actionmessage theme="simple"/>
        </div>
    </s:if>
       <div class="errorstyle" id="testMaster_error" style="display: none;"></div>
       <span align="center" style="display:none" id="testNameunique">
		  <div class="errorstyle" >
		         <s:text name="testName.already.exists"/>
		  </div>
		</span>
    <s:form action="testMaster" name="testMasterForm"  theme="simple">
    <s:if test="%{sourcepage!='view'}">
    <s:token />
    </s:if>
    <s:push value="model">
    <s:hidden name="id" id="id" />
    <s:hidden name="sourcepage" id="sourcepage" />
    <s:hidden name="materialTypeId" id="materialTypeId" />
     
   
    <div class="navibarshadowwk"></div>
    <div class="formmainbox">
    <div class="insidecontent">
      <div class="rbroundbox2">
    <div class="rbtop2"><div></div></div>
    <div class="rbcontent2">
   
    <table width="100%" border="0" cellspacing="0" cellpadding="0">         
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                   <td colspan="4" class="headingwk">
                       <div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
                     <div class="headplacer"><s:text name="testMaster.header" /></div>
                  </td>
              </tr>
        
               <tr>
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="qualityControl.materialType.name" /></td>
                <td class="whitebox2wk"><s:textfield name="materialName" type="text" cssClass="selectwk" id="materialName" value="%{materialType.name}" disabled="true"/></td>
       
                <td class="whiteboxwk"><span class="mandatory">*</span><s:text name="qualityControl.testMaster.name" /></td>
                <td class="whitebox2wk"><s:textfield name="testName" type="text" cssClass="selectwk" id="testName" value = "%{testName}"  autocomplete="off"  onkeyup="UniqueCheckOnTestName();" onblur="checkForTestName();" />
					<egov:uniquecheck id="testNameunique" fields="['Value']" url='/qualityControl/ajaxTestMaster!testNameUniqueCheck.action' key='testName.already.exists' fieldtoreset="testName" />
				</td>
             </tr>
            
             <tr>
                <td class="greyboxwk"><s:text name="qualityControl.testMaster.description" /></td>
                <td class="greybox2wk"><s:textarea name="remarks" cols="35" cssClass="selectwk" id="remarks" value="%{remarks}"/></td>
                             
                <td class="greyboxwk"><s:text name="qualityControl.testMaster.unit" /></td>
                <td class="greybox2wk"><s:textfield name="unit" type="text" cssClass="selectwk" id="unit" value="%{unit}"/></td>
            </tr>
         </table>
        </td>
     </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>
        <table id="ratesTable" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="${pageContext.request.contextPath}/image/arrow.gif" /></div>
            <div class="headplacer"><s:text name="testMaster.rateDetails" /></div>
            </td>
                <td align="right" class="headingwk" style="border-left-width: 0px"><a href="#" onclick="testMasterRateDataTable.addRow({SlNo:testMasterRateDataTable.getRecordSet().getLength()+1});return false;"><img border="0" alt="Add Rate" src="${pageContext.request.contextPath}/image/add.png" /></a>
            </td>
        </tr>
        <tr>
            <td colspan="4">
            <div class="yui-skin-sam">
            <div id="testMasterRateTable"></div>
            <script>
                makeTestMasterRateDataTable();
                <s:iterator id="rateIterator" value="testRates" status="rate_row_status">
                testMasterRateDataTable.addRow(
                                    {testMaster:'<s:property value="testMaster.id"/>',                                           
                                    SlNo:'<s:property value="#rate_row_status.count"/>',
                                    rate:'<s:property value="rate"/>',
                                    startDate:'<s:property value="startDate"/>',
                                    endDate:'<s:property value="endDate"/>'
                                    }
                                    );
        var record = testMasterRateDataTable.getRecord(parseInt('<s:property value="#rate_row_status.index"/>'));                         

        var column = testMasterRateDataTable.getColumn('rate'); 
        dom.get(column.getKey()+record.getId()).value = '<s:property value="rate"/>';
       
        var column = testMasterRateDataTable.getColumn('startDate');
        <s:date name="startDate" var="startDateFormat" format="dd/MM/yyyy"/>  
        dom.get(column.getKey()+record.getId()).value = '<s:property value='%{startDateFormat}'/>';
                          
        var column = testMasterRateDataTable.getColumn('endDate'); 

        <s:if test="%{endDate!=null}">
    	<s:date name="endDate" var="endDateFormat" format="dd/MM/yyyy"/>
  	    dom.get(column.getKey()+record.getId()).value = '<s:property value='%{endDateFormat}'/>';
    	</s:if>
        
        </s:iterator>         
               </script>
              </div>
            </td>
        </tr>
        </table>
        </td>
    </tr>
    <tr>
          <td colspan="4" class="shadowwk"></td>
      </tr>
    <tr>
        <td><div align="right" class="mandatory">* <s:text name="message.mandatory" /></div></td>
      </tr>  
    </table>
    </div>
    <div class="buttonholderwk">
    <s:if test="%{sourcepage != 'view'}">
         <s:submit cssClass="buttonfinal" onclick='return validateInput()' value="Submit" id="submitButton" method="save" />
         <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='testMaster.close.confirm'/>');"/>
     </s:if>
     <s:else>
         <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
     </s:else>
    </div>
    <div class="rbbot2"><div></div></div>
    </div>
    </div>
    </div>
    </s:push>
    </s:form>
    </body>
</html> 