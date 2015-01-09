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
require path_to_file('stores_mrn_methods.rb')
require path_to_file('stores_gmi_methods.rb')

include Stores_common_methods
include Stores_item_methods
include Stores_material_indent_methods
include Stores_mmrn_methods
include Stores_mii_methods
include Stores_mrn_methods
include Stores_gmi_methods

class Stores_TC_Approve_MRN< Test::Unit::TestCase 
  
def setup

@pid = Process.create(
:app_name => "ruby \"#{path_to_file("clicker.rb")}\"",
:creation_flags => Process::DETACHED_PROCESS
).process_id

#~ login_to_stores

rescue Exception => e
puts e
end

   
def test_0010_Approve_from_user_manager

  tc = '0010'
  hash_array = get_input_data_oo_hasharray(tc, APPROVE_IMRN_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(USER_NAME, PASSWORD)          # USER LOGIN
  
      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "My Drafts")  
      
  logout_stores

  login_to_stores(USER_NAME1, PASSWORD1)       # MANAGER LOGIN
      approve_mmrn(tc, test_data, attributes, "My Works")
  logout_stores

  login_to_stores(USER_NAME, PASSWORD)          # USER LOGIN
      create_gmi_success(tc, test_data, attributes)
      approve_gmi(tc, test_data, attributes, "My Drafts")
  logout_stores

  login_to_stores(USER_NAME1, PASSWORD1)          # MANAGER LOGIN
      approve_gmi(tc, test_data, attributes, "My Works")
  logout_stores
  
  login_to_stores(USER_NAME, PASSWORD)  # USER LOGIN
      create_imrn_success(tc, test_data, attributes, "GMI")
      approve_imrn(tc, test_data, attributes, "My Drafts")
  logout_stores
  
  login_to_stores(USER_NAME1, PASSWORD1)  # MANAGER LOGIN
      approve_imrn(tc, test_data, attributes, "My Works")
  logout_stores
  

end


def test_0020_Approve_from_user_reject_from_manager

  tc = '0020'
  hash_array = get_input_data_oo_hasharray(tc, APPROVE_IMRN_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(USER_NAME, PASSWORD)          #USER LOGIN
  
      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "My Drafts")  
      
  logout_stores

  login_to_stores(USER_NAME1, PASSWORD1)       # MANAGER LOGIN
      approve_mmrn(tc, test_data, attributes, "My Works")
  logout_stores


  login_to_stores(USER_NAME, PASSWORD)          # USER LOGIN
      create_gmi_success(tc, test_data, attributes)
      approve_gmi(tc, test_data, attributes, "My Drafts")
  logout_stores

  login_to_stores(USER_NAME1, PASSWORD1)          # MANAGER LOGIN
      approve_gmi(tc, test_data, attributes, "My Works")
  logout_stores

  login_to_stores(USER_NAME, PASSWORD)      # USER LOGIN
      create_imrn_success(tc, test_data, attributes, "GMI")
      approve_imrn(tc, test_data, attributes, "My Drafts")
  logout_stores

    login_to_stores(USER_NAME1, PASSWORD1)      # MANAGER LOGIN
      reject_imrn(tc, test_data, attributes, "My Works")
  logout_stores

end
  
  def test_0030_reject_from_user

  tc = '0030'
  hash_array = get_input_data_oo_hasharray(tc, APPROVE_IMRN_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(USER_NAME, PASSWORD)          #USER LOGIN
  
      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "My Drafts")  
      
  logout_stores

  login_to_stores(USER_NAME1, PASSWORD1)       # MANAGER LOGIN
      approve_mmrn(tc, test_data, attributes, "My Works")
  logout_stores


  login_to_stores(USER_NAME, PASSWORD)          # USER LOGIN
      create_gmi_success(tc, test_data, attributes)
      approve_gmi(tc, test_data, attributes, "My Drafts")
  logout_stores

  login_to_stores(USER_NAME1, PASSWORD1)          # MANAGER LOGIN
      approve_gmi(tc, test_data, attributes, "My Works")
  logout_stores

  login_to_stores(USER_NAME, PASSWORD)      # USER LOGIN
      create_imrn_success(tc, test_data, attributes, "GMI")
      reject_imrn(tc, test_data, attributes, "My Drafts")
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

  

