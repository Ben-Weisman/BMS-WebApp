const memberIDInputEl = document.querySelector('#memberID');
const checkIfMemberExistFormEl = document.querySelector('#checkIfMemberExistForm');
const updateMemberFormEl = document.querySelector('#updateMemberForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');
const newNameInputEl = document.querySelector('#newName');
const newAgeInputEl = document.querySelector('#newAge');
const newCommentsInputEl = document.querySelector('#newComments');
const newExpirationDateInputEl = document.querySelector('#newExpirationDate');
const hasPrivateBoatCheckboxEl = document.querySelector('#hasPrivateBoat');
const privateBoatSerialNumberInputEl = document.querySelector('#privateBoatSerialNumber');
const newPhoneInputEl = document.querySelector('#newPhone');
const newEmailInputEl = document.querySelector('#newEmail');
const newPasswordInputEl = document.querySelector('#newPassword');
const isManagerCheckboxEl = document.querySelector('#isManager');

let memberID;
function submitMemberID(event) {
    console.log("in submitMemberID()");
    memberID = memberIDInputEl.value;
    submitMemberAsync();
    event.preventDefault();

}
async function submitMemberAsync() {

    try {

        let response = await fetch('/BMSWebApp/updateMember?' + "memberID=" + memberID);

        let text = await response.text();
        console.log("text is: --" + text);
        if (text === "false") {
            updateSubmitMessage("Member didn't found", 0);
            updateMemberFormEl.style.display = "none"
        } else if (response.status == 200) {
            updateSubmitMessage("Member found", 200);
            updateMemberFormEl.style.display = "block"
        }
    } catch (e) {
        console.log(e.message);
    }
}

checkIfMemberExistFormEl.addEventListener('submit', submitMemberID)

async function updateMember(event) {
    event.preventDefault();
    let newName = newNameInputEl.value;
    let newAge = newAgeInputEl.value;
    let newComments = newCommentsInputEl.value;
    let newExpirationDate = newExpirationDateInputEl.value;
    let hasPrivateBoat = hasPrivateBoatCheckboxEl.checked;
    let privateBoatSerialNumber = privateBoatSerialNumberInputEl.checked;
    let newPhone = newPhoneInputEl.checked;
    let newEmail = newEmailInputEl.checked;
    let newPassword = newPasswordInputEl.checked;
    let isManager = isManagerCheckboxEl.checked;

    const data = {
        memberID: memberID,
        newName: newName,
        newAge: newAge,
        newComments: newComments,
        newExpirationDate: newExpirationDate,
        hasPrivateBoat: hasPrivateBoat,
        privateBoatSerialNumber: privateBoatSerialNumber,
        newPhone: newPhone,
        newEmail: newEmail,
        newPassword: newPassword,
        isManager: isManager
    }

    const response = await fetch('/BMSWebApp/updateMember', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });
    updateSubmitMessage(await response.text(), response.status);
}

updateBoatFormEl.addEventListener('submit', updateMember)

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