import axios from "axios";

const USERNAME_REGEX = /^[a-zA-Z0-9]{5,16}$/;
const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/;

const setError = (message) => {
    const errorElement = document.querySelector("#error");
    errorElement.innerHTML = message;
    errorElement.style.display = message ? "block" : "none";
};

const setInputBorder = (element, color) => {
    element.style.borderBottom = `2px solid ${color}`;
};

export const checkUsername = (usernameInput) => {
    const password = document.querySelector("input[name='password']").value;
    const cPassword = document.querySelector("input[name='Cpassword']");

    if (USERNAME_REGEX.test(usernameInput.value)) {
        setError("");
        setInputBorder(usernameInput, "black");

        if (PASSWORD_REGEX.test(password) && password === cPassword.value) {
            document.querySelector("#registerBtn").disabled = false;
            setInputBorder(cPassword, "black");
        }
    } else {
        setError("Username must be between 5 and 16 characters long, symbols aren't allowed");
        setInputBorder(usernameInput, "red");
        document.querySelector("#registerBtn").disabled = true;
    }
};

export const checkPassword = (passwordInput) => {
    const cPassword = document.querySelector("input[name='Cpassword']");
    if (PASSWORD_REGEX.test(passwordInput.value)) {
        setError("");
        setInputBorder(passwordInput, "black");
        if (passwordInput.value === cPassword.value) {
            setInputBorder(cPassword, "black");
        }
    } else {
        setError("Password must be at least 8 characters long, and contain at least one lowercase letter, one uppercase letter, one digit and one special character");
        setInputBorder(passwordInput, "red");
        document.querySelector("#registerBtn").disabled = true;
    }
};

export const checkCPassword = (cPasswordInput) => {
    const password = document.querySelector("input[name='password']").value;
    if (cPasswordInput.value === password && PASSWORD_REGEX.test(password)) {
        setError("");
        setInputBorder(cPasswordInput, "black");
    } else {
        setError("Password fields don't match");
        setInputBorder(cPasswordInput, "red");
        document.querySelector("#registerBtn").disabled = true;
    }
};

export const resetForm = () => {
    document.querySelectorAll(".registerInput").forEach(input => {
        input.style.borderBottom = "2px solid black";
    })
    document.querySelector("#error").style.display = "none";
};

export const axiosRegister = (username, password) => {
    axios.post('https://localhost:8443/auth/register', { username, password }).then(res => {
        document.querySelector("#title").innerHTML = "Welcome";
        setTimeout(() => document.querySelector("#toggleForm").click(), 1200);
    }).catch(err => {
        console.log(err);
        document.querySelector("#title").style.color = 'red';
        let error = document.querySelector("#error");
        error.innerHTML = err.response.data;
        error.style.display = "inline";
    })

}