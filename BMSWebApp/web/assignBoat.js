const boatsDivEL = document.querySelector('#boats');
const bookingIDValue = document.querySelector('#bookingIdinput');
const assignBoatURL = 'assignBoat';
const availableBoatsURL = 'getAvailableBoats';


async function fetchBoats(bookingID) {
    const data = {bookingID};
    let options = {
        method: 'POST',
        header: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    let response = await fetch(availableBoatsURL, options);
    return  await response.json();
}


function createLIelement(boat) {
    let li = document.createElement('li');
    let input = document.createElement('input');
    li.setAttribute('class', 'boatOption')
    li.innerHTML += '<br><br>Boat Name: ' + boat.boatName;
    li.innerHTML += '<br>Boat Type: ' + boat.boatType;
    li.innerHTML += '<br>Has Coxswain: ' + boat.hasCoxwain;
    li.innerHTML += '<br>Is Wide: ' + boat.isWide;
    li.innerHTML += '<br>Boat ID: ' + boat.boatID + '<br>';

    return li;
}


function printBoats(boats) {
    const ul = document.createElement('ul');
    ul.innerHTML += '<h2>Available boats for the selected booking: </h2>';
    ul.innerHTML += '<span>Please choose the boat ID you wish to assign to the booking</span><br><br>';
    ul.innerHTML += "    <form id = \"idInput\">\n" +
        "        <input type=\"number\" id=\"in\" placeholder=\"Enter Boat ID\"><br><br>\n" +
        "        <input type=\"submit\" value=\"Assign Boat\">\n" +
        "    </form>";

    boats.forEach(boat => {
        ul.appendChild(createLIelement(boat));
    })
    boatsDivEL.appendChild(ul);

}

async function handleFormInput() {
    console.log('entered form eventListener');
    const bookingID = bookingIDValue.value;
    return await fetchBoats(bookingID);
}

async function postAssignedBoatAsync(bookingID, boatID) {
    const data = {bookingID, boatID};
    let options = {
        method: 'POST',
        header: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    const response = await fetch(assignBoatURL,options)
    const json = await response.json();

    if (json.status === 'error'){
        alert('ERROR: ' + json.message);
    }
    else {
        alert('Boat assigned successfully.');
    }
}

function boatAssignmentEventHandler() {
    let boatIDForm = document.querySelector('#idInput');
        boatIDForm.addEventListener('submit', async ()=>{
            event.preventDefault();
            const bookingID = bookingIDValue.value;
            const boatID = document.querySelector('#in').value;
            await postAssignedBoatAsync(bookingID,boatID);
        })

}

window.addEventListener('load',()=>{
    let form = document.querySelector('#idForm');
    form.addEventListener('submit', async (event) =>{
        event.preventDefault();
        let boats = await handleFormInput();
        printBoats(boats);
        boatAssignmentEventHandler();
    })

})