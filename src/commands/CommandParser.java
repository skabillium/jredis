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
            case "keys":
                if (split.length > 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'keys' command");
                }
                var keysCmd = new Command.KeysCommand("*");
                if (split.length == 2) {
                    keysCmd.pattern = split[1];
                }
                return keysCmd;
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
            case "llen":
                if (split.length != 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'llen' command");
                }
                return new Command.LLenCommand(split[1]);
            case "lpush": {
                if (split.length < 3) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'llen' command");
                }
                var values = new String[split.length - 2];
                for (int i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.LPushCommand(split[1], values);
            }
            case "rpush": {
                if (split.length < 3) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'llen' command");
                }
                var values = new String[split.length - 2];
                for (int i = 2; i < split.length; i++) {
                    values[i - 2] = split[i];
                }
                return new Command.RPushCommand(split[1], values);
            }
            case "lpop":
                if (split.length != 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'lpop' command");
                }
                return new Command.LPopCommand(split[1]);
            case "rpop":
                if (split.length != 2) {
                    throw new IllegalArgumentException("Invalid number of arguments for 'rpop' command");
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
