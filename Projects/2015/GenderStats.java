
class GenderStats {
	// DCOM1 B - Shane O Donovan - R00141078
	/* Presuming the constants haven't been changed in Main:
	   Infant		= 0 - 1
	   Toddler		= 1 - 5
	   Preschooler	= 5 +
	*/
	private int mNumberOfInfants = 0;
	private int mNumberOfToddlers = 0;
	private int mNumberOfPreschoolers = 0;

	public int getInfants() {
		return mNumberOfInfants;
	}

	void addInfants(int numberOfInfants) {
		mNumberOfInfants += numberOfInfants;
	}

	void addInfant() {
		addInfants(1);
	}

	public int getToddlers() {
		return mNumberOfToddlers;
	}

	void addToddlers(int numberOfToddlers) {
		mNumberOfToddlers += numberOfToddlers;
	}

	void addToddler() {
		addToddlers(1);
	}

	public int getPreschoolers() {
		return mNumberOfPreschoolers;
	}

	void addPreschoolers(int numberOfPreschoolers) {
		mNumberOfPreschoolers += numberOfPreschoolers;
	}

	void addPreschooler() {
		addPreschoolers(1);
	}
}
