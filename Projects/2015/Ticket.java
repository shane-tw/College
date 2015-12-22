import java.util.List;
import java.util.ArrayList;

class Ticket {
	// DCOM1 B - Shane O Donovan - R00141078
	private List<Family> mFamilies;
	private int totalChildren = 0;
	private int totalPicsRequired = 0;
	private double mCost = 0;
	private GenderStats mBoyStats;
	private GenderStats mGirlStats;

	public Ticket() {
		mFamilies = new ArrayList<Family>();
		mBoyStats = new GenderStats();
		mGirlStats = new GenderStats();
	}

	public List<Family> getFamilies() {
		return mFamilies;
	}

	public void setFamilies(List<Family> families) {
		mFamilies = families;
	}

	public void addFamily(Family family) { /* Adds family and costs associated
											  with said family, and adds
											  to ticket gender stats. */
		mFamilies.add(family);
		addCost(family.getCost());
		totalChildren += family.getChildren().size();
		totalPicsRequired += (family.isPictureRequired() ? 1 : 0);
		addStats(family);
	}

	private void addStats(Family family) { /* Adds numbers to
											  boy stats and girl stats */
		mBoyStats.addInfants(family.getBoyStats().getInfants());
		mBoyStats.addToddlers(family.getBoyStats().getToddlers());
		mBoyStats.addPreschoolers(family.getBoyStats().getPreschoolers());
		mGirlStats.addInfants(family.getGirlStats().getInfants());
		mGirlStats.addToddlers(family.getGirlStats().getToddlers());
		mGirlStats.addPreschoolers(family.getGirlStats().getPreschoolers());
	}

	private void addCost(double cost) {
		mCost += cost;
	}

	public double getCost() {
		return mCost;
	}

	public GenderStats getBoyStats() {
		return mBoyStats;
	}

	public GenderStats getGirlStats() {
		return mGirlStats;
	}

	public void printSummary() { // Prints total ticket for all families
		System.out.println("Summary");
		System.out.println("=======");
		System.out.printf("Total number of bookings: %d%n", getFamilies().size());
		System.out.printf("Total number of children booked: %d%n", totalChildren);
		System.out.println("Presents required for:");
		System.out.println("Age Range\t\tGirls\tBoys");
		System.out.println("---------\t\t-----\t----");
		System.out.printf("<=%d\t\t\t0\t0%n", Main.INFANT_AGE_MAX);
		System.out.printf(">%d and <=%d\t\t%d\t%d%n", Main.TODDLER_AGE_MIN, Main.TODDLER_AGE_MAX,
				getGirlStats().getToddlers(), getBoyStats().getToddlers());
		System.out.printf(">%d\t\t\t%d\t%d%n", Main.PRESCHOOLER_AGE_MIN, getGirlStats().getPreschoolers(),
				getBoyStats().getPreschoolers());
		System.out.printf("Total number of photos required: %d%n", totalPicsRequired);
		System.out.printf("Total Booking Value(€): %.2f", getCost());
	}
}
