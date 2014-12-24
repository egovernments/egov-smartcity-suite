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


class Stores_TC_Modify_Supplier_bill < Test::Unit::TestCase 
  
  
   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   #~ login_to_stores
      
   rescue Exception => e
     puts e
   end
   
   

def test_0010_Modify_supplier_bill_by_adding_acc_and_deduction_details
  
  tc = '0010'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_MODIFY_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
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
    modify_supplier_bill(tc, test_data, attributes, "Drafts", "Success")
  logout_stores
   
 end
 
 
 
 def test_0020_Modify_supplier_bill_for_future_bill_dates
  
  tc = '0020'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_MODIFY_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
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
    modify_supplier_bill(tc, test_data, attributes, "Drafts", "Failure")
  logout_stores
   
end
_
 def test_0030_Modify_supplier_bill_for_bill_date_less_than_MRN_date
  
  tc = '0030'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_MODIFY_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
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
    modify_supplier_bill(tc, test_data, attributes, "Drafts", "Success")
  logout_stores
   
end

 def test_0040_Modify_supplier_date_less_than_MRN_date_duplicating_the_account_code
  
  tc = '0040'   
  hash_array =  get_input_data_oo_hasharray(tc, STORES_MODIFY_SUPPLIER_BILL_SHEET,Stores_constants::INPUT_OO)
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
    modify_supplier_bill(tc, test_data, attributes, "Drafts", "Failure")
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
