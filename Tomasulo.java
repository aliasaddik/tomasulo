import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java. util. *;



public class Tomasulo {
	static Queue<String> InstructionQueue=new LinkedList<String>();
	static Queue<ReadyQueueElement> ReadyQueue = new LinkedList<>();
	static Queue<String> WriteReady = new LinkedList<>();
	static HashMap<String, Integer> Instructionlatency = new HashMap<String, Integer>();
	static ReservationStations[]RSAddSub= new ReservationStations[3];
	static ReservationStations[]RSMulDiv= new ReservationStations[2];
	static StoreBuffer[]Stores=new StoreBuffer[3];
	static loadBuffer[]Loads=new loadBuffer[3];
	static FPregisterFile[]RegFile= new FPregisterFile[32];
	static int cycle=1;
	static double Memory[] = new double[2048];
	static int RSAddSubCount=0;
	static int RSMulDivCount=0;
	static int StoresCount=0;
	static int LoadsCount=0;
	public static void CodeParser(String fileName) throws IOException
	{
		BufferedReader br;
		br = new BufferedReader(new FileReader(fileName));
		String st;
		//String s="";
		while ((st = br.readLine()) != null)
		{
			//String [] splited = st.split("");
//		    System.out.println(st);
//		    for(int j=0; j<splited.length ;j++)
//		    {
//		         s=s+splited[j];
//		    }

			InstructionQueue.add(st);
			//System.out.println(st);
			//System.out.println(st.length());
			// s="";
		}
		//System.out.println(InstructionQueue);
	}
	public static void memoryvalues()
	{
		for(int i=0;i<Memory.length;i++)
		{
			Memory[i]=i;
		}
	}
	public static void InsertInRegFile()
	{
		for(int i=0;i<32;i++) {
			RegFile[i]= new FPregisterFile("F"+i,"",0);
			//System.out.println(  RegFile[i].RegName);
		}

	}
	public static boolean issueInst (String Inst)
	{

		//ReservationStations[]RSAddSub= new ReservationStations[3];
		//ReservationStations[]RSMulDiv= new ReservationStations[2];
		//StoreBuffer[]Stores=new StoreBuffer[3];
		//loadBuffer[]Loads=new loadBuffer[3];
		//String Inst="";
		boolean flag=false;
		String InstType="";
		//String InstType="";
		//while(!InstructionQueue.isEmpty()) {
		//Inst=InstructionQueue.remove();
		int i=0;
		while(Inst.charAt(i) != ' ')
		{
			InstType=InstType+Inst.charAt(i);
			i++;
		}

		if(InstType.equals("ADD.D")||InstType.equals("SUB.D"))
		{
			String Tag="A";

			String Dest="";
			int j=6;
			while(Inst.charAt(j)!= ' ')
			{
				Dest=Dest+Inst.charAt(j);
				j++;
			}
			System.out.println("Dest: "+ Dest);
			j++;
			String Src1 ="";
			while(Inst.charAt(j)!= ' ')
			{
				Src1=Src1+Inst.charAt(j);
				j++;
			}
			System.out.println("Src1: "+ Src1);
			j++;
			String Src2="";
			while(j<Inst.length())
			{
				Src2=Src2+Inst.charAt(j);
				j++;
			}
			System.out.println("Src2: "+ Src2);
			boolean Vjflag = false;
			boolean Vkflag = false;
			double Vj=-1;
			double Vk=-1;
			String Qj="";
			String Qk="";
			if(RSAddSubCount!=3)
			{     //0 , 1 , 2

				//Tag+=RSAddSubCount+1;
				for(int x =0;x<RSAddSub.length;x++){
					if(RSAddSub[x].Busy==0){
						flag=true;
						if(RegFile[Integer.parseInt(Src1.substring(1, Src1.length()))].Qi.equals(""))
						{
							Vj=RegFile[Integer.parseInt(Src1.substring(1, Src1.length()))].Content;
							Vjflag=true;
						}
						else
						{
							Qj=RegFile[Integer.parseInt(Src1.substring(1, Src1.length()))].Qi;
						}
						if(RegFile[Integer.parseInt(Src2.substring(1, Src2.length()))].Qi.equals(""))
						{
							Vk=RegFile[Integer.parseInt(Src2.substring(1, Src2.length()))].Content;
							Vkflag=true;

						}
						else
						{
							Qk=RegFile[Integer.parseInt(Src2.substring(1, Src2.length()))].Qi;
						}
						int dest = Integer.parseInt( Dest.substring( 1,Dest.length() ) );
						if(InstType.equals("ADD.D"))
						{
							RSAddSub[x]=new ReservationStations(RSAddSub[x].Tag,1,"add", Vj, Vk, Qj, Qk,Integer.parseInt(Dest.substring(1, Src1.length())));
							RegFile[dest].Qi=RSAddSub[x].Tag;
						}
						else if(InstType.equals("SUB.D"))
						{
							RSAddSub[x]=new ReservationStations(RSAddSub[x].Tag,1,"sub", Vj, Vk, Qj, Qk,Integer.parseInt(Dest.substring(1, Src1.length())));
							RegFile[dest].Qi=RSAddSub[x].Tag;
						}
						if(Vjflag==true && Vkflag==true)
						{

							ReadyQueueElement Taglatency = new ReadyQueueElement(RSAddSub[x].Tag, Instructionlatency.get(InstType),true);
							ReadyQueue.add(Taglatency);
						}
						RSAddSubCount++;
						break;
					}
				}

			}
		}
		else if(InstType.equals("MUL.D")||InstType.equals("DIV.D"))
		{
			//System.out.println("I came to mul case");
			String Tag="M";
			String Dest="";

			int j=6;
			while(Inst.charAt(j)!= ' ')
			{
				Dest=Dest+Inst.charAt(j);
				j++;
			}
			System.out.println("Dest: "+ Dest);
			j++;
			String Src1 ="";
			while(Inst.charAt(j)!= ' ')
			{
				Src1=Src1+Inst.charAt(j);
				j++;
			}
			System.out.println("Src1: "+ Src1);
			j++;
			String Src2="";
			while(j<Inst.length())
			{
				Src2=Src2+Inst.charAt(j);
				j++;
			}
			System.out.println("Src2: "+ Src2);
			boolean Vjflag = false;
			boolean Vkflag = false;
			double Vj=-1; //AS IF WE ARE NOT HAVING THE VALUE
			double Vk=-1;
			String Qj="";
			String Qk="";
			if(RSMulDivCount!=2)
			{
				//System.out.println("Count is less than 2");
				for(int x =0;x<RSMulDiv.length;x++){
					if(RSMulDiv[x].Busy==0){
						flag=true;
						//System.out.println("res station isnt busy");
						if(RegFile[Integer.parseInt(Src1.substring(1, Src1.length()))].Qi.equals("")) {
							Vj=RegFile[Integer.parseInt(Src1.substring(1, Src1.length()))].Content;
							Vjflag=true;
						}
						else {
							Qj=RegFile[Integer.parseInt(Src1.substring(1, Src1.length()))].Qi;
						}
						if(RegFile[Integer.parseInt(Src2.substring(1, Src2.length()))].Qi.equals("")) {
							Vk=RegFile[Integer.parseInt(Src2.substring(1, Src2.length()))].Content;
							Vkflag=true;
						}
						else {
							Qk=RegFile[Integer.parseInt(Src2.substring(1, Src2.length()))].Qi;
						}
						int dest = Integer.parseInt(Dest.substring( 1,Dest.length() ));
						if(InstType.equals("MUL.D"))
							RSMulDiv[x]=new ReservationStations(RSMulDiv[x].Tag,1,"mul", Vj, Vk, Qj, Qk,Integer.parseInt(Dest.substring(1, Src1.length())));
						if(InstType.equals("DIV.D"))
							RSMulDiv[x]=new ReservationStations(RSMulDiv[x].Tag,1,"div", Vj, Vk, Qj, Qk,Integer.parseInt(Dest.substring(1, Src1.length())));
						RegFile[dest].Qi=RSMulDiv[x].Tag;
						if(Vjflag==true && Vkflag==true)
						{
							//	System.out.println("MUL INSTRUCTION TO REAADY QUEUE");
							ReadyQueueElement Taglatency = new ReadyQueueElement(RSMulDiv[x].Tag, Instructionlatency.get(InstType),true);
							ReadyQueue.add(Taglatency);
						}
						RSMulDivCount++;
						break;
					}
				}

			}
		}

		else if(InstType.equals("LD.D"))
		{
			String Dest=""; //reg which we will store in
			int j=5;
			//System.out.println("Inst :"+Inst);
			while(Inst.charAt(j)!= ' ')
			{
				Dest=Dest+Inst.charAt(j);
				j++;
				//System.out.println(Inst.length()+" j "+j);
			}
			//System.out.println("Dest: "+ Dest);
			j++;
			String Src1 =""; //Address
			while(j<Inst.length())
			{
				Src1=Src1+Inst.charAt(j);
				j++;
			}
			//System.out.println("Src1: "+ Src1);
			if(LoadsCount!=3) {
				for(int x =0;x<Loads.length;x++){
					if(Loads[x].Busy==0){
						flag=true;
						System.out.println("load src"+Src1);
						Loads[x]=new loadBuffer(Loads[x].Tag,1,Integer.parseInt(Src1));
						int dest = Integer.parseInt(Dest.substring( 1,Dest.length() ));
						RegFile[dest].Qi=Loads[x].Tag;
						ReadyQueueElement Taglatency = new ReadyQueueElement(Loads[x].Tag, Instructionlatency.get(InstType),true);
						ReadyQueue.add(Taglatency);
						LoadsCount++;
						break;
					}
				}


			}


		}
		else if(InstType.equals("S.D"))
		{
			String Tag="S";
			String Dest=""; //reg which we will use to store it's value in memory
			int j=4;
			while(Inst.charAt(j)!= ' ')
			{
				Dest=Dest+Inst.charAt(j);
				j++;
			}
			//System.out.println("Dest: "+ Dest);
			j++;
			String Src1 =""; //Address
			while(j<Inst.length())
			{
				Src1=Src1+Inst.charAt(j);
				j++;
			}
			System.out.println("Src1: "+ Src1);
			boolean Vflag=false;
			double V =-1;
			String Q="";
			if (StoresCount!=3)
			{
				for(int x =0;x<Stores.length;x++){
					if(Stores[x].Busy==0){
						flag=true;
						StoresCount++;
						System.out.println(Dest);
						System.out.println(RegFile[Integer.parseInt(Dest.substring(1, Dest.length()))].Qi);
						if (RegFile[Integer.parseInt(Dest.substring(1, Dest.length()))].Qi.equals("")) {
							V=RegFile[Integer.parseInt(Dest.substring(1, Dest.length()))].Content;
							Vflag=true;
						}
						else
							Q=RegFile[Integer.parseInt(Dest.substring(1, Dest.length()))].Qi;
						Stores[x]=new StoreBuffer(Stores[x].Tag,1,Integer.parseInt(Src1),V,Q);

						if(Vflag==true)
						{
							ReadyQueueElement Taglatency = new ReadyQueueElement(Stores[x].Tag, Instructionlatency.get(InstType),true);
							ReadyQueue.add(Taglatency);
						}
						break;
					}
				}

			}
		}
		//cycle++;
		return flag;
		//}
	}
	public static void WriteBack() {
		System.out.println("Write Ready Queue"+ WriteReady.toString());
		boolean entered = false;
		int k=0;
		int j = WriteReady.size();
		while(!entered && k<j){
			if (!WriteReady.isEmpty()) {
				String inst = WriteReady.poll();

				String[] data = inst.split( " " );
				int f =Integer.parseInt(data[2]);
				if(f==0){
					entered= true;
					String tag = data[1];
					if (tag.charAt( 0 ) == 'A') {
						RSAddSub[Integer.parseInt(tag.charAt( 1 )+"")-1].Busy=0;
						RSAddSubCount--;
					}
					else if(tag.charAt(0)=='M'){
						RSMulDiv[Integer.parseInt( tag.charAt(1)+"" )-1].Busy=0;
						RSMulDivCount--;
					}
					else if(tag.charAt(0)=='L'){
						Loads[Integer.parseInt( tag.charAt( 1 )+"" )-1].Busy=0;
						LoadsCount--;
					}
					else{
						Stores[Integer.parseInt( tag.charAt(1)+"" )-1].Busy=0;
						StoresCount--;
					}
					double res = Double.parseDouble( data[0] );
					for (int i = 0; i < RSAddSub.length; i++) {
						if (RSAddSub[i].Qj.equals( tag  )) {
							RSAddSub[i].Vj = res;
							RSAddSub[i].Qj = "";
							if (RSAddSub[i].Qk == "" && RSAddSub[i].Qj == "" && RSAddSub[i].Busy == 1 && !(RSAddSub[i].Tag.equals(tag ))) {
								int latency = 0;
								if (RSAddSub[i].Op.equals( "add" )) latency = Instructionlatency.get( "ADD.D" );
								else latency = Instructionlatency.get( "SUB.D" );
								ReadyQueue.add( new ReadyQueueElement( RSAddSub[i].Tag, latency, false ) );
							}


						}
						if (RSAddSub[i].Qk.equals( tag )) {
							RSAddSub[i].Vk = res;
							RSAddSub[i].Qk = "";
							if (RSAddSub[i].Qk == "" && RSAddSub[i].Qj == "" && RSAddSub[i].Busy == 1 && !(RSAddSub[i].Tag.equals(tag ))) {
								int latency = 0;
								if (RSAddSub[i].Op.equals( "add" )) latency = Instructionlatency.get( "ADD.D" );
								else latency = Instructionlatency.get( "SUB.D" );
								ReadyQueue.add( new ReadyQueueElement( RSAddSub[i].Tag, latency, false ) );
							}
						}

					}
					for (int i = 0; i < RSMulDiv.length; i++) {
						if (RSMulDiv[i].Qj.equals( tag )) {
							//System.out.println("QJ"+RSMulDiv[i].Qj);
							//System.out.println("readyInst"+readyInst);
							RSMulDiv[i].Vj = res;
							RSMulDiv[i].Qj = "";
							if (RSMulDiv[i].Qk == "" && RSMulDiv[i].Qj == "" && RSMulDiv[i].Busy == 1&& !(RSMulDiv[i].Tag.equals(tag ))) {

								int latency = 0;
								if (RSMulDiv[i].Op.equals( "mul" ))
									latency = Instructionlatency.get( "MUL.D" );
								else
									latency = Instructionlatency.get( "DIV.D" );
								ReadyQueue.add( new ReadyQueueElement( RSMulDiv[i].Tag, latency, false ) );
							}
						}
						if (RSMulDiv[i].Qk.equals( tag )) {
							//System.out.println("MY QK AND QJ ARE EMPTY");
							RSMulDiv[i].Vk = res;
							RSMulDiv[i].Qk = "";
							if (RSMulDiv[i].Qk == "" && RSMulDiv[i].Qj == "" && RSMulDiv[i].Busy == 1&& !(RSMulDiv[i].Tag.equals(tag ))) {

								int latency = 0;
								if (RSMulDiv[i].Op.equals( "mul" ))
									latency = Instructionlatency.get( "MUL.D" );
								else
									latency = Instructionlatency.get( "DIV.D" );
								ReadyQueue.add( new ReadyQueueElement( RSMulDiv[i].Tag, latency, false ) );
							}
							//System.out.println("QK: "+ RSMulDiv[i].Qk+ "QJ : "+RSMulDiv[i].Qj+"Busy "+RSMulDiv[i].Busy);
						}

					}
					for (int i = 0; i < Stores.length; i++) {
						if (Stores[i].Q.equals( tag )) {
							Stores[i].V = res;
							Stores[i].Q = "";
							if (Stores[i].Busy == 1 && Stores[i].Q == ""&& !(Stores[i].Tag.equals(tag)))
								ReadyQueue.add( new ReadyQueueElement( Stores[i].Tag, Instructionlatency.get( "S.D" ), false ) );
						}
					}

					for(int i =0;i<RegFile.length;i++){
						if(RegFile[i].Qi.equals(tag)){
							RegFile[i].Qi="";
							RegFile[i].Content=res;
							System.out.println("ana fel wb: "+RegFile[i].Content);
						}
					}
				}
				else{
					WriteReady.add(inst);
					k++;
				}}
			else {break;}}
	}
	public static void Execute(){
		int readyQueueSize = ReadyQueue.size();
		double res=0;
		for(int j=0;j<readyQueueSize;j++) {
			//System.out.println("ANA J INFINITE "+j);
			ReadyQueueElement queueElement = ReadyQueue.poll();
			if (queueElement.mustWait == false) {
				queueElement.latency--;
				System.out.println( "Instruction at reservation station " + queueElement.tag + " is being executed ...." );
				if (queueElement.latency >0) {
					ReadyQueue.add( queueElement );
				}
				else {

					System.out.println( "Instruction at reservation station " + queueElement.tag + " finished execution and released the output to the bus !" );
					String readyInst = queueElement.tag;
					if (readyInst.charAt( 0 ) == 'A') {

						ReservationStations r = RSAddSub[Character.getNumericValue( readyInst.charAt( 1 ) ) - 1];
						if (r.Op == "add") {

							res = r.Vj + r.Vk;
						} else {
							res = r.Vj - r.Vk;

						}
						WriteReady.add( res + " " + r.Tag +" "+"1");
						//System.out.println();
						//RSAddSubCount--;
						//r.Busy = 0;
					} else if (readyInst.charAt( 0 ) == 'M') {
						ReservationStations r = RSMulDiv[Character.getNumericValue( readyInst.charAt( 1 ) ) - 1];
						if (r.Op == "mul") {
							res = r.Vj * r.Vk;
						} else {
							res = r.Vj / r.Vk;
						}
						WriteReady.add( res + " " + r.Tag+ " "+"1");
						System.out.println("WriteReady in mul case"+WriteReady.toString());
						//RSMulDivCount--;
						//r.Busy = 0;


						//WriteBack( res,r.Address );

					} else if (readyInst.charAt( 0 ) == 'L') {
						//2
						loadBuffer l = Loads[Character.getNumericValue( readyInst.charAt( 1 ) ) - 1];
						res = Memory[l.Address];
						WriteReady.add( res + " " + l.Tag +" "+"1");
						//LoadsCount--;
						//l.Busy = 0;
						//WriteBack( res,l.Address );

					} else {
						StoreBuffer s = Stores[Character.getNumericValue( readyInst.charAt( 1 ) ) - 1];
						Memory[s.Address] = s.V;
						//StoresCount--;
						//s.Busy = 0;
					}

				}
			}
			else{
				ReadyQueue.add(queueElement);
			}
		}
	}
	public static void run()
	{
		while(!InstructionQueue.isEmpty() || !ReadyQueue.isEmpty()|| !WriteReady.isEmpty())
		{	System.out.println("//////////////Cycle "+cycle+" State//////////////");
			String Inst;
			if(!InstructionQueue.isEmpty()) {
				Inst = InstructionQueue.peek();
				boolean flag1 = issueInst( Inst );
				if (flag1 == true) {
					System.out.println("Instruction"+Inst+" Got removed");
					InstructionQueue.remove();
					System.out.println(Inst+" is issued successfully !");
				}
			}
			if(cycle>=1){
				Execute();
				WriteBack();
			}
			for (int i =0;i<ReadyQueue.size();i++){
				ReadyQueueElement rq = ReadyQueue.poll();
				rq.mustWait=false;
				ReadyQueue.add( rq );
				//System.out.println("ANA INFINITE");
			}
			for (int i=0; i<WriteReady.size();i++){
				String wr=WriteReady.poll();
				wr= wr.substring(0,wr.length()-1);
				wr+="0";
				WriteReady.add(wr);

			}
			System.out.println();

			System.out.print("Instruction Queue : ");
			System.out.print(InstructionQueue.toString());
			System.out.println();
			System.out.println("Ready Queue : ");
			Object[] rq =  ReadyQueue.toArray();
			for(int i=0;i<rq.length;i++){
				ReadyQueueElement r = (ReadyQueueElement) rq[i];
				System.out.print("Tag : "+r.tag+" Latency : "+r.latency+" , MustWait : "+r.mustWait+" || ");
			}
			System.out.println();
			System.out.print("Register File : ");
			for(int i =0;i<RegFile.length;i++) {
				System.out.print( RegFile[i].myToString()+" " );
				System.out.print("|| ");
			}
			System.out.println("Addition and Subtraction stations : ");
			for(int i=0;i<RSAddSub.length;i++){
				System.out.println(RSAddSub[i].myToString());}
			System.out.println();
			System.out.println("Multiplication and Division reservation stations : ");
			for(int i =0;i<RSMulDiv.length;i++){
				System.out.println(RSMulDiv[i].myToString());
			}
			System.out.println();
			System.out.println("Load Buffer : ");
			for (int i=0;i<Loads.length;i++){
				System.out.println(Loads[i].myToString());
			}
			System.out.println();
			System.out.println("Store Buffer");
			for (int i=0;i<Stores.length;i++){
				System.out.println(Stores[i].myToString());
			}
			System.out.println();
			cycle++;

		}
	}
	public static void main (String[]args) throws IOException {
		memoryvalues();
		for(int i =1;i<=RSAddSub.length;i++){
			RSAddSub[i-1]= new ReservationStations("A"+i,0,"",-1,-1,"","",-1);
		}
		for(int i =1;i<=RSMulDiv.length;i++){
			RSMulDiv[i-1]= new ReservationStations("M"+i,0,"",-1,-1,"","",-1);
		}
		for(int i =1;i<=Loads.length;i++){
			Loads[i-1]= new loadBuffer("L"+i,0,-1);
		}
		for(int i =1;i<=Stores.length;i++){
			Stores[i-1]= new StoreBuffer("S"+i,0,-1,-1,"");
		}
		System.out.println("please enter latencty for add: ");
		Scanner in = new Scanner(System.in);
		String add = in.nextLine();
		Instructionlatency.put("ADD.D",Integer.parseInt(add));
		System.out.println("please enter latencty for sub: ");
		Scanner in1 = new Scanner(System.in);
		String sub = in1.nextLine();
		Instructionlatency.put("SUB.D", Integer.parseInt(sub));
		System.out.println("please enter latencty for mul: ");
		Scanner in2 = new Scanner(System.in);
		String mul = in2.nextLine();
		Instructionlatency.put("MUL.D",Integer.parseInt(mul) );
		System.out.println("please enter latencty for div: ");
		Scanner in3 = new Scanner(System.in);
		String div = in3.nextLine();
		Instructionlatency.put("DIV.D", Integer.parseInt(div));
		System.out.println("please enter latencty for load: ");
		Scanner in4 = new Scanner(System.in);
		String ld = in4.nextLine();
		Instructionlatency.put("LD.D",Integer.parseInt(ld) );
		System.out.println("please enter latencty for store: ");
		Scanner in5 = new Scanner(System.in);
		String sd = in5.nextLine();

		Instructionlatency.put("S.D", Integer.parseInt(sd));
		//System.out.println("Values " + Instructionlatency);
		CodeParser("src/Instructions");
		InsertInRegFile();
		run();
		System.out.println(Memory[300]);

		//issueInst("ADD.D F10 F2 F3");
		//issueInst("ADD.D F10 F2 F3");
		//System.out.println(ReadyQueue);
	}
}