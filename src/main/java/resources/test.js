function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.response);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous 
    xmlHttp.send(null);
}

function responseHandler(data){
    var b64Response = btoa(unescape(encodeURIComponent(data)));
    // create an image
    var outputImg = document.createElement('img');
    outputImg.src = 'data:image/png;base64,'+b64Response;

    // append it to your page
    document.body.appendChild(outputImg)
    console.log("DATA B64: \n" + b64Response);
}

console.log("Sending...")
httpGetAsync("http://localho.st:80/insek.png", responseHandler)
