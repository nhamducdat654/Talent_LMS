let up__option = document.querySelector(".up__option");
up__option.addEventListener("click", function(e) {
    e.preventDefault();
    e.stopPropagation();
    document.querySelector(".option__item").classList.toggle("active");
})

document.addEventListener("click", function() {
    document.querySelector(".option__item").classList.remove("active");
})