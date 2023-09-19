import React from "react";
import "./uploadVideo.css"

import { updateFileName, updateThumbnail, filePick, thumbnailPick, clearForm, videoUpload } from "./uploadVideoUtil";

export default function UploadVideo() {

    return (
        <div id="uploadVideo">
            <form onSubmit={(e) => videoUpload(e)} onReset={(e) => clearForm(e.target)}>
                <h1 id="uploadTitle" className="titlesFont">Upload a video</h1>
                <p className="error" id="uploadError">Hello</p>
                <label>Video Title</label>
                <input required name="title" type="text" placeholder="Enter title" />
                <label>Description</label>
                <textarea required name="description"></textarea>
                <input onChange={(e) => updateFileName(e)} id="fileInput" type="file" name="video" required accept=".mp4 .mov" />
                <label id="fileName"></label>
                <label id="thumbnailName"></label>
                <button id="filePick" type="button" onClick={filePick}>Chose a video</button>
                <input onChange={(e) => updateThumbnail(e)} id="thumbnailInput" type="file" name="thumbnail" required accept=".jpg, .jpeg, .png" />
                <button id="thumbnailPick" type="button" onClick={thumbnailPick}>Chose a thumbnail</button>
                <div id="buttonDiv">
                    <button type="submit">Upload</button>
                    <button type="reset">Cancel</button>
                </div>
            </form>
            <div id="thumbnailContainer">
                <img id="thumbnailPreview" src="" alt="Thumbnail preview" />
            </div>
        </div>
    )
}