document.querySelector(".save .show-options i").addEventListener("click", function(e) {
    document.querySelector(".show-options .more").classList.toggle("active");
    e.stopPropagation();
})
window.addEventListener("click", function() {
    document.querySelector(".show-options .more").classList.remove("active");

})
document.querySelector(".save .manager .delete").addEventListener("click", function(e) {
    e.preventDefault();
    document.querySelector(".delete-confirm").classList.add("active");
    document.querySelector(".delete-confirm .delete-form").classList.add("active");
})

document.querySelector(".delete-confirm  .delete-form .head i").addEventListener("click", function() {
    document.querySelector(".delete-confirm").classList.remove("active");
    document.querySelector(".delete-confirm .delete-form").classList.remove("active");
})

document.querySelector(".delete-confirm ").addEventListener("click", function() {
    document.querySelector(".delete-confirm").classList.remove("active");
    document.querySelector(".delete-confirm .delete-form").classList.remove("active");

})
document.querySelector(".delete-confirm  .delete-form").addEventListener("click", function(e) {
    e.stopPropagation();
})

document.querySelector(".delete-confirm  .delete-form .manager .cancel").addEventListener("click", function(e) {
    document.querySelector(".delete-confirm").classList.remove("active");
    document.querySelector(".delete-confirm .delete-form").classList.remove("active");
    e.preventDefault();
})

let checkActivate = 0;

let add = document.querySelectorAll(".add");
let checkMark = document.querySelectorAll(".checkmark");
let checkBox = document.querySelectorAll("td input");

add.forEach(function(item, index) {
    item.addEventListener('click', function(e) {
        add[index].classList.toggle("active");
        checkActivate++;
        if (add[index].getAttribute("class").includes("active")) {
            add[index].innerHTML = "Added";
        } else {
            add[index].innerHTML = "Add";
        }
        e.preventDefault();
    });
});