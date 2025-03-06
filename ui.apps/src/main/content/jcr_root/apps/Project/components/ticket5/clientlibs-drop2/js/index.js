(function(document, $, Coral) {
    "use strict";

    $(document).on("foundation-contentloaded", function(e) {
        $(".cmp-list__editor coral-select.cq-dialog-dropdown-showhide", e.target).each(function(i, element) {
            var target = $(element).data("cq-dialog-dropdown-showhide-target");
            if (target) {
                Coral.commons.ready(element, function(component) {
                    showHideTab(component, target);
                    component.on("change", function() {
                        showHideTab(component, target);
                    });
                });
            }
        });

        $(".cq-dialog-dropdown-showhide", e.target).each(function(i, element) {
            var target = $(element).data("cq-dialog-dropdown-showhide-target");
            if (target) {
                showHideTab(element, target);
            }
        });
    });

    function showHideTab(component, target) {
        var value = component.value;

        $(target).not(".hide").addClass("hide");

        var tabsContent = $(target).closest("coral-panelstack");
        var tabsList = tabsContent.siblings("coral-tablist");

        tabsContent.children().each(function(i, element) {
            var tabShowValue = $(element).find("div").attr("data-showhidetargetvalue");

            if (checkUndefined(tabShowValue)) {
                if (value === tabShowValue) {
                    var tabIDShow = $(element).attr("aria-labelledby");

                    $(element).attr("selected", "");
                    $(tabsList).children("[id='" + tabIDShow + "']").removeClass("hide");
                } else {
                    var tabIDHide = $(element).attr("aria-labelledby");

                    $(tabsList).children("[id='" + tabIDHide + "']").addClass("hide");
                }
            }
        });
    }

    function checkUndefined(target) {
        return typeof target !== "undefined";
    }

})(document, Granite.$, Coral);
