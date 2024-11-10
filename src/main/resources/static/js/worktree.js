
const BASE_URL = new URL(window.location.href).protocol + "//" + new URL(window.location.href).hostname  + ":" + new URL(window.location.href).port + "/";

const userNameLS = "userName";
const userNameTINFO = "userNameTINFO";
const sec_key_mech = 'NWIYRFIYF%@&#$)ABCDEFGHIJKLMNOP';

function dynamicXhrApi(method, url, headers, requestBody, callback, isFileUpload = false) {
    // Create a new XMLHttpRequest object
    const xhr = new XMLHttpRequest();
   xhr.timeout = 45000;

    // Open the request with the provided method and URL
    xhr.open(method, url, true);

    // Set the provided headers, unless it's a file upload (headers are handled differently for FormData)
    if (headers) {
        Object.keys(headers).forEach(key => {
            xhr.setRequestHeader(key, headers[key]);
        });
    }

    xhr.setRequestHeader("X-TOKEN_PACK_ID",getISTTimestamp());

    // Set up an event listener for when the response is received
    xhr.onreadystatechange = function () {
        // Check if the request is complete (readyState 4) and successful (status 200-299)
        if (xhr.readyState === 4) {

            if(xhr.status == 0) {
                return;
            }

            if(xhr.status == 401 && !url.endsWith("/api/login")) {
               removeCurrentUserCache();
               gotohome();
            }



var decryptedResponse;
  try {
  decryptedResponse = JSON.parse(xhr.responseText);
  } catch (error) {
    decryptedResponse    = _0x3c2b1a(xhr.responseText,sec_key_mech);
      if(decryptedResponse!=undefined){
             decryptedResponse=   JSON.parse(decryptedResponse);
                }
  }

//            var decryptedResponse = _0x3c2b1a(xhr.responseText,sec_key_mech);
//
//            if(decryptedResponse!=undefined){
//         decryptedResponse=   JSON.parse(decryptedResponse);
//            }


            if (xhr.status >= 200 && xhr.status < 300) {
                // Call the callback function with the response text
                callback(xhr,decryptedResponse);
            } else {
                // Call the callback with an error if the request was not successful
                callback(xhr,decryptedResponse);
            }
        }
    };

    // Handle network errors
    xhr.onerror = function () {
        stopLoader();
        showErrorMessage('Unreachable', 'Please check your internet connection and try again', true);
    };



    // Prepare and send the request
    if (isFileUpload && requestBody instanceof FormData) {
        // Send FormData directly for file uploads
        xhr.send(requestBody);
    } else if (requestBody) {
        // For JSON requests, send as JSON string
        xhr.send(_0x1a2b3c(JSON.stringify(requestBody), sec_key_mech));
    } else {
        // Send request without body if no data is provided
        xhr.send();
    }
}


function logout(loadercallback,callback){

loadercallback();

var currentToken = getCurrentTokenWithoutRedirect();

if(currentToken!=undefined){
    dynamicXhrApi('POST',BASE_URL + 'api/logout',{ 'Content-Type': 'application/json', 'Accept': 'application/json', 'Authorization':getCurrentToken() },undefined,callback)
}

removeCurrentUserCache();

}

function signoutallaccounts(){

  var storedUsers = JSON.parse(window.localStorage.getItem(userNameTINFO));

for (var i = 0; i < storedUsers.length; i++) {
    var user = storedUsers[i];
 dynamicXhrApi('POST',BASE_URL + 'api/logout',{ 'Content-Type': 'application/json', 'Accept': 'application/json', 'Authorization':user.jwt },undefined,donothing);
}

 window.localStorage.clear();

 window.location.href = "login";
}

function redirectafterlogout(){
window.location.href = "login";
}

function donothing(){
}


function getCurrentTokenWithoutRedirect(){
     var currentUserName = window.localStorage.getItem(userNameLS);



var userNameTINFOJson = window.localStorage.getItem(userNameTINFO);



// Initialize an empty array to hold the data
var userNameList = [];

// Check if there is already data in localStorage
if (userNameTINFOJson !== null) {
    // Parse the existing data if it exists
    userNameList = JSON.parse(userNameTINFOJson);
}

// Check if the userName already exists in the array
var existingUser = userNameList.find(user => user.userName === currentUserName);

    if(existingUser==null || existingUser==undefined)
          return undefined;

     return existingUser.jwt;
}


function getCurrentUserFeatures(){
     var currentUserName = window.localStorage.getItem(userNameLS);



var userNameTINFOJson = window.localStorage.getItem(userNameTINFO);



// Initialize an empty array to hold the data
var userNameList = [];

// Check if there is already data in localStorage
if (userNameTINFOJson !== null) {
    // Parse the existing data if it exists
    userNameList = JSON.parse(userNameTINFOJson);
}

// Check if the userName already exists in the array
var existingUser = userNameList.find(user => user.userName === currentUserName);

    if(existingUser==null || existingUser==undefined)
          return undefined;

     return existingUser.userFeatures;
}


function getCurrentDisplayNameImageWithoutRedirect(){
     var currentUserName = window.localStorage.getItem(userNameLS);



var userNameTINFOJson = window.localStorage.getItem(userNameTINFO);



// Initialize an empty array to hold the data
var userNameList = [];

// Check if there is already data in localStorage
if (userNameTINFOJson !== null) {
    // Parse the existing data if it exists
    userNameList = JSON.parse(userNameTINFOJson);
}

// Check if the userName already exists in the array
var existingUser = userNameList.find(user => user.userName === currentUserName);

    if(existingUser==null || existingUser==undefined)
          return undefined;

     return existingUser.userDisplayName;
}

function getCurrentProfileImageWithoutRedirect(){
     var currentUserName = window.localStorage.getItem(userNameLS);



var userNameTINFOJson = window.localStorage.getItem(userNameTINFO);



// Initialize an empty array to hold the data
var userNameList = [];

// Check if there is already data in localStorage
if (userNameTINFOJson !== null) {
    // Parse the existing data if it exists
    userNameList = JSON.parse(userNameTINFOJson);
}

// Check if the userName already exists in the array
var existingUser = userNameList.find(user => user.userName === currentUserName);

    if(existingUser==null || existingUser==undefined)
          return undefined;

     return existingUser.userProfileImage;
}

function getCurrentToken(){
      var currentUserName = window.localStorage.getItem(userNameLS);



var userNameTINFOJson = window.localStorage.getItem(userNameTINFO);



// Initialize an empty array to hold the data
var userNameList = [];

// Check if there is already data in localStorage
if (userNameTINFOJson !== null) {
    // Parse the existing data if it exists
    userNameList = JSON.parse(userNameTINFOJson);
}

// Check if the userName already exists in the array
var existingUser = userNameList.find(user => user.userName === currentUserName);

    if(existingUser==null || existingUser==undefined)
          window.location.href = "login";

     return existingUser.jwt;

}



    function loginCacheTokenInfo(xhr,decryptedResponseObj){

var response = decryptedResponseObj;
var userNameTINFOJson = window.localStorage.getItem(userNameTINFO);

// Initialize an empty array to hold the data
var userNameList = [];

// Check if there is already data in localStorage
if (userNameTINFOJson !== null) {
    // Parse the existing data if it exists
    userNameList = JSON.parse(userNameTINFOJson);
}

// Check if the userName already exists in the array
var existingUser = userNameList.find(user => user.userName === userNameCache);

if (existingUser) {
    // If user exists, update the JWT for that user
    existingUser.jwt = response.jwt;
} else {
    // If user doesn't exist, add a new entry with jwt and userName
    userNameList.push({ "jwt": response.jwt, "userName": userNameCache ,"userProfileImage" : response.userProfileImage,"userDisplayName":response.userDisplayName,"userFeatures":response.userFeatures});
}

// Update localStorage with the modified array
window.localStorage.setItem(userNameTINFO, JSON.stringify(userNameList));

    }

    function removeCurrentUserCache(){


 // Get the current user name from localStorage
    var currentUserName = window.localStorage.getItem(userNameLS);

    // Get the stored user info JSON from localStorage
    var userNameTINFOJson = window.localStorage.getItem(userNameTINFO);

    // Initialize an empty array to hold the data
    var userNameList = [];

    // Check if there is already data in localStorage
    if (userNameTINFOJson !== null) {
        // Parse the existing data if it exists
        userNameList = JSON.parse(userNameTINFOJson);
    }

    // Filter out the current user from the list
    var filteredList = userNameList.filter(user => user.userName !== currentUserName);

    // If the current user was found and removed, update the localStorage
    if (filteredList.length !== userNameList.length) {
        // Update the localStorage with the new filtered list
        window.localStorage.setItem(userNameTINFO, JSON.stringify(filteredList));
    }


        }


        function setAsCurrentUser(user){
  window.localStorage.setItem(userNameLS, user);
        }


        function manageUserProfiles(loadercallback){
        loadercallback();
          window.location.href =   "manage-profile";
        }


function manageTenants(loadercallback){
loadercallback();
window.location.href="manage-tenents";
}


function gotohome(){
window.location.href="home";
}

function showReports(){
startLoader();
window.location.href="log";
}




//error popup code

    // Get the modal element
    var uniqueModal9875 = document.getElementById("uniqueErrorModal9875");

    // Get the modal content element
    var modalContent9875 = document.getElementById("modalContent9875");

    // Get the Okay button to close the modal
    var uniqueOkayBtn9875 = document.getElementById("uniqueOkayBtn9875");

    // Get the read more element
    var readMore9875 = document.getElementById("readMore9875");
    var errorFooter9875 = document.getElementById("errorFooter9875");

    // When the user clicks on the Okay button, close the modal
    uniqueOkayBtn9875.onclick = function() {
      uniqueModal9875.style.display = "none";
      stopLoader();
    }

    // Toggle message expansion on "Read more" click
    readMore9875.onclick = function() {
      if (errorFooter9875.classList.contains("message-container-expanded9875")) {
        errorFooter9875.classList.remove("message-container-expanded9875");
        readMore9875.innerText = "Read more";
      } else {
        errorFooter9875.classList.add("message-container-expanded9875");
        readMore9875.innerText = "Read less";
      }
    }

    // Function to open the error modal and set header and footer messages, with optional shake
    function showErrorMessage(headerMessage, footerMessage, shouldShake) {
      // Set the header and footer content dynamically
      document.getElementById("errorHeader9875").innerText = headerMessage;
      document.getElementById("errorFooter9875").innerText = footerMessage;

      // Reset message container to collapsed state and reset the Read more link
      errorFooter9875.classList.remove("message-container-expanded9875");
      readMore9875.style.display = "inline-block";  // Show "Read more" link
      readMore9875.innerText = "Read more";

      // If the message is short, hide the "Read more" link
      if (footerMessage.length <= 150) {
        readMore9875.style.display = "none";
      }

      // Display the modal
      uniqueModal9875.style.display = "flex";

      // Check if we should shake the modal
      if (shouldShake) {
        // Add the shake effect
        modalContent9875.classList.add("shake9875");

        // Remove the shake class after the animation ends (0.4s)
        setTimeout(function() {
          modalContent9875.classList.remove("shake9875");
        }, 400);
      }
    }


 document.addEventListener('keydown', function(event) {
    if (event.key === "Escape") {
         uniqueModal9875.style.display = "none";
         stopLoader();
    }
});

function clearCurrentToken() {
removeCurrentUserCache();
 window.location.href = "login";
}


function navigateToHome() {
    startLoader();
    gotohome();
}


function openProfileSettings(){
   startLoader();
 window.location.href = "settings";
}


function loadProfilePhoto(){

 const profilePicBase64 = getCurrentProfileImageWithoutRedirect();
      const profileBtn = document.querySelector('.profile img');
            profileBtn.src = profilePicBase64;
            }

function _0x1a2b3c(_0x4d5e6f, _0x7f8e9d) {
    // Ensure the key length is exactly 32 bytes for AES-256
    if (_0x7f8e9d.length < 32) {
        _0x7f8e9d = _0x7f8e9d.padEnd(32, '0'); // Pad with '0' if less than 32 chars
    } else if (_0x7f8e9d.length > 32) {
        _0x7f8e9d = _0x7f8e9d.slice(0, 32); // Truncate if more than 32 chars
    }

    const _0x9b8c7a = CryptoJS.enc.Utf8.parse(_0x7f8e9d);
    const _0x8d7a6e = CryptoJS.lib.WordArray.random(16);
    const _0x6c5d4b = CryptoJS.AES.encrypt(_0x4d5e6f, _0x9b8c7a, {
        iv: _0x8d7a6e,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });

    return _0x8d7a6e.toString(CryptoJS.enc.Hex) + "::" + _0x6c5d4b.ciphertext.toString(CryptoJS.enc.Hex);
}



//decryption code
function _0x3c2b1a(encryptedData, _0x7f8e9d) {
    // Ensure the key length is exactly 32 bytes for AES-256
    if (_0x7f8e9d.length < 32) {
        _0x7f8e9d = _0x7f8e9d.padEnd(32, '0'); // Pad with '0' if less than 32 chars
    } else if (_0x7f8e9d.length > 32) {
        _0x7f8e9d = _0x7f8e9d.slice(0, 32); // Truncate if more than 32 chars
    }

    const _0x9b8c7a = CryptoJS.enc.Utf8.parse(_0x7f8e9d);

    // Split the encrypted data to retrieve IV and ciphertext
    const [ivHex, ciphertextHex] = encryptedData.split("::");

    const _0x8d7a6e = CryptoJS.enc.Hex.parse(ivHex); // Parse IV from hex
    const _0x6c5d4b = CryptoJS.enc.Hex.parse(ciphertextHex); // Parse ciphertext from hex

    // Decrypt the ciphertext
    const decrypted = CryptoJS.AES.decrypt(
        {
            ciphertext: _0x6c5d4b
        },
        _0x9b8c7a,
        {
            iv: _0x8d7a6e,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        }
    );

    return decrypted.toString(CryptoJS.enc.Utf8);
}



function gotoconfiguration(){
  window.location.href = "configuration";
}
function getISTTimestamp() {
    const currentTime = new Date();

    // Convert to IST by adding 5 hours and 30 minutes (IST is UTC + 5:30)
    const offset = currentTime.getTimezoneOffset() * 60000; // Timezone offset in milliseconds
    const ISTTime = new Date(currentTime.getTime() + offset + (5.5 * 60 * 60 * 1000));

    return ISTTime.getTime(); // Return in milliseconds
}


function gotoemailsettings(){
startLoader();
window.location.href = "email-configuration";
}

function gotoMobilesettings(){
startLoader();
window.location.href = "mobile-configuration";
}


function gotoaisettings(){
startLoader();
window.location.href = "ai-configuration";
}
