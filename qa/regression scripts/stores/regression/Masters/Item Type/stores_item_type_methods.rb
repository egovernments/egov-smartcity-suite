
require path_to_file("stores_constants.rb") 

module Stores_item_type_methods

include Stores_constants
include Test::Unit::Assertions

  
def create_item_type_success(tc, test_data, attributes)

#~ ie_create_item_type  = start_browser(CREATE_ITEM_TYPE_URL)

  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, CREATE_ITEM_TYPE_LINK).click
  ie_create_item_type=Watir::IE.attach(:url,/itemtypecentral*/)
  
assert_not_nil(ie_create_item_type.contains_text(CREATE_ITEM_TYPE_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_ITEM_TYPE_SCREEN + ". Create item type screen."))
populate_create_item_type(tc, test_data, attributes, ie_create_item_type)
ie_create_item_type.button(:class, CREATE_NEW_ITEM_TYPE_BUTN).click

ensure
ie_create_item_type.close

end


def create_item_type_failure(tc, test_data, attributes)

#~ ie_create_item_type  = start_browser(CREATE_ITEM_TYPE_URL)
 #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
 $browser.dt(:text,'Masters').click
  $browser.link(:text, CREATE_ITEM_TYPE_LINK).click
  ie_create_item_type=Watir::IE.attach(:url,/itemtypecentral*/)

assert_not_nil(ie_create_item_type.contains_text(CREATE_ITEM_TYPE_SCREEN), ("TC: " + tc + ". Couldn't find text: " + CREATE_ITEM_TYPE_SCREEN + ". Create item type screen."))
populate_create_item_type(tc, test_data, attributes, ie_create_item_type)
ie_create_item_type.button(:class, CREATE_NEW_ITEM_TYPE_BUTN).click
ensure
ie_create_item_type.close

end



def populate_create_item_type(tc, test_data, attributes, ie_create_item_type)

  ie_create_item_type.button(:name, CREATE_NEW_ITEM_TYPE_BTN).click
  
  if test_data[CREATE_ITEM_TYPE_FLD] && attributes[CREATE_ITEM_TYPE_FLD]!=nil
    ie_create_item_type.text_field(:id,attributes[CREATE_ITEM_TYPE_FLD]).set test_data[CREATE_ITEM_TYPE_FLD]    
  end
  
  if test_data[CREATE_ITEM_TYPE_DESC_FLD] && attributes[CREATE_ITEM_TYPE_DESC_FLD]!=nil
    ie_create_item_type.text_field(:id,attributes[CREATE_ITEM_TYPE_DESC_FLD]).set test_data[CREATE_ITEM_TYPE_DESC_FLD]
    #~ assert(ie_create_item_type.text_field(:id,attributes[CREATE_ITEM_TYPE_DESC_FLD]).verify_contains(test_data[CREATE_ITEM_TYPE_DESC_FLD]) )    
    
  end
  
  if test_data[CREATE_ITEM_TYPE_STOCK_CODE_FLD] && attributes[CREATE_ITEM_TYPE_STOCK_CODE_FLD]!=nil
    ie_create_item_type.select_list(:id,attributes[CREATE_ITEM_TYPE_STOCK_CODE_FLD]).set test_data[CREATE_ITEM_TYPE_STOCK_CODE_FLD]
    #~ assert(ie_create_item_type.select_list(:id,attributes[CREATE_ITEM_TYPE_STOCK_CODE_FLD]).verify_contains(test_data[CREATE_ITEM_TYPE_STOCK_CODE_FLD]) )    
  end
  
  if test_data[CREATE_ITEM_TYPE_PARENT_TYPE_FLD] && attributes[CREATE_ITEM_TYPE_PARENT_TYPE_FLD]!=nil
    ie_create_item_type.select_list(:id,attributes[CREATE_ITEM_TYPE_PARENT_TYPE_FLD]).set test_data[CREATE_ITEM_TYPE_PARENT_TYPE_FLD]    
  end

end


def view_item_type_success(tc, test_data, attributes)

  #~ ie_view_item_type = start_browser(VIEW_ITEM_TYPE_URL)
  
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_ITEM_TYPE_LINK).click
  ie_view_item_type=Watir::IE.attach(:url,/itemtypecentral*/)

  assert_not_nil(ie_view_item_type.contains_text(VIEW_ITEM_TYPE_TITLE), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_TYPE_TITLE + ". Not in View Item type Screen."))

  if test_data[VIEW_ITEM_TYPE_FLD] && attributes[VIEW_ITEM_TYPE_FLD]!=nil
  assert(ie_view_item_type.select_list(:id,attributes[VIEW_ITEM_TYPE_FLD]).includes?(test_data[VIEW_ITEM_TYPE_FLD]) )
  ie_view_item_type.select_list(:id,attributes[VIEW_ITEM_TYPE_FLD]).set test_data[VIEW_ITEM_TYPE_FLD]    
  end

  ie_view_item_type.button(:name, VIEW_ITEM_TYPE_BUTTON).click

  assert_not_nil(ie_view_item_type.contains_text(VIEW_ITEM_TYPE_SUCCESS_MESG), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_TYPE_SUCCESS_MESG + ". Not in View Item type Screen."))

  
  if test_data[CREATE_ITEMTYPE_SUCCESS]=='1'
  assert(ie_view_item_type.text_field(:name, attributes[CREATE_ITEM_TYPE_FLD]).readonly? )
  assert(ie_view_item_type.text_field(:name, attributes[CREATE_ITEM_TYPE_FLD]).verify_contains(test_data[CREATE_ITEM_TYPE_FLD]))
  end

  if test_data[MODIFY_ITEMTYPE_SUCCESS]=='1'
  assert(ie_view_item_type.text_field(:name, attributes[MODIFY_ITEM_TYPE_FLD]).readonly? )
  assert(ie_view_item_type.text_field(:name, attributes[MODIFY_ITEM_TYPE_FLD]).verify_contains(test_data[MODIFY_ITEM_TYPE_FLD]))
  end

  ensure
  ie_view_item_type.close

end

def view_item_type_failure(tc, test_data, attributes)
  
  #~ ie_view_item_type = start_browser(VIEW_ITEM_TYPE_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, VIEW_ITEM_TYPE_LINK).click
  ie_view_item_type=Watir::IE.attach(:url,/itemtypecentral*/)

  assert_not_nil(ie_view_item_type.contains_text(VIEW_ITEM_TYPE_TITLE), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_TYPE_TITLE + ". View Item type Screen."))

  if test_data[CREATE_ITEMTYPE_FIALURE]=='1'
  assert_not_nil(ie_view_item_type.select_list(:id,attributes[VIEW_ITEM_TYPE_FLD]).includes?(test_data[VIEW_ITEM_TYPE_FLD]))
  end

  if test_data[MODIFY_ITEMTYPE_FAILURE]=='1'
  assert_not_nil(ie_view_item_type.select_list(:id,attributes[VIEW_ITEM_TYPE_FLD]).includes?(test_data[MODIFY_ITEM_TYPE_FLD])) 
  end

  assert_not_nil(ie_view_item_type.contains_text(VIEW_ITEM_TYPE_FAILURE_MESG), ("TC: " + tc + ". Couldn't find text: " + VIEW_ITEM_TYPE_FAILURE_MESG + ". View Item type Screen."))
  
  ensure
  ie_view_item_type.close
 
end



def modify_item_type_success(tc, test_data, attributes)
  
  #~ ie_modify_item_type = start_browser(MODIFY_ITEM_TYPE_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, MODIFY_ITEM_TYPE_LINK).click
  ie_modify_item_type=Watir::IE.attach(:url,/itemtypecentral*/)
  
  assert_not_nil(ie_modify_item_type.contains_text(MODIFY_ITEM_TYPE_SCREEN), ("TC: " + tc +". Couldn't find the text:" + MODIFY_ITEM_TYPE_SCREEN + ". Modfify item type screen"))
  populate_modify_item_type_details(tc, test_data, attributes, ie_modify_item_type)
  ie_modify_item_type.button(:id, MODIFY_ITEM_TYPE_BTTN).click
  ensure
  ie_modify_item_type.close
  
end

def modify_item_type_failure(tc, test_data, attributes)
  
  #~ ie_modify_item_type = start_browser(MODIFY_ITEM_TYPE_URL)
  #~ $browser.cell(:xpath, "//[contains(text(),'Masters')]").click
  $browser.dt(:text,'Masters').click
  $browser.link(:text, MODIFY_ITEM_TYPE_LINK).click
  ie_modify_item_type=Watir::IE.attach(:url,/itemtypecentral*/)
  
  assert_not_nil(ie_modify_item_type.contains_text(MODIFY_ITEM_TYPE_SCREEN), ("TC: " + tc +". Couldn't find the text:" + MODIFY_ITEM_TYPE_SCREEN + ". Modfify item type screen"))
  populate_modify_item_type_details(tc, test_data, attributes, ie_modify_item_type)
  ie_modify_item_type.button(:id, MODIFY_ITEM_TYPE_BTTN).click
  ensure
  ie_modify_item_type.close
  
end


def populate_modify_item_type_details(tc, test_data, attributes, ie_modify_item_type)

  if test_data[MODFIY_ITEM_TYPE_FIELD] && attributes[MODFIY_ITEM_TYPE_FIELD]!=nil
  assert_true(ie_modify_item_type.select_list(:id,attributes[MODFIY_ITEM_TYPE_FIELD]).includes?(test_data[MODFIY_ITEM_TYPE_FIELD]))
  ie_modify_item_type.select_list(:id,attributes[MODFIY_ITEM_TYPE_FIELD]).set test_data[MODFIY_ITEM_TYPE_FIELD]    
  end

  ie_modify_item_type.button(:name, MODIFY_ITEM_TYPE_BTN).click
  
  assert_not_nil(ie_modify_item_type.contains_text(MODIFY_ITEM_TYPE_SCREEN), ("TC: " + tc +". Couldn't find the text:" + MODIFY_ITEM_TYPE_SCREEN + ". Modfify item type screen"))

  if test_data[MODIFY_ITEM_TYPE_FLD] && attributes[MODIFY_ITEM_TYPE_FLD]!=nil
  ie_modify_item_type.text_field(:id,attributes[MODIFY_ITEM_TYPE_FLD]).set test_data[MODIFY_ITEM_TYPE_FLD]    
  end

  if test_data[MODIFY_ITEM_TYPE_DESC_FLD] && attributes[MODIFY_ITEM_TYPE_DESC_FLD]!=nil
  ie_modify_item_type.text_field(:id,attributes[MODIFY_ITEM_TYPE_DESC_FLD]).set test_data[MODIFY_ITEM_TYPE_DESC_FLD]    
  end

  if test_data[MODIFY_ITEM_TYPE_STOCK_CODE_FLD] && attributes[MODIFY_ITEM_TYPE_STOCK_CODE_FLD]!=nil
  ie_modify_item_type.select_list(:id,attributes[MODIFY_ITEM_TYPE_STOCK_CODE_FLD]).set test_data[MODIFY_ITEM_TYPE_STOCK_CODE_FLD]    
  end

  if test_data[MODIFY_ITEM_TYPE_PARENT_TYPE_FLD] && attributes[MODIFY_ITEM_TYPE_PARENT_TYPE_FLD]!=nil
  ie_modify_item_type.select_list(:id,attributes[MODIFY_ITEM_TYPE_PARENT_TYPE_FLD]).set test_data[MODIFY_ITEM_TYPE_PARENT_TYPE_FLD]    
  end  

end


end#MODULE END
