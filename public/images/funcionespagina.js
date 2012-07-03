function Trim(s) {
	var tmp = "" + s + "";
	return tmp.replace(/^\s*|\s*$/g, "");
}

function LTrim(s) {
	var tmp = "" + s + "";
	return tmp.replace(/^\s*/g, "");
}

function RTrim(s) {
	var tmp = "" + s + "";
	return tmp.replace(/\s*$/g, "");
}

function camporequerido(campo)
{
	return ( Trim(campo).length > 0 )
}

function Validar(val, label, field)
{	
	if (!camporequerido(val))
	{
		alert("Debes ingresar "+label);
		return false;
	}
	return true;
}

function Validars(val, label, field)
{	
	if (!camporequerido(val))
	{
		alert("Debes seleccionar "+label);
		return false;
	}
	return true;
}

function Validarv(val, label, field)
{	
	if (!camporequerido(val))
	{
		alert("Debes ingresar "+label);
		return false;
	}
	return true;
}

function solonumeros(e)
{
	var key;
	if(window.event) // IE
	{
		key = e.keyCode;
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		key = e.which;
	}
	if ((key < 48 || key > 57)&&(key != 8))
    {
      return false;
    }
 return true;
}

function newWindow(mypage,myname,w,h,features) 
{
	var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	if (winl < 0) winl = 0; 
	if (wint < 0) wint = 0; 
	var settings = 'height=' + h + ','; settings += 'width=' + w + ','; settings += 'top=' + wint + ','; settings += 'left=' + winl + ',';
	settings += features;
	win = window.open(mypage,myname,settings);
    win.window.focus();
}

function vermapa(codigo, sucursal)
{
	newWindow('mapa.asp?cod='+codigo+'&suc='+sucursal,'mapa',640,480,'status=0,toolbar=0,menubar=0,directories=0,resizable=0,scrollbars=0,location=no');
}

function vermapa2(codigo, sucursal)
{
	newWindow('/PagAm/mapa.asp?cod='+codigo+'&suc='+sucursal,'mapa',640,480,'status=0,toolbar=0,menubar=0,directories=0,resizable=0,scrollbars=0,location=no');
}