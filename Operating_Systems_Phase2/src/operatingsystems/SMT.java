package operatingsystems;

/*
 * SMT or Segment Map table is used to point to the Segments that are pointing to the disk.The segments present here are Program Segment,
 * Input Segment and Output segment.
 */

import java.util.ArrayList;

public class SMT extends SYSTEM {

	public int segment_code;
	public int pmt_reference;
	public ArrayList<PMT> pmt = new ArrayList();

}
