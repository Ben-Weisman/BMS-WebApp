const infoDivEL = document.querySelector('#info');

function createLIelement(booking) {
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
    const ul = document.createElement('ul');

    bookings.forEach(booking => {
        ul.appendChild(createLIelement(booking));
    })
    infoDivEL.appendChild(ul);
}

window.addEventListener('load',async () =>{
    let response = await fetch('getReqHistory');
    let bookings = await response.json();
    if (bookings.length === 0)
        infoDivEL.innerHTML = "<h1>You don't have requests history yet.</h1>";
    else printBookings(bookings);

})