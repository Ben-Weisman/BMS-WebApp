const getBoatsButtonEl = document.querySelector("#getBoats");
const getBookingsButtonEl = document.querySelector("#getBookings");
const getMembersButtonEl = document.querySelector("#getMembers");
const getScheduleWindowsButtonEl = document.querySelector("#getScheduleWindows");

const listingUlEl = document.querySelector("#listingListPlaceHolder")

function createLiEl(listing) {
    const li = document.createElement("li");
    for (let property in listing){
        li.innerHTML += property + " : " + listing[property] + '<br>';
    }
    return li;
}

function updateListingEl(listings) {
    listingUlEl.innerHTML = "";
    listings.forEach((listing)=>{
        const listingEl = createLiEl(listing);
        listingUlEl.append(listingEl);
    })
}

async function getBoatsList() {
    const response = await fetch('/BMSWebApp/getAllBoats');
    const boats = await response.json();
    updateListingEl(boats);
}

if (getBoatsButtonEl) {getBoatsButtonEl.addEventListener('click', getBoatsList);}

async function getBookingsList() {
    const response = await fetch('/BMSWebApp/getAllBookings');
    const bookings = await response.json();
    updateListingEl(bookings);
}

if (getBookingsButtonEl){getBookingsButtonEl.addEventListener('click', getBookingsList);}

async function getMembersList() {
    const response = await fetch('/BMSWebApp/getAllMembers');
    const members = await response.json();
    updateListingEl(members);
}

if (getMembersButtonEl){getMembersButtonEl.addEventListener('click', getMembersList);}

async function getScheduleWindowsList() {
    const response = await fetch('/BMSWebApp/getAllScheduleWindows');
    const scheduleWindows = await response.json();
    updateListingEl(scheduleWindows);
}

if (getScheduleWindowsButtonEl){getScheduleWindowsButtonEl.addEventListener('click', getScheduleWindowsList);}
