<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<#include "../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="saveForm" class="form-horizontal" method="post" action="/department/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>部门名称：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-rules="{required:true,uniqueName:true}" name="name" value="${(entity.name)!}" />
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    BUI.use('bui/form',function  (Form) {
        Form.Rules.add({
            name : 'uniqueName',  //规则名称
            msg : '部门名称必须唯一',//默认显示的错误信息
            validator : function(value,baseValue,formatMsg,group){ //验证函数，验证值、基准值、格式化后的错误信息、goup控件
                var flag = false;
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: "/department/uniqueCheck",
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
        new Form.Form({
            srcNode : '#saveForm',
            submitType : 'ajax',
            callback : function(data){
                if (edy.ajaxHelp.handleAjax((data))) {
                    edy.alert(data.message || "操作成功");
                    top.reload();
                }
            }
        }).render();
    });

</script>
<!-- script end -->
</body>
</html>