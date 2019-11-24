package bulletapp.services;

public final class SerializerFactory {

    private SerializerFactory(){}

    public static ISerializer getXmlSerializer() {
        return new XmlSerializer();
    }
}
