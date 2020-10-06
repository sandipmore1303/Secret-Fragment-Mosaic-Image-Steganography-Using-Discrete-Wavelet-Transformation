/**
 * @(#)selectionsort.java
 *
 *
 * @author 
 * @version 1.00 2009/12/11
 */


public class selectionsort {
	 
   
       double [] x;
      int[] INDEX;
     
    public selectionsort(double arr[]) {
    	this.x=arr;
        this.INDEX=new int[arr.length];
        int i=0,j=0;
         for (  i=0; i<x.length; i++) {
             this.INDEX[i]=i;
         }
         
       for ( i=0; i<x.length-1; i++) {
        int maxIndex = i;      // Index of largest remaining value.
        for ( j=i+1; j<x.length; j++) {
            if (x[maxIndex] < x[j]) {
                maxIndex = j;  // Remember index of new minimum
            }
        }
       if (maxIndex != i)
        { 
            //...  Exchange current element with smallest remaining.
            double temp = x[i];
            x[i] = x[maxIndex];
            x[maxIndex] = temp;
            int temp_int=INDEX[i];
            INDEX[i]=INDEX[maxIndex];
            INDEX[maxIndex]=temp_int;
            
        }
      }
        
    }
    
         void dispaly(){
           for (int i = 0; i < this.x.length; i++)
        {
           
         System.out.println("  "+x[i]+"  "+INDEX[i]);
        }
        
       }
    
    public static void MAIN(String args[]) {
       double arr[]={4,3,2,5}; 
       
        
    	int i,j;
    	double  temp ;
    	
    	//x[] =i;
    	System.out.println("\nBefore Sort\n");
        
        
       
    
    
     System.out.println("\nAfter Sort\n");
         selectionsort mm=new selectionsort(arr); 
         mm.dispaly();
         
         
         
}
    
    
}
        