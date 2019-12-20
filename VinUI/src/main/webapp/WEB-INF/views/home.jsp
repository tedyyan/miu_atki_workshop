<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<head>
<title>MyWebProject</title>
<meta charset="utf-8" />
<link type="text/css" href="https://ajax.aspnetcdn.com/ajax/jquery.ui/1.8.19/themes/vader/jquery-ui.css"
	rel="stylesheet" />
<script type="text/javascript" src="http://code.jquery.com/jquery-1.8.3.js"></script>
<script type="text/javascript">

	
	
	function search(){  
		var vinNum = $("#vinnum123").val();
    	$("#vinnum123").attr("disabled", true);
    	$("#searchVin").attr("disabled", true);
    	  $("#vinDetail tr:not(#title)").remove();		
  	  $("#makerList tr:not(#title)").remove();		
  	  $("#modelList tr:not(#title)").remove();	
        $.ajax({  
            type:"GET",  
            //curl -X GET "http://localhost:3188/refreshToken/{vin}?vin=5UXWX7C5*BA" -H "accept: */*"
            //url: "http://localhost:3188/vinchecker/{vin}?vin=" + vinNum,  
            url: "http://localhost:8060/vinchecker/vinchecker/{vin}?vin=" + vinNum,  
            dataType:"json",  
            global:false,   
            error: function(data){
            	$("#vinnum123").attr("disabled", false);
            	$("#searchVin").attr("disabled", false);
            	alert("No records!");
            },   
            timeout: function(data){
            	$("#vinnum123").attr("disabled", false);
            	$("#searchVin").attr("disabled", false);
            	
            },
            success: function(data){  		
            	$("#vinnum123").attr("disabled", false);
            	$("#searchVin").attr("disabled", false);
        	  $.each(data.vinDetail,function(index,item){
        		  $("#vinDetail").append(
        				  "<tr><td bgcolor='#FFFFCC'>" + item.BodyClass + " &nbsp;</td>"
        				  + "<td >"  + item.VehicleType  + " &nbsp;</td>"
                          + "<td bgcolor='#FFFFCC'>" + item.Manufacturer   + " &nbsp;</td>"
        				  + "<td >"  + item.PlantCity  + "&nbsp;</td>"
                          + "<td bgcolor='#FFFFCC'>" + item.PlantCountry   + " &nbsp;</td>"
        				  + "</tr>");

        			document.cookie = "cityname="+ item.PlantCity;
        	  });
        	  
        	  	  
        	  $.each(data.allMakers,function(index,item){
        		  $("#makerList").append(
        				  "<tr><td bgcolor='#FFFFCC'>" + item.MakeName + "</td>"
        				  + "<td bgcolor='#FFFFCC'>"  + item.VehicleTypeId  + "</td>"
                          + "<td>" + item.VehicleTypeName   + "</td>"
        				  + "</tr>");
        	  });
        	    
        	  $.each(data.allModels,function(index,item){
        		  $("#modelList").append(
        				  "<tr><td bgcolor='#FFFFCC'>" + item.Make_ID + "</td>"
        				  + "<td bgcolor='#FFFFCC'>"  + item.Make_Name  + "</td>"
                          + "<td>" + item.Model_Name   + "</td>"
        				  + "</tr>");
        	  });
            }
        });
	}
        
        
</script>
</head>

<body>

Please input Vin Number:
<input name = "vinnum123" id = "vinnum123" type="text"/>
<a href="./map">goto map wowo!</a>
<br>
<input type="button" id="searchVin" name = "searchVin" value="search" onClick="search();">
<br>
<p>&nbsp;</p>
<table id="vinDetail" width="90%" border="1" align="center" cellspacing="1">
  <tr id="title">
    <td width="20%" bgcolor="lightblue"><div align="center">
      <div align="left">BodyClass</div>
    </div></td>
    <td width="20%" bgcolor="lightblue"><div align="center">
      <div align="left">VehicleType</div>
    </div></td>
    <td width="20%" bgcolor="lightblue"><div align="center">
      <div align="left">Manufacturer</div>
    </div></td>
    <td width="20%" bgcolor="lightblue"><div align="center">
      <div align="left">PlantCity</div>
    </div></td>
    <td width="20%" bgcolor="lightblue"><div align="center">
      <div align="left">PlantCountry</div>
    </div></td>
  </tr>      
</table>

<p>&nbsp;</p>
<p>makerList:</p>
<table id="makerList" width="90%" border="1" align="center" cellspacing="1">
  <tr id="title">
    <td width="100" bgcolor="lightblue"><div align="center">
      <div align="left">MakeName</div>
    </div></td>
    <td width="200" bgcolor="lightblue"><div align="center">
      <div align="left">VehicleTypeId</div>
    </div></td>
    <td width="80" bgcolor="lightblue"><div align="center">
      <div align="left">VehicleTypeName</div>
    </div></td>
  </tr>      
</table>

<p>&nbsp;</p>
<p>modelList:</p>
<table id="modelList" width="90%" border="1" align="center" cellspacing="1">
  <tr id="title">
    <td width="100" bgcolor="lightblue"><div align="center">
      <div align="left">Make_ID</div>
    </div></td>
    <td width="200" bgcolor="lightblue"><div align="center">
      <div align="left">Make_Name</div>
    </div></td>
    <td width="80" bgcolor="lightblue"><div align="center">
      <div align="left">Model_Name</div>
    </div></td>
  </tr>      
</table>

</body>

</html>
