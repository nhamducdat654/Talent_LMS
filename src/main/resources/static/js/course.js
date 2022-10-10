$(document).click(function() {
    $(".course__1 .sub_menu").removeClass("active");
})

$(".course__1 .add .btn-primary").click(function(e) {
    e.stopPropagation();
    $(".course__1 .sub_menu").toggleClass("active");
})

$(".course__1 .btn_sub_menu").click(function(e) {
    e.stopPropagation();
    $(".course__1 .sub_menu").toggleClass("active");
})