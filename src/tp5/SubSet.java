package tp5;

public class SubSet {
	public int rank;
	public SmallSet set;
	
	public SubSet() {
		this.rank = 0;
		this.set = null;
	}
	
	public SubSet(int rank, SmallSet set) {
		this.rank = rank;
		this.set = set;
	}
}
