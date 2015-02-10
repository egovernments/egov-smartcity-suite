dojo.provide("utils.AutoComplete");

dojo.require("dojo.dom");
dojo.require("dojo.event");
dojo.require("dojo.html");
dojo.require("dojo.lang");
dojo.require("dojo.io");
dojo.require("dojo.string");
dojo.require("dojo.widget.HtmlWidget");

dojo.widget.tags.addParseTreeHandler("dojo:AutoComplete");


utils.AutoComplete = function() {
	
    // call super constructor
    dojo.widget.HtmlWidget.call(this);

    // load template
    this.templatePath = dojo.uri.dojoUri("utils/templates/AutoComplete.html");

    // Constants
    this.SELECTED_CLASS = "selected";
    this.widgetType = "AutoComplete";

    // Instance variables
    this.action = "";
    this.formId = "";
    this.textbox = {};
    this.textboxId = "";
    this.form = {};

    this.postCreate = function() {
    	
        this.form = document.getElementById(this.formId);
        this.textbox = document.getElementById(this.textboxId);
        this.choices.style.width = this.textbox.style.width;

        dojo.event.connect(document, "onclick", this, "documentOnClick");
        dojo.event.connect(this.textbox, "onkeypress", this, "textboxOnKeyPress");
        dojo.event.connect(this.textbox, "onkeydown", this, "textboxOnKeyDown");
        dojo.event.connect(this.textbox, "onkeyup", this, "textboxOnKeyUp");
    }

    // If a form submission via enter is detected while the choices are being displayed, dont't
    // submit the form and close the choices div.  Give focus to the next form element.
    this.textboxOnKeyPress = function(evt) {
        if (evt.keyCode == 13 && this.choices.style.display != "none") {
            this.choices.style.display = "none";
            evt.preventDefault();
            for (var i=0; i<this.form.elements.length; i++) {
                if (this.textbox.name == this.form.elements[i].name && (i+1 < this.form.elements.length)) {
                    this.form.elements[i+1].focus();
                    break;
                }
            }
        }
    }

    // If user clicks anywhere else on the screen, don't show the choices div
    this.documentOnClick = function(evt) {
        this.choices.style.display = "none";
    }


    // Check for tab key which is detected on "keyDown" event and not "keyUp"
    this.textboxOnKeyDown = function(evt) {
        if (this.choices.style.display != "none" && evt.keyCode == 9) {
            this.choices.style.display = "none";
        }
    }

    // Convenience function that checks if the first argument is repeated in subsequent ones
    this.isKey = function () {
        if (arguments.length < 2) return false;
        for (var i=1;i<arguments.length;i++) {
            if (arguments[0] == arguments[i]) return true;
        }
        return false;
    }


    // If up or down arrow is pressed, call this.checkUpDownArrows, otherwise get latest list
    this.textboxOnKeyUp = function(evt) {
        if (this.isKey(evt.keyCode, 38, 40)) {
            this.checkUpDownArrows(evt.keyCode);
        } else {
            bindArgs = {
                url:        this.action,
                mimetype:   "text/javascript",
                formNode:   this.form
            };
            var req = dojo.io.bind(bindArgs);
            dojo.event.connect(req, "load", this, "populateChoices");
        }
    }


    // If up or down arrow is pressed, select approriate element
    this.checkUpDownArrows = function(characterCode) {
        if (this.choices.style.display == "none") return;

        var selIndex = this.getSelected();
        var children = this.choices.childNodes;

        if (selIndex == -1) {

            if (characterCode == 38) {
                this.setSelected(0, children.length-1);
            } else if (characterCode == 40){
                this.setSelected(0, 0);
            }


        } else if (characterCode == 38) {

             if (selIndex-1 >= 0) {
                this.setSelected(selIndex,selIndex-1);
             } else if (children.length > 0) {
                this.setSelected(selIndex,children.length-1);
             }

        } else if (characterCode == 40) {

             if (selIndex+1 < children.length) {
                this.setSelected(selIndex,selIndex+1);
             } else {
                this.setSelected(selIndex,0);
             }
        }
    }


    // Unselects div at oldIndex while selecting newIndex
    this.setSelected = function(oldIndex, newIndex) {
        var children = this.choices.childNodes;
        if (children) {
            children[oldIndex].className = "";
            children[newIndex].className = this.SELECTED_CLASS;
            this.textbox.value = children[newIndex].innerHTML;
        }
    }

    // Returns the index of the div that is selected.  Returns -1 if nothing is selected
    this.getSelected = function() {
        var size = (this.choices.childNodes == null) ? 0 : this.choices.childNodes.length;
        for (var i=0; i<size; i++) {
            if (this.choices.childNodes[i].className == this.SELECTED_CLASS) return i;
        }
        return -1;
    }


    // Adds an item div to choices div
    this.addItem = function(item) {
        var itemDiv = document.createElement("div");
        itemDiv.innerHTML = item;
        itemDiv.style.width = "100%";
        this.choices.appendChild(itemDiv);
        dojo.event.connect(itemDiv, "onmouseover", this, "itemOnMouseOver");
        dojo.event.connect(itemDiv, "onclick", this, "itemOnClick");
    }


    // Handler for "load" action of dojo.io.bind() call.  Puts choices into div
    this.populateChoices = function(type, data, evt) {
        dojo.dom.removeChildren(this.choices);
        for (var i=0; i<data.length; i++) {
            this.addItem(data[i]);
        }
        this.choices.style.display = (this.choices.childNodes.length > 0) ? "block" : "none";
    }


    this.itemOnMouseOver = function(evt) {
        var selIndex = this.getSelected();
        if (selIndex != -1) this.choices.childNodes[selIndex].className = "";
        dojo.html.getEventTarget(evt).className = this.SELECTED_CLASS;
    }

    this.itemOnClick = function(evt) {
        this.textbox.value = dojo.html.getEventTarget(evt).innerHTML;
        this.choices.style.display = "none";
    }
}

// define inheritance
dj_inherits(utils.AutoComplete, dojo.widget.HtmlWidget);
