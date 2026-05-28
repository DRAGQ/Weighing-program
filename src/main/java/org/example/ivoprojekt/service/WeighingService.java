package org.example.ivoprojekt.service;

import javafx.scene.control.Alert;
import org.example.ivoprojekt.api.request.WeighingOverviewRequest;
import org.example.ivoprojekt.api.response.WeighingPrintResponse;
import org.example.ivoprojekt.api.response.WeighingTableOverviewResponse;
import org.example.ivoprojekt.api.response.WeighingTableResponse;
import org.example.ivoprojekt.api.response.WeighingUpdateResponse;
import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.domain.Weighing;
import org.example.ivoprojekt.repository.WeighingRepository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeighingService {
    WeighingRepository repository;

    public WeighingService(WeighingRepository repository) {
        this.repository = repository;
    }

    public List<WeighingTableResponse> getAllWeighingForTable(String chosenTimePeriodStart, String chosenTimePeriodEnd) {
        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter uiFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        if (chosenTimePeriodStart == null || chosenTimePeriodEnd == null) {
            throw new ValidationException("Začiatok alebo koniec časového obdobia je null");
        }

        List<WeighingTableResponse> weighing = repository.getAllWeighingForTable(chosenTimePeriodStart, chosenTimePeriodEnd);
        //zrejme nechcem vynimku ked nenajde nic len ukazem prazdny list
        /*if (weighing == null || weighing.isEmpty()) {
            throw new NotFoundException("Váženie pre toto časové obdobie nebolo nájdené");
        }*/

        weighing.forEach(wei -> {
            wei.setTara(formatDoubleValue(wei.getTara()));
            wei.setGross(formatDoubleValue(wei.getGross()));
            wei.setNett(formatDoubleValue(wei.getNett()));
            System.out.println("TARA: " + wei.getTara());
            //if (wei.getDateTime() != null) {
            LocalDateTime ldt = LocalDateTime.parse(wei.getDateTime(), dbFormatter);
            wei.setDateTime(ldt.format(uiFormatter));
            //}
        });

        return weighing;

    }

    public String formatTime(LocalTime localTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        //return LocalTime.parse(localTime.format(timeFormatter));
        return localTime.format(timeFormatter);
    }

    public BigDecimal formatDoubleValue(BigDecimal value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return new BigDecimal(df.format(value).replace(",", "."));
    }

    public WeighingUpdateResponse getWeighingByNumber(Integer number) {
        if (number == null) {
            throw new ValidationException("Číslo váženia je null");
        }
        WeighingUpdateResponse response = this.repository.getWeighingByNumber(number);
        response.setTara(formatDoubleValue(response.getTara()));
        response.setGross(formatDoubleValue(response.getGross()));
        response.setNett(formatDoubleValue(response.getNett()));

        System.out.println("RESPONSE: " + response);
        if (response == null) {
            throw new NotFoundException("Váženie pre číslo " + number + " nebolo nájdené");
        }
        return response;

    }

    public WeighingPrintResponse getWeighingPrintResponse(Integer number) {
        if (number == null) {
            throw new ValidationException("number je null");
        }
        WeighingPrintResponse response = this.repository.getWeighingPrintResponse(number);
        if (response == null) {
            throw new NotFoundException("Váženie s číslom " + number + " nebolo nájdené");
        }
        return response;
    }

    /*public List<WeighingTableOverviewResponse> getWeighingTableRows(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, Integer type, String partnerName,
                                                                    String vehicleName, String userName, String materialName) {
        //maybe if dates is null throw error
        List<WeighingTableOverviewResponse> weighing =  this.repository.getWeighingTableRows(dateStart.toString(), dateEnd.toString(), timeStart.toString(), timeEnd.toString(), type, partnerName,
                vehicleName, userName, materialName);

        if  (weighing == null) {
            throw new NotFoundException("Váženie nebolo nájdené");
        }

        return weighing;
    }*/

    public List<WeighingTableOverviewResponse> getWeighingTableRows(WeighingOverviewRequest request) {
        //maybe if dates is null throw error
        List<WeighingTableOverviewResponse> weighing =  this.repository.getWeighingTableRows(request);

        /*if  (weighing.isEmpty()) {
            throw new NotFoundException("Váženie nebolo nájdené");
        }*/

        return weighing;
    }

    public int generateWeighingNumber(LocalDate date) {
        if (date == null) {
            throw new ValidationException("date je null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = date.format(formatter);
        int numberOfRows = this.repository.generateWeighingNumber(formattedDate + "%");
        int sequence = numberOfRows + 1;
        return Integer.parseInt(formattedDate + String.format("%03d", sequence));
    }

    /*public void saveWeighing(int number, boolean type, LocalDate localDate, LocalTime localTimeEntry, LocalTime localTimeDeparture,
                             BigDecimal gross, BigDecimal tara, BigDecimal nett,
                             String description, int userId, int partnerId, int vehicleId, int materialId)
    {
        //If something is null throw exception, mozno to vadit nebude.
        //dam to do dto metody nech tu nemam tolko propertyes
        Weighing weighing = new Weighing(null, number, type, localDate.toString(), localTimeEntry.toString(), localTimeDeparture.toString(),
                    gross, tara, nett, description, userId, partnerId, vehicleId, materialId);

        this.repository.save(weighing);
    }*/

    public void saveWeighing(Weighing weighing)
    {
        double tara;
        double gross;

        if (!checkInputs(weighing)) {
            throw new ValidationException("Všetky inputy musia byť vyplnené!");
        }

        try {
            tara = weighing.getNett().doubleValue();
            gross = weighing.getTara().doubleValue();
            double net =  weighing.getNett().doubleValue();
        } catch (ValidationException e) {
            throw new ValidationException("Hmotnosť brutto, netto a tara musia byť čísla!");
        }

        if (tara < 0) {
            throw new ValidationException("Tara musí byť väčšia ako 0!");
        }
        if (gross < tara) {
            throw new ValidationException("Hmotnosť brutto musí byť väčšia ako tara!");
        }

        this.repository.save(weighing);
    }

    private boolean checkInputs(Weighing weighing) {
        return weighing.getLocalDate() != null && weighing.getGross() != null && weighing.getTara() != null && weighing.getNett() != null && weighing.getLocalTimeEntry() != null;
    }

    public void updateWeighing(Weighing weighing)
    {
        //issued name som dal null aby sa nezmenilo
        //If something is null throw exception, mozno to vadit nebude.
        //dam to do dto metody nech tu nemam tolko propertyes
        //Weighing weighing = new Weighing(null, number, type, localDate.toString(), localTimeEntry.toString(), localTimeDeparture.toString(),
        //        gross, tara, nett, description, userId, partnerId, vehicleId, materialId);

        this.repository.update(weighing);
    }

    public void deleteById(Integer number) {
        if (number == null) {
            throw new ValidationException("Id je null");
        }
        this.repository.delete(number);
    }

}
