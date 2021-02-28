const adminNotificationsUlEl = document.querySelector("#adminNotifications");
const autoNotificationsUlEl = document.querySelector("#autoNotifications");
const refreshRate = 5000;


function createLiEl(listing) {
    const li = document.createElement("li");
    for (let property in listing){
        li.innerHTML += property + " : " + listing[property] + '\t';
    }
    return li;
}

function updateNotifications(adminNotifications, autoNotifications) {
    adminNotificationsUlEl.innerHTML = "";
    adminNotifications.forEach((notification)=>{
        const listingEl = createLiEl(notification);
        adminNotificationsUlEl.append(listingEl);
    })
    autoNotificationsUlEl.innerHTML = "";
    autoNotifications.forEach((notification)=>{
        const listingEl = createLiEl(notification);
        autoNotificationsUlEl.append(listingEl);
    })
}

async function fetchNotificationsAsync() {
    const response = await fetch('/BMSWebApp/pullNotifications');
    const notificationsData = await response.json();
    /*
      data will receive in following json template:
      {
          adminNotifications:[ Notifications_Array ],
          adminNotificationsVersion: the version (int),
          autoNotifications: [ Notifications_Array ]
      }
   */
    updateNotifications(notificationsData['adminNotifications'], notificationsData['autoNotifications']);
}

window.addEventListener('load', () => {
    setInterval(fetchNotificationsAsync, refreshRate);
});