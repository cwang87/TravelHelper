// comparision function generator: user function closure to return a comparision function
 function generateCompareTo(iCol, sDataType){
 	return function compareTRs(oTR1, oTR2){
 		var sValue1 = convert(oTR1.cells[iCol].firstChild.nodeValue.sDataType);
 		var svalue2 = convert(oTR2.cells[iCol].firstChild.nodeValue.sDataType);
 		
 		if(sValue1 < sValue2){
 			return -1;
 		}else if (sValue1 > sValue2){
 			return 1;
 		}else{
 			return 0;
 		}
 }
 
 
 
 // data type converter
 function convert(sValue, sDataType){
		switch(sDataType){
			case"int": return parseInt(sValue);
			case"float": return parseFloate(sValue);
			case"date": return Date.parse(sValue);
			default: return sValue.toString();
		} 
 }
 

 function sortTable(sTableId, colindex, sDataType){
	 
	 var oTable = document.getElementById("reviews");
	 var oTbody = oTable.tBodies[0];
	 var colDataRows = oTbody.rows;
	 var oTRs = new Array();
	 for (var i=0; i<colDataRows.length; i++){
		 oTRs.push(colDataRows[i]);
	 }
	 
	 oTRs.sort(generateCompareTo(colindex));
	 //oTRs.reverse()
	 
	 var oFragment = document.createDocumentFragment();
	 for(var i=0; i<oTRs.length; i++){
		 oFragment.appendChild(oTRs[i]);
	 }
	 
	 oTbody.appendChild(oFragment);
 }










