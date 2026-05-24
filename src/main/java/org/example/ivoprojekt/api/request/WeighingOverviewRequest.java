package org.example.ivoprojekt.api.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class WeighingOverviewRequest {
    String dateStart;
    String dateEnd;
    String timeStart;
    String timeEnd;
    Integer type;
    String partnerName, vehicleName, userName, materialName;

    public WeighingOverviewRequest(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, Integer type, String partnerName, String vehicleName, String userName, String materialName) {
        this.dateStart = dateStart.toString();
        this.dateEnd = dateEnd.toString();
        this.timeStart = timeStart.toString();
        this.timeEnd = timeEnd.toString();
        this.type = type;
        this.partnerName = partnerName;
        this.vehicleName = vehicleName;
        this.userName = userName;
        this.materialName = materialName;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public Integer getType() {
        return type;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getUserName() {
        return userName;
    }

    public String getMaterialName() {
        return materialName;
    }
}
