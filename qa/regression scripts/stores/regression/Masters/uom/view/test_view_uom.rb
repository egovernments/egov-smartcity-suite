require 'watir'
require 'test/unit'
require 'win32/process'
require 'ci/reporter/rake/test_unit_loader'

require 'path_helper'
require path_to_file("Utils.rb")
require path_to_file("stores_common_methods.rb")
require path_to_file("stores_uom_methods.rb")

include Stores_common_methods
include Stores_uom_methods

class Stores_TC_View_UOM< Test::Unit::TestCase 
  

   def setup
  
   @pid = Process.create(   
     :app_name       => "ruby \"#{path_to_file("clicker.rb")}\"",
     :creation_flags  => Process::DETACHED_PROCESS
     ).process_id

   login_to_stores
      
   #~ rescue Exception => e
     #~ puts e

end
   
   
def test_0010_view_uom_by_selecting_uom

  tc = '0010'
  hash_array = get_input_data_oo_hasharray(tc, VIEW_UOM_SHEET, Stores_constants::INPUT_OO)
  attributes = hash_array[1]
  test_data = hash_array[2]

  create_uom(tc, test_data, attributes)
  view_uom_success(tc, test_data, attributes)

end
  
  
 
def teardown

logout_stores
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