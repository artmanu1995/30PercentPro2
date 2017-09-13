<?php
mysql_connect("192.168.1.38","insert","57402482");
mysql_select_db("restaurant");

//ดึงข้อมูลออกมาในรูปแบบ UTF 8
mysql_query("SET NAMES UTF8");

//ส่วนของ SELECT ข้อมูลจาก DataBase
$moreYear=$_REQUEST['moreYear'];
if(!$moreYear) $moreYear=0;
$q=mysql_query("SELECT * FROM user_table where id > $moreYear");
while($e=mysql_fetch_assoc($q))
       $output[]=$e;

//แปลงข้อมูลให้อยู่ในรูปแบบของ JSON และพิมพ์ข้อมูลออกมา
print(json_encode($output));
mysql_close();
?>