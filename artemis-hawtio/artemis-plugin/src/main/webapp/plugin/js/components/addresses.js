/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var Artemis;
(function (Artemis) {
    //Artemis.log.debug("loading addresses");
    Artemis._module.component('artemisAddresses', {
        template:
            `
            <h1>Browse Addresses
                <button type="button" class="btn btn-link jvm-title-popover"
                          uib-popover-template="'addresses-instructions.html'" popover-placement="bottom-left"
                          popover-title="Instructions" popover-trigger="'outsideClick'">
                    <span class="pficon pficon-help"></span>
                </button>
            </h1>
             <div ng-include="'plugin/artemistoolbar.html'"></div>
             <table id="addressTable"
               data-row-style="rowStyle"
               data-search="false"
               data-pagination="false"
               data-resizable="true"
               data-resizable-columns-id="addressTable">
               <thead>
                   <tr>
                     <th data-resizable-column-id="ID" data-switchable="true"/>
                     <th data-resizable-column-id="Name" data-switchable="true"/>
                     <th data-resizable-column-id="Routing Types" data-switchable="true"/>
                     <th data-resizable-column-id="Queue Count" data-switchable="true"/>
                     <th data-resizable-column-id="Actions" data-switchable="true"/>
                   </tr>
                 </thead>
                 <tbody/>
             </table>
             <script>
                function rowStyle(row, index) {
                      if (index % 2 === 0 ) {
                        return {
                          classes: 'even'
                        }
                      }
                      return {
                        classes: 'odd'
                      }
                    }
             </script>

             <div ng-include="'plugin/artemispagination.html'"></div>
             <script type="text/ng-template" id="addresses-instructions.html">
             <div>
                <p>
                    This page allows you to browse all address on the broker. These can be narrowed down
                    by specifying a filter and also sorted using the sort function in the toolbar. To execute a query
                    click on the <span class="fa fa-search"></span> button.
                </p>
                <p>
                    You can also navigate directly to the JMX attributes and operations tabs by using the  <code>attributes</code>
                    and <code>operations</code> button under the <code>Actions</code> column.You can navigate to the
                    addresses queues by clicking on the <code>Queue Count</code> field.
                  </p>
                  <p>
                    Note that each page is loaded in from the broker when navigating to a new page or when a query is executed.
                  </p>
                </div>
             </script>
             `,
              controller: AddressesController
    })
    .name;


    function AddressesController($scope, workspace, jolokia, localStorage, artemisMessage, $location, $timeout, $filter, $sanitize, pagination, artemisAddress, $route) {
        var ctrl = this;
        ctrl.route = $route;
        ctrl.pagination = pagination;
        ctrl.pagination.reset();
        var mbean = Artemis.getBrokerMBean(workspace, jolokia);
        ctrl.addresses = [];
        ctrl.workspace = workspace;
        ctrl.refreshed = false;

        ctrl.btTableColumns = [
            { title: 'ID', field: 'id' , visible: true},
            { title: 'Name', field: 'name', visible: true },
            { title: 'Routing Types', field: 'routingTypes', visible: true },
            { title: 'Queue Count', field: 'queueCount' , visible: true },
            { title: 'Actions', field: 'actions' , visible: true }
        ];

        Artemis.log.debug('sessionStorage: addressColumnDefs =', localStorage.getItem('addressColumnDefs'));
        if (localStorage.getItem('addressColumnDefs')) {
            loadedDefs = JSON.parse(localStorage.getItem('addressColumnDefs'));

            ctrl.btTableColumns.forEach(function (column) {
                Artemis.log.info(column.title + ' ' + column.visible);
                var btCol = loadedDefs.find( col => {
                  return column.title == col.name || column.title == col.title;
                });
                Artemis.log.info("btcol " + btCol);
                if (btCol) {
                    column.visible = btCol.visible;
                    Artemis.log.info(btCol.visible);
                }
            })
        } else {
            localStorage.setItem('addressColumnDefs', JSON.stringify(ctrl.btTableColumns));
        }

        ctrl.updateColumns = function () {
            var attributes = [];
            ctrl.btTableColumns.forEach(function (column) {
                attributes.push({title: column.title, visible: column.visible});
                if (column.visible) {
                    $('#addressTable').bootstrapTable('showColumn', column.field);
                } else {
                    $('#addressTable').bootstrapTable('hideColumn', column.field);
                }
            });
            Artemis.log.debug("saving columns " + JSON.stringify(attributes));
            localStorage.setItem('addressColumnDefs', JSON.stringify(attributes));
        }

        ctrl.filter = {
            fieldOptions: [
                {id: 'id', name: 'ID'},
                {id: 'name', name: 'Name'},
                {id: 'routingTypes', name: 'Routing Types'},
                {id: 'queueCount', name: 'Queue Count'}
            ],
            operationOptions: [
                {id: 'EQUALS', name: 'Equals'},
                {id: 'CONTAINS', name: 'Contains'},
                {id: 'GREATER_THAN', name: 'Greater Than'},
                {id: 'LESS_THAN', name: 'Less Than'}
            ],
            sortOptions: [
                {id: 'asc', name: 'ascending'},
                {id: 'desc', name: 'descending'}
            ],
            values: {
                field: "",
                operation: "",
                value: "",
                sortOrder: "asc",
                sortColumn: "id"
            },
            text: {
                fieldText: "Filter Field..",
                operationText: "Operation..",
                sortOrderText: "ascending",
                sortByText: "ID"
            }
        };

        ctrl.tableConfig = {
            selectionMatchProp: 'id',
            showCheckboxes: false
        };
        ctrl.tableColumns = [
            { header: 'ID', itemField: 'id' },
            { header: 'Name', itemField: 'name' },
            { header: 'Routing Types', itemField: 'routingTypes' },
            { header: 'Queue Count', itemField: 'queueCount' , templateFn: function(value, item) { return '<a href="#" onclick="selectQueues(' + item.idx + ')">' + $sanitize(value) + '</a>' }}
        ];


        ctrl.refresh = function () {
            ctrl.refreshed = true;
            ctrl.pagination.load();
        };
        ctrl.reset = function () {
            ctrl.filter.values.field = "";
            ctrl.filter.values.operation = "";
            ctrl.filter.values.value = "";
            ctrl.filter.sortOrder = "asc";
            ctrl.filter.sortColumn = "id";
            ctrl.refreshed = true;
            artemisAddress.address = null;
            ctrl.pagination.load();
        };

        if (artemisAddress.address) {
            Artemis.log.debug("navigating to address = " + artemisAddress.address.address);
            ctrl.filter.values.field = ctrl.filter.fieldOptions[1].id;
            ctrl.filter.values.operation = ctrl.filter.operationOptions[0].id;
            ctrl.filter.values.value = artemisAddress.address.address;
            artemisAddress.address = null;
        }

        selectQueues = function (address) {
            Artemis.log.debug("navigating to queues:" + address)
            artemisAddress.address = { address: address };
            $location.path("artemis/artemisQueues");
        };

        navigateToAddressAtts = function(address) {
            $location.path("artemis/attributes").search({"tab": "artemis", "nid": getAddressNid(address, $location)});
            ctrl.workspace.loadTree();
        };
        navigateToAddressOps = function(address) {
            $location.path("artemis/operations").search({"tab": "artemis", "nid": getAddressNid(address, $location)});
            ctrl.workspace.loadTree();
        };
        function getAddressNid(address, $location) {
            var rootNID = getRootNid($location);
            var targetNID = rootNID + "addresses-" + address;
            Artemis.log.info("targetNID=" + targetNID);
            return targetNID;
        }
        function getRootNid($location) {
            var currentNid = $location.search()['nid'];
            Artemis.log.debug("current nid=" + currentNid);
            var firstDash = currentNid.indexOf('-');
            var secondDash = currentNid.indexOf('-', firstDash + 1);
            var thirdDash = currentNid.indexOf('-', secondDash + 1);
            if (thirdDash < 0) {
                return currentNid + "-";
            }
            var rootNID = currentNid.substring(0, thirdDash + 1);
            return rootNID;
        }
        ctrl.loadOperation = function () {
            if (mbean) {
                var method = 'listAddresses(java.lang.String, int, int)';
                var addressFilter = {
                    field: ctrl.filter.values.field,
                    operation: ctrl.filter.values.operation,
                    value: ctrl.filter.values.value,
                    sortOrder: ctrl.filter.values.sortOrder,
                    sortColumn: ctrl.filter.values.sortColumn
                };

                if (ctrl.refreshed == true) {
                    ctrl.pagination.reset();
                    ctrl.refreshed = false;
                }
                jolokia.request({ type: 'exec', mbean: mbean, operation: method, arguments: [JSON.stringify(addressFilter), ctrl.pagination.pageNumber, ctrl.pagination.pageSize] }, Core.onSuccess(populateTable, { error: onError }));
            }
        };

        ctrl.pagination.setOperation(ctrl.loadOperation);

        function onError(response) {
            Core.notification("error", "could not invoke list sessions" + response.error);
            $scope.workspace.selectParentNode();
        };
        function populateTable(response) {
            var data = JSON.parse(response.value);
            Artemis.log.info(JSON.stringify(data.data));
            ctrl.addresses = [];
            angular.forEach(data["data"], function (value, idx) {
                ctrl.addresses.push ({
                    id: value.id,
                    name: value.name,
                    routingTypes: value.routingTypes,
                    queueCount: '<a href="#" onclick="selectQueues(\'' + value.name + '\')">' + $sanitize(value.queueCount) + '</a>',
                    actions: '<button class="btn btn-default" title="Navigate to attributes" onClick="navigateToAddressAtts(\'' + value.name + '\')"><span class="ng-binding ng-scope">attributes</span></button>' +
                             '<button class="btn btn-default" title="Navigate to operations" onClick="navigateToAddressOps(\'' + value.name + '\')"><span class="ng-binding ng-scope">operations</span></button>'
                });
                Artemis.log.info("val " + JSON.stringify(value));
            });
            ctrl.pagination.page(data["count"]);
            Artemis.log.info(ctrl.btData);
            $('#addressTable').bootstrapTable('load', ctrl.addresses);
            $("#addressTable").resizableColumns({
                store: {
                    get: function (key, optionalDefaultValue) {
                        if (localStorage.getItem('addressColumnDefs')) {
                            loadedDefs = JSON.parse(localStorage.getItem('addressColumnDefs'));
                            var def = loadedDefs.find( col => {
                                return key.endsWith(col.title);
                            });
                            if (def && def.width) {
                                return def.width;
                            }
                        }
                        Artemis.log.info("in getter: " + key + " " + optionalDefaultValue);
                    },
                    set: function (key, value) {
                        if (localStorage.getItem('addressColumnDefs')) {
                            loadedDefs = JSON.parse(localStorage.getItem('addressColumnDefs'));
                            var def = loadedDefs.find( col => {
                                Artemis.log.info(" key=" + key + " col=" + col.title);
                                Artemis.log.info(key.endsWith(col.title));
                                return key.endsWith(col.title);
                            });
                             Artemis.log.info(def);
                            if (def) {
                                def.width = value;
                                localStorage.setItem('addressColumnDefs', JSON.stringify(loadedDefs));
                            }
                        }
                        Artemis.log.info("in setter "+ key + " " + value);
                    }
                }
            });
            Core.$apply($scope);
        }

        ctrl.pagination.load();

        Artemis.log.info("here ");
        $('#addressTable').bootstrapTable({
           columns: ctrl.btTableColumns,
           data: ctrl.addresses
        });
        Artemis.log.info("here");
    }
    AddressesController.$inject = ['$scope', 'workspace', 'jolokia', 'localStorage', 'artemisMessage', '$location', '$timeout', '$filter', '$sanitize', 'pagination', 'artemisAddress', '$route'];


})(Artemis || (Artemis = {}));