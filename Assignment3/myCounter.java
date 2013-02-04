
// This class is a simple counter

public class myCounter {

    // Constructor with start value specifier
	public myCounter(int startValue) {
		counter = startValue;
	}
	
    // Constructor with start value defaulted to 1
	public myCounter() {
		counter = 1;
	}
	
    // member function to get next value
	public int nextValue() {
		int temp = counter;
		counter++;
		return temp;
	}

    // private class variable for value that gets incremented
	private int counter;
}
