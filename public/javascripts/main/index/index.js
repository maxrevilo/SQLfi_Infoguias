/**
 * JavaScript de la pagina index.scala.html
 */
var CAT_DIFF_LABEL = "Similares";

$.ajax({
    url      : "/categories",
    dataType : "json"
}).done(function(data) {
    $("#what").autocomplete( "option", "source", data);
});

removeOptions($( "#where1" ));
addOptions($( "#where1" ), [{id:"0", val:"Toda Venezuela"}, {id:"1", val:"Gran Caracas"}, {id:"2", val:"Miranda"}]);

function addOptions(select, array) {
    var options = select[0].options;
    for(i in array) {
        options.add(new Option(array[i].val, array[i].id));
    }
    return select;
}

function removeOptions(select) {
    var options = select[0].options;
    while(options.length > 0) {
        options.remove(0);
    }
    return select;
}

function resetOption(select) {
    return select;
}

$(function() {

    $("#what").autocomplete({
        source: ["Cargando"],
        delay : 0,
        autoFocus: true
    });

    $("#whatHow").buttonset();

    $("#cat_diff").button({ disabled: true, label: CAT_DIFF_LABEL});

    $("#nam_rad").change(function(){
        //console.log("disable");
        $("#what").autocomplete("disable");
        $("#cat_diff").button("disable");
    });

    $("#cat_rad").change(function(){
        //console.log("enable");
        $("#what").autocomplete("enable");
        $("#cat_diff").button("enable");
    });



    $( "#where1" )
        .combobox();
    $( "#where1" ).change(function() {
            if($(this).val() == "0") {
                $( "#where2, #where3" ).combobox("hide");
            } else {
                $( "#where2" ).combobox("show");


                addOptions(removeOptions(resetOption($( "#where2" ))),
                    [{id:"0", val:"Todas las ciudades"}, {id:"1", val:"Caracas"}, {id:"2", val:"Ocumare"}]
                    );
            }
        });

    $( "#where2" ).combobox().combobox("hide");
    $( "#where2" ).change(function() {
        if($(this).val() == "0") {
            $( "#where3" ).combobox("hide");
        } else {
            $( "#where3" ).combobox("show");

            addOptions(removeOptions(resetOption($( "#where3" ))),
                [{id:"0", val:"Todas las urbanizaciones"}, {id:"1", val:"Recreo"}, {id:"2", val:"Altamira"}]
            );
        }
    });

    $( "#where3" ).combobox().combobox("hide");
});