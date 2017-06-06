package com.example.jinghui.model;

import com.example.jinghui.R;

/**
 * Created by 赵欣 on 2017/2/28.
 */

public class AppSimpleData {
    private String[] townData = {"东北庄杂技园区", "濮东产业集聚区", "大庆办", "胜利办", "黄河办", "岳村镇", "孟轲乡", "建设办", "人民办", "濮东办", "长庆办", "任丘办", "中原办"};
    private String[] siteDta = {"东湖湾1号", "东湖湾2号", "苏北小区", "中央公园2号", "君悦兰庭", "中央公园1号", "后铁炉城中村改造工地", "荣域花果园工地", "检疫局建设工地", "龙湖澜岸1号", "龙湖澜岸2号"};
    private String[] dataSimple = {"日数据", "小时数据", "所有", "乡镇办", "建设工地"};
    private String[] allData = {"东北庄杂技园区", "濮东产业集聚区", "大庆办", "胜利办", "黄河办", "岳村镇", "孟轲乡", "建设办", "人民办", "濮东办", "长庆办", "任丘办", "中原办",
            "东湖湾1号", "东湖湾2号", "苏北小区", "中央公园2号", "君悦兰庭", "中央公园1号", "后铁炉城中村改造工地", "荣域花果园工地", "检疫局建设工地", "龙湖澜岸1号", "龙湖澜岸2号"};
    private int[] image = new int[]{R.mipmap.fengjing,
            R.mipmap.fengjing2, R.mipmap.fengjing3, R.mipmap.fengjing4};

    public AppSimpleData(String[] townData, String[] siteDta) {
        this.townData = townData;
        this.siteDta = siteDta;
    }

    public AppSimpleData() {
        super();
    }

    public String[] getTownData() {
        return townData;
    }

    public void setTownData(String[] townData) {
        this.townData = townData;
    }

    public String[] getSiteDta() {
        return siteDta;
    }

    public void setSiteDta(String[] siteDta) {
        this.siteDta = siteDta;
    }

    public String[] getDataSimple() {
        return dataSimple;
    }

    public void setDataSimple(String[] dataSimple) {
        this.dataSimple = dataSimple;
    }

    public AppSimpleData(String[] dataSimple) {

        this.dataSimple = dataSimple;
    }

    public int[] getImage() {
        return image;
    }

    public String[] getAllData() {
        return allData;
    }

    public void setAllData(String[] allData) {
        this.allData = allData;
    }
}
