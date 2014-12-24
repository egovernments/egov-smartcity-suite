require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_stores_methods.rb")

include Stores_common_methods
include Stores_stores_methods

class Stores_TC_Create_Stores< Test::Unit::TestCase 
  

   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id
     
   login_to_stores
    
   #~ rescue Exception => e
     #~ puts e
   
   end
   
  
def test_0010_create_stores_valid_data

  tc = '0010'    
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)    
  view_stores_success(tc, test_data, attributes)
  
end
  
  
def test_0020_create_stores_valid_data

  tc ='0020'
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)    
  view_stores_success(tc, test_data, attributes)
  
end
  
  
def test_0030_create_stores_valid_data

  tc = '0030'
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)    
  view_stores_success(tc, test_data, attributes)

end
  
  
def test_0040_create_stores_without_bins

  tc = '0040'
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)    
  view_stores_success(tc, test_data, attributes)

end
  
def test_0050_create_stores_without_store_name

  tc ='0050'
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)   
  view_stores_failure(tc, test_data, attributes)
  
end
  
  
def test_0060_create_stores_without_store_no_description

  tc ='0060'
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)   
  view_stores_failure(tc, test_data, attributes)

end
  
  
def test_0070_create_stores_without_Department_Billingaddress

  tc ='0070'
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)   
  view_stores_failure(tc, test_data, attributes)

end

  
def test_0080_create_store_with_already_existing_store

  tc = '0080'
  hash_array  = get_input_data_oo_hasharray(tc, CREATE_STORES_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_stores(tc,test_data, attributes)   
  view_stores_failure(tc, test_data, attributes)

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
  