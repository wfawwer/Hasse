import java.io.IOException;

public class generator {
	public static void main (String [] args)throws IOException{
		int[] s ={1,2,1,3,1,4,1,5,1,6,2,8,2,11,3,7,3,9,4,8,4,10,5,9,5,11,6,7,6,10,7,11,7,12,8,13,9,14,10,15,11,16,2,14,2,15,3,15,3,16,4,12,4,16,5,12,5,13,6,13,6,14,12,14,14,16,16,13,13,15,15,12};
		for(int i=0; i<s.length;i++){
			if(i%2==0){
				System.out.print("new Rel("+s[i]+","+s[i+1]+"), ");
			}
			
			
		}
		
		
		
	}
}
