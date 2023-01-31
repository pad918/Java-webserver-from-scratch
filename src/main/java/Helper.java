public class Helper {

	public static String charToBinary(char c){
		StringBuilder b = new StringBuilder(" ");
		for(int i=7; i>=0; i--){
			if(((c>>i) & 1)==1)
				b.append("1");
			else
				b.append("0");
		}
		return b.toString();
	}

	public static String byteToBinary(byte c){
		StringBuilder b = new StringBuilder(" ");
		for(int i=7; i>=0; i--){
			if(((c>>i) & 1)==1)
				b.append("1");
			else
				b.append("0");
		}
		return b.toString();
	}

	public static String charArrToBinary(char[] bytes){
		StringBuilder s = new StringBuilder();
		for (char c : bytes){
			s.append(charToBinary(c));
		}
		return s.toString();
	}

	public static String byteArrToBinary(byte[] bytes){
		StringBuilder s = new StringBuilder();
		for (byte c : bytes){
			s.append(byteToBinary(c));
		}
		return s.toString();
	}
}
