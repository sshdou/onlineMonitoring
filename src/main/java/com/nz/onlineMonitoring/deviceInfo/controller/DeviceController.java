package com.nz.onlineMonitoring.deviceInfo.controller;

import com.nz.onlineMonitoring.deviceInfo.model.Device;
import com.nz.onlineMonitoring.deviceInfo.service.DeviceService;
import com.nz.onlineMonitoring.utils.AuthorityUtil;
import com.nz.onlineMonitoring.utils.JacksonData;
import com.nz.onlineMonitoring.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/device")
public class DeviceController {

	@Autowired
	private DeviceService deviceService;

	//设备信息管理查询
	@RequestMapping(value = "/queryDeviceInfo")
	public String queryDeviceInfo(Map<String, Object> map, Device device,
								  @RequestParam(required=false,defaultValue="1")Integer pages,
								  @RequestParam(required=false,name="city")String[] citys,
								  String dev_object,HttpServletRequest request) {

		try {
			AuthorityUtil.getInstance().assignPermissions(citys, request, device);
			//存储前端传来的数据
			map = PageBean.serverMap(map, device, pages);
			List<Device> list = deviceService.queryDeviceInfo(map,device);
			//返回给前端的数据
			map = PageBean.clientMap(map, pages, request);
		}catch (Exception e){
			map.put("message", e.getMessage());
		}
		return  "device/devInfoManage_list";
	}

	//设备信息查询（设备采集数据图片再次接口上写）
	@RequestMapping(value = "/queryDeviceInfoList")
	public String queryDeviceInfoList(Map<String, Object> map, Device device,
								  @RequestParam(required=false,defaultValue="1")Integer pages,
								  @RequestParam(required=false,name="city")String[] citys,
								  String dev_object,HttpServletRequest request) {
		if(dev_object != null && !dev_object.equals("")){
			device.setDev_code("dev"+dev_object);
		}
		try {
			AuthorityUtil.getInstance().assignPermissions(citys, request, device);
			//存储前端传来的数据
			map = PageBean.serverMap(map, device, pages);
			List<Device> list = deviceService.queryDeviceInfo(map,device);
			//返回给前端的数据
			map = PageBean.clientMap(map, pages, request);
		}catch (Exception e){
			map.put("message", e.getMessage());
		}
		return  "device/deviceInfo_list";
	}

	//查看更多信息
	@RequestMapping(value = "/deviceLoad")
	@ResponseBody
	public JacksonData deviceLoad(Integer id){
		JacksonData jacksonData = new JacksonData();
		try{
			Device device = deviceService.deviceLoad(id);
			jacksonData.success(device);
		}catch (Exception e){
			jacksonData.failure(e.getMessage());
		}
		return jacksonData;
	}

	//修改设备信息和配置设备信息共用次接口。
	@RequestMapping(value = "/updateById")
	@ResponseBody
	public JacksonData updateById(Device device){
		JacksonData jacksonData = new JacksonData();
		try {
			Integer res = deviceService.updateById(device);
			jacksonData.success(res);
		} catch (Exception e) {
			jacksonData.failure(e.getMessage());
		}
		return jacksonData;
	}

	//设备状态查看
	@RequestMapping(value = "/devcieStatusList")
//	@ResponseBody
	public String devcieStatusList(Device device){
//		JacksonData jacksonData = new JacksonData();
//		try {
//			Integer res = deviceService.updateById(device);
//			jacksonData.success(res);
//		} catch (Exception e) {
//			jacksonData.failure(e.getMessage());
//		}
		return "device/deviceStatus_list";
	}

	@RequestMapping(value = "/photo")
	@ResponseBody
	public JacksonData photo(){
		JacksonData jacksonData = new JacksonData();
		File file = new File("D:\\photo");
		List list = new ArrayList();
//		File[] fileList = file.listFiles();
//		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return null;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						System.out.println("文件夹:" + file2.getAbsolutePath());
//						traverseFolder2(file2.getAbsolutePath());
					} else {
						System.out.println("文件:" + file2.getAbsolutePath());
						list.add(file2.getName());
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
		jacksonData.success(list);
		return jacksonData;
	}

}
