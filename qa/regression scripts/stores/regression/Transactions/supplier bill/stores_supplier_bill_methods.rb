
require path_to_file('stores_constants.rb')

module Stores_supplier_bill_methods

include Stores_constants
include Test::Unit::Assertions




def create_supplier_bill_success(tc, test_data, attributes,  action)

    if action == "MMRN"
      
            #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
            $browser.dt(:text,'Transactions').click
            $browser.link(:text, STORES_CREATE_SUPPLIER_BILL_LINK).click
            ie_create_supplier_bill=Watir::IE.attach(:url,/create*/)

            assert_not_nil(ie_create_supplier_bill.contains_text(STORES_CREATE_SUPPLIER_BILL_SEARCH_SCREEN),("TC: " + tc + ". Text not found : " +  STORES_CREATE_SUPPLIER_BILL_SEARCH_SCREEN + " - Supplier Search Screen "))
            ie_create_supplier_bill.button(:value, STORES_CREATE_SUPPLIER_BILL_SEARCH_BTN).click # to see the created MRN's  to submit
            
            row_loop(tc, test_data, attributes,ie_create_supplier_bill)    
            
            ie_create_supplier_bill.button(:value, STORES_CREATE_SUPPLIER_BILL_SUBMIT_BTN).click             # Select the created MRN & submit to it create supplier bill

            populate_create_supplier_bill_details(tc, test_data, attributes, ie_create_supplier_bill)
            
            ie_create_supplier_bill.button(:value, STORES_CREATE_SUPPLIER_BILL_SAVE_AND_CLOSE_BTN).click  # To save the supplier bill 
            
            assert_true(ie_create_supplier_bill.button(:value, STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON).exists?, "Supplier bill not Created" )

            # To capture the Supplier Bill number created
            test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD] = ie_create_supplier_bill.text_field(:id,STORES_CREATE_SUPPLIER_BILL_NUMBER_FLD).value

             puts "Supplier Bill number created is : "  +  test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD]

    end

          ensure
           ie_create_supplier_bill.close
        
end #END OF CREATE SUPPLIER BILL SUCCESS METHOD
      
def create_supplier_bill_failure(tc, test_data, attributes, action)

    if action == "MMRN"
      
            #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
            $browser.dt(:text,'Transactions').click
            $browser.link(:text, STORES_CREATE_SUPPLIER_BILL_LINK).click
            ie_create_supplier_bill=Watir::IE.attach(:url,/create*/)

            assert_not_nil(ie_create_supplier_bill.contains_text(STORES_CREATE_SUPPLIER_BILL_SEARCH_SCREEN),("TC: " + tc + ". Text not found : " +  STORES_CREATE_SUPPLIER_BILL_SEARCH_SCREEN + " - Supplier Search Screen "))
            ie_create_supplier_bill.button(:value, STORES_CREATE_SUPPLIER_BILL_SEARCH_BTN).click # to see the created MRN's  to submit
            
            row_loop(tc, test_data, attributes,ie_create_supplier_bill)    
            
            ie_create_supplier_bill.button(:value, STORES_CREATE_SUPPLIER_BILL_SUBMIT_BTN).click             # Select the created MRN & submit to it create supplier bill

            populate_create_supplier_bill_details(tc, test_data, attributes, ie_create_supplier_bill)
            
            ie_create_supplier_bill.button(:value, STORES_CREATE_SUPPLIER_BILL_SAVE_AND_CLOSE_BTN).click  # To save the supplier bill 
            
            assert_false(ie_create_supplier_bill.button(:value, STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON).exists?, "Supplier bill Created" )

    end

        ensure
           ie_create_supplier_bill.close

end   # END OF THE CREATE SUPPLIER FAILURE METHOD


def row_loop(tc, test_data, attributes,ie_create_supplier_bill)  # Method to locate the exact MRN record & select the check box.

      i =0 
      ie_create_supplier_bill.table(:id, STORES_CREATE_SUPPLIER_BILL_MRN_TABLE).each {|row|
      #~ if row.link(:text,'IMRN/19/25/2009-10').exists?
      if row.link(:text,/#{test_data[MMRN_NUMBER_FLD]}/).exists?
      puts i
      row.checkbox(:id,STORES_CREATE_SUPPLIER_BILL_MRN_CHECK_BOX).set
      else
        i=i+1
      end
      }

end


def populate_create_supplier_bill_details(tc, test_data, attributes, ie_create_supplier_bill)


    assert_not_nil(ie_create_supplier_bill.contains_text(STORES_CREATE_SUPPLIER_BILL_SCREEN),("TC: " + tc + ". Text not found : " + STORES_CREATE_SUPPLIER_BILL_SCREEN  + " - Not in Supplier Bill create screen"))

  if test_data[SUPPLIER_BILL_DATE_MMRN_DATE] == "1"
      if test_data[CREATE_MMRN_DATE_FLD] && attributes[CREATE_MMRN_DATE_FLD] !=nil
        ie_create_supplier_bill.text_field(:id,attributes[STORES_CREATE_SUPPLIER_BILL_DATE_FLD]).set test_data[CREATE_MMRN_DATE_FLD]
      end
  end
  
   if test_data[SUPPLIER_BILL_DATE] == "1"
      if test_data[CREATE_MMRN_DATE_FLD] && attributes[CREATE_MMRN_DATE_FLD] !=nil
        ie_create_supplier_bill.text_field(:id,attributes[STORES_CREATE_SUPPLIER_BILL_DATE_FLD]).set test_data[STORES_CREATE_SUPPLIER_BILL_DATE_FLD]
      end
   end
  
    ie_create_supplier_bill.link(:text, STORES_CREATE_SUPPLLIER_BILL_ACCOUNT_DETAILS_TAB).click


##########################   To add multiple rows for account details   #############################
 
 if test_data[STORES_SUPPLIER_BILL_ACCOUNT_DETAILS] == "1"
 
    rowcount = ie_create_supplier_bill.table(:id, STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE).row_count
    puts rowcount
    loop = test_data[STORES_FOR_LOOP_SUPPLIER_BILL_ACC_DETAILS].to_i

    for i in 1...loop+1 #For loop to add multiple line items

          ie_create_supplier_bill.image(:id,STORES_SUPPLIER_BILL_ADD_ACC_DETAILS_BTN).flash
          ie_create_supplier_bill.image(:id,STORES_SUPPLIER_BILL_ADD_ACC_DETAILS_BTN).click
        
        if test_data[STORES_CREATE_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s] && attributes[STORES_CREATE_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s] !=nil
          ie_create_supplier_bill.table(:id, STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE)[i+rowcount][1].text_field(:id,attributes[STORES_CREATE_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s]).set test_data[STORES_CREATE_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s]
        end

        if test_data[STORES_CREATE_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s] && attributes[STORES_CREATE_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s] !=nil
          ie_create_supplier_bill.table(:id, STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE)[i+rowcount][3].text_field(:id,attributes[STORES_CREATE_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s]).set test_data[STORES_CREATE_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s]
        end
        
    end

end

 ################################################################################ 
  
 ######################### To add multiple rows for deduction details  ###############################
 
  if test_data[STORES_SUPPLIER_BILL_DEDUCTION_DETAILS] == "1"
    
    loop = test_data[STORES_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS].to_i   

    for i in 1...loop+1 #For loop to add multiple line items

        if test_data[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s] && attributes[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s] !=nil
          ie_create_supplier_bill.table(:id, STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE)[i+1][1].text_field(:id,attributes[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s]).set test_data[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s]
        end

        if test_data[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s] && attributes[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s] !=nil
          ie_create_supplier_bill.table(:id, STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE)[i+1][3].text_field(:id,attributes[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s]).set test_data[STORES_CREATE_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s]
        end
        
      if i<test_data[STORES_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS].to_i   
        ie_create_supplier_bill.image(:id,STORES_SUPPLIER_BILL_ADD_DED_DETAILS_BTN).flash
        ie_create_supplier_bill.image(:id,STORES_SUPPLIER_BILL_ADD_DED_DETAILS_BTN).click
      end
        
    end   

    rowcount_deduc = ie_create_supplier_bill.table(:id, STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE).row_count
    puts  rowcount_deduc
end

 ################################################################################ 
  
  

    if test_data[STORES_CREATE_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD] && attributes[STORES_CREATE_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD] !=nil
      ie_create_supplier_bill.select_list(:id,attributes[STORES_CREATE_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD]).set test_data[STORES_CREATE_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD]
    end
    
    
       if test_data[STORES_CREATE_MI_DEGIGNATION_FLD] && attributes[STORES_CREATE_MI_DEGIGNATION_FLD]!=nil
      ie_create_supplier_bill.select_list(:id,attributes[STORES_CREATE_MI_DEGIGNATION_FLD]).set test_data[STORES_CREATE_MI_DEGIGNATION_FLD]
    end
    
    
       if test_data[STORES_CREATE_MI_APPROVER_FLD] && attributes[STORES_CREATE_MI_APPROVER_FLD]!=nil
      ie_create_supplier_bill.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).set test_data[STORES_CREATE_MI_APPROVER_FLD]
    end

    

end  # END OF POPULATE SUPPLIER BILL METHOD


def view_supplier_bill(tc, test_data, attributes)

      #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
      $browser.dt(:text,'Transactions').click
      $browser.link(:text, STORES_VIEW_SUPPLIER_BILL_LINK).click
      ie_view_supplier_bill=Watir::IE.attach(:url,/view*/)
      
      assert_not_nil(ie_view_supplier_bill.contains_text(STORES_VIEW_SUPPLIER_BILL_SEARCH_SCREEN),("TC: " + tc + ". Text not found : " +  STORES_VIEW_SUPPLIER_BILL_SEARCH_SCREEN + " - NOT IN VIEW SUPPLIER BILL SEARCH SCREEN "))
      
      ie_view_supplier_bill.button(:value, STORES_VIEW_SUPPLIER_BILL_SEARCH_BUTTON).click
      #~ ie_view_supplier_bill.link(:text, test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD]).click
      
      assert_true(ie_view_supplier_bill.contains_text(test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD]), " VIEW SUPPLIER BILL FAILED")
    
    ensure
      ie_view_supplier_bill.close



end  # END OF VIEW SUPPLIER METHOD


def approve_reject_supplier_bill(tc, test_data, attributes, action, actions)
  
    ie_approve_reject_supplier_bill = start_browser(STORES_APPROVE_REJECT_SUPPLIER_BILL_URL)
      
    assert_not_nil(ie_approve_reject_supplier_bill.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "Drafts"
      #~ ie_approve_reject_supplier_bill.cell(:xpath, "//[contains(text(),'Drafts')]").click   
      ie_approve_reject_supplier_bill.dt(:text,'Drafts').click
      assert(ie_approve_reject_supplier_bill.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_approve_reject_supplier_bill.cell(:xpath, "//[contains(text(),'Inbox')]").click
      ie_approve_reject_supplier_bill.dt(:text,'Inbox').click
      assert(ie_approve_reject_supplier_bill.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
      
      #~ ie_approve_reject_supplier_bill.frame(INBOX_FRAME).cell(:text, "SBILL/5/0910").click
      ie_approve_reject_supplier_bill.frame(INBOX_FRAME).cell(:text, /#{test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD]}/).click
      
      ie_approve_reject_supplier_bill_note = Watir::IE.attach(:title, /Supplier Bill/) # To attach the supplier bill modify screen to approve it
      assert(ie_approve_reject_supplier_bill_note.text_field(:id,STORES_CREATE_SUPPLIER_BILL_NUMBER_FLD).verify_contains(test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD]) )
     
 if( ie_approve_reject_supplier_bill_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_approve_reject_supplier_bill_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
    if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
      Watir::Waiter.wait_until{ie_approve_reject_supplier_bill_note.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).includes?(test_data[STORES_APPROVE_MI_APPROVER_FLD])}     
      ie_approve_reject_supplier_bill_note.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end


    if actions == "Approve"
          ie_approve_reject_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_APPROVE_BUTTON).click
          # After approval of supplier bill confriming it through print button displayed on the screen
          assert(ie_approve_reject_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON).exists?, "Supplier bill not Approved" )  
          puts "SUPPIER BILL APPROVED"
    end

    if actions == "Reject"
          ie_approve_reject_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_REJECT_BUTTON).click
          # After rejecting the supplier bill confirming it through print button displayed on the screen
          assert(ie_approve_reject_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON).exists?, "Supplier bill not Rejected" )  
          puts "SUPPIER BILL REJECTED"
    end
  
  
    ie_approve_reject_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_CLOSE_BUTTON).click
      
  ensure
    ie_approve_reject_supplier_bill.close

end     #END OF APPROVE & REJECT SUPPLIER BILL METHOD


def modify_supplier_bill(tc, test_data, attributes, action, actions)
  
    ie_modify_supplier_bill = start_browser(STORES_MODIFY_SUPPLIER_BILL_URL)
      
    assert_not_nil(ie_modify_supplier_bill.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "Drafts"
      #~ ie_modify_supplier_bill.cell(:xpath, "//[contains(text(),'Drafts')]").click   
      ie_modify_supplier_bill.dt(:text,'Drafts').click
      assert(ie_modify_supplier_bill.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_modify_supplier_bill.cell(:xpath, "//[contains(text(),'Inbox')]").click
      ie_modify_supplier_billl.dt(:text,'Inbox').click
      assert(ie_modify_supplier_bill.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    

      ie_modify_supplier_bill.frame(INBOX_FRAME).cell(:text, /#{test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD]}/).click
      
      ie_modify_supplier_bill_note = Watir::IE.attach(:title, /Supplier Bill/) # To attach the supplier bill modify screen to approve it
      assert(ie_modify_supplier_bill_note.text_field(:id,STORES_CREATE_SUPPLIER_BILL_NUMBER_FLD).verify_contains(test_data[STORES_CREATE_SUPPLIER_BILL_NUM_FLD]) )
      populate_modify_supplier_bill_details(tc, test_data, attributes, action, actions, ie_modify_supplier_bill_note)
  

  
  ensure
    ie_modify_supplier_bill.close
  
end # END OF MODIFY SUPPLIER BILL METHOD



def populate_modify_supplier_bill_details(tc, test_data, attributes, action, actions, ie_modify_supplier_bill_note)
  
  
  assert_not_nil(ie_modify_supplier_bill_note.contains_text(STORES_MODIFY_SUPPLIER_BILL_SCREEN),("TC: " + tc + ". Text not found : " + STORES_MODIFY_SUPPLIER_BILL_SCREEN  + " - Not in Supplier Bill Modify screen"))

  if test_data[MODIFY_SUPPLIER_BILL_DATE_MMRN_DATE] == "1"
      if test_data[CREATE_MMRN_DATE_FLD] && attributes[CREATE_MMRN_DATE_FLD] !=nil
        ie_modify_supplier_bill_note.text_field(:id,attributes[STORES_MODIFY_SUPPLIER_BILL_DATE_FLD]).set test_data[CREATE_MMRN_DATE_FLD]
      end
  end
  
   if test_data[MODIFY_SUPPLIER_BILL_DATE] == "1"
      if test_data[STORES_MODIFY_SUPPLIER_BILL_DATE_FLD] && attributes[STORES_MODIFY_SUPPLIER_BILL_DATE_FLD] !=nil
        ie_modify_supplier_bill_note.text_field(:id,attributes[STORES_MODIFY_SUPPLIER_BILL_DATE_FLD]).set test_data[STORES_MODIFY_SUPPLIER_BILL_DATE_FLD]
      end
   end
  
    ie_modify_supplier_bill_note.link(:text, STORES_CREATE_SUPPLLIER_BILL_ACCOUNT_DETAILS_TAB).click


###############################    TO DELETE ALREADY EXISITNG ACCOUNT DETIALS    ##############################

  if test_data[STORES_MODIFY_SUPPLIER_BILL_DELETE_ACC_DETAILS_LINES] == "1"             

  loop = test_data[STORES_MODIFY_SUPPLIER_BILL_FOR_LOOP_DELETE_ACC_DETAILS].to_i

      for i in 1...loop+1

            ie_modify_supplier_bill_note.image(:src, /removerow.gif/).flash
            ie_modify_supplier_bill_note.image(:src, /removerow.gif/).click

      end  

  end

###########################################################################################


##########################   To add multiple rows for account details   #############################
 
 if test_data[STORES_MODIFY_SUPPLIER_BILL_ACCOUNT_DETAILS] == "1"
 
    rowcount = ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE).row_count
    puts rowcount
    
    loop = test_data[STORES_MODIFY_FOR_LOOP_SUPPLIER_BILL_ACC_DETAILS].to_i

    for i in 1...loop+1 #For loop to add multiple line items

          ie_modify_supplier_bill_note.image(:id,STORES_MODIFY_SUPPLIER_BILL_ADD_ACC_DETAILS_BTN).flash
          ie_modify_supplier_bill_note.image(:id,STORES_MODIFY_SUPPLIER_BILL_ADD_ACC_DETAILS_BTN).click
        
        if test_data[STORES_MODIFY_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s] && attributes[STORES_MODIFY_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s] !=nil
          ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE)[i+rowcount][1].text_field(:id,attributes[STORES_MODIFY_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s]).set test_data[STORES_MODIFY_SUPPLIER_BILL_ACC_CODE_FLD+i.to_s]
        end

        if test_data[STORES_MODIFY_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s] && attributes[STORES_MODIFY_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s] !=nil
          ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE)[i+rowcount][3].text_field(:id,attributes[STORES_MODIFY_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s]).set test_data[STORES_MODIFY_SUPPLIER_BILL_ACC_AMOUNT_FLD+i.to_s]
        end
        
    end

end

    rowcount = ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_ACC_DETAILS_TABLE).row_count
    puts rowcount

################################################################################ 

###############################    TO DELETE ALREADY EXISITNG DEDUCTION DETAILS      ##############################

  if test_data[STORE_MODIFY_SUPPLIER_BILL_DELETE_DEDUCTION_DETIALS] == "1"             

  loop = test_data[MODIFY_FOR_LOOP_PO_DELETE_DEDUCTION_DETAILS].to_i

      for i in 1...loop+1

          ie_modify_supplier_bill_note.image(:id,STORES_MODIFY_SUPPLIER_BILL_DELETE_DEDUCTION_DETAILS_BTN).flash
          ie_modify_supplier_bill_note.image(:id,STORES_MODIFY_SUPPLIER_BILL_DELETE_DEDUCTION_DETAILS_BTN).click

      end  

  end

###########################################################################################

  
######################### To add multiple rows for deduction details  ###############################
 
  if test_data[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_DETAILS] == "1"
    
    loop = test_data[STORES_MODIFY_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS].to_i   
    
    rowcount_deduc = ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE).row_count
    puts  rowcount_deduc
    

    for i in 1...loop+1 #For loop to add multiple line items
    
   #~ if i<test_data[STORES_MODIFY_FOR_LOOP_SUPPLIER_BILL_DEDUCTION_DETAILS].to_i   
        ie_modify_supplier_bill_note.image(:id,STORES_SUPPLIER_BILL_ADD_DED_DETAILS_BTN).flash
        ie_modify_supplier_bill_note.image(:id,STORES_SUPPLIER_BILL_ADD_DED_DETAILS_BTN).click
      #~ end

        if test_data[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s] && attributes[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s] !=nil
          ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE)[i+rowcount_deduc][1].text_field(:id,attributes[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s]).set test_data[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_CODE_FLD+i.to_s]
        end

        if test_data[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s] && attributes[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s] !=nil
          ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE)[i+rowcount_deduc][3].text_field(:id,attributes[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s]).set test_data[STORES_MODIFY_SUPPLIER_BILL_DEDUCTION_AMOUNT_FLD+i.to_s]
        end
        
        
    end   
    
  end
  
    rowcount_deduc = ie_modify_supplier_bill_note.table(:id, STORES_SUPPLLIER_BILL_DEDUCTION_DETAILS_TABLE).row_count
    puts  rowcount_deduc

################################################################################ 
  
    if test_data[STORES_MODIFY_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD] && attributes[STORES_MODIFY_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD] !=nil
      ie_modify_supplier_bill_note.select_list(:id,attributes[STORES_MODIFY_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD]).set test_data[STORES_MODIFY_SUPPLIER_BILL_NETPAY_ACC_CODE_FLD]
    end


       if test_data[STORES_CREATE_MI_DEGIGNATION_FLD] && attributes[STORES_CREATE_MI_DEGIGNATION_FLD]!=nil
      ie_modify_supplier_bill_note.select_list(:id,attributes[STORES_CREATE_MI_DEGIGNATION_FLD]).set test_data[STORES_CREATE_MI_DEGIGNATION_FLD]
    end
    
    
       if test_data[STORES_CREATE_MI_APPROVER_FLD] && attributes[STORES_CREATE_MI_APPROVER_FLD]!=nil
      ie_modify_supplier_bill_note.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).set test_data[STORES_CREATE_MI_APPROVER_FLD]
    end


    if actions == "Success"
          ie_modify_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_APPROVE_BUTTON).click
          # After modifying & approving the supplier bill confriming it through print button displayed on the screen
          assert(ie_modify_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON).exists?, "Supplier Bill Modification is Failed" )  
          puts "SUPPIER BILL IS MODIFIED"
    end

    if actions == "Failure"
          ie_modify_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_APPROVE_BUTTON).click
          # After modifying & saving not able to find the print button, modification is not done to supplier bill
          assert_false(ie_modify_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_APPROVE_PRINT_BUTTON).exists?, "Supplier Bill is Successfully Modified" )  
          puts "SUPPIER BILL IS NOT MODIFIED"
    end
  
         ie_modify_supplier_bill_note.button(:value, STORES_SUPPLIER_BILL_CLOSE_BUTTON).click
  
  
end # END OF POPULATE MODIFY SUPPLIER BILL METHOD







end  #END OF SUPPLIER BILL MODULE


