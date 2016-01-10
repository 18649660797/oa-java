<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理员列表</title>
    <#include "../include/resource.ftl"/>
</head>
<body>
<div class="row">
    <form id="J_FORM" class="form-panel" action="data" method="post">
        <ul class="panel-content">
            <li>
                <label>名称：</label>
                <input type="text" class="control-text" name="like_name" value="" />
                <button type="submit" class="button button-primary">查询>></button>
            </li>
        </ul>
    </form>
</div>
<div id="grid"></div>
<script>
    (function($) {
        $(function() {
            var Grid = BUI.Grid,
                    Data = BUI.Data;
            var Grid = Grid,
                Store = Data.Store,
                columns = [
                    {title: 'ID', dataIndex: 'id', width: 80},
                    {title: '名称', dataIndex: 'name', width: 60},
                    {title: '操作', dataIndex: 'id', width: 150, renderer: function(val, row) {
                        var html = edy.rendererHelp.createJavaScriptLink("edit", val, "编辑");
                        html += "  " + edy.rendererHelp.createJavaScriptLink("delete", val, "删除");
                        html += "  " + edy.rendererHelp.createJavaScriptLink("set-pwd", val, "修改密码");
                        return html;
                    }}
                ];
            var store = new Store({
                url : '/admin/grid',
                params: {gt_id: 0},
                autoLoad:true, //自动加载数据
                pageSize:30	// 配置分页数目
            }),
            editing = new Grid.Plugins.CellEditing({
                triggerSelected : false //触发编辑的时候不选中行
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
                                edy.confirm("确认要删除选中的账号：" + getSelectionNames(), function() {
                                    $.post("/admin/delete", {ids: ids}, function(data) {
                                        if (edy.ajaxHelp.handleAjax(data)) {
                                            edy.confirm("删除成功！");
                                            reload();
                                        }
                                    });
                                });
                            }
                        }
                    }]
                },
                plugins : [editing,Grid.Plugins.CheckSelection,Grid.Plugins.ColumnResize],
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
            function getSelections() {
                var selections = grid.getSelection();
                var ids = [];
                for (var key in selections) {
                    ids.push(selections[key].id);
                }
                return ids.join(",");
            }
            function getSelectionNames() {
                var selections = grid.getSelection();
                var ids = [];
                for (var key in selections) {
                    ids.push(selections[key].name);
                }
                return ids.join(",");
            }
            function reload() {
                store.load();
            }
            top.reload = reload;
            $(document).on("click", "[data-edit]", edit).on("click", "[data-delete]", function() {
                var id = $(this).attr("data-delete");
                edy.confirm("确定删除？", function() {
                    $.post("/admin/delete", {ids: id}, function(data) {
                        if (edy.ajaxHelp.handleAjax(data)) {
                            edy.alert("操作成功！");
                            reload();
                        }
                    });
                })
            }).on("click", "[data-set-pwd]", function() {
                var id = $(this).attr("data-set-pwd");
                var dialog = new top.BUI.Overlay.Dialog({
                    title: '管理员密码重设',
                    width:450,
                    height:250,
                    closeAction: "destroy",
                    loader : {
                        url : '/admin/viewPwdSet',
                        autoLoad : false, //不自动加载
                        lazyLoad : false, //不延迟加载
                    },
                    mask:true,
                    success: function() {
                        top.$("#pwdForm").submit();
                        this.close();
                    }
                });
                dialog.show();
                dialog.get('loader').load({id : id})
            });
            function edit () {
                var id = $(this).attr("data-edit");
                var dialog = new top.BUI.Overlay.Dialog({
                    title: (id && '编辑' || '新增') + '管理员',
                    width:800,
                    height:600,
                    closeAction: "destroy",
                    loader : {
                        url : '/admin/edit',
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
                dialog.get('loader').load({id : id})
            }
        });
    } (jQuery));
</script>
</body>
</html>