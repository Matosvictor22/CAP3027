import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.*;
import java.io.*;
import java.lang.Object;
import java.lang.Float.*;
import java.awt.Graphics.*;
import java.awt.Graphics2D.*;
import java.awt.geom.Line2D.Double.*;

import javax.imageio.*;
import javax.swing.*;

import java.util.Random;

public class DisplayImage {
	private static final int WIDTH = 402;
	private static final int HEIGHT = 402;
    
	public static void main( String[] args)
	{
		SwingUtilities.invokeLater(new Runnable(){
		public void run(){
			createAndShowGUI();
			}
		});
	}
	
	public static void createAndShowGUI(){
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class ImageFrame extends JFrame
{
	private BufferedImage image = null;
	int size;
	int stemNum;
	int	steps;
	float transProb;
	float maxRota;
	int maxGrowth;
	int xPos;
	int yPos;
	int startingColor = 0xFF421010;
	int endingColor= 0xFF329932;

    
	public ImageFrame( int width, int height)
	{
    	this.setTitle( "CAP 3027 2014- HW05 - Victor Matos");
    	this.setSize(width,height);
   	 
    	addMenu();
   	 
   	 
	}
    
	private void addMenu()
	{
    	JMenu fileMenu = new JMenu( "File");
    	

    	JMenuItem openItem = new JMenuItem( "Directed random walk plant");
    	openItem.addActionListener(new ActionListener()
    	{
        	public void actionPerformed( ActionEvent event)
        	{
        		randomWalkPlant();   	 
        		
        	}
    	} );
   	 

    	fileMenu.add( openItem);

    	JMenuItem colorItem = new JMenuItem( "Change Colors");
    	colorItem.addActionListener( new
            	ActionListener()
    	{
        	public void actionPerformed( ActionEvent event)
        	{
            	changeColorPrompt();
        	}
    	} );
       	 
    	fileMenu.add( colorItem);
   	 
    	JMenuItem exitItem = new JMenuItem( "Exit");
    	exitItem.addActionListener( new
            	ActionListener()
    	{
        	public void actionPerformed( ActionEvent event)
        	{
            	System.exit(0);
        	}
    	} );
       	 
    	fileMenu.add( exitItem);
    	

   	 
    	JMenuBar menuBar = new JMenuBar();
    	menuBar.add( fileMenu);
    	this.setJMenuBar( menuBar);
       	 
       	 
    
   	 
	}
    

	public void displayBufferedImage( BufferedImage img)
	{
    	this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(img))));
    	this.validate();
	}
    
	public void promptUser(){
    	String result = JOptionPane.showInputDialog("What is the Canvas size?");
    	size = Integer.parseInt(result);
    	result = JOptionPane.showInputDialog("What is the number of stems?");
    	stemNum = Integer.parseInt(result);
    	result = JOptionPane.showInputDialog("What is the number of steps per stem?");
    	steps = Integer.parseInt(result);
    	result = JOptionPane.showInputDialog("What is the transmission probability?");
    	transProb = Float.parseFloat(result);
    	result = JOptionPane.showInputDialog("What is the max rotation increment?");
    	maxRota = Float.parseFloat(result);
    	result = JOptionPane.showInputDialog("What is the growth segment increment?");
    	maxGrowth = Integer.parseInt(result);
    	xPos = size/2;
    	yPos = size/2;
	}
    
	public void interpolateArrayStrokes(float[] strokes, float[] current, int steps,float starting, float ending){
		
		for(int i =0; i< strokes.length; i++){
		/*	if(i >= 0)
				strokes[i] =5;
			if(i > steps/5)
				strokes[i] =4;
			if(i >2*steps/5){
				strokes[i] =3;
			}
			if(i >3*steps/5){
				strokes[i] =2;
			}
			if(i >4*steps/5){
				strokes[i] =1;
			}
			*/
			strokes[i]= (int)(starting + i*(ending-starting)/(float) steps);
			current[i]=0.5f;
		}
		
		
		for(int i =0; i< strokes.length; i++){
			System.out.println("Stroke" + i + " " + strokes[i]);
		}
	}
	
	public void interpolateArrayColors(Color[] colors, int steps, int startingColor, int endingColor){
	
	//Extracts RGB components of startingColor and endingColor
	
		int red1 = (startingColor >> 16) & 0x000000FF;
		int red2 = (endingColor >> 16) & 0x000000FF;
		int red3 ;
	
		int gre1 = (startingColor >> 8) & 0xFF;
		int gre2 = (endingColor >> 8) & 0xFF;
		int gre3 ;
	
		int blu1 = startingColor & 0x000000FF;
		int blu2 = endingColor & 0x000000FF;
		int blu3 ;

			for(int i =0; i< colors.length; i++){
				red3 = (int)(red1 + i*(red2-red1)/(float) steps);
				gre3 = (int)(gre1 + i*(gre2-gre1)/(float) steps);
				blu3 = (int)(blu1 + i*(blu2-blu1)/(float) steps);
				colors[i] =new Color( 0xFF000000|((red3 << 16) & 0xFF0000)|((gre3 << 8) & 0xFF00)| (blu3 & 0xFF));
			}
		
			for(int i =0; i< colors.length; i++){
				System.out.println("colors" + i + " " + colors[i]);
			}
		}

	public void generateCoordinates(float[][] x2d,float [][] y2d, int stemCount, int steps){
		double xGrowth =0;
	   	double yGrowth =0;
	    double theta = 0;
	   	double phi = 1.0;
	   	double tao = 0;
	   	double reflective = 1-transProb;
	   	int direction =0;
	   	Random r = new Random();
    	
    	
    	for(int stemCounter =0; stemCounter < stemNum; stemCounter++){
        	xPos=size/2;
        	yPos=size/2;
        	theta =0;
        	tao = 0;
        	direction = 0;
        	x2d[stemCounter][0] = xPos;
        	y2d[stemCounter][0] = yPos;
       	 
        	for(int i = 1; i <steps; i++){
        		 //determine coin bias
          		 if(direction == -1)
          			 tao = transProb;
          		 else
          			 tao = reflective;
          		 
          		 double x = r.nextDouble();
           	//generate Direction based on tao
          		 if(x > tao)
          			 direction = 1;    
          		 else
          			 direction = -1;
          		 

          		 x = r.nextDouble()*maxRota;
          		 
          		 xGrowth = Math.abs((phi + maxGrowth)*Math.cos(theta + x ));
          		 yGrowth = (phi + maxGrowth)*Math.sin(theta + x);
          		 
          		 if (yGrowth > 0 )
          			 yGrowth*= -1;
          		if(direction == -1)//go left
          			xGrowth*=-1;
          		
          		 xPos += xGrowth;
           		 yPos += yGrowth;
           		 theta += x;
           		 
           		 x2d[stemCounter][i] = xPos;
           		 y2d[stemCounter][i] = yPos;
        	}
    	}
    	
	}

	public void setBackground(BufferedImage img, Color col){
    	Graphics2D g2d = (Graphics2D) img.createGraphics();
    	g2d.setColor(col);
    	g2d.fillRect(0, 0, size, size);
           	 
	}

    
	public void drawStrokes(BufferedImage img, Color col, float[] strokeContainer, float[] currentStroke, Color[] colorContainer, float[][] xContainer, float[][] yContainer){
   	 	Graphics2D g2d = (Graphics2D) img.createGraphics();
   	 	g2d.setColor(col);
   	 	g2d.setRenderingHint( 
   		RenderingHints.KEY_ANTIALIASING,
   		RenderingHints.VALUE_ANTIALIAS_ON);
   
    	g2d.setStroke(new BasicStroke(strokeContainer[0],BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,10f));
    	Line2D l2d = new Line2D.Double(xPos,yPos,xPos,yPos);
    	g2d.draw(l2d);
 	 
  	for(int i =0; i < steps; i++){
  		for(int j = 0; j<i; j++){
  			if(currentStroke[i] <= strokeContainer[i])
  			currentStroke[i] +=0.5;
  		}
    	for(int stemCounter = 0; stemCounter <stemNum; stemCounter++){
    		
    		 g2d.setStroke(new BasicStroke(currentStroke[i],BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,1));
      		 g2d.setColor(colorContainer[i]);
      		 if((i+1)< steps){
      			 
      			 l2d.setLine(xContainer[stemCounter][i],yContainer[stemCounter][i],xContainer[stemCounter][i+1],yContainer[stemCounter][i+1]);
      			 System.out.println("x[" +stemCounter+"]" +"[" + i +"] = " + xContainer[stemCounter][i] + " " +"y[" + stemCounter +"]" +"[" + i +"]= " + yContainer[stemCounter][i] );
      			 
      			 g2d.draw(l2d);
      			 
      			 
      		//	icon.setImage(img);
      		//	label.repaint();
      		//	validate();
      		 }
    		}
  		}
  	g2d.dispose();
	}
	void changeColorPrompt(){
		String result = JOptionPane.showInputDialog("What is the starting Color?");
    	startingColor = Integer.parseInt(result,16);
  
    	result = JOptionPane.showInputDialog("What is the ending Color?");
    	endingColor = Integer.parseInt(result,16);
	}
	
	void randomWalkPlant(){

		new Thread(new Runnable(){
			public void run(){
		    	promptUser();
		    	float strokeContainer[];
		    	Color colorContainer[];
		    	float xContainer[][];
		    	float yContainer[][];
		    	float strokeCurrent[];
		    	strokeContainer= new float[steps];
		    	colorContainer= new Color[steps];
		    	xContainer = new float[stemNum][steps];
		    	yContainer = new float[stemNum][steps];
		    	strokeCurrent = new float[steps];
		    	final  BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		    	setBackground(image,Color.black);
		    	interpolateArrayStrokes(strokeContainer,strokeCurrent,steps,6.0f,0.5f);
		    	interpolateArrayColors(colorContainer, steps,startingColor,endingColor);
		    	generateCoordinates(xContainer, yContainer,stemNum,steps);
		    	drawStrokes(image, Color.black, strokeContainer,strokeCurrent, colorContainer,xContainer,yContainer);
		    	
		    	SwingUtilities.invokeLater(new Runnable(){
		    		public void run(){
		    			displayBufferedImage(image);
		    		}
		    	}
		    	);
		    	
			}
		}
		).start();

	}
	
}
    


