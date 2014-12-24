
require path_to_file("stores_constants.rb") 

module Stores_uom_category_methods

include Stores_constants
include Test::Unit::Assertions  
  
def create_uom_category(tc, test_data, attributes)

  #~ ie_create_uom_category = start_browser(CREATE_UOM_CATEGORY_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, CREATE_UOM_CATEGORY_LINK).click
  ie_create_uom_category=Watir::IE.attach(:url,/jsp*/)
  ie_create_uom_category.maximize()
  
  assert_not_nil(ie_create_uom_category.contains_text(CREATE_UOM_CATEGORY_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_UOM_CATEGORY_SCREEN + ". Create UOM category Screen."))
  populate_create_uom_category_details(tc, test_data, attributes, ie_create_uom_category)
  ie_create_uom_category.button(:id, CREATE_UOM_CATEGORY_BTN).click
  
  ensure
  ie_create_uom_category.close

end

def populate_create_uom_category_details(tc, test_data, attributes, ie_create_uom_category)

  ie_create_uom_category.button(:name, CREATE_NEW_UOM_CATEGORY_BTN).click
  assert_not_nil(ie_create_uom_category.contains_text(CREATE_UOM_CATEGORY_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_UOM_CATEGORY_SCREEN + ". Create UOM category Screen."))

  if test_data[CREATE_UOM_CATEGORY_FLD] && attributes[CREATE_UOM_CATEGORY_FLD]!= nil
  ie_create_uom_category.text_field(:id, attributes[CREATE_UOM_CATEGORY_FLD]).set test_data[CREATE_UOM_CATEGORY_FLD]
  end

  if test_data[CREATE_UOM_CATEGORY_DESC_FLD] && attributes[CREATE_UOM_CATEGORY_DESC_FLD]!= nil
  ie_create_uom_category.text_field(:id, attributes[CREATE_UOM_CATEGORY_DESC_FLD]).set test_data[CREATE_UOM_CATEGORY_DESC_FLD]
  end

end


def view_uom_category_success(tc, test_data, attributes) 

  #~ ie_view_uom_category = start_browser(VIEW_UOM_CATEGORY_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_UOM_CATEGORY_LINK).click
  ie_view_uom_category=Watir::IE.attach(:url,/jsp*/)
  
  assert_not_nil(ie_view_uom_category.contains_text(VIEW_UOM_CATEGORY_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_CATEGORY_SCREEN+ ".View uom category screen."))
  
  if test_data[VIEW_UOM_CATEGORY_FLD] && attributes[VIEW_UOM_CATEGORY_FLD]!= nil
  ie_view_uom_category.select_list(:id, attributes[VIEW_UOM_CATEGORY_FLD]).set test_data[VIEW_UOM_CATEGORY_FLD]
  end

  ie_view_uom_category.button(:name, VIEW_UOM_CATEGORY_BTN).click

  assert_not_nil(ie_view_uom_category.contains_text(VIEW_UOM_CATEGORY_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_CATEGORY_SCREEN+ ".View uom category screen."))
  
  if test_data[CREATE_UOM_CATEGORY_SUCCESS]=='1'
  assert(ie_view_uom_category.text_field(:name, attributes[CREATE_UOM_CATEGORY_FLD]).readonly? )
  assert(ie_view_uom_category.text_field(:name, attributes[CREATE_UOM_CATEGORY_FLD]).verify_contains(test_data[CREATE_UOM_CATEGORY_FLD]) )
  end

  if test_data[MODIFY_UOM_CATEGORY_SUCCESS]=='1'
  assert(ie_view_uom_category.text_field(:name, attributes[MODIFY_UOM_CATEGORY_FIELD]).verify_contains(test_data[MODIFY_UOM_CATEGORY_FIELD]))
end

ensure
ie_view_uom_category.close

end

  
 def view_uom_category_failure(tc, test_data, attributes)  
 
 #~ ie_view_uom_category = start_browser(VIEW_UOM_CATEGORY_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_UOM_CATEGORY_LINK).click
  ie_view_uom_category=Watir::IE.attach(:url,/jsp*/)
  
 assert_not_nil(ie_view_uom_category.contains_text(VIEW_UOM_CATEGORY_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_CATEGORY_SCREEN+ ".View uom category screen."))
 
  if test_data[CREATE_UOM_CATEGORY_FAILURE]=='1'
  assert_not_nil(ie_view_uom_category.select_list(:id, attributes[VIEW_UOM_CATEGORY_FLD]).includes?(test_data[VIEW_UOM_CATEGORY_FLD]))
  end
 
 if test_data[MODIFY_UOM_CATEGORY_FAILURE]=='1'
  assert_not_nil(ie_view_uom_category.select_list(:id, attributes[VIEW_UOM_CATEGORY_FLD]).includes?(test_data[VIEW_UOM_CATEGORY_FLD]))
  end
 
 assert_not_nil(ie_view_uom_category.contains_text(VIEW_UOM_CATEGORY_FAILURE_MESG), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_CATEGORY_FAILURE_MESG+ ".View uom category failed."))

 ensure
 ie_view_uom_category.close

 
 end

def modify_uom_category(tc, test_data, attributes)
 
 #~ ie_modify_uom_category = start_browser(MODIFY_UOM_CATEGORY_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, MODIFY_UOM_CATEGORY_LINK).click
  ie_modify_uom_category=Watir::IE.attach(:url,/jsp*/)
  
 assert_not_nil(ie_modify_uom_category.contains_text(MODIFY_UOM_CATEGORY_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + MODIFY_UOM_CATEGORY_SCREEN+ ".Modify uom category screen."))
 populate_uom_category_details(tc, test_data,attributes, ie_modify_uom_category)
 ie_modify_uom_category.button(:id, MODIFY_UOM_CATEGORY_BTTN).click
 
 ensure
 ie_modify_uom_category.close

end

def populate_uom_category_details(tc, test_data,attributes, ie_modify_uom_category)

  if test_data[MODIFY_UOM_CATEGORY_FLD] && attributes[MODIFY_UOM_CATEGORY_FLD]!= nil
  ie_modify_uom_category.select_list(:id, attributes[MODIFY_UOM_CATEGORY_FLD]). set test_data[MODIFY_UOM_CATEGORY_FLD]
  end

  ie_modify_uom_category.button(:name, MODIFY_UOM_CATEGORY_BTN).click
  assert_not_nil(ie_modify_uom_category.contains_text(MODIFY_UOM_CATEGORY_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + MODIFY_UOM_CATEGORY_SCREEN+ ".Modify uom category screen."))

  if test_data[MODIFY_UOM_CATEGORY_FIELD] && attributes[MODIFY_UOM_CATEGORY_FIELD]!= nil
  ie_modify_uom_category.text_field(:id, attributes[MODIFY_UOM_CATEGORY_FIELD]). set test_data[MODIFY_UOM_CATEGORY_FIELD]
  end

  if test_data[MODIFY_UOM_CATEGORY_DESC_FLD] && attributes[MODIFY_UOM_CATEGORY_DESC_FLD]!= nil
  ie_modify_uom_category.text_field(:id, attributes[MODIFY_UOM_CATEGORY_DESC_FLD]). set test_data[MODIFY_UOM_CATEGORY_DESC_FLD]
  end

end
  
  
end#MODULE END