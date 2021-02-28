const memberOrderedIDInputEl = document.querySelector('#memberOrderedID');
const addBookingRequestFormEl = document.querySelector('#addBookingRequest');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');
const practiceDateInputEl = document.querySelector('#practiceDate');
const windowIDInputEl = document.querySelector('#requestedWindowID');
const newBoatTypesCheckboxesEl = document.querySelectorAll('input[name=boatType]');
const otherParticipatingRowerIDInputEl = document.querySelector('#otherParticipatingRowersID');
const otherParticipatingRowerMessageEl = document.querySelector('#otherParticipatingRowersIDSmall');


let otherParticipatingRowersIDs = '';

async function addBooking(event) {
    event.preventDefault();
    let memberOrderedID = memberOrderedIDInputEl.value;
    let requestedPracticeDate = practiceDateInputEl.value;
    let requestedWindowID = windowIDInputEl.value;
    let newBoatTypes = '';
    for (const type of newBoatTypesCheckboxesEl) {
        if (type.checked) {
            newBoatTypes += type.value + ',';
        }
    }
    if (newBoatTypes.length != 0){
        newBoatTypes = newBoatTypes.slice(0, -1);
    }
    if (otherParticipatingRowersIDs.length != 0){
        otherParticipatingRowersIDs = otherParticipatingRowersIDs.slice(0, -1);
    }

    const data = {
        requestedWindowID: requestedWindowID,
        memberOrderedID: memberOrderedID,
        requestedBoatTypes: newBoatTypes,
        otherParticipatingRowersIDs: otherParticipatingRowersIDs,
        requestedPracticeDate: requestedPracticeDate
    }

    const response = await fetch('/BMSWebApp/newBookingRequest', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });
    updateSubmitMessage(await response.text(), response.status);
}

addBookingRequestFormEl.addEventListener('submit', addBooking)

function addParticipatingRowerID(event) {
    otherParticipatingRowersIDs += event.target.value + ',';
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