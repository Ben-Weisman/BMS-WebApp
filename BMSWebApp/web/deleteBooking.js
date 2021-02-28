const form = document.querySelector("#bookingDeletion");


async function handleDeletionRequest() {
    const bookingID = document.querySelector('#bookingIDInput').value;
    const data = {bookingID};
    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }

    let response = await fetch('removeBooking',options);
    let json = await response.json();
}

window.addEventListener('load', () =>{
    form.addEventListener('submit',async () =>{
        await handleDeletionRequest();
    })
})