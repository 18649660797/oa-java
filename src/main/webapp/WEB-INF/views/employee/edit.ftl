<!DOCTYPE html>
<html lang="en">
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="saveForm" class="form-horizontal" method="post" action="/employee/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>姓名：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-rules="{required:true}" name="name" value="${(entity.name)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">考勤号：</label>
                    <div class="controls">
                        <input type="text" class="control-text" name="attendanceCN" data-rules="{required:true,uniqueCheck:true}" value="${(entity.attendanceCN)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">部门：</label>
                    <div class="controls">
                        <select id="department" name="department">
                            <option value="">全部</option>
                        <#if departmentList?? && departmentList?size gt 0>
                            <#list departmentList as department>
                                <option value="${(department.id)!}">${(department.name)!}</option>
                            </#list>
                        </#if>
                        </select>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    BUI.use('bui/form',function  (Form) {
        Form.Rules.add({
            name : 'uniqueCheck',  //规则名称
            msg : '部门名称必须唯一',//默认显示的错误信息
            validator : function(value,baseValue,formatMsg,group){ //验证函数，验证值、基准值、格式化后的错误信息、goup控件
                var flag = false;
                $.ajax({
                    type: "post",
                    dataType: "json",
                    url: "/employee/uniqueCheck",
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
        $("#department").val("${(entity.department.id)!}");
    });

</script>
<!-- script end -->
</body>
</html>