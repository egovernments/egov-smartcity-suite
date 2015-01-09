require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_uom_category_methods.rb")
require path_to_file("stores_uom_methods.rb")

include Stores_common_methods
include Stores_uom_category_methods
include Stores_uom_methods

class Stores_TC_Create_UOM< Test::Unit::TestCase 
  

   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   login_to_stores
      
   #~ rescue Exception => e
     #~ puts e
     
 end
  
  
def test_0010_create_uom_with_valid_values

  tc = '0010'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_UOM_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  uom_category_details(tc, test_data, attributes)
  create_uom(tc, test_data, attributes)
  view_uom_success(tc, test_data, attributes)
  

end
  
  
#~ def test_0020_create_uom_without_selecting_baseuom_checkbox

  #~ tc = '0020'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_UOM_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ create_uom(tc, test_data, attributes)
  #~ view_uom_success(tc, test_data, attributes)
    

#~ end
  
#~ def test_0030_create_uom_without_uom

  #~ tc = '0030'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_UOM_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ create_uom(tc, test_data, attributes)
  #~ view_uom_failure(tc, test_data, attributes)
  
#~ end
  
#~ def test_0040_create_uom_without_uom_description

  #~ tc = '0040'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_UOM_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ create_uom(tc, test_data, attributes)
  #~ view_uom_success(tc, test_data, attributes)
  
#~ end
  
#~ def test_0050_create_uom_with_special_chars_for_uom

  #~ tc = '0050'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_UOM_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]
  
  #~ create_uom(tc, test_data, attributes)
  #~ view_uom_failure(tc, test_data, attributes)
  
#~ end
  
#~ def test_0060_create_uom_without_selecting_uomcategory

  #~ tc = '0060'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_UOM_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ create_uom(tc, test_data, attributes)
  #~ view_uom_failure(tc, test_data, attributes)

#~ end
  
#~ def test_0070_create_uom_without_conversion_factor

  #~ tc = '0070'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_UOM_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ create_uom(tc, test_data, attributes)
  #~ view_uom_failure(tc, test_data, attributes)
  
#~ end
  
def teardown

  logout_stores
  close_all_windows

  rescue Exception => e
  puts e  


  #~ if $browser !=nil
  #~ $browser.close
  #~ end  

  ensure
  Process.kill(9,@pid)
  
end

end#End of the class