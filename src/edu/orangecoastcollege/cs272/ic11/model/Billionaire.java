package edu.orangecoastcollege.cs272.ic11.model;

public class Billionaire {

	private int mId;
	private String mName;
	private int mAge;
	private String mGender;
	private double mWorth;
	private String mCitizenship;
	private String mSector;
	private boolean mPolitical;
	
	public Billionaire(int id, String name, int age, String gender, double worth, String citizenship, String sector,
			boolean political) {
		super();
		mId = id;
		mName = name;
		mAge = age;
		mGender = gender;
		mWorth = worth;
		mCitizenship = citizenship;
		mSector = sector;
		mPolitical = political;
	}

	public int getId()
	{
		return mId;		
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getAge() {
		return mAge;
	}

	public void setAge(int age) {
		mAge = age;
	}

	public String getGender() {
		return mGender;
	}

	public void setGender(String gender) {
		mGender = gender;
	}

	public double getWorth() {
		return mWorth;
	}

	public void setWorth(double worth) {
		mWorth = worth;
	}

	public String getCitizenship() {
		return mCitizenship;
	}

	public void setCitizenship(String citizenship) {
		mCitizenship = citizenship;
	}

	public String getSector() {
		return mSector;
	}

	public void setSector(String wealthType) {
		mSector = wealthType;
	}

	public boolean isPolitical() {
		return mPolitical;
	}

	public void setPolitical(boolean political) {
		mPolitical = political;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mAge;
		result = prime * result + ((mCitizenship == null) ? 0 : mCitizenship.hashCode());
		result = prime * result + ((mGender == null) ? 0 : mGender.hashCode());
		result = prime * result + ((mName == null) ? 0 : mName.hashCode());
		result = prime * result + (mPolitical ? 1231 : 1237);
		result = prime * result + ((mSector == null) ? 0 : mSector.hashCode());
		long temp;
		temp = Double.doubleToLongBits(mWorth);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Billionaire other = (Billionaire) obj;
		if (mAge != other.mAge)
			return false;
		if (mCitizenship == null) {
			if (other.mCitizenship != null)
				return false;
		} else if (!mCitizenship.equals(other.mCitizenship))
			return false;
		if (mGender == null) {
			if (other.mGender != null)
				return false;
		} else if (!mGender.equals(other.mGender))
			return false;
		if (mName == null) {
			if (other.mName != null)
				return false;
		} else if (!mName.equals(other.mName))
			return false;
		if (mPolitical != other.mPolitical)
			return false;
		if (mSector == null) {
			if (other.mSector != null)
				return false;
		} else if (!mSector.equals(other.mSector))
			return false;
		if (Double.doubleToLongBits(mWorth) != Double.doubleToLongBits(other.mWorth))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Billionaire [Name=" + mName + ", Age=" + mAge + ", Gender=" + mGender + ", Worth=$" + mWorth + "B"
				+ ", Citizenship=" + mCitizenship + ", Sector=" + mSector + ", Political=" + mPolitical + "]";
	}

	
	
	
}
