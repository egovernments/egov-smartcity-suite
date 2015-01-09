
require path_to_file("stores_constants")

#~ require 'stores_constants.rb'

module Stores_stores_methods

include Stores_constants
include Test::Unit::Assertions
  
def create_stores(tc, test_data, attributes)

#~ ie_create_stores = start_browser(CREATE_STORES_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, CREATE_STORES_LINK).click
  ie_create_stores=Watir::IE.attach(:url,/jsp*/)
  
assert(ie_create_stores.text_field(:id, STORES_FLD).exists?,("TC: " + tc + ". Could not find text: " + CREATE_STORES_SCREEN + " - Create Stores screen")) 
populate_create_stores_details(tc, test_data, attributes, ie_create_stores)
ie_create_stores.button(:class, CREATE_STORES_BTN).click
ensure
ie_create_stores.close

end


def populate_create_stores_details(tc, test_data, attributes, ie_create_stores)

  ie_create_stores.button(:name, STORES_NEW_BTN).click
  #~ ie_create_stores.button(:name, STORES_NEW_BTN).fire_event("onclick")
  
  #~ puts ie_create_stores.text
  assert_not_nil(ie_create_stores.contains_text(CREATE_STORES_SCREEN),("TC: " + tc + ". Could not find text: " + CREATE_STORES_SCREEN + " - Create Stores screen"))


  if test_data[CREATE_STORE_NAME_FLD] && attributes[CREATE_STORE_NAME_FLD]!=nil
  ie_create_stores.text_field(:name,attributes[CREATE_STORE_NAME_FLD]).set test_data[CREATE_STORE_NAME_FLD]
  end

  if test_data[CREATE_STORE_NO_FLD] && attributes[CREATE_STORE_NO_FLD]!=nil
  ie_create_stores.text_field(:name,attributes[CREATE_STORE_NO_FLD]).set test_data[CREATE_STORE_NO_FLD]
  end

  if test_data[CREATE_STORE_DESC_FLD] && attributes[CREATE_STORE_DESC_FLD]!=nil
  ie_create_stores.text_field(:name,attributes[CREATE_STORE_DESC_FLD]).set test_data[CREATE_STORE_DESC_FLD]
  end

  if test_data[CREATE_DEPT_NAME_FLD] && attributes[CREATE_DEPT_NAME_FLD]!=nil
  ie_create_stores.select_list(:name,attributes[CREATE_DEPT_NAME_FLD]).set test_data[CREATE_DEPT_NAME_FLD]
  end

  if test_data[CREATE_DELIVERY_ADDRESS_FLD] && attributes[CREATE_DELIVERY_ADDRESS_FLD]!=nil
  ie_create_stores.text_field(:name,attributes[CREATE_DELIVERY_ADDRESS_FLD]).set test_data[CREATE_DELIVERY_ADDRESS_FLD]
  end

  if test_data[CREATE_BILLING_ADDRESS_FLD] && attributes[CREATE_BILLING_ADDRESS_FLD]!=nil
  ie_create_stores.text_field(:name,attributes[CREATE_BILLING_ADDRESS_FLD]).set test_data[CREATE_BILLING_ADDRESS_FLD]
  end

if test_data[CREATE_BIN]=='1'
  
  ie_create_stores.radio(:name, RADIO_BTN).set
  
  loop = test_data[FOR_LOOP_STORES].to_i

 for i in 1...loop+1# For loop to add multiple bins to a store
 

  if test_data[CREATE_BIN_CODE_FLD+i.to_s] && attributes[CREATE_BIN_NAME_FLD+i.to_s]!=nil
  ie_create_stores.table(:id, CREATE_BIN_TABLE)[i+1][1].text_field(:id,attributes[CREATE_BIN_CODE_FLD+i.to_s]).set test_data[CREATE_BIN_CODE_FLD+i.to_s]
  end

  if test_data[CREATE_BIN_NAME_FLD+i.to_s] && attributes[CREATE_BIN_NAME_FLD+i.to_s]!=nil
  ie_create_stores.table(:id, CREATE_BIN_TABLE)[i+1][2].text_field(:id,attributes[CREATE_BIN_NAME_FLD+i.to_s]).set test_data[CREATE_BIN_NAME_FLD+i.to_s]
  end

  if test_data[CREATE_BIN_STATUS_FLD+i.to_s] && attributes[CREATE_BIN_STATUS_FLD+i.to_s]!=nil
  ie_create_stores.table(:id, CREATE_BIN_TABLE)[i+1][3].select_list(:id,attributes[CREATE_BIN_STATUS_FLD+i.to_s]).set test_data[CREATE_BIN_STATUS_FLD+i.to_s]
  end

  if i <test_data[FOR_LOOP_STORES].to_i
  ie_create_stores.image(:src,/addrow.gif/).flash   #CREATE_INDENT_ADD_IMG
  ie_create_stores.image(:src,/addrow.gif/).click    #CREATE_INDENT_ADD_IMG
  end

 end #End of for loop

end

end #End of populate method

def view_stores_success(tc, test_data, attributes)

  #~ ie_view_stores  = start_browser(VIEW_STORES_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_STORES_LINK).click
  ie_view_stores=Watir::IE.attach(:url,/jsp*/)
  
  assert(ie_view_stores.text_field(:id, STORES_FLD).exists?,("TC: " + tc + ". Could not find text: " + VIEW_STORES_SCREEN + " -  View Stores screen")) 
  
  if test_data[VIEW_STORES_NUM_FLD] && attributes[VIEW_STORES_NUM_FLD]!= nil
  assert_true(ie_view_stores.select_list(:id,attributes[VIEW_STORES_NUM_FLD]).includes?(test_data[VIEW_STORES_NUM_FLD]))  
  ie_view_stores.select_list(:name,attributes[VIEW_STORES_NUM_FLD]).set test_data[VIEW_STORES_NUM_FLD]
  end  

  ie_view_stores.button(:name, VIEW_STORES_BTN).click

  assert_not_nil(ie_view_stores.contains_text(VIEW_STORES_SCREEN),("TC: " + tc + ". Could not find text: " + VIEW_STORES_SCREEN + " - View Stores screen"))

  if test_data[CREATE_STORES_SUCCESS]=='1'
  #~ assert(ie_view_stores.text_field(:name, attributes[CREATE_STORE_NO_FLD]).readonly? )
  assert(ie_view_stores.text_field(:name, attributes[CREATE_STORE_NO_FLD]).verify_contains(test_data[CREATE_STORE_NO_FLD]))
  end

  if test_data[MODIFY_STORES_SUCCESS]=='1'
  #~ assert(ie_view_stores.text_field(:name, attributes[MODIFY_DEPT_NAME_FLD]).readonly? )
  assert(ie_view_stores.text_field(:name, attributes[MODIFY_DEPT_NAME_FLD]).verify_contains(test_data[MODIFY_DEPT_NAME_FLD]) )
  end

ensure
ie_view_stores.close

end

def view_stores_failure(tc, test_data, attributes)

  #~ ie_view_stores  = start_browser(VIEW_STORES_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_STORES_LINK).click
  ie_view_stores=Watir::IE.attach(:url,/jsp*/)
  
  assert(ie_view_stores.text_field(:id, STORES_FLD).exists?,("TC: " + tc + ". Could not find text: " + VIEW_STORES_SCREEN + " -  View Stores screen")) 

  if test_data[CREATE_STORES_FAILURE]=='1'
  assert_not_nil(ie_view_stores.select_list(:id,attributes[VIEW_STORES_NUM_FLD]).includes?(test_data[VIEW_STORES_NUM_FLD]))
  end

  if test_data[MODIFY_STORES_FAILURE]=='1'
  assert_not_nil(ie_view_stores.select_list(:id,attributes[VIEW_STORES_NUM_FLD]).includes?(test_data[VIEW_STORES_NUM_FLD]))
  end

  ensure
  ie_view_stores.close

end


def modify_stores(tc, test_data, attributes)

  #~ ie_modify_stores = start_browser(MODIFY_STORES_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, MODIFY_STORES_LINK).click
  ie_modify_stores=Watir::IE.attach(:url,/jsp*/)
  
  assert(ie_modify_stores.text_field(:id, STORES_FLD).exists?,("TC: " + tc + ". Could not find text: " + MODIFY_STORES_SCREEN + " - ModifY Stores Screen")) 
  #~ assert_not_nil(ie_modify_stores.contains_text(MODIFY_STORES_SCREEN),("TC: " + tc + ". Text found : " + MODIFY_STORES_SCREEN  + " - ModifY Stores Screen "))
  populate_modify_stores_details(tc,test_data, attributes, ie_modify_stores)
  ie_modify_stores.button(:id, MODIFY_STORES_BTTN).click
  ensure
  ie_modify_stores.close

end


def populate_modify_stores_details(tc,test_data, attributes, ie_modify_stores)

  if test_data[MODIFY_STORES_NUM_FLD] && attributes[MODIFY_STORES_NUM_FLD]!= nil
  assert_true(ie_modify_stores.select_list(:name, attributes[MODIFY_STORES_NUM_FLD]).includes?([MODIFY_STORES_NUM_FLD]) )  
  ie_modify_stores.select_list(:name, attributes[MODIFY_STORES_NUM_FLD]).set test_data[MODIFY_STORES_NUM_FLD]
  end

  ie_modify_stores.button(:name,MODIFY_STORES_BTN).click
  assert_not_nil(ie_modify_stores.contains_text(MODIFY_STORES_SCREEN),("TC: " + tc + ". Text found : " + MODIFY_STORES_SCREEN  + " - ModifY Stores Screen "))

  if test_data[MODIFY_STORE_NAME_FLD] && attributes[MODIFY_STORE_NAME_FLD]!= nil
  ie_modify_stores.text_field(:id, attributes[MODIFY_STORE_NAME_FLD]).set test_data[MODIFY_STORE_NAME_FLD]
  end

  if test_data[MODIFY_STORE_DESC_FLD] && attributes[MODIFY_STORE_DESC_FLD]!= nil
  ie_modify_stores.text_field(:id, attributes[MODIFY_STORE_DESC_FLD]).set test_data[MODIFY_STORE_DESC_FLD]
  end

  ie_modify_stores.select_list(:id, attributes[MODIFY_DEPT_NAME_FLD]).flash

  if test_data[MODIFY_DEPT_NAME_FLD] && attributes[MODIFY_DEPT_NAME_FLD]!=nil
  ie_modify_stores.select_list(:id, attributes[MODIFY_DEPT_NAME_FLD]).select test_data[MODIFY_DEPT_NAME_FLD]
  end

  if test_data[MODIFY_DELIVERY_ADDRESS_FLD] && attributes[MODIFY_DELIVERY_ADDRESS_FLD]!=nil
  ie_modify_stores.text_field(:id, attributes[MODIFY_DELIVERY_ADDRESS_FLD]).set test_data[MODIFY_DELIVERY_ADDRESS_FLD]
  end

  if test_data[MODIFY_BILLING_ADDRESS_FLD] && attributes[MODIFY_BILLING_ADDRESS_FLD]!=nil
  ie_modify_stores.text_field(:id,attributes[MODIFY_BILLING_ADDRESS_FLD]).set test_data[MODIFY_BILLING_ADDRESS_FLD]
  end

end

    
#~ def check_for_modify_stores_success(tc,test_data, attributes, ie_modify_stores)    

  #~ assert_not_nil(ie_modify_stores.contains_text(MODIFY_STORES_SUCCESS_MESG),("TC: " + tc + ". Text found : " + MODIFY_STORES_SUCCESS_MESG  + " - Stores Modified "))

#~ ensure
  #~ ie_modify_stores.close
#~ end

 
#~ def check_for_modify_stores_failure(tc,test_data, attributes, ie_modify_stores)

 #~ assert_not_nil(ie_modify_stores.contains_text(MODIFY_STORES_SUCCESS_MESG),("TC: " + tc + ". Text found : " + MODIFY_STORES_SUCCESS_MESG  + " - Stores Modified "))

#~ ensure
 #~ ie_modify_stores.close 
#~ end    


end#Module end
