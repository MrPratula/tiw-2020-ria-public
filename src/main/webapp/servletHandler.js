function callServlet(method, url, formElement, cback, reset = true) {

    const request = new XMLHttpRequest(); // visible by closure
    //console.log(formElement);
    request.onreadystatechange = function() {
        cback(request)
    }; // closure
    request.open(method, url);
    if (formElement == null) {
        request.send();
    } else {
        request.send(new FormData(formElement));
    }
    if (formElement !== null && reset === true) {
        formElement.reset();
    }
}