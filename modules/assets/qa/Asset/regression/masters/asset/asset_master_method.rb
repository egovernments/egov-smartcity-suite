    require path_to_file('asset_constants.rb')

    module Asset_master_method

          include Asset_constants
          include Test::Unit::Assertions


        # create an asset with any of the asset statuses(Created/CWIP/Written- off/Disposed)
          def assets_create_asset_master_success(tc,test_data)
                
                $browser.dt(:text,/#{ASSET_MASTER_LINK}/).click
		
                assert_true($browser.contains_text(ASSET_MASTER_LINK_FOUND_SUCCESS_MSG), ("TC: " + tc + ". Couldn't find text: " + ASSET_MASTER_LINK_FOUND_SUCCESS_MSG))
                
                $browser.link(:text, ASSET_CREATE_ASSET_LINK).click
                      
                browser_create_asset=Watir::IE.attach(:title,/Create Asset/)
		sleep(2)
		
		browser_create_asset.maximize
                
                assert_true(browser_create_asset.contains_text(ASSET_CREATE_ASSET_SUCCESS_MSG),("TC: " + tc + ". Couldn't find text: " + ASSET_CREATE_ASSET_SUCCESS_MSG + " Creating asset failed"))
                
                asset_populate_create_asset_fields(browser_create_asset, tc, test_data)
                
                browser_create_asset.button(:id,ASSET_SAVE_BUTTON).click
                sleep(2)
                assert_true(browser_create_asset.contains_text(ASSET_CREATION_SUCCESS_MSG),("TC: " + tc + " .couldn't find text: " + ASSET_CREATION_SUCCESS_MSG + "creating asset failed"))
                
                ensure
                 
                  browser_create_asset.close
                
          end
              
          
          



        
          def assets_create_asset_with_status_capitalized(tc,test_data)
            
                #~ $browser.cell(:xpath, "//[contains(text(),'#{ASSET_MASTER_LINK}')]").click
                $browser.dt(:text,/#{ASSET_MASTER_LINK}/).click
                assert_true($browser.contains_text(ASSET_MASTER_LINK_FOUND_SUCCESS_MSG), ("TC: " + tc + ". Couldn't find text: " + ASSET_MASTER_LINK_FOUND_SUCCESS_MSG))
                
                $browser.link(:text,   ASSET_CREATE_ASSET_LINK).click
		
                browser_create_asset=Watir::IE.attach(:title,/Create Asset/)
		browser_create_asset.maximize

                assert_true(browser_create_asset.contains_text(ASSET_CREATE_ASSET_SUCCESS_MSG),("TC: " + tc + ". Couldn't find text: " + ASSET_CREATE_ASSET_SUCCESS_MSG + " Creating asset failed"))
                
                asset_populate_create_asset_fields(browser_create_asset, tc, test_data)
                
                assert_false(browser_create_asset.text_field(:id,ASSET_GROSS_VALUE_FLD).readonly?)
                
                browser_create_asset.button(:id,ASSET_SAVE_BUTTON).click
                
                assert_true(browser_create_asset.contains_text(ASSET_CREATION_SUCCESS_MSG),("TC: " + tc + " .couldn't find text: " +ASSET_CREATION_SUCCESS_MSG+ "creating asset failed"))
                
                ensure
                 
                  #~ browser_create_asset.close
                
          end



          def assets_create_asset_with_duplicate_code(tc,test_data)
            
                #~ $browser.cell(:xpath, "//[contains(text(),'#{ASSET_MASTER_LINK}')]").click
                $browser.dt(:text,/#{ASSET_MASTER_LINK}/).click
                assert_true($browser.contains_text(ASSET_MASTER_LINK_FOUND_SUCCESS_MSG), ("TC: " + tc + ". Couldn't find text: " + ASSET_MASTER_LINK_FOUND_SUCCESS_MSG))
                
                $browser.link(:text, ASSET_CREATE_ASSET_LINK).click
                      
                browser_create_asset=Watir::IE.attach(:title,/Create Asset/)
		
		browser_create_asset.maximize

                assert_true(browser_create_asset.contains_text(ASSET_CREATE_ASSET_SUCCESS_MSG),("TC: " + tc + ". Couldn't find text: " + ASSET_CREATE_ASSET_SUCCESS_MSG + " Creating asset failed"))
                
                asset_populate_create_asset_fields(browser_create_asset, tc, test_data)
                
                assert_false(browser_create_asset.text_field(:id,ASSET_GROSS_VALUE_FLD).readonly?)
                
                browser_create_asset.button(:id,ASSET_SAVE_BUTTON).click
                sleep(2)
                assert_true(browser_create_asset.contains_text(ASSET_CHECK_DUPLICATE_ASSET_SUCCESS_MSG),("TC: " + tc + " .couldn't find text: " + ASSET_CHECK_DUPLICATE_ASSET_SUCCESS_MSG + "creating asset failed"))
                
                ensure
                 
                  browser_create_asset.close
                
          end
              
              
          #populate create asset fields
         def asset_populate_create_asset_fields(browser_create_asset, tc, test_data)
           
              if test_data[ASSET_CODE_FLD]!=nil
              browser_create_asset.text_field(:id,ASSET_CODE_FLD).set test_data[ASSET_CODE_FLD]
              end
            
              if test_data[ASSET_NAME_FLD]!=nil
              browser_create_asset.text_field(:id,ASSET_NAME_FLD).set test_data[ASSET_NAME_FLD]
              end
            
              if test_data[ASSET_CATEGORY_TYPE_FLD] !=nil
              browser_create_asset.select_list(:id,ASSET_CATEGORY_TYPE_FLD).select test_data[ASSET_CATEGORY_TYPE_FLD] 
              end
              
              if test_data[ASSET_CATEGORY_FLD]!=nil
              browser_create_asset.select_list(:name,ASSET_CATEGORY_FLD).select test_data[ASSET_CATEGORY_FLD]
              end
              
              if test_data[ASSET_DESCRIPTION_FLD]!=nil
              browser_create_asset.text_field(:name,ASSET_DESCRIPTION_FLD).set test_data[ASSET_DESCRIPTION_FLD]
              end
              
              if test_data[ASSET_AREA_FLD]!=nil
              browser_create_asset.select_list(:id,ASSET_AREA_FLD).select test_data[ASSET_AREA_FLD]
              end
              
              if test_data[ASSET_LOCATION_FLD]!=nil
              browser_create_asset.select_list(:id,ASSET_LOCATION_FLD).select test_data[ASSET_LOCATION_FLD]
              end
              
              if test_data[ASSET_STREET_FLD]!=nil
              browser_create_asset.select_list(:id,ASSET_STREET_FLD).select test_data[ASSET_STREET_FLD]
              end
              
              if test_data[ASSET_DETAILS_FLD]!=nil
              browser_create_asset.text_field(:name,ASSET_DETAILS_FLD).set test_data[ASSET_DETAILS_FLD]
              end
              
              if test_data[ASSET_MODE_OF_ACQUISITION_FLD]!=nil
              browser_create_asset.select_list(:name,ASSET_MODE_OF_ACQUISITION_FLD).select test_data[ASSET_MODE_OF_ACQUISITION_FLD]
              end
              
              if test_data[ASSET_DATE_OF_COMMISSIONING_FLD]!=nil
              
              browser_create_asset.text_field(:id,ASSET_DATE_OF_COMMISSIONING_FLD).set test_data[ASSET_DATE_OF_COMMISSIONING_FLD]
              end
              
              if test_data[ASSET_STATUS_FLD]!=nil
              browser_create_asset.select_list(:id,ASSET_STATUS_FLD).select test_data[ASSET_STATUS_FLD]
              end
              
              if test_data[ASSET_GROSS_VALUE_FLD]!=nil
              browser_create_asset.text_field(:id,ASSET_GROSS_VALUE_FLD).set test_data[ASSET_GROSS_VALUE_FLD]
              end
              
              if test_data[ASSET_ACCUMULATIVE_DEPRECIATION_FLD]!=nil
              browser_create_asset.text_field(:name,ASSET_ACCUMULATIVE_DEPRECIATION_FLD).set test_data[ASSET_ACCUMULATIVE_DEPRECIATION_FLD]
              end
              
              if test_data[ASSET_WRITTEN_DOWN_VALUE_FLD]!=nil
              browser_create_asset.text_field(:name,ASSET_WRITTEN_DOWN_VALUE_FLD).set test_data[ASSET_WRITTEN_DOWN_VALUE_FLD]
              end
                            
         end
        
    end