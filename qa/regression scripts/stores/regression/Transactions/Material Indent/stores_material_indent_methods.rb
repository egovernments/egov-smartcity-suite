
require path_to_file('stores_constants.rb')

module Stores_material_indent_methods

include Stores_constants
include Test::Unit::Assertions

#for purpose capital

def create_material_indent_success(tc, test_data, attributes, action)
  

      if action == 'Indent'
          #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
            $browser.dt(:text,'Transactions').click
          $browser.link(:text, CREATE_MATERIAL_INDENT_LINK).click
          ie_create_material_indent=Watir::IE.attach(:url,/loadcreateMR*/)
      end

      if action == 'MTN'
          #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
            $browser.dt(:text,'Transactions').click
          $browser.link(:text, CREATE_MTN_INDENT_LINK).click
          ie_create_material_indent=Watir::IE.attach(:url,/createMtn*/)
      end
 
      
      assert_not_nil(ie_create_material_indent.contains_text(CREATE_INDENT_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_INDENT_SCREEN+ ". Create indent sccreen."))
      populate_create_material_indent_details(tc, test_data, attributes, ie_create_material_indent)
      
      ie_create_material_indent.button(:type,CREATE_MI_BTN).click

      assert_true(ie_create_material_indent.button(:value, CREATE_INDENT_PRINT_BUTTON).exists?, "Indent not created")

        test_data[INDENT_NUMBER_FLD] = ie_create_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD).value

       puts "Indent number generated is "  +  test_data[INDENT_NUMBER_FLD]

    ensure
        ie_create_material_indent.close

end

def create_material_indent_failure(tc, test_data, attributes, action)
  
      if action == 'Indent'
          #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
            $browser.dt(:text,'Transactions').click
          $browser.link(:text, CREATE_MATERIAL_INDENT_LINK).click
          ie_create_material_indent=Watir::IE.attach(:url,/loadcreateMR*/)
      end

      if action == 'MTN'
          #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
            $browser.dt(:text,'Transactions').click
          $browser.link(:text, CREATE_MATERIAL_TRANSFER_NOTE_LINK).click
          ie_create_material_indent=Watir::IE.attach(:url,/createMtn*/)
      end

        
      assert_not_nil(ie_create_material_indent.contains_text(CREATE_INDENT_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_INDENT_SCREEN+ ". Create indent sccreen."))
      populate_create_material_indent_details(tc, test_data, attributes, ie_create_material_indent)
      ie_create_material_indent.button(:type,CREATE_MI_BTN).click

      assert_false(ie_create_material_indent.button(:value, CREATE_INDENT_PRINT_BUTTON).exists?, "Indent created")

    ensure
      ie_create_material_indent.close

end


def populate_create_material_indent_details(tc, test_data, attributes, ie_create_material_indent)
  
    if test_data[CREATE_MI_TYPE_FLD] && attributes[CREATE_MI_TYPE_FLD]!=nil
      ie_create_material_indent.select_list(:id, attributes[CREATE_MI_TYPE_FLD]).set test_data[CREATE_MI_TYPE_FLD]
    end

    #~ if test_data[CREATE_MI_DEPARTMENT_FLD] && attributes[CREATE_MI_DEPARTMENT_FLD]!=nil
      #~ ie_create_material_indent.select_list(:id,attributes[CREATE_MI_DEPARTMENT_FLD]).set test_data[CREATE_MI_DEPARTMENT_FLD]    
    #~ end
    
    if test_data[CREATE_MI_INDENTING_STORE_FLD] && attributes[CREATE_MI_INDENTING_STORE_FLD]!=nil
      assert_true(ie_create_material_indent.select_list(:id,attributes[CREATE_MI_INDENTING_STORE_FLD]).includes?(test_data[CREATE_MI_INDENTING_STORE_FLD]) )  
      ie_create_material_indent.select_list(:id,attributes[CREATE_MI_INDENTING_STORE_FLD]).set test_data[CREATE_MI_INDENTING_STORE_FLD]
    end
        
    if test_data[CREATE_MI_INDENT_PURPOSE_FLD] && attributes[CREATE_MI_INDENT_PURPOSE_FLD]!=nil
      ie_create_material_indent.select_list(:id,attributes[CREATE_MI_INDENT_PURPOSE_FLD]).set test_data[CREATE_MI_INDENT_PURPOSE_FLD]
      #~ ie_create_material_indent.text_field(:id, attributes[CREATE_MI_INDENT_PURPOSE_FLD]).fire_event('onchange')
    end
   
   
    if test_data[CREATE_MI_ISSUING_STORE_FLD] && attributes[CREATE_MI_ISSUING_STORE_FLD]!=nil
      assert_true(ie_create_material_indent.select_list(:id, attributes[CREATE_MI_ISSUING_STORE_FLD]).includes?(test_data[CREATE_MI_ISSUING_STORE_FLD]) ) 
      ie_create_material_indent.select_list(:id, attributes[CREATE_MI_ISSUING_STORE_FLD]).set test_data[CREATE_MI_ISSUING_STORE_FLD]
    end
  
  
    if test_data[CREATE_MI_ACCOUNT_CODE_FLD] && attributes[CREATE_MI_ACCOUNT_CODE_FLD]!=nil
      ie_create_material_indent.select_list(:id, attributes[CREATE_MI_ACCOUNT_CODE_FLD]).set test_data[CREATE_MI_ACCOUNT_CODE_FLD]
      ie_create_material_indent.select_list(:id, attributes[CREATE_MI_ACCOUNT_CODE_FLD]).fire_event("onblur")
    end

    if test_data[CREATE_MI_PROJECT_CODE_FLD] && attributes[CREATE_MI_PROJECT_CODE_FLD]!=nil
      ie_create_material_indent.text_field(:id, attributes[CREATE_MI_PROJECT_CODE_FLD]).value = test_data[CREATE_MI_PROJECT_CODE_FLD]
      ie_create_material_indent.text_field(:id, attributes[CREATE_MI_PROJECT_CODE_FLD]).fire_event('onKeyPress')
      ie_create_material_indent.text_field(:id, attributes[CREATE_MI_PROJECT_CODE_FLD]).fire_event('onKeyUp')
      ie_create_material_indent.text_field(:id, attributes[CREATE_MI_PROJECT_CODE_FLD]).fire_event('onblur')

    end
  
    if test_data[CREATE_MI_ASSET_CODE_FLD] && attributes[CREATE_MI_ASSET_CODE_FLD]!=nil 
      ie_create_material_indent.text_field(:id, attributes[CREATE_MI_ASSET_CODE_FLD]).set test_data[CREATE_MI_ASSET_CODE_FLD]
      ie_create_material_indent.text_field(:id, attributes[CREATE_MI_ASSET_CODE_FLD]).fire_event('onKeyPress')
      ie_create_material_indent.text_field(:id, attributes[CREATE_MI_ASSET_CODE_FLD]).fire_event('onKeyUp')
      #~ ie_create_material_indent.text_field(:id, attributes[CREATE_MI_ASSET_CODE_FLD]).fire_event('onblur')  

      #~ Watir::Waiter.wait_until{ ie_create_material_indent.li(:text,/#{test_data[CREATE_MI_ASSET_CODE_FLD]}/).exists?}
      #~ ie_create_material_indent.li(:text, /#{test_data[CREATE_MI_ASSET_CODE_FLD]}/).click
      
    end
  

###########################################################################################################################

loop = test_data[FOR_LOOP_MATERIAL_INDENT].to_i

for i in 1...loop+1 #For loop to add multiple rows of item 

    if test_data[CREATE_MI_ITEM_FLD+i.to_s] && attributes[CREATE_MI_ITEM_FLD+i.to_s]!=nil
      ie_create_material_indent.table(:id, CREATE_MI_TABLE)[i+1][2].text_field(:id, attributes[CREATE_MI_ITEM_FLD+i.to_s]).set test_data[CREATE_MI_ITEM_FLD+i.to_s] 
    end

  
    if test_data[CREATE_MI_QUANTITY_REQUIRED_FLD+i.to_s] && attributes[CREATE_MI_QUANTITY_REQUIRED_FLD+i.to_s]!=nil
      ie_create_material_indent.table(:id, CREATE_MI_TABLE)[i+1][5].text_field(:id, attributes[CREATE_MI_QUANTITY_REQUIRED_FLD+i.to_s]).set test_data[CREATE_MI_QUANTITY_REQUIRED_FLD+i.to_s] 
    end

    if i<test_data[FOR_LOOP_MATERIAL_INDENT].to_i
        ie_create_material_indent.image(:id,CREATE_INDENT_ADD_IMG).flash
        ie_create_material_indent.image(:id,CREATE_INDENT_ADD_IMG).click
    end

end #End of for loop


#~ ##########################################################################################################################3

    if test_data[CREATE_MI_FUND_FLD] && attributes[CREATE_MI_QUANTITY_REQUIRED_FLD]!=nil
      ie_create_material_indent.select_list(:id,attributes[CREATE_MI_QUANTITY_REQUIRED_FLD]).set test_data[CREATE_MI_QUANTITY_REQUIRED_FLD]
    end

    if test_data[CREATE_MI_FINANCING_SOURCE_FLD] && attributes[CREATE_MI_FINANCING_SOURCE_FLD]!=nil
      ie_create_material_indent.select_list(:id,attributes[CREATE_MI_FINANCING_SOURCE_FLD]).set test_data[CREATE_MI_FINANCING_SOURCE_FLD]
    end


    if test_data[CREATE_MI_FUNCTION_FLD] && attributes[CREATE_MI_FUNCTION_FLD]!=nil
      ie_create_material_indent.select_list(:id,attributes[CREATE_MI_FUNCTION_FLD]).set test_data[CREATE_MI_FUNCTION_FLD]
    end

    if test_data[CREATE_MI_FUNCTIONARY_FLD] && attributes[CREATE_MI_FUNCTIONARY_FLD]!=nil
      ie_create_material_indent.select_list(:id,attributes[CREATE_MI_FUNCTIONARY_FLD]).set test_data[CREATE_MI_FUNCTIONARY_FLD]
    end
    
     if( ie_create_material_indent.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
               if test_data[STORES_CREATE_MI_DEGIGNATION_FLD] && attributes[STORES_CREATE_MI_DEGIGNATION_FLD]!=nil
              ie_create_material_indent.select_list(:id,attributes[STORES_CREATE_MI_DEGIGNATION_FLD]).set test_data[STORES_CREATE_MI_DEGIGNATION_FLD]
            end
            
            
               if test_data[STORES_CREATE_MI_APPROVER_FLD] && attributes[STORES_CREATE_MI_APPROVER_FLD]!=nil
              ie_create_material_indent.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).set test_data[STORES_CREATE_MI_APPROVER_FLD]
            end

    end
    

end


  
def view_material_indent(tc, test_data, attributes)

    #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
     $browser.dt(:text,'Masters').click
    $browser.link(:text, VIEW_MATERIAL_INDENT_LINK).click
    ie_view_material_indent=Watir::IE.attach(:url,/searchviewMR*/)
    
    assert_not_nil(ie_view_material_indent.contains_text(VIEW_INDENT_SCREEN), ("TC: " + tc + ". Couldn't find text: " + VIEW_INDENT_SCREEN+ ". View indent sccreen not found."))

    if test_data[VIEW_MI_TYPE_FLD] && attributes[VIEW_MI_TYPE_FLD]!=nil
      ie_view_material_indent.select_list(:id, attributes[VIEW_MI_TYPE_FLD]).set test_data[VIEW_MI_TYPE_FLD]
    end

    ie_view_material_indent.button(:value,INDENT_SEARCH_BTN).click

    ie_view_material_indent.link(:text, test_data[INDENT_NUMBER_FLD]).click

    assert_not_nil(ie_view_material_indent.contains_text(VIEW_INDENT_SUCCESS_MSG),("TC: " + tc + ". Text not found : " + VIEW_INDENT_SUCCESS_MSG  + " - View indent screen not found"))

    assert(ie_view_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD).verify_contains(test_data[INDENT_NUMBER_FLD]) )

ensure 
    ie_view_material_indent.close
    
end#METHOD END
  



               #BOTH APPROVE MAT INDENT AND REJECT INDENT ARE REPLACED IN THE SUBSEQUENT METHOD




def approve_material_indent(tc, test_data, attributes, action)

      ie_approve_indent = start_browser(APPROVE_MATERIAL_INDENT_URL)
      assert_not_nil(ie_approve_indent.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox"))

    if action == "Drafts"
      #~ ie_approve_indent.cell(:xpath, "//[contains(text(),'Drafts')]").click
      ie_approve_indent.dt(:text,'Drafts').click
      assert(ie_approve_indent.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_approve_indent.cell(:xpath, "//[contains(text(),'Inbox')]").click
        ie_approve_indent.dt(:text,'Inbox').click
      assert(ie_approve_indent.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      ie_approve_indent.frame(INBOX_FRAME).cell(:text, /#{test_data[INDENT_NUMBER_FLD]}/).click
      
      ie_approve_material_indent = Watir::IE.attach(:title, /Material Indent - Modify/)
      
      assert(ie_approve_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD ).verify_contains(test_data[INDENT_NUMBER_FLD]) )

    populate_create_material_indent_details(tc, test_data, attributes, ie_create_material_indent)
    
      ie_approve_material_indent.button(:value, MATERIAL_INDENT_APPROVE_BTN).click
      
      assert(ie_approve_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD ).verify_contains(test_data[INDENT_NUMBER_FLD]) )
      
      ie_approve_material_indent.button(:value, MATERIAL_INDENT_CLOSE_BTN).click
      

  ensure
      ie_approve_indent.close

end


#~ def reject_material_indent(tc, test_data, attributes,action) 

      #~ ie_reject_indent = start_browser(REJECT_MATERIAL_INDENT_URL)

      #~ assert_not_nil(ie_reject_indent.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox.")) 

  #~ if action == "Drafts"
      #~ ie_reject_indent.cell(:xpath, "//[contains(text(),'Drafts')]").click
      #~ assert(ie_reject_indent.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    #~ end
    
    #~ if action == "Inbox"
      #~ ie_reject_indent.cell(:xpath, "//[contains(text(),'Inbox')]").click
      #~ assert(ie_reject_indent.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    #~ end

      #~ ie_reject_indent.frame(INBOX_FRAME).cell(:text, /#{test_data[INDENT_NUMBER_FLD]}/).click

      #~ ie_reject_material_indent = Watir::IE.attach(:title, /Material Indent - Modify/)
      
      #~ assert(ie_reject_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD ).verify_contains(test_data[INDENT_NUMBER_FLD]) )
      
      #~ ie_reject_material_indent.button(:value, MATERIAL_INDENT_REJECT_BTN).click
      
      #~ assert(ie_reject_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD ).verify_contains(test_data[INDENT_NUMBER_FLD]) )
      
      #~ ie_reject_material_indent.button(:value, MATERIAL_INDENT_CLOSE_BTN).click

  #~ ensure
      #~ ie_reject_indent.close

#~ end
 
 
 
 def approve_reject_material_indent(tc, test_data, attributes, action, actions)
   
   
      ie_approve_reject_indent = start_browser(APPROVE_REJECT_MATERIAL_INDENT_URL)
      
      assert_not_nil(ie_approve_reject_indent.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox"))

    if action == "Drafts"
      #~ ie_approve_reject_indent.cell(:xpath, "//[contains(text(),'Drafts')]").click
        ie_approve_reject_indent.dt(:text,'Drafts').click
      assert(ie_approve_reject_indent.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_approve_reject_indent.cell(:xpath, "//[contains(text(),'Inbox')]").click
       ie_approve_reject_indent.dt(:text,'Inbox').click
      assert(ie_approve_reject_indent.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      ie_approve_reject_indent.frame(INBOX_FRAME).cell(:text, /#{test_data[INDENT_NUMBER_FLD]}/).click
      
      ie_approve_reject_material_indent = Watir::IE.attach(:title, /Indent/)
      
      assert(ie_approve_reject_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD ).verify_contains(test_data[INDENT_NUMBER_FLD]) )
     
      if( ie_approve_reject_material_indent.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_approve_reject_material_indent.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
       if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
      ie_approve_reject_material_indent.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end
   
   if actions == "Approve"
      ie_approve_reject_material_indent.button(:value, MATERIAL_INDENT_APPROVE_BTN).click
      assert_true(ie_approve_reject_material_indent.button(:value, APPROVE_REJECT_INDENT_PRINT_BUTTON).exists?, "TC:" "Indent not Approved")
      puts "Material Indent Approved"
    end
    
    if actions == "Reject"
      ie_approve_reject_material_indent.button(:value, MATERIAL_INDENT_REJECT_BTN).click
      assert_true(ie_approve_reject_material_indent.button(:value, APPROVE_REJECT_INDENT_PRINT_BUTTON).exists?, "TC:" "Indent not Rejected")
      puts "Material Indent Rejected"
    end
    
      ie_approve_reject_material_indent.button(:value, MATERIAL_INDENT_CLOSE_BTN).click
      
   ensure
    ie_approve_reject_material_indent.close
   
   
 end






#PRAMOD

 
 def approve_reject_material_modify_indent(tc, test_data, attributes, action, actions)
   
   
      ie_modify_approve_reject_indent = start_browser(APPROVE_REJECT_MATERIAL_INDENT_URL)
      
      assert_not_nil(ie_modify_approve_reject_indent.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox"))
    
    if action == "Inbox"
      #~ ie_modify_approve_reject_indent.cell(:xpath, "//[contains(text(),'Inbox')]").click
       ie_modify_approve_reject_indent.dt(:text,'Inbox').click
      assert(ie_modify_approve_reject_indent.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      ie_modify_approve_reject_indent.frame(INBOX_FRAME).cell(:text, /#{test_data[INDENT_NUMBER_FLD]}/).click
      
      ie_modify_approve_reject_material_indent = Watir::IE.attach(:title, /Indent/)
      
      assert(ie_modify_approve_reject_material_indent.text_field(:id,CREATE_INDENT_NUMBER_FLD ).verify_contains(test_data[INDENT_NUMBER_FLD]) )
     
      #~ populate_create_material_indent_details(tc, test_data, attributes, ie_create_material_indent)
     
      if( ie_modify_approve_reject_material_indent.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_modify_approve_reject_material_indent.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
       if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
      ie_modify_approve_reject_material_indent.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end
   
   if actions == "Approve"
      ie_modify_approve_reject_material_indent.button(:value, MATERIAL_INDENT_APPROVE_BTN).click
      assert_true(ie_modify_approve_reject_material_indent.button(:value, APPROVE_REJECT_INDENT_PRINT_BUTTON).exists?, "TC:" "Indent not Approved")
      puts "Material Indent Approved"
    end
    
    if actions == "Reject"
      ie_modify_approve_reject_material_indent.button(:value, MATERIAL_INDENT_REJECT_BTN).click
      assert_true(ie_modify_approve_reject_material_indent.button(:value, APPROVE_REJECT_INDENT_PRINT_BUTTON).exists?, "TC:" "Indent not Rejected")
      puts "Material Indent Rejected"
    end
    
      ie_modify_approve_reject_material_indent.button(:value, MATERIAL_INDENT_CLOSE_BTN).click
      
   ensure
    ie_modify_approve_reject_material_indent.close
   
   
end



end # END OF MATERIAL INDENT MODULE


