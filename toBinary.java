/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sam
 */
public class toBinary {
    int size=0;
    int B[];
    int B_8bit[];
    int B_7bit[];
      toBinary(int d)
    {//calcualtae size of poosible binary value
        int sz=0;
        int X=d;
        do{X=(int)(Math.ceil(X/2));
            sz++;
        }while(X !=0);
                
        this.size=sz;
        B=new int[this.size];
        X=d;
        
        do{B[--sz]=X%2;
            X=(int)(Math.ceil(X/2));         
        }while(X !=0);   
        
        B_8bit=new int[8];
         int k=0;
           for(int kk=8-this.size;kk<8;kk++){
             B_8bit[kk]=this.B[k++]; 
             //System.out.println("tb1[kk]="+tb1[kk]+"\tkk="+kk);
          }
           
         B_7bit=new int[7];
          //k=0;
           for(int kk=0;kk<7;kk++){
             this.B_7bit[kk]=this.B_8bit[kk+1]; 
             //System.out.println("tb1[kk]="+tb1[kk]+"\tkk="+kk);
          }  
           
             
    }
    int []  getBinaryValue()
      {
        return this.B;  
      }
      public static void MAIN (String args[])   {
            toBinary tb=new toBinary(255);
            int tb1[]=new int[8];
            
            for(int i=0;i<tb.size;i++){
             //tb1[i]=tb.B[i++]; 
            // System.out.println("t[i]="+tb.B[i]+"\ti="+i);
          }
            
            int k=0;
           for(int kk=8-tb.size;kk<8;kk++){
             tb1[kk]=tb.B[k++]; 
             //System.out.println("tb1[kk]="+tb1[kk]+"\tkk="+kk);
          }
             
             for(int kk=0;kk<8;kk++){
             //tb1[kk]=tb.B[k++]; 
             //System.out.println("tb1[kk]="+tb.B_8bit[kk]+"\tkk="+kk);
          }
            for(int kk=0;kk<7;kk++){
             //tb1[kk]=tb.B[k++]; 
             System.out.println("tb1[kk]="+tb.B_7bit[kk]+"\tkk="+kk);
          }
          
       System.out.println((int)(121.0/10.0));
        
}
}
