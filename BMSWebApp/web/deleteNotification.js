const notificationMessageInputEl = document.querySelector('#notificationMessage');
const deleteNotificationFormEl = document.querySelector('#deleteNotificationForm');

async function submitNotification() {
    let message = notificationMessageInputEl.value;
    const data = {
        message: message
    }
    await fetch('/BMSWebApp/deleteNotification', {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8'
        }),
        body: JSON.stringify(data)
    });
}

deleteNotificationFormEl.addEventListener('submit', submitNotification)