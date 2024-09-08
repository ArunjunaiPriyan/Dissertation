var chartBar;
var frActionData;
var configPie;
var Interval;
var Intervaltable;
var multiLineChart;
var chartperform;
var current_page = 1;
var records_per_page = 15;
var fileToUpload;
var picToUpload;

var findCurrentURL = window.location;
var baseUrl = findCurrentURL.protocol + "//" + findCurrentURL.host + "/" + findCurrentURL.pathname.split('/')[1];

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

function numPages() {
	var l = document.getElementById("tableDynamic").rows.length
	return Math.ceil((l - 1) / records_per_page);
}

function logout() {
	window.location.href = "index.html";
	deleteCookie('login')
}

function BackToHome() {
	history.pushState({ state: 'someElement was clicked' }, 'Magna', 'main.html');
}

function chartContent() {
	if (document.cookie.split(';').length < 3) {
		logout()
	}
	var userName = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8)
	if (userName !== "Admin") {
		document.getElementById("adminsec").style.display = "none";
	}
	if (getCookie('login') != undefined && getCookie('login') != null && getCookie('login') != "" && document.cookie.split(';').length != 0) {
		document.getElementById("homeBtn").style.display = 'block'
		var deptslt = document.getElementById("department").value
		var filterMonth = document.getElementById("month").value
		var section = document.getElementById("sectionDropDown").value
		let encryptUsername = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8);
		var logoutbtn = document.getElementById("logout")
		logoutbtn.title = encryptUsername;

		var datasetOpen = [];
		var datasetPending = [];
		var datasetClosed = [];
		var datasetOverdue = [];
		let putZero = 0
		var formData = new FormData();
		formData.append('dept', deptslt)
		formData.append('month', filterMonth)
		formData.append('section', section)
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/getFilteredOverallDetails', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {
			var respdata;
			if (xhr.readyState == 4) {
				respdata = JSON.parse(xhr.responseText);
				if (respdata.open != null && respdata.open != "" && respdata.open != undefined) {
					var data = respdata.open;
					if (data.Maintenance != undefined) { datasetOpen.push(data.Maintenance); } else { datasetOpen.push(putZero); }
					if (data.ME != undefined) { datasetOpen.push(data.ME); } else { datasetOpen.push(putZero); }
					if (data.Production != undefined) { datasetOpen.push(data.Production); } else { datasetOpen.push(putZero); }
					if (data.Quality != undefined) { datasetOpen.push(data.Quality); } else { datasetOpen.push(putZero); }
					if (data.Logistic_FG != undefined) { datasetOpen.push(data.Logistic_FG); } else { datasetOpen.push(putZero); }
					if (data.Logistic_RM != undefined) { datasetOpen.push(data.Logistic_RM); } else { datasetOpen.push(putZero); }
					if (data.HR != undefined) { datasetOpen.push(data.HR); } else { datasetOpen.push(putZero); }
					if (data.Safety != undefined) { datasetOpen.push(data.Safety); } else { datasetOpen.push(putZero); }
					if (data.IT != undefined) { datasetOpen.push(data.IT); } else { datasetOpen.push(putZero); }
					if (data.Engineering != undefined) { datasetOpen.push(data.Engineering); } else { datasetOpen.push(putZero); }
					if (data.Finance != undefined) { datasetOpen.push(data.Finance); } else { datasetOpen.push(putZero); }
					if (data.Purchase != undefined) { datasetOpen.push(data.Purchase); } else { datasetOpen.push(putZero); }
					if (data.Program_Management != undefined) { datasetOpen.push(data.Program_Management); } else { datasetOpen.push(putZero); }
					if (data.Sales != undefined) { datasetOpen.push(data.Sales); } else { datasetOpen.push(putZero); }
					if (data.SDE != undefined) { datasetOpen.push(data.SDE); } else { datasetOpen.push(putZero); }
					if (data._5S != undefined) { datasetOpen.push(data._5S); } else { datasetOpen.push(putZero); }

				}
				if (respdata.pending != null && respdata.pending != "" && respdata.pending != undefined) {
					var data = respdata.pending;
					if (data.Maintenance != undefined) { datasetPending.push(data.Maintenance); } else { datasetPending.push(putZero); }
					if (data.ME != undefined) { datasetPending.push(data.ME); } else { datasetPending.push(putZero); }
					if (data.Production != undefined) { datasetPending.push(data.Production); } else { datasetPending.push(putZero); }
					if (data.Quality != undefined) { datasetPending.push(data.Quality); } else { datasetPending.push(putZero); }
					if (data.Logistic_FG != undefined) { datasetPending.push(data.Logistic_FG); } else { datasetPending.push(putZero); }
					if (data.Logistic_RM != undefined) { datasetPending.push(data.Logistic_RM); } else { datasetPending.push(putZero); }
					if (data.HR != undefined) { datasetPending.push(data.HR); } else { datasetPending.push(putZero); }
					if (data.Safety != undefined) { datasetPending.push(data.Safety); } else { datasetPending.push(putZero); }
					if (data.IT != undefined) { datasetPending.push(data.IT); } else { datasetPending.push(putZero); }
					if (data.Engineering != undefined) { datasetPending.push(data.Engineering); } else { datasetPending.push(putZero); }
					if (data.Finance != undefined) { datasetPending.push(data.Finance); } else { datasetPending.push(putZero); }
					if (data.Purchase != undefined) { datasetPending.push(data.Purchase); } else { datasetPending.push(putZero); }
					if (data.Program_Management != undefined) { datasetPending.push(data.Program_Management); } else { datasetPending.push(putZero); }
					if (data.Sales != undefined) { datasetPending.push(data.Sales); } else { datasetPending.push(putZero); }
					if (data.SDE != undefined) { datasetPending.push(data.SDE); } else { datasetPending.push(putZero); }
					if (data._5S != undefined) { datasetPending.push(data._5S); } else { datasetPending.push(putZero); }

				}
				if (respdata.closed != null && respdata.closed != "" && respdata.closed != undefined) {
					var data = respdata.closed;
					if (data.Maintenance != undefined) { datasetClosed.push(data.Maintenance); } else { datasetClosed.push(putZero); }
					if (data.ME != undefined) { datasetClosed.push(data.ME); } else { datasetClosed.push(putZero); }
					if (data.Production != undefined) { datasetClosed.push(data.Production); } else { datasetClosed.push(putZero); }
					if (data.Quality != undefined) { datasetClosed.push(data.Quality); } else { datasetClosed.push(putZero); }
					if (data.Logistic_FG != undefined) { datasetClosed.push(data.Logistic_FG); } else { datasetClosed.push(putZero); }
					if (data.Logistic_RM != undefined) { datasetClosed.push(data.Logistic_RM); } else { datasetClosed.push(putZero); }
					if (data.HR != undefined) { datasetClosed.push(data.HR); } else { datasetClosed.push(putZero); }
					if (data.Safety != undefined) { datasetClosed.push(data.Safety); } else { datasetClosed.push(putZero); }
					if (data.IT != undefined) { datasetClosed.push(data.IT); } else { datasetClosed.push(putZero); }
					if (data.Engineering != undefined) { datasetClosed.push(data.Engineering); } else { datasetClosed.push(putZero); }
					if (data.Finance != undefined) { datasetClosed.push(data.Finance); } else { datasetClosed.push(putZero); }
					if (data.Purchase != undefined) { datasetClosed.push(data.Purchase); } else { datasetClosed.push(putZero); }
					if (data.Program_Management != undefined) { datasetClosed.push(data.Program_Management); } else { datasetClosed.push(putZero); }
					if (data.Sales != undefined) { datasetClosed.push(data.Sales); } else { datasetClosed.push(putZero); }
					if (data.SDE != undefined) { datasetClosed.push(data.SDE); } else { datasetClosed.push(putZero); }
					if (data._5S != undefined) { datasetClosed.push(data._5S); } else { datasetClosed.push(putZero); }

				}
				if (respdata.overDue != null && respdata.overDue != "" && respdata.overDue != undefined) {
					var data = respdata.overDue;
					if (data.Maintenance != undefined) { datasetOverdue.push(data.Maintenance); } else { datasetOverdue.push(putZero); }
					if (data.ME != undefined) { datasetOverdue.push(data.ME); } else { datasetOverdue.push(putZero); }
					if (data.Production != undefined) { datasetOverdue.push(data.Production); } else { datasetOverdue.push(putZero); }
					if (data.Quality != undefined) { datasetOverdue.push(data.Quality); } else { datasetOverdue.push(putZero); }
					if (data.Logistic_FG != undefined) { datasetOverdue.push(data.Logistic_FG); } else { datasetOverdue.push(putZero); }
					if (data.Logistic_RM != undefined) { datasetOverdue.push(data.Logistic_RM); } else { datasetOverdue.push(putZero); }
					if (data.HR != undefined) { datasetOverdue.push(data.HR); } else { datasetOverdue.push(putZero); }
					if (data.Safety != undefined) { datasetOverdue.push(data.Safety); } else { datasetOverdue.push(putZero); }
					if (data.IT != undefined) { datasetOverdue.push(data.IT); } else { datasetOverdue.push(putZero); }
					if (data.Engineering != undefined) { datasetOverdue.push(data.Engineering); } else { datasetOverdue.push(putZero); }
					if (data.Finance != undefined) { datasetOverdue.push(data.Finance); } else { datasetOverdue.push(putZero); }
					if (data.Purchase != undefined) { datasetOverdue.push(data.Purchase); } else { datasetOverdue.push(putZero); }
					if (data.Program_Management != undefined) { datasetOverdue.push(data.Program_Management); } else { datasetOverdue.push(putZero); }
					if (data.Sales != undefined) { datasetOverdue.push(data.Sales); } else { datasetOverdue.push(putZero); }
					if (data.SDE != undefined) { datasetOverdue.push(data.SDE); } else { datasetOverdue.push(putZero); }
					if (data._5S != undefined) { datasetOverdue.push(data._5S); } else { datasetOverdue.push(putZero); }

				}
				if (respdata.overallClosed != null && respdata.overallClosed != "" && respdata.overallClosed != undefined
					&& respdata.overallOpen != null && respdata.overallOpen != "" && respdata.overallOpen != undefined
					&& respdata.overallTrend != null && respdata.overallTrend != "" && respdata.overallTrend != undefined
					&& respdata.TotaloverDue != null && respdata.TotaloverDue != "" && respdata.TotaloverDue != undefined) {
					var dataClosed = respdata.overallClosed;
					var dataopen = respdata.overallOpen;
					var dataTrend = respdata.overallTrend
					var datatotalDue = respdata.TotaloverDue

					if (chartBar != undefined) {
						chartBar.destroy();
						chartBar = null;
					}
					if (configPie != undefined) {
						configPie.destroy();
						configPie = null;
					}
					if (multiLineChart != undefined) {
						multiLineChart.destroy();
						multiLineChart = null;
					}
					if (chartperform != undefined) {
						chartperform.destroy();
						chartperform = null;
					}
					loadchart(datasetOpen, datasetClosed, datasetOverdue, datasetPending)
					loadPieChart(dataClosed.TotalClosed, dataopen.TotalOpen, datatotalDue)
					trendLineChart(dataTrend)
					topPerformChart(respdata.overallBest)
				}
			}

		}
	} else {
		window.location.href = "index.html";
	}
}

function loadchart(dataOpen, dataClosed, datasetOverdue, datasetPending) {
	var overallchart = document.getElementById("ctx")
	overallchart.style.height = "9%"
	chartBar = new Chart(ctx, {
		type: 'bar',
		data: {
			labels: ['Maintenance', 'ME', 'Production', 'Quality', 'Logistic_FG', 'Logistic_RM', 'HR', 'Safety', 'IT', 'Engineering', 'Finance', 'Purchase', 'Program Management', 'Sales', 'SDE', '5S'],
			// create 12 datasets, since we have 12 items
			// data[0] = labels[0] (data for first bar - 'Standing costs') | data[1] = labels[1] (data for second bar - 'Running costs')
			// put 0, if there is no data for the particular bar

			datasets: [{
				label: 'Open',
				data: dataOpen,
				backgroundColor: '#b82e2e',
			},
			{
				label: 'Closed',
				data: dataClosed,
				backgroundColor: '#66aa00',
			}, {
				label: 'Overdue',
				data: datasetOverdue,
				backgroundColor: '#FFA500',
			}, {
				label: 'Pending List',
				data: datasetPending,
				backgroundColor: '#FFFF00',
			},]
		},
		options: {
			responsive: true,
			legend: {
				position: 'right' // place legend on the right side of chart
			},
			scales: {
				xAxes: [{
					stacked: true // this should be set to make the bars stacked
				}],
				yAxes: [{
					stacked: true // this also..
				}]
			}
		}
	});

}

function loadPieChart(dataclosed, dataOpen, datatotalDue) {
	var dataArr
	var labl
	if (datatotalDue.TotalOverdue == 0) {
		dataArr = [dataclosed, dataOpen]
		labl = ['Closed', 'Open']
	} else {
		dataArr = [dataclosed, dataOpen, datatotalDue.TotalOverdue]
		labl = ['Closed', 'Open', 'OverDue']
	}

	if (dataclosed > 0 && dataOpen > 0) {
		document.getElementById("mychartnodataimg").style.display = "none"
		configPie = new Chart(myChart, {
			plugins: [ChartDataLabels],
			type: 'doughnut',
			data: {
				labels: labl,
				indexLabelPlacement: "outside",
				datasets: [{
					data: dataArr,
					backgroundColor: [
						'rgb(255, 99, 132)',
						'rgb(54, 162, 235)',
						'rgb(255, 205, 86)'
					],
					hoverOffset: 4,
				}]
			},
			options: {
				legend: {
					position: 'bottom'
				},
				responsive: true,
				plugins: {
					datalabels: {
						color: 'white',
						textAlign: 'center',
						formatter: function(value, ctx) {
							var index = ctx.dataIndex;
							var label = ctx.chart.data.labels[index];
							if (value < 1) return ''
							return label + '\n' + value;
						}
					}
				}
			}
		});
	} else {
		document.getElementById("mychartnodataimg").style.display = "block"
	}
}

function trendLineChart(trend) {
	var trnData = Object.values(trend);
	var canvas = document.getElementById("myLineChart");
	var ctx = canvas.getContext('2d');

	var data = {
		labels: ["January", "February", "March", "April", "May", "June", "July", "Augest", "September", "October", "November", "December"],
		datasets: [{
			label: "Overall",
			"fill": true,
			data: trnData,
			borderColor: "rgba(100,100,100,0.8)",
			backgroundColor: "rgba(120,120,120,0.5)"
		}]
	};

	var options = {
		responsive: true,
		//maintainAspectRatio: false,
		scales: {
			yAxes: [{
				display: true,
				position: 'right',
				ticks: {
					beginAtZero: true
				}
			}, {
				display: true,
				position: 'left',
				ticks: {
					beginAtZero: true,
					max: 45,
					min: 0,
					stepSize: 5
				}
			}]
		},
		tooltips: {
			enabled: true,
			mode: 'label'
		},
		legend: {
			display: true,
		},
		plugins: {
			datalabels: {
				color: 'black',
				textAlign: 'center',
				formatter: function(value, ctx) {
					var index = ctx.dataIndex;
					var label = ctx.chart.data.labels[index];
					return label + '\n' + value;
				}
			}
		}
	};

	// Chart declaration:
	multiLineChart = new Chart(ctx, {
		plugins: [ChartDataLabels],
		width: "100",
		type: 'line',
		data: data,
		options: options
	});
}

function topPerformChart(dataset) {

	var labels;
	var dataValue;

	var isEmpty = true;

	for (var key in dataset) {
		if (dataset.hasOwnProperty(key)) {
			isEmpty = false;
			break;
		}
	}
	if (isEmpty == false) {

		document.getElementById("nodataimg").style.display = "none"
		labels = Object.keys(dataset)
		dataValue = Object.values(dataset)
		chartperform = new Chart(topPerfm, {
			plugins: [ChartDataLabels],
			type: 'pie',
			data: {
				labels: labels,
				datasets: [{
					data: dataValue,
					backgroundColor: ['#ff9800', '#e91e63', '#2196f3']
				}]
			},
			labels: { labels },
			options: {
				responsive: true,
				//maintainAspectRatio: false,
				plugins: {
					datalabels: {
						color: "black",
						textAlign: 'center',
						formatter: function(value, ctx) {
							var index = ctx.dataIndex;
							var label = ctx.chart.data.labels[index];
							return label + '\n' + value;
						}
					}
				}
			}
		});
	} else {
		document.getElementById("nodataimg").style.display = "block"
	}
}

function deptSelect() {
	chartContent()
}

function mntSelect() {
	chartContent()
}

function sectionSelect() {
	chartContent()
}

function adminSectionChange(i) {
	var fairAcdata = frActionData;
	let frActArr = [];
	var sectionval = document.getElementById("adminsection");
	var sectiontextval = sectionval.options[sectionval.selectedIndex].text;
	if (i === '1') {
		frActionData.map((frActdata) => {
			if (frActdata.com_section === sectiontextval) {
				frActArr.push(frActdata)
			} else if (sectiontextval === "GET ALL") {
				frActArr.push(frActdata)
			}
			return frActArr;
		})
		frActionData = frActArr;
	}


	var userName = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8)
	var table = document.getElementById('tableDynamic');
	table.innerHTML = "";
	var thead = document.createElement('thead');
	var tbody = document.createElement('tbody');
	var tr = document.createElement('tr');

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Created Date");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Type");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Section");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Section Creator");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Action Point");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Responsibility");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Target Date");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Closed Date");
	th.appendChild(text);
	tr.appendChild(th);

	var th = document.createElement('th');
	th.style = "text-align:center;color:white;background-color:black";
	var text = document.createTextNode("Status");
	th.appendChild(text);
	tr.appendChild(th);

	thead.appendChild(tr);
	table.appendChild(thead);
	if (frActionData.length == 0) {
		var dataNA = document.createTextNode("NO DATA");
		var td = document.createElement('td');
		td.colSpan = 9
		td.style = "font-weight: bold;text-align:center;"
		td.appendChild(dataNA);
		tbody.appendChild(td);
		table.appendChild(tbody);
	}
	for (var i = 0; i < frActionData.length; i++) {
		var tabData = frActionData[i];
		if (tabData.target_date != "fvs") {
			var tr = document.createElement('tr');
			var td = document.createElement('td');
			td.id = "zoom"
			var dtSplit = tabData.date.split("-")
			let setdate = dtSplit[2] + "-" + dtSplit[1] + "-" + dtSplit[0]
			var text = document.createTextNode(setdate);
			if (tabData.isactive == 1) {
				if (userName !== "Admin") {
					td.onclick = function(e) { getValue(e) };
					td.style = "cursor:pointer"
				}

			} else {
				td.onclick = function() { getClosedAlert() };
			}
			td.appendChild(text);
			td.style.textAlign = "center"
			tr.appendChild(td);

			var td = document.createElement('td');
			var text = document.createTextNode(tabData.sec_type);
			td.appendChild(text);
			td.style.textAlign = "center"
			tr.appendChild(td);

			var td = document.createElement('td');
			var text = document.createTextNode(tabData.com_section);
			td.appendChild(text);
			td.style.textAlign = "center"
			tr.appendChild(td);

			var td = document.createElement('td');
			var text = document.createTextNode(tabData.sec_owner);
			td.appendChild(text);
			td.style.textAlign = "center"
			tr.appendChild(td);

			var td = document.createElement('td');
			td.style = "width:40%;";

			var text = ""

			const hasPointerMark = tabData.actionPoint.includes("#");
			if (hasPointerMark) {
				let SplitByPointer = tabData.actionPoint.split("#");
				let actionPointer = SplitByPointer.length - 1
				const tableSub = document.createElement('table');
				tableSub.id = "table_" + i
				for (let k = 0; k <= actionPointer; k++) {
					const trSub = document.createElement('tr');
					trSub.id = "subTable"
					if (k != actionPointer) {
						const pointToDevide = SplitByPointer[k];
						let SplitPointer = pointToDevide.split("/");
						var td1 = document.createElement('td');
						td1.style = "width:250px;background-color:transparent"
						var text1 = document.createTextNode(SplitPointer[0]);
						text1.style = "border:none; width:100%"
						td1.appendChild(text1);
						trSub.appendChild(td1);
						var td2 = document.createElement('td');
						td2.style = "width:100px;background-color:transparent"
						var text2 = document.createTextNode(SplitPointer[1]);
						text2.style = "border:none; width:100%"
						td2.appendChild(text2);
						trSub.appendChild(td2);
						var td3 = document.createElement('td');
						td3.style = "width:100px;background-color:transparent"
						var text3 = document.createTextNode(SplitPointer[2]);
						text3.style = "border:none; width:100%"
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
							text4 = document.createElement('span')
							text4.className = 'material-icons';
							var symbol = document.createTextNode('close');
							text4.appendChild(symbol);
						}
						text4.disabled = true
						td4.appendChild(text4);
						trSub.appendChild(td4);
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

			var text = document.createTextNode(tabData.responsibility);

			td.appendChild(text);
			td.style.textAlign = "center"
			tr.appendChild(td);

			var td = document.createElement('td');
			if (tabData.target_date != null) {
				var dtSplitTarget = tabData.target_date.split("-")
				var setdateTar = dtSplitTarget[2] + "-" + dtSplitTarget[1] + "-" + dtSplitTarget[0]
				var text = document.createTextNode(setdateTar);
				td.style.textAlign = "center"
				td.appendChild(text);
			} else {
				var text = document.createTextNode("-");
				td.style.textAlign = "center"
				td.appendChild(text)
			}
			tr.appendChild(td);

			var td = document.createElement('td');
			if (tabData.closed_date != null) {
				var dtSplitTarget = tabData.closed_date.split("-")
				var setdateTar = dtSplitTarget[2] + "-" + dtSplitTarget[1] + "-" + dtSplitTarget[0]
				var text = document.createTextNode(setdateTar);
				td.style.textAlign = "center"
				td.appendChild(text);
			} else {
				var text = document.createTextNode("-");
				td.style.textAlign = "center"
				td.appendChild(text)
			}
			tr.appendChild(td);

			var td = document.createElement('td');
			let statusValue;
			if (tabData.isactive == 1) {
				statusValue = "Open"
				td.style = "color:red;font-weight:bold";
			} else if (tabData.isactive == 0) {
				statusValue = "Closed"
				td.style = "color:green;font-weight:bold";
			} else {
				statusValue = "Pending for Approval / Rejected"
				td.style = "color:yellow;font-weight:bold";
			}
			var text = document.createTextNode(statusValue);
			td.appendChild(text);
			tr.appendChild(td);

			tbody.appendChild(tr);
			table.appendChild(tbody);
			changePage(current_page);
		}
	}
	frActionData = fairAcdata;
}

function mainDisFunction() {

	if (CryptoJS.AES.decrypt(getCookie('user'), "magna").toString(CryptoJS.enc.Utf8) != '0') {
		clearInterval(Intervaltable);
		document.getElementById("body").style.display = "none"
		document.getElementById("nextBlock").style.display = "block"

		var department = CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8);
		var userName = CryptoJS.AES.decrypt(getCookie('userName'), "magna").toString(CryptoJS.enc.Utf8)

		var formData = new FormData();
		if (userName === "Admin") {
			formData.append('dept', "1")
			formData.append('isactive', "GETALL")
			formData.append('userName', userName)
		} else {
			formData.append('dept', department)
			formData.append('isactive', "GETALL")
			formData.append('userName', userName)
		}

		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/getfairactiondata', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {

			if (xhr.readyState == 4) {
				frActionData = JSON.parse(xhr.responseText)
				/*	document.getElementById("tablespinner").style.display = "none"
					document.getElementById("tabspinner").remove();
					document.getElementById("tableDynamic").style.display= "block" */
				if (frActionData.length != 0) {
					if (userName === "Admin") {
						document.getElementById("updatePointList").innerHTML = "Admin";
						document.getElementById("updatePoint").innerHTML = "Admin";
					} else {
						document.getElementById("updatePointList").innerHTML = frActionData[0].department;
						document.getElementById("updatePoint").innerHTML = frActionData[0].department;
					}
					//
					adminSectionChange('0')
				}
			}
		}
	}
}

function backtomain() {
	document.getElementById("searchBox").value = ""
	document.getElementById("body").style.display = "block"
	document.getElementById("nextBlock").style.display = "none"
	document.getElementById("tableDynamic").innerHTML = "";
	document.getElementById("staticDate").value = ""
	document.getElementById("staticResp").value = ""
	document.getElementById("staticPoint").value = ""
	document.getElementById("canvaspic").value = ""
	document.getElementById("formFile").value = ""
	document.getElementById("staticSec").value = ""
	var video = document.getElementById("video");
	video.style.display = "none";
	video.srcObject = null;
	document.getElementById('checkBox').checked = false;
	document.getElementById("file").style.display = "block";
	document.getElementById("camera").style.display = "none";
	document.getElementById("uploadOption").style.display = "none"
	document.getElementById("listOption").style.display = "block"
	clearInterval(Interval);
}


async function opencamare() {
	var video = document.getElementById("video")
	document.getElementById("canvaspic").style.display = "none"
	video.style.display = "block"
	let stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: false });
	video.srcObject = stream;
}
function getpic() {
	var canvas = document.getElementById("canvaspic")
	canvas.style.display = "block"
	var video = document.getElementById("video")
	canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
	let image_data_url = canvas.toDataURL('image/jpeg');
	document.getElementById("video").style.display = "none"
	picToUpload = image_data_url
}  

function toBlob(dataURL) {
	var array, binary, i;
	binary = atob(dataURL.split(',')[1]);
	array = [];
	i = 0;
	while (i < binary.length) {
		array.push(binary.charCodeAt(i));
		i++;
	}
	return new Blob([new Uint8Array(array)], {
		type: dataURL.split(',')[0].split(':')[1].split(';')[0]
	});
}

function getValue(e) {
	document.getElementById("reasonHide").style.display = "none"
	var rowIndex = e.target.parentNode.rowIndex - 1
	var selectedData = frActionData[rowIndex]
	if (selectedData.before != null && selectedData.before != "") {
		var formData = new FormData();
		formData.append('path', selectedData.before)
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/getPathbase64Content', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {
			var respdata;
			if (xhr.readyState == 4) {
				respdata = xhr.responseText;
				if (respdata.length > 0) {
					document.getElementById("beforePic").style.display = "none"
					document.getElementById("imgBefore").style.display = "block"
					document.getElementById("imgBefore").setAttribute('src', respdata);
				}
			}
		}
	} else {
		document.getElementById("beforePic").innerHTML = "No Before Picture Uploaded";
		document.getElementById("beforePic").style = "color:red;font-weight:bold"
	}

	document.getElementById("backbtn").style.display = "none"
	document.getElementById("uploadOption").style.display = "block"
	document.getElementById("listOption").style.display = "none"
	document.getElementById("staticDate").value = moment(selectedData.target_date).format('DD-MMM-YYYY');
	document.getElementById("sid").innerHTML = selectedData.id
	document.getElementById("staticResp").value = selectedData.responsibility
	document.getElementById("staticPoint").value = selectedData.actionPoint
	document.getElementById("staticSec").value = selectedData.com_section
	if (selectedData.reject_remark != null) {
		document.getElementById("reasonHide").style.display = "flex";
		document.getElementById("reasonHide").style = "color:red;font-weight:bold"
		document.getElementById("Remark").value = selectedData.reject_remark;
		document.getElementById("Remark").style = "color:red;font-weight:bold"
	}

}

function cancelUpload() {
	var video = document.getElementById("video");
	video.style.display = "none";
	video.srcObject = null;
	document.getElementById("beforePic").style.display = "none"
	document.getElementById("imgBefore").style.display = "none"
	document.getElementById("staticDate").value = ""
	document.getElementById("staticResp").value = ""
	document.getElementById("staticPoint").value = ""
	document.getElementById("canvaspic").value = ""
	document.getElementById("formFile").value = ""
	document.getElementById("staticSec").value = ""
	document.getElementById("comment").value = ""
	document.getElementById("canvaspic").style.display = "none";
	document.getElementById('checkBox').checked = false;
	document.getElementById("file").style.display = "block";
	document.getElementById("camera").style.display = "none";
	document.getElementById("uploadOption").style.display = "none"
	document.getElementById("listOption").style.display = "block"
	Interval = setInterval(function() {
		video.style.display = "none"; video.srcObject = null, 5000
	});
	clearInterval(Interval);
	document.getElementById("backbtn").style.display = "block"
}
const getImageInfo = (url) => {
	fetch(url)
		.then((response) => {
			if (!response.ok) {
				throw new Error('Failed to fetch image.');
			}
			return response.blob();
		})
		.then((imageBlob) => {
			const imageObject = {
				lastModified: new Date().getTime(),
				name: url.substring(url.lastIndexOf('/') + 1),
				size: imageBlob.size,
				type: imageBlob.type,
			};

			// Now you have the imageObject with required information
			console.log('Image Information:', imageObject);
		})
		.catch((error) => {
			console.error(error.message);
		});
};

function getuploadpic(event) {
/*	var file = event.target.files[0];
	fileToUpload = file
	var reader = new FileReader();
	reader.readAsBinaryString(file);

	reader.onload = function(e) {
		//var data64 = btoa(reader.result);
		var data64 = e.target.result;
		fileToUpload = data64
		console.log(data64)
	};
	reader.onerror = function() {
		console.log('there are some error');
	}; */
	const file = event.target.files[0];

  const reader = new FileReader();
  reader.onload = function (e) {
    const base64Image = e.target.result;
    fileToUpload = base64Image
    console.log(base64Image); // Base64 representation of the image
    // You can perform further operations with the base64Image, such as sending it to a server or displaying it in the UI.
  };
  reader.readAsDataURL(file);
}


function SubmitUpload() {
	var date = document.getElementById("staticDate").value
	var resp = document.getElementById("staticResp").value
	var point = document.getElementById("staticPoint").value
	var pic = document.getElementById("canvaspic").value
	var comment = document.getElementById("comment").value
	var id = document.getElementById("sid").innerHTML
	var dept = CryptoJS.AES.decrypt(getCookie('dept'), "magna").toString(CryptoJS.enc.Utf8)
	let chckbx = document.getElementById('checkBox').value

	//const imageUrl = 'https://thenounproject.com/api/private/icons/2879926/edit/?backgroundShape=SQUARE&backgroundShapeColor=%23000000&backgroundShapeOpacity=0&exportSize=752&flipX=false&flipY=false&foregroundColor=%23000000&foregroundOpacity=1&imageFormat=png&rotation=0';
	//var obj = getImageInfo(imageUrl);
	//const img = new Image();
	if (chckbx != 0 && chckbx != "on") {
		fileToUpload = picToUpload
	}
	var noimage = "data:image/png;base64,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"
  
	if (date != "" && resp != "" && point != "" && comment != "") {
		var formData = new FormData();
		// if (chckbx != 0 && chckbx != "on") {
		if (fileToUpload != undefined) {
			formData.append('file', toBlob(fileToUpload))
			console.log(fileToUpload)
		} else {
			formData.append('file', toBlob(noimage))
		}
		formData.append('dept', dept)
		formData.append('target_date', moment(date).format("YYYY-MM-DD"))
		formData.append('resp', resp)
		formData.append('id', id)
		formData.append('comment', comment)
		// } else {
		//			if(fileToUpload != undefined){
		//				formData.append('file', fileToUpload)
		//			}else{
		//				formData.append('file', toBlob(noimage))
		//			}
		//			formData.append('dept', dept)
		//			formData.append('target_date', moment(date).format("YYYY-MM-DD"))
		//			formData.append('resp', resp)
		//			formData.append('id', id)
		//			formData.append('comment', comment)
		//		}
		let xhr = new XMLHttpRequest();
		xhr.open('POST', baseUrl + '/updateEvidenceFile', true);
		xhr.send(formData)

		xhr.onreadystatechange = function() {

			if (xhr.readyState == 4) {
				var data = xhr.responseText
				if (data == "success") {
					Toastify({
						text: "Successfully Uploaded",
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
					Intervaltable = setInterval(mainDisFunction(), 1000);
					document.getElementById("uploadOption").style.display = "none"
					document.getElementById("listOption").style.display = "block"
					document.getElementById('checkBox').checked = false;
					document.getElementById("file").style.display = "block";
					document.getElementById("camera").style.display = "none";
					document.getElementById("backbtn").style.display = "block"
					document.getElementById("staticDate").value = ""
					document.getElementById("staticResp").value = ""
					document.getElementById("staticPoint").value = ""
					document.getElementById("canvaspic").value = ""
					document.getElementById("formFile").value = ""
					document.getElementById("staticSec").value = ""
					document.getElementById("comment").value = ""
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
														Request for Approval Notification</th>
												</tr>
												<tr>
													<td>Department</td>
													<td>${dept}</td>
												</tr>
												<tr>
													<td>Action Comment</td>
													<td>${comment}</td>
												</tr>
												<tr>
													<td>Target date</td>
													<td>${date}</td>
												</tr>
												<tr>
													<td>Responsible person</td>
													<td>${resp}</td>
												</tr>
												<tr>
													<td>Link</td>
													<td><a href="http://10.215.8.94:8789/magna/main.html">Magna Project Dashboard</a></td>
												</tr>
											</table>
											<img id="imgElem" src=${fileToUpload} style="height:100%;width:100%"></img>
											<img id="noimage" src="./img/noimage.png" alt="Magna-Logo">
										</div>
										<div>
											<br>											<h5>MAGNA AUTOMOTIVE INDIA PVT LTD</h5>
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
						"subject": "Action Point Approval Request Notification"
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




	} else {
		Toastify({
			text: "Please select a file to upload",
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
	var video = document.getElementById("video");
	video.style.display = "none";
	video.srcObject = null;
}

function toggleClick() {
	var file = document.getElementById("file")
	var camera = document.getElementById("camera")
	if (file.style.display == "block") {
		document.getElementById('checkBox').value = 1;
		camera.style.display = "block"
		file.style.display = "none"
	} else {
		camera.style.display = "none"
		file.style.display = "block"
		document.getElementById("video").style.display = "none";
		document.getElementById("canvaspic").style.display = "none";
		document.getElementById('checkBox').value = 0;
	}
}

function performSearch() {
	const searchBox = document.getElementById('searchBox');
	const tabled = document.getElementById("tableDynamic");
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
		mainDisFunction()
	}
}

function getClosedAlert() {
	Toastify({
		text: "Selected Item is Closed",
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



function deptDropwn() {
	var respn = document.getElementById("department");
	respn.innerHTML = ""
	shr = new XMLHttpRequest();
	shr.open('POST', baseUrl + '/getDepartmentDrop', true);
	shr.send()

	shr.onreadystatechange = function() {

		if (shr.readyState == 4) {

			var deptList;
			deptList = JSON.parse(shr.responseText)
			var options = document.createElement("option");
			options.text = "GET ALL";
			respn.appendChild(options);
			for (var i = 0; i < deptList.length; i++) {
				var option = document.createElement("option");
				option.value = deptList[i].dept_code;
				option.text = deptList[i].dept;
				respn.appendChild(option);
			}
			sectionDropdown()
		}
	}

}

function sectionDropdown() {
	var secListadmin;
	var secrespnadmin = document.getElementById("adminsection");
	secrespnadmin.innerHTML = ""
	var secrespn = document.getElementById("sectionDropDown");
	secrespn.innerHTML = ""
	shr = new XMLHttpRequest();
	shr.open('POST', baseUrl + '/getSectionList', true);
	shr.send();
	shr.onreadystatechange = function() {
		if (shr.readyState == 4) {

			secListadmin = JSON.parse(shr.responseText)

			var secoptionsadmin = document.createElement("option");
			secoptionsadmin.text = "GET ALL";
			secrespnadmin.appendChild(secoptionsadmin);
			for (var i = 0; i < secListadmin.length; i++) {
				var secoptionadmin = document.createElement("option");
				secoptionadmin.value = secListadmin[i].com_Code;
				secoptionadmin.text = secListadmin[i].com_Name;
				secrespnadmin.appendChild(secoptionadmin);
			}

			var secoptions = document.createElement("option");
			secoptions.text = "GET ALL";
			secrespn.appendChild(secoptions);

			for (var i = 0; i < secListadmin.length; i++) {
				var secoption = document.createElement("option");
				secoption.value = secListadmin[i].com_Code;
				secoption.text = secListadmin[i].com_Name;
				secrespn.appendChild(secoption);
			}

		}
	}


}

