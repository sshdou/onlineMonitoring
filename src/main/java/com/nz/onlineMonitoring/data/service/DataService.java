package com.nz.onlineMonitoring.data.service;

import java.util.List;

import com.nz.onlineMonitoring.data.model.Data;

public interface DataService {
    /**
     * 
     * 方法描述：查询山东省的所有市
     * @param data_value
     * @return
     * @author ssh
     * @date 2018年6月5日 上午9:24:18
     */
    List<Data> listCity();
    /**
     * 
     * 方法描述：根据市的id，查询市的区，会把市的长度取前四位，然后模糊查询
     * @param data_value
     * @return
     * @author ssh
     * @date 2018年6月5日 上午9:24:52
     */
    List<Data> listArea(int data_value);
}