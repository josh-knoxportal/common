package org.oh.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

/**
 * 이미지 유틸
 */
public abstract class ImageUtils {
	/**
	 * 썸네일 생성
	 *
	 * @param orgName 원본 이미지 파일 경로
	 * @param destName 썸에릴로 저장될 이미지 파일 경로
	 * @param width 줄일 가로 길이
	 * @param height 줄일 세로 길이
	 * @return 썸네일 파일 이름
	 */
	public static String createThumb(String orgName, String destName, int width, int height) throws IOException {
		File orgFile = new File(orgName);
		File destFile = new File(destName);

		return createThumb(orgFile, destFile, width, height);
	}

	/**
	 * 썸네일 생성
	 *
	 * @param orgFile 원본 이미지 파일 객체
	 * @param destFile 썸네일로 저장될 이미지 파일 객체
	 * @param width 줄일 가로 길이
	 * @param height 줄일 세로 길이
	 * @return 썸네일 파일이름
	 */
	public static String createThumb(File orgFile, File destFile, int width, int height) throws IOException {
		Image srcImg = null;
		String suffix = orgFile.getName().substring(orgFile.getName().lastIndexOf('.') + 1).toLowerCase();

		if (suffix.equals("bmp") || suffix.equals("png") || suffix.equals("gif")) {
			srcImg = ImageIO.read(orgFile);
		} else {
			srcImg = new ImageIcon(orgFile.toString()).getImage();
		}

		int srcWidth = srcImg.getWidth(null);
		int srcHeight = srcImg.getHeight(null);
		int destWidth = -1, destHeight = -1;

		if (width < 0) {
			destWidth = srcWidth;
		} else if (width > 0) {
			destWidth = width;
		}

		if (height < 0) {
			destHeight = srcHeight;
		} else if (height > 0) {
			destHeight = height;
		}

		Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
		int pixels[] = new int[destWidth * destHeight];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);

		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			throw new IOException("Create thumb file \"" + orgFile.getPath() + "\" error", e);
		} catch (Exception e) {
			LogUtil.writeLog(e, ImageUtils.class);
		}

		BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
		destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
		ImageIO.write(destImg, "jpg", destFile);

		return destFile.getName();
	}

	// ThumbnailUtils /////////////////////////////////////////////////////////

	public static void makeThumbnail(File src, File dest, int w, int h) throws IOException, IllegalArgumentException {
		OutputStream os = new FileOutputStream(dest);

		try {
			makeThumbnail(src, os, w, h);
		} finally {
			os.close();
		}
	}

	public static void makeThumbnail(File src, OutputStream dest, int w, int h)
			throws IOException, IllegalArgumentException {
		String formatName = getFormatName(src);
		byte[] resizedImg;
		TiffImageMetadata exif = null;

		resizedImg = resize(ImageIO.read(src), formatName, w, h);
		if (resizedImg == null) {
			throw new RuntimeException(
					String.format("Resizing image file '%s' to (%d X %d) failed", src.getAbsolutePath(), w, h));
		}

		try {
			exif = getExif(src);
			write(resizedImg, formatName, exif, dest);
		} catch (ImageWriteException e) {
			throw new IOException(e);
		} catch (ImageReadException e) {
			throw new IOException(e);
		}
	}

	public static String getFormatName(File img) throws IOException {
		ImageInputStream imgInput = null;
		ImageReader imgReader = null;
		String formatName = "";

		try {
			imgInput = ImageIO.createImageInputStream(img);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(imgInput);

			if (!iter.hasNext())
				throw new RuntimeException("No image readers found");

			imgReader = iter.next();
			formatName = imgReader.getFormatName();
		} finally {
			imgReader.dispose();
			if (imgInput != null)
				imgInput.close();
		}

		return formatName;
	}

	private static TiffImageMetadata getExif(File src) throws ImageReadException, IOException {
		ImageMetadata meta = Imaging.getMetadata(src);
		JpegImageMetadata jpegMeta;

		if (meta instanceof JpegImageMetadata == false)
			return null;

		jpegMeta = (JpegImageMetadata) meta;

		return jpegMeta.getExif();
	}

	private static byte[] resize(BufferedImage img, String formatName, int w, int h) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedImage resized;

		try {
			resized = Scalr.resize(img, Mode.FIT_EXACT, w, h);
			if (resized == null)
				return null;

			ImageIO.write(resized, formatName, bos);
		} finally {
			bos.close();
		}

		return bos.toByteArray();
	}

	private static void write(byte[] imgData, String formatName, TiffImageMetadata exif, OutputStream dest)
			throws IOException, ImageReadException, ImageWriteException {
		if (exif != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {
				new ExifRewriter().updateExifMetadataLossless(imgData, os, exif.getOutputSet());
				imgData = null;
				imgData = os.toByteArray();
			} finally {
				os.close();
			}
		}

		IOUtils.write(imgData, dest);
	}
}
