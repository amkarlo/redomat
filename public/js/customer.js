 function checkIfActive()
 {
    activeNums = {};
    office = offices[0];
    for (var i=0; i < office.info.length; ++i)
    {
        var serviceName = office.info[i].service;
        activeNums[serviceName] = false;
            
        if (typeof services[serviceName][0] != 'undefined')
        {
            var active = offices[services[serviceName][0] - 1].info[i].details;
            var myNum = services[serviceName][1];
            if (active[0] < myNum && active[1] > 0 || active[0] == myNum)
                activeNums[serviceName] = true;
        }

    }   
 }
 
 
 function printToTable(service, key, i)
{   
     if (i < offices[0].info.length)
    {
        var row = $('<tr>');
        row.append($('<td>').html(key).addClass('tekst'));       
        if (service[0] != null && activeNums[key]){
            var date = new Date(service[2]);
            var time = date.toString('hh:mm');
            var office = findOfficeName(service[0]);               
            row.append($('<td>').html('Već ste uzeli broj ' + service[1] + ' u poslovnici ' + office + ' u ' + time + ' sati'));
        }
        else
            row.append($('<td>').html("Uzmite broj")
            .css("cursor", "pointer"));
            row.on('click', function(){
                            var timeStamp = service[2];
                            var number = service[1];
                            var id = service[0];
                            if (typeof timeStamp == 'undefined')
                            {
                                number = 0;
                                id = 0;
                            }
                            $.ajax({
                                    type: 'POST',
                                    url: "/getNumber/"+ key +"/" + bankId + "/" + number + "/" + id,
                                    success: function(data) {
                                        window.location.href = data;
                                    },
                                    error : function(data) {
                                        console.log('Error : getNumber');
                                    }
                                });   

            });
        $('#chooseService').append(row);
    }
}

function findOfficeName(id)
{
    for (var i = 0; i < offices.length; i++)
    {
        if  (offices[i].id == id)
            return offices[i].name;
    }
}

function printActiveToTable(service, key, i)
{   
    if (i < offices[0].info.length)
    {
        var row = $('<tr>');
        row.append($('<td>').html(key).addClass('tekst'));  

        if (service[0] != null && activeNums[key]){
            var date = new Date(service[2]);
            var time = date.toString('hh:mm');
            var office = findOfficeName(service[0]);
            
            if (offices[service[0]-1].info[i].details[0] == service[1])
                row.append($('<td>').html('Na redu ste u poslovnici ' + office));
            else
                row.append($('<td>').html('broj ' + service[1] + ' u poslovnici ' + office + ' u ' + time + ' sati'));
        }
        else
            row.append($('<td>').html('nema aktivnih brojeva za ovu uslugu'));
        
        $('#activeTable').append(row);
    }
    
}

function printOffice(office, id)
{   
    var table = $('<table>').addClass("table-hover table-bordered table-condensed text-center center");               

    var row = $('<tr>');               
    row.append($('<th colspan="4" style="text-align:center">').html(office.name));
    table = table.append(row);

    row = $('<tr>');
    row.append($('<td>').html('\\'));
    row.append($('<td>').html('Broj u obradi'));
    row.append($('<td>').html('Brojeva na čekanju'));
    row.append($('<td>').html('Vaš broj'));
    table = table.append(row);

    for (var i=0; i < office.info.length; ++i)
    {
        service = office.info[i].service; 
        row = $('<tr>');               
        row.append($('<td>').html(service));
        row.append($('<td>').html(office.info[i].details[0]));
        row.append($('<td>').html(office.info[i].details[1]));
        if (services[service][0] == office.id && activeNums[service])
                row.append($('<td>').html(services[service][1]));
        else
            if (activeNums[service])
                row.append($('<td>').html('<i class="fa fa-square-o fa-2x"></i>'));
            else
            {
                    var el = $('<i>').attr({id: id, service: service})
                                .addClass("fa fa-plus-square-o fa-2x getNumber")
                                .css("cursor", "pointer");
                row.append($('<td>').append(el));             
            } 
        table = table.append(row);
    }

    $('#table').append(table);
    
}

function userLocation(pos) {
    closestOffice(pos.lat, pos.lng);		  
}

function Deg2Rad(deg) {
    return deg * Math.PI / 180;
}

function PythagorasEquirectangular(lat1, lon1, lat2, lon2) 
{
        lat1 = Deg2Rad(lat1);
        lat2 = Deg2Rad(lat2);
        lon1 = Deg2Rad(lon1);
        lon2 = Deg2Rad(lon2);
        var R = 6371; // km
        var x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        var y = (lat2 - lat1);
        var d = Math.sqrt(x * x + y * y) * R;
        return d;
    }

function handleLocationError(browserHasGeolocation, infoWindow, pos) 
{
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
                            'Error: The Geolocation service failed.' :
                            'Error: Your browser doesn\'t support geolocation.');
}

function closestOffice(latitude, longitude)
{
    var mindif = 99999;
    var newClosest;

    for (index = 0; index < data.length; index++) 
    {
        var dif = PythagorasEquirectangular(latitude, longitude, data[index].lat, data[index].lng);
        if (dif < mindif) 
        {
            newClosest = data[index].id;
            mindif = dif;  
        }
    }		
    // vrati najbližu poslovnicu (lokaciju)
    closest = newClosest;
}

function getANumber(event)
{
    var number = event.feature.getProperty('number');
    window.location.href = "/chooseService/" + number;     
}