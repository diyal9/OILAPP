package com.diyal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;

public class GlobalsUtil {
	public static String getStackTrace(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}

	/**
	 * 查找资源id
	 * 
	 * @param context
	 * @param resType
	 *            res类型: layout drawable string..
	 * @param resName
	 *            res name
	 * @return
	 */
	public static int findId(Context context, String resType, String resName) {
		return context.getResources().getIdentifier(resName, resType,
				context.getApplicationContext().getPackageName());
	}

	/**
	 * 加载本地图片
	 * 
	 * @param context
	 * @param resName
	 *            res name
	 */
	public static BitmapDrawable getLoacalBitmap(Context context, String resName) {
		String filePath = FileUtil.getRootPath() + FileUtil.LUNASDK_STR
				+ File.separator + resName;

		FileInputStream fis = null;
		BitmapDrawable bd = null;
		try {
			fis = new FileInputStream(filePath);
			bd = new BitmapDrawable(context.getResources(),
					BitmapFactory.decodeStream(fis));
		} catch (FileNotFoundException e) {
			LogWriter
					.print("Method:getLoacalBitmap, Error:FileNotFoundException");
		}

		return bd;
	}

	/**
	 * 加载九宫格资源文件，并且设置大小
	 * 
	 * @param context
	 * @param resName
	 *            res name
	 * @param x
	 *            宽
	 * @param y
	 *            高
	 */
	public static BitmapDrawable get_ninepatch(String resName, int x, int y,
			Context context) {
		String filePath = FileUtil.getRootPath() + FileUtil.LUNASDK_STR
				+ File.separator + resName;

		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = new FileInputStream(filePath);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			LogWriter
					.print("Method:getLoacalBitmap, Error:FileNotFoundException");
		}

		byte[] chunk = bitmap.getNinePatchChunk();
		NinePatchDrawable np_drawable = new NinePatchDrawable(bitmap, chunk,
				new Rect(), null);
		np_drawable.setBounds(0, 0, x, y);

		Bitmap output_bitmap = Bitmap.createBitmap(x, y,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output_bitmap);
		np_drawable.draw(canvas);

		BitmapDrawable bmd = new BitmapDrawable(output_bitmap);

		return bmd;
	}
}
