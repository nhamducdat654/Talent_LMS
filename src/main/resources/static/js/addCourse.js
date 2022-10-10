let course_name = document.getElementById("txtName");

course_name.addEventListener("input", () => {
    let count = course_name.value.length;
    document.getElementById("rangeInput").value = 100 - count;
});

let btnDown = document.getElementById("btn_down");

let course_nam1e = document.getElementById("icon");

course_nam1e.addEventListener("click", () => {
    document.getElementById("inputPrice").style.display = "block";
    document.getElementById("icon").style.display = "none";
    document.getElementById("waring2").style.display = "block";
});

document.getElementById("btn_Price").addEventListener("click", () => {
    document.getElementById("icon").style.display = "block";
    document.getElementById("inputPrice").style.display = "none";
    document.getElementById("waring2").style.display = "none";
});

var popoverTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="popover"]')
);
var popoverList = popoverTriggerList.map(function(popoverTriggerEl) {
    return new bootstrap.Popover(popoverTriggerEl);
});

var popover = new bootstrap.Popover(
    document.querySelector(".example-popover"), {
        container: "hover",
    }
);

function userClick() {
    document.getElementById("all_course").style.display = "none";
    document.getElementById("all_user").style.display = "block";
}

function courseClick() {
    document.getElementById("all_course").style.display = "block";
    document.getElementById("all_user").style.display = "none";
}