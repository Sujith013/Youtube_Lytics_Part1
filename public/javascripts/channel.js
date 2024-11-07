function renderChannelProfile() {
    const recentVideosContainer = document.getElementById("recentVideos");

    recentVideosData.forEach((video, index) => {
        const videoDiv = document.createElement("div");
        videoDiv.classList.add("video-item");

        const videoHTML = `
            <div style="border: 1px solid #ccc; padding: 10px; margin: 10px;">
                <p><b>${index + 1}. Title:</b> <a href="${video[1]}" target="_blank">${video[0]}</a></p>
                <p><b>Channel:</b> <a href="${video[2]}" target="_blank">${video[2].split('@')[1]}</a></p>
                <p><b>Description:</b> ${video[3]}</p>
                <img src="${video[4]}" alt="Video Thumbnail" width="120px" height="80px">
                <p><a href="${video[5]}" target="_blank">View Channel</a></p>
            </div>
        `;

        videoDiv.innerHTML = videoHTML;
        videoDiv.classList.add("tagsList");
        recentVideosContainer.appendChild(videoDiv);
    });
}
