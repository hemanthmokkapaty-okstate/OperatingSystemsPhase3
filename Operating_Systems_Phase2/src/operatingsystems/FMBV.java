package operatingsystems;

public class FMBV extends SYSTEM {
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
