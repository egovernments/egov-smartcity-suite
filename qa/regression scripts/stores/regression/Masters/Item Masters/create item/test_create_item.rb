require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file("stores_common_methods.rb")

include Stores_item_methods
include Stores_common_methods

class Stores_TC_Create_Item< Test::Unit::TestCase 
  

   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   login_to_stores
      
   #~ rescue Exception => e
     #~ puts e
 end
   
   
    
  def test_0010_create_item_valid_values
    
    tc = '0010'
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    
    end
  
  def test_0020_create_item_valid_values
    
    tc = '0020'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes)  
    view_item_success(tc, test_data, attributes)
  end
  
  def test_0030_create_item_valid_values
    
    tc = '0030'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
  end
   
   
  def test_0040_create_item_valid_values
    
    tc = '0040'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
      test_data = hash_array[2]
   
   create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    end
  
  def test_0050_create_item_valid_values
    
    tc = '0050'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    end
  
  def test_0060_create_item_valid_values
    
    tc = '0060'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)  
    
     end
  
  def test_0070_create_item_valid_values
    
    tc = '0070'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    
    end
  
  def test_0080_create_item_without_description
    
    tc = '0080'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes) 
    view_item_failure(tc, test_data, attributes)
    
    end
  
  def test_0090_create_item_without_UOM_contract_required
    tc = '0090'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes) 
    view_item_failure(tc, test_data, attributes)
    
    end
  
  def test_0091_create_item_wtihout_item_number 
    tc = '0091'    
    hash_array = get_input_data_oo_hasharray(tc, CREATE_ITEM_SHEET, Stores_constants::INPUT_OO)
    attributes = hash_array[1]
    test_data = hash_array[2]
    
    create_item(tc, test_data, attributes) 
    view_item_failure(tc, test_data, attributes)
    
    end
  
   
def teardown

logout_stores
close_all_windows

  rescue Exception => e
  puts e  
    
  ensure
    Process.kill(9,@pid)
  end

end

