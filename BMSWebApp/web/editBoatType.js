const form = document.querySelector("#boatTypeForm");


async function handleBoatTypeUpdate() {
    const bookingID = document.querySelector('#bookingIDInput').value;
    const boatTypes = document.querySelector('#boatType').value;

    const data = {bookingID, boatTypes};

    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    let response = await fetch('updateBoatType',options);
    let json = await response.json();
    if (json.status === "ok")
        alert("Window updated successfully!");
    else alert("Failed to update window: " + json.message);
}

window.addEventListener('load', ()=>{
    form.addEventListener('submit', async ()=>{
        await handleBoatTypeUpdate()
    })
})
