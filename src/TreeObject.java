public class TreeObject{
	private Long key;
	private int frequency;

	public TreeObject(Long key) {
		this.key = key;
		this.frequency = 1;
	}
	
	public Long getKey(){
		return this.key;
	}
	
	public int getFrequency(){
		return this.frequency;
	}
	
	public void increaseFrequency() {
		this.frequency++;
	}

	@Override
	public String toString() {
		return "Key: " + key + ", Frequency: " + frequency;
	}
}
