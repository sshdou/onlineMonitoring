package com.nz.onlineMonitoring.realData.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nz.onlineMonitoring.data.service.DataService;
import com.nz.onlineMonitoring.realData.model.Real;
import com.nz.onlineMonitoring.realData.service.RealService;
import com.nz.onlineMonitoring.utils.PageBean;

@Controller
@RequestMapping("/realData")
public class RealController {
    
    @Autowired
    private RealService realService;
    @Autowired
    private DataService dataService;
    /**
     * 
     * 方法描述查询实时数据，根据监测站名称、监测站编码（见数据字典中行政区划代码，输入市、县代码则显示全市、全县的监测站列表）、
     * 设备类型、监测对象、设备状态
     * @param map
     * @param real
     * @return
     * @author ssh
     * @date 2018年6月3日 下午4:18:48
     */
    @RequestMapping("/listRealData")
    public String listReal(Map<String, Object> map,Real real,HttpServletRequest request,@RequestParam(required=false,defaultValue="1") int pages,@RequestParam(required=false,name="city")String[] citys){
        if (citys != null) {
            if (citys[1] != null && citys[1] != "") {
                real.setMs_code(citys[1]);
            }else if (citys[0] != null && citys[0] != "") {
                real.setMs_code(citys[0]);
            }else {
                real.setMs_code("37");
            }
        }
        map = PageBean.serverMap(map , real , pages);
        List<Real> listReal = realService.listReal(map);
        map = PageBean.clientMap(map ,pages,request);
        map.put("listReal", listReal);
        map.put("devStauts", dataService.listDevStauts());
        map.put("devType", dataService.listDevType());
        map.put("devType1", dataService.listDevType1());
        return "realData/listRealData";
    }
}