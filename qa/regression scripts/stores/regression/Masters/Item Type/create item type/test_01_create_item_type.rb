#~ require 'watir/ie'
#~ require 'test/unit'
#~ require 'watir/testcase'
#~ require 'win32/process'
#~ require 'ci/reporter/rake/test_unit_loader'

#~ require 'path_helper'
#~ require path_to_file("Utils.rb")
#~ require path_to_file("stores_item_type_methods.rb")

#~ include Stores_item_type_methods

#~ class Test_Create_Item_type< Test::Unit::TestCase 
  

   #~ def setup
  
   #~ @pid = Process.create(   
     #~ :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     #~ :creation_flags  => Process::DETACHED_PROCESS
     #~ ).process_id

   #~ login_to_stores
      
   #~ rescue Exception => e
     #~ puts e
 #~ end
   
   
    
  #~ def test_0010_create_item_type_with_valid_values
  
    #~ tc = '0010'
    #~ hash_array = get_input_data(tc, CREATE_ITEM_TYPE_SHEET)
    #~ attributes = hash_array[1]
    #~ test_data = hash_array[2]
    
    #~ ie_create_item_type = create_item_type(tc, test_data, attributes)
    #~ check_for_create_item_type_success(tc, test_data, attributes, ie_create_item_type)
    
  #~ end
  
  
  #~ def test_0020_create_item_type_with_parent_item_type
    
    #~ tc = '0020'
    #~ hash_array = get_input_data(tc, CREATE_ITEM_TYPE_SHEET)
    #~ attributes = hash_array[1]
    #~ test_data = hash_array[2]
    
    #~ ie_create_item_type = create_item_type(tc, test_data, attributes)
    #~ check_for_create_item_type_success(tc, test_data, attributes, ie_create_item_type)
    
  #~ end
  
  #~ def test_0030_create_item_type_with_special_char_for_item_type
    
    #~ tc = '0030'
    #~ hash_array = get_input_data(tc, CREATE_ITEM_TYPE_SHEET)
    #~ attributes = hash_array[1]
    #~ test_data = hash_array[2]
    
    #~ ie_create_item_type = create_item_type(tc, test_data, attributes)
    #~ check_for_create_item_type_failure(tc, test_data, attributes, ie_create_item_type)
    
  #~ end
  
  #~ def test_0040_create_item_type_without_item_type
    
    #~ tc = '0040'
    #~ hash_array = get_input_data(tc, CREATE_ITEM_TYPE_SHEET)
    #~ attributes = hash_array[1]
    #~ test_data = hash_array[2]
    
    #~ ie_create_item_type = create_item_type(tc, test_data, attributes)
    #~ check_for_create_item_type_failure(tc, test_data, attributes, ie_create_item_type)
    
  #~ end
  
  
  #~ def test_0050_create_item_type_withtout_item_type_description
    
    #~ tc = '0050'
    #~ hash_array = get_input_data(tc, CREATE_ITEM_TYPE_SHEET)
    #~ attributes = hash_array[1]
    #~ test_data = hash_array[2]

    #~ ie_create_item_type = create_item_type(tc, test_data, attributes)
    #~ check_for_create_item_type_failure(tc, test_data, attributes, ie_create_item_type)
    
  #~ end
  
  
  #~ def test_0060_create_item_type_without_stock_in_hand_code
    
    #~ tc = '0060'
    #~ hash_array = get_input_data(tc, CREATE_ITEM_TYPE_SHEET)
    #~ attributes = hash_array[1]
    #~ test_data = hash_array[2]
    
    #~ ie_create_item_type = create_item_type(tc, test_data, attributes)
    #~ check_for_create_item_type_failure(tc, test_data, attributes, ie_create_item_type)
    
  #~ end
  
  
  
  
  
  
  
  
  
   #~ def teardown
    
    #~ logout_stores
   
  #~ rescue Exception => e
    #~ puts e  
    
  #~ ensure
    #~ Process.kill(9,@pid)
  #~ end

#~ end