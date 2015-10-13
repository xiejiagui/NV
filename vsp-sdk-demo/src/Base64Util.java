

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Element;

public class Base64Util {

	public static void main(String[] args) {

		String str = "谁是最可爱的人";
		String ss1 = new String(Base64.encodeBase64(str.getBytes()));
		System.out.println(ss1);
		System.out.println(new String(decode(ss1)));
		System.out.println(GetImageStr("/home/vinson/tools/2286a67ecbab4bbe4e49017ed3f9b010.jpg"));
	}

	/**
	 * base64 编码
	 * 
	 * @param binaryData
	 * @return
	 */
	private static String encode(byte[] binaryData) {
		try {
			return new String(Base64.encodeBase64(binaryData), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * base64 解码
	 * 
	 * @param base64String
	 * @return
	 */
	private static byte[] decode(String base64String) {
		try {
			return Base64.decodeBase64(base64String.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * String base64编码
	 * 
	 * @param base64String
	 * @return
	 */
	public static String StringEncode(String base64String) {
		return encode(base64String.getBytes());
	}

	/**
	 * String base64解码
	 * 
	 * @param base64String
	 * @return
	 */
	public static String StringDecode(String base64String) {
		return new String(Base64.decodeBase64(base64String));
	}

	/**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @param imgFilePath
	 * @return
	 */
	public static String GetImageStr(String imgFilePath) {
		byte[] data = null;

		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码
		return encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * 
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 */
	public static boolean GenerateImage(String imgStr, String imgFilePath) {
		if (imgStr == null) // 图像数据为空
			return false;
		try {
			// Base64解码
			byte[] bytes = decode(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 将dom 直接base64编码
	 * @param elm
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64(Element elm) throws Exception {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		TransformerFactory transFac = TransformerFactory.newInstance();
		Transformer trans = transFac.newTransformer();
		trans.transform(new DOMSource(elm), new StreamResult(ostream));
		return (new String(Base64.encodeBase64(ostream.toByteArray(), true)));
	}
}
