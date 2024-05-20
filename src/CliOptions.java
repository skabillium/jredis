public class CliOptions {
    public int port = 5678;
    public boolean authEnabled = true;
    public String user = "jredis";
    public String password = "password";

    public CliOptions() {
    }

    public CliOptions setPort(int port) {
        this.port = port;
        return this;
    }

    public CliOptions setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
        return this;
    }

    public CliOptions setUser(String user) {
        this.user = user;
        return this;
    }

    public CliOptions setPassword(String password) {
        this.password = password;
        return this;
    }
}