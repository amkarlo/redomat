$('document').ready(function(){
  var services = JSON.parse(localStorage.getItem('BankServices'));

  if (localStorage.getItem('BankServices') == undefined || localStorage.getItem('BankServices') == 'null' )
  {
    setInitial();
  }
  else
    if (services["Datum"] != null)
    {
      var date = Date.today();
      today = date.toString('dd-MMM-yyyy');
              
      date = new Date(services["Datum"]);
      var serviceDate = date.toString('dd-MMM-yyyy');
      if (today != serviceDate)
          setInitial();
    }
});

function setInitial()
{
    var initial = {};
    // fill the localStorage data structure with the desired services
    initial["Transakcije"] = [];
    initial["OsobniBankar"] = [];
    initial["Studenti"] = [];
    initial["NovaUsluga"] = [];
    initial["Datum"] = new Date().getTime();
    localStorage.setItem('BankServices', JSON.stringify(initial));
}