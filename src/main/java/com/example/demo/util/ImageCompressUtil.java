package com.example.demo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageCompressUtil {


	/**
	 * 保存文件到服务器临时路径(用于文件上传)
	 * @param fileName
	 * @param is
	 * @return 文件全路径
	 */
	public static void writeFile(String filePath, String fileId, InputStream is) {
		File forderFile = new File(filePath);
		if (!forderFile.exists())
			forderFile.mkdirs();
		try {
			/** 首先保存到临时文件 */
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(filePath + File.separator + fileId)));
			byte[] readBytes = new byte[1024];// 缓冲大小
			int readed = 0;
			while ((readed = is.read(readBytes)) > 0) {
				fos.write(readBytes, 0, readed);
			}
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 等比例压缩算法： 
	 * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
	 * @param srcURL 原图地址
	 * @param deskURL 缩略图地址
	 * @param comBase 压缩基数
	 * @param scale 压缩限制(宽/高)比例  一般用1：
	 * 当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例
	 * @throws Exception
	 * @createTime 2014-12-16
	 * @lastModifyTime 2014-12-16
	 */
	public static void saveMinPhoto(String srcURL, String deskURL, double comBase,
			double scale) throws Exception {
		File srcFile = new File(srcURL);
		Image src = ImageIO.read(srcFile);
		int srcHeight = src.getHeight(null);
		int srcWidth = src.getWidth(null);
		int deskHeight = 0;// 缩略图高
		int deskWidth = 0;// 缩略图宽
		double srcScale = (double) srcHeight / srcWidth;
		/**缩略图宽高算法*/
		if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
			if (srcScale >= scale || 1 / srcScale > scale) {
				if (srcScale >= scale) {
					deskHeight = (int) comBase;
					deskWidth = srcWidth * deskHeight / srcHeight;
				} else {
					deskWidth = (int) comBase;
					deskHeight = srcHeight * deskWidth / srcWidth;
				}
			} else {
				if ((double) srcHeight > comBase) {
					deskHeight = (int) comBase;
					deskWidth = srcWidth * deskHeight / srcHeight;
				} else {
					deskWidth = (int) comBase;
					deskHeight = srcHeight * deskWidth / srcWidth;
				}
			}
		} else {
			deskHeight = srcHeight;
			deskWidth = srcWidth;
		}
		BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
		tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
		String filePath = deskURL.substring(0,deskURL.lastIndexOf("/"));
		File forderFile = new File(filePath);
		if (!forderFile.exists())
			forderFile.mkdirs();
		ImageIO.write(tag,  "jpeg" , new File(deskURL)); 
		tag.flush();
	}

	public static void main(String args[]) throws Exception {
		ImageCompressUtil.saveMinPhoto("E:/Grp1.dat_001098.png", "E:/11.jpg", 128, 1);
	}
}
