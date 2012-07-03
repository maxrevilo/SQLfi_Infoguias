function validarbusqueda()
{
	if (Validarv(document.formulario.txt.value, "el Texto para hacer la Búsqueda", "txt"))
	{
		return true;
	}
	return false;
}