package main.core;

public record Guest (
        String name,
        String middleName,
        String lastName,
        Passport passport
)
{
    @Override
    public String toString() {
        return "Гость:" +
                "\nФИО: " + name + " " + middleName + " " + lastName +
                "\n" + passport;
                //"Надежность: " + (isReliable ? "надежный" : "ненадежный");
    }
}
