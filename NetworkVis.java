import java.util.Random;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Node{
	double x, y;
	double dx, dy;
	
	Node(double x, double y){
		this.x = x;
		this.y = y;
		this.dx = this.dy = 0;
	}
	
}


public class NetworkVis {

	private Node[] nodes;
	private boolean[][] network;
	private int size;
	private int iterations;
	
	public static double KR = 0.01;
	public static double KF = 0.01;
	public static int DEFAULT_ITERATIONS = 20;
	
	
	
	public NetworkVis(){ //randomly generated network
		
		size = 10;
		
		nodes = new Node[100];
		network = new boolean[100][100];
		
		Random rand = new Random(System.currentTimeMillis());
		
		iterations = DEFAULT_ITERATIONS;
		
		for (int i = 0; i < size; i ++){
			for (int j = 0; j < i; j ++){
				network[i][j] = rand.nextBoolean();
			}
		}

		for (int i = 0; i < size; i ++){
			nodes[i] = new Node(rand.nextDouble(), rand.nextDouble());
		}
		
	}
	
	public NetworkVis(String fileName, int iterations){
	
		File file = new File(fileName);
		Scanner sc = null;
		String s, s1, s2;
		int i1, i2;
		try {
	        sc = new Scanner(file);
		}
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
		// read number
		//System.out.println("+++");
		s = sc.nextLine();
	
		if (s == null || !s.matches("[\\d]+")){
			System.out.println("Invalid first line");
			System.exit(1);					
		}
		size = Integer.parseInt(s);
		if (size <= 0){
			System.out.println("Invalid size: " + size);
			System.exit(1);					
		}
		nodes = new Node[100];
		network = new boolean[100][100];
		
		Random rand = new Random(System.currentTimeMillis());

		for (int i = 0; i < size; i ++){
			nodes[i] = new Node(rand.nextDouble(), rand.nextDouble());
		}
		for (int i = 0; i < size; i ++){
			for (int j = 0; j < i; j ++){
				network[i][j] = false;
			}
		}
		while (sc.hasNextLine()){
			
			s = sc.nextLine().trim();
			
			//System.out.println(s);
			
			if (!s.matches("^[(]?[\\d]+[\\s]*[,][\\s]*[0-9]+[)]?$")){
				System.out.println("Invalid line: " +s);
			}
				
			if (s.charAt(0) == '(')
				s = s.substring(1);
		
			//System.out.println(s);
			
			
			s1 = s.substring(0, s.indexOf(',')).trim();
			
			
			i1 = Integer.parseInt(s1);
			
			s2 = s.substring(s.indexOf(',')+1).trim();
			
			//System.out.println(s2);
			
			if (s2.charAt(s2.length()-1) == ')'){
				//System.out.println("yes");
			
				s2 = s2.substring(0, s2.length()-1);
			}
			//System.out.println(s2);
			i2 = Integer.parseInt(s2);
			
			if (i1 != i2 && i1 < size && i2 < size && i1 >= 0 && i2 >= 0)
				network[Math.max(i1, i2)][ Math.min(i1, i2)] = true;

			
		}
		sc.close();
		
		if (iterations > 0)
			this.iterations = iterations;
		else
			this.iterations = DEFAULT_ITERATIONS;
		
		
	}
	
	
	
	public static void main(String[] args) {

		String file = "";
		int iter = 0;
		
		if (args.length == 0){
			
			System.out.println("Welcome to NetworkVis");
			System.out.println("Usage: \n\tNetworkVis -f[file name] -i{number of iterations (default: " +
					DEFAULT_ITERATIONS + ")}");
			System.exit(0);
			
		}
		
		for (int i = 0; i < args.length; i++) {
            if (args[i].length() > 2 && args[i].substring(0,2).equals("-i")){
            	try{
            		iter = Integer.parseInt(args[i].substring(2)) ;
            	}
            	catch(Exception e){
            		//do nothing
            	}
            	if (iter <= 0){
            		System.out.println("Invalid number of iterations, using default (" + DEFAULT_ITERATIONS + " )instead");
            	}
            }
            else if (args[i].length() > 2 && args[i].substring(0,2).equals("-f")){
            	
            	file = args[i].substring(2);
            }
            else {
                System.out.println("invalid flag");
                System.exit(1);
            }
        }
		
		if (file.equals("")){
			System.out.println("File must be specified");
			System.exit(1);
		}
		iter = iter<=0?DEFAULT_ITERATIONS: iter;
		
		NetworkVis nv = new NetworkVis(file, iter);
		
		StdDraw.setCanvasSize(700, 700);
		StdDraw.clear(StdDraw.BOOK_BLUE);
		
		while (iter-- >0){
		
			nv.paint();
		
			try {
			    Thread.sleep(1000);                 
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			nv.calculate();
		
		}
		
	}
	
	public void paint(){
	
		StdDraw.show(0);
		
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		StdDraw.filledRectangle(0.5, 0.5, 0.5, 0.5);
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(0.5, 0.5, 5/12., 5/12.);
		StdDraw.setPenColor(StdDraw.BOOK_RED);
		StdDraw.rectangle(0.5, 0.5, 5/12., 5/12.);

				
				//(0.2, 0.2, 0.4, 0.4);
		
		
		for (int i = 0; i < size; i++){
			for (int j = 0; j < i; j++){
				if (network[i][j]){
					
					//System.out.println("" + nodes[i].x +"," + nodes[i].y + "-" + nodes[j].x + "," + nodes[j].y);
					StdDraw.setPenColor(StdDraw.BOOK_BLUE);
					StdDraw.line(coord(nodes[i].x), 
							coord(nodes[i].y), 
							coord(nodes[j].x), 
							coord(nodes[j].y));
				}
			}
		}
		
		
		
		for (int i = 0; i < size; i++){
			
			StdDraw.setPenColor(StdDraw.BOOK_BLUE);
			StdDraw.filledCircle(coord(nodes[i].x),  coord(nodes[i].y) , 0.025);
			StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
			StdDraw.text(coord(nodes[i].x), coord(nodes[i].y), Integer.toString(i));
			StdDraw.setPenColor(StdDraw.BOOK_RED);
			StdDraw.circle(coord(nodes[i].x),  coord(nodes[i].y) , 0.025);

		}
		
		StdDraw.show();
		
	}
	
	
	private void calculate(){
		
		double F, theta;
		
		for (int i = 0; i < size; i++){
			for (int j = 0; j < i; j++){
				
				theta = Math.atan2(nodes[j].y - nodes[i].y, nodes[j].x - nodes[i].x);
				
				if (network[i][j]){ //edge
					
					F = KF *  ((nodes[j].x - nodes[i].x)*(nodes[j].x - nodes[i].x) +
							(nodes[j].x - nodes[i].x)*(nodes[j].x - nodes[i].x));
					
					//System.out.println("FA = " + F + " theta = " + theta);
				}
				else{
					F =  Math.sqrt((nodes[j].x - nodes[i].x)*(nodes[j].x - nodes[i].x) +
							(nodes[j].y - nodes[i].y)*(nodes[j].y - nodes[i].y));
					//System.out.println("FR = " + F + " theta = " + theta);
					F = (F==0?0:- KR /F);
				}
				
				// NB: KR < 0
				nodes[i].dx += F * Math.cos(theta);
				nodes[j].dx -= F * Math.cos(theta);
				nodes[i].dy += F * Math.sin(theta);
				nodes[j].dy -= F * Math.sin(theta);
			}
		}
		
		for (int i = 0; i < size; i++){
			
			//System.out.println("For " + i + " (" + nodes[i].x + ", " + nodes[i].y + ")");
			
			nodes[i].x += nodes[i].dx;
			nodes[i].y += nodes[i].dy;
			nodes[i].dx = 0;
			nodes[i].dy = 0;
			nodes[i].x = bound(nodes[i].x);
			nodes[i].y = bound(nodes[i].y);
			
			
			
			
			//System.out.println("For " + i + " (" + nodes[i].x + ", " + nodes[i].y + ")");
			
		}
		
	}
	
	private double bound(double value){
		if(value > 1.)
			return 1.;
		if (value < 0.)
			return 0.;
		return value;
		
	}
	
	private double coord(double value){
		
		return 5.* (0.2 + value)/7.;
		
	}
	
	
	
	

}
