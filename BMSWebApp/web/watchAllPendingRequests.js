const bookingsDivEL = document.querySelector('#bookings');
const optionsDivEL = document.querySelector('#options');

function createLIelement(booking) {
    let li = document.createElement('li');
    const bookingID = booking.bookingID;
    let requestedDate = new Date();
    li.setAttribute('id', 'bookingOption');
    requestedDate.setFullYear(booking.requestedPracticeDate.year, booking.requestedPracticeDate.month, booking.requestedPracticeDate.day);
    li.innerHTML += 'Requested Practice Date: ' + requestedDate.toLocaleDateString() + '<br>';
    li.innerHTML += 'Booking ID: ' + bookingID + '<br><br>';
    return li;
}

function printBookings(bookings) {
    const ul = document.createElement('ul');

    bookings.forEach(booking => {
        ul.appendChild(createLIelement(booking));
    })
    bookingsDivEL.appendChild(ul);
}


function printOptions() {
    const html = "        <ul class=\"optionLinks\">\n" +
        "                <span>Edit Booking Request</span><br>\n" +
        "            <span>Please prepare the ID of the booking you want to modify.</span><br><br>\n" +
        "                <li><a href=\"assignBoat.html\">Assign Boat</a> </li>\n" +
        "                <li><a href=\"addRowers.html\">Add Rowers</a> </li>\n" +
        "                <li><a href=\"removeRowers.html\">Remove Rowers</a> </li>\n" +
        "            </ul>";
    optionsDivEL.innerHTML = html;
}

window.addEventListener('load', async () => {
    let response = await fetch('getPending');
    let bookings = await response.json();
    if (bookings.length === 0)
        bookingsDivEL.innerHTML = "<h1>No pending requests at the moment. </h1>";
    else {
        printBookings(bookings);
        printOptions();
    }
})