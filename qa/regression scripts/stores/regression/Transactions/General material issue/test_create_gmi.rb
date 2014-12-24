require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_uom_category_methods.rb")
require path_to_file("stores_uom_methods.rb")
require path_to_file("stores_item_type_methods.rb")
require path_to_file("stores_stores_methods.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file('stores_mmrn_methods.rb')
require path_to_file('stores_gmi_methods.rb')


include Stores_common_methods
include Stores_uom_category_methods
include Stores_uom_methods
include Stores_item_type_methods
include Stores_stores_methods
include Stores_item_methods
include Stores_mmrn_methods
include Stores_gmi_methods

class Stores_TC_Create_GMI< Test::Unit::TestCase 
  
def setup

@pid = Process.create(
:app_name => "ruby \"#{path_to_file("clicker.rb")}\"",
:creation_flags => Process::DETACHED_PROCESS
).process_id

#~ login_to_stores

#~ rescue Exception => e
#~ puts e
end

   
def test_0010_create_GMI_Inter_Store_Transfer

  tc = '0010'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_success(tc, test_data, attributes, "GMI")    
  logout_stores

end
   
   
def test_0020_create_GMI_Internal_Consumption

  tc = '0020'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_success(tc, test_data, attributes, "GMI")    
  logout_stores

end


def test_0030_create_GMI_Stock_Adjustment

  tc = '0030'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_success(tc, test_data, attributes, "GMI")
  logout_stores

end

def test_0040_create_GMI_Written_off

  tc = '0040'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_success(tc, test_data, attributes, "GMI")    
  logout_stores

end
  
def test_0050_create_GMI_without_store

  tc = '0050'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_failure(tc, test_data, attributes, "GMI")
  logout_stores

end
   
def test_0060_create_GMI_without_purpose

  tc = '0060'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_failure(tc, test_data, attributes, "GMI")
  logout_stores

end


def test_0070_create_GMI_without_itemno_acc_code


  tc = '0070'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_failure(tc, test_data, attributes, "GMI")
  logout_stores

  end
     
     
def test_0080_create_GMI_without_Quantity

  tc = '0080'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_failure(tc, test_data, attributes, "GMI")
  logout_stores

end
   
   
def test_0090_create_GMI_Capital

  tc = '0090'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_success(tc, test_data, attributes, "GMI")
  logout_stores
  
end


#Dont use 0091


#~ def test_0091_create_GMI_Asset_Repair

  #~ tc = '0091'
  #~ hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      #~ create_item(tc, test_data, attributes)
      #~ view_item_success(tc, test_data, attributes)
      #~ create_mmrn_success(tc, test_data, attributes)
      #~ approve_mmrn(tc, test_data, attributes, "Drafts")
  
  #~ logout_stores

  #~ login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      #~ approve_mmrn(tc, test_data, attributes, "Inbox")
  #~ logout_stores
  
  #~ login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    #~ create_gmi_success(tc, test_data, attributes, "GMI")
  #~ logout_stores

#~ end


def test_0092_create_GMI_Sale
  
  tc = '0092'
  hash_array = get_input_data_oo_hasharray(tc, CREATE_GMI_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX

      create_item(tc, test_data, attributes)
      view_item_success(tc, test_data, attributes)
      create_mmrn_success(tc, test_data, attributes)
      approve_mmrn(tc, test_data, attributes, "Drafts")
  
  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1) #MANAGER INBOX
      approve_mmrn(tc, test_data, attributes, "Inbox")
  logout_stores
  
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) # USER INBOX
    create_gmi_success(tc, test_data, attributes, "GMI")
  logout_stores

end

   
def teardown

#~ logout_stores
close_all_windows

  rescue Exception => e
  puts e  
  
  #~ if $browser !=nil
  #~ $browser.close
  #~ end  

ensure
  Process.kill(9,@pid)
 end

end