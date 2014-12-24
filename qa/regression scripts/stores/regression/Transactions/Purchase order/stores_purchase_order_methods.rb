 
require path_to_file('stores_constants.rb')

module Stores_purchase_order_methods

include Stores_constants
include Test::Unit::Assertions

  
def create_purchase_order_success(tc, test_data, attributes, action)
  
if action == "po non indent"
  
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, STORES_CREATE_PO_NON_INDENT_LINK).click
    ie_create_purchase_order=Watir::IE.attach(:url,/GeneralPurchaseOrderData*/)
  
end
  
if action == "po indent"

    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, STORES_CREATE_PO_INDENT_LINK).click
    ie_create_purchase_order=Watir::IE.attach(:url,/PurchaseOrderCentral*/)
    
    assert_not_nil(ie_create_purchase_order.contains_text(CREATE_PO_SEARCH_SCREEN),("TC: " + tc + ". Text not found : " +  CREATE_PO_SEARCH_SCREEN + " - Po Screen "))
    ie_create_purchase_order.button(:index, CREATE_PO_SEARCH_BTN).click # to see the created indent for to submit
    row_loop(tc, test_data, attributes,ie_create_purchase_order)    
    ie_create_purchase_order.button(:index, CREATE_PO_SUBMIT_BTN).click# to select the created indent for to submit and to approve

end

    populate_create_purchase_order_details(tc, test_data, attributes, ie_create_purchase_order)
    ie_create_purchase_order.button(:class, CREATE_PO_SAVE_BUTTON).click 
    
    assert_true(ie_create_purchase_order.button(:value, STORES_PO_APPROVE_PRINT_BUTTON).exists?, "PO not Created" )

    # To capture the po number created
    test_data[CREATE_PO_NUM_FLD] = ie_create_purchase_order.text_field(:id,CREATE_PO_NUMBER_FLD).value

     puts "PO number created is "  +  test_data[CREATE_PO_NUM_FLD]
     
    ensure
      ie_create_purchase_order.close

end


def row_loop(tc, test_data, attributes,ie_create_purchase_order)

    i =0 
    ie_create_purchase_order.table(:id, CREATE_PO_INDENT_TABLE).each {|row|
    if row.link(:text,/#{test_data[INDENT_NUMBER_FLD]}/).exists?
    puts i
    row.checkbox(:id,CREATE_PO_INDENT_CHECK_BOX).set
    else
      i=i+1
    end
    }

end
  

def create_purchase_order_failure(tc, test_data, attributes, action)
  
    if action == "po non indent"
   
    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, STORES_CREATE_PO_NON_INDENT_LINK).click
    ie_create_purchase_order=Watir::IE.attach(:url,/GeneralPurchaseOrderData*/)
    
end
  
if action == "po indent"

    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, STORES_CREATE_PO_INDENT_LINK).click
    ie_create_purchase_order=Watir::IE.attach(:url,/PurchaseOrderCentral*/)
    
    assert_not_nil(ie_create_purchase_order.contains_text(CREATE_PO_SEARCH_SCREEN),("TC: " + tc + ". Text not found : " +  CREATE_PO_SEARCH_SCREEN + " - Po Screen "))
    ie_create_purchase_order.button(:index, CREATE_PO_SEARCH_BTN).click                    # to see the created indent for to submit
    row_loop(tc, test_data, attributes,ie_create_purchase_order)    
    ie_create_purchase_order.button(:index, CREATE_PO_SUBMIT_BTN).click                   # to select the created indent for to submit and to approve

end

    populate_create_purchase_order_details(tc, test_data, attributes, ie_create_purchase_order)
    ie_create_purchase_order.button(:class, CREATE_PO_SAVE_BUTTON).click 
    assert_false(ie_create_purchase_order.button(:value, STORES_PO_APPROVE_PRINT_BUTTON).exists?, "PO Created" )
    
    ensure
    ie_create_purchase_order.close

end




def populate_create_purchase_order_details(tc, test_data, attributes, ie_create_purchase_order)
  
    if test_data[PO_NON_INDENT] == '1'
    assert(ie_create_purchase_order.contains_text(CREATE_PO_NON_INDENT_SCREEN),("TC: " + tc + ". Text not found : " + CREATE_PO_NON_INDENT_SCREEN  + " - Not in PO create screen"))
    end
      
        
    if test_data[PO_INDENT] == '1'
    assert(ie_create_purchase_order.contains_text(CREATE_PO_SCREEN_INDENT),("TC: " + tc + ". Text not found : " + CREATE_PO_SCREEN_INDENT  + " - Not in PO create screen"))
    end

    if test_data[CREATE_PO_NON_INDENT_STORE_FLD] && attributes[CREATE_PO_NON_INDENT_STORE_FLD] !=nil
      ie_create_purchase_order.select_list(:id,attributes[CREATE_PO_NON_INDENT_STORE_FLD]).set test_data[CREATE_PO_NON_INDENT_STORE_FLD]
    end


    ie_create_purchase_order.text_field(:id, CREATE_PO_SUPPLIER_IMG).click
      
    ie_create_purchase_order_other_details = Watir::IE.attach(:title, /SUPPLIER/)
    Watir::Waiter.wait_until{ie_create_purchase_order_other_details.contains_text("Supplier Search")}

   

   if test_data[CREATE_PO_SUPPLIER_NAME_FLD] && attributes[CREATE_PO_SUPPLIER_NAME_FLD] !=nil
      ie_create_purchase_order_other_details.text_field(:id,attributes[CREATE_PO_SUPPLIER_NAME_FLD]).set test_data[CREATE_PO_SUPPLIER_NAME_FLD]
    end
    
      ie_create_purchase_order_other_details.button(:id, STORES_SEARCH_SUPP_BUTTON).click
    
    
    ie_create_purchase_order_other_details.div(:class,'yui-dt-col-attributeName yui-dt-col-1 yui-dt-liner yui-dt-sortable yui-dt-resizeable').click
    
     
    #~ ie_create_purchase_order_other_details = Watir::IE.attach(:title, /Search screen/)
    #~ Watir::Waiter.wait_until{ie_create_purchase_order_other_details.contains_text("Search screen")}

    #~ ie_create_purchase_order_other_details.div(:class,'yui-dt-col-attributeId yui-dt-col-0 yui-dt-liner yui-dt-sortable yui-dt-resizeable').click

    if test_data[CREATE_PO_ORDER_DATE_FLD] && attributes[CREATE_PO_ORDER_DATE_FLD] !=nil
      ie_create_purchase_order.text_field(:id,attributes[CREATE_PO_ORDER_DATE_FLD]).set test_data[CREATE_PO_ORDER_DATE_FLD]
    end

    if test_data[CREATE_PO_DELIVERY_DATE_FLD] && attributes[CREATE_PO_DELIVERY_DATE_FLD] !=nil
      ie_create_purchase_order.text_field(:id,attributes[CREATE_PO_DELIVERY_DATE_FLD]).set test_data[CREATE_PO_DELIVERY_DATE_FLD]
    end


##########################     To enter the material detials for po indent type    #####################################

  if test_data[PO_INDENT_MATERIAL_DETAILS] == '1'            

  loop = test_data[FOR_LOOP_PO_MATERIAL_UNIT_PRICE].to_i

      for i in 1...loop+1               #For loop to add price for multiple line items

          if test_data[CREATE_PO_UNIT_PRICE_FLD+i.to_s] && attributes[CREATE_PO_UNIT_PRICE_FLD+i.to_s] !=nil
            ie_create_purchase_order.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+1][8].text_field(:id,attributes[CREATE_PO_UNIT_PRICE_FLD+i.to_s]).set test_data[CREATE_PO_UNIT_PRICE_FLD+i.to_s]
          end
      end

  end


###############################################################################################

################################    To enter the material detials for po no indent type    #################################

  if test_data[PO_NON_INDENT_MATERIAL_DETAILS] == '1'             
    
    loop = test_data[FOR_LOOP_PO_MATERIAL_DETAILS].to_i

          for i in 1...loop+1            #For loop to add multiple line items

              if test_data[CREATE_PO_NON_INDENT_ITEM_FLD+i.to_s] && attributes[CREATE_PO_NON_INDENT_ITEM_FLD+i.to_s] !=nil
                ie_create_purchase_order.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+1][2].text_field(:id,attributes[CREATE_PO_NON_INDENT_ITEM_FLD+i.to_s]).set test_data[CREATE_PO_NON_INDENT_ITEM_FLD+i.to_s]
              end

              if test_data[CREATE_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s] && attributes[CREATE_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s] !=nil
                ie_create_purchase_order.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+1][5].text_field(:id,attributes[CREATE_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s]).set test_data[CREATE_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s]
              end

              if test_data[CREATE_PO_UNIT_PRICE_FLD+i.to_s] && attributes[CREATE_PO_UNIT_PRICE_FLD+i.to_s] !=nil
                ie_create_purchase_order.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+1][6].text_field(:id,attributes[CREATE_PO_UNIT_PRICE_FLD+i.to_s]).set test_data[CREATE_PO_UNIT_PRICE_FLD+i.to_s]
              end

              if i<test_data[FOR_LOOP_PO_MATERIAL_DETAILS].to_i
                ie_create_purchase_order.image(:id,CREATE_PO_ADD_MATERIAL_BTN).flash
                ie_create_purchase_order.image(:id,CREATE_PO_ADD_MATERIAL_BTN).click
              end

          end

puts test_data[STORES_CREATE_PO_TAX_PRICE_FLD]
puts attributes[STORES_CREATE_PO_TAX_PRICE_FLD]
  #~ if test_data[STORES_CREATE_PO_TAX_PRICE_FLD] && attributes[STORES_CREATE_PO_TAX_PRICE_FLD] !=nil
            #~ ie_create_purchase_order.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[2][9].text_field(:id,attributes[STORES_CREATE_PO_TAX_PRICE_FLD]).set test_data[STORES_CREATE_PO_TAX_PRICE_FLD]
          #~ end


  
if test_data[STORES_CREATE_MI_DEGIGNATION_FLD] && attributes[STORES_CREATE_MI_DEGIGNATION_FLD]!=nil
      ie_create_purchase_order.select_list(:id,attributes[STORES_CREATE_MI_DEGIGNATION_FLD]).set test_data[STORES_CREATE_MI_DEGIGNATION_FLD]
    end
    
    
       if test_data[STORES_CREATE_MI_APPROVER_FLD] && attributes[STORES_CREATE_MI_APPROVER_FLD]!=nil
      ie_create_purchase_order.select_list(:id,attributes[STORES_CREATE_MI_APPROVER_FLD]).set test_data[STORES_CREATE_MI_APPROVER_FLD]
    end
    
     end
  ###########################################################################################
  
end  # END OF POPULATE CREATE PURCHASE ORDER METHOD
 
 
 
 def for_loop_indent()
 
  login_to_stores(STORES_USER_NAME, STORES_PASSWORD) #  USER INBOX

  create_item(tc, test_data, attributes)
  create_material_indent_success(tc, test_data, attributes)
  approve_material_indent(tc, test_data, attributes, "Drafts")

  logout_stores

  login_to_stores(STORES_USER_NAME1, STORES_PASSWORD1)   # MANAGER INBOX
  approve_material_indent(tc, test_data, attributes, "Inbox")
  logout_stores 
 
 end
 

def view_purchase_order(tc, test_data, attributes)

    #~ $browser.cell(:xpath, "//[contains(text(),'Transactions')]").click
    $browser.dt(:text,'Transactions').click
    $browser.link(:text, STORES_VIEW_PO_INDENT_AND_NON_INDENT_LINK).click
    ie_view_purchase_order=Watir::IE.attach(:url,/searchView*/)
    
    assert_not_nil(ie_view_purchase_order.contains_text(STORES_VIEW_PO_SEARCH_SCREEN),("TC: " + tc + ". Text not found : " +  STORES_VIEW_PO_SEARCH_SCREEN + " - NOT IN VIEW PO SEARCH SCREEN "))
    
    ie_view_purchase_order.button(:value, STORES_VIEW_PO_SEARCH_BUTTON).click
    
    ie_view_purchase_order.link(:text, test_data[CREATE_PO_NUM_FLD]).click
  
    assert_not_nil(ie_view_purchase_order.contains_text(STORES_VIEW_PO_SUCCESS_MESG),("TC: " + tc + ". Text not found : " +  STORES_VIEW_PO_SUCCESS_MESG + " - NOT IN VIEW PO SCREEN"))
    assert(ie_view_purchase_order.text_field(:id,CREATE_PO_NUMBER_FLD).verify_contains(test_data[CREATE_PO_NUM_FLD]) )
    
  ensure
  ie_view_purchase_order.close

end


def approve_reject_purchase_order(tc, test_data, attributes, action, actions)
  
    ie_approve_reject_purchase_order = start_browser(STORES_APPROVE_REJECT_PO_URL)
      
    assert_not_nil(ie_approve_reject_purchase_order.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "Drafts"
      #~ ie_approve_reject_purchase_order.cell(:xpath, "//[contains(text(),'Drafts')]").click   
      ie_approve_reject_purchase_order.dt(:text,'Drafts').click
      assert(ie_approve_reject_purchase_order.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_approve_reject_purchase_order.cell(:xpath, "//[contains(text(),'Inbox')]").click
       ie_approve_reject_purchase_order.dt(:text,'Inbox').click
      assert(ie_approve_reject_purchase_order.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      ie_approve_reject_purchase_order.frame(INBOX_FRAME).cell(:text, /#{test_data[CREATE_PO_NUM_FLD]}/).click
      
      if test_data[APPROVE_PO_NON_INDENT] == '1'
      ie_approve_reject_purchase_order_note = Watir::IE.attach(:title, /eGov Stores - Purchase Order General/) # To attach the po modify screen for po non indent
      end
    
      if test_data[APPROVE_PO_INDENT] == '1'
      ie_approve_reject_purchase_order_note = Watir::IE.attach(:title, /eGov Stores - Purchase Order/) # To attach the po modify screen for po indent
      end
    
      assert(ie_approve_reject_purchase_order_note.text_field(:id,CREATE_PO_NUMBER_FLD).verify_contains(test_data[CREATE_PO_NUM_FLD]) )
      
   
    if test_data[STORES_PO_REVISED_NOTES_FLD] && attributes[STORES_PO_REVISED_NOTES_FLD] != nil
      ie_approve_reject_purchase_order_note.text_field(:id, attributes[STORES_PO_REVISED_NOTES_FLD]).set test_data[STORES_PO_REVISED_NOTES_FLD]
    end
    
    
    
      if( ie_approve_reject_purchase_order_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_approve_reject_purchase_order_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
       if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
      ie_approve_reject_purchase_order_note.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end
    
    if actions == "Approve"
          ie_approve_reject_purchase_order_note.button(:value, STORES_PO_APPROVE_BUTTON).click
          # After approval of po confriming it through print button displayed on the screen
          assert(ie_approve_reject_purchase_order_note.button(:value, STORES_PO_APPROVE_PRINT_BUTTON).exists?, "PO not Approved" )  
           puts "PO APPROVED"
    end

    if actions == "Reject"
          ie_approve_reject_purchase_order_note.button(:value, STORES_PO_REJECT_BUTTON).click
          # After rejecting the po confriming it through print button displayed on the screen
          assert(ie_approve_reject_purchase_order_note.button(:value, STORES_PO_APPROVE_PRINT_BUTTON).exists?, "PO not Rejected" )  
       puts "PO REJECTED"
    end
  
  
    ie_approve_reject_purchase_order_note.button(:value, STORES_PO_CLOSE_BUTTON).click
      
  ensure
    ie_approve_reject_purchase_order.close

end #END OF APPROVE & REJECT METHOD



def modify_purchase_order_success(tc, test_data, attributes, action, actions)


    ie_modify_purchase_order = start_browser(STORES_MODIFY_PO_URL)
      
    assert_not_nil(ie_modify_purchase_order.contains_text(INBOX_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + INBOX_SUCCESS_MESG+ ". Not able to open Inbox."))
    
    if action == "Drafts"
      #~ ie_modify_purchase_order.cell(:xpath, "//[contains(text(),'Drafts')]").click   
       ie_modify_purchase_order.dt(:text,'Drafts').click
      assert(ie_modify_purchase_order.contains_text(STORES_INBOX_DRAFTS_FOLDER), "Not in Drafts Folder")
    end
    
    if action == "Inbox"
      #~ ie_modify_purchase_order.cell(:xpath, "//[contains(text(),'Inbox')]").click
       ie_modify_purchase_order.dt(:text,'Inbox').click
      assert(ie_modify_purchase_order.contains_text(STORES_INBOX_WORKS_FOLDER), "Not in Inbox Folder")
    end
    
      ie_modify_purchase_order.frame(INBOX_FRAME).cell(:text, /#{test_data[CREATE_PO_NUM_FLD]}/).click
      
      if test_data[APPROVE_PO_NON_INDENT] == '1'
      ie_modify_purchase_order_note = Watir::IE.attach(:title, /eGov Stores - Purchase Order General/) # To attach the po modify screen for po non indent
      end
    
      if test_data[APPROVE_PO_INDENT] == '1'
      ie_modify_purchase_order_note = Watir::IE.attach(:title, /eGov Stores - Purchase Order/) # To attach the po modify screen for po indent
      end
    
    assert(ie_modify_purchase_order_note.text_field(:id,CREATE_PO_NUMBER_FLD).verify_contains(test_data[CREATE_PO_NUM_FLD]) )
      
    populate_modify_purchase_order_details(tc, test_data, attributes, action, actions, ie_modify_purchase_order_note)
      
  ensure
    ie_modify_purchase_order.close
  
end # END OF MODIFY PURCHASE  ORDER METHOD



def populate_modify_purchase_order_details(tc, test_data, attributes, action, actions, ie_modify_purchase_order_note)

if test_data[MODIFY_PO_NON_INDENT_STORE_FLD] && attributes[MODIFY_PO_NON_INDENT_STORE_FLD] !=nil
  ie_modify_purchase_order_note.select_list(:id,attributes[MODIFY_PO_NON_INDENT_STORE_FLD]).set test_data[MODIFY_PO_NON_INDENT_STORE_FLD]
end


ie_modify_purchase_order_note.text_field(:id, MODIFY_PO_SUPPLIER_IMG).click
 
ie_create_purchase_order_other_details = Watir::IE.attach(:title, /Search screen/)
Watir::Waiter.wait_until{ie_create_purchase_order_other_details.contains_text("Search screen")}

ie_create_purchase_order_other_details.div(:class,'yui-dt-col-attributeId yui-dt-col-0 yui-dt-liner yui-dt-sortable yui-dt-resizeable').click

if test_data[MODIFY_PO_ORDER_DATE_FLD] && attributes[MODIFY_PO_ORDER_DATE_FLD] !=nil
  ie_modify_purchase_order_note.text_field(:id,attributes[MODIFY_PO_ORDER_DATE_FLD]).set test_data[MODIFY_PO_ORDER_DATE_FLD]
end

if test_data[MODIFY_PO_DELIVERY_DATE_FLD] && attributes[MODIFY_PO_DELIVERY_DATE_FLD] !=nil
  ie_modify_purchase_order_note.text_field(:id,attributes[MODIFY_PO_DELIVERY_DATE_FLD]).set test_data[MODIFY_PO_DELIVERY_DATE_FLD]
end


rowcount = ie_modify_purchase_order_note.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE).row_count
puts rowcount


###############################    TO DELETE ALREADY EXISITNG LINE ITEMS    ##############################

  if test_data[MODIFY_PO_DELETE_LINE_ITEMS] == "1"             

  loop = test_data[MODIFY_FOR_LOOP_PO_DELETE_LINE_ITEMS].to_i

      for i in 1...loop+1

          ie_modify_purchase_order_note.image(:id,CREATE_PO_DELETE_MATERIAL_BTN).flash
          ie_modify_purchase_order_note.image(:id,CREATE_PO_DELETE_MATERIAL_BTN).click

      end  

  end

###########################################################################################

##########################   TO ADD UNIT PRICE FOR MULTIPLE LINE ITEMS  ################################

  if test_data[MODIFY_PO_INDENT_MATERIAL_DETAILS] == '1' # TO enter the material detials for po indent type  

  loop = test_data[MODIFY_FOR_LOOP_PO_MATERIAL_UNIT_PRICE].to_i

      for i in 1...loop+1 #For loop to add price for multiple line items

          if test_data[MODIFY_PO_UNIT_PRICE_FLD+i.to_s] && attributes[MODIFY_PO_UNIT_PRICE_FLD+i.to_s] !=nil
            ie_modify_purchase_order_note.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+rowcount][9].text_field(:id,attributes[MODIFY_PO_UNIT_PRICE_FLD+i.to_s]).set test_data[MODIFY_PO_UNIT_PRICE_FLD+i.to_s]
          end

      end

  end

##############################################################################################

#################################   TO ADD MULTIPLE LINE ITEMS   ########################################

  if test_data[MODIFY_PO_NON_INDENT_MATERIAL_DETAILS] == '1' # TO enter the material detials for po no indent type  
    
    loop = test_data[MODIFY_FOR_LOOP_PO_MATERIAL_DETAILS].to_i

          for i in 1...loop+1 #For loop to add multiple line items
            
                ie_modify_purchase_order_note.image(:id,CREATE_PO_ADD_MATERIAL_BTN).flash
                ie_modify_purchase_order_note.image(:id,CREATE_PO_ADD_MATERIAL_BTN).click

                if test_data[MODIFY_PO_NON_INDENT_ITEM_FLD+i.to_s] && attributes[MODIFY_PO_NON_INDENT_ITEM_FLD+i.to_s] !=nil
                  ie_modify_purchase_order_note.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+rowcount][2].text_field(:id,attributes[MODIFY_PO_NON_INDENT_ITEM_FLD+i.to_s]).set test_data[MODIFY_PO_NON_INDENT_ITEM_FLD+i.to_s]
                end

                if test_data[MODIFY_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s] && attributes[MODIFY_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s] !=nil
                  ie_modify_purchase_order_note.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+rowcount][6].text_field(:id,attributes[MODIFY_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s]).set test_data[MODIFY_PO_NON_INDENT_ORDERED_QUANTITY_FLD+i.to_s]
                end

                if test_data[MODIFY_PO_UNIT_PRICE_FLD+i.to_s] && attributes[MODIFY_PO_UNIT_PRICE_FLD+i.to_s] !=nil
                  ie_modify_purchase_order_note.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE)[i+rowcount][7].text_field(:id,attributes[MODIFY_PO_UNIT_PRICE_FLD+i.to_s]).set test_data[MODIFY_PO_UNIT_PRICE_FLD+i.to_s]
                end

          end
      
      rowcount1 = ie_modify_purchase_order_note.table(:id, CREATE_PO_MATERIAL_DETAILS_TABLE).row_count
      puts rowcount1

  end
############################################################################################


if test_data[STORES_PO_REVISED_NOTES_FLD] && attributes[STORES_PO_REVISED_NOTES_FLD] != nil
      ie_modify_purchase_order_note.text_field(:id, attributes[STORES_PO_REVISED_NOTES_FLD]).set test_data[STORES_PO_REVISED_NOTES_FLD]
    end
    
     
    if( ie_modify_purchase_order_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).exists?)
    
     if test_data[STORES_APPROVE_MI_DESIGNATION_FLD] && attributes[STORES_APPROVE_MI_DESIGNATION_FLD]!=nil
      ie_modify_purchase_order_note.select_list(:id,attributes[STORES_APPROVE_MI_DESIGNATION_FLD]).set test_data[STORES_APPROVE_MI_DESIGNATION_FLD]
     end
    
       if test_data[STORES_APPROVE_MI_APPROVER_FLD] && attributes[STORES_APPROVE_MI_APPROVER_FLD]!=nil
      ie_modify_purchase_order_note.select_list(:id,attributes[STORES_APPROVE_MI_APPROVER_FLD]).set test_data[STORES_APPROVE_MI_APPROVER_FLD]
    end
    
    end
    

 if actions == "Approve"
          ie_modify_purchase_order_note.button(:value, STORES_PO_APPROVE_BUTTON).click
          # After approval of po confriming it through print button displayed on the screen
          assert(ie_modify_purchase_order_note.button(:value, STORES_PO_APPROVE_PRINT_BUTTON).exists?, "PO not Approved" )  
           puts "PO MODIFIED & APPROVED "
    end

    if actions == "Reject"
          ie_modify_purchase_order_note.button(:value, STORES_PO_REJECT_BUTTON).click
          # After rejecting the po confriming it through print button displayed on the screen
          assert(ie_modify_purchase_order_note.button(:value, STORES_PO_APPROVE_PRINT_BUTTON).exists?, "PO not Rejected" )  
       puts "PO REJECTED"
    end
  
      ie_modify_purchase_order_note.button(:value, STORES_PO_CLOSE_BUTTON).click

end # END OF POPULATE MODIFY DETAILS METHOD


end # END OF PO MODULE