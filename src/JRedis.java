public class JRedis {
    public static void main(String[] args) {
        var options = parseCliArgs(args);
        var server = new JRedisServer(options);
        server.start();
    }

    private static CliOptions parseCliArgs(String[] args) {
        System.out.printf("Arguments: %s \n", String.join(" ", args));
        return new CliOptions();
    }
}
