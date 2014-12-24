			function openDetails(obj,ctrlName,screenName){ 
				var trObj=obj.parentNode.parentNode;
				var glObj= PageManager.DataService.getControlInBranch(trObj,ctrlName);
				var filter=glObj.value;
				if(!filter){
					alert("select account code first");
					return;
				} 
				var oldList=getOldList(trObj.rowIndex);
				var screenData='';
				if(screenName){
					screenData='&screenName='+screenName;
				}
				var sRtn =showModalDialog("AccountDetails.html?accCode="+filter+"&oldList="+oldList+screenData,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
				if(!sRtn || sRtn.length==0)
				{ 
				if(sRtn=='') return;
				cleanDetailsGrid(trObj.rowIndex); 
				return; 
				}
				cleanDetailsGrid(trObj.rowIndex);
				fillDetailsGrid(sRtn,trObj.rowIndex);
			}
			function fillDetailsGrid(str,rowIdx){
				var tableObj=document.getElementById('entities_grid');
				var iRow=str.split(";"); 
				var rowContents=null,newRow=null,tempObj=null;
				for(var i=1;i<iRow.length;i++){
					rowContents=iRow[i].split("^");	
					newRow=tableObj.rows[1].cloneNode(true);
					tableObj.tBodies[0].appendChild(newRow);
					tempObj=PageManager.DataService.getControlInBranch(newRow,"grid_detTypeId");
					tempObj.value=rowContents[0];
					tempObj=PageManager.DataService.getControlInBranch(newRow,"grid_detKeyId");
					tempObj.value=rowContents[1];
					tempObj=PageManager.DataService.getControlInBranch(newRow,"grid_detValue");
					tempObj.value=rowContents[2];
					tempObj=PageManager.DataService.getControlInBranch(newRow,"grid_rowIndex");
					tempObj.value=rowIdx;
				}
			}
			function cleanDetailsGrid(rowIdx){ 
				var tableObj=document.getElementById('entities_grid');
				var tempObj=null;
				if(PageManager.DataService.getQueryField('showMode')=='edit')
				{
					for(var i=eval(tableObj.rows.length)-1;i>0 ;i--){
						tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_rowIndex");
						if(eval(tableObj.rows.length)==2 && i== 1)
						{	//alert("inside emtpying:");
							tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_detTypeId");
							tempObj.value="";
							tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_detKeyId");
							tempObj.value="";
							tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_detValue");
							tempObj.value="";
							tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_rowIndex");
							tempObj.value="";
							break;
	
						}
						tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_rowIndex");
						if(parseInt(tempObj.value)==rowIdx){ //alert("inside if");
							tableObj.deleteRow(tempObj.parentNode.parentNode.rowIndex);
						}
					 }
				}
				else
				{	
					for(var i=tableObj.rows.length-1;i>1 ;i--){
						tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_rowIndex");
						if(parseInt(tempObj.value)==rowIdx){ 
							tableObj.deleteRow(tempObj.parentNode.parentNode.rowIndex);
						}
					 }
				}
			}
			function getOldList(rowIdx){ 
				
				var result="";
				var tableObj=document.getElementById('entities_grid');
				for(var i=1;i<tableObj.rows.length;i++){
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_rowIndex");
					if(parseInt(tempObj.value)==rowIdx){
						result+=";";
						result+=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_detTypeId").value;
						result+=","+PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_detKeyId").value;
						break;
					}
				} 
				return result;
			}	
			function checkEntityDuplication(rowTC,rowC){
				var table=document.getElementById('entities_grid');	 
				var rowTData=new Array(table.rows.length);
				var	rowTData1 =new Array(table.rows.length);
				var rowKData=new Array(table.rows.length);
				var	rowKData1 =new Array(table.rows.length);
				var rTInd=0,rInd=0;
				for(var i=2;i<table.rows.length;i++){
					var rowNum=PageManager.DataService.getControlInBranch(table.rows[i],"grid_rowIndex");
					if(rowNum==null)continue;
					var tObj=PageManager.DataService.getControlInBranch(table.rows[i],"grid_detTypeId");
					var kObj=PageManager.DataService.getControlInBranch(table.rows[i],"grid_detKeyId");	
					if(tObj==null || kObj==null)continue;
					if(parseInt(rowNum.value)==rowTC){
						rowTData[rTInd]=tObj.value;
						rowKData[rTInd]=kObj.value;	
						rTInd++;
					}
					if(parseInt(rowNum.value)==rowC){
						rowTData1[rInd]=tObj.value;
						rowKData1[rInd]=kObj.value;	
						rInd++;
					}
			    }
			    var count=0;
			    for(var j=0;j<rTInd;j++){
					for(var k=0;k<rTInd;k++){
					  if(rowTData[j]==rowTData1[k] && rowKData[j]==rowKData1[k]){
						count++;
					  }
					}
			    }
			    if(count>=rTInd)return true;
			    return false;
		 } 		
		 
		 function  createRowIndex(dc,gridName)
			{
			 var gridObj1=dc.grids[gridName];
			 	var entityGridObj1=dc.grids['entities_grid'];
			 		for(var i=1;i<gridObj1.length;i++)
			 		{
			 		 for(var j=1;j<entityGridObj1.length;j++)
			 		  {
			 
			 		    if(gridObj1[i][2]==entityGridObj1[j][3])
			 
			 		    {
			 
			 	            dc.grids['entities_grid'][j][3]=i;
			 
			 	        }
			 
			 	      }			 
                    }
                   return dc;
             }
