<!DOCTYPE html>
<html lang="en">
<head>
<#include "../../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="saveForm" class="form-horizontal" method="post" action="/attendance/leave/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>名称：</label>
                    <div class="controls">
                        <input type="text" class="control-text" data-rules="{required:true}" name="label" value="${(entity.label)!}" />
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">类型：</label>
                    <div class="controls">
                        <select id="type" name="type">
                        <#if leaveTypeEnums?? && leaveTypeEnums?size gt 0>
                            <#list leaveTypeEnums as leaveType>
                                <option value="${(leaveType.type!)}">${(leaveType.label)!}</option>
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
        $("#type").val("${(entity.type.type)!}");
    });

</script>
</body>
</html>