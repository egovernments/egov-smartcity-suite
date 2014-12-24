	<script language="JavaScript"  type="text/JavaScript">

		function formOnLoad()
		{
			//document.getElementById('msg').innerHTML ='';
		}
		
		function onSubmit()
	  	{
			if(document.getElementById("certTypeIds").length!=0)
			{
				var certTypeIdsLength = document.getElementById("certTypeIds").length;			
				for (var i = 0; i<certTypeIdsLength; i++){				    
					document.getElementById("certTypeIds").options[i].selected=true;
				}
			}
			return true;
	  	}
	  	
		function move(inputControl)
		{  
			var left = document.getElementById("certTypeIdLeft");  
			var right = document.getElementById("certTypeIds"); 
			var from, to;  
			var bAll = false;  
			switch (inputControl.value)  
			{  
				case '<':    
				from = right; 
				to = left;    
				break;  
				case '>':    
				from = left; 
				to = right;    
				break;  
			}  
			
			for (var i = from.length - 1; i >= 0; i--)  
			{    
				var o = from.options[i];    
				if (o.selected)    
				{      
					try      
					{        
						var clone = o.cloneNode(true);
						to.appendChild(clone);
					    //Standard method, fails in IE (6&7 at least)      
					}      
					catch (e)  
					{ 
					 to.add(o); 
					// IE only  
					} 
					from.remove(i);        
				}  
			}
		}
	</script>
	
	<s:push value="model">
		<tr>
			<td colspan="4" class="headingwk">
			<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
              	<div class="headplacer"><s:text name="NomineeCertReq.Heading"/></div>
           </td>
        </tr>
		
		<tr>
			<td class="whiteboxwk" ><span class="mandatory">*</span><s:text name="NomineeCertReq.RelationType"/> </td>
			<td class="whitebox2wk" colspan="3">
			<s:select name="relationType" id="relationType" list="dropdownData.eisRelationTypeList" listKey="id" 
			listValue="nomineeType" headerKey="-1" headerValue="----Select----"  value="%{relationType.id}"/> </td>
		</tr>
		
		<tr>
			<td  class="greyboxwk"><span class="mandatory">*</span><s:text name="NomineeCertReq.CertType"/> </td>
   			<td class="greybox2wk">
				<s:select name="certTypeIdLeft" id="certTypeIdLeft" multiple="true" list="dropdownData.eligCertTypeList" listKey="id" 
				listValue="type" value="%{certTypeIdLeft.id}" size="4" style="width:200px" />
			</td>
			<td align="center" class="greyboxwk" >				    		
				<input type="button" value='&gt;' style="width:50px" class="button" id="left" onclick="move(this);"/>
				<br>
				<s:textfield value="" style="width:50px;height:5px;border:0px" readOnly="true" />
				<br>
				<input type="button" value='&lt' style="width:50px" class="button" id="right" onclick="move(this);"/>
			</td>
			<td class="greybox2wk">
				<select name="certTypeIds" id="certTypeIds" multiple="true" size="4" style="width:200px">
				</select>
			</td>
	
		</tr>
	</s:push>
