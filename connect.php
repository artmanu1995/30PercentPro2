<!DOCTYPE html>
<html>
<head>
	<title>connection</title>
</head>
<body>
	<?php
	header("content-type;text/javascript;charset=utf-8");
	$con=mysql_connect('192.168.1.38','root','57402482','restaurant')or die(mysql_error());
	mysql_select_db('restaurant')or die(mysql_error());
	mysql_query("SET NAMES UTF8");
	$sql="SELECT * FROM usertest";
	$res=mysql_query($sql);
	while ($row=mysql_fetch_assoc($res)) {
		$output[]=$row;
	}
	print(json_encode($output));
	mysql_close();
	?>
</body>
</html>