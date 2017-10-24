import java.util.ArrayList;

public class antichainer {

	public Rel[] rels;
	public int[] left;
	public int[] right;
	public  Double[] dist;
	public int[][] graph;
	public int vertex;
	public antichainer( Rel[] rels, int vertex){
		this.vertex = vertex;
		this.rels = rels;
		this.left = new int[vertex+1];
		//holds pointers from left vertices to right vertices//
		this.right = new int[vertex+1];
		//holds pointers from right vertices to left vertices//
		this.dist = new Double[vertex+1];
		this.graph = new int[2*vertex+1][2*vertex+1];
		for(int i=0; i<rels.length;i++){
			graph[rels[i].lesser][vertex+rels[i].greater]=1;
			graph[vertex+rels[i].greater][rels[i].lesser]=1;
			
			//creates an adjacency matrix//
		}
	}
	
	
	
	public static void main(String[] args) {
		int[] elements = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
		Rel[] edges = {new Rel(1,2), new Rel(1,3), new Rel(1,4), new Rel(2,5), new Rel(2,7), new Rel(3,6), new Rel(3,8), new Rel(4,7), new Rel(4,9), new Rel(5,10), new Rel(5,11), new Rel(6,11), new Rel(6,13), new Rel(7,12), new Rel(8,13), new Rel(8,15), new Rel(9,14), new Rel(9,15), new Rel(10,16), new Rel(10,17), new Rel(12,17), new Rel(12,19), new Rel(13,18), new Rel(14,19), new Rel(14,20), new Rel(15,20), new Rel(16,21), new Rel(17,22), new Rel(18,21), new Rel(18,23), new Rel(19,22), new Rel(20,23), new Rel(21,24), new Rel(22,24), new Rel(23,24)};
		antichainer ant = new antichainer(edges,24);

		
		while(ant.bfs()){
			for(int i=1; i<=ant.vertex;i++){
				if(ant.left[i]==0 && ant.dfs(i)){
					System.out.println(i+" "+ ant.left[i]);
				}
			}
			
			
			
		}
		//above is just for testing purposes//
	}
	
	public ArrayList<Integer> chains(){
		ArrayList<Integer> s = new ArrayList<Integer>();
		while(bfs()){
			//while our bfs still finds free vertex//
			for(int i=1; i<=vertex;i++){
				if(left[i]==0 && dfs(i)){
					//left[i]==0 ensures edges only get added in once, as dfs edits this information//
					s.add(i);
					s.add(left[i]);
					//add edge pairs to the output//
				}
			}
			
			
			
		}
		return s;
	}
	public boolean bfs(){
		ArrayList<Integer> Q = new ArrayList<Integer>();
		for(int i=1; i<left.length;i++){
			if(left[i]==0){
				dist[i]=(double) 0;
				Q.add(i);
				//if its a free vertex add it to our list of starting points//
			}
			else{
				dist[i]=Double.POSITIVE_INFINITY;
				//otherwise dont bother with it//
			}
			
		}
		dist[0]=Double.POSITIVE_INFINITY;
		//creates "null vertex" with index zero and distance infinity to all other vertices//
		while(Q.size()!=0){
			int u = Q.remove(0);
			if(dist[u]<dist[0]){
				
				//look at neighbors of u//
				for(int i=0; i<graph[u].length;i++){
					if(graph[u][i]==1){
						if(dist[right[i-vertex]].isInfinite()){
							//if the right vertex is free//
							dist[right[i-vertex]]=dist[u]+1;
							Q.add(right[i-vertex]);
							//travel to the right vertex and then its associated left pointer,
							//then update distance//
						}				
					}
				}
			}
			
		}
		
		return !(dist[0].isInfinite());
		//since the null vertex is connected to the right side, if //
		//we can reach it (finite distance) then we have a free vertex//
		
		
	}
	public boolean dfs(int u){
		if(u!=0){
			//given a starting point//
			for(int i=0; i<graph[u].length;i++){
				if(graph[u][i]==1){
					if(dist[right[i-vertex]]==dist[u]+1){
					//if the vertex is one layer above it in the tree//
						if(dfs(right[i-vertex])==true){
						//recursively go up the bfs tree//
							right[i-vertex]=u;
							left[u]=i-vertex;
							return true;
							//if we can reach the top we have an augmenting path, take this edge//
							//note since we went two steps back up, we have thus swapped our augmenting path//
							
						}
						
						
					}
					
				}
			}
			
			dist[u]=Double.POSITIVE_INFINITY;
			//otherwise, we cannot find a shortest augmented path//
			//so stop considering these vertices//
			return false;
		}
		return true;
		
	}
}
