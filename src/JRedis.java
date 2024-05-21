import java.util.ArrayList;

import resp.RespSerializer;

public class JRedis {
    public static void main(String[] args) {
        try {
            var options = parseCliArgs(args);
            var server = new JRedisServer(options);
            server.start();
        } catch (IllegalArgumentException e) {
            System.out.println("Error while parsing arguments, " + e.getMessage());
        }
    }

    private static CliOptions parseCliArgs(String[] args) throws IllegalArgumentException {
        var options = new CliOptions();
        for (int i = 0; i < args.length; i++) {
            var arg = args[i];
            switch (arg) {
                case "--port":
                    if (i == args.length - 1) {
                        throw new IllegalArgumentException("Expected to specify port");
                    }

                    try {
                        options.setPort(Integer.parseInt(args[i + 1]));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(String.format("'%s' is not a valid integer", args[i + 1]));
                    }
                    i++;
                    break;
                case "--user":
                    if (i == args.length - 1) {
                        throw new IllegalArgumentException("Expected to specify user");
                    }
                    options.setUser(args[i + 1]);
                    i++;
                    break;
                case "--password":
                    if (i == args.length - 1) {
                        throw new IllegalArgumentException("Expected to specify password");
                    }
                    options.setPassword(args[i + 1]);
                    i++;
                    break;
                case "--noauth":
                    options.setAuthEnabled(false);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown parameter: '%s'", arg));
            }
        }

        return options;
    }
}
