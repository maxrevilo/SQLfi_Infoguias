function validarbusqueda()
{
	if (Validarv(document.formulario.txt.value, "el Texto para hacer la B�squeda", "txt"))
	{
		return true;
	}
	return false;
}