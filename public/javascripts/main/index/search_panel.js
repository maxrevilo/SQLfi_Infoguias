/**
 * JavaScript de la pagina index.scala.html
 */

function addOptions(select, array) {
    var options = select[0].options, first = true, newOption;
    
    for(i in array) {
        newOption = new Option(array[i].label, array[i].value, first, false);
        options.add(newOption);
        first = false;

    }

    select.next("span").children("input").val(options[0].text);
    select.change();

    return select;
}

function removeOptions(select) {
    select[0].options.length = 0;
    return select;
}

function resetOptions(select) {
    removeOptions(select);
    select.next("span").children("input").val("Cargando");
    return select;
}

$(function() {
    $("#what").autocomplete({
        source: [],
        delay : 0,
        autoFocus: true,
        disabled: !$("#cat_rad")[0].checked
    });

    $("#whereHow").hide();

    $("#nam_rad").change(function(){
        $("#what").autocomplete("disable");
        $("#cat_similar").hide();
    });

    $("#cat_rad").change(function(){
        $("#what").autocomplete("enable");
        $("#cat_similar").show();
    });

    $("#query_map_cont").dialog({
        autoOpen  : false,
        modal     : true,
        show      : "slide",
        height    : "auto",
        width     : "auto",
        resizable : false,
        buttons: {
            "Buscar": function() {
                $( this ).dialog( "close" );
                $( "#buscar_btn" ).click();
            }
        },
        open : function() {
            query_map();
        }
    });
    $("#query_map_btn").on("click", function() {
        $( "#query_map_cont" ).dialog( "open" );
        $( "#where_region" ).val("map");
        return false;
    });



    $( "#where1" )
        .combobox();
    $( "#where1" ).change(function() {
            $( "#where2, #where3" ).combobox("hide");

            $( "#where_region" ).val("state");

            $("#whereHow").show();
            if($(this).val() == "-1") {

                $("#whereHow").hide();

            } else if($(this).val() == "-2") {

                $( "#query_map_cont" ).dialog( "open" );
                $( "#where_region" ).val("map");

            } else {

                resetOptions($( "#where2" ));
                $( "#where2" ).combobox("show");
                $.ajax({
                    url      : "/cities",
                    dataType : "json",
                    data : {state_id: $(this).val()}
                }).done(function(data) {
                    var arr = data;
                    arr.unshift({label:"Todas las ciudades", value:"-1"});

                    removeOptions($( "#where2" ));
                    addOptions($( "#where2" ), data);
                });

            }
        });

    $( "#where2" ).combobox().combobox("hide");
    $( "#where2" ).change(function() {
        $( "#where3" ).combobox("hide");


        if($(this).val() == "-1") {
            $( "#where_region" ).val("state");
        } else {
            $( "#where_region" ).val("city");

            resetOptions($( "#where3" ));
            $( "#where3" ).combobox("show");
            $.ajax({
                url      : "/neighborhoods",
                dataType : "json",
                data : {city_id: $(this).val()}
            }).done(function(data) {
                var arr = data;
                arr.unshift({label:"Todas las urbanizaciones", value:"-1"});

                removeOptions($( "#where3" ));
                addOptions($( "#where3" ), data);
            });

        }
    });

    $( "#where3" ).combobox().combobox("hide");

    $( "#where3" ).change(function() {
        if($(this).val() == "-1") {
            $( "#where_region" ).val("city");
        } else {
            $( "#where_region" ).val("neighbor");
        }
    });

    $( "#search_form" ).submit(function() {
        if($( "#where_region" ).val() == "map") {
            $( "#where1, #where2, #where3" ).remove();
        } else {

            $( "#where_llat, #where_llon, #where_glat, #where_glon" ).remove();

            if($( "#where1" ).val() == "-1" || $( "#where2" ).val() == "-1" ) {
                $( "#where2, #where3" ).remove();
            } else {
                if($( "#where3" ).val() == "-1") {
                    $( "#where1, #where3" ).remove();
                } else {
                    $( "#where2, #where3" ).remove();
                }
            }

        }
        return true;
    });

    resetOptions($( "#where1" ));
    $.ajax({
        url      : "/categories",
        dataType : "json"
    }).done(function(data) {
        $("#what").autocomplete( "option", "source", data);

        $.ajax({
            url      : "/states",
            dataType : "json"
        }).done(function(data) {
            var arr = data;
            arr.unshift({label:"Busqueda por mapa", value:"-2"});
            arr.unshift({label:"Todo el pais", value:"-1"});
            removeOptions($( "#where1" ));
            addOptions($( "#where1" ), data);
        });
    });


});