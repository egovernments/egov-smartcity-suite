/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
# accountability and the service delivery of the government  organizations.
#   
#  Copyright (C) <2015>  eGovernments Foundation
#   
#  The updated version of eGov suite of products as by eGovernments Foundation 
#  is available at http://www.egovernments.org
#   
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  any later version.
#   
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#   
#  You should have received a copy of the GNU General Public License
#  along with this program. If not, see http://www.gnu.org/licenses/ or 
#  http://www.gnu.org/licenses/gpl.html .
#   
#  In addition to the terms of the GPL license to be adhered to in using this
#  program, the following additional terms are to be complied with:
#   
# 1) All versions of this program, verbatim or modified must carry this 
#    Legal Notice.
#   
# 2) Any misrepresentation of the origin of the material is prohibited. It 
#    is required that all modified versions of this material be marked in 
#    reasonable ways as different from the original version.
#   
# 3) This license does not grant any rights to any user of the program 
#    with regards to rights under trademark law for use of the trade names 
#    or trademarks of eGovernments Foundation.
#   
# In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
			function openDetails(obj,ctrlName,screenName,gn,prefix){
			  // bootbox.alert("opendetails");
				var trObj=obj.parentNode.parentNode;
				var glObj= PageManager.DataService.getControlInBranch(trObj,ctrlName);
				var filter='';
				//bootbox.alert(glObj.type.substring(0,6)+','+glObj.options[glObj.selectedIndex].text)
				if(glObj.type.substring(0,6)=='select' || glObj.type.substring(0,6)=='SELECT')
					filter = glObj.options[glObj.selectedIndex].text;
				else
				 	filter=glObj.value;
				//bootbox.alert('filter='+filter);
				tempObj=null;
				if(!filter){
					bootbox.alert("select account code first");
					return;
				}
				//  bootbox.alert("before oldlist"+trObj.rowIndex+gn+prefix);
				var oldList=getOldList(trObj.rowIndex,gn,prefix);
				//  bootbox.alert("after oldlist");
				var screenData='';
				var mode=PageManager.DataService.getQueryField("showMode");
				//Disable all when show Mode="view" Or "Modify"
				if(mode=='view'||mode=='modify'||mode=='viewBank'||mode=='viewCash'||mode=='reverseBank'||mode=='reverseCash')
					screenName="view";
				if(screenName){
					screenData='&screenName='+screenName;
				}
				//bootbox.alert('before showmodaldialog...filter'+filter);
				var sRtn =showModalDialog("/"+getContext()+"/HTML/VMC/AccountDetails.html?accCode="+filter+"&oldList="+oldList+screenData,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
				
				if(!sRtn || sRtn.length==0)return;
				
				/*set 1st Row value to empty */
				
				var tableObj=document.getElementById(gn);
			
				if(tableObj.rows.length<=1)
				{
				
					PageManager.DataService.addNewRow(gn);
				}
				if(screenName =="modifyCBill" && trObj.rowIndex ==1)
				{
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detTypeId");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detKeyId");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detValue");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_rowIndex");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_amount");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detCode");
					tempObj.value="";
				
				}
				//added by mani to make first row empty
					/*tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detTypeId");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detKeyId");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detValue");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_rowIndex");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_amount");
					tempObj.value="";
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[1],prefix+"grid_detCode");
					tempObj.value="";*/
				cleanDetailsGrid(trObj.rowIndex,gn,prefix);
				
				fillDetailsGrid(sRtn,trObj.rowIndex,gn,prefix);
					
			}
			
			
			function fillDetailsGrid(str,rowIdx,gn,prefix){
				var tableObj=document.getElementById(gn);
				
				
				var iRow=str.split(";");
				var rowContents=null,newRow=null,tempObj=null;
				if(tableObj.rows.length<=1)
					PageManager.DataService.addNewRow(gn);
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
					tempObj=PageManager.DataService.getControlInBranch(newRow,prefix+"grid_amount");
					tempObj.value=rowContents[3];
					//bootbox.alert(tempObj.value);
					tempObj=PageManager.DataService.getControlInBranch(newRow,prefix+"grid_detCode");
					tempObj.value=rowContents[4];
				}
			}
			function cleanDetailsGrid(rowIdx,gn,prefix){
			
				var tableObj=document.getElementById(gn);
				var tempObj=null;
				for(var i=tableObj.rows.length-1;i>=1;i--){ // i>1 ->i>=1 by mani
					
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_rowIndex");
					if(parseInt(tempObj.value)==rowIdx){
						tableObj.deleteRow(i);
					}
				}
			}
			function getOldList(rowIdx,gn,prefix){
		
				var result="";
				var tableObj=document.getElementById(gn);
				for(var i=1;i<tableObj.rows.length;i++)
				{
					tempObj=PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_rowIndex");
					//bootbox.alert("comparing"+tempObj.value+" and"+rowIdx);
					if(parseInt(tempObj.value)==rowIdx){
						result+=";";
						result+=PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_detTypeId").value;
						result+=","+PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_detKeyId").value;
						result+=","+PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_amount").value;
						result+=","+PageManager.DataService.getControlInBranch(tableObj.rows[i],prefix+"grid_detCode").value;
						
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
             
          function  insertBlankRow(gridName,pos) // inserts new row at position pos
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


		    /*Assigns the row index value from entity grid object to the grid object 
             added by Sapna */
                           
    function  createRowIndexForGrid(dc,gridName,gn,prefix)
				{
			 			 var gridObj1=dc.grids[gridName];
			 			 var entityGridObj1=dc.grids[gn];
			 			for(var i=1;i<gridObj1.length;i++)
			 			 			 		{
			 			 			 		 for(var j=1;j<entityGridObj1.length;j++)
			 			 			 		  {
													if(prefix =='')
													{
														if(gridObj1[i][0]==entityGridObj1[j][3] )

														{
															dc.grids[gn][j][3]=i;
															
														}

													}
													else
													{
														if(gridObj1[i][2]==entityGridObj1[j][3] )

														{
															dc.grids[gn][j][3]=i;
																													
														}

													}
			 			 			 					 			 
			 			 			 	      }			 
			 			                     }
			 			                    return dc;
           						  }
             
             
     function  insertBlankRow(gridName,pos) // inserts new row at position pos
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
		   
		   
//This method replaces the glcode of entitygrid by corresponding row number	 where colno is the position of glcode in the mainGrid
	function  createRowIndexWithColnoNo(dc,gridName,gn,prefix,colno)
		   			{
		   				var gridObj1=dc.grids[gridName];
		   			 	var entityGridObj1=dc.grids[gn];
		   			 	if(entityGridObj1!=undefined)
		   			 		{
		   			 		for(var i=1;i<gridObj1.length;i++)
		   			 			{
		   			 			 for(var j=1;j<entityGridObj1.length;j++)
		   			 				{
		   			 					if(gridObj1[i][colno]==entityGridObj1[j][3])
		   			  					 dc.grids[gn][j][3]=i;
									 }
								 }
							 }
		               return dc;
				  }
             
             
  function CalculateRowTotal(maintab,entitytab,prefix,columnname)
		{
			var gltable= document.getElementById(maintab);
			var entitygltable= document.getElementById(entitytab);
			var entitytotal1 =0 ;
			var val ;
			for(var i =1 ; i<gltable.rows.length;i++)
				{
					entitytotal1 =0 ;
					var dedAmount1 = PageManager.DataService.getControlInBranch(gltable.rows[i],columnname).value;
					if(dedAmount1 =='undefined' || dedAmount1=="")
						dedAmount1 =0;
						{
							for(var j =1 ; j<entitygltable.rows.length;j++)
								{
									val = PageManager.DataService.getControlInBranch(entitygltable.rows[j],prefix+"grid_amount");
									if(val!=null)
										{
											var rowIndex1 = PageManager.DataService.getControlInBranch(entitygltable.rows[j],prefix+"grid_rowIndex").value;
											if(rowIndex1 == i && eval(val.value)>0)
													entitytotal1 = eval(entitytotal1)+ eval(val.value) ;
										}
								}
					// if only payee details entered and amount is not entered
							if(eval(dedAmount1)<eval(entitytotal1))
								{
								bootbox.alert(" amount should be equal to the total payee amount in Row "+ i);
									PageManager.DataService.getControlInBranch(gltable.rows[i],columnname).focus();
									return false;
								}
							else if((eval(entitytotal1)!=0)&&eval(dedAmount1)>eval(entitytotal1))
								{
								bootbox.alert(" amount should be equal to the total payee amount in Row "+ i);
									PageManager.DataService.getControlInBranch(gltable.rows[i],columnname).focus();
									return false;
								}
						}
						/** Both Total matches then call calTotal & calNetPayment */
						if(eval(dedAmount1) == eval(entitytotal1))
							continue;
						else if(dedAmount1 !='' && eval(dedAmount1)>0 && eval(entitytotal1)!=0 && entitytotal1!= "-0" )
						{
							if(eval(dedAmount1) > eval(entitytotal1) || eval(dedAmount1) < eval(entitytotal1) && entitytotal1!= "-0")
							{
								bootbox.alert("Payees amount should be equal to the total amount in Row "+ i);
								PageManager.DataService.getControlInBranch(gltable.rows[i],columnname).focus();
								return false;
							}
						}
					}
			return true;
		}
           
             

