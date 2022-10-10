function calTotal(){
    var price = parseFloat(document.getElementById("price").innerHTML);
    var multi = 1;
    var total = price * multi;
    document.getElementById("total").innerHTML = total.toFixed(2);
}


