
require path_to_file('stores_constants.rb')

module Stores_mmrn_methods

include Stores_constants
include Test::Unit::Assertions
 
 
 
def create_mmrn_success(tc, test_data, attributes)

  #~ ie_create_mmrn = start_browser(CREATE_MMRN_URL)
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, CREATE_MMRN_LINK).click
    ie_create_mmrn=Watir::IE.attach(:url,/MMReceiptData*/)

  assert_not_nil(ie_create_mmrn.contains_text(CREATE_MMRN_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_MMRN_SCREEN+ ".  Create MMRN sccreen."))
  populate_create_mmrn_details(tc, test_data, attributes, ie_create_mmrn)
  ie_create_mmrn.button(:value, CREATE_MMRN_BTN).click
  assert_true(ie_create_mmrn.contains_text(CREATE_MMRN_NUMBER_LABEL),("TC: " + tc + ". Text not found : " + CREATE_MMRN_NUMBER_LABEL  + " - MMRN not created"))
  
  if ie_create_mmrn.text_field(:id, RECEIPT_NUMBER_FLD).exists?
  test_data[MMRN_NUMBER_FLD] = ie_create_mmrn.text_field(:id,RECEIPT_NUMBER_FLD).value
  end

  ensure
  ie_create_mmrn.close
  
end

def create_mmrn_failure(tc, test_data, attributes)

  #~ ie_create_mmrn = start_browser(CREATE_MMRN_URL)
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, CREATE_MMRN_LINK).click
    ie_create_mmrn=Watir::IE.attach(:url,/MMReceiptData*/)
    
  assert_not_nil(ie_create_mmrn.contains_text(CREATE_MMRN_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_MMRN_SCREEN+ ". Create MMRN sccreen."))
  populate_create_mmrn_details(tc, test_data, attributes, ie_create_mmrn)
  ie_create_mmrn.button(:value, CREATE_MMRN_BTN).click
  
  assert_false(ie_create_mmrn.text_field(:id, RECEIPT_NUMBER_FLD).exists?)
  assert_false(ie_create_mmrn.contains_text(CREATE_MMRN_NUMBER_LABEL),("TC: " + tc + ". Text not found : " + CREATE_MMRN_NUMBER_LABEL  + " - MMRN not created"))

  ensure
  ie_create_mmrn.close

end

def populate_create_mmrn_details(tc, test_data, attributes, ie_create_mmrn)

  #~ if test_data[CREATE_MMRN_DEPARTMENT_FLD] && attributes[CREATE_MMRN_DEPARTMENT_FLD]!=nil
    #~ ie_create_mmrn.select_list(:name,attributes[CREATE_MMRN_DEPARTMENT_FLD]).set test_data[CREATE_MMRN_DEPARTMENT_FLD]
  #~ end
  
  if test_data[CREATE_MMRN_DATE_FLD] && attributes[CREATE_MMRN_DATE_FLD]!=nil
    ie_create_mmrn.text_field(:id,attributes[CREATE_MMRN_DATE_FLD]).set test_data[CREATE_MMRN_DATE_FLD]
  end
  
  test_data[CREATE_MMRN_DATE] = ie_create_mmrn.text_field(:id, CREATE_MMRNDATE_FLD).value

  #~ puts "MMRN is created on : "  + test_data[CREATE_MMRN_DATE]

  if test_data[CREATE_MMRN_STORE_FLD] && attributes[CREATE_MMRN_STORE_FLD]!=nil
  assert_true(ie_create_mmrn.select_list(:name,attributes[CREATE_MMRN_STORE_FLD]).includes?(test_data[CREATE_MMRN_STORE_FLD]) )  
  ie_create_mmrn.select_list(:name,attributes[CREATE_MMRN_STORE_FLD]).set test_data[CREATE_MMRN_STORE_FLD]
  end

  if test_data[CREATE_MMRN_RECEIPT_TYPE_FLD] && attributes[CREATE_MMRN_RECEIPT_TYPE_FLD]!=nil
  ie_create_mmrn.select_list(:name,attributes[CREATE_MMRN_RECEIPT_TYPE_FLD]).set test_data[CREATE_MMRN_RECEIPT_TYPE_FLD]
  end

if test_data[MMRN_SUPPLIER] == '1' #Boolean opeartion to select the supplier if purpose = supplier receipt.

  ie_create_mmrn.image(:id, 'suppImg').click
   
    ie_create_mmrn1 = Watir::IE.attach(:title, /SUPPLIER/)
    Watir::Waiter.wait_until{ie_create_mmrn1.contains_text("Supplier Search")}

   

   if test_data[CREATE_PO_SUPPLIER_NAME_FLD] && attributes[CREATE_PO_SUPPLIER_NAME_FLD] !=nil
      ie_create_mmrn1.text_field(:id,attributes[CREATE_PO_SUPPLIER_NAME_FLD]).set test_data[CREATE_PO_SUPPLIER_NAME_FLD]
    end
    
      ie_create_mmrn1.button(:id, STORES_SEARCH_SUPP_BUTTON).click
    
    
    ie_create_mmrn1.div(:class,'yui-dt-col-attributeName yui-dt-col-1 yui-dt-liner yui-dt-sortable yui-dt-resizeable').click
    
  #~ ie_create_mmrn1 = Watir::IE.attach(:title, /Search screen/)
  #~ Watir::Waiter.wait_until{ie_create_mmrn1.contains_text("Search screen")}
  #~ ie_create_mmrn1.div(:class,'yui-dt-col-attributeDesc yui-dt-col-2 yui-dt-liner yui-dt-sortable yui-dt-resizeable').click
  
end

  if test_data[CREATE_MMRN_ACCOUNT_CODE_FLD] && attributes[CREATE_MMRN_ACCOUNT_CODE_FLD]!=nil
  ie_create_mmrn.select_list(:name, attributes[CREATE_MMRN_ACCOUNT_CODE_FLD]).select test_data[CREATE_MMRN_ACCOUNT_CODE_FLD]
  end

  if test_data[CREATE_MMRN_ITEM_NO_FLD] && attributes[CREATE_MMRN_ITEM_NO_FLD] !=nil
  ie_create_mmrn.text_field(:id, attributes[CREATE_MMRN_ITEM_NO_FLD]).set test_data[CREATE_MMRN_ITEM_NO_FLD]
  end

  if test_data[CREATE_MMRN_QUANTITY_FLD] && attributes[CREATE_MMRN_QUANTITY_FLD]!=nil
  ie_create_mmrn.text_field(:name,attributes[CREATE_MMRN_QUANTITY_FLD]).set test_data[CREATE_MMRN_QUANTITY_FLD]
  end

  if test_data[CREATE_MMRN_RATE_PER_UNIT_FLD] && attributes[CREATE_MMRN_RATE_PER_UNIT_FLD]!=nil
  ie_create_mmrn.text_field(:name,attributes[CREATE_MMRN_RATE_PER_UNIT_FLD]).set test_data[CREATE_MMRN_RATE_PER_UNIT_FLD]
  end

  #~ ***************************************************************************************************************************************************

  if test_data[BREAK_UP_DETAILS] == '1'  # Boolean logic to enter the brekup details

  ie_create_mmrn.link(:text, BREAK_UP_LINK).flash 
  ie_create_mmrn.link(:text, BREAK_UP_LINK).click 


  ie_create_mmrn_other_details = Watir::IE.attach(:title, /Material Other Details/)   # Attach the Material Other Details screen to enter the breakup details
  Watir::Waiter.wait_until{ie_create_mmrn_other_details.contains_text("Material Other Details")}

  if test_data[CREATE_MMRN_BREAKUP_QUANTITY_FLD] && attributes[CREATE_MMRN_BREAKUP_QUANTITY_FLD]!=nil
  ie_create_mmrn_other_details.text_field(:id,attributes[CREATE_MMRN_BREAKUP_QUANTITY_FLD]).set test_data[CREATE_MMRN_BREAKUP_QUANTITY_FLD]
  end

  if test_data[CREATE_MMRN_LOT_NO_FLD] && attributes[CREATE_MMRN_LOT_NO_FLD]!=nil
  ie_create_mmrn_other_details.text_field(:id,attributes[CREATE_MMRN_LOT_NO_FLD]).set test_data[CREATE_MMRN_LOT_NO_FLD]
  end

  if test_data[CREATE_MMRN_EXPIRY_DATE_FLD] && attributes[CREATE_MMRN_EXPIRY_DATE_FLD]!=nil
  ie_create_mmrn_other_details.text_field(:id,attributes[CREATE_MMRN_EXPIRY_DATE_FLD]).set test_data[CREATE_MMRN_EXPIRY_DATE_FLD]
  end

  if test_data[CREATE_MMRN_BIN_NO_FLD] && attributes[CREATE_MMRN_BIN_NO_FLD]!=nil
  ie_create_mmrn_other_details.select_list(:id,attributes[CREATE_MMRN_BIN_NO_FLD]).set test_data[CREATE_MMRN_BIN_NO_FLD]
  end

  ie_create_mmrn_other_details.button(:id, ITEM_OTHER_DETAILS_BTN).click

  end

  #~ **************************************************************************************************************************************************************************

  if test_data[STORES_CREATE_MI_DEGIGNATION_FLD] && attributes[STORES_CREATE_MI_DEGIGNATION_FLD]!=nil
  ie_create_mmrn.select_list(:id,attributes[STORES_CREATE_MI_DEGIGNATION_FLD]).set test_data[STORES_CREATE_MI_DEGIGNATION_FLD]
  end
    
    
  if test_data[STORES_CREATE_MI_APPROVER_FLD] && attributes[STORES_CREATE_MI_APPROVER_FLD]!=nil
    Watir::Waiter.wait_until{ie_create_mmrn.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).includes?(test_data[STORES_APPROVE_MI_APPROVER_FLD])}     
  ie_create_mmrn.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).set test_data[STORES_CREATE_MI_APPROVER_FLD]
  end


end

def view_mmrn_success(tc, test_data, attributes)
  
      #~ ie_view_mmrn = start_browser(VIEW_MMRN_URL)
        #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
        $browser.dt(:text,'Transactions').click
        $browser.link(:text, VIEW_MMRN_LINK).click
        ie_view_mmrn=Watir::IE.attach(:url,/MMRN*/)
        
      assert_not_nil(ie_view_mmrn.contains_text(VIEW_MMRN_SEARCH_SCREEN),("TC: " + tc + ". Text not found : " + VIEW_MMRN_SEARCH_SCREEN  + " - View MMRN search screen not found"))
      ie_view_mmrn.button(:type, SEARCH_MMRN_BTN).click
      
      ie_view_mmrn.link(:text, test_data[MMRN_NUMBER_FLD]).click
      assert_not_nil(ie_view_mmrn.contains_text(VIEW_MMRN_SUCCESS_MSG),("TC: " + tc + ". Text not found : " + VIEW_MMRN_SUCCESS_MSG  + " - View MMRN screen not found"))
      assert(ie_view_mmrn.text_field(:id,RECEIPT_NUMBER_FLD).verify_contains(test_data[MMRN_NUMBER_FLD]) )
      
      ensure
      ie_view_mmrn.close

end  
  
def approve_mmrn(tc, test_data, attributes, action)

    ie_approve_mmrn = start_browser(APPROVE_MMRN_URL)
    
    assert_not_nil(ie_approve_mmrn.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "Drafts"
      #~ ie_approve_mmrn.cell(:xpath, "//[contains(text(),'Drafts')]").click   
      ie_approve_mmrn.dt(:text,'Drafts').click
      assert(ie_approve_mmrn.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_approve_mmrn.cell(:xpath, "//[contains(text(),'Inbox')]").click
      ie_approve_mmrn.dt(:text,'Inbox').click
      assert(ie_approve_mmrn.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      ie_approve_mmrn.frame(INBOX_FRAME).cell(:text, /#{test_data[MMRN_NUMBER_FLD]}/).click
      
      ie_approve_mmrn_note = Watir::IE.attach(:title, /eGov Stores - Miscellaneous Material Receipt Note/)
      
      assert(ie_approve_mmrn_note.text_field(:id,RECEIPT_NUMBER_FLD).verify_contains(test_data[MMRN_NUMBER_FLD]) )
  
      
      if(ie_approve_mmrn_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
        
        puts 'designation exists'
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_approve_mmrn_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
       if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
          #~ Watir::Waiter.wait_until{ie_approve_mmrn_note.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).includes?(test_data[STORES_APPROVE_MI_APPROVER_FLD])}     
      ie_approve_mmrn_note.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end
       

    ie_approve_mmrn_note.button(:value, MMRN_APPROVE_BUTTON).click
      assert(ie_approve_mmrn_note.contains_text(test_data[MMRN_NUMBER_FLD]), "MMRN not Approved" )
      
      ie_approve_mmrn_note.button(:value, MMRN_CLOSE_BUTTON).click
      
  ensure
    ie_approve_mmrn.close

end


def reject_mmrn(tc, test_data, attributes, action) 

    ie_reject_mmrn = start_browser(REJECT_MATERIAL_INDENT_URL)

    assert_not_nil(ie_reject_mmrn.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))

    if action == "Drafts"
      #~ ie_reject_mmrn.cell(:xpath, "//[contains(text(),'Drafts')]").click
       ie_reject_mmrn.dt(:text,'Drafts').click
      assert(ie_reject_mmrn.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end

    if action == "Inbox"
      #~ ie_reject_mmrn.cell(:xpath, "//[contains(text(),'Inbox')]").click
      ie_reject_mmrn.dt(:text,'Inbox').click
      assert(ie_reject_mmrn.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
    ie_reject_mmrn.frame(INBOX_FRAME).cell(:text, /#{test_data[MMRN_NUMBER_FLD]}/).click

    ie_reject_mmrn_note = Watir::IE.attach(:title, /eGov Stores - Miscellaneous Material Receipt Note/)

    assert(ie_reject_mmrn_note.text_field(:id,RECEIPT_NUMBER_FLD).verify_contains(test_data[MMRN_NUMBER_FLD]) )

    ie_reject_mmrn_note.button(:value, REJECT_APPROVE_BUTTON).click

    assert(ie_reject_mmrn_note.contains_text(test_data[MMRN_NUMBER_FLD]), "MMRN not Rejected" )

    ie_reject_mmrn_note.button(:value, MMRN_CLOSE_BUTTON).click

  ensure
    ie_reject_mmrn.close

end
  
  
end#Module end
