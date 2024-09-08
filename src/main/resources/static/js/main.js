var opencount;
var closedcount
var Interval;
var current_page = 1;
var records_per_page = 10;
var current_page_cls = 1;
var current_page_emp = 1;
var records_per_page_cls = 10;
var records_per_page_emp = 10;
var frActionData;
var selectedData;
var userDropDown;
var EmpData;
var EmpCodeResp;
var EmpNameResp;
var tarDate;
var respStatus = 0;
var fileToUpload = "";
var findCurrentURL = window.location;
var baseUrl = findCurrentURL.protocol + "//" + findCurrentURL.host + "/" + findCurrentURL.pathname.split('/')[1];
let empList;

function deleteAllCookies() {
	const cookies = document.cookie.split(";");

	for (let i = 0; i < cookies.length; i++) {
		const cookie = cookies[i];
		const eqPos = cookie.indexOf("=");
		const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
		document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
	}
}

window.addEventListener('storage', function(e) {
	deleteAllCookies()
	logout()
});

function deleteCookie(name) {
	document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
	}
	return null;
}

function prevPage() {

	if (current_page > 1) {
		current_page--;
		changePage(current_page);
	}
}

function nextPage() {
	if (current_page < numPages()) {
		current_page++;
		changePage(current_page);
	}
}

function prevPagecls() {

	if (current_page_cls > 1) {
		current_page_cls--;
		changePagecls(current_page_cls);
	}
}

function nextPagecls() {
	if (current_page_cls < numPagescls()) {
		current_page_cls++;
		changePagecls(current_page_cls);
	}
}

function prevPageemp() {

	if (current_page_emp > 1) {
		current_page_emp--;
		changePageemp(current_page_emp);
	}
}

function nextPageemp() {
	if (current_page_emp < numPagesemp()) {
		current_page_emp++;
		changePageemp(current_page_emp);
	}
}

function changePage(page) {
	var btn_next = document.getElementById("btn_next");
	var btn_prev = document.getElementById("btn_prev");
	var listing_table = document.getElementById("tableDynamic");
	var page_span = document.getElementById("page");

	if (page < 1) page = 1;
	if (page > numPages()) page = numPages();

	[...listing_table.getElementsByTagName('tr')].forEach((tr) => {
		tr.style.display = 'none';
	});
	listing_table.rows[0].style.display = "";

	for (var i = (page - 1) * records_per_page + 1; i < (page * records_per_page) + 1; i++) {
		if (listing_table.rows[i]) {
			listing_table.rows[i].style.display = ""
		} else {
			continue;
		}
	}

	page_span.innerHTML = page + "/" + numPages();

	if (page == 1) {
		btn_prev.disabled = true;
	} else {
		btn_prev.disabled = false;
	}

	if (page == numPages()) {
		btn_next.disabled = true;
	} else {
		btn_next.disabled = false;
	}
}

function changePagecls(page) {
	var btn_next = document.getElementById("btn_nextcls");
	var btn_prev = document.getElementById("btn_prevcls");
	var listing_table = document.getElementById("tableDynamicClosed");
	var page_span = document.getElementById("pagecls");

	if (page < 1) page = 1;
	if (page > numPagescls()) page = numPagescls();

	[...listing_table.getElementsByTagName('tr')].forEach((tr) => {
		tr.style.display = 'none';
	});
	listing_table.rows[0].style.display = "";

	for (var i = (page - 1) * records_per_page_cls + 1; i < (page * records_per_page_cls) + 1; i++) {
		if (listing_table.rows[i]) {
			listing_table.rows[i].style.display = ""
		} else {
			continue;
		}
	}

	page_span.innerHTML = page + "/" + numPagescls();

	if (page == 1) {
		btn_prev.disabled = true;
	} else {
		btn_prev.disabled = false;
	}

	if (page == numPagescls()) {
		btn_next.disabled = true;
	} else {
		btn_next.disabled = false;
	}
}

function changePageemp(page) {
	var btn_next = document.getElementById("btn_nextemp");
	var btn_prev = document.getElementById("btn_prevemp");
	var listing_table = document.getElementById("tableDynamicEmpl");
	var page_span = document.getElementById("pageemp");

	if (page < 1) page = 1;
	if (page > numPagesemp()) page = numPagesemp();

	[...listing_table.getElementsByTagName('tr')].forEach((tr) => {
		tr.style.display = 'none';
	});
	listing_table.rows[0].style.display = "";

	for (var i = (page - 1) * records_per_page_emp + 1; i < (page * records_per_page_emp) + 1; i++) {
		if (listing_table.rows[i]) {
			listing_table.rows[i].style.display = ""
		} else {
			continue;
		}
	}

	page_span.innerHTML = page + "/" + numPagesemp();

	if (page == 1) {
		btn_prev.disabled = true;
	} else {
		btn_prev.disabled = false;
	}

	if (page == numPagesemp()) {
		btn_next.disabled = true;
	} else {
		btn_next.disabled = false;
	}
}


function numPages() {
	var l = document.getElementById("tableDynamic").rows.length
	return Math.ceil((l - 1) / records_per_page);
}

function numPagescls() {
	var l = document.getElementById("tableDynamicClosed").rows.length
	return Math.ceil((l - 1) / records_per_page_cls);
}

function numPagesemp() {
	var l = document.getElementById("tableDynamicEmpl").rows.length
	return Math.ceil((l - 1) / records_per_page_emp);
}


function welcomeadmin() {
	var myVar = CryptoJS.AES.decrypt(getCookie('login'), "magna").toString(CryptoJS.enc.Utf8) || 'Admin';
	Toastify({
		text: `Welcome ${myVar}`,
		duration: 3000,
		newWindow: true,
		close: true,
		gravity: "top",
		position: "center",
		stopOnFocus: true,
		style: {
			background: "#90EE90",
			color: 'black'
		},
	}).showToast();
}
function getSummaryDtl(name) {
	var decryptDept = CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8);
	var decryptLogin = CryptoJS.AES.decrypt(getCookie('login'), "magna").toString(CryptoJS.enc.Utf8)
	var decryptUser = CryptoJS.AES.decrypt(getCookie('user'), "magna").toString(CryptoJS.enc.Utf8)
	var userName = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8)
	
	document.getElementById("passwordChange").style.display = "none"
	document.getElementById("hidesub").style.display = "block"
	document.getElementById("sltName").innerHTML = name;
	document.getElementById('summary_table').style.display = "none";
	document.getElementById("subtable_1").style.display = "block";
	
	if(decryptDept == "D0017"){
		
	if(decryptDept!= name){
		document.getElementById("frmodal").style.display="none"
	}else{
		document.getElementById("frmodal").style.display="block"
	} 
	}

	var formData = new FormData();

	
	if(userName === "Admin"){
		formData.append('dept', "1")
		formData.append('isactive', "GETALL")
		formData.append('userName', userName)
	}else{
		formData.append('dept', name)
	    formData.append('isactive', "1")
		formData.append('userName', userName)
		}
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getfairactiondata', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {

		if (xhr.readyState == 4) {
			frActionData = JSON.parse(xhr.responseText)
			document.getElementById("sltNamedept").innerHTML = frActionData.length > 0 ? frActionData[0].department : "No Data"
			var table = document.getElementById('tableDynamic');
			var tr = document.createElement('tr');
			var thead = document.createElement('thead');
			
			var th = document.createElement('th');
			th.style.display = "none";
	        var text = document.createTextNode("Created Date");
			th.appendChild(text);
			tr.appendChild(th);
			
			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Created Date");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Type");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("section");
			th.appendChild(text);
			tr.appendChild(th);
			
			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("section Creator");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Action Point");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Department");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Responsibility");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Target Date");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Status");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Submit");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Send Back");
			th.appendChild(text);
			tr.appendChild(th);

			thead.appendChild(tr)
			table.appendChild(thead);
			var tbody = document.createElement('tbody');
			document.getElementById("opencount").innerHTML = frActionData.length;
			if (frActionData.length == 0) {
				var dataNA = document.createTextNode("NO DATA");
				var td = document.createElement('td');
				td.colSpan = 10
				dataNA.style = "text-align:center;";
				td.style = "font-weight: bold;"
				td.appendChild(dataNA);
				tbody.appendChild(td);
				table.appendChild(tbody);
			}
			for (var i = 0; i < frActionData.length; i++) {
				var tabData = frActionData[i];
				var tr = document.createElement('tr');
				
				var td = document.createElement("td")
				td.id = "rowind" + i
				td.style.display = "none"
			    var text = document.createTextNode(tabData.id);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				var dtSplit = tabData.date.split("-")
				let setdate = dtSplit[2] + "-" + dtSplit[1] + "-" + dtSplit[0]
				var text = document.createTextNode(setdate);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				td.style = "text-align:center;";
				var text = document.createTextNode(tabData.sec_type);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				td.style = "text-align:center;";
				var text = document.createTextNode(tabData.com_section);
				td.appendChild(text);
				tr.appendChild(td);
				
				var td = document.createElement('td');
				td.style = "text-align:center;";
				var text = document.createTextNode(tabData.sec_owner);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				var text = "";
				const hasPointerMark = tabData.actionPoint.includes("#");
				if (hasPointerMark) {
					let SplitByPointer = tabData.actionPoint.split("#");
					let actionPointer = SplitByPointer.length - 1
					const tableSub = document.createElement('table');
					tableSub.id = "table_" + i
					var btnId = "getEditBtn_" + i
					var canBtn = "getEditBtncancel_" + i
					var addRow = "getAddNewRowBtn_" + i
					var delrow = "getDelRowBtn_" + i
					for (let k = 0; k <= actionPointer; k++) {
						const trSub = document.createElement('tr');
						trSub.id = "subTable"
						if (k != actionPointer) {
							const pointToDevide = SplitByPointer[k];
							let SplitPointer = pointToDevide.split("/");
							var td1 = document.createElement('td');
							td1.style = "width:250px;background-color:transparent"
							var text1 = document.createElement("textarea");
							text1.style = "border:none; width:100%"
							text1.rows = "1"
							text1.disabled = true
							text1.value = SplitPointer[0]
							text1.id = "getEdit"+i
							td1.appendChild(text1);
							trSub.appendChild(td1);
							var td2 = document.createElement('td');
							td2.style = "width:100px;background-color:transparent"
							var text2 = document.createElement("input");
							text2.style = "border:none; width:100%"
							text2.value = SplitPointer[1]
							text2.id = "getEdit1"+i
							text2.disabled = true
							td2.appendChild(text2);
							trSub.appendChild(td2);
							var td3 = document.createElement('td');
							td3.style = "width:100px;background-color:transparent"
							var text3 = document.createElement("input");
							text3.style = "border:none; width:100%"
							text3.value = SplitPointer[2]
							text3.id = "getEdit2"+i
							text3.disabled = true
							td3.appendChild(text3);
							trSub.appendChild(td3);
							var td4 = document.createElement('td');
							td4.style = "width:100px;background-color:transparent"
							var text4 = ""
							if (SplitPointer[3] == "on") {
								text4 = document.createElement('span')
								text4.className = 'material-icons';
								var symbol = document.createTextNode('done');
								text4.appendChild(symbol);
							} else {
								text4 = document.createElement("input");
								text4.type = "checkbox"
								text4.style = "border:none; width:100%"
							}
							text4.disabled = true
							text4.id = "getEdit3"+i
							td4.appendChild(text4);
							trSub.appendChild(td4);
							tableSub.appendChild(trSub);
						} else {
							var tdFinal = document.createElement('td');
							tdFinal.colSpan = 1;
							var btn1 = document.createElement('button');
							btn1.id = btnId
							btn1.innerHTML = "Edit"
							btn1.className = "btn btn-primary"
							btn1.style = "margin-left:95%"
							var tdFinalcancel = document.createElement('td');
							tdFinalcancel.colSpan = 5;
							var btn2 = document.createElement('button');
							btn2.id = canBtn
							var btn3 = document.createElement('button');
							btn3.id = addRow
							var btn4 = document.createElement('button');
							btn4.id = delrow
							
							btn2.style = "display:none";
							btn2.innerHTML = "Cancel"
							btn2.className="btn btn-primary"
							
							btn3.style = "display:none";
							btn3.innerHTML = "Add Row"
							btn3.className = "btn btn-primary"
							
							btn4.style = "display:none";
							btn4.innerHTML = "Del Row"
							btn4.className = "btn btn-primary"
							
							btn4.onclick = function() { onDelRowBtn("table_",this.id) }
							btn3.onclick = function() { onAddRowBtn("table_",this.id,i)  }
							btn2.onclick = function() { onEditSubDataCancel(tableSub.id, "getEditBtn_", this.id,"getAddNewRowBtn_","getDelRowBtn_") };
							btn1.onclick = function() { onEditSubData(tableSub.id, tabData, this.id, "getEditBtncancel_","getAddNewRowBtn_","tableDynamic","rowind","getDelRowBtn_") };
						
							if (decryptLogin != "Ramanathan") {
								tdFinal.style = "border:none;display:none"
								tdFinalcancel.style = "border:none;display:none"
							} else {
								tdFinal.style = "border:none"
								tdFinalcancel.style = "border:none"
							}
							tdFinal.appendChild(btn1);
							trSub.appendChild(tdFinal);
							tdFinalcancel.appendChild(btn2);
							tdFinalcancel.appendChild(btn3);
							tdFinalcancel.appendChild(btn4);
							trSub.appendChild(tdFinalcancel);
							tableSub.appendChild(trSub);
						}
					}
					text = tableSub;
				} else {
					td.style = "width:40%;text-align:left;font-weight:bold";
					text = document.createTextNode(tabData.actionPoint);
				}
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				var text = document.createTextNode(tabData.department);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				if (decryptUser == '2'
					&& decryptDept == name && tabData.resp_status == 0) {

					var text = document.createElement('select');
					text.onchange = function() { setRespnsPerson(this.value, this) };
					text.class = "form-select"
					text.innerHTML = ""
					var options = document.createElement("option");
					respStatus = 0
					EmpCodeResp = ""
					options.text = tabData.responsibility;
					options.style.fontWeight = 'bold'
					text.appendChild(options);
					if (userDropDown != null && userDropDown != undefined) {
						for (var ik = 0; ik < userDropDown.length; ik++) {
							var option = document.createElement("option");
							option.value = userDropDown[ik].emp_code;
							option.text = userDropDown[ik].emp;
							text.appendChild(option);
						}
					}
					td.appendChild(text);
					tr.appendChild(td);
				} else {
					var text = document.createTextNode(tabData.responsibility);
					td.appendChild(text);
					tr.appendChild(td);
				}


				var td = document.createElement('td');
				if (tabData.target_date != null) {
					if (tabData.target_status == 0) {
						var btn = document.createElement('input');
						btn.type = "date";
						btn.className = "form-control datepicker";
						btn.id = tabData.id;
						btn.value = tabData.target_date;
						tarDate = tabData.target_date;
						if (CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8) != name) {
							btn.disabled = true
						}
						if (tabData.target_status == 1) {
							btn.disabled = true
						}
						btn.onchange = function() { dateUpdate(this.value) };
						td.appendChild(btn);
					} else {
						var dtSplitTarget = tabData.target_date.split("-")
						var setdateTar = dtSplitTarget[2] + "-" + dtSplitTarget[1] + "-" + dtSplitTarget[0]
						var text = document.createTextNode(setdateTar);
						td.appendChild(text);
					}
				} else {
					var btn = document.createElement('input');
					btn.type = "date";
					btn.className = "form-control datepicker";
					btn.id = tabData.id;
					if (CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8) != name) {
						btn.disabled = true
					}
					btn.onchange = function(e) { dateUpdate(e, this.value, this.id, 0) };
					td.appendChild(btn);
				}

				tr.appendChild(td);

				var td = document.createElement('td');
				let statusValue;
				if(tabData.isactive == 1 && (tabData.app_status == 1 || tabData.app_status == 2)){
					statusValue = "Pending For Approval"
					td.style = "color:blue;font-weight:bold";
				}else if (tabData.isactive == 1 && tabData.app_status == 0 ) {
					statusValue = "Open"
					td.style = "color:red;font-weight:bold";	
				} else {
					statusValue = "Closed"
					td.style = "color:green;font-weight:bold";
				}
				var text = document.createTextNode(statusValue);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				var btn = document.createElement('button');
				btn.type = "button";
				btn.innerHTML = "Submit"
				btn.className = "btn btn-primary";
				btn.id = tabData.id;
				btn.value = tabData.id;
				if (CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8) != name || tabData.target_status == 1) {
					btn.disabled = true
				}
				btn.onclick = function(e) { getOnSubmit(e, this.value) };
				td.appendChild(btn);
				tr.appendChild(td);

				var td = document.createElement('td');
				var btn = document.createElement('button');
				btn.type = "button";
				btn.innerHTML = "Send Back"
				btn.className = "btn btn-primary";
				btn.id = tabData.id;
				btn.value = tabData.id;
				if (CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8) != name || tabData.target_status == 1) {
					btn.disabled = true
				}
				btn.onclick = function(e) { sendBackPopupwindow(e, this.value) };
				td.appendChild(btn);
				tr.appendChild(td);

				tbody.appendChild(tr);
				table.appendChild(tbody);
				changePage(current_page);
			}
		}
	}
	getSectionList()
	toggleClick(1)
	getempcode(name)
	getDepartmentList();
	getEmpList()
}

function SendBackClose(){
	document.getElementById("sendbackpopup").style = "display:none"
}

function onDelRowBtn(tableSub, curid){

	var idSpl = curid.split("_")
	let tableid = tableSub+idSpl[1];
	const table = document.getElementById(tableid)
	
	var rowCount = table.rows.length;

    if (rowCount > 0) {
      table.deleteRow(rowCount - 2);
    }

    
}

function onAddRowBtn(tableSub,addrow_id,i){
	
	var idSpl = addrow_id.split("_")
	                    let tableid = tableSub+idSpl[1];
					    const table = document.getElementById(tableid)
					    const tablelen = table.rows.length;
					    console.log(tablelen,"length")
	           
						const trSub = document.createElement('tr');
						trSub.id = "subTable"
				        
							var td1 = document.createElement('td');
							td1.style = "width:250px;background-color:transparent"
							var text1 = document.createElement("textarea");
							text1.style = "border:none; width:100%"
							text1.rows = "1"
							text1.id = "getEdit"+i
							td1.appendChild(text1);
							trSub.appendChild(td1);
							
							var td2 = document.createElement('td');
							td2.style = "width:100px;background-color:transparent"
							var text2 = document.createElement("input");
							text2.style = "border:none; width:100%"
							text2.id = "getEdit1"+i
							td2.appendChild(text2);
							trSub.appendChild(td2);
							
							var td3 = document.createElement('td');
							td3.style = "width:100px;background-color:transparent"
							var text3 = document.createElement("input");
							text3.style = "border:none; width:100%"
							text3.disabled = false
							text3.id = "getEdit2" + i;
							td3.appendChild(text3);
							trSub.appendChild(td3);
							
							var td4 = document.createElement('td');
							td4.style = "width:100px;background-color:transparent"
							var text4 = ""
							text4 = document.createElement("input");
							text4.type = "checkbox"
							text4.style = "border:none; width:100%"
							text4.id = "getEdit3"+i
							td4.appendChild(text4);
							trSub.appendChild(td4);
							
							
							var row = table.insertRow(tablelen-1)
							  var cell1 = row.insertCell(0);
                              var cell2 = row.insertCell(1);
                              var cell3 = row.insertCell(2);
                              var cell4 = row.insertCell(3);
  cell1.appendChild(td1);
  cell2.appendChild(td2);
  cell3.appendChild(td3);
  cell4.appendChild(td4);
 
						
}


function getEmpList() {
	
    var respn = document.getElementById("Responsibility");
	respn.innerHTML = ""

     var formData =  new FormData();
     formData.append('empType',"2")
	let shr = new XMLHttpRequest();
	shr.open('POST', baseUrl + '/getEmployeeDetails', true);
	shr.send(formData)

	shr.onreadystatechange = function() {

		if (shr.readyState == 4) {
			empList = JSON.parse(shr.responseText)
			var options = document.createElement("option");
			options.text = "<--Select-->";
			respn.appendChild(options);
			for (var i = 0; i < empList.length; i++) {
				var option = document.createElement("option");
				option.value = empList[i].emp_code;
				option.text = empList[i].emp;
				respn.appendChild(option);
			}
			
		}
	}
}

function departmentonchange(e) {
	var respn = document.getElementById("Responsibility");
	respn.innerHTML = ""
	respn.readOnly = true
	var searText = e.target.value.toString();
	var filteredData = empList.filter((item) => {
		 if(item.dept === searText){
			return item.emp;
		 }
	})
			for(var i=0; i<filteredData.length; i++){
				if(filteredData.length > 0){
				var option = document.createElement("option");
				option.value = filteredData[i].emp_code;
				option.text = filteredData[i].emp;
				respn.appendChild(option);
			}
			}			
}

function getDepartmentList() {
   
   if(CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8) === "D0017"){
	   document.getElementById("deptforadmin").style.display = "block"
	   document.getElementById("resforadmin").style.display = "block"
	   document.getElementById("resforuser").style.display = "none"
	  
	var respn = document.getElementById("dept");
	respn.innerHTML = ""
	var formDataresp = new FormData();
	formDataresp.append('dept', "GETALL")
	let shr = new XMLHttpRequest();
	shr.open('POST', baseUrl + '/getDepartmentList', true);
	shr.send(formDataresp)

	shr.onreadystatechange = function() {

		if (shr.readyState == 4) {
			var deptList;
			deptList = JSON.parse(shr.responseText)
			var options = document.createElement("option");
			options.text = "<--Select-->";
			respn.appendChild(options);
			for (var i = 0; i < deptList.length; i++) {
				if(i > 0){
					if (deptList[i].dept !== deptList[i-1].dept) {
				var option = document.createElement("option");
				option.value = deptList[i].dept;
				option.text = deptList[i].is_active;
				respn.appendChild(option);
				}else{
					break;
				}
				}else{
					var option = document.createElement("option");
				option.value = deptList[i].dept;
				option.text = deptList[i].is_active;
				respn.appendChild(option);
				}
				
			}
		}
	}
	
	
	
   }
   	
}


function getSectionList() {

	var dept = document.getElementById("sltName").innerHTML
	var respn = document.getElementById("Section");
	respn.innerHTML = ""
	let ehr = new XMLHttpRequest();
	ehr.open('POST', baseUrl + '/getSectionList', true);
	ehr.send()

	ehr.onreadystatechange = function() {

		if (ehr.readyState == 4) {
			var deptList;
			deptList = JSON.parse(ehr.responseText)
			var options = document.createElement("option");
			options.text = "<--Select-->";
			respn.appendChild(options);
			for (var i = 0; i < deptList.length; i++) {
				if (deptList[i].creator == CryptoJS.AES.decrypt(getCookie('login'), "magna").toString(CryptoJS.enc.Utf8)) {
					var option = document.createElement("option");
					option.value = deptList[i].com_Code;
					option.text = deptList[i].com_Name;
					option.id = deptList[i].creator;
					respn.appendChild(option);
				}

			}
		}
	}
}


function getempcode(name) {
	var respn = document.getElementById("Respondibility");
	respn.innerHTML = ""
	var formDataresp = new FormData();
	formDataresp.append('dept', name)
	let shr = new XMLHttpRequest();
	shr.open('POST', baseUrl + '/getDepartmentList', true);
	shr.send(formDataresp)

	shr.onreadystatechange = function() {

		if (shr.readyState == 4) {
			var deptList;
			deptList = JSON.parse(shr.responseText)
			var options = document.createElement("option");
			if(deptList.length > 0){
			var filteredData = deptList.filter((item) => {
		       if(item.emp_type === 2){
			       return item;
		       }
	         })
			}
	//		options.text = "<--Select-->";
	//		respn.appendChild(options);
			for (var i = 0; i < filteredData.length; i++) {
				//if (deptList[i].emp_type == 2) {
				var option = document.createElement("option");
				option.value = filteredData[i].emp_code;
				option.text = filteredData[i].emp;
				respn.appendChild(option);
				//}
			}
		}
	}
}

function logout() {
	window.location.href = "index.html";
	deleteCookie('login')

}

function backtomain() {
	var lenDyn = 1
	var lenCls = 1
	const tabled = document.getElementById("tableDynamic");
	if (tabled.tBodies.length != 0) {
		const trs = tabled.tBodies[0].getElementsByTagName("tr");
		var lenDyn = Math.ceil(trs.length)
	}

	const tabledcls = document.getElementById("tableDynamicClosed");
	if (tabledcls.tBodies.length != 0) {
		const trscls = tabledcls.tBodies[0].getElementsByTagName("tr");
		var lenCls = Math.ceil(trscls.length)
	}

	mainTableContent()
	ShowPicBtn()
	for (let i = 0; i < lenDyn; i++) {
		document.getElementById('btn_prev').onclick();
	}
	for (let i = 0; i < lenCls; i++) {
		document.getElementById('btn_prevcls').onclick();
	}
	document.getElementById("formFile").value = ""
	document.getElementById("updateEmp").style.display = "none"
	//document.getElementById("showmenuMain6").style.display = "none"
	document.getElementById("EmpList").style.display = "none"
	document.getElementById('returnList').style.display = "none"
	document.getElementById("searchBox").value = "";
	document.getElementById("searchBoxClosed").value = "";
	document.getElementById("passwordChange").style.display = "none"
	//	document.getElementById("showmenuMain4").style.display = "none"
	//	document.getElementById("showmenuMain5").style.display = "none"
	document.getElementById('summary_table').style.display = "block";
	document.getElementById("subtable_1").style.display = "none";
	document.getElementById("tableDynamic").innerHTML = "";
	document.getElementById('tableDynamicClosed').innerHTML = "";
	document.getElementById('checkBox').checked = false;
	//	document.getElementById('btn_prev').onclick();
	//	document.getElementById('btn_prevcls').onclick();
	//	document.getElementById("showmenuMain1").style.display = "none"
	//	document.getElementById("showmenuMain2").style.display = "none"
	//	document.getElementById("showmenuMain3").style.display = "none"
	//	document.getElementById("openMenuMain").style.display = "block"
	//	document.getElementById("closeMenuMain").style.display = "none"
	document.getElementById('appList').style.display = "none"
}


function updatefairness() {
	document.getElementById("subData").innerHTML = ""
	var subData = document.getElementById("subData")
	for (let i = 0; i < 15; i++) {
		const trSub = document.createElement('tr');
		trSub.id = "subTable"
		var td1 = document.createElement('td');
		var text1 = document.createElement('input');
		text1.class = "form-control"
		text1.style = "width:150px"
		td1.appendChild(text1);
		trSub.appendChild(td1);
		var td2 = document.createElement('td');
		var text2 = document.createElement('input');
		text2.class = "form-control"
		text2.style = "width:150px"
		td2.appendChild(text2);
		trSub.appendChild(td2);
		var td3 = document.createElement('td');
		var text3 = document.createElement('input');
		text3.class = "form-control"
		text3.style = "width:150px"
		td3.appendChild(text3);
		trSub.appendChild(td3);
		subData.appendChild(trSub);
	}
	var dept = document.getElementById("sltNamedept").innerHTML;
	document.getElementById("dpt").innerHTML = dept;
}

function insertActionpoint() {
	var oTable = document.getElementById('subData');
	var rowLength = oTable.rows.length;
	var subDataValue = ""
	for (i = 0; i < rowLength; i++) {
		var oCells = oTable.rows.item(i).cells;
		var cellLength = oCells.length;
		for (var j = 0; j < cellLength; j++) {
			var cellVal = oCells[j].getElementsByTagName('input')[0].value;
			if (cellVal != undefined && cellVal != "") {
				if (j == cellLength - 1) {
					subDataValue = subDataValue += cellVal + "#";
				} else {
					subDataValue = subDataValue += cellVal + "/";
				}
			} else {
				break;
			}
		}
	}
	let actionPt;
	const actionTog = document.getElementById("checkBoxAction").checked
	if (actionTog) {
		actionPt = subDataValue;
	} else {
		actionPt = document.getElementById('Action').value
	}
	var tgDate = document.getElementById('target').value

	var respn = document.getElementById('Respondibility')
	var department = document.getElementById("sltName").innerHTML
	var section = document.getElementById("Section").value
	var pointtype = document.getElementById("Type").value
	
	var respon = document.getElementById("Responsibility").value;
	var dept = document.getElementById("dept").value;
	
    var DeptCode = getCookie('dept');
	var Decdeptcode  = CryptoJS.AES.decrypt(DeptCode,"magna").toString(CryptoJS.enc.Utf8)
    var ischecked = false;
    
	if(Decdeptcode == "D0017"){
		if(dept != '<--Select-->'){
			ischecked = true;
		}else{
			ischecked = false;
		}
	}else{
	  ischecked = true
	}
	var myblob = new Blob([fileToUpload], {
		type: 'text/plain'
	});
	if (actionPt != '' && respn.value != '' & section != '<--Select-->' && tgDate != '' && ischecked != false) {
		var formData = new FormData();

		if (fileToUpload == "") {
			formData.append('file', myblob)
			formData.append('targetDate', tgDate)
			formData.append('actionPoint', actionPt)
			formData.append('section', section)
			formData.append('pointtype', pointtype)
			if(Decdeptcode == "D0017"){
			formData.append('depart', dept)
			formData.append('user', dept)
			formData.append('responsibility', respon)
			formData.append('empCode',respon)
			}else{
			formData.append('depart', department)
			formData.append('user', department)
			formData.append('responsibility', respn.value)
			formData.append('empCode',respn.value)
			}
			
			formData.append('file_status', '0')
		} else {
			formData.append('file', fileToUpload)
			formData.append('targetDate', tgDate)
			formData.append('actionPoint', actionPt)
			formData.append('section', section)
			formData.append('pointtype', pointtype)
			if(Decdeptcode == "D0017"){
			formData.append('depart', dept)
			formData.append('user', dept)
			formData.append('responsibility', respon)
			formData.append('empCode',empcod)
			}else{
			formData.append('depart', department)
			formData.append('user', department)
			formData.append('responsibility', respn.value)
			formData.append('empCode',respn.value)
			}
			formData.append('file_status', '1')
		}
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/updateFairnessAction', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {
			var respdata;
			if (xhr.readyState == 4) {
				respdata = xhr.responseText;
				if (respdata == "Success") {
					Toastify({
						text: 'Action Point Added',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "4BB543",
							color: 'black',
						},
					}).showToast();
					
					document.getElementById("tableDynamic").innerHTML = "";
					//getSummaryDtl(department)
					var resp = document.getElementById("Respondibility")
					document.getElementById("formFile").value = ""
					document.getElementById("closeModal2").click();
					document.getElementById('target').value = '';
					document.getElementById('Action').value = '';
					//document.getElementById('Respondibility').value = '<--Select-->'
					
					var htmlMsgBody = `<!DOCTYPE html>
									<html>
									<head>
										<style>
											h5{
									            margin-top:10px;
									            margin-bottom:2px;
									        }
									        p{
									            margin-top:5px
									        }
									        img{
									            width:15%
									        }
											#customers {
												font-family: Arial, Helvetica, sans-serif;
												border-collapse: collapse;
												width: 100%;
											}
											#customers td,
											#customers th {
												border: 1px solid #ddd;
												padding: 8px;
											}
											#customers tr:nth-child(even) {
												background-color: #f2f2f2;
											}
											#customers tr:hover {
												background-color: #ddd;
											}
											#customers th {
												padding-top: 12px;
												padding-bottom: 12px;
												text-align: left;
												background-color: #5A5A5A;
												color: white;
												text-align: center;
											}
											#customers td{
												text-align: center;
											}
										</style>
									</head>
									<body>
										<div>
											<table id="customers">
												<tr>
													<th colspan=4>
														New Action Point Added</th>
												</tr>
												<tr>
													<td>Target Date</td>
													<td>${tgDate}</td>
												</tr>
												<tr>
													<td>Action Point</td>
													<td>${actionPt}</td>
												</tr>
												<tr>
													<td>Link</td>
													<td><a href="http://10.215.8.94:8789/magna/main.html">Magna Project Dashboard</a></td>
												</tr>
											</table>
										</div>
										<div>
											<br>
											<p>Regards,</p>
											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>
											<p>
												Plot No:7B, 8th Avenue, 1st Cross Road,<br>
												Mahindra World City,<br>
												Chengalpattu District - 603004.<br>
												Tamil Nadu, India<br>
											</p>
											<img src="./img/Magna-Logo.png" alt="Magna-Logo">
										</div>
									</body>
									</html>`
					var formData1 = {
						"recipient": resp.value,
						"msgBody": htmlMsgBody,
						"subject": "New Action Point Added"
					};
					getSummaryDtl(department)
					let zhr = new XMLHttpRequest();
					zhr.open('POST', baseUrl + '/sendMailaction', true);
					zhr.setRequestHeader("Content-Type", "application/json");
					zhr.send(JSON.stringify(formData1))
					zhr.onreadystatechange = function() {
						if (zhr.readyState == 4) {
							var respdata = zhr.responseText;
							Toastify({
								text: "Mail Sent Successfully...",
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
				} else {
					Toastify({
						text: 'Action Point Update Error',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "red",
							color: 'white',
						},
					}).showToast();
				}
			}

		}
	} else {
		Toastify({
			text: 'Please fill all fields properly',
			duration: 3000,
			newWindow: true,
			close: true,
			gravity: "top",
			position: "center",
			stopOnFocus: true,
			style: {
				background: "red",
			},
		}).showToast();
	}
	document.getElementById("formFile").value = ""
}


function toggleClick(renderclosed) {
	var text = document.getElementById("toggleswitch").innerHTML;
	if (text == "Open") {
		var name = document.getElementById("sltName").innerHTML;
		if (renderclosed == undefined) {
			document.getElementById("toggleswitch").innerHTML = "Closed";
			document.getElementById("divIdClosed").style.display = "block";
			document.getElementById("divIdOpen").style.display = "none";
			document.getElementById("frmodal").style.display = "none";
			var frActionData;
			document.getElementById('summary_table').style.display = "none";
			document.getElementById("subtable_1").style.display = "block";
			var respn = document.getElementById("Respondibility");
			respn.innerHTML = ""
		}
		var userName = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8)
		var formData = new FormData();
		
		if(userName === "Admin"){
		  formData.append('dept', "1")
		  formData.append('isactive', "GETALL")
		  formData.append('userName', userName)
		}else{
		  formData.append('dept', name)
		  formData.append('isactive', "0")
		  formData.append('userName', userName)
		}
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/getfairactiondata', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {

			if (xhr.readyState == 4) {
				frActionData = JSON.parse(xhr.responseText)
				var tableClosed = document.getElementById('tableDynamicClosed');
				tableClosed.innerHTML = "";
				var tr = document.createElement('tr');
				var thead = document.createElement('thead');

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Created Date");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Type");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Section");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Action Point");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Department");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Responsibility");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Target Date");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Closed Date");
				th.appendChild(text);
				tr.appendChild(th);

				var th = document.createElement('th');
				th.style = "background-color:#596744";
				var text = document.createTextNode("Status");
				th.appendChild(text);
				tr.appendChild(th);
				thead.appendChild(tr)
				tableClosed.appendChild(thead)
				document.getElementById("closedcount").innerHTML = frActionData.length;
				var tbody = document.createElement('tbody');
				if (frActionData.length == 0) {
					var dataNA = document.createTextNode("NO DATA");
					var td = document.createElement('td');
					td.colSpan = 9
					dataNA.style = "text-align:center;";
					td.style = "font-weight: bold;"
					td.appendChild(dataNA);
					tbody.appendChild(td);
					tableClosed.appendChild(tbody);
				}
				for (var i = 0; i < frActionData.length; i++) {
					var tabData = frActionData[i];
					var tr = document.createElement('tr');

					var td = document.createElement('td');
					var dtSplit = tabData.date.split("-")
					let setdate = dtSplit[2] + "-" + dtSplit[1] + "-" + dtSplit[0]
					var text = document.createTextNode(setdate);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					td.style = "text-align:center;";
					var text = document.createTextNode(tabData.sec_type);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					td.style = "text-align:center;";
					var text = document.createTextNode(tabData.com_section);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					td.style = "width:40%;text-align:left;font-weight:bold";
					var text = document.createTextNode(tabData.actionPoint);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					var text = document.createTextNode(tabData.department);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					var text = document.createTextNode(tabData.responsibility);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					if (tabData.target_date != null) {
						var dtSplitTarget = tabData.target_date.split("-")
						var setdateTar = dtSplitTarget[2] + "-" + dtSplitTarget[1] + "-" + dtSplitTarget[0]
						var text = document.createTextNode(setdateTar);
						td.appendChild(text);
					} else {
						var text = document.createTextNode("-");
						td.appendChild(text);
					}
					tr.appendChild(td);

					var td = document.createElement('td');
					if (tabData.closed_date != null) {
						var dtSplitTarget = tabData.closed_date.split("-")
						var setdateTar = dtSplitTarget[2] + "-" + dtSplitTarget[1] + "-" + dtSplitTarget[0]
						var text = document.createTextNode(setdateTar);
						if (tabData.target_date != null) {
							const target = new Date(tabData.target_date);
							const close = new Date(tabData.closed_date);
							if (target < close) {
								td.style = "color:red;font-weight:bold";
							}
						}
						td.appendChild(text);
					} else {
						var text = document.createTextNode("-");
						td.appendChild(text);
					}
					tr.appendChild(td);

					var td = document.createElement('td');
					let statusValue;
					if (tabData.isactive == 1) {
						statusValue = "Open"
						td.style = "color:red;font-weight:bold";	
					} else {
						statusValue = "Closed"
						td.style = "color:green;font-weight:bold";
					}
					var text = document.createTextNode(statusValue);
					td.appendChild(text);
					tr.appendChild(td);
					tbody.appendChild(tr);
					tableClosed.appendChild(tbody);
					changePagecls(current_page_cls);
				}
			}
			Interval = setInterval(function() { updateTotalCount(), 1000 });

		}
	} else {
		document.getElementById("toggleswitch").innerHTML = "Open";
		document.getElementById("divIdClosed").style.display = "none"
		document.getElementById("divIdOpen").style.display = "block"
		document.getElementById("frmodal").style.display = "block";
		document.getElementById('btn_prevcls').onclick();
	}

}

function dateUpdate(value) {
	tarDate = value;
}

function mainTableContent() {
	let encryptUser = CryptoJS.AES.decrypt(getCookie('user'), "magna").toString(CryptoJS.enc.Utf8);
	let encryptUsername = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8);
	if (document.cookie.split(';').length < 3) {
		logout()
	}

	if(encryptUser == "0") {
		document.getElementById("showmenuMain1").style.display = "block"
		document.getElementById("showmenuMain3").style.display = "block"
		document.getElementById("showmenuMain6").style.display = "block"
		document.getElementById("showmenuMain2").style.display = "none"
	} else {
		document.getElementById("showmenuMain1").style.display = "none"
		document.getElementById("showmenuMain3").style.display = "none"
	}
	if (encryptUser == "1") {
		document.getElementById("showmenuMain2").style.display = "none"
	} else if (encryptUser != "0") {
		document.getElementById("showmenuMain2").style.display = "block"
	}
	if (encryptUser == '2') {
		document.getElementById("showmenuMain5").style.display = "block"
	}
	if(encryptUsername === "Admin" && encryptUser == '2'){
		document.getElementById("showmenuMain2").style.display = "none"
		document.getElementById("showmenuMain5").style.display = "none"
	}

    var logoutbtn = document.getElementById("logout")
    logoutbtn.title = encryptUsername;
    
	document.getElementById("showmenuMain4").style.display = "block"
	

	//	document.getElementById("openMenuMain").style.display = "none"
	//	document.getElementById("closeMenuMain").style.display = "block"
	if (getCookie('login') != undefined && getCookie('login') != null && getCookie('login') != "" && document.cookie.split(';').length != 0) {
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/getOverallDetails', true);
		xhr.send()

		xhr.onreadystatechange = function() {
			var respdata;
			if (xhr.readyState == 4) {
				respdata = JSON.parse(xhr.responseText);
				jsdyntable(respdata)
			}

		}

		var respn = document.getElementById("secRespondibility");
		respn.innerHTML = ""
		var formDataresp = new FormData();
		formDataresp.append('dept', 'GETALL')
		let shr = new XMLHttpRequest();
		shr.open('POST', baseUrl + '/getDepartmentList', true);
		shr.send(formDataresp)

		shr.onreadystatechange = function() {

			if (shr.readyState == 4) {
				var deptList;
				deptList = JSON.parse(shr.responseText)
				var options = document.createElement("option");
				options.text = "<--Select-->";
				respn.appendChild(options);
				for (var i = 0; i < deptList.length; i++) {
					if (deptList[i].emp_type == 2) {
						var option = document.createElement("option");
						option.value = deptList[i].emp_code;
						option.text = deptList[i].emp;
						respn.appendChild(option);
					}
				}
			}
		}

		if (encryptUser == '2') {
			var formDataresp = new FormData();
			formDataresp.append('dept', CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8))
			let shr = new XMLHttpRequest();
			shr.open('POST', baseUrl + '/getDepartmentList', true);
			shr.send(formDataresp)

			shr.onreadystatechange = function() {

				if (shr.readyState == 4) {
					userDropDown = JSON.parse(shr.responseText)
				}
			}
		}
	} else {
		window.location.href = "index.html";
	}
}

function updateTotalCount() {
	var clsCount = document.getElementById("closedcount").innerHTML
	var opnCount = document.getElementById("opencount").innerHTML
	let totalcount = parseInt(opnCount) + parseInt(clsCount);
	document.getElementById("totalcount").innerHTML = totalcount;
	clearInterval(Interval);
}


//function showMenuMain() {
//	if (document.getElementById("showmenuMain4").style.display == "none") {
//		if (localStorage['user'] == "0") {
//			document.getElementById("showmenuMain1").style.display = "block"
//			document.getElementById("showmenuMain3").style.display = "block"
//			document.getElementById("showmenuMain6").style.display = "block"
//		} else {
//			document.getElementById("showmenuMain1").style.display = "none"
//			document.getElementById("showmenuMain3").style.display = "none"
//		}
//		if (localStorage['user'] == "1") {
//			document.getElementById("showmenuMain2").style.display = "none"
//		} else {
//			document.getElementById("showmenuMain2").style.display = "block"
//		}
//		if (localStorage['user'] == '2') {
//			document.getElementById("showmenuMain5").style.display = "block"
//		}
//
//		document.getElementById("showmenuMain4").style.display = "block"
//
//		document.getElementById("openMenuMain").style.display = "none"
//		document.getElementById("closeMenuMain").style.display = "block"
//	} else {
//		document.getElementById("showmenuMain1").style.display = "none"
//		document.getElementById("showmenuMain2").style.display = "none"
//		document.getElementById("showmenuMain4").style.display = "none"
//		document.getElementById("openMenuMain").style.display = "block"
//		document.getElementById("showmenuMain3").style.display = "none"
//		document.getElementById("closeMenuMain").style.display = "none"
//		document.getElementById("showmenuMain5").style.display = "none"
//		document.getElementById("showmenuMain6").style.display = "none"
//	}
//}

function password_show_hide(value) {
	var x = document.getElementById("password" + value);
	var show_eye = document.getElementById("show_eye_" + value);
	var hide_eye = document.getElementById("hide_eye_" + value);
	hide_eye.classList.remove("d-none");
	if (x.type === "password") {
		x.type = "text";
		show_eye.style.display = "none";
		hide_eye.style.display = "block";
	} else {
		x.type = "password";
		show_eye.style.display = "block";
		hide_eye.style.display = "none";
	}
}

function showCngPwd() {
	document.getElementById("passwordChange").style.display = "block"
	document.getElementById("summary_table").style.display = "none"
}

function performSearch() {
	/* const searchBox = document.getElementById('searchBox');
	const tabled = document.getElementById("tableDynamic");
	const trs = tabled.tBodies[0].getElementsByTagName("tr");
	var filter = searchBox.value.toUpperCase();
	var name = document.getElementById("sltName").innerHTML;

	if (filter != "") {
		for (var rowI = 0; rowI < trs.length; rowI++) {

			var tds = trs[rowI].getElementsByTagName("td");

			trs[rowI].style.display = "none";

			for (var cellI = 0; cellI < tds.length; cellI++) {

				if (tds[cellI].innerHTML.toUpperCase().indexOf(filter) > -1) {

					trs[rowI].style.display = "";

					continue;

				}
			}
		}
	} else {
		//getSummaryDtl(name)
		
	} */
	
$(document).ready(function () {
  $("#searchBox").keyup(function () {
    search_table($(this).val());
  });
  function search_table(value) {
    $("#tableDynamic tr").each(function () {
      var found = "false";
      $(this).each(function () {
        if ($(this).text().toLowerCase().indexOf(value.toLowerCase()) >= 0) {
          found = "true";
        }
      });
      if (found == "true") {
        $(this).show();
      } else {
		 
        $(this).hide();
      
      }
    });
  }
});
}


function performSearchCls() {
	const searchBox = document.getElementById('searchBoxClosed');
	const tabled = document.getElementById("tableDynamicClosed");
	const trs = tabled.tBodies[0].getElementsByTagName("tr");
	var filter = searchBox.value.toUpperCase();

	if (filter != "") {
		for (var rowI = 0; rowI < trs.length; rowI++) {

			var tds = trs[rowI].getElementsByTagName("td");

			trs[rowI].style.display = "none";

			for (var cellI = 0; cellI < tds.length; cellI++) {

				if (tds[cellI].innerHTML.toUpperCase().indexOf(filter) > -1) {

					trs[rowI].style.display = "";

					continue;

				}
			}
		}
	} else {
		document.getElementById("toggleswitch").innerHTML = "Open"
		toggleClick()
	}
}

function jsdyntable(data) {
	
	var section = Object.keys(data.section).sort(function(a, b) { return data.section[a] - data.section[b] })
	section.push("Total")
	var datasetOpen = [];
	var datasetClosed = [];
	var overAllOpen = [];
	var overAllClosed = [];
	var putZero = "-"
	var d_code = ['D0001', "D0002", "D0003", "D0004", "D0005", "D0006", "D0007", "D0008", "D0009", "D0010", "D0011", "D0012", "D0013", "D0014", "D0015", "D0016", "D0017"]
    var userName = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8)
    
	const openObj = Object.keys(data)
		.filter((key) => key.includes("open_"))
		.reduce((obj, key) => {
			return Object.assign(obj, {
				[key]: data[key]
			});
		}, {});

	const closeObj = Object.keys(data)
		.filter((key) => key.includes("close_"))
		.reduce((obj, key) => {
			return Object.assign(obj, {
				[key]: data[key]
			});
		}, {});

	function preferredOrder(obj, order) {
		var newObject = [];
		for (var i = 0; i < order.length; i++) {
			if (obj.hasOwnProperty(order[i])) {
				newObject.push(order[i])
			}
		}
		return newObject;
	}
	const keys = preferredOrder(openObj, ["open_1", "open_2", "open_3", "open_4", "open_5", "open_6", "open_7", "open_8", "open_9",
		"open_10", "open_11", "open_12", "open_13", "open_14", "open_15", "open_16","open_17"]);
	const resOpen = [];
	for (let i = 0; i < keys.length; i++) {
		resOpen.push(openObj[keys[i]]);
	};
	const keyscls = preferredOrder(closeObj, ["close_1", "close_2", "close_3", "close_4", "close_5", "close_6", "close_7", "close_8", "close_9",
		"close_10", "close_11", "close_12", "close_13", "close_14", "close_15", "close_16",,"close_17"]);
	var resclose = []
	for (let i = 0; i < keyscls.length; i++) {
		resclose.push(closeObj[keyscls[i]]);
	};

	for (let ka = 0; ka < d_code.length; ka++) {
		var arr = resOpen[ka]

		for (let kl = 0; kl < section.length - 1; kl++) {
			var sen = Object.fromEntries(Object.entries(arr).filter(([key]) => key.includes(section[kl])));
			var val = Object.values(sen);
			if (Object.getOwnPropertyNames(sen).length != 0) {

				datasetOpen.push(val[0]);
			} else {
				datasetOpen.push("-");
			}
		}
		var arr1 = resclose[ka]
		for (let ks = 0; ks < section.length - 1; ks++) {
			var sen = Object.fromEntries(Object.entries(arr1).filter(([key]) => key.includes(section[ks])));
			var val = Object.values(sen);
			if (Object.getOwnPropertyNames(sen).length != 0) {

				datasetClosed.push(val[0]);
			} else {
				datasetClosed.push("-");
			}
		}
	}

	var arr2 = data.overallOpen

	for (let ks = 0; ks < section.length - 1; ks++) {
		var sen = Object.fromEntries(Object.entries(arr2).filter(([key]) => key.includes(section[ks])));
		var val = Object.values(sen);
		if (Object.getOwnPropertyNames(sen).length != 0) {

			datasetOpen.push(val[0]);
		} else {
			datasetOpen.push("-");
		}
	}

	var arr3 = data.overallClosed

	for (let ks = 0; ks < section.length - 1; ks++) {
		var sen = Object.fromEntries(Object.entries(arr3).filter(([key]) => key.includes(section[ks])));
		var val = Object.values(sen);
		if (Object.getOwnPropertyNames(sen).length != 0) {

			datasetClosed.push(val[0]);
		} else {
			datasetClosed.push("-");
		}
	}
	if (data.overDeptOpen != null && data.overDeptOpen != "" && data.overDeptOpen != undefined) {
		var dataOpn = data.overDeptOpen;
		if (dataOpn.Maintenance != undefined) { overAllOpen.push(dataOpn.Maintenance); } else { overAllOpen.push(putZero); }
		if (dataOpn.ME != undefined) { overAllOpen.push(dataOpn.ME); } else { overAllOpen.push(putZero); }
		if (dataOpn.Production != undefined) { overAllOpen.push(dataOpn.Production); } else { overAllOpen.push(putZero); }
		if (dataOpn.Quality != undefined) { overAllOpen.push(dataOpn.Quality); } else { overAllOpen.push(putZero); }
		if (dataOpn.Logistic_FG != undefined) { overAllOpen.push(dataOpn.Logistic_FG); } else { overAllOpen.push(putZero); }
		if (dataOpn.Logistic_RM != undefined) { overAllOpen.push(dataOpn.Logistic_RM); } else { overAllOpen.push(putZero); }
		if (dataOpn.HR != undefined) { overAllOpen.push(dataOpn.HR); } else { overAllOpen.push(putZero); }
		if (dataOpn.Safety != undefined) { overAllOpen.push(dataOpn.Safety); } else { overAllOpen.push(putZero); }
		if (dataOpn.IT != undefined) { overAllOpen.push(dataOpn.IT); } else { overAllOpen.push(putZero); }
		if (dataOpn.Engineering != undefined) { overAllOpen.push(dataOpn.Engineering); } else { overAllOpen.push(putZero); }
		if (dataOpn.Finance != undefined) { overAllOpen.push(dataOpn.Finance); } else { overAllOpen.push(putZero); }
		if (dataOpn.Purchase != undefined) { overAllOpen.push(dataOpn.Purchase); } else { overAllOpen.push(putZero); }
		if (dataOpn.Program_Management != undefined) { overAllOpen.push(dataOpn.Program_Management); } else { overAllOpen.push(putZero); }
		if (dataOpn.Sales != undefined) { overAllOpen.push(dataOpn.Sales); } else { overAllOpen.push(putZero); }
		if (dataOpn.SDE != undefined) { overAllOpen.push(dataOpn.SDE); } else { overAllOpen.push(putZero); }
		if (dataOpn._5S != undefined) { overAllOpen.push(dataOpn._5S); } else { overAllOpen.push(putZero); }
		if (dataOpn.Management != undefined) { overAllOpen.push(dataOpn.Management); } else { overAllOpen.push(putZero); }
	}

	if (data.overDeptClose != null && data.overDeptClose != "" && data.overDeptClose != undefined) {
		var dataCls = data.overDeptClose;
		if (dataCls.Maintenance != undefined) { overAllClosed.push(dataCls.Maintenance); } else { overAllClosed.push(putZero); }
		if (dataCls.ME != undefined) { overAllClosed.push(dataCls.ME); } else { overAllClosed.push(putZero); }
		if (dataCls.Production != undefined) { overAllClosed.push(dataCls.Production); } else { overAllClosed.push(putZero); }
		if (dataCls.Quality != undefined) { overAllClosed.push(dataCls.Quality); } else { overAllClosed.push(putZero); }
		if (dataCls.Logistic_FG != undefined) { overAllClosed.push(dataCls.Logistic_FG); } else { overAllClosed.push(putZero); }
		if (dataCls.Logistic_RM != undefined) { overAllClosed.push(dataCls.Logistic_RM); } else { overAllClosed.push(putZero); }
		if (dataCls.HR != undefined) { overAllClosed.push(dataCls.HR); } else { overAllClosed.push(putZero); }
		if (dataCls.Safety != undefined) { overAllClosed.push(dataCls.Safety); } else { overAllClosed.push(putZero); }
		if (dataCls.IT != undefined) { overAllClosed.push(dataCls.IT); } else { overAllClosed.push(putZero); }
		if (dataCls.Engineering != undefined) { overAllClosed.push(dataCls.Engineering); } else { overAllClosed.push(putZero); }
		if (dataCls.Finance != undefined) { overAllClosed.push(dataCls.Finance); } else { overAllClosed.push(putZero); }
		if (dataCls.Purchase != undefined) { overAllClosed.push(dataCls.Purchase); } else { overAllClosed.push(putZero); }
		if (dataCls.Program_Management != undefined) { overAllClosed.push(dataCls.Program_Management); } else { overAllClosed.push(putZero); }
		if (dataCls.Sales != undefined) { overAllClosed.push(dataCls.Sales); } else { overAllClosed.push(putZero); }
		if (dataCls.SDE != undefined) { overAllClosed.push(dataCls.SDE); } else { overAllClosed.push(putZero); }
		if (dataCls._5S != undefined) { overAllClosed.push(dataCls._5S); } else { overAllClosed.push(putZero); }
		if (dataCls.Management != undefined) { overAllClosed.push(dataCls.Management); } else { overAllClosed.push(putZero); }
	}

	var sectionsub = ["open", "closed"]
	var dept = ['Maintenance', 'ME', 'Production', 'Quality', 'Logistic_FG', 'Logistic_RM', 'HR', 'Safety', 'IT', 'Engineering', 'Finance', 'Purchase', 'Program Management', 'Sales', 'SDE', '5S','Management']
	dept.push("Total")
	var tableContainer = document.querySelector('.table-container');
	var table = document.getElementById('tableDynamicss');
	table.innerHTML = ""
	var tr = document.createElement('tr');
	var trsub = document.createElement('tr');
	var thead = document.createElement('thead');
	var theadsub = document.createElement('thead');

	var th = document.createElement('th');
	th.className = "fix"
	th.style = "text-align:center;padding-bottom:25px;";
	th.rowSpan = 2
	var text = document.createTextNode("Department");
	th.appendChild(text);
	tr.appendChild(th);

	thead.appendChild(tr)
	table.appendChild(thead);

	for (var il = 0; il < section.length - 1; il++) {
		var th = document.createElement('th');
		th.style = "text-align:center;";
		th.colSpan = 2
		var text = document.createTextNode(section[il]);
		th.appendChild(text);
		tr.appendChild(th);

		thead.appendChild(tr)
		table.appendChild(thead);
	}

	var th = document.createElement('th');
	th.style = "text-align:center;";
	th.colSpan = 2
	var text = document.createTextNode(section[section.length - 1]);
	th.appendChild(text);
	tr.appendChild(th);

	thead.appendChild(tr)
	table.appendChild(thead);

	var th = document.createElement('th');
	th.style = "text-align:center;padding-bottom:25px";
	th.rowSpan = 2
	var text = document.createTextNode("Update");
	th.appendChild(text);
	tr.appendChild(th);

	thead.appendChild(tr)
	table.appendChild(thead);

	for (var ik = 0; ik < section.length; ik++) {
		var th = document.createElement('th');
		th.style = "text-align:center;padding:5px";
		var text = document.createTextNode(sectionsub[0]);
		th.appendChild(text);
		trsub.appendChild(th);

		var th = document.createElement('th');
		th.style = "text-align:center;padding:5px";
		var text = document.createTextNode(sectionsub[1]);
		th.appendChild(text);
		trsub.appendChild(th);

		thead.appendChild(trsub)
		table.appendChild(theadsub);
	}
	var tbody = document.createElement('tbody');
	for (var i = 0; i < dept.length; i++) {
		var tr = document.createElement('tr');
		var td = document.createElement('td');
		td.className = "fix"
		td.style = "text-align:left;font-weight:bold";
		var text = document.createTextNode(dept[i]);
		td.appendChild(text);
		tr.appendChild(td);

		for (var j = 0; j < section.length - 1; j++) {
			let u
			if (i == 0) {
				u = i;
			} else {
				u = ((section.length - 1) * i)
			}
			var td = document.createElement('td');
			td.style = "text-align:center;";
			var text = document.createTextNode(datasetOpen[u + j]);
			td.appendChild(text);
			tr.appendChild(td);

			var td = document.createElement('td');
			td.style = "text-align:center;";
			var text = document.createTextNode(datasetClosed[u + j]);
			td.appendChild(text);
			tr.appendChild(td);

		}

		var td = document.createElement('td');
		td.style = "textlign:center;";
		var text = document.createTextNode(overAllOpen[i] == undefined ? "-" : overAllOpen[i]);
		td.appendChild(text);
		tr.appendChild(td);

		var td = document.createElement('td');
		td.style = "text-align:center;";
		var text = document.createTextNode(overAllClosed[i] == undefined ? "-" : overAllClosed[i]);
		td.appendChild(text);
		tr.appendChild(td);

		if (i != dept.length - 1) {
			var td = document.createElement('td');
			td.style = "text-align:center;";
			var btn = document.createElement('button');
			if (CryptoJS.AES.decrypt(getCookie('user'), "magna").toString(CryptoJS.enc.Utf8) == "0"
				|| CryptoJS.AES.decrypt(getCookie('user'), "magna").toString(CryptoJS.enc.Utf8) == "1" || userName === "Admin") {
				btn.disabled = true
			}
			btn.value = d_code[i]
			btn.onclick = function() { getSummaryDtl(this.value) };
			btn.className = "btn btn-primary"
			btn.innerHTML = "View"
			td.appendChild(btn);
			tr.appendChild(td);
		}
		else {
			var td = document.createElement('td');
			td.style = "text-align:center;";
			var text = document.createTextNode("-");
			td.appendChild(text);
			tr.appendChild(td);
		}

		tbody.appendChild(tr);
		table.appendChild(tbody);
		tableContainer.appendChild(table);
	}
	 

}

function showSec() {
	document.getElementById('secModal').style.display = "block"
	document.getElementById('secBodyAdd').style.display = "none"
	document.getElementById('secBodyRem').style.display = "none"
	var respn = document.getElementById("oldSec");
	respn.innerHTML = ""
	let ehr = new XMLHttpRequest();
	ehr.open('POST', baseUrl + '/getSectionList', true);
	ehr.send()

	ehr.onreadystatechange = function() {

		if (ehr.readyState == 4) {
			var deptList;
			deptList = JSON.parse(ehr.responseText)
			var options = document.createElement("option");
			options.text = "<--Select-->";
			respn.appendChild(options);
			for (var i = 0; i < deptList.length; i++) {
				var option = document.createElement("option");
				option.value = deptList[i].com_Code;
				option.text = deptList[i].com_Name;
				option.id = deptList[i].creator
				respn.appendChild(option);
			}
		}
	}
}

function insertSection() {

	var title = document.getElementById("SecTitle").value
	var type = document.getElementById("secType").value
	var secValue = document.getElementById('secRespondibility').value
	if (title != "") {
		var formData = new FormData();
		formData.append('SecName', title)
		formData.append('User', secValue)
		formData.append('SecType', type)
		let ehr = new XMLHttpRequest();
		ehr.open('POST', baseUrl + '/updateNewSection', true);
		ehr.send(formData)

		ehr.onreadystatechange = function() {

			if (ehr.readyState == 4) {
				var respdata = ehr.responseText;
				if (respdata == "success") {
					Toastify({
						text: 'New Section successfully Created',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "#90EE90",
							color: 'black',
						},
					}).showToast();
					mainTableContent()
					document.getElementById("closeModalsec").click();
					document.getElementById("SecTitle").value = ""
					document.getElementById("secRespondibility").value = "open"
				} else {
					Toastify({
						text: 'Something went wrong',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "red",
							color: 'Black',
						},
					}).showToast();
				}
			}
		}
	} else {
		Toastify({
			text: 'Please enter title',
			duration: 3000,
			newWindow: true,
			close: true,
			gravity: "top",
			position: "center",
			stopOnFocus: true,
			style: {
				background: "red",
				color: 'Black',
			},
		}).showToast();
	}
}

function InsertSec() {
	document.getElementById('secBodyAdd').style.display = "block"
	document.getElementById('secBodyRem').style.display = "none"
}

function RemoveSec() {
	document.getElementById('secBodyAdd').style.display = "none"
	document.getElementById('secBodyRem').style.display = "block"
}

function RemoveSection() {
	var secToRemove = document.getElementById('oldSec').value
	if (secToRemove != "<--Select-->") {
		var formData = new FormData();
		formData.append('SecName', secToRemove)
		let ehr = new XMLHttpRequest();
		ehr.open('POST', baseUrl + '/getRemoveSection', true);
		ehr.send(formData)

		ehr.onreadystatechange = function() {

			if (ehr.readyState == 4) {
				var respdata = ehr.responseText;
				if (respdata == "success") {
					Toastify({
						text: 'Section Removed successfully',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "#90EE90",
							color: 'black',
						},
					}).showToast();
					backtomain()
					document.getElementById("closeModalsec").click();
				} else {
					Toastify({
						text: 'Please Try Again',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "red",
							color: 'white',
						},
					}).showToast();
				}
			}
		}
	} else {
		Toastify({
			text: 'Please Select Section',
			duration: 3000,
			newWindow: true,
			close: true,
			gravity: "top",
			position: "center",
			stopOnFocus: true,
			style: {
				background: "red",
				color: 'white',
			},
		}).showToast();
	}
}

function showApp() {
	var dept = "GETALL"
	document.getElementById('summary_table').style.display = "none"
	document.getElementById('appList').style.display = "block"
	var formData = new FormData();
	formData.append('dept', dept)
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getfairactiondataApp', true);
	xhr.send(formData)
	xhr.onreadystatechange = function() {

		if (xhr.readyState == 4) {
			var AppData = JSON.parse(xhr.responseText)
			if (AppData.length != 0) {
				document.getElementById("sltNamedept").innerHTML = AppData[0].department;
			}
			var table = document.getElementById('tableDynamicApp');
			table.innerHTML = ""
			var tr = document.createElement('tr');
			var thead = document.createElement('thead');
			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Created Date");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Type");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("section");
			th.appendChild(text);
			tr.appendChild(th);
			
			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Section Creator");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Action Point");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Department");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Responsibility");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Target Date");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("View File");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Approve");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Reject");
			th.appendChild(text);
			tr.appendChild(th);

			thead.appendChild(tr)
			table.appendChild(thead);

			var tbody = document.createElement('tbody');
			document.getElementById("opencount").innerHTML = AppData.length;
			if (AppData.length == 0) {
				var dataNA = document.createTextNode("NO DATA");
				var td = document.createElement('td');
				td.colSpan = 10
				dataNA.style = "text-align:center;";
				td.style = "font-weight: bold;"
				td.appendChild(dataNA);
				tbody.appendChild(td);
				table.appendChild(tbody);
			}
			var isactive = 0;
			if(AppData.length > 0){
				for (var i = 0; i < AppData.length; i++) {
				var tabData = AppData[i];
				if (CryptoJS.AES.decrypt(getCookie('login'), "magna").toString(CryptoJS.enc.Utf8) == tabData.evidance) {
					isactive = 1;
					var tr = document.createElement('tr');

					var td = document.createElement('td');
					var dtSplit = tabData.date.split("-")
					let setdate = dtSplit[2] + "-" + dtSplit[1] + "-" + dtSplit[0]
					var text = document.createTextNode(setdate);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					td.style = "text-align:center;";
					var text = document.createTextNode(tabData.sec_type);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					td.style = "text-align:center;";
					var text = document.createTextNode(tabData.com_section);
					td.appendChild(text);
					tr.appendChild(td);
					
					var td = document.createElement('td');
					td.style = "text-align:center;";
					var text = document.createTextNode(tabData.sec_owner);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					td.style = "width:35%;text-align:left;font-weight:bold";
					var text = document.createTextNode(tabData.actionPoint);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					var text = document.createTextNode(tabData.department);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					var text = document.createTextNode(tabData.responsibility);
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					if (tabData.target_date != null) {
						var dtSplitTarget = tabData.target_date.split("-")
						var setdateTar = dtSplitTarget[2] + "-" + dtSplitTarget[1] + "-" + dtSplitTarget[0]
						var text = document.createTextNode(setdateTar);
						td.appendChild(text);
					} else {
						var text = document.createTextNode("-");
						td.appendChild(text);
					}

					tr.appendChild(td);

					var td = document.createElement('td');
					var text = document.createElement('button');
					text.className = 'btn btn-primary'
					text.innerHTML = "Open"
					text.value = tabData.comment
					text.id = tabData.id
					text.onclick = function() { getAttachedFile(this.id, this.value) };
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					var text = document.createElement('button');
					text.className = 'btn btn-primary'
					text.innerHTML = "Approve"
					text.value = tabData.id
					text.id = tabData.actionPoint
					text.onclick = function() { getApproveList(this.value, this.id) };
					td.appendChild(text);
					tr.appendChild(td);

					var td = document.createElement('td');
					var text = document.createElement('button');
					text.className = 'btn btn-primary';
					text.innerHTML = "Reject"
					text.value = tabData.id
					text.id = tabData.actionPoint
					text.onclick = function() { getRejectList(this.value, this.id) };
					td.appendChild(text);
					tr.appendChild(td);

					tbody.appendChild(tr);
					table.appendChild(tbody);
				}
			    }
			    if(isactive === 0){
					var dataNA = document.createTextNode("NO DATA");
					var td = document.createElement('td');
					td.colSpan = 10
					dataNA.style = "text-align:center;";
					td.style = "font-weight: bold;"
					td.appendChild(dataNA);
					tbody.appendChild(td);
					table.appendChild(tbody);
				}

			} else{
					var dataNA = document.createTextNode("NO DATA");
					var td = document.createElement('td');
					td.colSpan = 10
					dataNA.style = "text-align:center;";
					td.style = "font-weight: bold;"
					td.appendChild(dataNA);
					tbody.appendChild(td);
					table.appendChild(tbody);
			} 
		}
	}
}

function onSubmitPwd() {

	var oldPwd = document.getElementById('passwordOld').value
	var newPwd = document.getElementById('passwordNew').value
	var rePwd = document.getElementById('passwordAgain').value
	var userType = document.getElementById("userSelect").value
	var userName = document.getElementById("empSelect").value
	if (userType != "" && userName != "<--Select-->") {

		if (newPwd != rePwd) {
			Toastify({
				text: 'New Password Does not Match',
				duration: 3000,
				newWindow: true,
				close: true,
				gravity: "top",
				position: "center",
				stopOnFocus: true,
				style: {
					background: "red",
					color: 'white',
				},
			}).showToast();
		} else {
			var formData = new FormData();
			formData.append('username', userName)
			formData.append('codePass', oldPwd)
			formData.append('userType', userType)
			formData.append('codeNew', newPwd)
			let ehr = new XMLHttpRequest();
			ehr.open('POST', baseUrl + '/setNewPassword', true);
			ehr.send(formData)

			ehr.onreadystatechange = function() {

				if (ehr.readyState == 4) {
					var respdata = ehr.responseText;
					if (respdata == "success") {
						Toastify({
							text: 'Password Changed Successfully',
							duration: 3000,
							newWindow: true,
							close: true,
							gravity: "top",
							position: "center",
							stopOnFocus: true,
							style: {
								background: "#90EE90",
								color: 'black',
							},
						}).showToast();
						deleteCookie('login')

						window.location.href = "index.html";
					} else if (respdata == "error") {
						Toastify({
							text: 'Please try again',
							duration: 3000,
							newWindow: true,
							close: true,
							gravity: "top",
							position: "center",
							stopOnFocus: true,
							style: {
								background: "red",
								color: 'white',
							},
						}).showToast();
					} else if (respdata == "NOM") {
						Toastify({
							text: 'Current Password is worng',
							duration: 3000,
							newWindow: true,
							close: true,
							gravity: "top",
							position: "center",
							stopOnFocus: true,
							style: {
								background: "red",
								color: 'white',
							},
						}).showToast();
					}
				}
			}
		}
	} else {
		Toastify({
			text: 'Please select UserType / UserName',
			duration: 3000,
			newWindow: true,
			close: true,
			gravity: "top",
			position: "center",
			stopOnFocus: true,
			style: {
				background: "red",
				color: 'black',
			},
		}).showToast();
	}
}

function showDash() {
	window.location.href = "dashboard.html";
}

function setRespnsPerson(value, textval) {
	EmpNameResp = textval.options[textval.selectedIndex].text
	EmpCodeResp = value;
	respStatus = 1
}

function dataURLtoFile(dataurl, filename) {

	var arr = dataurl.split(','),
		mime = arr[0].match(/:(.*?);/)[1],
		bstr = atob(arr[1]),
		n = bstr.length,
		u8arr = new Uint8Array(n);

	while (n--) {
		u8arr[n] = bstr.charCodeAt(n);
	}

	return new File([u8arr], filename, { type: mime });
}

function getAttachedFile(id, comment) {
	if (comment != null && comment != "null") {
		document.getElementById("commentBox").innerHTML = comment
		var style = document.getElementById("commentBox")
		style.style = "color:black";
		style.style.fontWeight = "bold"
	} else {
		document.getElementById("commentBox").innerHTML = "Not Available"
		var style = document.getElementById("commentBox")
		style.style = "color:red";
		style.style.fontWeight = "bold"
	}

	var formData = new FormData();
	formData.append('rowId', id)
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getbase64Content', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {
		var respdata;
		if (xhr.readyState == 4) {
			respdata = xhr.responseText;
			if (respdata.length > 0) {
				document.getElementById("bsmodal1").style.display = "block"
				document.getElementById("imgElem").setAttribute('src', respdata);
			}
		}
	}
}

function ShowPicBtn() {
	document.getElementById("bsmodal1").style.display = "none"
}

function getApproveList(id, value) {

	var formData = new FormData();
	formData.append('rowId', id)
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getUpdateApprove', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {
		var respdata;
		if (xhr.readyState == 4) {
			respdata = xhr.responseText;
			if (respdata == "success") {
				ShowPicBtn()
				Toastify({
					text: 'Item Approved Successfully',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "#90EE90",
						color: 'black',
					},
				}).showToast();
				showApp()
				var htmlMsgBody = `<!DOCTYPE html>
									<html>
									<head>
										<style>
											h5{
									            margin-top:10px;
									            margin-bottom:2px;
									        }
									        p{
									            margin-top:5px
									        }
									        img{
									            width:15%
									        }
											#customers {
												font-family: Arial, Helvetica, sans-serif;
												border-collapse: collapse;
												width: 100%;
											}
											#customers td,
											#customers th {
												border: 1px solid #ddd;
												padding: 8px;
											}
											#customers tr:nth-child(even) {
												background-color: #f2f2f2;
											}
											#customers tr:hover {
												background-color: #ddd;
											}
											#customers th {
												padding-top: 12px;
												padding-bottom: 12px;
												text-align: left;
												background-color: #5A5A5A;
												color: white;
												text-align: center;
											}
											#customers td{
												text-align: center;
											}
										</style>
									</head>
									<body>
										<div>
											<table id="customers">
												<tr>
													<th colspan=4>
														Action Point Approved Notification</th>
												</tr>
												<tr>
													<td>Status</td>
													<td>Approved</td>
												</tr>
												<tr>
													<td>Action Point</td>
													<td>${value}</td>
												</tr>
												<tr>
													<td>Link</td>
													<td><a href="http://10.215.8.94:8789/magna/main.html">Magna Project Dashboard</a></td>
												</tr>
											</table>
										</div>
										<div>
											<br>
											<p>Regards,</p>
											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>
											<p>
												Plot No:7B, 8th Avenue, 1st Cross Road,<br>
												Mahindra World City,<br>
												Chengalpattu District - 603004.<br>
												Tamil Nadu, India<br>
											</p>
											<img src="./img/Magna-Logo.png" alt="Magna-Logo">
										</div>
									</body>
									</html>`
				var formData1 = {
					"recipient": id,
					"msgBody": htmlMsgBody,
					"subject": "Action Point Approved"
				};
				let zhr = new XMLHttpRequest();
				zhr.open('POST', baseUrl + '/sendMail', true);
				zhr.setRequestHeader("Content-Type", "application/json");
				zhr.send(JSON.stringify(formData1))
				zhr.onreadystatechange = function() {
					if (zhr.readyState == 4) {
						var respdata = zhr.responseText;
						Toastify({
							text: "Mail Sent Successfully...",
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
			} else {
				Toastify({
					text: 'Something went wrong',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "red",
						color: 'black',
					},
				}).showToast();
			}
		}
	}

}

function confirmReject(id, Report, value) {
	var formData = new FormData();
	formData.append('rowId', id)
	formData.append('reason', Report)
	if(Report !== "" ){

	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getRejectApprove', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {
		var respdata;
		if (xhr.readyState == 4) {
			respdata = xhr.responseText;
			if (respdata == "success") {
				ShowPicBtn()
				Toastify({
					text: 'Item Rejected Successfully',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "#90EE90",
						color: 'black',
					},
				}).showToast();
				showApp()
				var htmlMsgBody = `<!DOCTYPE html>
									<html>
									<head>
										<style>
											h5{
									            margin-top:10px;
									            margin-bottom:2px;
									        }
									        p{
									            margin-top:5px
									        }
									        img{
									            width:15%
									        }
											#customers {
												font-family: Arial, Helvetica, sans-serif;
												border-collapse: collapse;
												width: 100%;
											}
											#customers td,
											#customers th {
												border: 1px solid #ddd;
												padding: 8px;
											}
											#customers tr:nth-child(even) {
												background-color: #f2f2f2;
											}
											#customers tr:hover {
												background-color: #ddd;
											}
											#customers th {
												padding-top: 12px;
												padding-bottom: 12px;
												text-align: left;
												background-color: #5A5A5A;
												color: white;
												text-align: center;
											}
											#customers td{
												text-align: center;
											}
										</style>
									</head>
									<body>
										<div>
											<table id="customers">
												<tr>
													<th colspan=4>
														Action Point Rejected Notification</th>
												</tr>
												<tr>
													<td>Action Point</td>
													<td>${value}</td>
												</tr>
												<tr>
													<td>Reason</td>
													<td>${Report}</td>
												</tr>
												<tr>
													<td>Link</td>
													<td><a href="http://10.215.8.94:8789/magna/main.html">Magna Project Dashboard</a></td>
												</tr>
											</table>
											<a href=${'10.215.8.94:8789'}>Magna Project Dashboard</a>
										</div>
										<div>
											<br>
											<p>Regards,</p>
											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>
											<p>
												Plot No:7B, 8th Avenue, 1st Cross Road,<br>
												Mahindra World City,<br>
												Chengalpattu District - 603004.<br>
												Tamil Nadu, India<br>
											</p>
											<img src="./img/Magna-Logo.png" alt="Magna-Logo">
										</div>
									</body>
									</html>`
				var formData1 = {
					"recipient": id,
					"msgBody": htmlMsgBody,
					"subject": "Action Point Rejected Notification"
				};
				let zhr = new XMLHttpRequest();
				zhr.open('POST', baseUrl + '/sendMail', true);
				zhr.setRequestHeader("Content-Type", "application/json");
				zhr.send(JSON.stringify(formData1))
				zhr.onreadystatechange = function() {
					if (zhr.readyState == 4) {
						var respdata = zhr.responseText;
						Toastify({
							text: "Mail Sent Successfully...",
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
			} else {
				Toastify({
					text: 'Something went wrong',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
					background: "red",
					color: 'black',
				},
			  }).showToast();
			}
		}
	}
  }
}

function getRejectList(id, value) {
  
	BootstrapDialog.show({
		title: 'Remark',
		message: $('<textarea class="form-control" placeholder="Enter Reason for Rejection..."></textarea>'),
		onhide: function(dialogRef) {
			var Report = dialogRef.getModalBody().find('textarea').val();
			if (Report == '') {
				Toastify({
					text: 'Reason for Rejection is Mandatory',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "red",
						color: 'black',
					},
				}).showToast();
				}
				return false;
		},
		type: BootstrapDialog.TYPE_PRIMARY,
		closable: true,
		draggable: true,
		btnCancelLabel: 'Close',
		btnOKLabel: 'Submit',
		btnOKClass: 'btn-warning',
		buttons: [{
			label: 'submit',
			cssClass: 'btn-primary',
			hotkey: 13,
			action: function(dialogRef) {
				confirmReject(id, dialogRef.getModalBody().find('textarea').val(), value)
				dialogRef.close();
			}
		}]
	});


}

function getuserList(value) {
	var respn = document.getElementById("empSelect");
	if (value != "") {
		respn.innerHTML = ""
		var formData = new FormData();
		formData.append('userType', value)
		let ehr = new XMLHttpRequest();
		ehr.open('POST', baseUrl + '/getempUserList', true);
		ehr.send(formData)

		ehr.onreadystatechange = function() {

			if (ehr.readyState == 4) {
				var deptList;
				deptList = JSON.parse(ehr.responseText)
				if (value != '0') {
					respn.disabled = false
					var options = document.createElement("option");
					options.value = "<--Select-->"
					options.text = "<--Select-->";
					respn.appendChild(options);
				}
				for (var i = 0; i < deptList.length; i++) {
					var option = document.createElement("option");
					option.value = deptList[i].username;
					option.text = deptList[i].username;
					if (value == '0') {
						option.selected = true
					}
					respn.appendChild(option);
				}
			}
		}
	} else {
		respn.disabled = true
	}
}

function showRtnList() {
	var deptList;
	let shr = new XMLHttpRequest();
	shr.open('POST', baseUrl + '/getDepartmentDrop', true);
	shr.send()

	shr.onreadystatechange = function() {

		if (shr.readyState == 4) {
			deptList = JSON.parse(shr.responseText)
		}
	}
	var user = CryptoJS.AES.decrypt(getCookie('login'), "magna").toString(CryptoJS.enc.Utf8)
	document.getElementById('summary_table').style.display = "none"
	document.getElementById('returnList').style.display = "block"
	var formData = new FormData();
	formData.append('user', user)
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getfairactiondataReturn', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {

		if (xhr.readyState == 4) {
			var AppData = JSON.parse(xhr.responseText)
			var table = document.getElementById('tableDynamicReturn');
			table.innerHTML = ""
			var tr = document.createElement('tr');
			var thead = document.createElement('thead');
			var th = document.createElement('th');
			th.style = "background-color:#596744;width:10%";
			var text = document.createTextNode("Created Date");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Type");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("section");
			th.appendChild(text);
			tr.appendChild(th);
			
			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Section Creator");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Action Point");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Department");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Responsibility");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744;width:10%";
			var text = document.createTextNode("Target Date");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Department");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744;width:25%";
			var text = document.createTextNode("Action");
			th.appendChild(text);
			tr.appendChild(th);
			
			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("SendBack Comment");
			th.appendChild(text);
			tr.appendChild(th);

			thead.appendChild(tr)
			table.appendChild(thead);

			var tbody = document.createElement('tbody');
			document.getElementById("opencount").innerHTML = AppData.length
			if (AppData.length == 0) {
				var dataNA = document.createTextNode("NO DATA");
				var td = document.createElement('td');
				td.colSpan = 10
				dataNA.style = "text-align:center;";
				td.style = "font-weight: bold;"
				td.appendChild(dataNA);
				tbody.appendChild(td);
				table.appendChild(tbody);
			}
			for (var i = 0; i < AppData.length; i++) {

				var tabData = AppData[i];
				var tr = document.createElement('tr');

				var td = document.createElement('td');
				var dtSplit = tabData.date.split("-")
				let setdate = dtSplit[2] + "-" + dtSplit[1] + "-" + dtSplit[0]
				var text = document.createTextNode(setdate);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				td.style = "text-align:center;";
				var text = document.createTextNode(tabData.sec_type);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				td.style = "text-align:center;";
				var text = document.createTextNode(tabData.com_section);
				td.appendChild(text);
				tr.appendChild(td);
				
				var td = document.createElement('td');
				td.style = "text-align:center;";
				var text = document.createTextNode(tabData.sec_owner);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				td.style = "width:35%;text-align:left;font-weight:bold";
				var text = document.createTextNode(tabData.actionPoint);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				var text = document.createTextNode(tabData.department);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				var text = document.createTextNode(tabData.responsibility);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				if (tabData.target_date != null) {
					var dtSplitTarget = tabData.target_date.split("-")
					var setdateTar = dtSplitTarget[2] + "-" + dtSplitTarget[1] + "-" + dtSplitTarget[0]
					var text = document.createTextNode(setdateTar);
					td.appendChild(text);
				} else {
					var text = document.createTextNode("-");
					td.appendChild(text);
				}

				tr.appendChild(td);

				var td = document.createElement('td');
				var select = document.createElement("select")
				select.id = "rejdept_" + i;
			//	select.onchange = function(e) { getonchangereturnList(e, this.value, AppData, this) };
				var options = document.createElement("option");
				options.text = "<--Select-->";
				select.appendChild(options);
				for (var ik = 0; ik < deptList.length; ik++) {
					var option = document.createElement("option");
					option.value = deptList[ik].dept_code;
					option.text = deptList[ik].dept;
					
					select.appendChild(option);
				}
				td.appendChild(select);
				tr.appendChild(td);

				var td = document.createElement('td');
				var text1 = document.createElement('button');
				text1.className = 'btn btn-primary'
				text1.innerHTML = "Submit"
				text1.onclick = function(e) { getonchangereturnList(e, AppData, "rejdept_") };
				var text = document.createElement('button');
				text.className = 'btn btn-danger'
				text.innerHTML = "Delete"
				text.style="margin-left:5px"
				text.onclick = function(e) { getDeleteList(e, AppData) };
				td.appendChild(text1);
				td.appendChild(text);
				tr.appendChild(td);
				
				var td = document.createElement('td');
				td.style = "width:35%;text-align:left;font-weight:bold";
				var text = document.createTextNode(tabData.sendback_comment !== null ? tabData.sendback_comment : "" );
				td.appendChild(text);
				tr.appendChild(td);

				tbody.appendChild(tr);
				table.appendChild(tbody);
			}
		}
	}
}

function getonchangereturnList(e, value, dropdata) {
	
	//var nameResp = 
	var rowIndex = e.target.parentNode.parentNode.rowIndex - 1;
	var id = dropdata + rowIndex;
	var dropvalue = document.getElementById(id).value;
	var nameResp = document.getElementById(id);
	var deptname = nameResp.options[nameResp.selectedIndex].innerHTML
	if(deptname !== "&lt;--Select--&gt;" ){
	
	var selectedData = value[rowIndex]

	var formData = new FormData();
	formData.append('rowId', selectedData.id)
	formData.append('dept', dropvalue)
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getreturnListUpdate', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {
		var respdata;
		if (xhr.readyState == 4) {
			respdata = xhr.responseText;
			if (respdata != "error") {
				Toastify({
					text: 'Item Updated Successfully',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "#90EE90",
						color: 'black',
					},
				}).showToast();
				showRtnList()
				var htmlMsgBody = `<!DOCTYPE html>
									<html>
									<head>
										<style>
											h5{
									            margin-top:10px;
									            margin-bottom:2px;
									        }
									        p{
									            margin-top:5px
									        }
									        img{
									            width:15%
									        }
											#customers {
												font-family: Arial, Helvetica, sans-serif;
												border-collapse: collapse;
												width: 100%;
											}
											#customers td,
											#customers th {
												border: 1px solid #ddd;
												padding: 8px;
											}
											#customers tr:nth-child(even) {
												background-color: #f2f2f2;
											}
											#customers tr:hover {
												background-color: #ddd;
											}
											#customers th {
												padding-top: 12px;
												padding-bottom: 12px;
												text-align: left;
												background-color: #5A5A5A;
												color: white;
												text-align: center;
											}
											#customers td{
												text-align: center;
											}
										</style>
									</head>
									<body>
										<div>
											<table id="customers">
												<tr>
													<th colspan=4>
														Action Point Update Notification</th>
												</tr>
												<tr>
													<td>Department</td>
													<td>${selectedData.department}</td>
												</tr>
												<tr>
													<td>Action Point</td>
													<td>${selectedData.actionPoint}</td>
												</tr>
												<tr>
													<td>Target date</td>
													<td>${selectedData.target_date || "-"}</td>
												</tr>
												<tr>
													<td>Responsible person</td>
													<td>${nameResp}</td>
												</tr>
												<tr>
													<td>Link</td>
													<td><a href="http://10.215.8.94:8789/magna/main.html">Magna Project Dashboard</a></td>
												</tr>
											</table>
										</div>
										<div>
											<br>
											<p>Regards,</p>
											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>
											<p>
												Plot No:7B, 8th Avenue, 1st Cross Road,<br>
												Mahindra World City,<br>
												Chengalpattu District - 603004.<br>
												Tamil Nadu, India<br>
											</p>
											<img src="./img/Magna-Logo.png" alt="Magna-Logo">
										</div>
									</body>
									</html>`
				var formData1 = {
					"recipient": selectedData.id,
					"msgBody": htmlMsgBody,
					"subject": "Action Point Update Notification"
				};
				let zhr = new XMLHttpRequest();
				zhr.open('POST', baseUrl + '/sendMail', true);
				zhr.setRequestHeader("Content-Type", "application/json");
				zhr.send(JSON.stringify(formData1))
				zhr.onreadystatechange = function() {
					if (zhr.readyState == 4) {
						var respdata = zhr.responseText;
						Toastify({
							text: "Mail Sent Successfully...",
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
			} else {
				Toastify({
					text: 'Something went wrong',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "red",
						color: 'black',
					},
				}).showToast();
			}
		}
	}
	}else{
		Toastify({
			text: 'Please fill the empty field',
			duration: 3000,
			newWindow: true,
			close: true,
			gravity: "top",
			position: "center",
			stopOnFocus: true,
			style: {
				background: "red",
			},
		}).showToast();
	}
}

function sendBackPopupwindow(e,value){

	var rowIndex = e.target.parentNode.parentNode.rowIndex - 1
	selectedData = frActionData[rowIndex]
	var objectString = JSON.stringify(selectedData);
	document.getElementById("sendbackpopup").style = "display:block";
	document.getElementById("sendbackparam1").value = objectString;
	document.getElementById("sendbackparam2").value = value;
}

function getListSendBack() {
	
	var e = document.getElementById("sendbackparam1").value;
	var value = document.getElementById("sendbackparam2").value;
	var comment = document.getElementById("comment").value;
	
	if(comment !== ""){
	selectedData = JSON.parse(e)
	var department = document.getElementById("sltName").innerHTML
	var formData = new FormData();
	formData.append('rowId', value)
	formData.append('comment',comment)
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getUpdatereturnlist', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {
		var respdata;
		if (xhr.readyState == 4) {
			respdata = xhr.responseText;
			if (respdata == "success") {
				document.getElementById("sendbackpopup").style = "display:none";
				document.getElementById("comment").value = ""
				Toastify({
					text: 'Item Send back Successfully',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "#90EE90",
						color: 'black',
					},
				}).showToast();
				document.getElementById("tableDynamic").innerHTML = "";
				getSummaryDtl(department);
				var htmlMsgBody = `<!DOCTYPE html>
									<html>
									<head>
										<style>
											h5{
									            margin-top:10px;
									            margin-bottom:2px;
									        }
									        p{
									            margin-top:5px
									        }
									        img{
									            width:15%
									        }
											#customers {
												font-family: Arial, Helvetica, sans-serif;
												border-collapse: collapse;
												width: 100%;
											}
											#customers td,
											#customers th {
												border: 1px solid #ddd;
												padding: 8px;
											}
											#customers tr:nth-child(even) {
												background-color: #f2f2f2;
											}
											#customers tr:hover {
												background-color: #ddd;
											}
											#customers th {
												padding-top: 12px;
												padding-bottom: 12px;
												text-align: left;
												background-color: #5A5A5A;
												color: white;
												text-align: center;
											}
											#customers td{
												text-align: center;
											}
										</style>
									</head>
									<body>
										<div>
											<table id="customers">
												<tr>
													<th colspan=4>
														Action Point Sent Back Notification</th>
												</tr>
												<tr>
													<td>Department</td>
													<td>${selectedData.department}</td>
												</tr>
												<tr>
													<td>Action Point</td>
													<td>${selectedData.actionPoint}</td>
												</tr>
												<tr>
													<td>Target date</td>
													<td>${selectedData.target_date || "-"}</td>
												</tr>
												<tr>
													<td>Link</td>
													<td><a href="http://10.215.8.94:8789/magna/main.html">Magna Project Dashboard</a></td>
												</tr>
											</table>
										</div>
										<div>
											<br>
											<p>Regards,</p>
											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>
											<p>
												Plot No:7B, 8th Avenue, 1st Cross Road,<br>
												Mahindra World City,<br>
												Chengalpattu District - 603004.<br>
												Tamil Nadu, India<br>
											</p>
											<img src="./img/Magna-Logo.png" alt="Magna-Logo">
										</div>
									</body>
									</html>`
				var formData1 = {
					"recipient": selectedData.evidance,
					"msgBody": htmlMsgBody,
					"subject": "Action Point Return Notification"
				};
				let zhr = new XMLHttpRequest();
				zhr.open('POST', baseUrl + '/sendMail', true);
				zhr.setRequestHeader("Content-Type", "application/json");
				zhr.send(JSON.stringify(formData1))
				zhr.onreadystatechange = function() {
					if (zhr.readyState == 4) {
						var respdata = zhr.responseText;
						Toastify({
							text: "Mail Sent Successfully...",
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
			} else {
				Toastify({
					text: 'Something went wrong',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "red",
						color: 'black',
					},
				}).showToast();
			}
		}
	}
  }else{
	  Toastify({
		  text: 'Please fill Empty field',
		  duration: 3000,
		  newWindow: true,
		  close: true,
		  gravity: "top",
		  position: "center",
		  stopOnFocus: true,
		  style: {
			background: "red",
			color: 'black',
		  },
	  }).showToast();
  }	
}

function showEmpMaster() {

	var respn = document.getElementById("Department");
	respn.innerHTML = ""
	let shr = new XMLHttpRequest();
	shr.open('POST', baseUrl + '/getDepartmentDrop', true);
	shr.send()

	shr.onreadystatechange = function() {

		if (shr.readyState == 4) {
			var deptList;
			deptList = JSON.parse(shr.responseText)
			var options = document.createElement("option");
			options.text = "<--Select-->";
			respn.appendChild(options);
			for (var i = 0; i < deptList.length; i++) {
				var option = document.createElement("option");
				option.value = deptList[i].dept_code;
				option.text = deptList[i].dept;
				respn.appendChild(option);
			}
		}
	}

	document.getElementById('summary_table').style.display = "none"
	document.getElementById("EmpList").style.display = "block"

     var formData =  new FormData();
     formData.append('empType',"GETALL")
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/getEmployeeDetails', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {

		if (xhr.readyState == 4) {
			EmpData = JSON.parse(xhr.responseText)
			var table = document.getElementById('tableDynamicEmpl');
			table.innerHTML = ""
			var tr = document.createElement('tr');
			var thead = document.createElement('thead');
			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Emp Code");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Emp Name");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Email");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Department");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Role");
			th.appendChild(text);
			tr.appendChild(th);

			var th = document.createElement('th');
			th.style = "background-color:#596744";
			var text = document.createTextNode("Status");
			th.appendChild(text);
			tr.appendChild(th);

			thead.appendChild(tr)
			table.appendChild(thead);

			var tbody = document.createElement('tbody');
			for (var i = 0; i < EmpData.length; i++) {
				var tabData = EmpData[i];
				var tr = document.createElement('tr');

				var td = document.createElement('td');
				td.style = "font-weight:bold;cursor:pointer";
				var text = document.createTextNode(tabData.emp_code);
				td.onclick = function(e) { getValue(e) };
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				td.style = "text-align:left;";
				var text = document.createTextNode(tabData.emp);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				td.style = "text-align:left;";
				var text = document.createTextNode(tabData.mail);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				var text = document.createTextNode(tabData.dept);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				let status
				if (tabData.emp_type == 2) {
					status = "Admin"
					td.style = "font-weight:bold";
				} else {
					status = "Department"
					td.style = "font-weight:bold";
				}
				var text = document.createTextNode(status);
				td.appendChild(text);
				tr.appendChild(td);

				var td = document.createElement('td');
				let active
				if (tabData.is_active == 1) {
					active = "Active"
					td.style = "font-weight:bold;color:green";
				} else {
					active = "Blocked"
					td.style = "font-weight:bold;color:red";
				}
				var text = document.createTextNode(active);
				td.appendChild(text);
				tr.appendChild(td);

				tbody.appendChild(tr);
				table.appendChild(tbody);
				changePageemp(current_page_emp);
			}
		}
	}
}

function empSearchCls() {
	const searchBox = document.getElementById('searchBoxEmp');
	const tabled = document.getElementById("tableDynamicEmpl");
	const trs = tabled.tBodies[0].getElementsByTagName("tr");
	var filter = searchBox.value.toUpperCase();

	if (filter != "") {
		for (var rowI = 0; rowI < trs.length; rowI++) {

			var tds = trs[rowI].getElementsByTagName("td");

			trs[rowI].style.display = "none";

			for (var cellI = 0; cellI < tds.length; cellI++) {

				if (tds[cellI].innerHTML.toUpperCase().indexOf(filter) > -1) {

					trs[rowI].style.display = "";

					continue;

				}
			}
		}

	} else {
		showEmpMaster()
	}

}

function getValue(e) {

	var rowIndex = e.target.parentNode.rowIndex - 1
	var selectedData = EmpData[rowIndex]
	document.getElementById("updateEmp").style.display = "block"
	document.getElementById("EmName").value = selectedData.emp
	document.getElementById("EmCode").value = selectedData.emp_code
	var selectDpt = document.getElementById("Department")
	selectDpt.selectedIndex = [...selectDpt.options].findIndex(option => option.text === selectedData.dept);
	document.getElementById("EmEmail").value = selectedData.mail
	document.getElementById("Roll").value = selectedData.emp_type
	document.getElementById("Status").value = selectedData.is_active

	window.scrollBy(0, 500);

}

function submitempDetails() {
	var emCode = document.getElementById("EmCode").value
	var name = document.getElementById("EmName").value
	var selectDpt = document.getElementById("Department")
	var selectedText = selectDpt.options[selectDpt.selectedIndex].text;
	var mail = document.getElementById("EmEmail").value
	var empType = document.getElementById("Roll").value
	var empStatus = document.getElementById("Status").value

	if (name != "" && selectDpt.value != "<--Select-->" && mail != "" && empType != "" && empStatus != "") {

		var formData = new FormData();
		formData.append('name', name)
		formData.append('code', emCode)
		formData.append('deptCode', selectDpt.value)
		formData.append('dept', selectedText)
		formData.append('mail', mail)
		formData.append('empType', empType)
		formData.append('status', empStatus)
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/getUpdateEmpDetail', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {
			var respdata;
			if (xhr.readyState == 4) {
				respdata = xhr.responseText;
				if (respdata == "success") {
					Toastify({
						text: 'Employee Details Updated Successfully',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "#90EE90",
							color: 'black',
						},
					}).showToast();
					document.getElementById('tableDynamicEmpl').innerHTML = ""
					showEmpMaster()
					cancelEmpDetails()
				} else {
					Toastify({
						text: 'Something went wrong',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "red",
							color: 'black',
						},
					}).showToast();
				}
			}
		}
	}

}

function AddEmp() {
	document.getElementById("EmCode").value = ""
	document.getElementById("EmName").value = ""
	document.getElementById("Department").value = "<--Select-->"
	document.getElementById("EmEmail").value = ""
	document.getElementById("Roll").value = ""
	document.getElementById("Status").value = ""
	document.getElementById("updateEmp").style.display = "block"
	window.scrollBy(0, 500);
}

function cancelEmpDetails() {
	document.getElementById("EmCode").value = ""
	document.getElementById("EmName").value = ""
	document.getElementById("Department").value = "<--Select-->"
	document.getElementById("EmEmail").value = ""
	document.getElementById("Roll").value = ""
	document.getElementById("Status").value = ""
	document.getElementById("updateEmp").style.display = "none"
	window.scrollBy(0, -500);
}

function getOnSubmit(e, id) {
	var department = document.getElementById("sltName").innerHTML
	var rowIndex = e.target.parentNode.parentNode.rowIndex - 1
	var selectedData = frActionData[rowIndex]
	var formData = new FormData();
	formData.append('respn', EmpCodeResp === "" ? selectedData.responsibility : EmpCodeResp)
	formData.append('status', respStatus)
	formData.append('rowId', id)
	let xhrs = new XMLHttpRequest();
	xhrs.open('POST', baseUrl + '/updateResponsePerson', true);
	xhrs.send(formData)

	xhrs.onreadystatechange = function() {
		var respdata;
		if (xhrs.readyState == 4) {
			respdata = xhrs.responseText;
			if (respdata == "success") {
			}
		}
	}

	var department = document.getElementById("sltName").innerHTML
	var formData = new FormData();
	formData.append('targetDate', tarDate)
	formData.append('rowId', id)
	formData.append('status', 1)
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + '/updateFairnessTarget', true);
	xhr.send(formData)

	xhr.onreadystatechange = function() {
		var respdata;
		if (xhr.readyState == 4) {
			respdata = JSON.parse(xhr.responseText);
			if (respdata != null && respdata.length > 0) {
				document.getElementById("tableDynamic").innerHTML = "";
				getSummaryDtl(department)
				Toastify({
					text: 'Action point target date added',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "4BB543",
						color: 'black',
					},
				}).showToast();
				var htmlMsgBody = `<!DOCTYPE html>
									<html>
									<head>
										<style>
											h5{
									            margin-top:10px;
									            margin-bottom:2px;
									        }
									        p{
									            margin-top:5px
									        }
									        img{
									            width:15%
									        }
											#customers {
												font-family: Arial, Helvetica, sans-serif;
												border-collapse: collapse;
												width: 100%;
											}
											#customers td,
											#customers th {
												border: 1px solid #ddd;
												padding: 8px;
											}
											#customers tr:nth-child(even) {
												background-color: #f2f2f2;
											}
											#customers tr:hover {
												background-color: #ddd;
											}
											#customers th {
												padding-top: 12px;
												padding-bottom: 12px;
												text-align: left;
												background-color: #5A5A5A;
												color: white;
												text-align: center;
											}
											#customers td{
												text-align: center;
											}
										</style>
									</head>
									<body>
										<div>
											<table id="customers">
												<tr>
													<th colspan=4>
														Action Point Notification</th>
												</tr>
												<tr>
													<td>Department</td>
													<td>${selectedData.department}</td>
												</tr>
												<tr>
													<td>Action Point</td>
													<td>${selectedData.actionPoint}</td>
												</tr>
												<tr>
													<td>Target date</td>
													<td>${tarDate}</td>
												</tr>
												<tr>
													<td>Responsible person</td>
													<td>${EmpCodeResp === "" ? selectedData.responsibility : EmpCodeResp}</td>
												</tr>
												<tr>
													<td>Link</td>
													<td><a href="http://10.215.8.94:8789/magna/main.html">Magna Project Dashboard</a></td>
												</tr>
											</table>
										</div>
										<div>
											<br>
											<p>Regards,</p>
											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>
											<p>
												Plot No:7B, 8th Avenue, 1st Cross Road,<br>
												Mahindra World City,<br>
												Chengalpattu District - 603004.<br>
												Tamil Nadu, India<br>
											</p>
											<img src="./img/Magna-Logo.png" alt="Magna-Logo">
										</div>
									</body>
									</html>`
				var formData1 = {
					"recipient": id,
					"msgBody": htmlMsgBody,
					"subject": "Action Point Added Request"
				};
				let zhr = new XMLHttpRequest();
				zhr.open('POST', baseUrl + '/sendMail', true);
				zhr.setRequestHeader("Content-Type", "application/json");
				zhr.send(JSON.stringify(formData1))
				zhr.onreadystatechange = function() {
					if (zhr.readyState == 4) {
						var respdata = zhr.responseText;
						Toastify({
							text: "Mail sent successfully...",
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
			} else {
				Toastify({
					text: 'Something went wrong',
					duration: 3000,
					newWindow: true,
					close: true,
					gravity: "top",
					position: "center",
					stopOnFocus: true,
					style: {
						background: "red",
						color: 'Black',
					},
				}).showToast();
			}
		}

	}
}

function getuploadpic(event) {
	var file = event.srcElement.files[0];
	fileToUpload = file
	var reader = new FileReader();
	reader.readAsBinaryString(file);

	reader.onload = function() {
		var data64 = btoa(reader.result);
	};
	reader.onerror = function() {
		console.log('there are some error');
	};
}


toggleAction = () => {
	const actionTog = document.getElementById("checkBoxAction").checked
	if (actionTog) {
		document.getElementById("toggleswitchAction").innerHTML = "Sub Point"
		document.getElementById("subData").style.display = "block"
		document.getElementById("Action").style.display = "none"
		document.getElementById("frModal_Dialog").style.height = "170%";
	} else {
		document.getElementById("frModal_Dialog").style.height = "90%";
		document.getElementById("toggleswitchAction").innerHTML = "Normal"
		document.getElementById("subData").style.display = "none"
		document.getElementById("Action").style.display = "block"
	}
}

function onEditSubDataCancel(value, tab_id, id,Addbtn_id, Delbtn_id) {
	//cancel
	var idSpl = id.split("_")
	var table = document.getElementById(value)
	var rowLength = table.rows.length;
	let Addbtn = Addbtn_id + idSpl[1];
	let Delbtn = Delbtn_id + idSpl[1];
	for (i = 0; i < rowLength - 1; i++) {
		var oCells = table.rows.item(i).cells;
		var cellLength = oCells.length;
		for (var j = 0; j < cellLength; j++) {
			if (j == 0) {
				oCells[j].getElementsByTagName('textarea')[0].disabled = true;
			} else {
				if (oCells[j].textContent == "")
					oCells[j].getElementsByTagName('input')[0].disabled = true;
			}
		}
	}
	document.getElementById(tab_id + idSpl[1]).innerHTML = "Edit"
	document.getElementById(tab_id + idSpl[1]).style="margin-left:95%";
	document.getElementById(id).style = "display:none"
	document.getElementById(Addbtn).style = "display:none"
	document.getElementById(Delbtn).style = "display:none"
	document.getElementById(tab_id + idSpl[1]).className = "btn btn-primary"
}


function onEditSubData(value, data, id, cancel_id, Addbtn_id, tableid,cdate, Delbtn_id) {

	//Edit Button
	var idSpl = id.split("_")
	var department = document.getElementById("sltName").innerHTML
	var editBtn = document.getElementById(id).innerHTML
	var editbutton = document.getElementById(id)
	var table = document.getElementById(value)
	let Addbtn = Addbtn_id + idSpl[1];
	let Delbtn = Delbtn_id + idSpl[1];
	let tab1 = document.getElementById(tableid);
	let createdat = cdate + idSpl[1];
	var rowIndex = document.getElementById(createdat).innerHTML;
	
	/*table.addEventListener("click", function() {
       
       var row = tab1.rows[rowIndex];
       for (var i = 0; i < row.cells.length; i++) {
          console.log("Cell " + i + ": " + row.cells[i].innerHTML);
       }
     }); */
	
    
	if (editBtn == "Edit") {
		editbutton.className = "btn btn-primary"
		var rowLength = table.rows.length;
		for (i = 0; i < rowLength - 1; i++) {
			var oCells = table.rows.item(i).cells;
			var cellLength = oCells.length;
			for (var j = 0; j < cellLength; j++) {
				if (j == 0) {
					oCells[j].getElementsByTagName('textarea')[0].disabled = false;
				} else {
					if (oCells[j].textContent == "")
						oCells[j].getElementsByTagName('input')[0].disabled = false;
				}
			}
		}
		let cnclId = cancel_id + idSpl[1]
		
		document.getElementById(id).innerHTML = "save"
		document.getElementById(id).style = "display:inline-block;margin-left:-50%"
		document.getElementById(cnclId).style = "display:inline-block;margin-left:-45%"
		document.getElementById(Addbtn).style = "display:inline-block;margin-left:25%"
		document.getElementById(Delbtn).style = "display:inline-block;margin-left:25%" 
		

	} else {
	
		var rowLength = table.rows.length;
		var subDataValue = ""
		for (i = 0; i < rowLength - 1; i++) {
			var oCells = table.rows.item(i).cells;
			var cellLength = oCells.length;
			for (var j = 0; j < cellLength; j++) {
				var cellVal = ""
				if (j == 0) {
					cellVal = oCells[j].getElementsByTagName('textarea')[0].value;
				} else {
					if (j != cellLength - 1) {
						cellVal = oCells[j].getElementsByTagName('input')[0].value;
					} else {
						if (oCells[j].textContent == "") {
							if (oCells[j].getElementsByTagName('input')[0].checked) {
								cellVal = "on"
							} else {
								cellVal = ""
							}
						} else {
							cellVal = "on"
						}
					}
				}
				if (cellVal != undefined) {
					if (j == cellLength - 1) {
						subDataValue = subDataValue += cellVal + "#";
					} else {
						subDataValue = subDataValue += cellVal + "/";
					}
				} else {
					break;
				}
			}
		}
		var formData = new FormData();
		formData.append('action', subDataValue)
		formData.append('id', rowIndex)
		let ehr = new XMLHttpRequest();
		ehr.open('POST', baseUrl + '/getUpdatedActionPoint', true);
		ehr.send(formData)

		ehr.onreadystatechange = function() {

			if (ehr.readyState == 4) {
				var respdata = ehr.responseText;
				if (respdata == "success") {
					Toastify({
						text: 'Action Point Updated successfully',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "#90EE90",
							color: 'black',
						},
					}).showToast();
					document.getElementById('tableDynamic').innerHTML = "";
					getSummaryDtl(department)
				} else {
					Toastify({
						text: 'Please Try Again',
						duration: 3000,
						newWindow: true,
						close: true,
						gravity: "top",
						position: "center",
						stopOnFocus: true,
						style: {
							background: "red",
							color: 'white',
						},
					}).showToast();
				}
			}
		}
		var rowLength = table.rows.length;
		var subDataValue = ""
		for (i = 0; i < rowLength - 1; i++) {
			var oCells = table.rows.item(i).cells;
			var cellLength = oCells.length;
			for (var j = 0; j < cellLength; j++) {
				if (j == 0) {
					oCells[j].getElementsByTagName('textarea')[0].disabled = true;
				} else {
					if (oCells[j].textContent == "")
						oCells[j].getElementsByTagName('input')[0].disabled = true;
				}
			}
		}
		document.getElementById(id).innerHTML = "Edit"
		document.getElementById(id).className = "btn btn-danger"
		document.getElementById(Addbtn).style = "display:none"
		

	}

}