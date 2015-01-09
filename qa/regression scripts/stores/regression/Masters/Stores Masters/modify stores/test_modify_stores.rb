#~ require 'watir'
#~ require 'test/unit'
#~ require 'win32/process'
#~ require 'ci/reporter/rake/test_unit_loader'

#~ require 'path_helper'
#~ require path_to_file("Utils.rb")
#~ require path_to_file("stores_common_methods.rb")
#~ require path_to_file("stores_stores_methods.rb")

#~ include Stores_common_methods
#~ include Stores_stores_methods

#~ class Stores_TC_Modfiy_Stores< Test::Unit::TestCase 
  

   #~ def setup
  
   #~ @pid = Process.create(   
     #~ :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     #~ :creation_flags  => Process::DETACHED_PROCESS
     #~ ).process_id

   #~ login_to_stores
   
      
   #~ rescue Exception => e
     #~ puts e
   #~ end
   
   
   
#~ def test_0010_modify_stores_valid_data

  #~ tc = '0010'
  #~ hash_array =  get_input_data_oo_hasharray(tc, MODIFY_STORES_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]

  #~ create_stores(tc,test_data, attributes)  
  #~ modify_stores(tc, test_data, attributes)
  #~ view_stores_success(tc, test_data, attributes)
  
#~ end

  
#~ def test_0020_modify_stores_valid_data

  #~ tc ='0020'
  #~ hash_array =  get_input_data_oo_hasharray(tc,MODIFY_STORES_SHEET, Stores_constants::INPUT_OO)
  #~ attributes = hash_array[1]
  #~ test_data = hash_array[2]
  
  #~ ie_create_stores = create_stores(tc,test_data, attributes)
  #~ check_for_create_stores_success(tc,test_data, attributes,ie_create_stores) 
  
  #~ ie_modify_stores = modify_stores(tc,test_data, attributes)
  #~ check_for_modify_stores_success(tc,test_data, attributes, ie_modify_stores)   

#~ end


#~ def test_0030_modify_stores_valid_data

#~ tc = '0030'
#~ hash_array =  get_input_data_oo_hasharray(tc,MODIFY_STORES_SHEET, Stores_constants::INPUT_OO)
#~ attributes = hash_array[1]
#~ test_data = hash_array[2]

#~ ie_create_stores = create_stores(tc,test_data, attributes)
#~ check_for_create_stores_success(tc,test_data, attributes,ie_create_stores) 

#~ ie_modify_stores = modify_stores(tc,test_data, attributes)
#~ check_for_modify_stores_success(tc,test_data, attributes, ie_modify_stores)  

#~ end
  
  
#~ def test_0040_modify_stores_invalid_data_special_chars

#~ tc = '0030'
#~ hash_array =  get_input_data_oo_hasharray(tc,MODIFY_STORES_SHEET, Stores_constants::INPUT_OO)
#~ attributes = hash_array[1]
#~ test_data = hash_array[2]

#~ ie_create_stores = create_stores(tc,test_data, attributes)
#~ check_for_create_stores_success(tc,test_data, attributes,ie_create_stores) 

#~ ie_modify_stores = modify_stores(tc,test_data, attributes)
#~ check_for_modify_stores_failure(tc,test_data, attributes, ie_modify_stores) 

#~ end
  
  
  

#~ def teardown

#~ logout_stores
#~ close_all_windows

#~ rescue Exception => e
    #~ puts e  
    
    #~ ensure
    #~ Process.kill(9,@pid)
  #~ end

#~ end   
   
   