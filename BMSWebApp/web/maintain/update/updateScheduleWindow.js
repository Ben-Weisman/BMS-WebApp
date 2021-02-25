const scheduleWindowNameInputEl = document.querySelector('#windowName');
const checkIfWindowExistFormEl = document.querySelector('#checkIfWindowExistForm');
const updateScheduleWindowFormEl = document.querySelector('#updateScheduleWindowForm');
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');
const newScheduleWindowNameInputEl = document.querySelector('#newScheduleWindowName');
const newStartTimeInputEl = document.querySelector('#newStartTime');
const newEndTimeInputEl = document.querySelector('#newEndTime');
const newBoatTypeRadioButtonsEl = document.querySelectorAll('input[name=boatType]');


let windowName;

function submitScheduleWindowName(event) {
    windowName = scheduleWindowNameInputEl.value;
    submitScheduleWindowAsync();
    event.preventDefault();

}
async function submitScheduleWindowAsync() {

    try {

        let response = await fetch('/BMSWebApp/updateScheduleWindow?' + "windowName=" + windowName);

        let text = await response.text();
        if (text === "false") {
            updateSubmitMessage("Schedule Window didn't found", 0);
            updateScheduleWindowFormEl.style.display = "none"
        } else if (response.status == 200) {
            updateSubmitMessage("Schedule Window found", 200);
            updateScheduleWindowFormEl.style.display = "block"
        }
    } catch (e) {
        console.log(e.message);
    }
}

checkIfWindowExistFormEl.addEventListener('submit', submitScheduleWindowName)

async function updateScheduleWindow(event) {
    event.preventDefault();
    let newScheduleWindowName = newScheduleWindowNameInputEl.value;
    let newStartTime = newStartTimeInputEl.value;
    let newEndTime = newEndTimeInputEl.value;
    let newBoatType;
    for (const type of newBoatTypeRadioButtonsEl) {
        if (type.checked) {
            newBoatType = type.value;
        }
    }

    const data = {
        windowName: windowName,
        newScheduleWindowName: newScheduleWindowName,
        newStartTime: newStartTime,
        newEndTime: newEndTime,
        newBoatType: newBoatType
    }

    const response = await fetch('/BMSWebApp/updateScheduleWindow', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });
    updateSubmitMessage(await response.text(), response.status);
}

updateScheduleWindowFormEl.addEventListener('submit', updateScheduleWindow)

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