
require path_to_file("stores_constants.rb") 

module Stores_item_methods

include Stores_constants
include Test::Unit::Assertions

#~ *******************************************************************18/08/2009/*****************************************************************************************************************************************
def create_item(tc, test_data, attributes)
  
  #~ ie_create_item = start_browser(CREATE_ITEM_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text,CREATE_MATERIAL_LINK).click
  ie_create_item=Watir::IE.attach(:url,/new*/)
  ie_create_item.maximize()
  
  
  assert_not_nil(ie_create_item.contains_text(CREATE_ITEM_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_ITEM_SCREEN + ". Create item screen."))
  populate_create_item_details(tc, test_data, attributes, ie_create_item)
  ie_create_item.button(:value, CREATE_ITEM_BUTTON).click
  
  ensure
  ie_create_item.close

end


def populate_create_item_details(tc, test_data, attributes,ie_create_item)
  
  if test_data[CREATE_ITEM_NO] && attributes[CREATE_ITEM_NO]!=nil
  ie_create_item.text_field(:name,attributes[CREATE_ITEM_NO]).set test_data[CREATE_ITEM_NO]
  end

  if test_data[CREATE_ITEM_TYPE] && attributes[CREATE_ITEM_TYPE]!=nil
  ie_create_item.select_list(:name,attributes[CREATE_ITEM_TYPE]).set test_data[CREATE_ITEM_TYPE]
  end

  if test_data[CREATE_ITEM_DESCRIPTION] && attributes[CREATE_ITEM_DESCRIPTION]!=nil
  ie_create_item.text_field(:name,attributes[CREATE_ITEM_DESCRIPTION]).set test_data[CREATE_ITEM_DESCRIPTION]    
  end

  if test_data[CREATE_BASE_UOM] && attributes[CREATE_BASE_UOM]!=nil
  ie_create_item.select_list(:id,attributes[CREATE_BASE_UOM]).set test_data[CREATE_BASE_UOM]
  end

  if test_data[CREATE_ITEM_STATUS] && attributes[CREATE_ITEM_STATUS]!=nil
  ie_create_item.select_list(:name,attributes[CREATE_ITEM_STATUS]).set test_data[CREATE_ITEM_STATUS]
  end

  if test_data[CREATE_ITEM_INDENT_REQUIRED_CHECK_BOX] == "1"
  ie_create_item.checkbox(:id, attributes[CREATE_ITEM_INDENT_REQUIRED_CHECK_BOX]).clear 
  end

  if test_data[CREATE_ITEM_SCRAP_ITEM] == ("y"|| "Y")  
  ie_create_item.checkbox(:name, attributes[CREATE_ITEM_SCRAP_ITEM]).set test_data[CREATE_ITEM_SCRAP_ITEM] 
  end 

  if test_data[CREATE_ITEM_PURCHASED]  == ("y"|| "Y")
  ie_create_item.checkbox(:name,attributes[CREATE_ITEM_PURCHASED]).set test_data[CREATE_ITEM_PURCHASED]
  end

  if test_data[CREATE_CONTRACT_REQD] && attributes[CREATE_CONTRACT_REQD]!=nil
  ie_create_item.select_list(:name, attributes[CREATE_CONTRACT_REQD]).set test_data[CREATE_CONTRACT_REQD]
  end

  if test_data[CREATE_ITEM_LOT_CONTROL] == ("y"|| "Y")
  ie_create_item.checkbox(:name,attributes[CREATE_ITEM_LOT_CONTROL]).set test_data[CREATE_ITEM_LOT_CONTROL] 
  end

  if test_data[CREATE_ITEM_SHELF_LIFE_CONTROL] == ("y"|| "Y")
  ie_create_item.checkbox(:name,attributes[CREATE_ITEM_SHELF_LIFE_CONTROL]).set test_data[CREATE_ITEM_SHELF_LIFE_CONTROL] 
  end

  if test_data[CREATE_ITEM_BIN_LEVEL_STORAGE] == ("y"|| "Y")
  ie_create_item.checkbox(:name,attributes[CREATE_ITEM_BIN_LEVEL_STORAGE]).set  test_data[CREATE_ITEM_BIN_LEVEL_STORAGE]
  end


 if test_data[CREATE_ITEM_DEPARTMENT_FROM_FLD] && attributes[CREATE_ITEM_DEPARTMENT_FROM_FLD] != nil
   ie_create_item.select_list(:name,attributes[CREATE_ITEM_DEPARTMENT_FROM_FLD]).set  test_data[CREATE_ITEM_DEPARTMENT_FROM_FLD]
 end

#~ ie_create_item.image(:name,attributes[CREATE_ITEM_ADD_DEPARTMENT_FLD]).flash
    ie_create_item.image(:name,CREATE_ITEM_ADD_DEPARTMENT_FLD).click
  
#~ ********************************************************************************
#~ loop = test_data[CREATE_DEPT_MULTIPLE_SELECT_LIST_LOOP].to_i

#~ for i in 1...loop

  #~ if test_data[CREATE_ITEM_DEPARTMENT_FROM_FLD+i.to_s] && attributes[CREATE_ITEM_DEPARTMENT_FROM_FLD+i.to_s] != nil
   #~ ie_create_item.select_list(:name,attributes[CREATE_ITEM_DEPARTMENT_FROM_FLD+i.to_s]).set  test_data[CREATE_ITEM_DEPARTMENT_FROM_FLD+i.to_s]
 #~ end

  #~ if test_data[CREATE_ITEM_ADD_DEPARTMENT_FIELD] == ("y"|| "Y")
   #~ ie_create_item.image(:name,attributes[CREATE_ITEM_ADD_DEPARTMENT_FLD]).click
  #~ end

#~ end

 #~ if test_data[CREATE_ITEM_DEPARTMENT_TO_FLD] && attributes[CREATE_ITEM_DEPARTMENT_FROM_FLD] != nil
  #~ ie_create_item.checkbox(:name,attributes[CREATE_ITEM_DEPARTMENT_TO_FLD]).set  test_data[CREATE_ITEM_DEPARTMENT_TO_FLD]
 #~ end

#~ **********************************************************************************8  
end #End of populate_create_method

#~ **********************************Autocompete code to set the item************************************************
#~ ie_view_item.text_field(:id, attributes[VIEW_ITEM_NUMBER]).value = test_data[VIEW_ITEM_NUMBER]
#~ ie_view_item.text_field(:id, attributes[VIEW_ITEM_NUMBER]).fire_event('onKeyPress')
#~ ie_view_item.text_field(:id, attributes[VIEW_ITEM_NUMBER]).fire_event('onKeyDown')
#~ ie_view_item.text_field(:id, attributes[VIEW_ITEM_NUMBER]).fire_event('onKeyUp')
#~ ie_view_item.text_field(:id, attributes[VIEW_ITEM_NUMBER]).fire_event('onChange')
#~ ie_view_item.text_field(:id, attributes[VIEW_ITEM_NUMBER]).fire_event('onblur')

#~ *****************************************************************************************************************************************************************************************

def view_item_success(tc, test_data, attributes)

  #~ ie_view_item = start_browser(VIEW_ITEM_URL)
 
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text,VIEW_MATERIAL_LINK).click
  ie_view_item=Watir::IE.attach(:url,/view*/)
  
  assert_not_nil(ie_view_item.contains_text(VIEW_ITEM_TITLE), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_TITLE + ". View Item Screen."))
    
  if test_data[VIEW_ITEM_NUMBER] && attributes[VIEW_ITEM_NUMBER]!=nil
    ie_view_item.text_field(:id,attributes[VIEW_ITEM_NUMBER]).set test_data[VIEW_ITEM_NUMBER]
  end

  ie_view_item.button(:value,VIEW_BUTTON).click

  if test_data[CREATE_SUCCESS_DATA] =='1'
  assert(ie_view_item.text_field(:name, attributes[CREATE_ITEM_NO]).readonly? )
  assert(ie_view_item.text_field(:name, attributes[CREATE_ITEM_NO]).verify_contains(test_data[CREATE_ITEM_NO]))
  end

  if test_data[MODIFY_SUCCESS_DATA] =='1'
  assert(ie_view_item.text_field(:name, attributes[MODIFY_ITEM_DESCRIPTION]).verify_contains(test_data[MODIFY_ITEM_DESCRIPTION]))
  end

  assert_not_nil(ie_view_item.contains_text(VIEW_ITEM_SUCCESS_MSG), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_SUCCESS_MSG + ". View Item Screen."))

  ensure
  ie_view_item.close

#~ *************************************************************************18/08/2009/**************************************************************************************************************************************

end

def view_item_failure(tc, test_data, attributes)

#~ ie_view_item = start_browser(VIEW_ITEM_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text,VIEW_MATERIAL_LINK).click
  ie_view_item=Watir::IE.attach(:url,/view*/)

 
assert_not_nil(ie_view_item.contains_text(VIEW_ITEM_TITLE), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_TITLE + ". View Item Screen."))
  
if test_data[VIEW_ITEM_NUMBER] && attributes[VIEW_ITEM_NUMBER]!=nil
  ie_view_item.text_field(:id,attributes[VIEW_ITEM_NUMBER]).set test_data[VIEW_ITEM_NUMBER]
end

ie_view_item.button(:value,VIEW_BUTTON).click 
 
if test_data[CREATE_FAILURE_DATA] == '1'
assert_not_nil(ie_view_item.contains_text(VIEW_ITEM_FAILURE_MSG), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_FAILURE_MSG + ". View Item Screen."))
end

if test_data[MODIFY_FAILURE_DATA] == '1'
assert(ie_view_item.text_field(:name, attributes[MODIFY_ITEM_DESCRIPTION]).verify_contains(test_data[MODIFY_ITEM_DESCRIPTION]))
end

ensure
ie_view_item.close

end

#~ *********************************************************************************************************************************************************************************************************  


def modify_item_success(tc, test_data, attributes)

  #~ ie_modify_item = start_browser(MODIFY_ITEM_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text,MODIFY_MATERIAL_LINK).click
  ie_modify_item=Watir::IE.attach(:url,/modify*/)

  assert_not_nil(ie_modify_item.contains_text(MODIFY_ITEM_SCREEN), ("TC: " + tc + ". Couldn't find text: " + MODIFY_ITEM_SCREEN + ". Modify Item Screen."))
  populate_modify_item_details(tc, test_data, attributes, ie_modify_item)
  ie_modify_item.button(:value,SAVE_BUTTON).click
  ensure
  ie_modify_item.close
  view_item_success(tc, test_data, attributes)

end

def modify_item_failure(tc, test_data, attributes)

  #~ ie_modify_item = start_browser(MODIFY_ITEM_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text,MODIFY_MATERIAL_LINK).click
  ie_modify_item=Watir::IE.attach(:url,/modify*/)
  
  assert_not_nil(ie_modify_item.contains_text(MODIFY_ITEM_SCREEN), ("TC: " + tc + ". Couldn't find text: " + MODIFY_ITEM_SCREEN + ". Modify Item Screen."))
  populate_modify_item_details(tc, test_data, attributes, ie_modify_item)
  ie_modify_item.button(:value,SAVE_BUTTON).click
  ensure
  ie_modify_item.close
  view_item_failure(tc, test_data, attributes)

end


def populate_modify_item_details(tc, test_data, attributes, ie_modify_item)

  if test_data[MODIFY_ITEM_NUMBER] && attributes[MODIFY_ITEM_NUMBER]!=nil
  ie_modify_item.text_field(:name,attributes[MODIFY_ITEM_NUMBER]).set test_data[MODIFY_ITEM_NUMBER] 
  end

  ie_modify_item.button(:value,MODIFY_BUTTON).click
  assert_not_nil(ie_modify_item.contains_text(MODIFY_ITEM_SCREEN), ("TC: " + tc + ". Couldn't find text: " + MODIFY_ITEM_SCREEN + ". Modify Item Screen."))

  if test_data[MODIFY_ITEM] && attributes[MODIFY_ITEM] !=nil
  ie_modify_item.text_field(:name,attributes[MODIFY_ITEM]).flash
  ie_modify_item.text_field(:name,attributes[MODIFY_ITEM]).set test_data[MODIFY_ITEM] 
  end

  if test_data[MODIFY_ITEM_TYPE] && attributes[MODIFY_ITEM_TYPE]!=nil
  ie_modify_item.select_list(:class, attributes[MODIFY_ITEM_TYPE]).set test_data[MODIFY_ITEM_TYPE]
  end

  if test_data[MODIFY_ITEM_DESCRIPTION] && attributes[MODIFY_ITEM_DESCRIPTION]!=nil
  ie_modify_item.text_field(:id,attributes[MODIFY_ITEM_DESCRIPTION]).set test_data[MODIFY_ITEM_DESCRIPTION]    
  end

  if test_data[MODIFY_BASE_UOM] && attributes[MODIFY_BASE_UOM]!=nil
  ie_modify_item.select_list(:name,attributes[MODIFY_BASE_UOM]).set test_data[MODIFY_BASE_UOM]
  end

  if test_data[MODIFY_PURCHASE_UOM] && attributes[MODIFY_PURCHASE_UOM]!=nil
  ie_modify_item.select_list(:name,attributes[MODIFY_PURCHASE_UOM]).set test_data[MODIFY_PURCHASE_UOM]  
  end


  if test_data[MODIFY_CONTRACT_REQD] && attributes[MODIFY_CONTRACT_REQD]!=nil
  ie_modify_item.select_list(:id, attributes[MODIFY_CONTRACT_REQD]).set test_data[MODIFY_CONTRACT_REQD]
  end


#~ ie_modify_item.checkbox(:name,MODIFY_SHELF_LIFE_CONTROL).clear
#~ ie_modify_item.checkbox(:name,ALLOW_NEGATIVE_QUANTITIES).set

end #End of populate_modify_ method

  
end#MODULE END

