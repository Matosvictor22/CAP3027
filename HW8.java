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
import java.util.Scanner;

public class DisplayImage {
    private static final int WIDTH = 402;
    private static final int HEIGHT = 402;
    
    public static void main( String[] args)
    {
    JFrame frame = new ImageFrame(WIDTH, HEIGHT);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    }
}

class ImageFrame extends JFrame
{
	private final JFileChooser chooser;
	File file;
	private Scanner sc ;
    private BufferedImage image = null;
    int imgWidth;
    int imgHeight;
    int foregroundCol;
    int backgroundCol;
    int genNum;
    int turtleOffsetX;
    int turtleOffsetY;
    int turtleTheta;
    float segLength;
    int delta;
    int segScaler;
    String initiator;
    char conditional[] = new char[10];
    String generator[] = new String[10];
    
    
    
        
    public ImageFrame( int width, int height)
    {
        this.setTitle( "CAP 3027 2014- HW08 - Victor Matos");
        this.setSize(width,height);
        chooser = new JFileChooser();
        chooser.setCurrentDirectory( new File(".") );
     
        addMenu();
        
        
    }
    
    private void addMenu()
    {
        JMenu fileMenu = new JMenu( "File");
        
        JMenuItem LoadLSystemItem = new JMenuItem( "Load L-System");
        LoadLSystemItem.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent event)
            {
                loadLSystem();
                parseToVariables();
            }
        } );
        
        fileMenu.add( LoadLSystemItem);
        
        JMenuItem configureOptionsItem = new JMenuItem( "Configure Options");
        configureOptionsItem.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent event)
            {
                
                promptImageConfig();
                BufferedImage image = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_ARGB);
                String bCol = ""+backgroundCol;
                setBackground(image,Color.decode(bCol));
                //displayBufferedImage(image);
                
            }
        } );
        
        fileMenu.add( configureOptionsItem);
        
        JMenuItem displayImageItem = new JMenuItem( "Display L-System");
        displayImageItem.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent event)
            {
                
                promptDisplay();
               
               // BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
                displayBufferedImage(image);
                
            }
        } );
        
        fileMenu.add( displayImageItem);

        JMenuItem saveImageSystem = new JMenuItem( "Save Image");
        saveImageSystem.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent event)
            {
                
               //TODO: Save
                
            }
        } );
       

        fileMenu.add( saveImageSystem);
        
        
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
    
    
    private void loadLSystem()
    {
    	File file = getFile();
    	if (file != null)
    	{
    		loadFile(file);
    	}

    }
    
    private File getFile()
    {
    	File file = null;
    	if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    	{
    		file = chooser.getSelectedFile();
    	}
    	return file;
    }
   
    private void loadFile( File file){
		try
		{
			sc = new Scanner(file);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this,  e);
		}
	}


    public void displayBufferedImage( BufferedImage img)
    {
        this.setContentPane( new JScrollPane( new JLabel( new ImageIcon(img))));
        this.validate();
    }
    
    public void promptImageConfig(){
    	
        String result = JOptionPane.showInputDialog("Canvas width?");
        imgWidth = Integer.parseInt(result);
        
        result = JOptionPane.showInputDialog("Canvas height");
        imgHeight = Integer.parseInt(result);
        
        result = JOptionPane.showInputDialog("background color");
        backgroundCol = Integer.parseInt(result,16);
        
        result = JOptionPane.showInputDialog("Foreground color");
        foregroundCol = Integer.parseInt(result,16);
    }
    
    public void promptDisplay(){
        String result = JOptionPane.showInputDialog("Number of generations");
        genNum = Integer.parseInt(result);
        
        result = JOptionPane.showInputDialog("Initial x position from center");
        turtleOffsetX = Integer.parseInt(result);
        
        result = JOptionPane.showInputDialog("Initial y position from center");
        turtleOffsetY = Integer.parseInt(result);
        
        result = JOptionPane.showInputDialog("Initial theta from positive x-axis");
        turtleTheta = Integer.parseInt(result);
        
        result = JOptionPane.showInputDialog("Base segment length");
        segLength = Float.parseFloat(result);
    }
    
    public void setBackground(BufferedImage img, Color col){
        Graphics2D g2d = (Graphics2D) img.createGraphics();
        g2d.setColor(col);
        g2d.fillRect(0, 0, imgWidth, imgHeight);
                
    }
    public void parseToVariables(){
    	delta=sc.nextInt();
	    segScaler=sc.nextInt();
	    initiator=sc.nextLine();
	    String entireLine[] = new String[10];
	    
	    for(int i=0; i<10;i++){
	    	entireLine[i]= sc.nextLine();
	    	System.out.print("entireLine["+i+"]= "+entireLine[i]);
	    }
	 }
}
    

