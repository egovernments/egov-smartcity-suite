
require path_to_file('stores_constants.rb')

module Stores_mii_methods

include Stores_constants
include Test::Unit::Assertions


def create_mii_success(tc, test_data, attributes, action)
  
    if action == "Indent Issue"
        #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
        $browser.dt(:text,'Transactions').click

        $browser.link(:text, CREATE_MII_LINK).click
        ie_create_mii=Watir::IE.attach(:url,/beforeSearchPage*/)
    end
    

    if action == "MTN Issue"
        #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
        $browser.dt(:text,'Transactions').click
        $browser.link(:text, CREATE_MTN_OUTWARD_LINK).click
        ie_create_mii=Watir::IE.attach(:url,/createMtn*/)
    end
    

      assert(ie_create_mii.contains_text(CREATE_MII_SEARCH),("TC: " + tc + ". Text not found : " + CREATE_MII_SEARCH  + " - MII Search screen"))
      ie_create_mii.button(:value, SEARCH_CREATE_MII_BTN).click  # searchs the approved indents to make an issue
      ie_create_mii.link(:text, test_data[INDENT_NUMBER_FLD]).click
      
      populate_create_mii_details(tc, test_data, attributes, ie_create_mii)  
      ie_create_mii.button(:id, CREATE_MII_BTN).click  
      
      assert_true(ie_create_mii.text_field(:id, CREATE_MII_RECEIPT_NUMBER_FLD).exists?)
      assert_true(ie_create_mii.button(:value, CREATE_MII_PRINT_BUTTON).exists?, "Indent for the material is not issued")
      #~ assert_not_nil(ie_create_mii.contains_text(CREATE_MII_LABEL), "Indent for the material is not issued")

    if ie_create_mii.text_field(:id, CREATE_MII_RECEIPT_NUMBER_FLD).exists?
      test_data[CREATE_MII_NUMBER_FLD] = ie_create_mii.text_field(:id,CREATE_MII_RECEIPT_NUMBER_FLD).value
      puts "Indent Issue Number is : " + test_data[CREATE_MII_NUMBER_FLD]
    end

ensure 
      ie_create_mii.close
  
end

def create_mii_failure(tc, test_data, attributes, action)
  
    if action == "Indent Issue"
        #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
        $browser.dt(:text,'Transactions').click

        $browser.link(:text, CREATE_MII_LINK).click
        ie_create_mii=Watir::IE.attach(:url,/beforeSearchPage*/)
    end
    

    if action == "MTN Issue"
        #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
        $browser.dt(:text,'Transactions').click
        $browser.link(:text, CREATE_MTN_OUTWARD_LINK).click
        ie_create_mii=Watir::IE.attach(:url,/createMtn*/)
    end
    

      assert(ie_create_mii.contains_text(CREATE_MII_SEARCH),("TC: " + tc + ". Text not found : " + CREATE_MII_SEARCH  + " - MII Search screen"))
      ie_create_mii.button(:value, SEARCH_CREATE_MII_BTN).click   # searchs the approved indents to make an issue
      ie_create_mii.link(:text, test_data[INDENT_NUMBER_FLD]).click
      #~ ie_create_mii.link(:text, "MTNIN/11/25/2009-10").click

      populate_create_mii_details(tc, test_data, attributes, ie_create_mii)  
      ie_create_mii.button(:id, CREATE_MII_BTN).click  

      assert_false(ie_create_mii.button(:value, CREATE_MII_PRINT_BUTTON).exists?, "Indent for the material is issued")
      
ensure 
  ie_create_mii.close  
  
end


def populate_create_mii_details(tc, test_data, attributes,ie_create_mii)
  
      assert(ie_create_mii.contains_text(CREATE_MII_SUCCESS_MESG),("TC: " + tc + ". Text not found : " + CREATE_MII_SUCCESS_MESG  + " - Create MII screen "))
  
  if test_data[CREATE_MII_DATE_FLD] && attributes[CREATE_MII_DATE_FLD]!= nil
      ie_create_mii.text_field(:id, attributes[CREATE_MII_DATE_FLD]).set test_data[CREATE_MII_DATE_FLD]
    end  
    
    
loop = test_data[FOR_LOOP_MATERIAL_INDENT].to_i

for i in 1...loop+1 #For loop to issue multiple rows of item 

    if test_data[CREATE_MII_ISSUED_QUANTITY_FLD+i.to_s] && attributes[CREATE_MII_ISSUED_QUANTITY_FLD+i.to_s]!=nil
      ie_create_mii.table(:id, CREATE_MII_TABLE)[i+1][7].text_field(:id, attributes[CREATE_MII_ISSUED_QUANTITY_FLD+i.to_s]).set test_data[CREATE_MII_ISSUED_QUANTITY_FLD+i.to_s] 
    end

end #End of for loop
    
    
    

  if test_data[CREATE_MII_ISSUED_QUANTITY_FLD] && attributes[CREATE_MII_ISSUED_QUANTITY_FLD]!= nil
      ie_create_mii.text_field(:id, attributes[CREATE_MII_ISSUED_QUANTITY_FLD]).set test_data[CREATE_MII_ISSUED_QUANTITY_FLD]
    end  
    
    
  if test_data[STORES_CREATE_MI_DEGIGNATION_FLD] && attributes[STORES_CREATE_MI_DEGIGNATION_FLD]!=nil
  ie_create_mii.select_list(:id,attributes[STORES_CREATE_MI_DEGIGNATION_FLD]).set test_data[STORES_CREATE_MI_DEGIGNATION_FLD]
  end
    
    
  if test_data[STORES_CREATE_MI_APPROVER_FLD] && attributes[STORES_CREATE_MI_APPROVER_FLD]!=nil
     Watir::Waiter.wait_until{ie_create_mii.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).includes?(test_data[STORES_APPROVE_MI_APPROVER_FLD])}     
  ie_create_mii.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).set test_data[STORES_CREATE_MI_APPROVER_FLD]
  end
    
    ie_create_mii.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).flash
    
    
    

end

  
def view_mii_success(tc, test_data, attributes)

      #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
      $browser.dt(:text,'Transactions').click
      $browser.link(:text, VIEW_MII_LINK).click
      ie_view_mii=Watir::IE.attach(:url,/searchviewMrin*/)

      assert_not_nil(ie_view_mii.contains_text(VIEW_MII_SEARCH),("TC: " + ". Text not found : " + VIEW_MII_SEARCH + " - MII view search screen"))
      ie_view_mii.button(:value, VIEW_MII_BTN).click
      ie_view_mii.link(:text, test_data[CREATE_MII_RECEIPT_NUMBER_FLD]).click
      assert_not_nil(ie_view_mii.contains_text(VIEW_MII_SUCCESS_MESG),("TC: " + ". Text not found : " + VIEW_MII_SUCCESS_MESG + " - MII view screen"))

ensure 
  ie_view_mii.close

end


def approve_mii(tc, test_data, attributes, action)

  ie_approve_mii = start_browser(APPROVE_MII_URL)

  assert_not_nil(ie_approve_mii.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "Drafts"
      #~ ie_approve_mii.cell(:xpath , "//[contains(text(),'Drafts')]").click
       ie_approve_mii.dt(:text,'Drafts').click
      assert(ie_approve_mii.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_approve_mii.cell(:xpath , "//[contains(text(),'Inbox')]").click
         ie_approve_mii.dt(:text,'Inbox').click
      assert(ie_approve_mii.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      #~ ie_approve_mii.frame(INBOX_FRAME).cell(:text, /#{MTNO/11/27/2009-10}/).click
      
      ie_approve_mii.frame(INBOX_FRAME).cell(:text, /#{test_data[CREATE_MII_NUMBER_FLD]}/).click
      
      #~ ie_approve_mii.frame(INBOX_FRAME).cell(:text, /#{ IMI/86/2009-10}/).click
     
     ie_approve_mii_screen = Watir::IE.attach(:title, /Indent/)
      
      assert(ie_approve_mii_screen.text_field(:id,CREATE_MII_RECEIPT_NUMBER_FLD).verify_contains(test_data[CREATE_MII_NUMBER_FLD]) )
    
    
    if( ie_approve_mii_screen.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_approve_mii_screen.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
       if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
          Watir::Waiter.wait_until{ie_approve_mii_screen.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).includes?(test_data[STORES_APPROVE_MI_APPROVER_FLD])}     
      ie_approve_mii_screen.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end
  
      ie_approve_mii_screen.button(:value, MII_APPROVE_BUTTON).click
      
      assert_true(ie_approve_mii_screen.button(:value, CREATE_MII_PRINT_BUTTON).exists?, "Issue Created is not Approved")
      
      ie_approve_mii_screen.button(:value, MII_CLOSE_BUTTON).click

ensure
    ie_approve_mii.close

end

def reject_mii(tc, test_data, attributes, action) 

ie_reject_mii = start_browser(REJECT_MII_URL)

    assert_not_nil(ie_reject_mii.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))

    if action == "Drafts"
      #~ ie_reject_mii.cell(:xpath , "//[contains(text(),'Drafts')]").click
         ie_reject_mii.dt(:text,'Drafts').click
      assert(ie_reject_mii.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end

    if action == "Inbox"
      #~ ie_reject_mii.cell(:xpath , "//[contains(text(),'Inbox')]").click
         ie_reject_mii.dt(:text,'Inbox').click
      assert(ie_reject_mii.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
    ie_reject_mii.frame(INBOX_FRAME).cell(:text, /#{test_data[CREATE_MII_NUMBER_FLD]}/).click

    ie_reject_mii_screen = Watir::IE.attach(:title, /Indent/)

    assert(ie_reject_mii_screen.text_field(:id,CREATE_MII_RECEIPT_NUMBER_FLD).verify_contains(test_data[CREATE_MII_NUMBER_FLD]) )

    ie_reject_mii_screen.button(:value, MII_REJECT_BUTTON).click

    assert_true(ie_reject_mii_screen.button(:value, CREATE_MII_PRINT_BUTTON).exists?, "Issue Created is not Rejected")

    ie_reject_mii_screen.button(:value, MII_CLOSE_BUTTON).click

ensure
  ie_reject_mii.close

end


def modify_mii(tc, test_data, attributes, action)

  ie_modify_mii = start_browser(APPROVE_MII_URL)

  assert_not_nil(ie_modify_mii.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    
    if action == "Inbox"
      #~ ie_modify_mii.cell(:xpath , "//[contains(text(),'Inbox')]").click
         ie_modify_mii.dt(:text,'Inbox').click
      assert(ie_modify_mii.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      #~ ie_approve_mii.frame(INBOX_FRAME).cell(:text, /#{MTNO/11/27/2009-10}/).click
      
      ie_modify_mii.frame(INBOX_FRAME).cell(:text, /#{test_data[CREATE_MII_NUMBER_FLD]}/).click
      
      ie_modify_mii_screen = Watir::IE.attach(:title, /Indent/)
      
      assert(ie_modify_mii_screen.text_field(:id,CREATE_MII_RECEIPT_NUMBER_FLD).verify_contains(test_data[CREATE_MII_NUMBER_FLD]) )
    
      populate_modify_mii_details(tc, test_data, attributes,ie_modify_mii_screen)
    
      ie_modify_mii_screen.button(:value, MII_APPROVE_BUTTON).click
      
      assert_true(ie_modify_mii_screen.button(:value, CREATE_MII_PRINT_BUTTON).exists?, "Issue Created is not Approved")
      
      ie_modify_mii_screen.button(:value, MII_CLOSE_BUTTON).click

ensure
    ie_modify_mii.close

end


def populate_modify_mii_details(tc, test_data, attributes,ie_modify_mii_screen)
  
      #~ assert(ie_modify_mii_screen.contains_text(CREATE_MII_SUCCESS_MESG),("TC: " + tc + ". Text not found : " + CREATE_MII_SUCCESS_MESG  + " - Create MII screen "))
  
  if test_data[STORES_MODIFY_MII_DATE_FLD] && attributes[STORES_MODIFY_MII_DATE_FLD]!= nil
      ie_modify_mii_screen.text_field(:id, attributes[STORES_MODIFY_MII_DATE_FLD]).set test_data[STORES_MODIFY_MII_DATE_FLD]
    end  
    
    
loop = test_data[STORES_MODIFY_FOR_LOOP_MATERIAL_INDENT].to_i

for i in 1...loop+1 #For loop to issue multiple rows of item 

    if test_data[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD+i.to_s] && attributes[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD+i.to_s]!=nil
      ie_modify_mii_screen.table(:id, CREATE_MII_TABLE)[i+1][7].text_field(:id, attributes[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD+i.to_s]).set test_data[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD+i.to_s] 
    end

end #End of for loop
    

  if test_data[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD] && attributes[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD]!= nil
     Watir::Waiter.wait_until{ie_modify_mii_screen.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).includes?(test_data[STORES_APPROVE_MI_APPROVER_FLD])}     
     ie_modify_mii_screen.text_field(:id, attributes[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD]).set test_data[STORES_MODIFY_MII_ISSUED_QUANTITY_FLD]
    end  
 
    ie_modify_mii_screen.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).flash

end


 
end#Module End