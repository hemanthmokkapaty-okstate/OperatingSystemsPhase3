package operatingsystems;

/*
Description:The Loader will be responsible for transferring the pages from the secondary memory disk to the main memory Memory.Basically the loader performs 
all the Page Replacement functions and generates Page Faults.The technique we used here for the Page Replacement is Second Chance Algorithm. 
 */

import java.io.*;
import java.lang.*;
import java.math.*;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

//LOADER class that loads the data from the File System
public class LOADER extends SYSTEM {

	public static PCB pcb = new PCB();
	public static int PC_Page_Replacement;
	public static int EA_Page_Replacement;
	public static int Page_Fault_Clock = 0;
	public static int Segment_Fault_Clock = 0;
	public static String Temporary[] = new String[8];

	// ArrayList to see all the pages in the memory
	static ArrayList<Integer> pagesInMemory = new ArrayList();

	LOADER() {

	}

	// Load the First page into the Memory
	public static void First_Page_Loading() {

		if (PC < 8) {
			First_Page_Number = 0;
			First_Page_Index = 0;
		} else if (PC > 8) {
			First_Page_Number = (PC / 8);
			First_Page_Index = (PC / 8) * 8;
		}
		// Obtain the frame number
		int frameNumber = PCB.FreeFrames.get(0);
		First_Frame_Index = frameNumber * 8;
		pagesInMemory.add(First_Page_Number);
		// Copying the contents of the disk into the memory
		for (int i = 0; i < 8; i++) {
			MEMORY.MEM[First_Frame_Index] = DISK.DISK[First_Page_Index];
			First_Frame_Index++;
			First_Page_Index++;
		}
		Total_Frames = Math.min(6, (INPUT_SPOOLING.Program_Segment_Length) + 2);
		PC_Page_Number = First_Page_Number;
		PC_Frame_Number = frameNumber;
		LOADER.pcb.smt[0] = new SMT();
		// Initializing the Program Segment
		for (int i = 0; i < INPUT_SPOOLING.Program_Segment_Length; i++) {
			LOADER.pcb.smt[0].pmt.add(new PMT(i, -1, 0, 0, 0));
		}

		LOADER.pcb.smt[0].pmt.get(PC_Page_Number).page_no = PC_Page_Number;
		LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no = frameNumber;
		LOADER.pcb.smt[0].pmt.get(PC_Page_Number).valid_bit = 1;
		LOADER.pcb.smt[0].pmt.get(PC_Page_Number).ref_bit = 0;
		LOADER.pcb.smt[0].pmt.get(PC_Page_Number).dirty_bit = 0;
		PCB.FreeFrames.remove(0);

	}

	// Loading the Page that contains the Input value in the Disk
	public static String Input_Loading() {
		Page_Faults++;
		Segment_Fault_Clock = Segment_Fault_Clock + 5;
		int input_page_Index = INPUT_SPOOLING.Input_Start_Index;
		int pageNumber1 = input_page_Index / 8;
		int frame_Number = PCB.FreeFrames.get(0);
		int input_frame_index = frame_Number * 8;
		for (int i = 0; i < 8; i++) {
			MEMORY.MEM[input_frame_index] = DISK.DISK[input_page_Index];
			input_frame_index++;
			input_page_Index++;
		}
		String input_value = MEMORY.MEM[frame_Number * 8];
		FMBV.FMBV[frame_Number - 1] = false;
		LOADER.pcb.smt[1] = new SMT();
		LOADER.pcb.smt[1].pmt.add(new PMT(1, -1, 0, 0, 0));
		LOADER.pcb.smt[2] = new SMT();
		LOADER.pcb.smt[2].pmt.add(new PMT(1, -1, 0, 0, 0));
		LOADER.pcb.smt[1].pmt.get(0).page_no = INPUT_SPOOLING.Input_Start_Index / 8;
		LOADER.pcb.smt[1].pmt.get(0).frame_no = frame_Number;
		LOADER.pcb.smt[1].pmt.get(0).valid_bit = 1;
		LOADER.pcb.smt[1].pmt.get(0).ref_bit = -1;
		LOADER.pcb.smt[1].pmt.get(0).dirty_bit = 0;
		pagesInMemory.add(pageNumber1);

		PCB.FreeFrames.remove(0);

		return input_value;
	}

	// Method to calculate the new effective Address
	public static int New_Calculated_Address(int address) {
		int pagesize = address / 8;
		EA_Frame_Number = LOADER.pcb.smt[0].pmt.get(pagesize).frame_no;
		int new_address = (EA_Frame_Number * 8) + (address % 8);
		return new_address;
	}

	// Method to handle the Page Fault if it is caused by PC
	public static void pagefault_PC(int address)

	{
		PC_Page_Replacement = address;
		int page_number = (address / 8);
		PC_Page_Number = page_number;
		int page_index = page_number * 8;
		PC_Page_Index = page_index;
		int diskAddress = 0;
		int memoryAddress = 0;
		int frameNumber = 0;
		// Check if there are any free frames available in the memory
		if (PCB.FreeFrames.size() > 0) {
			// page fault is being handled
			LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no = PCB.FreeFrames.get(0);
			frameNumber = LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no;
			memoryAddress = frameNumber * 8;
			diskAddress = PC_Page_Number * 8;

			PCB.FreeFrames.remove(0);
			pagesInMemory.add(PC_Page_Number);

			// copy to memory
			for (int i = 0; i < 8; i++) {
				MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
				memoryAddress++;
				diskAddress++;
			}

			PC_Frame_Number = frameNumber;

		}

		else {
			// If there is no frame available then go the second chance page
			// replacement algorithm
			Page_Faults++;
			Page_Fault_Clock = Page_Fault_Clock + 10;
			VtuClock = VtuClock + 10;

			for (int j = 0; j < pagesInMemory.size(); j++)

			{
				if (pagesInMemory.get(j) > INPUT_SPOOLING.Program_Segment_Length - 1) {
					if (pagesInMemory.get(j) > INPUT_SPOOLING.Program_Segment_Length - 1 + INPUT_SPOOLING.Input_Words) {
						int frame_value2 = LOADER.pcb.smt[2].pmt
								.get(pagesInMemory.get(j) - INPUT_SPOOLING.Program_Segment_Length).frame_no;
						LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no = frame_value2;
						LOADER.pcb.smt[2].pmt
								.get(pagesInMemory.get(j) - INPUT_SPOOLING.Program_Segment_Length).frame_no = -1;

						// copy the values into disk

						int diskAddress1;
						int memoryAddress2;

						diskAddress1 = pagesInMemory.get(j) * 8;
						memoryAddress2 = frame_value2 * 8;

						for (int i = 0; i < 8; i++) {
							DISK.DISK[diskAddress1] = MEMORY.MEM[memoryAddress2];
							memoryAddress2++;
							diskAddress1++;
						}
						memoryAddress = frame_value2 * 8;
						diskAddress = PC_Page_Number * 8;

						// copy to memory
						for (int i = 0; i < 8; i++) {
							MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
							memoryAddress++;
							diskAddress++;
						}
						pagesInMemory.remove(j);
						pagesInMemory.add(PC_Page_Number);
						PC_Frame_Number = frame_value2;
					} else {
						int frame_value2 = LOADER.pcb.smt[1].pmt.get(0).frame_no;

						LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no = frame_value2;
						LOADER.pcb.smt[1].pmt.get(0).frame_no = -1;
						// copy to disk
						int diskAddress1;
						int memoryAddress2;

						diskAddress1 = pagesInMemory.get(j) * 8;
						memoryAddress2 = frame_value2 * 8;

						for (int i = 0; i < 8; i++) {
							DISK.DISK[diskAddress1] = MEMORY.MEM[memoryAddress2];
							memoryAddress2++;
							diskAddress1++;
						}
						memoryAddress = frame_value2 * 8;
						diskAddress = PC_Page_Number * 8;

						// copy to memory
						for (int i = 0; i < 8; i++) {
							MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
							memoryAddress++;
							diskAddress++;
						}
						pagesInMemory.remove(j);
						pagesInMemory.add(PC_Page_Number);
						PC_Frame_Number = frame_value2;

					}
				} else

				{
					{

						// get frame number of first page of pages of memory
						int frame_value = LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).frame_no;
						// assign the frame number to PC_Page number

						LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no = frame_value;
						LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).frame_no = -1;
						// copy to disk

						int diskAddress1;
						int memoryAddress2;

						diskAddress1 = pagesInMemory.get(j) * 8;
						memoryAddress2 = frame_value * 8;

						for (int i = 0; i < 8; i++) {
							DISK.DISK[diskAddress1] = MEMORY.MEM[memoryAddress2];
							memoryAddress2++;
							diskAddress1++;
						}

						memoryAddress = frame_value * 8;
						diskAddress = PC_Page_Number * 8;

						// copy to memory
						for (int i = 0; i < 8; i++) {
							MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
							memoryAddress++;
							diskAddress++;
						}

						pagesInMemory.remove(j);
						pagesInMemory.add(PC_Page_Number);
						PC_Frame_Number = frame_value;

					}

				}
				j = pagesInMemory.size();

			}

		}

	}

	// Handles the Page_Faults that are caused due to Effective Address
	public static void pagefaut_EA(int address) {
		Page_Faults++;
		Page_Fault_Clock = Page_Fault_Clock + 10;
		VtuClock = VtuClock + 10;
		PC_Page_Replacement = address;
		int page_number = (address / 8);
		PC_Page_Number = page_number;
		int page_index = page_number * 8;
		PC_Page_Index = page_index;
		int diskAddress = 0;
		int memoryAddress = 0;
		int frameNumber = 0;
		// Check if there are any free frames available in the memory
		if (PCB.FreeFrames.size() > 0) {
			// page fault
			LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no = PCB.FreeFrames.get(0);
			frameNumber = LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no;
			memoryAddress = frameNumber * 8;
			diskAddress = PC_Page_Number * 8;

			PCB.FreeFrames.remove(0);
			pagesInMemory.add(PC_Page_Number);

			// copy to memory
			for (int i = 0; i < 8; i++) {
				MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
				memoryAddress++;
				diskAddress++;
			}
			EA_Frame_Number = frameNumber;
		}

		else {
			for (int j = 0; j < pagesInMemory.size(); j++)

			{
				if (LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).ref_bit == 0)

				{
					// get frame number of first page of pages of memory
					int frame_value = LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).frame_no;
					// assign the frame number to PC_Page number
					LOADER.pcb.smt[0].pmt.get(PC_Page_Number).frame_no = frame_value;
					LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).frame_no = -1;
					pagesInMemory.remove(j);
					pagesInMemory.add(PC_Page_Number);

					// copy to disk

					int diskAddress1;
					int memoryAddress2;

					diskAddress1 = pagesInMemory.get(j) * 8;
					memoryAddress2 = frame_value * 8;

					for (int i = 0; i < 8; i++) {
						DISK.DISK[diskAddress1] = MEMORY.MEM[memoryAddress2];
						memoryAddress2++;
						diskAddress1++;
					}
					memoryAddress = frame_value * 8;
					diskAddress = PC_Page_Number * 8;

					// copy to memory
					for (int i = 0; i < 8; i++) {
						MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
						memoryAddress++;
						diskAddress++;
					}

					EA_Frame_Number = frame_value;

					j = pagesInMemory.size();
				}
			}
		}

	}

	// Handles the segment fault caused due to the output Segment
	public static void Output_Segment_Fault(int address, String output) {
		Page_Faults++;
		Segment_Fault_Clock = Segment_Fault_Clock + 5;
		VtuClock = VtuClock + 5;
		PC_Page_Replacement = address;
		int page_number = (address / 8);
		PC_Page_Number = page_number;
		int page_index = page_number * 8;
		PC_Page_Index = page_index;
		int diskAddress = 0;
		int memoryAddress = 0;
		int frameNumber = 0;
		// Check if the memory has any free frames available or not.
		if (PCB.FreeFrames.size() > 0) {

			LOADER.pcb.smt[2].pmt.get(0).frame_no = PCB.FreeFrames.get(0);
			frameNumber = LOADER.pcb.smt[2].pmt.get(0).frame_no;
			memoryAddress = frameNumber * 8;
			diskAddress = PC_Page_Number * 8;
			PCB.FreeFrames.remove(0);
			pagesInMemory.add(PC_Page_Number);
			// copy to memory
			for (int i = 0; i < 8; i++) {
				MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
				memoryAddress++;
				diskAddress++;
			}
			EA_Frame_Number = frameNumber;
		}

		else {
			for (int j = 0; j < pagesInMemory.size(); j++) {
				if (LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).ref_bit == 0) {
					int frame_value = LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).frame_no;
					LOADER.pcb.smt[2].pmt.get(0).page_no = page_number;
					LOADER.pcb.smt[2].pmt.get(0).frame_no = frame_value;
					LOADER.pcb.smt[2].pmt.get(0).valid_bit = 1;
					LOADER.pcb.smt[2].pmt.get(0).ref_bit = -1;
					LOADER.pcb.smt[2].pmt.get(0).dirty_bit = 0;
					LOADER.pcb.smt[0].pmt.get(pagesInMemory.get(j)).frame_no = -1;
					pagesInMemory.remove(j);
					pagesInMemory.add(PC_Page_Number);

					int diskAddress1;
					int memoryAddress2;

					diskAddress1 = pagesInMemory.get(j) * 8;
					memoryAddress2 = frame_value * 8;

					for (int i = 0; i < 8; i++) {
						DISK.DISK[diskAddress1] = MEMORY.MEM[memoryAddress2];
						memoryAddress2++;
						diskAddress1++;
					}
					memoryAddress = frame_value * 8;
					Output_Memory_Address = memoryAddress;
					diskAddress = PC_Page_Number * 8;

					for (int i = 0; i < 8; i++) {
						MEMORY.MEM[memoryAddress] = DISK.DISK[diskAddress];
						memoryAddress++;
						diskAddress++;
					}

					EA_Frame_Number = frame_value;

					j = pagesInMemory.size();

				}

			}

		}

	}

}
