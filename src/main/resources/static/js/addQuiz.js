document.querySelector(".save .show-options i").addEventListener("click", function(e) {
    document.querySelector(".show-options .more").classList.toggle("active");
    e.stopPropagation();
})
window.addEventListener("click", function() {
    document.querySelector(".show-options .more").classList.remove("active");

})

let checkActivate = 0;

let add = document.querySelectorAll(".add");

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