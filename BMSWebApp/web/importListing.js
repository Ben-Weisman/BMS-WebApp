const importBoatsFormEl = document.querySelector("#importBoats");
const importMembersFormEl = document.querySelector("#importMembers");
const importScheduleWindowsFormEl = document.querySelector("#importScheduleWindows");
const boatsFileInputEl = document.querySelector("#boatsFile");
const membersFileInputEl = document.querySelector("#membersFile");
const scheduleWindowsFileInputEl = document.querySelector("#scheduleWindowsFile");
const messagePlaceHolderEl = document.querySelector('.messagePlaceHolder');

async function submitImportListing(fileType, file) {
    let formData = new FormData();
    formData.append(fileType, file);
    const response = await fetch('/BMSWebApp/importListing', {
        method: 'POST',
        body: formData
    });
    updateSubmitMessage(await response.text(), response.status);
}

function submitImportBoats(event) {
    event.preventDefault();
    submitImportListing("boatsFile", boatsFileInputEl.files[0]);
}

importBoatsFormEl.addEventListener('submit', submitImportBoats);

function submitImportMembers(event) {
    event.preventDefault();
    submitImportListing("membersFile",membersFileInputEl.files[0]);
}

importMembersFormEl.addEventListener('submit', submitImportMembers);

function submitImportScheduleWindows(event) {
    event.preventDefault();
    submitImportListing("scheduleWindowsFile", scheduleWindowsFileInputEl.files[0]);
}

importScheduleWindowsFormEl.addEventListener('submit', submitImportScheduleWindows);

function updateSubmitMessage(message, responseStatus) {
    let HttpCodes = {
        success : 200,
        needToRemoveOther : 409
    }
    messagePlaceHolderEl.innerHTML=message;
    if (responseStatus === HttpCodes.success){
        messagePlaceHolderEl.setAttribute("style", "color:black;")
    }
    else{
        messagePlaceHolderEl.setAttribute("style", "color:red;")
    }
}
