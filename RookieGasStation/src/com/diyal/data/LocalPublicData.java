package com.diyal.data;

import java.io.File;
import java.util.UUID;

import android.content.Context;

import com.diyal.util.FileUtil;

/**
 * 本地公共数据操作工具类
 * 
 * @author diyal.yin
 * 
 */
public class LocalPublicData {
	// /**
	// * 读取本地数据，获得已注册的账号信息。 文件格式： 1. DES密钥 2. 账号DES密文 3. 密码DES密文
	// *
	// * @param context
	// * @return 账号信息或者为null
	// */
	// public static C2SAccountInfoBean getLastAccount(Context context) {
	// C2SAccountInfoBean acc = null;
	//
	// String accFile = FileUtil.getAccFilePath();
	// ArrayList<String> contentArr = FileUtil.readTextContents(accFile);
	//
	// if (contentArr != null && contentArr.size() == 4) {
	// String desKey = getDesKey(contentArr.get(0));
	// String accStr = contentArr.get(1);
	// String accNameStr = contentArr.get(2);
	// String pswdStr = contentArr.get(3);
	//
	// acc = new C2SAccountInfoBean();
	// try {
	// acc.account_id = new String(DesUtil.decrypt(
	// Base64Util.decode(accStr), desKey.getBytes()));
	// acc.lunauserid = new String(DesUtil.decrypt(
	// Base64Util.decode(accNameStr), desKey.getBytes()));
	// acc.lunapwd = new String(DesUtil.decrypt(
	// Base64Util.decode(pswdStr), desKey.getBytes()));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// contentArr.clear();
	// contentArr = null;
	// } else {
	// // 文件格式错误则删除当前账号文件
	// FileUtil.delete(accFile);
	// }
	//
	// return acc;
	// }
	//
	// public static void saveLastAccount(C2SAccountInfoBean acc) {
	// if (acc != null) {
	// String preKey = DesUtil.randHexStr(8);
	// String desKey = DesUtil.genDesKey();
	// String key = preKey + desKey;
	//
	// try {
	// byte[] accBytes = DesUtil.encrypt(acc.account_id.getBytes(),
	// desKey.getBytes());
	// byte[] accNameBytes = DesUtil.encrypt(
	// acc.lunauserid.getBytes(), desKey.getBytes());
	// byte[] pswdBytes = DesUtil.encrypt(acc.lunapwd.getBytes(),
	// desKey.getBytes());
	//
	// String accStr = Base64Util.base64Encode(accBytes);
	// String accNameStr = Base64Util.base64Encode(accNameBytes);
	// String pswdStr = Base64Util.base64Encode(pswdBytes);
	//
	// String s = key + "\n" + accStr + "\n" + accNameStr + "\n"
	// + pswdStr;
	//
	// String accFile = FileUtil.getAccFilePath();
	// FileUtil.writeToFile(accFile, s.getBytes(), false);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }

	private static String getDesKey(String preDesKey) {
		if (preDesKey.length() == 32) {
			return preDesKey.substring(8);
		}
		return null;
	}

	private static String uuid = null;

	public synchronized static String getUUID(Context context) {

		if (uuid == null) {
			String uPath = FileUtil.getUuidFilePath();
			File cfgFile = new File(uPath);
			try {
				if (!cfgFile.exists()) {
					// 生成新的
					uuid = UUID.randomUUID().toString();

					FileUtil.writeToFile(uPath, uuid.getBytes(), false);
				} else {
					uuid = FileUtil.getFileContent(uPath);

					if (uuid == null || uuid.length() == 0) {
						// 生成新的
						uuid = UUID.randomUUID().toString();

						FileUtil.writeToFile(uPath, uuid.getBytes(), false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return uuid;
	}
}
