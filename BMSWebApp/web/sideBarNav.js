const navBarEL = document.getElementById("sideNav");

function injectSideNavBar(){

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
        "    </ul>\n" +
        "</nav>";

}

window.addEventListener('load',()=>{
    injectSideNavBar();
})