let box = document.querySelectorAll(".box");

let circle = document.querySelectorAll(".circle");
let checkmark = document.querySelectorAll(".checkmark");
let checkBox = document.querySelectorAll(".answer input");
let questionWrap = document.querySelectorAll(".question-wrap");
let btnSubmit = document.querySelectorAll(".submit .btn-start");
questionWrap.item(0).classList.add("active");
let totalPage = document.querySelectorAll(".total-page");
let currentPage = document.querySelectorAll(".current-page");
totalPage.item(0).innerHTML = btnSubmit.length;
currentPage.item(0).innerHTML = "1";
btnSubmit.forEach(function (item, index) {
    if (index == btnSubmit.length - 1) {
        item.innerHTML = "Submit test";
    }
    item.addEventListener("click", function (e) {
        let checkInputCheck = document.querySelectorAll(".question-wrap.active .answer input");
        checkInputCheck.forEach(function (itemC, indexC) {
            if (itemC.checked) {
                if (index < btnSubmit.length - 1) {
                    totalPage.item(index + 1).innerHTML = btnSubmit.length;
                    currentPage.item(index + 1).innerHTML = index + 2;
                    questionWrap.item(index).classList.remove("active");
                    questionWrap.item(index + 1).classList.add("active");
                } else {
                    item.innerHTML = "Submit test";
                    getInputChecked();
                }
            } else {
                item.innerHTML = "Please choose answer";
            }
        })
    })
})

//get input checked
function getInputChecked() {
    let newArray = [];

    checkBox.forEach(function (itemC, indexC) {
        if (itemC.checked) {
            newArray.push(itemC.getAttribute("value"));
        }
    })
    location.href = "resultQuiz?txtArray=" + newArray + "&quizid=" + document.querySelector(".quizid").getAttribute("value") + "&txtMaxIndex=" + document.querySelector(".maxIndex").getAttribute("value");
}