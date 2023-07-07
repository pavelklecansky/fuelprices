package cz.klecansky.gasprices;

public enum Country {
    CZ("Česká republika"), AT("Rakousko"), SI("Slovinsko"), HR("Chorvatsko"), SK("Slovensko"), HU("Maďarsko"), CH("Švýcarsko"), PL("Polsko"), DE("Německo"), IT("Itálie"), FR("Francie"), ES("Španělsko"), GR(
            "Řecko");

    private final String name;

    Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
