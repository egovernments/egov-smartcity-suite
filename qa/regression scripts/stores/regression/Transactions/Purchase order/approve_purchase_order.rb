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


class Stores_TC_Approve_Purchase_Order < Test::Unit::TestCase 
  
  
   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   #~ login_to_stores
      
   rescue Exception => e
     puts e
   end
   
   

def test_0010_approve_po_non_indent_by_creator_and_manager
  
  tc = '0010'   
  hash_array =  get_input_data_oo_hasharray(tc,APPROVE_REJECT_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                                  # CREATOR LOGIN
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_success(tc, test_data, attributes, "po non indent")          # Create PO 
  approve_reject_purchase_order(tc, test_data, attributes, "Drafts", "Approve")      # Approve PO by creator
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                                # MANAGER LOGIN
  approve_reject_purchase_order(tc, test_data, attributes, "Inbox", "Approve")        # Approve PO by manager
  logout_stores 

end

def test_0020_approve_po_non_indent_by_creator_and_rejected_by_manager
  
  tc = '0020'   
  hash_array =  get_input_data_oo_hasharray(tc,APPROVE_REJECT_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                                                  # CREATOR LOGIN

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_success(tc, test_data, attributes, "po non indent")                           # Create PO 
  approve_reject_purchase_order(tc, test_data, attributes, "Drafts", "Approve")                      # Approve PO by creator
  logout_stores 

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                                                # MAN AGER LOGIN
  approve_reject_purchase_order(tc, test_data, attributes, "Inbox", "Reject")                          # Reject PO by manger
  logout_stores 

end

def test_0030_create_po_non_indent_by_creator_and_reject_it
  
  tc = '0030'   
  hash_array =  get_input_data_oo_hasharray(tc,APPROVE_REJECT_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                                #  CREATOR LOGIN

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_success(tc, test_data, attributes, "po non indent")        # Create PO
  approve_reject_purchase_order(tc, test_data, attributes, "Drafts", "Reject")     # Reject PO by creator
  logout_stores

end


def test_0040_approve_po_indent_by_creator_and_manager
  
  tc = '0040'   
  hash_array =  get_input_data_oo_hasharray(tc,APPROVE_REJECT_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                          # CREATOR LOGIN

    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
                                                                 # Create indent by creator
                                         # Approve indent by creator
 create_material_indent_success(tc, test_data, attributes, "Indent")

approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
  
  logout_stores


  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                        # MANAGER LOGIN
                                                             # Approve indent by manager
 
  approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")

  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                            # CREATOR LOGIN
    create_purchase_order_success(tc, test_data, attributes, "po indent")        # Create PO
    approve_reject_purchase_order(tc, test_data, attributes, "Drafts", "Approve")  # Approve po by creator
  logout_stores
    
  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                          # MANAGER LOGIN
  approve_reject_purchase_order(tc, test_data, attributes, "Inbox", "Approve")   # Approve po by manager
  logout_stores 

end


def test_0050_po_non_indent_approved_by_creator_and_rejected_by_manager_and_again_approve_by_manager
  
  tc = '0050'   
  hash_array =  get_input_data_oo_hasharray(tc,APPROVE_REJECT_PO_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                                      # CREATOR LOGIN

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_purchase_order_success(tc, test_data, attributes, "po non indent")             # Create PO
  approve_reject_purchase_order(tc, test_data, attributes, "Drafts", "Approve")         # Approve po by creator
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                                  # MAN AGER LOGIN
  approve_reject_purchase_order(tc, test_data, attributes, "Inbox", "Reject")            # Reject po by manager     
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                                     # CREATOR LOGIN
  approve_reject_purchase_order(tc, test_data, attributes, "Inbox", "Approve")          # Approve po by creator 
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

