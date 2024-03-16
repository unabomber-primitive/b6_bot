package main.core;

public record DeparturePersonInfo(
        String date,
        Integer cardNumber,
        Integer roomNumber
) {
    @Override
    public String toString() {
        return "Номер пропуска: " + cardNumber + "\nКомната: " + roomNumber + "\nДата отъезда: " + date;
    }
}
