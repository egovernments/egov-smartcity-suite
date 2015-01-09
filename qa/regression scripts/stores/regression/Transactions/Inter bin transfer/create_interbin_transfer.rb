require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_item_methods.rb")
require path_to_file("stores_mmrn_methods.rb")
require path_to_file("stores_interbin_methods.rb")

include Stores_common_methods
include Stores_item_methods
include Stores_mmrn_methods
include Stores_interbin_methods

class Stores_TC_Create_inter_bin_transfer< Test::Unit::TestCase 
  
def setup

@pid = Process.create(
:app_name => "ruby \"#{path_to_file("clicker.rb")}\"",
:creation_flags => Process::DETACHED_PROCESS
).process_id

#~ login_to_stores

#~ rescue Exception => e
#~ puts e
end

 
    
def test_0010_create_interbin_transfer_success

  tc = '0010'    
  
  hash_array = get_input_data_oo_hasharray(tc, CREATE_INTERBIN_TRANSFER_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]
  
    login_to_stores(STORES_USER_NAME, STORES_PASSWORD)
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)

    create_mmrn_success(tc, test_data, attributes)
    approve_mmrn(tc, test_data, attributes,"Drafts")
    logout_stores

    login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)
    approve_mmrn(tc, test_data, attributes,"Inbox")
    logout_stores  

    login_to_stores(STORES_USER_NAME, STORES_PASSWORD) 
  
   create_inter_bin_transfer_success(tc,test_data, attributes )
   view_interbin_transfer(tc, test_data, attributes)
  
  logout_stores

end




def test_0020_create_interbin_transfer_failure

  tc = '0020'    
  
  hash_array = get_input_data_oo_hasharray(tc, CREATE_INTERBIN_TRANSFER_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]


    login_to_stores(STORES_USER_NAME, STORES_PASSWORD)
    create_item(tc, test_data, attributes)
    view_item_success(tc, test_data, attributes)

    create_mmrn_success(tc, test_data, attributes)
    approve_mmrn(tc, test_data, attributes,"Drafts")
    logout_stores

    login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)
    approve_mmrn(tc, test_data, attributes,"Inbox")
    logout_stores  

    login_to_stores(STORES_USER_NAME, STORES_PASSWORD) 
  
    inter_bin_transfer_failure(tc, test_data, attributes)


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

   