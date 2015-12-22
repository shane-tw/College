import java.util.List;
import java.util.ArrayList;

class Family {
	// DCOM1 B - Shane O Donovan - R00141078
	private List<Child> mChildren;
	private GenderStats mBoyStats;
	private GenderStats mGirlStats;
	private String mName;
	private boolean mPictureRequired = false;
	private int mNoOfAdults = 0;
	private double mCost = 0;

	public Family() {
		mChildren = new ArrayList<Child>();
		mBoyStats = new GenderStats();
		mGirlStats = new GenderStats();
	}

	void getInfoFromUser() { /* Asks the user for all
								the input for the family. */
		String familyName = null;
		while (familyName == null) {
			System.out.print("Enter the family name: ");
			familyName = Main.keyboard.nextLine().trim();
			if (familyName.length() == 0) {
				System.out.println("Family name must contain at least 1 letter.");
				familyName = null;
			}
		}
		setName(familyName);
		int numberOfChildren = 0;
		while (numberOfChildren <= 0) {
			System.out.print("How many children are there in your family?: ");
			try {
				numberOfChildren = Integer.parseInt(Main.keyboard.nextLine().trim());
				if (numberOfChildren <= 0) {
					System.out.println("Sorry, you must bring kids with you.");
				}
			} catch (NumberFormatException e) {
				System.out.printf("Sorry, I didn't quite catch that number.%n");
			}
		}
		for (int n = 1; n <= numberOfChildren; n++) {
			Child child = new Child();
			System.out.printf("%nChild %d Details:%n", n);
			System.out.println("=======================");
			String childName = null;
			while (childName == null) {
				System.out.print("Enter the child name: ");
				childName = Main.keyboard.nextLine().trim();
				if (childName.length() == 0) {
					System.out.println("Child name must contain at least 1 letter.");
					childName = null;
				} else {
					child.setName(childName);
				}
			}
			String childGender = null;
			while (childGender == null) {
				System.out.printf("Is %s a girl or a boy (g/b)?: ", child.getName());
				childGender = Main.keyboard.nextLine().trim().toLowerCase();
				if (childGender.equals("b") || childGender.equals("boy")) {
					child.setGender("boy");
				} else if (childGender.equals("g") || childGender.equals("girl")) {
					child.setGender("girl");
				} else {
					System.out.println("Invalid input. Must answer b or g.");
					childGender = null;
				}
			}
			Integer childAge = null;
			while (childAge == null) {
				System.out.printf("How old is %s?: ", childName);
				try {
					childAge = Integer.parseInt(Main.keyboard.nextLine().trim());
					if (childAge < 0 || childAge > 12) {
						System.out.println(
								"Sorry, you must be at least 0 years old and at most 12 years old to qualify as a child.");
						childAge = null;
					} else {
						child.setAge(childAge);
					}
				} catch (NumberFormatException e) {
					System.out.printf("Sorry, I didn't quite catch that number.%n");
				}
			}
			addChild(child);
		}
		Integer numberOfAdults = null;
		while (numberOfAdults == null) {
			System.out.print("How many adults accompanying children?: ");
			try {
				numberOfAdults = Integer.parseInt(Main.keyboard.nextLine().trim());
				if (numberOfAdults <= 0) {
					System.out.println("Children must be accompanied by at least 1 adult.");
					numberOfAdults = null;
				} else {
					setNumberOfAdults(numberOfAdults);	
				}
			} catch (NumberFormatException e) {
				System.out.printf("Sorry, I didn't quite catch that number.%n");
			}
		}
		String isPictureRequired = null;
		while (isPictureRequired == null) {
			System.out.print("Picture Required (y/n)?: ");
			isPictureRequired = Main.keyboard.nextLine().trim().toLowerCase();
			if (isPictureRequired.equals("y") || isPictureRequired.equals("yes")) {
				setPictureRequired(true);
			} else if (isPictureRequired.equals("n") || isPictureRequired.equals("no")) {
				setPictureRequired(false);
			} else {
				System.out.println("Invalid input. Must answer y or n.");
				isPictureRequired = null;
			}
		}
		determineAndSetCost();
	}

	void printDetails() { // Print this specific family's ticket/details.
		System.out.println("Ticket");
		System.out.println("======");
		System.out.println("Family Name: " + getName());
		System.out.println("Number of Children: " + getChildren().size());
		for (int n = 0; n < getChildren().size(); n++) {
			Child child = getChildren().get(n);
			System.out.printf("Child %d: %s, %s, %d years old, %s, cost(€): %.2f%n", n + 1, child.getName(),
					child.getGender(), child.getAge(),
					child.isPresentRequired() ? "present required" : "present not required", child.getCost());
		}
		if (getNumberOfAdults() > Main.NUM_OF_FREE_ADULTS) {
			System.out.printf("%d adult(s) free and %d additional adult(s) @ €%.2f per adult.%n",
					Main.NUM_OF_FREE_ADULTS, getNumberOfAdults() - Main.NUM_OF_FREE_ADULTS, Main.EXTRA_ADULT_COST);
		} else {
			System.out.printf("%d adult(s) free.%n", getNumberOfAdults());
		}
		if (isPictureRequired()) {
			System.out.println("Picture required.");
		} else {
			System.out.println("Picture not required.");
		}
		System.out.printf("Cost: %.2f%n", getCost());
	}

	public List<Child> getChildren() {
		return mChildren;
	}

	public void setChildren(List<Child> children) {
		mChildren = children;
	}

	private void addChild(Child child) { /* Adds a child and the costs of the
											child.*/
		addCost(child.determineAndSetCost());
		getChildren().add(child);
		mBoyStats.addInfants(child.getBoyStats().getInfants());
		mBoyStats.addToddlers(child.getBoyStats().getToddlers());
		mBoyStats.addPreschoolers(child.getBoyStats().getPreschoolers());
		mGirlStats.addInfants(child.getGirlStats().getInfants());
		mGirlStats.addToddlers(child.getGirlStats().getToddlers());
		mGirlStats.addPreschoolers(child.getGirlStats().getPreschoolers());
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public boolean isPictureRequired() {
		return mPictureRequired;
	}

	public void setPictureRequired(boolean pictureRequired) {
		mPictureRequired = pictureRequired;
	}

	public int getNumberOfAdults() {
		return mNoOfAdults;
	}

	public void setNumberOfAdults(int noOfAdults) {
		mNoOfAdults = noOfAdults;
	}

	public double getCost() {
		return mCost;
	}

	public void setCost(double cost) {
		mCost = cost;
	}

	private void addCost(double cost) {
		mCost += cost;
	}

	private double determineAndSetCost() { /* Adds the family-specific costs
											  (not children costs) */
		if (mNoOfAdults > Main.NUM_OF_FREE_ADULTS) {
			addCost(Main.EXTRA_ADULT_COST * (mNoOfAdults - Main.NUM_OF_FREE_ADULTS));
		}
		if (isPictureRequired()) {
			addCost(Main.PHOTO_COST);
		}
		return getCost();
	}

	public GenderStats getBoyStats() {
		return mBoyStats;
	}

	public GenderStats getGirlStats() {
		return mGirlStats;
	}
}
