const form = document.querySelector('#dateForm');


async function submitNewDate() {
    const bookingID = document.querySelector('#bookingIDInput').value;
    const newDate = document.querySelector('#bookingDate').value;

    const data = {bookingID,newDate};
    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }

    let response = await fetch('updateBookingDate',options);
    let json = await response.json();
}

window.addEventListener('load', () =>{
    form.addEventListener('submit', async () =>{
        submitNewDate();
    })
})