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

class Stores_TC_View_GMI< Test::Unit::TestCase 
  
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
  hash_array = get_input_data_oo_hasharray(tc, VIEW_GMI_SHEET, Stores_constants::INPUT_OO)
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
     
      approve_gmi(tc, test_data, attributes, "Drafts") 
      view_gmi_success(tc, test_data, attributes)

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
