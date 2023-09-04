export let checkPassword = (passwordInput) => {
    let passRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");

    if (passRegex.test(passwordInput.value)) {
        passwordInput.style.borderBottom = "2px solid black"
        document.querySelector("#error").style.display = "none";
        document.querySelector("#registerBtn").disabled = false;
        document.querySelector("#error").innerHTML = ""

    } else {
        passwordInput.style.borderBottom = "2px solid red"
        document.querySelector("#error").innerHTML = "Password must be atleast 8 characters long, and contain at least one lowercase letter, one uppercase letter, one digit and one special character";
        document.querySelector("#error").style.display = "block";
        document.querySelector("#registerBtn").disabled = true;
    }
}

export let checkUsername = (usernameInput) => {

    let usernameRegex = new RegExp("^[a-zA-Z0-9]{5,16}$");

    if(usernameRegex.test(usernameInput.value)) {
        document.querySelector("#error").style.display = "none";
        document.querySelector("#registerBtn").disabled = false;
        document.querySelector("#error").innerHTML = ""
        usernameInput.style.borderBottom = "2px solid black"

    } else {
        usernameInput.style.borderBottom = "2px solid red"
        document.querySelector("#error").innerHTML = "Username must be at least 5 characters long, symbols aren't allowed";
        document.querySelector("#error").style.display = "block";
        document.querySelector("#registerBtn").disabled = true;
    }
}

export let resetForm = () => {
    document.querySelectorAll(".registerInput").forEach(input => {
        input.style.borderBottom = "2px solid black"
    })
    document.querySelector("#error").style.display = "none";
}