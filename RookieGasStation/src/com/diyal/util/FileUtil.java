package com.diyal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

public class FileUtil {
	public static final String LUNASDK_STR = "lunasdk";

	private static final String LB_ROOT_PATH = ".lunabox/"; //
	private static final String PUB_DIR = "pub/"; // 公共数据目录
	private static final String UUID_FILE = "uInfo.dat";
	private static final String ACC_FILE = "aInfo.dat";
	private static final String IMG_CACHE_DIR = "imgCache/";
	private static final String LUNASDK_RES_DIR = LUNASDK_STR + File.separator;

	private static Context mContext;

	public static void setContext(Context context) {
		mContext = context.getApplicationContext();
	}

	public static boolean hasStorage() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	private static boolean canSave(byte[] byteData) {
		if (byteData.length < getAvailaleSize()) {
			return true;
		}
		return false;
	}

	public static int getAvailaleSize() {
		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		int blockSize = stat.getBlockSize();
		int availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	private static String getPrivatePath() {
		return mContext.getCacheDir().getAbsolutePath();
	}

	// Sdcard
	private static String getSdcardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	private static boolean getSdcardIsReady() {
		String sdready = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(sdready)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(sdready)) {
			return true;
		}

		return false;
	}

	// root dir
	public static String getRootPath() {
		String fileSavePath = null;
		if (getSdcardIsReady()) {
			fileSavePath = getSdcardPath() + File.separator;
		} else {
			fileSavePath = getPrivatePath() + File.separator;
		}
		return fileSavePath + LB_ROOT_PATH;
	}

	public static String getUuidFilePath() {
		return getRootPath() + FileUtil.PUB_DIR + FileUtil.UUID_FILE;
	}

	public static String getAccFilePath() {
		return getRootPath() + FileUtil.PUB_DIR + FileUtil.ACC_FILE;
	}

	public static String getPublicDir() {
		return getRootPath() + FileUtil.PUB_DIR;
	}

	public static String getImgCacheDir() {
		return getRootPath() + FileUtil.IMG_CACHE_DIR;
	}

	public static String getLunasdkDir() {
		return getRootPath() + FileUtil.LUNASDK_RES_DIR;
	}

	public static void writeToFile(String filePath, byte[] bytes, boolean append) {
		try {
			File file = new File(filePath);
			if (!file.isFile()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file, append);// openFileOutput(filePath,MODE_PRIVATE);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> readTextContents(String filePath) {
		ArrayList<String> contentArr = new ArrayList<String>();
		File file = new File(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行
			while ((tempString = reader.readLine()) != null) {
				contentArr.add(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return contentArr;
	}

	public static String getFileContent(String filePath) {
		StringBuilder sb = new StringBuilder();

		File file = new File(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		return sb.toString();
	}

	public static boolean isExistFile(String strFile) {
		File f = new File(strFile);
		return f.exists();
	}

	public static void mkdir(String path) {
		File dir = new File(path);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
	}

	public static void mkdirs(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void mkAllDirs() {
		mkdirs(getRootPath());
		mkdirs(getPublicDir());
		mkdirs(getImgCacheDir());
		mkdirs(getLunasdkDir());
	}

	public static boolean delete(String filePath) {
		LogWriter.print("FileUtil delete filePath:" + filePath);
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径
	 * @param newPath
	 *            String 复制后路径
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：
	 * @param newPath
	 *            String 复制后路径 如：
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			LogWriter.print("文件夹复制失败");
		}

	}

	/**
	 * 图片资源读取图片
	 */
	public static Bitmap getImageFromSDFile(Context context, String fileName) {
		Bitmap image = null;

		String sdCardPath = getRootPath() + "/" + fileName;
		File file = new File(sdCardPath);
		if (file.exists()) { // SD卡中存在
			image = BitmapFactory.decodeFile(sdCardPath);

			if (image == null) {
				file.delete(); // 如果解码后不是Bitmap文件，则删除
			}
		}

		if (image == null) { // 如果SD卡读取不到文件，则到Assert中查
			AssetManager am = context.getResources().getAssets();
			try {
				InputStream is = am.open(fileName);
				if (is != null)
					image = BitmapFactory.decodeStream(is);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 如果Assert中读取到正确的图片文件，则保存一份在SD卡中
			if (image != null) {
				try {
					FileOutputStream out = new FileOutputStream(file);
					image.compress(Bitmap.CompressFormat.PNG, 100, out);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return image;
	}

	public static void copyFile(InputStream in, String outPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			FileOutputStream fs = new FileOutputStream(outPath);
			byte[] buffer = new byte[1024];
			while ((byteread = in.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
			in.close();
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}
}
