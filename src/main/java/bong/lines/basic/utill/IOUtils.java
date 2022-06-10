package bong.lines.basic.utill;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    //body값 contentLength 를 통해 가져오기
	public static String readData(BufferedReader br, int contentLength) throws IOException {
		char[] body = new char[contentLength];
		br.read(body, 0, contentLength);
		return String.copyValueOf(body);
	}
}