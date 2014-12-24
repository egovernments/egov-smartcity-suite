require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_item_type_methods.rb")

include Stores_common_methods
include Stores_item_type_methods

class Stores_TC_Modify_Item_type< Test::Unit::TestCase 
  

   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   login_to_stores
      
   #~ rescue Exception => e
     #~ puts e
     
 end
   
   
    
def test_0010_modify_item_type_with_valid_values
  
  tc = '0010'
  hash_array = get_input_data_oo_hasharray(tc, MODIFY_ITEM_TYPE_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item_type_success(tc, test_data, attributes)
  modify_item_type_success(tc, test_data, attributes)
  view_item_type_success(tc, test_data, attributes)

  
  end
  
  
 def test_0020_modify_item_type_changing_stockinhandcode
    
  tc = '0020'
  hash_array = get_input_data_oo_hasharray(tc, MODIFY_ITEM_TYPE_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
    
  create_item_type_success(tc, test_data, attributes)
  modify_item_type_success(tc, test_data, attributes)
  view_item_type_success(tc, test_data, attributes)
    
  end
  

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

end