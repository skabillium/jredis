package commands;

import java.util.ArrayList;

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
            case "get":
                if (split.length != 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'get' command");
                }
                return new Command.GetCommand(split[1]);
            case "set":
                if (split.length != 3) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'set' command");
                }
                return new Command.SetCommand(split[1], split[2]);
            case "del":
                if (split.length < 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'del' command");
                }
                var keys = new String[split.length - 1];
                for (int i = 1; i < split.length; i++) {
                    keys[i - 1] = split[i];
                }
                return new Command.DeleteCommand(keys);
            default:
                throw new IllegalArgumentException(String.format("Unknown command: '%s'", cmd));
        }
    }

    private static String[] splitTokens(String source) {
        return source.split(" ");
    }
}
