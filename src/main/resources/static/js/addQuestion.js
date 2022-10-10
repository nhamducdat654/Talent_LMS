window.addEventListener("click", function (e) {
    let more = document.querySelector(".more");
    more.classList.remove("active");
})
let moreBtn = document.querySelector(".show-options i");
moreBtn.addEventListener("click", function (e) {
    e.stopPropagation();
    let more = document.querySelector(".more");
    more.classList.toggle("active");
})