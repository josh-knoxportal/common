package com.nemustech.platform.lbs.common.controller;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;

import com.nemustech.platform.lbs.common.service.PropertyService;
import com.nemustech.platform.lbs.common.util.SftpUploader;

public class DefaultController {

	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

	public static final String SESSION_ATTR_KEY_ACCOUNT = "account";
	public static final String SESSION_ATTR_KEY_ACCESS_TOKEN = "access_token";

	public final static String REQUESTMAPPING_DANGER_ROOT = ""; // danger dg
	public final static String REQUESTMAPPING_DANGER_V1_ROOT = "/v1/wwms";
	public final static String REQUESTMAPPING_VEHICLE_ROOT = "/dangervehicle"; // vehicle
																				// vhc
	public final static String REQUESTMAPPING_VEHICLE_V1_ROOT = "/v1/ngms";

	public enum Return_Message { //
		SUCCESS("SUCCESS"), FAIL("FAIL");
		String message;

		Return_Message(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	@Autowired
	protected PropertyService props;

	protected String saveImage(MultipartFile ciImage, String path, String uri) throws Exception {
		if ((ciImage == null) || (ciImage.isEmpty()))
			return "";

		String srcName = ciImage.getOriginalFilename();
		int idx = srcName.lastIndexOf(".");
		String ext = (idx != -1) ? srcName.substring(idx) : "";
		String dstName = UUID.randomUUID().toString() + ext;

		String fileUploadPath = path;
		String subDir = "";
		File savePath = new File(fileUploadPath, subDir);
		if (!savePath.exists() || savePath.isFile()) {
			savePath.mkdirs();
		}

		File destFile = new File(savePath, dstName);
		logger.debug("savepath = " + savePath);
		logger.debug("dstName = " + dstName);
		logger.debug("destFile = " + destFile.getAbsolutePath());
		ciImage.transferTo(destFile);

		String ftpUrl = props.getString("sftp.hostname");
		logger.debug("ftpUrl = " + ftpUrl);
		if (ftpUrl != null && ftpUrl.length() > 0) {
			ftpUpload(destFile, props.getString("sftp.uri"));
		}

		return "/data" + uri + "/" + destFile.getName();
	}

	protected boolean deleteImage(String fileName) {
		boolean ret = false;
		String host = props.getString("sftp.hostname");
		String username = props.getString("sftp.username");
		String password = props.getString("sftp.password");
		int port = props.getInt("sftp.port");

		String dir = props.getString("sftp.uri");

		SftpUploader uploader = new SftpUploader();
		uploader.init(host, username, password, port);
		if (uploader.delete(dir, fileName)) {
			ret = true;
		}
		uploader.disconnection();
		return ret;
	}

	private boolean ftpUpload(File destFile, String uri) {
		String host = props.getString("sftp.hostname");
		String username = props.getString("sftp.username");
		String password = props.getString("sftp.password");
		int port = props.getInt("sftp.port");

		logger.debug("host : " + host);
		logger.debug("username : " + username);
		logger.debug("password : " + password);
		logger.debug("port : " + port);
		logger.debug("destFile : " + destFile.getName());
		logger.debug("uri : " + uri);

		try {
			SftpUploader uploader = new SftpUploader();
			uploader.init(host, username, password, port);
			logger.debug("inited");
			uploader.upload(uri, destFile);
			logger.debug("uploaded");
			uploader.disconnection();
			logger.debug("disconnected");
		} catch (Exception e) {
			logger.error("failed server connection error : " + e.getMessage());
			return false;
		}

		logger.debug("success");
		;
		return true;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
		binder.setAutoGrowNestedPaths(true);
		binder.setAutoGrowCollectionLimit(5000);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

		binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String text) {
				logger.debug("initBinder MultipartFile.class: {}; set null;", text);
				setValue(null);
			}
		});
	}

}
