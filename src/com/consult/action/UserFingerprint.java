package com.consult.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class UserFingerprint
 */
@WebServlet("/UserFingerprint")
public class UserFingerprint extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(UserFingerprint.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserFingerprint() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String temp_str = sdf.format(new Date());
		File uploadPath = new File("D:" + File.separator + "static");
		logger.info("uploadPath=====" + uploadPath);
		if (!uploadPath.exists() && !uploadPath.isDirectory()) {
			uploadPath.mkdir();
		}
		File imagePath = new File(uploadPath.getAbsoluteFile() + File.separator
				+ "image");
		if (!imagePath.exists() && !imagePath.isDirectory()) {
			// ����Ŀ¼
			imagePath.mkdir();
		}
		File datePath = new File(imagePath.getAbsolutePath() + File.separator
				+ temp_str);
		logger.info("datePath=====" + datePath);
		if (!datePath.exists() && !datePath.isDirectory()) {
			datePath.mkdir();
		}
		File userPath = new File(datePath.getAbsolutePath() + File.separator
				+ "userFingerprint");
		logger.info("userPath=====" + userPath);
		if (!userPath.exists() && !userPath.isDirectory()) {
			userPath.mkdir();
		}
		File tempPath = new File(getServletContext().getRealPath("temp"));
		if (!tempPath.exists()) {
			tempPath.mkdir();
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(4096);
		// the location for saving data that is larger than getSizeThreshold()
		factory.setRepository(tempPath);

		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum size before a FileUploadException will be thrown
		upload.setSizeMax(1000000 * 20);

		PrintWriter out = response.getWriter();

		try {

			List fileItems = upload.parseRequest(request);

			String itemNo = "";
			for (Iterator iter = fileItems.iterator(); iter.hasNext();) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					if ("itemNo".equals(item.getFieldName())) {
						itemNo = item.getString();
					}
				}
				if (!item.isFormField()) {
					String fileName = item.getName();

					long size = item.getSize();
					if ((fileName == null || fileName.equals("")) && size == 0) {
						continue;
					}
					fileName = fileName.substring(
							fileName.lastIndexOf("\\") + 1, fileName.length());

					String imageName = new Date().getTime() + ".jpg";
					File file = new File(userPath, imageName);
					logger.info("图片路径========" + file.getAbsolutePath());
					item.write(file);

					JSONObject reJo = new JSONObject();
					reJo.put("respCode", "0000");
					reJo.put("respMsg", "image upload succsess");
					reJo.put(
							"picAddr",
							request.getScheme() + "://"
									+ request.getServerName() + ":"
									+ request.getServerPort()
									+ request.getContextPath() + "/image/"
									+ temp_str + "/userFingerprint/" + imageName);
					out.print(reJo);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			JSONObject reJo = new JSONObject();
			reJo.put("respCode", "9999");
			reJo.put("respMsg", "image upload fail:" + e.getMessage());
			out.print(reJo);
		}

	}

}
