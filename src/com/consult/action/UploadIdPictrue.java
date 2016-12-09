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
 * Servlet implementation class UploadIdPictrue
 */
@WebServlet("/UploadIdPictrue")
public class UploadIdPictrue extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(UploadIdPictrue.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadIdPictrue() {
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

		// ��ϵͳ������ʱ�򣬾Ϳ�ʼ��ʼ�����ڳ�ʼ��ʱ������ϴ�ͼƬ���ļ��кʹ����ʱ�ļ����ļ����Ƿ���ڣ������ڣ��ʹ���
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		// ��ȡ��Ŀ¼��Ӧ����ʵ����·��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String temp_str = sdf.format(new Date());
		File uploadPath = new File("D:" + File.separator + "static");
		logger.info("uploadPath=====" + uploadPath);
		// ���Ŀ¼������
		if (!uploadPath.exists() && !uploadPath.isDirectory()) {
			// ����Ŀ¼
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
			// ����Ŀ¼
			datePath.mkdir();
		}
		File userPath = new File(datePath.getAbsolutePath() + File.separator
				+ "idPic");
		logger.info("userPath=====" + userPath);
		if (!userPath.exists() && !userPath.isDirectory()) {
			// ����Ŀ¼
			userPath.mkdir();
		}
		// ��ʱĿ¼
		// File tempFile = new File(item.getName())������ʱ����
		File tempPath = new File(getServletContext().getRealPath("temp"));
		if (!tempPath.exists()) {
			tempPath.mkdir();
		}

		// ��item_upload.jsp����ȡ��ݣ���Ϊ�ϴ�ҳ�ı����ʽ��һ��Ĳ�ͬ��ʹ�õ���enctype="multipart/form-data"
		// form�ύ����multipart/form-data,�޷�����req.getParameter()ȡ�����
		// String itemNo = req.getParameter("itemNo");
		// System.out.println("itemNo======" + itemNo);

		/******************************** ʹ�� FileUpload ��������? ********************/

		// DiskFileItemFactory������ FileItem
		// ����Ĺ�����������������п��������ڴ滺�����С�ʹ����ʱ�ļ���Ŀ¼��
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(4096);
		// the location for saving data that is larger than getSizeThreshold()
		factory.setRepository(tempPath);

		// ServletFileUpload���������ϴ����ļ���ݣ�����ÿ���ֵ���ݷ�װ��һ�� FileItem
		// �����С�
		// �ڽ����ϴ��ļ����ʱ���Ὣ���ݱ��浽�ڴ滺�����У�����ļ����ݳ�����
		// DiskFileItemFactory ָ���Ļ�����Ĵ�С��
		// ��ô�ļ��������浽�����ϣ��洢Ϊ DiskFileItemFactory ָ��Ŀ¼�е���ʱ�ļ���
		// ���ļ���ݶ�������Ϻ�ServletUpload�ٴ��ļ��н����д�뵽�ϴ��ļ�Ŀ¼�µ��ļ���

		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum size before a FileUploadException will be thrown
		upload.setSizeMax(1000000 * 20);

		PrintWriter out = response.getWriter();

		/******************************* �����?���ݹ�������ݣ�����List�������-����:FileItem ***********/

		try {

			List fileItems = upload.parseRequest(request);

			String itemNo = "";
			// Iterator iter = fileItems.iterator()ȡ������
			// iter.hasNext()����������Ƿ���Ԫ��
			for (Iterator iter = fileItems.iterator(); iter.hasNext();) {
				// ��������е���һ��Ԫ��
				FileItem item = (FileItem) iter.next();

				// �ж����ļ������ı���Ϣ
				// ����ͨ�ı?������
				if (item.isFormField()) {
					if ("itemNo".equals(item.getFieldName())) {
						itemNo = item.getString();
					}
				}
				// �Ƿ�Ϊinput="type"������
				if (!item.isFormField()) {
					// �ϴ��ļ�����ƺ�����·��
					String fileName = item.getName();

					long size = item.getSize();
					// �ж��Ƿ�ѡ�����ļ�
					if ((fileName == null || fileName.equals("")) && size == 0) {
						continue;
					}
					// ��ȡ�ַ� �磺C:\WINDOWS\Debug\PASSWD.LOG
					fileName = fileName.substring(
							fileName.lastIndexOf("\\") + 1, fileName.length());

					// �����ļ��ڷ���������������У���һ�������ǣ�����·�����������ļ���ڶ��������ǣ��ļ����
					// item.write(file);
					// �޸��ļ����������һ�£���ǿ���޸����ļ���չ��Ϊgif
					// item.write(new File(uploadPath, itemNo + ".gif"));
					// ���ļ����浽Ŀ¼�£����޸��ļ���

					String imageName = new Date().getTime() + ".jpg";
					File file = new File(userPath, imageName);
					logger.info("路径========" + file.getAbsolutePath());
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
									+ temp_str + "/idPic/" + imageName);
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
