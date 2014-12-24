require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file('stores_material_indent_methods.rb')

include Stores_common_methods
include Stores_item_methods
include Stores_material_indent_methods

class Stores_TC_Material_Indent < Test::Unit::TestCase 
  
  
   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   login_to_stores
      
   rescue Exception => e
     puts e
     
   end
   
   

def test_0010_create_material_indent_valid_data_Capital
  
  tc = '0010'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  
  end
    
def test_0020_create_material_indent_valid_data_consumption
 
  tc = '0020'
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET,Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  
  
end
  
def test_0030_create_material_indent_without_purpose
 
  tc = '0030'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET,Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_failure(tc, test_data, attributes, "Indent")
  
end

def test_0040_create_material_indent_without_issuing_store
 
  tc = '0040'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET, Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_failure(tc, test_data, attributes, "Indent")

  
end

def test_0050_create_material_indent_without_account_code
 
  tc = '0050'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET, Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_failure(tc, test_data, attributes, "Indent")

end

def test_0060_create_material_indent_without_Quantity

  tc = '0060'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET, Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_failure(tc, test_data, attributes, "Indent")

end

def test_0070_create_material_indent_without_indenting_store_issuing_store
   
  tc = '0070'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET, Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_failure(tc, test_data, attributes, "Indent")

end
  
def test_0080_create_material_indent_without_Financial_details

  tc = '0080'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET, Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  
  
end
  
def test_0090_create_material_indent_purchase

  tc = '0090'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET, Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  
end
  
def test_0091_create_material_indent_item_not_purchasable

  tc = '0091'       
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET, Stores_constants::INPUT_OO)  
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_failure(tc, test_data, attributes, "Indent")

end


def test_0092_create_material_indent_valid_data_Indent_purpose_capital
  
  tc = '0092'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  
end

#~ #93 not to be used
 
#~ def test_0093_create_material_indent_valid_data_Indent_purpose_maintenence_and_repair
  
  #~ tc = '0093'   
  #~ hash_array =  get_input_data_oo_hasharray(tc,CREATE_INDENT_SHEET,Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ create_item(tc, test_data, attributes)
  #~ view_item_success(tc, test_data, attributes)
  #~ create_material_indent_success(tc, test_data, attributes, "Indent")
  
  #~ end





def teardown

logout_stores
close_all_windows

rescue Exception => e
puts e  


ensure
Process.kill(9,@pid)
end

end
