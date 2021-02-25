const infoDivEL = document.querySelector('#info');
const personalInfoFormEL = document.querySelector('#updatePersonalInfoForm');

function show(){
    document.getElementById('subNav').classList.toggle('show');
}

function showForm(fieldName){
    console.log("Currently in function showForm()");
    let html;
    switch (fieldName){
        case 'password':
            personalInfoFormEL.setAttribute('name', 'password');
            html = "<label>Update password </label>" +
                "<input type=\"password\" id=\"valueText\" name=\"param\">" +
                "<input type=\"submit\" value=\"submit\">";
            personalInfoFormEL.innerHTML = html;
            break;
        case 'name':
            personalInfoFormEL.setAttribute('name', 'name');
            html = "<label>Update name</label>" +
                "<input type=\"text\" id=\"valueText\" name=\"param\">" +
                "<input type=\"submit\" value=\"submit\">";
            personalInfoFormEL.innerHTML = html;
            break;
        case 'phone':
            personalInfoFormEL.setAttribute('name', 'phone');
            html = "<label>Update phone</label>" +
                "<input type=\"text\" id=\"valueText\" name=\"param\">" +
                "<input type=\"submit\" value=\"submit\">";
            personalInfoFormEL.innerHTML = html;
            break;
        case 'email':
            personalInfoFormEL.setAttribute('name', 'email');
            html = "<label>Update email</label>" +
                "<input type=\"text\" id=\"valueText\" name=\"param\">" +
                "<input type=\"submit\" value=\"submit\">";
            personalInfoFormEL.innerHTML = html;
            break;
    }


    // personalInfoFormEL.setAttribute('class',"updatePersonalInfoForm");
    // personalInfoFormEL.setAttribute('id',"updatePersonalInfoForm");
    // personalInfoFormEL.setAttribute('name',fieldName);
    // personalInfoFormEL.setAttribute('action','updateMemberData');
    // personalInfoFormEL.setAttribute('method','post');
    // personalInfoFormEL.setAttribute('submit','validateForm()');
    //
    // let i = document.createElement('input');
    // i.setAttribute('type','text');
    // i.setAttribute('name','param');
    // i.setAttribute('id','valueText')
    //
    // let s = document.createElement('input');
    // s.setAttribute('type','submit');
    // s.setAttribute('value','submit');
    //
    // let text = document.createTextNode('Please enter the new ' + fieldName + '  ');
    // let label = document.createElement('label');
    // label.appendChild(text);
    // personalInfoFormEL.appendChild(label);
    // personalInfoFormEL.appendChild(i);
    // personalInfoFormEL.appendChild(s);
    // infoDivEL.append(personalInfoFormEL);

}

function clearFormDisplay(){
    personalInfoFormEL.innerHTML = "";
}

function validateName(param) {
    console.log('validating name...');
    if (param === null || param === "" || !(/^[A-Za-z\s]+$/.test(param))) {
        alert('Name is illegal. Please enter again.')
        return false;
    }
    return true;
}

function validatePhone(param){
    console.log('validating phone...');
    if (param === null || param === ""){
        alert('Phone is illegal. Please enter again.')
        return false;
    }
    return true;
}

function validateEmail(param){
    console.log('validating email...');
    if (param === null || param === ""){
        alert('Email is illegal. Please enter again.')
        return false;
    }
    return true;

}

function validatePassword(param){
    console.log('validating password...');
    if (param === null || param === ""){
        alert('Password is illegal. Please enter again.')
        return false;
    }
    return true;

}


async function postData(param, fieldName){
    const data = {fieldName,param};
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }
    return await fetch('personalInfo', options);
}


async function validateForm(event) {
    event.preventDefault();
    let param = document.querySelector('#valueText').value;
    switch (personalInfoFormEL.name){
        case 'name':
            if (!validateName(param))
                return false;
            break;
        case 'phone':
            if (!validatePhone(param))
                return false;
            break;
        case 'email':
            if (!validateEmail(param))
                return false;
            break;
        case 'password':
            if (!validatePassword(param))
                return false;
            break;
        default: return false;
    }
    const response = await postData(param, personalInfoFormEL.name);
    const json = await response.json();
    //handleResponse(json)


    console.log(json);

}



    function handleUpdatePersonalInfoEvent(chosenFieldName) {
        clearFormDisplay();

        showForm(chosenFieldName);
    }


    function setupEventHandlers(){

        const updateNameEL = document.querySelector('#updateName');
        updateNameEL.addEventListener('click', function(){handleUpdatePersonalInfoEvent('name')});

        const updatePhoneEL = document.querySelector('#updatePhone');
        updatePhoneEL.addEventListener('click', function (){handleUpdatePersonalInfoEvent('phone')});

        const updateEmailEL = document.querySelector('#updateEmail');
        updateEmailEL.addEventListener('click', function (){handleUpdatePersonalInfoEvent('email')});

        const updatePasswordEL = document.querySelector('#updatePassword');
        updatePasswordEL.addEventListener('click', function (){handleUpdatePersonalInfoEvent('password')});

        personalInfoFormEL.addEventListener('submit',validateForm);
 }

    window.addEventListener('load', function () {

        const infoDivEL = document.querySelector("info");
        setupEventHandlers();
        personalInfoFormEL.addEventListener('submit',validateForm);


    })










