<!DOCTYPE html>
<html lang="en">
<head>
<#include "../../include/resource.ftl"/>
</head>
<body>
    <div class="row">
        <form id="J_FORM" class="form-panel" action="data" method="post" style="margin-bottom:0;">
            <div class="panel-title">
            <span>
                <label>名称：</label>
                <input type="text" name="like_name" value="">
                <label>状态：</label>
                <select name="eq_status">
                    <option value="">全部</option>
                    <option value="1">启用</option>
                    <option value="0">关闭</option>
                </select>
                <label>类型：</label>
                <select name="eq_type" style="width: 100x;">
                    <option value="">全部</option>
                    <option value="0">上班打卡时间设置</option>
                    <option value="1">下班打卡时间设置</option>
                </select>
            </span>
            </div>
            <ul class="panel-content">
                <li>
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
                var Grid = BUI.Grid,
                        Data = BUI.Data;
                var Grid = Grid,
                    Store = Data.Store,
                    columns = [
                        {title: '操作', dataIndex: 'id', width: 60, renderer: function(val, row) {
                            return edy.rendererHelp.createJavaScriptLink("edit", val, "编辑");
                        }},
                        {title: 'id', dataIndex: 'id', width: 60},
                        {title: '名称', dataIndex: 'name', width: 150},
                        {title: '状态', dataIndex: 'status', width: 80},
                        {title: '类型', dataIndex: 'type', width: 150},
                        {title: '开始日期', dataIndex: "beginDate", width: 100, renderer: BUI.Grid.Format.dateRenderer},
                        {title: '结束日期', dataIndex: 'endDate', width: 100, renderer: BUI.Grid.Format.dateRenderer}
                    ];
                var store = new Store({
                    url : '/attendance/rule/grid',
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
                            text : '<i class="icon-plus"></i>新增',
                            listeners : {
                                'click' : edit
                            }
                        },{
                            btnCls : 'button button-small',
                            text : '<i class="icon-remove"></i>删除',
                            listeners : {
                                'click' : function() {
                                    var ids = getSelections();
                                    if (!ids) {
                                        return edy.alert("至少选择一个记录");
                                    }
                                    edy.confirm("确认要删除选中的规则?", function() {
                                        $.post("/attendance/rule/delete", {ids: ids}, function(data) {
                                            if (edy.ajaxHelp.handleAjax(data)) {
                                                edy.alert("删除成功！");
                                                reload();
                                            }
                                        });
                                    });
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
                    store.load(obj);
                    return false;
                });
                var obj = form.serializeToObject();
                store.load(obj);
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
                    title: (id && '编辑' || '新增') + '高级考勤规则',
                    width:500,
                    height:400,
                    closeAction: "destroy",
                    loader : {
                        url : '/attendance/rule/edit',
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