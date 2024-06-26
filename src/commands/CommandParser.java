package commands;

import errors.InvalidNumArgsException;
import errors.UnbalancedQuotesException;

public class CommandParser {
    public static Command parseCommand(String source) throws InvalidNumArgsException, UnbalancedQuotesException {
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
                if (argc > 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                var keysCmd = new Command.KeysCommand("*");
                if (split.length == 2) {
                    keysCmd.pattern = split[1];
                }
                return keysCmd;
            case "get":
                if (argc != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.GetCommand(split[1]);
            case "set":
                if (argc < 3) {
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
            case "del": {
                if (argc < 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                var keys = new String[split.length - 1];
                for (int i = 1; i < split.length; i++) {
                    keys[i - 1] = split[i];
                }
                return new Command.DeleteCommand(keys);
            }
            case "llen":
                if (argc != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.LLenCommand(split[1]);
            case "lpush": {
                if (argc < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                var values = new String[split.length - 2];
                for (int i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.LPushCommand(split[1], values);
            }
            case "rpush": {
                if (argc < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                var values = new String[split.length - 2];
                for (int i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.RPushCommand(split[1], values);
            }
            case "lpop":
                if (argc != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.LPopCommand(split[1]);
            case "rpop":
                if (argc != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.RPopCommand(split[1]);
            case "sadd": {
                if (argc < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                var values = new String[split.length - 2];
                for (var i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.SAddCommand(split[1], values);
            }
            case "srem": {
                if (argc < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                var values = new String[split.length - 2];
                for (var i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.SRemCommand(split[1], values);
            }
            case "sismember":
                if (argc < 3) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.SIsMemberCommand(split[1], split[2]);
            case "scard":
                if (argc != 2) {
                    throw new InvalidNumArgsException(cmd);
                }
                return new Command.SCardCommand(split[1]);
            case "sinter": {
                if (argc == 1) {
                    throw new InvalidNumArgsException(cmd);
                }
                var keys = new String[split.length - 1];
                for (var i = 0; i < keys.length; i++) {
                    keys[i] = split[i + 1];
                }
                return new Command.SInterCommand(keys);
            }
            default:
                throw new IllegalArgumentException(String.format("Unknown command: '%s'", cmd));
        }
    }

    private static String[] splitTokens(String source) {
        return source.split(" ");
    }
}
