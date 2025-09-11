$(document).ready(function() {

    duration = 777;

    function toggle_button_text(b) {
	if (b.textContent == "Hide details") {
	    b.textContent = "Show details";
	} else {
	    b.textContent = "Hide details";
	}
    }

    $("button.collapser").click(function(event) {
	sibling = $(this).next("div.collapsable");
	sibling.slideToggle(duration);
	toggle_button_text(this);
    });

});
