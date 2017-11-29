/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
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
 *
 */

//tab switch handle
var currenttabidx=0;
var lasttabidx=$('a[data-tabidx]').length-1;

document.body.addEventListener('keydown', function (e) {
	if(document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA' || document.activeElement.tagName == 'SELECT'){
		
	}else if(document.activeElement.tagName == 'BODY' || document.activeElement.tagName == 'A'){
		if((e.which === 37 || e.which === 39))
	    {
	      if(currenttabidx === lasttabidx)
	      {
	    	  if(e.which === 39)
	    	  $('a[data-tabidx="0"]').tab('show');
	    	  else if(e.which === 37)
	    	  $('a[data-tabidx="'+ (currenttabidx-1) +'"]').tab('show');
	      }
	      else
	      {
	    	  if(e.which === 37)
	    	  {
	    		 
	    		  currenttabidx = (currenttabidx === 0 ? lasttabidx : (currenttabidx-1));    		  
	    		  $('a[data-tabidx="'+ currenttabidx +'"]').tab('show');
	    	  }
	    	  else{
	    	    $('a[data-tabidx="'+ (currenttabidx+1) +'"]').tab('show');
	    	  }
	      }
	    }
	}
});

$(document).ready(function(){
	
	$('#code').attr('disabled',true);
	
   //file chooser filter validation
   $("input:file").change(
			function(e) {
				var fileName = $(this).val();
				if (fileName) {
					var fileext = fileName.split(".");
					var acceptedext = $(this).data('accept');
					if (acceptedext.split(',').indexOf(
							fileext[fileext.length - 1]) < 0) {
						bootbox.alert($(this).data('errormsg'));
						$(this).val('');
					}
					else if($(this).attr('id')== 'logo')
					{
						var reader = new FileReader();

			            reader.onload = function (e) {
			                $('#imglogo').attr('src', e.target.result);
			            };

			            reader.readAsDataURL(this.files[0]);
			        }
				}
				else {
					if($(this).attr('id')== 'logo')
					{
						$('#imglogo').attr('src', '');
					}
				}
			
	});
	
	//popup initialize
	$('[data-toggle="popover"]').popover({ html : true });

	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		  currenttabidx=$(this).data('tabidx');
	});
	
	jQuery('form').validate({
        ignore: ".ignore",
        invalidHandler: function(e, validator){
            if(validator.errorList.length)
            	$('#settingstab a[href="#' + jQuery(validator.errorList[0].element).closest(".tab-pane").attr('id') + '"]').tab('show');
        }
    });
	
	$('#submitform').click(function(e){
		if($('form').valid()){
			
		}else{
			e.preventDefault();
		}
	});
	
});

