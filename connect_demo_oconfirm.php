<?php
	header("content-type;text/javascript;charset=utf-8");
	$con=mysql_connect('192.168.1.49','insert','57402482','restaurant')or die(mysql_error());
	mysql_select_db('restaurant')or die(mysql_error());
	mysql_query("SET NAMES UTF8");
	$sql="SELECT * FROM demo_oconfirm";
	$res=mysql_query($sql);
	while ($row=mysql_fetch_assoc($res)) {
		$output[]=$row;
	}
	print(json_encode($output));
	mysql_close();
?>