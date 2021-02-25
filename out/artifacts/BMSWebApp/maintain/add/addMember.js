const memberNameInputEl = document.querySelector('#memberName');
const memberAgeInputEl = document.querySelector('#memberAge');
const commentsInputEl = document.querySelector('#comments');
const levelRadioButtonsEl = document.querySelectorAll('input[name=level]');
const joiningDateInputEl = document.querySelector('#joiningDate');
const expirationDateInputEl = document.querySelector('#expirationDate');
const hasPrivateBoatInputEl = document.querySelector('#hasPrivateBoat');
const phoneNumberInputEl = document.querySelector('#phoneNumber');
const emailInputEl = document.querySelector('#email');
const passwordInputEl = document.querySelector('#password');
const isAdminInputEl = document.querySelector('#isAdmin');
const privateBoatSerialNumberInputEl = document.querySelector('#privateBoatSerialNumber');

const addMemberFormEl = document.querySelector('#addMemberForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');

addMemberFormEl.addEventListener('submit', validateForm);

function resetForm () {
    memberNameInputEl.value = "";
    memberAgeInputEl.value = "";
    commentsInputEl.value = "";
    hasPrivateBoatInputEl.checked = false;
    phoneNumberInputEl.value = "";
    emailInputEl.value = "";
    passwordInputEl.value = "";
    isAdminInputEl.checked = false;
}

function validateForm(event) {

    const newMemberName = memberNameInputEl.value;
    const newMemberAge = memberAgeInputEl.value;
    const newComments = commentsInputEl.value;
    const newJoiningDate = joiningDateInputEl.value;
    const newExpirationDate = expirationDateInputEl.value;
    const newHasPrivateBoat = hasPrivateBoatInputEl.checked;
    const newPhoneNumber = phoneNumberInputEl.value;
    const newEmail = emailInputEl.value;
    const newPassword = passwordInputEl.value;
    const newIsAdmin = isAdminInputEl.checked;
    const newPrivateBoatSerialNumber = privateBoatSerialNumberInputEl.value;

    let selectedLevel;
    for (const level of levelRadioButtonsEl) {
        if (level.checked) {
            selectedLevel = level.value;
            break;
        }
    }
    submitNewMember(newMemberName,
        newMemberAge,
        newComments,
        selectedLevel,
        newJoiningDate,
        newExpirationDate,
        newHasPrivateBoat,
        newPhoneNumber,
        newEmail,
        newPassword,
        newIsAdmin,
        newPrivateBoatSerialNumber);
    resetForm();

    event.preventDefault();
}

async function submitNewMember(memberName,
                               memberAge,
                               comments,
                               selectedLevel,
                               joiningDate,
                               expirationDate,
                               hasPrivateBoat,
                               phoneNumber,
                               email,
                               password,
                               isAdmin,
                               privateBoatSerialNumber) {
    const data = {
        memberName : memberName,
        memberAge : memberAge,
        comments : comments,
        selectedLevel : selectedLevel,
        joiningDate : joiningDate,
        expirationDate : expirationDate,
        hasPrivateBoat : hasPrivateBoat,
        phoneNumber : phoneNumber,
        email : email,
        password : password,
        isAdmin : isAdmin,
        privateBoatSerialNumber: privateBoatSerialNumber}

    console.log("addMember.js - submitNewForm(): before fetch")
    const response = await fetch('/BMSWebApp/addMember', {
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

