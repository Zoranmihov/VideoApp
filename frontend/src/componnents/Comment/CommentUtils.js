import axios from "axios";

export const updateCharCount = (e) => {
    const charCount = document.querySelector("#charCount");
    if (e.target.value.length === 0) {
        charCount.style.display = "none";
        document.querySelector("#subComment").disabled = true;
    } else {
        charCount.style.display = "block";
        document.querySelector("#subComment").disabled = false;
        charCount.textContent = e.target.value.length + " of 2000";
    }
};

export const updateEditCharCount = (e) => {
    const charCount = document.querySelector("#editCharCount");
    if (e.target.value.length === 0) {
        charCount.style.display = "none";
        document.querySelector("#editConfirm").disabled = true;
    } else {
        charCount.style.display = "block";
        document.querySelector("#editConfirm").disabled = false;
        charCount.textContent = e.target.value.length + " of 2000";
    }
};


export const delComment = (comment, user, resetComments) => {
    axios.post("https://localhost:8443/user/delcomment", {
        commentID: comment.commentId,
        commentedBy: comment.commentedBy,
        username: user.username,
        userRole: user.role
    }, { withCredentials: true }).then(res => resetComments()).catch(err => alert("You don't have permission to do that"));

};

export const postComment = async (e, videoId, username, resetComments) => {
    e.preventDefault();

    axios.post("https://localhost:8443/user/leavecomment", {
        videoId: Number(videoId),
        content: e.target.comment.value,
        commentedBy: username
    },
        { withCredentials: true }
    ).then(res => {
        resetComments();
    }).then(err => {
        let error = document.querySelector("#editError");
        error.innerHTML = err.response.data
        error.style.display = "block";
    })
}

export const openEdit = (comment) => {
    document.body.style.overflow = 'hidden';
    document.querySelector("#editModel").style.display = 'flex';
    document.querySelector("#commentIdInput").value = comment.commentId;
    document.querySelector("#commentBy").value = comment.commentedBy;
    document.querySelector("#editComment").value = comment.content;
}

export const closeEdit = () => {
    document.body.style.overflow = '';
    document.querySelector("#editModel").style.display = 'none';
    document.querySelector("#editForm").reset();
}

export const editComment = (e, user, resetComments) => {
    e.preventDefault();

    axios.put("https://localhost:8443/user/editcomment", {
        commentId: e.target.commentId.value,
        newContent: e.target.edit.value,
        username: user.username,
        commentedBy: e.target.commentBy.value,
        role: user.role
    }, { withCredentials: true }).then(res => {
        resetComments();
    }).catch(err => {
        let error = document.querySelector("#editError");
        error.style.display = "block";
        error.innerHTML = err.response.data
    })

}