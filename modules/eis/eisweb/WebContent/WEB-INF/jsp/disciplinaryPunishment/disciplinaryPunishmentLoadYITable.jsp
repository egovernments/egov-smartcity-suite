	
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>

		path="${pageContext.request.contextPath}";
		var ENQDETAILLIST='enqDetailsList';
		var enqDetailsTable;
		var enqDetailTableIndex = 0;
		var makeEnqDetailTable = function() {
		var enqDetailColumns = [ 
			//{key:"enquiryOfficerId",hidden:false,width:10, formatter:createTextFieldFormatter(ENQDETAILLIST,".enquiryOfficerId")},
			{key:"empId",hidden:true,width:10, formatter:createTextFieldFormatter(ENQDETAILLIST,".empId")},
			{key:"enquiryOfficerCode",label:'Enquiry Officer Code',width:125, formatter:createTextFieldFormatterAuto(ENQDETAILLIST,".enquiryOfficerCode")},
			{key:"enquiryOfficerName", label:'Enquiry Officer Name',width:130,formatter:createTextFieldFormatter(ENQDETAILLIST,".enquiryOfficerName")},				
			{key:"designation", label:'EO Designation',width:110,formatter:createTextFieldFormatter(ENQDETAILLIST,".designation")},
			{key:"nominatedDate", label:'EO Nominated Date',width:120,formatter:createTextFieldDateFormatter(ENQDETAILLIST,".nominatedDate")},
			{key:"reportDate", label:'EO Report Date',width:110,formatter:createTextFieldDateFormatter(ENQDETAILLIST,".reportDate")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var enqDetailDS = new YAHOO.util.DataSource(); 
		enqDetailsTable = new YAHOO.widget.DataTable("enqDetailTable",enqDetailColumns, enqDetailDS);
		enqDetailsTable.on('cellClickEvent',function (oArgs) {
			
			if('<s:property value="%{modifyType}"/>'=='menutree'){
				return false;
			}
			
			var target = oArgs.target;
			var record = this.getRecord(target);
			//var recordIndex = this.getRecordIndex(record);alert(record.getData('enquiryOfficerId'));
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				enqDetailTableIndex=parseInt(enqDetailTableIndex)+1;
				enqDetailsTable.addRow({SlNo:enqDetailsTable.getRecordSet().getLength()+1});
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){	
					//enqDetailTableIndex=parseInt(enqDetailTableIndex)-1;		
					//var enqOffId = record.getData('enquiryOfficerId');//alert(enqOffId);
					//if(enqOffId!=undefined){ alert('here');
						//setDeletedRecordSet(enqOffId);
					//}
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
				}
				else{
					alert("This row can not be deleted");
				}
			}
		});
		<s:iterator value="enqDetailsList" status="stat">
			enqDetailTableIndex='<s:property value="#stat.index"/>';
			enqDetailsTable.addRow({SlNo:enqDetailsTable.getRecordSet().getLength()+1,
				//"enquiryOfficerId":'<s:property value="enquiryOfficerId"/>',
				"empId":'<s:property value="employeeId.idPersonalInformation"/>', 
				"enquiryOfficerCode":'<s:property value="employeeId.code"/>', 
				"enquiryOfficerName":'<s:property value="enquiryOfficerName"/>',
				"designation":'<s:property value="designation"/>',
				"nominatedDate":'<s:property value="nominatedDate"/>',
				"reportDate":'<s:property value="reportDate"/>'
			});
			var index = '<s:property value="#stat.index"/>';
			//updateGrid('enquiryOfficerId',index,'<s:property value="enquiryOfficerId"/>');
			updateGrid('empId',index,'<s:property value="employeeId.idPersonalInformation"/>');
			updateGrid('enquiryOfficerCode',index,'<s:property value="employeeId.code"/>');
			updateGrid('enquiryOfficerName',index,'<s:property value="enquiryOfficerName"/>');
			updateGrid('designation',index,'<s:property value="designation"/>');
			updateGrid('nominatedDate',index,'<s:date name="nominatedDate" format="dd/MM/yyyy"/>');
			updateGrid('reportDate',index,'<s:date name="reportDate" format="dd/MM/yyyy"/>');
		</s:iterator>	
		
	}
	
	function updateGrid(field,index,value){
		document.getElementById(field+index).value=value;
	}
	function createTextFieldFormatterAuto(prefix,suffix){
	    return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			var tmp1 =suffix.substr(1,suffix.length);
			tmp1=tmp1+enqDetailTableIndex;
			el.innerHTML = "<input type='text'  id='"+tmp1+"' name='"+prefix+"["+enqDetailTableIndex+"]"+suffix+"' style='width:90px;' autocomplete='off' onkeypress='autocompleteEnquiryEmp(this)' onblur='splitEnquiryEmpCode(this)'/>";
		}
	}
	function createTextFieldFormatter(prefix,suffix){
	    return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			var tmp2 =suffix.substr(1,suffix.length);
			tmp2=tmp2+enqDetailTableIndex;
			el.innerHTML = "<input type='text'  id='"+tmp2+"' name='"+prefix+"["+enqDetailTableIndex+"]"+suffix+"' readonly style='width:120px;'/>";
		}
	}
	function createTextFieldDateFormatter(prefix,suffix){
	    return function(el, oRecord, oColumn, oData) {
			var value = (YAHOO.lang.isValue(oData))?oData:"";
			var tmp3 =suffix.substr(1,suffix.length);
			tmp3=tmp3+enqDetailTableIndex;
			el.innerHTML = "<input type='text'  id='"+tmp3+"' name='"+prefix+"["+enqDetailTableIndex+"]"+suffix+"' style='width:90px;'  onblur = 'validateDateFormat(this);'  onfocus='javascript:vDateType=3' onkeyup='DateFormat(this,this.value,event,false,3)'/>"+
			"<a href=javascript:show_calendar('forms[0]."+tmp3+"')><img src='<%=request.getContextPath()%>/common/image/calendar.png' border='0'></a>";
		}
	}
	function createAddImageFormatter(baseURL){
		var addImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/common/image/add.png";
	    markup='<img height="16" border="0" width="14" alt="Add" src="'+imageURL+'"/>';
	    el.innerHTML = markup;
		}
		return addImageFormatter;
	}
	
	function createDeleteImageFormatter(baseURL)
	{
		var deleteImageFormatter = function(el, oRecord, oColumn, oData) {
	    var imageURL=baseURL+"/common/image/cancel.png";
	    markup='<img height="14" border="0" width="14" alt="Delete" src="'+imageURL+'"/>';
	    el.innerHTML = markup;
		}
		return deleteImageFormatter;
	}
	</script>
