<!DOCTYPE html>
<html lang="en">
<head>
<#include "../include/resource.ftl"/>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="span24">
            <form id="saveForm" class="form-horizontal" method="post" action="/leave/save">
                <input type="hidden" name="id" value="${(entity.id)!}"/>
                <div class="control-group">
                    <label class="control-label"><s>*</s>姓名：</label>
                    <div class="controls">
                        <#if (entity.employee.id)??>
                            <input type="text" readonly value="${(entity.employee.name)!}"/>
                            <input type="hidden" name="employeeId" value="${(entity.employee.id)!}"/>
                        <#else>
                            <div id="s1" data-rules="{required:true}">

                            </div>
                        </#if>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><s>*</s>类型：</label>
                    <div class="controls">
                        <select id="type" name="type">
                        <#list typeCustomList as type>
                            <option value="${(type.id)!}">${(type.label)!}</option>
                        </#list>
                        </select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><s>*</s>开始时间：</label>
                    <div class="controls">
                        <#if (entity.beginDate)??>
                            <input type="text" data-rules="{required:true}" class="calendar calendar-time" name="beginDate" value="${(entity.beginDate)?string("Y-MM-dd HH:mm:ss")}" />
                        <#else>
                            <input type="text" data-rules="{required:true}" class="calendar calendar-time" name="beginDate"  />
                        </#if>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><s>*</s>结束时间：</label>
                    <div class="controls">
                    <#if (entity.endDate)??>
                        <input type="text" data-rules="{required:true}" class="calendar calendar-time" name="endDate" value="${(entity.endDate)?string("Y-MM-dd HH:mm:ss")}" />
                    <#else>
                        <input type="text" data-rules="{required:true}" class="calendar calendar-time" name="endDate" />
                    </#if>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">备注：</label>
                    <div class="controls control-row-auto">
                        <textarea name="remark" class="control-row4 input-large">${(entity.remark)!}</textarea>
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
        var Select = BUI.Select;
        var suggest = new Select.Suggest({
            render:'#s1',
            name:'name',
            url:'/employee/suggest/name'
        });
        suggest.render();
        $("[name='name']").val("${(entity.employee.name)!}");
        $("#type").val("${(entity.leaveTypeCustome.id)!}");
    });
</script>
<!-- script end -->
</body>
</html>