import axios from "axios";


const enableSubmit = () => {
    const USERNAME_REGEX = /^[a-zA-Z0-9]{5,16}$/;
    const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/;
    const username = document.querySelector("input[name='username']").value;
    const password = document.querySelector("input[name='password']").value;
    const cpassword = document.querySelector("input[name='Cpassword']").value;


    if (USERNAME_REGEX.test(username) && PASSWORD_REGEX.test(password) && password == cpassword) {
        document.querySelector("#registerBtn").disabled = false;
    }

}

const setError = (display, message) => {
    let error = document.querySelector("#registerError");

    error.innerHTML = message
    error.style.display = display;
}

const clearError = (eventTarget) => {
    eventTarget.style.borderColor = 'black';
    setError("none", "");
    enableSubmit();
}


export const checkUsername = (event) => {
    const USERNAME_REGEX = /^[a-zA-Z0-9]{5,16}$/;
    if (USERNAME_REGEX.test(event.target.value)) {
        clearError(event.target);
    } else {
        setError("block", "Username must be between 5 and 16 characters long, symbols and whitespace aren't allowed");
        event.target.style.borderColor = 'red'
    }
};

export const checkPassword = (event) => {
    const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/;

    if (PASSWORD_REGEX.test(event.target.value)) {
        clearError(event.target);
    } else {
        setError("block", "Password must be at least 8 characters long, and contain at least one lowercase letter, one uppercase letter, one digit and one special character");
        event.target.style.borderColor = 'red'
    }
};

export const checkCPassword = (event) => {
    const password = document.querySelector("input[name='password']").value;
    if (event.target.value === password) {
        clearError(event.target);

    } else {
        setError("block", "Password fields don't match");
        event.target.style.borderColor = 'red'
    }
};

export const resetForm = () => {
    document.querySelectorAll(".registerInput").forEach(input => {
        input.style.borderColor = "black";
    })
    document.querySelector("#registerError").style.display = "none";
    document.querySelector("#avatarPreview").style.display = "none";
    document.querySelector("#avatarName").innerHTML = "";

};

export const axiosRegister = (username, password, avatar) => {

    axios.post('https://localhost:8443/auth/register', { username, password, avatarFile: avatar }, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(res => {
        document.querySelector("#title").innerHTML = "Welcome";
        setTimeout(() => document.querySelector("#toggleForm").click(), 1200);
    }).catch(err => {
        document.querySelector("#title").style.color = 'red';
        let error = document.querySelector("#registerError");
        error.innerHTML = err.response.data;
        error.style.display = "inline";
    })

}

export const registerUser = (event) => {
    event.preventDefault();
    const username = event.target.username.value;
    const password = event.target.password.value;

    if (password != event.target.Cpassword.value) {
        document.querySelector("#title").style.color = 'red';
        let registerError = document.querySelector("#registerError")
        registerError.innerHTML = "Passwords don't match";
        registerError.style.display = "inline"
        return

    }

    document.querySelector("#title").style.color = 'black';
    document.querySelector("#registerError").style.display = 'none';
    let avatar = null;
    if (event.target.avatar.files[0]) {

        avatar = event.target.avatar.files[0]
    }

    axiosRegister(username, password, avatar);
}

export const openFilePicker = () => {
    document.querySelector("#avatarImg").click()
}

export const updateAvartar = (e) => {
    if (e.target.files[0]) {
        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];

        if (!allowedTypes.includes(e.target.files[0].type)) {
            const error = document.querySelector("#registerError");
            error.innerHTML = "Only pictures of type jpeg, jpg and png are allowed";
            error.style.display = 'block';

        } else {
            document.querySelector("#registerError").style.display = 'none';
            document.querySelector("#avatarName").innerHTML = e.target.files[0].name;
            document.querySelector("#avatarImgDisplay").src = URL.createObjectURL(e.target.files[0]);
            document.querySelector("#avatarPreview").style.display = "block";
        }
    }
}