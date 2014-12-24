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


class Stores_TC_Modify_Purchase_Order < Test::Unit::TestCase 
  
  
   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   #~ login_to_stores
      
   rescue Exception => e
     puts e
   end
   
   

def test_0010_modify_po_non_indent_by_creator_by_adding_the_line_items_to_the_existing_line_items
  
  tc = '0010'   
  hash_array =  get_input_data_oo_hasharray(tc,MODIFY_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  # CREATOR INBOX

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
                                               
  create_purchase_order_success(tc, test_data, attributes, "po non indent")                            # Create PO 
  modify_purchase_order_success(tc, test_data, attributes, "Drafts", "Approve")                       # Modify PO by creator
  logout_stores

end

def test_0020_modify_PO_deleting_the_existing_line_items_add_it
  
  tc = '0020'   
  hash_array =  get_input_data_oo_hasharray(tc,MODIFY_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
    
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                                     # CREATOR LOGIN
    
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    create_purchase_order_success(tc, test_data, attributes, "po non indent")             # Create PO
    modify_purchase_order_success(tc, test_data, attributes, "Drafts", "Approve")        # Modify po by creator
    
  logout_stores

end

def test_0030_modify_PO_indent_modify_order_date_and_deleting_the_existing_line_items
  
  tc = '0030'   
  hash_array =  get_input_data_oo_hasharray(tc,MODIFY_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                          # CREATOR LOGIN

    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
                                           
     create_material_indent_success(tc, test_data, attributes, "Indent")
    approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
                                                     # Approve indent by creator
  
  logout_stores


  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                        # MANAGER LOGIN
                                                                  # Approve indent by manager
 approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
 logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                                     # CREATOR LOGIN
  
    create_purchase_order_success(tc, test_data, attributes, "po indent")             # Create PO
    modify_purchase_order_success(tc, test_data, attributes, "Drafts", "Approve")        # Modify po by creator
    
  logout_stores

end




def teardown

#~ logout_stores
close_all_windows

rescue Exception => e
puts e  


ensure
Process.kill(9,@pid)
end

end

