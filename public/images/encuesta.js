function votacion()
{
	var opciones = document.getElementsByName("opcion");
	var eleccion = 0;
	for (i = 0; i < opciones.length; i++)
	{
		if(opciones[i].checked)
		{
			eleccion = opciones[i].value;
		}
	}
	if(eleccion == 0)
	{
		alert("Debe elegir alguna opción");
	}
	else
	{
		http.open('POST', 'include/encuesta.asp', true);
		http.onreadystatechange = guardaencuesta;
		valor = "op=" + eleccion + "&cache=" + Math.random();
		http.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
		http.send(valor);
	}
}

function guardaencuesta()
{
	if (http.readyState == 4) 
	{
		document.getElementById("encuesta").innerHTML = "<br><b>Gracias por Su Voto!!!</b>";
	}
}