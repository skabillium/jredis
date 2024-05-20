package commands;

public class CommandParser {
    public static Command parseCommand(String source) throws IllegalArgumentException {
        var split = splitTokens(source);
        if (split.length == 0) {
            throw new IllegalArgumentException("Empty string");
        }

        var cmd = split[0].toLowerCase();
        switch (cmd) {
            case "info":
                return new Command.InfoCommand();
            case "ping":
                return new Command.PingCommand();
            default:
                throw new IllegalArgumentException(String.format("Unknown command: '%s'", cmd));
        }
    }

    private static String[] splitTokens(String source) {
        return source.split(" ");
    }
}
