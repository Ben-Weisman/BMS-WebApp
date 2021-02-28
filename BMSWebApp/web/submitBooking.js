const getWindowsListURL = 'getWindowsList';
const windowsDivEL = document.querySelector('#windows');
const bookingDiv = document.querySelector('#booking');
const otherMembersSelectionArray = [];

async function fetchWindows() {

    let response = await fetch(getWindowsListURL);
    console.log('return await response.json();');
    return await response.json();
}


function printWindow(window) {


    let li = document.createElement('li');
    const windowButtonEL = document.createElement('button');
    windowButtonEL.setAttribute('type', 'button');
    windowButtonEL.setAttribute('name', window.scheduleWindowID);
    windowButtonEL.setAttribute('id', 'windowsClass');
    windowButtonEL.setAttribute('class', 'windowButton');


    let startTime;
    let endTime;

    if (window.startTime.minute.toString().length === 1)
        startTime = (window.startTime.hour).toString().concat(':0' + window.startTime.minute);
    else startTime = (window.startTime.hour).toString().concat(':' + window.startTime.minute);

    if (window.endTime.minute.toString().length === 1)
        endTime = (window.endTime.hour).toString().concat(':0' + window.endTime.minute);
    else endTime = (window.endTime.hour).toString().concat(':' + window.endTime.minute);

    let text = 'Activity name: ' + window.name + '<br>' +
        'Start time: ' + startTime + '<br>' +
        'End time: ' + endTime + '<br>';
    windowButtonEL.innerHTML += text;

    li.appendChild(windowButtonEL);
    return li;

}

async function postWindow(event) {

    let activityName = document.getElementById('#windowName').value;
    let startTime = document.getElementById('#startTime').value;
    let endTime = document.getElementById('#endTime').value;
    let boatTypes = "";
    // TODO: if (isAdmin){
    boatTypes = document.getElementById('#boatTypes').value;
    // }

    let data = {activityName, startTime, endTime, boatTypes};
    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    let response = await fetch('newScheduleWindow', options);
    let json = await response.json();
    return json.windowID;
}

async function getMembers() {
    let response = await fetch('members');
    let json = await response.json();
    return json;
}

function printMembers(otherMembersJSON) {

    if (Object.keys(otherMembersJSON).length === 0) {
        alert('No available members');
        return;
    }


    let i;
    let ul = document.createElement('ul');
    for (i = 0; i < otherMembersJSON.length; i++) {
        let name = otherMembersJSON[i].name;
        let age = otherMembersJSON[i].age;
        let memberID = otherMembersJSON[i].memberID;
        let li = document.createElement('li');
        li.appendChild(document.createTextNode(name + ', age: ' + age + ', Member ID: ' + memberID));
        li.setAttribute('name', memberID);
        ul.appendChild(li);

        ul.appendChild(li);
    }
    bookingDiv.innerHTML += "<span><h3>Rowers in the club</h3></span>";
    bookingDiv.appendChild(ul);


}

async function postNewBooking(data){
    console.log(data);
    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    let response = await fetch('newBookingRequest',options);
    return await response.json();

}

async function getMemberID(){
    let response = await fetch('memberID');
    let json = await response.json();
    return json.memberID;
}



async function displayBookingForm(requestedWindowID) {
    console.log('Entered display form');
    let form = document.createElement('form');
    form.setAttribute('id', 'bookingForm');
    let dateInputEL = document.createElement('input');
    dateInputEL.setAttribute('type', 'date');
    dateInputEL.setAttribute('id','dateInput')
    let dateInputLabel = document.createElement('label').appendChild(document.createTextNode('Date: '));
    let boatTypeEL = document.createElement('input');
    boatTypeEL.setAttribute('type', 'text');
    boatTypeEL.setAttribute('id','boatType');
    let boatTypeLabel = document.createElement('label').appendChild(document.createTextNode('Boat type: '));

    let submit = document.createElement('input');
    submit.setAttribute('type', 'submit');
    submit.setAttribute('value', 'Submit Booking');

    form.appendChild(dateInputLabel);
    form.appendChild(dateInputEL);
    form.appendChild(boatTypeLabel);
    form.appendChild(boatTypeEL);
    form.appendChild(submit);
    bookingDiv.innerHTML += "<span><h3>Please fill the booking details</h3></span>";
    bookingDiv.appendChild(form);


        console.log('entered event listener of otherMembers');

        let label = document.createTextNode("Please enter other participating rowers' ID, separated by a comma (,). " +
            "You can leave this field empty.");

        let otherMembersInput = document.createElement('input');
        otherMembersInput.setAttribute('type', 'text');
        otherMembersInput.setAttribute('id', 'otherMembersList');

        form.appendChild(label);
        form.appendChild(otherMembersInput);
        let otherMembersJSON = await getMembers();
        await printMembers(otherMembersJSON);

    form.addEventListener('submit', async (event) =>{
        event.preventDefault();
        let memberOrderedID = await getMemberID();
        console.log(memberOrderedID);
        let requestedBoatTypes = document.querySelector('#boatType').value;
        otherParticipatingRowersIDs = document.querySelector('#otherMembersList').value;
        let requestedPracticeDate = document.querySelector('#dateInput').value;
        let data = {requestedWindowID, memberOrderedID, requestedBoatTypes, otherParticipatingRowersIDs, requestedPracticeDate};
        let responseJSON = await postNewBooking(data);
        if (responseJSON.status == 'error'){
            bookingDiv.innerHTML = "<h1> Failed to create booking</h1>" +
                "<p>" + responseJSON.message +"</p>";
        }
        else bookingDiv.innerHTML = "<h1> Booking created successfully</h1>";
    })
}


async function checkIfAdmin() {
    let response = await fetch('admin');
    let json = await response.json();
    return json.isAdmin;
}

async function getWindowFromClient() {
    console.log('creating form')
    let form = document.createElement('form');
    form.setAttribute('id', 'newWindow');
    let windowNameInputEL = document.createElement('input');
    windowNameInputEL.setAttribute('type', 'text');
    windowNameInputEL.setAttribute('id', 'windowName');
    let startTimeInputEL = document.createElement('input');
    startTimeInputEL.setAttribute('type', 'time');
    startTimeInputEL.setAttribute('min', '06:00');
    startTimeInputEL.setAttribute('max', '21:00');
    startTimeInputEL.setAttribute('id', 'startTime');
    let endTimeInputEL = document.createElement('input');
    endTimeInputEL.setAttribute('type', 'time');
    endTimeInputEL.setAttribute('min', '06:00');
    endTimeInputEL.setAttribute('max', '21:00');
    endTimeInputEL.setAttribute('id', 'endTime');


    let nameText = document.createTextNode('Activity name: ');
    let nameLabel = document.createElement('label').appendChild(nameText);
    let startTimeText = document.createTextNode('Start time: ');
    let startTimeLabel = document.createElement('label').appendChild(startTimeText);
    let endTimeText = document.createTextNode('End time: ');
    let endTimeLabel = document.createElement('label').appendChild(endTimeText);

    let formSubmit = document.createElement('input');
    formSubmit.setAttribute('type', 'submit');
    formSubmit.setAttribute('value', 'Submit');

    form.appendChild(nameLabel);
    form.appendChild(windowNameInputEL);
    form.appendChild(startTimeLabel);
    form.appendChild(startTimeInputEL);

    form.appendChild(endTimeLabel);
    form.appendChild(endTimeInputEL);
    let isAdmin = await checkIfAdmin();
    if (isAdmin === 'true') {
        let requestedBoatTypeInputEL = document.createElement('input');
        requestedBoatTypeInputEL.setAttribute('type', 'text');
        requestedBoatTypeInputEL.setAttribute('id', 'boatTypes')
        let boatTypeText = document.createTextNode('Boat type: ');
        let boatTypeLabel = document.createElement('label').appendChild(boatTypeText);
        form.appendChild(boatTypeLabel);
        form.appendChild(requestedBoatTypeInputEL);
    }
    form.appendChild(formSubmit);

    windowsDivEL.appendChild(form);
    let formEL = document.querySelector('#newWindow');
    formEL.addEventListener('submit', () => {
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
    if (Object.keys(windowsJSON).length === 0) {
        // no windows in the db. let user create one.
        getWindowFromClient();
    } else {
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


function handleWindowSelection(event) {
    bookingDiv.innerHTML = "";
    let chosenWindowID = event.target.getAttribute('name');
    displayBookingForm(chosenWindowID);
}


window.addEventListener('load',  () => {
    console.log('Entered event listener for submitBooking');
     showWindows();

})