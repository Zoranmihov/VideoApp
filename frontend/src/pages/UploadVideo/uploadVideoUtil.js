import axios from "axios";


export const updateFileName = (e) => {
    if (e.target.files[0]) {
        if (e.target.files[0].type != 'video/mp4') {
            const error = document.querySelector("#uploadError");
            error.innerHTML = "Only videos of type mp4 are allowed";
            error.style.display = 'block';

        } else {
            document.querySelector("#uploadError").style.display = 'none';
            document.querySelector("#fileName").innerHTML = e.target.files[0].name;

        }

    }
}

export const updateThumbnail = (e) => {
    if (e.target.files[0]) {
        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];

        if (!allowedTypes.includes(e.target.files[0].type)) {
            const error = document.querySelector("#uploadError");
            error.innerHTML = "Only pictures of type jpeg, jpg and png are allowed";
            error.style.display = 'block';

        } else {
            document.querySelector("#uploadError").style.display = 'none';
            document.querySelector("#thumbnailName").innerHTML = e.target.files[0].name;
            const img = document.querySelector("#thumbnailPreview").src = URL.createObjectURL(e.target.files[0]);
            document.querySelector("#thumbnailContainer").style.display = "block";
        }
    }
}

export const filePick = () => {
    document.querySelector("#fileInput").click();
}

export const thumbnailPick = () => {
    document.querySelector("#thumbnailInput").click();
}

export const clearForm = (form) => {
    document.querySelector("#uploadError").style.display = "none";
    document.querySelector("#fileName").innerHTML = "";
    document.querySelector("#thumbnailName").innerHTML = "";
    document.querySelector("#thumbnailContainer").style.display = "none";
    form.reset();

}

export const videoUpload = (event) => {
    event.preventDefault();

    const formData = new FormData();
    formData.append('title', event.target.title.value);
    formData.append('description', event.target.description.value);
    formData.append('video', event.target.video.files[0]);
    formData.append('thumbnail', event.target.thumbnail.files[0]);

    axios.post('https://localhost:8443/user/uploadvideo', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        withCredentials: true,
    })
        .then(res => console.log("Worked"))
        .catch(err => console.log(err));
}