import axios from "axios";

export const closeEditButtons = () => {
    document.body.style.overflow = '';
    document.querySelector("#editUserVideos").style.display = "none";
}

export const openEditButtons = () => {
    document.body.style.overflow = 'hidden';
    document.querySelector("#editUserVideos").style.display = "flex";
}

export const openForm = (infoToUpdate) => {
    document.querySelector("#editUserVideos").style.display = "none";
    document.body.style.overflow = 'hidden';
    toggleSubButton(true);
    document.querySelector("#videoTargetPicked").value = infoToUpdate;
    switch (infoToUpdate) {
        case "title":
            toggleTextBasedLabel("block")
            updateTextBasedLabel("Enter new title")
            toggleFilePicker("none");
            toggleTextInput("block");
            toggleSubText("Update");
            updateTitle("Change Title");
            document.querySelector("#updateVideoInfoModal").style.display = 'flex';
            break
        case "description":
            toggleTextArea("block");
            toggleTextBasedLabel("block");
            updateTextBasedLabel("Enter new description");
            toggleFilePicker("none");
            toggleTextInput("none");
            toggleSubText("Update");
            updateTitle("Change description");
            document.querySelector("#updateVideoInfoModal").style.display = 'flex';
            break
        case "thumbnail":
            toggleTextBasedLabel("none")
            toggleFilePicker("flex");
            toggleTextInput("none");
            toggleSubText("Change");
            updateTitle("Change thumbnail");
            document.querySelector("#updateVideoInfoModal").style.display = 'flex';
            break
        case "delete":
            toggleSubButton(false);
            toggleTextBasedLabel("none")
            toggleFilePicker("none");
            toggleTextInput("none");
            toggleSubText("Delete");
            updateTitle("Are you sure you want to delete the video?");
            document.querySelector("#updateVideoInfoModal").style.display = 'flex';
            break
    }
}

export const resetVideoEditForm = () => {
    if (document.querySelector("#videoTargetPicked").value == "delete") {
        document.body.style.overflow = '';
        document.querySelector("#updateVideoInfoModal").style.display = "none";
    } else {
        document.querySelector("#updateVideoInfoModal").style.display = "none";
        document.querySelector("#editUserVideos").style.display = 'flex';
    }
    toggleSubButton(true);
    toggleUpdateError("", "none");
    toggleTextArea("none");
    const form = document.querySelector("#updateProfileForm");
    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
        input.style.borderColor = 'black';
    });
    form.reset();

}

export const updateThumbnailPicker = () => {
    document.querySelector("#editThumbnailName").innerHTML = "";
    document.querySelector("#changeThumbnailPicker").click();
}

export const updateFileName = (event) => {
    if (event.target.files[0]) {
        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];

        if (!allowedTypes.includes(event.target.files[0].type)) {
            toggleSubButton(true);
            toggleUpdateError("Only jpg or png image types are allowed.", "Block");

        } else {
            toggleSubButton(false);
            toggleUpdateError("", "none");
            document.querySelector("#editThumbnailName").innerHTML = event.target.files[0].name;
        }
    }


}

export const subVideoEditForm = (event, username, videoId, resetVideos) => {
    event.preventDefault();

    const action = event.target.targetPicked.value;
    let data = null;
    let thumbnail = null;

    switch (action) {
        case "title":
            data = event.target.textBasedUpdates.value;
            break;

        case "description":
            data = event.target.descriptionUpdate.value;
            break;

        case "thumbnail":
            thumbnail = event.target.thumbnailUpdates.files[0];
            break;
    }

    axios.put('https://localhost:8443/user/updatevideoinfo', { videoId: Number(videoId), action, username, data, thumbnail }, {
        withCredentials: true,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(res => {
        resetVideos()
        console.log(res)
    }).catch(err => {
        toggleUpdateError(err.response.data, "block");
        console.log(err)
    });
}

export const checkTitle = (event) => {
    if (event.target.value.length > 0 && event.target.value.length < 256) {
        toggleSubButton(false);
        event.target.style.borderColor = 'black'
        toggleUpdateError("", "none");
    } else {
        toggleSubButton(true);
        event.target.style.borderColor = 'red'
        toggleUpdateError("Title must be between 1 and 255 characters", "block");
    }
};

export const checkDescription = (event) => {
    if (event.target.value.length > 0 && event.target.value.length < 2001) {
        toggleSubButton(false);
        event.target.style.borderColor = 'black'
        toggleUpdateError("", "none");
    } else {
        toggleSubButton(true);
        event.target.style.borderColor = 'red'
        toggleUpdateError("Description must be between 1 and 2000 characters", "block");
    }
};

const toggleUpdateError = (message, display) => {
    const element = document.querySelector('#updateVidError');
    element.innerHTML = message;
    element.style.display = display;
}

const toggleSubButton = (isDisabled) => {
    document.querySelector('#ChangeVidInfoSubBtn').disabled = isDisabled;
}

const toggleSubText = (text) => {
    document.querySelector("#ChangeVidInfoSubBtn").innerHTML = text;
}

const updateTextBasedLabel = (text) => {
    document.querySelector('#vidTextBasedLabel').innerHTML = text;
}

const toggleTextBasedLabel = (display) => {
    document.querySelector('#vidTextBasedLabel').style.display = display;
}

const toggleFilePicker = (display) => {
    document.querySelectorAll(".thumbnailPicker").forEach(element => element.style.display = display)
}

const toggleTextInput = (display) => {
    document.querySelector("#videoTextUpdates").style.display = display;
}

const toggleTextArea = (display) => {
    document.querySelector("#descriptionUpdateText").style.display = display;
}

const updateTitle = (text) => {
    document.querySelector("#updateVideoTitle").innerHTML = text;
}
