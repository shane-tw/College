import java.util.Scanner;

class Main {
	// DCOM1 B - Shane O Donovan - R00141078
	static Scanner keyboard = new Scanner(System.in);
	static final double PHOTO_COST = 10;
	static final double EXTRA_ADULT_COST = 2.50;
	static final double INFANT_COST = 0;
	static final double TODDLER_COST = 5;
	static final double PRESCHOOLER_COST = 7.50;
	static final int NUM_OF_FREE_ADULTS = 2;
	static final int INFANT_AGE_MAX = 1;
	static final int TODDLER_AGE_MIN = 1;
	static final int TODDLER_AGE_MAX = 5;
	static final int PRESCHOOLER_AGE_MIN = 5;
	static final int PRESCHOOLER_AGE_MAX = 12;

	public static void main(String[] args) {
		System.out.println("*********************************");
		System.out.println("* Welcome to CIT's Santa Grotto *");
		System.out.println("*********************************");

		Ticket ticket = new Ticket();

		boolean continueLoop = true;
		while (continueLoop) {
			Family family = new Family(); // instantiating a family
			family.getInfoFromUser(); // asks for all the family input
			family.printDetails(); // individual family ticket
			ticket.addFamily(family); /* adding the family to the total ticket
										 for all families */

			//Do more families? continueLoop = true does more families
			String doAnotherFamily = null;
			while (doAnotherFamily == null) {
				System.out.print("Do you want to enter the details of another family (y or n)?: ");
				doAnotherFamily = keyboard.nextLine().trim().toLowerCase();
				if (doAnotherFamily.equals("y") || doAnotherFamily.equals("yes")) {
					continueLoop = true;
				} else if (doAnotherFamily.equals("n") || doAnotherFamily.equals("no")) {
					continueLoop = false;
				} else {
					System.out.println("Invalid input. Must answer y or n.");
					doAnotherFamily = null;
				}
			}
		}
		keyboard.close();

		ticket.printSummary(); // ticket containing details of all families.
	}
}
