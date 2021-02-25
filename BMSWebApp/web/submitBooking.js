const getWindowsListURL = 'getWindowsList';
const windowsDivEL = document.querySelector('#windows');
const bookingDiv = document.querySelector('#booking');
async function fetchWindows(){

    let response =  await fetch(getWindowsListURL);
    console.log('return await response.json();');
    return await response.json();
}


function printWindow(window){


    let li = document.createElement('li');
    const windowButtonEL = document.createElement('button');
    windowButtonEL.setAttribute('type','button');
    windowButtonEL.setAttribute('name', window.scheduleWindowID);
    windowButtonEL.setAttribute('id', 'windowsClass');
    windowButtonEL.setAttribute('class','windowButton');


    let startTime;
    let endTime;

    if (window.startTime.minute.toString().length === 1)
        startTime = (window.startTime.hour).toString().concat(':0' + window.startTime.minute);
    else startTime = (window.startTime.hour).toString().concat(':' + window.startTime.minute);

    if (window.endTime.minute.toString().length === 1)
        endTime = (window.endTime.hour).toString().concat(':0' + window.endTime.minute);
    else     endTime = (window.endTime.hour).toString().concat(':' + window.endTime.minute);

    let text = 'Activity name: ' + window.name + '<br>' +
        'Start time: ' + startTime + '<br>' +
        'End time: ' + endTime + '<br>';
    let textEL = document.createTextNode(text);
    windowButtonEL.appendChild(textEL);
    li.appendChild(windowButtonEL);
    return li;

}

async function postWindow(event){

    let activityName = document.getElementById('#windowName').value;
    let startTime = document.getElementById('#startTime').value;
    let endTime = document.getElementById('#endTime').value;
    let boatTypes = "";
    // TODO: if (isAdmin){
        boatTypes = document.getElementById('#boatTypes').value;
    // }

    let data = {activityName,startTime,endTime,boatTypes};
    let options = {
        method:'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    let response = await fetch('newScheduleWindow',options);
    let json = await response.json();
    return json.windowID;
}

async function getMembers(){
    let response =  await fetch('members');
    return await response.json();
}

function printMembers(otherMembersJSON){

    if (Object.keys(otherMembersJSON).length === 0){
        alert('No available members');
        return ;
    }


    let i;
    let ul = document.createElement('ul');
    for (i=0; i<otherMembersJSON.length; i++){
        let name = otherMembersJSON.name;
        let li = document.createElement('li');
        li.appendChild(document.createTextNode(name));
        ul.appendChild(li);
    }
    const bookingDiv = document.querySelector('#booking');
    bookingDiv.appendChild(ul);


}

function displayBookingForm(windowID){
    console.log('Entered display form');
    let form = document.createElement('form');
    form.setAttribute('id','bookingForm');
    let dateInputEL = document.createElement('input');
    dateInputEL.setAttribute('type','date');
    let dateInputLabel = document.createElement('label').appendChild(document.createTextNode('Date: '));
    let boatTypeEL = document.createElement('input');
    boatTypeEL.setAttribute('type', 'text');
    let boatTypeLabel = document.createElement('label').appendChild(document.createTextNode('Boat type: '));

    let otherMembersEL = document.createElement('input');
    otherMembersEL.setAttribute('type','button');
    otherMembersEL.setAttribute('id','otherMembers')
    otherMembersEL.setAttribute('value','Add other members');
    let otherMembersLabel = document.createElement('label').appendChild(document.createTextNode('Check to invite other members: '));
    let submit = document.createElement('input');
    submit.setAttribute('type', 'submit');
    submit.setAttribute('value','Submit');

    form.appendChild(dateInputLabel);
    form.appendChild(dateInputEL);
    form.appendChild(boatTypeLabel);
    form.appendChild(boatTypeEL);
    form.appendChild(otherMembersEL);
    form.appendChild(submit);
    bookingDiv.appendChild(form);

    let otherMember = document.querySelector('#otherMembers')
    otherMember.addEventListener('click',()=>{
        console.log('entered event listener of otherMembers');
        let otherMembersJSON = getMembers();
        console.log(otherMembersJSON[0]);
        printMembers(otherMembersJSON);
    })


}


function getWindowFromClient() {
    console.log('creating form')
    let form = document.createElement('form');
    form.setAttribute('id', 'newWindow');
    let windowNameInputEL = document.createElement('input');
    windowNameInputEL.setAttribute('type', 'text');
    windowNameInputEL.setAttribute('id','windowName');
    let startTimeInputEL = document.createElement('input');
    startTimeInputEL.setAttribute('type', 'time');
    startTimeInputEL.setAttribute('min', '06:00');
    startTimeInputEL.setAttribute('max', '21:00');
    startTimeInputEL.setAttribute('id','startTime');
    let endTimeInputEL = document.createElement('input');
    endTimeInputEL.setAttribute('type', 'time');
    endTimeInputEL.setAttribute('min', '06:00');
    endTimeInputEL.setAttribute('max', '21:00');
    endTimeInputEL.setAttribute('id','endTime');


    // TODO: add label to each input element
    let nameText = document.createTextNode('Activity name: ');
    let nameLabel = document.createElement('label').appendChild(nameText);
    let startTimeText = document.createTextNode('Start time: ');
    let startTimeLabel = document.createElement('label').appendChild(startTimeText);
    let endTimeText = document.createTextNode('End time: ');
    let endTimeLabel = document.createElement('label').appendChild(endTimeText);

    let formSubmit = document.createElement('input');
    formSubmit.setAttribute('type','submit');
    formSubmit.setAttribute('value','Submit');

    form.appendChild(nameLabel);
    form.appendChild(windowNameInputEL);
    form.appendChild(startTimeLabel);
    form.appendChild(startTimeInputEL);

    form.appendChild(endTimeLabel);
    form.appendChild(endTimeInputEL);
    // TODO: if (isAdmin){
    let requestedBoatTypeInputEL = document.createElement('input');
    requestedBoatTypeInputEL.setAttribute('type','text');
    requestedBoatTypeInputEL.setAttribute('id','boatTypes')
    let boatTypeText = document.createTextNode('Boat type: ');
    let boatTypeLabel = document.createElement('label').appendChild(boatTypeText);
    form.appendChild(boatTypeLabel);
    form.appendChild(requestedBoatTypeInputEL);
    // }
    form.appendChild(formSubmit);

    windowsDivEL.appendChild(form);
    let formEL = document.querySelector('#newWindow');
    formEL.addEventListener('submit', () =>{
        console.log('Entered form eventListener')
        event.preventDefault();
        const windowID = postWindow(form);
        displayBookingForm(windowID);
    });


}


async function showWindows() {
    console.log('Entered showWindows');
    let windowsJSON = await fetchWindows();
    console.log('typeof windowJSON - ' + typeof windowsJSON)
    if (Object.keys(windowsJSON).length === 0){
        // no windows in the db. let user create one.
        getWindowFromClient();
    }
    else {
        let i;
        const ul = document.createElement('ul');
        ul.setAttribute('id', 'windowsUL')
        for (i = 0; i < windowsJSON.length; i++) {
            ul.appendChild(printWindow(windowsJSON[i]));
        }
        windowsDivEL.appendChild(ul);
    }
    const windowButtonEL = document.querySelector('#windows');

    windowButtonEL.addEventListener('click', () => {
        console.log('listening to windows');
        handleWindowSelection(event);
    });
}


function buildBookingForm(chosenWindowID){
        let form = document.createElement('form');
        form.setAttribute('id', 'bookingSubmitionForm');
        let i = document.createElement('input');

}


function handleWindowSelection(event){
    bookingDiv.innerHTML = "";
    let chosenWindowID = event.target.getAttribute('name');
    displayBookingForm(chosenWindowID);
}


window.addEventListener('load', () => {
    console.log('Entered event listener for submitBooking');
    showWindows();

})