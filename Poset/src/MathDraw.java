import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;

import javax.swing.*;

public class MathDraw extends JComponent{
	Image image;
	Graphics2D graphics2D;
	int[] elements;
	Rel[] edges;
	double[] xcoord;
	double[] ycoord;
	int currentX;
	int currentY;
	int nextX;
	int nextY;
	int pointer;

	public MathDraw(int[] elements, final Rel[] edges){
		this.elements = elements;
		this.edges = edges;
	    this.xcoord = new double[elements.length];
		this.ycoord = new double[elements.length];
		setDoubleBuffered(false);

		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				currentX = e.getX();
				currentY = e.getY();
				boolean seen = false;
				for(int i=0; i<xcoord.length;i++){
					if(currentX-xcoord[i]<10 && currentY-ycoord[i]<10 && currentX-xcoord[i]>0 && currentY-ycoord[i]>0){
						pointer = i;
						seen = true;
						System.out.println(pointer);
						//gets vertex we are clicking on//
					}
				}
				if(seen==false){
					pointer = -1;
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				nextX = e.getX();
				nextY = e.getY();
				if(pointer>=0){
					xcoord[pointer]=nextX;
					ycoord[pointer]= nextY;
					//drags vertices with mouse movement//
				}
				clear();
				for(int i=0; i<xcoord.length;i++){			
					graphics2D.fillRect((int) xcoord[i], (int) ycoord[i], 10, 10);
				}

				for(int i=0; i<edges.length;i++){
					int s = edges[i].lesser;
					int f = edges[i].greater;
					graphics2D.drawLine((int)xcoord[s-1]+5,(int)ycoord[s-1]+5,(int)xcoord[f-1]+5,(int)ycoord[f-1]+5);
					
				}
				repaint();
				//updates to show vertex change//
				currentX = nextX;
				currentY = nextY;
			}

		});
		
	}

	public void paintComponent(Graphics g){
		if(image == null){
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D)image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
		//gets called everytime we call repaint()//
	}

	public void clear(){
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
		
		//draws over everything with a white rectngle//
	}
	
	public void bloominitialize(){
		clear();
		//bloomdraw//
		for(int i=0; i<xcoord.length;i++){
			
			xcoord[i]=450;
			ycoord[i]=400;
			
			graphics2D.fillRect((int) xcoord[i], (int) ycoord[i], 10, 10);
		}
		
		
		
	
		
		for(int i=0; i<edges.length;i++){
			int s = edges[i].lesser;
			int f = edges[i].greater;
			graphics2D.drawLine((int)xcoord[s-1]+5,(int)ycoord[s-1]+5,(int)xcoord[f-1]+5,(int)ycoord[f-1]+5);
			
		}
		repaint();
		//instantiates all the vertices at the same location//
		
	}
	public void centralinitialize(){
		clear();
		antichainer A =  new antichainer(edges, elements.length);
		ArrayList<Integer> matching = A.chains();
		
		ArrayList<ArrayList<String>> chains = new ArrayList<ArrayList<String>>();
		int[] depths = new int[elements.length];
		for(int i=0; i<matching.size()/2;i++){
			String s = String.valueOf(matching.get(2*i));
			String f = String.valueOf(matching.get(2*i+1));
			boolean pres = false;
			for(int j=0; j<chains.size();j++){
				if(chains.get(j).contains(s)){
					chains.get(j).add(f);
					pres = true;
				}
			}
			if(!pres){
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(s);
				temp.add(f);
				chains.add(temp);
			}
		}
		//above code processes the output of the chains we get from antichainer into something usable//
		
		
		//now call dijkstra based on length//
		for(int i=1; i<depths.length;i++){
			depths[i]=1000;
		}
		ArrayList<Integer> vertexset = new ArrayList<Integer>();
		for(int i=2; i<=elements.length;i++){
			vertexset.add(i);
		}
		for(int i=0; i<edges.length;i++)
		{
			if(edges[i].lesser==1){
				depths[edges[i].greater-1]=depths[edges[i].lesser-1]+1;
			}
		}
		while(vertexset.size()!=0){
			int index = -1;
			int dist = 10000;
			for(int i=0; i<vertexset.size();i++){
				if(depths[vertexset.get(i)-1]<dist){
					index = i;
					dist = depths[vertexset.get(i)-1];
				}
			}
			for(int i=0; i<edges.length;i++)
			{
				if(edges[i].lesser==vertexset.get(index)){
					depths[edges[i].greater-1]=depths[edges[i].lesser-1]+1;;
				}
			}
			vertexset.remove(index);
			//organizes vertices in order based of off distance from root node//
		}
		//centralizeddraw//
				
				for(int i=0; i<chains.size();i++){
						if(i==0){
							int xc=450;
				
							for(int j=0; j<chains.get(i).size();j++){
								int p = Integer.parseInt(chains.get(i).get(j))-1;
							
								xcoord[p]=xc;
								ycoord[p]=300+45*depths[p];
								//places vertices on the central chain//
								//downward corresponding to depth//
							}
						
						}
						else{
							//places chains on the right//
							if(i%2==0){
								int xc=450+50*(i/2);
						
								for(int j=0; j<chains.get(i).size();j++){
									int p = Integer.parseInt(chains.get(i).get(j))-1;
								
									xcoord[p]=xc;
									ycoord[p]=300+45*depths[p];
								}
							
							
							}
							//places chains on the left//
							else{
								int xc = 450-50*((i+1)/2);
		
								for(int j=0; j<chains.get(i).size();j++){
									int p = Integer.parseInt(chains.get(i).get(j))-1;
								
									xcoord[p]=xc;
									ycoord[p]=300+45*depths[p];
								}
						
						}
					}
					
				}
				
				for(int i=0; i<xcoord.length;i++){
				graphics2D.fillRect((int) xcoord[i], (int) ycoord[i], 10, 10);
				}
				
				for(int i=0; i<edges.length;i++){
					int s = edges[i].lesser;
					int f = edges[i].greater;
					graphics2D.drawLine((int)xcoord[s-1]+5,(int)ycoord[s-1]+5,(int)xcoord[f-1]+5,(int)ycoord[f-1]+5);
					
				}
				repaint();
			//draws//
				
	}
	public void orderinitialize(){
		clear();
		antichainer A =  new antichainer(edges, elements.length);
		ArrayList<Integer> matching = A.chains();
	
		ArrayList<ArrayList<String>> chains = new ArrayList<ArrayList<String>>();
		int[] depths = new int[elements.length];
		for(int i=0; i<matching.size()/2;i++){
			String s = String.valueOf(matching.get(2*i));
			String f = String.valueOf(matching.get(2*i+1));
			boolean pres = false;
			for(int j=0; j<chains.size();j++){
				if(chains.get(j).contains(s)){
					chains.get(j).add(f);
					pres = true;
				}
			}
			if(!pres){
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(s);
				temp.add(f);
				chains.add(temp);
			}
		}
		//processes antichainer output into usable form//
		
		//now call dijkstra based on length//
		for(int i=1; i<depths.length;i++){
			depths[i]=1000;
		}
		ArrayList<Integer> vertexset = new ArrayList<Integer>();
		for(int i=2; i<=elements.length;i++){
			vertexset.add(i);
		}
		for(int i=0; i<edges.length;i++)
		{
			if(edges[i].lesser==1){
				depths[edges[i].greater-1]=depths[edges[i].lesser-1]+1;
			}
		}
		while(vertexset.size()!=0){
			int index = -1;
			int dist = 10000;
			for(int i=0; i<vertexset.size();i++){
				if(depths[vertexset.get(i)-1]<dist){
					index = i;
					dist = depths[vertexset.get(i)-1];
				}
			}
			for(int i=0; i<edges.length;i++)
			{
				if(edges[i].lesser==vertexset.get(index)){
					depths[edges[i].greater-1]=depths[edges[i].lesser-1]+1;;
				}
			}
			vertexset.remove(index);
			
		}
	
		
		
		//ordereddraw//
		for(int i=0; i<chains.size();i++){
			if(chains.size()%2==1){
				int pivot = (chains.size()-1)/2;
				if(i<pivot){
					
					int xc = 450-50*(pivot-i);
					for(int j=0; j<chains.get(i).size();j++){
						int p = Integer.parseInt(chains.get(i).get(j))-1;
						
						xcoord[p]=xc;
						ycoord[p]=300+45*depths[p];
					}
				}
				else{
					int xc = 450+50*(i-pivot);
					for(int j=0; j<chains.get(i).size();j++){
						int p = Integer.parseInt(chains.get(i).get(j))-1;
						
						xcoord[p]=xc;
						ycoord[p]=300+45*depths[p];
					}
				}
				
			}
			else{
				double pivot =(chains.size()-1)/2;
				if(i<pivot){
					int xc = (int) (450-50*(pivot-i));
					for(int j=0; j<chains.get(i).size();j++){
						int p = Integer.parseInt(chains.get(i).get(j))-1;
						
						xcoord[p]=xc;
						ycoord[p]=300+45*depths[p];
					}
				}
				else{
					int xc = (int) (450+50*(i-pivot));
					for(int j=0; j<chains.get(i).size();j++){
						int p = Integer.parseInt(chains.get(i).get(j))-1;
						
						xcoord[p]=xc;
						ycoord[p]=300+45*depths[p];
					}
				}
			}
			//just ends up placing chains in order of decreasing size//
			//and vertices upwards and downwards based off of depth//
			//and tries to centralize the overall drawing//
		}
		
		for(int i=0; i<xcoord.length;i++){
		graphics2D.fillRect((int) xcoord[i], (int) ycoord[i], 10, 10);
		}
		for(int i=0; i<edges.length;i++){
			int s = edges[i].lesser;
			int f = edges[i].greater;
			graphics2D.drawLine((int)xcoord[s-1]+5,(int)ycoord[s-1]+5,(int)xcoord[f-1]+5,(int)ycoord[f-1]+5);
			
		}
		//actually draws the vertices//
		repaint();
	
		
	}
	public void randominitialize(){
		clear();	
		for(int i=0; i<xcoord.length;i++){
			
			xcoord[i]=700*Math.random();
			ycoord[i]=700*Math.random();
			
			graphics2D.fillRect((int) xcoord[i], (int) ycoord[i], 10, 10);
		}
		//instantiates everything randomly//
		for(int i=0; i<edges.length;i++){
			int s = edges[i].lesser;
			int f = edges[i].greater;
			graphics2D.drawLine((int)xcoord[s-1]+5,(int)ycoord[s-1]+5,(int)xcoord[f-1]+5,(int)ycoord[f-1]+5);
			
		}
		repaint();
	}
	public void update(){
		double[] xforcevector = new double[elements.length];
		double[] yforcevector = new double[elements.length];
		//first calculate edge attractions//
		for(int i=0; i<edges.length;i++){
			int s = edges[i].lesser-1;
			int f = edges[i].greater-1;
			double dx = xcoord[f]-xcoord[s];
			double dy = ycoord[f]-ycoord[s];
			double xchange =0;
			double ychange =0;
			double d = Math.sqrt(dx*dx + dy*dy);
			if(d ==0){
				xchange+=Math.random();
				ychange+=Math.random();
			}
			else{
			double Mag = (d-75)/225;
			xchange = Mag*dx;
			ychange = Mag*dy;
		
			}
			xforcevector[s]+=xchange;
			xforcevector[f]-=xchange;
			yforcevector[s]+=ychange;
			yforcevector[f]-=ychange;
			
		}
		
		
		//now calculate repulsion//
		for(int i=0; i<elements.length;i++){
			double xchange =0;
			double ychange =0;
			for(int j=0; j<elements.length;j++){
				if(i==j){
					continue;
				}
				double dx = xcoord[j]-xcoord[i];
				double dy = ycoord[j]-ycoord[i];
				double dsq = dx*dx + dy*dy;
				if(dsq==0){
					xchange+=Math.random();
					ychange+=Math.random();
					//allows movement even when stacked on each other//
				}
				else{
					if(dsq<40000){
						xchange+=dx/dsq;
						//vector decomposal//
						ychange+=dy/dsq;
					}
				}
				
			}
			
			double totalchange=Math.pow(xchange,2)+Math.pow(ychange, 2);
			if(totalchange>0){
				totalchange = Math.sqrt(totalchange);
				xforcevector[i]-=(xchange/totalchange)*1;
				yforcevector[i]-=(ychange/totalchange)*1;
				//can change this one to scale repulsion//
			}
			//calculates the repulsive force//
					
		}
		
		clear();
		
		//now evaluate the differences//
		for(int i=0; i<xcoord.length;i++){
			xcoord[i]+=Math.max(-5,Math.min(5, xforcevector[i]));
			ycoord[i]+=Math.max(-5,Math.min(5, yforcevector[i]));
			graphics2D.fillRect((int) xcoord[i], (int) ycoord[i], 10, 10);
		}
		//updates vertex location//
		for(int i=0; i<edges.length;i++){
			int s = edges[i].lesser;
			int f = edges[i].greater;
			graphics2D.drawLine((int)xcoord[s-1]+5,(int)ycoord[s-1]+5,(int)xcoord[f-1]+5,(int)ycoord[f-1]+5);
			
		}
		//redraws vertex and edge location//
		repaint();
		
		
	}
	
}

