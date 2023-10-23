import axios from "axios";

export const delVideo = (videoId, uploadedBy, user, resetSearch) => {
    axios.post("https://localhost:8443/user/delvideo", {
        videoId,
        uploadedBy,
        userName: user.username,
        userRole: user.role

    }, { withCredentials: true }).then(res => {
        resetSearch()
    }).catch(err => alert("Something went wrong please try again"))
}

export const openEditVideo = (video, user) => {
    document.body.style.overflow = 'hidden';
    document.querySelector("#editVideoFormModal").style.display = 'flex';
    document.querySelector("#videoTitle").value = video.title;
    document.querySelector("#videoDesc").value = video.videoDescription;
    document.querySelector("#videoId").value = video.videoId;
    document.querySelector("#uploadedBy").value = video.uploadedBy;

}

export const closeVideoEdit = () => {
    document.body.style.overflow = '';
    document.querySelector("#editVideoFormModal").style.display = 'none';
    document.querySelector("#editVidForm").reset();
}

export const editVideo = (e, user, resetSearch) => {
    e.preventDefault();
    axios.put("https://localhost:8443/user/editvideo", {
        videoId: e.target.videoId.value,
        title: e.target.title.value,
        description: e.target.description.value,
        uploadedBy: e.target.uploadedBy.value,
        username: user.username,
        userRole: user.role
    }, {withCredentials: true}).then(res => resetSearch()).catch(err => {
        let error = document.querySelector("#editVidError");
        error.style.display = "block";
        error.innerHTML = err.response.data
    })
}