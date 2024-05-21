import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import commands.*;
import database.Database;
import errors.*;

public class JRedisServer {
    private CliOptions config;

    public JRedisServer(CliOptions config) {
        this.config = config;
    }

    public void start() {
        try {
            // Create a server socket that listens on port 12345
            ServerSocket serverSocket = new ServerSocket(config.port);
            System.out.printf("JRedis server started at port %d \n", this.config.port);

            // Accept client connections
            Socket clientSocket = serverSocket.accept();

            var inputStream = clientSocket.getInputStream();
            var context = new ConnectionContext(inputStream, clientSocket.getOutputStream());
            var db = new Database();

            // Read data from the client
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                try {
                    var payload = new String(buffer, 0, bytesRead);
                    payload = payload.trim();
                    if (payload.isEmpty()) {
                        continue;
                    }

                    var command = CommandParser.parseCommand(payload);
                    switch (command) {
                        case Command.InfoCommand info ->
                            context.write("JRedis server version 0.0.0");
                        case Command.PingCommand ping -> context.simple("PONG");
                        case Command.KeysCommand keys -> context.write(db.listKeys(keys.pattern));
                        case Command.SetCommand set -> {
                            db.set(set.key, set.value, set.expires);
                            context.ok();
                        }
                        case Command.GetCommand get -> context.write(db.get(get.key));
                        case Command.DeleteCommand del -> context.write(db.delete(del.keys));
                        case Command.LLenCommand llen -> context.write(db.listLength(llen.key));
                        case Command.LPushCommand lpush -> context.write(db.listLPush(lpush.key, lpush.values));
                        case Command.RPopCommand rpop -> context.write(db.listRPop(rpop.key));
                        case Command.LPopCommand lpop -> context.write(db.listLPop(lpop.key));
                        default -> throw new Exception("Unreachable");
                    }
                } catch (NotFoundException e) {
                    context.nil();
                } catch (Exception e) {
                    context.write(e);
                }
            }

            // Close the client socket
            clientSocket.close();
            // Close the server socket
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
