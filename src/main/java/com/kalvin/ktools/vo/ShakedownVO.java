package com.kalvin.ktools.vo;

/**
 * 12306抢票参数实体
 */
public class ShakedownVO {

    private String username;
    private String password;
    private String trainDate;
    private String fromStation;
    private String toStation;
    private String trainNum;
    private String seatType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getTrainNum() {
        return trainNum;
    }

    public void setTrainNum(String trainNum) {
        this.trainNum = trainNum;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    @Override
    public String toString() {
        return "ShakedownVO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", trainDate='" + trainDate + '\'' +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", trainNum='" + trainNum + '\'' +
                ", seatType='" + seatType + '\'' +
                '}';
    }
}
