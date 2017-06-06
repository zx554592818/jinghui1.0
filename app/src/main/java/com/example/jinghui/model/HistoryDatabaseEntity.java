package com.example.jinghui.model;

import java.io.Serializable;

/**
 * Created by zhaoxin on 2017/3/24.
 */

public class HistoryDatabaseEntity implements Serializable {
    //用来存放后台返回的数据
    public String tm;            //日期时间
    public String Pm25ave;     // PM2.5平均值
    public String Pm10ave;            //PM10平均值
    public String Speedave;    //风速平均值
    public String Speedmax;    //风速最大值
    public String Speedmin;    //风速最小值
    public String Directave;    //风向平均值
    public String Temperave;    //温度平均值
    public String Tempermax;    //温度最大值
    public String Tempermin;    //温度最小值
    public String Pressave;    //气压平均值
    public String Pressmax;    //气压最大值
    public String Pressmin;    //气压最小值
    public String Shiduave;    //湿度平均值
    public String Shidumax;    //湿度最大值
    public String Shidumin;//湿度最小值
    public String Place;


    //带了地名的构造函数
    public HistoryDatabaseEntity(String place, String tm, String pm25ave, String pm10ave, String speedave, String directave, String pressave, String temperave, String shiduave) {
        Place = place;
        this.tm = tm;
        Directave = directave;
        //排除掉为空的状况
        if (pm25ave.equals("") || pm25ave == null) {
            Pm25ave = "0.0";
        } else {
            Pm25ave = pm25ave;
        }
        if (pm10ave.equals("") || pm10ave == null) {
            Pm10ave = "0.0";
        } else {
            Pm10ave = pm10ave;
        }
        if (speedave.equals("") || speedave == null) {
            Speedave = "0.0";
        } else {
            Speedave = speedave;
        }
        if (temperave.equals("") || temperave == null) {
            Temperave = "0.0";
        } else {
            Temperave = temperave;
        }
        if (pressave.equals("") || pressave == null) {
            Pressave = "0.0";
        } else {
            Pressave = pressave;
        }
        if (shiduave.equals("") || shiduave == null) {
            Shiduave = "0.0";
        } else {
            Shiduave = shiduave;
        }
    }

    //没有带地名的构造函数
    public HistoryDatabaseEntity(String tm, String pm25ave, String pm10ave, String speedave, String directave, String pressave, String temperave, String shiduave) {
        this.tm = tm;
        Directave = directave;
        //排除掉为空的状况
        if (pm25ave.equals("") || pm25ave == null) {
            Pm25ave = "0.0";
        } else {
            Pm25ave = pm25ave;
        }
        if (pm10ave.equals("") || pm10ave == null) {
            Pm10ave = "0.0";
        } else {
            Pm10ave = pm10ave;
        }
        if (speedave.equals("") || speedave == null) {
            Speedave = "0.0";
        } else {
            Speedave = speedave;
        }
        if (temperave.equals("") || temperave == null) {
            Temperave = "0.0";
        } else {
            Temperave = temperave;
        }
        if (pressave.equals("") || pressave == null) {
            Pressave = "0.0";
        } else {
            Pressave = pressave;
        }
        if (shiduave.equals("") || shiduave == null) {
            Shiduave = "0.0";
        } else {
            Shiduave = shiduave;
        }
    }

    //扬尘构造
    public HistoryDatabaseEntity(String tm, String pm25ave, String pm10ave) {
        this.tm = tm;
        Pm25ave = pm25ave;
        Pm10ave = pm10ave;
    }

    //风向构造函数
    public HistoryDatabaseEntity(String tm, String directave) {
        this.tm = tm;
        Directave = directave;
    }
    //

    public HistoryDatabaseEntity(String tm, String pm25ave, String pm10ave, String speedave, String speedmax, String speedmin, String directave, String temperave, String tempermax,
                                 String tempermin, String pressave, String pressmax, String pressmin, String shiduave, String shidumax, String shidumin) {
        this.tm = tm;
        Pm25ave = pm25ave;
        Pm10ave = pm10ave;
        Speedave = speedave;
        Speedmax = speedmax;
        Speedmin = speedmin;
        Directave = directave;
        Temperave = temperave;
        Tempermax = tempermax;
        Tempermin = tempermin;
        Pressave = pressave;
        Pressmax = pressmax;
        Pressmin = pressmin;
        Shiduave = shiduave;
        Shidumax = shidumax;
        Shidumin = shidumin;
    }


    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getPm25ave() {
        return Pm25ave;
    }

    public void setPm25ave(String pm25ave) {
        Pm25ave = pm25ave;
    }

    public String getPm10ave() {
        return Pm10ave;
    }

    public void setPm10ave(String pm10ave) {
        Pm10ave = pm10ave;
    }

    public String getSpeedave() {
        return Speedave;
    }

    public void setSpeedave(String speedave) {
        Speedave = speedave;
    }

    public String getSpeedmax() {
        return Speedmax;
    }

    public void setSpeedmax(String speedmax) {
        Speedmax = speedmax;
    }

    public String getSpeedmin() {
        return Speedmin;
    }

    public void setSpeedmin(String speedmin) {
        Speedmin = speedmin;
    }

    public String getDirectave() {
        return Directave;
    }

    public void setDirectave(String directave) {
        Directave = directave;
    }

    public String getTemperave() {
        return Temperave;
    }

    public void setTemperave(String temperave) {
        Temperave = temperave;
    }

    public String getTempermax() {
        return Tempermax;
    }

    public void setTempermax(String tempermax) {
        Tempermax = tempermax;
    }

    public String getTempermin() {
        return Tempermin;
    }

    public void setTempermin(String tempermin) {
        Tempermin = tempermin;
    }

    public String getPressave() {
        return Pressave;
    }

    public void setPressave(String pressave) {
        Pressave = pressave;
    }

    public String getPressmax() {
        return Pressmax;
    }

    public void setPressmax(String pressmax) {
        Pressmax = pressmax;
    }

    public String getPressmin() {
        return Pressmin;
    }

    public void setPressmin(String pressmin) {
        Pressmin = pressmin;
    }

    public String getShiduave() {
        return Shiduave;
    }

    public void setShiduave(String shiduave) {
        Shiduave = shiduave;
    }

    public String getShidumax() {
        return Shidumax;
    }

    public void setShidumax(String shidumax) {
        Shidumax = shidumax;
    }

    public String getShidumin() {
        return Shidumin;
    }

    public void setShidumin(String shidumin) {
        Shidumin = shidumin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryDatabaseEntity)) return false;

        HistoryDatabaseEntity that = (HistoryDatabaseEntity) o;

        if (tm != null ? !tm.equals(that.tm) : that.tm != null) return false;
        if (Pm25ave != null ? !Pm25ave.equals(that.Pm25ave) : that.Pm25ave != null) return false;
        if (Pm10ave != null ? !Pm10ave.equals(that.Pm10ave) : that.Pm10ave != null) return false;
        if (Speedave != null ? !Speedave.equals(that.Speedave) : that.Speedave != null)
            return false;
        if (Speedmax != null ? !Speedmax.equals(that.Speedmax) : that.Speedmax != null)
            return false;
        if (Speedmin != null ? !Speedmin.equals(that.Speedmin) : that.Speedmin != null)
            return false;
        if (Directave != null ? !Directave.equals(that.Directave) : that.Directave != null)
            return false;
        if (Temperave != null ? !Temperave.equals(that.Temperave) : that.Temperave != null)
            return false;
        if (Tempermax != null ? !Tempermax.equals(that.Tempermax) : that.Tempermax != null)
            return false;
        if (Tempermin != null ? !Tempermin.equals(that.Tempermin) : that.Tempermin != null)
            return false;
        if (Pressave != null ? !Pressave.equals(that.Pressave) : that.Pressave != null)
            return false;
        if (Pressmax != null ? !Pressmax.equals(that.Pressmax) : that.Pressmax != null)
            return false;
        if (Pressmin != null ? !Pressmin.equals(that.Pressmin) : that.Pressmin != null)
            return false;
        if (Shiduave != null ? !Shiduave.equals(that.Shiduave) : that.Shiduave != null)
            return false;
        if (Shidumax != null ? !Shidumax.equals(that.Shidumax) : that.Shidumax != null)
            return false;
        return Shidumin != null ? Shidumin.equals(that.Shidumin) : that.Shidumin == null;

    }

    @Override
    public int hashCode() {
        int result = tm != null ? tm.hashCode() : 0;
        result = 31 * result + (Pm25ave != null ? Pm25ave.hashCode() : 0);
        result = 31 * result + (Pm10ave != null ? Pm10ave.hashCode() : 0);
        result = 31 * result + (Speedave != null ? Speedave.hashCode() : 0);
        result = 31 * result + (Speedmax != null ? Speedmax.hashCode() : 0);
        result = 31 * result + (Speedmin != null ? Speedmin.hashCode() : 0);
        result = 31 * result + (Directave != null ? Directave.hashCode() : 0);
        result = 31 * result + (Temperave != null ? Temperave.hashCode() : 0);
        result = 31 * result + (Tempermax != null ? Tempermax.hashCode() : 0);
        result = 31 * result + (Tempermin != null ? Tempermin.hashCode() : 0);
        result = 31 * result + (Pressave != null ? Pressave.hashCode() : 0);
        result = 31 * result + (Pressmax != null ? Pressmax.hashCode() : 0);
        result = 31 * result + (Pressmin != null ? Pressmin.hashCode() : 0);
        result = 31 * result + (Shiduave != null ? Shiduave.hashCode() : 0);
        result = 31 * result + (Shidumax != null ? Shidumax.hashCode() : 0);
        result = 31 * result + (Shidumin != null ? Shidumin.hashCode() : 0);
        return result;
    }
}
