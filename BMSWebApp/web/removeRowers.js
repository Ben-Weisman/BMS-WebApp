const membersDivEL = document.querySelector('#bookings');
const getRowersURL = 'getRowers';
const removeRowersURL = 'removeRowers';
const bookingIDValue = document.querySelector('#bookingIdInput');


async function deleteRowersAsync(bookingID, rowers) {
    const data = {bookingID, rowers};
    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    const response = await fetch(removeRowersURL,options);
    const json = await response.json();

    if (json.status === 'error'){
        alert('ERROR: ' + json.message);
    }
    else {
        alert(json.message);
    }
}

function removeRowersEventHandler() {
    let rowersToRemoveFormEL = document.querySelector('#idInput');
    rowersToRemoveFormEL.addEventListener('submit', async ()=>{
        event.preventDefault();
        const bookingID = bookingIDValue.value;
        const rowers = document.querySelector('#in').value;
        await deleteRowersAsync(bookingID,rowers);
    })
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


    ul.innerHTML += '<h2>Participating rowers list: </h2>';
    ul.innerHTML += '<span>Please choose the member IDs you wish to remove from the booking</span><br>' +
        'Separate each id with a comma (,)<br><br>';
    ul.innerHTML += "    <form id = \"idInput\">\n" +
        "        <input type=\"text\" id=\"in\" placeholder=\"Enter member IDs\"><br><br>\n" +
        "        <input type=\"submit\" value=\"Remove Rowers\">\n" +
        "    </form>";

    members.forEach(member => {
        ul.appendChild(createLIelement(member));
    })
    membersDivEL.appendChild(ul);


}

async function getRowersFromBooking(bookingID) {

    const data = {bookingID};
    let options = {
        method: 'POST',
        header: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }

    let response = await fetch(getRowersURL,options);
    return await response.json();

}

window.addEventListener('load',()=>{
    let form = document.querySelector('#idForm');
    form.addEventListener('submit', async () =>{
        event.preventDefault();
        const bookingID = bookingIDValue.value;
        let members = await getRowersFromBooking(bookingID);
        printMembers(members);
        removeRowersEventHandler();

    })

})