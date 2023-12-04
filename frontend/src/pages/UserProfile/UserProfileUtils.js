import axios from "axios"

// Did it this way to showcase of advanced state maanged using only js. It's a better alternative to split them into their own components.

export const toggleWindow = (e, setToggle, toggle) => {

    document.querySelectorAll(".navActive").forEach(element => {
        element.classList.remove("navActive")
    })

    e.target.classList.add("navActive")

    setToggle(toggle)
}

export const updateAvatarPicker = () => {
    document.querySelector("#editAvatarName").innerHTML = "";
    document.querySelector("#changeAvatarPicker").click();
}

export const resetEditForm = () => {
    document.body.style.overflow = '';
    document.querySelector("#updateProfileModal").style.display = 'none';
    toggleSubButton(true);
    toggleUpdateError("", "none");
    const form = document.querySelector('#updateProfileForm');
    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
        input.style.borderColor = 'black';
    });
    form.reset();
}

export const openForm = (infoToUpdate) => {
    document.body.style.overflow = 'hidden';
    toggleSubButton(true);
    document.querySelector("#targetPicked").value = infoToUpdate;
    switch (infoToUpdate) {
        case "username":
            document.querySelector('#textUpdates').removeEventListener("input", checkPassword)
            document.querySelector('#textUpdates').addEventListener("input", (e) => checkUsername(e));
            toggleTextBasedLabel("block")
            updateTextBasedLabel("Enter new username")
            toggleFilePicker("none");
            toggleTextInput("block");
            toggleSubText("Update");
            updateTitle("Change username");
            document.querySelector("#updateProfileModal").style.display = 'flex';
            break
        case "password":
            document.querySelector('#textUpdates').removeEventListener("input", checkUsername)
            document.querySelector('#textUpdates').addEventListener("input", (e) => checkPassword(e));
            toggleTextBasedLabel("block")
            updateTextBasedLabel("Enter new password")
            toggleFilePicker("none");
            toggleTextInput("block");
            toggleSubText("Update");
            updateTitle("Change password");
            document.querySelector("#updateProfileModal").style.display = 'flex';
            break
        case "avatar":
            toggleTextBasedLabel("none")
            toggleFilePicker("flex");
            toggleTextInput("none");
            toggleSubText("Change");
            updateTitle("Change avatar");
            document.querySelector("#updateProfileModal").style.display = 'flex';
            break
        case "delete":
            toggleSubButton(false);
            toggleTextBasedLabel("none")
            toggleFilePicker("none");
            toggleTextInput("none");
            toggleSubText("Delete");
            updateTitle("Are you sure you want to delete your profile?");
            document.querySelector("#updateProfileModal").style.display = 'flex';
            break
    }
}

export const subEditForm = (event, username, userRole, setUser) => {
    event.preventDefault();
    const target = event.target.targetPicked.value;
    const password = event.target.cPassword.value;
    let data = null;
    let avatar = null;

    switch (target) {
        case "username":
        case "password":
            data = event.target.textBasedUpdates.value;
            break;
        case "avatar":
            avatar = event.target.avatarUpdates.files[0];
            break;
    }

    axios.put('https://localhost:8443/user/updateuserinfo', { target, username, password, userRole, data, avatar }, {
        withCredentials: true,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(res => {
        if(res.status == 204) {
            setTimeout(() => {
                setUser({});
                window.location.href = "/";
                return;
            }, 100)
        }
        setUser(res.data);
        resetEditForm();
    }).catch(err => {
        toggleUpdateError(err.response.data.message, "block");
        event.target.querySelectorAll('input').forEach(input => {
            input.value = null;
        });
    });
}

const toggleFilePicker = (display) => {
    document.querySelectorAll(".avatarPicker").forEach(element => element.style.display = display)
}

const toggleTextInput = (display) => {
    document.querySelector("#textUpdates").style.display = display;
}

const toggleSubText = (text) => {
    document.querySelector("#ChangeSubBtn").innerHTML = text;
}

const updateTitle = (text) => {
    document.querySelector("#updateProfileTitle").innerHTML = text;
}

const updateTextBasedLabel = (text) => {
    document.querySelector('#textBasedLabel').innerHTML = text;
}

const toggleTextBasedLabel = (display) => {
    document.querySelector('#textBasedLabel').style.display = display;
}


const toggleSubButton = (isDisabled) => {
    document.querySelector('#ChangeSubBtn').disabled = isDisabled;
}

const toggleUpdateError = (message, display) => {
    const element = document.querySelector('#updateError');
    element.innerHTML = message;
    element.style.display = display;
}

// Info check functions:

const checkUsername = (event) => {
    const USERNAME_REGEX = /^[a-zA-Z0-9]{5,16}$/;
    if (USERNAME_REGEX.test(event.target.value)) {
        toggleSubButton(false);
        event.target.style.borderColor = 'black'
        toggleUpdateError("", "none");
    } else {
        toggleSubButton(true);
        event.target.style.borderColor = 'red'
        toggleUpdateError("Username must be between 5 and 16 characters long, symbols and whitespace aren't allowed", "block");
    }
};

const checkPassword = (event) => {
    const PASSWORD_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/;

    if (PASSWORD_REGEX.test(event.target.value)) {
        toggleSubButton(false);
        event.target.style.borderColor = 'black'
        toggleUpdateError("", "none");
    } else {
        toggleSubButton(true);
        event.target.style.borderColor = 'red'
        toggleUpdateError("Password must be at least 8 characters long, and contain at least one lowercase letter, one uppercase letter, one digit and one special character", "block");
    }
};

export const updateFileName = (event) => {
    if (event.target.files[0]) {
        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];

        if (!allowedTypes.includes(event.target.files[0].type)) {
            toggleSubButton(true);
            toggleUpdateError("Only jpg or png image types are allowed.", "Block");

        } else {
            toggleSubButton(false);
            toggleUpdateError("", "none");
            document.querySelector("#editAvatarName").innerHTML = event.target.files[0].name;
        }
    }


}
