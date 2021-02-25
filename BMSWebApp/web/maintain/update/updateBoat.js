const boatIDInputEl = document.querySelector('#boatID');
const checkIfBoatExistFormEl = document.querySelector('#checkIfBoatExistForm');
const updateBoatFormEl = document.querySelector('#updateBoatForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');
const boatTypeRadioButtonsEl = document.querySelectorAll('input[name=newBoatType]');
const newNameInputEl = document.querySelector('#newName');
const isPrivateCheckboxEl = document.querySelector('#isPrivate');
const isWideCheckboxEl = document.querySelector('#isWide');
const hasCoxwainCheckboxEl = document.querySelector('#hasCoxwain');
const isCoastalCheckboxEl = document.querySelector('#isCoastal');
const isFunctioningCheckboxEl = document.querySelector('#isFunctioning');

let boatID;
function submitBoatID(event) {
    console.log("in submitBoatID()");
    boatID = boatIDInputEl.value;
    submitBoatAsync();
    event.preventDefault();

}
async function submitBoatAsync() {

    try {

        let response = await fetch('/BMSWebApp/updateBoat?' + "boatID=" + boatID);

        let text = await response.text();
        console.log("text is: --" + text);
        if (text === "false") {
            updateSubmitMessage("Boat didn't found", 0);
            updateBoatFormEl.style.display = "none"
        } else if (response.status == 200) {
            updateSubmitMessage("Boat found", 200);
            updateBoatFormEl.style.display = "block"
        }
    } catch (e) {
        console.log(e.message);
    }
}

checkIfBoatExistFormEl.addEventListener('submit', submitBoatID)

async function updateBoat(event) {
    event.preventDefault();
    let newName = newNameInputEl.value;
    let isPrivate = isPrivateCheckboxEl.checked;
    let isWide = isWideCheckboxEl.checked;
    let hasCoxwain = hasCoxwainCheckboxEl.checked;
    let isCoastal = isCoastalCheckboxEl.checked;
    let isFunctioning = isFunctioningCheckboxEl.checked;
    let selectedBoatType;
    for (const type of boatTypeRadioButtonsEl) {
        if (type.checked) {
            selectedBoatType = type.value;
            break;

        }
    }
    const data = {
        boatID: boatID,
        newName: newName,
        isPrivate: isPrivate,
        isWide: isWide,
        hasCoxwain: hasCoxwain,
        isCoastal: isCoastal,
        isFunctioning: isFunctioning,
        selectedBoatType: selectedBoatType
    }
    console.log("updateBoat.js - submitUpdateBoat(): before fetch")

    const response = await fetch('/BMSWebApp/updateBoat', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });
    updateSubmitMessage(await response.text(), response.status);
}

updateBoatFormEl.addEventListener('submit', updateBoat)

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