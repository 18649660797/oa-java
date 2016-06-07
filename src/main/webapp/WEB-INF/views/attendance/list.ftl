<!DOCTYPE html>
<html lang="en">
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
    <div class="row">
        <form id="J_FORM" class="form-panel" action="data" method="post" style="margin-bottom:0;">
            <input type="hidden" name="sort" value="employee.id asc,workDate asc"/>
            <div class="panel-title">
            <span>
                <label>考勤月：</label>
                <input type="text" id="J_Month" name="bw_workDateFormat" value="">

                <label>打卡日期：</label><input name="ge_workDate" type="text" class="calendar" /> <label>至</label> <input name="le_workDate" type="text" class="calendar" />

                <label>姓名：</label>
                <div class="suggest" id="realName"></div>

                <label>部门：</label>
                <select name="eq_employee.department.id">
                    <option value="">全部</option>
                <#if departmentList?? && departmentList?size gt 0>
                    <#list departmentList as department>
                        <option value="${(department.id)!}">${(department.name)!}</option>
                    </#list>
                </#if>
                </select>
            </span>
            </div>
            <ul class="panel-content">
                <li>
                    <label class="control-text">迟到：</label><input style="width:20px;" id="delay" type="checkbox" name="gt_amTime" value="09:00" />
                    <label class="control-text">早退：</label><input style="width:20px;" id="early" type="checkbox" name="lt_pmTime" value="18:00" />
                    <label class="control-text">迟到15分钟内：</label><input style="width:20px;" type="checkbox" id="delay15min" name="between_amTime" value="09:01,09:15" />
                    <button type="submit" class="button button-primary">查询>></button>
                </li>
            </ul>
        </form>
    </div>
    <div id="grid"></div>
    <div id="calendarDiv">
        <div id="s1">
            <input type="hidden" id="hide" value="" name="hide">
        </div>
        <div id="calendar"></div>
    </div>
<script>
    (function($) {
        $(function() {
            BUI.use('bui/calendar',function(Calendar){
                var form = new BUI.Form.HForm({
                    srcNode : '#J_FORM'
                }).render();

                var inputEl = $('#J_Month'),
                    monthpicker = new BUI.Calendar.MonthPicker({
                        trigger : inputEl,
                        // month:1, //月份从0开始，11结束
                        autoHide : true,
                        align : {
                            points:['bl','tl']
                        },
                        //year:2000,
                        success:function(){
                            var month = this.get('month'),
                                    year = this.get('year');
                            inputEl.val(year + '-' + (++month < 10 ? '0' + month : month));//月份从0开始，11结束
                            this.hide();
                        }
                    });
                monthpicker.render();
                monthpicker.on('show',function(ev){
                    var val = inputEl.val(),
                            arr,month,year;
                    if(val){
                        arr = val.split('-'); //分割年月
                        year = parseInt(arr[0]);
                        month = parseInt(arr[1]);
                        monthpicker.set('year',year);
                        month = --month < 10 ? '0' + month : month;
                        monthpicker.set('month', month);
                    }
                });
                var now = new Date(), year = now.getFullYear(), month = now.getMonth();
                if (month == 0) {
                    month = 12;
                    year--;
                }
                inputEl.val(year + "-" + (month < 10 ? "0" + month : month));
                var Grid = BUI.Grid,
                        Data = BUI.Data;
                var Grid = Grid,
                    Store = Data.Store,
                    columns = [
                        {title: '操作', dataIndex: 'id', width: 60, renderer: function(val, row) {
                            return edy.rendererHelp.createJavaScriptLink("edit", val, "编辑");
                        }},
                        {title: 'id', dataIndex: 'id', width: 60},
                        {title: '姓名', dataIndex: 'employee', width: 80},
                        {title: '状态', dataIndex: 'status', width: 80},
                        {title: '部门', dataIndex: 'department', width: 80},
                        {title: '打卡日期', dataIndex: 'workDate', width: 100, renderer: BUI.Grid.Format.dateRenderer},
                        {title: '上午打卡', dataIndex: 'amTime', width: 80},
                        {title: '下午打卡', dataIndex: 'pmTime', width: 80},
                        {title: '前日晚卡', dataIndex: 'yesterday', width: 80, renderer: function(val, row) {
                            if (!val) {
                                return "";
                            }
                            var result = "";
                            var arr= val.split(":");
                            if (arr[0] > 21 || (arr[0] == 21 && arr[1] >= 30)) {
                                return "<label style='color:green;'>" + val + "</label>";
                            }
                            return val;
                        }},
                        {title: '迟到', dataIndex: 'amTime', width: 80, renderer: function(val, row) {
                            if (!val) {
                                return "";
                            }
                            var result = "";
                            var amArr= val.split(":");
                            var basicAmH = 9, basicAmM = 0;
                            var amRule = (amArr[0] - basicAmH) * 60 + (amArr[1] - basicAmM);
                            if (amRule >  0) {
                                result += amRule + "分钟";
                            }
                            return result;
                        }},
                        {title: '早退', dataIndex: 'pmTime', width: 80, renderer: function(val, row) {
                            if (!val) {
                                return "";
                            }
                            var result = "";
                            var pmArr = val.split(":");
                            var basicPmH = 18, basicPmM = 0;
                            var pmRule = (basicPmH - pmArr[0]) * 60 + (basicPmM - pmArr[1]);
                            if (pmRule >  0) {
                                result += pmRule + "分钟" ;
                            }
                            return result;
                        }}
                    ];
                var store = new Store({
                    url : '/attendance/grid',
                    autoLoad:false, //自动加载数据
//                        params : $("#J_FORM").serialize(),
                    pageSize:10	// 配置分页数目
                }),
                grid = new Grid.Grid({
                    height: edy.getSuggestGridHeight(),
                    render:'#grid',
                    columns : columns,
                    loadMask: true, //加载数据时显示屏蔽层
                    store: store,
                    // 底部工具栏
                    bbar:{
                        // pagingBar:表明包含分页栏
                        pagingBar:true
                    },
                    tbar:{ //添加、删除
                        items : [{
                            btnCls : 'button button-small',
                            text : '<i class="icon-plus"></i>导入数据',
                            listeners : {
                                'click' : function() {
                                    var dialog = new top.BUI.Overlay.Dialog({
                                        title: '导入考勤数据',
                                        width:430,
                                        height:150,
                                        closeAction: "destroy",
                                        loader : {
                                            url : '/attendance/importView',
                                            autoLoad : false, //不自动加载
                                            lazyLoad : false, //不延迟加载
                                        },
                                        mask:true,
                                        success: function() {
                                            edy.loading();
                                            top.$.ajaxFileUpload({
                                                url : '/attendance/import',
                                                secureuri: false,
                                                fileElementId: "file",
                                                dataType : 'json',
                                                method : 'post',
                                                success: function (data) {
                                                    edy.loaded();
                                                    edy.alert("导入成功！");
                                                    reload();
                                                },
                                                error: function (data, status, e) {
                                                    edy.loaded();
                                                    //edy.alert("导入失败！");
                                                    reload();
                                                }
                                            });
                                            this.close();
                                        }
                                    });
                                    dialog.show();
                                    dialog.get('loader').load()
                                }
                            }
                        },
                        {
                            btnCls : 'button button-small',
                            text : '<i class="icon-remove"></i>清空数据',
                            listeners : {
                                'click' : function() {
                                    var dialog = new top.BUI.Overlay.Dialog({
                                        title: '清除月份考勤记录',
                                        width:430,
                                        height:150,
                                        closeAction: "destroy",
                                        loader : {
                                            url : '/attendance/dropView',
                                            autoLoad : false, //不自动加载
                                            lazyLoad : false, //不延迟加载
                                        },
                                        mask:true,
                                        success: function() {
                                            top.$("#saveForm").submit();
                                            this.close();
                                        }
                                    });
                                    dialog.show();
                                    dialog.get('loader').load()
                                }
                            }
                        },
                        {
                            btnCls : 'button button-small',
                            text : '<i class="icon-remove"></i>设置节假日',
                            listeners : {
                                'click' : function() {
                                    var Select = BUI.Select, select, month = $("#J_Month").val(), calendar;
                                    if (!month) {
                                        return edy.alert("请选择一个考勤月");
                                    }
                                    $.post("/attendance/getDays", {month: month}, function(data) {
                                        var items = data;
                                        select = new Select.Combox({
                                            render:'#s1',
                                            showTag:true,
                                            limit : 3, //限制最多3个
                                            width: 500,
                                            valueField : '#hide',//显示tag的Combox必须存在valueField
                                            items:items,
                                            forbitInput : true, //只能从列表中选择，输入无效,一般用于suggest中
                                            tagFormatter : function(value){ //将id : text中的id去掉
                                                var arr = value.split(':');

                                                return arr[1]; //用户输入
                                            },
                                            list : { //自定义列表
                                                textGetter : function(item){
                                                    return item.id + ':' + item.text;
                                                },
                                                idField : 'id' //默认是value
                                            }
                                        });
                                        select.render();
                                        var Calendar = BUI.Calendar;
                                        calendar = new Calendar.Calendar({
                                            render:'#calendar',
                                        });
                                        calendar.render();
                                        calendar.on('selectedchange',function (ev) {
                                            var _tmp_ = function(val) {
                                                return val > 9 ? val : "0" + val;
                                            };
                                            var date = new Date(ev.date);
                                            date = date.getFullYear() + "-" + _tmp_(date.getMonth() + 1) + "-" + _tmp_(date.getDate());
                                            select.setSelectedValue(date);
                                        });
                                    });
                                    new BUI.Overlay.Dialog({
                                        closeAction: "destroy",
                                        title: "过滤节假日",
                                        width: 580,
                                        height: 250,
                                        contentId: "calendarDiv",
                                        success: function() {
                                            var days = $("#hide").val();
                                            var tmpArr = days.split(";");
                                            for (var i = 0; i < tmpArr.length; i++) {
                                                tmpArr[i] = "\'" + tmpArr[i] + " 00:00:00\'";
                                            }
                                            $.post("/attendance/unsetDays", {days:tmpArr.join(",")}, function(data) {
                                                if (edy.ajaxHelp.handleAjax(data)) {
                                                    reload();
                                                }
                                            });
                                            $("#s1 li").remove();
                                            $("#hide").val("");
                                            select.destroy();
                                            calendar.destroy();
                                            this.close();
                                        },
                                        cancel: function() {
                                            $("#s1 li").remove();
                                            $("#hide").val("");
                                            select.destroy();
                                            calendar.destroy();
                                            this.close();
                                        }
                                    }).show();
                                }
                            }
                        },
                        {
                            btnCls : 'button button-small',
                            text : '<i class="icon-remove"></i>设置工作日',
                            listeners : {
                                'click' : function() {
                                    var Select = BUI.Select, select, month = $("#J_Month").val(), calendar;
                                    if (!month) {
                                        return edy.alert("请选择一个考勤月");
                                    }
                                    $.post("/attendance/getDays", {month: month}, function(data) {
                                        var items = data;
                                        select = new Select.Combox({
                                            render:'#s1',
                                            showTag:true,
                                            limit : 3, //限制最多3个
                                            width: 500,
                                            valueField : '#hide',//显示tag的Combox必须存在valueField
                                            items:items,
                                            forbitInput : true, //只能从列表中选择，输入无效,一般用于suggest中
                                            tagFormatter : function(value){ //将id : text中的id去掉
                                                var arr = value.split(':');

                                                return arr[1]; //用户输入
                                            },
                                            list : { //自定义列表
                                                textGetter : function(item){
                                                    return item.id + ':' + item.text;
                                                },
                                                idField : 'id' //默认是value
                                            }
                                        });
                                        select.render();
                                        var Calendar = BUI.Calendar;
                                        calendar = new Calendar.Calendar({
                                            render:'#calendar',
                                        });
                                        calendar.render();
                                        calendar.on('selectedchange',function (ev) {
                                            var _tmp_ = function(val) {
                                                return val > 9 ? val : "0" + val;
                                            };
                                            var date = new Date(ev.date);
                                            date = date.getFullYear() + "-" + _tmp_(date.getMonth() + 1) + "-" + _tmp_(date.getDate());
                                            select.setSelectedValue(date);
                                        });
                                    });
                                    new BUI.Overlay.Dialog({
                                        closeAction: "destroy",
                                        title: "过滤节假日",
                                        width: 580,
                                        height: 250,
                                        contentId: "calendarDiv",
                                        success: function() {
                                            var days = $("#hide").val();
                                            var tmpArr = days.split(";");
                                            for (var i = 0; i < tmpArr.length; i++) {
                                                tmpArr[i] = "\'" + tmpArr[i] + " 00:00:00\'";
                                            }
                                            $.post("/attendance/setDays", {days:tmpArr.join(",")}, function(data) {
                                                if (edy.ajaxHelp.handleAjax(data)) {
                                                    reload();
                                                }
                                            });
                                            $("#s1 li").remove();
                                            $("#hide").val("");
                                            select.destroy();
                                            calendar.destroy();
                                            this.close();
                                        },
                                        cancel: function() {
                                            $("#s1 li").remove();
                                            $("#hide").val("");
                                            select.destroy();
                                            calendar.destroy();
                                            this.close();
                                        }
                                    }).show();
                                }
                            }
                        },{
                            btnCls : 'button button-small',
                            text : '<i class="icon-plus"></i>分析数据',
                            listeners : {
                                'click' : function() {
                                    var Select = BUI.Select, select, month = $("#J_Month").val(), calendar;
                                    if (!month) {
                                        return edy.alert("请选择一个考勤月");
                                    }
                                    window.open("/attendance/analysis?month=" + month)
                                }
                            }
                        },{
                                btnCls : 'button button-small',
                                text : '<i class="icon-plus"></i>分析数据(新版)',
                                listeners : {
                                    'click' : function() {
                                        var Select = BUI.Select, select, month = $("#J_Month").val(), calendar;
                                        if (!month) {
                                            return edy.alert("请选择一个考勤月");
                                        }
                                        window.open("/attendance/analysis/new?month=" + month)
                                    }
                                }
                            }]
                    },
                    plugins : [Grid.Plugins.CheckSelection,Grid.Plugins.ColumnResize],
                });
                grid.render();
                var form = new BUI.Form.HForm({
                    srcNode : '#J_FORM'
                }).render();
                form.on('beforesubmit',function(ev) {
                    //序列化成对象
                    var obj = form.serializeToObject();
                    obj.start = 0; //返回第一页
                    var lastParams = store.get("lastParams");
                    lastParams["between_amTime"] = "";
                    lastParams["gt_amTime"] = "";
                    lastParams["lt_pmTime"] = "";
                    store.set("lastParams", lastParams);
                    store.load(obj);
                    return false;
                });
                var obj = form.serializeToObject();
                store.load(obj);
                var Select = BUI.Select;
                var suggest = new Select.Suggest({
                    render:'#realName',
                    name:'like_employee.name',
                    url:'/employee/suggest/name'
                });
                suggest.render();
                function getSelections() {
                    var selections = grid.getSelection();
                    var ids = [];
                    for (var key in selections) {
                        ids.push(selections[key].id);
                    }
                    return ids.join(",");
                }
                function reload() {
                    store.load();
                }
                top.reload = reload;
            });
            function edit () {
                var id = $(this).attr("data-edit");
                var dialog = new top.BUI.Overlay.Dialog({
                    title: (id && '编辑' || '新增') + '请假登记',
                    width:800,
                    height:350,
                    closeAction: "destroy",
                    loader : {
                        url : '/attendance/edit',
                        autoLoad : false, //不自动加载
                        lazyLoad : false
                    },
                    mask:true,
                    success: function() {
                        var data = top.$("#saveForm").serializeArray();
                        var action = top.$("#saveForm").attr("action");
                        $.post(action, data, function(data) {
                            if (edy.ajaxHelp.handleAjax((data))) {
                                if (edy.ajaxHelp.handleAjax((data))) {
                                    edy.alert(data.message || "操作成功");
                                    top.reload();
                                }
                            }
                        });
                        this.close();
                    }
                });
                dialog.show();
                dialog.get('loader').load({id : id})
            }
            $(document).on("click", "[data-edit]", edit);
        });
    } (jQuery));
</script>
</body>
</html>