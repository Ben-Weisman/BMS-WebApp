const notificationMessageInputEl = document.querySelector('#notificationMessage');
const addNotificationFormEl = document.querySelector('#addNotificationForm');

async function submitNotification() {
    event.preventDefault();
    let message = notificationMessageInputEl.value;
    const data = {
        message: message
    }
    await fetch('/BMSWebApp/sendNotification', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });
}

addNotificationFormEl.addEventListener('submit', submitNotification)