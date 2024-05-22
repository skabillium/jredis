public class CliOptions {
    public int port = 5678;
    public boolean authEnabled = true;
    public String user = "jredis";
    public String password = "password";
    public boolean walEnabled = false;
    public String wal = "wal.log";

    public void setPort(int port) {
        this.port = port;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWalEnabled(boolean walEnabled) {
        this.walEnabled = walEnabled;
    }

    public void setWal(String wal) {
        this.wal = wal;
    }
}