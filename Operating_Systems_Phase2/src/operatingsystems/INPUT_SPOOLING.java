package operatingsystems;
/*
 * The input job is loaded through an external or an independent peripheral device into the disk.This method is called Input Spooling
 * The Input Spooling is responsible to divide the Memory into equally sized Pages and later divide them into segments of variable sizes. 
 */

import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.lang.*;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;

//INPUT_SPOOLING class to carry out reading the File into the Disk.
public class INPUT_SPOOLING extends SYSTEM {
	public static Scanner readLines;
	public static int linecount = 0;
	public static String each_line = null;
	public static int Input_Words;
	public static int Output_Words;
	public static int Program_Segment_Length;
	public static int Input_Start_Index;
	public static int Output_Start_Index;
	public static int Total_Pages;
	public static int line_count = 0;

	// Method to open the File from the File System
	public void openFile() {
		try {
			readLines = new Scanner(new File(FileName));
		} catch (Exception e) {

		}
	}

	// Method to read the values from the file and parsing them into variables.
	// Here File is read line by line instead of dumping the whole file.
	public void ReadFile() {
		boolean input_flag = false;
		boolean Job_flag = false;
		boolean Fin_flag = false;
		int disk_index = 0;
		int input_line_no = 0;
		int input_count = 0;
		int job_count = 0;
		int fin_count = 0;
		// check whether the file has reached the end of the file or not
		while (readLines.hasNext()) {
			// Storing the each line of the file into a variable.
			each_line = readLines.nextLine();
			if (each_line.contains("**INPUT")) {
				input_count++;
			}
			if (input_count > 1) {
				ERROR_HANDLER.ERROR(11);
			}
			if (each_line.contains("**JOB")) {
				job_count++;
			}
			if (job_count > 1) {
				ERROR_HANDLER.ERROR(15);
			}
			if (each_line.contains("**FIN")) {
				fin_count++;
			}
			if (fin_count > 1) {
				ERROR_HANDLER.ERROR(16);
			}
			if (!each_line.contains("**INPUT")) {

			}

			linecount++;
			// Storing the values from the first line
			if (linecount == 1) {
				String Job_line = each_line;
				if (!Job_line.contains("**JOB")) {
					ERROR_HANDLER.ERROR(12);
				}
				String[] Job_line_values = Job_line.split("\\s+");

				if (!Job_line_values[1].matches("-?[0-9a-fA-F]+")) {
					ERROR_HANDLER.ERROR(103);
				}

				if (!Job_line_values[2].matches("-?[0-9a-fA-F]+")) {
					ERROR_HANDLER.ERROR(103);
				}
				Input_Words = Hex_to_Dec(Job_line_values[1]);
				Output_Words = Hex_to_Dec(Job_line_values[2]);
				Job_flag = true;
			}
			// Storing the values from the second line
			if (linecount == 2) {
				String second_line = each_line;
				String[] second_line_values = second_line.split("\\s+");
				Job_Id = Hex_to_Dec(second_line_values[0]);

				if (!second_line_values[0].matches("-?[0-9a-fA-F]+")) {
					ERROR_HANDLER.ERROR(4);
				}
				Base_Address = Hex_to_Dec(second_line_values[1]);
				if (Base_Address != 0) {
					ERROR_HANDLER.ERROR(104);
				}
				if (!second_line_values[1].matches("-?[0-9a-fA-F]+")) {
					ERROR_HANDLER.ERROR(103);
				}

				PC = Hex_to_Dec(second_line_values[2]);
				if (!second_line_values[2].matches("-?[0-9a-fA-F]+")) {
					ERROR_HANDLER.ERROR(103);
				}
				Program_length = Hex_to_Dec(second_line_values[3]);
				if (!second_line_values[3].matches("-?[0-9a-fA-F]+")) {
					ERROR_HANDLER.ERROR(103);
				}
				Trace_Flag = Hex_to_Dec(second_line_values[4]);
				if (Trace_Flag != 1 && Trace_Flag != 0) {
					ERROR_HANDLER.ERROR(107);
				}
				if (!second_line_values[4].matches("-?[0-9a-fA-F]+")) {
					ERROR_HANDLER.ERROR(103);
				}

				if (PC > Program_length) {
					ERROR_HANDLER.ERROR(2);
				}

			}
			// checks if a line contains **INPUT or not
			if (each_line.contains("**INPUT")) {
				if (each_line.length() > 7) {
					ERROR_HANDLER.ERROR(11);
				}
				input_flag = true;
				input_line_no = linecount + 1;
			}

			// Parsing each word of a file
			if (linecount != 1 && linecount != 2 && input_flag != true) {
				line_count++;
				int word_count = 0;

				while (word_count < each_line.length()) {
					if (each_line.length() > 16) {
						ERROR_HANDLER.ERROR(10);
					}
					if (each_line.length() % 4 != 0) {
						ERROR_HANDLER.ERROR(1);
					}
					// Storing the value of Each Word
					String each_word = each_line.substring(word_count, Math.min(word_count + 4, each_line.length()));
					// checks whether a word is a valid hexa or not
					if (!each_word.matches("-?[0-9a-fA-F]+")) {
						ERROR_HANDLER.ERROR(103);
					}
					word_count = word_count + 4;
					String first_word = Hex_to_Bin_8_bit(each_word.substring(0, 2));
					String second_word = Hex_to_Bin_8_bit(each_word.substring(2, 4));
					String combined_binary_word = first_word + second_word;
					DISK.DISK[disk_index] = combined_binary_word;
					disk_index++;
				}

			}

			// Reading the Input Value through the file
			if (input_line_no == linecount) {
				String inputline = each_line;
				if (inputline.length() != Input_Words * 4) {
					ERROR_HANDLER.ERROR(17);
				}
				String first_word = Hex_to_Bin_8_bit(inputline.substring(0, 2));
				String second_word = Hex_to_Bin_8_bit(inputline.substring(2, 4));
				String combined_binary_word = first_word + second_word;
				disk_index = Program_length + (Program_length % 8);
				DISK.DISK[disk_index] = combined_binary_word;
			}

		}

		if (Program_length != line_count * 4) {
			ERROR_HANDLER.ERROR(105);

		}

		if (input_flag == false) {
			ERROR_HANDLER.ERROR(13);
		}

		if (Job_flag == false) {
			ERROR_HANDLER.ERROR(12);
		}
		if (fin_count == 0) {
			ERROR_HANDLER.ERROR(14);
		}
	}

	// Closing the file
	public static void closeFile() {
		readLines.close();
	}

	// Segmenting the Disk into Program ,Input and Output Segments
	public static void Disk_Segmenting() {
		if (Program_length % 8 == 0) {
			Program_Segment_Length = (Program_length / 8);
		} else if (Program_length % 8 != 0) {
			Program_Segment_Length = (Program_length / 8) + 1;
		} else if (Program_length < 8 && Program_length > 0) {
			Program_Segment_Length = 1;
		}
		Input_Start_Index = (8 * Program_Segment_Length);
		Output_Start_Index = Input_Start_Index + 8;
		Output_Disk_Address = Output_Start_Index;
	}

}
