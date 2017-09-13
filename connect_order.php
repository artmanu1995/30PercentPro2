<!DOCTYPE html>
<html>
<head>
	<title>connection</title>
</head>
<body>
	<?php
	header("content-type;text/javascript;charset=utf-8");
	$con=mysql_connect('192.168.1.32','root','57402482','restaurant')or die(mysql_error());
	mysql_select_db('restaurant')or die(mysql_error());
	mysql_query("SET NAMES UTF8");
	if (isset($_POST)) {
		if ($_POST['isAdd'] = 'true') {
		 	$strOfficer = $_POST['officer'];
		 	$strPrice = $_POST['price'];
		 	$strFood = $_POST['food'];
		 	$strVolume = $_POST['amount'];

		 	$sql = "INSERT INTO 'ordertable' ('id','officer', 'price', 'food', 'amount') VALUES (NULL, '$strOfficer', '$strPrice', '$strFood', '$strVolume')";
		 	mysql_query($sql);
		 } 
	}
	
	mysql_close();
	?>
</body>
</html>