const bookingIDValue = document.querySelector('#bookingIdInput');
const relevantMembersURL = 'relevant';
const membersDivEL = document.querySelector('#bookings');
const addRowersURL = 'addRowersBooking';
async function getMembers(bookingID) {

    const data = {bookingID};
    let options = {
        method: 'POST',
        header: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }

    let response = await fetch(relevantMembersURL,options);
    return await response.json();
}

function createLIelement(member) {
    let li = document.createElement('li');
    li.setAttribute('class', 'rowerOption')
    li.innerHTML += '<br><br>Name: ' + member.name;
    li.innerHTML += '<br>Age: ' + member.age;
    li.innerHTML += '<br>Level: ' + member.level;
    li.innerHTML += '<br>Email: ' + member.emailAddress;
    li.innerHTML += '<br>ID: ' + member.memberID + '<br>';

    return li;
}
function printMembers(members){
    const ul = document.createElement('ul');


    ul.innerHTML += '<h2>The relevant rowers can join the selected booking: </h2>';
    ul.innerHTML += '<span>Please choose the member IDs you wish to add to the booking</span><br>' +
        'Separate each id with a comma (,)<br><br>';
    ul.innerHTML += "    <form id = \"idInput\">\n" +
        "        <input type=\"text\" id=\"in\" placeholder=\"Enter member IDs\"><br><br>\n" +
        "        <input type=\"submit\" value=\"Add Rowers\">\n" +
        "    </form>";

    members.forEach(member => {
        ul.appendChild(createLIelement(member));
    })
    membersDivEL.appendChild(ul);


}


async function postRowersAsync(bookingID, rowers) {
    const data = {bookingID, rowers};
    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    const response = await fetch(addRowersURL,options);
    const json = await response.json();

    if (json.status === 'error'){
        alert('ERROR: ' + json.message);
    }
    else {
        alert(json.message);
    }
}

function addRowerEventHandler() {
    let rowersToAddFormEL = document.querySelector('#idInput');
    rowersToAddFormEL.addEventListener('submit', async ()=>{
        event.preventDefault();
        const bookingID = bookingIDValue.value;
        const rowers = document.querySelector('#in').value;
        await postRowersAsync(bookingID,rowers);
    })
}

window.addEventListener('load',()=>{
    let form = document.querySelector('#idForm');
    form.addEventListener('submit', async () =>{
        event.preventDefault();
        const bookingID = bookingIDValue.value;
        let members = await getMembers(bookingID);
        printMembers(members);
        addRowerEventHandler();

    })

})