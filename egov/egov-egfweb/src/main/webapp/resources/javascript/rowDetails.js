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
			function openDetails(obj,ctrlName,screenName){ 
				var trObj=obj.parentNode.parentNode;
				var glObj= PageManager.DataService.getControlInBranch(trObj,ctrlName);
				var filter=glObj.value;
				if(!filter){
					bootbox.alert("select account code first");
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
					if(tableObj.rows.length<=1)
						PageManager.DataService.addNewRow('entities_grid');
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
					tempObj=PageManager.DataService.getControlInBranch(newRow,"grid_rowFunId");
					if(tempObj!=null) tempObj.value="";
					
				}
			}
			
			function cleanDetailsGrid(rowIdx){ 
							var tableObj=document.getElementById('entities_grid');
							var tempObj=null;
							for(var i=tableObj.rows.length-1;i>0 ;i--){
								tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],"grid_rowIndex");
								if(parseInt(tempObj.value)==rowIdx){ 
									tableObj.deleteRow(tempObj.parentNode.parentNode.rowIndex);
									return ;
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
		 
		 function  createRowIndex(dc,gridName){
			 var gridObj1=dc.grids[gridName];
			 var entityGridObj1=dc.grids['entities_grid'];
			  	//if  entity grid  have entityGridObj1[j][4] i.e. functionid  
			 var funIdx=null;
			for(var ii=0;ii<gridObj1[0].length;ii++){
				if(gridObj1[0][ii]=='cv_fromFunctionCodeId')
					funIdx=ii;
			}
			if(funIdx==null){ bootbox.alert("functionId if not retrieved"); return false; }
			for(var i=1;i<gridObj1.length;i++)
			{
			 for(var j=1;j<entityGridObj1.length;j++)
			  {
				if(entityGridObj1[j][4]!=null){
					//bootbox.alert("ddd:"+PageManager.DataService.getControlInBranch(gridObj1[0],"cv_fromFunctionCodeId"));
				    if((gridObj1[i][2]==entityGridObj1[j][3]) && (gridObj1[i][funIdx]==entityGridObj1[j][4]))
						dc.grids['entities_grid'][j][3]=i; //bootbox.alert(i+"<--if:-->"+dc.grids['entities_grid'][j][3]);
				}else{
					if(gridObj1[i][2]==entityGridObj1[j][3])
						     dc.grids['entities_grid'][j][3]=i; //bootbox.alert("else");
				}
			  }	
			}
			return dc;
               }
               
             
             /*
             * inserts new blank row at position pos
             */
             
		   function  insertBlankRow(gridName,pos) 
			{
			 //for inserting first blank row in grid
				var tableObj=document.getElementById(gridName);
				if(tableObj.rows[pos]!=null)
				{
					newRow=tableObj.rows[pos].cloneNode(true);
					tableObj.tBodies[0].appendChild(newRow);
					for(var i=0;i<tableObj.rows[pos].cells.length;i++)
					{
						var id=tableObj.rows[pos].cells[i].childNodes[0].getAttribute("id");
						document.getElementById(id).value="";
					}

			 	}
		   }
