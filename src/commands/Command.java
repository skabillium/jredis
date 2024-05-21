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

    public static class LLenCommand extends Command {
        public String key;

        LLenCommand(String key) {
            this.key = key;
        }
    }

    public static class LPushCommand extends Command {
        public String key;
        public String[] values;

        LPushCommand(String key, String[] values) {
            this.key = key;
            this.values = values;
        }
    }

    public static class RPushCommand extends Command {
        public String key;
        public String[] values;

        RPushCommand(String key, String[] values) {
            this.key = key;
            this.values = values;
        }
    }

    public static class LPopCommand extends Command {
        public String key;

        LPopCommand(String key) {
            this.key = key;
        }
    }

    public static class RPopCommand extends Command {
        public String key;

        RPopCommand(String key) {
            this.key = key;
        }
    }
}
