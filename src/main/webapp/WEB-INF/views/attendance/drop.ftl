<!DOCTYPE html>
<html>
<head>
<#include "../include/resource.ftl"/>
    <style>
        .bui-monthpicker.bui-overlay{
            z-index: 9999999999999;
        }
    </style>
</head>
<body>
<form id="saveForm" action="/attendance/dropMonth" method="post" class="form-horizontal">
    <div class="control-group">
        <label class="control-label">清除月份：</label>
        <div class="controls">
            <input type="text" id="J_Month" name="month">
        </div>
    </div>
</form>
<script>
    (function($) {
        $(function() {
            var inputEl = $('#J_Month'),
                monthpicker = new BUI.Calendar.MonthPicker({
                    trigger: inputEl,
                    // month:1, //月份从0开始，11结束
                    autoHide: true,
                    align: {
                        points: ['bl', 'tl']
                    },
                    //year:2000,
                    success: function () {
                        var month = this.get('month'),
                                year = this.get('year');
                        if (!year || !month) {
                            return edy.alert("请选择一个月份");
                        }
                        inputEl.val(year + '-' + (++month < 10 ? '0' + month : month));//月份从0开始，11结束
                        this.hide();
                    }
                });
            monthpicker.render();
            monthpicker.on('show', function (ev) {
                var val = inputEl.val(), arr, month, year;
                if (val) {
                    arr = val.split('-'); //分割年月
                    year = parseInt(arr[0]);
                    month = parseInt(arr[1]);
                    monthpicker.set('year', year);
                    month = --month < 10 ? '0' + month : month;
                    monthpicker.set('month', month);
                }
            });

            var Form = BUI.Form;
            new Form.Form({
                srcNode: '#saveForm',
                submitType: 'ajax',
                callback: function (data) {
                    if (edy.ajaxHelp.handleAjax((data))) {
                        edy.alert(data.message || "操作成功");
                        top.reload();
                    }
                }
            }).render();
        });
    } (jQuery));
</script>
</body>
</html>