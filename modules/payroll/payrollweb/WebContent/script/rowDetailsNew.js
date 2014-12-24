			function openDetails(obj,ctrlName,screenName,gn,prefix){
				var trObj=obj.parentNode.parentNode;
				var glObj= PageManager.DataService.getControlInBranch(trObj,ctrlName);
				var filter=glObj.value;
				if(!filter){
					alert("select account code first");
					return;
				}
				var oldList=getOldList(trObj.rowIndex,gn,prefix);
				var screenData='';
				if(screenName){
					screenData='&screenName='+screenName;
				}
				var sRtn =showModalDialog("AccountDetails.html?accCode="+filter+"&oldList="+oldList+screenData,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
				if(!sRtn || sRtn.length==0)return;
				cleanDetailsGrid(trObj.rowIndex,gn,prefix);
				fillDetailsGrid(sRtn,trObj.rowIndex,gn,prefix);
			}
			function fillDetailsGrid(str,rowIdx,gn,prefix){
				var tableObj=document.getElementById(gn);
				var iRow=str.split(";");
				var rowContents=null,newRow=null,tempObj=null;
				for(var i=1;i<iRow.length;i++){
					rowContents=iRow[i].split("^");
					newRow=tableObj.rows[1].cloneNode(true);
					tableObj.tBodies[0].appendChild(newRow);
					tempObj=PageManager.DataService.getControlInBranch(newRow,prefix+"grid_detTypeId");
					tempObj.value=rowContents[0];
					tempObj=PageManager.DataService.getControlInBranch(newRow,prefix+"grid_detKeyId");
					tempObj.value=rowContents[1];
					tempObj=PageManager.DataService.getControlInBranch(newRow,prefix+"grid_detValue");
					tempObj.value=rowContents[2];
					tempObj=PageManager.DataService.getControlInBranch(newRow,prefix+"grid_rowIndex");
					tempObj.value=rowIdx;
				}
			}
			function cleanDetailsGrid(rowIdx,gn,prefix){
				var tableObj=document.getElementById(gn);
				var tempObj=null;
				for(var i=tableObj.rows.length-1;i>1;i--){
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_rowIndex");
					if(parseInt(tempObj.value)==rowIdx){
						tableObj.deleteRow(i);
					}
				}
			}
			function getOldList(rowIdx,gn,prefix){
				var result="";
				var tableObj=document.getElementById(gn);
				for(var i=1;i<tableObj.rows.length;i++){
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_rowIndex");
					if(parseInt(tempObj.value)==rowIdx){
						result+=";";
						result+=PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_detTypeId").value;
						result+=","+PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_detKeyId").value;
					}
				}
				return result;
			}	
			function checkEntityDuplication(rowTC,rowC,gn,prefix){
				var table=document.getElementById(gn);	 
				var rowTData=new Array(table.rows.length);
				var	rowTData1 =new Array(table.rows.length);
				var rowKData=new Array(table.rows.length);
				var	rowKData1 =new Array(table.rows.length);
				var rTInd=0,rInd=0;
				for(var i=2;i<table.rows.length;i++){
					var rowNum=PageManager.DataService.getControlInBranch(table.rows[i],prefix+"grid_rowIndex");
					if(rowNum==null)continue;
					var tObj=PageManager.DataService.getControlInBranch(table.rows[i],prefix+"grid_detTypeId");
					var kObj=PageManager.DataService.getControlInBranch(table.rows[i],prefix+"grid_detKeyId");	
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
		 
		 function  createRowIndex(dc,gridName,gn)
			{
			 var gridObj1=dc.grids[gridName];
			 	var entityGridObj1=dc.grids[gn];
			 
			 		for(var i=1;i<gridObj1.length;i++)
			 		{
			 		 for(var j=1;j<entityGridObj1.length;j++)
			 		  {
			 			
			 		    if(gridObj1[i][2]==entityGridObj1[j][3])
			 
			 		    {
			 
			 	            dc.grids[gn][j][3]=i;
			 	           break;
			 
			 	        }
			 
			 	      }			 
                    }
                   return dc;
             }