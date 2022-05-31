 function CreditCard(elem){
    var m = elem.value.length+1;

    if (m > 19 || event.keyCode < 48 || event.keyCode > 57)
    event.returnValue= false;

    else if (m == 5 || m == 10 || m == 15)
    elem.value += ' ';
}