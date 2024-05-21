package commands;

public class Command {
    public static class InfoCommand extends Command {
    }

    public static class PingCommand extends Command {
    }

    public static class GetCommand extends Command {
        public String key;

        GetCommand(String key) {
            this.key = key;
        }

    }

    public static class SetCommand extends Command {
        public String key;
        public String value;

        SetCommand(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class DeleteCommand extends Command {
        public String[] keys;

        DeleteCommand(String[] keys) {
            this.keys = keys;
        }
    }
}
