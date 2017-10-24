
import java.util.Timer;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;

import javax.swing.*;

public class PosetDisp {
	static Timer t;
	public static void main (String [] args)throws IOException{
					JFrame frame = new JFrame("math dong");
					Container content = frame.getContentPane();
					content.setLayout(new BorderLayout());
					//final PadDraw drawPad = new PadDraw();//
					/*
					 
					int[] elements = {1,2,3,4,5,6,7,8};
					Rel[] edges = {new Rel(1,2), new Rel(1,3), new Rel(1,4), new Rel(2,5), new Rel(2,6), new Rel(3,5), new Rel(3,7), new Rel(4,6), new Rel(4,7), new Rel(5,8), new Rel(6,8), new Rel(7,8)};
					*/
					//cube//
					int[] elements = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
					Rel[] edges = {new Rel(1,2), new Rel(1,3), new Rel(1,4), new Rel(2,5), new Rel(2,7), new Rel(3,6), new Rel(3,8), new Rel(4,7), new Rel(4,9), new Rel(5,10), new Rel(5,11), new Rel(6,11), new Rel(6,13), new Rel(7,12), new Rel(8,13), new Rel(8,15), new Rel(9,14), new Rel(9,15), new Rel(10,16), new Rel(10,17), new Rel(11,16), new Rel(12,17), new Rel(12,19), new Rel(13,18), new Rel(14,19), new Rel(14,20),new Rel(15,20), new Rel(16,21), new Rel(17,22), new Rel(18,21), new Rel(18,23), new Rel(19,22), new Rel(20,23), new Rel(21,24), new Rel(22,24), new Rel(23,24)};
					//s4 bruhat//
			
					final MathDraw drawPad = new MathDraw(elements, edges);
					content.add(drawPad, BorderLayout.CENTER);
				
					JPanel panel = new JPanel();
					//creates a JPanel
					panel.setPreferredSize(new Dimension(32, 68));
					panel.setMinimumSize(new Dimension(32, 68));
					panel.setMaximumSize(new Dimension(32, 68));
					Icon iconF = new ImageIcon("otherhull.gif");
					Icon icono = new ImageIcon("iconplay.gif");
					Icon icont = new ImageIcon("iconpause.gif");
					Icon iconc = new ImageIcon("iconcenter.gif");
					Icon iconr = new ImageIcon("iconorder.gif");
					Icon icona= new ImageIcon("iconrandom.gif");
					JButton firstButton = new JButton(iconF);
					firstButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							drawPad.bloominitialize();
							//all points overlapping//
						}
					
					});
					firstButton.setPreferredSize( new Dimension(16,16));
					panel.add(firstButton);
					JButton centralButton = new JButton(iconc);
					centralButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							drawPad.centralinitialize();
							//one antichain heuristic//
						}
					
					});
					centralButton.setPreferredSize( new Dimension(16,16));
					panel.add(centralButton);
					JButton orderButton = new JButton(iconr);
					orderButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							drawPad.orderinitialize();
							//another antichain heuristic//
						}
					
					});
					orderButton.setPreferredSize( new Dimension(16,16));
					panel.add(orderButton);
					JButton randomButton = new JButton(icona);
					randomButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							drawPad.randominitialize();
							//random placement//
						}
					
					});
					randomButton.setPreferredSize( new Dimension(16,16));
					panel.add(randomButton);
					JButton secondButton = new JButton(icono);
					secondButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							t = new Timer();
							t.scheduleAtFixedRate(new TimerTask() {
								  @Override
								  public void run() {
								    drawPad.update();

								  }
								}, 10, 10);
							//starts process of physics//
						}
						
					});
					
					JButton thirdButton = new JButton(icont);
					thirdButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							t.cancel();
							t.purge();
							//stops physics//
						}
					
					});
					
					secondButton.setPreferredSize( new Dimension(16,16));
					panel.add(secondButton);
					thirdButton.setPreferredSize( new Dimension(16,16));
					panel.add(thirdButton);
					
					
					
					
					content.add(panel, BorderLayout.WEST);
			
					frame.setSize(900, 800);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
					//this just makes the frame and adds buttons to call methods from MathDraw//
	}
}