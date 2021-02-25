const ScheduleWindowNameInputEl = document.querySelector('#ScheduleWindowName');
const boatTypeRadioButtonsEl = document.querySelectorAll('input[name=boatType]');
const startTimeInputEl = document.querySelector('#startTime');
const endTimeInputEl = document.querySelector('#endTime');
const addScheduleWindowFormEl = document.querySelector('#addScheduleWindowForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');

addScheduleWindowFormEl.addEventListener('submit', validateForm);

function resetForm () {
    ScheduleWindowNameInputEl.value = '';
    startTimeInputEl.value = '';
    endTimeInputEl.value = '';
}

function validateForm(event) {

    const newScheduleWindowName = ScheduleWindowNameInputEl.value;
    const newStartTime = startTimeInputEl.value;
    const newEndTime = endTimeInputEl.value;

    let selectedBoatType;
    for (const type of boatTypeRadioButtonsEl) {
        if (type.checked) {
            selectedBoatType = type.value;
            break;
        }
    }

    submitNewScheduleWindow(newScheduleWindowName,
    selectedBoatType,
    newStartTime,
    newEndTime);
    resetForm();


    event.preventDefault();
}

async function submitNewScheduleWindow(ScheduleWindowName, boatType, startTime, endTime) {
    const data = {
        ScheduleWindowName: ScheduleWindowName,
        boatType: boatType,
        startTime: startTime,
        endTime: endTime
    }
    console.log("addScheduleWindow.js - submitNewForm(): before fetch")
    const response = await fetch('/BMSWebApp/addScheduleWindow', {
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

