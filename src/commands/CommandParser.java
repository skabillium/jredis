package commands;

import errors.InvalidNumArgsException;

public class CommandParser {
    public static Command parseCommand(String source) throws InvalidNumArgsException {
        var split = splitTokens(source);
        if (split.length == 0) {
            throw new IllegalArgumentException("Empty string");
        }

        var argc = split.length;
        var cmd = split[0].toLowerCase();
        switch (cmd) {
            case "info":
                return new Command.InfoCommand();
            case "ping":
                return new Command.PingCommand();
            case "keys":
                if (split.length > 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                var keysCmd = new Command.KeysCommand("*");
                if (split.length == 2) {
                    keysCmd.pattern = split[1];
                }
                return keysCmd;
            case "get":
                if (split.length != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.GetCommand(split[1]);
            case "set":
                if (split.length < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                var set = new Command.SetCommand(split[1], split[2]);
                if (argc > 3 && split[3].toLowerCase().equals("ex")) {
                    // Parse expiration option
                    if (argc != 5) {
                        throw new InvalidNumArgsException(cmd);
                    }
                    set.expires = Integer.parseInt(split[4]);
                }
                return set;
            case "del":
                if (split.length < 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                var keys = new String[split.length - 1];
                for (int i = 1; i < split.length; i++) {
                    keys[i - 1] = split[i];
                }
                return new Command.DeleteCommand(keys);
            case "llen":
                if (split.length != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.LLenCommand(split[1]);
            case "lpush": {
                if (split.length < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                var values = new String[split.length - 2];
                for (int i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.LPushCommand(split[1], values);
            }
            case "rpush": {
                if (split.length < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                var values = new String[split.length - 2];
                for (int i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.RPushCommand(split[1], values);
            }
            case "lpop":
                if (split.length != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.LPopCommand(split[1]);
            case "rpop":
                if (split.length != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.RPopCommand(split[1]);
            default:
                throw new IllegalArgumentException(String.format("Unknown command: '%s'", cmd));
        }
    }

    private static String[] splitTokens(String source) {
        return source.split(" ");
    }
}
