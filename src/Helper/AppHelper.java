package Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONObject;

public class AppHelper {
	
	
	public static boolean checkNewVersion() throws Exception{
		URLConnection connection = new URL("http://192.168.233.15:90/classify/app.json").openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String str = "";
		String temp = null;
		while((temp = reader.readLine()) != null){
			str += temp + "\n";
		}
		JSONObject server = JSONObject.fromObject(str);
		
		String localStr = FileHelper.readTxt(new File("app.json"));
		JSONObject local = JSONObject.fromObject(localStr);
		String localVersion = local.getString("version");
		String serverVersion = server.getString("version");
		if (!localVersion.equals(serverVersion)) {
			return true;
		}else{
			return false;
		}
	}
}
