package org.example.ivoprojekt.api.mapper;

import org.example.ivoprojekt.api.request.SessionUser;
import org.example.ivoprojekt.api.response.*;
import org.example.ivoprojekt.domain.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DtoMapper {
    public static SessionUser toSessionUser(User user) {
        final SessionUser sessionUser = new SessionUser();
        sessionUser.setId(user.getId());
        sessionUser.setLogin(user.getLogin());
        sessionUser.setName(user.getName());
        sessionUser.setAdmin(user.getIsAdmin());
        sessionUser.setIsProtected(user.getIsProtected());
        sessionUser.setPartnerId(user.getPartnerId());
        return sessionUser;
    }

    public static DialUserResponse toDialUserResponse(User user) {
        return new DialUserResponse(user.getId(), user.getLogin(), user.getName(), user.getIsProtected(), user.getIsAdmin(),  user.getIsActive());
    }

    public static DialPartnerResponse toDialPartnerResponse(Partner partner) {
        String name = partner.getName() + ", " + partner.getTownship();
        String adress = partner.getStreet() + ", " + partner.getPostcode() + " " + partner.getTownship();
        return new DialPartnerResponse(partner.getId(), name, adress);
    }

    /*public static WeighingTableResponse toWeighingTableResponse(Weighing weighing, String material) {
        String type = weighing.getType() ? "Výdajka" : "Príjemka";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(weighing.getDateTime(), formatter);
        WeighingTableResponse wighingTableResponse = new WeighingTableResponse(
                weighing.getNumber(), type, dateTime, weighing.getPartnerNameSnapshot(), weighing.getVehicleIdentificationNumberSnapshot(),
                weighing.getIssuedNameSnapshot(), material
        )
    }*/

//    public static DialVehicleResponse toDialVehicleResponse(Vehicle vehicle) {
//        return new DialVehicleResponse(vehicle.getId(), vehicle.getIdentificationNumber(), vehicle.getDescription());
//    }
//
//    public static DialMaterialResponse toDialMaterialResponse(Material material) {
//        return new DialMaterialResponse(material.getId(), material.getName());
//    }
//
//    public static WeightTaraResponse toWeightTaraResponse(Vehicle vehicle) {
//        return new WeightTaraResponse(vehicle.getId(), vehicle.getIdentificationNumber(), vehicle.getTara());
//    }
}
