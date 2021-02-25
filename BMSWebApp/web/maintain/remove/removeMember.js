const memberIDInputEl = document.querySelector('#memberID');
const removeMemberFormEl = document.querySelector('#removeMemberForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');


removeMemberFormEl.addEventListener('submit', validateForm);

function resetForm () {
    memberIDInputEl.value = '';
}

function validateForm(event) {

    const memberID = memberIDInputEl.value;
    submitRemoveMember(memberID);
    resetForm();

    event.preventDefault();
}

async function submitRemoveMember(memberID) {
    console.log("removeMember.js - submitRemoveMember(): before fetch")
    const response = await fetch('/BMSWebApp/removeMember', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'text/plain'
        }),
        body: memberID
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

