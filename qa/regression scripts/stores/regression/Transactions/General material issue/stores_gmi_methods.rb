
require path_to_file('stores_constants.rb')

module Stores_gmi_methods

include Stores_constants
#~ include Test::Unit::Assertions
  
  


def create_master_data(tc, test_data, attributes)
  
  create_uom_category(tc, test_data, attributes)
  view_uom_category_success(tc, test_data, attributes)
  
  create_uom(tc, test_data, attributes)
  view_uom_success(tc, test_data, attributes)
  
  create_item_type_success(tc, test_data, attributes)
  view_item_type_success(tc, test_data, attributes)
  
  create_stores(tc,test_data, attributes)    
  view_stores_success(tc, test_data, attributes)
  
  create_supplier(tc, test_data, attributes)
  modify_supplier(tc, test_data, attributes)
  view_supplier_success(tc, test_data, attributes)
  
  create_item(tc, test_data, attributes)
  view_item_success(tc, test_data, attributes)
  
  end


def create_gmi_success(tc, test_data, attributes, action)    

if action == "GMI-MTN"
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, CREATE_GMI_LINK).click
    ie_create_gmi=Watir::IE.attach(:url,/createMtn*/)
end

if action == "GMI"
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, CREATE_GMI_LINK).click
    ie_create_gmi=Watir::IE.attach(:url,/GeneralIssueCreate*/)
end
  
    assert(ie_create_gmi.contains_text(CREATE_GMI_SCREEN),("TC: " + tc + ". Text not found : " + CREATE_GMI_SCREEN  + " - GMI Screen "))
    populate_create_gmi_details(tc, test_data, attributes, ie_create_gmi)
    ie_create_gmi.button(:value, CREATE_GMI_BTN).click

    assert_true(ie_create_gmi.contains_text(CREATE_GMI_NUMBER_LABEL),("TC: " + tc + ". Text not found : " + CREATE_GMI_NUMBER_LABEL  + " - GMI not created"))
    
    if ie_create_gmi.text_field(:id, CREATE_GMI_RECEIPT_NUMBER_FLD).exists?
      test_data[GMI_NUMBER_FLD] = ie_create_gmi.text_field(:id,CREATE_GMI_RECEIPT_NUMBER_FLD).value
    end

ensure
  ie_create_gmi.close

end

def create_gmi_failure(tc, test_data, attributes, action)    

if action =="GMI-MTN"
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, CREATE_GMI_LINK).click
    ie_create_gmi=Watir::IE.attach(:url,/createMtn*/)
end

if action == "GMI"
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, CREATE_GMI_LINK).click
    ie_create_gmi=Watir::IE.attach(:url,/GeneralIssueCreate*/)
end
  
  
  assert(ie_create_gmi.contains_text(CREATE_GMI_SCREEN),("TC: " + tc + ". Text not found : " + CREATE_GMI_SCREEN  + " - GMI Screen "))
  populate_create_gmi_details(tc, test_data, attributes, ie_create_gmi)
  ie_create_gmi.button(:value, CREATE_GMI_BTN).click
  
  assert(ie_create_gmi.text_field(:id, CREATE_GMI_RECEIPT_NUMBER_FLD).exists?)
  #~ assert_false(ie_create_gmi.contains_text(CREATE_GMI_NUMBER_LABEL),("TC: " + tc + ". Text not found : " + CREATE_GMI_NUMBER_LABEL  + " - GMI not created"))
  
  ensure
  ie_create_gmi.close

end


def populate_create_gmi_details(tc, test_data, attributes, ie_create_gmi)

  #~ if test_data[CREATE_GMI_DEPT_FLD] && attributes[CREATE_GMI_DEPT_FLD]!= nil
  #~ ie_create_gmi.select_list(:id,attributes[CREATE_GMI_DEPT_FLD]).set test_data[CREATE_GMI_DEPT_FLD]
  #~ end

  if test_data[CREATE_GMI_ISSUING_STORE_FLD] && attributes[CREATE_GMI_ISSUING_STORE_FLD]!= nil
  ie_create_gmi.select_list(:id,attributes[CREATE_GMI_ISSUING_STORE_FLD]).set test_data[CREATE_GMI_ISSUING_STORE_FLD]
  end


  if test_data[CREATE_GMI_PURPOSE_FLD] && attributes[CREATE_GMI_PURPOSE_FLD]!= nil
  ie_create_gmi.select_list(:id,attributes[CREATE_GMI_PURPOSE_FLD]).set test_data[CREATE_GMI_PURPOSE_FLD]
  end

  if test_data[CREATE_GMI_ACCOUNTCODE_FLD] && attributes[CREATE_GMI_ACCOUNTCODE_FLD]!= nil
  ie_create_gmi.select_list(:id,attributes[CREATE_GMI_ACCOUNTCODE_FLD]).select test_data[CREATE_GMI_ACCOUNTCODE_FLD]
  end

if test_data[CREATE_GMI_PROJECT_CODE_FLD] && attributes[CREATE_GMI_PROJECT_CODE_FLD]!=nil
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_PROJECT_CODE_FLD]).value = test_data[CREATE_GMI_PROJECT_CODE_FLD]
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_PROJECT_CODE_FLD]).fire_event('onKeyPress')
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_PROJECT_CODE_FLD]).fire_event('onKeyUp')
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_PROJECT_CODE_FLD]).fire_event('onblur')
  end
  
 if test_data[CREATE_GMI_ASSET_CODE_FLD] && attributes[CREATE_GMI_ASSET_CODE_FLD]!=nil 
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_ASSET_CODE_FLD]).value = test_data[CREATE_GMI_ASSET_CODE_FLD]
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_ASSET_CODE_FLD]).fire_event('onKeyPress')
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_ASSET_CODE_FLD]).fire_event('onKeyUp')
  #~ ie_create_gmi.text_field(:id, attributes[CREATE_GMI_ASSET_CODE_FLD]).fire_event('onblur')  

end

  if test_data[CREATE_GMI_INDENTING_STORE_FLD] && attributes[CREATE_GMI_INDENTING_STORE_FLD]!= nil
  ie_create_gmi.select_list(:id,attributes[CREATE_GMI_INDENTING_STORE_FLD]).set test_data[CREATE_GMI_INDENTING_STORE_FLD]
  end

  if test_data[CREATE_GMI_ITEM_FLD] && attributes[CREATE_GMI_ITEM_FLD]!= nil
  ie_create_gmi.text_field(:id, attributes[CREATE_GMI_ITEM_FLD]).set test_data[CREATE_GMI_ITEM_FLD]
  end


  if test_data[CREATE_GMI_QUANTIY_FLD] && attributes[CREATE_GMI_QUANTIY_FLD]!= nil
  ie_create_gmi.text_field(:id,attributes[CREATE_GMI_QUANTIY_FLD]).set test_data[CREATE_GMI_QUANTIY_FLD]
  end


  if test_data[CREATE_GMI_SALE_PRICE_FLD] && attributes[CREATE_GMI_SALE_PRICE_FLD]!= nil
  ie_create_gmi.text_field(:id,attributes[CREATE_GMI_SALE_PRICE_FLD]).set test_data[CREATE_GMI_SALE_PRICE_FLD]
  end


  if test_data[CREATE_GMI_SCRAP_ITEM_FLD] && attributes[CREATE_GMI_SCRAP_ITEM_FLD]!= nil
  ie_create_gmi.text_field(:id,attributes[CREATE_GMI_SCRAP_ITEM_FLD]).set test_data[CREATE_GMI_SCRAP_ITEM_FLD]
  end


  if test_data[STORES_CREATE_MI_DEGIGNATION_FLD] && attributes[STORES_CREATE_MI_DEGIGNATION_FLD]!=nil
  ie_create_gmi.select_list(:id,attributes[STORES_CREATE_MI_DEGIGNATION_FLD]).set test_data[STORES_CREATE_MI_DEGIGNATION_FLD]
  end
    
    
  if test_data[STORES_CREATE_MI_APPROVER_FLD] && attributes[STORES_CREATE_MI_APPROVER_FLD]!=nil
  ie_create_gmi.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).set test_data[STORES_CREATE_MI_APPROVER_FLD]
  end

end

def view_gmi_success(tc, test_data, attributes)

  #~ ie_view_gmi = start_browser(VIEW_GMI_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
  $browser.dt(:text,'Transactions').click
  $browser.link(:text, VIEW_GMI_LINK).click
  ie_view_gmi=Watir::IE.attach(:url,/searchviewMrin*/)
  
  assert(ie_view_gmi.contains_text(VIEW_GMI_SEARCH_SCREEN),("TC: " + ". Text not found : " + VIEW_GMI_SEARCH_SCREEN + " - GMI view screen"))
  
  ie_view_gmi.button(:value, VIEW_GMI_BTN).click                            # SEARCH FOR THE CREATED GMI's TO VIEW.
  ie_view_gmi.link(:text, test_data[GMI_NUMBER_FLD]).click               # CLICK ON THE GMI DRILL DOWN TO VIEW IT.
  
  assert(ie_view_gmi.contains_text(VIEW_GMI_SCREEN),("TC: " + ". Text not found : " + VIEW_GMI_SCREEN + " - GMI view screen"))
  assert(ie_view_gmi.text_field(:id,CREATE_GMI_RECEIPT_NUMBER_FLD).verify_contains(test_data[GMI_NUMBER_FLD]) )

ensure
  ie_view_gmi.close

end


def approve_gmi(tc, test_data, attributes, action)

    ie_approve_gmi = start_browser(APPROVE_GMI_URL)

    assert_not_nil(ie_approve_gmi.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "Drafts"
      #~ ie_approve_gmi.cell(:xpath, "//[contains(text(),'Drafts')]").click
      ie_approve_gmi.dt(:text,'Drafts').click
      assert(ie_approve_gmi.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_approve_gmi.cell(:xpath, "//[contains(text(),'Inbox')]").click
      ie_approve_gmi.dt(:text,'Inbox').click
      assert(ie_approve_gmi.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      ie_approve_gmi.frame(INBOX_FRAME).cell(:text, /#{test_data[GMI_NUMBER_FLD]}/).click
      
      ie_approve_gmi_note = Watir::IE.attach(:title, /General Material Issue/)
      
      assert(ie_approve_gmi_note.text_field(:id,CREATE_GMI_RECEIPT_NUMBER_FLD).verify_contains(test_data[GMI_NUMBER_FLD]) )
  
   if( ie_approve_gmi_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_approve_gmi_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
       if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
      ie_approve_gmi_note.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end
  
      ie_approve_gmi_note.button(:value, GMI_APPROVE_BUTTON).click
      
      assert(ie_approve_gmi_note.text_field(:id,CREATE_GMI_RECEIPT_NUMBER_FLD).verify_contains(test_data[GMI_NUMBER_FLD]) )

      ie_approve_gmi_note.button(:value, GMI_CLOSE_BUTTON).click
    
  ensure
      ie_approve_gmi.close

end


def reject_gmi(tc,test_data, attributes, action)

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
    
    ie_reject_mmrn.frame(INBOX_FRAME).cell(:text, /#{test_data[GMI_NUMBER_FLD]}/).click

    ie_reject_mmr_note = Watir::IE.attach(:title, /General Material Issue/)

    assert(ie_reject_mmr_note.text_field(:id,CREATE_GMI_RECEIPT_NUMBER_FLD).verify_contains(test_data[GMI_NUMBER_FLD]) )

    ie_reject_mmr_note.button(:value, GMI_REJECT_BUTTON).click

    assert(ie_reject_mmr_note.text_field(:id,CREATE_GMI_RECEIPT_NUMBER_FLD).verify_contains(test_data[GMI_NUMBER_FLD]) )

    ie_reject_mmr_note.button(:value, GMI_CLOSE_BUTTON).click

  ensure
    ie_reject_mmrn.close

end

end#Module End