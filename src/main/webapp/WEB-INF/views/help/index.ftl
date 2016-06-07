<!DOCTYPE html>
<html lang="en">
<head>
    <title>OA Beta</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="//apps.bdimg.com/libs/bootstrap/3.3.4/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/bootstrap/picker/bootstrap-datetimepicker.min.css">
    <script src="/static/resources/jquery-1.8.1.min.js"></script>
    <script src="http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min.js"></script>
    <script src="/static/bootstrap/picker/bootstrap-datetimepicker.min.js"></script>
    <style>
        .w-sm{width:150px;}
        .datepick{

        }
    </style>
</head>
<body ng-app="myApp" ng-controller="leaveCtrl">

<div class="container">
    <h3>请假记录</h3>
    <div class="row wrapper">
        <div class="col-sm-5 m-b-xs">
            <select ng-model="filterType" class="input-sm form-control w-sm inline v-middle" ng-options="x.id as x.label for x in typeFilters">
            </select>
        </div>
        <div class="col-sm-4">
        </div>
        <div class="col-sm-3">
            <div class="input-group">
                <input type="text" class="timepick form_datetime1" ng-model="filterBeginDate" ng-readonly="true" placeholder="开始时间">
                <input type="text" class="timepick form_datetime1" ng-model="filterEndDate" ng-readonly="true" placeholder="结束时间">
          <span class="input-group-btn">
            <button class="btn btn-sm btn-default" type="button" ng-click="search()">搜索!</button>
          </span>
            </div>
        </div>
    </div>
    <table class="table table-striped">
        <thead><tr>
            <th>操作</th>
            <th>类别</th>
            <th>开始时间</th>
            <th>结束时间</th>
        </tr></thead>
        <tbody><tr ng-repeat="leave in leaveList">
            <td>
                <button class="btn" ng-click="edit(leave)">
                    <span class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;Edit
                </button>
            </td>
            <td>{{leave.type}}</td>
            <td>{{leave.beginDate | date: 'MM-dd hh:mm'}}</td>
            <td>{{leave.endDate | date: 'MM-dd hh:mm'}}</td>
        </tr></tbody>
    </table>
    <nav>
        <ul class="pagination">
            <li>
                <a href="#" ng-click="page(pageOptions.page - 1)" ng-disabled="pageOptions.page < 1" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li ng-repeat="x in pageOptions.array"><a href="#" ng-click="page(x)">{{x}}</a></li>
            <li>
                <a href="#" ng-click="page(pageOptions.page + 1)" aria-label="Next" ng-disabled="pageOptions.page > pageOptions.pages">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
    <hr>
    <button class="btn btn-success" ng-click="edit()">
        <span class="glyphicon"></span> 新外出请假登记
    </button>
    <hr>

    <h3 ng-show="edit">外出请假登记:</h3>

    <form class="form-horizontal">
        <div class="form-group">
            <input type="hidden" ng-model="id" value="{{id}}"/>
            <label class="col-sm-2 control-label">类别:</label>
            <div class="col-sm-10">
                <select ng-model="type" ng-options="o.id as o.label for o in typeCustoms">

                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">开始时间:</label>
            <div class="col-sm-10">
                <input type="text" value="{{beginDate | date: 'yyyy-MM-dd hh:mm:ss'}}" class="datepick form_datetime1" ng-model="beginDate" ng-readonly="true" placeholder="开始时间">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">结束时间:</label>
            <div class="col-sm-10">
                <input type="datetime" value="{{endDate | date: 'yyyy-MM-dd hh:mm:ss'}}" class="datepick form_datetime1" ng-model="endDate" ng-readonly="true"  placeholder="结束时间">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">备注:</label>
            <div class="col-sm-10">
                <textarea ng-model="remark">
                    {{leave.remark}}
                </textarea>
            </div>
        </div>
    </form>

    <hr>
    <button class="btn btn-success"  ng-click="save()">
        <span class="glyphicon glyphicon-save">保存</span>
    </button>
</div>
<script>
    var app = angular.module('myApp', []);
    app.controller('leaveCtrl', function($scope, $http, $timeout, $filter) {
        $scope.typeFilters = [
            {id: "", label: "全部"},
        <#list typeCustoms as type>
            {id: "${type.id}", label: "${type.label}"}
            <#if type_has_next>,</#if>
        </#list>
        ];
        $scope.filterType = "";
        $scope.typeCustoms = [
        <#list typeCustoms as type>
            {id: "${type.id}", label: "${type.label}"}
            <#if type_has_next>,</#if>
        </#list>
        ];
        $scope.type = "1";
        $scope.edit = function(leave) {
            leave = leave || {typeId : "1"};
            $scope.type = leave.typeId + "";
            $scope.id = leave.id;
            $scope.beginDate = leave.beginDate;
            $scope.endDate = leave.endDate;
            $scope.remark = leave.remark;
        }
        $.fn.datetimepicker.dates['zh-CN'] = {
            days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
            daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
            daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
            months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
            today: "今天",
            suffix: [],
            meridiem: ["上午", "下午"]
        };
        $scope.save = function() {
            var dateFilter = $filter("date");
            if (isNaN($scope.beginDate)) {
                dateFilter = function(val) {
                    return val;
                }
            }
            if (new Date($scope.beginDate).getTime() > new Date($scope.endDate).getTime()) {
                return alert("开始时间不能晚于结束时间");
            }
            $http({url: "/help/leave/save", method:"POST", params: {id: $scope.id,type: $scope.type, beginDate: dateFilter($scope.beginDate, "yyyy-MM-dd hh:mm:ss"), endDate: dateFilter($scope.endDate, "yyyy-MM-dd hh:mm:ss"), remark: $scope.remark}}).success(function(response) {
                if (response && response.error || response.message) {
                    alert(response.message);
                } else {
                    alert("操作成功!");
                    location.reload();
                }
            });
        };
        $scope.pageOptions = {
            start: 0,
            limit: 5,
            page: 1,
            pages: 1,
            params: []
        };
        $scope.search = function() {
          var beginDate = $scope.filterBeginDate;
          var endDate = $scope.filterEndDate;
          var type = $scope.filterType;
            var params = [
                "eq_leaveTypeCustom.id=" + type
            ];
            if (beginDate) {
                params.push("ge_beginDate=" + beginDate) + " 00:00:00";
            }
            if (endDate) {
                params.push("le_endDate=" + endDate + "  23:59:59");
            }
            $scope.pageOptions.params = params;
        };
        $scope.$watch("pageOptions.params", function(newValue, oldValue) {
            reload();
        });
        $scope.$watch("pageOptions.page", function(newValue, oldValue) {
            reload(newValue);
        });

        function reload(page) {
            page = page || $scope.pageOptions.page;
            var limit = $scope.pageOptions.limit;
            var start = (page-1) * limit;
            $http.get("/help/leave/grid?start=" + start + "&limit=" + limit + "&" + decodeURIComponent($scope.pageOptions.params.join("&"))).success(function(response) {
                $scope.leaveList = response.rows;
                var total = $scope.pageOptions.total = response.results;
                var pages = Math.ceil((total/limit));
                var arr = [];
                for (var i = page - 2; i <= pages; i++) {
                    if (arr.length == 5) {
                        break;
                    }
                    if (i < 1) {
                        continue;
                    }
                    arr.push(i);
                }
                $scope.pageOptions.array = arr;
                $scope.pageOptions.pages = pages;
            });
        }

        $scope.page = function(page) {
            if (page < 0 || page > $scope.pageOptions.pages) {
                return;
            }
            $scope.pageOptions.page = page;
        };
        $(function() {
            $('.datepick').datetimepicker({
                format: 'yyyy-mm-dd hh:ii:00',
                language: 'zh-CN',
                autoclose: true,
                todayBtn: true,
                minuteStep: 15
            });
            $('.timepick').datetimepicker({
                format: 'yyyy-mm-dd',
                language: 'zh-CN',
                autoclose: true,
                todayBtn: true,
                minView: 2,
                maxView: 2
            });
        });
    });
</script>
</body>
</html>