
public class FPregisterFile {
 String RegName;
 String Qi;
 double Content;
 public FPregisterFile(String RegName,String Qi, double Content) 
     {
	 this.RegName=RegName;
	 this.Qi=Qi;
	 this.Content=Content;
     }
     public  String myToString(){
     String result = "Register "+this.RegName+" , Qi :"+this.Qi+" , Content : "+this.Content;
     return result;
     }
}
