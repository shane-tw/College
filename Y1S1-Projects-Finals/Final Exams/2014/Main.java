import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Main {
	static final java.util.Scanner lineScanner = new java.util.Scanner(System.in);
	static final List<Map<String,String>> fruitAndVegList = new ArrayList<>();
	static final List<Map<String,String>> dairyList = new ArrayList<>();
	static final List<Map<String,String>> confectioneryList = new ArrayList<>();
	static final Double costTotal = 0.0;
	static final Integer itemsCount = 0;
	static final Map<String,String> mostExpensive = new HashMap<String,String>();
	
	/* Note:
	   It would have made a lot more sense for me to create multiple classes
	   rather than use Maps.
	   However, I had limited knowledge of classes at the time.
	 */
	
	public static void main(String[] args) {
		while (true) {
			parseCart();
			System.out.print("Do you have more items? ");
			boolean yOrN = lineScanner.nextLine().equalsIgnoreCase("y") ? true : false;
			if (!yOrN) break;
		}
		if (fruitAndVegList.size() > 0) {
			System.out.println("Fruit and Veg");
			for (Map<String,String> cartItem : fruitAndVegList) {
				System.out.println("\t" + cartItem.get("itemName") + ": " + cartItem.get("priceNoVAT") + "(ex VAT) + " + cartItem.get("costVAT") + "(VAT) = " + cartItem.get("netIncVAT") + "(inc VAT)");
			}
		}
		if (dairyList.size() > 0) {
			System.out.println("Dairy");
			for (Map<String,String> cartItem : dairyList) {
				System.out.println("\t" + cartItem.get("itemName") + ": " + cartItem.get("priceNoVAT") + "(ex VAT) + " + cartItem.get("costVAT") + "(VAT) = " + cartItem.get("netIncVAT") + "(inc VAT)");
			}
		}
		if (confectioneryList.size() > 0) {
			System.out.println("Confectionery");
			for (Map<String,String> cartItem : confectioneryList) {
				System.out.println("\t" + cartItem.get("itemName") + ": " + cartItem.get("priceNoVAT") + "(ex VAT) + " + cartItem.get("costVAT") + "(VAT) = " + cartItem.get("netIncVAT") + "(inc VAT)");
			}
		}
		System.out.println("You have spent €" + costTotal + " in total.");
		System.out.println("You have spent an average of €" + (costTotal/itemsCount) + " per item.");
		System.out.println("Most expensive item(s): " + mostExpensive.get("itemName") + " in " + mostExpensive.get("itemCategory"));
	}

	public static boolean isNumericInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}
	
	public static boolean isNumericDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
	}
	
	public static void parseCart() {
		String itemName = "";
		while (itemName.length() < 3) {
			System.out.print("What is the item? ");
			itemName = lineScanner.nextLine();
		}
		String strPriceNoVAT = "-";
		while (true) {
			if (isNumericDouble(strPriceNoVAT))
				if (Double.parseDouble(strPriceNoVAT) > 0)
					break;
			System.out.print("What is the price excluding VAT? ");
			strPriceNoVAT = lineScanner.nextLine();
		}
		Double priceNoVAT = Double.parseDouble(strPriceNoVAT);
		String strItemCategory = "-";
		while (true) {
			if (isNumericInteger(strItemCategory))
				if (Integer.parseInt(strItemCategory) >= 1 && Integer.parseInt(strItemCategory) <= 3)
					break;
			System.out.println(
					"What is the category?\n" +
					"1: Fruit and Veg\n" +
					"2: Dairy\n" +
					"3: Confectionery"
				);
			strItemCategory = lineScanner.nextLine();
		}
		double rateVAT = 0;
		Integer itemCategory = Integer.parseInt(strItemCategory);
		String itemCategoryName = "";
		switch (itemCategory) {
			case 1:
				itemCategoryName = "Fruit and Veg";
				break;
			case 2:
				itemCategoryName = "Dairy";
				rateVAT = 0.1;
				break;
			case 3:
				itemCategoryName = "Confectionery";
				rateVAT = 0.2;
				break;
		}
		double costVAT = (priceNoVAT * rateVAT);
		double netIncVAT = priceNoVAT + costVAT;
		costTotal += netIncVAT;
		itemsCount += 1;
		
		Map<String,String> itemInfo = new HashMap<String,String>();
		itemInfo.put("itemName", itemName);
		itemInfo.put("itemCategory", itemCategoryName);
		itemInfo.put("priceNoVAT", Double.toString(priceNoVAT));
		itemInfo.put("costVAT", Double.toString(costVAT));
		itemInfo.put("netIncVAT", Double.toString(netIncVAT));
		if (mostExpensive.isEmpty() || Double.parseDouble(mostExpensive.get("netIncVAT")) < netIncVAT) {
			mostExpensive.put("itemName", itemName);
			mostExpensive.put("itemCategory", itemCategoryName);
			mostExpensive.put("netIncVAT", Double.toString(netIncVAT));
		}
		switch (itemCategory) {
			case 1:
				fruitAndVegList.add(itemInfo);
				break;
			case 2:
				dairyList.add(itemInfo);
				break;
			case 3:
				confectioneryList.add(itemInfo);
				break;
		}
	}
}
