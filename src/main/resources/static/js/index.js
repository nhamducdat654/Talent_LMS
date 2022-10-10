$(document).ready(function () {
    let checkPage = document.querySelector('main');
    if (checkPage.classList.contains('listUser')) {
        let tagName = $('.users__tab .users__tab-title');
        let tagContent = $('.tag__content .tag__content-list');
        tagName.click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            let index = $(this).index();
            tagContent.eq(index).addClass('active').siblings().removeClass('active');
        })
    }

    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#blah')
                        .attr('src', e.target.result)
                        .width(150)
                        .height(200);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }

    $("#files").change(function () {
        filename = this.files[0].name
        console.log(filename);
    });


    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });

    function activeCheck() {
        var checkBox = document.getElementById("activeCheckBox");
        var text = document.getElementById("activeCheck");
        if (checkBox.checked == true) {
            text.style.display = "block";
        } else {
            text.style.display = "none";
        }
    }
    function deactivateCheck() {
        var checkBox = document.getElementById("deactivateCheckBox");
        var text = document.getElementById("deactivateCheck");
        if (checkBox.checked == true) {
            text.style.display = "block";
        } else {
            text.style.display = "none";
        }
    }
});



var flag = 0;
let viewShow = $(".course__view button");
$(viewShow).click(function (e) {
    viewShow.removeClass("active");
    $(this).addClass("active");
    flag = 0;
    if ($(this).index() == 1) {
        flag = 1;
        $(".course__content-list").addClass("active");
        $(".course-list").addClass("active");
    } else {
        $(".course__content-list").removeClass("active");
        $(".course-list").removeClass("active");
    }
    searchCourse(flag);
});


$(".course__sort").click(function (e) {
    e.stopPropagation();
    $(".course__sort-current").toggleClass("active");
    $(".course__sort-option").toggleClass("active");

});
$(document).click(function () {
    $(".course__sort-current").removeClass("active");
    $(".course__sort-option").removeClass("active");
    $(".editCourse .dropdown_role").removeClass("active");
})

$(".course__sort-option a").click(function (e) {
    let chooseCurrent = $(this).text();
    $(".course__sort-option a").removeClass("active");
    $(this).addClass("active");
    $(".course__sort-current span").text(chooseCurrent);
});
$(".categories__item").click(function (e) {
    e.preventDefault();
    $(this).toggleClass("active");
});

//home slideUp slideDown

$(document).on("click", ".home .list-category", function () {
    $(this).next().slideToggle();
    $(this).toggleClass("active");
    $(this).next().toggleClass("rotate");
});

// course check all
$(".course__list tr th .course__check").click(function (e) {
    if ($(".course__list tr th .course__check").is(":checked")) {
        $(".course__list tr td .course__check").prop('checked', true);
    } else {
        $(".course__list tr td .course__check").prop('checked', false);
    }
});

$(".course__list .course__check").click(function (e) {
    $(".--another-option").addClass("active");
    if (!$(".course__list .course__check").is(":checked")) {
        $(".--another-option").removeClass("active");
    }
});

$(".course__list tr td .course__check").click(function (e) {
    if (!$(".course__list tr td .course__check").is(":checked")) {
        $(".course__list tr th .course__check").prop('checked', false);
        $(".--another-option").removeClass("active");
    }
});

$(".mass-actions").click(function (e) {
    e.stopPropagation();
    $(".mass-actions__another").toggleClass("active");
});

// -----------EditCOURSE
$("#role_value").click(function (e) {
    e.stopPropagation();
    $(".editCourse .dropdown_role").toggleClass("active");
})
$(".change__role-item").click(function (e) {
    let value__drop = $(this).text();
    let role_value = $("#role_value a").text();
    $("#role_value a").text(value__drop);
    $(this).text(role_value);
    $(".editCourse .dropdown_role").removeClass("active");
})

// Search My Course
function searchMyCourse() {
    var input, filter, li, a, i, txtValue;
    input = document.getElementById("searchMyCourse");
    filter = input.value.toUpperCase();
    li = document.getElementsByClassName("course__content-item");
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByClassName("course-name")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "block";
        } else {
            li[i].style.display = "none";
        }
    }
}

//Search Course in catalog

function keyupSearchCourse() {
    searchCourse(flag);
}

function searchCourse(flag) {
    var input, filter, li, a, i, txtValue;
    input = document.getElementById("searchCourse");
    filter = input.value.toUpperCase();
    li = document.getElementsByClassName("course-detail");

    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByClassName("course__title")[0];
        txtValue = a.textContent || a.innerText;

        if (flag == 1) {
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                li[i].style.display = "grid";
            } else {
                li[i].style.display = "none";
            }
        } else {
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                li[i].style.display = "block";
            } else {
                li[i].style.display = "none";
            }
        }
    }
}

let element = document.querySelectorAll(".course__view button");
if (element.length > 0) {
    element[1].addEventListener("click", function () {
        searchCourse("1");
    });
}


$(".enroll").click(function (e) {
    let newArray = [];
    $("td .course__check:checked").each(function (i) {
        newArray.push($(this).val());
    })
    location.href = "enroll?txtArray=" + newArray + "&txtCount=" + document.getElementById("txtCount").innerHTML;
});

$(".unEnroll").click(function (e) {
    let newArray = [];
    $("td .course__check:checked").each(function (i) {
        newArray.push($(this).val());
    })
    location.href = "unEnroll?txtArray=" + newArray + "&txtCount=" + document.getElementById("txtCount").innerHTML;
});


$(".activate").click(function (e) {
    let newArray = [];
    $("td .course__check:checked").each(function (i) {
        if ($(this).val().toString().includes("I")) {
            newArray.push($(this).val().toString().substring(0, $(this).val().toString().length - 1));
        } else {
            newArray.push($(this).val());
        }


    })
    location.href = "active?txtArray=" + newArray + "&txtCount=" + document.getElementById("txtCount").innerHTML;
});

$(".deActivate").click(function (e) {
    let newArray = [];
    $("td .course__check:checked").each(function (i) {
        if ($(this).val().toString().includes("I")) {
            newArray.push($(this).val().toString().substring(0, $(this).val().toString().length - 1));
        } else {
            newArray.push($(this).val());
        }
    })
    location.href = "deActive?txtArray=" + newArray + "&txtCount=" + document.getElementById("txtCount").innerHTML;
});

$(".deleteCourses").click(function (e) {
    let newArray = [];
    $("td .course__check:checked").each(function (i) {
        if ($(this).val().toString().includes("I")) {
            newArray.push($(this).val().toString().substring(0, $(this).val().toString().length - 1));
        } else {
            newArray.push($(this).val());
        }
    })
    location.href = "deleteCourses?txtArray=" + newArray + "&txtCount=" + document.getElementById("txtCount").innerHTML;
});

$(".countAc").click(function (e) {
    let count = 0;
    $("td .course__check:checked").each(function (i) {
        if ($(this).val().toString().includes("I")) {
            count++;
        }
    })
    if (count <= 1) {
        document.getElementById("activeModelBody").innerHTML = `
                <p>Are you sure you want to activate these courses?</p>
                <p style="display: block">${count} course will be affected by this action </p>
                `
    } else {
        document.getElementById("activeModelBody").innerHTML = `
                <p>Are you sure you want to activate these courses?</p>
                <p style="display: block">${count} courses will be affected by this action </p>
                `
    }
});

$(".countDeac").click(function (e) {
    let count = 0;
    $("td .course__check:checked").each(function (i) {
        if (!$(this).val().toString().includes("I")) {
            count++;
        }
    })
    if (count <= 1) {
        document.getElementById("deActiveModelBody").innerHTML = `
                <p>Are you sure you want to deactivate these courses?</p>
                <p style="display: block">${count} course will be affected by this action </p>
                `
    } else {
        document.getElementById("deActiveModelBody").innerHTML = `
                <p>Are you sure you want to deactivate these courses?</p>
                <p style="display: block">${count} courses will be affected by this action </p>
                `
    }
});



$(".countDe").click(function (e) {
    let count = 0;
    $("td .course__check:checked").each(function (i) {
        count++;
    })
    if (count <= 1) {
        document.getElementById("deleteModelBody").innerHTML = `
                <p>Are you sure you want to delete these courses?</p>
                <p style="display: block">${count} course will be affected by this action </p>
                `
    } else {
        document.getElementById("deleteModelBody").innerHTML = `
                <p>Are you sure you want to delete these courses?</p>
                <p style="display: block">${count} courses will be affected by this action </p>
                `
    }
});

$(document).ready(function () {
    let checkPage = document.querySelector('main');
    if (checkPage.classList.contains('listUser')) {
        let tagName = $('.users__tab .users__tab-title');
        let tagContent = $('.tag__content .tag__content-list');
        tagName.click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            let index = $(this).index();
            tagContent.eq(index).addClass('active').siblings().removeClass('active');
        })
    }

    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#blah')
                        .attr('src', e.target.result)
                        .width(150)
                        .height(200);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }

    $("#files").change(function () {
        filename = this.files[0].name
        console.log(filename);
    });


    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });

    function activeCheck() {
        var checkBox = document.getElementById("activeCheckBox");
        var text = document.getElementById("activeCheck");
        if (checkBox.checked == true) {
            text.style.display = "block";
        } else {
            text.style.display = "none";
        }
    }
    function deactivateCheck() {
        var checkBox = document.getElementById("deactivateCheckBox");
        var text = document.getElementById("deactivateCheck");
        if (checkBox.checked == true) {
            text.style.display = "block";
        } else {
            text.style.display = "none";
        }
    }
});

