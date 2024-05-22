package resp;

public class RespSimpleString {
    private String string;

    public RespSimpleString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
