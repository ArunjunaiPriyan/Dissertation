
var findCurrentURL = window.location;
var baseUrl = findCurrentURL.protocol + "//" + findCurrentURL.host + "/" + findCurrentURL.pathname.split('/')[1];

function deleteCookie(name) {
	document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function deleteAllCookies() {
	const cookies = document.cookie.split(";");

	for (let i = 0; i < cookies.length; i++) {
		const cookie = cookies[i];
		const eqPos = cookie.indexOf("=");
		const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
		document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
	}
}

function loginpage() {
	deleteCookie('login')
	deleteAllCookies()
}

function setCookie(name, value) {
	var date = new Date();
	date.setTime(date.getTime() + (365 * 24 * 60 * 60 * 1000));
	var expires = "; expires=" + date.toUTCString();
	document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function login() {
    document.getElementById("loader").style.display = "block"
    document.get
	var userName = document.getElementById("usName").value
	var video = document.getElementById("myVideo");
	var pasCode = document.getElementById("pssCd").value
	var userType = document.getElementById("userSelect").value

	var encrypted_User = CryptoJS.AES.encrypt(userType, "magna").toString();
	setCookie('user', encrypted_User);
	var loginData;
	if (userName != null && userName != '' && pasCode != null && pasCode != '') {

		var formData = new FormData();
		formData.append('username', userName)
		formData.append('userType', userType)
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/loginSecure', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {

			if (xhr.readyState == 4) {
				loginData = xhr.responseText
				document.getElementById("loader").style.display = "none"
				if (loginData != "" && loginData != null) {
					var pswd = loginData.split("/");
					if (pswd[0] == pasCode) {
						var encrypted_Pass1 = CryptoJS.AES.encrypt(pswd[1], "magna").toString();
						var encrypted_Pass2 = CryptoJS.AES.encrypt(pswd[2], "magna").toString();
						var encrypted_Pass3 = CryptoJS.AES.encrypt(pswd[3], "magna").toString();
						var enctypted_Pass4 = CryptoJS.AES.encrypt(userName, "magna").toString();
						
						setCookie('login', encrypted_Pass1);
						setCookie('dept', encrypted_Pass2);
						setCookie('empCode', encrypted_Pass3);
						setCookie('userName', enctypted_Pass4);
						
						
						if (userName == pswd[1]) {
							window.location.href = "main.html";
							video.pause();
						}

					} else {
						document.getElementById("loader").style.display = "none"
						Toastify({
							text: "Password is Incorrect",
							duration: 3000,
							newWindow: true,
							close: true,
							gravity: "top",
							position: "center",
							stopOnFocus: true,
							style: {
								background: "black",
							},
						}).showToast();
					}
				} else {
					document.getElementById("loader").style.display = "none"
					Toastify({
						text: "Username is Incorrect",
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "black",
						},
					}).showToast();
				}
			}
		};
	}else{
		document.getElementById("loader").style.display = "none"
		Toastify({
						text: "Field is Mandatory",
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "black",
						},
					}).showToast();
	}
}