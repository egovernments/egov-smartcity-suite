require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file('stores_mmrn_methods.rb')
require path_to_file('stores_supplier_bill_methods.rb')


include Stores_common_methods
include Stores_item_methods
include Stores_mmrn_methods
include Stores_supplier_bill_methods


class Stores_TC_Approve_Reject_Supplier_bill < Test::Unit::TestCase 
  
  
   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   #~ login_to_stores
      
   rescue Exception => e
     puts e
   end
   
   

def test_0010_Approve_supplier_bill_from_Creator_and_Reject_by_Approver
  
  tc = '0010'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_APPROVE_REJECT_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                          # CREATER LOGIN

    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    create_mmrn_success(tc, test_data, attributes)
    approve_mmrn(tc, test_data, attributes, "Drafts")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                       # APPROVER LOGIN
   approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                         # CREATER LOGIN
    create_supplier_bill_success(tc, test_data, attributes, "MMRN")
    view_supplier_bill(tc, test_data, attributes)
    approve_reject_supplier_bill(tc, test_data, attributes, "Drafts", "Approve")
  logout_stores
  
  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   
   approve_reject_supplier_bill(tc, test_data, attributes, "Inbox", "Reject")    # APPROVER LOGIN
  logout_stores 
  
end

def test_0020_Create_supplier_bill_by_Creator_and_Reject_it_by_himself
  
  tc = '0020'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_APPROVE_REJECT_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                              # CREATER LOGIN

    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    create_mmrn_success(tc, test_data, attributes)
    approve_mmrn(tc, test_data, attributes, "Drafts")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                          # APPROVER LOGIN
   approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                              # CREATER LOGIN
    create_supplier_bill_success(tc, test_data, attributes, "MMRN")
    approve_reject_supplier_bill(tc, test_data, attributes, "Drafts", "Reject")
  logout_stores
  
end

def test_0030_Create_supplier_bill_by_Creator_Approve_it_from_both_levels
  
  tc = '0030'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_APPROVE_REJECT_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                               # CREATER LOGIN

    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    create_mmrn_success(tc, test_data, attributes)
    approve_mmrn(tc, test_data, attributes, "Drafts")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                            # APPROVER LOGIN
   approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                               # CREATER LOGIN
    create_supplier_bill_success(tc, test_data, attributes, "MMRN")
    approve_reject_supplier_bill(tc, test_data, attributes, "Drafts", "Approve")
  logout_stores
  
  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                            # APPROVER LOGIN
   approve_reject_supplier_bill(tc, test_data, attributes, "Inbox", "Approve")
  logout_stores 
  
end

def test_0040_Create_supplier_bill_Approve_by_Creator_Reject_by_Approver_again_Approve_by_Creater
  
  tc = '0040'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_APPROVE_REJECT_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                               # CREATER LOGIN

    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)
    create_mmrn_success(tc, test_data, attributes)
    approve_mmrn(tc, test_data, attributes, "Drafts")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                            # APPROVER LOGIN
   approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores 
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                               # CREATER LOGIN
    create_supplier_bill_success(tc, test_data, attributes, "MMRN")
    approve_reject_supplier_bill(tc, test_data, attributes, "Drafts", "Approve")
  logout_stores
  
  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)                            # APPROVER LOGIN
   approve_reject_supplier_bill(tc, test_data, attributes, "Inbox", "Reject")
  logout_stores 
  
    login_to_stores(STORES_USER_NAME, STORES_PASSWORD)                               # CREATER LOGIN
    approve_reject_supplier_bill(tc, test_data, attributes, "Inbox", "Approve")
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
