
public class ReservationStations {
String Tag;
int Busy;
String Op;
double  Vj; 
double Vk; 
String Qj;
String Qk;
int Address;
	
public ReservationStations(String Tag,int Busy,String Op,double Vj,double Vk,String Qj,String Qk,int Address) {
this.Tag=Tag;
this.Busy=Busy;
this.Op=Op;
this.Vj=Vj;
this.Vk=Vk;
this.Qj=Qj;
this.Qk=Qk;
this.Address=Address;
}
public String myToString(){
    String result ="Reservation station "+this.Tag+" : "+" Busy: "+Busy+" Operation : "+this.Op+" Vj "+this.Vj+
            " Vk : "+this.Vk+" Qj : "+this.Qj+" Qk : "+this.Qk+" Address : "+this.Address;
    return result;
}
}
