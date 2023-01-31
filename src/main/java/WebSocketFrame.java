import java.io.BufferedInputStream;
import java.io.IOException;

public class WebSocketFrame {
	boolean FIN;
	byte opcode;
	boolean mask;
	long length;
	enum LengthHeaderType{I8, I16, I64}
	LengthHeaderType lengthType;
	byte[] masking_key = new byte[4];
	byte[] payload_data;



	//BufferedReader input_stream;
	BufferedInputStream input_stream;
	private long byteShift(byte c, int off){
		long k = (((long)c)&0xFF) << (off*8);
		return k;
		//return ((long)(c<<(off*8))) & 0xFF;
	}

	void printHeaderValues(){
		System.out.println("WEB SOCKET FRAME HEADER:");
		System.out.println("Fin = " + FIN);
		System.out.println("opcode = " + opcode);
		System.out.println("mask = " + mask);
		System.out.println("length = " + length);
		//System.out.println("Mask key = " + Integer.toBinaryString(masking_key));
	}

	private void read_header_start() throws IOException {
		byte[] c = new byte[2];
		input_stream.read(c);
		System.out.println(Helper.byteArrToBinary(c));
		FIN = ((c[0]>>7) & 1) == 1;
		opcode = (byte)(c[0] & 0xF); // 4-7
		mask = ((c[1]>>7) & 1) == 1;
		byte len =  (byte)(c[1] & 0x7F);
		length = len;

		if(len<126) {
			lengthType = LengthHeaderType.I8;
		}
		else if(len==126) {
			lengthType = LengthHeaderType.I16;
		}
		else {
			lengthType = LengthHeaderType.I64;
		}
	}

	private void readI16LengthHeader() throws IOException{
		length = length << (2*8);
		byte[] c = new byte[2];
		input_stream.read(c);
		System.out.println(Helper.byteArrToBinary(c));
		for(int i=0; i<2; i++){
			length |= byteShift(c[i], 1-i);
		}
	}

	private void readI64LengthHeader() throws IOException {
		length = length << (6*8);
		byte[] c = new byte[6];
		input_stream.read(c);
		System.out.println(Helper.byteArrToBinary(c));
		for(int i=0; i<6; i++){
			length |= byteShift(c[i], 5-i);
		}
	}

	private void readMaskKey() throws  IOException{
		//OBS KAN INTE ANTA ATT 4 KOMMER ATT LÄSAS!!!
		byte[] c = new byte[4];
		input_stream.read(c);
		System.out.println(Helper.byteArrToBinary(c));
		//masking_key = 0;
		for (int i=0; i<4;i++){
			masking_key[i] = c[i];
 		}
	}

	private void readPayload() throws IOException {
		byte[] c = new byte[16];
		int l = 1;
		int readBytes = 0;
		payload_data = new byte[(int)length]; // Dålig lösning, men borde fungera
		while(l>0 && readBytes<length){
			l = input_stream.read(c, 0, (int)Math.min(c.length, length-readBytes));
			System.out.println(Helper.byteArrToBinary(c));
 			for(int i=0; i<l; i++)
				payload_data[readBytes++] = c[i];
		}
	}

	void readHeader() throws IOException {
		int word = 0;
		int readBytes = 0;
		//[] arrr = input_stream.readNBytes(4);

		// READ FIRST WORD (32bit)

		read_header_start();
		if(lengthType == LengthHeaderType.I16)
			readI16LengthHeader();

		else if(lengthType == LengthHeaderType.I64)
			readI64LengthHeader();

		readMaskKey();
		readPayload();

		System.out.println("FOUND DATA: ");
		for(int i=0; i<length; i++)
			System.out.print((char)(payload_data[i]^masking_key[i&0b11]));
		System.out.println("");
	}

	WebSocketFrame(BufferedInputStream input){
		this.input_stream = input;
	}


}
