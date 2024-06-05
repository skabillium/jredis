package commands;

public class Command {
    public boolean isUpdate = false;

    public static class InfoCommand extends Command {
    }

    public static class PingCommand extends Command {
    }

    public static class KeysCommand extends Command {
        public String pattern;

        KeysCommand(String pattern) {
            this.pattern = pattern;
        }
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
        public int expires = 0;

        SetCommand(String key, String value) {
            this.key = key;
            this.value = value;
            this.isUpdate = true;
        }
    }

    public static class DeleteCommand extends Command {
        public String[] keys;

        DeleteCommand(String[] keys) {
            this.keys = keys;
            this.isUpdate = true;
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
            this.isUpdate = true;
        }
    }

    public static class RPushCommand extends Command {
        public String key;
        public String[] values;

        RPushCommand(String key, String[] values) {
            this.key = key;
            this.values = values;
            this.isUpdate = true;
        }
    }

    public static class LPopCommand extends Command {
        public String key;

        LPopCommand(String key) {
            this.key = key;
            this.isUpdate = true;
        }
    }

    public static class RPopCommand extends Command {
        public String key;

        RPopCommand(String key) {
            this.key = key;
            this.isUpdate = true;
        }
    }

    public static class SAddCommand extends Command {
        public String key;
        public String[] values;

        SAddCommand(String key, String[] values) {
            this.isUpdate = true;
            this.key = key;
            this.values = values;
        }
    }
}
