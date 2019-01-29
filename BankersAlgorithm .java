import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Eric Combee on 11/14/18
 *  Description: A program to demonstrate Bankers Algorithm.
 *               It will take N processes and M resources types
 *                where N<10 && M<10
 */
public class BankersAlgorithm {
	
	
    private static int np; // number of processes
    private static int nr; // number of resources
    private static int mainCrt =0;
    private static int complete =0;
    
    private static int []initial; // initial state
    private static int max[][];
    private static int allocate[][];
    private static int need[][];
    private static int [] allocatedResources;
	private static int [] ttlResorces;
	private static int [] holdInitial;
	private static int [] processSeq;
	
	private static Boolean multiSolution = true;
	private static Boolean firstTimeThru = true;
	private static Boolean []finished;
	private static Boolean []knownSolutions;
	
	
	/*
	 * setUpFile method loads and sets the variables   *
	 *  Here is an example of how my files are lay out * 
	 *  											   *
	 *  5  # processes								   *
     *  4  # recourses  						       *
     *  | max | |alloc.| |ttl res                      *
     *  3,2,2,2,2,1,1,0,5,6,8,4                        *
     *  2,1,1,2,0,1,1,0,5,6,8,4                        *
     *  1,1,3,1,1,1,1,0,5,6,8,4                        *
     *  3,4,2,2,1,1,2,1,5,6,8,4                        *
     *  2,4,1,4,1,2,1,1,5,6,8,4                        *
	 ***************************************************/
	
	public void setupFile() throws NumberFormatException, IOException {

		Scanner ec = new Scanner(System.in);
        String fileName;
        System.out.println("Enter in the file name:(Name only and txt file only)");
        fileName = ec.nextLine();
        
        System.out.println("      ===================== ");
        System.out.println("      = Bankers Algorithm =");
        System.out.println("      ===================== ");
        
        FileReader diskIn = new FileReader(fileName+".txt");
        BufferedReader inFile = new BufferedReader(diskIn);
        StringTokenizer tok;
        String rec;
        System.out.println();
        
        int i=0;
        int p =0;
   
        np = Integer.parseInt(inFile.readLine());
        nr = Integer.parseInt(inFile.readLine());
        
        
        initial = new int[nr];
        holdInitial = new int[nr];
        max = new int[np][nr];
        allocate = new int[np][nr];
        need = new int[np][nr];
        finished = new Boolean[np];
        knownSolutions = new Boolean[np];
        allocatedResources = new int[nr];
        ttlResorces = new int [nr];
        processSeq = new int[np];
        
        while((rec = inFile.readLine()) != null && i < np){

            tok = new StringTokenizer(rec,",");
            
            for(int j=0;j<nr;j++) {
             max[i][j] = Integer.parseInt(tok.nextToken());
             
            }
            for(int j=0;j<nr;j++) {
                allocate[i][j] = Integer.parseInt(tok.nextToken());
                need[i][j] = max[i][j] - allocate[i][j]; 
                
               }
            for(int j=0;j<nr;j++) {
                ttlResorces[j] = Integer.parseInt(tok.nextToken());
                
               }
            
          i++;
    }
    inFile.close();
    
        int process =0;
         /*
          *  Used to load are allocatedResources array
          */
        
        
        while(process < nr) {
        
          for(int j=0;j<np;j++) {
        	  allocatedResources[p] += allocate[j][p]; 
        	  
        	  
          }
          p++;
          process++;
        }
         /*
          * used to load initial and holdIntial array
          */
        for(int j=0;j<ttlResorces.length;j++) {
        	initial[j] = ttlResorces[j] - allocatedResources[j];
        	holdInitial[j] = initial[j];
        	
        }
        
         /*
          *  used to load finished and knowSolutions array
          */
          for(int a=0;a<np;a++) {
        	  finished[a] = false;
        	  knownSolutions[a] = false;
          }
    
	}
	
	/********************************************
	 * chechSafeSeq() checks to see if there is 
	 * any solutions before we continue.
	 *********************************************/
	public boolean checkSafeSeq() {
		
		int crt = 0;
		int i   = 0;
		
		while(crt != nr && i < np) {
			crt = 0;
		for(int j=0;j<nr;j++) {
			if(need[i][j] <= initial[j]) {
				crt++;
			}
			
		   }
		    i++;
		}
	  
		if(i < np)
			return true;
			
		
		return false;
	}
	/************************************************
	 * 
	 * printMatrix() displays the content of the file.
	 * It takes the max,allocate, and need array's
	 * and creates string duplicates for display
	 * purposes only.
	 * 
	 ************************************************/
	//===============print===============
	public void printMatrix() {
		
		String maxStr     = "";
		String allStr     = "";
		String needStr    = "";
		String ttlReStr   = "";
		String initialStr = "";
		String allReStr   = "";
		
		System.out.println("Pn  Max Claim   Allocation    Need");
		System.out.println("==================================");
		
		for(int j=0;j<np;j++) {
			for(int k=0;k<nr;k++) {
				maxStr += Integer.toString(max[j][k]);
				allStr += Integer.toString(allocate[j][k]);
				needStr += Integer.toString(need[j][k]);
				
			}
			System.out.printf("P%d %6s  %11s  %10s",j,maxStr,allStr,needStr);
			
			maxStr ="";
			allStr = "";
			needStr = "";
			
			System.out.println();
		}
		
		for(int j=0;j<initial.length;j++) {
			ttlReStr += Integer.toString(ttlResorces[j])+",";
		    allReStr += Integer.toString(allocatedResources[j])+",";
		    initialStr += Integer.toString(initial[j])+",";
		    
		}
		
		
		System.out.println("\nTotal number of resources: ("+ttlReStr.substring(0,ttlReStr.length()-1)+")");
		System.out.println("Total number of Allocated resources: ("+allReStr.substring(0,allReStr.length()-1)+")");
		System.out.println("Total number of resources available in this initial state is: ("+initialStr.substring(0,initialStr.length()-1)+")");
		System.out.println();
		System.out.println();
	}
    /*****************************************
     * 
     *  check() takes in a N(int i) and
     *  searches to see if need[N] <= allocate[N].
     *  
     *  It also takes a look to see if there
     *  are multiples solutions.
     * 
     *****************************************/
	public Boolean check(int i) {
		
		String intStr = "";
		String needStr = "";
	
		 /*
		  * convert to string so that it can 
		  * compare to make sure that need <= initial
		  */
		    for(int k=0;k<nr;k++) {
				 intStr += initial[k];
				 needStr += need[i][k];
					
			}
			 
			
			if(Integer.parseInt(needStr)<= Integer.parseInt(intStr)) {
				
				/*************************************
				 * 
				 * This is triggered on the first run
				 * through the file. 
				 * 
				 **************************************/
				if(firstTimeThru && !knownSolutions[i]) {  
					knownSolutions[i] = true;
					firstTimeThru = false;
					return true;
			     /**********************************************
			      * Triggered while still on the first run thru 
			      * all the process.
			      **********************************************/
				}else if(!firstTimeThru && !knownSolutions[i]) {  
					
					return true;
				/************************************************
				 * 
				 * Triggered on the second run through the process
				 * to find another solution.
				 * 
				 * It checks if it is the first time thru and
				 * if the process it is looking at has already
				 * been used.
				 * 
				 ************************************************/
				}else if(!firstTimeThru && knownSolutions[i]){  
					return true;
				}
				
			}
			
			
			
		return false;
		
	    
	}
   /******************************************
    * 
    *  algorithm() is the method that handles 
    *  Bankers Algorithm.
    *  
    ******************************************/
	public void algorithm() throws NumberFormatException, IOException {
		int timesThruLoop =0;
	 
       while(complete < np && multiSolution) {
        	int i =0;
        	for(i=0;i<np;i++) {
        		
        			if(check(i) && !finished[i]) {
        				System.out.print("P"+(i)+" released all that it holds. Available is: ");
        				
        				for(int j=0;j<nr;j++) {
        				initial[j] = (initial[j] - need[i][j]) + max[i][j];
        				System.out.print(initial[j]+",");
        				}
        				System.out.println();
        				
        				complete++;
        				finished[i] = true;
        				processSeq[complete-1] = i;
        			}
        	    }
        	
        	    
        	       timesThruLoop++;
        	       
        	    if(timesThruLoop == np) 
        		    multiSolution = false;
        		    
        		
        }
       if(multiSolution) {
       System.out.print("\nSafe Sequence: < ");
       for(int j=0;j<processSeq.length;j++) {
    	   if(j != processSeq.length-1)
    	       System.out.print("P"+processSeq[j]+",");
    	   else
    		   System.out.print("P"+processSeq[j]);
       }
       System.out.print(" >\n");
       }
       
      
       
	
       complete =0;
       for(int j=0;j<finished.length;j++) {
    	   finished[j] = false;
    	   
       }
       
       /*
        * reset the initial array before it
        * runs again
        */
       for(int j=0;j<initial.length;j++)
    	   initial[j] = holdInitial[j];
    	   
       
      
       mainCrt++;
       firstTimeThru = true;
       
       
       System.out.println();
		
		
	}

    public static void main(String[] args) throws IOException {
        
        BankersAlgorithm bank = new BankersAlgorithm();
        
        bank.setupFile();
        
       if(!bank.checkSafeSeq()) {
    	   System.out.println("There is no safe squence!");
    	   return;
       }
        
        bank.printMatrix();
        
        while(mainCrt < np && multiSolution) {
        bank.algorithm();
        }
    }
}
