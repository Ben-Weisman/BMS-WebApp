const bookingsDivEL = document.querySelector('#bookings');


function printBooking(booking) {
    let li = document.createElement('li');
    const bookingID = booking.bookingID;
    let requestedDate = new Date();
    li.setAttribute('id','bookingOption');
    requestedDate.setFullYear(booking.requestedPracticeDate.year,booking.requestedPracticeDate.month,booking.requestedPracticeDate.day);
    li.innerHTML += 'Requested Practice Date: ' + requestedDate.toLocaleDateString() + '<br>';
    li.innerHTML += 'Booking ID: ' + bookingID + '<br><br>';
    return li;
}

function printBookings(bookings) {
    let ul = document.createElement('ul');

    bookings.forEach(booking => {
        ul.appendChild(printBooking(booking));
    })
    bookingsDivEL.appendChild(ul);
}



window.addEventListener('load', async () =>{
    let response = await fetch('getAllBookings');
    let bookings = await response.json();
    if (bookings.length === 0)
        bookingsDivEL.innerHTML = "<h1>You don't have requests history yet.</h1>";
    else printBookings(bookings);

})