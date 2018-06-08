package com.nz.onlineMonitoring.stationInfo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nz.onlineMonitoring.data.mapper.DataMapper;
import com.nz.onlineMonitoring.data.model.Data;
import com.nz.onlineMonitoring.stationInfo.mapper.StationMapper;
import com.nz.onlineMonitoring.stationInfo.model.Station;
import com.nz.onlineMonitoring.stationInfo.service.StationService;
@Service
public class StationServiceImpl implements StationService{
 
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private DataMapper dataMapper;
    /**
     * t_station
     * 方法描述:查询全部数据
     * @param  
     * @return 
     * @throws 
     * @date 2018年6月2日 上午9:54:59
     * 
     **/
    @Override
    public List<Station> listStation(Map<String, Object> map) {
        Integer count = stationMapper.countStation(map);
        map.put("count", count);
        List<Station> listStation = stationMapper.listStation(map);
        List<Data> listData = dataMapper.listMsDev();
        Map<String, String> mapDev = new HashMap<>();
        //将data中的ms_code的value作为键，name作为值
        for (Data d : listData) {
            mapDev.put(String.valueOf(d.getData_value()), d.getData_name());
        }
        //循环listStation,将ms_code中的值，一一取出，然后通过值去mapdev中比较，拿到具体的名字，然后拼接到ms_dev_value中
        for (Station s : listStation) {
            if (s.getMs_dev() != null && s.getMs_dev() != "") {
                String[] temp = s.getMs_dev().split(",");
                for (int i = 0,n = temp.length; i < n; i++) {
                    temp[i] = mapDev.get(temp[i]);
                    //将设备编码解析成具体的name
                    StringBuffer sb = new StringBuffer();
                    sb.append(dataMapper.loadByDevType(Integer.parseInt(temp[i].substring(3, 4))).getData_name());
                    sb.append(dataMapper.loadByDevType1(Integer.parseInt(temp[i].substring(3, 6))).getData_name());
                    temp[i] = sb.toString();
                }
                s.setMs_dev_value(String.join(",", temp));
            }
        }
        return listStation;
        
    }
    /**
     * 
     * 方法描述：根据查询条件查询数据的条数
     * @return
     * @param
     * @throws
     * @author ssh
     * @date 2018年6月2日 上午9:57:22
     */
    @Override
    public Integer countStation(Map<String , Object> map) {
        return stationMapper.countStation(map);
    }
    /**
     * 
     * 方法描述：查询单条数据
     * @param id
     * @return
     * @author ssh
     * @date 2018年6月2日 下午6:56:43
     */
    @Override
    public Station load(Integer id) {
        Station station = stationMapper.load(id);
        String dev = station.getMs_dev();
        List<Data> listData = dataMapper.listMsDev();
        Map<String, String> mapDev = new HashMap<>();
        //将data中的ms_code的value作为键，name作为值
        for (Data d : listData) {
            mapDev.put(String.valueOf(d.getData_value()), d.getData_name());
        }
        //因为ms_dev中的数据是用，分开的多个数据，所以没法用mapper直接查询，如果ms_dev不等于空，那么循环其中的数据，把从字典表中拿到的name值，拼接成字符串，传到ms_dev_value,用，隔开
        if (dev != null && dev != "") {
            String[] temp = dev.split(",");
            for (int i = 0, n = temp.length; i < n; i++) {
                temp[i] = mapDev.get(temp[i]);
                StringBuffer sb = new StringBuffer();
                sb.append(dataMapper.loadByDevType(Integer.parseInt(temp[i].substring(3, 4))).getData_name());
                sb.append(dataMapper.loadByDevType1(Integer.parseInt(temp[i].substring(3, 6))).getData_name());
                temp[i] = sb.toString();
            }
            station.setMs_dev_value(String.join(",", temp));
        }
        //解析监测站编码
        String ms_code = station.getMs_code();
        String code01 = ms_code.substring(0, 6);
        String code02 = ms_code.substring(4, 6);
        
        String code2 = ms_code.substring(6, 8);
        if (code02.equals("00")) {
            String name1= dataMapper.loadCity(Integer.parseInt(code01)).getData_name();
            station.setMs_code(name1 + "第" + code2 +"个");
        }else {
            String code03 = ms_code.substring(0, 4);
            code03 += "00";
            String name2= dataMapper.loadCity(Integer.parseInt(code03)).getData_name();
            String name1= dataMapper.loadCity(Integer.parseInt(code01)).getData_name();
            station.setMs_code(name2+name1 + "第" + code2 +"个");
        }
        
        
        return station;
    }
    /**
     * 
     * 方法描述：根据id，删除一条监测站
     * @param id
     * @return
     * @author ssh
     * @date 2018年6月2日 下午8:07:08
     */
    @Override
    public String delete(Integer id) {
        int result = stationMapper.delete(id);
        if (result > 0) {
            return "删除成功";
        }
        return "删除失败";
    }
    /**
     * 根据id，修改监测站的信息
     * 方法描述：
     * @param station
     * @return
     * @author ssh
     * @date 2018年6月2日 下午8:50:50
     */
    @Override
    public String update(Station station) {
        int result = stationMapper.update(station);
        if (result > 0) {
            return "修改成功";
        }
        return "修改失败";
    }
    /**
     * 
     * 方法描述：修改编码时，判断是否有重复的编码
     * @param ms_code
     * @return
     * @author ssh
     * @date 2018年6月3日 上午10:17:15
     */
    @Override
    public Integer existMsCode(String ms_code) {
        return stationMapper.existMsCode(ms_code);
    }

}
