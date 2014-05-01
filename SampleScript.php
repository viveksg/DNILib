<?php
$val_key=$_REQUEST['val_key'];
$ver=$_REQUEST['version'];
$latest_version=2;//here $latest_version is hardcoded but you can store latest version  in DB and fetch it at runtime.
$data="link_to_APK_file_of_latest_version";
if($val_key=="your_key")
echo "$latest_version</br>$data";
/*This is just a simple example you can
 store version,link and key values in some database
 and script can fetch them while execution and do the
Key check and provide the link*/
?>

