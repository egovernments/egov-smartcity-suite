require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file('stores_common_methods.rb')
require path_to_file("stores_item_methods.rb")
require path_to_file('stores_material_indent_methods.rb')
require path_to_file('stores_mmrn_methods.rb')
require path_to_file('stores_mii_methods.rb')

include Stores_common_methods
include Stores_item_methods
include Stores_material_indent_methods
include Stores_mmrn_methods
include Stores_mii_methods

class Stores_TC_Create_Material_Indnet_issue< Test::Unit::TestCase 
  
def setup

@pid = Process.create(
:app_name => "ruby \"#{path_to_file("clicker.rb")}\"",
:creation_flags => Process::DETACHED_PROCESS
).process_id

#~ login_to_stores

#~ rescue Exception => e
#~ puts e

end

   
def test_0010_create_Mii_with_issued_quantity

  tc = '0010'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_MII_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
                                                   
      create_material_indent_success(tc, test_data, attributes, "Indent")
      create_mmrn_success(tc, test_data, attributes)
                                                                
      approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
      approve_mmrn(tc, test_data, attributes, "Drafts")  
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)       # MANAGER LOGIN
    
       approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
      create_mii_success(tc, test_data, attributes, "Indent Issue")  
  logout_stores

end
  
  
def test_0020_create_Mii_with_quantity_zero

  tc = '0020'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_MII_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_material_indent_success(tc, test_data, attributes, "Indent")
      create_mmrn_success(tc, test_data, attributes)
                                                           
       approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
      approve_mmrn(tc, test_data, attributes, "Drafts")  
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)       # MANAGER LOGIN
 
        approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
      create_mii_failure(tc, test_data, attributes, "Indent Issue")    
  logout_stores

end


def test_0030_create_Mii_for_multiple_rows
  
  tc = '0030'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_MII_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

    login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
     create_material_indent_success(tc, test_data, attributes, "Indent")
      create_mmrn_success(tc, test_data, attributes)
                                                                                   
       approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
      approve_mmrn(tc, test_data, attributes, "Drafts")  
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)       # MANAGER LOGIN
      approve_material_indent(tc, test_data, attributes, "Inbox")
      approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
      create_mii_success(tc, test_data, attributes, "Indent Issue")    
  logout_stores

end


def test_0040_create_Mii_quantity_greater_than_indent_quantity

  tc = '0040'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_MII_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

    login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_material_indent_success(tc, test_data, attributes, "Indent")
      create_mmrn_success(tc, test_data, attributes)
                                                                          
         approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
      approve_mmrn(tc, test_data, attributes, "Drafts")  
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)       # MANAGER LOGIN
                                                                      
         approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
    create_mii_failure(tc, test_data, attributes, "Indent Issue")  
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




