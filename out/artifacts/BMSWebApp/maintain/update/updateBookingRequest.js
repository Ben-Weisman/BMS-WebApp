const bookingIDInputEl = document.querySelector('#bookingID');
const checkIfBookingExistFormEl = document.querySelector('#checkIfBookingExistForm');
const updateBookingRequestFormEl = document.querySelector('#updateBookingRequestForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');
const practiceDateInputEl = document.querySelector('#practiceDate');
const newWindowIDInputEl = document.querySelector('#newWindowID');
const newAssignedBoatIDInputEl = document.querySelector('#newAssignedBoatID');
const newBoatTypesCheckboxesEl = document.querySelectorAll('input[name=boatType]');
const otherParticipatingRowerIDInputEl = document.querySelector('#otherParticipatingRowersID');
const otherParticipatingRowerMessageEl = document.querySelector('#otherParticipatingRowersIDSmall');


let bookingID;
let otherParticipatingRowersIDs = [];

function submitBookingID(event) {
    console.log("in submitBookingID()");
    bookingID = bookingIDInputEl.value;
    submitBookingAsync();
    event.preventDefault();

}
async function submitBookingAsync() {

    try {

        let response = await fetch('/BMSWebApp/updateBooking?' + "bookingID=" + bookingID);

        let text = await response.text();
        console.log("text is: --" + text);
        if (text === "false") {
            updateSubmitMessage("Booking didn't found", 0);
            updateBookingRequestFormEl.style.display = "none"
        } else if (response.status == 200) {
            updateSubmitMessage("Booking found", 200);
            updateBookingRequestFormEl.style.display = "block"
        }
    } catch (e) {
        console.log(e.message);
    }
}

checkIfBookingExistFormEl.addEventListener('submit', submitBookingID)

async function updateBooking(event) {
    event.preventDefault();
    let practiceDate = practiceDateInputEl.value;
    let newWindowID = newWindowIDInputEl.value;
    let newAssignedBoatID = newAssignedBoatIDInputEl.value;
    let newBoatTypes = [];
    for (const type of newBoatTypesCheckboxesEl) {
        if (type.checked) {
            newBoatTypes.push(type.value);
        }
    }

    const data = {
        bookingID: bookingID,
        practiceDate: practiceDate,
        newWindowID: newWindowID,
        newAssignedBoatID: newAssignedBoatID,
        newBoatTypes: newBoatTypes,
        otherParticipatingRowersIDs: otherParticipatingRowersIDs
    }

    const response = await fetch('/BMSWebApp/updateBooking', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });
    updateSubmitMessage(await response.text(), response.status);
}

updateBoatFormEl.addEventListener('submit', updateBooking)

function addParticipatingRowerID(event) {
    otherParticipatingRowersIDs.push(event.target.value);
    otherParticipatingRowerMessageEl.innerHTML += event.target.value + ' ';
}

otherParticipatingRowerIDInputEl.addEventListener('change', addParticipatingRowerID);

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