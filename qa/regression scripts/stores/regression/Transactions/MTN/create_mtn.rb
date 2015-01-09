require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file("stores_material_indent_methods.rb")
require path_to_file('stores_mmrn_methods.rb')
require path_to_file('stores_mii_methods.rb')
require path_to_file("stores_gmi_methods.rb")
require path_to_file("stores_mtn_methods.rb")

include Stores_common_methods
include Stores_item_methods
include Stores_material_indent_methods
include Stores_mmrn_methods
include Stores_mii_methods
include Stores_gmi_methods
include Stores_mtn_methods

class Test_Create_MTN< Test::Unit::TestCase 
  
def setup

@pid = Process.create(
:app_name => "ruby \"#{path_to_file("clicker.rb")}\"",
:creation_flags => Process::DETACHED_PROCESS
).process_id

#~ login_to_stores

rescue Exception => e
puts e
end

  


#~ def test_0010_create_Material_Transfer_Note_General_issue

  #~ tc = '0010'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_MTN_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #USER INBOX

  #~ create_item(tc, test_data, attributes)
  #~ view_item_success(tc, test_data, attributes)
  #~ create_material_indent_success(tc, test_data, attributes, "MTN")
  #~ create_mmrn_success(tc, test_data, attributes)
  #~ approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
  #~ approve_mmrn(tc, test_data, attributes, "Drafts")
  #~ logout_stores

  #~ login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  #~ approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
  #~ approve_mmrn(tc, test_data, attributes, "Drafts")
  #~ logout_stores

  #~ login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
      #~ create_gmi_success(tc, test_data, attributes, "GMI-MTN")
      #~ approve_gmi(tc, test_data, attributes, "Drafts")
  #~ logout_stores

  #~ login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  #~ approve_gmi(tc, test_data, attributes, "Inbox")
  #~ logout_stores

  #~ login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
   #~ create_mtn_success(tc, test_data, attributes, "GMI")
  #~ logout_stores

#~ end

def test_0020_create_Material_Transfer_Note_indent_issue

  tc = '0020'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_MTN_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #USER INBOX

  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes, "MTN")
  create_mmrn_success(tc, test_data, attributes)
  approve_reject_material_indent(tc, test_data, attributes, "Drafts", "Approve")
  approve_mmrn(tc, test_data, attributes, "Drafts")
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  approve_reject_material_indent(tc, test_data, attributes, "Inbox", "Approve")
  approve_mmrn(tc, test_data, attributes, "Drafts")
  logout_stores

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
      create_mii_success(tc, test_data, attributes, "MTN Issue")
      approve_mii(tc, test_data, attributes, "Drafts")
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  approve_mii(tc, test_data, attributes, "Inbox")
  logout_stores

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD)  #USER INBOX
   create_mtn_success(tc, test_data, attributes, "Indent Issue")
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




