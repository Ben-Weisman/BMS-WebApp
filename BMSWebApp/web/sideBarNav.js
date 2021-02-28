const navBarEL = document.getElementById("sideNav");

function injectAdminSideNavBar(){
    navBarEL.innerHTML = "<nav>\n" +
        "    <ul class=\"nav\">\n" +
        "        <li><a href=\"updatePersonalInfo.html\">Update Personal Info</a> </li>\n" +
        "        <li><a href=\"submitNewBookingRequest.html\">Submit New Booking Request</a> </li>\n" +
        "        <li><a href=\"showAllFutureBookingRequests.html\">Show All Future Booking Requests</a> </li>\n" +
        "        <li><a href=\"showRequestsHistory.html\">Show Requests History</a> </li>\n" +
        "        <li><a href=\"maintain.html\">Maintain</a> </li>\n" +
        "        <li><a href=\"watchBookingRequests.html\">Watch Booking Requests</a> </li>\n" +
        "        <li><a href=\"importFromXML.html\">Import From XML</a> </li>\n" +
        "        <li><a href=\"exportToXML.html\">Export To XML</a> </li>\n" +
        "        <li><a href=\"addNotification.html\">Add Notification</a> </li>\n" +
        "        <li><a href=\"deleteNotification.html\">Delete Notification</a> </li>\n" +
        "        <li><a href=\"logout\">Logout</a> </li>\n " +
        "    </ul>\n" +
        "</nav>";

}
function injectRegularSideBar(){
    navBarEL.innerHTML = "<nav>\n" +
        "    <ul class=\"nav\">\n" +
        "        <li><a href=\"updatePersonalInfo.html\">Update Personal Info</a> </li>\n" +
        "        <li><a href=\"submitNewBookingRequest.html\">Submit New Booking Request</a> </li>\n" +
        "        <li><a href=\"showAllFutureBookingRequests.html\">Show All Future Booking Requests</a> </li>\n" +
        "        <li><a href=\"showRequestsHistory.html\">Show Requests History</a> </li>\n" +
        "        <li><a href=\"logout\">Logout</a> </li>\n " +
        "    </ul>\n" +
        "</nav>";
}

async function isAdmin() {
    let response = await fetch('adminVerify');
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