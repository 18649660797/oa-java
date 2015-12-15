(function(w, $) {
    function edy() {

    }
    edy.alert = function(msg) {
        return w.BUI && w.BUI.Message && w.BUI.Message.Alert && w.BUI.Message.Alert(msg) || alert(msg);
    };

    edy.confirm = function(msg, callback) {
        return w.BUI && w.BUI.Message && w.BUI.Message.Confirm && w.BUI.Message.Confirm(msg, callback) || (function(m, cb) {
            var result = confirm(msg);
            if (result && typeof callback === "function") {
                callback();
            }
        } ());
    };
    edy.ajaxHelp = {
        handleAjax: function(data) {
            if (!data || !data.result) {
                edy.alert(data.message);
                return false;
            }
            return true;
        }
    };
    edy.rendererHelp = {
        createLink: function(href, text) {
            return "<a href='{0}'>{1}</a>".replace("{0}", href || "").replace("{1}", text || "");
        }
    };
    w.edy = edy;
} (window, jQuery));