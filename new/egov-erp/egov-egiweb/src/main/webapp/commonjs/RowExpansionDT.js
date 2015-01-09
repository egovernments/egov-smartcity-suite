/**********
*
* Row Expansion DataTable for YUI 2
* Author: gelinase@yahoo-inc.com / Eric Gelinas
* <br/>
* Modified by Daniel Barreiro (satyam@satyam.com.ar)
* @module rowexpansiondatatable
* @requires yahoo, dom, event, element, datasource, datatable
* @title RowExpansionDataTable Widget
***********/
(function(){

    var Dom = YAHOO.util.Dom,
		Event = YAHOO.util.Event,
		Lang = YAHOO.lang,
		DT = YAHOO.widget.DataTable,

        ROW_EXPANSION = 'yuiDtRowExpansion',
		
		TEMPLATE = 'rowExpansionTemplate',

        CLASS_EXPANDED = 'yui-dt-expansion-expanded',
        CLASS_COLLAPSED = 'yui-dt-expansion-collapsed',
        CLASS_EXPANSION = 'yui-dt-expansion-expansion',
        CLASS_LINER = 'yui-dt-expansion-liner',
		CLASS_TRIGGER = 'yui-dt-expansion-trigger';


    /**
    * The RowExpansionDataTable class extends the DataTable class to provide
    * functionality for expanding rows to show more contextual data.
    *
    * @namespace YAHOO.widget
    * @class RowExpansionDataTable
    * @extends YAHOO.widget.DataTable
    * @constructor
    * @param elContainer {HTMLElement} Container element for the TABLE.
    * @param aColumnDefs {Object[]} Array of object literal Column definitions.
    * @param oDataSource {YAHOO.util.DataSource} DataSource instance.
    * @param oConfigs {object} (optional) Object literal of configuration values.
    */
    var REDT = function(elContainer,aColumnDefs,oDataSource,oConfigs) {

		// add trigger column
		aColumnDefs.unshift({
			key:ROW_EXPANSION,
			label:'',
			className:CLASS_TRIGGER
		});
        REDT.superclass.constructor.call(this, elContainer,aColumnDefs,oDataSource,oConfigs); 

    };
	
	YAHOO.widget.RowExpansionDataTable = REDT;

	// Copy over DataTable constants and other static members
	Lang.augmentObject(REDT, DT);
		
    Lang.extend( 
        REDT,
        DT, 
        {
			/**
			 * Initialize internal event listeners
			 *
			 * @method _initEvents
			 * @private
			 */
			_initEvents: function () {
				REDT.superclass._initEvents.call(this);

                this.on( 'postRenderEvent', this.restoreExpandedRows );			
				this.on( 'cellClickEvent', this.onEventToggleRowExpansion );

			},
			/**
			 * Loops through all Record instances in the RecordSet
			 * @method forAllRecords
			 * @param fn {Function} Reference to a function that will be called once for ever record.  
			 *                      fn will receive a Record instance and the index for the record as its arguments.
			 *                      fn may return false to break out of the loop.
			 * @param scope {Object} (optional) Scope to execute fn in.  It defaults to the DataTable instance.
			 *
			 */
			forAllRecords: function (fn, scope) {
				if (!Lang.isFunction(fn)) {return;}
				scope = scope || this;
				for (var recs = this._oRecordSet._records, l = recs.length, i = 0; i < l; i++) {
					if (fn.call(scope, recs[i], i) === false) {return;}
				}
			},

            /**
             * Gets expansion state object for a specific record associated with the
             * DataTable.
             *
             * @method getExpansionState
             * @param {Mixed} recordId Record / Row / or Index id
             * @param {String} key  (optional) Key to return within the state object. Default is to
             * return all as a map
             * @return {Object} State data object or value for Key. 
			 * The full state object contains <ul>
			 * <li><b>record</b>: reference to the associated Record instance</li>
			 * <li><b>expanded</b>: true when the row is expanded</li>
			 * <li><b>expTrEl</b>: the TR element for the expansion row</li>
			 * <li><b>expLinerEl</b>: the container for the expansion</li>
			 * <li><i>other</i>: Developers may store extra information in it</li>
			 * </ul>
            **/
            getExpansionState : function( recordId, key ){

                var record = this.getRecord( recordId ),
                    expansionState = record._expansionState;
				if (key) {
					return expansionState?expansionState[key]:null;
				} else {
					expansionState = expansionState || {};
					expansionState.record = record;
					return expansionState;
				}
            },

            /**
             * Sets a value to a expansion state object with a given Key for a record
             * which is associated with the DataTable
             *
             * @method setExpansionState
             * @param {Mixed} recordId Record / Row / or Index id
             * @param {String} key Key to use in map
             * @param {Mixed} value Value to assign to the key
             * @return {Object} State data object
            **/
            setExpansionState : function( recordId, key, value ){

                var record = this.getRecord( recordId ),
                    expansionState = record._expansionState || (record._expansionState = {});

                expansionState[ key ] = value;

                return expansionState;

            },

            /**
             * Over-ridden initAttributes method from DataTable, inherited from Element
             *
             * @method initAttributes
             * @param {Object} configuration attributes taken from the fourth argument to the constructor
            **/
            initAttributes : function( oConfigs ) {

                oConfigs = oConfigs || {};

                REDT.superclass.initAttributes.call( this, oConfigs );

                /**
                 * Template for the expansion row.  
				 * If a String, it will be processed through YAHOO.lang.substitute and will have all the values
				 * in the associated Record available.
				 * If a Function, it will receive the expansion state object, see: <a href="#method_getExpansionState">getExpansionState</a></li>
                 *
                 * @attribute rowExpansionTemplate
                 * @type {String or Function} 
                 * @default null
                **/
                this.setAttributeConfig(TEMPLATE, {
                    value: null,
                    validator: function( template ){
                        return (
                            Lang.isString( template ) ||
                            Lang.isFunction( template )
                        );
                    }
                });

            },


            /**
             * Toggles the expansion state of a row
             *
             * @method toggleRowExpansion
             * @param {Mixed} recordId Record / Row / or Index id
            **/
            toggleRowExpansion : function( recordId ){

                if( this.getExpansionState( recordId,'expanded' ) ){

                    this.collapseRow( recordId );

                } else {

                    this.expandRow( recordId );

                }

            },

            /**
             * Sets the expansion state of a row to expanded
             *
             * @method expandRow
             * @param {Mixed} recordId Record / Row / or Index id
             * @return {Boolean} successful
            **/
            expandRow : function( recordId ){

                var state = this.getExpansionState( recordId );

                if( !state.expanded){
					this.setExpansionState( recordId, 'expanded', true );
					var expTrEl = state.expTrEl,
						record = state.record,
                        trEl = this.getTrEl( record );
					if (expTrEl) {
						Dom.insertAfter(state.expTrEl,this.getTrEl(recordId));
						Dom.setStyle(state.expTrEl,'display','');
					} else {

						expTrEl = document.createElement('tr');
						var expTdEl = document.createElement( 'td' ),
							expLinerEl = document.createElement( 'div' ),
							template = this.get(TEMPLATE);

						Dom.addClass(expTrEl, CLASS_EXPANSION);
						expTdEl.colSpan = this.getFirstTrEl().childNodes.length;
						Dom.addClass(expLinerEl, CLASS_LINER);
						expTrEl.appendChild( expTdEl );
						expTdEl.appendChild( expLinerEl);
						
						this.setExpansionState( recordId, 'expTrEl', expTrEl);
						this.setExpansionState( recordId, 'expLinerEl', expLinerEl);
						
						// refresh the copy of the expansion state
						state = this.getExpansionState(recordId);

						if( Lang.isString( template ) ){

							expLinerEl.innerHTML = Lang.substitute( template, record.getData() );

						} else if( Lang.isFunction( template ) ) {

							template.call(this,  state );

						} else {

							return false;

						}
					}

                    //Insert new row
                    Dom.insertAfter( expTrEl, trEl );




					Dom.replaceClass( trEl, CLASS_COLLAPSED, CLASS_EXPANDED );

					/**
					 * Fires when a row is expanded
					 *
					 * @event rowExpandedEvent
					 * @param state {Object} see <a href="#method_getExpansionState">getExpansionState</a>
					 */
					this.fireEvent( "rowExpandedEvent", state);

					return true;

                }

            },

            /**
             * Sets the expansion state of a row to collapsed
             * @method collapseRow
             * @param {Mixed} recordId Record / Row / or Index id
             * @return {Boolean} successful
            **/
            collapseRow : function( recordId ){

                var state = this.getExpansionState(recordId);

                if(state.expanded ){

                    this.setExpansionState( recordId, 'expanded', false );
					

                    Dom.replaceClass( this.getTrEl(state.record), CLASS_EXPANDED, CLASS_COLLAPSED );
					Dom.setStyle(state.expTrEl,'display','none');

					/**
					 * Fires when a row is collapsed
					 *
					 * @event rowCollapsedEvent
					 * @param state {Object} see <a href="#method_getExpansionState">getExpansionState</a>
					 */
					this.fireEvent("rowCollapsedEvent",this.getExpansionState(recordId));

					return true;

				} else {

					return false;

				}

            },

            /**
             * Collapses all expanded rows. 
             *
             * @method collapseAllRows
            **/
            collapseAllRows : function(){

				this.forAllRecords(this.collapseRow);

            },

            /**
             * Restores rows which have been expanded state but the original markup
			 * has been removed from the page.
             *
             * @method restoreExpandedRows
            **/
            restoreExpandedRows : function(){

				this.forAllRecords(function(record) {
					if (this.getExpansionState(record,'expanded')) {
						var trEl = this.getTrEl(record);
						if (trEl) {
							Dom.replaceClass(trEl,CLASS_COLLAPSED, CLASS_EXPANDED);        
							Dom.insertAfter(this.getExpansionState(record,'expTrEl'),trEl);
						}
					}
				});

            },
			/**
			 * returns the container element for the expansion
			 * @method getExpansionContainer
             * @param {Mixed} recordId Record / Row / or Index id
			 * @return {HTMLElement} container element
			 */
			getExpansionContainer: function (recordId) {
				return this.getExpansionState(recordId,'expLinerEl');
			},


            /**
             * Helper method which toggles row expansion. 
             *
             * @method onEventToggleRowExpansion
             * @param {Object} oArgs context of a subscribed event
            **/
            onEventToggleRowExpansion : function( oArgs ){
				var column = this.getColumn(oArgs.target);
				if (column && column.key == ROW_EXPANSION) {

                    this.toggleRowExpansion( oArgs.target );
					Event.stopPropagation(oArgs.event);
					return false;

                }

            },
			/**
			 * Destroys a expansion row
			 * @method _destroyExpansion
			 * @param recordId <HTMLElement | Number | String>    DOM reference to a TR element (or child of a TR element), RecordSet position index, or Record ID.
			 * @private
			 */
			_destroyExpansion: function (recordId) {
				var state = this.getExpansionState(recordId),
					expTrEl = state.expTrEl;
				if (expTrEl) {
					/**
					 * Fires when a row is about to be deleted
					 *
					 * @event rowExpansionDestroyEvent
					 * @param state {Object} see <a href="#method_getExpansionState">getExpansionState</a>
					 */
					this.fireEvent('rowExpansionDestroyEvent', state);
					expTrEl.parentNode.removeChild(expTrEl);
				}
			},
			/**
			 * Overrides DataTable's destroy method to also destroy expansion rows.
			 * @method destroy
			 */
			destroy: function() {
				this.forAllRecords(this._destroyExpansion);
				REDT.superclass.destroy.apply(this, arguments);
			
			},
			/**
			 * This is an override for DataTable's own <code>initializeTable</code>
			 * which it calls after it destroys all expansion rows
			 *
			 * @method initializeTable
			 */
			initializeTable: function () {
				this.forAllRecords(this._destroyExpansion);
				REDT.superclass.initializeTable.apply(this, arguments);
			},
			/**
			 * This is an override for DataTable's own <code>onDataReturnSetRows</code>
			 * which it calls after it destroys all expansion rows since
			 * with server-side pagination, with every new page it ignores the previous records
			 *
			 * @method onDataReturnSetRows
			 */
			onDataReturnSetRows: function () {
				this.forAllRecords(this._destroyExpansion);
				REDT.superclass.onDataReturnSetRows.apply(this, arguments);
			},
			/**
			 * This is an override for DataTable's own <code>deleteRow</code>
			 * to delete the expansion row before the main row is deleted
			 *
			 * @method deleteRow
			 * @param row {HTMLElement | String | Number} DOM element reference or ID string
			 * to DataTable page element or RecordSet index.
			 */
			deleteRow:function (row) {
				this._destroyExpansion(row);
				REDT.superclass.deleteRow.apply(this,arguments);
			},
			/**
			 * This is an override for DataTable's own <code>deleteRows</code>
			 * to delete the expansion rows before the main rows are deleted
			 *
			 * @method deleteRows
			 * @param row {HTMLElement | String | Number} DOM element reference or ID string
			 * to DataTable page element or RecordSet index.
			 * @param count {Number} (optional) How many rows to delete. A negative value
			 * will delete towards the beginning.
			 */
			deleteRows:function (row, count) {
				if (count > 0) {
					while (count--) {
						this._destroyExpansion(row);
						row = this.getNextTrEl(row);
					}
				} else {
					while (count++) {
						this._destroyExpansion(row);
						row = this.getPreviousTrEl(row);
					}
				}
				REDT.superclass.deleteRows.apply(this,arguments);
			}
		}
	);

})();

