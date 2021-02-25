const bookingIDInputEl = document.querySelector('#bookingID');
const removeBookingFormEl = document.querySelector('#removeBookingForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');


removeBookingFormEl.addEventListener('submit', validateForm);

function resetForm () {
    bookingIDInputEl.value = '';
}

function validateForm(event) {

    const bookingID = bookingIDInputEl.value;
    submitRemoveBooking(bookingID);
    resetForm();

    event.preventDefault();
}

async function submitRemoveBooking(bookingID) {
    console.log("removeBooking.js - submitRemoveBooking(): before fetch")
    const response = await fetch('/BMSWebApp/removeBooking', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'text/plain'
        }),
        body: bookingID
    });

    updateSubmitMessage(await response.text(), response.status);
}

function updateSubmitMessage(message, responseStatus) {
    let HttpCodes = {
        success : 200,
        needToRemoveOther : 409
    }
    messagePlaceHolderEl.innerHTML=message;
    if (responseStatus === HttpCodes.success){
        messagePlaceHolderEl.setAttribute("style", "color:black;")
    }
    else{
        messagePlaceHolderEl.setAttribute("style", "color:red;")
    }
}

