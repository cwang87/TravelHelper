window.onload = function () {

 var oTable = document.getElementById("reviews");
 var oTbody = oTable.tBodies[0];
 var DataRows = oTbody.rows;
 var oTRs = new Array();
 
 var oBtn = document.getElementById('sort');
 
 oBtn.onclick = function () {
	 for(var i=0; i<DataRows.length; i++){
		 oTRs.push(DataRows[i]);
	 }
         
     //数组根据cells[0].innerHTML来排序
     arr.sort(function (td1, td2){
         if(isAsc) {
             return parseInt(td1.cells[0].innerHTML) - parseInt(td2.cells[0].innerHTML);
             } else {
                 return parseInt(td2.cells[0].innerHTML) - parseInt(td1.cells[0].innerHTML);
                 }
         
         });
     //把排序后的tr 重新插入tbody
     for(var j =0; j < arr.length; j++ ) {
         oTbody.appendChild(arr[j]);
         }
     //判断升序，降序
     isAsc = !isAsc;
     } 
 
 
}
 



 function getCompareTo(iCol){
 	return function compareTRs(oTR1, oTR2){
 		var sValue1 = oTR1.cells[iCol].firstChild.nodeValue;
 		var svalue2 = oTR2.cells[iCol].firstChild.nodeValue;
 		
 		return sValue1.localCompare(sValue2);
 		//string本身有localCompare方法
 	}
 	
 var compareTRs = getCompareTo(0);	//对第0列排序	
 var compareTRs1 = getCompareTo(1);	//对第1列排序
 	
 	
 }

 function convert(sValue, sDataType){
		switch(sDataType){
			case"int": return parseInt(sValue);
			case"float": return parseFloate(sValue);
			case"date": return Date.parse(sValue);
			default: return sValue.toString();
		}
 }
 
 }





