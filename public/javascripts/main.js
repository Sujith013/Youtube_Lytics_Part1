document.getElementById('searchForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const form = this;
    const formData = new FormData(form);
    const searchParams = new URLSearchParams();

    // Convert FormData to URLSearchParams
    for (const pair of formData) {
        searchParams.append(pair[0], pair[1]);
    }

    // Perform asynchronous request using Fetch API
    fetch('/search?' + searchParams.toString(), {
        method: 'GET',
    })
    .then(response => response.json())
    .then(data => printOutput(data?.data,data?.senti,searchParams))
    .catch(error => {
        console.error('Error:', error);
    });
});

function printOutput(data,sentiment,searchParams)
{
    const sR = document.getElementById('searchResults');

    var newHead = document.createElement("p");
    var headVal = "<b><u>Search Terms</u>:</b> "+searchParams.toString().split("=")[1].replaceAll("+"," ")+"  "+sentiment+"<br>";
    newHead.innerHTML = headVal;
    newHead.classList.add("list_1");

    for(let i=0;i<data.length;i++)
    {
       var newDiv = document.createElement('div');

       var innerVal = "<p class=\"para\">"+(10-i) + ". <b>Title:</b><a target=\"_blank\" href=\""+data[i][1]+"\">"+data[i][0]+"</a>, <b>Channel:</b><a target=\"_blank\" href=\""+data[i][5]+"\">"+data[i][2].split("@")[1]+"</a>"+", <b>Description:</b>\""+data[i][3]+"\", <a target=\"_blank\" href=\"/tags/"+data[i][1].split("=")[1]+"\">Tags</a></p></br></br><img alt=\"thumbnail\" src=\""+data[i][4]+"\" width=\"75px\" height=\"50px\" class=\"thumbnail\">";

       newDiv.innerHTML = innerVal;
       newDiv.classList.add("list_1");

       if(sR.firstChild)
           sR.insertBefore(newDiv, sR.firstChild); // Insert before the first child
       else
           sR.appendChild(newDiv);
    }

    if(sR.firstChild)
        sR.insertBefore(newHead, sR.firstChild); // Insert before the first child
    else
        sR.appendChild(newHead);
}