    require 'watir'
    #~ ENV['DT_IE_AGENT_ACTIVE']='true'
    #~ ENV['DT_IE_SESSION_NAME'] = 'Create asset test'
    require 'test/unit'
    require 'win32/process'

    require 'path_helper'
    require path_to_file("asset_methods.rb")
    require path_to_file("Utils.rb")
    require path_to_file("asset_master_method.rb")

    include Asset_master_method
    include Asset_methods

    class Asset_Test_Create_Asset<Test::Unit::TestCase
  

            def setup
            
            @pid=Process.create(
            :app_name=>"ruby \"#{path_to_file("clicker.rb")}\"",
            :creation_flags => Process::DETACHED_PROCESS
            ).process_id
            
            end
             
             
            def test_0010_create_asset
              
                tc='0010'
                test_data=get_input_data_oo(tc,ASSET_CREATE_ASSET_SHEET,Asset_constants::INPUT_OO)
                asset_user_login(ASSET_USER_NAME,ASSET_PASSWORD)
                assets_create_asset_master_success(tc,test_data)
            
            end
  
            
            def test_0020_create_capitalized_asset
                 
                tc='0020'
                test_data=get_input_data_oo(tc,ASSET_CREATE_ASSET_SHEET,Asset_constants::INPUT_OO)
                asset_user_login(ASSET_USER_NAME,ASSET_PASSWORD)
		@window.execScript('_dt_addMark("After Login")')
                assets_create_asset_with_status_capitalized(tc,test_data)
                
            end
              
              
              
            def test_0030_create_duplicate_asset
              
                tc='0030'
                test_data=get_input_data_oo(tc,ASSET_CREATE_ASSET_SHEET,Asset_constants::INPUT_OO)
                asset_user_login(ASSET_USER_NAME,ASSET_PASSWORD)
		 #~ @window.execScript('_dt_addMark("After Login")')
                assets_create_asset_with_duplicate_code(tc,test_data)
                
            end
              
              
            def teardown
                  logout_asset
                  close_all_windows
                  rescue Exception => e
                     puts e  
                  ensure
                 Process.kill(9,@pid)
            end
              
  end