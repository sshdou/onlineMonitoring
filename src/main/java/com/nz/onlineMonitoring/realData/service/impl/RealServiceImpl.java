package com.nz.onlineMonitoring.realData.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nz.onlineMonitoring.realData.mapper.RealMapper;
import com.nz.onlineMonitoring.realData.model.Real;
import com.nz.onlineMonitoring.realData.service.RealService;
@Service
public class RealServiceImpl implements RealService {
    
    @Autowired
    private RealMapper RealMapper;
    /**
     * 
     * 方法描述：查询实时数据，根据监测站名称、监测站编码（见数据字典中行政区划代码，输入市、县代码则显示全市、全县的监测站列表）、
     * 设备类型、监测对象、设备状态
     * @param map
     * @return
     * @author ssh
     * @date 2018年6月3日 下午2:36:02
     */
    @Override
    public List<Real> listReal(Map<String, Object> map) {
        Integer count = RealMapper.countReal(map);
        map.put("count", count);
        List<Real> realList = RealMapper.listReal(map);
        return realList;
    }

}
