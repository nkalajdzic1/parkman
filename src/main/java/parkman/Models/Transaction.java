package parkman.Models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private int id;
    private byte[] carPicture;
    private byte[] platePicture;
    private String plateNumber;
    private Timestamp entranceTimestamp;
    private Timestamp exitTimestamp;
    private float pricePerHour;
    private String parkingSpot;
    private float totalPrice;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public Transaction(int id, byte[] carPicture, byte[] platePicture, String plateNumber, Timestamp entranceTimestamp, Timestamp exitTimestamp, float pricePerHour, String parkingSpot) {
        this.id = id;
        this.carPicture = carPicture;
        this.platePicture = platePicture;
        this.plateNumber = plateNumber;
        this.entranceTimestamp = entranceTimestamp;
        this.exitTimestamp = exitTimestamp;
        this.parkingSpot = parkingSpot;
        this.pricePerHour = pricePerHour;
    }

    public Transaction(byte[] carPicture, Timestamp entranceTimestamp, float pricePerHour, String parkingSpot) {
        this.carPicture = carPicture;
        this.entranceTimestamp = entranceTimestamp;
        this.parkingSpot = parkingSpot;
        this.pricePerHour = pricePerHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getCarPicture() {
        return carPicture;
    }

    public void setCarPicture(byte[] carPicture) {
        this.carPicture = carPicture;
    }

    public byte[] getPlatePicture() {
        return platePicture;
    }

    public void setPlatePicture(byte[] platePicture) {
        this.platePicture = platePicture;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Timestamp getEntranceTimestamp() {
        return entranceTimestamp;
    }

    public float getRawEntranceTimestamp() {
        return Float.parseFloat(exitTimestamp.toString());
    }

    public String getFormattedEnteranceTimestamp() { return sdf.format(entranceTimestamp); }

    public void setEntranceTimestamp(Timestamp entranceTimestamp) {
        this.entranceTimestamp = entranceTimestamp;
    }

    public Timestamp getExitTimestamp() {
        return exitTimestamp;
    }

    public float getRawExitTimestamp() {
        return Float.parseFloat(exitTimestamp.toString());
    }

    public String getFormattedExitTimestamp() { return sdf.format(exitTimestamp); }

    public void setExitTimestamp(Timestamp exitTimestamp) {
        this.exitTimestamp = exitTimestamp;
    }

    public float getPricePerHour() { return pricePerHour; }

    public void setPricePerHour(float pricePerHour) { this.pricePerHour = pricePerHour; }

    public String getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(String parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

//    public void getTotalPrice() {
//        System.out.println(new Date(this.getRawExitTimestamp() - this.getRawEntranceTimestamp()));
//    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", carPicture=" + carPicture +
                ", platePicture=" + platePicture +
                ", plateNumber='" + plateNumber + '\'' +
                ", entranceTmestamp=" + entranceTimestamp +
                ", exitTimestamp=" + exitTimestamp +
                ", employeeName='" + parkingSpot + '\'' +
                '}';
    }
}
