
public class StoreBuffer {
	String Tag;
	int Busy;
	int Address;
	double V;
	String Q;
public StoreBuffer(String Tag, int Busy, int Address, double V, String Q) {
	this.Tag=Tag;
	this.Busy=Busy;
	this.Address=Address;
	this.V=V;
	this.Q=Q;
	
}
public String myToString(){
	String result ="Reservation station "+this.Tag+" : "+" Busy: "+Busy+
			" V : "+this.V+" Q: "+this.Q+" Address : "+this.Address;
	return result;
}
}
