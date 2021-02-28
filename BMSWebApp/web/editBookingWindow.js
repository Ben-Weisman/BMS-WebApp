const form = document.querySelector("#bookingWindowForm");

async function handleFormSubmition() {
    const bookingID = document.querySelector('#bookingIDInput').value;
    const newWindowID = document.querySelector('#newWindowID').value;

    const data = {newWindowID , bookingID};

    let options = {
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }

    let response = await fetch('updateWindow',options);
    let json = await response.json();
    if (json.status === "ok")
        alert("Window updated successfully!");
    else editWindowEL.innerHTML = "Failed to update window" + json.message;

}

window.addEventListener('load', ()=>{
    form.addEventListener('submit', async () =>{
        await handleFormSubmition();
    })
})