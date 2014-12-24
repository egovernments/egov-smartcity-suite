require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file('stores_material_indent_methods.rb')
require path_to_file('stores_purchase_order_methods.rb')


include Stores_common_methods
include Stores_item_methods
include Stores_material_indent_methods
include Stores_purchase_order_methods


class Stores_TC_Create_Purchase_Order < Test::Unit::TestCase 
  
  
   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   #~ login_to_stores
      
   rescue Exception => e
     puts e
   end
   
   

def test_0010_create_po_indent_type_valid_values_for_future_date_and_delivery_date
  
  tc = '0010'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX

  create_item(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX
  create_purchase_order_success(tc, test_data, attributes, "po indent")
  logout_stores
  
end


def test_0020_create_purchase_order_for_past_dates_less_than_indent_date
  
  tc = '0020'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX

  create_item(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX
  create_purchase_order_failure(tc, test_data, attributes, "po indent")
  logout_stores
  
end


def test_0030_create_purchase_order_for_multiple_line_items_from_the_indent
  
  tc = '0030'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
    
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX

  create_item(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "Indent")
  approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX
  create_purchase_order_success(tc, test_data, attributes, "po indent")
  logout_stores

  
end


def test_0040_create_po_non_indent_with_item_indent_type_and_purchasable
  
  tc = '0040'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_success(tc, test_data, attributes, "po non indent")
  view_purchase_order(tc, test_data, attributes)
  
  logout_stores
  
end
  
def test_0050_create_po_non_indent_with_item_non_indent_type_and_purchasable

  tc = '0050'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_success(tc, test_data, attributes, "po non indent")
  view_purchase_order(tc, test_data, attributes)

  logout_stores

end

  def test_0060_create_po_non_indent_with_item_indent_type_and_not_purchasable
  
  tc = '0060'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_failure(tc, test_data, attributes, "po non indent")
  
  logout_stores

end
  
def test_0070_create_po_non_indent_for_multiple_line_items
  
  tc = '0070'   
  hash_array =  get_input_data_oo_hasharray(tc,CREATE_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_success(tc, test_data, attributes, "po non indent")
  view_purchase_order(tc, test_data, attributes)
  
  logout_stores

end
   
  
  
  

def teardown

#~ logout_stores
#~ close_all_windows

rescue Exception => e
puts e  


ensure
Process.kill(9,@pid)
end

end
