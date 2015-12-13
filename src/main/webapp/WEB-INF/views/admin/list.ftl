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
            </li>
            <li>
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
                    {title: '操作', dataIndex: 'id', width: 100, renderer: function(val, row) {
//                        return edy.rendererHelp.createLink("/admin/edit?id=" + val, "编辑");
                        return "<a href='javascript:void(0);' data-edit='" + val + "'>编辑</a>";
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
                                BUI.Message.Confirm("确认要删除选中的账号：" + getSelectionNames(), function() {
                                    $.post("/admin/delete", {ids: ids}, function(data) {
                                        if (edy.ajaxHelp.handleAjax(data)) {
                                            edy.confirm("删除成功！");
                                            reload();
                                        }
                                    });
                                }, "question");
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
            BUI.use(['bui/overlay','bui/mask'],function(Overlay){
                $(document).on("click", "[data-edit]", edit);
            });
            function edit () {
                var id = $(this).attr("data-edit");
                var dialog = new top.BUI.Overlay.Dialog({
                    title: (id && '编辑' || '新增') + '管理员',
                    width:800,
                    height:400,
                    closeAction: "destroy",
                    loader : {
                        url : '/admin/edit',
                        autoLoad : false, //不自动加载
//                            params : {id : id},//附加的参数
                        lazyLoad : false, //不延迟加载
                        /*, //以下是默认选项
                        dataType : 'text',   //加载的数据类型
                        property : 'bodyContent', //将加载的内容设置到对应的属性
                        loadMask : {
                          //el , dialog 的body
                        },
                        lazyLoad : {
                          event : 'show', //显示的时候触发加载
                          repeat : true //是否重复加载
                        },
                        callback : function(text){
                          var loader = this,
                            target = loader.get('target'); //使用Loader的控件，此处是dialog
                          //
                        }
                        */
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