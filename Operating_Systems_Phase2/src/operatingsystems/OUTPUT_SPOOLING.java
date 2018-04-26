package operatingsystems;

/*
 * After the Execution of each and every Job in the System, the Input and Output values wrt to the given program must be spooled properly into 
 * the Memory.In addition to this the output spooling will display the Memory Utilization and the typical Disk Utilization.
 */
import java.io.*;
import java.lang.*;
import java.math.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class OUTPUT_SPOOLING extends SYSTEM {

	OUTPUT_SPOOLING() {

	}

	// Output Spooling of the given Input Job
	public static void OUTPUT_SPOOLING(int jobid, int sysclock, int ioclock, int pageclock, int segmentclock,
			String output) {
		try (BufferedWriter b = new BufferedWriter(new FileWriter(outfile, true))) {
			b.write("Cumulative Job Identification number :" + jobid + "(DEC)");
			b.newLine();
			b.write("Nature of termination : Normal");
			b.newLine();
			b.write("Input Data Segment for Job Id " + (jobid) + " :");
			b.newLine();
			for (int i = 0; i < INPUT_SPOOLING.Input_Words; i++) {
				b.write(CPU.Input_Segment_Words.get(i));
				b.write("(BIN)");
				b.newLine();
			}
			b.write("Output Data Segment for Job Id " + (jobid) + " :");
			b.newLine();
			for (int i = 0; i < INPUT_SPOOLING.Output_Words; i++) {
				b.write(CPU.Output_Segment_Words.get(i));
				b.write("(BIN)");
				b.newLine();
			}
			String sclock;
			int totalclock = sysclock + pageclock + segmentclock;
			sclock = Integer.toHexString(totalclock);
			b.write("Clock value at termination: " + sclock + " (HEX)");
			b.newLine();
			b.write("Run time for the job:" + (sysclock + pageclock + segmentclock) + "(DECIMAL)");
			b.newLine();
			b.write("Execution time: " + (sysclock - ioclock) + "(DECIMAL)");
			b.newLine();
			b.write("Input/output time: " + ioclock + "(DECIMAL)");
			b.newLine();
			b.write("Page fault handling time: " + pageclock + "(DECIMAL)");
			b.newLine();
			b.write("Segment fault handling time: " + segmentclock + "(DECIMAL)");
			b.newLine();
			b.write("Memory Utilization(WORDS):");
			b.newLine();
			int num_mem = (int) Memory_Numerator;
			int dec_mem = (int) Memory_Denominator;

			b.write("Ratio:" + num_mem + ":" + dec_mem);
			b.newLine();
			b.write("Percentage:" + ((Memory_Numerator / Memory_Denominator) * 100) + "%");
			b.newLine();
			b.write("Memory Utilization(FRAMES):");
			b.newLine();
			int num_mem_frame = (int) Memory_Frames_Used;
			int dec_mem_frame = (int) Memory_Frames_Available;
			b.write("Ratio:" + num_mem_frame + ":" + dec_mem_frame);
			b.newLine();
			b.write("Percentage:" + ((Memory_Frames_Used / Memory_Frames_Available) * 100) + "%");
			b.newLine();
			b.write("DISK Utilization(WORDS):");
			b.newLine();
			int num_disk = (int) Disk_Numerator;
			int dec_disk = (int) Disk_Denominator;
			b.write("Ratio:" + num_disk + ":" + dec_disk);
			b.newLine();
			b.write("Percentage:" + ((Disk_Numerator / Disk_Denominator) * 100) + "%");
			b.newLine();
			b.write("DISK Utilization(FRAMES):");
			b.newLine();
			int num_disk_frame = (int) Disk_Frames_Used;
			int dec_disk_frame = (int) Disk_Frames_Available;
			b.write("Ratio:" + num_disk_frame + ":" + dec_disk_frame);
			b.newLine();
			b.write("Percentage:" + ((Disk_Frames_Used / Disk_Frames_Available) * 100) + "%");
			b.newLine();

			b.write("Memory Fragmentation:" + Memory_Fragmentation);
			b.newLine();
			b.write("DISK Fragmentation:" + Disk_Fragmentation);
			b.newLine();
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Prints the Program ,Input and Output Segment Map table values for every
	// 15 VTU.
	public static void VtuPrint() {
		try (BufferedWriter b = new BufferedWriter(new FileWriter(outfile, true))) {
			b.write("Program Map table");
			b.newLine();
			for (int i = 0; i < INPUT_SPOOLING.Program_Segment_Length; i++) {
				if (LOADER.pcb.smt[0].pmt.get(i).frame_no != -1) {
					b.write("PageNo:" + LOADER.pcb.smt[0].pmt.get(i).page_no + "  " + "FrameNo:"
							+ LOADER.pcb.smt[0].pmt.get(i).frame_no);
					b.newLine();
				}
			}
			b.newLine();
			if (LOADER.pcb.smt[1].pmt.get(0).frame_no != -1) {
				b.write("Input Segment Map table");
				b.newLine();
				b.write("PageNo:" + LOADER.pcb.smt[1].pmt.get(0).page_no + "  " + "FrameNo:"
						+ LOADER.pcb.smt[1].pmt.get(0).frame_no);
				b.newLine();
			}
			b.newLine();
			if (LOADER.pcb.smt[2].pmt.get(0).frame_no != -1) {
				b.write("Output Segment Map table");
				b.newLine();
				b.write("PageNo:" + LOADER.pcb.smt[2].pmt.get(0).page_no + "  " + "FrameNo:"
						+ LOADER.pcb.smt[2].pmt.get(0).frame_no);
				b.newLine();
			}
			b.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Handles the errors caused due to Spooling
	public static void Spooling_Error(int jobid, int sysclock, int ioclock, int pageclock, int segmentclock,
			String error) {
		try (BufferedWriter b = new BufferedWriter(new FileWriter(outfile))) {
			b.write("Cumulative Job Identification number :" + jobid + "(DEC)");
			b.newLine();
			b.write("Nature of termination : Abnormal");
			b.newLine();
			b.write(error);
			b.newLine();
			b.write("Input Data Segment for Job Id " + (jobid) + " :");
			b.newLine();
			for (int i = 0; i < CPU.Input_Segment_Words.size(); i++) {
				b.write(CPU.Input_Segment_Words.get(i));
				b.write("(BIN)");
				b.newLine();
			}
			b.write("Output Data Segment for Job Id " + (jobid) + " :");
			b.newLine();
			for (int i = 0; i < CPU.Output_Segment_Words.size(); i++) {
				b.write(CPU.Output_Segment_Words.get(i));
				b.write("(BIN)");
				b.newLine();
			}
			String sclock;
			int totalclock = sysclock + pageclock + segmentclock;
			sclock = Integer.toHexString(totalclock);
			b.write("Clock value at termination: " + sclock + " (HEX)");
			b.newLine();
			b.write("Run time for the job:" + (sysclock + pageclock + segmentclock) + "(DECIMAL)");
			b.newLine();
			b.write("Execution time: " + (sysclock - ioclock) + "(DECIMAL)");
			b.newLine();
			b.write("Input/output time: " + ioclock + "(DECIMAL)");
			b.newLine();
			b.write("Page fault handling time: " + pageclock + "(DECIMAL)");
			b.newLine();
			b.write("Segment fault handling time: " + segmentclock + "(DECIMAL)");
			b.newLine();
			b.write("Memory Utilization(WORDS):");
			b.newLine();
			int num_mem = (int) Memory_Numerator;
			int dec_mem = (int) Memory_Denominator;
			b.write("Ratio:" + num_mem + ":" + dec_mem);
			b.newLine();
			b.write("Percentage:" + ((Memory_Numerator / Memory_Denominator) * 100) + "%");
			b.newLine();
			b.write("Memory Utilization(FRAMES):");
			b.newLine();
			int num_mem_frame = (int) Memory_Frames_Used;
			int dec_mem_frame = (int) Memory_Frames_Available;
			b.write("Ratio:" + num_mem_frame + ":" + dec_mem_frame);
			b.newLine();
			b.write("Percentage:" + ((Memory_Frames_Used / Memory_Frames_Available) * 100) + "%");
			b.newLine();
			b.write("DISK Utilization(WORDS):");
			b.newLine();
			int num_disk = (int) Disk_Numerator;
			int dec_disk = (int) Disk_Denominator;
			b.write("Ratio:" + num_disk + ":" + dec_disk);
			b.newLine();
			b.write("Percentage:" + ((Disk_Numerator / Disk_Denominator) * 100) + "%");
			b.newLine();
			b.write("DISK Utilization(FRAMES):");
			b.newLine();
			int num_disk_frame = (int) Disk_Frames_Used;
			int dec_disk_frame = (int) Disk_Frames_Available;
			b.write("Ratio:" + num_disk_frame + ":" + dec_disk_frame);
			b.newLine();
			b.write("Percentage:" + ((Disk_Frames_Used / Disk_Frames_Available) * 100) + "%");
			b.newLine();
			b.write("Memory Fragmentation:" + Memory_Fragmentation);
			b.newLine();
			b.write("DISK Fragmentation:" + Disk_Fragmentation);
			b.newLine();
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}
}
