package com.nemustech.platform.lbs.common.httpconn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.nemustech.platform.lbs.common.vo.UploadImageVo;

public class HttpLbsConn implements LbsConnInf {
	public static final String CRLF = "\r\n";

	@Override
	public String post(String urls, String path, String data, String access_token) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			int responseCode = -1;
			BufferedReader in = null;
			OutputStreamWriter osw = null;

			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();

				con.setUseCaches(false);
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestMethod("POST");

				con.setRequestProperty("Cache-Control", "no-cache");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");

				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", "2");

				osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
				osw.write(data.toString());
				osw.flush();

				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine;
				response = new StringBuffer();

				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				if (osw != null)
					osw.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				try {
					if (in != null)
						in.close();
					if (osw != null)
						osw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		}
		return response.toString();
	}

	@Override
	public String put(String urls, String path, String data, String access_token) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			int responseCode = -1;
			BufferedReader in = null;
			OutputStreamWriter osw = null;

			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();

				con.setUseCaches(false);
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestMethod("PUT");

				con.setRequestProperty("Cache-Control", "no-cache");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");

				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", "2");

				osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
				osw.write(data.toString());
				osw.flush();

				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine;
				response = new StringBuffer();

				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				if (osw != null)
					osw.close();
				if (con != null)
					con.disconnect();

			} catch (Exception e) {
				try {
					if (in != null)
						in.close();
					if (osw != null)
						osw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		}

		return response.toString();
	}

	@Override
	public Model outputStreamConnect(String urls, String path, String data, String method, Model model,
			String access_token) {

		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			int responseCode = -1;
			BufferedReader in = null;
			OutputStreamWriter osw = null;

			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();

				con.setUseCaches(false);
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestMethod(method);

				con.setRequestProperty("Cache-Control", "no-cache");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");

				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", "2");
				osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
				osw.write(data.toString());
				osw.flush();

				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine;
				response = new StringBuffer();

				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				if (osw != null)
					osw.close();
				if (con != null)
					con.disconnect();

				model.addAttribute("r_cde", responseCode);
				model.addAttribute("r_msg", response);
			} catch (Exception e) {
				try {
					if (in != null)
						in.close();
					if (osw != null)
						osw.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		}

		return model;

	}

	@Override
	public Model pathConnect(String urls, String path, String method, Model model, String access_token) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();
		int responseCode = -1;

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			BufferedReader in = null;

			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod(method);

				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", "2");
				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				if (in != null)
					in.close();
				if (con != null)
					con.disconnect();
			} catch (Exception e) {
				try {
					if (in != null)
						in.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		}

		model.addAttribute("r_cde", responseCode);
		model.addAttribute("r_msg", response);
		return model;

	}

	@Override
	public String get(String urls, String path, String access_token) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			BufferedReader in = null;
			int responseCode = -1;

			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", "2");

				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				if (in != null)
					in.close();
				if (con != null)
					con.disconnect();

			} catch (Exception e) {
				try {
					if (in != null)
						in.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		}

		return response.toString();
	}

	@Override
	public String httpConnect(String urls, String path, String method, String access_token) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			int responseCode = -1;
			BufferedReader in = null;
			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod(method);
				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", "2");
				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine;
				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				if (in != null)
					in.close();
				if (con != null)
					con.disconnect();

			} catch (Exception e) {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e1) {

						e1.printStackTrace();
					}
				}
				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		}
		return response.toString();
	}

	@Override
	public String fileUploadConnect(String urls, String path, MultipartFile image, String method, String access_token) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();
		HttpURLConnection conn = null;

		for (String url : url_arr) {
			// Delimeter 생성
			String delimeter = makeDelimeter();
			byte[] newLineBytes = CRLF.getBytes();
			byte[] delimeterBytes = delimeter.getBytes();
			byte[] dispositionBytes = "Content-Disposition: form-data; name=".getBytes();
			byte[] quotationBytes = "\"".getBytes();
			byte[] contentTypeBytes = "Content-Type: application/octet-stream".getBytes();
			byte[] fileNameBytes = "; filename=".getBytes();
			byte[] twoDashBytes = "--".getBytes();

			BufferedOutputStream out = null;
			url = url.trim();
			String req_url = url + path;
			int responseCode = -1;
			BufferedReader in = null;
			try {
				URL obj = new URL(req_url);
				conn = (HttpURLConnection) obj.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("request_type", "2");
				conn.setRequestProperty("access_token", access_token);
				conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + delimeter);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);

				out = new BufferedOutputStream(conn.getOutputStream());

				// Delimeter 전송
				out.write(twoDashBytes);
				out.write(delimeterBytes);
				out.write(newLineBytes);

				// 파라미터 이름 출력
				out.write(dispositionBytes);
				out.write(quotationBytes);
				out.write("image".getBytes());
				out.write(quotationBytes);

				// File이 존재하는 지 검사한다.
				out.write(fileNameBytes);
				out.write(quotationBytes);
				out.write(image.getOriginalFilename().getBytes());
				out.write(quotationBytes);
				out.write(newLineBytes);
				out.write(contentTypeBytes);
				out.write(newLineBytes);
				out.write(newLineBytes);

				// File 데이터를 전송한다.
				// file에 있는 내용을 전송한다.
				BufferedInputStream is = null;
				try {
					is = new BufferedInputStream(image.getInputStream());
					byte[] fileBuffer = new byte[1024 * 8]; // 8k
					int len = -1;
					while ((len = is.read(fileBuffer)) != -1) {
						out.write(fileBuffer, 0, len);
					}
				} finally {
					if (is != null)
						try {
							is.close();
						} catch (IOException ex) {
						}
				}
				out.write(newLineBytes);

				// 마지막 Delimeter 전송
				out.write(twoDashBytes);
				out.write(delimeterBytes);
				out.write(twoDashBytes);
				out.write(newLineBytes);
				out.flush();

				responseCode = conn.getResponseCode();

				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					if (conn != null)
						conn.disconnect();
					if (out != null)
						out.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				break;
			}
		} // end of for

		UploadImageVo uvo = new UploadImageVo();
		try {
			Gson gson = new Gson();
			uvo = gson.fromJson(response.toString(), UploadImageVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return uvo.getImage_url();
	}

	private static String makeDelimeter() {
		return "---------------------------7d115d2a20060c";
	}

	@Override
	public boolean updateRootbranch(String urls, String path, String access_token, String request_type) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			int responseCode = -1;
			BufferedReader in = null;
			OutputStreamWriter osw = null;

			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();

				con.setUseCaches(false);
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestMethod("PUT");

				con.setRequestProperty("Cache-Control", "no-cache");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");

				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", request_type);

				osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
				osw.write("");
				osw.flush();

				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String inputLine;
				response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				if (osw != null)
					osw.close();
				if (con != null)
					con.disconnect();

				if (responseCode == HttpURLConnection.HTTP_OK) {
					return true;
				}
			} catch (Exception e) {
				try {
					if (in != null)
						in.close();
					if (osw != null)
						osw.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public Model reloadMapConnect(String urls, String path, String method, Model model, String access_token) {
		String[] url_arr = urls.split(";");
		StringBuffer response = new StringBuffer();
		int responseCode = -1;

		for (String url : url_arr) {
			url = url.trim();
			String req_url = url + path;
			URL obj = null;
			HttpURLConnection con = null;
			BufferedReader in = null;

			try {
				obj = new URL(req_url);
				con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod(method);

				con.setRequestProperty("access_token", access_token);
				con.setRequestProperty("request_type", "2");
				responseCode = con.getResponseCode();

				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				response = new StringBuffer();

				while (responseCode == HttpURLConnection.HTTP_OK && (inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				if (in != null)
					in.close();
				if (con != null)
					con.disconnect();
			} catch (Exception e) {
				try {
					if (in != null)
						in.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				if (con != null)
					con.disconnect();
				e.printStackTrace();
			}
		}

		model.addAttribute("r_cde", responseCode);
		model.addAttribute("r_msg", response);
		return model;

	}
}
