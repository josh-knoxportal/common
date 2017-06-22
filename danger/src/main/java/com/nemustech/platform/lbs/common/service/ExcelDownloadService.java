package com.nemustech.platform.lbs.common.service;

import java.awt.Color;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.nemustech.platform.lbs.ngms.vo.DrivingEventExcelVo;
import com.nemustech.platform.lbs.wwms.vo.BeaconDangerVo;
import com.nemustech.platform.lbs.wwms.vo.DeviceDangerVo;
import com.nemustech.platform.lbs.wwms.vo.WorkIssueVo;
import com.nemustech.platform.lbs.wwms.vo.WorkMntVo;
import com.nemustech.platform.lbs.wwms.vo.WorkStatusVo;
import com.nemustech.platform.lbs.wwms.vo.WorkerMntVo;

@Component(value = "excelDownloadService")
public class ExcelDownloadService extends AbstractView {

	/*
	 * 공통 액셀 다운로드 service
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> ModelMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		XSSFWorkbook workbook = new XSSFWorkbook();
		String excelName = ModelMap.get("target").toString();
		XSSFSheet worksheet = null;
		XSSFRow row = null;

		if (excelName.equals("work_issue")) {
			worksheet = workbook.createSheet(excelName);
			@SuppressWarnings("unchecked")
			List<WorkIssueVo> list = (List<WorkIssueVo>) ModelMap.get("excelList");

			XSSFCellStyle tcs = this.getHeaderCellStyle(workbook);

			// header setting
			row = worksheet.createRow(0);
			row.createCell(0).setCellValue("No");
			row.createCell(1).setCellValue("작업자정보");
			row.createCell(4).setCellValue("인가구역설정");
			row.createCell(6).setCellValue("출입감지구역");
			row.createCell(11).setCellValue("작업정보");

			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 10));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 16));

			row = worksheet.createRow(1);
			row.createCell(1).setCellValue("작업자");
			row.createCell(2).setCellValue("폰번호");
			row.createCell(3).setCellValue("업체명");
			row.createCell(4).setCellValue("공장");
			row.createCell(5).setCellValue("Zone");
			row.createCell(6).setCellValue("구분");
			row.createCell(7).setCellValue("공장");
			row.createCell(8).setCellValue("Zone");
			row.createCell(9).setCellValue("진입시간");
			row.createCell(10).setCellValue("이탈시간");
			row.createCell(11).setCellValue("작업유형");
			row.createCell(12).setCellValue("작업번호");
			row.createCell(13).setCellValue("작업명");
			row.createCell(14).setCellValue("작업시작");
			row.createCell(15).setCellValue("작업종료");
			row.createCell(16).setCellValue("작업자수");

			worksheet = getHeaderCellStyleSetting(worksheet, tcs);

			// data setting
			for (int i = 0; i < list.size(); i++) {
				row = worksheet.createRow(i + 2);
				row.createCell(0).setCellValue(list.size() - i);
				row.createCell(1).setCellValue(list.get(i).getWorker_name());
				row.createCell(2).setCellValue(list.get(i).getDevice_no());
				row.createCell(3).setCellValue(list.get(i).getParter_name());
				row.createCell(4).setCellValue(list.get(i).getFactory_name());
				row.createCell(5).setCellValue(list.get(i).getZone_name());
				if (list.get(i).getType() == 0) {
					row.createCell(6).setCellValue("비인가자 진입");
				} else if (list.get(i).getType() == 1) {
					row.createCell(6).setCellValue("인가자 진입");
				}
				row.createCell(7).setCellValue(list.get(i).getE_factory_name());
				row.createCell(8).setCellValue(list.get(i).getE_zone_name());
				row.createCell(9).setCellValue(list.get(i).getIn_date());
				row.createCell(10).setCellValue(list.get(i).getOut_date());
				row.createCell(11).setCellValue(list.get(i).getWork_type_name());
				row.createCell(12).setCellValue(list.get(i).getWork_no());
				row.createCell(13).setCellValue(list.get(i).getWork_name());
				row.createCell(14).setCellValue(list.get(i).getStarting_date());
				row.createCell(15).setCellValue(list.get(i).getComplete_date());
				row.createCell(16).setCellValue(list.get(i).getWorker_count());
			}

			excelName = URLEncoder.encode("출입감지 현황", "UTF-8");
		}

		if (excelName.equals("work_status")) {
			worksheet = workbook.createSheet(excelName);
			@SuppressWarnings("unchecked")
			List<WorkStatusVo> list = (List<WorkStatusVo>) ModelMap.get("excelList");

			XSSFCellStyle tcs = this.getHeaderCellStyle(workbook);

			// header setting
			row = worksheet.createRow(0);
			row.createCell(0).setCellValue("No");
			row.createCell(1).setCellValue("관리일자");
			row.createCell(2).setCellValue("작업자정보");
			row.createCell(5).setCellValue("인가구역설정");
			row.createCell(7).setCellValue("최종위치");
			row.createCell(9).setCellValue("작업정보");

			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 14));

			row = worksheet.createRow(1);
			row.createCell(2).setCellValue("작업자");
			row.createCell(3).setCellValue("폰번호");
			row.createCell(4).setCellValue("업체명");
			row.createCell(5).setCellValue("공장");
			row.createCell(6).setCellValue("Zone");
			row.createCell(7).setCellValue("공장");
			row.createCell(8).setCellValue("Zone");
			row.createCell(9).setCellValue("작업유형");
			row.createCell(10).setCellValue("작업번호");
			row.createCell(11).setCellValue("작업명");
			row.createCell(12).setCellValue("작업시작");
			row.createCell(13).setCellValue("작업종료");
			row.createCell(14).setCellValue("작업자수");

			worksheet = getHeaderCellStyleSetting(worksheet, tcs);
			// data setting
			for (int i = 0; i < list.size(); i++) {
				row = worksheet.createRow(i + 2);
				row.createCell(0).setCellValue(list.size() - i);
				row.createCell(1).setCellValue(list.get(i).getWork_day());
				row.createCell(2).setCellValue(list.get(i).getWorker_name());
				row.createCell(3).setCellValue(list.get(i).getDevice_no());
				row.createCell(4).setCellValue(list.get(i).getParter_name());
				row.createCell(5).setCellValue(list.get(i).getFactory_name());
				row.createCell(6).setCellValue(list.get(i).getZone_name());
				row.createCell(7).setCellValue(list.get(i).getLast_factory_name());
				row.createCell(8).setCellValue(list.get(i).getLast_zone_name());
				row.createCell(9).setCellValue(list.get(i).getWork_type_name());
				row.createCell(10).setCellValue(list.get(i).getWork_no());
				row.createCell(11).setCellValue(list.get(i).getName());
				row.createCell(12).setCellValue(list.get(i).getStarting_date());
				row.createCell(13).setCellValue(list.get(i).getComplete_date());
				row.createCell(14).setCellValue(list.get(i).getWorker_count());
			}

			excelName = URLEncoder.encode("작업자별 현황", "UTF-8");
		}

		if (excelName.equals("worker_mnt")) {
			worksheet = workbook.createSheet(excelName);
			@SuppressWarnings("unchecked")
			List<WorkerMntVo> list = (List<WorkerMntVo>) ModelMap.get("excelList");

			XSSFCellStyle tcs = this.getHeaderCellStyle(workbook);

			// header setting
			row = worksheet.createRow(0);
			row.createCell(0).setCellValue("No");
			row.createCell(1).setCellValue("작업자정보");
			row.createCell(4).setCellValue("인가구역설정");
			row.createCell(6).setCellValue("최종위치");
			row.createCell(8).setCellValue("작업정보");

			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 13));

			row = worksheet.createRow(1);
			row.createCell(1).setCellValue("작업자");
			row.createCell(2).setCellValue("폰번호");
			row.createCell(3).setCellValue("업체명");
			row.createCell(4).setCellValue("공장");
			row.createCell(5).setCellValue("Zone");
			row.createCell(6).setCellValue("공장");
			row.createCell(7).setCellValue("Zone");
			row.createCell(8).setCellValue("작업유형");
			row.createCell(9).setCellValue("작업번호");
			row.createCell(10).setCellValue("작업명");
			row.createCell(11).setCellValue("작업시작");
			row.createCell(12).setCellValue("작업종료");
			row.createCell(13).setCellValue("작업자수");

			worksheet = getHeaderCellStyleSetting(worksheet, tcs);
			// data setting
			for (int i = 0; i < list.size(); i++) {
				row = worksheet.createRow(i + 2);
				row.createCell(0).setCellValue(list.size() - i);
				row.createCell(1).setCellValue(list.get(i).getWorker_name());
				row.createCell(2).setCellValue(list.get(i).getDevice_no());
				row.createCell(3).setCellValue(list.get(i).getParter_name());
				row.createCell(4).setCellValue(list.get(i).getFactory_name());
				row.createCell(5).setCellValue(list.get(i).getZone_name());
				row.createCell(6).setCellValue(list.get(i).getLast_factory_name());
				row.createCell(7).setCellValue(list.get(i).getLast_zone_name());
				row.createCell(8).setCellValue(list.get(i).getWork_type_name());
				row.createCell(9).setCellValue(list.get(i).getWork_no());
				row.createCell(10).setCellValue(list.get(i).getName());
				row.createCell(11).setCellValue(list.get(i).getStarting_date());
				row.createCell(12).setCellValue(list.get(i).getComplete_date());
				row.createCell(13).setCellValue(list.get(i).getWorker_count());
			}

			excelName = URLEncoder.encode("작업자 관리", "UTF-8");
		}

		if (excelName.equals("work_mnt")) {
			worksheet = workbook.createSheet(excelName);
			@SuppressWarnings("unchecked")
			List<WorkMntVo> list = (List<WorkMntVo>) ModelMap.get("excelList");

			XSSFCellStyle tcs = this.getHeaderCellStyle(workbook);

			// header setting
			row = worksheet.createRow(0);
			row.createCell(0).setCellValue("No");
			row.createCell(1).setCellValue("관리일자");
			row.createCell(2).setCellValue("작업번호");
			row.createCell(3).setCellValue("작업명");
			row.createCell(4).setCellValue("부서/업체명");
			row.createCell(5).setCellValue("작업유형");
			row.createCell(6).setCellValue("작업계획정보");
			row.createCell(11).setCellValue("작업정보");
			row.createCell(14).setCellValue("인가구역설정");

			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
			worksheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 10));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 13));
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 14, 15));

			// 여기
			row = worksheet.createRow(1);
			row.createCell(6).setCellValue("작업시작");
			row.createCell(7).setCellValue("작업종료");
			row.createCell(8).setCellValue("작업자수");
			row.createCell(9).setCellValue("공장");
			row.createCell(10).setCellValue("Zone");
			row.createCell(11).setCellValue("작업시작");
			row.createCell(12).setCellValue("작업종료");
			row.createCell(13).setCellValue("작업자수");
			row.createCell(14).setCellValue("공장");
			row.createCell(15).setCellValue("Zone");

			worksheet = getHeaderCellStyleSetting(worksheet, tcs);
			// data setting
			for (int i = 0; i < list.size(); i++) {
				row = worksheet.createRow(i + 2);
				row.createCell(0).setCellValue(list.size() - i);
				row.createCell(1).setCellValue(list.get(i).getWork_day());
				row.createCell(2).setCellValue(list.get(i).getWork_no());
				row.createCell(3).setCellValue(list.get(i).getName());
				row.createCell(4).setCellValue(list.get(i).getParter_name());
				row.createCell(5).setCellValue(list.get(i).getWork_type_name());
				row.createCell(6).setCellValue(list.get(i).getStarting_date());
				row.createCell(7).setCellValue(list.get(i).getComplete_date());
				row.createCell(8).setCellValue(list.get(i).getWorker_count());
				row.createCell(9).setCellValue(list.get(i).getFactory_name());
				row.createCell(10).setCellValue(list.get(i).getZone_name());
				row.createCell(11).setCellValue(list.get(i).getReal_starting_date());
				row.createCell(12).setCellValue(list.get(i).getReal_ending_date());
				row.createCell(13).setCellValue(list.get(i).getReal_worker_count());
				row.createCell(14).setCellValue(list.get(i).getWorker_factory_name());
				row.createCell(15).setCellValue(list.get(i).getWorker_zone_name());
			}

			excelName = URLEncoder.encode("작업 관리", "UTF-8");
		}

		if (excelName.equals("vehicle_hist")) {
			worksheet = workbook.createSheet(excelName);
			@SuppressWarnings("unchecked")
			List<DrivingEventExcelVo> list = (List<DrivingEventExcelVo>) ModelMap.get("excelList");

			XSSFCellStyle tcs = this.getHeaderCellStyle(workbook);

			// header setting
			row = worksheet.createRow(0);
			row.createCell(0).setCellValue("No");
			row.createCell(1).setCellValue("차량번호");
			row.createCell(2).setCellValue("단말기 번호");
			row.createCell(3).setCellValue("입차시간");
			row.createCell(4).setCellValue("과속 여부");
			row.createCell(5).setCellValue("차량 속도");
			row.createCell(6).setCellValue("위험지역 출입 여부");
			row.createCell(7).setCellValue("이력 등록 시간");

			worksheet = getHeaderCellStyleSetting(worksheet, tcs);
			// data setting
			for (int i = 0; i < list.size(); i++) {
				row = worksheet.createRow(i + 1);
				row.createCell(0).setCellValue(list.get(i).getNo());
				row.createCell(1).setCellValue(list.get(i).getVehicle_no());
				row.createCell(2).setCellValue(list.get(i).getDevice_no());
				row.createCell(3).setCellValue(list.get(i).getCome_in_date());
				if (list.get(i).getIs_over_speed() == 1) {
					row.createCell(4).setCellValue("과속");
				} else {
					row.createCell(4).setCellValue("정상");
				}

				row.createCell(5).setCellValue(list.get(i).getSpeed() + "km/h");
				if (list.get(i).getIs_on_restrict_area() == 1) {
					row.createCell(6).setCellValue("위험지역 출입");
				} else {
					row.createCell(6).setCellValue("정상");
				}

				row.createCell(7).setCellValue(list.get(i).getReg_date());
			}

			excelName = URLEncoder.encode("운행 이력", "UTF-8");
		}

		if (excelName.equals("danger_device_list")) {

			worksheet = workbook.createSheet(excelName);

			@SuppressWarnings("unchecked")
			List<DeviceDangerVo> list = (List<DeviceDangerVo>) ModelMap.get("excelList");

			XSSFCellStyle tcs = this.getHeaderCellStyle(workbook);

			// header setting
			row = worksheet.createRow(0);
			row.createCell(0).setCellValue("No");
			row.createCell(1).setCellValue("스마트폰번호");
			row.createCell(2).setCellValue("폰구분");
			row.createCell(3).setCellValue("관리공장");
			row.createCell(4).setCellValue("관리자");
			row.createCell(5).setCellValue("상태");
			row.createCell(6).setCellValue("업데이트일자");

			worksheet = getHeaderCellStyleSetting(worksheet, tcs);

			// data setting
			for (int i = 0; i < list.size(); i++) {
				row = worksheet.createRow(i + 1);
				row.createCell(0).setCellValue(list.size() - i);
				row.createCell(1).setCellValue(list.get(i).getDevice_no());
				if (list.get(i).getNetwork_type() == "totalcompany") {
					row.createCell(2).setCellValue("통합(법인)폰");
				} else if (list.get(i).getNetwork_type() == "totalperson") {
					row.createCell(2).setCellValue("통합(개인)폰");
				} else {
					row.createCell(2).setCellValue(list.get(i).getNetwork_type());
				}
				row.createCell(3).setCellValue(list.get(i).getFactory_name());
				row.createCell(4).setCellValue(list.get(i).getAccount_name());
				row.createCell(5).setCellValue(getDeviceStatusValue(list.get(i).getIs_used()));
				row.createCell(6).setCellValue(list.get(i).getUpd_date());
			}

			excelName = URLEncoder.encode("단말관리", "UTF-8");
		}

		if (excelName.equals("danger_beacon_list")) {

			worksheet = workbook.createSheet(excelName);

			@SuppressWarnings("unchecked")
			List<BeaconDangerVo> list = (List<BeaconDangerVo>) ModelMap.get("excelList");

			XSSFCellStyle tcs = this.getHeaderCellStyle(workbook);

			// header setting
			row = worksheet.createRow(0);
			row.createCell(0).setCellValue("No");
			row.createCell(1).setCellValue("Beacon");
			row.createCell(2).setCellValue("단위공장");
			row.createCell(3).setCellValue("Zone");
			row.createCell(4).setCellValue("상태");
			row.createCell(5).setCellValue("배터리");
			row.createCell(6).setCellValue("업데이트일시");

			worksheet = getHeaderCellStyleSetting(worksheet, tcs);

			// data setting
			for (int i = 0; i < list.size(); i++) {
				row = worksheet.createRow(i + 1);
				row.createCell(0).setCellValue(list.size() - i);
				row.createCell(1).setCellValue(list.get(i).getName());
				row.createCell(2).setCellValue(list.get(i).getFactory_name());
				row.createCell(3).setCellValue(list.get(i).getZone_name());
				if (list.get(i).getIs_activated() == 0) {
					row.createCell(4).setCellValue("비활성");
				} else if (list.get(i).getIs_activated() == 1) {
					row.createCell(4).setCellValue("활성");
				}
				row.createCell(5).setCellValue(list.get(i).getBattery_ox());
				row.createCell(6).setCellValue(list.get(i).getUpd_date());
			}

			excelName = URLEncoder.encode("비콘관리", "UTF-8");
		}

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		excelName = dateFormat.format(calendar.getTime()) + " " + excelName.replaceAll("\\+", "%20");
		response.setContentType("application/xlsx");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "ATTachment; Filename=" + excelName + ".xlsx");

		OutputStream fileStream = response.getOutputStream();
		workbook.write(fileStream);

		fileStream.flush();
		fileStream.close();
	}

	protected String getDeviceStatusValue(int is_used) {
		String used_value = "-";

		if (is_used == 1) {
			used_value = "사용중";
		} else if (is_used == 2) {
			used_value = "수리";
		} else if (is_used == 3) {
			used_value = "정지";
		} else if (is_used == 0) {
			used_value = "사용종료";
		}
		return used_value;
	}

	public XSSFCellStyle getHeaderCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle hcs = workbook.createCellStyle();

		XSSFFont fontColor = workbook.createFont();
		fontColor.setColor(new XSSFColor(Color.white));
		hcs.setFont(fontColor);
		hcs.setFillForegroundColor(new XSSFColor(Color.GRAY));
		hcs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		hcs.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		hcs.setWrapText(true);

		return hcs;
	}

	public XSSFSheet getHeaderCellStyleSetting(XSSFSheet worksheet, XSSFCellStyle hcs) {
		int rowFirst = worksheet.getFirstRowNum();
		int rowLast = worksheet.getLastRowNum();
		for (int i = rowFirst; i < rowLast + 1; i++) {
			int cellFirst = worksheet.getRow(i).getFirstCellNum();
			int cellLast = worksheet.getRow(i).getLastCellNum();
			for (int j = cellFirst; j < cellLast + 1; j++) {
				if (worksheet.getRow(i).getCell(j) != null) {
					worksheet.getRow(i).getCell(j).setCellStyle(hcs);
				}
			}
			worksheet.getRow(i);
		}

		return worksheet;
	}
}