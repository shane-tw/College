import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

class Main {
	public static int calculateCost(int daysCount, int musiciansCount) {
		int perDayCost = 0;
		if (daysCount == 1)
			perDayCost = 260;
		else if (daysCount >= 2 && daysCount <= 4)
			perDayCost = 240;
		else if (daysCount >= 5 && daysCount <= 8)
			perDayCost = 210;
		else if (daysCount >= 9)
			perDayCost = 200;
		int musicianCost = 100;
		int totalCost = (perDayCost * daysCount) + (musiciansCount * musicianCost);
		return totalCost;
	}
	
	public static void main(String[] args) {
		List<Map<String,String>> membersList = new ArrayList<Map<String,String>>();
		Scanner lineScanner = new Scanner(System.in);
		System.out.print("What is your name?: ");
		String name = lineScanner.nextLine();
		System.out.print("What is your email address?: ");
		String email = lineScanner.nextLine();
		System.out.print("What is your phone number?: ");
		String phoneNumber = lineScanner.nextLine();
		System.out.print("\nWhat start date are you looking for (dd/mm/yyyy)?: ");
		String startDate = lineScanner.nextLine();
		System.out.print("\n\nHow many members of the band?: ");
		int membersCount = Integer.parseInt(lineScanner.nextLine());
		System.out.print("\nHow many days?: ");
		int daysCount = Integer.parseInt(lineScanner.nextLine());
		System.out.print("\n");
		for (int i = 1; i <= membersCount; i++) {
			Map<String,String> memberMap = new HashMap<String,String>();
			System.out.print("What is band member #" + i + "'s name?: ");
			String memberName = lineScanner.nextLine();
			System.out.print("What is " + memberName + "'s instrument?: ");
			String instrument = lineScanner.nextLine();
			System.out.print("\n");
			memberMap.put("member_name", memberName);
			memberMap.put("instrument", instrument);
			membersList.add(memberMap);
		}
		System.out.print("There is room for 5 session musicians - how many do you want?: ");
		int musiciansWanted = Integer.parseInt(lineScanner.nextLine());
		int totalCost = calculateCost(daysCount,musiciansWanted);
		Map<Integer, String> paymentMethods = new HashMap<Integer, String>();
		paymentMethods.put(1,"Credit card");
		paymentMethods.put(2,"Cash");
		paymentMethods.put(3,"Cheque");
		System.out.println(
			"Will you pay by\n" +
			"1: Credit card (5% levy)\n" +
			"2: Cash (5% discount)\n" +
			"3: Cheque"
		);
		int paymentMethod = Integer.parseInt(lineScanner.nextLine());
		if (paymentMethod == 1)
			totalCost *= 1.05;
		else if (paymentMethod == 2)
			totalCost *= 0.95;
		System.out.println("\nBooking application");
		System.out.println("-------------------");
		System.out.println("Requested by: " + name + "(Contact: " + email + " & " + phoneNumber + ")\n");
		System.out.println("Date requested --> " + startDate + "\n");
		System.out.println("Band Members");
		System.out.println("-----------");
		for (int i = 0; i < membersList.size(); i++)
			System.out.println(i + ": " + membersList.get(i).get("member_name") + " - " + membersList.get(i).get("instrument"));
		System.out.println("\nIncludes " + musiciansWanted + " session musicians per day.");
		System.out.println("Payment will be €" + totalCost + " to be paid by " + paymentMethods.get(paymentMethod));
		lineScanner.close();
	}

}
