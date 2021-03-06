var table = document.getElementById("orderTable");

//$("document").ready(function(){
//	var chartWidth = $(".chart").width();
//	alert("chartWidth: " + chartWidth);
//});

function getStoreSalesRankD(date) {
	var xhr = new XMLHttpRequest();
	xhr.open("GET","../SalesRankD.json?date=" + date + "&id=" + id, true);
	xhr.send();
	var rank = 0;
	var prevTotal = null;
	
	xhr.onreadystatechange = function(){
		if(xhr.readyState == 4 && xhr.status == 200){
			//alert(xhr.responseText);
			var dailySales = JSON.parse(xhr.responseText);
			table.innerHTML = "<tr><th>銷售排行</th><th>餐點名稱</th><th>餐點單價</th><th>銷售數量</th><th>銷售總額</th></tr>";
//			var data =[];
//			var maxTotal = dailySales[0].item_price * dailySales[0].item_amount;
			for(var i = 0; i < dailySales.length; i++){
				if(i == 0){
					var maxTotal = dailySales[i].item_price * dailySales[i].item_amount;
				}
				var total = dailySales[i].item_price * dailySales[i].item_amount;

//				塞銷售總額給data陣列, 直接塞json給d3後用不到了
//				data.push(total);
				
				//塞資料給order table
				var tr = document.createElement("tr");
				
				var td1 = document.createElement("td");
				td1.textContent = getRank(total);
				
				var td2 = document.createElement("td");
				td2.setAttribute("nowrap", "");
				td2.textContent = dailySales[i].item_name;
				
				var td3 = document.createElement("td");
				td3.textContent = dailySales[i].item_price;
				
				var td4 = document.createElement("td");
				td4.textContent = dailySales[i].item_amount;
				
				var td5 = document.createElement("td");
				td5.textContent = "$" + total;
				
				tr.appendChild(td1);
				tr.appendChild(td2);
				tr.appendChild(td3);
				tr.appendChild(td4);
				tr.appendChild(td5);
				
				table.appendChild(tr);
				
			}
			//d3畫長條圖
			//data可以直接塞json物件
			rank = 0;
			prevTotal = null;
			d3.select(".chart").selectAll("div").remove();
			d3.select(".chart").selectAll("div").data(dailySales).enter().append("div")
			.style("background-color", function(d){
				switch(getRank(d.item_price * d.item_amount)){
					case 1:
						return "#FFD700";
						break;
					case 2:
						return "#C0C0C0";
						break;
					case 3:
						return "#CD7F32";
						break;
					default:
					
				}
			})
			.style("width", function(d) {
				//限制最大寬度等於chart的寬度, 限制最小寬度等於chart寬度的1/10
				if((d.item_amount * d.item_price / maxTotal) > 0.1){
					return d.item_amount * d.item_price / maxTotal * $(".chart").width() + "px";
				} else{
					return 0.1 * $(".chart").width() + "px";
				}
			}).text(function(d) {
				return d.item_name + ' $' + d.item_amount * d.item_price;
			}).attr("font-family", "Tahoma").attr("font-size", "20px");
		}
	}
	//計算排名
	function getRank(curTotal){
		
		if(prevTotal == curTotal){
			return rank;
		} else{
			prevTotal = curTotal;
			return rank += 1;
		}
	}
}