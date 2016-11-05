package ag.main;

import java.io.InputStream;

public class Util {
	public static InputStream getResourceStream(String path) {
		return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
	}
}
