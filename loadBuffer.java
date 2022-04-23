
public class loadBuffer {
String Tag;
int Busy;
int Address;
public loadBuffer(String Tag,int Busy, int Address) 
   {
	this.Tag=Tag;
	this.Busy=Busy;
	this.Address=Address;
  }
	public String myToString(){
		String result ="Reservation station "+this.Tag+" : "+" Busy: "+Busy+
				" Address : "+this.Address;
		return result;
	}
}
