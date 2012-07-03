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


    $("#whatHow").buttonset();

    $("#cat_diff").button({disabled: !$("#cat_rad")[0].checked});

    $("#nam_rad").change(function(){
        $("#what").autocomplete("disable");
        $("#cat_diff").button("disable");
    });

    $("#cat_rad").change(function(){
        $("#what").autocomplete("enable");
        $("#cat_diff").button("enable");
    });




    $( "#where1" )
        .combobox();
    $( "#where1" ).change(function() {
            $( "#where2, #where3" ).combobox("hide");

            $( "#where_region" ).val("state");

            if($(this).val() == "-1") {
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

    $( "#whereHow" ).buttonset();

    $( "#buscar_btn" ).button();

    $( "#search_form" ).submit(function() {
      if($( "#where1" ).val() == "-1" || $( "#where2" ).val() == "-1" ) {
        $( "#where2, #where3" ).remove();
      } else {
        if($( "#where3" ).val() == "-1") {
          $( "#where1, #where3" ).remove();
        } else {
          $( "#where2, #where3" ).remove();
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
            arr.unshift({label:"Todos los estados", value:"-1"});
            removeOptions($( "#where1" ));
            addOptions($( "#where1" ), data);
        });
    });


});