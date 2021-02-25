const boatNameInputEl = document.querySelector('#boatName');
const boatTypeRadioButtonsEl = document.querySelectorAll('input[name=boatType]');
const wideOrCoastalInputEl = document.querySelector("#wideOrCoastal");
const isPrivateInputEl = document.querySelector('#isPrivate');
const addBoatFormEl = document.querySelector('#addBoatForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');

addBoatFormEl.addEventListener('submit', validateForm);

function resetForm () {
    boatNameInputEl.value = '';
    isPrivateInputEl.checked = false;
}

function validateForm(event) {

    const newBoatName = boatNameInputEl.value;
    const isPrivate = isPrivateInputEl.checked;
    let newBoatType;

    for (const type of boatTypeRadioButtonsEl) {
        if (type.checked) {
            newBoatType = type.value;
            break;
        }
    }
    newBoatType += " " + wideOrCoastalInputEl.value;

    if (newBoatName.length !== 0 && newBoatType.length !== 0) {
        submitNewBoat(newBoatType, newBoatName, isPrivate);
        resetForm();
    }

    event.preventDefault();
}

async function submitNewBoat(boatType, boatName, isPrivate) {
    const data = {
        boatName: boatName,
        boatType: boatType,
        isPrivate: isPrivate
    }
    console.log("addBoat.js - submitNewForm(): before fetch")
    const response = await fetch('/BMSWebApp/addBoat', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });

    updateSubmitMessage(await response.text(), response.status===200);
}

function updateSubmitMessage(message, passed) {
    messagePlaceHolderEl.innerHTML=message;
    if (passed){
        messagePlaceHolderEl.setAttribute("style", "color:black;")
    }
    else {
        messagePlaceHolderEl.setAttribute("style", "color:red;")
    }
}

