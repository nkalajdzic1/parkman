package parkman.Models;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Transaction {
    private int id;
    private byte[] carPicture;
    private byte[] platePicture;
    private String plateNumber;
    private Timestamp entranceTimestamp;
    private Timestamp exitTimestamp;
    private String employeeName;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public Transaction(int id, byte[] carPicture, byte[] platePicture, String plateNumber, Timestamp entranceTimestamp, Timestamp exitTimestamp, String employeeName) {
        this.id = id;
        this.carPicture = carPicture;
        this.platePicture = platePicture;
        this.plateNumber = plateNumber;
        this.entranceTimestamp = entranceTimestamp;
        this.exitTimestamp = exitTimestamp;
        this.employeeName = employeeName;
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

    public String getFormattedEnteranceTimestamp() { return sdf.format(entranceTimestamp); }

    public void setEntranceTimestamp(Timestamp entranceTimestamp) {
        this.entranceTimestamp = entranceTimestamp;
    }

    public Timestamp getExitTimestamp() {
        return exitTimestamp;
    }

    public String getFormattedExitTimestamp() { return sdf.format(exitTimestamp); }

    public void setExitTimestamp(Timestamp exitTimestamp) {
        this.exitTimestamp = exitTimestamp;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", carPicture=" + carPicture +
                ", platePicture=" + platePicture +
                ", plateNumber='" + plateNumber + '\'' +
                ", entranceTmestamp=" + entranceTimestamp +
                ", exitTimestamp=" + exitTimestamp +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
