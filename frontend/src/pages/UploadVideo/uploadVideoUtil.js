import axios from "axios";


export const updateFileName = (e) => {
    if (e.target.files[0]) {


        if (e.target.files[0].type != 'video/mp4' && e.target.files[0].type !== 'video/quicktime') {
            const error = document.querySelector("#uploadError");
            error.innerHTML = "Only videos of type mp4 or mov are allowed";
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
    document.querySelector("#uploadSub").disabled = true;
    const formData = new FormData();
    formData.append('title', event.target.title.value);
    formData.append('description', event.target.description.value);
    formData.append('video', event.target.video.files[0]);
    if (event.target.thumbnail.files[0]) {
        // Get the thumbnail file.
        const thumbnailFile = event.target.thumbnail.files[0];

        // Create a new FileReader object.
        const reader = new FileReader();

        // Set the onload event handler for the FileReader object.
        reader.onload = function () {
            // Get the bytes of the thumbnail file.
            const thumbnailBytes = reader.result;

            // Append the thumbnail bytes to the FormData object.
            formData.append('thumbnail', thumbnailBytes);
        }
    }

    axios.post('https://localhost:8443/user/uploadvideo', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        withCredentials: true,
    })
        .then(res => {
            const status = document.querySelector("#uploadError")
            document.querySelector("#uploadTitle").style.color = "rgb(128, 201, 18)"
            status.innerHTML = 'Video was uploaded and is being processed.'
            status.style.color = "rgb(128, 201, 18)"
            status.style.display = "block";

            setTimeout(() => {
                document.querySelector("#uploadTitle").style.color = "black"
                const status = document.querySelector("#uploadError")
                status.style.display = 'none'
                status.style.color = "red"
                document.querySelector("#uploadSub").disabled = false;
            }, 1300)
        })
        .catch(err => {
            console.log(err.response.data)
            const status = document.querySelector("#uploadError")
            document.querySelector("#uploadTitle").style.color = "red"
            status.innerHTML = 'Upload failed please contact support.'
            status.style.color = "red"
            status.style.display = "block";

            setTimeout(() => {
                document.querySelector("#uploadTitle").style.color = "black"
                const status = document.querySelector("#uploadError")
                status.style.display = 'none'
                document.querySelector("#uploadSub").disabled = false;
            }, 1300)
        });
}