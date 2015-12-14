<!DOCTYPE html>
<html lang="en">
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<div class="row">
    <form id="J_FORM" class="form-panel" action="data" method="post" style="margin-bottom:0;">
        <div class="panel-title">
            <span>
                <label>姓名：</label>
                <input type="text" class="control-text" name="like_employee.name" value="" />
                <label>开始时间：</label><input name="ge_beginDate" type="text" class="calendar" /> <label>至</label> <input name="le_beginDate" type="text" class="calendar" />
                <label>结束时间：</label><input name="ge_endDate" type="text" class="calendar" /> <label>至</label> <input name="le_endDate" type="text" class="calendar" />
            </span>
        </div>
        <ul class="panel-content">
            <li>
                <label>假别：</label>
                <select name="eq_type">
                    <option value="">全部</option>
                    <option value="1">事假</option>
                    <option value="2">病假</option>
                    <option value="3">调休</option>
                    <option value="4">外出</option>
                    <option value="5">丧假</option>
                    <option value="6">年假</option>
                    <option value="7">婚假</option>
                    <option value="8">产假</option>
                </select>

                <label>部门：</label>
                <select name="eq_employee.department.id">
                    <option value="">全部</option>
                <#if departmentList?? && departmentList?size gt 0>
                    <#list departmentList as department>
                        <option value="${(department.id)!}">${(department.name)!}</option>
                    </#list>
                </#if>
                </select>
                <button type="submit" class="button button-primary">查询>></button>
            </li>
        </ul>
    </form>
</div>
<div id="grid"></div>
<script>
    (function($) {
        $(function() {
            var Grid = BUI.Grid, Data = BUI.Data;
            var editing = new Grid.Plugins.CellEditing({
                triggerSelected : false //触发编辑的时候不选中行
            });
            var Grid = Grid,
                Store = Data.Store,
                columns = [
                    {title: 'id', dataIndex: 'id', width: 60, renderer: function(val, row) {
                        return "<a href='javascript:void(0);' data-edit='" + val + "'>编辑</a>";
                    }},
                    {title: '姓名', dataIndex: 'realName', width: 60},
                    {title: '部门', dataIndex: 'department', width: 60},
                    {title: '类型', dataIndex: 'type', width: 60},
                    {title: '开始日期', dataIndex: 'beginDate', width: 150, renderer: BUI.Grid.Format.datetimeRenderer},
                    {title: '结束时间', dataIndex: 'endDate', width: 150, renderer: BUI.Grid.Format.datetimeRenderer},
                    {title: '备注', dataIndex: 'remark', width: 150}
                ];
            var store = new Store({
                url : '/leave/grid',
                autoLoad:false, //自动加载数据
//                        params : $("#J_FORM").serialize(),
                pageSize:10	// 配置分页数目
            }),
            grid = new Grid.Grid({
                height: 450,
                render:'#grid',
                columns : columns,
                loadMask: true, //加载数据时显示屏蔽层
                store: store,
                // 底部工具栏
                bbar:{
                    // pagingBar:表明包含分页栏
                    pagingBar:true,
                },
                tbar:{ //添加、删除
                    items : [{
                        btnCls : 'button button-small',
                        text : '<i class="icon-plus"></i>添加',
                        listeners : {
                            'click' : edit
                        }
                    },
                    {
                        btnCls : 'button button-small',
                        text : '<i class="icon-remove"></i>删除',
                        listeners : {
                            'click' : function() {
                                var ids = getSelections();
                                if (!ids) {
                                    return edy.alert("至少选择一个记录");
                                }
                                edy.confirm("确认要删除选中的账号?", function() {
                                    $.post("/leave/delete", {ids: ids}, function(data) {
                                        if (edy.ajaxHelp.handleAjax(data)) {
                                            edy.confirm("删除成功！");
                                            reload();
                                        }
                                    });
                                });
                            }
                        }
                    }, {
                        btnCls : 'button button-small',
                        text : '<i class="icon-remove"></i>清空月份记录',
                        listeners : {
                            'click' : function() {
                                location.href = "/leave/drop"
                            }
                        }
                    }, {
                            btnCls : 'button button-small',
                            text : '<i class="icon-plus"></i>导入',
                            listeners : {
                                'click' : function() {
                                    var dialog = new top.BUI.Overlay.Dialog({
                                        title: '导入请假外出登记',
                                        width:430,
                                        height:150,
                                        closeAction: "destroy",
                                        loader : {
                                            url : '/leave/importView',
                                            autoLoad : false, //不自动加载
                                            lazyLoad : false, //不延迟加载
                                        },
                                        mask:true,
                                        success: function() {
                                            top.$.ajaxFileUpload({
                                                url : '/leave/import',
                                                secureuri: false,
                                                fileElementId: "file",
                                                dataType : 'json',
                                                method : 'post',
                                                success: function (data) {
                                                    edy.alert("导入成功！");
                                                    reload();
                                                },
                                                error: function (data, status, e) {
                                                    edy.alert("导入失败！");
                                                }
                                            });
                                            this.close();
                                        }
                                    });
                                    dialog.show();
                                    dialog.get('loader').load()
                                }
                            }
                        }]
                },
                plugins : [editing,Grid.Plugins.CheckSelection],
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
            function edit () {
                var id = $(this).attr("data-edit");
                var dialog = new top.BUI.Overlay.Dialog({
                    title: (id && '编辑' || '新增') + '请假登记',
                    width:800,
                    height:400,
                    closeAction: "destroy",
                    loader : {
                        url : '/leave/edit',
                        autoLoad : false, //不自动加载
                        lazyLoad : false
                    },
                    mask:true,
                    success: function() {
                        top.$("#saveForm").submit();
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