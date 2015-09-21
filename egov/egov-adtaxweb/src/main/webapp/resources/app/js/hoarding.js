$(document).ready(function(){
    
   
   var agency = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '../agency/agencies?name=%QUERY',
			filter: function (data) {
				return $.map(data, function (ct) {
					return {
						name: ct.name,
						value: ct.id
					};
				});
			}
		}
	});
	
   agency.initialize(); // Instantiate the Typeahead UI
	
	$('.typeahead').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: agency.ttAdapter()
	}).on('typeahead:selected typeahead:autocompleted typeahead:matched', function(event, data){
		$("#agencyId").val(data.value);    
   });
   
   $('.add-attachment').click(function(){
       console.log('came');
       $(this).parent().before('<div class="col-sm-3 add-margin"> <input type="file" class="form-control" required> </div>');
   });

   $('#category').change(function(){
		if (this.value === '') {
			return;
		} else {
			$.ajax({
				type: "GET",
				url: "subcategories",
				cache: true,
				dataType: "json",
				data:{'categoryId' : this.value}
			}).done(function(value) {
				$.each(value, function(index, val) {
				     $('#subCategory').append($('<option>').text(val.description).attr('value', val.id));
				});
			});
		}
	});	
});