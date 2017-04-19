package com.consult.action;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.consult.service.CommonService;
import com.consult.util.JsonUtil;

@Controller
@RequestMapping("/common")
public class CommonController {
	Logger logger = Logger.getLogger(CommonController.class);

	@Qualifier("commonServiceImpl")
	@Autowired
	CommonService service;
	
	@RequestMapping(value = "/checkIsNew", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String checkIsNew(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param)
			throws UnsupportedEncodingException {
		logger.info("methodName : checkIsNew");
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}
			
			if(JsonUtil.isBlank(JSONObject.fromObject(param).get("psptId"))) {
				return returnJo("9999", "psptId不能为空！").toString();
			}

			logger.info(param);

			return service.checkIsNew(param);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/checkIdCard", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String checkIdCard(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param)
			throws UnsupportedEncodingException {
		logger.info("methodName : checkIdCard");
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			logger.info(param);

			return service.checkIdCard(param);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/configArea", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String configArea(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : configArea");
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			logger.info(param);

			// JSONObject paramJo = JSONObject.fromObject(param);
			//
			// if (JsonUtil.isBlank(paramJo.get("areaCode"))
			// || JsonUtil.isBlank(paramJo.get("areaName"))) {
			// return returnJo("9999", "参数areaCode或者参数areaName不能为空！")
			// .toString();
			// }

			return service.saveArea(param);

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/deleteArea", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String deleteArea(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : deleteArea");
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			logger.info(param);

			// JSONObject paramJo = JSONObject.fromObject(param);
			//
			// if (JsonUtil.isBlank(paramJo.get("areaCode"))
			// || JsonUtil.isBlank(paramJo.get("areaName"))) {
			// return returnJo("9999", "参数areaCode或者参数areaName不能为空！")
			// .toString();
			// }

			return service.deleteArea(param);

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/updateArea", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String updateArea(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : updateArea");
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			logger.info(param);

			// JSONObject paramJo = JSONObject.fromObject(param);
			//
			// if (JsonUtil.isBlank(paramJo.get("areaCode"))
			// || JsonUtil.isBlank(paramJo.get("areaName"))) {
			// return returnJo("9999", "参数areaCode或者参数areaName不能为空！")
			// .toString();
			// }

			return service.updateArea(param);

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/qryArea", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String qryArea(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : qryArea");
		try {
			// if (JsonUtil.isBlank(param)) {
			// return returnJo("9999", "参数不能为空！").toString();
			// }
			//
			// if (!JsonUtil.isJson(param)) {
			// return returnJo("9999", "参数不是JSON格式！").toString();
			// }

			return service.qryArea(param);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/saveRecord", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8", consumes = "application/json")
	public @ResponseBody String saveRecord(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : saveRecord");
		logger.info(response.getCharacterEncoding());
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			logger.info(param);

			return service.saveRecord(param);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/queryRecords", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String queryRecords(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : queryRecords");
		// logger.info(request.getScheme() + "://"
		// + request.getServerName() + ":"
		// + request.getServerPort()
		// + request.getContextPath() + "/"
		// + "aa");

		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			return service.queryRecords(param);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/createWord", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String createWord(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : createWord");
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			logger.info(param);

			JSONObject paramJo = JSONObject.fromObject(param);

			// if(!paramJo.containsKey("psptId")) {
			// return returnJo("9999", "psptId参数不存在！").toString();
			// }
			// if(JsonUtil.isBlank(paramJo.get("psptId"))) {
			// return returnJo("9999", "psptId参数不能为空！").toString();
			// }

			if (!paramJo.containsKey("wordType")) {
				return returnJo("9999", "wordType参数不存在！").toString();
			}
			if (JsonUtil.isBlank(paramJo.get("wordType"))) {
				return returnJo("9999", "wordType参数不能为空！").toString();
			}

			return service.createWord(param, request);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/doctorQueryUser", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String doctorQueryUser(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : doctorQueryUser");
		try {
			return service.doctorQueryUser(param);
		}catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/printAllYear", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String printAllYear(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : printAllYear");
		try {
			return service.printAllYear(param,request);
		}catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}
	
	@RequestMapping(value = "/doctorConfirm", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String doctorConfirm(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : doctorConfirm");
		try {
			return service.doctorConfirm(param);
		}catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}
		

	@RequestMapping(value = "/createLineUp", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json;charset=utf-8")
	public @ResponseBody String createLineUp(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		logger.info("methodName : createLineUp");
		try {
			if (JsonUtil.isBlank(param)) {
				return returnJo("9999", "参数不能为空！").toString();
			}

			if (!JsonUtil.isJson(param)) {
				return returnJo("9999", "参数不是JSON格式！").toString();
			}

			if (JsonUtil.isBlank(JSONObject.fromObject(param).get("psptId"))) {
				return returnJo("9999", "psptId不能为空！").toString();
			}

			logger.info(param);
			return service.createLineUp(param);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return returnJo("9999", e.getMessage()).toString();
		}
	}

	@RequestMapping(value = "/vvbbbss")
	public @ResponseBody String vvbbbss(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String param) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String newName = "image.jpg";
		try {
			URL url = new URL(
					"http://127.0.0.1:8080/consult/common/autopictrue");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			InputStream fStream = new BufferedInputStream(
					new FileInputStream(
							"D:\\static\\image\\2016-07-26\\doctorAutograph\\1469522695467.jpg"));
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;

			// /* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			logger.info(b);
		} catch (Exception e) {
		}
		return null;
	}
	
    @RequestMapping("/uploadcomponent")
    public String uploadTest(HttpServletRequest request,
            HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile imageObj = multipartRequest.getFile("imageObj");
        
        String ctxPath = request.getSession()
                .getServletContext()
                .getRealPath("/")
                + "upload/";
        File dirPath = new File(ctxPath);
        if (!dirPath.exists()) {
            dirPath.mkdir();
        }
        String filename = imageObj.getOriginalFilename();
        if (!"".equals(filename) && filename != null) {
            String imgtype = filename.substring(filename.lastIndexOf("."));// 得到扩展名
            // 在对象目录下创建一个以时间串为名的文件
            File uploadFile = new File(ctxPath + "wtphoto"
                    + System.currentTimeMillis() + imgtype);
            try {
                FileCopyUtils.copy(imageObj.getBytes(), uploadFile);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
            }// 上传文件，就生成文件
        }
        
        return null;
    }

	private JSONObject returnJo(String respCode, String respMsg) {
		JSONObject jo = new JSONObject();
		jo.put("respCode", respCode);
		jo.put("respMsg", respMsg);
		return jo;
	}
}
