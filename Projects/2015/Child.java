
class Child {
	// DCOM1 B - Shane O Donovan - R00141078
	private String mName;
	private String mGender;
	private int mAge;
	private double mCost = 0;
	private boolean mPresentRequired = false;
	private GenderStats mBoyStats;
	private GenderStats mGirlStats;

	public Child() {
		mBoyStats = new GenderStats();
		mGirlStats = new GenderStats();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getGender() {
		return mGender;
	}

	public void setGender(String gender) {
		mGender = gender;
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

	double determineAndSetCost() { /* sets child costs, returns them, also adds
									 gender to genderStats. */
		if (mAge <= Main.INFANT_AGE_MAX) {
			addCost(Main.INFANT_COST);
			if (getGender().equals("boy")) {
				mBoyStats.addInfant();
			} else {
				mGirlStats.addInfant();
			}
		} else if (mAge > Main.TODDLER_AGE_MIN && mAge <= Main.TODDLER_AGE_MAX) {
			addCost(Main.TODDLER_COST);
			mPresentRequired = true;
			if (getGender().equals("boy")) {
				mBoyStats.addToddler();
			} else {
				mGirlStats.addToddler();
			}
		} else if (mAge > Main.PRESCHOOLER_AGE_MIN && mAge <= Main.PRESCHOOLER_AGE_MAX) {
			addCost(Main.PRESCHOOLER_COST);
			mPresentRequired = true;
			if (getGender().equals("boy")) {
				mBoyStats.addPreschooler();
			} else {
				mGirlStats.addPreschooler();
			}
		}
		return getCost();
	}

	public int getAge() {
		return mAge;
	}

	public void setAge(int age) {
		mAge = age;
	}

	public boolean isPresentRequired() {
		return mPresentRequired;
	}

	public GenderStats getBoyStats() {
		return mBoyStats;
	}

	public GenderStats getGirlStats() {
		return mGirlStats;
	}
}
