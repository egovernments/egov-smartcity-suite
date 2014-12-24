
require path_to_file('stores_constants.rb')

module Stores_mtn_methods

include Stores_constants
include Test::Unit::Assertions
  
  
def create_mtn_success(tc, test_data, attributes, action)

      #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
      $browser.dt(:text,'Transactions').click
      $browser.link(:text, CREATE_MTN_INWARD_LINK).click
      ie_create_mtn=Watir::IE.attach(:url,/loadSearchForMRIN*/)
      
      assert(ie_create_mtn.contains_text(STORES_CREATE_MTN_SEARCH_PAGE),("TC: " + tc + ". Text not found : " + STORES_CREATE_MTN_SEARCH_PAGE  + "Not in MTN Search screen"))
      ie_create_mtn.button(:value, STORES_CREATE_MTN_SEARCH_BTN).click  # SEARCHS THE APPROVED INDENT ISSUE TO MAKE A RECEIPT AGAINST AN INDENT ISSUE 
      
    if action == "Indent Issue"
      #~ ie_create_mtn.link(:text, "MTNO/12/27/2009-10").click       # CLICK THE INDENT ISSUE DRILL DOWN
      ie_create_mtn.link(:text, test_data[CREATE_MII_NUMBER_FLD]).click       # CLICK THE INDENT ISSUE DRILL DOWN

    end
    
    if action == "GMI"
      ie_create_mtn.link(:text, test_data[GMI_NUMBER_FLD]).click                 # CLICK THE GMI DRILL DOWN
    end
    
      populate_create_mtn_details(tc, test_data, attributes, ie_create_mtn)  
      ie_create_mtn.button(:value, STORES_CREATE_MTN_BTN).click                                 # SAVE THE mtn
      
      assert_true(ie_create_mtn.text_field(:id, STORES_CREATE_MTN_RECEIPT_NUMBER_FLD).exists?)
      #~ assert_true(ie_create_mtn.contains_text(STORES_CREATE_MTN_LABEL), "Receipt for the material indent issue is not done")

    if ie_create_mtn.text_field(:id, STORES_CREATE_MTN_RECEIPT_NUMBER_FLD).exists?
      test_data[STORES_CREATE_MTN_NUMBER] = ie_create_mtn.text_field(:id,STORES_CREATE_MTN_RECEIPT_NUMBER_FLD).value       # COPY THE  mtn NUMBER
      puts "MATERIAL TRANSFER NOTE CREATED IS : "  +  test_data[STORES_CREATE_MTN_NUMBER]
    end
   
   ensure 
   ie_create_mtn.close
    
end



def populate_create_mtn_details(tc, test_data, attributes, ie_create_mtn )
  
    assert(ie_create_mtn.contains_text(STORES_CREATE_MTN_SCREEN),("TC: " + tc + ". Text not found : " + STORES_CREATE_MTN_SCREEN  + " - Not in Material Transfer Note screen"))  
  
  
loop = test_data[STORES_CREATE_MTN_FOR_LOOP].to_i

  for i in 1...loop+1 #For loop to add multiple rows of item 
    
      if test_data[STORES_CREATE_MTN_RECEIVED_QTY_FLD+i.to_s] && attributes[STORES_CREATE_MTN_RECEIVED_QTY_FLD+i.to_s]!=nil
        ie_create_mtn.table(:id, STORES_MTN_TABLE)[i+1][5].text_field(:id, attributes[STORES_CREATE_MTN_RECEIVED_QTY_FLD+i.to_s]).set test_data[STORES_CREATE_MTN_RECEIVED_QTY_FLD+i.to_s]
      end

      if test_data[STORES_CREATE_MTN_ACCEPTED_QTY_FLD+i.to_s] && attributes[STORES_CREATE_MTN_ACCEPTED_QTY_FLD]+i.to_s!=nil
        ie_create_mtn.table(:id, STORES_MTN_TABLE)[i+1][6].text_field(:id, attributes[STORES_CREATE_MTN_ACCEPTED_QTY_FLD+i.to_s]).set test_data[STORES_CREATE_MTN_ACCEPTED_QTY_FLD+i.to_s]
      end

      if test_data[STORES_CREATE_MTN_OTHER_CHARGES_FLD+i.to_s] && attributes[STORES_CREATE_MTN_OTHER_CHARGES_FLD+i.to_s]!=nil
        ie_create_mtn.table(:id, STORES_MTN_TABLE)[i+1][8].text_field(:id,attributes[STORES_CREATE_MTN_OTHER_CHARGES_FLD+i.to_s]).set test_data[STORES_CREATE_MTN_OTHER_CHARGES_FLD+i.to_s]
      end

      
        #~ ie_create_mtn.table(:id, 'MRNLTable')[i+1][8].link(:text, BREAK_UP_LINK).click 
        
###########################################################################################################################

if test_data[STORES_CREATE_MTN_BREAK_UP_DETAILS_LINK] == '1'  # Boolean logic to enter the breakup details

    #~ ie_create_mtn.link(:text, BREAK_UP_LINK).flash 
    #~ ie_create_mtn.link(:text, BREAK_UP_LINK).click 
        ie_create_mtn.table(:id, STORES_MTN_TABLE)[i+1][10].link(:text, BREAK_UP_LINK).click

    ie_create_mtn_other_details = Watir::IE.attach(:title, /OtherDetails Entry/)   # Attach the Material Other Details screen to enter the breakup details
    Watir::Waiter.wait_until{ie_create_mtn_other_details.contains_text("Material Other Details")}

  if test_data[STORES_CREATE_MTN_BREAKUP_QTY_FLD] && attributes[STORES_CREATE_MTN_BREAKUP_QTY_FLD]!=nil
    ie_create_mtn_other_details.text_field(:id, attributes[STORES_CREATE_MTN_BREAKUP_QTY_FLD]).set test_data[STORES_CREATE_MTN_BREAKUP_QTY_FLD]
   end

  if test_data[STORES_CREATE_MTN_BREAKUP_LOT_FLD] && attributes[STORES_CREATE_MTN_BREAKUP_LOT_FLD]!=nil
    ie_create_mtn_other_details.text_field(:id, attributes[STORES_CREATE_MTN_BREAKUP_LOT_FLD]).set test_data[STORES_CREATE_MTN_BREAKUP_LOT_FLD]
  end

  if test_data[STORES_CREATE_MTN_BREAKUP_EXP_DATE_FLD] && attributes[STORES_CREATE_MTN_BREAKUP_EXP_DATE_FLD]!=nil
    ie_create_mtn_other_details.text_field(:id, attributes[STORES_CREATE_MTN_BREAKUP_EXP_DATE_FLD]).set test_data[STORES_CREATE_MTN_BREAKUP_EXP_DATE_FLD]
  end

  if test_data[STORES_CREATE_MTN_BIN_NO_FLD] && attributes[STORES_CREATE_MTN_BIN_NO_FLD]!=nil
    ie_create_mtn_other_details.select_list(:id,attributes[STORES_CREATE_MTN_BIN_NO_FLD]).set test_data[STORES_CREATE_MTN_BIN_NO_FLD]
  end

    ie_create_mtn_other_details.button(:value, STORES_CREATE_MTN_ITEM_OTHER_DETAILS_BTN).click

end # End of other detials

#~ ##############################################################################################



  end  # END OF FOR LOOP
    




end  # END OF POPULATE mtn DETIALS METHOD


def approve_mtn(tc, test_data, attributes, action)

    ie_approve_mtn = start_browser(STORES_APPROVE_MRN_URL)

    assert_not_nil(ie_approve_mtn.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "My Drafts"
      ie_approve_mtn.div(:class, STORES_TRANSACTIONS_LINK).link(:text, STORES_TRANSACTIONS_DRAFTS_LINK).click
      assert(ie_approve_mtn.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "My Works"
      ie_approve_mtn.div(:class, STORES_TRANSACTIONS_LINK).link(:text, STORES_TRANSACTIONS_LINK_WORKS_LINK).click
      assert(ie_approve_mtn.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Works Folder")
    end
    
      ie_approve_mtn.frame(INBOX_FRAME).cell(:text, /#{test_data[STORES_CREATE_MTN_NUMBER]}/).click
      
      ie_approve_mtn_note = Watir::IE.attach(:title, /Material/)
      
      assert(ie_approve_mtn_note.text_field(:id,STORES_CREATE_MTN_RECEIPT_NUMBER_FLD).verify_contains(test_data[STORES_CREATE_MTN_NUMBER]) )
  
      ie_approve_mtn_note.button(:value, STORES_MTN_APPROVE_BUTTON).click
    
      assert(ie_approve_mtn_note.contains_text(test_data[STORES_CREATE_MTN_NUMBER]), "mtn not Approved" )  
    
      ie_approve_mtn_note.button(:type, STORES_MTN_CLOSE_BUTTON).click

  ensure
   ie_approve_mtn.close


end  # END OF APPROVE mtn METHOD


def reject_mtn(tc, test_data, attributes, action)

    ie_reject_mtn = start_browser(STORES_REJECT_MRN_URL)

    assert_not_nil(ie_reject_mtn.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "My Drafts"
      ie_reject_mtn.div(:class, STORES_TRANSACTIONS_LINK).link(:text, STORES_TRANSACTIONS_DRAFTS_LINK).click
      assert(ie_reject_mtn.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "My Works"
      ie_reject_mtn.div(:class, STORES_TRANSACTIONS_LINK).link(:text, STORES_TRANSACTIONS_LINK_WORKS_LINK).click
      assert(ie_reject_mtn.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Works Folder")
    end
    
      ie_reject_mtn.frame(INBOX_FRAME).cell(:text, /#{test_data[STORES_CREATE_MTN_NUMBER]}/).click
      
     ie_reject_mtn_note = Watir::IE.attach(:title, /eGov Stores - MTN-Inward/)
      
      assert(ie_reject_mtn_note.text_field(:id,STORES_CREATE_MTN_RECEIPT_NUMBER_FLD).verify_contains(test_data[STORES_CREATE_MTN_NUMBER]) )
  
      ie_reject_mtn_note.button(:value, STORES_MTN_REJECT_BUTTON).click
    
      assert(ie_reject_mtn_note.contains_text(test_data[STORES_CREATE_MTN_NUMBER]), "mtn not Rejected" )  
    
      ie_reject_mtn_note.button(:type, STORES_MTN_CLOSE_BUTTON).click

  ensure
   ie_reject_mtn.close


end  # END OF REJECT mtn METHOD





end#Module End



