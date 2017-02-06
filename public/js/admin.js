function printOneOffice(office, id)
{
    var table = $('<table>').addClass("table-hover table-bordered table-condensed text-center center");

    var row = $('<tr>');               
    row.append($('<th colspan="4" style="text-align:center">').html(office.name));
    table = table.append(row);

    row = $('<tr>');
    row.append($('<td>').html('\\'));
    row.append($('<td>').html('Broj u obradi'));
    row.append($('<td>').html('Brojeva na čekanju'));
    row.append($('<td>').html('Otpusti broj'));
    table = table.append(row);

    for (var i=0; i < office.info.length; ++i)
    {
        var service = office.info[i].service;
        row = $('<tr>');               
        row.append($('<td>').html(service));
        row.append($('<td>').html(office.info[i].details[0]));
        row.append($('<td>').html(office.info[i].details[1]));
        if (office.info[i].details[1] == 0 && office.info[i].details[0] == 0)
            row.append($('<td>').html('<i class="fa fa-square-o fa-2x"></i>'));
        else
        {
            var el = $('<i>').attr({id: id, service: service, number: office.info[i].details[0]})
                            .addClass("fa fa-plus-square-o fa-2x freeNumber")
                            .css("cursor", "pointer");
            row.append($('<td>').append(el));             
        } 
        table = table.append(row);
    }

    $('#table').append(table);

    $('.freeNumber').unbind('click');

    $('.freeNumber').on('click', function(){
            var id = $(this).attr('id');
            var service = $(this).attr('service');
            var number = $(this).attr('number');
            $.ajax({
                type: 'POST',
                url: 'freeNumber/' + service + "/" + id +"/" + number,
                dataType: "text",
                success: function(data) {
                    offices[id-1] = JSON.parse(data);
                    printOffice(offices[id-1], id);
                },
                error : function(data) {
                    console.log('Error : freeNumber');
                }
            });
        });
    
}

function printOffice(office, id)
{
    $('#table').html('');
    printOneOffice(office, id);
}

function printOffices()
{
    $('#table').html('');
    for (var i = 0; i < offices.length; i++)
        printOneOffice(offices[i], i+1);      
}