const navBarEL = document.getElementById("sideNav");

function injectAdminSideNavBar(){
    navBarEL.innerHTML = "<nav>\n" +
        "    <ul class=\"nav\">\n" +
        "        <li><a href=\"/BMSWebApp/updatePersonalInfo.html\">Update Personal Info</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/submitNewBookingRequest.html\">Submit New Booking Request</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/showAllFutureBookingRequests.html\">Show All Future Booking Requests</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/showRequestsHistory.html\">Show Requests History</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/maintain.html\">Maintain</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/watchBookingRequests.html\">Watch Booking Requests</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/importFromXML.html\">Import From XML</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/exportToXML.html\">Export To XML</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/addNotification.html\">Add Notification</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/deleteNotification.html\">Delete Notification</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/logout\">Logout</a> </li>\n " +
        "    </ul>\n" +
        "</nav>";

}
function injectRegularSideBar(){
    navBarEL.innerHTML = "<nav>\n" +
        "    <ul class=\"nav\">\n" +
        "        <li><a href=\"/BMSWebApp/updatePersonalInfo.html\">Update Personal Info</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/submitNewBookingRequest.html\">Submit New Booking Request</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/showAllFutureBookingRequests.html\">Show All Future Booking Requests</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/showRequestsHistory.html\">Show Requests History</a> </li>\n" +
        "        <li><a href=\"/BMSWebApp/logout\">Logout</a> </li>\n " +
        "    </ul>\n" +
        "</nav>";
}

async function isAdmin() {
    let response = await fetch('/BMSWebApp/adminVerify');
    let json =  await response.json();
    console.log(json.isAdmin)
    return json.isAdmin;
}

window.addEventListener('load',async ()=>{
    let admin = await isAdmin();
    console.log(admin);
    switch (admin){
        case false:
            injectRegularSideBar();
            break;
        case true:
            injectAdminSideNavBar();
            break;
    }
})