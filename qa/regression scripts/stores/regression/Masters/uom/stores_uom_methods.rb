
require path_to_file("stores_constants.rb") 
#require 'win32ole'

module Stores_uom_methods

include Stores_constants
include Test::Unit::Assertions
  
  

def uom_category_details(tc, test_data, attributes)

  create_uom_category(tc, test_data, attributes)
  view_uom_category_success(tc, test_data, attributes)

end
  
  
def create_uom(tc, test_data, attributes)

#~ ie_create_uom = start_browser(CREATE_UOM_URL)
#~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
$browser.dt(:text,'Masters').click
$browser.link(:text, CREATE_UOM_LINK).click
ie_create_uom=Watir::IE.attach(:url,/jsp*/)

assert_not_nil(ie_create_uom.contains_text(CREATE_UOM_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + CREATE_UOM_SCREEN + ".Create uom screen."))
populate_create_uom_details(tc, test_data, attributes, ie_create_uom)
ie_create_uom.button(:id, CREATE_UOM_BTN).click

ensure
ie_create_uom.close

end

def populate_create_uom_details(tc, test_data, attributes, ie_create_uom)

  ie_create_uom.button(:name, CREATE_NEW_UOM_BTN).click

  if test_data[CREATE_NEW_UOM_FLD] && attributes[CREATE_NEW_UOM_FLD]!= nil
  ie_create_uom.text_field(:id, attributes[CREATE_NEW_UOM_FLD]).set test_data[CREATE_NEW_UOM_FLD]
  end

  if test_data[CREATE_UOM_DESC_FLD] && attributes[CREATE_UOM_DESC_FLD]!= nil
  ie_create_uom.text_field(:id, attributes[CREATE_UOM_DESC_FLD]).set test_data[CREATE_UOM_DESC_FLD]
  end

  if test_data[CREATE_UOMCATEGORY_FLD] && attributes[CREATE_UOMCATEGORY_FLD]!= nil
  ie_create_uom.select_list(:id, attributes[CREATE_UOMCATEGORY_FLD]).set test_data[CREATE_UOMCATEGORY_FLD]
  end

  if test_data[CREATE_UOM_BASE_UOM_FLD]== ("y"|| "Y")
  ie_create_uom.checkbox(:id, attributes[CREATE_UOM_BASE_UOM_FLD]).set 
  end

  if test_data[CREATE_UOM_CONVERSION_FACTOR_FLD] && attributes[CREATE_UOM_CONVERSION_FACTOR_FLD]!= nil
  ie_create_uom.text_field(:id, attributes[CREATE_UOM_CONVERSION_FACTOR_FLD]).set test_data[CREATE_UOM_CONVERSION_FACTOR_FLD]
  end

end
  
def view_uom(tc, test_data, attributes)  
  
#~ ie_view_uom = start_browser(VIEW_UOM_URL)
#~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
$browser.dt(:text,'Masters').click
$browser.link(:text, VIEW_UOM_LINK).click
ie_view_uom=Watir::IE.attach(:url,/jsp*/)

if test_data[VIEW_UOM_FLD] && attributes[VIEW_UOM_FLD]!= nil
ie_view_uom.select_list(:id, attributes[VIEW_UOM_FLD]).set test_data[VIEW_UOM_FLD]
end

ie_view_uom.button(:name, VIEW_UOM_BTN).click

return ie_view_uom

end


def view_uom_success(tc, test_data, attributes)

#~ ie_view_uom = start_browser(VIEW_UOM_URL)
#~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
$browser.dt(:text,'Masters').click
$browser.link(:text, VIEW_UOM_LINK).click
ie_view_uom=Watir::IE.attach(:url,/jsp*/)

assert_not_nil(ie_view_uom.contains_text(VIEW_UOM_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_SCREEN+ ".view uom screen."))

if test_data[VIEW_UOM_FLD] && attributes[VIEW_UOM_FLD]!= nil
ie_view_uom.select_list(:id, attributes[VIEW_UOM_FLD]).set test_data[VIEW_UOM_FLD]
end

ie_view_uom.button(:name, VIEW_UOM_BTN).click

assert_not_nil(ie_view_uom.contains_text(VIEW_UOM_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_SCREEN+ ".view uom screen."))

if test_data[CREATE_UOM_SUCCESS]=='1'
assert(ie_view_uom.text_field(:id, attributes[CREATE_NEW_UOM_FLD]).readonly? )
assert(ie_view_uom.text_field(:id, attributes[CREATE_NEW_UOM_FLD]).verify_contains(test_data[CREATE_NEW_UOM_FLD]) )
end

if test_data[MODIFY_UOM_SUCCESS]=='1'
assert(ie_view_uom.text_field(:id, attributes[MODIFY_UOM_FIELD]).readonly? )
assert(ie_view_uom.text_field(:id, attributes[MODIFY_UOM_FIELD]).verify_contains(test_data[MODIFY_UOM_FIELD]) )
end

#~ ensure
#~ ie_view_uom.close

end

def view_uom_failure(tc, test_data, attributes)

  #~ ie_view_uom = start_browser(VIEW_UOM_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_UOM_LINK).click
  ie_view_uom=Watir::IE.attach(:url,/jsp*/)

  assert_not_nil(ie_view_uom.contains_text(VIEW_UOM_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_SCREEN+ ".view uom screen."))

  if test_data[CREATE_UOM_FAILURE]=='1'
  assert_false(ie_view_uom.select_list(:id, attributes[VIEW_UOM_FLD]).includes?(test_data[VIEW_UOM_FLD]))
  end

  if test_data[MODIFY_UOM_FAILURE]=='1'
  assert_not_nil(ie_view_uom.select_list(:id, attributes[VIEW_UOM_FLD]).includes?(test_data[VIEW_UOM_FLD]))
  end

  assert_not_nil(ie_view_uom.contains_text(VIEW_UOM_FAILURE_MESG), ("TC: " + tc + " . Couldn't find the text: " + VIEW_UOM_FAILURE_MESG+ ".view uom failed."))

  ensure
  ie_view_uom.close

end


def modify_uom_success(tc, test_data, attributes)

  #~ ie_modify_uom = start_browser(MODIFY_UOM_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, MODIFY_UOM_LINK).click
  ie_modify_uom=Watir::IE.attach(:url,/jsp*/)

  assert_not_nil(ie_modify_uom.contains_text(MODIFY_UOM_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + MODIFY_UOM_SCREEN+ ". Modify uom screen."))
  populate_modify_uom_details(tc, test_data, attributes, ie_modify_uom)
  ie_modify_uom.button(:id, MODIFY_UOM_BUTTON).click
  ensure
  ie_modify_uom.close

end

def populate_modify_uom_details(tc, test_data, attributes, ie_modify_uom)

  if test_data[MODIFY_UOM_FLD] && attributes[MODIFY_UOM_FLD]!= nil
  ie_modify_uom.select_list(:id, attributes[MODIFY_UOM_FLD]).set test_data[MODIFY_UOM_FLD]
  end

  ie_modify_uom.button(:name, MODIFY_UOM_BTN).click
  
  assert_not_nil(ie_modify_uom.contains_text(MODIFY_UOM_SCREEN), ("TC: " + tc + " . Couldn't find the text: " + MODIFY_UOM_SCREEN+ ". Modify uom screen."))

  if test_data[MODIFY_UOM_FIELD] && attributes[MODIFY_UOM_FIELD]!= nil
  ie_modify_uom.text_field(:id, attributes[MODIFY_UOM_FIELD]).set test_data[MODIFY_UOM_FIELD]
  end

  if test_data[MODIFY_UOM_DESC_FLD] && attributes[MODIFY_UOM_DESC_FLD]!= nil
  ie_modify_uom.text_field(:id, attributes[MODIFY_UOM_DESC_FLD]).set test_data[MODIFY_UOM_DESC_FLD]
  end

  if test_data[MODIFY_UOMCATEGORY_FLD] && attributes[MODIFY_UOMCATEGORY_FLD]!= nil
  ie_modify_uom.select_list(:id, attributes[MODIFY_UOMCATEGORY_FLD]).set test_data[MODIFY_UOMCATEGORY_FLD]
  end

  if test_data[MODIFY_BASE_UOM_FLD]== ("y"|| "Y")
  ie_modify_uom.checkbox(:id, attributes[MODIFY_BASE_UOM_FLD]).set 
  end

  if test_data[MODIFY_UOM_CONVERSION_FACTOR_FLD] && attributes[MODIFY_UOM_CONVERSION_FACTOR_FLD]!= nil
  ie_modify_uom.text_field(:id, attributes[MODIFY_UOM_CONVERSION_FACTOR_FLD]).set test_data[MODIFY_UOM_CONVERSION_FACTOR_FLD]
  end

end
  
end#MODULE END