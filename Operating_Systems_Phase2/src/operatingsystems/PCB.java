package operatingsystems;
/*
 * PCB :Process Control Block or Process Context Block is used to represent every single job in the system.(Though
 * we are using a single Job for Phase 2 it should also be represented by PCB).
 */

import java.io.*;
import java.lang.*;
import java.math.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class PCB extends SYSTEM {
	public static PMT program_map_table;
	public static PMT Input_map_table;
	public static PMT Output_map_table;
	public static SMT Segment_Map_Table;

	SMT[] smt = new SMT[3];
	static ArrayList<Integer> FreeFrames = new ArrayList<Integer>();
}

// Class to Initialize the Free Memory Bit Vector
/*
class FMBV {
	public static boolean[] FMBV = new boolean[32];

	public static void FMBV_Initialize() {
		FMBV[4] = true;
		PCB.FreeFrames.add(5);
		FMBV[7] = true;
		PCB.FreeFrames.add(8);
		FMBV[9] = true;
		PCB.FreeFrames.add(10);
		FMBV[16] = true;
		PCB.FreeFrames.add(17);
		FMBV[19] = true;
		PCB.FreeFrames.add(20);
		FMBV[30] = true;
		PCB.FreeFrames.add(31);

	}
}
*/