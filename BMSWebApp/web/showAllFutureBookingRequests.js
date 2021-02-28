const bookingsDivEL = document.querySelector('#bookings');
const optionsDivEL = document.querySelector('#options');
const deleteButtonEL = document.createElement('button');
const editRequestedWindowButtonEL = document.createElement('button');
const editRequestedPracticeDateButtonEL = document.createElement('button');
const editRequestedBoatTypeButtonEL = document.createElement('button');

async function fetchBookings() {
    const response = await fetch('futureBookings');
    const resJson = await response.json();
    return resJson;

}

function printBooking(booking) {
    let li = document.createElement('li');
    const bookingID = booking.bookingID;
    let requestedDate = new Date();
    li.setAttribute('id', 'bookingOption');
    requestedDate.setFullYear(booking.requestedPracticeDate.year, booking.requestedPracticeDate.month, booking.requestedPracticeDate.day);
    li.innerHTML += 'Requested Practice Date: ' + requestedDate.toLocaleDateString() + '<br>';
    li.innerHTML += 'Booking ID: ' + bookingID + '<br><br>';
    return li;
}

function showOptions() {
    optionsDivEL.innerHTML = "";
    optionsDivEL.innerHTML += "<h2>Edit booking options</h2>";
    optionsDivEL.innerHTML += "        <ul class=\"options\">\n" +
        "                <li><a href=\"editBoatType.html\">Edit Boat Type</a> </li>\n" +
        "                <li><a href=\"editBookingDate.html\">Edit Booking Date</a> </li>\n" +
        "                <li><a href=\"editBookingWindow.html\">Edit Booking Window</a> </li>\n" +
        "               <li> <a href=\"deleteBooking.html\">Delete Booking</a></li>\n" +
        "            </ul>";

}

function setupEditBookingEventHandler() {
    deleteButtonEL.addEventListener('click', () => {
        injectForm();
        displayOptions();

    })
}


function setupOptionsEventHandler() {
    editRequestedBoatTypeButtonEL.addEventListener('click', () => {
        location.replace("editBoatType.html");
    })
    editRequestedPracticeDateButtonEL.addEventListener('click', () => {
        location.replace("editBookingDate.html");
    })

    editRequestedWindowButtonEL.addEventListener('click', () => {
        location.replace("editBookingWindow.html");
    });
}

function showBookings(bookings) {
    const ul = document.createElement('ul');
    if (bookings.length === 0) {
        bookingsDivEL.innerHTML += "<span><h2>No future bookings available for this account.<br></h2></span>";
    } else {
        bookings.forEach(booking => {
            ul.appendChild(printBooking(booking));
        })
        bookingsDivEL.appendChild(ul);
        showOptions();
        setupOptionsEventHandler();
    }
}

window.addEventListener('load', async () => {

    const bookings = await fetchBookings();
    showBookings(bookings);
})
