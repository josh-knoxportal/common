package com.nemustech.platform.lbs.common.httpconn;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface LbsConnInf {
	public String post(String urls, String path, String data, String access_token);
	public String put(String urls, String path, String data, String access_token);
	public Model outputStreamConnect(String urls, String path, String data, String method, Model model, String access_token);
	public Model pathConnect(String urls, String path, String method, Model model, String access_token);
	public String get(String urls, String path, String access_token);
	public String httpConnect(String urls, String path, String method, String access_token);
	public String fileUploadConnect(String urls, String path, MultipartFile image, String method, String access_token);
	public boolean updateRootbranch(String urls, String path, String access_token, String request_type);
	public Model reloadMapConnect(String urls, String path, String method, Model model, String access_token);
}
