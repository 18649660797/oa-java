<!DOCTYPE html>
<html lang="en">
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="saveForm" class="form-horizontal" method="post" action="/admin/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>账号：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-rules="{required:true,uniqueName:true}" name="name" value="${(entity.name)!}" />
                    </div>
                </div>
                <#if entity?has_content == false>
                <div class="control-group">
                    <label class="control-label">密码：</label>
                    <div class="controls">
                        <input type="password" class="control-text" name="password" value="${(entity.password)!}" />
                    </div>
                </div>
                </#if>
                <div class="control-group">
                    <label class="control-label">权限：</label>
                    <div class="controls">
                        <div id="t1"></div>
                        <input type="hidden" name="groups" id="groups"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    BUI.use(['bui/form', 'bui/tree'],function  (Form) {
        Form.Rules.add({
            name : 'uniqueName',  //规则名称
            msg : '管理员名称必须唯一',//默认显示的错误信息
            validator : function(value,baseValue,formatMsg,group){ //验证函数，验证值、基准值、格式化后的错误信息、goup控件
                var flag = false;
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: "/admin/uniqueCheck",
                    data: {name: $.trim(value), id: "${(entity.id)!}"},
                    success: function(data) {
                        flag = data && data.result
                    },
                    async: false
                });
                if (!flag) {
                    return formatMsg;
                }
            }
        });
        var form = new Form.Form({
            srcNode : '#saveForm',
            submitType : 'ajax',
            callback : function(data){
                if (edy.ajaxHelp.handleAjax((data))) {
                    edy.alert(data.message || "操作成功");
                    top.reload();
                }
            }
        });
        form.render();
        window.submit = form.submit;
        var tree;
        $.get("/permission/simpleTree?gt_id=0", function (data) {
            var data = data && data.data || [];
            var store = new BUI.Data.TreeStore({
                pidField : 'pid', //设置pid的字段名称
                data : data,
                root : {
                    id : '0',
//                    text : '0'
                }
                // 这边是使用异步加载的方式，每次只加载一级分类，点击节点后继续加载
//                    ,url: "/admin/adminProduct/category/simpleTreeAjax",
//                    autoLoad: true
            });
            //由于这个树，不显示根节点，所以可以不指定根节点
            tree = new BUI.Tree.TreeList({
                render : '#t1',
                width: 200,
                height: 200,
                store : store,
                showLine : true, //显示连接线
                checkType: 'all', //checkType:勾选模式，提供了4中，all,onlyLeaf,none,custom
                cascadeCheckd : false //不级联勾选
//                ,dirCls : 'icon-pkg', leafCls: 'icon-example' // 可自定义图标
            });
            tree.render();
            var permissionIds = "${permissions!}";
            var tmpArr = store.get("data");
            var arr = permissionIds.split(",");
            for (var i = 0; i < arr.length; i++) {
                tree.setChecked(tree.findNode(arr[i]));
            }
            for (var i = 0; i < tmpArr.length; i++) {
                var node = tmpArr[i];
                if (permissionIds.indexOf(node.id) > -1) {
                    node.checkable = true;
                    tree.setChecked(node);
                }
            }
            // 手动展开所有子节点
            for (var key in data) {
                var node = data[key]
                store.load({id: node.id, pid: node.id});
            }
            tree.set("cascadeCheckd", true);
        });
        form.on("beforesubmit", function() {
            var checkedNodes = tree.getCheckedNodes();
            var ids = [];
            var json = {};
            BUI.each(checkedNodes,function(node){
                var id = node.id;
                var flag = true;
                BUI.each(checkedNodes,function(node){
                    if (node.pid == id) {
                        flag = false;
                    }
                });
                // 过滤掉父节点
//                if (flag) {
                if (!json[id]) {
                    ids.push(id);
                    json[id] = 1;
                }
                if (!json[node.pid]) {
                    ids.push(node.pid);
                    json[node.pid] = 1;
                }
//                }
                $("#groups").val(ids.join(","));
            });
        });
    });
</script>
<!-- script end -->
</body>
</html>