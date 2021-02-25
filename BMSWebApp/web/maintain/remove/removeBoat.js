const boatIDInputEl = document.querySelector('#boatID');
const removeBoatFormEl = document.querySelector('#removeBoatForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');
const forceRemoveButtonEl = document.querySelector("#forceRemove");
const confirmationDivEl = document.querySelector('#confirmation');

removeBoatFormEl.addEventListener('submit', validateForm);
forceRemoveButtonEl.addEventListener('click', forceRemove);

function resetForm () {
   boatIDInputEl.value = '';
}

function validateForm(event) {

    const boatID = boatIDInputEl.value;
    let forceRemove = false;
    submitRemoveBoat(boatID, forceRemove);
    resetForm();

    event.preventDefault();
}

function forceRemove(event) {
    const boatID = boatIDInputEl.value;
    let forceRemove = true;
    submitRemoveBoat(boatID, forceRemove);
    resetForm();
    event.preventDefault();
}
async function submitRemoveBoat(boatID, forceRemove) {
    const data = {
        boatID: boatID,
        forceRemove: forceRemove
    }
    console.log("removeBoat.js - submitRemoveBoat(): before fetch")
    const response = await fetch('/BMSWebApp/removeBoat', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
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
    if (responseStatus === HttpCodes.needToRemoveOther){
        confirmationDivEl.style.display = "block";
    }
}

