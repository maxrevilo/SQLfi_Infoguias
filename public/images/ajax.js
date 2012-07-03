	salida = '';
	function handleHttpResponse() 
	{
		if (http.readyState == 4) 
		{
			if (salida != '') 
			{
				document.getElementById(salida).innerHTML = http.responseText;
			}
		}
	}

	function getHTTPObject() {
		var xmlhttp;
		try
		{
			xmlhttp = new ActiveXObject('Msxml2.XMLHTTP');
		}
		catch (e)
		{
			try
			{
				xmlhttp = new ActiveXObject('Microsoft.XMLHTTP');
			}
			catch (E)
			{
				xmlhttp = false;
			}
		}
		if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
			try {
				xmlhttp = new XMLHttpRequest();
			} catch (e) {
				xmlhttp = false;
			}
		}
		return xmlhttp;
	}
//	var http = getHTTPObject(); // We create the HTTP Object
var http;
