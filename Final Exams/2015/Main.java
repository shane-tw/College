import java.util.Scanner;

class Main {

	static final double BASIC_COST_PER_DAY = 200;
	static final double STANDARD_COST_PER_DAY = 250;
	static final double PREMIUM_COST_PER_DAY = 350;
	static final double DISCOUNT_PERCENT = 10;
	static final int MIN_FIRST_NAME_LENGTH = 2;
	static final int MIN_SURNAME_LENGTH = 2;
	static final int MIN_STAY_LENGTH = 1;
	static final int MIN_ACCOMMODATION_NUMBER = 1;
	static final int MAX_ACCOMMODATION_NUMBER = 3;

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		String bookingsList = "";
		double bookingsCost = 0;
		double bookingsCostAfterDiscount = 0;
		int numberOfBookings = 0;
		boolean moreBookings = true;
		System.out.println("--- Wild Atlantic Cruise Bookinig ---");
		while (moreBookings) {
			String firstName = null;
			while (firstName == null) {
				System.out.printf("%nEnter your first name: ");
				firstName = keyboard.nextLine().trim();
				if (firstName.length() < MIN_FIRST_NAME_LENGTH) {
					System.out.printf("Error: First name must be at least %d characters long.%n", MIN_FIRST_NAME_LENGTH);
					firstName = null;
				}
			}
			String firstNameInitials = firstName.trim().charAt(0) + ".";
			String surname = null;
			while (surname == null) {
				System.out.print("Enter your surname: ");
				surname = keyboard.nextLine().trim();
				if (surname.length() < MIN_SURNAME_LENGTH) {
					System.out.printf("Error: Surname must be at least %d characters long.%n", MIN_SURNAME_LENGTH);
					surname = null;
				}
			}
			System.out.print("Enter the length of your stay in days: ");
			Integer stayLength = null;
			while (stayLength == null) {
				try {
					stayLength = Integer.parseInt(keyboard.nextLine().trim());
				} catch (NumberFormatException e) {
					System.out.println("Error: The stay length must be a number.");
				}
				if (stayLength != null) {
					if (stayLength < MIN_STAY_LENGTH) {
						System.out.printf("Error: You must stay for at least %d day.%n", MIN_STAY_LENGTH);
						stayLength = null;
					}
				}
			}
			System.out.println("Types of accommodation available:");
			System.out.println("1\tBasic");
			System.out.println("2\tStandard");
			System.out.println("3\tPremium");
			Integer accommodationNumber = null;
			while (accommodationNumber == null) {
				System.out.print("Enter your choice now: ");
				try {
					accommodationNumber = Integer.parseInt(keyboard.nextLine().trim());
				} catch (NumberFormatException e) {
					System.out.println("Error: Accommodation type must be a number.");
				}
				if (accommodationNumber != null) {
					if (accommodationNumber < MIN_ACCOMMODATION_NUMBER || accommodationNumber > MAX_ACCOMMODATION_NUMBER) {
						System.out.println("Error: Accommodation type must be between 1 and 3.");
						accommodationNumber = null;
					}
				}
			}
			String accommodationName = "";
			double accommodationCost = 0;
			boolean useExtraTab = true; // Fixes there being a larger gap for
										// standard than the other two due to tabs.
			switch (accommodationNumber) {
			case 1:
				accommodationName = "Basic";
				accommodationCost = BASIC_COST_PER_DAY * stayLength;
				break;
			case 2:
				accommodationName = "Standard";
				accommodationCost = STANDARD_COST_PER_DAY * stayLength;
				useExtraTab = false;
				break;
			case 3:
				accommodationName = "Premium";
				accommodationCost = PREMIUM_COST_PER_DAY * stayLength;
				break;
			}
			bookingsList += (firstNameInitials + "\t" + surname + "\t\t" + stayLength + "\t" + accommodationName + "\t"
					+ (useExtraTab ? "\t" : "") + accommodationCost + "\n");
			bookingsCost += accommodationCost;
			numberOfBookings++;
			String moreBookingsYesOrNo = null;
			while (moreBookingsYesOrNo == null) {
				System.out.print("Would you like to enter the details for another passenger? (Y/N): ");
				moreBookingsYesOrNo = keyboard.nextLine().trim().toLowerCase();
				if (!moreBookingsYesOrNo.equals("y") && !moreBookingsYesOrNo.equals("n")
						&& !moreBookingsYesOrNo.equals("yes") && !moreBookingsYesOrNo.equals("no")) {
					System.out.println("Error: You must answer y or n.");
					moreBookingsYesOrNo = null;
				}
			}
			moreBookings = moreBookingsYesOrNo.equals("y") || moreBookingsYesOrNo.equals("yes");
		}
		System.out.printf("%nWild Atlantic Cruise Booking Details%n");
		System.out.println("------------------------------------");
		System.out.println("Name\tSurname\t\tDays\tType\t\tCost");
		System.out.print(bookingsList);
		System.out.printf("%nTotal Cost(€): %.2f%n", bookingsCost);
		if (numberOfBookings >= 3) {
			bookingsCostAfterDiscount = bookingsCost * (1 - (DISCOUNT_PERCENT / 100));
			System.out.printf("Total Cost After Discount (%.2f%%) (€): %.2f%n", DISCOUNT_PERCENT,
					bookingsCostAfterDiscount);
		}
		keyboard.close();
	}

}
