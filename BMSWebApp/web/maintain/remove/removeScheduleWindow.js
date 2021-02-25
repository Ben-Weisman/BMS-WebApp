const scheduleWindowNameInputEl = document.querySelector('#scheduleWindowName');
const removeScheduleWindowFormEl = document.querySelector('#removeScheduleWindowForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');

removeScheduleWindowFormEl.addEventListener('submit', validateForm);

function resetForm () {
    scheduleWindowNameInputEl.value = '';
}

function validateForm(event) {

    const scheduleWindowName = scheduleWindowNameInputEl.value;
    submitRemoveScheduleWindow(scheduleWindowName);
    resetForm();

    event.preventDefault();
}

async function submitRemoveScheduleWindow(scheduleWindowName) {
    console.log("removeScheduleWindow.js - submitRemoveScheduleWindow(): before fetch")
    const response = await fetch('/BMSWebApp/removeScheduleWindow', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'text/plain'
        }),
        body: scheduleWindowName
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

