package operatingsystems;

/*
Description: The ERROR_HANDLER subsystem handles warnings,errors and special conditions.Whatever the error or a warning that we can encounter 
during the runtime of the system will be trapped in this subsystem and an appropriate error number will be generated .In case of an error 
the system dumps the memory upto 256 words and terminates itself. 
 */




import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//ERROR_HANDLER Subsystem which catches all the errors and warnings thrown in the system 
public class ERROR_HANDLER extends SYSTEM {
	
	 public static int Error_Number;
	 public static String message = "";

	 // The ERROR method will take the parameter an error number
	 public static void ERROR(int err) {
	  try (BufferedWriter b = new BufferedWriter(new FileWriter(outfile, true))) {
	   // If the error number is greater than 100 then it is a warning
	   if (err > 100) {
	    switch (err) {
	     
	    case 101:
	    	message = "Input File not found";
	    	outputerror(Job_Id,System_Clock,IO_Clock,message);
	    	System.exit(0);
	      
	      break;
	     case 102:
	    	 	message = "Invalid loader Format:Job Lines should not exceed 16 hex digits";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);
	      break;
	     case 103:
	    	 	message = "Invalid loader Format:Invalid Hex code in input";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);
	      
	      break;
	     case 104:
	    	 	message = "Invalid base address(Should be 0 for the single batch)";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);

	      
	      break;
	     case 105:
	    	 	message = "Length of the Input Job doesn't match the specified size";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);
		    	
	      break;  
	     case 106:
	    	 	message = "Warning:Invalid Job Id: Should be 1 for the simple batch";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	//System.exit(0);

	      break; 
	     case 107:
	    	 	message = "Invalid Trace Flag";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);

	      break; 
	      
	    }
	   }
	   // If the error number is less than 100 then it is an error
	   else if (err < 100) {
	    switch (err) {
	     case 1:
	    	 	message = "The length of one of the words is not equal to 4";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    System.exit(0);
	     
	      break;
	     case 2:
	    	 message = "PC value exceeds the length of the Program";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);
	      break;
	     case 3:
	    	 message = "Only Integer values are Allowed as Inputs";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);
	     
	      break;
	     case 4:
	    	 	message = "Invalid Input value(Should be only decimals)v";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);
	     
	      break;
	     
	     case 5:
	    	 	message = "Invalid opcode";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0);
	      
	      break;
	     case 6:
	    	 	message = "Suspected Infinite Loop";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	     case 7:
	    	 	message = "Effective Address Should not be greater than the Program length";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	     case 8:
	    	 	message = "Illegal Instruction Trap ,Stack Overflow";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	     case 9:
	    	 	message = "Illegal Instruction Trap ,Stack Underflow";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	      
	     case 10:
	    	 	message = "Invalid loader Format:Job Lines should not exceed 16 hex digits";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break; 
	      
	     case 11:
	    	 	message = "More than 1 **Input";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	      
	     case 12:
	    	 	message = "Missing **JOB";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	     case 13:
	    	 	message = "Missing **INPUT";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	     case 14:
	    	 	message = "Missing **FIN";
		    	outputerror(Job_Id,System_Clock,IO_Clock,message);
		    	System.exit(0); 
	      break;
	    }
	   }
	  } catch (IOException e) {
	   e.printStackTrace();
	  }

	 }
	}


